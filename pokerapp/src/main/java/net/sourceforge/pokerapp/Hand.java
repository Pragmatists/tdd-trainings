/******************************************************************************************
 * Hand.java                       PokerApp                                               *
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

import java.util.ArrayList;

/****************************************************
 * Hand is the class that describes the cards in a player's hand.
 * 
 * @author Dan Puperi
 * @version 1.00
 * 
 **/
public class Hand {

	private ArrayList cardsHole; // List of cards held in player's hole (down)
	private ArrayList cardsShared; // List of shared cards that player can use
	private ArrayList cardsUp; // List of up cards in players hand (that others
								// can see)

	/***************************
	 * The default constructor creates and empty hand
	 **/
	public Hand() {
		clearHand();
	}

	/***************************
	 * clearHand() clears out the hand of all cards
	 **/
	public void clearHand() {
		cardsHole = new ArrayList();
		cardsShared = new ArrayList();
		cardsUp = new ArrayList();
	}

	/***************************
	 * addHoleCard() gives the Hand another hole card
	 * 
	 * @param c
	 *            The Card that will be added to this hand's hole
	 * 
	 **/
	public void addHoleCard(Card c) {
		cardsHole.add(c);
	}

	/***************************
	 * addSharedCard() gives the Hand another shared card
	 * 
	 * @param c
	 *            The Card that will be added to this hand's shared list
	 * 
	 **/
	public void addSharedCard(Card c) {
		cardsShared.add(c);
	}

	/***************************
	 * addUpCard() gives the Hand another up card
	 * 
	 * @param c
	 *            The Card that will be added to this hand's up list
	 * 
	 **/
	public void addUpCard(Card c) {
		cardsUp.add(c);
	}

	/***************************
	 * getNumHole() returns the number of hole cards in this hand
	 * 
	 * @return The number of hole cards in this hand.
	 * 
	 **/
	public int getNumHole() {
		return cardsHole.size();
	}

	/***************************
	 * getNumUp() returns the number of up cards in this hand
	 * 
	 * @return The number of up cards in this hand.
	 * 
	 **/
	public int getNumUp() {
		return cardsUp.size();
	}

	/***************************
	 * getNumShared() returns the number of shared cards in this hand
	 * 
	 * @return The number of shared cards in this hand.
	 * 
	 **/
	public int getNumShared() {
		return cardsShared.size();
	}

	/***************************
	 * getHoleCard() returns the requested hole card
	 * 
	 * @param i
	 *            The index of the desired card
	 * @return The Card which is at the requested index.
	 * 
	 **/
	public Card getHoleCard(int i) {
		return ((i < cardsHole.size()) ? (Card) cardsHole.get(i) : null);
	}

	/***************************
	 * getSharedCard() returns the requested shared card
	 * 
	 * @param i
	 *            The index of the desired card
	 * @return The Card which is at the requested index.
	 * 
	 **/
	public Card getSharedCard(int i) {
		return ((i < cardsShared.size()) ? (Card) cardsShared.get(i) : null);
	}

	/***************************
	 * getUpCard() returns the requested up card
	 * 
	 * @param i
	 *            The index of the desired card
	 * @return The Card which is at the requested index.
	 * 
	 **/
	public Card getUpCard(int i) {
		return ((i < cardsUp.size()) ? (Card) cardsUp.get(i) : null);
	}
}