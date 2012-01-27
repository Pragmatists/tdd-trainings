/******************************************************************************************
 * PokerFrame.java                 PokerApp                                               *
 *                                                                                        *
 *   Revision History                                                                     *
 * +---------+----------+---------------------------------------------------------------+ *
 * | Version | DATE     | Description                                                   | *
 * +---------+----------+---------------------------------------------------------------+ *
 * |  0.95   | 09/15/04 | Initial documented release                                    | *
 * |  0.96   | 09/17/04 | Check to make sure client socket is not null before discon.   | *
 * |  0.96   | 10/04/04 | Fixed the glass pane names / descriptions all fit in the      | *
 * |         |          | space allotted for them.  Changed from a 5x3 setup to a 3x3   | *
 * |         |          | setup which made it look better.                              | *
 * |  0.97   | 10/26/04 | Added display of how much money each player has to glass pane | *
 * |         |          | labels.                                                       | *
 * |  0.97   | 11/08/04 | Added a message to the server when the mouse is clicked       | *
 * |  0.98   | 12/08/04 | Changed buttons for betLimit and potLimit games.              | *
 * |  0.98   | 12/09/04 | Fix null pointer exception for mouse clicks.                  | *
 * |  0.98   | 12/10/04 | Change disconnectSocket call to pass a boolean whether to     | *
 * |         |          | try to redraw or not.                                         | *
 * |  0.98   | 12/12/04 | Change the JFrame icon.                                       | *
 * |  0.99   | 05/17/05 | For structured betting games, player does not have to press   | *
 * |         |          | the submit bet button - disable buttons as appropriate.       | *
 * |  1.00   | 06/05/05 | Set focus painting off on the buttons.                        | *
 * |  1.00   | 06/06/05 | Added logging.                                                | *
 * |  1.00   | 07/25/05 | Added option for one click check and call buttons.            | *
 * |  1.00   | 08/26/05 | Saved past messages so they can be shown in message window.   | *
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

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/****************************************************
 * PokerFrame is the class that represents the main PokerApp window for each
 * player.
 * 
 * @author Dan Puperi
 * @version 1.00
 * 
 **/
public final class PokerFrame extends JFrame {

	private PokerGlassPane gp; // The glass pane.
	private JPanel messagePanel; // Panel where messages are displayed
	private JPanel cashPanel; // Panel where info about the game and betting is
								// displayed
	private JTextField messageText; // The message text being displayed
	private JTextField bankText; // How much money this player had
	private JTextField potText; // How much money is in the pot
	private JTextField betText; // How much is the current bet
	private JTextField yourBetText; // How much is this player about to bet
	private ArrayList positionLabels; // Labels under each possible player
										// location
	private ArrayList cashLabels; // Labels showing how much money each player
									// has.
	private ArrayList dealerLabels; // Labels with icon of dealer button to
									// designate who is the dealer.
	private String playerText[]; // String of the text under each player
	private PokerApp theApp; // Instance of the PokerApp that started this
								// window
	private JButton foldButton; // Button used to fold
	private JButton checkButton; // Button used to check
	private JButton callButton; // Button used to call the current bet
	private JButton oneButton; // Button used to increase bet by lowest
								// denomination
	private JButton twoButton; // Button used to increase bet by 2nd lowest
								// denomination
	private JButton threeButton; // Button used to increase bet by 2nd highest
									// denomination
	private JButton fourButton; // Button used to increase bet by highest
								// denomination or to place the fixed bet
	private JButton allinButton; // Button to go All In or to Raise
	private JButton betButton; // Button used to submit this player's bet.
	private PokerMenuBar menuBar; // The menu bar for this window

	/***************************
	 * The PokerFrame class is created by specifying the title of the frame and
	 * the PokerApp class instance to which this frame belongs.
	 * 
	 * @param title
	 *            The String title which will appear at the top of the frame in
	 *            the title bar
	 * @param a
	 *            The PokerApp class to which this frame belongs
	 * 
	 **/
	public PokerFrame(String title, PokerApp a) {
		super(title);
		theApp = a;
		theApp.log("Constructing PokerFrame", 3);
		ClassLoader cl = getClass().getClassLoader();
		java.net.URL url = cl.getResource("Images/icon.gif");
		setIconImage(Toolkit.getDefaultToolkit().getImage(url));
		//
		// The player text is the string displayed under the players cards -
		// usually the player's name or their name and last action.
		//
		playerText = new String[PokerGame.MAX_PLAYERS];
		//
		// Create the menu bar instance and add it to the window
		//
		menuBar = new PokerMenuBar(theApp);
		setJMenuBar(menuBar);
		//
		// The table takes up the majority of the content pane. The view is used
		// to decide what is displayed, how to redraw, etc..
		//
		JPanel table = new JPanel(new BorderLayout());
		table.add(theApp.getView(), BorderLayout.CENTER);
		getContentPane().add(table, BorderLayout.CENTER);
		//
		// The bottom panel contains the information area and the betting
		// buttons. The betting buttons are placed on 2 panels
		// with 4 buttons each. The message are is also split into 2 panels -
		// the cashPanel and the messagePanel.
		//
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new GridLayout(4, 1));
		JPanel bet1Panel = new JPanel();
		JPanel bet2Panel = new JPanel();
		bet1Panel.setLayout(new GridLayout(1, 4));
		bet2Panel.setLayout(new GridLayout(1, 4));

		foldButton = new JButton("Fold");
		foldButton.addActionListener(new foldAction());
		foldButton.setFocusPainted(false);
		bet1Panel.add(foldButton);
		checkButton = new JButton("Check");
		checkButton.addActionListener(new checkAction());
		checkButton.setFocusPainted(false);
		bet1Panel.add(checkButton);
		callButton = new JButton("Call");
		callButton.addActionListener(new callAction());
		callButton.setFocusPainted(false);
		bet1Panel.add(callButton);
		betButton = new JButton("Submit Bet");
		betButton.addActionListener(new betAction());
		betButton.setFocusPainted(false);
		bet1Panel.add(betButton);

		oneButton = new JButton();
		oneButton.addActionListener(new oneAction());
		oneButton.setFocusPainted(false);
		bet2Panel.add(oneButton);
		twoButton = new JButton();
		twoButton.addActionListener(new twoAction());
		twoButton.setFocusPainted(false);
		bet2Panel.add(twoButton);
		threeButton = new JButton();
		threeButton.addActionListener(new threeAction());
		threeButton.setFocusPainted(false);
		bet2Panel.add(threeButton);
		fourButton = new JButton();
		fourButton.addActionListener(new fourAction());
		fourButton.setFocusPainted(false);
		bet2Panel.add(fourButton);
		allinButton = new JButton();
		allinButton.addActionListener(new allinAction());
		allinButton.setFocusPainted(false);
		bet2Panel.add(allinButton);

		cashPanel = new JPanel();
		cashPanel.setLayout(new GridLayout(1, 4));
		cashPanel.setBorder(new EtchedBorder(EtchedBorder.RAISED));
		bankText = new JTextField();
		potText = new JTextField();
		betText = new JTextField();
		bankText.setEditable(false);
		potText.setEditable(false);
		betText.setEditable(false);
		cashPanel.add(bankText);
		cashPanel.add(potText);
		cashPanel.add(betText);

		messagePanel = new JPanel();
		messagePanel.setLayout(new BorderLayout());
		messageText = new JTextField();
		messageText.setEditable(false);
		messagePanel.add(messageText);
		yourBetText = new JTextField();
		yourBetText.setPreferredSize(new Dimension(130, 1));
		yourBetText.setEditable(false);
		messagePanel.add(yourBetText, BorderLayout.EAST);
		sendMessage("Ready to play?");
		updateMoneyLine();

		bottomPanel.add(cashPanel);
		bottomPanel.add(messagePanel);
		bottomPanel.add(bet1Panel);
		bottomPanel.add(bet2Panel);

		getContentPane().add(bottomPanel, BorderLayout.SOUTH);

		gp = new PokerGlassPane(menuBar, foldButton, checkButton, callButton,
				oneButton, twoButton, threeButton, fourButton, betButton,
				allinButton, getContentPane());
		setGlassPane(gp);
		gp.setVisible(true);
		setResizable(false);
	}

	/***********************
	 * sendMessage() is used to display a message in the message box of the
	 * window..
	 * 
	 * @param m
	 *            The String message to display
	 * 
	 **/
	public void sendMessage(String m) {
		messageText.setText(m);
		for (int i = 0; i < PokerApp.NUM_SAVED_MESSAGES - 1; i++) {
			theApp.pastMessages[i] = new String(theApp.pastMessages[i + 1]);
		}
		theApp.pastMessages[PokerApp.NUM_SAVED_MESSAGES - 1] = new String(m);
		if (theApp.messageWindow != null) {
			theApp.messageWindow.sendMessage(m);
		}
	}

	/**********************
	 * updateMoneyLine() is used to redisplay the data about the pot, bankroll,
	 * and betting This one zeros out the data.
	 **/
	public void updateMoneyLine() {
		updateMoneyLine(new PokerMoney(), new PokerMoney(), new PokerMoney(),
				new PokerMoney(), new String());
	}

	/**********************
	 * updateMoneyLine() is used to redisplay the data about the pot, bankroll,
	 * and betting This version the data is filled in through arguments to the
	 * function.
	 * 
	 * @param br
	 *            The player's bankroll
	 * @param pbet
	 *            The player's current bet
	 * @param pot
	 *            The current amount in the pot
	 * @param bet
	 *            The current high bet
	 * @param name
	 *            The player's name who has the high bet.
	 * 
	 **/
	public void updateMoneyLine(PokerMoney br, PokerMoney pbet, PokerMoney pot,
			PokerMoney bet, String name) {
		theApp.log("PokerFrame.updateMoneyLine( " + br + ", " + pbet + ", "
				+ pot + ", " + bet + ", " + name + " )", 3);
		if (theApp.getThisPlayer() != null) {
			theApp.getThisPlayer().setBankroll(br.amount());
		}
		bankText.setText("Cash : " + br);
		potText.setText("Pot : " + pot);
		betText.setText("Current Bet : " + bet + "  by  " + name);
		yourBetText.setText("Your Bet = " + pbet);
	}

	/**********************
	 * enableButtons() enables all the playing buttons that the user is allowed
	 * to interact with.
	 **/
	public void enableButtons() {
		theApp.log("PokerFrame.enableButtons()", 3);
		foldButton.setEnabled(true);
		checkButton.setEnabled(true);
		callButton.setEnabled(true);
		allinButton.setEnabled(true);
		fourButton.setEnabled(true);
		oneButton.setEnabled(true);
		twoButton.setEnabled(true);
		threeButton.setEnabled(true);
		betButton.setEnabled(true);
		if (!(theApp.noLimit || theApp.betLimit || theApp.potLimit)) {
			oneButton.setEnabled(false);
			twoButton.setEnabled(false);
			threeButton.setEnabled(false);
			betButton.setEnabled(false);
			if (theApp.structuredRaiseEnable) {
				checkButton.setEnabled(false);
				fourButton.setEnabled(false);
			} else {
				allinButton.setEnabled(false);
				callButton.setEnabled(false);
			}
		}
	}

	/**********************
	 * disableButtons() makes a player not be able to perform any input.
	 **/
	public void disableButtons() {
		theApp.log("PokerApp.disableButtons()", 3);
		foldButton.setEnabled(false);
		checkButton.setEnabled(false);
		callButton.setEnabled(false);
		betButton.setEnabled(false);
		oneButton.setEnabled(false);
		twoButton.setEnabled(false);
		threeButton.setEnabled(false);
		fourButton.setEnabled(false);
		allinButton.setEnabled(false);
	}

	/**********************
	 * buttonRedisplay() redisplays the buttons - with updated labels.
	 **/
	public void buttonRedisplay() {
		theApp.log("PokerApp.buttonRedisplay()", 3);
		if (theApp.noLimit) {
			oneButton.setText(theApp.button1Val.toString());
			twoButton.setText(theApp.button2Val.toString());
			threeButton.setText(theApp.button3Val.toString());
			fourButton.setText(theApp.button4Val.toString());
			allinButton.setText("All In");
			betButton.setText("Submit Bet");
			callButton.setText("Call");
			checkButton.setText("Check");
		} else if (theApp.betLimit || theApp.potLimit) {
			oneButton.setText(theApp.button1Val.toString());
			twoButton.setText(theApp.button2Val.toString());
			threeButton.setText(theApp.button3Val.toString());
			fourButton.setText(theApp.button4Val.toString());
			allinButton.setText("Max Bet");
			betButton.setText("Submit Bet");
			callButton.setText("Call");
			checkButton.setText("Check");
		} else {
			oneButton.setText(" ");
			twoButton.setText(" ");
			threeButton.setText(" ");
			betButton.setText(" ");
			if (theApp.structuredRaiseEnable) {
				checkButton.setText(" ");
				fourButton.setText(" ");
				callButton.setText("Call");
				allinButton.setText("Raise");
			} else {
				checkButton.setText("Check");
				fourButton.setText("Bet");
				callButton.setText(" ");
				allinButton.setText(" ");
			}
		}
	}

	/**********************
	 * setPlayerText() sets the text that should be displayed by a players
	 * position on the board.
	 * 
	 * @param p
	 *            The player index of whose text to change
	 * @param t
	 *            The String to which to change the display
	 * 
	 **/
	public void setPlayerText(int p, String t) {
		playerText[p] = new String(t);
	}

	/**********************
	 * getPlayerText() gets the text that should be displayed by a players
	 * position on the board.
	 * 
	 * @param p
	 *            The player index being inquired
	 * @return The text that is displayed.
	 * 
	 **/
	public String getPlayerText(int p) {
		return playerText[p];
	}

	/**********************
	 * menuRedisplay() redisplays all the data on the menu
	 **/
	public void menuRedisplay() {
		theApp.log("PokerFrame.menuRedisplay()", 3);
		if (menuBar.rulesWindow != null) {
			menuBar.rulesWindow.dispose();
		}
		getGlassPane().setVisible(false);
		remove(getGlassPane());
		remove(menuBar);
		menuBar = new PokerMenuBar(theApp);
		setJMenuBar(menuBar);
		gp = new PokerGlassPane(menuBar, foldButton, checkButton, callButton,
				oneButton, twoButton, threeButton, fourButton, betButton,
				allinButton, getContentPane());
		setGlassPane(gp);
		gp.setVisible(true);
	}

	/***********************
	 * processWindowEvent() processes the close window event
	 * 
	 * @param e
	 *            The window event to process
	 * 
	 **/
	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			if (theApp.getSocket() != null) {
				theApp.disconnectSocket(false);
			}
			if (theApp.startUpApp == null) {
				dispose();
				theApp.closeLogFile();
				System.exit(0);
			} else {
				theApp.startUpApp.KillClient();
			}
		}
		super.processWindowEvent(e);
	}

	/**********************
	 * The Mouse class defines what happens when the mouse buttons are pushed.
	 **/
	class Mouse extends MouseAdapter {

		Toolkit tk; // The toolkit for this window
		JMenuBar menuBar; // The menu bar
		Component b1, b2, b3, b4, b5, b6, b7, b8, b9; // All of the buttons
		PokerGlassPane glassPane; // The glass pane is an invisible pane used to
									// capture mouse events.
		Container contentPane; // The content pane is the main pane of the
								// window.

		// ----------------------
		// Constructor
		//
		public Mouse(JMenuBar mb, Component b1, Component b2, Component b3,
				Component b4, Component b5, Component b6, Component b7,
				Component b8, Component b9, PokerGlassPane gp, Container cp) {
			tk = Toolkit.getDefaultToolkit();
			this.menuBar = mb;
			this.b1 = b1;
			this.b2 = b2;
			this.b3 = b3;
			this.b4 = b4;
			this.b5 = b5;
			this.b6 = b6;
			this.b7 = b7;
			this.b8 = b8;
			this.b9 = b9;
			this.glassPane = gp;
			this.contentPane = cp;
		}

		// ----------------------
		// action fucntions
		//
		// what to do with a mouse event.
		//
		public void mouseClicked(MouseEvent e) {
			redispatchMouseEvent(e, false);
		}

		public void mousePressed(MouseEvent e) {
			redispatchMouseEvent(e, false);
		}

		public void mouseReleased(MouseEvent e) {
			redispatchMouseEvent(e, true);
		}

		// ----------------------
		// redispatchMouseEvent()
		//
		// When a mouse event is recorded, need to figure out what to do with
		// it.
		// When the mouse even happens over the menu or a button, need to pass
		// that event to the menu or button.
		// When a mouse event happens on the table, figure out what to do with
		// it.
		//
		public void redispatchMouseEvent(MouseEvent e, boolean repaint) {
			boolean inMenuBar = false;
			boolean inButtons = false;
			Point glassPanePoint = e.getPoint();
			Component component = null;
			Container container = contentPane;
			Point containerPoint = SwingUtilities.convertPoint(glassPane,
					glassPanePoint, contentPane);
			int eventID = e.getID();

			if (containerPoint.y < 0) {
				inMenuBar = true;
				container = menuBar;
				containerPoint = SwingUtilities.convertPoint(glassPane,
						glassPanePoint, menuBar);
			}

			component = SwingUtilities.getDeepestComponentAt(container,
					containerPoint.x, containerPoint.y);

			if (component == null) {
				return;
			}
			if (component.equals(b1) || component.equals(b2)
					|| component.equals(b3) || component.equals(b4)
					|| component.equals(b5) || component.equals(b6)
					|| component.equals(b7) || component.equals(b8)
					|| component.equals(b9)) {
				inButtons = true;
			}

			if ((inMenuBar) || (inButtons)) {
				Point componentPoint = SwingUtilities.convertPoint(glassPane,
						glassPanePoint, component);
				component
						.dispatchEvent(new MouseEvent(component, eventID, e
								.getWhen(), e.getModifiers(), componentPoint.x,
								componentPoint.y, e.getClickCount(), e
										.isPopupTrigger()));
			} else {
				if ((SwingUtilities.isLeftMouseButton(e))
						&& (e.getID() == MouseEvent.MOUSE_PRESSED)) {
					if (theApp.getThisPlayer() != null) {
						theApp.messageToServer("LEFT MOUSE &"
								+ theApp.getThisPlayer().getName() + "&"
								+ e.getX() + "&" + e.getY());
					}
				}
			}

			if (repaint) {
				glassPane.repaint();
			}
		}
	}

	/**********************
	 * PokerGlassPane class extends the glass pane so the menu works, but
	 * nothing else does
	 **/
	class PokerGlassPane extends JComponent {

		// ----------------------
		// Constructor
		//
		public PokerGlassPane(JMenuBar mb, Component b1, Component b2,
				Component b3, Component b4, Component b5, Component b6,
				Component b7, Component b8, Component b9, Container cp) {
			Mouse listener = new Mouse(mb, b1, b2, b3, b4, b5, b6, b7, b8, b9,
					this, cp);
			addMouseListener(listener);
			setLayout(new GridLayout(3, 5));
			setBorder(new javax.swing.border.EmptyBorder(15, 25, 110, 10));

			positionLabels = new ArrayList();
			cashLabels = new ArrayList();
			dealerLabels = new ArrayList();
			for (int pos = 1; pos < 10; pos++) {
				int playerNumber = playerAtPosition(pos);
				if ((playerNumber == -9)
						&& (theApp
								.playerIndex(theApp.getThisPlayer().getName()) == -1)) {
					positionLabels.add(new JLabel());
					cashLabels.add(new JLabel());
					dealerLabels.add(new SitDownPanel(pos));
				} else if (playerNumber == -9) {
					positionLabels.add(new JLabel());
					cashLabels.add(new JLabel());
					dealerLabels.add(new JLabel());
				} else if (playerNumber == -1) {
					positionLabels.add(new JLabel());
					cashLabels.add(new JLabel());
					dealerLabels.add(new JLabel());
				} else if (playerNumber == theApp.dealerIndex) {
					positionLabels.add(new JLabel(playerText[playerNumber]));
					cashLabels.add(new JLabel("Cash = "
							+ ((Player) theApp.getPlayerList()
									.get(playerNumber)).getBankroll()));
					dealerLabels.add(new JLabel(new ImageIcon(theApp.getView()
							.getDealerButtonImg())));
				} else {
					positionLabels.add(new JLabel(playerText[playerNumber]));
					cashLabels.add(new JLabel("Cash = "
							+ ((Player) theApp.getPlayerList()
									.get(playerNumber)).getBankroll()));
					dealerLabels.add(new JLabel());
				}
			}
			for (int i = 0; i < positionLabels.size(); i++) {
				((JComponent) positionLabels.get(i)).setForeground(Color.WHITE);
				((JComponent) cashLabels.get(i)).setForeground(Color.WHITE);
				JPanel bigPanel = new JPanel();
				bigPanel.setOpaque(false);
				bigPanel.setLayout(new GridLayout(3, 1));
				bigPanel.add(new JLabel());
				bigPanel.add(new JLabel());
				JPanel dispPanel = new JPanel();
				dispPanel.setOpaque(false);
				dispPanel.setLayout(new BorderLayout());
				JPanel iconPanel = new JPanel();
				iconPanel.setOpaque(false);
				iconPanel.add((JComponent) dealerLabels.get(i));
				JPanel textPanel = new JPanel();
				textPanel.setOpaque(false);
				textPanel.setLayout(new GridLayout(3, 1));
				textPanel.add(new JLabel());
				textPanel.add((JComponent) positionLabels.get(i));
				textPanel.add((JComponent) cashLabels.get(i));
				dispPanel.add(iconPanel, BorderLayout.WEST);
				dispPanel.add(textPanel, BorderLayout.CENTER);
				bigPanel.add(dispPanel);
				add(bigPanel);
			}
		}

		// ----------------------
		// playerAtPosition()
		//
		// Maps the position on the table to a player.
		//
		private int playerAtPosition(int pos) {
			if (theApp.getSocket() == null) {
				return -1;
			}
			switch (pos) {
			case 1:
				return theApp.getPlayerInSeat(0);
			case 2:
				return theApp.getPlayerInSeat(1);
			case 3:
				return theApp.getPlayerInSeat(2);
			case 4:
				return theApp.getPlayerInSeat(7);
			case 5:
				return -1;
			case 6:
				return theApp.getPlayerInSeat(3);
			case 7:
				return theApp.getPlayerInSeat(6);
			case 8:
				return theApp.getPlayerInSeat(5);
			case 9:
				return theApp.getPlayerInSeat(4);
			default:
				return -1;
			}
		}

		// ----------------------
		// SitDownPanel class displays the "Sit Down" buttons
		//
		public class SitDownPanel extends JPanel {

			int position;
			JButton button;

			// ----------------------
			// Constructor
			//
			public SitDownPanel(int pos) {
				super();

				switch (pos) {
				case 1:
					position = 0;
					break;
				case 2:
					position = 1;
					break;
				case 3:
					position = 2;
					break;
				case 4:
					position = 7;
					break;
				case 6:
					position = 3;
					break;
				case 7:
					position = 6;
					break;
				case 8:
					position = 5;
					break;
				case 9:
					position = 4;
					break;
				default:
					position = 0;
					break;
				}
				setOpaque(false);
				setBorder(new EmptyBorder(10, 0, 10, 20));
				button = new JButton("Sit Down");
				button.addActionListener(new sitDownAction(position));
				add(button);
			}

			// ----------------------
			// SitDownAction class defines what happens when a player presses a
			// sit down button
			//
			class sitDownAction extends AbstractAction {

				int position;

				// ----------------------
				// Constructor
				//
				public sitDownAction(int p) {
					position = p;
				}

				// ----------------------
				// The actionPerformed() function is called when the button is
				// pressed
				//
				public void actionPerformed(ActionEvent e) {
					theApp.log(
							"PokerFrame.PokerGlassPane.SitDownPanel.sitDownAction.actionPerformed() in seat "
									+ position, 3);
					int show = 1;
					if (theApp.muckLosingHands) {
						show = 0;
					}
					theApp.messageToServer("NEW PLAYER &" + position + "&"
							+ show);
					theApp.getThisPlayer().seat = position;
				}
			}
		}
	}

	/**********************
	 * foldAction class defines what happens when the fold button is clicked.
	 **/
	class foldAction extends AbstractAction {

		// ----------------------
		// Constructor
		//
		foldAction() {
			super();
		}

		// ----------------------
		// The actionPerformed() function is called when the button is pressed
		//
		public void actionPerformed(ActionEvent e) {
			theApp.log("PokerFrame.foldAction.actionPerformed()", 3);
			theApp.messageToServer("BET FOLD");
		}
	}

	/**********************
	 * checkAction class defines what happens when the check button is clicked
	 **/
	class checkAction extends AbstractAction {

		// ----------------------
		// Constructor
		//
		checkAction() {
			super();
		}

		// ----------------------
		// The actionPerformed() function is called when the button is pressed
		//
		public void actionPerformed(ActionEvent e) {
			theApp.log("PokerFrame.checkAction.actionPerformed()", 3);
			theApp.messageToServer("BET CHECK");
			if (theApp.oneClickCheckCall) {
				theApp.messageToServer("BET SUBMIT");
			}
		}
	}

	/**********************
	 * callAction class defines what happens when the call button is clicked.
	 **/
	class callAction extends AbstractAction {

		// ----------------------
		// Constructor
		//
		callAction() {
			super();
		}

		// ----------------------
		// The actionPerformed() function is called when the button is pressed
		//
		public void actionPerformed(ActionEvent e) {
			theApp.log("PokerFrame.callAction.actionPerformed()", 3);
			theApp.messageToServer("BET CALL");
			if (theApp.oneClickCheckCall) {
				theApp.messageToServer("BET SUBMIT");
			}
		}
	}

	/**********************
	 * oneAction class defines what happens when the 1st value button is
	 * clicked.
	 **/
	class oneAction extends AbstractAction {

		// ----------------------
		// Constructor
		//
		oneAction() {
			super();
		}

		// ----------------------
		// The actionPerformed() function is called when the button is pressed
		//
		public void actionPerformed(ActionEvent e) {
			theApp.log("PokerFrame.oneAction.actionPerformed()", 3);
			theApp.messageToServer("BET ONE");
		}
	}

	/**********************
	 * twoAction class defines what happens when the 2nd value button is
	 * clicked.
	 **/
	class twoAction extends AbstractAction {

		// ----------------------
		// Constructor
		//
		twoAction() {
			super();
		}

		// ----------------------
		// The actionPerformed() function is called when the button is pressed
		//
		public void actionPerformed(ActionEvent e) {
			theApp.log("PokerFrame.twoAction.actionPerformed()", 3);
			theApp.messageToServer("BET TWO");
		}
	}

	/**********************
	 * threeAction class defines what happens when the 3rd value button is
	 * clicked.
	 **/
	class threeAction extends AbstractAction {

		// ----------------------
		// Constructor
		//
		threeAction() {
			super();
		}

		// ----------------------
		// The actionPerformed() function is called when the button is pressed
		//
		public void actionPerformed(ActionEvent e) {
			theApp.log("PokerFrame.threeAction.actionPerformed()", 3);
			theApp.messageToServer("BET THREE");
		}
	}

	/**********************
	 * fourAction class defines what happens when the 4th value button is
	 * clicked.
	 **/
	class fourAction extends AbstractAction {

		// ----------------------
		// Constructor
		//
		fourAction() {
			super();
		}

		// ----------------------
		// The actionPerformed() function is called when the button is pressed
		//
		public void actionPerformed(ActionEvent e) {
			theApp.log("PokerFrame.fourAction.actionPerformed()", 3);
			theApp.messageToServer("BET FOUR");
		}
	}

	/**********************
	 * betAction class defines what happens when the bet button is clicked.
	 **/
	class betAction extends AbstractAction {

		// ----------------------
		// Constructor
		//
		betAction() {
			super();
		}

		// ----------------------
		// The actionPerformed() function is called when the button is pressed
		//
		public void actionPerformed(ActionEvent e) {
			theApp.log("PokerFrame.betAction.actionPerformed()", 3);
			theApp.messageToServer("BET SUBMIT");
		}
	}

	/**********************
	 * allinAction - defines what happens when the All In button is clicked.
	 **/
	class allinAction extends AbstractAction {

		// ----------------------
		// Constructor
		//
		allinAction() {
			super();
		}

		// ----------------------
		// The actionPerformed() function is called when the button is pressed
		//
		public void actionPerformed(ActionEvent e) {
			theApp.log("PokerFrame.allinAction.actionPerformed()", 3);
			theApp.messageToServer("BET ALLIN");
		}
	}
}