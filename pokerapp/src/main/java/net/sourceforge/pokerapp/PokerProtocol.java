/******************************************************************************************
 * PokerProtocol.java              PokerApp                                               *
 *                                                                                        *
 *   Revision History                                                                     *
 * +---------+----------+---------------------------------------------------------------+ *
 * | Version | DATE     | Description                                                   | *
 * +---------+----------+---------------------------------------------------------------+ *
 * |  0.95   | 09/15/04 | Initial documented release                                    | *
 * |  0.96   | 09/16/04 | Fixed issue with last raise for limit games.                  | *
 * |  0.96   | 09/16/04 | Fixed issue with limit bet for blind games                    | *
 * |  0.96   | 09/17/04 | Removed the re-request for data when server sends a bad       | *
 * |         |          | dealer.  This was causing more harm than good.                | *
 * |  0.96   | 09/20/04 | Added a call to PokerMultiServerThread.stopRunning() when the | *
 * |         |          | player disconnects from the server                            | *
 * |  0.96   | 09/20/04 | Changed calls to addPerson() to match the changes done in     | *
 * |         |          | StartPoker.                                                   | *
 * |  0.96   | 09/20/04 | Added a new command to the clients - KICKED - to let the      | *
 * |         |          | player know when the have been forcibly removed from the      | *
 * |         |          | server - and they don't just get to get up from the table.    | *
 * |  0.97   | 10/25/04 | Added encryption / decryption routines so that data flowing   | *
 * |         |          | across the newtwork is more secure and less obvious.          | *
 * |  0.97   | 10/26/04 | Pass each players bankroll data across so that each client    | *
 * |         |          | knows how much money everyone has.                            | *
 * |  0.97   | 11/06/04 | Null pointer for an AI with a card was dealt up               | *
 * |  0.97   | 11/09/04 | Added client messages to deal with draw games                 | *
 * |  0.98   | 12/06/04 | Check for null before accessing the server window             | *
 * |  0.98   | 12/07/04 | Removed restart command - probably was causing an error       | *
 * |  0.98   | 12/08/04 | Made updates to accomodate bet limit and pot limit games.     | *
 * |  0.98   | 12/08/04 | Fixed an problem after somebody folded and there was nobody   | *
 * |         |          | left at the table.                                            | *
 * |  0.98   | 12/10/04 | Change disconnectSocket calls to include boolean argument     | *
 * |  0.98   | 12/13/04 | Removed code to find new dealer when dealer leaves.  The      | *
 * |         |          | timerAction in StartPoker should be taking care of this.      | *
 * |  0.98   | 12/14/04 | Add a message so server can tell clients which game it is     | *
 * |         |          | auto-dealing.                                                 | *
 * |  0.98   | 12/14/04 | Redisplay when player's bankroll is changed.                  | *
 * |  0.98   | 12/14/04 | Added boolean argument to displayError calls.                 | *
 * |  0.99   | 03/30/05 | Set PokerApp.turnToBet variable as appropriate.               | *
 * |  0.99   | 03/30/05 | If somebody wins because everybody else folded, it still      | *
 * |         |          | looked like their turn to bet.                                | *
 * |  0.99   | 05/16/05 | Remove players from side pots when they fold.                 | *
 * |  0.99   | 05/17/05 | Handle side pot messages from the server.                     | *
 * |  0.99   | 05/17/05 | For structure betting games, the submit button is no longer   | *
 * |         |          | required to be pressed for the action to happen.              | *
 * |  0.99   | 05/17/05 | Set the PokerApp.inGame flag as required.                     | *
 * |  0.99   | 05/17/05 | Fixed error encoding lowercase z                              | *
 * |  0.99   | 05/24/05 | Make the card placements not interfere with showing names     | *
 * |  1.00   | 06/06/05 | Added logging.                                                | *
 * |  1.00   | 08/19/05 | Fixed minor error when player tried to turn on muck cards     | *
 * |         |          | before the server had the player in the game.                 | *
 * |  1.00   | 08/26/05 | Removed redundant "sat down at the table" message.            | *
 * |  1.00   | 08/29/05 | Cleanup code for PLAYER CALL CLEAR - should not look up name  | *
 * |         |          | if name is CLEAR.                                             | *
 * |  1.00   | 08/30/05 | When person first joins server, display any cards that might  | *
 * |         |          | be in play.                                                   | *
 * |  1.00   | 09/07/05 | When playing structured betting game, one-click call button   | *
 * |         |          | submitted the bet twice - removed that from happening here.   | *
 * |  1.00   | 10/14/05 | Found null pointer error                                      | *
 * |  1.00   | 10/25/05 | Move checking registration logic when a new player joins      | *
 * |         |          | the game - moved to PokerMultiServerThread                    | *
 * |  1.00   | 07/13/07 | Prepare for open source.  Header/comments/package/etc...      | *
 * |  1.00   | 08/02/07 | Not sure what I was thinking on 09/07/05 - put that logic     | *
 * |         |          | back in so that player could make a "call" bet with the       | *
 * |         |          | oneClickCheckCall disabled.                                   | *
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
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

/****************************************************
 * PokerProtocol describes how the client and the servers talk to each other. It
 * contains functions to process information to the server and to the clients.
 * 
 * @author Dan Puperi
 * @version 1.00
 * 
 **/
public class PokerProtocol {

	private StartPoker theStartApp; // If this is a server, the StartPoker class
	private PokerApp theApp; // If this is a human client, the PokerApp class
	private AIApp theAIApp; // If this is an AI client, the AIApp class
	private static String[] strings = { "P0kerAPp", "ThisGAMeRulZ",
			"teXAshOLDem", "anaCONDa", "MOsQuitO", "hidetheSALami",
			"BeAUmaHAholDEm", "JaKesORbeTTer", "HichICagoSHIpWreck",
			"Ir0NXwilDCEnter" };

	/***********************
	 * Constructor that is used to create a the protocol for a server
	 * 
	 * @param a
	 *            The StartPoker class that started this protocol for the
	 *            server.
	 * 
	 **/
	public PokerProtocol(StartPoker a) {
		theStartApp = a;
		theStartApp.log("Constructing PokerProtocol for StartPoker", 3);
		theApp = null;
		theAIApp = null;
	}

	/***********************
	 * Constructor that is used to create a the protocol for a human player
	 * 
	 * @param a
	 *            The PokerApp class that started this protocol
	 * 
	 **/
	public PokerProtocol(PokerApp a) {
		theApp = a;
		theApp.log("Constructing PokerProtocol for PokerApp", 3);
		theStartApp = null;
		theAIApp = null;
	}

	/***********************
	 * Constructor that is used to create a the protocol for an AI player
	 * 
	 * @param a
	 *            The AIApp class that started this protocol
	 * 
	 **/
	public PokerProtocol(AIApp a) {
		theAIApp = a;
		theAIApp.log("Constructing PokerProtocol for AIApp", 3);
		theApp = null;
		theStartApp = null;
	}

	/***********************
	 * processServerInput() handles the messages from the clients to the server
	 * 
	 * @param name
	 *            The player from whom this message came
	 * @param in
	 *            The message to the server
	 * @return The server response string.
	 * 
	 **/
	public String processServerInput(String name, String in) {
		// System.out.println( "Server Input -> " + in + " <- from " + name );
		if (theStartApp != null) {
			theStartApp.log("Server Input -> " + in + " <- from " + name, 2);
		}

		// COMMUNICATE will display a message on all players talk windows
		//
		if (in.startsWith(new String("COMMUNICATE"))) {
			int i = in.indexOf('&');
			String m = new String(in.substring(i + 1, in.length()));
			broadcastMessage("COMMUNICATE  &" + name + "&" + m);
			return "nothing";
		}

		// REQUEST DATA is used by the client to get all the server data
		//
		if (in.startsWith(new String("REQUEST DATA"))) {
			if (theStartApp != null) {
				for (int j = 0; j < theStartApp.getPlayerList().size(); j++) {
					theStartApp.messageToPlayer(name,
							"PLAYER ADD &"
									+ ((Player) theStartApp.getPlayerList()
											.get(j)).getName()
									+ "&"
									+ ((Player) theStartApp.getPlayerList()
											.get(j)).seat
									+ "&"
									+ ((Player) theStartApp.getPlayerList()
											.get(j)).getBankroll().amount());
				}

				theStartApp.messageToPlayer(name, "RESET GAMES");
				theStartApp.messageToPlayer(name, "RESET RULES");
				for (int k = 0; k < theStartApp.getRuleNames().size(); k++) {
					theStartApp.messageToPlayer(name, "RULE ADD &"
							+ theStartApp.getRuleNames().get(k) + "="
							+ theStartApp.getRuleValues().get(k));
				}

				for (int k = 0; k < theStartApp.getGameLabels().size(); k++) {
					theStartApp.messageToPlayer(name, "GAME ADD &"
							+ theStartApp.getGameLabels().get(k) + "&"
							+ theStartApp.getGameClasses().get(k));
				}
				if (theStartApp.autoDealing) {
					for (int k = 0; k < theStartApp.dealingGames.size(); k++) {
						theStartApp.messageToPlayer(name, "DEALING GAME &"
								+ theStartApp.dealingGames.get(k));
					}
				}

				if (theStartApp.dealer != null) {
					theStartApp.messageToPlayer(name, "DEALER  &"
							+ theStartApp.dealer.getName());
				}
				//
				// Show all the cards currently on the table
				//
				if (theStartApp.getGame() != null) {
					int inPlayerIndex = -1;
					for (int j = 0; j < theStartApp.getPlayerList().size(); j++) {
						int numCards = 0;
						if (((Player) theStartApp.getPlayerList().get(j)).in) {
							if (inPlayerIndex == -1) {
								int numShared = 0;
								for (int c = 0; c < ((Player) theStartApp
										.getPlayerList().get(j)).getHand()
										.getNumShared(); c++) {
									Card card = ((Player) theStartApp
											.getPlayerList().get(j)).getHand()
											.getSharedCard(c);
									if (((Object) theStartApp.getGame())
											.getClass().getName()
											.indexOf("IronCross") > 0) {
										theStartApp.messageToPlayer(name,
												"CARD ICSHARED  &Table&"
														+ numShared + "&"
														+ card);
									} else {
										theStartApp.messageToPlayer(name,
												"CARD SHARED  &Table&"
														+ numShared + "&"
														+ card);
									}
									numShared++;
								}
								inPlayerIndex = j;
							}
							for (int c = 0; c < ((Player) theStartApp
									.getPlayerList().get(j)).getHand()
									.getNumHole(); c++) {
								theStartApp
										.messageToPlayer(
												name,
												"CARD HOLE  &"
														+ ((Player) theStartApp
																.getPlayerList()
																.get(j))
																.getName()
														+ "&" + numCards);
								numCards++;
							}
							for (int c = 0; c < ((Player) theStartApp
									.getPlayerList().get(j)).getHand()
									.getNumUp(); c++) {
								Card card = ((Player) theStartApp
										.getPlayerList().get(j)).getHand()
										.getUpCard(c);
								theStartApp.messageToPlayer(name, "CARD UP  &"
										+ ((Player) theStartApp.getPlayerList()
												.get(j)).getName() + "&"
										+ numCards + "&" + card);
								numCards++;
							}
						}
					}
				}
				theStartApp.messageToPlayer(name, "REFRESH");
			}
			return "nothing";
		}

		// LEFT MOUSE is sent to the server when a player presses the left mouse
		// button in the window
		// The button click is used for some games to pick cards or whatever
		//
		if (in.startsWith(new String("LEFT MOUSE"))) {
			int i = in.indexOf('&');
			int j = in.indexOf('&', i + 1);
			int k = in.indexOf('&', j + 1);

			String n = in.substring(i + 1, j);
			int x = Integer.parseInt(in.substring(j + 1, k));
			int y = Integer.parseInt(in.substring(k + 1, in.length()));
			if (theStartApp.getGame() != null) {
				theStartApp.getGame().mouseClick(n, x, y);
			}

			return "nothing";
		}

		// NEW PLAYER is used to add a new player to the game - the seat and
		// whether they want to show cards on
		// hands they lose is sent as data.
		//
		if (in.startsWith(new String("NEW PLAYER"))) {
			int i = in.indexOf('&');
			int j = in.indexOf('&', i + 1);

			Player P = new Player(name);
			P.add(theStartApp.startingCash.amount());
			int seat = Integer.parseInt(in.substring(i + 1, j));
			int show = Integer.parseInt(in.substring(j + 1, in.length()));
			P.seat = seat;
			theStartApp.waitingPlayerList.add(P);
			if (show == 0) {
				theStartApp.showingLostCards[seat] = false;
			} else {
				theStartApp.showingLostCards[seat] = true;
			}

			broadcastMessage("PLAYER ADD NEW &" + P.getName() + "&" + seat
					+ "&" + theStartApp.startingCash.amount());
			theStartApp.updateMoneyLine(new PokerMoney(), new String());
			return (P.getName() + " sat down at the table.");
		}

		// SHOWING CARDS is used to indicate whether the player wants to show
		// their cards from losing hands.
		//
		if (in.startsWith(new String("SHOWING CARDS"))) {
			int i = in.indexOf('&');
			int show = Integer.parseInt(in.substring(i + 1, in.length()));
			int seat = theStartApp.seatIndex(name);
			if ((show == 0) && (seat >= 0) && (seat < PokerGame.MAX_PLAYERS)) {
				theStartApp.showingLostCards[seat] = false;
			} else if ((seat >= 0) && (seat < PokerGame.MAX_PLAYERS)) {
				theStartApp.showingLostCards[seat] = true;
			}
			return "nothing";
		}

		// LEAVE is used to let the server know that the player is leaving
		//
		if (in.startsWith(new String("LEAVE"))) {
			if (theStartApp.playerIndex(name) >= 0) {
				if (theStartApp.serverThreadFromPlayerIndex(theStartApp
						.playerIndex(name)) >= 0) {
					((PokerMultiServerThread) theStartApp.getServerThreadList()
							.get(theStartApp
									.serverThreadFromPlayerIndex(theStartApp
											.playerIndex(name)))).stopRunning();
					theStartApp.getServerThreadList().remove(
							theStartApp.serverThreadFromPlayerIndex(theStartApp
									.playerIndex(name)));
					theStartApp.getServerThreadList().trimToSize();
				}
				if ((Player) theStartApp.getPlayerList().get(
						theStartApp.playerIndex(name)) != null) {
					((Player) theStartApp.getPlayerList().get(
							theStartApp.playerIndex(name))).in = false;
					((Player) theStartApp.getPlayerList().get(
							theStartApp.playerIndex(name))).potOK = true;
				}
				theStartApp.leavingPlayerList.add(name);
				broadcastMessage("PLAYER NOPICS &" + name);
				broadcastMessage("REFRESH");
			} else {
				for (int i = 0; i < theStartApp.getServerThreadList().size(); i++) {
					if (name.equals(((PokerMultiServerThread) theStartApp
							.getServerThreadList().get(i)).getPlayerName())) {
						((PokerMultiServerThread) theStartApp
								.getServerThreadList().get(i)).stopRunning();
						theStartApp.getServerThreadList().remove(i);
						theStartApp.getServerThreadList().trimToSize();
					}
				}
			}
			return "nothing";
		}

		// For the rest of commands, a player who is in the game must have sent
		// the command
		//
		int index = theStartApp.playerIndex(name);
		Player P = null;
		if (index != -1) {
			P = (Player) theStartApp.getPlayerList().get(index);
		}

		if (P == null) {
			theStartApp
					.log("ERROR : could not locate player with name " + name);
			theStartApp.log("        From " + StartPoker.getCallerClassName()
					+ "." + StartPoker.getCallerMethodName());
			theStartApp.log("        exiting");
			theStartApp.closeLogFile();
			System.exit(1);
		}

		// STAND UP lets the server know that the player has stood up and left
		// the table, but is still connected.
		//
		if (in.startsWith(new String("STAND UP"))) {
			((Player) theStartApp.getPlayerList().get(
					theStartApp.playerIndex(name))).in = false;
			((Player) theStartApp.getPlayerList().get(
					theStartApp.playerIndex(name))).potOK = true;
			theStartApp.leavingPlayerList.add(name);
			broadcastMessage("PLAYER NOPICS &" + name);
		}

		// NEW GAME lets the server know that a player is dealing a new game
		//
		if (in.startsWith(new String("NEW GAME"))) {
			int j = in.indexOf('&');
			String gameName = new String(in.substring(j + 1, in.length()));
			if (theStartApp.getGame() == null) {
				String gameTitle = new String();
				if (theStartApp.noLimit) {
					gameTitle = "No Limit " + gameName;
				} else if (theStartApp.betLimit) {
					gameTitle = "" + theStartApp.maximumBet + " limit "
							+ gameName;
				} else if (theStartApp.potLimit) {
					gameTitle = "Pot limit " + gameName;
				} else {
					PokerMoney d = new PokerMoney(
							2.0f * theStartApp.minimumBet.amount());
					gameTitle = "" + theStartApp.minimumBet + "/" + d + " "
							+ gameName;
				}
				broadcastMessage("NEW GAME  &" + gameTitle);

				for (int i = 0; i < theStartApp.getGameLabels().size(); i++) {
					if (gameName.equals(theStartApp.getGameLabels().get(i))) {
						try {
							Class game_class = Class
									.forName("net.sourceforge.pokerapp.games."
											+ theStartApp.getGameClasses().get(
													i));
							Class arg_types[] = { Class.forName(theStartApp
									.getClass().getName()) };
							Constructor construct = game_class
									.getConstructor(arg_types);
							Object argList[] = { theStartApp };
							theStartApp.setGame((PokerGame) construct
									.newInstance(argList));
						} catch (Exception x) {
							theStartApp
									.log("Warning : Caught exception while trying to start new game.");
							theStartApp.logStackTrace(x);
						}
					}
				}
				if (theStartApp.getGame() != null) {
					if (theStartApp.getGame().getGameError()) {
						theStartApp.nullifyGame();
					}
				}
			}
		}

		// BET is sent to the server when a player presses one of their betting
		// buttons.
		// The button pressed will also be part of the command.
		//
		else if (in.startsWith(new String("BET"))) {
			if (theStartApp.getGame() != null) {
				if (P.getName().equals(
						((Player) theStartApp.getPlayerList().get(
								theStartApp.getGame().currPlayerIndex))
								.getName())) {

					// BET SUBMIT is sent to the server when the player pressed
					// the submit bet button
					//
					if (in.startsWith(new String("SUBMIT"), 4)) {
						theStartApp.getGame().bet();
						theStartApp.timeCounter = 0;

						// BET CALL is sent when the player presses the call
						// button
						//
					} else if (in.startsWith(new String("CALL"), 4)) {
						float needToMatch = theStartApp.getGame().getCurrBet()
								.amount()
								- P.getPrevBet().amount();
						if (needToMatch > P.getBankroll().amount()) {
							P.setBet(P.getBankroll().amount());
							P.allin = true;
						} else {
							P.setBet(needToMatch);
						}
						theStartApp.getPlayerList().set(
								theStartApp.getGame().currPlayerIndex, P);
						theStartApp.updateMoneyLine(theStartApp.getGame()
								.getCurrBet(), theStartApp.getGame().highBettor
								.getName());
						if (!(theStartApp.noLimit || theStartApp.betLimit || theStartApp.potLimit)) {
							theStartApp.getGame().bet();
							theStartApp.timeCounter = 0;
						}

						// BET CHECK is sent when the player presses the check
						// button
						//
					} else if (in.startsWith(new String("CHECK"), 4)) {
						P.setBet(0.00f);
						theStartApp.getPlayerList().set(
								theStartApp.getGame().currPlayerIndex, P);
						theStartApp.updateMoneyLine(theStartApp.getGame()
								.getCurrBet(), theStartApp.getGame().highBettor
								.getName());
						if (!(theStartApp.noLimit || theStartApp.betLimit || theStartApp.potLimit)) {
							theStartApp.getGame().bet();
							theStartApp.timeCounter = 0;
						}

						// BET ONE is sent when the player presses the lowest
						// betting denomination button
						//
					} else if (in.startsWith(new String("ONE"), 4)) {
						P.addBet(theStartApp.button1Val.amount());
						float needToMatch = theStartApp.getGame().getCurrBet()
								.amount()
								- P.getPrevBet().amount();
						if (theStartApp.betLimit) {
							if (P.getBet().amount() > (needToMatch + theStartApp.maximumBet
									.amount())) {
								P.setBet(needToMatch
										+ theStartApp.maximumBet.amount());
							}
						} else if (theStartApp.potLimit) {
							float maxRaise = theStartApp.getPot().amount()
									+ needToMatch;
							if (P.getBet().amount() > (needToMatch + maxRaise)) {
								P.setBet(needToMatch + maxRaise);
							}
						}
						theStartApp.getPlayerList().set(
								theStartApp.getGame().currPlayerIndex, P);
						theStartApp.updateMoneyLine(theStartApp.getGame()
								.getCurrBet(), theStartApp.getGame().highBettor
								.getName());

						// BET TWO is sent when the player presses the second
						// lowest betting denomination button
						//
					} else if (in.startsWith(new String("TWO"), 4)) {
						P.addBet(theStartApp.button2Val.amount());
						float needToMatch = theStartApp.getGame().getCurrBet()
								.amount()
								- P.getPrevBet().amount();
						if (theStartApp.betLimit) {
							if (P.getBet().amount() > (needToMatch + theStartApp.maximumBet
									.amount())) {
								P.setBet(needToMatch
										+ theStartApp.maximumBet.amount());
							}
						} else if (theStartApp.potLimit) {
							float maxRaise = theStartApp.getPot().amount()
									+ needToMatch;
							if (P.getBet().amount() > (needToMatch + maxRaise)) {
								P.setBet(needToMatch + maxRaise);
							}
						}
						theStartApp.getPlayerList().set(
								theStartApp.getGame().currPlayerIndex, P);
						theStartApp.updateMoneyLine(theStartApp.getGame()
								.getCurrBet(), theStartApp.getGame().highBettor
								.getName());

						// BET THREE is sent when the player presses the second
						// highest betting denomination button
						//
					} else if (in.startsWith(new String("THREE"), 4)) {
						P.addBet(theStartApp.button3Val.amount());
						float needToMatch = theStartApp.getGame().getCurrBet()
								.amount()
								- P.getPrevBet().amount();
						if (theStartApp.betLimit) {
							if (P.getBet().amount() > (needToMatch + theStartApp.maximumBet
									.amount())) {
								P.setBet(needToMatch
										+ theStartApp.maximumBet.amount());
							}
						} else if (theStartApp.potLimit) {
							float maxRaise = theStartApp.getPot().amount()
									+ needToMatch;
							if (P.getBet().amount() > (needToMatch + maxRaise)) {
								P.setBet(needToMatch + maxRaise);
							}
						}
						theStartApp.getPlayerList().set(
								theStartApp.getGame().currPlayerIndex, P);
						theStartApp.updateMoneyLine(theStartApp.getGame()
								.getCurrBet(), theStartApp.getGame().highBettor
								.getName());

						// BET FOUR is sent when the player presses the highest
						// betting denomination (or bet) button
						//
					} else if (in.startsWith(new String("FOUR"), 4)) {
						if (theStartApp.noLimit || theStartApp.betLimit
								|| theStartApp.potLimit) {
							P.addBet(theStartApp.button4Val.amount());
							float needToMatch = theStartApp.getGame()
									.getCurrBet().amount()
									- P.getPrevBet().amount();
							if (theStartApp.betLimit) {
								if (P.getBet().amount() > (needToMatch + theStartApp.maximumBet
										.amount())) {
									P.setBet(needToMatch
											+ theStartApp.maximumBet.amount());
								}
							} else if (theStartApp.potLimit) {
								float maxRaise = theStartApp.getPot().amount()
										+ needToMatch;
								if (P.getBet().amount() > (needToMatch + maxRaise)) {
									P.setBet(needToMatch + maxRaise);
								}
							}
						} else {
							if (theStartApp.getGame().getActionNum() > (int) (theStartApp
									.getGame().getMaxActionNum() / 2)) {
								P.setBet(2.0f * theStartApp.minimumBet.amount());
							} else if ((theStartApp.getGame().getActionNum() == 1)
									&& (theStartApp.blinds)) {
								P.setBet(theStartApp.minimumBet.amount()
										- P.getPrevBet().amount());
							} else {
								P.setBet(theStartApp.minimumBet.amount());
							}
						}
						theStartApp.getPlayerList().set(
								theStartApp.getGame().currPlayerIndex, P);
						theStartApp.updateMoneyLine(theStartApp.getGame()
								.getCurrBet(), theStartApp.getGame().highBettor
								.getName());
						if (!(theStartApp.noLimit || theStartApp.betLimit || theStartApp.potLimit)) {
							theStartApp.getGame().bet();
							theStartApp.timeCounter = 0;
						}

						// BET ALLIN is sent when the player presses the al in
						// (or raise) button
						//
					} else if (in.startsWith(new String("ALLIN"), 4)) {
						if (theStartApp.noLimit) {
							P.setBet(P.getBankroll().amount());
						} else if (theStartApp.betLimit) {
							float needToMatch = theStartApp.getGame()
									.getCurrBet().amount()
									- P.getPrevBet().amount();
							P.setBet(needToMatch
									+ theStartApp.maximumBet.amount());
						} else if (theStartApp.potLimit) {
							float needToMatch = theStartApp.getGame()
									.getCurrBet().amount()
									- P.getPrevBet().amount();
							float maxRaise = theStartApp.getPot().amount()
									+ needToMatch;
							P.setBet(needToMatch + maxRaise);
						} else {
							if (theStartApp.getGame().getActionNum() > (int) (theStartApp
									.getGame().getMaxActionNum() / 2)) {
								if (theStartApp.getGame().getCurrBet().amount() >= 8.0f * theStartApp.minimumBet
										.amount()) {
									P.setBet(theStartApp.getGame().getCurrBet()
											.amount()
											- P.getPrevBet().amount());
								} else {
									if (theStartApp.getGame().getCurrBet()
											.amount() == 0.0) {
										P.setBet(2.0f * theStartApp.minimumBet
												.amount());
									} else {
										P.setBet(theStartApp.getGame()
												.getCurrBet().amount()
												+ 2.0f
												* theStartApp.minimumBet
														.amount()
												- P.getPrevBet().amount());
									}
								}
							} else {
								if (theStartApp.getGame().getCurrBet().amount() >= 4.0f * theStartApp.minimumBet
										.amount()) {
									P.setBet(theStartApp.getGame().getCurrBet()
											.amount()
											- P.getPrevBet().amount());
								} else {
									if (theStartApp.getGame().getCurrBet()
											.amount() == 0.0) {
										P.setBet(theStartApp.minimumBet
												.amount());
									} else {
										P.setBet(theStartApp.getGame()
												.getCurrBet().amount()
												+ theStartApp.minimumBet
														.amount()
												- P.getPrevBet().amount());
									}
								}
							}
						}
						theStartApp.getPlayerList().set(
								theStartApp.getGame().currPlayerIndex, P);
						theStartApp.updateMoneyLine(theStartApp.getGame()
								.getCurrBet(), theStartApp.getGame().highBettor
								.getName());
						if (!(theStartApp.noLimit || theStartApp.betLimit || theStartApp.potLimit)) {
							theStartApp.getGame().bet();
							theStartApp.timeCounter = 0;
						}

						// BET FOLD is sent when the player presses the fold
						// button
						//
					} else if (in.startsWith(new String("FOLD"), 4)) {
						P.setBet(0.0f);
						theStartApp.timeCounter = 0;
						P.in = false;
						P.allin = false;
						P.potOK = true;
						theStartApp.getGame().removeFromSidePots(P.getName());
						broadcastMessage("PLAYER NOPICS &" + P.getName());
						broadcastMessage("PLAYER CALL  &" + P.getName()
								+ "&Fold&" + P.getBankroll().amount());
						int prevSeat = P.seat;
						theStartApp.getPlayerList().set(
								theStartApp.getGame().currPlayerIndex, P);
						boolean foundNext = false;
						int attempts = 0;
						while (!foundNext) {
							attempts++;
							prevSeat = theStartApp.nextSeat(prevSeat);
							boolean waitingToLeave = false;
							for (int j = 0; j < theStartApp.leavingPlayerList
									.size(); j++) {
								if (((String) theStartApp.leavingPlayerList
										.get(j)).equals(((Player) theStartApp
										.getPlayerList()
										.get(theStartApp
												.getPlayerInSeat(prevSeat)))
										.getName())) {
									waitingToLeave = true;
								}
							}
							if ((((Player) theStartApp.getPlayerList().get(
									theStartApp.getPlayerInSeat(prevSeat))).in)
									&& (!waitingToLeave)
									&& !(((Player) theStartApp.getPlayerList()
											.get(theStartApp
													.getPlayerInSeat(prevSeat))).allin)) {
								foundNext = true;
								theStartApp.getGame().currPlayerIndex = theStartApp
										.getPlayerInSeat(prevSeat);
								theStartApp.getGame().currSeatIndex = prevSeat;
								if (P.getName().equals(
										theStartApp.getGame().highBettor
												.getName())) {
									theStartApp.getGame().highBettor = (Player) theStartApp
											.getPlayerList()
											.get(theStartApp.getGame().currPlayerIndex);
								}
							}
							if (attempts > PokerGame.MAX_PLAYERS) {
								foundNext = true;
							}
						}
						int lastIn = 0;
						int numIn = 0;
						for (int j = 0; j < theStartApp.getPlayerList().size(); j++) {
							if (((Player) theStartApp.getPlayerList().get(j)).in) {
								numIn++;
								lastIn = j;
							}
						}
						if (numIn == 0) {
							broadcastMessage("MESSAGE  &Everybody left. The poker server gets to keep the pot.");
							theStartApp.resetPot();
							theStartApp.updateMoneyLine(new PokerMoney(0.0f),
									"  ");
							theStartApp.nullifyGame();
							theStartApp.needNewDealer = false;
							theStartApp.dealer = null;
							theStartApp.lastDealer = null;
							return ("Everybody left. The poker server gets to keep the pot.");
						} else if (numIn == 1) {
							broadcastMessage("MESSAGE  &"
									+ P.getName()
									+ " folded.  Take it down, "
									+ ((Player) theStartApp.getPlayerList()
											.get(lastIn)).getName()
									+ ".  Pot = " + theStartApp.getPot());
							((Player) theStartApp.getPlayerList().get(lastIn))
									.add(theStartApp.getPot().amount());
							PokerMoney m = new PokerMoney(theStartApp.getPot()
									.amount());
							broadcastMessage("PLAYER CASH &"
									+ ((Player) theStartApp.getPlayerList()
											.get(lastIn)).getName()
									+ "&"
									+ ((Player) theStartApp.getPlayerList()
											.get(lastIn)).getBankroll()
											.amount());
							theStartApp.playedInLastGame = new ArrayList();
							for (int i = 0; i < theStartApp.getPlayerList()
									.size(); i++) {
								theStartApp.playedInLastGame
										.add(((Player) theStartApp
												.getPlayerList().get(i))
												.getName());
							}
							broadcastMessage("DISABLE BUTTONS");
							theStartApp.resetPot();
							theStartApp.updateMoneyLine(new PokerMoney(0.0f),
									"  ");
							theStartApp.nullifyGame();
							theStartApp.nextDealer();
							return ("Take it down, "
									+ ((Player) theStartApp.getPlayerList()
											.get(lastIn)).getName()
									+ ".  Pot = " + m);
						} else {
							broadcastMessage("MESSAGE  &"
									+ P.getName()
									+ " folded.  "
									+ ((Player) theStartApp
											.getPlayerList()
											.get(theStartApp.getGame().currPlayerIndex))
											.getName() + "'s bet.");
							if (!(theStartApp.noLimit || theStartApp.potLimit || theStartApp.betLimit)
									&& (theStartApp.getGame().getCurrBet()
											.amount() > 0.009f)) {
								broadcastMessage("PLAYER TURN &"
										+ ((Player) theStartApp
												.getPlayerList()
												.get(theStartApp.getGame().currPlayerIndex))
												.getName() + "&enable raise");
							} else {
								broadcastMessage("PLAYER TURN &"
										+ ((Player) theStartApp
												.getPlayerList()
												.get(theStartApp.getGame().currPlayerIndex))
												.getName());
							}
							theStartApp.updateMoneyLine(
									theStartApp.getGame().currBet,
									theStartApp.getGame().highBettor.getName());
							boolean potOK = true;
							int allins = 0, ins = 0;
							for (int j = 0; j < theStartApp.getPlayerList()
									.size(); j++) {
								if ((((Player) (theStartApp.getPlayerList()
										.get(j))).in)
										&& (!((Player) (theStartApp
												.getPlayerList().get(j))).potOK)
										&& (!((Player) (theStartApp
												.getPlayerList().get(j))).allin)) {
									potOK = false;
								}
								if (((Player) (theStartApp.getPlayerList()
										.get(j))).allin) {
									allins++;
								}
								if (((Player) (theStartApp.getPlayerList()
										.get(j))).in) {
									ins++;
								}
							}

							if (allins >= (ins - 1)) {
								if (potOK) {
									if (theStartApp.getGame() != null) {
										while (theStartApp.getGame()
												.getActionNum() < theStartApp
												.getGame().getMaxActionNum()) {
											broadcastMessage("PLAYER CALL &CLEAR& ");
											theStartApp.getGame().nextAction();
										}
									}
								}
							} else {
								if (potOK) {
									broadcastMessage("PLAYER CALL &CLEAR& ");
									theStartApp.getGame().nextAction();
								}
							}
						}
					}
				}
			}
		}
		return "nothing";
	}

	/***********************
	 * processClientInput() handles the messages from the server to the clients.
	 **/
	public String processClientInput(String in) {
		if (theApp != null) {
			// System.out.println( theApp.getThisPlayer().getName() +
			// " input -> " + in );
			theApp.log(theApp.getThisPlayer().getName() + " input -> " + in, 2);
		}
		if (theAIApp != null) {
			// System.out.println( theAIApp.getThisPlayer().getName() +
			// " input -> " + in );
			theAIApp.log(
					theAIApp.getThisPlayer().getName() + " input -> " + in, 2);
		}

		// SERVER KILLED is sent to the players is the server was terminated
		//
		if (in.startsWith(new String("SERVER KILLED"))) {
			if (theApp != null) {
				theApp.disconnectSocket(true);
			}
			if (theAIApp != null) {
				theAIApp.disconnectSocket();
			}
			return ("Server was killed.");

			// NAME CHANGED is sent to a player is he cannot use the name he
			// requested because it was taken.
			// The new name that the player must use will be sent as data.
			//
		} else if (in.startsWith(new String("NAME CHANGED"))) {
			int i = in.indexOf('&');
			String newName = new String(in.substring(i + 1, in.length()));
			if (theApp != null) {
				theApp.getThisPlayer().setName(newName);
			}
			if (theAIApp != null) {
				theAIApp.getThisPlayer().setName(newName);
			}

			// COMMUNICATE is sent when one of the players uses the talk window
			//
		} else if ((in.startsWith(new String("COMMUNICATE")))
				&& (theApp != null)) {
			int i = in.indexOf('&');
			int j = in.indexOf('&', i + 1);
			String pname = new String(in.substring(i + 1, j));
			String m = new String(in.substring(j + 1, in.length()));
			if (theApp.communicator != null) {
				theApp.communicator.sendMessage("{ " + pname + " } " + m);
			}

			// MESSAGE is used to send a message from the server and display it
			// on the players main window.
			//
		} else if (in.startsWith(new String("MESSAGE"))) {
			int i = in.indexOf('&');
			return in.substring(i + 1, in.length());

			// DISABLE BUTTONS is used to let the player know that his betting
			// buttons should be disabled
			//
		} else if (in.startsWith(new String("DISABLE BUTTONS"))) {
			if (theApp != null) {
				theApp.turnToBet = false;
				theApp.getWindow().menuRedisplay();
				theApp.getWindow().buttonRedisplay();
				theApp.getWindow().disableButtons();
			}

			// KICKED lets the player know that the server kicked them out.
			//
		} else if (in.startsWith(new String("KICKED"))) {
			if (theApp != null) {
				theApp.disconnectSocket(true);
			}
			if (theAIApp != null) {
				theAIApp.disconnectSocket();
			}
			return ("You were kicked off the server.");

			// REFRESH is used to let the player know to refresh his display
			//
		} else if (in.startsWith(new String("REFRESH"))) {
			if (theAIApp != null) {
				theAIApp.getAIAction().messageToAI("AI REFRESH");

			}
			if (theApp != null) {
				for (int a = 0; a < theApp.getPlayerList().size(); a++) {
					theApp.getWindow().setPlayerText(a,
							((Player) theApp.getPlayerList().get(a)).getName());
				}
				theApp.getWindow().buttonRedisplay();
				theApp.getWindow().menuRedisplay();
			}

			// DEALER is used to send the dealer from the server to the clients.
			//
		} else if (in.startsWith(new String("DEALER"))) {
			int i = in.indexOf('&');
			String dname = in.substring(i + 1, in.length());

			if (theApp != null) {
				theApp.dealerIndex = theApp.playerIndex(dname);
				theApp.getWindow().menuRedisplay();
			}
			if (theAIApp != null) {
				theAIApp.dealerIndex = theAIApp.playerIndex(dname);
			}

			// DEALING game lets the clients know which games the server is
			// dealing.
			//
		} else if (in.startsWith(new String("DEALING GAME"))) {
			int i = in.indexOf('&');
			String gamename = in.substring(i + 1, in.length());

			if (theApp != null) {
				theApp.dealingGames.add(gamename);
			}

			// PLAYER is a prefix used to send many commands to the clients
			//
		} else if (in.startsWith(new String("PLAYER"))) {
			int i = in.indexOf('&');

			// PLAYER ADD is used to add a player to the game
			//
			if (in.startsWith(new String("ADD"), 7)) {
				int j = in.indexOf('&', i + 1);
				int k = in.indexOf('&', j + 1);

				// PLAYER ADD NEW lets the client know that a new player is
				// coming to the game
				//
				if (in.startsWith(new String("NEW"), 11)) {
					String pname = in.substring(i + 1, j);
					int seat = Integer.parseInt(in.substring(j + 1, k));
					float cash = Float.parseFloat(in.substring(k + 1,
							in.length()));
					if (theApp != null) {
						for (int ind = 0; ind < theApp.getPlayerList().size(); ind++) {
							if (pname.equals(((Player) theApp.getPlayerList()
									.get(ind)).getName())) {
								theApp.dropPlayer(pname);
							}
						}
						theApp.getPlayerList().add(new Player(pname));
						((Player) theApp.getPlayerList().get(
								theApp.playerIndex(pname))).seat = seat;
						((Player) theApp.getPlayerList().get(
								theApp.playerIndex(pname))).setBankroll(cash);
						theApp.getWindow().setPlayerText(
								theApp.playerIndex(pname),
								pname + " - waiting to join");
						theApp.getWindow().menuRedisplay();
					}
					if (theAIApp != null) {
						for (int ind = 0; ind < theAIApp.getPlayerList().size(); ind++) {
							if (pname.equals(((Player) theAIApp.getPlayerList()
									.get(ind)).getName())) {
								theAIApp.dropPlayer(pname);
							}
						}
						theAIApp.getPlayerList().add(new Player(pname));
						((Player) theAIApp.getPlayerList().get(
								theAIApp.playerIndex(pname))).seat = seat;
						((Player) theAIApp.getPlayerList().get(
								theAIApp.playerIndex(pname))).setBankroll(cash);
					}
					return (pname + " sat down at the table.");
				} else {
					String name = in.substring(i + 1, j);
					int seat = Integer.parseInt(in.substring(j + 1, k));
					float cash = Float.parseFloat(in.substring(k + 1,
							in.length()));
					if (theApp != null) {
						theApp.getPlayerList().add(new Player(name));
						((Player) theApp.getPlayerList().get(
								theApp.playerIndex(name))).seat = seat;
						((Player) theApp.getPlayerList().get(
								theApp.playerIndex(name))).setBankroll(cash);
					}
					if (theAIApp != null) {
						theAIApp.getPlayerList().add(new Player(name));
						((Player) theAIApp.getPlayerList().get(
								theAIApp.playerIndex(name))).seat = seat;
						((Player) theAIApp.getPlayerList().get(
								theAIApp.playerIndex(name))).setBankroll(cash);
					}
				}

				// PLAYER REMOVE is used to let the client know that a player is
				// leaving the game
				//
			} else if (in.startsWith(new String("REMOVE"), 7)) {
				String name = in.substring(i + 1, in.length());
				if (theApp != null) {
					if (theApp.getThisPlayer().getName().equals(name)) {
						theApp.getThisPlayer().seat = -9;
						theApp.resetTable();
						theApp.resetPlayerList();
						theApp.messageToServer("REQUEST DATA");
					} else {
						if (theApp.dealerIndex > theApp.playerIndex(name)) {
							theApp.dealerIndex = theApp.dealerIndex - 1;
						}
						theApp.clearPlayerPics(name);
						theApp.dropPlayer(name);
						for (int a = 0; a < theApp.getPlayerList().size(); a++) {
							if (theApp.getWindow().getPlayerText(a)
									.indexOf("waiting to join") == -1) {
								theApp.getWindow()
										.setPlayerText(
												a,
												((Player) theApp
														.getPlayerList().get(a))
														.getName());
							}
						}
					}
					theApp.getWindow().menuRedisplay();
				}
				if (theAIApp != null) {
					if (theAIApp.getThisPlayer().getName().equals(name)) {
						theAIApp.disconnectSocket();
					} else {
						if (theAIApp.dealerIndex > theAIApp.playerIndex(name)) {
							theAIApp.dealerIndex = theAIApp.dealerIndex - 1;
						}
						theAIApp.dropPlayer(name);
						return (name + " left the table.");
					}
				}

				// PLAYER NOPICS lets the client know to not display the
				// pictures for the specified player
				//
			} else if ((in.startsWith(new String("NOPICS"), 7))
					&& (theApp != null)) {
				theApp.clearPlayerPics(in.substring(i + 1, in.length()));
				if (theApp.getThisPlayer().getName()
						.equals(in.substring(i + 1, in.length()))) {
					theApp.getThisPlayer().in = false;
				}
				theApp.getWindow().menuRedisplay();

				// PLAYER CLEAR HAND lets the client know that his hand is being
				// cleared out. Presumably he will
				// be assigned a new hand.
				//
			} else if (in.startsWith(new String("CLEAR HAND"), 7)) {
				if (theApp != null) {
					theApp.clearPlayerPics(in.substring(i + 1, in.length()));
					theApp.getThisPlayer().getHand().clearHand();
					theApp.selectedCards = new boolean[10];
				}
				if (theAIApp != null) {
					theAIApp.getThisPlayer().getHand().clearHand();
				}

				// PLAYER TURN lets the client know whose turn it is
				//
			} else if (in.startsWith(new String("TURN"), 7)) {
				if (theApp != null) {
					int j = in.indexOf('&', i + 1);
					if (j > 0) {
						theApp.structuredRaiseEnable = true;
					} else {
						theApp.structuredRaiseEnable = false;
						j = in.length();
					}

					if (theApp.getThisPlayer().getName()
							.equals(in.substring(i + 1, j))) {
						theApp.turnToBet = true;
						theApp.getWindow().menuRedisplay();
						theApp.getWindow().buttonRedisplay();
						theApp.getWindow().enableButtons();
					} else {
						theApp.turnToBet = false;
						theApp.getWindow().menuRedisplay();
						theApp.getWindow().buttonRedisplay();
						theApp.getWindow().disableButtons();
					}
				}
				if (theAIApp != null) {
					int j = in.indexOf('&', i + 1);
					if (j <= 0) {
						j = in.length();
					}
					if (theAIApp.getThisPlayer().getName()
							.equals(in.substring(i + 1, j))) {
						theAIApp.getAIAction().messageToAI("AI TURN");
					}
				}

				// PLAYER CASH lets all the players know how much money a player
				// has
				//
			} else if (in.startsWith(new String("CASH"), 7)) {
				int j = in.indexOf('&', i + 1);
				String name = in.substring(i + 1, j);
				float cash = Float.parseFloat(in.substring(j + 1, in.length()));
				int pindex = 0;
				if (theApp != null) {
					pindex = theApp.playerIndex(name);
					((Player) theApp.getPlayerList().get(pindex))
							.setBankroll(cash);
					theApp.getWindow().menuRedisplay();
				}
				if (theAIApp != null) {
					pindex = theAIApp.playerIndex(name);
					((Player) theAIApp.getPlayerList().get(pindex))
							.setBankroll(cash);
				}

				// PLAYER CALL lets all the players know what action a player
				// just took
				// PLAYER CALL CLEAR will remove the previous action description
				//
			} else if (in.startsWith(new String("CALL"), 7)) {
				int j = in.indexOf('&', i + 1);
				String name = in.substring(i + 1, j);
				int pindex = 0;

				if (theApp != null) {
					for (int k = 0; k < theApp.getPlayerList().size(); k++) {
						if ((name.equals(new String("CLEAR")))
								&& (theApp.getWindow().getPlayerText(k)
										.indexOf("waiting to join") == -1)
								&& (theApp.getPlayerList().get(k) != null)) {
							theApp.getWindow().setPlayerText(
									k,
									((Player) theApp.getPlayerList().get(k))
											.getName());
						}
					}
				}
				if (!name.equals(new String("CLEAR"))) {
					if (theApp != null) {
						pindex = theApp.playerIndex(name);
					}
					if (theAIApp != null) {
						pindex = theAIApp.playerIndex(name);
					}

					int k = in.indexOf('&', j + 1);
					String action = in.substring(j + 1, k);
					float cash = Float.parseFloat(in.substring(k + 1,
							in.length()));
					if ((theApp != null) && (pindex >= 0)) {
						theApp.getWindow().setPlayerText(
								pindex,
								""
										+ ((Player) theApp.getPlayerList().get(
												pindex)).getName() + " - "
										+ action);
						((Player) theApp.getPlayerList().get(pindex))
								.setBankroll(cash);
					}
					if ((theAIApp != null) && (pindex >= 0)) {
						if (!name.equals(theAIApp.getThisPlayer().getName())) {
							theAIApp.getAIAction().messageToAI(
									"ACTION &" + name + "&" + action);
						}
						((Player) theAIApp.getPlayerList().get(pindex))
								.setBankroll(cash);
					}
				}
				if (theApp != null) {
					theApp.getWindow().menuRedisplay();
				}
			}
			if (theApp != null) {
				theApp.getView().repaint();
			}

			// NEW GAME is used to let the client know that a new game is about
			// to start
			//
		} else if (in.startsWith(new String("NEW GAME"))) {
			int i = in.indexOf('&');
			if (theApp != null) {
				theApp.resetTable();
				theApp.inGame = true;
				theApp.getThisPlayer().newGame();
				theApp.getWindow().setTitle(in.substring(i + 1, in.length()));
				theApp.getWindow().enableButtons();
			}
			if (theAIApp != null) {
				theAIApp.resetTable();
				theAIApp.getThisPlayer().newGame();
				theAIApp.getWindow().setTitle(in.substring(i + 1, in.length()));
				theAIApp.getAIAction().messageToAI("AI NEW GAME");
			}

			// END GAME is used to let the client know that the game is ending
			//
		} else if (in.startsWith(new String("END GAME"))) {
			if (theApp != null) {
				theApp.inGame = false;
				theApp.getWindow().menuRedisplay();
			}

			// RESET GAMES is used to erase the clients game list
			//
		} else if (in.startsWith(new String("RESET GAMES"))) {
			if (theApp != null) {
				theApp.resetGameLabels();
				theApp.resetGameClasses();
			}
			if (theAIApp != null) {
				theAIApp.resetGameLabels();
				theAIApp.resetGameClasses();
			}

			// RESET RULES is used to erase the clients rules list
			//
		} else if (in.startsWith(new String("RESET RULES"))) {
			if (theApp != null) {
				theApp.resetRuleNames();
				theApp.resetRuleValues();
				theApp.dealingGames = new ArrayList();
			}
			if (theAIApp != null) {
				theAIApp.resetRuleNames();
				theAIApp.resetRuleValues();
			}

			// GAME ADD is used to add a game to the clients game list
			//
		} else if (in.startsWith(new String("GAME ADD"))) {
			int i = in.indexOf('&');
			int j = in.indexOf('&', i + 1);
			if (theApp != null) {
				theApp.getGameLabels().add(in.substring(i + 1, j));
				theApp.getGameClasses().add(in.substring(j + 1, in.length()));
			}
			if (theAIApp != null) {
				theAIApp.getGameLabels().add(in.substring(i + 1, j));
				theAIApp.getGameClasses().add(in.substring(j + 1, in.length()));
			}

			// RULE ADD is used to add a rule to the clients rule list
			//
		} else if (in.startsWith(new String("RULE ADD"))) {
			int i = in.indexOf('&');
			int j = in.indexOf('=', i + 1);
			if (theApp != null) {
				theApp.getRuleNames().add(in.substring(i + 1, j));
				theApp.getRuleValues().add(in.substring(j + 1, in.length()));
				theApp.parseRules();
			}
			if (theAIApp != null) {
				theAIApp.getRuleNames().add(in.substring(i + 1, j));
				theAIApp.getRuleValues().add(in.substring(j + 1, in.length()));
				theAIApp.parseRules();
			}

			// MONEYLINE is used to cause the client to update the clients data
			// on its display
			//
		} else if (in.startsWith(new String("MONEYLINE"))) {
			int i = in.indexOf('&');
			int j = in.indexOf('&', i + 1);
			int k = in.indexOf('&', j + 1);
			int l = in.indexOf('&', k + 1);
			int m = in.indexOf('&', l + 1);
			PokerMoney br = new PokerMoney(Float.parseFloat(in.substring(i + 1,
					j)));
			PokerMoney pbet = new PokerMoney(Float.parseFloat(in.substring(
					j + 1, k)));
			PokerMoney p = new PokerMoney(Float.parseFloat(in.substring(k + 1,
					l)));
			PokerMoney b = new PokerMoney(Float.parseFloat(in.substring(l + 1,
					m)));
			String n = in.substring(m + 1, in.length());

			if (theApp != null) {
				theApp.getWindow().updateMoneyLine(br, pbet, p, b, n);
			}

			if (theAIApp != null) {
				theAIApp.getWindow().updateMoneyLine(br, pbet, p, b, n);
			}

			// CHANGED BANKROLL is used to alert player that the server changed
			// their cash
			//
		} else if ((in.startsWith(new String("CHANGED BANKROLL")))
				&& (theApp != null)) {
			int i = in.indexOf('&');
			int j = in.indexOf('&', i + 1);
			String before = in.substring(i + 1, j);
			String after = in.substring(j + 1, in.length());
			theApp.displayError("Warning - Bankroll changed",
					"Alert: The server changed your bankroll from " + before
							+ " to " + after, false);

			// CARD is used to add a card to a players hand
			//
		} else if (in.startsWith(new String("CARD"))) {
			int pindex, cindex;
			int i = in.indexOf('&');
			int j = in.indexOf('&', i + 1);
			int k = in.indexOf('&', j + 1);
			String name = in.substring(i + 1, j);
			Card card = null;
			if (k > 0) {
				card = new Card(in.substring(k + 1, in.length()));
				cindex = Integer.parseInt(in.substring(j + 1, k));
			} else {
				cindex = Integer.parseInt(in.substring(j + 1, in.length()));
			}

			// CARD HOLE is used to add a card to the players down cards
			//
			if (in.startsWith(new String("HOLE"), 5)) {
				if (theApp != null) {
					pindex = theApp.playerIndex(name);
					int seat = theApp.seatIndex(name);
					if ((name.equals(theApp.getThisPlayer().getName()))
							&& (card != null) && (pindex >= 0) && (seat >= 0)) {
						theApp.getThisPlayer().getHand().addHoleCard(card);
						if (!theApp.hideHoleCards) {
							theApp.cardPics[pindex][cindex] = new Picture(
									card.img, theApp.LOCATION[seat][0] + 30
											* cindex, theApp.LOCATION[seat][1]);
						} else {
							theApp.cardPics[pindex][cindex] = new Picture(
									theApp.getView().getCardBackImage(),
									theApp.LOCATION[seat][0] + 30 * cindex,
									theApp.LOCATION[seat][1]);
						}
						theApp.getModel().add(theApp.cardPics[pindex][cindex]);
					} else if (!name.equals(theApp.getThisPlayer().getName())) {
						theApp.cardPics[pindex][cindex] = new Picture(theApp
								.getView().getCardBackImage(),
								theApp.LOCATION[seat][0] + 30 * cindex,
								theApp.LOCATION[seat][1]);
						theApp.getModel().add(theApp.cardPics[pindex][cindex]);
					}
				}
				if ((theAIApp != null)
						&& (name.equals(theAIApp.getThisPlayer().getName()))
						&& (card != null)) {
					theAIApp.getThisPlayer().getHand().addHoleCard(card);
				}

				// CARD UP is used to add to a players up cards
				//
			} else if (in.startsWith(new String("UP"), 5)) {
				if (theApp != null) {
					pindex = theApp.playerIndex(name);
					int seat = theApp.seatIndex(name);
					if ((pindex >= 0) && (seat >= 0)) {
						if (name.equals(theApp.getThisPlayer().getName())) {
							theApp.getThisPlayer().getHand().addUpCard(card);
						}
						theApp.cardPics[pindex][cindex] = new Picture(card.img,
								theApp.LOCATION[seat][0] + 30 * cindex,
								theApp.LOCATION[seat][1]);
						theApp.getModel().add(theApp.cardPics[pindex][cindex]);
					}
				}
				if ((theAIApp != null)
						&& (name.equals(theAIApp.getThisPlayer().getName()))) {
					theAIApp.getThisPlayer().getHand().addHoleCard(card);
				}

				// CARD SHARED is used to add a shared card to the table
				//
			} else if (in.startsWith(new String("SHARED"), 5)) {
				if (theApp != null) {
					theApp.getThisPlayer().getHand().addSharedCard(card);
					theApp.getModel().add(
							new Picture(card.img, 340 + 30 * cindex, 170));
				}
				if (theAIApp != null) {
					theAIApp.getThisPlayer().getHand().addSharedCard(card);
				}

				// CARD ICSHARED is used to add a shared cared to the table in a
				// cross pattern
				//
			} else if (in.startsWith(new String("ICSHARED"), 5)) {
				if (theApp != null) {
					theApp.getThisPlayer().getHand().addSharedCard(card);
					switch (cindex) {
					case 0:
						theApp.getModel().add(new Picture(card.img, 370, 140));
						break;
					case 1:
						theApp.getModel().add(new Picture(card.img, 440, 170));
						break;
					case 2:
						theApp.getModel().add(new Picture(card.img, 370, 200));
						break;
					case 3:
						theApp.getModel().add(new Picture(card.img, 300, 170));
						break;
					case 4:
						theApp.getModel().add(new Picture(card.img, 370, 170));
						break;
					}
					theApp.getView().repaint();
				}
				if (theAIApp != null) {
					theAIApp.getThisPlayer().getHand().addSharedCard(card);
				}
			}

			// SHOW is used to show a players hand
			//
		} else if ((in.startsWith(new String("SHOW"))) && (theApp != null)) {
			if (in.startsWith(new String("CARD"), 5)) {
				int pindex, cindex;
				int i = in.indexOf('&');
				int j = in.indexOf('&', i + 1);
				int k = in.indexOf('&', j + 1);
				String name = in.substring(i + 1, j);
				Card card = null;
				if (k > 0) {
					card = new Card(in.substring(k + 1, in.length()));
					cindex = Integer.parseInt(in.substring(j + 1, k));
				} else {
					cindex = Integer.parseInt(in.substring(j + 1, in.length()));
				}
				pindex = theApp.playerIndex(name);
				int seat = theApp.seatIndex(name);

				if ((pindex >= 0) && (seat >= 0)) {
					theApp.getModel().remove(theApp.cardPics[pindex][cindex]);
					if (card != null) {
						theApp.cardPics[pindex][cindex] = new Picture(card.img,
								theApp.LOCATION[seat][0] + 30 * cindex,
								theApp.LOCATION[seat][1]);
						theApp.getModel().add(theApp.cardPics[pindex][cindex]);
					}
					theApp.getView().repaint();
				}
			}

			// SELECT CARD is used to indicate a card has been selected for draw
			// or trade.
			//
		} else if ((in.startsWith(new String("SELECT CARD")))
				&& (theApp != null)) {
			int i = in.indexOf('&');
			int cindex = Integer.parseInt(in.substring(i + 1, in.length()));
			int pindex = theApp.playerIndex(theApp.getThisPlayer().getName());
			int seat = theApp.getThisPlayer().seat;

			theApp.selectedCards[cindex] = true;
			for (int c = cindex; c < (theApp.getThisPlayer().getHand()
					.getNumUp() + theApp.getThisPlayer().getHand().getNumHole()); c++) {
				int offset = 0;
				if (theApp.selectedCards[c]) {
					offset = 8;
				}
				if (theApp.cardPics[pindex][c].getImage() != null) {
					java.awt.Image img = theApp.cardPics[pindex][c].getImage();
					theApp.getModel().remove(theApp.cardPics[pindex][c]);
					theApp.cardPics[pindex][c] = new Picture(img,
							theApp.LOCATION[seat][0] + 30 * c,
							theApp.LOCATION[seat][1] - offset);
					theApp.getModel().add(theApp.cardPics[pindex][c]);
				}
			}
			theApp.getView().repaint();

			// DESELECT CARD is used to indicate a card is no longer selected
			// for draw or trade.
			//
		} else if ((in.startsWith(new String("DESELECT CARD")))
				&& (theApp != null)) {
			int i = in.indexOf('&');
			int cindex = Integer.parseInt(in.substring(i + 1, in.length()));
			int pindex = theApp.playerIndex(theApp.getThisPlayer().getName());
			int seat = theApp.getThisPlayer().seat;
			theApp.selectedCards[cindex] = false;
			for (int c = cindex; c < (theApp.getThisPlayer().getHand()
					.getNumUp() + theApp.getThisPlayer().getHand().getNumHole()); c++) {
				int offset = 0;
				if (theApp.selectedCards[c]) {
					offset = 8;
				}
				if (theApp.cardPics[pindex][c].getImage() != null) {
					java.awt.Image img = theApp.cardPics[pindex][c].getImage();
					theApp.getModel().remove(theApp.cardPics[pindex][c]);
					theApp.cardPics[pindex][c] = new Picture(img,
							theApp.LOCATION[seat][0] + 30 * c,
							theApp.LOCATION[seat][1] - offset);
					theApp.getModel().add(theApp.cardPics[pindex][c]);
				}
			}
			theApp.getView().repaint();

			// DISPLAY DRAW BUTTON is used to draw the "Draw Cards" button
			// picture in the middle of the table
			//
		} else if ((in.startsWith(new String("DISPLAY DRAW BUTTON")))
				&& (theApp != null)) {
			theApp.midTablePic = new Picture(theApp.getView()
					.getDrawButtonImg(), 340, 200);
			theApp.getModel().add(theApp.midTablePic);
			theApp.getView().repaint();

			// DISPLAY PASS BUTTON is used to draw the "Pass Cards" button
			// picture in the middle of the table
			//
		} else if ((in.startsWith(new String("DISPLAY PASS BUTTON")))
				&& (theApp != null)) {
			theApp.midTablePic = new Picture(theApp.getView()
					.getPassButtonImg(), 340, 200);
			theApp.getModel().add(theApp.midTablePic);
			theApp.getView().repaint();

			// DISPLAY DONE BUTTON is used to draw the "Done" button picture in
			// the middle of the table
			//
		} else if ((in.startsWith(new String("DISPLAY DONE BUTTON")))
				&& (theApp != null)) {
			theApp.midTablePic = new Picture(theApp.getView()
					.getDoneButtonImg(), 340, 200);
			theApp.getModel().add(theApp.midTablePic);
			theApp.getView().repaint();

			// DISPLAY YESNO BUTTON is used to draw the "Yes" and "No" button
			// pictures in the middle of the table
			//
		} else if ((in.startsWith(new String("DISPLAY YESNO BUTTON")))
				&& (theApp != null)) {
			theApp.midTablePic = new Picture(theApp.getView()
					.getYesNoButtonImg(), 340, 200);
			theApp.getModel().add(theApp.midTablePic);
			theApp.getView().repaint();

			// DISPLAY NOTHING is used to remove the "Draw Cards" button picture
			// in the middle of the table
			//
		} else if ((in.startsWith(new String("DISPLAY NOTHING")))
				&& (theApp != null)) {
			if (theApp.midTablePic != null) {
				theApp.getModel().remove(theApp.midTablePic);
				theApp.getView().repaint();
			}

			// SIDE POTS is used to display the dialog box of who wins the side
			// pots.
			//
		} else if ((in.startsWith(new String("SIDE POTS"))) && (theApp != null)) {
			if (in.startsWith(new String("SIDE POTS START"))) {
				theApp.sidePotMessages = new ArrayList();
			} else if (in.startsWith(new String("SIDE POTS END"))) {
				theApp.displaySidePotMessages();
			} else {
				int i = in.indexOf('&');
				String m = in.substring(i + 1, in.length());
				theApp.sidePotMessages.add(m);
			}
		}
		return "nothing";
	}

	/***********************
	 * broadcastMessage() sends a message from the server to all the clients.
	 * 
	 * @param m
	 *            The message to send
	 * 
	 **/
	public void broadcastMessage(String m) {
		if (theStartApp != null) {
			theStartApp.broadcastMessage(m);
		}
	}

	/***********************
	 * makeString() routine is used to encrypt data sent over the client-server
	 * connection.
	 **/
	public static String makeString(String in) {
		byte[] inbytes = in.getBytes();
		byte[] outbytes = new byte[inbytes.length + 1];

		int r = (int) (10.0 * Math.random());
		String in2 = strings[r];
		byte[] in2bytes = in2.getBytes();
		byte[] tempbytes = new String("" + r).getBytes();
		outbytes[0] = tempbytes[0];
		int index = 0;
		for (int inindex = 0; inindex < inbytes.length; inindex++) {
			if ((inbytes[inindex] != '&') && (inbytes[inindex] != 'z')) {
				if (((inbytes[inindex] ^ in2bytes[index]) >= 32)
						&& ((inbytes[inindex] ^ in2bytes[index]) <= 121)) {
					outbytes[inindex + 1] = (byte) (inbytes[inindex] ^ in2bytes[index]);
					if (outbytes[inindex + 1] == '&') {
						outbytes[inindex + 1] = inbytes[inindex];
					}
				} else {
					outbytes[inindex + 1] = inbytes[inindex];
				}
			} else {
				outbytes[inindex + 1] = inbytes[inindex];
			}
			index++;
			if (index >= in2bytes.length) {
				index = 0;
			}
		}
		return new String(outbytes);
	}

	/***********************
	 * getString() routine is used to decrypt data sent over the client-server
	 * connection.
	 **/
	public static String getString(String in) {
		byte[] inbytes = in.getBytes();
		byte[] outbytes = new byte[inbytes.length - 1];

		String in2 = strings[Integer.parseInt(in.substring(0, 1))];
		byte[] in2bytes = in2.getBytes();

		int index = 0;
		for (int inindex = 1; inindex < inbytes.length; inindex++) {
			if ((inbytes[inindex] != '&') && (inbytes[inindex] != 'z')) {
				if (((inbytes[inindex] ^ in2bytes[index]) >= 32)
						&& ((inbytes[inindex] ^ in2bytes[index]) <= 121)) {
					outbytes[inindex - 1] = (byte) (inbytes[inindex] ^ in2bytes[index]);
					if (outbytes[inindex - 1] == '&') {
						outbytes[inindex - 1] = inbytes[inindex];
					}
				} else {
					outbytes[inindex - 1] = inbytes[inindex];
				}
			} else {
				outbytes[inindex - 1] = inbytes[inindex];
			}
			index++;
			if (index >= in2bytes.length) {
				index = 0;
			}
		}
		return new String(outbytes);
	}
}
