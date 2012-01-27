/******************************************************************************************
 * PApp.java                 PokerApp                                                     *
 *                                                                                        *
 *   Revision History                                                                     *
 * +---------+----------+---------------------------------------------------------------+ *
 * | Version | DATE     | Description                                                   | *
 * +---------+----------+---------------------------------------------------------------+ *
 * |  0.95   | 09/15/04 | Initial documented release                                    | *
 * |  0.98   | 12/06/04 | Add INPUT_FNAME to this class and take it out of others. This | *
 * |         |          | was done so it could be better controlled.                    | *
 * |  0.98   | 12/06/04 | Add additional rules and options to poker_inputs file         | *
 * |  0.98   | 12/12/04 | Change the JFrame icon                                        | *
 * |  0.98   | 12/14/04 | Move displayError to PokerApp                                 | *
 * |  0.99   | 03/25/05 | Add additional rules to poker_inputs file - add ability to    | *
 * |         |          | have the blinds automatically double based on time or hands.  | *
 * |  0.99   | 03/29/05 | Add some null argument checking to some of the functions.     | *
 * |  1.00   | 05/26/05 | Add APPLET_INPUTS static variable                             | *
 * |  1.00   | 06/05/05 | Added logging functionality.                                  | *
 * |  1.00   | 08/19/05 | Allow antes and blinds to both be selected for the same game  | *
 * |  1.00   | 08/29/05 | Change the constructor to call an initialize function rather  | *
 * |         |          | than do it all itself.                                        | *
 * |  1.00   | 07/09/07 | Prepare for open source.  Header/comments/package/etc...      | *
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
import java.io.*;

/****************************************************
 * PApp is a base abstract class for the various poker applications. Each of the
 * applications running a part of the poker program is an extension of this
 * class.
 * 
 * @author Dan Puperi
 * @version 1.00
 * 
 **/
public abstract class PApp {

	/**
	 * List of players who were removed during the game and therefore don't need
	 * dropped
	 **/
	public ArrayList playersRemoved;
	/**
	 * Games with blind bets to start
	 **/
	public boolean blinds;
	/**
	 * Games with antes to start.
	 **/
	public boolean antes;
	/**
	 * Games are structured betting or no limit betting.
	 **/
	public boolean noLimit;
	/**
	 * Maximum bet limited games
	 **/
	public boolean betLimit;
	/**
	 * Raises limited to the value of the pot
	 **/
	public boolean potLimit;
	/**
	 * Whether the server is autodealing of not
	 **/
	public boolean autoDealing;
	/**
	 * Which games are being auto-dealt
	 **/
	public ArrayList dealingGames;
	/**
	 * Should a non-responsive player be kicked out of the game
	 **/
	public boolean kickPlayer;
	/**
	 * How long to wait for non-responsive player to be kicked
	 **/
	public int kickTimeout;
	/**
	 * The value of the small blind
	 **/
	public PokerMoney smallBlind;
	/**
	 * The value of the ante
	 **/
	public PokerMoney ante;
	/**
	 * Minimum bet for structured betting games
	 **/
	public PokerMoney minimumBet;
	/**
	 * Maximum bet for bet limited games
	 **/
	public PokerMoney maximumBet;
	/**
	 * Double the small blind/ante based on time passed
	 **/
	public boolean autoDoubleTime;
	/**
	 * Number of seconds after which the small blind/ante will be doubled
	 **/
	public int timeToDouble;
	/**
	 * Double the small blind/ante based on number of hands played
	 **/
	public boolean autoDoubleHands;
	/**
	 * Number of hands after which the small blind/ante will be doubled
	 **/
	public int handsToDouble;
	/**
	 * How much money each player starts with
	 **/
	public PokerMoney startingCash;
	/**
	 * How much money the 1st button for the poker app adds to the bet
	 **/
	public PokerMoney button1Val;
	/**
	 * How much money the 2nd button for the poker app adds to the bet
	 **/
	public PokerMoney button2Val;
	/**
	 * How much money the 3rd button for the poker app adds to the bet
	 **/
	public PokerMoney button3Val;
	/**
	 * How much money the 4th button for the poker app adds to the bet
	 **/
	public PokerMoney button4Val;
	/**
	 * Index in playerList of who is dealing
	 **/
	public int dealerIndex;
	/**
	 * Number of hands played (or since last blind double)
	 **/
	public int numHandsPlayed;
	/**
	 * The starting time of the app (or since last blind double)
	 **/
	public long startingTime;
	/**
	 * Whether the application is logging data or not.
	 **/
	public boolean logging;
	/**
	 * Whether or not the log file will be kept after the application exits.
	 **/
	public boolean keepLogFile;
	/**
	 * Amount of logging to be kept.
	 **/
	public int logLevel;
	/**
	 * Labels for the available games.
	 **/
	protected ArrayList gameLabels;
	/**
	 * Class names for the available games.
	 **/
	protected ArrayList gameClasses;
	/**
	 * Labels for the available ai types.
	 **/
	protected ArrayList aiLabels;
	/**
	 * Class names for the available ai types.
	 **/
	protected ArrayList aiClasses;
	/**
	 * Names of rules set by the input file.
	 **/
	protected ArrayList ruleNames;
	/**
	 * Values corresponding to the variable name in ruleNames.
	 **/
	protected ArrayList ruleValues;
	/**
	 * List of players in the game.
	 **/
	protected ArrayList playerList;
	/**
	 * Input file name
	 **/
	protected String INPUT_FNAME;
	/**
	 * Input file name for applet
	 **/
	protected String APPLET_INPUTS;
	/**
	 * File name of log file
	 **/
	protected String LOG_FNAME;
	/**
	 * File name of the startup log file
	 **/
	protected String STARTUP_LOG_FNAME;
	/**
	 * The log file.
	 **/
	protected File logFile;
	/**
	 * PrintWriter that writes to the log file.
	 **/
	protected PrintWriter logWriter;

	/***********************
	 * The init() abstract function must be defined in any class extending this
	 * class. init() is used to initialize a class instance.
	 **/
	public abstract void init();

	/***************************
	 * The default constructor creates a new PApp class
	 **/
	public PApp() {
		initialize(new String());
	}

	/***************************
	 * A constructor that can specify the log file title and creates a new PApp
	 * class
	 **/
	public PApp(String logTitle) {
		initialize(logTitle);
	}

	/***************************
	 * initialize() sets all variables to their default values.
	 * 
	 * @param logTitle
	 *            A string given that will help disinguish this log file from
	 *            other log files.
	 * 
	 **/
	private void initialize(String logTitle) {
		INPUT_FNAME = "poker_inputs.txt";
		APPLET_INPUTS = "applet_inputs.txt";
		int startTime = (int) (System.currentTimeMillis() / 1000);
		if (!logTitle.equals(new String()))
			logTitle = "_" + logTitle;
		LOG_FNAME = "pokerapp_log_" + startTime + logTitle + ".txt";
		STARTUP_LOG_FNAME = "pokerapp_startup_log.txt";
		gameLabels = new ArrayList();
		gameClasses = new ArrayList();
		aiLabels = new ArrayList();
		aiClasses = new ArrayList();
		ruleNames = new ArrayList();
		ruleValues = new ArrayList();
		playerList = new ArrayList();
		playersRemoved = new ArrayList();
		dealerIndex = 0;
		autoDealing = false;
		dealingGames = new ArrayList();
		antes = false;
		blinds = false;
		noLimit = false;
		betLimit = false;
		potLimit = false;
		kickPlayer = false;
		kickTimeout = 0;
		startingCash = new PokerMoney();
		button1Val = new PokerMoney();
		button2Val = new PokerMoney();
		button3Val = new PokerMoney();
		button4Val = new PokerMoney();
		ante = new PokerMoney();
		smallBlind = new PokerMoney();
		minimumBet = new PokerMoney();
		maximumBet = new PokerMoney();
		autoDoubleTime = false;
		timeToDouble = 0;
		autoDoubleHands = false;
		handsToDouble = 0;
		numHandsPlayed = 0;
		startingTime = System.currentTimeMillis() / 1000;
		logging = false;
		keepLogFile = false;
		logFile = null;
		logWriter = null;
		logLevel = 0;
	}

	/***************************
	 * The parseRules() function is used to go through the ruleNames and
	 * ruleValues lists and set variables based on those lists.
	 **/
	public void parseRules() {
		log("PApp.parseRules()", 3);
		for (int i = 0; i < ruleNames.size(); i++) {
			if (((String) ruleNames.get(i)).equals("antes")) {
				antes = ((String) ruleValues.get(i)).equals("true");
			}
			if (((String) ruleNames.get(i)).equals("blinds")) {
				blinds = ((String) ruleValues.get(i)).equals("true");
			}
			if (((String) ruleNames.get(i)).equals("noLimit")) {
				noLimit = ((String) ruleValues.get(i)).equals("true");
			}
			if (((String) ruleNames.get(i)).equals("betLimit")) {
				betLimit = ((String) ruleValues.get(i)).equals("true");
			}
			if (((String) ruleNames.get(i)).equals("potLimit")) {
				potLimit = ((String) ruleValues.get(i)).equals("true");
			}
			//
			// Check for and fix inconsistency in the betting rules.
			//
			if (noLimit) {
				betLimit = false;
				potLimit = false;
			} else if (potLimit) {
				betLimit = false;
			}

			if (((String) ruleNames.get(i)).equals("kickPlayer")) {
				kickPlayer = ((String) ruleValues.get(i)).equals("true");
			}
			if (((String) ruleNames.get(i)).equals("autoDealing")) {
				autoDealing = ((String) ruleValues.get(i)).equals("true");
			}
			if (((String) ruleNames.get(i)).equals("autoDoubleTime")) {
				autoDoubleTime = ((String) ruleValues.get(i)).equals("true");
			}
			if (((String) ruleNames.get(i)).equals("autoDoubleHands")) {
				autoDoubleHands = ((String) ruleValues.get(i)).equals("true");
			}
			//
			// Check for fix inconsistent doubling blinds rules - time takes
			// precedence.
			//
			if (autoDoubleTime) {
				autoDoubleHands = false;
			}

			if (((String) ruleNames.get(i)).equals("timeToDouble")) {
				timeToDouble = Integer.parseInt((String) ruleValues.get(i));
			}
			if (((String) ruleNames.get(i)).equals("handsToDouble")) {
				handsToDouble = Integer.parseInt((String) ruleValues.get(i));
			}
			if (((String) ruleNames.get(i)).equals("kickTimeout")) {
				kickTimeout = Integer.parseInt((String) ruleValues.get(i));
			}
			if (((String) ruleNames.get(i)).equals("startingCash")) {
				startingCash = new PokerMoney(
						Float.parseFloat((String) ruleValues.get(i)));
			}
			if (((String) ruleNames.get(i)).equals("button1Val")) {
				button1Val = new PokerMoney(
						Float.parseFloat((String) ruleValues.get(i)));
			}
			if (((String) ruleNames.get(i)).equals("button2Val")) {
				button2Val = new PokerMoney(
						Float.parseFloat((String) ruleValues.get(i)));
			}
			if (((String) ruleNames.get(i)).equals("button3Val")) {
				button3Val = new PokerMoney(
						Float.parseFloat((String) ruleValues.get(i)));
			}
			if (((String) ruleNames.get(i)).equals("button4Val")) {
				button4Val = new PokerMoney(
						Float.parseFloat((String) ruleValues.get(i)));
			}
			if (((String) ruleNames.get(i)).equals("ante")) {
				ante = new PokerMoney(Float.parseFloat((String) ruleValues
						.get(i)));
			}
			if (((String) ruleNames.get(i)).equals("smallBlind")) {
				smallBlind = new PokerMoney(
						Float.parseFloat((String) ruleValues.get(i)));
			}
			if (((String) ruleNames.get(i)).equals("minimumBet")) {
				minimumBet = new PokerMoney(
						Float.parseFloat((String) ruleValues.get(i)));
			}
			if (((String) ruleNames.get(i)).equals("maximumBet")) {
				maximumBet = new PokerMoney(
						Float.parseFloat((String) ruleValues.get(i)));
			}
		}
	}

	/***************************
	 * The updateRules() function does the opposite of what the parseRules()
	 * function does. It takes the variable values and stores them in the
	 * ruleNames and ruleValues lists.
	 **/
	public void updateRules() {
		log("PApp.updateRules()", 3);
		resetRuleNames();
		resetRuleValues();

		ruleNames.add("antes");
		ruleValues.add(new Boolean(antes));
		ruleNames.add("blinds");
		ruleValues.add(new Boolean(blinds));
		ruleNames.add("noLimit");
		ruleValues.add(new Boolean(noLimit));
		ruleNames.add("betLimit");
		ruleValues.add(new Boolean(betLimit));
		ruleNames.add("potLimit");
		ruleValues.add(new Boolean(potLimit));
		ruleNames.add("kickPlayer");
		ruleValues.add(new Boolean(kickPlayer));
		ruleNames.add("autoDealing");
		ruleValues.add(new Boolean(autoDealing));
		ruleNames.add("kickTimeout");
		ruleValues.add(new Integer(kickTimeout));
		ruleNames.add("startingCash");
		ruleValues.add(new Float(startingCash.amount()));
		ruleNames.add("button1Val");
		ruleValues.add(new Float(button1Val.amount()));
		ruleNames.add("button2Val");
		ruleValues.add(new Float(button2Val.amount()));
		ruleNames.add("button3Val");
		ruleValues.add(new Float(button3Val.amount()));
		ruleNames.add("button4Val");
		ruleValues.add(new Float(button4Val.amount()));
		ruleNames.add("ante");
		ruleValues.add(new Float(ante.amount()));
		ruleNames.add("smallBlind");
		ruleValues.add(new Float(smallBlind.amount()));
		ruleNames.add("minimumBet");
		ruleValues.add(new Float(minimumBet.amount()));
		ruleNames.add("maximumBet");
		ruleValues.add(new Float(maximumBet.amount()));
		ruleNames.add("autoDoubleTime");
		ruleValues.add(new Boolean(autoDoubleTime));
		ruleNames.add("autoDoubleHands");
		ruleValues.add(new Boolean(autoDoubleHands));
		ruleNames.add("timeToDouble");
		ruleValues.add(new Integer(timeToDouble));
		ruleNames.add("handsToDouble");
		ruleValues.add(new Integer(handsToDouble));
	}

	/***************************
	 * The playerIndex() function returns the position in the ArrayList
	 * playerList that contains the player specified by the string name given as
	 * an argument. Returns -1 if not found.
	 * 
	 * @param name
	 *            The player name to find the index of
	 * @return The index of the player in the playerList or -1 if not found
	 * 
	 **/
	public int playerIndex(String name) {
		if (name == null) {
			log("Warning : Tried to call PApp.playerIndex() with a null argument");
			log("          From " + StartPoker.getCallerClassName() + "."
					+ StartPoker.getCallerMethodName());
			log("          Returning -1 for playerIndex");
			return -1;
		}
		for (int i = 0; i < playerList.size(); i++) {
			String pname = ((Player) playerList.get(i)).getName();
			if (name.equals(pname)) {
				log("" + name + " is at playerIndex " + i, 4);
				return i;
			}
		}
		if (!StartPoker.getCallerClassName()
				.equals(new String(
						"net.sourceforge.pokerapp.PokerFrame$PokerGlassPane"))) {
			log("Warning : tried to call PApp.playerIndex(), but could not find a player named "
					+ name);
			log("          From " + StartPoker.getCallerClassName() + "."
					+ StartPoker.getCallerMethodName());
			log("          Returning -1 for playerIndex");
		}
		return -1;
	}

	/***************************
	 * The seatIndex() function returns the integer seat of where that player is
	 * sitting. Returns -9 if not found.
	 * 
	 * @param name
	 *            The player name to find the seat of
	 * @return The seat that the player is sitting in or -9 if not found
	 * 
	 **/
	public int seatIndex(String name) {
		if (name == null) {
			log("Warning : Tried to call PApp.seatIndex() with a null argument");
			log("          From " + StartPoker.getCallerClassName() + "."
					+ StartPoker.getCallerMethodName());
			log("          Returning -9 for seatIndex");
			return -9;
		}
		for (int i = 0; i < playerList.size(); i++) {
			String pname = ((Player) playerList.get(i)).getName();
			if (name.equals(pname)) {
				int s = ((Player) playerList.get(i)).seat;
				log("" + name + " is in seat " + s, 4);
				return s;
			}
		}
		if (!StartPoker.getCallerMethodName().equals(new String("nextSeat"))) {
			log("Warning : Tried to call PApp.seatIndex(), but could not find a player named "
					+ name);
			log("          From " + StartPoker.getCallerClassName() + "."
					+ StartPoker.getCallerMethodName());
			log("          Returning -9 for seatIndex");
		}
		return -9;
	}

	/***************************
	 * dropPlayer() removes a player from the game.
	 * 
	 * @param n
	 *            The player name to drop
	 * 
	 **/
	public void dropPlayer(String n) {
		if (n == null) {
			log("Warning : Tried to call PApp.dropPlayer() with a null argument");
			log("          From " + StartPoker.getCallerClassName() + "."
					+ StartPoker.getCallerMethodName());
			return;
		}
		boolean alreadyDropped = false;
		for (int i = 0; i < playersRemoved.size(); i++) {
			if (n.equals((String) playersRemoved.get(i))) {
				alreadyDropped = true;
			}
		}
		int index = playerIndex(n);
		if ((index != -1) && (!alreadyDropped)) {
			playerList.remove(index);
			playerList.trimToSize();
			playersRemoved.add(n);
			log("Player " + n + " removed from playerList .", 1);
		} else {
			log("Warning : Tried to call PApp.dropPlayer(), but could not drop the player from the playerList.");
			log("          alreadyDropped = " + alreadyDropped
					+ " and index = " + index);
			log("          From " + StartPoker.getCallerClassName() + "."
					+ StartPoker.getCallerMethodName());
		}
	}

	/***************************
	 * createLogFile() creates the logFile and logWriter. Should be called when
	 * an instance of the application is created.
	 **/
	protected void createLogFile() {
		try {
			if (logging) {
				System.out.println("Starting PokerApp log file " + LOG_FNAME);
				logFile = new File(LOG_FNAME);
				logWriter = new PrintWriter(new FileOutputStream(logFile));
			}
		} catch (Exception x) {
			System.out.println("ERROR : could not set up log file.");
			x.printStackTrace();
		}
	}

	/***************************
	 * closeLogFile() closes the logWriter and deletes the logFile if desired.
	 * Should be called when the application is terminated.
	 **/
	protected void closeLogFile() {
		try {
			if (logging) {
				logWriter.close();
				if (!keepLogFile) {
					logFile.delete();
					System.out.println("Closing and deleting log file.");
				} else {
					System.out.println("Closing log file.");
				}
			}
		} catch (Exception x) {
			System.out.println("ERROR : could not close log file properly.");
			x.printStackTrace();
		}
	}

	/***************************
	 * log() records a line to the log file if logging is enabled.
	 * 
	 * @param s
	 *            The String line to write to the log file.
	 * 
	 **/
	public void log(String s) {
		if (logging) {
			if (logFile == null) {
				System.out
						.println("ERROR : Tried to write to log file, but the log file is null.");
				return;
			}
			if (logWriter == null) {
				System.out
						.println("ERROR : Tried to write to log file, but the log file is null.");
				return;
			}
			logWriter.println(s);
			logWriter.flush();
		}
	}

	/***************************
	 * log() records a line to the log file if logging is enabled. This function
	 * will only log the line if the passed variable l is less than or equal to
	 * the log level of the application.
	 * 
	 * @param s
	 *            The String line to write to the log file.
	 * @param l
	 *            The log level required to write the line
	 * 
	 **/
	public void log(String s, int l) {
		if (logLevel >= l) {
			log(s);
		}
	}

	/***************************
	 * logStackTrace() is used to write stackTrace to log file rather than
	 * System.out
	 * 
	 * @param t
	 *            The exception that should be logged.
	 * 
	 **/
	public void logStackTrace(Throwable t) {
		StackTraceElement[] trace = t.getStackTrace();
		if ((logFile != null) && (logWriter != null)) {
			for (int i = 0; i < trace.length; i++) {
				log(trace[i].toString());
			}
		} else {
			System.out
					.println("Warning : tried to write the stack trace to log, but could not");
			t.printStackTrace();
		}
	}

	/***************************
	 * getGameLabels() is used to access the private variable
	 * 
	 * @return The gameLabels ArrayList
	 * 
	 **/
	public ArrayList getGameLabels() {
		return gameLabels;
	}

	/***************************
	 * getGameClasses() is used to access the private variable
	 * 
	 * @return The gameClasses ArrayList
	 * 
	 **/
	public ArrayList getGameClasses() {
		return gameClasses;
	}

	/***************************
	 * getAILabels() is used to access the private variable
	 * 
	 * @return The gameAILabels ArrayList
	 * 
	 **/
	public ArrayList getAILabels() {
		return aiLabels;
	}

	/***************************
	 * getAIClasses() is used to access the private variable
	 * 
	 * @return The aiClasses ArrayList
	 * 
	 **/
	public ArrayList getAIClasses() {
		return aiClasses;
	}

	/***************************
	 * getRuleNames() is used to access the private variable
	 * 
	 * @return The ruleNames ArrayList
	 * 
	 **/
	public ArrayList getRuleNames() {
		return ruleNames;
	}

	/***************************
	 * getRuleValues() is used to access the private variable
	 * 
	 * @return The ruleValues ArrayList
	 * 
	 **/
	public ArrayList getRuleValues() {
		return ruleValues;
	}

	/***************************
	 * getPlayerList() is used to access the private variable
	 * 
	 * @return The playerList ArrayList
	 * 
	 **/
	public ArrayList getPlayerList() {
		return playerList;
	}

	/***************************
	 * getPlayerInSeat() returns the number of the player currently occupying
	 * the seat in question
	 * 
	 * @param s
	 *            The seat number which is being inquired.
	 * @return The player index which is their location in playerList.
	 * 
	 **/
	public int getPlayerInSeat(int s) {
		if ((s < 0) || (s > PokerGame.MAX_PLAYERS - 1)) {
			log("Warning : Tried to call PApp.getPlayerInSeat(), but seat was out of range : "
					+ s);
			log("          From " + StartPoker.getCallerClassName() + "."
					+ StartPoker.getCallerMethodName());
			log("          Returning -9 for seat");
			return -9;
		}
		for (int i = 0; i < playerList.size(); i++) {
			if (s == ((Player) playerList.get(i)).seat) {
				log("The player sitting in seat " + s + " is "
						+ ((Player) playerList.get(i)).getName(), 4);
				return i;
			}
		}
		if ((!StartPoker.getCallerClassName()
				.equals(new String(
						"net.sourceforge.pokerapp.PokerFrame$PokerGlassPane")))
				&& (!StartPoker.getCallerMethodName().equals(
						new String("nextSeat")))) {
			log("Warning : Tried to call PApp.getPlayerInSeat(), but could not find a player in seat "
					+ s);
			log("          From " + StartPoker.getCallerClassName() + "."
					+ StartPoker.getCallerMethodName());
			log("          Returning -9 for seat");
		}
		return -9;
	}

	/***************************
	 * resetGameLabels() is used to reset the data list to an empty list.
	 **/
	public void resetGameLabels() {
		gameLabels = new ArrayList();
	}

	/***************************
	 * resetGameClasses() is used to reset the data list to an empty list.
	 **/
	public void resetGameClasses() {
		gameClasses = new ArrayList();
	}

	/***************************
	 * resetAILabels() is used to reset the data list to an empty list.
	 **/
	public void resetAILabels() {
		aiLabels = new ArrayList();
	}

	/***************************
	 * resetAIClasses() is used to reset the data list to an empty list.
	 **/
	public void resetAIClasses() {
		aiClasses = new ArrayList();
	}

	/***************************
	 * resetRuleNames() is used to reset the data list to an empty list.
	 **/
	public void resetRuleNames() {
		ruleNames = new ArrayList();
	}

	/***************************
	 * resetRuleValues() is used to reset the data list to an empty list.
	 **/
	public void resetRuleValues() {
		ruleValues = new ArrayList();
	}

	/***************************
	 * resetPlayerList() is used to reset the data list to an empty list.
	 **/
	public void resetPlayerList() {
		playerList = new ArrayList();
	}

}
