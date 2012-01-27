/******************************************************************************************
 * PokerMenuBar.java               PokerApp                                               *
 *                                                                                        *
 *   Revision History                                                                     *
 * +---------+----------+---------------------------------------------------------------+ *
 * | Version | DATE     | Description                                                   | *
 * +---------+----------+---------------------------------------------------------------+ *
 * |  0.95   | 09/15/04 | Initial documented release                                    | *
 * |  0.97   | 10/28/04 | Change About box to correct version (oops - forgot for v0.96) | *
 * |  0.97   | 10/28/04 | Change About box to add link to PokerApp forum.               | *
 * |  0.98   | 11/15/04 | Change JoinFrame and RegisterFrame to JDialog classes so they | *
 * |         |          | won't get hidden behind the client window.                    | *
 * |  0.98   | 11/15/04 | Change about box to correct version                           | *
 * |  0.98   | 12/07/04 | Correct autoDealing check to gray out deal option.            | *
 * |  0.98   | 12/08/04 | The rules frame displays bet limit and pot limit rules        | *
 * |  0.98   | 12/10/04 | Change disconnectSocket calls to pass a boolean argument.     | *
 * |  0.98   | 12/12/04 | Change the JFrame icon.                                       | *
 * |  0.98   | 12/14/04 | Add which games the server is dealing to the rules frame.     | *
 * |  0.99   | 01/31/05 | Change the about box to correct version                       | *
 * |  0.99   | 03/30/05 | Make menu yellow when it's this player's turn to bet          | *
 * |  0.99   | 03/30/05 | Change the rules frame so all the rules are shown near top.   | *
 * |  0.99   | 05/17/05 | Make menu orange when it's this player's turn to deal.        | *
 * |  1.00   | 05/26/05 | Change the about box to correct version.                      | *
 * |  1.00   | 05/26/05 | Check for running an applet - if so when player joins, he     | *
 * |         |          | does not need to provide a server and port.                   | *
 * |  1.00   | 06/06/05 | Added logging.                                                | *
 * |  1.00   | 06/06/05 | Removed copywrite line in about box.  I forgot how much I     | *
 * |         |          | hate that BS.                                                 | *
 * |  1.00   | 07/25/05 | Added menu option for one click check and call buttons.       | *
 * |  1.00   | 08/19/05 | Allow antes and blinds to be selected at the same time.  Had  | *
 * |         |          | to update the rules frame.                                    | *
 * |  1.00   | 08/22/05 | Add window menu and place Communicator, Rules, and Messages   | *
 * |         |          | window menu items under there.                                | *
 * |  1.00   | 08/29/05 | Modified window placement for the various windows.            | *
 * |  1.00   | 07/13/07 | Prepare for open source.  Header/comments/package/etc...      | *
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

import net.sourceforge.pokerapp.games.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/****************************************************
 * The PokerMenuBar class defines the menu bar for the PokerFrame class.
 * 
 * @author Dan Puperi
 * @version 1.00
 * 
 **/
public final class PokerMenuBar extends JMenuBar {

	/**
	 * The window that displays the server rules.
	 **/
	public RulesFrame rulesWindow;

	private PokerApp theApp; // The PokerApp to which this menu belongs
	private JMenu menuGame; // The Game menu
	private JMenu menuDeal; // The Deal menu
	private JMenu menuOptions; // The Options menu
	private JMenu menuCards; // The Cards menu
	private JMenu menuTable; // The Table menu
	private JMenu menuWindow; // The Window menu
	private JMenu menuHelp; // The Help menu
	private JMenuItem menuGameJoin; // Menu item to join a game
	private JMenuItem menuGameLeave; // Menu item to get up from the table
	private JMenuItem menuGameDisconnect; // Menu item to disconnect from the
											// server
	private JMenuItem menuGameQuit; // Menu item to quit playing PokerApp
	private JMenuItem menuWindowComm; // Menu item to involk the communicator
	private JMenuItem menuWindowMessages; // Menu item to display messages.
	private JMenuItem menuWindowRules; // Menu item to view the rules of the
										// server
	private JMenuItem menuHelpHelp; // Menu item to access the help
	private JCheckBoxMenuItem menuOptionsHide; // Menu item to hide the players
												// hole cards
	private JCheckBoxMenuItem menuOptionsMuck; // Menu item to not show cards
												// from losing hands
	private JCheckBoxMenuItem menuOptionsOneClick; // Menu item to use one click
													// one check and call
													// buttons.
	private ArrayList menuCardBacks; // List of available card back images
	private ArrayList menuTableImgs; // List of available table top images
	private static final int NUM_RULES = 7; // Number of possible rules
											// displayed on the rules frame.

	/***********************
	 * The PokerMenuBar constructor creates a the menu bar
	 * 
	 * @param a
	 *            The PokerApp instance to which this menu bar belongs
	 * 
	 **/
	public PokerMenuBar(PokerApp a) {
		super();
		theApp = a;
		theApp.log("Constructing PokerMenuBar", 3);
		menuCardBacks = new ArrayList();
		menuTableImgs = new ArrayList();
		rulesWindow = null;

		boolean connected = false;
		boolean sittingDown = false;

		if (theApp.getSocket() != null) {
			connected = true;
		}
		if (theApp.getThisPlayer() != null) {
			if (theApp.getThisPlayer().seat >= 0) {
				sittingDown = true;
			}
		}

		menuGame = new JMenu("Game");
		menuGame.setMnemonic('G');
		add(menuGame);

		menuGameJoin = new JMenuItem("Join a game");
		menuGameJoin.addActionListener(new JoinAction());
		menuGameJoin.setEnabled(!connected);
		menuGame.add(menuGameJoin);

		menuGameLeave = new JMenuItem("Leave the table");
		menuGameLeave.addActionListener(new LeaveAction());
		menuGameLeave.setEnabled(connected & sittingDown);
		menuGame.add(menuGameLeave);

		menuGameDisconnect = new JMenuItem("Disconnect from server");
		menuGameDisconnect.addActionListener(new DisconnectAction());
		menuGameDisconnect.setEnabled(connected);
		menuGame.add(menuGameDisconnect);

		menuGame.addSeparator();

		menuGameQuit = new JMenuItem("Quit");
		menuGameQuit.addActionListener(new QuitAction());
		menuGame.add(menuGameQuit);

		boolean isDealer = false;
		if (theApp.getThisPlayer() != null) {
			int pIndex = theApp.playerIndex(theApp.getThisPlayer().getName());
			if ((theApp.dealerIndex == pIndex) && sittingDown) {
				isDealer = true;
			}
		}

		if (theApp.autoDealing) {
			isDealer = false;
		}
		if (theApp.inGame) {
			isDealer = false;
		}
		if (theApp.getSocket() == null) {
			isDealer = false;
		}

		menuDeal = new JMenu("Deal");
		menuDeal.setMnemonic('D');
		menuDeal.setEnabled(isDealer);
		add(menuDeal);

		for (int i = 0; i < theApp.getGameClasses().size(); i++) {
			JMenuItem menuItem = new JMenuItem((String) theApp.getGameLabels()
					.get(i));
			menuItem.addActionListener(new DealAction((String) theApp
					.getGameLabels().get(i)));
			menuDeal.add(menuItem);
		}

		menuOptions = new JMenu("Options");
		menuOptions.setMnemonic('O');
		add(menuOptions);

		menuOptionsHide = new JCheckBoxMenuItem("Hide hole cards");
		menuOptionsHide.addActionListener(new OptionsAction("Hide Hole"));
		menuOptionsHide.setSelected(theApp.hideHoleCards);
		menuOptions.add(menuOptionsHide);

		menuOptionsMuck = new JCheckBoxMenuItem("Don't show losing hands");
		menuOptionsMuck.addActionListener(new OptionsAction("Muck Cards"));
		menuOptionsMuck.setSelected(theApp.muckLosingHands);
		menuOptions.add(menuOptionsMuck);

		menuOptionsOneClick = new JCheckBoxMenuItem(
				"One click Check and Call buttons");
		menuOptionsOneClick.addActionListener(new OptionsAction("One Click"));
		menuOptionsOneClick.setSelected(theApp.oneClickCheckCall);
		menuOptions.add(menuOptionsOneClick);

		menuCards = new JMenu("Cards");
		menuCards.setMnemonic('C');
		add(menuCards);

		for (int i = 0; i < theApp.getCardBackLabels().size(); i++) {
			menuCardBacks.add(new JMenuItem((String) theApp.getCardBackLabels()
					.get(i)));
			((JMenuItem) menuCardBacks.get(i))
					.addActionListener(new CardBackAction((String) theApp
							.getCardBackLabels().get(i)));
			menuCards.add((JMenuItem) menuCardBacks.get(i));
		}

		menuTable = new JMenu("Table");
		menuTable.setMnemonic('T');
		add(menuTable);

		for (int i = 0; i < theApp.getTableImgLabels().size(); i++) {
			menuTableImgs.add(new JMenuItem((String) theApp.getTableImgLabels()
					.get(i)));
			((JMenuItem) menuTableImgs.get(i))
					.addActionListener(new TableImgAction((String) theApp
							.getTableImgLabels().get(i)));
			menuTable.add((JMenuItem) menuTableImgs.get(i));
		}

		menuWindow = new JMenu("Window");
		menuWindow.setMnemonic('W');
		add(menuWindow);

		menuWindowRules = new JMenuItem("View Rules for this table");
		menuWindow.add(menuWindowRules);
		menuWindowRules.setEnabled(connected);
		menuWindowRules.addActionListener(new RulesAction());

		menuWindowComm = new JMenuItem("Open Communicator");
		menuWindowComm.addActionListener(new CommAction());
		menuWindowComm.setEnabled(connected);
		menuWindow.add(menuWindowComm);

		menuWindowMessages = new JMenuItem("Open Message Window");
		menuWindowMessages.addActionListener(new MessagesAction());
		menuWindow.add(menuWindowMessages);

		menuHelp = new JMenu("Help");
		menuHelp.setMnemonic('H');
		add(menuHelp);

		menuHelpHelp = new JMenuItem("PokerApp Help");
		menuHelpHelp.addActionListener(new HelpAction());
		menuHelp.add(menuHelpHelp);

		String mes = new String();
		Color col = Color.lightGray;
		if (theApp.turnToBet) {
			col = Color.yellow;
			mes = new String("IT'S YOUR TURN TO BET");
		} else if (isDealer) {
			col = Color.orange;
			mes = new String("IT'S YOUR TURN TO DEAL");
		}
		if (theApp.turnToBet || isDealer) {
			setBackground(col);
			menuHelp.setBackground(col);
			menuTable.setBackground(col);
			menuCards.setBackground(col);
			menuOptions.setBackground(col);
			menuDeal.setBackground(col);
			menuGame.setBackground(col);
			menuWindow.setBackground(col);
			JMenu blank = new JMenu("         ");
			blank.setBackground(col);
			add(blank);
			JMenu alert = new JMenu(mes);
			alert.setBackground(col);
			add(alert);
		}
	}

	/***********************
	 * JoinAction class defines what happens when Game/Join is selected.
	 **/
	class JoinAction extends AbstractAction {

		// ----------------------
		// Constructor
		//
		JoinAction() {
			super();
		}

		// ----------------------
		// actionPerformed() is called when the menu item is selected
		//
		public void actionPerformed(ActionEvent e) {
			theApp.log("PokerMenuBar.JoinAction.actionPerformed()", 3);
			JoinFrame j = new JoinFrame("Join a poker game");
			Toolkit tk = j.getToolkit();
			Dimension screensize = tk.getScreenSize();
			j.setBounds(100, 100, 200, 150);
			j.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			j.setVisible(true);
		}
	}

	/***********************
	 * LeaveAction class defines what happens when Game/Leave is selected.
	 **/
	class LeaveAction extends AbstractAction {

		// ----------------------
		// Constructor
		//
		LeaveAction() {
			super();
		}

		// ----------------------
		// actionPerformed() is called when the menu item is selected
		//
		public void actionPerformed(ActionEvent e) {
			theApp.log("PokerMenuBar.LeaveAction.actionPerformed()", 3);
			theApp.messageToServer("BET FOLD");
			theApp.messageToServer("STAND UP");
			theApp.getThisPlayer().seat = -9;
			theApp.getPlayerList().remove(
					theApp.playerIndex(theApp.getThisPlayer().getName()));
			theApp.getPlayerList().trimToSize();
			theApp.turnToBet = false;
			theApp.getWindow().menuRedisplay();
			theApp.getWindow().disableButtons();
		}
	}

	/***********************
	 * DisconnectAction class defines what happens when Game/Disconnect is
	 * selected.
	 **/
	class DisconnectAction extends AbstractAction {

		// ----------------------
		// Constructor
		//
		DisconnectAction() {
			super();
		}

		// ----------------------
		// actionPerformed() is called when the menu item is selected
		//
		public void actionPerformed(ActionEvent e) {
			theApp.log("PokerMenuBar.DisconnectAction.actionPerformed()", 3);
			theApp.disconnectSocket(true);
		}
	}

	/***********************
	 * CommAction class defines what happens when Game/Communicator is selected.
	 **/
	class CommAction extends AbstractAction {

		// ----------------------
		// Constructor
		//
		CommAction() {
			super();
		}

		// ----------------------
		// actionPerformed() is called when the menu item is selected
		//
		public void actionPerformed(ActionEvent e) {
			theApp.log("PokerMenuBar.CommAction.actionPerformed()", 3);
			theApp.communicator = new TalkFrame("Poker Communicator", theApp);
			theApp.communicator.setBounds(600, 180, 420, 320);
			theApp.communicator
					.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			theApp.communicator.setVisible(true);
		}
	}

	/***********************
	 * MessagesAction class defines what happens when Window/Messages is
	 * selected.
	 **/
	class MessagesAction extends AbstractAction {

		// ----------------------
		// Constructor
		//
		MessagesAction() {
			super();
		}

		// ----------------------
		// actionPerformed() is called when the menu item is selected
		//
		public void actionPerformed(ActionEvent e) {
			theApp.log("PokerMenuBar.MessagesAction.actionPerformed()", 3);
			theApp.messageWindow = new MessageFrame("PokerApp Messages", theApp);
			theApp.messageWindow.setBounds(600, 500, 420, 240);
			theApp.messageWindow
					.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			theApp.messageWindow.setVisible(true);
		}
	}

	/***********************
	 * QuitAction class defines what happens when Game/Quit is selected.
	 **/
	class QuitAction extends AbstractAction {

		// ----------------------
		// Constructor
		//
		QuitAction() {
			super();
		}

		// ----------------------
		// actionPerformed() is called when the menu item is selected
		//
		public void actionPerformed(ActionEvent e) {
			theApp.log("PokerMenuBar.QuitAction.actionPerformed()", 3);
			theApp.disconnectSocket(false);
			if (theApp.startUpApp != null) {
				theApp.startUpApp.KillClient();
			} else {
				theApp.getWindow().dispose();
				theApp.closeLogFile();
				System.exit(0);
			}
		}
	}

	/***********************
	 * DealAction class defines what happens when Deal is selected.
	 **/
	class DealAction extends AbstractAction {

		String label = new String();

		// ----------------------
		// Constructor
		//
		DealAction(String l) {
			super();
			label = l;
		}

		// ----------------------
		// actionPerformed() is called when the menu item is selected
		//
		public void actionPerformed(ActionEvent e) {
			theApp.log("PokerMenuBar.DealAction.actionPerformed() - " + label,
					3);
			theApp.messageToServer("NEW GAME  &" + label);
		}
	}

	/***********************
	 * OptionsAction class defines what happens when options are changed.
	 **/
	class OptionsAction extends AbstractAction {

		String action = new String();

		// ----------------------
		// Constructor
		//
		OptionsAction(String a) {
			super();
			action = a;
		}

		// ----------------------

		// ----------------------
		// actionPerformed()
		//
		// The actionPerformed() function is called when the menu item is
		// selected
		//

		public void actionPerformed(ActionEvent e) {
			theApp.log("PokerMenuBar.OptionsAction.actionPerformed() - "
					+ action, 3);
			if (action.equals(new String("Hide Hole"))) {
				if (theApp.getThisPlayer() != null) {
					int pindex = theApp.playerIndex(theApp.getThisPlayer()
							.getName());
					int seat = theApp.seatIndex(theApp.getThisPlayer()
							.getName());
					if ((pindex < 0) || (pindex >= PokerGame.MAX_PLAYERS)
							|| (seat < 0) || (seat >= PokerGame.MAX_PLAYERS)) {
						theApp.getWindow().menuRedisplay();
						return;
					}

					for (int j = 0; j < 10; j++) {
						if (theApp.cardPics[pindex][j] != null) {
							theApp.getModel()
									.remove(theApp.cardPics[pindex][j]);
						}
					}

					if (theApp.hideHoleCards) {
						int cindex = 0;
						for (int i = 0; i < theApp.getThisPlayer().getHand()
								.getNumHole(); i++) {
							Card card = theApp.getThisPlayer().getHand()
									.getHoleCard(i);
							theApp.cardPics[pindex][cindex] = new Picture(
									card.img, theApp.LOCATION[seat][0] + 30
											* cindex, theApp.LOCATION[seat][1]);
							theApp.getModel().add(
									theApp.cardPics[pindex][cindex]);
							cindex++;
						}
						for (int i = 0; i < theApp.getThisPlayer().getHand()
								.getNumUp(); i++) {
							Card card = theApp.getThisPlayer().getHand()
									.getUpCard(i);
							theApp.cardPics[pindex][cindex] = new Picture(
									card.img, theApp.LOCATION[seat][0] + 30
											* cindex, theApp.LOCATION[seat][1]);
							theApp.getModel().add(
									theApp.cardPics[pindex][cindex]);
							cindex++;
						}
					} else {
						int cindex = 0;
						for (int i = 0; i < theApp.getThisPlayer().getHand()
								.getNumHole(); i++) {
							Card card = theApp.getThisPlayer().getHand()
									.getHoleCard(i);
							theApp.cardPics[pindex][cindex] = new Picture(
									theApp.getView().getCardBackImage(),
									theApp.LOCATION[seat][0] + 30 * cindex,
									theApp.LOCATION[seat][1]);
							theApp.getModel().add(
									theApp.cardPics[pindex][cindex]);
							cindex++;
						}
						for (int i = 0; i < theApp.getThisPlayer().getHand()
								.getNumUp(); i++) {
							Card card = theApp.getThisPlayer().getHand()
									.getUpCard(i);
							theApp.cardPics[pindex][cindex] = new Picture(
									card.img, theApp.LOCATION[seat][0] + 30
											* cindex, theApp.LOCATION[seat][1]);
							theApp.getModel().add(
									theApp.cardPics[pindex][cindex]);
							cindex++;
						}
					}
				}
				theApp.hideHoleCards = !theApp.hideHoleCards;
				theApp.getView().repaint();
			} else if (action.equals(new String("Muck Cards"))) {
				theApp.muckLosingHands = !theApp.muckLosingHands;
				int show = 1;
				if (theApp.muckLosingHands) {
					show = 0;
				}
				theApp.messageToServer("SHOWING CARDS &" + show);
			} else if (action.equals(new String("One Click"))) {
				theApp.oneClickCheckCall = !theApp.oneClickCheckCall;
			}
			theApp.getWindow().menuRedisplay();
		}
	}

	/***********************
	 * RulesAction class defines what happens when player wants to see rules
	 **/
	class RulesAction extends AbstractAction {

		// ----------------------
		// Constructor
		//
		RulesAction() {
			super();
		}

		// ----------------------
		// actionPerformed() is called when the menu item is selected
		//
		public void actionPerformed(ActionEvent e) {
			theApp.log("PokerMenuBar.RulesAction.actionPerformed()", 3);
			if (rulesWindow != null) {
				rulesWindow.dispose();
			}
			rulesWindow = null;
			rulesWindow = new RulesFrame("Table Rules...");
			Toolkit tk = rulesWindow.getToolkit();
			Dimension screensize = tk.getScreenSize();
			rulesWindow.setBounds(600, 10, 420, (25 + 20 * NUM_RULES));
			rulesWindow
					.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			rulesWindow.setVisible(true);
		}
	}

	/***********************
	 * CardBackAction class defines what happens when a new card back is
	 * selected.
	 **/
	class CardBackAction extends AbstractAction {

		String imgName = new String();

		// ----------------------
		// Constructor
		//
		CardBackAction(String s) {
			super();
			imgName = s;
		}

		// ----------------------
		// actionPerformed() is called when the menu item is selected
		//
		public void actionPerformed(ActionEvent e) {
			theApp.log("PokerMenuBar.CardBackAction.actionPerformed() - "
					+ imgName, 3);
			theApp.cardBackSelection = new ArrayList();
			for (int i = 0; i < theApp.getCardBackLabels().size(); i++) {
				if (imgName.equals((String) theApp.getCardBackLabels().get(i))) {
					theApp.cardBackSelection.add(new Boolean(true));
				} else {
					theApp.cardBackSelection.add(new Boolean(false));
				}
			}
			theApp.getWindow().menuRedisplay();
		}
	}

	/***********************
	 * TableImgAction class defines what happens when a new table image is
	 * selected
	 **/
	class TableImgAction extends AbstractAction {

		String imgName = new String();

		// ----------------------
		// Constructor
		//
		TableImgAction(String s) {
			super();
			imgName = s;
		}

		// ----------------------
		// actionPerformed() is called when the menu item is selected
		//
		public void actionPerformed(ActionEvent e) {
			theApp.log("PokerMenuBar.TableImgAction.actionPerformed() - "
					+ imgName, 3);
			theApp.tableImgSelection = new ArrayList();
			for (int i = 0; i < theApp.getTableImgLabels().size(); i++) {
				if (imgName.equals((String) theApp.getTableImgLabels().get(i))) {
					theApp.tableImgSelection.add(new Boolean(true));
				} else {
					theApp.tableImgSelection.add(new Boolean(false));
				}
			}
			theApp.getWindow().repaint();
			theApp.getWindow().menuRedisplay();
		}
	}

	/***********************
	 * HelpAction class defines what happens when the help menu item is selected
	 **/
	class HelpAction extends AbstractAction {

		// ----------------------
		// Constructor
		//
		HelpAction() {
			super();
		}

		// ----------------------
		// actionPerformed() is called when the menu item is selected
		//
		public void actionPerformed(ActionEvent e) {
			theApp.log("PokerMenuBar.HelpAction.actionPerformed()", 3);
			HelpFrame j = new HelpFrame("PokerApp Help");
			Toolkit tk = j.getToolkit();
			Dimension screensize = tk.getScreenSize();
			j.setBounds(240, 200, 330, 375);
			j.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			j.setVisible(true);
		}
	}

	/***********************
	 * JoinFrame class defines the window that is used to join a game.
	 **/
	class JoinFrame extends JDialog {

		JTextField textBox1, textBox2, textBox3;
		JButton submitButton;
		boolean applet;

		// ----------------------
		// Constructor
		//
		public JoinFrame(String title) {
			super(theApp.getWindow(), title);
			theApp.log("Constructing JoinFrame()", 3);
			getContentPane().setLayout(new BorderLayout());
			applet = theApp.isApplet;

			JPanel inputPanel = new JPanel();
			JPanel buttonPanel = new JPanel();

			textBox1 = new JTextField();
			textBox2 = new JTextField();
			textBox3 = new JTextField();
			submitButton = new JButton("Submit");
			submitButton.addActionListener(new submitAction());

			if (applet) {
				inputPanel.setLayout(new GridLayout(3, 2));
			} else {
				inputPanel.setLayout(new GridLayout(3, 2));
			}
			inputPanel.add(new JLabel("Your name : "));
			inputPanel.add(textBox1);

			if (!applet) {
				inputPanel.add(new JLabel("Host computer : "));
				inputPanel.add(textBox2);
				inputPanel.add(new JLabel("Port number : "));
				inputPanel.add(textBox3);
			}

			buttonPanel.setBorder(new EmptyBorder(3, 50, 3, 50));
			buttonPanel.add(submitButton);

			getContentPane().add(inputPanel, BorderLayout.CENTER);
			getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		}

		// ----------------------
		// submitAction class defines what happens when the submit button is
		// pressed.
		//
		class submitAction extends AbstractAction {

			// ..................
			// Constructor
			//
			submitAction() {
				super();
			}

			// ..................
			// actionPerformed() defines what happens when the button is
			// actually pressed
			//
			public void actionPerformed(ActionEvent e) {
				theApp.log(
						"PokerMenuBar.JoinFrame.submitAction.actionPerformed()",
						3);
				if (applet) {
					theApp.connectSocket(textBox1.getText(),
							theApp.getAppletServer(), theApp.getAppletPort());
				} else {
					theApp.connectSocket(textBox1.getText(),
							textBox2.getText(), textBox3.getText());
				}
				dispose();
			}
		}
	}

	/***********************
	 * RulesFrame class is the popup window that displays the game rules
	 **/
	class RulesFrame extends JFrame {

		// ----------------------
		// Constructor
		//
		public RulesFrame(String title) {
			super(title);
			theApp.log("Constructing RulesFrame()", 3);
			ClassLoader cl = getClass().getClassLoader();
			java.net.URL url = cl.getResource("Images/icon.gif");
			setIconImage(Toolkit.getDefaultToolkit().getImage(url));

			getContentPane().setLayout(new GridLayout(NUM_RULES, 1));

			String[] rulesText = new String[NUM_RULES];

			int ruleNum = 0;
			rulesText[ruleNum] = new String(" " + theApp.startingCash
					+ " buy in.");
			ruleNum++;

			if (theApp.antes) {
				rulesText[ruleNum] = new String(" " + theApp.ante + " ante.");
			}
			ruleNum++;

			if (theApp.blinds) {
				rulesText[ruleNum] = new String(" " + theApp.smallBlind
						+ " small blind.");
			}
			ruleNum++;

			if (theApp.noLimit) {
				rulesText[ruleNum] = new String(" No Limit Poker!");
			} else if (theApp.betLimit) {
				rulesText[ruleNum] = new String(" " + theApp.maximumBet
						+ " maximum raise poker.");
			} else if (theApp.potLimit) {
				rulesText[ruleNum] = new String(" Pot limit poker.");
			} else {
				PokerMoney d = new PokerMoney(theApp.minimumBet.amount() * 2.0f);
				rulesText[ruleNum] = new String(" " + theApp.minimumBet + "/"
						+ d
						+ " limit poker.  4 bets maximum per betting round.");
			}
			ruleNum++;

			if (theApp.kickPlayer) {
				rulesText[ruleNum] = new String(
						" Players who don't respond within "
								+ theApp.kickTimeout
								+ " seconds will be kicked out.");
				ruleNum++;
			}

			if (theApp.autoDealing) {
				StringBuffer txt = new StringBuffer(" The server is dealing");
				for (int k = 0; k < theApp.dealingGames.size(); k++) {
					String game = (String) theApp.dealingGames.get(k);
					int index = theApp.getGameClasses().indexOf(game);
					if (index != -1) {
						txt.append(" "
								+ (String) theApp.getGameLabels().get(index));
						if ((k < theApp.dealingGames.size() - 1)
								&& (theApp.dealingGames.size() > 2)) {
							txt.append(",");
						}
						if (k == theApp.dealingGames.size() - 2) {
							txt.append(" and");
						}
					}
				}
				rulesText[ruleNum] = new String(txt.toString());
				ruleNum++;
			}

			if (theApp.autoDoubleTime) {
				rulesText[ruleNum] = new String(
						" Antes/Blinds will be doubled every "
								+ (int) (theApp.timeToDouble / 60)
								+ " minutes.");
				ruleNum++;
			} else if (theApp.autoDoubleHands) {
				rulesText[ruleNum] = new String(
						" Antes/Blinds will be doubled every "
								+ theApp.handsToDouble + " hands.");
				ruleNum++;
			}

			for (int i = 0; i < NUM_RULES; i++) {
				if (rulesText[i] != null) {
					JLabel rule = new JLabel(rulesText[i]);
					getContentPane().add(rule);
				}
			}
		}
	}

	/***********************
	 * HelpFrame class is the window that displays the help
	 **/
	class HelpFrame extends JFrame {

		// ----------------------
		// Constructor
		//
		public HelpFrame(String title) {
			super(title);
			theApp.log("Constructing HelpFrame()", 3);
			ClassLoader cl = getClass().getClassLoader();
			java.net.URL url = cl.getResource("Images/icon.gif");
			setIconImage(Toolkit.getDefaultToolkit().getImage(url));
			getContentPane().setLayout(new BorderLayout());

			JPanel infoPanel = new JPanel();
			infoPanel.setLayout(new GridLayout(8, 1));
			infoPanel.add(new JLabel(" "));
			infoPanel.add(new JLabel("PokerApp", SwingConstants.CENTER));
			infoPanel.add(new JLabel("version 1.00", SwingConstants.CENTER));
			infoPanel.add(new JLabel(" "));
			infoPanel.add(new JLabel("by Dan Puperi", SwingConstants.CENTER));
			infoPanel.add(new JLabel(" "));
			infoPanel.add(new JLabel(" "));
			infoPanel.setBorder(new EtchedBorder());

			JButton link1 = new JButton("http://pokerapp.sourceforge.net");
			link1.setForeground(Color.BLUE);
			link1.setBorder(new EmptyBorder(1, 1, 1, 1));
			link1.addActionListener(new linkAction(link1.getText()));
			JButton link2 = new JButton(
					"http://pokerapp.sourceforge.net/docs.html");
			link2.setForeground(Color.BLUE);
			link2.setBorder(new EmptyBorder(1, 1, 1, 1));
			link2.addActionListener(new linkAction(link2.getText()));
			JButton link3 = new JButton(
					"http://pokerapp.sourceforge.net/help.html");
			link3.setForeground(Color.BLUE);
			link3.setBorder(new EmptyBorder(1, 1, 1, 1));
			link3.addActionListener(new linkAction(link3.getText()));
			JButton link4 = new JButton("http://pokerapp.proboards38.com");
			link4.setForeground(Color.BLUE);
			link4.setBorder(new EmptyBorder(1, 1, 1, 1));
			link4.addActionListener(new linkAction(link4.getText()));

			JPanel linkPanel = new JPanel();
			linkPanel.setLayout(new GridLayout(12, 1));
			linkPanel.add(new JLabel(" "));
			linkPanel.add(new JLabel("The PokerApp Home Page is located at:",
					SwingConstants.CENTER));
			linkPanel.add(link1);
			linkPanel.add(new JLabel(" "));
			linkPanel.add(new JLabel(
					"Complete PokerApp Documentation is available at:",
					SwingConstants.CENTER));
			linkPanel.add(link2);
			linkPanel.add(new JLabel(" "));
			linkPanel.add(new JLabel("Additional help can be found at:",
					SwingConstants.CENTER));
			linkPanel.add(link3);
			linkPanel.add(new JLabel(" "));
			linkPanel.add(new JLabel("The PokerApp Forum is located at:",
					SwingConstants.CENTER));
			linkPanel.add(link4);

			JButton okButton = new JButton("   OK   ");
			okButton.addActionListener(new okAction());
			okButton.setBorder(new BevelBorder(BevelBorder.RAISED));
			JPanel buttonPanel = new JPanel();
			buttonPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
			buttonPanel.add(okButton);

			getContentPane().add(infoPanel, BorderLayout.NORTH);
			getContentPane().add(linkPanel, BorderLayout.CENTER);
			getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		}

		// ----------------------
		// okAction class defines what happens when the OK button is pressed.
		//
		class okAction extends AbstractAction {

			// ..................
			// Constructor
			//
			okAction() {
				super();
			}

			// ..................
			// actionPerformed() defines what happens when the button is
			// actually pressed
			//
			public void actionPerformed(ActionEvent e) {
				theApp.log("PokerMenuBar.HelpFrame.okAction.actionPerformed()",
						3);
				dispose();
			}
		}

		// ----------------------
		// linkAction class
		//
		// Defines what happens when one of the links is pressed.
		//
		class linkAction extends AbstractAction {

			String link;

			// ..................
			// Constructor
			//
			linkAction(String l) {
				super();
				link = l;
			}

			// ..................
			// actionPerformed() defines what happens when the button is
			// actually pressed
			//
			public void actionPerformed(ActionEvent e) {
				theApp.log(
						"PokerMenuBar.HelpFrame.linkAction.actionPerformed() - "
								+ link, 3);
				String os = System.getProperty("os.name");
				if (os.startsWith("Windows")) {
					String cmd = new String(
							"rundll32 url.dll,FileProtocolHandler javascript:location.href='"
									+ link + "'");
					try {
						Process p = Runtime.getRuntime().exec(cmd);
					} catch (java.io.IOException x) {
					}
				}
			}
		}
	}
}
