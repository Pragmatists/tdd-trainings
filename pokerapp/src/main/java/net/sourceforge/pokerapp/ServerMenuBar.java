/******************************************************************************************
 * ServerMenuBar.java              PokerApp                                               *
 *                                                                                        *
 *   Revision History                                                                     *
 * +---------+----------+---------------------------------------------------------------+ *
 * | Version | DATE     | Description                                                   | *
 * +---------+----------+---------------------------------------------------------------+ *
 * |  0.95   | 09/15/04 | Initial documented release                                    | *
 * |  0.96   | 08/17/04 | Fixes to kick player frame.  Now should work properly when a  | *
 * |         |          | player is kicked.                                             | *
 * |  0.98   | 12/07/04 | Added bet limit and pot limit rules.                          | *
 * |  0.98   | 12/07/04 | Change the way auto-deal works.  Instead of the server menu   | *
 * |         |          | bar taking care of autodealing, a new Dealer class now does.  | *
 * |  0.98   | 12/07/04 | Took out random dealing.  Now the user selects all games he   | *
 * |         |          | wishes the dealer to choose between.                          | *
 * |  0.98   | 12/08/04 | Prevent possible divide by zero error.                        | *
 * |  0.98   | 12/12/04 | Change the JFrame icon.                                       | *
 * |  0.98   | 12/13/04 | Add logic to support new players paying the big blind         | *
 * |  0.98   | 12/14/04 | Fixed error with changing players bankroll.                   | *
 * |  0.99   | 03/25/05 | Added options to double the blinds based on time or number of | *
 * |         |          | hands played. Also fixed the blind double so that it would    | *
 * |         |          | double the minimum bet as well.                               | *
 * |  0.99   | 05/10/05 | Allowed the minumim bet to be doubled for all game types      | *
 * |  1.00   | 06/06/05 | Added logging.                                                | *
 * |  1.00   | 08/19/05 | Antes and Blinds are not mutually exclusive.  Game can have   | *
 * |         |          | both, either of them, or neither of them.                     | *
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

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.lang.reflect.Constructor;
import java.util.ArrayList;

/****************************************************
 * The ServerMenuBar class defines the menu bar for the ServerFrame class.
 * 
 * @author Dan Puperi
 * @version 1.00
 * 
 **/
public final class ServerMenuBar extends JMenuBar {

	private StartPoker theApp; // The StartPoker class to which this menu bar
								// belongs
	private JMenu menuGame; // The Game menu
	private JMenu menuAuto; // The Auto Start Games menu
	private JMenu menuAI; // The AI menu
	private JMenu menuRules; // The Rules menu
	private JMenu menuAdjust; // The Adjust menu
	private JMenuItem menuGameQuit; // The quit menu item
	private JMenuItem menuAIAdd; // Add an AI menu item
	private JMenuItem menuAdjustPlayerMoney; // Adjust money menu item
	private JMenuItem menuAdjustKickPlayer; // Menu item to kick a player out
	private JMenuItem menuRulesIncBlind; // Menu item to increase the blinds.
	private JMenuItem menuRulesIncAnte; // Menu item to increase the ante.
	private JMenuItem menuRulesIncMin; // Menu item to increase the minimum bet.
	private JMenuItem menuRulesIncMax; // Menu item to increase the maximum bet.
	private JCheckBoxMenuItem menuAutoAuto; // Menu item to autodeal games
	private JCheckBoxMenuItem menuRulesTimeout; // Menu item that sets players
												// will be kicked if they don't
												// respond
	private JCheckBoxMenuItem menuRulesAnte; // Menu item that sets antes
	private JCheckBoxMenuItem menuRulesBlinds; // Menu item that sets blinds
	private JCheckBoxMenuItem menuRulesStruct; // Menu item that sets structured
												// limit betting
	private JCheckBoxMenuItem menuRulesBetLim; // Menu item that sets bet limit
	private JCheckBoxMenuItem menuRulesNoLim; // Menu item that sets no limit
	private JCheckBoxMenuItem menuRulesPotLim; // Menu item that sets pot limit
	private JCheckBoxMenuItem menuRulesDoubleTime; // Menu item that sets to
													// double the blinds based
													// on time.
	private JCheckBoxMenuItem menuRulesDoubleHands; // Menu item that sets to
													// dobule the blinds based
													// on number of hands
													// played.
	private ArrayList menuAutoGames; // List of menu items containing games that
										// can be autostarted.

	/***********************
	 * Constructor creates a new ServerMenu Bar
	 **/
	public ServerMenuBar(StartPoker a) {
		super();
		theApp = a;
		theApp.log("Constructing SeverMenuBar", 3);
		menuAutoGames = new ArrayList();

		// Game menu - server can deal games or quit
		menuGame = new JMenu("Game");
		menuGame.setMnemonic('G');
		add(menuGame);

		// Check input file data for all possible game types to deal and add
		// them to the menu.
		for (int i = 0; i < theApp.getGameClasses().size(); i++) {
			JMenuItem menuItem = new JMenuItem("Start a game of "
					+ (String) theApp.getGameLabels().get(i));
			menuItem.addActionListener(new StartAction((String) theApp
					.getGameLabels().get(i)));
			menuGame.add(menuItem);
		}
		menuGame.addSeparator();

		// Option to quit the server
		menuGameQuit = new JMenuItem("Quit and Kill Server");
		menuGameQuit.addActionListener(new QuitAction());
		menuGame.add(menuGameQuit);

		// Auto-deal menu
		menuAuto = new JMenu("Autostart Games");
		menuAuto.setMnemonic('A');
		add(menuAuto);
		menuAutoAuto = new JCheckBoxMenuItem("Automatically deal games");
		menuAutoAuto.addActionListener(new AutoStartAction("AUTO"));
		menuAutoAuto.setSelected(theApp.autoDealing);
		menuAuto.add(menuAutoAuto);
		menuAuto.addSeparator();

		// Check input file data for all possible game types to auto-deal and
		// add them to the menu.
		for (int i = 0; i < theApp.getGameClasses().size(); i++) {
			menuAutoGames.add(new JCheckBoxMenuItem((String) theApp
					.getGameLabels().get(i)));
			((JCheckBoxMenuItem) menuAutoGames.get(i))
					.addActionListener(new AutoStartAction((String) theApp
							.getGameClasses().get(i)));
			boolean checked = false;
			for (int j = 0; j < theApp.dealingGames.size(); j++) {
				if (((String) theApp.getGameClasses().get(i))
						.equals((String) theApp.dealingGames.get(j))) {
					checked = true;
				}
			}
			((JCheckBoxMenuItem) menuAutoGames.get(i)).setSelected(checked);
			menuAuto.add((JCheckBoxMenuItem) menuAutoGames.get(i));
		}

		// Menu to select which rules to play with.
		menuRules = new JMenu("Rules");
		menuRules.setMnemonic('R');
		add(menuRules);
		menuRulesAnte = new JCheckBoxMenuItem("Ante");
		menuRulesAnte.addActionListener(new RulesAction("Ante"));
		menuRulesAnte.setSelected(theApp.antes);
		menuRules.add(menuRulesAnte);
		menuRulesBlinds = new JCheckBoxMenuItem("Blinds");
		menuRulesBlinds.addActionListener(new RulesAction("Blinds"));
		menuRulesBlinds.setSelected(theApp.blinds);
		menuRules.add(menuRulesBlinds);
		menuRulesIncAnte = new JMenuItem("Double Ante.  Currently="
				+ theApp.ante);
		menuRulesIncAnte.addActionListener(new RulesAction("DoubleAnte"));
		menuRulesIncAnte.setEnabled(theApp.antes);
		menuRules.add(menuRulesIncAnte);
		menuRulesIncBlind = new JMenuItem("Double Small Blind.  Currently="
				+ theApp.smallBlind);
		menuRulesIncBlind.addActionListener(new RulesAction("DoubleBlind"));
		menuRulesIncBlind.setEnabled(theApp.blinds);
		menuRules.add(menuRulesIncBlind);
		menuRulesDoubleTime = new JCheckBoxMenuItem(
				"Double Ante/Small Blind every " + theApp.timeToDouble
						+ " seconds.");
		menuRulesDoubleTime.addActionListener(new RulesAction("DoubleTime"));
		menuRulesDoubleTime.setSelected(theApp.autoDoubleTime);
		menuRules.add(menuRulesDoubleTime);
		menuRulesDoubleHands = new JCheckBoxMenuItem(
				"Double Ante/Small Blind every " + theApp.handsToDouble
						+ " hands.");
		menuRulesDoubleHands.addActionListener(new RulesAction("DoubleHands"));
		menuRulesDoubleHands.setSelected(theApp.autoDoubleHands);
		menuRules.add(menuRulesDoubleHands);
		menuRules.addSeparator();
		menuRulesStruct = new JCheckBoxMenuItem("Structured Betting");
		menuRulesStruct.addActionListener(new RulesAction("Struct"));
		menuRulesStruct.setSelected(!theApp.noLimit && !theApp.betLimit
				&& !theApp.potLimit);
		menuRules.add(menuRulesStruct);
		menuRulesBetLim = new JCheckBoxMenuItem("Bet Limit");
		menuRulesBetLim.addActionListener(new RulesAction("BetLim"));
		menuRulesBetLim.setSelected(theApp.betLimit);
		menuRules.add(menuRulesBetLim);
		menuRulesPotLim = new JCheckBoxMenuItem("Pot Limit");
		menuRulesPotLim.addActionListener(new RulesAction("PotLim"));
		menuRulesPotLim.setSelected(theApp.potLimit);
		menuRules.add(menuRulesPotLim);
		menuRulesNoLim = new JCheckBoxMenuItem("No Limit");
		menuRulesNoLim.addActionListener(new RulesAction("NoLim"));
		menuRulesNoLim.setSelected(theApp.noLimit);
		menuRules.add(menuRulesNoLim);
		menuRulesIncMin = new JMenuItem("Double Minimum Bet.  Currently="
				+ theApp.minimumBet);
		menuRulesIncMin.addActionListener(new RulesAction("DoubleMinimum"));
		menuRules.add(menuRulesIncMin);
		menuRulesIncMax = new JMenuItem("Double Maximum Bet.  Currently="
				+ theApp.maximumBet);
		menuRulesIncMax.addActionListener(new RulesAction("DoubleMaximum"));
		menuRulesIncMax.setEnabled(theApp.betLimit);
		menuRules.add(menuRulesIncMax);
		menuRules.addSeparator();
		menuRulesTimeout = new JCheckBoxMenuItem(
				"Kick a player who hasn't responded in " + theApp.kickTimeout
						+ " seconds.");
		menuRulesTimeout.addActionListener(new RulesAction("Timeout"));
		menuRulesTimeout.setSelected(theApp.kickPlayer);
		menuRules.add(menuRulesTimeout);

		// Menu to add an AI player to the game
		menuAI = new JMenu("AI Players");
		menuAI.setMnemonic('I');
		add(menuAI);
		menuAIAdd = new JMenuItem("Add AI Player");
		menuAIAdd.addActionListener(new AddAIAction());
		menuAI.add(menuAIAdd);

		// Adjust menu - can alter game parameters using this menu
		menuAdjust = new JMenu("Game adjustments");
		add(menuAdjust);

		// Change a players bottom line
		menuAdjustPlayerMoney = new JMenuItem("Change a player's bankroll");
		menuAdjustPlayerMoney.addActionListener(new AdjustMoneyAction());
		menuAdjust.add(menuAdjustPlayerMoney);

		// Kick a player out now.
		menuAdjustKickPlayer = new JMenuItem("Kick a player out");
		menuAdjustKickPlayer.addActionListener(new KickPlayerAction());
		menuAdjust.add(menuAdjustKickPlayer);
	}

	/***********************
	 * StartAction class defines what happen when the menu to start a new game
	 * is selected
	 **/
	class StartAction extends AbstractAction {

		String gameName = new String();

		// ----------------------
		// Constructor
		//
		StartAction(String l) {
			super();
			gameName = l;
		}

		// ----------------------
		// actionPerformed() is called when the menu item is selected
		//
		public void actionPerformed(ActionEvent e) {
			theApp.log("ServerMenuBar.StartAction.actionPerformed() - "
					+ gameName, 3);
			if (theApp.getGame() == null) {
				String gameTitle = new String();
				if (theApp.noLimit) {
					gameTitle = "No Limit " + gameName;
				} else if (theApp.betLimit) {
					gameTitle = "" + theApp.maximumBet + " limit " + gameName;
				} else if (theApp.potLimit) {
					gameTitle = "Pot limit " + gameName;
				} else {
					PokerMoney d = new PokerMoney(
							2.0f * theApp.minimumBet.amount());
					gameTitle = "" + theApp.minimumBet + "/" + d + " "
							+ gameName;
				}
				theApp.broadcastMessage("NEW GAME  &" + gameTitle);
				for (int i = 0; i < theApp.getGameLabels().size(); i++) {
					if (gameName.equals(theApp.getGameLabels().get(i))) {
						try {
							Class game_class = Class
									.forName("net.sourceforge.pokerapp.games."
											+ theApp.getGameClasses().get(i));
							Class arg_types[] = { Class.forName(theApp
									.getClass().getName()) };
							Constructor construct = game_class
									.getConstructor(arg_types);
							Object argList[] = { theApp };
							theApp.setGame((PokerGame) construct
									.newInstance(argList));
						} catch (Exception x) {
							theApp.log("Warning - Caught exception while trying to start game "
									+ gameTitle);
							theApp.logStackTrace(x);
						}
						if (theApp.getGame() != null)
							if (theApp.getGame().getGameError()) {
								theApp.nullifyGame();
							}
					}
				}
			} else {
				StartOverFrame check = new StartOverFrame("Start a new game?",
						gameName);
				check.setBounds(100, 100, 400, 80);
				check.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				check.setVisible(true);
			}
		}
	}

	/***********************
	 * AutoStartAction class defines what happens when one of the auto start
	 * games is selected
	 **/
	class AutoStartAction extends AbstractAction {

		String gameName = new String();

		// ----------------------
		// Constructor
		//
		AutoStartAction(String l) {
			super();
			gameName = l;
		}

		// ----------------------
		// actionPerformed() is called when the menu item is selected
		//
		public void actionPerformed(ActionEvent e) {
			theApp.log("ServerMenuBar.AutoStartAction.actionPerformed() - "
					+ gameName, 3);
			if (gameName.equals("AUTO")) {
				theApp.autoDealing = !theApp.autoDealing;
				if (theApp.autoDealing) {
					theApp.getServerDealer().startDealing();
				} else {
					theApp.getServerDealer().stopDealing();
				}
				theApp.updateRules();
				theApp.broadcastRules();
			} else {
				boolean prev = false;
				int index = -1;
				for (int i = 0; i < theApp.dealingGames.size(); i++) {
					if (gameName.equals((String) theApp.dealingGames.get(i))) {
						prev = true;
						index = i;
					}
				}

				if ((prev) && (index >= 0)) {
					theApp.dealingGames.remove(index);
					theApp.dealingGames.trimToSize();
				} else {
					theApp.dealingGames.add(gameName);
				}
			}
			theApp.getServerWindow().menuRedisplay();
		}
	}

	/***********************
	 * AddAIAction class defines what happens when Add AI is selected
	 **/
	class AddAIAction extends AbstractAction {

		// ----------------------
		// Constructor
		//
		AddAIAction() {
			super();
		}

		// ----------------------
		// actionPerformed()
		//
		// The actionPerformed() function is called when the menu item is
		// selected
		//
		public void actionPerformed(ActionEvent e) {
			theApp.log("ServerMenuBar.AddAIAction.actionPerformed()", 3);
			theApp.StartAIClient();
		}
	}

	/***********************
	 * RulesAction class defines what happens when one of the rules is changed.
	 **/
	class RulesAction extends AbstractAction {

		String actionString = new String();

		// ----------------------
		// Constructor
		//
		RulesAction(String s) {
			super();
			actionString = s;
		}

		// ----------------------
		// actionPerformed() is called when the menu item is selected
		//
		public void actionPerformed(ActionEvent e) {
			theApp.log("ServerMenuBar.RulesAction.actionPerformed() - "
					+ actionString, 3);
			if (theApp.inGame) {
				theApp.getServerWindow().sendMessage(
						"Cannot change the rules during a game.");
			} else {
				if (actionString.equals("Ante")) {
					theApp.antes = !theApp.antes;
				} else if (actionString.equals("Blinds")) {
					theApp.blinds = !theApp.blinds;
				} else if (actionString.equals("Struct")) {
					theApp.noLimit = false;
					theApp.betLimit = false;
					theApp.potLimit = false;
				} else if (actionString.equals("NoLim")) {
					theApp.noLimit = true;
					theApp.betLimit = false;
					theApp.potLimit = false;
				} else if (actionString.equals("BetLim")) {
					theApp.noLimit = false;
					theApp.betLimit = true;
					theApp.potLimit = false;
				} else if (actionString.equals("PotLim")) {
					theApp.noLimit = false;
					theApp.betLimit = false;
					theApp.potLimit = true;
				} else if (actionString.equals("DoubleTime")) {
					if (theApp.autoDoubleTime) {
						theApp.autoDoubleTime = false;
					} else {
						theApp.autoDoubleTime = true;
						theApp.autoDoubleHands = false;
						theApp.startingTime = System.currentTimeMillis() / 1000;
					}
				} else if (actionString.equals("DoubleHands")) {
					if (theApp.autoDoubleHands) {
						theApp.autoDoubleHands = false;
					} else {
						theApp.autoDoubleTime = false;
						theApp.autoDoubleHands = true;
						theApp.numHandsPlayed = 0;
					}
				} else if (actionString.equals("DoubleAnte")) {
					theApp.ante = new PokerMoney(2.0f * theApp.ante.amount());
				} else if (actionString.equals("DoubleBlind")) {
					theApp.smallBlind = new PokerMoney(
							2.0f * theApp.smallBlind.amount());
					theApp.minimumBet = new PokerMoney(
							2.0f * theApp.minimumBet.amount());
				} else if (actionString.equals("DoubleMinimum")) {
					theApp.minimumBet = new PokerMoney(
							2.0f * theApp.minimumBet.amount());
				} else if (actionString.equals("DoubleMaximum")) {
					theApp.maximumBet = new PokerMoney(
							2.0f * theApp.maximumBet.amount());
				} else if (actionString.equals("Timeout")) {
					theApp.kickPlayer = !theApp.kickPlayer;
				}

				theApp.updateRules();
				theApp.broadcastRules();
			}
			theApp.getServerWindow().menuRedisplay();
		}
	}

	/***********************
	 * AdjustMoneyAction class defines what happens when the adjust players
	 * bankrolls is selected
	 **/
	class AdjustMoneyAction extends AbstractAction {

		// ----------------------
		// Constructor
		//
		AdjustMoneyAction() {
			super();
		}

		// ----------------------
		// actionPerformed() is called when the menu item is selected
		//
		public void actionPerformed(ActionEvent e) {
			theApp.log("ServerMenuBar.AdjustMoneyAction.actionPerformed()", 3);
			AdjustCashFrame adjust = new AdjustCashFrame(
					"Change Players' Bankrolls");
			adjust.setBounds(200, 200, 200, 280);
			adjust.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			adjust.setVisible(true);
		}
	}

	/***********************
	 * KickPlayerAction class defines what happens when kick a player out is
	 * selected.
	 **/
	class KickPlayerAction extends AbstractAction {

		// ----------------------
		// Constructor
		//
		KickPlayerAction() {
			super();
		}

		// ----------------------
		// actionPerformed()
		//
		// The actionPerformed() function is called when the menu item is
		// selected
		//
		public void actionPerformed(ActionEvent e) {
			theApp.log("ServerMenuBar.KickPlayerAction.actionPerformed()", 3);
			KickPlayerFrame kick = new KickPlayerFrame("Kick a player out");
			kick.setBounds(200, 200, 170, 280);
			kick.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			kick.setVisible(true);
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
			theApp.log("ServerMenuBar.QuitAction.actionPerformed()", 3);
			theApp.KillServer();
		}
	}

	/***********************
	 * StartOverFrame class asks if you are sure to start a new game if one is
	 * in progress
	 **/
	class StartOverFrame extends JFrame {

		String gameName = new String();
		JButton yesBtn;
		JButton noBtn;

		// ----------------------
		// Constructor
		//
		StartOverFrame(String title, String l) {
			super(title);
			theApp.log("Constructing ServerMenuBar.StartOverFrame", 3);
			ClassLoader cl = getClass().getClassLoader();
			java.net.URL url = cl.getResource("Images/icon.gif");
			setIconImage(Toolkit.getDefaultToolkit().getImage(url));
			gameName = l;
			getContentPane().setLayout(new BorderLayout());
			getContentPane()
					.add(new JLabel(
							"Are you sure you want to evenly divide the pot and restart the game?"),
							BorderLayout.NORTH);
			JPanel yesnoPanel = new JPanel();
			yesnoPanel.setLayout(new GridLayout(1, 2, 8, 8));
			yesnoPanel.setBorder(new EmptyBorder(5, 15, 5, 15));
			yesBtn = new JButton("Yes");
			yesBtn.addActionListener(new yesAction());
			noBtn = new JButton("No");
			noBtn.addActionListener(new noAction());
			yesnoPanel.add(yesBtn);
			yesnoPanel.add(noBtn);
			getContentPane().add(yesnoPanel, BorderLayout.CENTER);
		}

		// ----------------------
		// yesAction class defines what happens when the yes button is selected
		//
		class yesAction extends AbstractAction {

			// ..................
			// Constructor
			//
			yesAction() {
				super();
			}

			// ..................
			// actionPerformed() defines what happens when the button is
			// actually pressed
			//
			public void actionPerformed(ActionEvent e) {
				theApp.log(
						"ServerMenuBar.StartOverFrame.yesAction.actionPerformed()",
						3);
				if (theApp.leavingPlayerList.size() > 0) {
					for (int i = 0; i < theApp.leavingPlayerList.size(); i++) {
						String name = (String) theApp.leavingPlayerList.get(i);
						theApp.dropPlayer(name);
						theApp.broadcastMessage("PLAYER REMOVE &" + name);
						theApp.getServerWindow().sendMessage(
								name + " left the table.");
					}
					theApp.leavingPlayerList = new ArrayList();
					theApp.playersRemoved = new ArrayList();
					theApp.updateMoneyLine(new PokerMoney(), new String());
					theApp.broadcastMessage("REFRESH");
				}
				int numin = 0;
				for (int i = 0; i < theApp.getPlayerList().size(); i++) {
					if (((Player) theApp.getPlayerList().get(i)).in) {
						numin++;
					}
				}
				float split = 0.0f;
				if (numin > 0) {
					split = theApp.getPot().amount() / (float) numin;
				}
				theApp.playedInLastGame = new ArrayList();
				for (int i = 0; i < theApp.getPlayerList().size(); i++) {
					theApp.playedInLastGame.add(((Player) theApp
							.getPlayerList().get(i)).getName());
					if (((Player) theApp.getPlayerList().get(i)).in) {
						((Player) theApp.getPlayerList().get(i)).add(split);
					}
				}
				String gameTitle = new String();
				if (theApp.noLimit) {
					gameTitle = "No Limit " + gameName;
				} else if (theApp.betLimit) {
					gameTitle = "" + theApp.maximumBet + " limit " + gameName;
				} else if (theApp.potLimit) {
					gameTitle = "Pot limit " + gameName;
				} else {
					PokerMoney d = new PokerMoney(
							2.0f * theApp.minimumBet.amount());
					gameTitle = "" + theApp.minimumBet + "/" + d + " "
							+ gameName;
				}
				theApp.broadcastMessage("NEW GAME  &" + gameTitle);
				for (int i = 0; i < theApp.getGameLabels().size(); i++) {
					if (gameName.equals(theApp.getGameLabels().get(i))) {
						try {
							Class game_class = Class
									.forName("net.sourceforge.pokerapp.games."
											+ theApp.getGameClasses().get(i));
							Class arg_types[] = { Class.forName(theApp
									.getClass().getName()) };
							Constructor construct = game_class
									.getConstructor(arg_types);
							Object argList[] = { theApp };
							theApp.setGame((PokerGame) construct
									.newInstance(argList));
						} catch (Exception x) {
							theApp.log("Warning : Caught exception while trying to start a new game");
							theApp.logStackTrace(x);
						}
						if (theApp.getGame() != null) {
							if (theApp.getGame().getGameError()) {
								theApp.nullifyGame();
							}
						}
					}
				}
				dispose();
			}
		}

		// ----------------------
		// noAction class defines what happens when the no button is selected
		//
		class noAction extends AbstractAction {

			// ..................
			// Constructor
			//
			noAction() {
				super();
			}

			// ..................
			// actionPerformed() defines what happens when the button is
			// actually pressed
			//
			public void actionPerformed(ActionEvent e) {
				theApp.log(
						"ServerMenuBar.StartOverFrame.noAction.actionPerformed()",
						3);
				dispose();
			}
		}
	}

	/***********************
	 * AdjustCashFrame class defines the window used to adjust players
	 * bankrolls.
	 **/
	class AdjustCashFrame extends JFrame {

		JTextField[] cashFields = new JTextField[PokerGame.MAX_PLAYERS];

		// ----------------------
		// Constructor
		//
		AdjustCashFrame(String title) {
			super(title);
			theApp.log("Constructing ServerMenuBar.AdjustCashFrame", 3);
			ClassLoader cl = getClass().getClassLoader();
			java.net.URL url = cl.getResource("Images/icon.gif");
			setIconImage(Toolkit.getDefaultToolkit().getImage(url));
			getContentPane().setLayout(new BorderLayout());
			getContentPane().add(new JLabel("Adjust Values and press OK"),
					BorderLayout.NORTH);
			JPanel adjustPanel = new JPanel();
			adjustPanel.setLayout(new GridLayout(8, 2, 4, 4));

			for (int i = 0; i < theApp.getPlayerList().size(); i++) {
				adjustPanel.add(new JLabel(((Player) theApp.getPlayerList()
						.get(i)).getName()));
				cashFields[i] = new JTextField(""
						+ ((Player) theApp.getPlayerList().get(i))
								.getBankroll().amount());
				adjustPanel.add(cashFields[i]);
			}

			for (int i = theApp.getPlayerList().size(); i < PokerGame.MAX_PLAYERS; i++) {
				adjustPanel.add(new JLabel(" "));
				cashFields[i] = new JTextField(" ");
				cashFields[i].setEnabled(false);
				adjustPanel.add(cashFields[i]);
			}

			getContentPane().add(adjustPanel, BorderLayout.CENTER);

			JButton okBtn = new JButton("OK");
			okBtn.addActionListener(new submitAction());
			getContentPane().add(okBtn, BorderLayout.SOUTH);
		}

		// ----------------------
		// submitAction class defines what happens when the submit button is
		// selected
		//
		class submitAction extends AbstractAction {

			// ..................
			// Constructor
			//
			submitAction() {
				super();
			}

			// ..................
			// actionPerformed()
			//
			// Defines what happens when the button is actually pressed
			//
			public void actionPerformed(ActionEvent e) {
				theApp.log(
						"ServerMenuBar.AdjustCashFrame.submitAction.actionPerformed()",
						3);
				float[] newValues = new float[PokerGame.MAX_PLAYERS];

				for (int i = 0; i < theApp.getPlayerList().size(); i++) {
					try {
						newValues[i] = (Float.valueOf(cashFields[i].getText()))
								.floatValue();
						float oldValue = ((Player) theApp.getPlayerList()
								.get(i)).getBankroll().amount();
						if (Math.abs(oldValue - newValues[i]) > 0.009) {
							((Player) theApp.getPlayerList().get(i))
									.setBankroll(0.0f);
							((Player) theApp.getPlayerList().get(i))
									.add(newValues[i]);
							theApp.messageToPlayer(((Player) theApp
									.getPlayerList().get(i)).getName(),
									"CHANGED BANKROLL &"
											+ new PokerMoney(oldValue)
											+ "&"
											+ ((Player) theApp.getPlayerList()
													.get(i)).getBankroll());
							theApp.broadcastMessage("PLAYER CASH &"
									+ ((Player) theApp.getPlayerList().get(i))
											.getName()
									+ "&"
									+ ((Player) theApp.getPlayerList().get(i))
											.getBankroll().amount());
						}
					} catch (NumberFormatException nfe) {
					}
				}
				if (theApp.getGame() != null) {
					theApp.updateMoneyLine(theApp.getGame().getCurrBet(),
							theApp.getGame().getHighBettorName());
				} else {
					theApp.updateMoneyLine(new PokerMoney(), new String());
				}
				dispose();
			}
		}
	}

	/***********************
	 * KickPlayerFrame class defines the window used to kick out a player
	 **/
	class KickPlayerFrame extends JFrame {

		JButton[] kickBtns = new JButton[PokerGame.MAX_PLAYERS];

		// ----------------------
		// Constructor
		//
		KickPlayerFrame(String title) {
			super(title);
			theApp.log("Constructing ServerMenuBar.KickPlayerFrame", 3);
			ClassLoader cl = getClass().getClassLoader();
			java.net.URL url = cl.getResource("Images/icon.gif");
			setIconImage(Toolkit.getDefaultToolkit().getImage(url));
			getContentPane().setLayout(new BorderLayout());
			getContentPane().add(new JLabel("Click on a player to kick"),
					BorderLayout.NORTH);
			JPanel kickPanel = new JPanel();
			kickPanel.setLayout(new GridLayout(8, 1, 4, 4));
			kickPanel.setBorder(new EmptyBorder(5, 10, 5, 10));

			for (int i = 0; i < theApp.getPlayerList().size(); i++) {
				kickBtns[i] = new JButton(((Player) theApp.getPlayerList().get(
						i)).getName());
				kickBtns[i].addActionListener(new kickAction(i));
				kickPanel.add(kickBtns[i]);
			}
			for (int i = theApp.getPlayerList().size(); i < PokerGame.MAX_PLAYERS; i++) {
				kickPanel.add(new JButton(" "));
			}
			getContentPane().add(kickPanel, BorderLayout.CENTER);
		}

		// ----------------------
		// kickAction class defines what happens when a button is pressed
		//
		class kickAction extends AbstractAction {

			private int id;

			// ..................
			// Constructor
			//
			kickAction(int a) {
				super();
				id = a;
			}

			// ..................
			// actionPerformed() defines what happens when the button is
			// actually pressed
			//
			public void actionPerformed(ActionEvent e) {
				theApp.log(
						"ServerMenuBar.KickPlayerFrame.kickAction.actionPerformed() - "
								+ id, 3);
				Player player = (Player) theApp.getPlayerList().get(id);
				theApp.messageToPlayer(player.getName(), "KICKED");
				dispose();
			}
		}
	}
}
