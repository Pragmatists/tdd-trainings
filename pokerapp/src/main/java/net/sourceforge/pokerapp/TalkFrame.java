/******************************************************************************************
 * TalkFrame.java                  PokerApp                                               *
 *                                                                                        *
 *   Revision History                                                                     *
 * +---------+----------+---------------------------------------------------------------+ *
 * | Version | DATE     | Description                                                   | *
 * +---------+----------+---------------------------------------------------------------+ *
 * |  0.95   | 08/10/04 | Initial documented release                                    | *
 * |  0.97   | 10/25/04 | Take out decryption routine and put a stronger one in         | *
 * |         |          | PokerProtocol                                                 | *
 * |  0.98   | 12/12/04 | Change the JFrame icon                                        | *
 * |  1.00   | 06/06/05 | Added log message when this frame is started.                 | *
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
 * The TalkFrame class represents the window which players can use to
 * communicate.
 * 
 * @author Dan Puperi
 * @version 1.00
 * 
 **/
public class TalkFrame extends JFrame {

	private PokerApp theApp; // The PokerApp instance to which this class
								// belongs
	private DefaultListModel messageListModel; // The list model of all of the
												// messages
	private JScrollPane messageListScrollPane; // The scroll pane where the
												// messages are displayed
	private JList messageList; // The list of messages
	private JTextField talkText; // The text field where the user can input a
									// message
	private JButton submitBtn; // Button to sumbit the message
	private JButton clearBtn; // Button to clear the text field

	/***************************
	 * The TalkFrame class is created by specifying the title of the frame and
	 * the PokerApp class instance to which this frame belongs.
	 * 
	 * @param title
	 *            The String title which will appear at the top of the frame in
	 *            the title bar
	 * @param a
	 *            The PokerApp class to which this frame belongs
	 * 
	 **/
	public TalkFrame(String title, PokerApp a) {
		super(title);
		ClassLoader cl = getClass().getClassLoader();
		java.net.URL url = cl.getResource("Images/icon.gif");
		setIconImage(Toolkit.getDefaultToolkit().getImage(url));
		theApp = a;

		theApp.log("Constructing TalkFrame", 3);
		getContentPane().setLayout(new BorderLayout());

		JPanel messagePanel = new JPanel(new BorderLayout());
		messagePanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		messageListModel = new DefaultListModel();
		messageList = new JList(messageListModel);
		messageListScrollPane = new JScrollPane(messageList);
		messageListScrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		messageListScrollPane
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		messagePanel.add(messageListScrollPane, BorderLayout.CENTER);
		getContentPane().add(messagePanel, BorderLayout.CENTER);

		Box talkBox = Box.createHorizontalBox();
		talkText = new JTextField();
		submitBtn = new JButton("Submit");
		submitBtn.addActionListener(new submitAction());
		clearBtn = new JButton("Clear");
		clearBtn.addActionListener(new clearAction());
		talkBox.add(talkText);
		talkBox.add(submitBtn);
		talkBox.add(clearBtn);
		getContentPane().add(talkBox, BorderLayout.SOUTH);
	}

	/***********************
	 * sendMessage() displays a message on the scoll area.
	 * 
	 * @param m
	 *            The String message to display
	 * 
	 **/
	public void sendMessage(String m) {
		messageListModel.insertElementAt(m, 0);
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
			dispose();
		}
		super.processWindowEvent(e);
	}

	/***********************
	 * submitAction class is used to define the action that occurs when the
	 * submit button is pressed.
	 **/
	class submitAction extends AbstractAction {

		// ----------------------
		// Constructor
		//
		submitAction() {
			super();
		}

		// ----------------------
		// The actionPerformed() function is called when the button is pressed
		//
		public void actionPerformed(ActionEvent e) {
			String t = new String(talkText.getText());
			if (t.indexOf('&') == -1) {
				theApp.messageToServer("COMMUNICATE &" + t);
			}
			talkText.setText("");
		}
	}

	/***********************
	 * The clearAction class is used to define the action that occurs when the
	 * clear button is pressed.
	 **/
	class clearAction extends AbstractAction {

		// ----------------------
		// Constructor
		//
		clearAction() {
			super();
		}

		// ----------------------
		// The actionPerformed() function is called when the button is pressed
		//
		public void actionPerformed(ActionEvent e) {
			talkText.setText("");
		}
	}
}