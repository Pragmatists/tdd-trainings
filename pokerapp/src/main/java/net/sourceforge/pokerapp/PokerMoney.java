/******************************************************************************************
 * PokerMoney.java                 PokerApp                                               *
 *                                                                                        *
 *   Revision History                                                                     *
 * +---------+----------+---------------------------------------------------------------+ *
 * | Version | DATE     | Description                                                   | *
 * +---------+----------+---------------------------------------------------------------+ *
 * |  0.95   | 09/15/04 | Initial documented release                                    | *
 * |  1.00   | 06/06/05 | Make this class a comparable class                            | *
 * |  1.00   | 07/11/07 | Prepare for open source.  Header/comments/package/etc...      | *
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

/****************************************************
 * PokerMoney is a class to represent a float value as a $ amount. Used for ease
 * of displaying a monetary amount.
 * 
 * @author Dan Puperi
 * @version 1.00
 * 
 **/
public class PokerMoney implements Comparable {

	private float value; // The float value represented by this class

	/***************************
	 * The default constructor creates a PokerMoney class with a value of $0.00
	 **/
	public PokerMoney() {
		value = 0.0f;
	}

	/***************************
	 * This constructor creates a PokerMoney class with a value equal to the
	 * float argument
	 * 
	 * @param f
	 *            The float value that this class should represent
	 * 
	 **/
	public PokerMoney(float f) {
		value = f;
	}

	/***********************
	 * amount() is used to access the float value represented by this class
	 * 
	 * @return The float amount represented by this class
	 * 
	 **/
	public float amount() {
		return value;
	}

	/***************************
	 * add() is used to add the given value to this object
	 * 
	 * @param a
	 *            The amount to add
	 * 
	 **/
	public void add(float a) {
		value = value + a;
		nearestCent();
	}

	/***************************
	 * subtract() is used to subtract the given value from this object
	 * 
	 * @param s
	 *            The amount to subtract
	 * 
	 **/
	public void subtract(float s) {
		value = value - s;
		nearestCent();
	}

	/***************************
	 * The compareTo() function implements the Comparable part of this class.
	 * Returns a negative integer if this PokerMoney is less than the PokerMoney
	 * argument. Returns zero if this PokerMoney is equal to the PokerMoney
	 * argument Returns a positive number if this PokerMoney is greater than the
	 * PokerMoney argument.
	 * 
	 * @param o
	 *            The PokerMoney object to which this class is to be compared.
	 * @return -1 if this class is less than o; 0 if this class is equal to o; 1
	 *         if this class is greater than o.
	 * 
	 **/
	public int compareTo(Object o) {
		PokerMoney m = (PokerMoney) o;
		if (value == m.amount()) {
			return 0;
		}
		return ((value > m.amount()) ? 1 : -1);
	}

	/***************************
	 * eq() is used to compare this PokerMoney class to another PokerMoney class
	 * to the nearest cent
	 * 
	 * @deprecated Starting with version 1.00, the class implements Comparable,
	 *             so the compareTo() function should be used instead.
	 * 
	 * @param m
	 *            The PokerMoney object to which this class is to be compared.
	 * @return Whether of not the compared class are equal.
	 * 
	 **/
	public boolean eq(PokerMoney m) {
		if (m == null) {
			return false;
		}
		int thisval = (int) (value * 100.0f);
		int otherval = (int) (m.amount() * 100.0f);
		return ((thisval == otherval) ? true : false);
	}

	/***************************
	 * ne() is used to compare this PokerMoney class to another PokerMoney class
	 * to the nearest cent
	 * 
	 * @deprecated Starting with version 1.00, the class implements Comparable,
	 *             so the compareTo() function should be used instead.
	 * 
	 * @param m
	 *            The PokerMoney object to which this class is to be compared.
	 * @return Whether of not the compared class are not equal.
	 * 
	 **/
	public boolean ne(PokerMoney m) {
		if (m == null) {
			return false;
		}
		int thisval = (int) (value * 100.0f);
		int otherval = (int) (m.amount() * 100.0f);
		return ((thisval != otherval) ? true : false);
	}

	/***************************
	 * gt() is used to compare this PokerMoney class to another PokerMoney class
	 * to the nearest cent
	 * 
	 * @deprecated Starting with version 1.00, the class implements Comparable,
	 *             so the compareTo() function should be used instead.
	 * 
	 * @param m
	 *            The PokerMoney object to which this class is to be compared.
	 * @return Whether this class is greater than the argument class.
	 * 
	 **/
	public boolean gt(PokerMoney m) {
		if (m == null) {
			return false;
		}
		int thisval = (int) (value * 100.0f);
		int otherval = (int) (m.amount() * 100.0f);
		return ((thisval > otherval) ? true : false);
	}

	/***************************
	 * ge() is used to compare this PokerMoney class to another PokerMoney class
	 * to the nearest cent
	 * 
	 * @deprecated Starting with version 1.00, the class implements Comparable,
	 *             so the compareTo() function should be used instead.
	 * 
	 * @param m
	 *            The PokerMoney object to which this class is to be compared.
	 * @return Whether this class is greater than or equal to the argument
	 *         class.
	 * 
	 **/
	public boolean ge(PokerMoney m) {
		if (m == null) {
			return false;
		}
		int thisval = (int) (value * 100.0f);
		int otherval = (int) (m.amount() * 100.0f);
		return ((thisval >= otherval) ? true : false);
	}

	/***************************
	 * lt() is used to compare this PokerMoney class to another PokerMoney class
	 * to the nearest cent
	 * 
	 * @deprecated Starting with version 1.00, the class implements Comparable,
	 *             so the compareTo() function should be used instead.
	 * 
	 * @param m
	 *            The PokerMoney object to which this class is to be compared.
	 * @return Whether this class is less than the argument class.
	 * 
	 **/
	public boolean lt(PokerMoney m) {
		if (m == null) {
			return false;
		}
		int thisval = (int) (value * 100.0f);
		int otherval = (int) (m.amount() * 100.0f);
		return ((thisval < otherval) ? true : false);
	}

	/***************************
	 * le() is used to compare this PokerMoney class to another PokerMoney class
	 * to the nearest cent
	 * 
	 * @deprecated Starting with version 1.00, the class implements Comparable,
	 *             so the compareTo() function should be used instead.
	 * 
	 * @param m
	 *            The PokerMoney object to which this class is to be compared.
	 * @return Whether this class is less than or equal to the argument class.
	 * 
	 **/
	public boolean le(PokerMoney m) {
		if (m == null) {
			return false;
		}
		int thisval = (int) (value * 100.0f);
		int otherval = (int) (m.amount() * 100.0f);
		return ((thisval <= otherval) ? true : false);
	}

	/***************************
	 * toString() overrides the default toString() function - this is the main
	 * purpose of this class: to display the float value as a dollar amoount.
	 * 
	 * @return The String representation of this class.
	 * 
	 **/
	public String toString() {
		int r = (int) value;
		float d = value - (float) r + 0.004f;
		int id = (int) (100 * d);
		return ((id > 9) ? ("$" + r + "." + id) : ("$" + r + ".0" + id));
	}

	/***************************
	 * nearestCent() is used to cut the value down to the nearest cent, because
	 * thats all PokerMoney should represent anyway.
	 **/
	private void nearestCent() {
		int r = (int) (value * 100.0f);
		value = (float) r / 100.0f;
	}
}