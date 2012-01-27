/******************************************************************************************
 * Card.java                       PokerApp                                               *
 *                                                                                        *
 *   Revision History                                                                     *
 * +---------+----------+---------------------------------------------------------------+ *
 * | Version | DATE     | Description                                                   | *
 * +---------+----------+---------------------------------------------------------------+ *
 * |  0.95   | 09/15/04 | Initial documented release                                    | *
 * |  0.96   | 10/01/04 | Make this class a Comparable class                            | *
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

import java.awt.Image;

/****************************************************
 * The Card class simply represents a playing card - including its image and
 * value.
 * 
 * @author Dan Puperi
 * @version 1.00
 * 
 **/
public class Card implements Comparable {

	/**
	 * The picture of the card
	 **/
	public Image img;
	/**
	 * Card class constants
	 **/
	public final static int BAD_CARD = -1;
	public final static int CLUBS = 1;
	public final static int DIAMONDS = 2;
	public final static int HEARTS = 3;
	public final static int SPADES = 4;
	public final static int ACE = 1;
	public final static int TWO = 2;
	public final static int THREE = 3;
	public final static int FOUR = 4;
	public final static int FIVE = 5;
	public final static int SIX = 6;
	public final static int SEVEN = 7;
	public final static int EIGHT = 8;
	public final static int NINE = 9;
	public final static int TEN = 10;
	public final static int JACK = 11;
	public final static int QUEEN = 12;
	public final static int KING = 13;
	public final static int NUM_SUITS = 4;
	public final static int NUM_RANKS = 13;
	public final static int NUM_CARDS = 52;

	private int rank; // Numerical rank of the card
	private int suit; // Numerical suit of the card

	/***************************
	 * The default constructor creates an empty card with no value or image.
	 **/
	public Card() {
		suit = BAD_CARD;
		rank = BAD_CARD;
		img = null;
	}

	/***************************
	 * A Card class can be created by specifying the rank and suit of the card
	 * as integers. If the Card is given bad arguments, it will still create a
	 * Card instance, but the Card will not be usuable.
	 * 
	 * @param r
	 *            The rank value of the Card. Ace through King = 1 through 13
	 * @param s
	 *            The suit of the Card. Clubs = 1, Diamonds = 2, Hearts = 3,
	 *            Spades = 4
	 * 
	 **/
	public Card(int r, int s) {
		if ((r > 0) && (r <= NUM_RANKS)) {
			rank = r;
		} else {
			rank = BAD_CARD;
		}

		if ((s > 0) && (s <= NUM_SUITS)) {
			suit = s;
		} else {
			suit = BAD_CARD;
		}
		loadImage();
	}

	/***************************
	 * A Card class can be created by specifying the string description of the
	 * Card. If the Card is given a bad description, it will still create a Card
	 * instance, but the Card will not be usuable.
	 * 
	 * @param desc
	 *            The string description. "Rank of Suit" format with the rank
	 *            and suit capitalized.
	 * 
	 **/
	public Card(String desc) {
		int r, s;
		int i = desc.indexOf(" of");

		String rs = desc.substring(0, i);
		String ss = desc.substring(i + 4, desc.length());
		if (rs.equals("Ace")) {
			r = ACE;
		} else if (rs.equals("Two")) {
			r = TWO;
		} else if (rs.equals("Three")) {
			r = THREE;
		} else if (rs.equals("Four")) {
			r = FOUR;
		} else if (rs.equals("Five")) {
			r = FIVE;
		} else if (rs.equals("Six")) {
			r = SIX;
		} else if (rs.equals("Seven")) {
			r = SEVEN;
		} else if (rs.equals("Eight")) {
			r = EIGHT;
		} else if (rs.equals("Nine")) {
			r = NINE;
		} else if (rs.equals("Ten")) {
			r = TEN;
		} else if (rs.equals("Jack")) {
			r = JACK;
		} else if (rs.equals("Queen")) {
			r = QUEEN;
		} else if (rs.equals("King")) {
			r = KING;
		} else {
			r = BAD_CARD;
		}

		if (ss.equals("Clubs")) {
			s = CLUBS;
		} else if (ss.equals("Diamonds")) {
			s = DIAMONDS;
		} else if (ss.equals("Hearts")) {
			s = HEARTS;
		} else if (ss.equals("Spades")) {
			s = SPADES;
		} else {
			s = BAD_CARD;
		}

		rank = r;
		suit = s;
		loadImage();
	}

	/***************************
	 * loadImage() is used to load the card image into the img variable.
	 **/
	private void loadImage() {
		ClassLoader cl = getClass().getClassLoader();
		java.net.URL url = cl.getResource("Images/card-" + (rank - 1) + "-"
				+ (suit - 1) + ".gif");
		img = java.awt.Toolkit.getDefaultToolkit().getImage(url);
	}

	/***************************
	 * setCard() is used to set the Card to the given rank and suit.
	 * 
	 * @param r
	 *            The rank value of the Card. Ace through King = 1 through 13
	 * @param s
	 *            The suit of the Card. Clubs = 1, Diamonds = 2, Hearts = 3,
	 *            Spades = 4
	 * 
	 **/
	public void setCard(int r, int s) {
		if ((r > 0) && (r <= NUM_RANKS)) {
			rank = r;
		} else {
			rank = BAD_CARD;
		}

		if ((s > 0) && (s <= NUM_SUITS)) {
			suit = s;
		} else {
			suit = BAD_CARD;
		}
	}

	/***************************
	 * getRank() is used to get the (private) rank of this Card.
	 * 
	 * @return The integer rank of this Card
	 * 
	 **/
	public int getRank() {
		return rank;
	}

	/***************************
	 * getSuit() is used to get the (private) suit of this Card.
	 * 
	 * @return The integer suit of this Card
	 * 
	 **/
	public int getSuit() {
		return suit;
	}

	/***************************
	 * toString() overrides the default Object toString function and returns a
	 * string representation of the card in readable form.
	 * 
	 * @return The String description of this Card.
	 * 
	 **/
	public String toString() {
		String s;

		switch (getRank()) {
		case ACE: {
			s = "Ace of ";
			break;
		}
		case TWO: {
			s = "Two of ";
			break;
		}
		case THREE: {
			s = "Three of ";
			break;
		}
		case FOUR: {
			s = "Four of ";
			break;
		}
		case FIVE: {
			s = "Five of ";
			break;
		}
		case SIX: {
			s = "Six of ";
			break;
		}
		case SEVEN: {
			s = "Seven of ";
			break;
		}
		case EIGHT: {
			s = "Eight of ";
			break;
		}
		case NINE: {
			s = "Nine of ";
			break;
		}
		case TEN: {
			s = "Ten of ";
			break;
		}
		case JACK: {
			s = "Jack of ";
			break;
		}
		case QUEEN: {
			s = "Queen of ";
			break;
		}
		case KING: {
			s = "King of ";
			break;
		}
		case BAD_CARD: {
			s = "This is not a valid card - bad rank";
			break;
		}
		default: {
			s = "Can't figure out what this card is - wrong rank";
			break;
		}
		}

		switch (getSuit()) {
		case CLUBS: {
			s += "Clubs";
			break;
		}
		case DIAMONDS: {
			s += "Diamonds";
			break;
		}
		case HEARTS: {
			s += "Hearts";
			break;
		}
		case SPADES: {
			s += "Spades";
			break;
		}
		case BAD_CARD: {
			s = "This is not a valid card - bad suit";
			break;
		}
		default: {
			s = "Can't figure out what this card is - wrong suit";
			break;
		}
		}
		return s;
	}

	/***************************
	 * changeRank() is used for A-Mod games where the rank can be increased or
	 * decreased by 1.
	 * 
	 * @param dir
	 *            The direction of the change. A positive 1 means increase the
	 *            rank of the Card by 1.
	 * @return The new rank of the card. (note that this Card instance is also
	 *         changed.
	 * 
	 **/
	public int changeRank(int dir) {
		if (dir == 1) {
			rank = rank + 1;
			if (rank > KING)
				rank = ACE;
		} else {
			rank = rank - 1;
			if (rank < ACE)
				rank = KING;
		}
		return rank;
	}

	/***************************
	 * compareTo() function implements the Comparable part of this class.
	 * 
	 * @param o
	 *            The object which to compare this Card value. Must be a Card
	 *            object.
	 * @return A negative integer if this object is less than the Card argument;
	 *         zero if this Card is equal to the Card argument; a positive
	 *         number if this Card is greater than the Card argument.
	 * 
	 **/
	public int compareTo(Object o) {
		Card c = (Card) o;
		if (rank == c.getRank())
			return 0;
		if (rank == Card.ACE)
			return 1;
		if (c.getRank() == Card.ACE)
			return -1;
		return ((rank > c.getRank()) ? 1 : -1);
	}
}