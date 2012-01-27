/******************************************************************************************
 * OmahaHoldEm.java                PokerApp                                               *
 *                                                                                        *
 *   Revision History                                                                     *
 * +---------+----------+---------------------------------------------------------------+ *
 * | Version | DATE     | Description                                                   | *
 * +---------+----------+---------------------------------------------------------------+ *
 * |  0.95   | 09/15/04 | Initial documented release                                    | *
 * |  0.96   | 09/27/04 | Add calcBestPossible routine to help the AI logic             | *
 * |  0.98   | 12/08/04 | Minor placeholder.  AI tries to determine its best hand by    | *
 * |         |          | using the bestHand() routine. Problem is with less than all   | *
 * |         |          | the cards, this fails miserably.  Set best = 0 in that case   | *
 * |  0.99   | 05/17/05 | Added logic to PLAYER TURN call for structured betting games  | *
 * |  0.99   | 05/19/05 | Define showPlayer() and move showCards() to PokerGame class   | *
 * |  0.99   | 05/22/05 | Remove the deal() call from the overloaded constructor.       | *
 * |  1.00   | 06/06/05 | Added logging.                                                | *
 * |  1.00   | 07/22/07 | Prepare for open source.  Header/comments/package/etc...      | *
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
 * Omaha Hold'Em game definition.
 * 
 * @author Dan Puperi
 * @version 1.00
 * 
 **/
public class OmahaHoldEm extends PokerGame {

	/***********************
	 * Constructor - creates a new OmahaHoldEm game
	 * 
	 * @param a
	 *            The StartPoker instance to which this game belongs
	 * 
	 **/
	public OmahaHoldEm(StartPoker a) {
		super(a, "OmahaHoldEm", false);
		maxActionNum = 5;
		if (theApp.getPlayerList().size() < 2) {
			return;
		}
		theApp.log("Constructing a game of Omaha Hold'Em", 3);
		deal();
	}

	/***********************
	 * Constructor - creates a new OmahaHoldEm game This constructor is so Omaha
	 * Hold'Em can be extened with other game variations
	 * 
	 * @param a
	 *            The StartPoker instance to which this game belongs
	 * @param gameName
	 *            The name of the game to play
	 * @param selfInit
	 *            Whether this game is able to initialize itself
	 * 
	 **/
	public OmahaHoldEm(StartPoker a, String gameName, boolean selfInit) {
		super(a, gameName, selfInit);
		maxActionNum = 5;
		if (theApp.getPlayerList().size() < 2) {
			return;
		}
		theApp.log("Constructing a game of Omaha Hold'Em", 3);
	}

	/***********************
	 * deal() deals the initial cards
	 **/
	protected void deal() {
		theApp.log("OmahaHoldEm.deal()", 3);
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

		theApp.broadcastMessage("MESSAGE  &Playing Omaha Hold'Em.  "
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
		theApp.log("OmahaHoldEm.nextAction() - previous actionNum = "
				+ actionNum, 3);
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
			flop();
			bestPossible = calcBestPossible();
			break;
		case 3:
			turn();
			bestPossible = calcBestPossible();
			break;
		case 4:
			river();
			bestPossible = calcBestPossible();
			break;
		case 5:
			show();
			break;
		default:
			break;
		}
	}

	/***********************
	 * flop() deals the Flop - 3 shared cards
	 **/
	private void flop() {
		theApp.log("OmahaHoldEm.flop()", 3);
		Card c = new Card();
		theApp.getDeck().deal();
		c = theApp.getDeck().deal();
		theApp.broadcastMessage("CARD SHARED  &Table&0&" + c);
		for (int i = 0; i < theApp.getPlayerList().size(); i++) {
			((Player) theApp.getPlayerList().get(i)).getHand().addSharedCard(c);
		}

		c = theApp.getDeck().deal();
		theApp.broadcastMessage("CARD SHARED  &Table&1&" + c);
		for (int i = 0; i < theApp.getPlayerList().size(); i++) {
			((Player) theApp.getPlayerList().get(i)).getHand().addSharedCard(c);
		}

		c = theApp.getDeck().deal();
		theApp.broadcastMessage("CARD SHARED  &Table&2&" + c);
		for (int i = 0; i < theApp.getPlayerList().size(); i++) {
			((Player) theApp.getPlayerList().get(i)).getHand().addSharedCard(c);
		}

		theApp.broadcastMessage("MESSAGE  &"
				+ ((Player) theApp.getPlayerList().get(currPlayerIndex))
						.getName() + "'s bet.");
		theApp.broadcastMessage("PLAYER TURN &"
				+ ((Player) theApp.getPlayerList().get(currPlayerIndex))
						.getName());
	}

	/***********************
	 * turn() deals the 4th shared card
	 **/
	private void turn() {
		theApp.log("OmahaHoldEm.turn()", 3);
		Card c = new Card();
		theApp.getDeck().deal();
		c = theApp.getDeck().deal();
		theApp.broadcastMessage("CARD SHARED  &Table&3&" + c);
		for (int i = 0; i < theApp.getPlayerList().size(); i++) {
			((Player) theApp.getPlayerList().get(i)).getHand().addSharedCard(c);
		}

		theApp.broadcastMessage("MESSAGE  &"
				+ ((Player) theApp.getPlayerList().get(currPlayerIndex))
						.getName() + "'s bet.");
		theApp.broadcastMessage("PLAYER TURN &"
				+ ((Player) theApp.getPlayerList().get(currPlayerIndex))
						.getName());
	}

	/***********************
	 * river() deals the 5th and final shared card
	 **/
	private void river() {
		theApp.log("OmahaHoldEm.river()", 3);
		Card c = new Card();
		theApp.getDeck().deal();
		c = theApp.getDeck().deal();
		theApp.broadcastMessage("CARD SHARED  &Table&4&" + c);
		for (int i = 0; i < theApp.getPlayerList().size(); i++) {
			((Player) theApp.getPlayerList().get(i)).getHand().addSharedCard(c);
		}

		theApp.broadcastMessage("MESSAGE  &"
				+ ((Player) theApp.getPlayerList().get(currPlayerIndex))
						.getName() + "'s bet.");
		theApp.broadcastMessage("PLAYER TURN &"
				+ ((Player) theApp.getPlayerList().get(currPlayerIndex))
						.getName());
	}

	/***********************
	 * firstToBet() determines who gets to bet first - calls the function common
	 * to all HoldEm type games in PokerGame class
	 * 
	 * @return The player index of the first player to bet this round.
	 * 
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
			theApp.broadcastMessage("SHOW CARD &" + p.getName() + "&0&"
					+ p.getHand().getHoleCard(0));
			theApp.broadcastMessage("SHOW CARD &" + p.getName() + "&1&"
					+ p.getHand().getHoleCard(1));
			theApp.broadcastMessage("SHOW CARD &" + p.getName() + "&2&"
					+ p.getHand().getHoleCard(2));
			theApp.broadcastMessage("SHOW CARD &" + p.getName() + "&3&"
					+ p.getHand().getHoleCard(3));
		} else {
			theApp.broadcastMessage("SHOW CARD &" + p.getName() + "&0");
			theApp.broadcastMessage("SHOW CARD &" + p.getName() + "&1");
			theApp.broadcastMessage("SHOW CARD &" + p.getName() + "&2");
			theApp.broadcastMessage("SHOW CARD &" + p.getName() + "&3");
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
		theApp.log("OmahaHoldEm.bestHand( " + h + " )", 3);
		int numHoleCards = h.getNumHole();
		int numShareCards = h.getNumShared();
		Card[] chole = new Card[numHoleCards];
		Card[] cshare = new Card[numShareCards];
		float best = 0.0f;
		float test = 0.0f;
		HandEvaluator he = new HandEvaluator();

		if (numShareCards < 3) {
			return best;
		}

		for (int i = 0; i < numHoleCards; i++) {
			if (h.getHoleCard(i) != null) {
				chole[i] = h.getHoleCard(i);
			}
		}

		for (int i = 0; i < numShareCards; i++) {
			if (h.getSharedCard(i) != null) {
				cshare[i] = h.getSharedCard(i);
			}
		}

		best = 0.0f;
		for (int i = 0; i < numHoleCards - 1; i++) {
			for (int j = i + 1; j < numHoleCards; j++) {
				for (int a = 0; a < numShareCards - 2; a++) {
					for (int b = a + 1; b < numShareCards - 1; b++) {
						for (int c = b + 1; c < numShareCards; c++) {
							test = he.rankHand(chole[i], chole[j], cshare[a],
									cshare[b], cshare[c]);
							if (test > best) {
								best = test;
							}
						}
					}
				}
			}
		}
		return best;
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
		float best = 29.9f;
		float test = 0.0f;

		// First need to find shared cards.
		// If can't find any - the high pair is the best possible hand.
		//
		int numShareCards = 0;
		int playerNum = 0;
		for (int i = 0; i < theApp.getPlayerList().size(); i++) {
			if (((Player) theApp.getPlayerList().get(i)).getHand() != null) {
				int n = ((Player) theApp.getPlayerList().get(i)).getHand()
						.getNumShared();
				if (n > 0) {
					numShareCards = n;
					playerNum = i;
				}
			}
		}
		if (numShareCards == 0) {
			return best;
		}

		Card[] cshare = new Card[numShareCards];
		HandEvaluator he = new HandEvaluator();

		for (int i = 0; i < numShareCards; i++) {
			cshare[i] = ((Player) theApp.getPlayerList().get(playerNum))
					.getHand().getSharedCard(i);
		}

		// Now start checking for best possible hands
		//
		// Straight flush
		//
		for (int i = 0; i < numShareCards - 2; i++) {
			for (int j = i + 1; j < numShareCards - 1; j++) {
				for (int k = j + 1; k < numShareCards; k++) {
					float straight = he.isStraight(cshare[i], cshare[j],
							cshare[k]);
					if (he.isFlush(cshare[i], cshare[j], cshare[k])
							&& (straight > 0.0f)) {
						test = 120.0f + straight;
						if (test > best) {
							best = test;
						}
					}
				}
			}
		}
		if (best >= 120.0f) {
			return best;
		}

		// Four of a kind
		//
		for (int i = 0; i < numShareCards - 1; i++) {
			for (int j = i + 1; j < numShareCards; j++) {
				if (cshare[i].getRank() == cshare[j].getRank()) {
					for (int k = 0; k < numShareCards; k++) {
						if ((k != i) && (k != j)) {
							test = 105.0f + he.rankHand(cshare[i])
									+ he.kicker(cshare[k]);
							if (test > best) {
								best = test;
							}
						}
					}
				}
				for (int k = 0; k < numShareCards; k++) {
					if ((k != i) && (k != j)
							&& (cshare[j].getRank() == cshare[k].getRank())) {
						test = 105.0f + he.rankHand(cshare[j])
								+ he.kicker(cshare[i]);
						if (test > best) {
							best = test;
						}
					}
				}
			}
		}
		if (best >= 105.0f) {
			return best;
		}

		//
		// If the full house is possible, then the 4 of a kind is possible - so
		// skip the full house test.
		//
		// Flush
		//
		for (int i = 0; i < numShareCards - 2; i++) {
			for (int j = i + 1; j < numShareCards - 1; j++) {
				for (int k = j + 1; k < numShareCards; k++) {
					if (he.isFlush(cshare[i], cshare[j], cshare[k])) {
						test = 89.0f + he.kicker(
								new Card(Card.KING, cshare[i].getSuit()),
								cshare[i], cshare[j], cshare[k]);
						if (test > best) {
							best = test;
						}
					}
				}
			}
		}
		if (best >= 75.0f) {
			return best;
		}

		// Straight
		//
		for (int i = 0; i < numShareCards - 2; i++) {
			for (int j = i + 1; j < numShareCards - 1; j++) {
				for (int k = j + 1; k < numShareCards; k++) {
					float straight = he.isStraight(cshare[i], cshare[j],
							cshare[k]);
					if (straight > 0.0f) {
						test = 60.0f + straight;
						if (test > best) {
							best = test;
						}
					}
				}
			}
		}
		if (best >= 60.0f) {
			return best;
		}

		// Three of a kind
		//
		for (int i = 0; i < numShareCards - 2; i++) {
			for (int j = i + 1; j < numShareCards - 1; j++) {
				for (int k = j + 1; k < numShareCards; k++) {
					if ((he.rankHand(cshare[i]) >= he.rankHand(cshare[j]))
							&& (he.rankHand(cshare[i]) >= he
									.rankHand(cshare[k]))) {
						test = 45.0f + he.rankHand(cshare[i])
								+ he.kicker(cshare[j], cshare[k]);
						if (test > best) {
							best = test;
						}
					} else if (he.rankHand(cshare[j]) >= he.rankHand(cshare[k])) {
						test = 45.0f + he.rankHand(cshare[j])
								+ he.kicker(cshare[i], cshare[k]);
						if (test > best) {
							best = test;
						}
					} else {
						test = 45.0f + he.rankHand(cshare[k])
								+ he.kicker(cshare[i], cshare[j]);
						if (test > best) {
							best = test;
						}
					}
				}
			}
		}
		return best;
	}

	/***********************
	 * mouseClick() handles mouse click events - none for this game.
	 **/
	protected void mouseClick(String name, int x, int y) {
	}
}
