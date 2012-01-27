/******************************************************************************************
 * PokerApplet.java                PokerApp                                               *
 *                                                                                        *
 *   Revision History                                                                     *
 * +---------+----------+---------------------------------------------------------------+ *
 * | Version | DATE     | Description                                                   | *
 * +---------+----------+---------------------------------------------------------------+ *
 * |  1.00   | 05/26/05 | Initial documented release                                    | *
 * |  1.00   | 07/09/07 | Prepare for open source.  Header/comments/package/etc...      | *
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

import java.applet.*;
import javax.swing.*;
import java.net.*;
import java.io.*;

/****************************************************
 * The PokerApplet class can be used to run a PokerApp client on a webpage.
 * 
 * @author Dan Puperi
 * @version 1.00
 * 
 **/
public class PokerApplet extends JApplet {

	/**
	 * The Applet object
	 **/
	public PokerApplet theApplet;

	/***************************
	 * The start() function is the entry point into the program.
	 **/
	public void start() {
		theApplet = this;
		if (theApplet != null) {
			new StartPoker(theApplet);
		} else {
			System.out.println("Error - Applet instance is null");
		}
	}

	/***************************
	 * connect() is how the applet connects to the server
	 * 
	 * @param portNum
	 *            The port number that should be used to connect to the server.
	 * @return The socket to which this client is connected.
	 * 
	 **/
	public Socket connect(int portNum) {
		try {
			URL serverURL = getCodeBase();
			Socket connection = new Socket(serverURL.getHost(), portNum);
			return connection;
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return null;
	}
}
