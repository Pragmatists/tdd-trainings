/******************************************************************************************
 * PokerMultiServerThread.java     PokerApp                                               *
 *                                                                                        *
 *   Revision History                                                                     *
 * +---------+----------+---------------------------------------------------------------+ *
 * | Version | DATE     | Description                                                   | *
 * +---------+----------+---------------------------------------------------------------+ *
 * |  0.95   | 09/15/04 | Initial documented release                                    | *
 * |  0.96   | 09/20/04 | Change this from extending the Thread class to making it a    | *
 * |         |          | Runnable class.  This is more correct.                        | *
 * |  0.97   | 10/25/04 | Use new decryption routine to receive data from clients       | *
 * |  0.98   | 12/06/04 | Check for null before sending message to the serverWindow     | *
 * |  1.00   | 06/06/05 | Added logging.                                                | *
 * |  1.00   | 10/25/05 | Add registration check to here when a new player joins.       | *
 * |  1.00   | 07/11/07 | Prepare for open source.  Header/comments/package/etc...      | *
 * +---------+----------+---------------------------------------------------------------+ *
 *                                                                                        *
 * PokerApp Copyright (C) 2004  Dan Puperi                                                *
 *                                                                                        *
 *   This program is free software: you can redistribute it and/or modify                 *
 *   it under the terms of the GNU General Public License as published by                 *
 *   the Free Software Foundation, either version 3 of the License, or                    *
 *   (at your option) any later version.                                                  *
 *                                                                                        *
 *   This program is distributed in the hope that it will be useful,                      *
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of                       *
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the                        *
 *   GNU General Public License for more details.                                         *
 *                                                                                        *
 *   You should have received a copy of the GNU General Public License                    *
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>                 *
 *                                                                                        *
 ******************************************************************************************/

package net.sourceforge.pokerapp;

import java.net.Socket;
import java.io.*;
import java.util.ArrayList;

/****************************************************
 * PokerMultiServerThread is the class that is used to server a single client.
 * Each client will have its own server thread listening and sending commands to
 * it.
 * 
 * @author Dan Puperi
 * @version 1.00
 * 
 **/
public class PokerMultiServerThread implements Runnable {

	private Socket socket; // The socket over which this thread communicates
	private PokerProtocol protocol; // The protocol which this thread is using
									// to communicate
	private String name; // The name of the person represented by this thread
	private PrintWriter out; // Data sent out by this thread
	private BufferedReader in; // Data received by this thread
	private StartPoker theStartApp; // The StartPoker class that created this
									// thread.
	private Thread pmst; // The actual server Thread
	private boolean working; // Whether this Thread is running or not

	/***********************
	 * The constructor creates the PokerMultiServerThread.
	 * 
	 * @param a
	 *            The StartPoker instance to which this thread belongs
	 * @param s
	 *            The socket on which this thread is connected
	 * 
	 **/
	public PokerMultiServerThread(StartPoker a, Socket s) {
		socket = s;
		theStartApp = a;
		theStartApp.log("Constructing PokerMultiServerThread", 3);
		pmst = null;
		protocol = new PokerProtocol(theStartApp);
		try {
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
		} catch (Exception e) {
			theStartApp
					.log("Warning : Caught exception while trying to create PrintWriter or BufferedReader in PokerMultiServerThread");
			theStartApp.logStackTrace(e);
		}

		theStartApp.getServerThreadList().add(this);
		name = null;
		working = true;
		pmst = new Thread(this, "PokerMultiServerThread");
		pmst.setPriority(7);
		pmst.start();
	}

	/***********************
	 * run() starts the thread. Called when the Thread.start() function is
	 * called.
	 **/
	public void run() {
		try {
			String input = in.readLine();
			String output;

			while ((input != null) && (working)) {
				input = PokerProtocol.getString(input);
				if (name == null) {
					if (input.startsWith("NEW NAME")) {
						theStartApp.log("PokerMultiServerThread got NEW NAME",
								3);
						int i = input.indexOf('&');
						String tname = input.substring(i + 1);
						String oldname = new String(tname);
						boolean nameOK = false;
						boolean nameChanged = false;
						while (!nameOK) {
							nameOK = true;
							for (int m = 0; m < theStartApp
									.getServerThreadList().size(); m++) {
								String currName = ((PokerMultiServerThread) theStartApp
										.getServerThreadList().get(m))
										.getPlayerName();
								if (tname.equals(currName)) {
									nameOK = false;
									char lastChar = currName.charAt(currName
											.length() - 1);
									if (Character.isDigit(lastChar)) {
										if (lastChar == '9') {
											String newName = tname.substring(0,
													tname.length() - 1);
											tname = new String(newName + "10");
										} else {
											String newName = tname.substring(0,
													tname.length() - 1);
											char chars[] = { lastChar };
											int next = Integer
													.parseInt(new String(chars)) + 1;
											tname = new String(newName + ""
													+ next);
										}
									} else {
										tname = new String(tname + "0");
									}
									nameChanged = true;
								}
							}
						}
						name = tname;
						if (nameChanged) {
							theStartApp.messageToPlayer(tname, "NAME CHANGED &"
									+ tname);
							theStartApp.log(
									"PokerMultiServerThread had to change name from "
											+ oldname + " to " + tname, 3);
						}
					}
					input = in.readLine();
				} else {
					output = protocol.processServerInput(name, input);
					if (!output.equals(new String("nothing"))) {
						if (theStartApp.getServerWindow() != null) {
							theStartApp.getServerWindow().sendMessage(output);
						} else {
							System.out.println(output);
						}
						theStartApp.log(output, 1);
					}
					input = in.readLine();
				}
			}
		} catch (Exception e) {
			theStartApp
					.log("Warning : caught exception in PokerMultiServerThread.run()");
			theStartApp.logStackTrace(e);
		}

		try {
			out.close();
			in.close();
			socket.close();
		} catch (IOException e) {
		}
	}

	/***********************
	 * stopRunning() is used to kill this Thread
	 **/
	public void stopRunning() {
		theStartApp.log("PokerMultiServerThread.stopRunning()", 3);
		working = false;
		pmst = null;
	}

	/***********************
	 * getPlayerName() is used to access the private name variable
	 * 
	 * @return The player name who is using this server thread
	 * 
	 **/
	public String getPlayerName() {
		return name;
	}

	/***********************
	 * getStreamOut() is used to access the private out variable
	 * 
	 * @return The output stream used by this server thread
	 * 
	 **/
	public PrintWriter getStreamOut() {
		return out;
	}

	/***********************
	 * getProtocol() is used to access the private protocol variable
	 * 
	 * @return The protocol used to interpret communications
	 * 
	 **/
	public PokerProtocol getProtocol() {
		return protocol;
	}
}