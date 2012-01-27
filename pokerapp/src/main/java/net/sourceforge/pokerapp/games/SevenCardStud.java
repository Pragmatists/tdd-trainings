/******************************************************************************************
 * SevenCardStud.java              PokerApp                                               *
 *                                                                                        *
 *   Revision History                                                                     *
 * +---------+----------+---------------------------------------------------------------+ *
 * | Version | DATE     | Description                                                   | *
 * +---------+----------+---------------------------------------------------------------+ *
 * |  0.95   | 09/15/04 | Initial documented release                                    | *
 * |  0.96   | 09/27/04 | Add calcBestPossible routine to help the AI logic             | *
 * |  0.97   | 11/01/04 | Fixed minor change for derived game name getting displayed.   | *
 * |  0.99   | 05/17/05 | Added logic to PLAYER TURN call for structured betting games  | *
 * |  0.99   | 05/19/05 | Define showPlayer() and move showCards() to PokerGame class   | *
 * |  0.99   | 05/24/05 | Make some functions protected instead of private              | *
 * |  1.00   | 06/06/05 | Added logging                                                 | *
 * |  1.00   | 07/31/07 | Prepare for open source.  Header/comments/package/etc...      | *
 * |  1.00   | 08/02/07 | Big blind player should have raise enabled                    | *
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
import java.util.ArrayList;

/****************************************************
 * Seven Card Stud game definition.
 * 
 * @author Dan Puperi
 * @version 1.00
 * 
 **/
public class SevenCardStud extends PokerGame {

	private ArrayList cardsSeen; // Defines which cards have been seen by
									// everyone - used in logic to determine
									// best hand

	/***********************
	 * Constructor creates an instance of a game of Seven Card Stud
	 * 
	 * @param a
	 *            The StartPoker instance to which this game belongs
	 * 
	 **/
	public SevenCardStud(StartPoker a) {
		super(a, "Seven Card Stud", true);
		maxActionNum = 6;
		if (theApp.getPlayerList().size() < 2) {
			return;
		}
		theApp.log("Constructing a game of Seven Card Stud.", 3);
		cardsSeen = new ArrayList();

		deal();
		currPlayerIndex = firstToBet();
		theApp.broadcastMessage("MESSAGE  &Playing Seven Card Stud.  "
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
		if (!theApp.blinds) {
			highBettor = (Player) theApp.getPlayerList().get(currPlayerIndex);
		}
		if (highBettor != null) {
			theApp.updateMoneyLine(currBet, highBettor.getName());
		}
	}

	/***********************
	 * Constructor creates an instance of a game of Seven Card Stud This
	 * constructor is used by other games which can extends this game
	 * 
	 * @param a
	 *            The StartPoker instance to which this game belongs
	 * @param gameName
	 *            The name of the game
	 * @param selfInit
	 *            Whether or not the game can initialize itself.
	 * 
	 **/
	public SevenCardStud(StartPoker a, String gameName, boolean selfInit) {
		super(a, gameName, selfInit);
		maxActionNum = 6;
		if (theApp.getPlayerList().size() < 2) {
			return;
		}
		theApp.log("Constructing a game of Seven Card Stud.", 3);
		cardsSeen = new ArrayList();

		deal();
		currPlayerIndex = firstToBet();
		theApp.broadcastMessage("MESSAGE  &"
				+ gameName
				+ " "
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
		if (!theApp.blinds) {
			highBettor = (Player) theApp.getPlayerList().get(currPlayerIndex);
		}
		if (highBettor != null) {
			theApp.updateMoneyLine(currBet, highBettor.getName());
		}
	}

	/***********************
	 * deal() deals the initial cards
	 **/
	protected void deal() {
		theApp.log("SevenCardStud.deal()", 3);
		Card c = new Card();

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

		// Third card up
		for (int i = 0; i < theApp.getPlayerList().size(); i++) {
			c = theApp.getDeck().deal();
			((Player) theApp.getPlayerList().get(i)).getHand().addUpCard(c);
			theApp.broadcastMessage("CARD UP  &"
					+ ((Player) theApp.getPlayerList().get(i)).getName()
					+ "&2&" + c);
			cardsSeen.add(c);
		}
		bestPossible = calcBestPossible();
	}

	/***********************
	 * nextAction() determines what happens next
	 **/
	protected void nextAction() {
		theApp.log("SevenCardStud.nextAction() - previous actionNum = "
				+ actionNum, 3);
		actionNum++;
		for (int i = 0; i < theApp.getPlayerList().size(); i++) {
			((Player) theApp.getPlayerList().get(i)).potOK = false;
			((Player) theApp.getPlayerList().get(i)).setBet(0.0f);
			((Player) theApp.getPlayerList().get(i)).setPrevBet(0.0f);
		}
		switch (actionNum) {
		case 2:
			card4();
			bestPossible = calcBestPossible();
			break;
		case 3:
			card5();
			bestPossible = calcBestPossible();
			break;
		case 4:
			card6();
			bestPossible = calcBestPossible();
			break;
		case 5:
			card7();
			bestPossible = calcBestPossible();
			break;
		case 6:
			show();
			break;
		default:
			break;
		}
		currPlayerIndex = firstToBet();
		highBettor = (Player) theApp.getPlayerList().get(currPlayerIndex);
		currBet = new PokerMoney(0.0f);
		theApp.updateMoneyLine(currBet, highBettor.getName());
	}

	/***********************
	 * card4() deals the 4th card up
	 **/
	protected void card4() {
		theApp.log("SevenCardStud.card4()", 3);
		Card c = new Card();
		for (int i = 0; i < theApp.getPlayerList().size(); i++) {
			if (((Player) theApp.getPlayerList().get(i)).in) {
				c = theApp.getDeck().deal();
				((Player) theApp.getPlayerList().get(i)).getHand().addUpCard(c);
				theApp.broadcastMessage("CARD UP  &"
						+ ((Player) theApp.getPlayerList().get(i)).getName()
						+ "&3&" + c);
				cardsSeen.add(c);
			}
		}
		currPlayerIndex = firstToBet();
		theApp.broadcastMessage("MESSAGE  &"
				+ ((Player) theApp.getPlayerList().get(currPlayerIndex))
						.getName() + "'s bet.");
		theApp.broadcastMessage("PLAYER TURN &"
				+ ((Player) theApp.getPlayerList().get(currPlayerIndex))
						.getName());
	}

	/***********************
	 * card5() deals the 5th card up
	 **/
	protected void card5() {
		theApp.log("SevenCardStud.card5()", 3);
		Card c = new Card();
		for (int i = 0; i < theApp.getPlayerList().size(); i++) {
			if (((Player) theApp.getPlayerList().get(i)).in) {
				c = theApp.getDeck().deal();
				((Player) theApp.getPlayerList().get(i)).getHand().addUpCard(c);
				theApp.broadcastMessage("CARD UP  &"
						+ ((Player) theApp.getPlayerList().get(i)).getName()
						+ "&4&" + c);
				cardsSeen.add(c);
			}
		}
		currPlayerIndex = firstToBet();
		theApp.broadcastMessage("MESSAGE  &"
				+ ((Player) theApp.getPlayerList().get(currPlayerIndex))
						.getName() + "'s bet.");
		theApp.broadcastMessage("PLAYER TURN &"
				+ ((Player) theApp.getPlayerList().get(currPlayerIndex))
						.getName());
	}

	/***********************
	 * card6() deals the 6th card up
	 **/
	protected void card6() {
		theApp.log("SevenCardStud.card6()", 3);
		Card c = new Card();
		for (int i = 0; i < theApp.getPlayerList().size(); i++) {
			if (((Player) theApp.getPlayerList().get(i)).in) {
				c = theApp.getDeck().deal();
				((Player) theApp.getPlayerList().get(i)).getHand().addUpCard(c);
				theApp.broadcastMessage("CARD UP  &"
						+ ((Player) theApp.getPlayerList().get(i)).getName()
						+ "&5&" + c);
				cardsSeen.add(c);
			}
		}
		currPlayerIndex = firstToBet();
		theApp.broadcastMessage("MESSAGE  &"
				+ ((Player) theApp.getPlayerList().get(currPlayerIndex))
						.getName() + "'s bet.");
		theApp.broadcastMessage("PLAYER TURN &"
				+ ((Player) theApp.getPlayerList().get(currPlayerIndex))
						.getName());
	}

	/***********************
	 * card7() deals the last card face down
	 **/
	protected void card7() {
		theApp.log("SevenCardStud.card7()", 3);
		Card c = new Card();
		for (int i = 0; i < theApp.getPlayerList().size(); i++) {
			if (((Player) theApp.getPlayerList().get(i)).in) {
				c = theApp.getDeck().deal();
				((Player) theApp.getPlayerList().get(i)).getHand().addHoleCard(
						c);
				theApp.messageToPlayer(
						((Player) theApp.getPlayerList().get(i)).getName(),
						"CARD HOLE  &"
								+ ((Player) theApp.getPlayerList().get(i))
										.getName() + "&6&" + c);
				theApp.broadcastMessage("CARD HOLE  &"
						+ ((Player) theApp.getPlayerList().get(i)).getName()
						+ "&6");
			}
		}
		currPlayerIndex = firstToBet();
		theApp.broadcastMessage("MESSAGE  &"
				+ ((Player) theApp.getPlayerList().get(currPlayerIndex))
						.getName() + "'s bet.");
		theApp.broadcastMessage("PLAYER TURN &"
				+ ((Player) theApp.getPlayerList().get(currPlayerIndex))
						.getName());
	}

	/***********************
	 * firstToBet() determines who gets to bet first - calls the function common
	 * to all Stud type games in PokerGame class
	 * 
	 * @return The player index of the play who will bet first this round
	 * 
	 **/
	protected int firstToBet() {
		return firstToBet_Stud();
	}

	/***********************
	 * showPlayer() defines how a player shows their cards
	 * 
	 * @param p
	 *            The player who is going to show
	 * @param b
	 *            Whether or not this player has to turn his cards face up
	 * 
	 **/
	protected void showPlayer(Player p, boolean b) {
		if (b) {
			theApp.broadcastMessage("SHOW CARD &" + p.getName() + "&0&"
					+ p.getHand().getHoleCard(0));
			theApp.broadcastMessage("SHOW CARD &" + p.getName() + "&1&"
					+ p.getHand().getHoleCard(1));
			theApp.broadcastMessage("SHOW CARD &" + p.getName() + "&2&"
					+ p.getHand().getUpCard(0));
			theApp.broadcastMessage("SHOW CARD &" + p.getName() + "&3&"
					+ p.getHand().getUpCard(1));
			theApp.broadcastMessage("SHOW CARD &" + p.getName() + "&4&"
					+ p.getHand().getUpCard(2));
			theApp.broadcastMessage("SHOW CARD &" + p.getName() + "&5&"
					+ p.getHand().getUpCard(3));
			theApp.broadcastMessage("SHOW CARD &" + p.getName() + "&6&"
					+ p.getHand().getHoleCard(2));
		} else {
			theApp.broadcastMessage("SHOW CARD &" + p.getName() + "&0");
			theApp.broadcastMessage("SHOW CARD &" + p.getName() + "&1");
			theApp.broadcastMessage("SHOW CARD &" + p.getName() + "&2");
			theApp.broadcastMessage("SHOW CARD &" + p.getName() + "&3");
			theApp.broadcastMessage("SHOW CARD &" + p.getName() + "&4");
			theApp.broadcastMessage("SHOW CARD &" + p.getName() + "&5");
			theApp.broadcastMessage("SHOW CARD &" + p.getName() + "&6");
		}
	}

	/***********************
	 * bestHand() returns the float value of the best possible hand that can be
	 * made from given hand h.
	 * 
	 * @param h
	 *            The hand to evaluate
	 * @return The float value of the best five card hand out of this hand.
	 * 
	 **/
	public float bestHand(Hand h) {
		theApp.log("SevenCardStud.bestHand( " + h + " )", 3);
		int numCards = h.getNumHole() + h.getNumUp();
		Card[] c = new Card[numCards];
		float best = 0.0f;
		float test = 0.0f;
		int[] ci = new int[5];
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
		} else if (numCards == 0) {
			return 0.0f;
		} else if (numCards >= 6) {
			for (int i = 0; i < numCards - 4; i++) {
				for (int j = i + 1; j < numCards - 3; j++) {
					for (int k = j + 1; k < numCards - 2; k++) {
						for (int l = k + 1; l < numCards - 1; l++) {
							for (int m = l + 1; m < numCards; m++) {
								test = he
										.rankHand(c[i], c[j], c[k], c[l], c[m]);
								if (test > best) {
									best = test;
									ci[0] = i;
									ci[1] = j;
									ci[2] = k;
									ci[3] = l;
									ci[4] = m;
								}
							}
						}
					}
				}
			}
		}

		return he.rankHand(c[ci[0]], c[ci[1]], c[ci[2]], c[ci[3]], c[ci[4]]);
	}

	/***********************
	 * calcBestPossible() determines the best possible hand available based on
	 * the shared cards. This function is mianly to aid the computer AI logic.
	 * 
	 * @return The float value of the best possible hand somebody can have.
	 * 
	 **/
	protected float calcBestPossible() {
		return 150.0f;
	}

	/***********************
	 * mouseClick() handles mouse click events - none for this game.
	 **/
	protected void mouseClick(String name, int x, int y) {
	}
}