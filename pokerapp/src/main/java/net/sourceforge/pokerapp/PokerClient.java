/******************************************************************************************
 * PokerClient.java                PokerApp                                               *
 *                                                                                        *
 *   Revision History                                                                     *
 * +---------+----------+---------------------------------------------------------------+ *
 * | Version | DATE     | Description                                                   | *
 * +---------+----------+---------------------------------------------------------------+ *
 * |  0.95   | 09/15/04 | Initial documented release                                    | *
 * |  0.96   | 10/05/04 | Change this from extending the Thread Class to making it      | *
 * |         |          | implement the Runnable class.  This is more correct.          | *
 * |  0.97   | 10/25/04 | Use new encryption and decryption routines to make data more  | *
 * |         |          | secure.                                                       | *
 * |  0.98   | 12/10/04 | Remove disconnectSocket calls when done with thread - this is | *
 * |         |          | not needed since diconnectSocket is what kills the thread.    | *
 * |  1.00   | 06/06/05 | Added logging.                                                | *
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

import net.sourceforge.pokerapp.ai.*;
import java.io.*;

/****************************************************
 * PokerClient is the client class for all of the poker players. It is a
 * separate thread that handles all communication with the server.
 * 
 * @author Dan Puperi
 * @version 1.00
 * 
 **/
public class PokerClient implements Runnable {

	private PokerApp theApp; // The instance of PokerApp that might have started
								// this class
	private AIApp theAIApp; // The instance of AIApp that might have started
							// this class
	private BufferedReader in; // Input data coming from the server
	private PrintWriter out; // Output data to the server
	private PokerProtocol protocol; // Protocol used to communicate with the
									// server
	private Thread clientThread; // The actual client Thread
	private boolean working; // Whether this thread is running or not

	/***********************
	 * The contructor to create a human player client
	 * 
	 * @param a
	 *            The PokerApp instance to which this PokerClient belongs
	 * 
	 **/
	public PokerClient(PokerApp a) {
		theApp = a;
		theAIApp = null;
		theApp.log("Constructing a new PokerClient for a PokerApp class.", 3);
		protocol = new PokerProtocol(theApp);
		try {
			out = new PrintWriter(theApp.getSocket().getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(theApp.getSocket()
					.getInputStream()));
			String message = PokerProtocol.makeString(new String("NEW NAME  &"
					+ theApp.getThisPlayer().getName()));
			out.println(message);
			working = true;
			clientThread = new Thread(this, "PokerClient");
			clientThread.setPriority(8);
			clientThread.start();
		} catch (Exception e) {
			theApp.log("Warning : Caught exception while trying to create the PokerClient");
			theApp.logStackTrace(e);
		}
	}

	/***********************
	 * The contructor to create a AI player client
	 * 
	 * @param a
	 *            The AIApp instance to which this PokerClient belongs
	 * 
	 **/
	public PokerClient(AIApp a) {
		theAIApp = a;
		theApp = null;
		theAIApp.log("Constructing a new PokerClient for a AIApp class.", 3);
		protocol = new PokerProtocol(theAIApp);
		try {
			out = new PrintWriter(theAIApp.getSocket().getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(theAIApp.getSocket()
					.getInputStream()));
			String message = PokerProtocol.makeString(new String("NEW NAME  &"
					+ theAIApp.getThisPlayer().getName()));
			out.println(message);
			working = true;
			clientThread = new Thread(this, "PokerClient");
			clientThread.setPriority(8);
			clientThread.start();
		} catch (Exception e) {
			theAIApp.log("Warning : Caught exception while trying to create the PokerClient");
			theAIApp.logStackTrace(e);
		}
	}

	/***********************
	 * run() implements the Thread classes run() function. Is it called when the
	 * thread is started with Thread.start()
	 **/
	public void run() {
		try {
			String input = in.readLine();
			String output;

			while ((input != null) && (working)) {
				output = protocol.processClientInput(PokerProtocol
						.getString(input));
				if (theApp != null) {
					if (!output.equals("nothing"))
						theApp.getWindow().sendMessage(output);
				}
				if (theAIApp != null) {
					if (!output.equals("nothing"))
						theAIApp.getWindow().sendMessage(output);
				}
				input = in.readLine();
			}
		} catch (IOException e) {
		}

		try {
			out.close();
			in.close();
		} catch (IOException e) {
		}
	}

	/***********************
	 * stopRunning() is used to kill this thread
	 **/
	public void stopRunning() {
		if (theApp != null)
			theApp.log("PokerClient.stopRunning()", 3);
		if (theAIApp != null)
			theAIApp.log("PokerClient.stopRunning()", 3);
		working = false;
		clientThread = null;
	}

	/***********************
	 * sendMessage() sends a message from the client to the server over the
	 * output stream.
	 * 
	 * @param m
	 *            The String message to send.
	 * 
	 **/
	public void sendMessage(String m) {
		if (out != null) {
			out.println(PokerProtocol.makeString(m));
		}
	}
}