/******************************************************************************************
 * Anaconda.java                    PokerApp                                              *
 *                                                                                        *
 *   Revision History                                                                     *
 * +---------+----------+---------------------------------------------------------------+ *
 * | Version | DATE     | Description                                                   | *
 * +---------+----------+---------------------------------------------------------------+ *
 * |  0.97   | 11/09/04 | Initial release                                               | *
 * |  0.99   | 05/17/05 | Added logic to PLAYER TURN call for structured betting games  | *
 * |  0.99   | 05/19/05 | Define showPlayer()                                           | *
 * |  0.99   | 05/23/05 | Set requiresInteraction flag = true                           | *
 * |  1.00   | 06/06/05 | Added logging.                                                | *
 * |  1.00   | 08/02/07 | Prepare for open source.  Header/comments/package/etc...      | *
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

package net.sourceforge.pokerapp.games;

import net.sourceforge.pokerapp.*;

/****************************************************
 * Anaconda game definition.
 * 
 * @author Dan Puperi
 * @version 1.00
 * 
 **/
public class Anaconda extends PokerGame {

	private boolean passed[]; // Defines who has passed their cards
	private boolean allPassed; // Has everybody had a chance to pass their
								// cards?
	private boolean passSelections[][]; // Which cards are selected to be passed
	private int numToPass; // Number of cards to pass.

	/***********************
	 * Constructor - creates a new Anaconda game
	 * 
	 * @param a
	 *            The StartPoker instance to which this game belongs
	 * 
	 **/
	public Anaconda(StartPoker a) {
		super(a, "Anaconda", false);
		maxActionNum = 10;
		if (theApp.getPlayerList().size() < 2) {
			return;
		}
		theApp.log("Constructing a game of Anaconda", 3);
		passed = new boolean[MAX_PLAYERS];
		passSelections = new boolean[MAX_PLAYERS][7];
		numToPass = 0;
		for (int i = 0; i < theApp.getPlayerList().size(); i++) {
			passed[i] = false;
		}
		allPassed = false;
		requiresInteraction = true;
		deal();
	}

	/***********************
	 * deal() deals the initial cards
	 **/
	protected void deal() {
		theApp.log("Anaconda.deal()", 3);
		Card c = new Card();
		bestPossible = calcBestPossible();

		// First card down
		for (int i = 0; i < theApp.getPlayerList().size(); i++) {
			c = theApp.getDeck().deal();
			((Player) theApp.getPlayerList().get(i)).getHand().addHoleCard(c);
			theApp.messageToPlayer(
					((Player) theApp.getPlayerList().get(i)).getName(),
					"CARD HOLE  &"
							+ ((Player) theApp.getPlayerList().get(i))
									.getName() + "&0&" + c);
			theApp.broadcastMessage("CARD HOLE  &"
					+ ((Player) theApp.getPlayerList().get(i)).getName() + "&0");
		}

		// Second card down
		for (int i = 0; i < theApp.getPlayerList().size(); i++) {
			c = theApp.getDeck().deal();
			((Player) theApp.getPlayerList().get(i)).getHand().addHoleCard(c);
			theApp.messageToPlayer(
					((Player) theApp.getPlayerList().get(i)).getName(),
					"CARD HOLE  &"
							+ ((Player) theApp.getPlayerList().get(i))
									.getName() + "&1&" + c);
			theApp.broadcastMessage("CARD HOLE  &"
					+ ((Player) theApp.getPlayerList().get(i)).getName() + "&1");
		}

		// Third card down
		for (int i = 0; i < theApp.getPlayerList().size(); i++) {
			c = theApp.getDeck().deal();
			((Player) theApp.getPlayerList().get(i)).getHand().addHoleCard(c);
			theApp.messageToPlayer(
					((Player) theApp.getPlayerList().get(i)).getName(),
					"CARD HOLE  &"
							+ ((Player) theApp.getPlayerList().get(i))
									.getName() + "&2&" + c);
			theApp.broadcastMessage("CARD HOLE  &"
					+ ((Player) theApp.getPlayerList().get(i)).getName() + "&2");
		}

		// Fourth card down
		for (int i = 0; i < theApp.getPlayerList().size(); i++) {
			c = theApp.getDeck().deal();
			((Player) theApp.getPlayerList().get(i)).getHand().addHoleCard(c);
			theApp.messageToPlayer(
					((Player) theApp.getPlayerList().get(i)).getName(),
					"CARD HOLE  &"
							+ ((Player) theApp.getPlayerList().get(i))
									.getName() + "&3&" + c);
			theApp.broadcastMessage("CARD HOLE  &"
					+ ((Player) theApp.getPlayerList().get(i)).getName() + "&3");
		}

		// Fifth card down
		for (int i = 0; i < theApp.getPlayerList().size(); i++) {
			c = theApp.getDeck().deal();
			((Player) theApp.getPlayerList().get(i)).getHand().addHoleCard(c);
			theApp.messageToPlayer(
					((Player) theApp.getPlayerList().get(i)).getName(),
					"CARD HOLE  &"
							+ ((Player) theApp.getPlayerList().get(i))
									.getName() + "&4&" + c);
			theApp.broadcastMessage("CARD HOLE  &"
					+ ((Player) theApp.getPlayerList().get(i)).getName() + "&4");
		}

		// Sixth card down
		for (int i = 0; i < theApp.getPlayerList().size(); i++) {
			c = theApp.getDeck().deal();
			((Player) theApp.getPlayerList().get(i)).getHand().addHoleCard(c);
			theApp.messageToPlayer(
					((Player) theApp.getPlayerList().get(i)).getName(),
					"CARD HOLE  &"
							+ ((Player) theApp.getPlayerList().get(i))
									.getName() + "&5&" + c);
			theApp.broadcastMessage("CARD HOLE  &"
					+ ((Player) theApp.getPlayerList().get(i)).getName() + "&5");
		}

		// Seventh card down
		for (int i = 0; i < theApp.getPlayerList().size(); i++) {
			c = theApp.getDeck().deal();
			((Player) theApp.getPlayerList().get(i)).getHand().addHoleCard(c);
			theApp.messageToPlayer(
					((Player) theApp.getPlayerList().get(i)).getName(),
					"CARD HOLE  &"
							+ ((Player) theApp.getPlayerList().get(i))
									.getName() + "&6&" + c);
			theApp.broadcastMessage("CARD HOLE  &"
					+ ((Player) theApp.getPlayerList().get(i)).getName() + "&6");
		}

		theApp.broadcastMessage("MESSAGE  &Playing Anaconda.  "
				+ ((Player) theApp.getPlayerList().get(currPlayerIndex))
						.getName() + "'s bet.");
		if ((theApp.blinds)
				&& !(theApp.noLimit || theApp.potLimit || theApp.betLimit)) {
			theApp.broadcastMessage("PLAYER TURN &"
					+ ((Player) theApp.getPlayerList().get(currPlayerIndex))
							.getName() + "&enable raise");
		} else {
			theApp.broadcastMessage("PLAYER TURN &"
					+ ((Player) theApp.getPlayerList().get(currPlayerIndex))
							.getName());
		}
	}

	/***********************
	 * nextAction() determines what happens next
	 **/
	protected void nextAction() {
		theApp.log("Anaconda.nextAction() - previous actionNum = " + actionNum,
				3);
		actionNum++;
		for (int i = 0; i < theApp.getPlayerList().size(); i++) {
			((Player) theApp.getPlayerList().get(i)).potOK = false;
			((Player) theApp.getPlayerList().get(i)).setBet(0.0f);
			((Player) theApp.getPlayerList().get(i)).setPrevBet(0.0f);
		}
		if (actionNum != maxActionNum) {
			currBet = new PokerMoney(0.0f);
			currPlayerIndex = firstToBet();
			if ((currPlayerIndex < 0)
					|| (currPlayerIndex >= theApp.getPlayerList().size())) {
				actionNum = maxActionNum;
				skipShow = true;
			} else {
				highBettor = (Player) theApp.getPlayerList().get(
						currPlayerIndex);
			}
		}
		theApp.updateMoneyLine(currBet, highBettor.getName());
		switch (actionNum) {
		case 2:
			theApp.broadcastMessage("DISABLE BUTTONS");
			theApp.broadcastMessage("MESSAGE &Select three cards to pass clockwise and press the Pass Cards button.");
			pass(3);
			break;
		case 3:
			theApp.broadcastMessage("MESSAGE &Select two cards to pass clockwise and press the Pass Cards button.");
			passSelections = new boolean[MAX_PLAYERS][7];
			pass(2);
			break;
		case 4:
			pass(1);
			theApp.broadcastMessage("MESSAGE &Select one card to pass clockwise and press the Pass Cards button.");
			passSelections = new boolean[MAX_PLAYERS][7];
			break;
		case 5:
			pickCards();
			theApp.broadcastMessage("MESSAGE &Reorder cards and press Done.  The last 2 cards (to the right) will be dropped.");
			passSelections = new boolean[MAX_PLAYERS][7];
			break;
		case 6:
			theApp.broadcastMessage("DISPLAY NOTHING");
			flipCard(0);
			break;
		case 7:
			flipCard(1);
			break;
		case 8:
			flipCard(2);
			break;
		case 9:
			flipCard(3);
			break;
		case 10:
			show();
			break;
		default:
			break;
		}
	}

	/***********************
	 * pass() defines what happens when its time for players to pass their cards
	 * to the left
	 * 
	 * @param num
	 *            The number of cards that will be passed this round.
	 * 
	 **/
	private void pass(int num) {
		theApp.log("Anaconda.pass( " + num + " )", 3);
		numToPass = num;
		allPassed = true;
		for (int i = 0; i < theApp.getPlayerList().size(); i++) {
			if ((((Player) theApp.getPlayerList().get(i)).in) && (!passed[i])) {
				allPassed = false;
			}
		}

		if (!allPassed) {
			theApp.broadcastMessage("DISPLAY NOTHING");
			for (int i = 0; i < theApp.getPlayerList().size(); i++) {
				if ((((Player) theApp.getPlayerList().get(i)).in)
						&& (!passed[i])) {
					theApp.messageToPlayer(((Player) theApp.getPlayerList()
							.get(i)).getName(), "DISPLAY PASS BUTTON");
				}
			}
		} else {
			allPassed = false;
			for (int i = 0; i < theApp.getPlayerList().size(); i++) {
				passed[i] = false;
			}

			int pindex = currPlayerIndex;
			int sindex = currSeatIndex;
			int firstSeat = sindex;
			boolean done = false;
			Hand prevPassHand = new Hand();

			while (!done) {
				pindex = theApp.getPlayerInSeat(sindex);
				if ((((Player) theApp.getPlayerList().get(pindex)).in)
						&& (((Player) theApp.getPlayerList().get(pindex))
								.getHand().getNumHole() >= 7)) {
					Hand tempHand = new Hand();
					Hand passHand = new Hand();
					for (int cindex = 0; cindex < 7; cindex++) {
						if (passSelections[pindex][cindex]) {
							passHand.addHoleCard(((Player) theApp
									.getPlayerList().get(pindex)).getHand()
									.getHoleCard(cindex));
						} else {
							tempHand.addHoleCard(((Player) theApp
									.getPlayerList().get(pindex)).getHand()
									.getHoleCard(cindex));
						}
					}
					((Player) theApp.getPlayerList().get(pindex)).getHand()
							.clearHand();
					theApp.messageToPlayer(
							((Player) theApp.getPlayerList().get(pindex))
									.getName(),
							"PLAYER CLEAR HAND &"
									+ ((Player) theApp.getPlayerList().get(
											pindex)).getName());
					int cindex = 0;
					for (int tempindex = 0; tempindex < tempHand.getNumHole(); tempindex++) {
						Card c = tempHand.getHoleCard(tempindex);
						((Player) theApp.getPlayerList().get(pindex)).getHand()
								.addHoleCard(c);
						theApp.messageToPlayer(
								((Player) theApp.getPlayerList().get(pindex))
										.getName(),
								"CARD HOLE  &"
										+ ((Player) theApp.getPlayerList().get(
												pindex)).getName() + "&"
										+ cindex + "&" + c);
						cindex++;
					}
					for (int passindex = 0; passindex < prevPassHand
							.getNumHole(); passindex++) {
						Card c = prevPassHand.getHoleCard(passindex);
						((Player) theApp.getPlayerList().get(pindex)).getHand()
								.addHoleCard(c);
						theApp.messageToPlayer(
								((Player) theApp.getPlayerList().get(pindex))
										.getName(),
								"CARD HOLE  &"
										+ ((Player) theApp.getPlayerList().get(
												pindex)).getName() + "&"
										+ cindex + "&" + c);
						cindex++;
					}
					prevPassHand.clearHand();
					for (int passindex = 0; passindex < passHand.getNumHole(); passindex++) {
						prevPassHand.addHoleCard(passHand
								.getHoleCard(passindex));
					}
				}
				sindex = theApp.nextSeat(sindex);
				if (sindex == firstSeat) {
					pindex = theApp.getPlayerInSeat(sindex);
					done = true;
					int cindex = ((Player) theApp.getPlayerList().get(pindex))
							.getHand().getNumHole();
					for (int passindex = 0; passindex < prevPassHand
							.getNumHole(); passindex++) {
						Card c = prevPassHand.getHoleCard(passindex);
						((Player) theApp.getPlayerList().get(pindex)).getHand()
								.addHoleCard(c);
						theApp.messageToPlayer(
								((Player) theApp.getPlayerList().get(pindex))
										.getName(),
								"CARD HOLE  &"
										+ ((Player) theApp.getPlayerList().get(
												pindex)).getName() + "&"
										+ cindex + "&" + c);
						cindex++;
					}
				}
			}
			nextAction();
		}
	}

	/***********************
	 * flipCard() defines what happens when its time for players to start
	 * flipping over their cards.
	 * 
	 * @param num
	 *            The card number that gets flipped over
	 * 
	 **/
	private void flipCard(int num) {
		theApp.log("Anaconda.flipCard( " + num + " )", 3);
		for (int pindex = 0; pindex < theApp.getPlayerList().size(); pindex++) {
			Player player = (Player) theApp.getPlayerList().get(pindex);
			theApp.broadcastMessage("PLAYER NOPICS &" + player.getName());
			if (player.in) {
				Hand tempHand = new Hand();
				for (int cindex = 0; cindex < player.getHand().getNumUp(); cindex++) {
					tempHand.addHoleCard(player.getHand().getUpCard(cindex));
				}
				for (int cindex = 0; cindex < player.getHand().getNumHole(); cindex++) {
					tempHand.addHoleCard(player.getHand().getHoleCard(cindex));
				}
				((Player) theApp.getPlayerList().get(pindex)).getHand()
						.clearHand();
				theApp.messageToPlayer(
						((Player) theApp.getPlayerList().get(pindex)).getName(),
						"PLAYER CLEAR HAND &"
								+ ((Player) theApp.getPlayerList().get(pindex))
										.getName());
				for (int tempindex = 0; tempindex < tempHand.getNumHole(); tempindex++) {
					Card c = tempHand.getHoleCard(tempindex);
					if (tempindex <= num) {
						((Player) theApp.getPlayerList().get(pindex)).getHand()
								.addUpCard(c);
						theApp.broadcastMessage("CARD UP  &"
								+ ((Player) theApp.getPlayerList().get(pindex))
										.getName() + "&" + tempindex + "&" + c);
					} else {
						((Player) theApp.getPlayerList().get(pindex)).getHand()
								.addHoleCard(c);
						theApp.messageToPlayer(
								((Player) theApp.getPlayerList().get(pindex))
										.getName(),
								"CARD HOLE  &"
										+ ((Player) theApp.getPlayerList().get(
												pindex)).getName() + "&"
										+ tempindex + "&" + c);
						theApp.broadcastMessage("CARD HOLE  &"
								+ ((Player) theApp.getPlayerList().get(pindex))
										.getName() + "&" + tempindex);
					}
				}
			}
		}
		currBet = new PokerMoney(0.0f);
		currPlayerIndex = firstToBet_Stud();
		if ((currPlayerIndex < 0)
				|| (currPlayerIndex >= theApp.getPlayerList().size())) {
			actionNum = maxActionNum;
			skipShow = true;
		} else {
			highBettor = (Player) theApp.getPlayerList().get(currPlayerIndex);
		}
		theApp.updateMoneyLine(currBet, highBettor.getName());
		theApp.broadcastMessage("MESSAGE  &"
				+ ((Player) theApp.getPlayerList().get(currPlayerIndex))
						.getName() + "'s bet.");
		theApp.broadcastMessage("PLAYER TURN &"
				+ ((Player) theApp.getPlayerList().get(currPlayerIndex))
						.getName());
	}

	/***********************
	 * pickCards() defines what happens when its time for players to pick their
	 * 5 best cards.
	 **/
	private void pickCards() {
		theApp.log("Anaconda.pickCards()", 3);
		allPassed = true;
		for (int i = 0; i < theApp.getPlayerList().size(); i++) {
			if ((((Player) theApp.getPlayerList().get(i)).in) && (!passed[i])) {
				allPassed = false;
			}
		}
		if (!allPassed) {
			theApp.broadcastMessage("DISPLAY NOTHING");
			for (int i = 0; i < theApp.getPlayerList().size(); i++) {
				if ((((Player) theApp.getPlayerList().get(i)).in)
						&& (!passed[i])) {
					theApp.messageToPlayer(((Player) theApp.getPlayerList()
							.get(i)).getName(), "DISPLAY DONE BUTTON");
				}
			}
		} else {
			allPassed = false;
			for (int i = 0; i < theApp.getPlayerList().size(); i++) {
				passed[i] = false;
			}
			for (int pindex = 0; pindex < theApp.getPlayerList().size(); pindex++) {
				theApp.broadcastMessage("PLAYER NOPICS &"
						+ ((Player) theApp.getPlayerList().get(pindex))
								.getName());
			}
			for (int pindex = 0; pindex < theApp.getPlayerList().size(); pindex++) {
				if ((((Player) theApp.getPlayerList().get(pindex)).in)
						&& (((Player) theApp.getPlayerList().get(pindex))
								.getHand().getNumHole() >= 5)) {
					Hand tempHand = new Hand();
					for (int cindex = 0; cindex < 5; cindex++) {
						tempHand.addHoleCard(((Player) theApp.getPlayerList()
								.get(pindex)).getHand().getHoleCard(cindex));
					}
					((Player) theApp.getPlayerList().get(pindex)).getHand()
							.clearHand();
					theApp.messageToPlayer(
							((Player) theApp.getPlayerList().get(pindex))
									.getName(),
							"PLAYER CLEAR HAND &"
									+ ((Player) theApp.getPlayerList().get(
											pindex)).getName());
					for (int tempindex = 0; tempindex < tempHand.getNumHole(); tempindex++) {
						Card c = tempHand.getHoleCard(tempindex);
						((Player) theApp.getPlayerList().get(pindex)).getHand()
								.addHoleCard(c);
						theApp.messageToPlayer(
								((Player) theApp.getPlayerList().get(pindex))
										.getName(),
								"CARD HOLE  &"
										+ ((Player) theApp.getPlayerList().get(
												pindex)).getName() + "&"
										+ tempindex + "&" + c);
						theApp.broadcastMessage("CARD HOLE  &"
								+ ((Player) theApp.getPlayerList().get(pindex))
										.getName() + "&" + tempindex);
					}
				}
			}
			nextAction();
		}
	}

	/***********************
	 * firstToBet() determines who gets to bet first - calls the function common
	 * to all HoldEm type games in PokerGame class
	 **/
	protected int firstToBet() {
		return firstToBet_HoldEm();
	}

	/***********************
	 * showPlayer() defines how a player shows their cards
	 * 
	 * @param p
	 *            The player being shown
	 * @param b
	 *            Whether or not this player is actually going to turn over
	 *            their cards.
	 * 
	 **/
	protected void showPlayer(Player p, boolean b) {
		if (b) {
			theApp.broadcastMessage("SHOW CARD &" + p.getName() + "&4&"
					+ p.getHand().getHoleCard(0));
		} else {
			theApp.broadcastMessage("SHOW CARD &" + p.getName() + "&0");
			theApp.broadcastMessage("SHOW CARD &" + p.getName() + "&1");
			theApp.broadcastMessage("SHOW CARD &" + p.getName() + "&2");
			theApp.broadcastMessage("SHOW CARD &" + p.getName() + "&3");
			theApp.broadcastMessage("SHOW CARD &" + p.getName() + "&4");
		}
	}

	/***********************
	 * showCards() defines what happens at the showdown
	 **/
	protected void showCards() {
		theApp.log("Anaconda.showCards()", 3);
		for (int i = 0; i < theApp.getPlayerList().size(); i++) {
			Player player = (Player) theApp.getPlayerList().get(i);
			if (player.in) {
				showPlayer(player, true);
			}
		}
	}

	/***********************
	 * bestHand() returns the float value of the best possible hand that can be
	 * made from given hand h.
	 * 
	 * @param h
	 *            The hand to evaluate
	 * @return The float value of that hand
	 * 
	 **/
	public float bestHand(Hand h) {
		theApp.log("Anaconda.bestHand( " + h + " )", 3);
		int numCards = h.getNumHole() + h.getNumUp();
		Card[] c = new Card[numCards];
		float best = 0.0f;
		float test = 0.0f;
		HandEvaluator he = new HandEvaluator();

		for (int i = 0; i < h.getNumHole(); i++) {
			if (h.getHoleCard(i) != null) {
				c[i] = h.getHoleCard(i);
			}
		}
		for (int i = 0; i < h.getNumUp(); i++) {
			if (h.getUpCard(i) != null) {
				c[h.getNumHole() + i] = h.getUpCard(i);
			}
		}

		if (numCards == 5) {
			return he.rankHand(c[0], c[1], c[2], c[3], c[4]);
		} else if (numCards == 4) {
			return he.rankHand(c[0], c[1], c[2], c[3]);
		} else if (numCards == 3) {
			return he.rankHand(c[0], c[1], c[2]);
		} else if (numCards == 2) {
			return he.rankHand(c[0], c[1]);
		} else if (numCards == 1) {
			return he.rankHand(c[0]);
		} else {
			return 0.0f;
		}
	}

	/***********************
	 * calcBestPossible() determines the best possible hand available based on
	 * the shared cards. This function is mainly to aid the computer AI logic.
	 * 
	 * @return The float value of the best possible hand based on what is
	 *         showing
	 * 
	 **/
	protected float calcBestPossible() {
		return 150.0f;
	}

	/***********************
	 * mouseClick() handles mouse click events.
	 * 
	 * @param name
	 *            The name of the player making the mouse click
	 * @param x
	 *            The x location of the mouse click
	 * @param y
	 *            The y location of the mouse click
	 * 
	 **/
	protected void mouseClick(String name, int x, int y) {
		theApp.log("Anaconda.mouseClick( " + name + ", " + x + ", " + y + " )",
				3);
		int pindex = theApp.playerIndex(name);
		int loc = ((Player) theApp.getPlayerList().get(pindex)).seat;

		int numSel = 0;
		for (int i = 0; i < 7; i++) {
			if (passSelections[pindex][i]) {
				numSel++;
			}
		}

		// Players selecting which cards to pass
		//
		if ((actionNum >= 2) && (actionNum <= 4)) {

			// Player has clicked the "Pass Cards" button in the middle of the
			// screen.
			//
			if ((x >= 340) && (x <= 340 + 150) && (y >= 200 + 23)
					&& (y <= 200 + 23 + 50)) {
				if (numSel == numToPass) {
					theApp.log(name + " has selected " + numToPass
							+ " cards to pass.", 1);
					theApp.messageToPlayer(name,
							"MESSAGE &Waiting for everyone else to select cards to pass.");
					passed[pindex] = true;
					pass(numSel);
					return;
				} else {
					theApp.messageToPlayer(name, "MESSAGE &You must select "
							+ numToPass + " cards to pass.");
					return;
				}
			}

			// Check to see if player has clicked to select or deselect any of
			// his cards.
			//
			for (int cardNum = 0; cardNum < 7; cardNum++) {
				int width = 29;
				if (cardNum == 6) {
					width = 71;
				}
				if ((x >= PokerApp.LOCATION[loc][0] + 30 * cardNum)
						&& (x <= PokerApp.LOCATION[loc][0] + 30 * cardNum
								+ width)
						&& (y >= PokerApp.LOCATION[loc][1] + 15)
						&& (y <= PokerApp.LOCATION[loc][1] + 23 + 96)) {
					if (passSelections[pindex][cardNum]) {
						theApp.messageToPlayer(name, "DESELECT CARD &"
								+ cardNum);
						passSelections[pindex][cardNum] = !passSelections[pindex][cardNum];
					} else if (numSel < numToPass) {
						theApp.messageToPlayer(name, "SELECT CARD &" + cardNum);
						passSelections[pindex][cardNum] = !passSelections[pindex][cardNum];
					}
				}
			}
			//
			// Players reordering cards
			//
		} else if (actionNum == 5) {
			//
			// Player has clicked the "Done" button in the middle of the screen.
			//
			if ((x >= 340) && (x <= 340 + 150) && (y >= 200 + 23)
					&& (y <= 200 + 23 + 50)) {
				theApp.log(name + " is done reordering cards.", 1);
				theApp.messageToPlayer(name,
						"MESSAGE &Waiting for everyone else to reorder their cards.");
				passed[pindex] = true;
				pickCards();
				return;
			}

			// Check to see if player has clicked to select or deselect any of
			// his cards.
			//
			for (int cardNum = 0; cardNum < 7; cardNum++) {
				int width = 29;
				if (cardNum == 6) {
					width = 71;
				}
				if ((x >= PokerApp.LOCATION[loc][0] + 30 * cardNum)
						&& (x <= PokerApp.LOCATION[loc][0] + 30 * cardNum
								+ width)
						&& (y >= PokerApp.LOCATION[loc][1] + 15)
						&& (y <= PokerApp.LOCATION[loc][1] + 23 + 96)) {
					if (passSelections[pindex][cardNum]) {
						theApp.messageToPlayer(name, "DESELECT CARD &"
								+ cardNum);
						passSelections[pindex][cardNum] = !passSelections[pindex][cardNum];
					} else if (numSel == 0) {
						theApp.messageToPlayer(name, "SELECT CARD &" + cardNum);
						passSelections[pindex][cardNum] = !passSelections[pindex][cardNum];
					} else {
						int c2Num = -1;
						for (int i = 0; i < 7; i++) {
							if (passSelections[pindex][i]) {
								c2Num = i;
							}
						}
						for (int i = 0; i < 7; i++) {
							passSelections[pindex][i] = false;
						}
						Card c1 = ((Player) theApp.getPlayerList().get(pindex))
								.getHand().getHoleCard(cardNum);
						Card c2 = ((Player) theApp.getPlayerList().get(pindex))
								.getHand().getHoleCard(c2Num);
						Hand tempHand = new Hand();
						for (int cindex = 0; cindex < ((Player) theApp
								.getPlayerList().get(pindex)).getHand()
								.getNumHole(); cindex++) {
							if (cindex == cardNum) {
								tempHand.addHoleCard(c2);
							} else if (cindex == c2Num) {
								tempHand.addHoleCard(c1);
							} else {
								tempHand.addHoleCard(((Player) theApp
										.getPlayerList().get(pindex)).getHand()
										.getHoleCard(cindex));
							}
						}
						((Player) theApp.getPlayerList().get(pindex)).getHand()
								.clearHand();
						theApp.messageToPlayer(
								((Player) theApp.getPlayerList().get(pindex))
										.getName(),
								"PLAYER CLEAR HAND &"
										+ ((Player) theApp.getPlayerList().get(
												pindex)).getName());
						for (int tempindex = 0; tempindex < tempHand
								.getNumHole(); tempindex++) {
							Card c = tempHand.getHoleCard(tempindex);
							((Player) theApp.getPlayerList().get(pindex))
									.getHand().addHoleCard(c);
							theApp.messageToPlayer(((Player) theApp
									.getPlayerList().get(pindex)).getName(),
									"CARD HOLE  &"
											+ ((Player) theApp.getPlayerList()
													.get(pindex)).getName()
											+ "&" + tempindex + "&" + c);
						}
					}
				}
			}
		}
	}
}