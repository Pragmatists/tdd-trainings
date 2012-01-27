/******************************************************************************************
 * OmahaShipwreck.java             PokerApp                                               *
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
 * |  0.99   | 05/22/05 | Place the deal call in this constructor - safer.              | *
 * |  1.00   | 06/06/05 | Added logging.                                                | *
 * +---------+----------+---------------------------------------------------------------+ *
 *                                                                                        *
 ******************************************************************************************/

package net.sourceforge.pokerapp.games;

import net.sourceforge.pokerapp.*;

/****************************************************
 * Omaha Hold'Em with shipwreck game definition.
 * 
 * @author Dan Puperi
 * @version 1.00
 * 
 **/
public class OmahaShipwreck extends OmahaHoldEm {

	/***********************
	 * Constructor creates an instance of a game of Omaha Hold Em with Shipwreck
	 * 
	 * @param a
	 *            The StartPoker instance to which this game belongs
	 * 
	 **/
	public OmahaShipwreck(StartPoker a) {
		super(a, "Omaha Hold'Em with Shipwreck", false);
		if (theApp.getPlayerList().size() < 2) {
			return;
		}
		theApp.log("Constructing a game of Omaha Hold'Em with Shipwreck", 3);
		deal();
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
		theApp.log("OmahaShipwreck.bestHand( " + h + " )", 3);
		int numHoleCards = h.getNumHole();
		int numShareCards = h.getNumShared();
		Card[] chole = new Card[numHoleCards];
		Card[] cshare = new Card[numShareCards];
		float best = 0.0f;
		float test = 0.0f;
		int wildRank = 15;
		int numShareWilds = 0;
		int numHoleWilds = 0;
		int[] cholei = new int[2];
		int[] csharei = new int[3];
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

		// Find lowest card in hole...that is wild along with all other of that
		// suit
		for (int i = 0; i < numHoleCards; i++) {
			if ((int) he.rankHand(chole[i]) < wildRank) {
				wildRank = (int) he.rankHand(chole[i]);
			}
		}

		// Assume that all wild cards will be part of played hand - find them.
		for (int i = 0; i < numHoleCards; i++) {
			if (((int) he.rankHand(chole[i]) == wildRank) && (numHoleWilds < 2)) {
				cholei[numHoleWilds] = i;
				numHoleWilds++;
			}
		}
		for (int i = 0; i < numShareCards; i++) {
			if ((int) he.rankHand(cshare[i]) == wildRank) {
				csharei[numShareWilds] = i;
				numShareWilds++;
			}
		}
		//
		// If there are 4 wilds, only need to look for royal straight flush or
		// five of a kind.
		//
		if (numHoleWilds == 2) {
			if (numShareWilds == 2) {
				for (int i = 0; i < numShareCards; i++) {
					if ((i != csharei[0]) && (i != csharei[1])) {
						if ((cshare[i].getRank() == Card.ACE)
								|| (cshare[i].getRank() >= Card.TEN)) {
							//
							// Royal straight flush
							//
							return 150.0f;
						} else {
							//
							// Five of a kind
							//
							test = 135.0f + he.rankHand(cshare[i]);
							if (test > best) {
								best = test;
							}
						}
					}
				}
				return best;

				//
				// Three wilds - must have at least 4 of a kind
				//
			} else if (numShareWilds == 1) {
				//
				// Royal straight flush
				//
				for (int i = 0; i < numShareCards - 1; i++) {
					if (i != csharei[0]) {
						for (int j = i + 1; j < numShareCards; j++) {
							if (j != csharei[0]) {
								if ((he.isStraight(cshare[i], cshare[j]) == 14.0f)
										&& (cshare[i].getSuit() == cshare[j]
												.getSuit())) {
									return 150.0f;
								}
							}
						}
					}
				}
				//
				// Five of a kind
				//
				for (int i = 0; i < numShareCards - 1; i++) {
					if (i != csharei[0]) {
						for (int j = i + 1; j < numShareCards; j++) {
							if (j != csharei[0]) {
								if (cshare[i].getRank() == cshare[j].getRank()) {
									test = 135.0f + he.rankHand(cshare[i]);
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
				//
				// Straight flush
				//
				for (int i = 0; i < numShareCards - 1; i++) {
					if (i != csharei[0]) {
						for (int j = i + 1; j < numShareCards; j++) {
							if (j != csharei[0]) {
								float straight = he.isStraight(cshare[i],
										cshare[j]);
								if ((cshare[i].getSuit() == cshare[j].getSuit())
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
				//
				// Four of a kind
				//
				for (int i = 0; i < numShareCards - 1; i++) {
					if (i != csharei[0]) {
						for (int j = i + 1; j < numShareCards; j++) {
							if (j != csharei[0]) {
								if (he.rankHand(cshare[i]) >= he
										.rankHand(cshare[j])) {
									test = 105.0f + he.rankHand(cshare[i])
											+ he.kicker(cshare[j]);
									if (test > best) {
										best = test;
									}
								} else {
									test = 105.0f + he.rankHand(cshare[j])
											+ he.kicker(cshare[i]);
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
				// Two wild cards
				//
			} else {
				//
				// Royal straight flush
				//
				for (int i = 0; i < numShareCards - 2; i++) {
					for (int j = i + 1; j < numShareCards - 1; j++) {
						for (int k = j + 1; k < numShareCards; k++) {
							if ((he.isStraight(cshare[i], cshare[j], cshare[k]) == 14.0f)
									&& he.isFlush(cshare[i], cshare[j],
											cshare[k])) {
								return 150.0f;
							}
						}
					}
				}
				//
				// Five of a kind
				//
				for (int i = 0; i < numShareCards - 2; i++) {
					for (int j = i + 1; j < numShareCards - 1; j++) {
						for (int k = j + 1; k < numShareCards; k++) {
							if ((cshare[i].getRank() == cshare[j].getRank())
									&& (cshare[i].getRank() == cshare[k]
											.getRank())) {
								test = 135.0f + he.rankHand(cshare[i]);
								if (test > best) {
									best = test;
								}
							}
						}
					}
				}
				if (best >= 135.0f) {
					return best;
				}
				//
				// Straight flush
				//
				for (int i = 0; i < numShareCards - 2; i++) {
					for (int j = i + 1; j < numShareCards - 1; j++) {
						for (int k = j + 1; k < numShareCards; k++) {
							float straight = he.isStraight(cshare[i],
									cshare[j], cshare[k]);
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
				//
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
					}
				}
				if (best >= 105.0f) {
					return best;
				}
				//
				// No full house available with 2 wilds.
				// Flush
				//
				for (int i = 0; i < numShareCards - 2; i++) {
					for (int j = i + 1; j < numShareCards - 1; j++) {
						for (int k = j + 1; k < numShareCards; k++) {
							if (he.isFlush(cshare[i], cshare[j], cshare[k])) {
								test = 89.0f + he.kicker(new Card(Card.ACE,
										cshare[i].getSuit()), cshare[i],
										cshare[j], cshare[k]);
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
				//
				// Straight
				//
				for (int i = 0; i < numShareCards - 2; i++) {
					for (int j = i + 1; j < numShareCards - 1; j++) {
						for (int k = j + 1; k < numShareCards; k++) {
							float straight = he.isStraight(cshare[i],
									cshare[j], cshare[k]);
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
				//
				// Three of a kind
				//
				for (int i = 0; i < numShareCards - 2; i++) {
					for (int j = i + 1; j < numShareCards - 1; j++) {
						for (int k = j + 1; k < numShareCards; k++) {
							if ((he.rankHand(cshare[i]) >= he
									.rankHand(cshare[j]))
									&& (he.rankHand(cshare[i]) >= he
											.rankHand(cshare[k]))) {
								test = 45.0f + he.rankHand(cshare[i])
										+ he.kicker(cshare[j], cshare[k]);
								if (test > best) {
									best = test;
								}
							} else if (he.rankHand(cshare[j]) >= he
									.rankHand(cshare[k])) {
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
		} else {
			//
			// If there are 4 wilds, only need to look for royal straight flush
			// or five of a kind.
			//
			if (numShareWilds == 3) {
				for (int i = 0; i < numHoleCards; i++) {
					if (i != cholei[0]) {
						if ((chole[i].getRank() == Card.ACE)
								|| (chole[i].getRank() >= Card.TEN)) {
							return 150.0f;
						}
					}
				}
				for (int i = 0; i < numHoleCards; i++) {
					if (i != cholei[0]) {
						test = 135.0f + he.rankHand(chole[i]);
						if (test > best) {
							best = test;
						}
					}
				}
				return best;
				//
				// Three wild cards - have at least a 4 of a kind.
				//
			} else if (numShareWilds == 2) {
				//
				// Royal straight flush
				//
				for (int i = 0; i < numHoleCards; i++) {
					if (i != cholei[0]) {
						for (int j = 0; j < numShareCards; j++) {
							if ((j != csharei[0]) && (j != csharei[1])) {
								if ((he.isStraight(chole[i], cshare[j]) == 14.0f)
										&& (chole[i].getSuit() == cshare[j]
												.getSuit())) {
									return 150.0f;
								}
							}
						}
					}
				}
				//
				// Five of a kind
				//
				for (int i = 0; i < numHoleCards; i++) {
					if (i != cholei[0]) {
						for (int j = 0; j < numShareCards; j++) {
							if ((j != csharei[0]) && (j != csharei[1])) {
								if (chole[i].getRank() == cshare[j].getRank()) {
									test = 135.0f + he.rankHand(chole[i]);
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
				//
				// Straight flush
				//
				for (int i = 0; i < numHoleCards; i++) {
					if (i != cholei[0]) {
						for (int j = 0; j < numShareCards; j++) {
							if ((j != csharei[0]) && (j != csharei[1])) {
								float straight = he.isStraight(chole[i],
										cshare[j]);
								if ((chole[i].getSuit() == cshare[j].getSuit())
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
				//
				// Four of a kind
				//
				for (int i = 0; i < numHoleCards; i++) {
					if (i != cholei[0]) {
						for (int j = 0; j < numShareCards; j++) {
							if ((j != csharei[0]) && (j != csharei[1])) {
								if (he.rankHand(chole[i]) >= he
										.rankHand(cshare[j])) {
									test = 105.0f + he.rankHand(chole[i])
											+ he.kicker(cshare[j]);
									if (test > best) {
										best = test;
									}
								} else {
									test = 105.0f + he.rankHand(cshare[j])
											+ he.kicker(chole[i]);
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
				// Two wild cards
				//
			} else if (numShareWilds == 1) {
				//
				// Royal straight flush
				//
				for (int i = 0; i < numHoleCards; i++) {
					if (i != cholei[0]) {
						for (int j = 0; j < numShareCards - 1; j++) {
							if (j != csharei[0]) {
								for (int k = j + 1; k < numShareCards; k++) {
									if (k != csharei[0]) {
										if ((he.isStraight(chole[i], cshare[j],
												cshare[k]) == 14.0f)
												&& he.isFlush(chole[i],
														cshare[j], cshare[k])) {
											return 150.0f;
										}
									}
								}
							}
						}
					}
				}
				//
				// Five of a kind
				//
				for (int i = 0; i < numHoleCards; i++) {
					if (i != cholei[0]) {
						for (int j = 0; j < numShareCards - 1; j++) {
							if (j != csharei[0]) {
								for (int k = j + 1; k < numShareCards; k++) {
									if (k != csharei[0]) {
										if ((chole[i].getRank() == cshare[j]
												.getRank())
												&& (chole[i].getRank() == cshare[k]
														.getRank())) {
											test = 135.0f + he
													.rankHand(chole[i]);
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
				//
				// Straight flush
				//
				for (int i = 0; i < numHoleCards; i++) {
					if (i != cholei[0]) {
						for (int j = 0; j < numShareCards - 1; j++) {
							if (j != csharei[0]) {
								for (int k = j + 1; k < numShareCards; k++) {
									if (k != csharei[0]) {
										float straight = he.isStraight(
												chole[i], cshare[j], cshare[k]);
										if (he.isFlush(chole[i], cshare[j],
												cshare[k]) && (straight > 0.0f)) {
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
				//
				// Four of a kind
				//
				for (int i = 0; i < numHoleCards; i++) {
					if (i != cholei[0]) {
						for (int j = 0; j < numShareCards; j++) {
							if (j != csharei[0]) {
								if (chole[i].getRank() == cshare[j].getRank()) {
									for (int k = 0; k < numShareCards; k++) {
										if ((k != csharei[0]) && (k != j)) {
											test = 105.0f
													+ he.rankHand(chole[i])
													+ he.kicker(cshare[k]);
											if (test > best) {
												best = test;
											}
										}
									}
								}
								for (int k = 0; k < numShareCards; k++) {
									if ((k != csharei[0])
											&& (k != j)
											&& (cshare[j].getRank() == cshare[k]
													.getRank())) {
										test = 105.0f + he.rankHand(cshare[j])
												+ he.kicker(chole[i]);
										if (test > best) {
											best = test;
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
				//
				// No full house available with 2 wilds.
				// Flush
				//
				for (int i = 0; i < numHoleCards; i++) {
					if (i != cholei[0]) {
						for (int j = 0; j < numShareCards - 1; j++) {
							if (j != csharei[0]) {
								for (int k = j + 1; k < numShareCards; k++) {
									if (k != csharei[0]) {
										if (he.isFlush(chole[i], cshare[j],
												cshare[k])) {
											test = 89.0f + he.kicker(
													new Card(Card.ACE, chole[i]
															.getSuit()),
													chole[i], cshare[j],
													cshare[k]);
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
				//
				// Straight
				//
				for (int i = 0; i < numHoleCards; i++) {
					if (i != cholei[0]) {
						for (int j = 0; j < numShareCards - 1; j++) {
							if (j != csharei[0]) {
								for (int k = j + 1; k < numShareCards; k++) {
									if (k != csharei[0]) {
										float straight = he.isStraight(
												chole[i], cshare[j], cshare[k]);
										if (straight > 0.0f) {
											test = 60.0f + straight;
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
				if (best >= 60.0f) {
					return best;
				}
				//
				// Three of a kind
				//
				for (int i = 0; i < numHoleCards; i++) {
					if (i != cholei[0]) {
						for (int j = 0; j < numShareCards - 1; j++) {
							if (j != csharei[0]) {
								for (int k = j + 1; k < numShareCards; k++) {
									if (k != csharei[0]) {
										if ((he.rankHand(chole[i]) >= he
												.rankHand(cshare[j]))
												&& (he.rankHand(chole[i]) >= he
														.rankHand(cshare[k]))) {
											test = 45.0f
													+ he.rankHand(chole[i])
													+ he.kicker(cshare[j],
															cshare[k]);
											if (test > best) {
												best = test;
											}
										} else if (he.rankHand(cshare[j]) >= he
												.rankHand(cshare[k])) {
											test = 45.0f
													+ he.rankHand(cshare[j])
													+ he.kicker(chole[i],
															cshare[k]);
											if (test > best) {
												best = test;
											}
										} else {
											test = 45.0f
													+ he.rankHand(cshare[k])
													+ he.kicker(chole[i],
															cshare[j]);
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
				// Only one wild card
				//
			} else {
				for (int i = 0; i < numHoleCards; i++) {
					for (int j = 0; j < numShareCards - 2; j++) {
						for (int k = j + 1; k < numShareCards - 1; k++) {
							for (int l = k + 1; l < numShareCards; l++) {
								if (i != cholei[0]) {
									for (int r0 = 1; r0 <= Card.NUM_RANKS; r0++) {
										for (int s0 = 1; s0 <= Card.NUM_SUITS; s0++) {
											test = he.rankHand(
													new Card(r0, s0), chole[i],
													cshare[j], cshare[k],
													cshare[l]);
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
		float best = 29.9f;
		float test = 0.0f;
		//
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
		//
		// Now start checking for best possible hands
		//
		// Royal straight flush
		//
		for (int i = 0; i < numShareCards - 1; i++) {
			for (int j = i + 1; j < numShareCards; j++) {
				if ((he.isStraight(cshare[i], cshare[j]) == 14.0f)
						&& (cshare[i].getSuit() == cshare[j].getSuit())) {
					return 150.0f;
				}
			}
		}
		//
		// Five of a kind
		//
		for (int i = 0; i < numShareCards - 1; i++) {
			for (int j = i + 1; j < numShareCards; j++) {
				if (cshare[i].getRank() == cshare[j].getRank()) {
					for (int k = 0; k < numShareCards; k++) {
						test = 135.0f + he.rankHand(cshare[k]);
						if (test > best) {
							best = test;
						}
					}
				}
			}
		}
		if (best >= 135.0f) {
			return best;
		}
		//
		// Straight flush
		//
		for (int i = 0; i < numShareCards - 1; i++) {
			for (int j = i + 1; j < numShareCards; j++) {
				float straight = he.isStraight(cshare[i], cshare[j]);
				if ((cshare[i].getSuit() == cshare[j].getSuit())
						&& (straight > 0.0)) {
					test = 120.0f + straight;
					if (test > best) {
						best = test;
					}
				}
			}
		}
		if (best >= 120.0f) {
			return best;
		}
		//
		// Four of a kind
		//
		for (int i = 0; i < numShareCards - 1; i++) {
			for (int j = i + 1; j < numShareCards; j++) {
				if (he.rankHand(cshare[i]) >= he.rankHand(cshare[j])) {
					test = 105.0f + he.rankHand(cshare[i])
							+ he.kicker(cshare[j]);
					if (test > best) {
						best = test;
					}
				} else {
					test = 105.0f + he.rankHand(cshare[j])
							+ he.kicker(cshare[i]);
					if (test > best) {
						best = test;
					}
				}
			}
		}
		return best;
	}
}
