/******************************************************************************************
 * LowChicago.java                 PokerApp                                               *
 *                                                                                        *
 *   Revision History                                                                     *
 * +---------+----------+---------------------------------------------------------------+ *
 * | Version | DATE     | Description                                                   | *
 * +---------+----------+---------------------------------------------------------------+ *
 * |  0.99   | 05/19/05 | Initial documented release                                    | *
 * |  1.00   | 06/07/05 | Added logging.                                                | *
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
import java.util.ArrayList;

/****************************************************
 * Seven card stud with low chicago game definition.
 * 
 * @author Dan Puperi
 * @version 1.00
 * 
 **/
public class LowChicago extends SevenCardStud {

	/**
	 * The index of the player who has low chicago.
	 **/
	protected int chicagoIndex;
	/**
	 * The card that wins low chicago.
	 **/
	protected Card chicagoCard;

	private boolean rememberNoLimit; // If the game was started as no limit,
										// make it bet limit, but change back
										// after the game.
	private boolean rememberPotLimit; // If the game was started as pot limit,
										// make it bet limit, but change back
										// after the game.
	private boolean rememberBetLimit; // Remember if the game was started as bet
										// limit, so it doesn't try to change it
										// after the game.

	/***********************
	 * Constructor creates an instance of a game of LowChicago
	 * 
	 * @param a
	 *            The StartPoker instance to which this game belongs
	 * 
	 **/
	public LowChicago(StartPoker a) {
		super(a, "Seven Card Stud with Low Chicago", true);
		if (theApp.getPlayerList().size() < 2) {
			return;
		}
		theApp.log("Constructing a game of Low Chicago", 3);
		rememberNoLimit = theApp.noLimit;
		rememberPotLimit = theApp.potLimit;
		rememberBetLimit = theApp.betLimit;
		chicagoIndex = -1;
		chicagoCard = new Card();
		if (theApp.noLimit) {
			theApp.betLimit = true;
			theApp.noLimit = false;
			theApp.updateRules();
			theApp.broadcastRules();
			theApp.broadcastMessage("REFRESH");
		}
		if (theApp.potLimit) {
			theApp.betLimit = true;
			theApp.potLimit = false;
			theApp.updateRules();
			theApp.broadcastRules();
			theApp.broadcastMessage("REFRESH");
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
	public LowChicago(StartPoker a, String gameName, boolean selfInit) {
		super(a, gameName, selfInit);
		if (theApp.getPlayerList().size() < 2) {
			return;
		}
		theApp.log("Constructing a game of Low Chicago", 3);
		rememberNoLimit = theApp.noLimit;
		rememberPotLimit = theApp.potLimit;
		rememberBetLimit = theApp.betLimit;
		chicagoIndex = -1;
		chicagoCard = new Card();
		if (theApp.noLimit) {
			theApp.betLimit = true;
			theApp.noLimit = false;
			theApp.updateRules();
			theApp.broadcastRules();
			theApp.broadcastMessage("REFRESH");
		}
		if (theApp.potLimit) {
			theApp.betLimit = true;
			theApp.potLimit = false;
			theApp.updateRules();
			theApp.broadcastRules();
			theApp.broadcastMessage("REFRESH");
		}
	}

	/**********************
	 * calcChicago() used to calculate the player who has the low spade in the
	 * hole. There are two overloaded methods because the first on just uses the
	 * player list the other one uses an argument for the player list.
	 * 
	 * @return The index of the player with the low Chicago
	 * 
	 **/
	protected int calcChicago() {
		theApp.log("LowChicago.calcChicago()", 3);
		int lowPlayer = -1;
		chicagoCard = new Card("Ace of Spades");
		for (int i = 0; i < theApp.getPlayerList().size(); i++) {
			Player p = (Player) theApp.getPlayerList().get(i);
			if (p.in) {
				for (int j = 0; j < 3; j++) {
					Card c = p.getHand().getHoleCard(j);
					if ((c.getSuit() == Card.SPADES)
							&& (((c.getRank() < chicagoCard.getRank()) && (c
									.getRank() != Card.ACE)) || (chicagoCard
									.getRank() == Card.ACE))) {
						lowPlayer = i;
						chicagoCard = c;
					}
				}
			}
		}
		return lowPlayer;
	}

	/**********************
	 * calcChicago() used to calculate the player who has the low spade in the
	 * hole. There are two overloaded methods because the first on just uses the
	 * player list the other one uses an argument for the player list.
	 * 
	 * @param players
	 *            A String array of player names who are eligible for the low
	 *            Chicago
	 * @return The index in the array of the player with low Chicago
	 * 
	 **/
	protected int calcChicago(String[] players) {
		theApp.log("LowChicago.calcChicago( " + players + " )", 3);
		int lowPlayer = -1;
		chicagoCard = new Card("Ace of Spades");
		for (int i = 0; i < players.length; i++) {
			Player p = (Player) theApp.getPlayerList().get(
					theApp.playerIndex(players[i]));
			if (p.in) {
				for (int j = 0; j < 3; j++) {
					Card c = p.getHand().getHoleCard(j);
					if ((c.getSuit() == Card.SPADES)
							&& (((c.getRank() < chicagoCard.getRank()) && (c
									.getRank() != Card.ACE)) || (chicagoCard
									.getRank() == Card.ACE))) {
						lowPlayer = theApp.playerIndex(players[i]);
						chicagoCard = c;
					}
				}
			}
		}
		return lowPlayer;
	}

	/**********************
	 * show() called at the showdown
	 **/
	protected void show() {
		theApp.log("LowChicago.show()", 3);
		theApp.playedInLastGame = new ArrayList();
		for (int i = 0; i < theApp.getPlayerList().size(); i++) {
			theApp.playedInLastGame
					.add(((Player) theApp.getPlayerList().get(i)).getName());
		}
		theApp.broadcastMessage("MESSAGE &Calculating winner...");
		chicagoIndex = calcChicago();
		calcWinner();
		showCards();
		if (chicagoIndex >= 0) {
			showPlayer((Player) theApp.getPlayerList().get(chicagoIndex), true);
		}
		theApp.betLimit = rememberBetLimit;
		theApp.noLimit = rememberNoLimit;
		theApp.potLimit = rememberPotLimit;
		theApp.updateRules();
		theApp.broadcastRules();
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
		theApp.log("LowChicago.calcWinner()", 3);
		int highPlayer = 0;
		float highHand = 0.0f;
		String winningHand = new String();
		int numWinners = 0;
		int winner[] = new int[MAX_PLAYERS];
		playerHandValues = new float[MAX_PLAYERS];

		for (int i = 0; i < theApp.getPlayerList().size(); i++) {
			Player p = (Player) theApp.getPlayerList().get(i);
			if (p.in) {
				playerHandValues[i] = bestHand(p.getHand());
				if (playerHandValues[i] > highHand) {
					highHand = playerHandValues[i];
					highPlayer = i;
				}
			}
		}
		for (int i = 0; i < theApp.getPlayerList().size(); i++) {
			Player p = (Player) theApp.getPlayerList().get(i);
			if (p.in) {
				if ((playerHandValues[i] == highHand) || (chicagoIndex == i)) {
					winner[numWinners] = i;
					numWinners++;
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
			if (chicagoIndex >= 0) {
				theApp.log(
						""
								+ ((Player) theApp.getPlayerList().get(
										highPlayer)).getName()
								+ " wins with a " + winningHand
								+ " and wins Chicago with a " + chicagoCard
								+ ".  Pot = " + winnersTake, 1);
				theApp.broadcastMessage("MESSAGE &"
						+ ((Player) theApp.getPlayerList().get(highPlayer))
								.getName() + " wins with a " + winningHand
						+ " and wins Chicago with a " + chicagoCard
						+ ".  Pot = " + winnersTake);
				if (theApp.getServerWindow() != null) {
					theApp.getServerWindow().sendMessage(
							""
									+ ((Player) theApp.getPlayerList().get(
											highPlayer)).getName()
									+ " wins with a " + winningHand
									+ " and wins Chicago with a " + chicagoCard
									+ ".  Pot = " + winnersTake);
				} else {
					System.out.println(""
							+ ((Player) theApp.getPlayerList().get(highPlayer))
									.getName() + " wins with a " + winningHand
							+ " and wins Chicago with a " + chicagoCard
							+ ".  Pot = " + winnersTake);
				}
				((Player) theApp.getPlayerList().get(highPlayer))
						.add(winnersTake.amount());
				theApp.broadcastMessage("PLAYER CASH &"
						+ ((Player) (theApp.getPlayerList().get(highPlayer)))
								.getName()
						+ "&"
						+ ((Player) theApp.getPlayerList().get(highPlayer))
								.getBankroll().amount());
			} else {
				theApp.log(
						""
								+ ((Player) theApp.getPlayerList().get(
										highPlayer)).getName()
								+ " wins with a " + winningHand
								+ ".  Nobody wins Chicago.  Pot = "
								+ winnersTake, 1);
				theApp.broadcastMessage("MESSAGE &"
						+ ((Player) theApp.getPlayerList().get(highPlayer))
								.getName() + " wins with a " + winningHand
						+ ".  Nobody wins Chicago.  Pot = " + winnersTake);
				if (theApp.getServerWindow() != null) {
					theApp.getServerWindow().sendMessage(
							""
									+ ((Player) theApp.getPlayerList().get(
											highPlayer)).getName()
									+ " wins with a " + winningHand
									+ ".  Nobody wins Chicago.  Pot = "
									+ winnersTake);
				} else {
					System.out.println(""
							+ ((Player) theApp.getPlayerList().get(highPlayer))
									.getName() + " wins with a " + winningHand
							+ ".  Nobody wins Chicago.  Pot = " + winnersTake);
				}
				((Player) theApp.getPlayerList().get(highPlayer))
						.add(winnersTake.amount());
				theApp.broadcastMessage("PLAYER CASH &"
						+ ((Player) (theApp.getPlayerList().get(highPlayer)))
								.getName()
						+ "&"
						+ ((Player) theApp.getPlayerList().get(highPlayer))
								.getBankroll().amount());
			}
		} else {
			PokerMoney money = new PokerMoney(winnersTake.amount() / numWinners);
			StringBuffer names = new StringBuffer();
			int winnersNotChi = 0;
			for (int i = 0; i < numWinners; i++) {
				if (playerHandValues[winner[i]] == highHand) {
					winnersNotChi++;
					if (names == new StringBuffer()) {
						names.append(((Player) theApp.getPlayerList().get(
								winner[i])).getName());
					} else {
						names.append(" and "
								+ ((Player) (theApp.getPlayerList()
										.get(winner[i]))).getName());
					}
				}
				((Player) theApp.getPlayerList().get(winner[i])).add(money
						.amount());
				theApp.broadcastMessage("PLAYER CASH &"
						+ ((Player) (theApp.getPlayerList().get(winner[i])))
								.getName()
						+ "&"
						+ ((Player) theApp.getPlayerList().get(winner[i]))
								.getBankroll().amount());
			}

			if (winnersNotChi < 2) {
				theApp.log(
						""
								+ ((Player) theApp.getPlayerList().get(
										highPlayer)).getName()
								+ " wins with a "
								+ winningHand
								+ " and "
								+ ((Player) theApp.getPlayerList().get(
										chicagoIndex)).getName()
								+ " wins Chicago with a " + chicagoCard
								+ ".  Each gets " + money, 1);
				theApp.broadcastMessage("MESSAGE &"
						+ ((Player) theApp.getPlayerList().get(highPlayer))
								.getName()
						+ " wins with a "
						+ winningHand
						+ " and "
						+ ((Player) theApp.getPlayerList().get(chicagoIndex))
								.getName() + " wins Chicago with a "
						+ chicagoCard + ".  Each gets " + money);
				if (theApp.getServerWindow() != null) {
					theApp.getServerWindow().sendMessage(
							""
									+ ((Player) theApp.getPlayerList().get(
											highPlayer)).getName()
									+ " wins with a "
									+ winningHand
									+ " and "
									+ ((Player) theApp.getPlayerList().get(
											chicagoIndex)).getName()
									+ " wins Chicago with a " + chicagoCard
									+ ".  Each gets " + money);
				} else {
					System.out.println(""
							+ ((Player) theApp.getPlayerList().get(highPlayer))
									.getName()
							+ " wins with a "
							+ winningHand
							+ " and "
							+ ((Player) theApp.getPlayerList()
									.get(chicagoIndex)).getName()
							+ " wins Chicago with a " + chicagoCard
							+ ".  Each gets " + money);
				}
			} else if (chicagoIndex >= 0) {
				theApp.log(
						"Tie between "
								+ names
								+ " with a "
								+ winningHand
								+ ".  "
								+ ((Player) theApp.getPlayerList().get(
										chicagoIndex)).getName()
								+ " wins Chicago with a " + chicagoCard
								+ ".  Each player gets " + money + ".", 1);
				theApp.broadcastMessage("MESSAGE &Tie between "
						+ names
						+ " with a "
						+ winningHand
						+ ".  "
						+ ((Player) theApp.getPlayerList().get(chicagoIndex))
								.getName() + " wins Chicago with a "
						+ chicagoCard + ".  Each player gets " + money + ".");
				if (theApp.getServerWindow() != null) {
					theApp.getServerWindow().sendMessage(
							"Tie between "
									+ names
									+ " with a "
									+ winningHand
									+ ".  "
									+ ((Player) theApp.getPlayerList().get(
											chicagoIndex)).getName()
									+ " wins Chicago with a " + chicagoCard
									+ ".  Each player gets " + money + ".");
				} else {
					System.out.println("Tie between "
							+ names
							+ " with a "
							+ winningHand
							+ ".  "
							+ ((Player) theApp.getPlayerList()
									.get(chicagoIndex)).getName()
							+ " wins Chicago with a " + chicagoCard
							+ ".  Each player gets " + money + ".");
				}
			} else {
				theApp.log("Tie between " + names + " with a " + winningHand
						+ ".  Nobody wins Chicago.  Each player gets " + money
						+ ".", 1);
				theApp.broadcastMessage("MESSAGE &Tie between " + names
						+ " with a " + winningHand
						+ ".  Nobody wins Chicago.  Each player gets " + money
						+ ".");
				if (theApp.getServerWindow() != null) {
					theApp.getServerWindow()
							.sendMessage(
									"Tie between "
											+ names
											+ " with a "
											+ winningHand
											+ ".  Nobody wins Chicago.  Each player gets "
											+ money + ".");
				} else {
					System.out.println("Tie between " + names + " with a "
							+ winningHand
							+ ".  Nobody wins Chicago.  Each player gets "
							+ money + ".");
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
		theApp.log("LowChicago.calcSidePotWinners( " + potNumber + " )", 3);
		int highPlayer = 0;
		float highHand = 0.0f;
		String winningHand = new String();
		int numWinners = 0;
		SidePot side = (SidePot) sidePots.get(potNumber);
		PokerMoney pot = side.getPot();
		String[] players = new String[side.getIncluded().size()];
		for (int i = 0; i < players.length; i++) {
			players[i] = (String) side.getIncluded().get(i);
		}

		int winner[] = new int[MAX_PLAYERS];
		float newPlayerHandValues[] = new float[MAX_PLAYERS];

		int numPlayers = 0;
		for (int i = 0; i < players.length; i++) {
			if (players[i] != null) {
				if (theApp.playerIndex(players[i]) >= 0) {
					numPlayers++;
				}
			}
		}

		chicagoIndex = calcChicago(players);

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
			if (newPlayerHandValues[i] > highHand) {
				highHand = newPlayerHandValues[i];
				highPlayer = theApp.playerIndex(players[i]);
			}
		}
		for (int i = 0; i < numPlayers; i++) {
			Player p = (Player) theApp.getPlayerList().get(
					theApp.playerIndex(players[i]));
			if ((newPlayerHandValues[i] == highHand)
					|| (chicagoIndex == theApp.playerIndex(players[i]))) {
				winner[numWinners] = theApp.playerIndex(players[i]);
				numWinners++;
			}
		}
		winningHand = HandEvaluator.nameHand(highHand);
		if (highHand < minValueToShow) {
			minValueToShow = highHand;
		}

		PokerMoney winnersTake = new PokerMoney(pot.amount());

		if (numWinners == 1) {
			if (chicagoIndex >= 0) {
				ret = new String(
						"Side pot #"
								+ (potNumber + 1)
								+ ": "
								+ ((Player) theApp.getPlayerList().get(
										highPlayer)).getName() + " wins "
								+ winnersTake + " with a " + winningHand
								+ " and wins Chicago with a " + chicagoCard);
			} else {
				ret = new String(
						"Side pot #"
								+ (potNumber + 1)
								+ ": "
								+ ((Player) theApp.getPlayerList().get(
										highPlayer)).getName() + " wins "
								+ winnersTake + " with a " + winningHand
								+ ".  No body wins Chicago");
			}
			((Player) theApp.getPlayerList().get(highPlayer)).add(winnersTake
					.amount());
			theApp.broadcastMessage("PLAYER CASH &"
					+ ((Player) (theApp.getPlayerList().get(highPlayer)))
							.getName()
					+ "&"
					+ ((Player) theApp.getPlayerList().get(highPlayer))
							.getBankroll().amount());
		} else {
			PokerMoney money = new PokerMoney(winnersTake.amount() / numWinners);
			StringBuffer names = new StringBuffer();
			int winnersNotChi = 0;
			for (int i = 0; i < numWinners; i++) {
				if (playerHandValues[winner[i]] == highHand) {
					winnersNotChi++;
					if (names == new StringBuffer()) {
						names.append(((Player) theApp.getPlayerList().get(
								winner[i])).getName());
					} else {
						names.append(" and "
								+ ((Player) (theApp.getPlayerList()
										.get(winner[i]))).getName());
					}
				}
				((Player) theApp.getPlayerList().get(winner[i])).add(money
						.amount());
				theApp.broadcastMessage("PLAYER CASH &"
						+ ((Player) (theApp.getPlayerList().get(winner[i])))
								.getName()
						+ "&"
						+ ((Player) theApp.getPlayerList().get(winner[i]))
								.getBankroll().amount());
			}

			if (winnersNotChi < 2) {
				ret = new String(
						"Side pot #"
								+ (potNumber + 1)
								+ ": "
								+ ((Player) theApp.getPlayerList().get(
										highPlayer)).getName()
								+ " wins with a "
								+ winningHand
								+ " and "
								+ ((Player) theApp.getPlayerList().get(
										chicagoIndex)).getName()
								+ " wins Chicago with a " + chicagoCard
								+ ".  Each gets " + money + ".");
			} else if (chicagoIndex >= 0) {
				ret = new String(
						"Side pot #"
								+ (potNumber + 1)
								+ ": "
								+ "Tie between "
								+ names
								+ " with a "
								+ winningHand
								+ ".  "
								+ ((Player) theApp.getPlayerList().get(
										chicagoIndex)).getName()
								+ " wins Chicago with a " + chicagoCard
								+ ".  Each player gets " + money + ".");
			} else {
				ret = new String("Side pot #" + (potNumber + 1) + ": "
						+ "Tie between " + names + " with a " + winningHand
						+ ".  Nobody wins Chicago.  Each player gets " + money
						+ ".");
			}
		}
		return ret;
	}
}