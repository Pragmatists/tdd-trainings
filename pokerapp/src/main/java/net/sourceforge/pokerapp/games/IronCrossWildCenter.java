/******************************************************************************************
 * IronCrossWildCenter.java        PokerApp                                               *
 *                                                                                        *
 *   Revision History                                                                     *
 * +---------+----------+---------------------------------------------------------------+ *
 * | Version | DATE     | Description                                                   | *
 * +---------+----------+---------------------------------------------------------------+ *
 * |  0.99   | 05/24/05 | Initial documented release                                    | *
 * |  1.00   | 06/06/05 | Added logging.                                                | *
 * |  1.00   | 08/10/07 | Prepare for open source.  Header/comments/package/etc...      | *
 * +---------+----------+---------------------------------------------------------------+ *
 *                                                                                        *
 ******************************************************************************************/

package net.sourceforge.pokerapp.games;

import net.sourceforge.pokerapp.*;

/****************************************************
 * Iron Cross Wild Center game definition.
 * 
 * @author Dan Puperi
 * @version 1.00
 * 
 **/
public class IronCrossWildCenter extends IronCross {

	/***********************
	 * Constructor creates an instance of a game of Iron Cross Wild Center
	 * 
	 * @param a
	 *            The StartPoker instance to which this game belongs
	 * 
	 **/
	public IronCrossWildCenter(StartPoker a) {
		super(a, "Iron Cross, with the center Wild", false);
		theApp.log("Constructing a game of Iron Cross Wild Center", 3);
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
		theApp.log("IronCrossWildCenter( " + h + " )", 3);
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
		HandEvaluator he = new HandEvaluator();

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
		if (numShareCards < 5) {
			return best;
		}
		wildRank = (int) he.rankHand(cshare[4]);
		//
		// Assume that all wild cards will be part of played hand - find them.
		//
		for (int i = 0; i < numHoleCards; i++) {
			if (((int) he.rankHand(chole[i]) == wildRank) && (numHoleWilds < 2)) {
				cholei[numHoleWilds] = i;
				numHoleWilds++;
			}
		}
		//
		// Iron Cross hand can be foremed by shared cards 1,3,5 or 2,4,5
		//
		// Count number of shared wild cards.
		//
		int dir1Count = 1;
		int dir2Count = 1;
		if ((int) he.rankHand(cshare[0]) == wildRank) {
			dir1Count++;
		}
		if ((int) he.rankHand(cshare[2]) == wildRank) {
			dir1Count++;
		}
		if ((int) he.rankHand(cshare[1]) == wildRank) {
			dir2Count++;
		}
		if ((int) he.rankHand(cshare[3]) == wildRank) {
			dir2Count++;
		}

		int dir = 0;
		numShareWilds = dir1Count;
		if (dir1Count > dir2Count) {
			dir = 1;
		} else if (dir2Count > dir1Count) {
			numShareWilds = dir2Count;
			dir = 2;
		}

		if (numHoleWilds == 0) {
			//
			// Three wild cards - have at least a 4 of a kind.
			//
			if (numShareWilds == 3) {
				//
				// Royal straight flush
				//
				for (int i = 0; i < numHoleCards - 1; i++) {
					for (int j = i + 1; j < numHoleCards; j++) {
						if ((he.isStraight(chole[i], chole[j]) == 14.0f)
								&& (chole[i].getSuit() == chole[j].getSuit())) {
							return 150.0f;
						}
					}
				}
				//
				// Five of a kind
				//
				for (int i = 0; i < numHoleCards - 1; i++) {
					for (int j = i + 1; j < numHoleCards; j++) {
						if (chole[i].getRank() == chole[j].getRank()) {
							test = 135.0f + he.rankHand(chole[i]);
							if (test > best) {
								best = test;
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
				for (int i = 0; i < numHoleCards - 1; i++) {
					for (int j = i + 1; j < numHoleCards; j++) {
						float straight = he.isStraight(chole[i], chole[j]);
						if ((chole[i].getSuit() == chole[j].getSuit())
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
				for (int i = 0; i < numHoleCards - 1; i++) {
					for (int j = i + 1; j < numHoleCards; j++) {
						if (he.rankHand(chole[i]) >= he.rankHand(chole[j])) {
							test = 105.0f + he.rankHand(chole[i])
									+ he.kicker(chole[j]);
							if (test > best) {
								best = test;
							}
						} else {
							test = 105.0f + he.rankHand(chole[j])
									+ he.kicker(chole[i]);
							if (test > best) {
								best = test;
							}
						}
					}
				}
				return best;
				//
				// Two Wilds
				//
			} else if (numShareWilds == 2) {
				//
				// Royal straight flush
				//
				for (int i = 0; i < numHoleCards - 1; i++) {
					for (int j = i + 1; j < numHoleCards; j++) {
						if ((dir == 1) || (dir == 0)) {
							int notWild;
							if ((int) he.rankHand(cshare[0]) != wildRank) {
								notWild = 0;
							} else {
								notWild = 2;
							}
							if ((he.isStraight(chole[i], chole[j],
									cshare[notWild]) == 14.0f)
									&& he.isFlush(chole[i], chole[j],
											cshare[notWild])) {
								return 150.0f;
							}
						}
						if ((dir == 2) || (dir == 0)) {
							int notWild;
							if ((int) he.rankHand(cshare[1]) != wildRank) {
								notWild = 1;
							} else {
								notWild = 3;
							}
							if ((he.isStraight(chole[i], chole[j],
									cshare[notWild]) == 14.0f)
									&& he.isFlush(chole[i], chole[j],
											cshare[notWild])) {
								return 150.0f;
							}
						}
					}
				}

				// Five of a kind
				//
				for (int i = 0; i < numHoleCards - 1; i++) {
					for (int j = i + 1; j < numHoleCards - 1; j++) {
						if ((dir == 1) || (dir == 0)) {
							int notWild;
							if ((int) he.rankHand(cshare[0]) != wildRank) {
								notWild = 0;
							} else {
								notWild = 2;
							}
							if ((chole[i].getRank() == chole[j].getRank())
									&& (chole[i].getRank() == cshare[notWild]
											.getRank())) {
								test = 135.0f + he.rankHand(chole[i]);
								if (test > best) {
									best = test;
								}
							}
						}
						if ((dir == 2) || (dir == 0)) {
							int notWild;
							if ((int) he.rankHand(cshare[1]) != wildRank) {
								notWild = 1;
							} else {
								notWild = 3;
							}
							if ((chole[i].getRank() == chole[j].getRank())
									&& (chole[i].getRank() == cshare[notWild]
											.getRank())) {
								test = 135.0f + he.rankHand(chole[i]);
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
				for (int i = 0; i < numHoleCards - 1; i++) {
					for (int j = i + 1; j < numHoleCards; j++) {
						if ((dir == 1) || (dir == 0)) {
							int notWild;
							if ((int) he.rankHand(cshare[0]) != wildRank) {
								notWild = 0;
							} else {
								notWild = 2;
							}
							float straight = he.isStraight(chole[i], chole[j],
									cshare[notWild]);
							if (he.isFlush(chole[i], chole[j], cshare[notWild])
									&& (straight > 0.0f)) {
								test = 120.0f + straight;
								if (test > best) {
									best = test;
								}
							}
						}
						if ((dir == 2) || (dir == 0)) {
							int notWild;
							if ((int) he.rankHand(cshare[1]) != wildRank) {
								notWild = 1;
							} else {
								notWild = 3;
							}
							float straight = he.isStraight(chole[i], chole[j],
									cshare[notWild]);
							if (he.isFlush(chole[i], chole[j], cshare[notWild])
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
				for (int i = 0; i < numHoleCards - 1; i++) {
					for (int j = i + 1; j < numHoleCards; j++) {
						if ((dir == 1) || (dir == 0)) {
							int notWild;
							if ((int) he.rankHand(cshare[0]) != wildRank) {
								notWild = 0;
							} else {
								notWild = 2;
							}
							if (chole[i].getRank() == chole[j].getRank()) {
								test = 105.0f + he.rankHand(chole[i])
										+ he.kicker(cshare[notWild]);
								if (test > best) {
									best = test;
								}
							}
							if ((chole[i].getRank() == cshare[notWild]
									.getRank())
									|| (chole[j].getRank() == cshare[notWild]
											.getRank())) {
								if (chole[i].getRank() > chole[j].getRank()) {
									test = 105.0f
											+ he.rankHand(cshare[notWild])
											+ he.kicker(chole[i]);
								} else {
									test = 105.0f
											+ he.rankHand(cshare[notWild])
											+ he.kicker(chole[j]);
								}
								if (test > best) {
									best = test;
								}
							}
						}
						if ((dir == 2) || (dir == 0)) {
							int notWild;
							if ((int) he.rankHand(cshare[1]) != wildRank) {
								notWild = 1;
							} else {
								notWild = 3;
							}
							if (chole[i].getRank() == chole[j].getRank()) {
								test = 105.0f + he.rankHand(chole[i])
										+ he.kicker(cshare[notWild]);
								if (test > best) {
									best = test;
								}
							}
							if ((chole[i].getRank() == cshare[notWild]
									.getRank())
									|| (chole[j].getRank() == cshare[notWild]
											.getRank())) {
								if (chole[i].getRank() > chole[j].getRank()) {
									test = 105.0f
											+ he.rankHand(cshare[notWild])
											+ he.kicker(chole[i]);
								} else {
									test = 105.0f
											+ he.rankHand(cshare[notWild])
											+ he.kicker(chole[j]);
								}
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
				// No full house available with 2 wilds.
				// Flush
				//
				for (int i = 0; i < numHoleCards - 1; i++) {
					for (int j = i + 1; j < numHoleCards; j++) {
						if ((dir == 1) || (dir == 0)) {
							int notWild;
							if ((int) he.rankHand(cshare[0]) != wildRank) {
								notWild = 0;
							} else {
								notWild = 2;
							}
							if (he.isFlush(chole[i], chole[j], cshare[notWild])) {
								test = 89.0f + he.kicker(new Card(Card.ACE,
										chole[i].getSuit()), chole[i],
										chole[j], cshare[notWild]);
								if (test > best) {
									best = test;
								}
							}
						}
						if ((dir == 2) || (dir == 0)) {
							int notWild;
							if ((int) he.rankHand(cshare[1]) != wildRank) {
								notWild = 1;
							} else {
								notWild = 3;
							}
							if (he.isFlush(chole[i], chole[j], cshare[notWild])) {
								test = 89.0f + he.kicker(new Card(Card.ACE,
										chole[i].getSuit()), chole[i],
										chole[j], cshare[notWild]);
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
				for (int i = 0; i < numHoleCards - 1; i++) {
					for (int j = i + 1; j < numHoleCards; j++) {
						if ((dir == 1) || (dir == 0)) {
							int notWild;
							if ((int) he.rankHand(cshare[0]) != wildRank) {
								notWild = 0;
							} else {
								notWild = 2;
							}
							float straight = he.isStraight(chole[i], chole[j],
									cshare[notWild]);
							if (straight > 0.0f) {
								test = 60.0f + straight;
								if (test > best) {
									best = test;
								}
							}
						}
						if ((dir == 2) || (dir == 0)) {
							int notWild;
							if ((int) he.rankHand(cshare[1]) != wildRank) {
								notWild = 1;
							} else {
								notWild = 3;
							}
							float straight = he.isStraight(chole[i], chole[j],
									cshare[notWild]);
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
				for (int i = 0; i < numHoleCards - 1; i++) {
					for (int j = i + 1; j < numHoleCards; j++) {
						if ((dir == 1) || (dir == 0)) {
							int notWild;
							if ((int) he.rankHand(cshare[0]) != wildRank) {
								notWild = 0;
							} else {
								notWild = 2;
							}
							if ((he.rankHand(chole[i]) >= he.rankHand(chole[j]))
									&& (he.rankHand(chole[i]) >= he
											.rankHand(cshare[notWild]))) {
								test = 45.0f + he.rankHand(chole[i])
										+ he.kicker(chole[j], cshare[notWild]);
								if (test > best) {
									best = test;
								}
							} else if (he.rankHand(chole[j]) >= he
									.rankHand(cshare[notWild])) {
								test = 45.0f + he.rankHand(chole[j])
										+ he.kicker(chole[i], cshare[notWild]);
								if (test > best) {
									best = test;
								}
							} else {
								test = 45.0f + he.rankHand(cshare[notWild])
										+ he.kicker(chole[i], chole[j]);
								if (test > best) {
									best = test;
								}
							}
						}
						if ((dir == 2) || (dir == 0)) {
							int notWild;
							if ((int) he.rankHand(cshare[1]) != wildRank) {
								notWild = 1;
							} else {
								notWild = 3;
							}
							if ((he.rankHand(chole[i]) >= he.rankHand(chole[j]))
									&& (he.rankHand(chole[i]) >= he
											.rankHand(cshare[notWild]))) {
								test = 45.0f + he.rankHand(chole[i])
										+ he.kicker(chole[j], cshare[notWild]);
								if (test > best) {
									best = test;
								}
							} else if (he.rankHand(chole[j]) >= he
									.rankHand(cshare[notWild])) {
								test = 45.0f + he.rankHand(chole[j])
										+ he.kicker(chole[i], cshare[notWild]);
								if (test > best) {
									best = test;
								}
							} else {
								test = 45.0f + he.rankHand(cshare[notWild])
										+ he.kicker(chole[i], chole[j]);
								if (test > best) {
									best = test;
								}
							}
						}
					}
				}
				return best;
				//
				// Only the center card is wild
				//
			} else {
				for (int i = 0; i < numHoleCards - 1; i++) {
					for (int j = i + 1; j < numHoleCards; j++) {
						for (int r0 = 1; r0 <= Card.NUM_RANKS; r0++) {
							for (int s0 = 1; s0 <= Card.NUM_SUITS; s0++) {
								test = he.rankHand(new Card(r0, s0), chole[i],
										chole[j], cshare[0], cshare[2]);
								if (test > best) {
									best = test;
								}
								test = he.rankHand(new Card(r0, s0), chole[i],
										chole[j], cshare[1], cshare[3]);
								if (test > best) {
									best = test;
								}
							}
						}
					}
				}
			}
		} else if (numHoleWilds == 1) {
			//
			// Four wild cards, must have at least a five of a kind.
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
				// Three wild cards, must have at least a four of a kind.
				//
				// Royal straight flush
				//
			} else if (numShareWilds == 2) {
				for (int i = 0; i < numHoleCards; i++) {
					if (i != cholei[0]) {
						if ((dir == 1) || (dir == 0)) {
							int notWild;
							if ((int) he.rankHand(cshare[0]) != wildRank) {
								notWild = 0;
							} else {
								notWild = 2;
							}
							if ((he.isStraight(chole[i], cshare[notWild]) == 14.0f)
									&& (chole[i].getSuit() == cshare[notWild]
											.getSuit())) {
								return 150.0f;
							}
						}
						if ((dir == 2) || (dir == 0)) {
							int notWild;
							if ((int) he.rankHand(cshare[1]) != wildRank) {
								notWild = 1;
							} else {
								notWild = 3;
							}
							if ((he.isStraight(chole[i], cshare[notWild]) == 14.0f)
									&& (chole[i].getSuit() == cshare[notWild]
											.getSuit())) {
								return 150.0f;
							}
						}
					}
				}
				//
				// Five of a kind
				//
				for (int i = 0; i < numHoleCards; i++) {
					if (i != cholei[0]) {
						if ((dir == 1) || (dir == 0)) {
							int notWild;
							if ((int) he.rankHand(cshare[0]) != wildRank) {
								notWild = 0;
							} else {
								notWild = 2;
							}
							if (chole[i].getRank() == cshare[notWild].getRank()) {
								test = 135.0f + he.rankHand(chole[i]);
								if (test > best) {
									best = test;
								}
							}
						}
						if ((dir == 2) || (dir == 0)) {
							int notWild;
							if ((int) he.rankHand(cshare[1]) != wildRank) {
								notWild = 1;
							} else {
								notWild = 3;
							}
							if (chole[i].getRank() == cshare[notWild].getRank()) {
								test = 135.0f + he.rankHand(chole[i]);
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
				for (int i = 0; i < numHoleCards; i++) {
					if (i != cholei[0]) {
						if ((dir == 1) || (dir == 0)) {
							int notWild;
							if ((int) he.rankHand(cshare[0]) != wildRank) {
								notWild = 0;
							} else {
								notWild = 2;
							}
							float straight = he.isStraight(chole[i],
									cshare[notWild]);
							if ((chole[i].getSuit() == cshare[notWild]
									.getSuit()) && (straight > 0.0f)) {
								test = 120.0f + straight;
								if (test > best) {
									best = test;
								}
							}
						}
						if ((dir == 2) || (dir == 0)) {
							int notWild;
							if ((int) he.rankHand(cshare[1]) != wildRank) {
								notWild = 1;
							} else {
								notWild = 3;
							}
							float straight = he.isStraight(chole[i],
									cshare[notWild]);
							if ((chole[i].getSuit() == cshare[notWild]
									.getSuit()) && (straight > 0.0f)) {
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
				for (int i = 0; i < numHoleCards; i++) {
					if (i != cholei[0]) {
						if ((dir == 1) || (dir == 0)) {
							int notWild;
							if ((int) he.rankHand(cshare[0]) != wildRank) {
								notWild = 0;
							} else {
								notWild = 2;
							}
							if (chole[i].getRank() > cshare[notWild].getRank()) {
								test = 105.0f + he.rankHand(chole[i])
										+ he.kicker(cshare[notWild]);
								if (test > best) {
									best = test;
								}
							} else {
								test = 105.0f + he.rankHand(cshare[notWild])
										+ he.kicker(chole[i]);
								if (test > best) {
									best = test;
								}
							}
						}
						if ((dir == 2) || (dir == 0)) {
							int notWild;
							if ((int) he.rankHand(cshare[1]) != wildRank) {
								notWild = 1;
							} else {
								notWild = 3;
							}
							if (chole[i].getRank() > cshare[notWild].getRank()) {
								test = 105.0f + he.rankHand(chole[i])
										+ he.kicker(cshare[notWild]);
								if (test > best) {
									best = test;
								}
							} else {
								test = 105.0f + he.rankHand(cshare[notWild])
										+ he.kicker(chole[i]);
								if (test > best) {
									best = test;
								}
							}
						}
					}
				}
				return best;
				//
				// Two wild cards, must have at least a three of a kind
				//
				//
				// Royal straight flush
				//
			} else {
				for (int i = 0; i < numHoleCards; i++) {
					if (i != cholei[0]) {
						if ((he.isStraight(chole[i], cshare[0], cshare[2]) == 14.0f)
								&& he.isFlush(chole[i], cshare[0], cshare[2])) {
							return 150.0f;
						}
						if ((he.isStraight(chole[i], cshare[1], cshare[3]) == 14.0f)
								&& he.isFlush(chole[i], cshare[1], cshare[3])) {
							return 150.0f;
						}
					}
				}

				// Five of a kind
				//
				for (int i = 0; i < numHoleCards; i++) {
					if (i != cholei[0]) {
						if ((chole[i].getRank() == cshare[0].getRank())
								&& (chole[i].getRank() == cshare[2].getRank())) {
							test = 135.0f + he.rankHand(chole[i]);
							if (test > best) {
								best = test;
							}
						}
						if ((chole[i].getRank() == cshare[1].getRank())
								&& (chole[i].getRank() == cshare[3].getRank())) {
							test = 135.0f + he.rankHand(chole[i]);
							if (test > best) {
								best = test;
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
						float straight = he.isStraight(chole[i], cshare[0],
								cshare[2]);
						if (he.isFlush(chole[i], cshare[0], cshare[2])
								&& (straight > 0.0f)) {
							test = 120.0f + straight;
							if (test > best) {
								best = test;
							}
						}
						straight = he
								.isStraight(chole[i], cshare[1], cshare[3]);
						if (he.isFlush(chole[i], cshare[1], cshare[3])
								&& (straight > 0.0f)) {
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
				for (int i = 0; i < numHoleCards; i++) {
					if (i != cholei[0]) {
						if (cshare[0].getRank() == cshare[2].getRank()) {
							test = 105.0f + he.rankHand(cshare[0])
									+ he.kicker(chole[i]);
							if (test > best) {
								best = test;
							}
						}
						if (cshare[1].getRank() == cshare[3].getRank()) {
							test = 105.0f + he.rankHand(cshare[1])
									+ he.kicker(chole[i]);
							if (test > best) {
								best = test;
							}
						}
						if (chole[i].getRank() == cshare[0].getRank()) {
							test = 105.0f + he.rankHand(chole[i])
									+ he.kicker(cshare[2]);
							if (test > best) {
								best = test;
							}
						}
						if (chole[i].getRank() == cshare[1].getRank()) {
							test = 105.0f + he.rankHand(chole[i])
									+ he.kicker(cshare[3]);
							if (test > best) {
								best = test;
							}
						}
						if (chole[i].getRank() == cshare[2].getRank()) {
							test = 105.0f + he.rankHand(chole[i])
									+ he.kicker(cshare[0]);
							if (test > best) {
								best = test;
							}
						}
						if (chole[i].getRank() == cshare[3].getRank()) {
							test = 105.0f + he.rankHand(chole[i])
									+ he.kicker(cshare[1]);
							if (test > best) {
								best = test;
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
						if (he.isFlush(chole[i], cshare[0], cshare[2])) {
							test = 89.0f + he.kicker(new Card(Card.ACE,
									chole[i].getSuit()), chole[i], cshare[0],
									cshare[2]);
							if (test > best) {
								best = test;
							}
						}

						if (he.isFlush(chole[i], cshare[1], cshare[3])) {
							test = 89.0f + he.kicker(new Card(Card.ACE,
									chole[i].getSuit()), chole[i], cshare[1],
									cshare[3]);
							if (test > best) {
								best = test;
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
						float straight = he.isStraight(chole[i], cshare[0],
								cshare[2]);
						if (straight > 0.0f) {
							test = 60.0f + straight;
							if (test > best) {
								best = test;
							}
						}
						straight = he
								.isStraight(chole[i], cshare[1], cshare[3]);
						if (straight > 0.0f) {
							test = 60.0f + straight;
							if (test > best) {
								best = test;
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
						if ((he.rankHand(chole[i]) >= he.rankHand(cshare[0]))
								&& (he.rankHand(chole[i]) >= he
										.rankHand(cshare[2]))) {
							test = 45.0f + he.rankHand(chole[i])
									+ he.kicker(cshare[0], cshare[2]);
							if (test > best) {
								best = test;
							}
						} else if (he.rankHand(cshare[0]) >= he
								.rankHand(cshare[2])) {
							test = 45.0f + he.rankHand(cshare[0])
									+ he.kicker(chole[i], cshare[2]);
							if (test > best) {
								best = test;
							}
						} else {
							test = 45.0f + he.rankHand(cshare[2])
									+ he.kicker(chole[i], cshare[0]);
							if (test > best) {
								best = test;
							}
						}
						if ((he.rankHand(chole[i]) >= he.rankHand(cshare[1]))
								&& (he.rankHand(chole[i]) >= he
										.rankHand(cshare[3]))) {
							test = 45.0f + he.rankHand(chole[i])
									+ he.kicker(cshare[1], cshare[3]);
							if (test > best) {
								best = test;
							}
						} else if (he.rankHand(cshare[1]) >= he
								.rankHand(cshare[3])) {
							test = 45.0f + he.rankHand(cshare[1])
									+ he.kicker(chole[i], cshare[3]);
							if (test > best) {
								best = test;
							}
						} else {
							test = 45.0f + he.rankHand(cshare[3])
									+ he.kicker(chole[i], cshare[1]);
							if (test > best) {
								best = test;
							}
						}
					}
				}
				return best;
			}
		} else {
			//
			// Four wild cards. Must have at least a five of a kind.
			//
			if (numShareWilds == 2) {
				int notWild;
				if ((int) he.rankHand(cshare[0]) != wildRank) {
					notWild = 0;
				} else {
					notWild = 2;
				}
				if ((cshare[notWild].getRank() == Card.ACE)
						|| (cshare[notWild].getRank() >= Card.TEN)) {
					return 150.0f;
				} else {
					test = 135.0f + he.rankHand(cshare[notWild]);
					if (test > best) {
						best = test;
					}
				}

				if ((int) he.rankHand(cshare[1]) != wildRank) {
					notWild = 1;
				} else {
					notWild = 3;
				}
				if ((cshare[notWild].getRank() == Card.ACE)
						|| (cshare[notWild].getRank() >= Card.TEN)) {
					return 150.0f;
				} else {
					test = 135.0f + he.rankHand(cshare[notWild]);
					if (test > best) {
						best = test;
					}
				}
				return best;
			} else {
				//
				// Three wild cards. Must have at least a four of a kind.
				//
				// Royal straight flush
				//
				if ((he.isStraight(cshare[0], cshare[2]) == 14.0f)
						&& (cshare[0].getSuit() == cshare[2].getSuit())) {
					return 150.0f;
				}
				if ((he.isStraight(cshare[1], cshare[3]) == 14.0f)
						&& (cshare[1].getSuit() == cshare[3].getSuit())) {
					return 150.0f;
				}
				//
				// Five of a kind
				//

				if (cshare[0].getRank() == cshare[2].getRank()) {
					test = 135.0f + he.rankHand(cshare[0]);
					if (test > best) {
						best = test;
					}
				}
				if (cshare[1].getRank() == cshare[3].getRank()) {
					test = 135.0f + he.rankHand(cshare[1]);
					if (test > best) {
						best = test;
					}
				}

				if (best >= 135.0f) {
					return best;
				}
				//
				// Straight flush
				//
				float straight = he.isStraight(cshare[0], cshare[2]);
				if ((cshare[0].getSuit() == cshare[2].getSuit())
						&& (straight > 0.0)) {
					test = 120.0f + straight;
					if (test > best) {
						best = test;
					}
				}
				straight = he.isStraight(cshare[1], cshare[3]);
				if ((cshare[1].getSuit() == cshare[3].getSuit())
						&& (straight > 0.0)) {
					test = 120.0f + straight;
					if (test > best) {
						best = test;
					}
				}
				if (best >= 120.0f) {
					return best;
				}
				//
				// Four of a kind
				//
				if (he.rankHand(cshare[0]) >= he.rankHand(cshare[2])) {
					test = 105.0f + he.rankHand(cshare[0])
							+ he.kicker(cshare[2]);
					if (test > best) {
						best = test;
					}
				} else {
					test = 105.0f + he.rankHand(cshare[2])
							+ he.kicker(cshare[0]);
					if (test > best) {
						best = test;
					}
				}
				if (he.rankHand(cshare[1]) >= he.rankHand(cshare[3])) {
					test = 105.0f + he.rankHand(cshare[1])
							+ he.kicker(cshare[3]);
					if (test > best) {
						best = test;
					}
				} else {
					test = 105.0f + he.rankHand(cshare[3])
							+ he.kicker(cshare[1]);
					if (test > best) {
						best = test;
					}
				}
				return best;
			}
		}
		return best;
	}
}
