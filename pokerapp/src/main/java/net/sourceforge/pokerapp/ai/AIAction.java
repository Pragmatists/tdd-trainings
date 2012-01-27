/******************************************************************************************
 * AIAction.java                   PokerApp                                               *
 *                                                                                        *
 *   Revision History                                                                     *
 * +---------+----------+---------------------------------------------------------------+ *
 * | Version | DATE     | Description                                                   | *
 * +---------+----------+---------------------------------------------------------------+ *
 * |  0.95   | 09/15/04 | Initial documented release                                    | *
 * |  0.96   | 09/16/04 | Fixed action CHECK logic.  Changed action() to return what it | *
 * |         |          | actually did.  Fixed null pointer exception in action().      | *
 * |  0.97   | 11/10/04 | Changed gameName to gameClass - safer to use the class name   | *
 * |  0.98   | 12/07/04 | Correct check for autoDealing when determining if AI deals    | *
 * |  0.98   | 12/08/04 | Minor adjustments for bet limit and pot limit. Will not limit | *
 * |         |          | the AI's desired bet here. Instead it will be limited in the  | *
 * |         |          | server - same as if a human player bets too much.             | *
 * |  0.99   | 12/20/04 | Make the return on action() more specific.                    | *
 * |  0.99   | 02/08/05 | Now call playerAction function rather than otherPlayerAction  | *
 * |  1.00   | 06/06/05 | Added logging.                                                | *
 * |  1.00   | 07/14/07 | Prepare for open source.  Header/comments/package/etc...      | *
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
import net.sourceforge.pokerapp.games.*;
import java.util.ArrayList;
import java.lang.reflect.Constructor;
import java.lang.reflect.Array;

/****************************************************
 * The AIAction class provides a way for the AI to interact with the game. It
 * receives commands from the AILogic class and executes them. It also receives
 * data from the poker game and passes that information on to the AI.
 * 
 * @author Dan Puperi
 * @version 1.00
 * 
 **/
public class AIAction {

	private AIApp theAIApp; // The instance of the AIApp that created this class
	private AILogic ai; // The AILogic class that will decide what the AI should
						// do

	/***********************
	 * Constructor creates a new AIAction instance
	 * 
	 * @param a
	 *            The AIApp instance to which this AIAction class belongs
	 * @param type
	 *            The type of AIAApp this is
	 * 
	 **/
	public AIAction(AIApp a, String type) {
		theAIApp = a;
		theAIApp.log("Constructing an AIAction class for " + type, 3);
		try {
			Class ai_class = Class.forName("net.sourceforge.pokerapp.ai."
					+ theAIApp.getAIClasses().get(
							theAIApp.getAILabels().indexOf(type)));
			Class arg_types[] = { Class.forName(theAIApp.getClass().getName()) };
			Constructor construct = ai_class.getConstructor(arg_types);
			Object arg_list[] = { theAIApp };
			ai = (AILogic) construct.newInstance(arg_list);
		} catch (Exception x) {
			theAIApp.log("ERROR : Caught and exception while trying to create and AIAction class");
			theAIApp.logStackTrace(x);
		}
	}

	/***********************
	 * messageToAI() function checks commands from the server to the AI and acts
	 * on them.
	 * 
	 * @param m
	 *            The message sent to this AI
	 * 
	 **/
	public void messageToAI(String m) {
		String myName = new String();
		if (theAIApp.getThisPlayer() != null) {
			myName = theAIApp.getThisPlayer().getName();
		}

		// Check if its my turn to bet
		//
		if (m.indexOf("AI TURN") != -1) {
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
			}
			ai.myBet();
		}

		// Check to see if the refresh command is given - this will be a clue
		// that it may be this AI's turn to deal
		//
		if (m.indexOf("AI REFRESH") != -1) {
			int pIndex = theAIApp.playerIndex(theAIApp.getThisPlayer()
					.getName());
			if ((theAIApp.dealerIndex == pIndex) && (!theAIApp.autoDealing)
					&& (theAIApp.getPlayerList().size() > 1)) {
				deal();
			}
		}

		// Starting a new game - set gameName and call AILogic function
		//
		if (m.indexOf("AI NEW GAME") != -1) {
			boolean flag;
			while (theAIApp.startUpApp.getGame() == null) {
				flag = true;
			}
			String s2 = theAIApp.startUpApp.getGame().getClass().getName();
			String s3 = s2.substring(s2.lastIndexOf('.') + 1);
			for (int k = 0; k < theAIApp.getGameClasses().size(); k++) {
				if (s3.equals((String) theAIApp.getGameClasses().get(k))) {
					ai.gameClass = (String) theAIApp.getGameClasses().get(k);
				}
			}
			ai.newGame();
		}

		// Another player acts - send message to AILogic
		//
		if (m.indexOf("ACTION") != -1) {
			ai.playerAction(m.substring(8));
		}
	}

	/***********************
	 * deal() function is used when its the AI's turn to deal. The AI will
	 * choose to deal a game based on the gamePreferences() list which is set in
	 * AILogic.
	 **/
	private void deal() {
		theAIApp.log("AIAction.deal()", 3);
		try {
			Thread.sleep(6000);
		} catch (InterruptedException e) {
		}
		try {
			boolean gameDealt = false;
			if (theAIApp.startUpApp.getGame() == null) {
				int total = 0;
				for (int k = 0; k < ai.getGamePreference().size(); k++) {
					total += ((Integer) ai.getGamePreference().get(k))
							.intValue();
				}

				int r = (int) (((double) total) * Math.random() + 1.0);
				for (int i = 0; i < ai.getGamePreference().size(); i++) {
					int sum = 0;
					for (int j = 0; j <= i; j++) {
						sum += ((Integer) ai.getGamePreference().get(j))
								.intValue();
					}
					if ((r <= sum) && (!gameDealt)) {
						String gameName = (String) theAIApp.getGameLabels()
								.get(i);
						theAIApp.messageToServer("NEW GAME  &" + gameName);
						gameDealt = true;
					}
				}
			}
		} catch (Exception e) {
			theAIApp.log("Warning : Caught exception in AIAction.deal()");
			theAIApp.logStackTrace(e);
		}
	}

	/***********************
	 * action() function is a command from the AILogic class to do something.
	 * 
	 * @param actionList
	 *            The list of possible actions this AI will take
	 * @param probList
	 *            The probability that the AI will choose the corresponding
	 *            action from actionList
	 * @return The description of what action the AI took.
	 * 
	 **/
	public String action(ArrayList actionList, ArrayList probList) {
		theAIApp.log("AIAction.action()", 3);
		try {
			boolean actionPerformed = false;
			String ret = new String();
			int pindex = theAIApp.startUpApp.playerIndex(theAIApp
					.getThisPlayer().getName());
			int total = 0;
			for (int k = 0; k < probList.size(); k++) {
				total += ((Integer) probList.get(k)).intValue();
			}

			if (pindex == -1) {
				theAIApp.messageToServer("BET FOLD");
				return new String("FOLD");
			}
			int r = (int) (((double) total) * Math.random() + 1.0);

			for (int i = 0; i < probList.size(); i++) {
				int sum = 0;
				for (int j = 0; j <= i; j++) {
					sum += ((Integer) probList.get(j)).intValue();
				}
				if ((r <= sum) && (!actionPerformed)) {
					actionPerformed = true;
					if (((String) actionList.get(i)).equals("FOLD")) {
						theAIApp.messageToServer("BET FOLD");
						theAIApp.log("Fold.", 3);
						return new String("FOLD");
					}
					if (((String) actionList.get(i)).equals("CHECK")) {
						if (theAIApp.startUpApp.getGame() != null) {
							if (((Player) theAIApp.startUpApp.getPlayerList()
									.get(pindex)).getPrevBet().amount() < theAIApp.startUpApp
									.getGame().getCurrBet().amount()) {
								theAIApp.messageToServer("BET FOLD");
								theAIApp.log(
										"Wanted to check, but had to fold.", 3);
								return new String("FOLD");
							} else {
								theAIApp.messageToServer("BET CALL");
								if (theAIApp.startUpApp.getGame().getCurrBet()
										.amount() == 0.0f) {
									ret = new String("CHECK");
								} else {
									ret = new String("CALL");
								}
								theAIApp.log("Check", 3);
							}
						}
					}
					if (((String) actionList.get(i)).equals("CALL")) {
						theAIApp.messageToServer("BET CALL");
						ret = new String("CALL");
						theAIApp.log("Call", 3);
					}
					if (((String) actionList.get(i)).equals("ALLIN")) {
						theAIApp.messageToServer("BET ALLIN");
						ret = new String("ALLIN");
						theAIApp.log("All In!", 3);
					}
					if (((String) actionList.get(i)).startsWith(new String(
							"BET"))) {
						if (!theAIApp.noLimit && !theAIApp.betLimit
								&& !theAIApp.potLimit) {
							theAIApp.messageToServer("BET FOUR");
							ret = new String("BET");
							theAIApp.log("Bet", 3);
						} else {
							String amounts[] = ((String) actionList.get(i))
									.split(" ");
							int ind = 1;
							while (ind < Array.getLength(amounts)) {
								if (amounts[ind].equals("ONE")) {
									((Player) theAIApp.startUpApp
											.getPlayerList().get(pindex))
											.addBet(theAIApp.button1Val
													.amount());
								} else if (amounts[ind].equals("TWO")) {
									((Player) theAIApp.startUpApp
											.getPlayerList().get(pindex))
											.addBet(theAIApp.button2Val
													.amount());
								} else if (amounts[ind].equals("THREE")) {
									((Player) theAIApp.startUpApp
											.getPlayerList().get(pindex))
											.addBet(theAIApp.button3Val
													.amount());
								} else if (amounts[ind].equals("FOUR")) {
									((Player) theAIApp.startUpApp
											.getPlayerList().get(pindex))
											.addBet(theAIApp.button4Val
													.amount());
								} else if (amounts[ind].equals("POT")) {
									((Player) theAIApp.startUpApp
											.getPlayerList().get(pindex))
											.addBet(theAIApp.startUpApp
													.getPot().amount());
								}
								ind++;
							}
							StringBuffer retb = new StringBuffer("BET ");
							for (int kk = 0; kk < amounts.length; kk++) {
								retb.append(amounts[kk] + " ");
							}
							ret = new String(retb);
							if (((Player) theAIApp.startUpApp.getPlayerList()
									.get(pindex)).getBet().amount() >= theAIApp
									.getThisPlayer().getBankroll().amount()) {
								((Player) theAIApp.startUpApp.getPlayerList()
										.get(pindex)).setBet(0.0f);
								theAIApp.messageToServer("BET ALLIN");
								theAIApp.messageToServer("BET SUBMIT");
								theAIApp.log("Bet - had to go all in.", 3);
								return new String("ALLIN");
							}
							if (theAIApp.startUpApp.getGame().getCurrBet()
									.amount() > 0.0f) {
								if (((Player) theAIApp.startUpApp
										.getPlayerList().get(pindex)).getBet()
										.amount() <= theAIApp.startUpApp
										.getGame().getCurrBet().amount()) {
									theAIApp.messageToServer("BET FOLD");
									theAIApp.log(
											"Tried to bet "
													+ ((Player) theAIApp.startUpApp
															.getPlayerList()
															.get(pindex))
															.getBet()
													+ ", but it wasn't enough so had to fold.",
											3);
									return new String("FOLD");
								} else {
									theAIApp.messageToServer("BET CALL");
									theAIApp.log(
											"Tried to bet "
													+ ((Player) theAIApp.startUpApp
															.getPlayerList()
															.get(pindex))
															.getBet()
													+ ", but should have raised, so have to call.",
											3);
									ret = new String("CALL");
								}
							} else {
								theAIApp.log(
										"Bet "
												+ ((Player) theAIApp.startUpApp
														.getPlayerList().get(
																pindex))
														.getBet(), 3);
							}
						}
					}
					if (((String) actionList.get(i)).startsWith(new String(
							"RAISE"))) {
						if (!theAIApp.noLimit && !theAIApp.betLimit
								&& !theAIApp.potLimit) {
							theAIApp.messageToServer("BET ALLIN");
							theAIApp.log("Raise.", 3);
							ret = new String("RAISE");
						} else {
							String amounts[] = ((String) actionList.get(i))
									.split(" ");
							((Player) theAIApp.startUpApp.getPlayerList().get(
									pindex)).addBet(theAIApp.startUpApp
									.getGame().getCurrBet().amount()
									- ((Player) theAIApp.startUpApp
											.getPlayerList().get(pindex))
											.getPrevBet().amount());
							int ind = 1;
							while (ind < Array.getLength(amounts)) {
								if (amounts[ind].equals("ONE")) {
									((Player) theAIApp.startUpApp
											.getPlayerList().get(pindex))
											.addBet(theAIApp.button1Val
													.amount());
								} else if (amounts[ind].equals("TWO")) {
									((Player) theAIApp.startUpApp
											.getPlayerList().get(pindex))
											.addBet(theAIApp.button2Val
													.amount());
								} else if (amounts[ind].equals("THREE")) {
									((Player) theAIApp.startUpApp
											.getPlayerList().get(pindex))
											.addBet(theAIApp.button3Val
													.amount());
								} else if (amounts[ind].equals("FOUR")) {
									((Player) theAIApp.startUpApp
											.getPlayerList().get(pindex))
											.addBet(theAIApp.button4Val
													.amount());
								} else if (amounts[ind].equals("POT")) {
									((Player) theAIApp.startUpApp
											.getPlayerList().get(pindex))
											.addBet(theAIApp.startUpApp
													.getPot().amount());
								}
								ind++;
							}
							StringBuffer retb = new StringBuffer("RAISE ");
							for (int kk = 0; kk < amounts.length; kk++) {
								retb.append(amounts[kk] + " ");
							}
							ret = new String(retb);
							if (((Player) theAIApp.startUpApp.getPlayerList()
									.get(pindex)).getBet().amount() >= theAIApp
									.getThisPlayer().getBankroll().amount()) {
								((Player) theAIApp.startUpApp.getPlayerList()
										.get(pindex)).setBet(0.0f);
								theAIApp.messageToServer("BET ALLIN");
								theAIApp.messageToServer("BET SUBMIT");
								theAIApp.log("Raise - had to go all in.", 3);
								return new String("ALLIN");
							}
							theAIApp.log(
									"Raise "
											+ new PokerMoney(
													((Player) theAIApp.startUpApp
															.getPlayerList()
															.get(pindex))
															.getBet().amount()
															- (theAIApp.startUpApp
																	.getGame()
																	.getCurrBet()
																	.amount() - ((Player) theAIApp.startUpApp
																	.getPlayerList()
																	.get(pindex))
																	.getPrevBet()
																	.amount())),
									3);
						}
					}
				}
			}
			if (!actionPerformed) {
				theAIApp.messageToServer("BET FOLD");
				theAIApp.log(
						"Warning : could not find an action for this AI to take, so had to fold.",
						1);
				return new String("FOLD");
			}
			theAIApp.messageToServer("BET SUBMIT");
			return ret;
		} catch (Exception e) {
			theAIApp.log("ERROR : Caught exception in AIAction.action()");
			theAIApp.logStackTrace(e);
			return new String("SCREWED");
		}
	}

	/***********************
	 * getAI() function is used to access the private class variable
	 * 
	 * @return The AILogic class which is used by this AI
	 * 
	 **/
	public AILogic getAI() {
		return ai;
	}
}
