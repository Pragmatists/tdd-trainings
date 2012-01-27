/******************************************************************************************
 * AIFrame.java               PokerApp                                                    *
 *                                                                                        *
 *   Revision History                                                                     *
 * +---------+----------+---------------------------------------------------------------+ *
 * | Version | DATE     | Description                                                   | *
 * +---------+----------+---------------------------------------------------------------+ *
 * |  0.95   | 09/15/04 | Initial documented release                                    | *
 * |  0.96   | 09/17/04 | Fixed when window closes - only disconnects null socket       | *
 * |  0.96   | 09/23/04 | Improvement to AI window - can display additional data        | *
 * |  0.97   | 11/10/04 | Changed gameName to gameClass - safer to use the class name   | *
 * |  0.98   | 12/12/04 | Changed JFrame icon.                                          | *
 * |  1.00   | 06/06/05 | Added logging.                                                | *
 * |  1.00   | 07/16/07 | Prepare for open source.  Header/comments/package/etc...      | *
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

package net.sourceforge.pokerapp.ai;

import net.sourceforge.pokerapp.*;
import javax.swing.*;
import java.awt.*;
import java.awt.BorderLayout;
import java.util.ArrayList;

/****************************************************
 * The AIFrame class defines the window which is used to control and monitor the
 * AIApp.
 * 
 * @author Dan Puperi
 * @version 1.00
 * 
 **/
public class AIFrame extends JFrame {

	/**
	 * Additional cheat data that can be displayed
	 **/
	public String cheatData[];

	private AIApp theAIApp; // The instance of the AIApp which created this
							// AIFrame class
	private DefaultListModel messageListModel; // The listbox model describing
												// how the message box works
	private JScrollPane messageListScrollPane; // The message area scroll bar
												// definitions
	private JList messageList; // The message area list of messages
	private int messageNum; // Which message should be displayed at the top of
							// the message list
	private DefaultListModel dataListModel; // The listbox model describing how
											// the data area works
	private JScrollPane dataListScrollPane; // The data area scroll bar
											// definitions
	private JList dataList; // The data area list of messages
	private JTextField nameText; // The text field where the AI's name is
									// entered
	private JTextField bankText; // Display of how much money in the AI's bank
	private JTextField potText; // Display of how much money is in the pot
	private JComboBox chooseType; // Combo box used to choose which type of AI
									// to play with
	private JButton button; // The create AI and join the game button
	private JButton dataButton; // Button to toggle cheat data on or off.
	private String playerType; // String describing what type of player this is
								// - from the combo box
	private boolean displayCheatData; // Whether or not the cheat data should be
										// displayed for this AI

	/***********************
	 * Constructor creates and instance of the AIFrame class
	 * 
	 * @param title
	 *            The title at the top of the window
	 * @param a
	 *            The AIApp instance to which this AIFrame belongs
	 * 
	 **/
	public AIFrame(String title, AIApp a) {
		super(title);
		ClassLoader cl = getClass().getClassLoader();
		java.net.URL url = cl.getResource("Images/icon.gif");
		setIconImage(Toolkit.getDefaultToolkit().getImage(url));
		theAIApp = a;
		theAIApp.log("Constructing AIFrame", 3);
		messageNum = 0;
		displayCheatData = false;
		cheatData = new String[4];
		for (int i = 0; i < 4; i++) {
			cheatData[i] = new String();
		}
		getContentPane().setLayout(new BorderLayout());
		JPanel topPanel = new JPanel(new BorderLayout());
		JPanel topLeft = new JPanel(new java.awt.GridLayout(1, 2));
		nameText = new JTextField();
		nameText.setText("AI");
		topLeft.add(nameText);

		playerType = (String) (theAIApp.getAILabels().toArray()[0]);
		chooseType = new JComboBox(theAIApp.getAILabels().toArray());
		chooseType.setSelectedItem(playerType);
		chooseType.addActionListener(new chooseAction());
		topLeft.add(chooseType);
		topPanel.add(topLeft, BorderLayout.WEST);

		button = new JButton("Choose Name and Select Player Type");
		button.addActionListener(new buttonAction());
		topPanel.add(button, BorderLayout.CENTER);

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

		JPanel dataPanel = new JPanel(new BorderLayout());
		dataPanel.setBorder(new javax.swing.border.EmptyBorder(5, 5, 5, 5));
		dataListModel = new DefaultListModel();
		dataList = new JList(dataListModel);
		dataListScrollPane = new JScrollPane(dataList);
		dataListScrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		dataListScrollPane
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		dataPanel.add(dataListScrollPane, BorderLayout.CENTER);

		JPanel scrollPanels = new JPanel(new java.awt.GridLayout(2, 1));
		scrollPanels.add(messagePanel);
		scrollPanels.add(dataPanel);
		getContentPane().add(scrollPanels, BorderLayout.CENTER);

		JPanel statusPanel = new JPanel();
		statusPanel.setLayout(new java.awt.GridLayout(1, 3));
		statusPanel.setBorder(new javax.swing.border.EtchedBorder());
		bankText = new JTextField();
		potText = new JTextField();
		bankText.setEditable(false);
		potText.setEditable(false);
		dataButton = new JButton("Toggle Cheat Data");
		dataButton.addActionListener(new dataAction());
		statusPanel.add(bankText);
		statusPanel.add(potText);
		statusPanel.add(dataButton);

		getContentPane().add(statusPanel, BorderLayout.SOUTH);
		updateMoneyLine();
	}

	/***********************
	 * sendMessage() prints a message to the message box as the first element
	 * (on top)
	 * 
	 * @param m
	 *            The String message to print
	 * 
	 **/
	public void sendMessage(String m) {
		theAIApp.getAIAction().messageToAI(m);
		messageNum++;
		messageListModel.insertElementAt(" " + messageNum + ": " + m, 0);
	}

	/***********************
	 * updateMoneyLine() function is called to display updated bank and pot
	 * values on the window. This version updates with default values.
	 **/
	public void updateMoneyLine() {
		updateMoneyLine(new PokerMoney(), new PokerMoney(), new PokerMoney(),
				new PokerMoney(), new String());
	}

	/***********************
	 * updateMoneyLine() function is called to display updated bank and pot
	 * values on the window. This version the actual data is filled in.
	 * 
	 * @param br
	 *            The AI player's bankroll
	 * @param pbet
	 *            The AI player's bet
	 * @param pot
	 *            The total amount in the pot
	 * @param bet
	 *            The current high bet
	 * @param name
	 *            The name of the player with the current high bet.
	 * 
	 **/
	public void updateMoneyLine(PokerMoney br, PokerMoney pbet, PokerMoney pot,
			PokerMoney bet, String name) {
		theAIApp.log("AIFrame.updateMoneyLine( " + br + ", " + pbet + ", "
				+ pot + ", " + bet + ", " + name + " )", 3);
		if (theAIApp.getThisPlayer() != null) {
			theAIApp.getThisPlayer().setBankroll(br.amount());
		}
		bankText.setText("Cash : " + br);
		potText.setText("Pot : " + pot);
		dataListModel.clear();
		if (displayCheatData) {
			updateData();
		}
	}

	/***********************
	 * updateData() function is called to display updated cheat information
	 **/
	public void updateData() {
		theAIApp.log("AIFrame.updateData()", 3);
		if (theAIApp.getAIAction() != null) {
			if (theAIApp.getAIAction().getAI() != null) {
				String gameName;
				if (theAIApp.getAIAction().getAI().getGameClass() == null) {
					gameName = new String("nothing.");
				} else {
					if (theAIApp.getAIAction().getAI().getGameClass().length() < 3) {
						gameName = new String("nothing");
					} else {
						gameName = theAIApp.getAIAction().getAI()
								.getGameClass();
					}
				}
				dataListModel.addElement("Playing " + gameName);
			}
		}
		Hand myHand = theAIApp.startUpApp.getAIHand(theAIApp);
		if (myHand != null) {
			StringBuffer cardsToDisplay = new StringBuffer();
			if (myHand.getNumHole() >= 1) {
				cardsToDisplay
						.append(myHand.getHoleCard(0) + new String("   "));
			}
			if (myHand.getNumHole() >= 2) {
				cardsToDisplay
						.append(myHand.getHoleCard(1) + new String("   "));
			}
			if (myHand.getNumHole() >= 3) {
				cardsToDisplay
						.append(myHand.getHoleCard(2) + new String("   "));
			}
			if (myHand.getNumHole() == 4) {
				cardsToDisplay.append(myHand.getHoleCard(3));
			}
			dataListModel.addElement(cardsToDisplay.toString());
			if (theAIApp.startUpApp.getGame() != null) {
				dataListModel.addElement("My hand = "
						+ HandEvaluator.nameHand(theAIApp.startUpApp.getGame()
								.bestHand(myHand)));
			}
			if (theAIApp.startUpApp.getGame() != null) {
				dataListModel.addElement("Best hand = "
						+ HandEvaluator.nameHand(theAIApp.startUpApp.getGame()
								.getBestPossible()));
			}
		}
		for (int i = 0; i < 4; i++) {
			dataListModel.addElement(cheatData[i]);
		}
	}

	/***********************
	 * processWindowEvent() processes the close window event.
	 * 
	 * @param e
	 *            The window event to process
	 * 
	 **/
	protected void processWindowEvent(java.awt.event.WindowEvent e) {
		if (e.getID() == java.awt.event.WindowEvent.WINDOW_CLOSING) {
			if (theAIApp.getSocket() != null) {
				theAIApp.disconnectSocket();
			}
		}
		super.processWindowEvent(e);
	}

	/***********************
	 * chooseAction class defines what happens when a player type choice is made
	 * via the combo box.
	 **/
	class chooseAction extends AbstractAction {

		// ----------------------
		// Constructor
		//
		chooseAction() {
			super();
		}

		// ----------------------
		// actionPerformed() function is called when the the user makes a choice
		// from the combo box.
		//
		public void actionPerformed(java.awt.event.ActionEvent e) {
			playerType = (String) chooseType.getSelectedItem();
		}
	}

	/***********************
	 * buttonAction class defines what happens when the button is pressed.
	 **/
	class buttonAction extends AbstractAction {

		// ----------------------
		// Constructor
		//
		buttonAction() {
			super();
		}

		// ----------------------
		// actionPerformed() function is called when the button is actually
		// pressed.
		//
		public void actionPerformed(java.awt.event.ActionEvent e) {
			theAIApp.log("AIFrame.buttonAction.actionPerformed()", 3);
			button.setEnabled(false);
			chooseType.setEnabled(false);
			nameText.setEnabled(false);
			theAIApp.connectSocket(nameText.getText(), playerType);

			// Now need to find an empty seat at the table
			// Pause to make sure it got all the player data first
			//
			try {
				Thread.sleep(1000);
			} catch (InterruptedException x) {
			}
			int seat = -1;
			int i = 0;
			while ((seat == -1) && (i < PokerGame.MAX_PLAYERS)) {
				seat = theAIApp.getPlayerInSeat(i);
				if (seat != -9) {
					seat = -1;
				} else {
					seat = i;
				}
				i++;
			}
			if (seat == -1) {
				theAIApp.disconnectSocket();
			} else {
				theAIApp.messageToServer("NEW PLAYER &" + seat + "&0");
				theAIApp.getThisPlayer().seat = seat;
			}
		}
	}

	/***********************
	 * dataAction class defines what happens when the toggle cheat data button
	 * is pressed.
	 **/
	class dataAction extends AbstractAction {

		// ----------------------
		// Constructor
		//
		dataAction() {
			super();
		}

		// ----------------------
		// actionPerformed() function is called when the the user makes a choice
		// from the combo box.
		//
		public void actionPerformed(java.awt.event.ActionEvent e) {
			theAIApp.log("AIFrame.dataAction.actionPerformed()", 3);
			displayCheatData = !displayCheatData;
			dataListModel.clear();
			if (displayCheatData) {
				updateData();
			}
		}
	}
}
