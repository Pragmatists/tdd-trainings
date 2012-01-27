/******************************************************************************************
 * Omaha8.java                     PokerApp                                               *
 *                                                                                        *
 *   Revision History                                                                     *
 * +---------+----------+---------------------------------------------------------------+ *
 * | Version | DATE     | Description                                                   | *
 * +---------+----------+---------------------------------------------------------------+ *
 * |  0.99   | 05/22/05 | Initial documented release                                    | *
 * |  1.00   | 06/06/05 | Added logging.                                                | *
 * |  1.00   | 07/22/07 | Prepare for open source.  Header/comments/package/etc...      | *
 * |  1.00   | 08/02/07 | Fixed logic that calculated the low hand.  Was not thorough   | *
 * |         |          | enough previously.                                            | *
 * |  1.00   | 08/02/07 | Fixed when same player wins high and low - it will not longer | *
 * |         |          | each gets "$X.XX" - it will just say total player won.        | *
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
 * Omaha 8 game definition - Hi/Low Split with an 8 or better qualifier
 * 
 * @author Dan Puperi
 * @version 1.00
 * 
 **/
public class Omaha8 extends OmahaHoldEm {

	/**
	 * Qualifier rank
	 **/
	public static int QUALIFIER;
	private int lowWinners[]; // Array of index of the winners of the low part
								// of the pot.
	private int numLowWinners; // Number of players sharing in the low.

	/***********************
	 * Constructor creates a Omaha8 game
	 * 
	 * @param a
	 *            The StartPoker instance to which this game belongs
	 * 
	 **/
	public Omaha8(StartPoker a) {
		super(a, "Omaha 8 High/Low Split", false);
		if (theApp.getPlayerList().size() < 2) {
			return;
		}
		theApp.log("Constructing a game of Omaha8", 3);
		numLowWinners = 0;
		lowWinners = new int[MAX_PLAYERS];
		QUALIFIER = Card.EIGHT;
		deal();
	}

	/***********************
	 * lowHand() calculates the low hand score for a player
	 * 
	 * @param h
	 *            The hand for which to look for the low.
	 * @return The float value of the lowest possible hand.
	 * 
	 **/
	protected float lowHand(Hand h) {
		theApp.log("Omaha8.lowHand( " + h + " )", 3);
		int numHoleCards = h.getNumHole();
		int numShareCards = h.getNumShared();
		Card[] chole = new Card[numHoleCards];
		Card[] cshare = new Card[numShareCards];
		float best = (float) QUALIFIER + 2.0f;
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
		//
		// Sort the cards in order of rank.
		//
		for (int i = 0; i < numShareCards; i++) {
			for (int j = i; j < numShareCards; j++) {
				if (cshare[j].getRank() < cshare[i].getRank()) {
					Card temp = cshare[i];
					cshare[i] = cshare[j];
					cshare[j] = temp;
				}
			}
		}

		for (int i = 0; i < numHoleCards; i++) {
			for (int j = i; j < numHoleCards; j++) {
				if (chole[j].getRank() < chole[i].getRank()) {
					Card temp = chole[i];
					chole[i] = chole[j];
					chole[j] = temp;
				}
			}
		}
		//
		// Check to see if player can even make a low hand
		//
		int num = 0;
		Card[] lowCards = new Card[5];
		for (int i = 0; i < numHoleCards; i++) {
			if (chole[i].getRank() <= QUALIFIER) {
				int num_shared_nopair_qual = 0;
				for (int j = 0; j < numShareCards; j++) {
					if (cshare[j].getRank() <= QUALIFIER) {
						if (chole[i].getRank() != cshare[j].getRank()) {
							boolean bogus = false;
							for (int k = 0; k < j; k++) {
								if (cshare[j].getRank() == cshare[k].getRank()) {
									bogus = true;
								}
							}
							if (!bogus) {
								num_shared_nopair_qual++;
							}
						}
					}
				}
				boolean paired = false;
				for (int j = 0; j < num; j++) {
					if (chole[i].getRank() == lowCards[j].getRank()) {
						paired = true;
					}
				}
				if ((num_shared_nopair_qual >= 3) && (!paired) && (num < 2)) {
					lowCards[num] = chole[i];
					num++;
				}
			}
		}
		if (num < 2) {
			return best;
		}

		for (int i = 0; i < numShareCards; i++) {
			if (cshare[i].getRank() <= QUALIFIER) {
				boolean paired = false;
				for (int j = 0; j < num; j++) {
					if (cshare[i].getRank() == lowCards[j].getRank()) {
						paired = true;
					}
				}
				if (!paired && (num < 5)) {
					lowCards[num] = cshare[i];
					num++;
				}
			}
		}

		if (num < 5) {
			return best;
		}
		//
		// Sort all the used cards and give the hand a rating. Lowest rating
		// wins.
		//
		for (int i = 0; i < num; i++) {
			for (int j = i; j < num; j++) {
				if (lowCards[j].getRank() < lowCards[i].getRank()) {
					Card temp = lowCards[i];
					lowCards[i] = lowCards[j];
					lowCards[j] = temp;
				}
			}
		}
		best = (float) lowCards[4].getRank() + (float) lowCards[3].getRank()
				/ 10.0f + (float) lowCards[2].getRank() / 100.0f
				+ (float) lowCards[1].getRank() / 1000.0f
				+ (float) lowCards[0].getRank() / 10000.0f;

		return best;
	}

	/**********************
	 * show() called at the showdown
	 **/
	protected void show() {
		theApp.log("Omaha8.show()", 3);
		theApp.playedInLastGame = new ArrayList();
		for (int i = 0; i < theApp.getPlayerList().size(); i++) {
			theApp.playedInLastGame
					.add(((Player) theApp.getPlayerList().get(i)).getName());
		}
		theApp.broadcastMessage("MESSAGE &Calculating winner...");
		calcWinner();
		showCards();
		for (int i = 0; i < numLowWinners; i++) {
			showPlayer((Player) theApp.getPlayerList().get(lowWinners[i]), true);
		}
		theApp.resetPot();
		theApp.nextDealer();
		theApp.updateMoneyLine(new PokerMoney(0.0f), "  ");
		theApp.nullifyGame();
		theApp.broadcastMessage("DISABLE BUTTONS");
	}

	/**********************
	 * calcWinner() calculate the winner(s) of the game.
	 **/
	protected void calcWinner() {
		theApp.log("Omaha8.calcWinner()", 3);
		int highPlayer = 0;
		int lowPlayer = 0;
		float highHand = 0.0f;
		float lowHand = (float) QUALIFIER + 1.0f;
		String winningHand = new String();
		String lowestHand = new String();
		int numWinners = 0;
		int winner[] = new int[MAX_PLAYERS];
		playerHandValues = new float[MAX_PLAYERS];
		float lowHandValues[] = new float[MAX_PLAYERS];

		for (int i = 0; i < theApp.getPlayerList().size(); i++) {
			Player p = (Player) theApp.getPlayerList().get(i);
			if (p.in) {
				playerHandValues[i] = bestHand(p.getHand());
				lowHandValues[i] = lowHand(p.getHand());

				if (playerHandValues[i] > highHand) {
					highHand = playerHandValues[i];
					highPlayer = i;
				}
				if (lowHandValues[i] < lowHand) {
					lowHand = lowHandValues[i];
					lowPlayer = i;
				}
			}
		}
		for (int i = 0; i < theApp.getPlayerList().size(); i++) {
			Player p = (Player) theApp.getPlayerList().get(i);
			if (p.in) {
				if (playerHandValues[i] == highHand) {
					winner[numWinners] = i;
					numWinners++;
				}
				if (lowHandValues[i] == lowHand) {
					lowWinners[numLowWinners] = i;
					numLowWinners++;
				}
			}
		}
		winningHand = HandEvaluator.nameHand(highHand);
		minValueToShow = highHand;

		PokerMoney winnersTake = new PokerMoney(theApp.getPot().amount());
		if (sidePots.size() != 0) {
			for (int i = 0; i < sidePots.size(); i++) {
				winnersTake.subtract(((SidePot) sidePots.get(i)).getPot()
						.amount());
			}
		}

		if (numWinners == 1) {
			if (numLowWinners == 0) {
				theApp.log(
						""
								+ ((Player) (theApp.getPlayerList()
										.get(highPlayer))).getName()
								+ " wins with a " + winningHand
								+ ".  No low winner.  Pot = " + winnersTake, 1);
				theApp.broadcastMessage("MESSAGE &"
						+ ((Player) (theApp.getPlayerList().get(highPlayer)))
								.getName() + " wins with a " + winningHand
						+ ".  No low winner.  Pot = " + winnersTake);
				if (theApp.getServerWindow() != null) {
					theApp.getServerWindow()
							.sendMessage(
									""
											+ ((Player) (theApp.getPlayerList()
													.get(highPlayer)))
													.getName()
											+ " wins with a " + winningHand
											+ ".  No low winner.  Pot = "
											+ winnersTake);
				} else {
					System.out
							.println(""
									+ ((Player) (theApp.getPlayerList()
											.get(highPlayer))).getName()
									+ " wins with a " + winningHand
									+ ".  No low winner.  Pot = " + winnersTake);
				}
				((Player) theApp.getPlayerList().get(highPlayer))
						.add(winnersTake.amount());
				theApp.broadcastMessage("PLAYER CASH &"
						+ ((Player) (theApp.getPlayerList().get(highPlayer)))
								.getName()
						+ "&"
						+ ((Player) theApp.getPlayerList().get(highPlayer))
								.getBankroll().amount());
			} else if (numLowWinners == 1) {
				String resStr;
				if (((Player) theApp.getPlayerList().get(highPlayer))
						.getName()
						.equals(((Player) theApp.getPlayerList().get(lowPlayer))
								.getName())) {
					resStr = new String(
							""
									+ ((Player) theApp.getPlayerList().get(
											highPlayer)).getName()
									+ " wins with a " + winningHand
									+ " and also wins the low hand. Pot = "
									+ winnersTake);
					winnersTake.subtract(winnersTake.amount() / 2.0f);
				} else {
					winnersTake.subtract(winnersTake.amount() / 2.0f);
					resStr = new String(
							""
									+ ((Player) theApp.getPlayerList().get(
											highPlayer)).getName()
									+ " wins with a "
									+ winningHand
									+ ". "
									+ ((Player) theApp.getPlayerList().get(
											lowPlayer)).getName()
									+ " wins the low.  Each gets = "
									+ winnersTake);
				}
				theApp.log(resStr, 1);
				theApp.broadcastMessage("MESSAGE &" + resStr);
				if (theApp.getServerWindow() != null) {
					theApp.getServerWindow().sendMessage(resStr);
				} else {
					System.out.println(resStr);
				}
				((Player) theApp.getPlayerList().get(highPlayer))
						.add(winnersTake.amount());
				((Player) theApp.getPlayerList().get(lowPlayer))
						.add(winnersTake.amount());
				theApp.broadcastMessage("PLAYER CASH &"
						+ ((Player) (theApp.getPlayerList().get(highPlayer)))
								.getName()
						+ "&"
						+ ((Player) theApp.getPlayerList().get(highPlayer))
								.getBankroll().amount());
				theApp.broadcastMessage("PLAYER CASH &"
						+ ((Player) (theApp.getPlayerList().get(lowPlayer)))
								.getName()
						+ "&"
						+ ((Player) theApp.getPlayerList().get(lowPlayer))
								.getBankroll().amount());
			} else {
				PokerMoney highTake = new PokerMoney(
						winnersTake.amount() / 2.0f);
				PokerMoney lowTake = new PokerMoney(highTake.amount());

				StringBuffer names = new StringBuffer();
				names.append(((Player) theApp.getPlayerList()
						.get(lowWinners[0])).getName());
				PokerMoney money = new PokerMoney(lowTake.amount()
						/ (float) numLowWinners);
				((Player) theApp.getPlayerList().get(lowWinners[0])).add(money
						.amount());
				for (int i = 1; i < numLowWinners; i++) {
					names.append(" and "
							+ ((Player) (theApp.getPlayerList()
									.get(lowWinners[i]))).getName());
					((Player) theApp.getPlayerList().get(lowWinners[i]))
							.add(money.amount());
					theApp.broadcastMessage("PLAYER CASH &"
							+ ((Player) (theApp.getPlayerList()
									.get(lowWinners[i]))).getName()
							+ "&"
							+ ((Player) theApp.getPlayerList().get(
									lowWinners[i])).getBankroll().amount());
				}
				theApp.log(
						""
								+ ((Player) theApp.getPlayerList().get(
										highPlayer)).getName()
								+ " wins with a " + winningHand + " and gets "
								+ highTake + ".  " + names
								+ " split the low.  Each gets " + money + ".",
						1);
				theApp.broadcastMessage("MESSAGE &"
						+ ((Player) theApp.getPlayerList().get(highPlayer))
								.getName() + " wins with a " + winningHand
						+ " and gets " + highTake + ".  " + names
						+ " split the low.  Each gets " + money + ".");
				if (theApp.getServerWindow() != null) {
					theApp.getServerWindow().sendMessage(
							""
									+ ((Player) theApp.getPlayerList().get(
											highPlayer)).getName()
									+ " wins with a " + winningHand
									+ " and gets " + highTake + ".  " + names
									+ " split the low.  Each gets " + money
									+ ".");
				} else {
					System.out.println(""
							+ ((Player) theApp.getPlayerList().get(highPlayer))
									.getName() + " wins with a " + winningHand
							+ " and gets " + highTake + ".  " + names
							+ " split the low.  Each gets " + money + ".");
				}
				((Player) theApp.getPlayerList().get(highPlayer)).add(highTake
						.amount());
				theApp.broadcastMessage("PLAYER CASH &"
						+ ((Player) (theApp.getPlayerList().get(highPlayer)))
								.getName()
						+ "&"
						+ ((Player) theApp.getPlayerList().get(highPlayer))
								.getBankroll().amount());
			}
		} else {
			if (numLowWinners == 0) {
				StringBuffer names = new StringBuffer();
				names.append(((Player) theApp.getPlayerList().get(winner[0]))
						.getName());
				PokerMoney money = new PokerMoney(winnersTake.amount()
						/ (float) numWinners);
				((Player) theApp.getPlayerList().get(winner[0])).add(money
						.amount());
				for (int i = 1; i < numWinners; i++) {
					names.append(" and "
							+ ((Player) (theApp.getPlayerList().get(winner[i])))
									.getName());
					((Player) theApp.getPlayerList().get(winner[i])).add(money
							.amount());
					theApp.broadcastMessage("PLAYER CASH &"
							+ ((Player) (theApp.getPlayerList().get(winner[i])))
									.getName()
							+ "&"
							+ ((Player) theApp.getPlayerList().get(winner[i]))
									.getBankroll().amount());
				}
				theApp.log("Tie between " + names + " with a " + winningHand
						+ ".  No low winner.  Each player gets " + money + ".",
						1);
				theApp.broadcastMessage("MESSAGE &Tie between " + names
						+ " with a " + winningHand
						+ ".  No low winner.  Each player gets " + money + ".");
				if (theApp.getServerWindow() != null) {
					theApp.getServerWindow().sendMessage(
							"Tie between " + names + " with a " + winningHand
									+ ".  No low winner.  Each player gets "
									+ money + ".");
				} else {
					System.out.println("Tie between " + names + " with a "
							+ winningHand
							+ ".  No low winner.  Each player gets " + money
							+ ".");
				}
			} else if (numLowWinners == 1) {
				PokerMoney highTake = new PokerMoney(
						winnersTake.amount() / 2.0f);
				PokerMoney lowTake = new PokerMoney(highTake.amount());

				StringBuffer names = new StringBuffer();
				names.append(((Player) theApp.getPlayerList().get(winner[0]))
						.getName());
				PokerMoney money = new PokerMoney(highTake.amount()
						/ (float) numWinners);
				((Player) theApp.getPlayerList().get(winner[0])).add(money
						.amount());
				for (int i = 1; i < numWinners; i++) {
					names.append(" and "
							+ ((Player) (theApp.getPlayerList().get(winner[i])))
									.getName());
					((Player) theApp.getPlayerList().get(winner[i])).add(money
							.amount());
					theApp.broadcastMessage("PLAYER CASH &"
							+ ((Player) (theApp.getPlayerList().get(winner[i])))
									.getName()
							+ "&"
							+ ((Player) theApp.getPlayerList().get(winner[i]))
									.getBankroll().amount());
				}
				theApp.log(
						"Tie between "
								+ names
								+ " with a "
								+ winningHand
								+ ".  Each player gets "
								+ money
								+ ".  "
								+ ((Player) theApp.getPlayerList().get(
										lowPlayer)).getName()
								+ " wins the low and gets " + lowTake, 1);
				theApp.broadcastMessage("MESSAGE &Tie between "
						+ names
						+ " with a "
						+ winningHand
						+ ".  Each player gets "
						+ money
						+ ".  "
						+ ((Player) theApp.getPlayerList().get(lowPlayer))
								.getName() + " wins the low and gets "
						+ lowTake);
				if (theApp.getServerWindow() != null) {
					theApp.getServerWindow().sendMessage(
							"Tie between "
									+ names
									+ " with a "
									+ winningHand
									+ ".  Each player gets "
									+ money
									+ ".  "
									+ ((Player) theApp.getPlayerList().get(
											lowPlayer)).getName()
									+ " wins the low and gets " + lowTake);
				} else {
					System.out.println("Tie between "
							+ names
							+ " with a "
							+ winningHand
							+ ".  Each player gets "
							+ money
							+ ".  "
							+ ((Player) theApp.getPlayerList().get(lowPlayer))
									.getName() + " wins the low and gets "
							+ lowTake);
				}
				((Player) theApp.getPlayerList().get(lowPlayer)).add(lowTake
						.amount());
				theApp.broadcastMessage("PLAYER CASH &"
						+ ((Player) (theApp.getPlayerList().get(lowPlayer)))
								.getName()
						+ "&"
						+ ((Player) theApp.getPlayerList().get(lowPlayer))
								.getBankroll().amount());
			} else {
				PokerMoney highTake = new PokerMoney(
						winnersTake.amount() / 2.0f);
				PokerMoney lowTake = new PokerMoney(highTake.amount());

				StringBuffer names = new StringBuffer();
				StringBuffer lowNames = new StringBuffer();
				names.append(((Player) theApp.getPlayerList().get(winner[0]))
						.getName());
				lowNames.append(((Player) theApp.getPlayerList().get(
						lowWinners[0])).getName());
				PokerMoney money = new PokerMoney(highTake.amount()
						/ (float) numWinners);
				PokerMoney lowMoney = new PokerMoney(lowTake.amount()
						/ (float) numLowWinners);
				((Player) theApp.getPlayerList().get(winner[0])).add(money
						.amount());
				((Player) theApp.getPlayerList().get(lowWinners[0]))
						.add(lowMoney.amount());
				for (int i = 1; i < numWinners; i++) {
					names.append(" and "
							+ ((Player) (theApp.getPlayerList().get(winner[i])))
									.getName());
					((Player) theApp.getPlayerList().get(winner[i])).add(money
							.amount());
					theApp.broadcastMessage("PLAYER CASH &"
							+ ((Player) (theApp.getPlayerList().get(winner[i])))
									.getName()
							+ "&"
							+ ((Player) theApp.getPlayerList().get(winner[i]))
									.getBankroll().amount());
				}
				for (int i = 1; i < numLowWinners; i++) {
					lowNames.append(" and "
							+ ((Player) (theApp.getPlayerList()
									.get(lowWinners[i]))).getName());
					((Player) theApp.getPlayerList().get(lowWinners[i]))
							.add(lowMoney.amount());
					theApp.broadcastMessage("PLAYER CASH &"
							+ ((Player) (theApp.getPlayerList()
									.get(lowWinners[i]))).getName()
							+ "&"
							+ ((Player) theApp.getPlayerList().get(
									lowWinners[i])).getBankroll().amount());
				}
				theApp.log("Tie between " + names + " with a " + winningHand
						+ ".  Each player gets " + money + ".  Tie between "
						+ lowNames + " for the low.  Each player gets "
						+ lowMoney, 1);
				theApp.broadcastMessage("MESSAGE &Tie between " + names
						+ " with a " + winningHand + ".  Each player gets "
						+ money + ".  Tie between " + lowNames
						+ " for the low.  Each player gets " + lowMoney);
				if (theApp.getServerWindow() != null) {
					theApp.getServerWindow().sendMessage(
							"Tie between " + names + " with a " + winningHand
									+ ".  Each player gets " + money
									+ ".  Tie between " + lowNames
									+ " for the low.  Each player gets "
									+ lowMoney);
				} else {
					System.out.println("Tie between " + names + " with a "
							+ winningHand + ".  Each player gets " + money
							+ ".  Tie between " + lowNames
							+ " for the low.  Each player gets " + lowMoney);
				}
			}
		}

		ArrayList sidePotResults = new ArrayList();
		for (int i = 0; i < sidePots.size(); i++) {
			sidePotResults.add(calcSidePotWinners(i));
		}

		theApp.broadcastMessage("SIDE POTS START");
		for (int i = 0; i < sidePotResults.size(); i++) {
			String str = ((String) sidePotResults.get(i));
			theApp.log("  " + str, 1);
			theApp.broadcastMessage("SIDE POTS &" + str);
			if (theApp.getServerWindow() != null) {
				theApp.getServerWindow().sendMessage("" + str);
			} else {
				System.out.println("" + str);
			}
		}
		int numIn = 0;
		for (int i = 0; i < theApp.getPlayerList().size(); i++) {
			if (((Player) theApp.getPlayerList().get(i)).in) {
				numIn++;
			}
		}
		if ((numIn > 2) && (sidePots.size() > 0)) {
			theApp.broadcastMessage("SIDE POTS END");
		}

		theApp.log("End of game.  Pot size = " + theApp.getPot(), 3);
		theApp.log("  Main pot : " + winnersTake, 3);
		for (int i = 0; i < sidePots.size(); i++) {
			theApp.log(
					"  Side pot #" + i + " : "
							+ ((SidePot) sidePots.get(i)).getPot(), 3);
		}
	}

	/**********************
	 * calcSidePotWinners() calculate the winner(s) of the side pots.
	 * 
	 * @param potNumber
	 *            Which pot number is of concern
	 * @return A string detailing who won the side pot
	 * 
	 **/
	protected String calcSidePotWinners(int potNumber) {
		theApp.log("Omaha8.calcSidePotWinners( " + potNumber + " )", 3);
		int highPlayer = 0;
		int lowPlayer = 0;
		float highHand = 0.0f;
		float lowHand = (float) QUALIFIER + 1.0f;
		String winningHand = new String();
		String lowestHand = new String();
		int numWinners = 0;
		SidePot side = (SidePot) sidePots.get(potNumber);
		PokerMoney pot = side.getPot();
		String[] players = new String[side.getIncluded().size()];
		for (int i = 0; i < players.length; i++) {
			players[i] = (String) side.getIncluded().get(i);
		}

		int winner[] = new int[MAX_PLAYERS];
		float newPlayerHandValues[] = new float[MAX_PLAYERS];
		float lowHandValues[] = new float[MAX_PLAYERS];
		int lw[] = new int[MAX_PLAYERS];
		int nlw = 0;

		int numPlayers = 0;
		for (int i = 0; i < players.length; i++) {
			if (players[i] != null) {
				if (theApp.playerIndex(players[i]) >= 0) {
					numPlayers++;
				}
			}
		}

		String ret = new String();
		if (numPlayers == 0) {
			return ret;
		}
		if (numPlayers == 1) {
			ret = new String("Side pot #" + (potNumber + 1) + ": " + players[0]
					+ " gets " + pot + " back.");

			int pindex = theApp.playerIndex(players[0]);
			((Player) theApp.getPlayerList().get(pindex)).add(pot.amount());
			theApp.broadcastMessage("PLAYER CASH &"
					+ players[0]
					+ "&"
					+ ((Player) theApp.getPlayerList().get(pindex))
							.getBankroll().amount());

			return ret;
		}

		for (int i = 0; i < numPlayers; i++) {
			Player p = (Player) theApp.getPlayerList().get(
					theApp.playerIndex(players[i]));

			newPlayerHandValues[i] = bestHand(p.getHand());
			lowHandValues[i] = lowHand(p.getHand());

			if (newPlayerHandValues[i] > highHand) {
				highHand = newPlayerHandValues[i];
				highPlayer = theApp.playerIndex(players[i]);
			}
			if (lowHandValues[i] < lowHand) {
				lowHand = lowHandValues[i];
				lowPlayer = i;
			}
		}

		for (int i = 0; i < numPlayers; i++) {
			Player p = (Player) theApp.getPlayerList().get(
					theApp.playerIndex(players[i]));

			if (newPlayerHandValues[i] == highHand) {
				winner[numWinners] = theApp.playerIndex(players[i]);
				numWinners++;
			}
			if (lowHandValues[i] == lowHand) {
				lw[nlw] = i;
				nlw++;
			}
		}
		winningHand = HandEvaluator.nameHand(highHand);
		if (highHand < minValueToShow) {
			minValueToShow = highHand;
		}

		PokerMoney winnersTake = new PokerMoney(pot.amount());

		if (numWinners == 1) {
			if (nlw == 0) {
				ret = new String(
						"Side pot #"
								+ (potNumber + 1)
								+ ": "
								+ ((Player) theApp.getPlayerList().get(
										highPlayer)).getName() + " wins "
								+ winnersTake + " with a " + winningHand
								+ ".  No low winner.");
				((Player) theApp.getPlayerList().get(highPlayer))
						.add(winnersTake.amount());
				theApp.broadcastMessage("PLAYER CASH &"
						+ ((Player) (theApp.getPlayerList().get(highPlayer)))
								.getName()
						+ "&"
						+ ((Player) theApp.getPlayerList().get(highPlayer))
								.getBankroll().amount());
			} else if (nlw == 1) {
				winnersTake.subtract(winnersTake.amount() / 2.0f);
				ret = new String(
						"Side pot #"
								+ (potNumber + 1)
								+ ": "
								+ ((Player) theApp.getPlayerList().get(
										highPlayer)).getName()
								+ " wins with a "
								+ winningHand
								+ ". "
								+ ((Player) theApp.getPlayerList().get(
										lowPlayer)).getName()
								+ " wins the low.  Each gets = " + winnersTake);
				((Player) theApp.getPlayerList().get(highPlayer))
						.add(winnersTake.amount());
				((Player) theApp.getPlayerList().get(lowPlayer))
						.add(winnersTake.amount());
				theApp.broadcastMessage("PLAYER CASH &"
						+ ((Player) (theApp.getPlayerList().get(highPlayer)))
								.getName()
						+ "&"
						+ ((Player) theApp.getPlayerList().get(highPlayer))
								.getBankroll().amount());
				theApp.broadcastMessage("PLAYER CASH &"
						+ ((Player) (theApp.getPlayerList().get(lowPlayer)))
								.getName()
						+ "&"
						+ ((Player) theApp.getPlayerList().get(lowPlayer))
								.getBankroll().amount());
			} else {
				PokerMoney highTake = new PokerMoney(
						winnersTake.amount() / 2.0f);
				PokerMoney lowTake = new PokerMoney(highTake.amount());

				StringBuffer names = new StringBuffer();
				names.append(((Player) theApp.getPlayerList().get(lw[0]))
						.getName());
				PokerMoney money = new PokerMoney(lowTake.amount()
						/ (float) nlw);
				((Player) theApp.getPlayerList().get(lw[0]))
						.add(money.amount());
				for (int i = 1; i < nlw; i++) {
					names.append(" and "
							+ ((Player) (theApp.getPlayerList().get(lw[i])))
									.getName());
					((Player) theApp.getPlayerList().get(lw[i])).add(money
							.amount());
					theApp.broadcastMessage("PLAYER CASH &"
							+ ((Player) (theApp.getPlayerList().get(lw[i])))
									.getName()
							+ "&"
							+ ((Player) theApp.getPlayerList().get(lw[i]))
									.getBankroll().amount());
				}
				ret = new String(
						"Side pot #"
								+ (potNumber + 1)
								+ ": "
								+ ((Player) theApp.getPlayerList().get(
										highPlayer)).getName()
								+ " wins with a " + winningHand + " and gets "
								+ highTake + ".  " + names
								+ " split the low.  Each gets " + money + ".");
				((Player) theApp.getPlayerList().get(highPlayer)).add(highTake
						.amount());
				theApp.broadcastMessage("PLAYER CASH &"
						+ ((Player) (theApp.getPlayerList().get(highPlayer)))
								.getName()
						+ "&"
						+ ((Player) theApp.getPlayerList().get(highPlayer))
								.getBankroll().amount());
			}
		} else {
			if (nlw == 0) {
				StringBuffer names = new StringBuffer();
				names.append(((Player) theApp.getPlayerList().get(winner[0]))
						.getName());
				PokerMoney money = new PokerMoney(winnersTake.amount()
						/ (float) numWinners);
				((Player) theApp.getPlayerList().get(winner[0])).add(money
						.amount());
				for (int i = 1; i < numWinners; i++) {
					names.append(" and "
							+ ((Player) (theApp.getPlayerList().get(winner[i])))
									.getName());
					((Player) theApp.getPlayerList().get(winner[i])).add(money
							.amount());
					theApp.broadcastMessage("PLAYER CASH &"
							+ ((Player) (theApp.getPlayerList().get(winner[i])))
									.getName()
							+ "&"
							+ ((Player) theApp.getPlayerList().get(winner[i]))
									.getBankroll().amount());
				}
				ret = new String("Side pot #" + (potNumber + 1) + ": " + names
						+ " win " + money + " with a " + winningHand
						+ ".  No low winner.");
			} else if (nlw == 1) {
				PokerMoney highTake = new PokerMoney(
						winnersTake.amount() / 2.0f);
				PokerMoney lowTake = new PokerMoney(highTake.amount());

				StringBuffer names = new StringBuffer();
				names.append(((Player) theApp.getPlayerList().get(winner[0]))
						.getName());
				PokerMoney money = new PokerMoney(highTake.amount()
						/ (float) numWinners);
				((Player) theApp.getPlayerList().get(winner[0])).add(money
						.amount());
				for (int i = 1; i < numWinners; i++) {
					names.append(" and "
							+ ((Player) (theApp.getPlayerList().get(winner[i])))
									.getName());
					((Player) theApp.getPlayerList().get(winner[i])).add(money
							.amount());
					theApp.broadcastMessage("PLAYER CASH &"
							+ ((Player) (theApp.getPlayerList().get(winner[i])))
									.getName()
							+ "&"
							+ ((Player) theApp.getPlayerList().get(winner[i]))
									.getBankroll().amount());
				}
				ret = new String(
						"Side pot #"
								+ (potNumber + 1)
								+ ": "
								+ "Tie between "
								+ names
								+ " with a "
								+ winningHand
								+ ".  Each player gets "
								+ money
								+ ".  "
								+ ((Player) theApp.getPlayerList().get(
										lowPlayer)).getName()
								+ " wins the low and gets " + lowTake);
				((Player) theApp.getPlayerList().get(lowPlayer)).add(lowTake
						.amount());
				theApp.broadcastMessage("PLAYER CASH &"
						+ ((Player) (theApp.getPlayerList().get(lowPlayer)))
								.getName()
						+ "&"
						+ ((Player) theApp.getPlayerList().get(lowPlayer))
								.getBankroll().amount());
			} else {
				PokerMoney highTake = new PokerMoney(
						winnersTake.amount() / 2.0f);
				PokerMoney lowTake = new PokerMoney(highTake.amount());

				StringBuffer names = new StringBuffer();
				StringBuffer lowNames = new StringBuffer();
				names.append(((Player) theApp.getPlayerList().get(winner[0]))
						.getName());
				lowNames.append(((Player) theApp.getPlayerList().get(lw[0]))
						.getName());
				PokerMoney money = new PokerMoney(highTake.amount()
						/ (float) numWinners);
				PokerMoney lowMoney = new PokerMoney(lowTake.amount()
						/ (float) nlw);
				((Player) theApp.getPlayerList().get(winner[0])).add(money
						.amount());
				((Player) theApp.getPlayerList().get(lw[0])).add(lowMoney
						.amount());
				for (int i = 1; i < numWinners; i++) {
					names.append(" and "
							+ ((Player) (theApp.getPlayerList().get(winner[i])))
									.getName());
					((Player) theApp.getPlayerList().get(winner[i])).add(money
							.amount());
					theApp.broadcastMessage("PLAYER CASH &"
							+ ((Player) (theApp.getPlayerList().get(winner[i])))
									.getName()
							+ "&"
							+ ((Player) theApp.getPlayerList().get(winner[i]))
									.getBankroll().amount());
				}
				for (int i = 1; i < nlw; i++) {
					lowNames.append(" and "
							+ ((Player) (theApp.getPlayerList().get(lw[i])))
									.getName());
					((Player) theApp.getPlayerList().get(lw[i])).add(lowMoney
							.amount());
					theApp.broadcastMessage("PLAYER CASH &"
							+ ((Player) (theApp.getPlayerList().get(lw[i])))
									.getName()
							+ "&"
							+ ((Player) theApp.getPlayerList().get(lw[i]))
									.getBankroll().amount());
				}
				ret = new String("Side pot #" + (potNumber + 1) + ": "
						+ "Tie between " + names + " with a " + winningHand
						+ ".  Each player gets " + money + ".  Tie between "
						+ lowNames + " for the low.  Each player gets "
						+ lowMoney);
			}
		}
		return ret;
	}
}
