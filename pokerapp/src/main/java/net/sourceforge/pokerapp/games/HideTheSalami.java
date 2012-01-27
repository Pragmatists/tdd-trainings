/******************************************************************************************
 * HideTheSalami.java              PokerApp                                               *
 *                                                                                        *
 * Hide The Salami game definition.                                                       *
 *                                                                                        *
 *   Revision History                                                                     *
 * +---------+----------+---------------------------------------------------------------+ *
 * | Version | DATE     | Description                                                   | *
 * +---------+----------+---------------------------------------------------------------+ *
 * |  0.99   | 05/21/05 | Initial documented release                                    | *
 * |  1.00   | 06/06/05 | Added logging.                                                | *
 * |  1.00   | 08/11/07 | Prepare for open source.  Header/comments/package/etc...      | *
 * +---------+----------+---------------------------------------------------------------+ *
 *                                                                                        *
 ******************************************************************************************/

package net.sourceforge.pokerapp.games;

import net.sourceforge.pokerapp.*;

/****************************************************
 * Hide the Salami game definition.
 * 
 * @author Dan Puperi
 * @version 1.00
 * 
 **/
public class HideTheSalami extends HighChicagoShipwreck {

	/**
	 * Defines who has flipped their 3 cards
	 **/
	protected boolean flipped[];
	/**
	 * Has everybody flipped cards.
	 **/
	protected boolean allFlipped;
	/**
	 * Which cards are selected to be flipped
	 **/
	protected boolean flipSelections[][];

	/***********************
	 * Constructor creates an instance of a game of Hide The Salami
	 * 
	 * @param a
	 *            The StartPoker instance to which this game belongs
	 * 
	 **/
	public HideTheSalami(StartPoker a) {
		super(a, "Hide the Salami", true);
		if (theApp.getPlayerList().size() < 2) {
			return;
		}
		theApp.log("Constructing a game of Hide the Salami", 3);
		maxActionNum = 5;
		flipSelections = new boolean[MAX_PLAYERS][5];
		flipped = new boolean[MAX_PLAYERS];
		for (int i = 0; i < theApp.getPlayerList().size(); i++) {
			flipped[i] = false;
		}
		allFlipped = false;
		requiresInteraction = true;
	}

	/***********************
	 * deal() deals the initial cards
	 **/
	protected void deal() {
		theApp.log("HideTheSalami.deal()", 3);
		Card c = new Card();

		// First card down
		for (int i = 0; i < theApp.getPlayerList().size(); i++) {
			c = theApp.getDeck().deal();
			((Player) theApp.getPlayerList().get(i)).getHand().addHoleCard(c);
			theApp.messageToPlayer(
					((Player) theApp.getPlayerList().get(i)).getName(),
					"CARD HOLE  &"
							+ ((Player) theApp.getPlayerList().get(i))
									.getName() + "&0&" + c);
			theApp.broadcastMessage("CARD HOLE  &"
					+ ((Player) theApp.getPlayerList().get(i)).getName() + "&0");
		}

		// Second card down
		for (int i = 0; i < theApp.getPlayerList().size(); i++) {
			c = theApp.getDeck().deal();
			((Player) theApp.getPlayerList().get(i)).getHand().addHoleCard(c);
			theApp.messageToPlayer(
					((Player) theApp.getPlayerList().get(i)).getName(),
					"CARD HOLE  &"
							+ ((Player) theApp.getPlayerList().get(i))
									.getName() + "&1&" + c);
			theApp.broadcastMessage("CARD HOLE  &"
					+ ((Player) theApp.getPlayerList().get(i)).getName() + "&1");
		}

		// Third card down
		for (int i = 0; i < theApp.getPlayerList().size(); i++) {
			c = theApp.getDeck().deal();
			((Player) theApp.getPlayerList().get(i)).getHand().addHoleCard(c);
			theApp.messageToPlayer(
					((Player) theApp.getPlayerList().get(i)).getName(),
					"CARD HOLE  &"
							+ ((Player) theApp.getPlayerList().get(i))
									.getName() + "&2&" + c);
			theApp.broadcastMessage("CARD HOLE  &"
					+ ((Player) theApp.getPlayerList().get(i)).getName() + "&2");
		}

		// Fourth card down
		for (int i = 0; i < theApp.getPlayerList().size(); i++) {
			c = theApp.getDeck().deal();
			((Player) theApp.getPlayerList().get(i)).getHand().addHoleCard(c);
			theApp.messageToPlayer(
					((Player) theApp.getPlayerList().get(i)).getName(),
					"CARD HOLE  &"
							+ ((Player) theApp.getPlayerList().get(i))
									.getName() + "&3&" + c);
			theApp.broadcastMessage("CARD HOLE  &"
					+ ((Player) theApp.getPlayerList().get(i)).getName() + "&3");
		}

		// Fifth card down
		for (int i = 0; i < theApp.getPlayerList().size(); i++) {
			c = theApp.getDeck().deal();
			((Player) theApp.getPlayerList().get(i)).getHand().addHoleCard(c);
			theApp.messageToPlayer(
					((Player) theApp.getPlayerList().get(i)).getName(),
					"CARD HOLE  &"
							+ ((Player) theApp.getPlayerList().get(i))
									.getName() + "&4&" + c);
			theApp.broadcastMessage("CARD HOLE  &"
					+ ((Player) theApp.getPlayerList().get(i)).getName() + "&4");
		}
		bestPossible = calcBestPossible();
	}

	/***********************
	 * nextAction() determines what happens next
	 **/
	protected void nextAction() {
		theApp.log("HideTheSalami.nextAction() - previous actionNum = "
				+ actionNum, 3);
		actionNum++;
		for (int i = 0; i < theApp.getPlayerList().size(); i++) {
			((Player) theApp.getPlayerList().get(i)).potOK = false;
			((Player) theApp.getPlayerList().get(i)).setBet(0.0f);
			((Player) theApp.getPlayerList().get(i)).setPrevBet(0.0f);
		}
		switch (actionNum) {
		case 2:
			theApp.broadcastMessage("DISABLE BUTTONS");
			theApp.broadcastMessage("MESSAGE &Select three cards to flip over and press the Done button.");
			flipCards();
			break;
		case 3:
			card6();
			bestPossible = calcBestPossible();
			break;
		case 4:
			card7();
			bestPossible = calcBestPossible();
			break;
		case 5:
			show();
			break;
		default:
			break;
		}
		currPlayerIndex = firstToBet();
		highBettor = (Player) theApp.getPlayerList().get(currPlayerIndex);
		currBet = new PokerMoney(0.0f);
		theApp.updateMoneyLine(currBet, highBettor.getName());
	}

	/***********************
	 * firstToBet() determines who gets to bet first
	 * 
	 * @return The player index of the play who will bet first this round
	 * 
	 **/
	protected int firstToBet() {
		if (actionNum <= 1) {
			return firstToBet_HoldEm();
		} else {
			return firstToBet_Stud();
		}
	}

	/***********************
	 * flipCards() defines what happens when the players flip their cards over.
	 **/
	protected void flipCards() {
		theApp.log("HideTheSalami.flipCards()");
		allFlipped = true;
		for (int i = 0; i < theApp.getPlayerList().size(); i++) {
			if ((((Player) theApp.getPlayerList().get(i)).in) && (!flipped[i])) {
				allFlipped = false;
			}
		}

		if (!allFlipped) {
			theApp.broadcastMessage("DISPLAY NOTHING");
			for (int i = 0; i < theApp.getPlayerList().size(); i++) {
				if ((((Player) theApp.getPlayerList().get(i)).in)
						&& (!flipped[i])) {
					theApp.messageToPlayer(((Player) theApp.getPlayerList()
							.get(i)).getName(), "DISPLAY DONE BUTTON");
				}
			}
		} else {
			allFlipped = false;
			theApp.broadcastMessage("DISPLAY NOTHING");
			for (int i = 0; i < theApp.getPlayerList().size(); i++) {
				flipped[i] = false;
				Player p = (Player) theApp.getPlayerList().get(i);
				theApp.broadcastMessage("PLAYER NOPICS &" + p.getName());
				if (p.in) {
					Hand newHand = new Hand();
					for (int cindex = 0; cindex < 5; cindex++) {
						if (flipSelections[i][cindex]) {
							newHand.addUpCard(p.getHand().getHoleCard(cindex));
						} else {
							newHand.addHoleCard(p.getHand().getHoleCard(cindex));
						}
					}
					((Player) theApp.getPlayerList().get(i)).getHand()
							.clearHand();
					theApp.messageToPlayer(p.getName(), "PLAYER CLEAR HAND &"
							+ p.getName());
					for (int c = 0; c < newHand.getNumHole(); c++) {
						((Player) theApp.getPlayerList().get(i)).getHand()
								.addHoleCard(newHand.getHoleCard(c));
						theApp.messageToPlayer(p.getName(),
								"CARD HOLE  &" + p.getName() + "&" + c + "&"
										+ newHand.getHoleCard(c));
						theApp.broadcastMessage("CARD HOLE  &" + p.getName()
								+ "&" + c);
					}
					for (int c = 0; c < newHand.getNumUp(); c++) {
						((Player) theApp.getPlayerList().get(i)).getHand()
								.addUpCard(newHand.getUpCard(c));
						theApp.broadcastMessage("CARD UP  &" + p.getName()
								+ "&" + (c + newHand.getNumHole()) + "&"
								+ newHand.getUpCard(c));
					}
				}
			}
			nextAction();
		}
	}

	/***********************
	 * mouseClick() handles mouse click events.
	 * 
	 * @param name
	 *            The name of the player making the mouse click
	 * @param x
	 *            The x location of the mouse click
	 * @param y
	 *            The y location of the mouse click
	 * 
	 **/
	protected void mouseClick(String name, int x, int y) {
		theApp.log("HideTheSalami.mouseClick( " + name + ", " + x + ", " + y
				+ " )", 3);
		int pindex = theApp.playerIndex(name);
		int loc = ((Player) theApp.getPlayerList().get(pindex)).seat;
		int numSel = 0;
		for (int i = 0; i < 5; i++) {
			if (flipSelections[pindex][i]) {
				numSel++;
			}
		}
		//
		// Players selecting which cards to flip
		//
		if (actionNum == 2) {
			//
			// Player has clicked the "Done" button in the middle of the screen.
			//
			if ((x >= 340) && (x <= 340 + 150) && (y >= 200 + 23)
					&& (y <= 200 + 23 + 50)) {
				if (numSel == 3) {
					theApp.log(name + " selected 3 cards to flip over.", 1);
					theApp.messageToPlayer(name,
							"MESSAGE &Waiting for everyone else to select cards to pass.");
					flipped[pindex] = true;
					flipCards();
					return;
				} else {
					theApp.messageToPlayer(name,
							"MESSAGE &You must select 3 cards to flip.");
					return;
				}
			}
			//
			// Check to see if player has clicked to select or deselect any of
			// his cards.
			//
			for (int cardNum = 0; cardNum < 5; cardNum++) {
				int width = 29;
				if (cardNum == 4) {
					width = 71;
				}
				if ((x >= PokerApp.LOCATION[loc][0] + 30 * cardNum)
						&& (x <= PokerApp.LOCATION[loc][0] + 30 * cardNum
								+ width)
						&& (y >= PokerApp.LOCATION[loc][1] + 15)
						&& (y <= PokerApp.LOCATION[loc][1] + 23 + 96)) {
					if (flipSelections[pindex][cardNum]) {
						theApp.messageToPlayer(name, "DESELECT CARD &"
								+ cardNum);
						flipSelections[pindex][cardNum] = !flipSelections[pindex][cardNum];
					} else if (numSel < 3) {
						theApp.messageToPlayer(name, "SELECT CARD &" + cardNum);
						flipSelections[pindex][cardNum] = !flipSelections[pindex][cardNum];
					}
				}
			}
		}
	}
}
