/******************************************************************************************
 * Picture.java                    PokerApp                                               *
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

import java.awt.Image;

/****************************************************
 * The picture class is used to display images on the poker table. It contains
 * the image and the X and Y coordinates of where the image is displayed.
 * 
 * @author Dan Puperi
 * @version 1.00
 * 
 **/
public class Picture {

	private int x; // X coordinate of this picture
	private int y; // Y coordinate of this picture
	private Image image; // The image

	/***************************
	 * A Picture class is created by specifying the image and x and y position
	 * of the image on the window.
	 * 
	 * @param i
	 *            The Image that this Picture class represents
	 * @param xpos
	 *            The x-coordinate of this picture on the window
	 * @param ypos
	 *            The y-coordinate of this picture on the window
	 * 
	 **/
	public Picture(Image i, int xpos, int ypos) {
		image = i;
		x = xpos;
		y = ypos;
	}

	/***************************
	 * getImage() is used to get the (private) Image that this Picture class
	 * represents
	 * 
	 * @return The Image class of this Picture
	 * 
	 **/
	public Image getImage() {
		return image;
	}

	/***************************
	 * getX() is used to get the (private) x-coordinate of the Picture
	 * 
	 * @return The x-coordinate of this Picture
	 * 
	 **/
	public int getX() {
		return x;
	}

	/***************************
	 * getY() is used to get the (private) y-coordinate of the Picture
	 * 
	 * @return The y-coordinate of this Picture
	 * 
	 **/
	public int getY() {
		return y;
	}
}