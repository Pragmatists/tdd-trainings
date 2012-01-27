/******************************************************************************************
 * PokerServerListener.java        PokerApp                                               *
 *                                                                                        *
 *   Revision History                                                                     *
 * +---------+----------+---------------------------------------------------------------+ *
 * | Version | DATE     | Description                                                   | *
 * +---------+----------+---------------------------------------------------------------+ *
 * |  0.95   | 09/15/04 | Initial documented release                                    | *
 * |  0.96   | 09/20/04 | Change this class from a Thread subclass to a class that      | *
 * |         |          | implements Runnable.  This is the more correct was to do it.  | *
 * |  0.96   | 09/20/04 | Fixed not being able to close the server listener because it  | *
 * |         |          | was busy listening for connections.  Used setSoTimeout.       | *
 * |  0.98   | 12/06/04 | Check for null pointer before using getMainWindow()           | *
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

import java.net.ServerSocket;

/****************************************************
 * PokerServerListener is a thread that listens for new connections for the
 * server
 * 
 * @author Dan Puperi
 * @version 1.00
 * 
 **/
public class PokerServerListener implements Runnable {

	private static int portNum; // The port number the server is listening on
	private boolean listening; // Whether the server is listening for
								// connections or not
	private StartPoker theStartApp; // The StartPoker class that is the server
									// and created this instance
	private ServerSocket serverSocket; // The socket the server is listening on
	private Thread serverThread; // The server listener thread.

	/***************************
	 * The constructor creates an a new PokerServerListener instance.
	 * 
	 * @param a
	 *            The StartPoker class which created this listener
	 * @param p
	 *            The port number on which to listen for connections
	 * 
	 **/
	public PokerServerListener(StartPoker a, int p) {
		portNum = p;
		theStartApp = a;
		theStartApp.log("Constructing PokerServerListener on port " + portNum,
				3);
		serverSocket = null;
		serverThread = null;
		startListening();
	}

	/***************************
	 * The run() function is called when the thread is started. When run is
	 * finished, this thread is destroyed.
	 **/
	public void run() {
		boolean notstarted = false;

		try {
			serverSocket = new ServerSocket(portNum);
			serverSocket.setSoTimeout(250);
			if (theStartApp.getMainWindow() != null) {
				theStartApp.getMainWindow().sendMessage(
						"Opened server socket on port " + portNum);
			} else {
				System.out.println("Opened server socket on port " + portNum);
			}
			theStartApp.log("Opened server socked on port " + portNum, 1);
		} catch (java.io.IOException e) {
			if (theStartApp.getMainWindow() != null) {
				theStartApp.getMainWindow().sendMessage(
						"Could not listen on port " + portNum);
			} else {
				System.out.println("Could not listen on port " + portNum);
			}
			theStartApp.log("Could not listen on port " + portNum, 1);
			stopListening();
			notstarted = true;
		}

		while (listening) {
			PokerMultiServerThread tempThread = null;
			try {
				tempThread = new PokerMultiServerThread(theStartApp,
						serverSocket.accept());
			} catch (Exception e) {
			}
		}

		try {
			if (!notstarted) {
				if (theStartApp.getMainWindow() != null) {
					theStartApp.getMainWindow().sendMessage("Server killed.");
				} else {
					System.out.println("Server killed.");
				}
				theStartApp.log("Server killed.", 1);
			}
			if (serverSocket != null) {
				serverSocket.close();
				serverSocket = null;
			}
		} catch (java.io.IOException e) {
		}
	}

	/***************************
	 * stopListening() causes the server to no longer listen for connections.
	 **/
	public void stopListening() {
		theStartApp.log("PokerServerListener.stopListening()", 3);
		listening = false;
		serverThread = null;
	}

	/***************************
	 * startListening() causes the server to listen for connections.
	 **/
	public void startListening() {
		theStartApp.log("PokerServerListener.startListening()", 3);
		listening = true;
		if (serverThread == null) {
			serverThread = new Thread(this, "Server Listener");
			serverThread.start();
		}
	}

	/***************************
	 * getServerSocket() is used to access the private serverSocket variable
	 * 
	 * @return The serverSocket used by this listener
	 * 
	 **/
	public ServerSocket getServerSocket() {
		return serverSocket;
	}

	/***************************
	 * getStatus() is used to determine if the listener is actively listening
	 * for connections
	 * 
	 * @return Whether or not the server is listening for connections
	 * 
	 **/
	public boolean getStatus() {
		return listening;
	}
}