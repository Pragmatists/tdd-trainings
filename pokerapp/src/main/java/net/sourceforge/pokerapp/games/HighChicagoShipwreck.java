/******************************************************************************************
 * HighChicagoShipwreck.java       PokerApp                                               *
 *                                                                                        *
 *   Revision History                                                                     *
 * +---------+----------+---------------------------------------------------------------+ *
 * | Version | DATE     | Description                                                   | *
 * +---------+----------+---------------------------------------------------------------+ *
 * |  0.99   | 05/21/05 | Initial documented release                                    | *
 * |  1.00   | 06/06/05 | Added logging                                                 | *
 * |  1.00   | 07/31/07 | Prepare for open source.  Header/comments/package/etc...      | *
 * +---------+----------+---------------------------------------------------------------+ *
 *                                                                                        *
 ******************************************************************************************/

package net.sourceforge.pokerapp.games;

import net.sourceforge.pokerapp.*;

/****************************************************
 * Seven card stud with shipwreck and high Chicago game definition.
 * 
 * @author Dan Puperi
 * @version 1.00
 * 
 **/
public class HighChicagoShipwreck extends LowChicago {

	/***********************
	 * Constructor creates an instance of a game of High Chicago Shipwreck
	 * 
	 * @param a
	 *            The StartPoker instance to which this game belongs
	 * 
	 **/
	public HighChicagoShipwreck(StartPoker a) {
		super(a, "Seven Card Stud High Chicago with Shipwreck", true);
		if (theApp.getPlayerList().size() < 2) {
			return;
		}
		theApp.log(
				"Constructing a game of Seven Card Stud High Chicago with Shipwreck",
				3);
	}

	/***********************
	 * Constructor creates an instance of a game of High Chicago Shipwreck This
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
	public HighChicagoShipwreck(StartPoker a, String gameName, boolean selfInit) {
		super(a, gameName, selfInit);
		if (theApp.getPlayerList().size() < 2) {
			return;
		}
		theApp.log(
				"Constructing a game of Seven Card Stud High Chicago with Shipwreck",
				3);
	}

	/**********************
	 * calcChicago() used to calculate the player who has the high spade in the
	 * hole. There are two overloaded methods because the first on just uses the
	 * player list the other one uses an argument for the player list.
	 * 
	 * @return The index of the player with the high Chicago
	 * 
	 **/
	protected int calcChicago() {
		theApp.log("HighChicagoShipwreck.calcChicago()");
		int highPlayer = -1;
		chicagoCard = new Card("Two of Spades");
		for (int i = 0; i < theApp.getPlayerList().size(); i++) {
			Player p = (Player) theApp.getPlayerList().get(i);
			if (p.in) {
				for (int j = 0; j < 3; j++) {
					Card c = p.getHand().getHoleCard(j);
					if ((c.getSuit() == Card.SPADES)
							&& (((c.getRank() > chicagoCard.getRank()) || (c
									.getRank() == Card.ACE)) && (chicagoCard
									.getRank() != Card.ACE))) {
						highPlayer = i;
						chicagoCard = c;
					}
				}
			}
		}
		return highPlayer;
	}

	/**********************
	 * calcChicago() used to calculate the player who has the high spade in the
	 * hole. There are two overloaded methods because the first on just uses the
	 * player list the other one uses an argument for the player list.
	 * 
	 * @param players
	 *            A String array of player names who are eligible for the high
	 *            Chicago
	 * @return The index in the array of the player with high Chicago
	 * 
	 **/
	protected int calcChicago(String[] players) {
		theApp.log("HighChicagoShipwreck.calcChicago( " + players + " )");
		int highPlayer = -1;
		chicagoCard = new Card("Two of Spades");
		for (int i = 0; i < players.length; i++) {
			Player p = (Player) theApp.getPlayerList().get(
					theApp.playerIndex(players[i]));
			if (p.in) {
				for (int j = 0; j < 3; j++) {
					Card c = p.getHand().getHoleCard(j);
					if ((c.getSuit() == Card.SPADES)
							&& (((c.getRank() > chicagoCard.getRank()) || (c
									.getRank() == Card.ACE)) && (chicagoCard
									.getRank() != Card.ACE))) {
						highPlayer = theApp.playerIndex(players[i]);
						chicagoCard = c;
					}
				}
			}
		}
		return highPlayer;
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
		theApp.log("HighChicagoShipwreck.bestHand( " + h + " )");
		int numHoleCards = h.getNumHole();
		int numCards = h.getNumHole() + h.getNumUp();
		Card[] chole = new Card[numHoleCards];
		Card[] c = new Card[numCards];

		float best = 0.0f;
		float test = 0.0f;
		int wildRank = 15;
		int numWilds = 0;
		int[] ci = new int[5];
		HandEvaluator he = new HandEvaluator();

		if (numCards < 5) {
			return best;
		}

		for (int i = 0; i < numHoleCards; i++) {
			if (h.getHoleCard(i) != null) {
				chole[i] = h.getHoleCard(i);
				c[i] = h.getHoleCard(i);
			}
		}
		for (int i = 0; i < h.getNumUp(); i++) {
			if (h.getUpCard(i) != null) {
				c[numHoleCards + i] = h.getUpCard(i);
			}
		}

		// Find lowest card in hole...that is wild along with all other of that
		// suit
		for (int i = 0; i < numHoleCards; i++) {
			if ((int) he.rankHand(chole[i]) < wildRank) {
				wildRank = (int) he.rankHand(chole[i]);
			}
		}

		// Assume that all wild cards will be part of played hand - find them.
		for (int i = 0; i < numCards; i++) {
			if ((int) he.rankHand(c[i]) == wildRank) {
				ci[numWilds] = i;
				numWilds++;
			}
		}
		//
		// If there are 4 wilds, only need to look for royal straight flush or
		// five of a kind.
		//
		if (numWilds == 4) {
			for (int i = 0; i < numCards; i++) {
				if ((i != ci[0]) && (i != ci[1]) && (i != ci[2])
						&& (i != ci[3])) {

					// Royal straight flush
					//
					if ((c[i].getRank() == Card.ACE)
							|| (c[i].getRank() >= Card.TEN)) {
						return 150.0f;

						// Five of a kind
						//
					} else {
						test = 135.0f + he.rankHand(c[i]);
						if (test > best) {
							best = test;
						}
					}
				}
			}
			return best;
			//
			// If there are 3 wilds at least have a four of a kind.
			//
		} else if (numWilds == 3) {

			// Royal straight flush
			//
			for (int i = 0; i < numCards - 1; i++) {
				if ((i != ci[0]) && (i != ci[1]) && (i != ci[2])) {
					for (int j = i + 1; j < numCards; j++) {
						if ((j != ci[0]) && (j != ci[1]) && (j != ci[2])) {
							if ((he.isStraight(c[i], c[j]) == 14.0f)
									&& (c[i].getSuit() == c[j].getSuit())) {
								return 150.0f;
							}
						}
					}
				}
			}

			// Five of a kind
			//
			for (int i = 0; i < numCards - 1; i++) {
				if ((i != ci[0]) && (i != ci[1]) && (i != ci[2])) {
					for (int j = i + 1; j < numCards; j++) {
						if ((j != ci[0]) && (j != ci[1]) && (j != ci[2])) {
							if (c[i].getRank() == c[j].getRank()) {
								test = 135.0f + he.rankHand(c[i]);
								if (test > best) {
									best = test;
								}
							}
						}
					}
				}
			}
			if (best >= 135.0f) {
				return best;
			}

			// Straight flush
			//
			for (int i = 0; i < numCards - 1; i++) {
				if ((i != ci[0]) && (i != ci[1]) && (i != ci[2])) {
					for (int j = i + 1; j < numCards; j++) {
						if ((j != ci[0]) && (j != ci[1]) && (j != ci[2])) {
							float straight = he.isStraight(c[i], c[j]);
							if ((c[i].getSuit() == c[j].getSuit())
									&& (straight > 0.0)) {
								test = 120.0f + straight;
								if (test > best) {
									best = test;
								}
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
			for (int i = 0; i < numCards - 1; i++) {
				if ((i != ci[0]) && (i != ci[1]) && (i != ci[2])) {
					for (int j = i + 1; j < numCards; j++) {
						if ((j != ci[0]) && (j != ci[1]) && (j != ci[2])) {
							if (he.rankHand(c[i]) >= he.rankHand(c[j])) {
								test = 105.0f + he.rankHand(c[i])
										+ he.kicker(c[j]);
								if (test > best) {
									best = test;
								}
							} else {
								test = 105.0f + he.rankHand(c[j])
										+ he.kicker(c[i]);
								if (test > best) {
									best = test;
								}
							}
						}
					}
				}
			}
			return best;
			//
			// Two wilds - lots of possibilities
			//
		} else if (numWilds == 2) {

			// Royal straight flush
			//
			for (int i = 0; i < numCards - 2; i++) {
				if ((i != ci[0]) && (i != ci[1])) {
					for (int j = i + 1; j < numCards - 1; j++) {
						if ((j != ci[0]) && (j != ci[1])) {
							for (int k = j + 1; k < numCards; k++) {
								if ((k != ci[0]) && (k != ci[1])) {
									if ((he.isStraight(c[i], c[j], c[k]) == 14.0f)
											&& he.isFlush(c[i], c[j], c[k])) {
										return 150.0f;
									}
								}
							}
						}
					}
				}
			}

			// Five of a kind
			//
			for (int i = 0; i < numCards - 2; i++) {
				if ((i != ci[0]) && (i != ci[1])) {
					for (int j = i + 1; j < numCards - 1; j++) {
						if ((j != ci[0]) && (j != ci[1])) {
							for (int k = j + 1; k < numCards; k++) {
								if ((k != ci[0]) && (k != ci[1])) {
									if ((c[i].getRank() == c[j].getRank())
											&& (c[i].getRank() == c[k]
													.getRank())) {
										test = 135.0f + he.rankHand(c[i]);
										if (test > best) {
											best = test;
										}
									}
								}
							}
						}
					}
				}
			}
			if (best >= 135.0f) {
				return best;
			}

			// Straight flush
			//
			for (int i = 0; i < numCards - 2; i++) {
				if ((i != ci[0]) && (i != ci[1])) {
					for (int j = i + 1; j < numCards - 1; j++) {
						if ((j != ci[0]) && (j != ci[1])) {
							for (int k = j + 1; k < numCards; k++) {
								if ((k != ci[0]) && (k != ci[1])) {
									float straight = he.isStraight(c[i], c[j],
											c[k]);
									if (he.isFlush(c[i], c[j], c[k])
											&& (straight > 0.0f)) {
										test = 120.0f + straight;
										if (test > best) {
											best = test;
										}
									}
								}
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
			for (int i = 0; i < numCards - 1; i++) {
				if ((i != ci[0]) && (i != ci[1])) {
					for (int j = i + 1; j < numCards; j++) {
						if ((j != ci[0]) && (j != ci[1])) {
							if (c[i].getRank() == c[j].getRank()) {
								for (int k = 0; k < numCards; k++) {
									if ((k != ci[0]) && (k != ci[1])
											&& (k != i) && (k != j)) {
										test = 105.0f + he.rankHand(c[i])
												+ he.kicker(c[k]);
										if (test > best) {
											best = test;
										}
									}
								}
							}
						}
					}
				}
			}
			if (best >= 105.0f) {
				return best;
			}

			// No full house available with 2 wilds.
			// Flush
			//
			for (int i = 0; i < numCards - 2; i++) {
				if ((i != ci[0]) && (i != ci[1])) {
					for (int j = i + 1; j < numCards - 1; j++) {
						if ((j != ci[0]) && (j != ci[1])) {
							for (int k = j + 1; k < numCards; k++) {
								if ((k != ci[0]) && (k != ci[1])) {
									if (he.isFlush(c[i], c[j], c[k])) {
										test = 89.0f + he.kicker(new Card(
												Card.ACE, c[i].getSuit()),
												c[i], c[j], c[k]);
										if (test > best) {
											best = test;
										}
									}
								}
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
			for (int i = 0; i < numCards - 2; i++) {
				if ((i != ci[0]) && (i != ci[1])) {
					for (int j = i + 1; j < numCards - 1; j++) {
						if ((j != ci[0]) && (j != ci[1])) {
							for (int k = j + 1; k < numCards; k++) {
								if ((k != ci[0]) && (k != ci[1])) {
									if (((c[i].getRank() == Card.ACE) || (c[i]
											.getRank() >= Card.TEN))
											&& ((c[j].getRank() == Card.ACE) || (c[j]
													.getRank() >= Card.TEN))
											&& ((c[k].getRank() == Card.ACE) || (c[k]
													.getRank() >= Card.TEN))) {
										return 74.0f;
									}
									if ((Math.abs(c[i].getRank()
											- c[j].getRank()) <= 4.0)
											&& (Math.abs(c[i].getRank()
													- c[k].getRank()) <= 4.0)
											&& (Math.abs(c[j].getRank()
													- c[k].getRank()) <= 4.0)) {
										if ((c[i].getRank() <= c[j].getRank())
												&& (c[i].getRank() <= c[k]
														.getRank())) {
											test = 60.0f + (float) c[i]
													.getRank() + 4.0f;
											if (test > best) {
												best = test;
											}
										} else if (c[j].getRank() <= c[k]
												.getRank()) {
											test = 60.0f + (float) c[j]
													.getRank() + 4.0f;
											if (test > best) {
												best = test;
											}
										} else {
											test = 60.0f + (float) c[k]
													.getRank() + 4.0f;
											if (test > best) {
												best = test;
											}
										}
									}
								}
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
			for (int i = 0; i < numCards - 2; i++) {
				if ((i != ci[0]) && (i != ci[1])) {
					for (int j = i + 1; j < numCards - 1; j++) {
						if ((j != ci[0]) && (j != ci[1])) {
							for (int k = j + 1; k < numCards; k++) {
								if ((k != ci[0]) && (k != ci[1])) {
									if ((he.rankHand(c[i]) >= he.rankHand(c[j]))
											&& (he.rankHand(c[i]) >= he
													.rankHand(c[k]))) {
										test = 45.0f + he.rankHand(c[i])
												+ he.kicker(c[j], c[k]);
										if (test > best) {
											best = test;
										}
									} else if (he.rankHand(c[j]) >= he
											.rankHand(c[k])) {
										test = 45.0f + he.rankHand(c[j])
												+ he.kicker(c[i], c[k]);
										if (test > best) {
											best = test;
										}
									} else {
										test = 45.0f + he.rankHand(c[k])
												+ he.kicker(c[i], c[j]);
										if (test > best) {
											best = test;
										}
									}
								}
							}
						}
					}
				}
			}
			return best;
			//
			// Only 1 wild card
			//
		} else {
			for (int i = 0; i < numCards - 3; i++) {
				if (i != ci[0]) {
					for (int j = i + 1; j < numCards - 2; j++) {
						if (j != ci[0]) {
							for (int k = j + 1; k < numCards - 1; k++) {
								if (k != ci[0]) {
									for (int l = k + 1; l < numCards; l++) {
										if (l != ci[0]) {
											for (int r0 = 1; r0 <= Card.NUM_RANKS; r0++) {
												for (int s0 = 1; s0 <= Card.NUM_SUITS; s0++) {
													test = he.rankHand(
															new Card(r0, s0),
															c[i], c[j], c[k],
															c[l]);
													if (test > best) {
														best = test;
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
			return best;
		}
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
}
