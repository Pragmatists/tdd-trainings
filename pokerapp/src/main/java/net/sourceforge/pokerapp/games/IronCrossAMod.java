/******************************************************************************************
 * IronCrossAMod.java              PokerApp                                               *
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
 * |  0.99   | 05/23/05 | Make this an extension of IronCross game                      | *
 * |  1.00   | 06/06/05 | Added logging                                                 | *
 * |  1.00   | 08/10/07 | Prepare for open source.  Header/comments/package/etc...      | *
 * +---------+----------+---------------------------------------------------------------+ *
 *                                                                                        *
 ******************************************************************************************/

package net.sourceforge.pokerapp.games;

import net.sourceforge.pokerapp.*;

/****************************************************
 * Iron Cross with A-Mod game definition.
 * 
 * @author Dan Puperi
 * @version 1.00
 * 
 **/
public class IronCrossAMod extends IronCross {

	/***********************
	 * Constructor creates an instance of a game of Iron Cross
	 * 
	 * @param a
	 *            The StartPoker instance to which this game belongs
	 * 
	 **/
	public IronCrossAMod(StartPoker a) {
		super(a, "Iron Cross, with the A-Mod", false);
		theApp.log("Constructing a game of Iron Cross with the A-Mod", 3);
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
		theApp.log("IronCrossAMod( " + h + " )", 3);
		int numHoleCards = h.getNumHole();
		int numShareCards = h.getNumShared();
		Card[] chole = new Card[numHoleCards];
		Card[] cshare = new Card[numShareCards];
		float best = 0.0f;
		float test = 0.0f;
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

		// Iron Cross hand can be foremed by shared cards 1,3,5 or 2,4,5
		// The AMod means any single shared card can change rank up or down by 1
		// or change to any suit
		for (int i = 0; i < numHoleCards - 1; i++) {
			for (int j = i + 1; j < numHoleCards; j++) {
				Card changed = new Card();

				// Check all suit changes first
				for (int a = 1; a < 5; a++) {
					changed = new Card(cshare[0].getRank(), a);
					test = he.rankHand(chole[i], chole[j], changed, cshare[2],
							cshare[4]);
					if (test > best) {
						best = test;
					}

					changed = new Card(cshare[2].getRank(), a);
					test = he.rankHand(chole[i], chole[j], cshare[0], changed,
							cshare[4]);
					if (test > best) {
						best = test;
					}

					changed = new Card(cshare[4].getRank(), a);
					test = he.rankHand(chole[i], chole[j], cshare[0],
							cshare[2], changed);
					if (test > best) {
						best = test;
					}

					changed = new Card(cshare[1].getRank(), a);
					test = he.rankHand(chole[i], chole[j], changed, cshare[3],
							cshare[4]);
					if (test > best) {
						best = test;
					}

					changed = new Card(cshare[3].getRank(), a);
					test = he.rankHand(chole[i], chole[j], cshare[1], changed,
							cshare[4]);
					if (test > best) {
						best = test;
					}

					changed = new Card(cshare[4].getRank(), a);
					test = he.rankHand(chole[i], chole[j], cshare[1],
							cshare[3], changed);
					if (test > best) {
						best = test;
					}
				}

				// Check for increasing or decreasing card rank by 1
				changed = new Card(cshare[0].getRank(), cshare[0].getSuit());
				changed.changeRank(1);
				test = he.rankHand(chole[i], chole[j], changed, cshare[2],
						cshare[4]);
				if (test > best) {
					best = test;
				}

				changed = new Card(cshare[0].getRank(), cshare[0].getSuit());
				changed.changeRank(-1);
				test = he.rankHand(chole[i], chole[j], changed, cshare[2],
						cshare[4]);
				if (test > best) {
					best = test;
				}

				changed = new Card(cshare[2].getRank(), cshare[2].getSuit());
				changed.changeRank(1);
				test = he.rankHand(chole[i], chole[j], cshare[0], changed,
						cshare[4]);
				if (test > best) {
					best = test;
				}

				changed = new Card(cshare[2].getRank(), cshare[2].getSuit());
				changed.changeRank(-1);
				test = he.rankHand(chole[i], chole[j], cshare[0], changed,
						cshare[4]);
				if (test > best) {
					best = test;
				}

				changed = new Card(cshare[4].getRank(), cshare[4].getSuit());
				changed.changeRank(1);
				test = he.rankHand(chole[i], chole[j], cshare[0], cshare[2],
						changed);
				if (test > best) {
					best = test;
				}

				changed = new Card(cshare[4].getRank(), cshare[4].getSuit());
				changed.changeRank(-1);
				test = he.rankHand(chole[i], chole[j], cshare[0], cshare[2],
						changed);
				if (test > best) {
					best = test;
				}

				changed = new Card(cshare[1].getRank(), cshare[1].getSuit());
				changed.changeRank(1);
				test = he.rankHand(chole[i], chole[j], changed, cshare[3],
						cshare[4]);
				if (test > best) {
					best = test;
				}

				changed = new Card(cshare[1].getRank(), cshare[1].getSuit());
				changed.changeRank(-1);
				test = he.rankHand(chole[i], chole[j], changed, cshare[3],
						cshare[4]);
				if (test > best) {
					best = test;
				}

				changed = new Card(cshare[3].getRank(), cshare[3].getSuit());
				changed.changeRank(1);
				test = he.rankHand(chole[i], chole[j], cshare[1], changed,
						cshare[4]);
				if (test > best) {
					best = test;
				}

				changed = new Card(cshare[3].getRank(), cshare[3].getSuit());
				changed.changeRank(-1);
				test = he.rankHand(chole[i], chole[j], cshare[1], changed,
						cshare[4]);
				if (test > best) {
					best = test;
				}

				changed = new Card(cshare[4].getRank(), cshare[4].getSuit());
				changed.changeRank(1);
				test = he.rankHand(chole[i], chole[j], cshare[1], cshare[3],
						changed);
				if (test > best) {
					best = test;
				}

				changed = new Card(cshare[4].getRank(), cshare[4].getSuit());
				changed.changeRank(-1);
				test = he.rankHand(chole[i], chole[j], cshare[1], cshare[3],
						changed);
				if (test > best) {
					best = test;
				}
			}
		}

		return best;
	}
}
