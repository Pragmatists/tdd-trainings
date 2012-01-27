/******************************************************************************************
 * ServerFrame.java                PokerApp                                               *
 *                                                                                        *
 *   Revision History                                                                     *
 * +---------+----------+---------------------------------------------------------------+ *
 * | Version | DATE     | Description                                                   | *
 * +---------+----------+---------------------------------------------------------------+ *
 * |  0.95   | 09/15/04 | Initial documented release                                    | *
 * |  0.98   | 12/12/04 | Changed the frame icon to a stack of poker chips              | *
 * |  0.99   | 01/31/05 | Added a field which displays the name of the server computer. | *
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

import javax.swing.*;
import java.awt.*;
import java.net.*;

/****************************************************
 * The ServerFrame class represents the poker server window.
 * 
 * @author Dan Puperi
 * @version 1.00
 * 
 **/
public final class ServerFrame extends JFrame {

	private StartPoker theApp; // The StartPoker class that started this window
	private DefaultListModel messageListModel; // List model for the messages
	private JScrollPane messageListScrollPane; // Scroll pane for the message
												// area
	private JList messageList; // The message area
	private int messageNum; // Number of the message - used to control which
							// message is on top
	private ServerMenuBar menuBar; // The menu on this window.

	/***************************
	 * The ServerFrame class is created by specifying the title of the frame and
	 * the StartPoker class instance to which this frame belongs.
	 * 
	 * @param title
	 *            The String title which will appear at the top of the frame in
	 *            the title bar
	 * @param a
	 *            The StartPoker class to which this frame belongs
	 * 
	 **/
	public ServerFrame(String title, StartPoker a) {
		super(title);
		ClassLoader cl = getClass().getClassLoader();
		java.net.URL url = cl.getResource("Images/icon.gif");
		setIconImage(Toolkit.getDefaultToolkit().getImage(url));
		theApp = a;
		messageNum = 0;
		theApp.log("Constructing ServerFrame", 3);

		menuBar = new ServerMenuBar(theApp);
		setJMenuBar(menuBar);

		getContentPane().setLayout(new BorderLayout());

		JPanel topPanel = new JPanel(new GridLayout(1, 2));
		topPanel.add(new JLabel("Welcome to the Poker Server"));
		//
		// Find this computer's IP address
		//
		String addressToDisplay = "0.0.0.0";
		try {
			InetAddress[] addresses;
			addresses = InetAddress.getAllByName(InetAddress.getLocalHost()
					.getHostName());
			boolean found = false;
			for (int i = 0; i < addresses.length; i++) {
				String aStr = new String(addresses[i].getHostAddress());
				if (!found && (!aStr.startsWith("10"))
						&& (!aStr.startsWith("127"))
						&& (!aStr.startsWith("192.168"))
						&& (!aStr.startsWith("0"))) {
					found = true;
					addressToDisplay = aStr;
				}
			}
			if (!found) {
				for (int i = 0; i < addresses.length; i++) {
					String aStr = new String(addresses[i].getHostAddress());
					if (!found && (!aStr.startsWith("127"))
							&& (!aStr.startsWith("0"))) {
						found = true;
						addressToDisplay = aStr;
					}
				}
			}
		} catch (Exception e) {
			theApp.log("Warning: could not get hostname for the server");
			theApp.log(e.getMessage());
			theApp.logStackTrace(e);
		}

		topPanel.add(new JLabel("Server = " + addressToDisplay));
		getContentPane().add(topPanel, BorderLayout.NORTH);

		JPanel messagePanel = new JPanel(new BorderLayout());
		messagePanel.setBorder(new javax.swing.border.EmptyBorder(5, 5, 5, 5));
		messageListModel = new DefaultListModel();
		messageList = new JList(messageListModel);
		messageListScrollPane = new JScrollPane(messageList);
		messageListScrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		messageListScrollPane
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		messagePanel.add(messageListScrollPane, BorderLayout.CENTER);
		getContentPane().add(messagePanel, BorderLayout.CENTER);
	}

	/***********************
	 * sendMessage() displays a message on the scoll area.
	 * 
	 * @param m
	 *            The String message to display
	 * 
	 **/
	public void sendMessage(String m) {
		messageNum++;
		messageListModel.insertElementAt(" " + messageNum + ": " + m, 0);
	}

	/***********************
	 * processWindowEvent() processes the close window event
	 * 
	 * @param e
	 *            The window event to process
	 * 
	 **/
	protected void processWindowEvent(java.awt.event.WindowEvent e) {
		if (e.getID() == java.awt.event.WindowEvent.WINDOW_CLOSING) {
			theApp.KillServer();
		}
		super.processWindowEvent(e);
	}

	/***********************
	 * getMenu() is used to access the menuBar object
	 * 
	 * @return The MenuBar
	 * 
	 **/
	public ServerMenuBar getMenu() {
		return menuBar;
	}

	/***********************
	 * menuRedisplay() redisplays all the data on the menu
	 **/
	public void menuRedisplay() {
		theApp.log("ServerFrame.menuRedisplay()", 3);
		Component gp = getGlassPane();
		gp.setVisible(false);
		remove(getGlassPane());
		remove(getJMenuBar());
		setJMenuBar(new ServerMenuBar(theApp));
		setGlassPane(gp);
		gp.setVisible(true);
	}
}