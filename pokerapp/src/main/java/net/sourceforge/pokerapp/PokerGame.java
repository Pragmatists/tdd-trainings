/******************************************************************************************
 * PokerGame.java                  PokerApp                                               *
 *                                                                                        *
 *   Revision History                                                                     *
 * +---------+----------+---------------------------------------------------------------+ *
 * | Version | DATE     | Description                                                   | *
 * +---------+----------+---------------------------------------------------------------+ *
 * |  0.95   | 08/10/04 | Initial documented release                                    | *
 * |  0.96   | 09/20/04 | Correct logic when player does not have enough money to keep  | *
 * |         |          | playing.  Needed to be dismissed more elegantly.              | *
 * |  0.96   | 09/27/04 | Define abstract function calcBestPossible - which helps the   | *
 * |         |          | computer players decide which is the best hand available.     | *
 * |  0.97   | 10/25/04 | Use encrypted communications to send messages.                | *
 * |  0.97   | 10/28/04 | Include players bankrolls when message is sent with what the  | *
 * |         |          | each person did.                                              | *
 * |  0.97   | 11/06/04 | Change firstToBet_Stud routine to be able to be used by more  | *
 * |         |          | game types.                                                   | *
 * |  0.97   | 11/08/04 | Changed mouseClick routine to accept a name as an arguemnt    | *
 * |  0.98   | 12/06/04 | Check for serverWindow is not null before accessing it.       | *
 * |  0.98   | 12/13/04 | Make all new players pay the big blind their first game.      | *
 * |  0.99   | 03/24/05 | If there are only 2 players, the dealer is the small blind    | *
 * |         |          | according to the rules...so I changed that in here.           | *
 * |  0.99   | 03/26/05 | Fixed the all-in problem so that the winner doesn't auto-     | *
 * |         |          | matically win the entire pot.  Any money that the winner      | *
 * |         |          | can't win is put into the sidePots list.                      | *
 * |  0.99   | 03/26/05 | Double the blinds if required and that option is selected.    | *
 * |  0.99   | 05/10/05 | Now the minimum bet affects all game types.                   | *
 * |  0.99   | 05/10/05 | Minimum raise amount allowed is the amount of the initial bet | *
 * |  0.99   | 05/13/05 | Fixed the display of the player's bankroll on the table.      | *
 * |  0.99   | 05/16/05 | Added the getMinimumRaise function for the AI.                | *
 * |  0.99   | 05/16/05 | Fixed a problem where IndexOutOfBounds exception would happen | *
 * |         |          | because dealerIndex was wrong due to removing players.        | *
 * |  0.99   | 05/17/05 | For structured betting games, modify PLAYER TURN message to   | *
 * |         |          | be able to disable some buttons as appropriate.               | *
 * |  0.99   | 05/18/05 | Added minValueToShow flag so that the cards of side pot       | *
 * |         |          | winners will be shown at the showdown.                        | *
 * |  0.99   | 05/19/05 | Move showCards into this class and each game class will have  | *
 * |         |          | a showPlayer function instead.                                | *
 * |  0.99   | 05/19/05 | Show cards of side pot winners as well.                       | *
 * |  0.99   | 05/23/05 | Added requiresInteraction flag so AI's know.                  | *
 * |  1.00   | 06/06/05 | Added logging.                                                | *
 * |  1.00   | 08/19/05 | Added lots of comments so that this code might be readable by | *
 * |         |          | somebody else to create new game types.                       | *
 * |  1.00   | 08/19/05 | Allow both antes and blinds to be selected for the game.      | *
 * |  1.00   | 09/02/05 | Add variables for smallIndex and bigIndex                     | *
 * |  1.00   | 07/13/07 | Prepare for open source.  Header/comments/package/etc...      | *
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

//
// Must import the ai package so the game can handle AI actions
// Also must import ArrayList because it is used everywhere.
//
import net.sourceforge.pokerapp.ai.*;
import java.util.ArrayList;

/****************************************************
 * PokerGame is the generic poker game. All defined games must be an extension
 * of this class. In order to play a game, a class which extends this class must
 * be written and several important functions defined.
 * 
 * @author Dan Puperi
 * @version 1.00
 * 
 **/
public abstract class PokerGame {

	/**
	 * Maximum number of players allowed in the game
	 **/
	public final static int MAX_PLAYERS = 8;

	/**
	 * The StartPoker class that this game belongs to
	 **/
	protected StartPoker theApp;

	/**
	 * Whether or not this game started with an error
	 **/
	protected boolean gameError;

	/**
	 * The current high bet in this game
	 **/
	protected PokerMoney currBet;

	/**
	 * Initial bet for this round. Sets the minimum raise and re-raise amount.
	 **/
	protected PokerMoney initialBet;

	/**
	 * The player who made the high bet
	 **/
	protected Player highBettor;

	/**
	 * The current action number describing how the game progresses
	 **/
	protected int actionNum;

	/**
	 * Current player index in the server player list
	 **/
	public int currPlayerIndex;

	/**
	 * The seat number the current player is sitting in
	 **/
	public int currSeatIndex;

	/**
	 * Maximum number of actions in this specific game type
	 **/
	protected int maxActionNum;

	/**
	 * If everybody left is all in
	 **/
	protected boolean allInShowDown;

	/**
	 * The value of each player's best possible hand
	 **/
	protected float playerHandValues[];

	/**
	 * Skips showing players cards - used if there was an irregularity in the
	 * game.
	 **/
	protected boolean skipShow;

	/**
	 * The best possible hand based on what cards are showing.
	 **/
	protected float bestPossible;

	/**
	 * List of side pots because of players going all in.
	 **/
	public ArrayList sidePots;

	/**
	 * The minumum hand score that will be shown (needed to show side pot
	 * winners)
	 **/
	protected float minValueToShow;

	/**
	 * Whether this game requires any special interaction.
	 **/
	public boolean requiresInteraction;

	/**
	 * Index of the big blind player
	 **/
	public int bigIndex;

	/**
	 * Index of the small blind player
	 **/
	public int smallIndex;

	/***********************
	 * Defines how to deal the initial card. Must be defined for all classes
	 * extending this class
	 **/
	protected abstract void deal();

	/***********************
	 * Defines what happens when the left mouse button is clicked. Must be
	 * defined for all classes extending this class
	 * 
	 * @param name
	 *            The player's name who clicked the mouse button
	 * @param x
	 *            The x location of the mouse button click
	 * @param y
	 *            The y location of the mouse button click
	 * 
	 **/
	protected abstract void mouseClick(String name, int x, int y);

	/***********************
	 * Defines which player bets first. Must be defined for all classes
	 * extending this class
	 * 
	 * @return The index of the player who is betting first
	 * 
	 **/
	protected abstract int firstToBet();

	/***********************
	 * Defines how the game is played. Must be defined for all classes extending
	 * this class
	 **/
	protected abstract void nextAction();

	/***********************
	 * Defines what happens at the showdown. Must be defined for all classes
	 * extending this class
	 * 
	 * @param p
	 *            The player who is showing their cards
	 * @param b
	 *            If the player has to show their cards
	 * 
	 **/
	protected abstract void showPlayer(Player p, boolean b);

	/***********************
	 * Defines what is the best 5 card hand out of the passed argument. Must be
	 * defined for all classes extending this class
	 * 
	 * @param h
	 *            The hand to figure out the best 5 card hand from
	 * @return The float value of the best hand
	 * 
	 **/
	public abstract float bestHand(Hand h);

	/***********************
	 * Defines which is the best possible hand available - based on known cards.
	 * Must be defined for all classes extending this class. This is to help out
	 * the AI - so if no AIs are designed to play the given game, this function
	 * can just be empty.
	 * 
	 * @return The float value of the best possible hand
	 * 
	 **/
	protected abstract float calcBestPossible();

	/***************************
	 * A PokerGame is created by providing the StartPoker class to which the
	 * game belongs, the game name, and whether this game is responsible for
	 * starting itself.
	 * 
	 * @param a
	 *            The StartPoker class to which this game belongs
	 * @param gameName
	 *            The name of the game
	 * @param selfInit
	 *            Whether this game type is capable of starting itself.
	 * 
	 **/
	public PokerGame(StartPoker a, String gameName, boolean selfInit) {
		theApp = a;
		theApp.log("Constructing a new PokerGame", 3);
		theApp.timeCounter = 0;
		if (theApp.getPlayerList().size() < 2) {
			if (theApp.getServerWindow() != null) {
				theApp.getServerWindow().sendMessage(
						"Need more people to play!");
			} else {
				System.out.println("Need more people to play!");
			}
			theApp.log("Need more people to play!", 1);
			theApp.broadcastMessage("MESSAGE &Need more people to play!");
			theApp.broadcastMessage("DISABLE BUTTONS");
			gameError = true;
			return;
		} else {
			for (int i = 0; i < theApp.getPlayerList().size(); i++) {
				((Player) theApp.getPlayerList().get(i)).newGame();
			}
		}

		if ((theApp.autoDoubleTime)
				&& (((System.currentTimeMillis() / 1000) - theApp.startingTime) >= theApp.timeToDouble)) {
			if (theApp.blinds) {
				theApp.smallBlind = new PokerMoney(
						2.0f * theApp.smallBlind.amount());
				theApp.minimumBet = new PokerMoney(
						2.0f * theApp.minimumBet.amount());
			}
			if (theApp.antes) {
				theApp.ante = new PokerMoney(2.0f * theApp.ante.amount());
			}
			theApp.startingTime = System.currentTimeMillis() / 1000;
			theApp.getServerWindow().menuRedisplay();
		}

		if ((theApp.autoDoubleHands)
				&& (theApp.numHandsPlayed >= theApp.handsToDouble)) {
			if (theApp.blinds) {
				theApp.smallBlind = new PokerMoney(
						2.0f * theApp.smallBlind.amount());
				theApp.minimumBet = new PokerMoney(
						2.0f * theApp.minimumBet.amount());
			}
			if (theApp.antes) {
				theApp.ante = new PokerMoney(2.0f * theApp.ante.amount());
			}
			theApp.numHandsPlayed = 0;
			theApp.getServerWindow().menuRedisplay();
		}

		theApp.numHandsPlayed++;
		gameError = false;
		allInShowDown = false;
		skipShow = false;
		actionNum = 0;
		bestPossible = 0.0f;
		minValueToShow = 0.0f;
		sidePots = new ArrayList();
		theApp.initGame(gameName);
		currBet = new PokerMoney(0.0f);
		initialBet = new PokerMoney(0.0f);
		currPlayerIndex = 0;
		smallIndex = -1;
		bigIndex = -1;
		requiresInteraction = false;
		currSeatIndex = ((Player) theApp.getPlayerList().get(currPlayerIndex)).seat;
		if ((theApp.lastDealer == null) || (theApp.dealer == null)) {
			theApp.dealer = (Player) theApp.getPlayerList()
					.get(currPlayerIndex);
			theApp.dealerIndex = 0;
			theApp.lastDealer = theApp.dealer;
		}
		if (theApp.antes) {
			float an = ante();
			if (an <= 0.0f) {
				gameError = true;
				return;
			}
			theApp.getPot().add(an);
		}
		if (theApp.blinds) {
			float bl = blind();
			if (bl <= 0.0f) {
				gameError = true;
				return;
			}
			theApp.getPot().add(bl);
			theApp.firstBlindGamePlayed = true;
		}
		actionNum++;

		if (!selfInit) {
			currPlayerIndex = firstToBet();
			if ((currPlayerIndex < 0) || (currPlayerIndex >= MAX_PLAYERS)) {
				gameError = true;
				return;
			}
			currSeatIndex = ((Player) theApp.getPlayerList().get(
					currPlayerIndex)).seat;
			if (!theApp.blinds) {
				highBettor = (Player) theApp.getPlayerList().get(
						currPlayerIndex);
			}
			if (highBettor != null) {
				theApp.updateMoneyLine(currBet, highBettor.getName());
			} else {
				gameError = true;
				return;
			}
		}
	}

	/**********************
	 * ante() function is called to take the ante amount from all the players
	 * and increase the pot by the correct amount
	 * 
	 * @return The amount of the ante collected
	 * 
	 **/
	public float ante() {
		theApp.log("PokerGame.ante()", 3);
		float r = 0.0f;

		if (!checkForCash(theApp.ante.amount())) {
			return r;
		}

		for (int i = 0; i < theApp.getPlayerList().size(); i++) {
			((Player) theApp.getPlayerList().get(i)).subtract(theApp.ante
					.amount());
			((Player) theApp.getPlayerList().get(i)).in = true;
			r = r + theApp.ante.amount();
			theApp.broadcastMessage("PLAYER CALL  &"
					+ ((Player) theApp.getPlayerList().get(i)).getName()
					+ "& &"
					+ ((Player) theApp.getPlayerList().get(i)).getBankroll()
							.amount());
		}
		return r;
	}

	/**********************
	 * blind() function is similar to the ante function in that it takes the
	 * blinds and puts that amount in the pot
	 * 
	 * @return Thr amount of blinds collected.
	 * 
	 **/
	public float blind() {
		theApp.log("PokerGame.blind()", 3);
		float r = 0.0f;

		if (!checkForCash(theApp.smallBlind.amount() * 2.0f)) {
			return r;
		}

		int dealerSeat = ((Player) theApp.getPlayerList().get(
				theApp.dealerIndex)).seat;
		int smallSeat = 0;
		if (theApp.getPlayerList().size() == 2) {
			smallSeat = dealerSeat;
		} else {
			smallSeat = theApp.nextSeat(dealerSeat);
		}

		int bigSeat = theApp.nextSeat(smallSeat);
		smallIndex = theApp.getPlayerInSeat(smallSeat);
		bigIndex = theApp.getPlayerInSeat(bigSeat);

		Player smallPlayer = (Player) theApp.getPlayerList().get(smallIndex);
		Player bigPlayer = (Player) theApp.getPlayerList().get(bigIndex);

		((Player) theApp.getPlayerList().get(smallIndex))
				.subtract(theApp.smallBlind.amount());
		((Player) theApp.getPlayerList().get(smallIndex)).in = true;
		((Player) theApp.getPlayerList().get(smallIndex))
				.setPrevBet(theApp.smallBlind.amount());
		r = r + theApp.smallBlind.amount();
		theApp.broadcastMessage("PLAYER CALL  &" + smallPlayer.getName()
				+ "&Small Blind = " + theApp.smallBlind + "&"
				+ smallPlayer.getBankroll().amount());

		((Player) theApp.getPlayerList().get(bigIndex))
				.subtract(2.0f * theApp.smallBlind.amount());
		((Player) theApp.getPlayerList().get(bigIndex)).in = true;
		((Player) theApp.getPlayerList().get(bigIndex))
				.setPrevBet(2.0f * theApp.smallBlind.amount());
		r = r + 2.0f * theApp.smallBlind.amount();
		highBettor = bigPlayer;
		currBet = new PokerMoney(2.0f * theApp.smallBlind.amount());
		theApp.broadcastMessage("PLAYER CALL  &" + bigPlayer.getName()
				+ "&Big Blind = " + currBet + "&"
				+ bigPlayer.getBankroll().amount());
		for (int i = 0; i < theApp.getPlayerList().size(); i++) {
			((Player) theApp.getPlayerList().get(i)).in = true;
			//
			// Check for players who have just joined the game. If they are new
			// to the table, they must pay the big blind.
			//
			if (theApp.firstBlindGamePlayed) {
				if (!theApp.playedInLastGame.contains((String) ((Player) theApp
						.getPlayerList().get(i)).getName())) {
					if (i == smallIndex) {
						//
						// If the player is already the small blind, they only
						// have to pay another small blind amount
						//
						((Player) theApp.getPlayerList().get(smallIndex))
								.subtract(theApp.smallBlind.amount());
						((Player) theApp.getPlayerList().get(smallIndex))
								.setPrevBet(2.0f * theApp.smallBlind.amount());
						r = r + theApp.smallBlind.amount();
						theApp.broadcastMessage("PLAYER CALL  &"
								+ smallPlayer.getName() + "&Big Blind = "
								+ currBet + "&"
								+ smallPlayer.getBankroll().amount());
					} else if (i == bigIndex) {
						//
						// No need to do anything if they are already in the big
						// blind position.
						//
					} else {
						((Player) theApp.getPlayerList().get(i))
								.subtract(2.0f * theApp.smallBlind.amount());
						((Player) theApp.getPlayerList().get(i)).in = true;
						((Player) theApp.getPlayerList().get(i))
								.setPrevBet(2.0f * theApp.smallBlind.amount());
						r = r + 2.0f * theApp.smallBlind.amount();
						theApp.broadcastMessage("PLAYER CALL  &"
								+ ((Player) theApp.getPlayerList().get(i))
										.getName()
								+ "&Big Blind = "
								+ currBet
								+ "&"
								+ ((Player) theApp.getPlayerList().get(i))
										.getBankroll().amount());
					}
				}
			}
		}
		initialBet = new PokerMoney(2.0f * theApp.smallBlind.amount());
		return r;
	}

	/**********************
	 * checkForCash() determines if players have enough money to continue
	 * playing - if they don't they are kicked out. If only 1 player is left in
	 * the game, return false so that the ante() or blind() function knows a
	 * game will not be happening.
	 * 
	 * @param amount
	 *            The amount of money to check each player for
	 * @return True if there are enough players to play the game.
	 * 
	 **/
	protected boolean checkForCash(float amount) {
		theApp.log("PokerGame.checkForCash( " + amount + " )", 3);
		ArrayList playersToRemove = new ArrayList();
		boolean needNewDealer = false;

		for (int i = 0; i < theApp.getPlayerList().size(); i++) {
			Player P = (Player) theApp.getPlayerList().get(i);
			if (P.getBankroll().amount() < amount) {
				if (theApp.getServerWindow() != null) {
					theApp.getServerWindow().sendMessage(
							P.getName()
									+ " does not have enough money to play!");
				} else {
					System.out.println(P.getName()
							+ " does not have enough money to play!");
				}
				theApp.log("" + P.getName()
						+ " does not have enough money to play", 1);
				theApp.broadcastMessage("MESSAGE &" + P.getName()
						+ " does not have enough money to play!");
				String message = PokerProtocol.makeString(new String(
						"MESSAGE &You don't have enough money.  Good Bye."));
				((PokerMultiServerThread) theApp.getServerThreadList().get(
						theApp.serverThreadFromPlayerIndex(i))).getStreamOut()
						.println(message);
				playersToRemove.add(P.getName());
			}
		}
		for (int i = 0; i < playersToRemove.size(); i++) {
			for (int j = 0; j < theApp.getAIApps().size(); j++) {
				if (((String) playersToRemove.get(i)).equals(((AIApp) theApp
						.getAIApps().get(j)).getThisPlayer().getName())) {
					int st = theApp.serverThreadFromPlayerIndex(theApp
							.playerIndex((String) playersToRemove.get(i)));
					if (st >= 0) {
						((PokerMultiServerThread) theApp.getServerThreadList()
								.get(st)).stopRunning();
						theApp.getServerThreadList().remove(st);
						theApp.getServerThreadList().trimToSize();
					}
					((AIApp) theApp.getAIApps().get(j)).disconnectSocket();
				}
			}
			theApp.dropPlayer((String) playersToRemove.get(i));
			theApp.broadcastMessage("PLAYER REMOVE  &"
					+ (String) playersToRemove.get(i));
			if (((String) playersToRemove.get(i)).equals(theApp.dealer
					.getName())) {
				needNewDealer = true;
			}
		}
		theApp.playersRemoved = new ArrayList();
		if (needNewDealer) {
			Player prevDealer = theApp.lastDealer;
			theApp.nextDealer();
			theApp.lastDealer = prevDealer;
			needNewDealer = false;
		}
		if (playersToRemove.size() > 0) {
			int dind = theApp.playerIndex(theApp.dealer.getName());
			if (dind != theApp.dealerIndex) {
				theApp.dealerIndex = dind;
				theApp.dealer = (Player) theApp.getPlayerList().get(dind);
				theApp.broadcastMessage("DEALER  &" + theApp.dealer.getName());
				theApp.broadcastMessage("REFRESH");
			}
		}
		playersToRemove = new ArrayList();
		if (theApp.getPlayerList().size() < 2) {
			if (theApp.getServerWindow() != null) {
				theApp.getServerWindow().sendMessage(
						"Need more people to play!");
			} else {
				System.out.println("Need more people to play!");
			}
			theApp.log("Need more people to play", 1);
			theApp.broadcastMessage("MESSAGE &Need more people to play!");
			return false;
		}
		return true;
	}

	/**********************
	 * firstToBet_HoldEm() is a generic function that can be used for HoldEm
	 * type games defining which player bets first
	 * 
	 * @return The integer player index of the player who will be betting first
	 * 
	 **/
	protected int firstToBet_HoldEm() {
		theApp.log("PokerGame.firstToBet_HoldEm()", 3);
		int dealerSeat = ((Player) theApp.getPlayerList().get(
				theApp.dealerIndex)).seat;
		if ((theApp.blinds) && (actionNum == 1)) {
			if (dealerSeat < 0) {
				return -9;
			}
			int prevSeat = theApp.nextSeat(dealerSeat);
			prevSeat = theApp.nextSeat(prevSeat);
			if (theApp.getPlayerList().size() == 2) {
				return theApp.getPlayerInSeat(dealerSeat);
			} else {
				return theApp.getPlayerInSeat(theApp.nextSeat(prevSeat));
			}
		} else {
			if (dealerSeat < 0) {
				return -9;
			}
			int prevSeat = theApp.nextSeat(dealerSeat);
			int pindex = theApp.getPlayerInSeat(prevSeat);
			if ((pindex >= 0) && (pindex < theApp.getPlayerList().size())) {
				while (!((Player) theApp.getPlayerList().get(pindex)).in) {
					prevSeat = theApp.nextSeat(prevSeat);
					pindex = theApp.getPlayerInSeat(prevSeat);
					if ((pindex < 0)
							|| (pindex >= theApp.getPlayerList().size()))
						return -9;
				}
				return pindex;
			}
			return -9;
		}
	}

	/**********************
	 * firstToBet_Stud() is a generic function that can be used for Stud type
	 * games defining which player bets first
	 * 
	 * @return The integer player index of the player who will be betting first
	 * 
	 **/
	protected int firstToBet_Stud() {
		theApp.log("PokerGame.firstToBet_Stud()", 3);
		float highHand = 0.0f;
		int highPlayer = 0;
		HandEvaluator he = new HandEvaluator();
		if (theApp.dealer == null) {
			return -9;
		}
		int dealerSeat = ((Player) theApp.getPlayerList().get(
				theApp.dealerIndex)).seat;
		if (dealerSeat < 0) {
			return -9;
		}
		if (actionNum == 1) {
			if (theApp.blinds) {
				int prevSeat = theApp.nextSeat(dealerSeat);
				prevSeat = theApp.nextSeat(prevSeat);
				if (theApp.getPlayerList().size() == 2) {
					return theApp.getPlayerInSeat(dealerSeat);
				} else {
					return theApp.getPlayerInSeat(theApp.nextSeat(prevSeat));
				}
			} else {
				for (int i = 0; i < theApp.getPlayerList().size(); i++) {
					Player p = (Player) theApp.getPlayerList().get(i);
					if (p.in) {
						if (he.rankHand(p.getHand().getUpCard(0)) > highHand) {
							highHand = he.rankHand(p.getHand().getUpCard(0));
							highPlayer = i;
						}
					}
				}
				return highPlayer;
			}
		} else {
			for (int i = 0; i < theApp.getPlayerList().size(); i++) {
				Player p = (Player) theApp.getPlayerList().get(i);
				if (p.in) {
					Hand h = new Hand();
					for (int j = 0; j < p.getHand().getNumUp(); j++) {
						h.addUpCard(p.getHand().getUpCard(j));
					}
					float r = he.rankHand(h);
					if (r > highHand) {
						highHand = r;
						highPlayer = i;
					}
				}
			}
			return highPlayer;
		}
	}

	/**********************
	 * bet() is called whenever a player bets. This is a hugely complicated
	 * function, but it works pretty well I think.
	 **/
	protected void bet() {
		Player currPlayer = (Player) theApp.getPlayerList()
				.get(currPlayerIndex);
		theApp.log("PokerGame.bet() - " + currPlayer.getName(), 3);
		float betS = currPlayer.getBet().amount();
		float betP = currPlayer.getPrevBet().amount();
		if (currPlayer.getBankroll().compareTo(new PokerMoney()) == 1) {
			currPlayer.allin = false;
		}
		if ((currPlayer.getBankroll().amount() - betS) < 0.009f) {
			currPlayer.allin = true;
		}
		if (((betP + betS) < currBet.amount()) && (!currPlayer.allin)) {
			if ((currBet.amount() - betP) >= currPlayer.getBankroll().amount()) {
				String message = PokerProtocol.makeString(new String(
						"MESSAGE &You must go all in to match the bet."));
				theApp.log("" + currPlayer.getName()
						+ " must go all in to match the bet", 1);
				((PokerMultiServerThread) theApp.getServerThreadList().get(
						theApp.serverThreadFromPlayerIndex(currPlayerIndex)))
						.getStreamOut().println(message);
			} else {
				String message = PokerProtocol.makeString(new String(
						"MESSAGE &You must bet at least "
								+ new PokerMoney(currBet.amount() - betP)
								+ " to call."));
				theApp.log(
						"" + currPlayer.getName() + " must bet at least "
								+ new PokerMoney(currBet.amount() - betP)
								+ " to call.", 1);
				((PokerMultiServerThread) theApp.getServerThreadList().get(
						theApp.serverThreadFromPlayerIndex(currPlayerIndex)))
						.getStreamOut().println(message);
			}
		} else {
			boolean potOK = true;
			boolean betOK = false;
			PokerMoney raise = new PokerMoney(betS + betP - currBet.amount());
			String m = new String();
			if ((betS + betP) > currBet.amount()) {
				if (currPlayer.allin) {
					m = currPlayer.getName() + " went All In!  ";
					theApp.broadcastMessage("PLAYER CALL  &"
							+ currPlayer.getName() + "&All In&0.0");
					if (initialBet.amount() == 0.0f) {
						initialBet = new PokerMoney(raise.amount());
					}
					betOK = true;
				} else if (currBet.amount() == 0.0) {
					if (raise.compareTo(theApp.minimumBet) == -1) {
						String message = PokerProtocol.makeString(new String(
								"MESSAGE &You must bet the minimum bet of at least "
										+ theApp.minimumBet));
						theApp.log("" + currPlayer.getName()
								+ " must bet the minimum bet of at least "
								+ theApp.minimumBet, 1);
						((PokerMultiServerThread) theApp
								.getServerThreadList()
								.get(theApp
										.serverThreadFromPlayerIndex(currPlayerIndex)))
								.getStreamOut().println(message);
					} else {
						m = currPlayer.getName() + " bet " + raise + ".  ";
						theApp.broadcastMessage("PLAYER CALL  &"
								+ currPlayer.getName() + "&Bet " + raise + "&"
								+ (currPlayer.getBankroll().amount() - betS));
						initialBet = new PokerMoney(raise.amount());
						betOK = true;
					}
				} else {
					if (raise.amount() < initialBet.amount()) {
						String message = PokerProtocol.makeString(new String(
								"MESSAGE &If you wish to raise, you must raise by at least "
										+ initialBet));
						theApp.log("" + currPlayer.getName()
								+ " must raise at least " + initialBet, 1);
						((PokerMultiServerThread) theApp
								.getServerThreadList()
								.get(theApp
										.serverThreadFromPlayerIndex(currPlayerIndex)))
								.getStreamOut().println(message);
					} else {
						m = currPlayer.getName() + " raised " + raise + ".  ";
						theApp.broadcastMessage("PLAYER CALL  &"
								+ currPlayer.getName() + "&Raised " + raise
								+ "&"
								+ (currPlayer.getBankroll().amount() - betS));
						betOK = true;
					}
				}

				if (betOK) {
					currBet = new PokerMoney(betS + betP);
					highBettor = currPlayer;
					currPlayer.subtract(betS);
					theApp.getPot().add(betS);
					currPlayer.betInGame();
					currPlayer.potOK = true;
					if (currPlayer.getBankroll().compareTo(
							new PokerMoney(0.010f)) == -1) {
						currPlayer.allin = true;
					}
					theApp.getPlayerList().set(currPlayerIndex, currPlayer);
					//
					// New side pot may need to be created if this player bet
					// more than one or more other player's have in their
					// bankroll.
					//
					boolean newSidePot = false;
					for (int i = 0; i < theApp.getPlayerList().size(); i++) {
						if ((((Player) theApp.getPlayerList().get(i)).in)
								&& ((((Player) theApp.getPlayerList().get(i))
										.getBankroll().amount() + ((Player) theApp
										.getPlayerList().get(i)).getPrevBet()
										.amount()) < (betS + betP))) {
							newSidePot = true;
						}
					}
					boolean sidepot_just_created = false;
					if (newSidePot) {
						sidepot_just_created = createSidePots(betS + betP);
					}
					if (!sidepot_just_created) {
						//
						// Add raise money to latest side pot if one was not
						// just created.
						//
						int lastPot = sidePots.size() - 1;
						if (lastPot >= 0) {
							ArrayList incLst = ((SidePot) sidePots.get(lastPot))
									.getIncluded();
							float tc = ((SidePot) sidePots.get(lastPot)).toCall[currPlayerIndex]
									.amount();
							if (incLst.contains(currPlayer.getName())) {
								((SidePot) sidePots.get(lastPot)).addMoney(tc
										+ raise.amount());
								for (int i = 0; i < theApp.getPlayerList()
										.size(); i++) {
									((SidePot) sidePots.get(lastPot)).toCall[i]
											.add(raise.amount());
								}
								((SidePot) sidePots.get(lastPot)).toCall[currPlayerIndex] = new PokerMoney();
								theApp.log("Side pot #" + lastPot
										+ " - side pot not just created", 3);
								theApp.log(
										"  Adding "
												+ (tc + raise.amount())
												+ ";  Pot = "
												+ ((SidePot) sidePots
														.get(lastPot)).getPot(),
										3);
								theApp.log("    toCall :", 3);
								for (int i = 0; i < theApp.getPlayerList()
										.size(); i++) {
									theApp.log(
											"      "
													+ ((Player) theApp
															.getPlayerList()
															.get(i)).getName()
													+ " :  "
													+ ((SidePot) sidePots
															.get(lastPot)).toCall[i],
											3);
								}
							}
							//
							// Must loop through previous pots to make sure
							// toCall is satisfied.
							//
							for (int i = 0; i < lastPot; i++) {
								ArrayList incLst2 = ((SidePot) sidePots.get(i))
										.getIncluded();
								float tc2 = ((SidePot) sidePots.get(i)).toCall[currPlayerIndex]
										.amount();
								if (incLst2.contains(currPlayer.getName())) {
									((SidePot) sidePots.get(i)).addMoney(tc2);
									((SidePot) sidePots.get(i)).toCall[currPlayerIndex] = new PokerMoney();
									theApp.log(
											"Side pot #"
													+ i
													+ " - side pot not just created - make sure toCall for previous pots is satisfied",
											3);
									theApp.log(
											"  Adding "
													+ (tc2)
													+ ";  Pot = "
													+ ((SidePot) sidePots
															.get(i)).getPot(),
											3);
									theApp.log("    toCall = ", 3);
									for (int j = 0; j < theApp.getPlayerList()
											.size(); j++) {
										theApp.log(
												"      "
														+ ((Player) theApp
																.getPlayerList()
																.get(j))
																.getName()
														+ " :  "
														+ ((SidePot) sidePots
																.get(i)).toCall[j],
												3);
									}
								}
							}
						}
					} else {
						int newPot = sidePots.size() - 1;
						float amount = betS
								- ((SidePot) sidePots.get(newPot)).getPot()
										.amount();
						for (int i = 0; i < sidePots.size() - 1; i++) {
							SidePot sp = (SidePot) sidePots.get(i);
							ArrayList incLst = sp.getIncluded();
							if ((incLst.contains(currPlayer.getName()))
									&& (sp.toCall[currPlayerIndex].amount() > 0.0f)) {
								float amt = 0.0f;
								if (i == sidePots.size() - 2) {
									float tc = sp.toCall[currPlayerIndex]
											.amount();
									amt = tc
											+ raise.amount()
											- ((SidePot) sidePots.get(newPot))
													.getPot().amount();
									;
									for (int k = 0; k < theApp.getPlayerList()
											.size(); k++) {
										((SidePot) sidePots.get(i)).toCall[k]
												.add(raise.amount()
														- ((SidePot) sidePots
																.get(newPot))
																.getPot()
																.amount());
									}
								} else if (amount > sp.toCall[currPlayerIndex]
										.amount()) {
									amt = sp.toCall[currPlayerIndex].amount();
								} else {
									amt = amount;
								}
								((SidePot) sidePots.get(i)).addMoney(amt);
								((SidePot) sidePots.get(i)).toCall[currPlayerIndex] = new PokerMoney();
								amount = amount - amt;
								theApp.log(
										"Side pot #"
												+ i
												+ " - side pot just created - make sure toCall is satisfied.",
										3);
								theApp.log("  Adding " + amt + ";  Pot = "
										+ ((SidePot) sidePots.get(i)).getPot(),
										3);
								theApp.log("    toCall = ", 3);
								for (int j = 0; j < theApp.getPlayerList()
										.size(); j++) {
									theApp.log(
											"      "
													+ ((Player) theApp
															.getPlayerList()
															.get(j)).getName()
													+ " :  "
													+ ((SidePot) sidePots
															.get(i)).toCall[j],
											3);
								}
							}
						}
					}
					//
					// Make sure all the players have their potOK flag set to
					// false if required.
					//
					for (int i = 0; i < theApp.getPlayerList().size(); i++) {
						((Player) theApp.getPlayerList().get(i)).potOK = false;
						if (((Player) theApp.getPlayerList().get(i)).allin) {
							((Player) theApp.getPlayerList().get(i)).potOK = true;
						}
						for (int j = 0; j < theApp.leavingPlayerList.size(); j++) {
							if (((String) theApp.leavingPlayerList.get(j))
									.equals(((Player) theApp.getPlayerList()
											.get(i)).getName())) {
								((Player) theApp.getPlayerList().get(i)).potOK = true;
							}
						}
					}
					((Player) theApp.getPlayerList().get(currPlayerIndex)).potOK = true;
					for (int i = 0; i < theApp.getPlayerList().size(); i++) {
						if ((((Player) theApp.getPlayerList().get(i)).in)
								&& (!((Player) theApp.getPlayerList().get(i)).potOK)) {
							potOK = false;
						}
					}
				}
			} else {
				currPlayer.subtract(betS);
				theApp.getPot().add(betS);
				currPlayer.betInGame();
				currPlayer.potOK = true;
				if (currPlayer.getBankroll().compareTo(new PokerMoney(0.010f)) == -1) {
					currPlayer.allin = true;
				}
				theApp.getPlayerList().set(currPlayerIndex, currPlayer);
				for (int i = 0; i < theApp.getPlayerList().size(); i++) {
					if ((((Player) theApp.getPlayerList().get(i)).in)
							&& (!((Player) theApp.getPlayerList().get(i)).potOK)
							&& (!((Player) theApp.getPlayerList().get(i)).allin)) {
						potOK = false;
					}
				}
				//
				// Add money to side pots as required.
				//
				float amount = betS;
				for (int i = 0; i < sidePots.size(); i++) {
					SidePot sp = (SidePot) sidePots.get(i);
					ArrayList incLst = sp.getIncluded();
					if ((incLst.contains(currPlayer.getName()))
							&& (sp.toCall[currPlayerIndex].amount() > 0.0f)) {
						float amt = 0.0f;
						if (amount > sp.toCall[currPlayerIndex].amount()) {
							amt = sp.toCall[currPlayerIndex].amount();
						} else {
							amt = amount;
						}
						((SidePot) sidePots.get(i)).addMoney(amt);
						((SidePot) sidePots.get(i)).toCall[currPlayerIndex]
								.subtract(amt);
						amount = amount - amt;
						theApp.log("Side pot #" + i
								+ " - add money to side pots as required", 3);
						theApp.log("  Adding " + amt + ";  Pot = "
								+ ((SidePot) sidePots.get(i)).getPot(), 3);
						theApp.log("    toCall = ", 3);
						for (int j = 0; j < theApp.getPlayerList().size(); j++) {
							theApp.log(
									"      "
											+ ((Player) theApp.getPlayerList()
													.get(j)).getName()
											+ " :  "
											+ ((SidePot) sidePots.get(i)).toCall[j],
									3);
						}
					}
				}
				if (currPlayer.allin) {
					m = currPlayer.getName() + " went All In!  ";
					theApp.broadcastMessage("PLAYER CALL  &"
							+ currPlayer.getName() + "&All In&0.0");
					betOK = true;
				} else if (currBet.amount() == 0.0) {
					m = currPlayer.getName() + " checked.  ";
					theApp.broadcastMessage("PLAYER CALL  &"
							+ currPlayer.getName() + "&Check&"
							+ currPlayer.getBankroll().amount());
					betOK = true;
				} else {
					m = currPlayer.getName() + " called "
							+ highBettor.getName() + "'s " + currBet
							+ " bet.  ";
					theApp.broadcastMessage("PLAYER CALL  &"
							+ currPlayer.getName() + "&Call&"
							+ currPlayer.getBankroll().amount());
					betOK = true;
				}
			}

			if (betOK && !potOK) {
				int prevSeat = currPlayer.seat;
				boolean foundNext = false;
				while (!foundNext) {
					prevSeat = theApp.nextSeat(prevSeat);
					boolean waitingToLeave = false;
					for (int j = 0; j < theApp.leavingPlayerList.size(); j++) {
						if (((String) theApp.leavingPlayerList.get(j))
								.equals(((Player) theApp.getPlayerList().get(
										theApp.getPlayerInSeat(prevSeat)))
										.getName())) {
							waitingToLeave = true;
						}
					}
					if ((((Player) theApp.getPlayerList().get(
							theApp.getPlayerInSeat(prevSeat))).in)
							&& (!waitingToLeave)
							&& !(((Player) theApp.getPlayerList().get(
									theApp.getPlayerInSeat(prevSeat))).allin)) {
						foundNext = true;
						currPlayerIndex = theApp.getPlayerInSeat(prevSeat);
						currSeatIndex = prevSeat;
					}
				}
				if ((actionNum + 1) <= maxActionNum) {
					theApp.broadcastMessage("MESSAGE  &"
							+ m
							+ ((Player) theApp.getPlayerList().get(
									currPlayerIndex)).getName() + "'s bet.");
					if (!(theApp.noLimit || theApp.potLimit || theApp.betLimit)
							&& (currBet.amount() > 0.009f)) {
						theApp.broadcastMessage("PLAYER TURN &"
								+ ((Player) theApp.getPlayerList().get(
										currPlayerIndex)).getName()
								+ "&enable raise");
					} else {
						theApp.broadcastMessage("PLAYER TURN &"
								+ ((Player) theApp.getPlayerList().get(
										currPlayerIndex)).getName());
					}
				} else {
					theApp.broadcastMessage("PLAYER TURN &   ");
				}
				theApp.updateMoneyLine(currBet, highBettor.getName());
			}

			if (betOK) {
				int allins = 0, ins = 0;
				for (int i = 0; i < theApp.getPlayerList().size(); i++) {
					if (((Player) theApp.getPlayerList().get(i)).allin) {
						allins++;
					}
					if (((Player) theApp.getPlayerList().get(i)).in) {
						ins++;
					}
				}
				if (allins >= (ins - 1)) {
					if (potOK) {
						allInShowDown = true;
						while (actionNum < maxActionNum) {
							endAction();
						}
					}
				} else {
					if (potOK)
						endAction();
				}
			}
		}
	}

	/**********************
	 * createSidePots() is called after somebody raises. If necessary, it will
	 * create side pots because some player(s) in the game cannot match the bet.
	 * 
	 * @param bet
	 *            The bet value which is needed to stay in the game.
	 * @return Whether of not the side pots were successfully created.
	 * 
	 **/
	protected boolean createSidePots(float bet) {
		theApp.log("PokerGame.createSidePots( " + bet + " )", 3);

		boolean ret = false;
		//
		// First need to order players in a list of how much they have relative
		// to how much they owe.
		//
		String[] names = new String[theApp.getPlayerList().size()];
		float[] amounts = new float[theApp.getPlayerList().size()];
		int count = 0;
		for (int i = 0; i < theApp.getPlayerList().size(); i++) {
			Player p = (Player) theApp.getPlayerList().get(i);
			if (p.in) {
				names[count] = new String(p.getName());
				amounts[count] = p.getBankroll().amount()
						+ p.getPrevBet().amount() - bet;
				count++;
			}
		}
		String tempN = new String();
		float tempA = 0.0f;
		for (int i = 0; i < count; i++) {
			for (int j = i; j < count; j++) {
				if (amounts[j] < amounts[i]) {
					tempN = names[i];
					tempA = amounts[i];
					names[i] = names[j];
					amounts[i] = amounts[j];
					names[j] = tempN;
					amounts[j] = tempA;
				}
			}
		}
		//
		// Create a side pot for all players which amount is negative
		//
		for (int i = 0; i < count; i++) {
			if (amounts[i] < 0) {
				ArrayList excludedList = new ArrayList();
				ArrayList includedList = new ArrayList();
				float amount = -amounts[i];
				for (int j = i + 1; j < count; j++) {
					if (amounts[j] < 0.0f) {
						amount = amount + amounts[j];
					}
				}
				for (int j = 0; j <= i; j++) {
					excludedList.add(names[j]);
				}
				for (int j = i + 1; j < count; j++) {
					includedList.add(names[j]);
				}
				//
				// Do not create a side pot with the exact same list of excluded
				// players - that side pot will be redundant
				//
				boolean needToCreate = true;
				for (int j = 0; j < sidePots.size(); j++) {
					boolean listsSame = true;
					ArrayList spExLst = ((SidePot) sidePots.get(j))
							.getExcluded();
					if (excludedList.size() != spExLst.size()) {
						listsSame = false;
					} else {
						for (int k = 0; k < excludedList.size(); k++) {
							if (!((String) excludedList.get(k))
									.equals((String) spExLst.get(k))) {
								listsSame = false;
							}
						}
						if (listsSame) {
							needToCreate = false;
						}
					}
				}
				if (needToCreate) {
					sidePots.add(new SidePot(includedList, excludedList, amount));
					ret = true;
					theApp.log("  Side Pot #" + sidePots.size() + " created = "
							+ amount, 3);
					theApp.log("    Included in this pot :", 3);
					for (int k = 0; k < includedList.size(); k++) {
						theApp.log("      " + includedList.get(k), 3);
					}
					theApp.log("    Excluded in this pot :", 3);
					for (int k = 0; k < excludedList.size(); k++) {
						theApp.log("      " + excludedList.get(k), 3);
					}
				}
			}
		}
		return ret;
	}

	/**********************
	 * removeFromSidePots() will remove a folded player from all sidepots.
	 * 
	 * @param Name
	 *            of the player to remove from side pots.
	 * 
	 **/
	public void removeFromSidePots(String name) {
		theApp.log("PokerGame.removeFromSidePots( " + name + " )", 3);
		for (int i = 0; i < sidePots.size(); i++) {
			if (!((SidePot) sidePots.get(i)).remove(name)) {
				theApp.log("Warning : could not find player " + name
						+ " to remove from side pot #" + i);
				theApp.log("          From " + StartPoker.getCallerClassName()
						+ "." + StartPoker.getCallerMethodName());
			}
			if (((SidePot) sidePots.get(i)).getExcluded().size() == 0) {
				if (!sidePots.remove(sidePots.get(i))) {
					theApp.log("Warning : could not remove from side pot #" + i
							+ " which is no longer needed.");
					theApp.log("          From "
							+ StartPoker.getCallerClassName() + "."
							+ StartPoker.getCallerMethodName());
				}
			}
		}
	}

	/**********************
	 * endAction() is used to clean up the current round of action and call the
	 * game defined nextAction() function.
	 **/
	protected void endAction() {
		theApp.log("PokerGame.endAction()", 3);
		theApp.broadcastMessage("PLAYER CALL &CLEAR& ");
		initialBet = new PokerMoney(0.0f);
		for (int i = 0; i < sidePots.size(); i++) {
			for (int j = 0; j < theApp.getPlayerList().size(); j++) {
				((SidePot) sidePots.get(i)).toCall[j] = new PokerMoney();
			}
		}
		nextAction();
	}

	/**********************
	 * show() is called at the showdown - it handles showing the cards,
	 * determining the winner and cleaning up the game.
	 **/
	protected void show() {
		theApp.log("PokerGame.show()", 3);
		theApp.playedInLastGame = new ArrayList();
		for (int i = 0; i < theApp.getPlayerList().size(); i++) {
			theApp.playedInLastGame
					.add(((Player) theApp.getPlayerList().get(i)).getName());
		}
		theApp.broadcastMessage("MESSAGE &Calculating winner...");
		calcWinner();
		showCards();
		theApp.resetPot();
		theApp.nextDealer();
		theApp.updateMoneyLine(new PokerMoney(0.0f), "  ");
		theApp.nullifyGame();
		theApp.broadcastMessage("DISABLE BUTTONS");
	}

	/**********************
	 * calcWinner() caluclates the winner(s) of the hand.
	 **/
	protected void calcWinner() {
		theApp.log("PokerGame.calcWinner()", 3);
		int highPlayer = 0;
		float highHand = 0.0f;
		String winningHand = new String();
		int numWinners = 0;
		int winner[] = new int[MAX_PLAYERS];
		playerHandValues = new float[MAX_PLAYERS];

		for (int i = 0; i < theApp.getPlayerList().size(); i++) {
			Player p = (Player) theApp.getPlayerList().get(i);
			if (p.in) {
				playerHandValues[i] = bestHand(p.getHand());
				if (playerHandValues[i] > highHand) {
					highHand = playerHandValues[i];
					highPlayer = i;
				}
			}
		}
		for (int i = 0; i < theApp.getPlayerList().size(); i++) {
			Player p = (Player) theApp.getPlayerList().get(i);
			if (p.in) {
				if (playerHandValues[i] == highHand) {
					winner[numWinners] = i;
					numWinners++;
				}
			}
		}
		winningHand = HandEvaluator.nameHand(highHand);
		minValueToShow = highHand;

		PokerMoney winnersTake = new PokerMoney(theApp.getPot().amount());
		if (sidePots.size() != 0) {
			for (int i = 0; i < sidePots.size(); i++) {
				winnersTake.subtract(((SidePot) sidePots.get(i)).getPot()
						.amount());
			}
		}

		if (numWinners == 1) {
			theApp.log(
					""
							+ ((Player) (theApp.getPlayerList().get(highPlayer)))
									.getName() + " wins with a " + winningHand
							+ ".  Pot = " + winnersTake, 1);
			theApp.broadcastMessage("MESSAGE &"
					+ ((Player) (theApp.getPlayerList().get(highPlayer)))
							.getName() + " wins with a " + winningHand
					+ ".  Pot = " + winnersTake);
			if (theApp.getServerWindow() != null) {
				theApp.getServerWindow().sendMessage(
						""
								+ ((Player) (theApp.getPlayerList()
										.get(highPlayer))).getName()
								+ " wins with a " + winningHand + ".  Pot = "
								+ winnersTake);
			} else {
				System.out.println(""
						+ ((Player) (theApp.getPlayerList().get(highPlayer)))
								.getName() + " wins with a " + winningHand
						+ ".  Pot = " + winnersTake);
			}
			((Player) theApp.getPlayerList().get(highPlayer)).add(winnersTake
					.amount());
			theApp.broadcastMessage("PLAYER CASH &"
					+ ((Player) (theApp.getPlayerList().get(highPlayer)))
							.getName()
					+ "&"
					+ ((Player) theApp.getPlayerList().get(highPlayer))
							.getBankroll().amount());
		} else {
			StringBuffer names = new StringBuffer();
			names.append(((Player) theApp.getPlayerList().get(winner[0]))
					.getName());
			PokerMoney money = new PokerMoney(winnersTake.amount()
					/ (float) numWinners);
			((Player) theApp.getPlayerList().get(winner[0]))
					.add(money.amount());
			for (int i = 1; i < numWinners; i++) {
				names.append(" and "
						+ ((Player) (theApp.getPlayerList().get(winner[i])))
								.getName());
				((Player) theApp.getPlayerList().get(winner[i])).add(money
						.amount());
				theApp.broadcastMessage("PLAYER CASH &"
						+ ((Player) (theApp.getPlayerList().get(winner[i])))
								.getName()
						+ "&"
						+ ((Player) theApp.getPlayerList().get(winner[i]))
								.getBankroll().amount());
			}
			theApp.log("Tie between " + names + " with a " + winningHand
					+ ".  Each player gets " + money + ".", 1);
			theApp.broadcastMessage("MESSAGE &Tie between " + names
					+ " with a " + winningHand + ".  Each player gets " + money
					+ ".");
			if (theApp.getServerWindow() != null) {
				theApp.getServerWindow().sendMessage(
						"Tie between " + names + " with a " + winningHand
								+ ".  Each player gets " + money + ".");
			} else {
				System.out.println("Tie between " + names + " with a "
						+ winningHand + ".  Each player gets " + money + ".");
			}
		}

		ArrayList sidePotResults = new ArrayList();
		for (int i = 0; i < sidePots.size(); i++) {
			sidePotResults.add(calcSidePotWinners(i));
		}
		theApp.broadcastMessage("SIDE POTS START");
		if (sidePotResults.size() > 0) {
			theApp.log("Side pots : ", 1);
		}
		for (int i = 0; i < sidePotResults.size(); i++) {
			String str = ((String) sidePotResults.get(i));
			theApp.log("  " + str, 1);
			theApp.broadcastMessage("SIDE POTS &" + str);
			if (theApp.getServerWindow() != null) {
				theApp.getServerWindow().sendMessage("" + str);
			} else {
				System.out.println("" + str);
			}
		}
		int numIn = 0;
		for (int i = 0; i < theApp.getPlayerList().size(); i++) {
			if (((Player) theApp.getPlayerList().get(i)).in) {
				numIn++;
			}
		}
		if ((numIn > 2) && (sidePots.size() > 0)) {
			theApp.broadcastMessage("SIDE POTS END");
		}

		theApp.log("End of game.  Pot size = " + theApp.getPot(), 3);
		theApp.log("  Main pot : " + winnersTake, 3);
		for (int i = 0; i < sidePots.size(); i++) {
			theApp.log(
					"  Side pot #" + i + " : "
							+ ((SidePot) sidePots.get(i)).getPot(), 3);
		}
	}

	/**********************
	 * calcSidePotWinners() calculate the winner(s) of the side pots.
	 * 
	 * @param potNumber
	 *            Which side pot to calculate the winner of
	 * @retrun The string which indicates who won the side pot.
	 * 
	 **/
	protected String calcSidePotWinners(int potNumber) {
		theApp.log("PokerGame.calcSidePotWinner( " + potNumber + " )", 3);
		int highPlayer = 0;
		float highHand = 0.0f;
		String winningHand = new String();
		int numWinners = 0;
		SidePot side = (SidePot) sidePots.get(potNumber);
		PokerMoney pot = side.getPot();
		String[] players = new String[side.getIncluded().size()];
		for (int i = 0; i < players.length; i++) {
			players[i] = (String) side.getIncluded().get(i);
		}

		int winner[] = new int[MAX_PLAYERS];
		float newPlayerHandValues[] = new float[MAX_PLAYERS];

		int numPlayers = 0;
		for (int i = 0; i < players.length; i++) {
			if (players[i] != null) {
				if (theApp.playerIndex(players[i]) >= 0) {
					numPlayers++;
				}
			}
		}

		String ret = new String();
		if (numPlayers == 0) {
			return ret;
		}
		if (numPlayers == 1) {
			ret = new String("Side pot #" + (potNumber + 1) + ": " + players[0]
					+ " gets " + pot + " back.");

			int pindex = theApp.playerIndex(players[0]);
			((Player) theApp.getPlayerList().get(pindex)).add(pot.amount());
			theApp.broadcastMessage("PLAYER CASH &"
					+ players[0]
					+ "&"
					+ ((Player) theApp.getPlayerList().get(pindex))
							.getBankroll().amount());

			return ret;
		}

		for (int i = 0; i < numPlayers; i++) {
			Player p = (Player) theApp.getPlayerList().get(
					theApp.playerIndex(players[i]));

			newPlayerHandValues[i] = bestHand(p.getHand());
			if (newPlayerHandValues[i] > highHand) {
				highHand = newPlayerHandValues[i];
				highPlayer = theApp.playerIndex(players[i]);
			}
		}
		for (int i = 0; i < numPlayers; i++) {
			Player p = (Player) theApp.getPlayerList().get(
					theApp.playerIndex(players[i]));

			if (newPlayerHandValues[i] == highHand) {
				winner[numWinners] = theApp.playerIndex(players[i]);
				numWinners++;
			}
		}
		winningHand = HandEvaluator.nameHand(highHand);
		if (highHand < minValueToShow) {
			minValueToShow = highHand;
		}

		PokerMoney winnersTake = new PokerMoney(pot.amount());

		if (numWinners == 1) {
			ret = new String(
					"Side pot #"
							+ (potNumber + 1)
							+ ": "
							+ ((Player) theApp.getPlayerList().get(highPlayer))
									.getName() + " wins " + winnersTake
							+ " with a " + winningHand);
			((Player) theApp.getPlayerList().get(highPlayer)).add(winnersTake
					.amount());
			theApp.broadcastMessage("PLAYER CASH &"
					+ ((Player) (theApp.getPlayerList().get(highPlayer)))
							.getName()
					+ "&"
					+ ((Player) theApp.getPlayerList().get(highPlayer))
							.getBankroll().amount());
		} else {
			StringBuffer names = new StringBuffer();
			names.append(((Player) theApp.getPlayerList().get(winner[0]))
					.getName());
			PokerMoney money = new PokerMoney(winnersTake.amount()
					/ (float) numWinners);
			((Player) theApp.getPlayerList().get(winner[0]))
					.add(money.amount());
			for (int i = 1; i < numWinners; i++) {
				names.append(" and "
						+ ((Player) (theApp.getPlayerList().get(winner[i])))
								.getName());
				((Player) theApp.getPlayerList().get(winner[i])).add(money
						.amount());
				theApp.broadcastMessage("PLAYER CASH &"
						+ ((Player) (theApp.getPlayerList().get(winner[i])))
								.getName()
						+ "&"
						+ ((Player) theApp.getPlayerList().get(winner[i]))
								.getBankroll().amount());
			}

			ret = new String("Side pot #" + (potNumber + 1) + ": " + names
					+ " win " + money + " with a " + winningHand);
		}
		return ret;
	}

	/***********************
	 * showCards() is what happens at the showdown when the cards are shown.
	 * Uses the showPlayer() function which must be defined for each game type.
	 **/
	protected void showCards() {
		theApp.log("PokerGame.showCards()", 3);
		if (allInShowDown) {
			for (int i = 0; i < theApp.getPlayerList().size(); i++) {
				Player player = (Player) theApp.getPlayerList().get(i);
				if (player.in) {
					showPlayer(player, true);
				}
			}
			return;
		}

		boolean shown[] = new boolean[MAX_PLAYERS];
		for (int i = 0; i < theApp.getPlayerList().size(); i++) {
			shown[i] = false;
		}
		boolean allShown = false;
		int showingSeat = highBettor.seat;
		int pindex = theApp.getPlayerInSeat(showingSeat);
		float highHandValue = playerHandValues[pindex];

		Player player = (Player) theApp.getPlayerList().get(pindex);
		showPlayer(player, true);
		shown[pindex] = true;
		showingSeat = theApp.nextSeat(showingSeat);

		while (!allShown || skipShow) {
			pindex = theApp.getPlayerInSeat(showingSeat);
			if ((pindex < 0) || (pindex >= theApp.getPlayerList().size())) {
				allShown = true;
				theApp.log("ERROR : trying to show cards, but cannot find next player");
			} else {
				player = (Player) theApp.getPlayerList().get(pindex);
				if ((player.in)
						&& ((playerHandValues[pindex] >= highHandValue) || (playerHandValues[pindex] >= minValueToShow))) {
					highHandValue = playerHandValues[pindex];
					showPlayer(player, true);
				} else if ((player.in)
						&& (theApp.getShowingLostCards(player.getName()))) {
					showPlayer(player, true);
				} else if (player.in) {
					showPlayer(player, false);
				}
				shown[pindex] = true;
				allShown = true;
				for (int i = 0; i < theApp.getPlayerList().size(); i++) {
					if ((((Player) theApp.getPlayerList().get(i)).in)
							&& (!shown[i])) {
						allShown = false;
					}
				}
				showingSeat = theApp.nextSeat(showingSeat);
			}
		}
	}

	/**********************
	 * getActionNum() is used to access the private class variable
	 * 
	 * @return The current action number (or round) in the game
	 * 
	 **/
	public int getActionNum() {
		return actionNum;
	}

	/**********************
	 * getMaxActionNum() is used to access the private class variable
	 * 
	 * @return The maximum number of actions (or rounds) in the game
	 * 
	 **/
	public int getMaxActionNum() {
		return maxActionNum;
	}

	/**********************
	 * getGameError() is used to access the private class variable
	 * 
	 * @return If there was an an error setting up the game, this will be true
	 * 
	 **/
	public boolean getGameError() {
		return gameError;
	}

	/**********************
	 * getCurrBet() is used to access the private class variable
	 * 
	 * @return The current high bet in the game
	 * 
	 **/
	public PokerMoney getCurrBet() {
		return currBet;
	}

	/**********************
	 * getHighBettorName() is used to access the private class variable
	 * 
	 * @return The name of the player with the current high bet.
	 * 
	 **/
	public String getHighBettorName() {
		return highBettor.getName();
	}

	/**********************
	 * getBestPossible() is used to access the private class variable
	 * 
	 * @return The best possible hand that can be made based on what cards are
	 *         known.
	 * 
	 **/
	public float getBestPossible() {
		return bestPossible;
	}

	/**********************
	 * getMinimumRaise() is used to access the private class variable
	 * 
	 * @return The amount of the minimum allowed raise, based on game rules
	 * 
	 **/
	public float getMinimumRaise() {
		return initialBet.amount();
	}

	/**********************
	 * SidePot Class is part of the PokerGame class. All it does is define a
	 * side pot. It handles how much money is in each side pot and which players
	 * are allowed to participate in the side pot.
	 **/
	public class SidePot {

		private PokerMoney pot; // Amount of money in this side pot
		private ArrayList included; // List of player names eligble to win this
									// pot
		private ArrayList excluded; // List of player names not-elible to win
									// this pot.
		public PokerMoney[] toCall; // Amount required to call this side pot.

		// ----------------------
		// Constructor
		//
		public SidePot(ArrayList in, ArrayList out, float amount) {
			theApp.log("Constructing new SidePot " + amount, 3);
			included = new ArrayList();
			included = in;
			excluded = new ArrayList();
			excluded = out;
			pot = new PokerMoney(amount);
			toCall = new PokerMoney[theApp.getPlayerList().size()];
			for (int i = 0; i < theApp.getPlayerList().size(); i++) {
				toCall[i] = new PokerMoney(amount);
			}
			toCall[currPlayerIndex] = new PokerMoney();
		}

		// ----------------------
		// addMoney() is called to increase the amount of money in this side pot
		//
		public void addMoney(float amount) {
			pot.add(amount);
		}

		// ----------------------
		// remove() is used to remove a player from the pot (if they folded or
		// quit or something)
		//
		public boolean remove(String name) {
			boolean res1 = included.remove(name);
			boolean res2 = excluded.remove(name);

			return (res1 || res2);
		}

		// ----------------------
		// getPot() is used to access the private class variable
		//
		public PokerMoney getPot() {
			return pot;
		}

		// ----------------------
		// getIncluded() is used to access the private class variable
		//
		public ArrayList getIncluded() {
			return included;
		}

		// ----------------------
		// getExcluded() is used to access the private class variable
		//
		public ArrayList getExcluded() {
			return excluded;
		}
	}
}