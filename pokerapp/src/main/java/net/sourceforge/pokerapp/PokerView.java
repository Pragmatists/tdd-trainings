/******************************************************************************************
 * PokerView.java                  PokerApp                                               *
 *                                                                                        *
 *   Revision History                                                                     *
 * +---------+----------+---------------------------------------------------------------+ *
 * | Version | DATE     | Description                                                   | *
 * +---------+----------+---------------------------------------------------------------+ *
 * |  0.95   | 09/15/04 | Initial documented release                                    | *
 * |  0.97   | 11/09/04 | Added the button images that might appear on the table        | *
 * |  0.97   | 11/09/04 | Only use one class loader for efficiency                      | *
 * |  1.00   | 06/06/05 | Added logging of problems.                                    | *
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

import java.util.*;
import java.awt.*;

/****************************************************
 * PokerView displays the images on the poker table.
 * 
 * @author Dan Puperi
 * @version 1.00
 * 
 **/
public class PokerView extends javax.swing.JComponent implements Observer {

	private PokerApp theApp; // The PokerApp that created this instance of
								// PokerView
	private ArrayList tableImgs; // List of table images.
	private ArrayList cardBacks; // List of card back images.
	private Image dealerButtonImg; // The image of the dealer button.
	private Image drawButtonImg; // The image for the "Draw Cards" button
	private Image passButtonImg; // The image for the "Pass Cards" button
	private Image doneButtonImg; // The image for the "Done" button
	private Image yesnoButtonImg; // The image for the "Yes or No" button

	/***************************
	 * The default constructor creates the PokerView
	 * 
	 * @param a
	 *            The PokerApp instance to which this view belongs.
	 * 
	 **/
	public PokerView(PokerApp a) {
		theApp = a;
		tableImgs = new ArrayList();
		cardBacks = new ArrayList();
		theApp.log("Constructing PokerView", 3);
		//
		// Create a media tracker for all of the images of the cards in the deck
		//
		try {
			MediaTracker tracker = new MediaTracker(this);
			ClassLoader cl = getClass().getClassLoader();
			java.net.URL url = cl.getResource("Images/dealer_button.gif");
			dealerButtonImg = Toolkit.getDefaultToolkit().getImage(url);
			tracker.addImage(dealerButtonImg, 0);
			tracker.waitForID(0);
			url = cl.getResource("Images/draw_btn.gif");
			drawButtonImg = Toolkit.getDefaultToolkit().getImage(url);
			tracker.addImage(drawButtonImg, 0);
			tracker.waitForID(0);
			url = cl.getResource("Images/pass_btn.gif");
			passButtonImg = Toolkit.getDefaultToolkit().getImage(url);
			tracker.addImage(passButtonImg, 0);
			tracker.waitForID(0);
			url = cl.getResource("Images/done_btn.gif");
			doneButtonImg = Toolkit.getDefaultToolkit().getImage(url);
			tracker.addImage(doneButtonImg, 0);
			tracker.waitForID(0);
			url = cl.getResource("Images/yesno_btn.gif");
			yesnoButtonImg = Toolkit.getDefaultToolkit().getImage(url);
			tracker.addImage(yesnoButtonImg, 0);
			tracker.waitForID(0);

			for (int i = 0; i < theApp.getCardBackFiles().size(); i++) {
				url = cl.getResource(((String) theApp.getCardBackFiles().get(i)));
				cardBacks.add(Toolkit.getDefaultToolkit().getImage(url));
				tracker.addImage(((Image) cardBacks.get(cardBacks.size() - 1)),
						0);
				tracker.waitForID(0);
			}
			for (int i = 0; i < theApp.getTableImgFiles().size(); i++) {
				url = cl.getResource(((String) theApp.getTableImgFiles().get(i)));
				tableImgs.add(Toolkit.getDefaultToolkit().getImage(url));
				tracker.addImage(((Image) tableImgs.get(tableImgs.size() - 1)),
						0);
				tracker.waitForID(0);
			}
			Deck deck = new Deck();
			for (int i = 0; i < Deck.NUM_CARDS; i++) {
				tracker.addImage(deck.cards[i].img, 0);
				tracker.waitForID(0);
			}
		} catch (Exception e) {
			theApp.log("Warning : Caught exception while trying to create the PokerView class");
			theApp.logStackTrace(e);
		}
	}

	/***********************
	 * getCardBackImage() is used to get the image that will be used for the
	 * card backs
	 * 
	 * @return The Image that will be used on the card backs
	 * 
	 **/
	public Image getCardBackImage() {
		for (int i = 0; i < theApp.cardBackSelection.size(); i++) {
			if (((Boolean) theApp.cardBackSelection.get(i)).booleanValue()) {
				return (Image) cardBacks.get(i);
			}
		}
		return null;
	}

	/***********************
	 * getDealerButtonImg() is used to get the dealer button image
	 * 
	 * @return The Image that will be used for the dealer button
	 * 
	 **/
	public Image getDealerButtonImg() {
		return dealerButtonImg;
	}

	/***********************
	 * getPassButtonImg() is used to get the pass button image
	 * 
	 * @return The Image that will be used for the pass button
	 * 
	 **/
	public Image getDrawButtonImg() {
		return drawButtonImg;
	}

	/***********************
	 * getPassButtonImg() is used to get the pass button image
	 * 
	 * @return The Image that will be used for the pass button
	 * 
	 **/
	public Image getPassButtonImg() {
		return passButtonImg;
	}

	/***********************
	 * getDoneButtonImg() is used to get the done button image
	 * 
	 * @return The Image that will be used for the done button
	 * 
	 **/
	public Image getDoneButtonImg() {
		return doneButtonImg;
	}

	/***********************
	 * getYesNoButtonImg() is used to get the yes/no button image
	 * 
	 * @return The Image that will be used for the yes/no button
	 * 
	 **/
	public Image getYesNoButtonImg() {
		return yesnoButtonImg;
	}

	/***********************
	 * update() is required by any class implementing Observer - nothing to do
	 * for this class though.
	 **/
	public void update(Observable o, Object rectangle) {
	}

	/***********************
	 * paint() overrides JComponent.paint() function - displays the images in
	 * the PokerModel
	 * 
	 * @param g
	 *            The graphics which to paint
	 * 
	 **/
	public void paint(Graphics g) {
		try {
			Graphics2D g2D = (Graphics2D) g;
			Iterator pictures = theApp.getModel().getIterator();

			for (int i = 0; i < theApp.tableImgSelection.size(); i++) {
				if (((Boolean) theApp.tableImgSelection.get(i)).booleanValue()) {
					g2D.drawImage((Image) tableImgs.get(i), 0, 0, null);
				}
			}

			while (pictures.hasNext()) {
				Picture pic = (Picture) pictures.next();
				g2D.drawImage(pic.getImage(), pic.getX(), pic.getY(), null);
			}
		} catch (ConcurrentModificationException x) {
			theApp.log(
					"Caught a ConcurrentModificationException in PokerView.paint()",
					3);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
			theApp.getWindow().repaint();
		}
	}
}