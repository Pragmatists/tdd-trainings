/******************************************************************************************
 * StartFrame.java                 PokerApp                                               *
 *                                                                                        *
 *   Revision History                                                                     *
 * +---------+----------+---------------------------------------------------------------+ *
 * | Version | DATE     | Description                                                   | *
 * +---------+----------+---------------------------------------------------------------+ *
 * |  0.95   | 09/15/04 | Initial documented release                                    | *
 * |  0.98   | 12/06/04 | Changed arguments for StartServer() call.                     | *
 * |  0.98   | 12/12/04 | Changes the JFrame icon.                                      | *
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
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/****************************************************
 * StartFrame defines the initial window that can be used to start a server or a
 * client.
 * 
 * @author Dan Puperi
 * @version 1.00
 * 
 **/
public final class StartFrame extends JFrame {

	/**
	 * Button to start a new server
	 **/
	public JButton startServerButton;

	/**
	 * Button to start a new client
	 **/
	public JButton startClientButton;

	private StartPoker theStartApp; // The StartPoker class to which this window
									// belongs
	private JTextField messageText; // Message to be displayed on this window on
									// the bottom line
	private JTextField portText; // The text field where the user can enter the
									// port number on which to start the server.

	/***************************
	 * The StartFrame class is created by specifying the title of the frame and
	 * the StartPoker class instance to which this frame belongs.
	 * 
	 * @param title
	 *            The String title which will appear at the top of the frame in
	 *            the title bar
	 * @param a
	 *            The StartPoker class to which this frame belongs
	 * 
	 **/
	public StartFrame(String title, StartPoker a) {
		super(title);
		ClassLoader cl = getClass().getClassLoader();
		java.net.URL url = cl.getResource("Images/icon.gif");
		setIconImage(Toolkit.getDefaultToolkit().getImage(url));
		theStartApp = a;
		theStartApp.log("Constructing StartFrame", 3);

		JPanel panel = new JPanel(new GridLayout(3, 1, 2, 2));
		panel.setBorder(new EmptyBorder(3, 3, 3, 3));

		Box serverBox = Box.createHorizontalBox();
		serverBox.add(new JLabel("Server Port Number : "));
		portText = new JTextField(theStartApp.getPort());
		serverBox.add(portText);
		startServerButton = new JButton("Start a server");
		startServerButton.addActionListener(new startServerAction());
		serverBox.add(startServerButton);

		startClientButton = new JButton("Start a client");
		startClientButton.addActionListener(new startClientAction());
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBorder(new EmptyBorder(0, 15, 0, 15));
		buttonPanel.add(startClientButton);

		messageText = new JTextField();
		messageText.setEditable(false);

		panel.add(serverBox);
		panel.add(buttonPanel);
		panel.add(messageText);
		getContentPane().add(panel);
		setResizable(false);
	}

	/***********************
	 * sendMessage() displays a message.
	 * 
	 * @param m
	 *            The String message to display
	 * 
	 **/
	public void sendMessage(String m) {
		messageText.setText(m);
	}

	/***********************
	 * processWindowEvent() processes the close window event
	 * 
	 * @param e
	 *            The window event to process
	 * 
	 **/
	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			theStartApp.KillServer();
			theStartApp.closeLogFile();
			System.exit(0);
		}
		super.processWindowEvent(e);
	}

	/***********************
	 * startServerAction class is used to define the action that occurs when the
	 * start server button is pressed.
	 **/
	class startServerAction extends AbstractAction {

		// ----------------------
		// Constructor
		//
		startServerAction() {
			super();
		}

		// ----------------------
		// The actionPerformed() function is called when the button is pressed
		//
		public void actionPerformed(ActionEvent e) {
			theStartApp
					.log("StartFrame.startServerAction.actionPerformed()", 3);
			try {
				int port = Integer.parseInt(portText.getText());
				theStartApp.StartServer(port, true);
			} catch (NumberFormatException x) {
				sendMessage("Invalid Port");
				theStartApp.log(
						"Could not start a server. Invalid port number given : "
								+ portText.getText(), 1);
			}
		}
	}

	/***********************
	 * startClientAction class is used to define the action that occurs when the
	 * start client button is pressed.
	 **/
	class startClientAction extends AbstractAction {

		// ----------------------
		// Constructor
		//
		startClientAction() {
			super();
		}

		// ----------------------
		// The actionPerformed() function is called when the button is pressed
		//
		public void actionPerformed(ActionEvent e) {
			theStartApp
					.log("StartFrame.startClientAction.actionPerformed()", 3);
			theStartApp.StartClient();
		}
	}
}