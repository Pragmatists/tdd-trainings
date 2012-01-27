/******************************************************************************************
 * HandEvaluator.java              PokerApp                                               *
 *                                                                                        *
 *   Revision History                                                                     *
 * +---------+----------+---------------------------------------------------------------+ *
 * | Version | DATE     | Description                                                   | *
 * +---------+----------+---------------------------------------------------------------+ *
 * |  0.95   | 09/15/04 | Initial documented release                                    | *
 * |  0.96   | 10/01/04 | Additional isStraight() and isFlush() functions               | *
 * |  0.96   | 10/01/04 | Make things slightly more efficient                           | *
 * |  0.97   | 11/11/04 | Add a rankHand function that takes up to 5 cards in the hand  | *
 * |         |          | argument and then calls the appropriate card argument         | *
 * |         |          | rankHand function.                                            | * 
 * |  0.97   | 11/11/04 | Found error in 3 card rank hand function for three of a kind  | *
 * |  1.00   | 07/05/07 | Prepare for open source.  Header/comments/package/etc...      | *        
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

package net.sourceforge.pokerapp;

import java.util.*;

/****************************************************
 * The HandEvaluator is a class that is used to rank hands numerically. For
 * PokerApp, the hand ranking system goes as follows:
 * 
 * Royal Straight Flush = 150.0f Five of a kind = 135.0f - 149.99999f Straight
 * flush = 120.0f - 134.99999f Four of a kind = 105.0f - 119.99999f Full House =
 * 90.0f - 104.99999f Flush = 75.0f - 89.99999f Straight = 60.0f - 74.99999f
 * Three of a kind = 45.0f - 59.99999f Two Pair = 30.0f - 44.99999f Pair = 15.0f
 * - 29.99999f High Card = 0.0f - 14.99999f The higher the number is in the
 * range, the better of that type of hand it is. The decimal places make up the
 * kicker(s).
 * 
 * @author Dan Puperi
 * @version 1.00
 * 
 **/
public class HandEvaluator {

	/***************************
	 * The default constructor creates an instance of this class.
	 **/
	public HandEvaluator() {
	}

	/***************************
	 * rankHand() is used to return the rank of a hand. This version will take a
	 * Hand argument and use up to 5 cards in the hand to rank. It will start
	 * with the hole cards, and then the up cards and finally the shared cards.
	 * It will only take up to 5 cards - if there are more, they get ignored.
	 * 
	 * @param h
	 *            The hand to rank
	 * @return The numerical rank of the Hand
	 * 
	 **/
	public float rankHand(Hand h) {
		ArrayList cardList = new ArrayList(5);
		int numCards = 0;
		for (int i = 0; i < h.getNumHole(); i++) {
			if (numCards < 5) {
				numCards++;
				cardList.add(h.getHoleCard(i));
			}
		}
		for (int i = 0; i < h.getNumUp(); i++) {
			if (numCards < 5) {
				numCards++;
				cardList.add(h.getUpCard(i));
			}
		}
		for (int i = 0; i < h.getNumShared(); i++) {
			if (numCards < 5) {
				numCards++;
				cardList.add(h.getSharedCard(i));
			}
		}
		if (numCards == 1) {
			return rankHand((Card) cardList.get(0));
		} else if (numCards == 2) {
			return rankHand((Card) cardList.get(0), (Card) cardList.get(1));
		} else if (numCards == 3) {
			return rankHand((Card) cardList.get(0), (Card) cardList.get(1),
					(Card) cardList.get(2));
		} else if (numCards == 4) {
			return rankHand((Card) cardList.get(0), (Card) cardList.get(1),
					(Card) cardList.get(2), (Card) cardList.get(3));
		} else if (numCards == 5) {
			return rankHand((Card) cardList.get(0), (Card) cardList.get(1),
					(Card) cardList.get(2), (Card) cardList.get(3),
					(Card) cardList.get(4));
		} else {
			return 0.0f;
		}
	}

	/***************************
	 * rankHand() is used to return the rank of a hand. This version will take a
	 * single Card and return its rank
	 * 
	 * @param c1
	 *            The Card to rank
	 * @return The numerical rank of the Card
	 * 
	 **/
	public float rankHand(Card c1) {
		float c1val = (float) c1.getRank();
		return ((c1val == 1.0f) ? 14.0f : c1val);
	}

	/***************************
	 * rankHand() is used to return the rank of a hand. This version will take a
	 * two Card hand and return its rank
	 * 
	 * @param c1
	 *            The first Card in the hand to rank
	 * @param c2
	 *            The second Card in the hand to rank
	 * @return The numerical rank of the cards
	 * 
	 **/
	public float rankHand(Card c1, Card c2) {
		float c1val = rankHand(c1);
		float c2val = rankHand(c2);
		//
		// Pair
		//
		if (c1val == c2val) {
			return 15.0f + c1val;
			//
			// High Card
			//
		} else {
			return kicker(c1, c2) * 15.0f;
		}
	}

	/***************************
	 * rankHand() is used to return the rank of a hand. This version will take a
	 * three Card hand and return its rank
	 * 
	 * @param c1
	 *            The first Card in the hand to rank
	 * @param c2
	 *            The second Card in the hand to rank
	 * @param c3
	 *            The third Card in the hand to rank
	 * @return The numerical rank of the cards
	 * 
	 **/
	public float rankHand(Card c1, Card c2, Card c3) {
		float c1val = rankHand(c1);
		float c2val = rankHand(c2);
		float c3val = rankHand(c3);
		//
		// Three of a kind
		//
		if ((c1val == c2val) && (c1val == c3val)) {
			return 45.0f + c1val;
			//
			// Pair
			//
		} else if (c1val == c2val) {
			return 15.0f + c1val + kicker(c3);
		} else if (c2val == c3val) {
			return 15.0f + c2val + kicker(c1);
		} else if (c1val == c3val) {
			return 15.0f + c1val + kicker(c2);
			//
			// High Card
			//
		} else {
			return kicker(c1, c2, c3) * 15.0f;
		}
	}

	/***************************
	 * rankHand() is used to return the rank of a hand. This version will take a
	 * four Card hand and return its rank
	 * 
	 * @param c1
	 *            The first Card in the hand to rank
	 * @param c2
	 *            The second Card in the hand to rank
	 * @param c3
	 *            The third Card in the hand to rank
	 * @param c4
	 *            The fourth Card in the hand to rank
	 * @return The numerical rank of the cards
	 * 
	 **/
	public float rankHand(Card c1, Card c2, Card c3, Card c4) {
		float c1val = rankHand(c1);
		float c2val = rankHand(c2);
		float c3val = rankHand(c3);
		float c4val = rankHand(c4);
		//
		// Four of a kind
		//
		if ((c1val == c2val) && (c1val == c3val) && (c1val == c4val)) {
			return 60.0f + c1val;
			//
			// Three of a kind
			//
		} else if ((c1val == c2val) && (c1val == c3val)) {
			return 45.0f + c1val + kicker(c4);
		} else if ((c1val == c2val) && (c1val == c4val)) {
			return 45.0f + c1val + kicker(c3);
		} else if ((c1val == c3val) && (c1val == c4val)) {
			return 45.0f + c1val + kicker(c2);
		} else if ((c2val == c3val) && (c2val == c4val)) {
			return 45.0f + c2val + kicker(c1);
			//
			// Two Pairs
			//
		} else if ((c1val == c2val) && (c3val == c4val)) {
			return 30.0f + kicker(c1, c3) * 15.0f;
		} else if ((c1val == c3val) && (c2val == c4val)) {
			return 30.0f + kicker(c1, c2) * 15.0f;
		} else if ((c1val == c4val) && (c2val == c3val)) {
			return 30.0f + kicker(c1, c2) * 15.0f;
			//
			// Pair
			//
		} else if (c1val == c2val) {
			return 15.0f + c1val + kicker(c3, c4);
		} else if (c1val == c3val) {
			return 15.0f + c1val + kicker(c2, c4);
		} else if (c1val == c4val) {
			return 15.0f + c1val + kicker(c2, c3);
		} else if (c2val == c3val) {
			return 15.0f + c2val + kicker(c1, c4);
		} else if (c2val == c4val) {
			return 15.0f + c2val + kicker(c1, c3);
		} else if (c3val == c4val) {
			return 15.0f + c3val + kicker(c1, c2);
			//
			// High card
			//
		} else {
			return kicker(c1, c2, c3, c4) * 15.0f;
		}
	}

	/***************************
	 * rankHand() is used to return the rank of a hand. This version will take a
	 * five Card hand and return its rank
	 * 
	 * @param c1
	 *            The first Card in the hand to rank
	 * @param c2
	 *            The second Card in the hand to rank
	 * @param c3
	 *            The third Card in the hand to rank
	 * @param c4
	 *            The fourth Card in the hand to rank
	 * @param c5
	 *            The fifth Card in the hand to rank
	 * @return The numerical rank of the cards
	 * 
	 **/
	public float rankHand(Card c1, Card c2, Card c3, Card c4, Card c5) {
		float c1val = rankHand(c1);
		float c2val = rankHand(c2);
		float c3val = rankHand(c3);
		float c4val = rankHand(c4);
		float c5val = rankHand(c5);
		float kicker = 0.0f;
		float straight = isStraight(c1, c2, c3, c4, c5);
		//
		// Royal Straight Flush
		//
		if ((straight == 14.0f) && isFlush(c1, c2, c3, c4, c5)) {
			return 150.0f;
			//
			// Five of a Kind
			//
		} else if ((c1val == c2val) && (c1val == c3val) && (c1val == c4val)
				&& (c1val == c5val)) {
			return 135.0f + c1val;
			//
			// Straight Flush
			//
		} else if ((straight > 0.0f) && isFlush(c1, c2, c3, c4, c5)) {
			return 120.0f + straight;
			//
			// Four of a kind
			//
		} else if ((c1val == c2val) && (c1val == c3val) && (c1val == c4val)) {
			return 105.0f + c1val + kicker(c5);
		} else if ((c1val == c2val) && (c1val == c3val) && (c1val == c5val)) {
			return 105.0f + c1val + kicker(c4);
		} else if ((c1val == c2val) && (c1val == c4val) && (c1val == c5val)) {
			return 105.0f + c1val + kicker(c3);
		} else if ((c1val == c3val) && (c1val == c4val) && (c1val == c5val)) {
			return 105.0f + c1val + kicker(c2);
		} else if ((c2val == c3val) && (c2val == c4val) && (c2val == c5val)) {
			return 105.0f + c2val + kicker(c1);
			//
			// Full House
			//
		} else if ((c1val == c2val) && (c1val == c3val) && (c4val == c5val)) {
			return 90.0f + c1val + kicker(c4);
		} else if ((c1val == c2val) && (c1val == c4val) && (c3val == c5val)) {
			return 90.0f + c1val + kicker(c3);
		} else if ((c1val == c2val) && (c1val == c5val) && (c3val == c4val)) {
			return 90.0f + c1val + kicker(c3);
		} else if ((c1val == c3val) && (c1val == c4val) && (c2val == c5val)) {
			return 90.0f + c1val + kicker(c2);
		} else if ((c1val == c3val) && (c1val == c5val) && (c2val == c4val)) {
			return 90.0f + c1val + kicker(c2);
		} else if ((c1val == c4val) && (c1val == c5val) && (c2val == c3val)) {
			return 90.0f + c1val + kicker(c2);
		} else if ((c2val == c3val) && (c2val == c4val) && (c1val == c5val)) {
			return 90.0f + c2val + kicker(c1);
		} else if ((c2val == c3val) && (c2val == c5val) && (c1val == c4val)) {
			return 90.0f + c2val + kicker(c1);
		} else if ((c2val == c4val) && (c2val == c5val) && (c1val == c3val)) {
			return 90.0f + c2val + kicker(c1);
		} else if ((c3val == c4val) && (c3val == c5val) && (c1val == c2val)) {
			return 90.0f + c3val + kicker(c1);
			//
			// Flush
			//
		} else if (isFlush(c1, c2, c3, c4, c5)) {
			return 75.0f + kicker(c1, c2, c3, c4, c5) * 15.0f;
			//
			// Straight
			//
		} else if (straight > 0.0f) {
			return 60.0f + straight;
			//
			// Three of a kind
			//
		} else if ((c1val == c2val) && (c1val == c3val)) {
			return 45.0f + c1val + kicker(c4, c5);
		} else if ((c1val == c2val) && (c1val == c4val)) {
			return 45.0f + c1val + kicker(c3, c5);
		} else if ((c1val == c2val) && (c1val == c5val)) {
			return 45.0f + c1val + kicker(c3, c4);
		} else if ((c1val == c3val) && (c1val == c4val)) {
			return 45.0f + c1val + kicker(c2, c5);
		} else if ((c1val == c3val) && (c1val == c5val)) {
			return 45.0f + c1val + kicker(c2, c4);
		} else if ((c1val == c4val) && (c1val == c5val)) {
			return 45.0f + c1val + kicker(c2, c3);
		} else if ((c2val == c3val) && (c2val == c4val)) {
			return 45.0f + c2val + kicker(c1, c5);
		} else if ((c2val == c3val) && (c2val == c5val)) {
			return 45.0f + c2val + kicker(c1, c4);
		} else if ((c2val == c4val) && (c2val == c5val)) {
			return 45.0f + c2val + kicker(c1, c3);
		} else if ((c3val == c4val) && (c3val == c5val)) {
			return 45.0f + c3val + kicker(c1, c2);
			//
			// Two Pairs
			//
		} else if ((c1val == c2val) && (c3val == c4val)) {
			return 30.0f + kicker(c1, c3) * 15.0f + kicker(c5) / 15.0f;
		} else if ((c1val == c2val) && (c3val == c5val)) {
			return 30.0f + kicker(c1, c3) * 15.0f + kicker(c4) / 15.0f;
		} else if ((c1val == c2val) && (c4val == c5val)) {
			return 30.0f + kicker(c1, c4) * 15.0f + kicker(c3) / 15.0f;
		} else if ((c1val == c3val) && (c2val == c4val)) {
			return 30.0f + kicker(c1, c2) * 15.0f + kicker(c5) / 15.0f;
		} else if ((c1val == c3val) && (c2val == c5val)) {
			return 30.0f + kicker(c1, c2) * 15.0f + kicker(c4) / 15.0f;
		} else if ((c1val == c3val) && (c4val == c5val)) {
			return 30.0f + kicker(c1, c4) * 15.0f + kicker(c2) / 15.0f;
		} else if ((c1val == c4val) && (c2val == c3val)) {
			return 30.0f + kicker(c1, c2) * 15.0f + kicker(c5) / 15.0f;
		} else if ((c1val == c4val) && (c2val == c5val)) {
			return 30.0f + kicker(c1, c2) * 15.0f + kicker(c3) / 15.0f;
		} else if ((c1val == c4val) && (c3val == c5val)) {
			return 30.0f + kicker(c1, c3) * 15.0f + kicker(c2) / 15.0f;
		} else if ((c1val == c5val) && (c2val == c3val)) {
			return 30.0f + kicker(c1, c2) * 15.0f + kicker(c4) / 15.0f;
		} else if ((c1val == c5val) && (c2val == c4val)) {
			return 30.0f + kicker(c1, c2) * 15.0f + kicker(c3) / 15.0f;
		} else if ((c1val == c5val) && (c3val == c4val)) {
			return 30.0f + kicker(c1, c3) * 15.0f + kicker(c2) / 15.0f;
		} else if ((c2val == c3val) && (c4val == c5val)) {
			return 30.0f + kicker(c2, c4) * 15.0f + kicker(c1) / 15.0f;
		} else if ((c2val == c4val) && (c3val == c5val)) {
			return 30.0f + kicker(c2, c3) * 15.0f + kicker(c1) / 15.0f;
		} else if ((c2val == c5val) && (c3val == c4val)) {
			return 30.0f + kicker(c2, c3) * 15.0f + kicker(c1) / 15.0f;
			//
			// Pair
			//
		} else if (c1val == c2val) {
			return 15.0f + c1val + kicker(c3, c4, c5);
		} else if (c1val == c3val) {
			return 15.0f + c1val + kicker(c2, c4, c5);
		} else if (c1val == c4val) {
			return 15.0f + c1val + kicker(c2, c3, c5);
		} else if (c1val == c5val) {
			return 15.0f + c1val + kicker(c2, c3, c4);
		} else if (c2val == c3val) {
			return 15.0f + c2val + kicker(c1, c4, c5);
		} else if (c2val == c4val) {
			return 15.0f + c2val + kicker(c1, c3, c5);
		} else if (c2val == c5val) {
			return 15.0f + c2val + kicker(c1, c3, c4);
		} else if (c3val == c4val) {
			return 15.0f + c3val + kicker(c1, c2, c5);
		} else if (c3val == c5val) {
			return 15.0f + c3val + kicker(c1, c2, c4);
		} else if (c4val == c5val) {
			return 15.0f + c4val + kicker(c1, c2, c3);
			//
			// High Card
			//
		} else {
			return kicker(c1, c2, c3, c4, c5) * 15.0f;
		}
	}

	/***************************
	 * kicker() is used to return the rank of the kicker part of the hand. This
	 * version will take a single card kicker and return its rank
	 * 
	 * @param c1
	 *            The first Card in the kicker
	 * @return The numerical rank of the kicker cards
	 * 
	 **/
	public float kicker(Card c1) {
		int c1val = (int) rankHand(c1);
		return (float) c1val / 15.0f;
	}

	/***************************
	 * kicker() is used to return the rank of the kicker part of the hand. This
	 * version will take a two card kicker and return its rank
	 * 
	 * @param c1
	 *            The first Card in the kicker
	 * @param c2
	 *            The second Card in the kicker
	 * @return The numerical rank of the kicker cards
	 * 
	 **/
	public float kicker(Card c1, Card c2) {
		int c1val = (int) rankHand(c1);
		int c2val = (int) rankHand(c2);
		return ((c1val > c2val) ? ((float) c1val + kicker(c2)) / 15.0f
				: ((float) c2val + kicker(c1)) / 15.0f);
	}

	/***************************
	 * kicker() is used to return the rank of the kicker part of the hand. This
	 * version will take a three card kicker and return its rank
	 * 
	 * @param c1
	 *            The first Card in the kicker
	 * @param c2
	 *            The second Card in the kicker
	 * @param c3
	 *            The third Card in the kicker
	 * @return The numerical rank of the kicker cards
	 * 
	 **/
	public float kicker(Card c1, Card c2, Card c3) {
		ArrayList cardList = new ArrayList(3);
		cardList.add(c1);
		cardList.add(c2);
		cardList.add(c3);
		Collections.sort(cardList);

		float c3val = rankHand((Card) cardList.get(2));
		return (c3val + kicker((Card) cardList.get(0), (Card) cardList.get(1))) / 15.0f;
	}

	/***************************
	 * kicker() is used to return the rank of the kicker part of the hand. This
	 * version will take a four card kicker and return its rank. Used for high
	 * card winning hand.
	 * 
	 * @param c1
	 *            The first Card in the kicker
	 * @param c2
	 *            The second Card in the kicker
	 * @param c3
	 *            The third Card in the kicker
	 * @param c4
	 *            The fourth Card in the kicker
	 * @return The numerical rank of the kicker cards
	 * 
	 **/
	public float kicker(Card c1, Card c2, Card c3, Card c4) {
		ArrayList cardList = new ArrayList(4);
		cardList.add(c1);
		cardList.add(c2);
		cardList.add(c3);
		cardList.add(c4);
		Collections.sort(cardList);

		float c4val = rankHand((Card) cardList.get(3));
		return (c4val + kicker((Card) cardList.get(0), (Card) cardList.get(1),
				(Card) cardList.get(2))) / 15.0f;
	}

	/***************************
	 * kicker() is used to return the rank of the kicker part of the hand. This
	 * version will take a five card kicker and return its rank. Used for high
	 * card winning hand
	 * 
	 * @param c1
	 *            The first Card in the kicker
	 * @param c2
	 *            The second Card in the kicker
	 * @param c3
	 *            The third Card in the kicker
	 * @param c4
	 *            The fourth Card in the kicker
	 * @param c5
	 *            The fifth Card in the kicker
	 * @return The numerical rank of the kicker cards
	 * 
	 **/
	public float kicker(Card c1, Card c2, Card c3, Card c4, Card c5) {
		ArrayList cardList = new ArrayList(5);
		cardList.add(c1);
		cardList.add(c2);
		cardList.add(c3);
		cardList.add(c4);
		cardList.add(c5);
		Collections.sort(cardList);

		float c5val = rankHand((Card) cardList.get(4));
		return (c5val + kicker((Card) cardList.get(0), (Card) cardList.get(1),
				(Card) cardList.get(2), (Card) cardList.get(3))) / 15.0f;
	}

	/***************************
	 * isFlush() is used to determine if the hand is (or can be) a flush. This
	 * version takes three Cards as arguments and returns true if all are the
	 * same suit.
	 * 
	 * @param c1
	 *            The first Card
	 * @param c2
	 *            The second Card
	 * @param c3
	 *            The third Card
	 * @return True if this hand is all the same suit. False otherwise.
	 * 
	 **/
	public boolean isFlush(Card c1, Card c2, Card c3) {
		return (((c1.getSuit() == c2.getSuit()) && (c1.getSuit() == c3
				.getSuit())) ? true : false);
	}

	/***************************
	 * isFlush() is used to determine if the hand is (or can be) a flush. This
	 * version takes four Cards as arguments and returns true if all are the
	 * same suit.
	 * 
	 * @param c1
	 *            The first Card
	 * @param c2
	 *            The second Card
	 * @param c3
	 *            The third Card
	 * @param c4
	 *            The fourth Card
	 * @return True if this hand is all the same suit. False otherwise.
	 * 
	 **/
	public boolean isFlush(Card c1, Card c2, Card c3, Card c4) {
		return (((c1.getSuit() == c2.getSuit())
				&& (c1.getSuit() == c3.getSuit()) && (c1.getSuit() == c4
				.getSuit())) ? true : false);
	}

	/***************************
	 * isFlush() is used to determine if the hand is (or can be) a flush. This
	 * version takes five Cards as arguments and returns true if all are the
	 * same suit.
	 * 
	 * @param c1
	 *            The first Card
	 * @param c2
	 *            The second Card
	 * @param c3
	 *            The third Card
	 * @param c4
	 *            The fourth Card
	 * @param c5
	 *            The fifth Card
	 * @return True if this hand is all the same suit. False otherwise.
	 * 
	 **/
	public boolean isFlush(Card c1, Card c2, Card c3, Card c4, Card c5) {
		return (((c1.getSuit() == c2.getSuit())
				&& (c1.getSuit() == c3.getSuit())
				&& (c1.getSuit() == c4.getSuit()) && (c1.getSuit() == c5
				.getSuit())) ? true : false);
	}

	/***************************
	 * isStraight() is used to determine if the hand is (or can be) a straight.
	 * This version takes two Cards as arguments and determines if they can be
	 * used as part of a straight.
	 * 
	 * @param c1
	 *            The first Card
	 * @param c2
	 *            The second Card
	 * @return The value of the highest possible straight the cards can make.
	 *         -1.0f if a straight cannot be made.
	 * 
	 **/
	public float isStraight(Card c1, Card c2) {
		ArrayList cardList = new ArrayList(2);
		cardList.add(c1);
		cardList.add(c2);
		Collections.sort(cardList);

		int c1val = ((Card) cardList.get(0)).getRank();
		int c2val = ((Card) cardList.get(1)).getRank();
		//
		// Straight if no pairs and the max-min <= 4
		//
		boolean nopairs = true;
		if (c1val == c2val)
			nopairs = false;
		if (nopairs && ((c2val - c1val) <= 4)) {
			return (((c2val - c1val) == 4) ? rankHand((Card) cardList.get(1))
					: Math.min((float) c1val + 4.0f, 14.0f));
		}
		//
		// Special cases - Ace High and Low
		//
		if (c1val == Card.ACE) {
			if (nopairs && (c1val <= Card.FIVE))
				return 5.0f;
			if (nopairs && (c1val >= Card.TEN))
				return 14.0f;
		}
		//
		// No straight
		//
		return -1.0f;
	}

	/***************************
	 * isStraight() is used to determine if the hand is (or can be) a straight.
	 * This version takes three Cards as arguments and determines if they can be
	 * used as part of a straight.
	 * 
	 * @param c1
	 *            The first Card
	 * @param c2
	 *            The second Card
	 * @param c3
	 *            The third Card
	 * @return The value of the highest possible straight the cards can make.
	 *         -1.0f if a straight cannot be made.
	 * 
	 **/
	public float isStraight(Card c1, Card c2, Card c3) {
		ArrayList cardList = new ArrayList(3);
		cardList.add(c1);
		cardList.add(c2);
		cardList.add(c3);
		Collections.sort(cardList);

		int c1val = ((Card) cardList.get(0)).getRank();
		int c2val = ((Card) cardList.get(1)).getRank();
		int c3val = ((Card) cardList.get(2)).getRank();
		//
		// Straight if no pairs and the max-min <= 4
		//
		boolean nopairs = true;
		if ((c1val == c2val) || (c1val == c3val))
			nopairs = false;
		if (c2val == c3val)
			nopairs = false;
		if (nopairs && ((c3val - c1val) <= 4)) {
			return (((c3val - c1val) == 4) ? rankHand((Card) cardList.get(2))
					: Math.min((float) c1val + 4.0f, 14.0f));
		}
		//
		// Special cases - Ace High and Low
		//
		if (c3val == Card.ACE) {
			if (nopairs && (c2val <= Card.FIVE))
				return 5.0f;
			if (nopairs && (c1val >= Card.TEN))
				return 14.0f;
		}
		//
		// No straight
		//
		return -1.0f;
	}

	/***************************
	 * isStraight() is used to determine if the hand is (or can be) a straight.
	 * This version takes four Cards as arguments and determines if they can be
	 * used as part of a straight.
	 * 
	 * @param c1
	 *            The first Card
	 * @param c2
	 *            The second Card
	 * @param c3
	 *            The third Card
	 * @param c4
	 *            The fourth Card
	 * @return The value of the highest possible straight the cards can make.
	 *         -1.0f if a straight cannot be made.
	 * 
	 **/
	public float isStraight(Card c1, Card c2, Card c3, Card c4) {
		ArrayList cardList = new ArrayList(4);
		cardList.add(c1);
		cardList.add(c2);
		cardList.add(c3);
		cardList.add(c4);
		Collections.sort(cardList);

		int c1val = ((Card) cardList.get(0)).getRank();
		int c2val = ((Card) cardList.get(1)).getRank();
		int c3val = ((Card) cardList.get(2)).getRank();
		int c4val = ((Card) cardList.get(3)).getRank();
		//
		// Straight if no pairs and the max-min <= 4
		//
		boolean nopairs = true;
		if ((c1val == c2val) || (c1val == c3val) || (c1val == c4val))
			nopairs = false;
		if ((c2val == c3val) || (c2val == c4val))
			nopairs = false;
		if (c3val == c4val)
			nopairs = false;
		if (nopairs && ((c4val - c1val) <= 4)) {
			return (((c4val - c1val) == 4) ? rankHand((Card) cardList.get(3))
					: Math.min((float) c1val + 4.0f, 14.0f));
		}
		//
		// Special cases - Ace High and Low
		//
		if (c4val == Card.ACE) {
			if (nopairs && (c3val <= Card.FIVE))
				return 5.0f;
			if (nopairs && (c1val >= Card.TEN))
				return 14.0f;
		}
		//
		// No straight
		//
		return -1.0f;
	}

	/***************************
	 * isStraight() is used to determine if the hand is (or can be) a straight.
	 * This version takes five Cards as arguments and determines if they can be
	 * used as part of a straight.
	 * 
	 * @param c1
	 *            The first Card
	 * @param c2
	 *            The second Card
	 * @param c3
	 *            The third Card
	 * @param c4
	 *            The fourth Card
	 * @param c5
	 *            The fifth Card
	 * @return The value of the highest possible straight the cards can make.
	 *         -1.0f if a straight cannot be made.
	 * 
	 **/
	public float isStraight(Card c1, Card c2, Card c3, Card c4, Card c5) {
		ArrayList cardList = new ArrayList(5);
		cardList.add(c1);
		cardList.add(c2);
		cardList.add(c3);
		cardList.add(c4);
		cardList.add(c5);
		Collections.sort(cardList);

		int c1val = ((Card) cardList.get(0)).getRank();
		int c2val = ((Card) cardList.get(1)).getRank();
		int c3val = ((Card) cardList.get(2)).getRank();
		int c4val = ((Card) cardList.get(3)).getRank();
		int c5val = ((Card) cardList.get(4)).getRank();
		//
		// Straight if no pairs and the max-min = 4
		//
		boolean nopairs = true;
		if ((c1val == c2val) || (c1val == c3val) || (c1val == c4val)
				|| (c1val == c5val))
			nopairs = false;
		if ((c2val == c3val) || (c2val == c4val) || (c2val == c5val))
			nopairs = false;
		if ((c3val == c4val) || (c3val == c5val))
			nopairs = false;
		if (c4val == c5val)
			nopairs = false;
		if (nopairs && ((c5val - c1val) == 4))
			return rankHand((Card) cardList.get(4));
		//
		// Special cases - Ace High and Low
		//
		if (c5val == Card.ACE) {
			if (nopairs && (c4val == Card.FIVE) && (c1val == Card.TWO))
				return 5.0f;
			if (nopairs && (c1val == Card.TEN))
				return 14.0f;
		}
		//
		// Otherwise no straight
		//
		return -1.0f;
	}

	/***************************
	 * nameHand() names a hand depending on its rank value.
	 * 
	 * @param r
	 *            The floating point value of the hand.
	 * @return The String description of this hand.
	 * 
	 **/
	public static String nameHand(float r) {
		if (r >= 150.0f) {
			return "Royal Straight Flush";
		} else if (r >= 135.0f) {
			return "Five of a Kind : " + nameCard(r - 135.0f) + "s";
		} else if (r >= 120.0f) {
			return "Straight Flush : " + nameCard(r - 120.0f) + " high";
		} else if (r >= 105.0f) {
			return "Four of a Kind : " + nameCard(r - 105.0f) + "s";
		} else if (r >= 90.0f) {
			float k = (float) Math.floor(r);
			return "Full House : " + nameCard(r - 90.0f) + "s over "
					+ nameCard((r - k) * 15.0f + 0.5f) + "s";
		} else if (r >= 75.0f) {
			return "Flush : " + nameCard(r - 75.0f) + " high";
		} else if (r >= 60.0f) {
			return "Straight : " + nameCard(r - 59.5f) + " high";
		} else if (r >= 45.0f) {
			return "Three of a kind : " + nameCard(r - 45.0f) + "s";
		} else if (r >= 30.0f) {
			float k = (float) Math.floor(r);
			return "Two Pair : " + nameCard(r - 30.0f) + "s and "
					+ nameCard((r - k) * 15.0f) + "s";
		} else if (r >= 15.0f) {
			return "Pair : " + nameCard(r - 15.0f) + "s";
		} else {
			return "High Card : " + nameCard(r);
		}
	}

	/***************************
	 * nameCard() names a card depending on its rank value.
	 * 
	 * @param r
	 *            The floating point value of the card.
	 * @return The String description of this card.
	 * 
	 **/
	public static String nameCard(float r) {
		if (r >= 14.0f)
			return "Ace";
		if (r >= 13.0f)
			return "King";
		if (r >= 12.0f)
			return "Queen";
		if (r >= 11.0f)
			return "Jack";
		if (r >= 10.0f)
			return "Ten";
		if (r >= 9.0f)
			return "Nine";
		if (r >= 8.0f)
			return "Eight";
		if (r >= 7.0f)
			return "Seven";
		if (r >= 6.0f)
			return "Six";
		if (r >= 5.0f)
			return "Five";
		if (r >= 4.0f)
			return "Four";
		if (r >= 3.0f)
			return "Three";
		return "Two";
	}
}