/******************************************************************************************
 * Pineapple.java                  PokerApp                                               *
 *                                                                                        *
 *   Revision History                                                                     *
 * +---------+----------+---------------------------------------------------------------+ *
 * | Version | DATE     | Description                                                   | *
 * +---------+----------+---------------------------------------------------------------+ *
 * |  0.99   | 05/22/05 | Initial documented release                                    | *
 * |  1.00   | 06/06/05 | Added logging                                                 | *
 * |  1.00   | 07/31/07 | Prepare for open source.  Header/comments/package/etc...      | *
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
 * Pineapple (Texas Hold'em Variant) game definition.
 * 
 * @author Dan Puperi
 * @version 1.00
 * 
 **/
public class Pineapple extends TexasHoldEm {

	/**
	 * Defines who has discared their card
	 **/
	protected boolean discarded[];
	/**
	 * Has everybody discarded a card
	 **/
	protected boolean allDiscarded;
	/**
	 * Index of cards that each player wants to drop
	 **/
	protected int cardToDrop[];

	/***********************
	 * Constructor creates an instance of a game of Pineapple
	 * 
	 * @param a
	 *            The StartPoker instance to which this game belongs
	 * 
	 **/
	public Pineapple(StartPoker a) {
		super(a, "Pineapple", false);
		maxActionNum = 6;
		if (theApp.getPlayerList().size() < 2) {
			return;
		}
		theApp.log("Construction a game of Pineapple", 3);
		discarded = new boolean[MAX_PLAYERS];
		cardToDrop = new int[MAX_PLAYERS];
		for (int i = 0; i < theApp.getPlayerList().size(); i++) {
			discarded[i] = false;
		}
		for (int i = 0; i < theApp.getPlayerList().size(); i++) {
			cardToDrop[i] = -1;
		}
		allDiscarded = false;
		requiresInteraction = true;
		deal();
	}

	/***********************
	 * deal() deals the initial cards
	 **/
	protected void deal() {
		theApp.log("Pineapple.deal()", 3);
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

		theApp.broadcastMessage("MESSAGE  &Playing Pineapple.  "
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
		theApp.log(
				"Pineapple.nextAction() - previous actionNum = " + actionNum, 3);
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
			theApp.broadcastMessage("MESSAGE &Select one card to drop and press the Done button.");
			dropCard();
			break;
		case 3:
			flop();
			bestPossible = calcBestPossible();
			break;
		case 4:
			turn();
			bestPossible = calcBestPossible();
			break;
		case 5:
			river();
			bestPossible = calcBestPossible();
			break;
		case 6:
			show();
			break;
		default:
			break;
		}
	}

	/***********************
	 * dropCard() defines what happens when its time for all players to discard
	 * their cards.
	 **/
	protected void dropCard() {
		theApp.log("Pineapple.dropCard()", 3);
		allDiscarded = true;
		for (int i = 0; i < theApp.getPlayerList().size(); i++) {
			if ((((Player) theApp.getPlayerList().get(i)).in)
					&& (!discarded[i])) {
				allDiscarded = false;
			}
		}

		if (!allDiscarded) {
			theApp.broadcastMessage("DISPLAY NOTHING");
			for (int i = 0; i < theApp.getPlayerList().size(); i++) {
				if ((((Player) theApp.getPlayerList().get(i)).in)
						&& (!discarded[i])) {
					theApp.messageToPlayer(((Player) theApp.getPlayerList()
							.get(i)).getName(), "DISPLAY DONE BUTTON");
				}
			}
		} else {
			allDiscarded = false;
			theApp.broadcastMessage("DISPLAY NOTHING");
			for (int i = 0; i < theApp.getPlayerList().size(); i++) {
				discarded[i] = false;
				Player p = (Player) theApp.getPlayerList().get(i);
				theApp.broadcastMessage("PLAYER NOPICS &" + p.getName());
				if (p.in) {
					int numCards = p.getHand().getNumHole();
					if ((cardToDrop[i] >= 0) && (cardToDrop[i] < numCards)) {
						Card holeCards[] = new Card[numCards - 1];
						int cnum = 0;
						for (int c = 0; c < numCards; c++) {
							if (c != cardToDrop[i]) {
								holeCards[cnum] = ((Player) theApp
										.getPlayerList().get(i)).getHand()
										.getHoleCard(c);
								cnum++;
							}
						}
						((Player) theApp.getPlayerList().get(i)).getHand()
								.clearHand();
						theApp.messageToPlayer(p.getName(),
								"PLAYER CLEAR HAND &" + p.getName());
						for (int c = 0; c < holeCards.length; c++) {
							((Player) theApp.getPlayerList().get(i)).getHand()
									.addHoleCard(holeCards[c]);
							theApp.messageToPlayer(p.getName(), "CARD HOLE  &"
									+ p.getName() + "&" + c + "&"
									+ holeCards[c]);
							theApp.broadcastMessage("CARD HOLE  &"
									+ p.getName() + "&" + c);
						}
					}
				}
			}
			nextAction();
		}
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
		theApp.log(
				"Pineapple.mouseClick( " + name + ", " + x + ", " + y + " )", 3);
		int pindex = theApp.playerIndex(name);
		int loc = ((Player) theApp.getPlayerList().get(pindex)).seat;

		// Players selecting which card to drop
		//
		if (actionNum == 2) {

			// Player has clicked the "Done" button in the middle of the screen.
			//
			if ((x >= 340) && (x <= 340 + 150) && (y >= 200 + 23)
					&& (y <= 200 + 23 + 50)) {
				if (cardToDrop[pindex] >= 0) {
					theApp.log(name + " has selected a card to drop.", 1);
					theApp.messageToPlayer(name,
							"MESSAGE &Waiting for everyone else to select a card to drop.");
					discarded[pindex] = true;
					dropCard();
					return;
				} else {
					theApp.messageToPlayer(name,
							"MESSAGE &You must select a card to drop.");
				}
			}

			// Check to see if player has clicked to select or deselect any of
			// his cards.
			//
			for (int cardNum = 0; cardNum < 3; cardNum++) {
				int width = 29;
				if (cardNum == 2) {
					width = 71;
				}
				if ((x >= PokerApp.LOCATION[loc][0] + 30 * cardNum)
						&& (x <= PokerApp.LOCATION[loc][0] + 30 * cardNum
								+ width)
						&& (y >= PokerApp.LOCATION[loc][1] + 15)
						&& (y <= PokerApp.LOCATION[loc][1] + 23 + 96)) {
					if (cardToDrop[pindex] < 0) {
						theApp.messageToPlayer(name, "SELECT CARD &" + cardNum);
						cardToDrop[pindex] = cardNum;
					} else if (cardToDrop[pindex] == cardNum) {
						theApp.messageToPlayer(name, "DESELECT CARD &"
								+ cardNum);
						cardToDrop[pindex] = -1;
					} else {
						theApp.messageToPlayer(name, "DESELECT CARD &"
								+ cardToDrop[pindex]);
						theApp.messageToPlayer(name, "SELECT CARD &" + cardNum);
						cardToDrop[pindex] = cardNum;
					}
				}
			}
		}
	}
}