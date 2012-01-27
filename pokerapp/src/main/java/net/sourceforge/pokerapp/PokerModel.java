/******************************************************************************************
 * PokerModel.java                 PokerApp                                               *
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

import java.util.*;

/****************************************************
 * PokerModel is a class that represents the pictures displayed on the screen.
 * It is used so that the PokerView can be updated.
 * 
 * @author Dan Puperi
 * @version 1.00
 * 
 **/
public class PokerModel extends Observable {

	/**
	 * List of all pictures to be displayed on the table.
	 **/
	protected List pictureList;

	/***************************
	 * The default constructor creates a PokerModel instance.
	 **/
	public PokerModel() {
		pictureList = Collections.synchronizedList(new LinkedList());
	}

	/***************************
	 * remove() removes a picture from the list and notify the view that it
	 * needs to be redisplayed
	 * 
	 * @param p
	 *            The picture to remove
	 * @return A flag indicating whether the picture had been successfully
	 *         removed
	 * 
	 **/
	public boolean remove(Picture p) {
		boolean removed = pictureList.remove(p);
		if (removed) {
			redisplay();
		}
		return removed;
	}

	/***************************
	 * remove() removes a picture from the list and notify the view that it
	 * needs to be redisplayed
	 * 
	 * @param p
	 *            The picture to remove
	 * @param r
	 *            A flag indicating whether to redisplay the view or not.
	 * @return A flag indicating whether the picture had been successfully
	 *         removed
	 * 
	 **/
	public boolean remove(Picture p, boolean r) {
		boolean removed = pictureList.remove(p);
		if (removed && r) {
			redisplay();
		}
		return removed;
	}

	/***************************
	 * add() adds another picture to the model and notify the view that it needs
	 * to be redisplayed
	 * 
	 * @param p
	 *            The picture to remove
	 * @return A flag indicating whether the picture had been successfully
	 *         removed
	 * 
	 **/
	public void add(Picture p) {
		pictureList.add(p);
		redisplay();
	}

	/***************************
	 * add() adds another picture to the model and notify the view that it needs
	 * to be redisplayed
	 * 
	 * @param p
	 *            The picture to remove
	 * @param r
	 *            A flag indicating whether to redisplay the view or not.
	 * 
	 **/
	public void add(Picture p, boolean r) {
		pictureList.add(p);
		if (r) {
			redisplay();
		}
	}

	/***************************
	 * Notify the view that it needs to be redisplayed
	 **/
	public void redisplay() {
		setChanged();
		notifyObservers();
	}

	/***************************
	 * Returns the picture list iterator - which is used to cycle through the
	 * pictures
	 * 
	 * @return The Iterator class from the pictureList
	 * 
	 **/
	public Iterator getIterator() {
		return pictureList.iterator();
	}
}