/******************************************************************************************
 * Deck.java                       PokerApp                                               *
 *                                                                                        *
 *   Revision History                                                                     *
 * +---------+----------+---------------------------------------------------------------+ *
 * | Version | DATE     | Description                                                   | *
 * +---------+----------+---------------------------------------------------------------+ *
 * |  0.95   | 09/15/04 | Initial documented release                                    | *
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

import java.util.Random;
import java.awt.Image;

/****************************************************
 * The deck class represents the entire deck. It contains functions to shuffle
 * the deck and deal the next card. It also contains the image on the back of
 * all the cards.
 * 
 * @author Dan Puperi
 * @version 1.00
 * 
 **/
public class Deck {

	/**
	 * Constant number of cards in a deck
	 **/
	public static final int NUM_CARDS = 52;
	/**
	 * Array of the cards
	 **/
	public Card cards[];
	/**
	 * Current location in the array
	 **/
	public int position;
	/**
	 * Image on the back of the cards.
	 **/
	public Image imgCardBack;

	private Random r; // Random number class used to shuffle.

	/***************************
	 * The default constructor creates a deck with no back of the card image.
	 **/
	public Deck() {
		cards = new Card[NUM_CARDS];
		position = 0;

		for (int i = 0; i < Card.NUM_SUITS; i++) {
			for (int j = 0; j < Card.NUM_RANKS; j++) {
				cards[position] = new Card(j + 1, i + 1);
				position++;
			}
		}
		position = 0;
		r = new Random(System.currentTimeMillis());

		imgCardBack = null;
	}

	/***************************
	 * The constructor creates a deck with a given image used as the card backs.
	 * 
	 * @param image
	 *            The Image class which is the picture on the back of the cards.
	 * 
	 **/
	public Deck(Image image) {
		cards = new Card[NUM_CARDS];
		position = 0;

		for (int i = 0; i < Card.NUM_SUITS; i++) {
			for (int j = 0; j < Card.NUM_RANKS; j++) {
				cards[position] = new Card(j + 1, i + 1);
				position++;
			}
		}
		position = 0;
		r = new Random(System.currentTimeMillis());

		imgCardBack = image;
	}

	/***************************
	 * shuffle() Shuffles the deck
	 **/
	public void shuffle() {
		Card tempCard = new Card();
		for (int i = 0; i < NUM_CARDS; i++) {
			int j = i + r.nextInt(NUM_CARDS - i);
			tempCard = cards[j];
			cards[j] = cards[i];
			cards[i] = tempCard;
		}
		position = 0;
	}

	/***************************
	 * deal() returns the card at the top of the deck
	 * 
	 * @return The Card a the top of the deck.
	 * 
	 **/
	public Card deal() {
		if (position < NUM_CARDS) {
			position++;
			return cards[position - 1];
		} else {
			return null;
		}
	}
}