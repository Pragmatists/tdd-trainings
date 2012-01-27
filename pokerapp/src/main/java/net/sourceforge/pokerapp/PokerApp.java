/******************************************************************************************
 * PokerApp.java                   PokerApp                                               *
 *                                                                                        *
 *   Revision History                                                                     *
 * +---------+----------+---------------------------------------------------------------+ *
 * | Version | DATE     | Description                                                   | *
 * +---------+----------+---------------------------------------------------------------+ *
 * |  0.95   | 09/15/04 | Initial documented release                                    | *
 * |  0.96   | 10/08/04 | Update due to PokerClient updates                             | *
 * |  0.97   | 10/25/04 | Remove encryption for reg name - now all comm is encrypted.   | *
 * |  0.97   | 11/07/04 | Changes the 5 game limit - now it will let person actually    | *
 * |         |          | play 10 games.                                                | *
 * |  0.97   | 11/08/04 | Make LOCATION a static variable so the poker games can see it | *
 * |  0.97   | 11/09/04 | Added the variables midTablePic and selectedCards.            | *
 * |  0.98   | 12/06/04 | Check to make sure main start window is not null.             | *
 * |  0.98   | 12/06/04 | Added to loadInputFile so it will read the new options.  Also | *
 * |         |          | move INPUT_FNAME to PApp rather than here.                    | *
 * |  0.98   | 12/09/04 | Changed disconnectSocket to accept a boolean argument. If it  | *
 * |         |          | is passed false, it will not try to redraw to the window.     | *
 * |  0.98   | 12/14/04 | Implemented better way to exit when come across a critical    | *
 * |         |          | error. No longer need to use Thread.sleep to wait to close    | *
 * |         |          | the application.  Moved displayError into this class.         | *
 * |  0.99   | 03/30/05 | Add a variable that tells if it's this player's turn to bet.  | *
 * |  0.99   | 05/16/05 | Make the PokerErrorBox a JDialog so it stays on top.          | *
 * |  0.99   | 05/16/05 | Side pot winners dialog box.                                  | *
 * |  0.99   | 05/17/05 | Add flag structuredRaiseEnable                                | *
 * |  0.99   | 05/17/05 | Added inGame flag to determine when a game is active          | *
 * |  1.00   | 05/26/05 | Modify so that PokerApp can also be an applet.                | *
 * |  1.00   | 06/05/05 | Add logging functions.                                        | *
 * |  1.00   | 07/25/05 | Added one click option for check and call buttons             | *
 * |  1.00   | 08/22/05 | Add variables for saved messages to be displayed.             | *
 * |  1.00   | 08/26/05 | Only display side pot window when message window is not open. | *
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

import java.util.ArrayList;
import java.net.Socket;
import java.io.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/****************************************************
 * PokerApp is the application that will start the a single human player client.
 * It is used to conenct to a server and play a game of poker.
 * 
 * @author Dan Puperi
 * @version 1.00
 * 
 **/
public class PokerApp extends PApp {

	/**
	 * Which card back is being used.
	 **/
	public ArrayList cardBackSelection;
	/**
	 * Which table image is being used.
	 **/
	public ArrayList tableImgSelection;
	/**
	 * If StartPoker was used to start this, the instance of the StartPoker
	 * class
	 **/
	public StartPoker startUpApp;
	/**
	 * If PokerApplet was used to start this, the instance of the PokerApplet
	 * class
	 **/
	public PokerApplet appletApp;
	/**
	 * The pictures for each of the players cards
	 **/
	public Picture cardPics[][];
	/**
	 * A picture that might be in the middle of the poker table.
	 **/
	public Picture midTablePic;
	/**
	 * Which cards are selected for action
	 **/
	public boolean selectedCards[];
	/**
	 * The communicator window if opened
	 **/
	public TalkFrame communicator;
	/**
	 * The message window if opened.
	 **/
	public MessageFrame messageWindow;
	/**
	 * Whether or not player is showing his hole cards.
	 **/
	public boolean hideHoleCards;
	/**
	 * Whether or not player shows losing hands.
	 **/
	public boolean muckLosingHands;
	/**
	 * Whether or not the check and call buttons are one-click or not.
	 **/
	public boolean oneClickCheckCall;
	/**
	 * If its this players turn to bet
	 **/
	public boolean turnToBet;
	/**
	 * The X and Y location on the table of all the players
	 **/
	public static int LOCATION[][];
	/**
	 * Messages displaying who wins the side pots
	 **/
	public ArrayList sidePotMessages;
	/**
	 * For structured betting games, whether this player can raise.
	 **/
	public boolean structuredRaiseEnable;
	/**
	 * Whether a game is actively going on.
	 **/
	public boolean inGame;
	/**
	 * Whether this instance is for an applet.
	 **/
	public boolean isApplet;
	/**
	 * Number of messages that are saved
	 **/
	public static int NUM_SAVED_MESSAGES;
	/**
	 * Saved messages (to display when message window is first opened)
	 **/
	public String pastMessages[];

	private PokerFrame mainWindow; // The PokerApp main window instance
	private PokerApp theApp; // The instance of this PokerApp
	private PokerView view; // The view port definition
	private PokerModel model; // The view port model
	private Player thisPlayer; // Player represented by this PokerApp
	private Socket clientSocket; // Socket to the poker server
	private String hostName; // Name of the host
	private PokerClient client; // The client for this player
	private int port; // Port used to connect to server
	private ArrayList cardBackLabels; // Names of the possible card backs
	private ArrayList cardBackFiles; // Path to the file name with the card back
										// image
	private ArrayList tableImgLabels; // Names of the possible table images
	private ArrayList tableImgFiles; // Path to the file name with the table
										// image
	private int numGamesPlayed; // Number of games played.
	private javax.swing.Timer timer; // Timer which causes game to exit
	private String appletServer; // The server that the applet should access.
	private String appletPort; // The port that the applet should connect to.

	/***************************
	 * The PokerApp constructor if this class is created by the StartPoker class
	 * 
	 * @param a
	 *            The StartPoker instance to which this PokerApp belongs
	 * @param fname
	 *            The input file from which inputs and settings are read
	 * 
	 **/
	public PokerApp(StartPoker a, String fname) {
		super();
		theApp = this;
		startUpApp = a;
		appletApp = null;
		isApplet = false;
		INPUT_FNAME = fname;
		theApp.init();
	}

	/***************************
	 * The PokerApp constructor if this class is created by the PokerApplet
	 * class
	 * 
	 * @param a
	 *            The PokerApplet instance to which this PokerApp belongs
	 * @param fname
	 *            The input file from which inputs and settings are read
	 * 
	 **/
	public PokerApp(PokerApplet a, String fname) {
		super();
		theApp = this;
		startUpApp = null;
		appletApp = a;
		isApplet = true;
		INPUT_FNAME = fname;
		logWriter = null;
		theApp.init();
	}

	/***********************
	 * The init() function initializes all of the variables and set up the
	 * application. It is run immediately after the creation of the class
	 * instance.
	 **/
	public void init() {
		PrintWriter log_writer = null;
		try {
			if (!isApplet) {
				File log_file = new File(STARTUP_LOG_FNAME);
				log_writer = new PrintWriter(new FileOutputStream(log_file),
						true);
				if (log_writer == null) {
					System.out
							.println("ERROR : log_writer is null.  Cannot record startup messages for some reason.");
				}
			}
		} catch (Exception x) {
			x.printStackTrace();
			System.exit(1);
		}
		if (log_writer != null) {
			log_writer.println("Initializing PokerApp");
		}

		appletServer = new String();
		appletPort = new String();
		loadInputData(log_writer);
		if (log_writer != null) {
			log_writer.println("Successful startup of PokerApp.");
			log_writer.close();
		}

		cardBackSelection = new ArrayList();
		tableImgSelection = new ArrayList();
		numGamesPlayed = 0;
		turnToBet = false;
		inGame = false;
		structuredRaiseEnable = false;
		cardBackSelection.add(new Boolean(true));
		tableImgSelection.add(new Boolean(true));
		for (int i = 1; i < cardBackLabels.size(); i++) {
			cardBackSelection.add(new Boolean(false));
		}
		for (int i = 1; i < tableImgLabels.size(); i++) {
			tableImgSelection.add(new Boolean(false));
		}

		LOCATION = new int[PokerGame.MAX_PLAYERS][2];
		LOCATION[0][0] = 10;
		LOCATION[0][1] = 10;
		LOCATION[1][0] = 310;
		LOCATION[1][1] = 10;
		LOCATION[2][0] = 610;
		LOCATION[2][1] = 10;
		LOCATION[3][0] = 610;
		LOCATION[3][1] = 160;
		LOCATION[4][0] = 610;
		LOCATION[4][1] = 310;
		LOCATION[5][0] = 310;
		LOCATION[5][1] = 310;
		LOCATION[6][0] = 10;
		LOCATION[6][1] = 310;
		LOCATION[7][0] = 10;
		LOCATION[7][1] = 160;

		NUM_SAVED_MESSAGES = 10;
		pastMessages = new String[NUM_SAVED_MESSAGES];
		for (int i = 0; i < NUM_SAVED_MESSAGES; i++) {
			pastMessages[i] = new String();
		}

		cardPics = new Picture[PokerGame.MAX_PLAYERS][10];
		selectedCards = new boolean[10];
		midTablePic = null;
		model = new PokerModel();
		view = new PokerView(theApp);
		model.addObserver(view);
		mainWindow = new PokerFrame("PokerApp Client", theApp);
		java.awt.Toolkit tk = mainWindow.getToolkit();
		java.awt.Dimension screensize = tk.getScreenSize();
		mainWindow.setBounds(10, 10, 900, 600);
		mainWindow
				.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		mainWindow.setVisible(true);
		mainWindow.disableButtons();
		if (startUpApp != null) {
			if (startUpApp.getMainWindow() != null) {
				startUpApp.getMainWindow().sendMessage("Client window opened");
			}
		} else {
			System.out.println("Client window opened");
		}
		log("Client window opened", 1);
	}

	/***********************
	 * loadInputData(). The first thing that must be done is read the input
	 * file. This file contains information about the various options available
	 * for the game.
	 * 
	 * @param log_writer
	 *            This function is passed the log_writer so the results can be
	 *            written to the log file.
	 * 
	 **/
	private void loadInputData(PrintWriter log_writer) {
		File inputFile;
		BufferedReader inputDataReader;
		boolean EOF = false;
		String line;
		cardBackLabels = new ArrayList();
		cardBackFiles = new ArrayList();
		tableImgLabels = new ArrayList();
		tableImgFiles = new ArrayList();

		try {
			if (log_writer != null) {
				log_writer.println("Loading inputs from " + INPUT_FNAME);
			}
			if (isApplet) {
				ClassLoader cl = getClass().getClassLoader();
				java.net.URL url = cl.getResource(INPUT_FNAME);
				inputDataReader = new BufferedReader(new InputStreamReader(
						url.openStream()));
			} else {
				inputFile = new File(INPUT_FNAME);
				inputDataReader = new BufferedReader(new InputStreamReader(
						new FileInputStream(inputFile)));
			}
			boolean readingRules = false;
			boolean readingGames = false;
			boolean readingAI = false;
			boolean readingCardBacks = false;
			boolean readingTableImgs = false;
			boolean readingClientOpts = false;
			boolean readingAppletOpts = false;
			boolean readingDealer = false;
			boolean readingLogging = false;

			while (!EOF) {
				try {
					line = inputDataReader.readLine();
					if (!line.matches("^#.*")) {
						if (readingCardBacks) {
							int i = line.indexOf(',');
							cardBackLabels.add(line.substring(0, i).trim());
							cardBackFiles.add(line.substring(i + 1).trim());
						}
						if (readingTableImgs) {
							int i = line.indexOf(',');
							tableImgLabels.add(line.substring(0, i).trim());
							tableImgFiles.add(line.substring(i + 1).trim());
						}
						if (readingClientOpts) {
							int i = line.indexOf('=');
							String option = line.substring(0, i).trim();
							String value = line.substring(i + 1).trim();

							if (option.equals("hideHoleCards")) {
								hideHoleCards = value.equals("true");
							}
							if (option.equals("muckLosingHands")) {
								muckLosingHands = value.equals("true");
							}
							if (option.equals("oneClickCheckCall")) {
								oneClickCheckCall = value.equals("true");
							}
						}
						if (readingAppletOpts) {
							int i = line.indexOf('=');
							String option = line.substring(0, i).trim();
							String value = line.substring(i + 1).trim();

							if (option.equals("server")) {
								int j = value.indexOf(':');
								appletServer = value.substring(0, j).trim();
								appletPort = value.substring(j + 1).trim();
							}
						}
						if (readingLogging) {
							int i = line.indexOf('=');
							String var = line.substring(0, i).trim();
							String val = line.substring(i + 1).trim();
							if (var.equals("logging")) {
								logging = val.equals("true");
							} else if (var.equals("keepLogFile")) {
								keepLogFile = val.equals("true");
							} else if (var.equals("logLevel")) {
								try {
									logLevel = Integer.parseInt(val);
									if (logging) {
										createLogFile();
									}
								} catch (Exception x) {
									System.out
											.println("Warning : Could not set logLevel.");
									if (log_writer != null) {
										log_writer
												.println("Warning : Could not set logLevel.");
									}
									x.printStackTrace();
								}
							} else {
								System.out
										.println("Warning : Bad variable in input file in the LOGGING block.");
								if (log_writer != null) {
									log_writer
											.println("Warning : Bad variable in input file in the LOGGING block.");
								}
							}
						}
					} else {
						if (line.matches(".*RULES")) {
							readingRules = true;
							readingGames = false;
							readingAI = false;
							readingCardBacks = false;
							readingTableImgs = false;
							readingClientOpts = false;
							readingAppletOpts = false;
							readingDealer = false;
							readingLogging = false;
						}
						if (line.matches(".*GAMES")) {
							readingRules = false;
							readingGames = true;
							readingAI = false;
							readingCardBacks = false;
							readingTableImgs = false;
							readingClientOpts = false;
							readingAppletOpts = false;
							readingDealer = false;
							readingLogging = false;
						}
						if (line.matches(".*AI")) {
							readingRules = false;
							readingGames = false;
							readingAI = true;
							readingCardBacks = false;
							readingTableImgs = false;
							readingClientOpts = false;
							readingAppletOpts = false;
							readingDealer = false;
							readingLogging = false;
						}
						if (line.matches(".*CARDBACKS")) {
							if (log_writer != null) {
								log_writer.println("  Reading card images");
							}
							readingRules = false;
							readingGames = false;
							readingAI = false;
							readingCardBacks = true;
							readingTableImgs = false;
							readingClientOpts = false;
							readingAppletOpts = false;
							readingDealer = false;
							readingLogging = false;
						}
						if (line.matches(".*TABLEIMGS")) {
							if (log_writer != null) {
								log_writer.println("  Reading table images");
							}
							readingRules = false;
							readingGames = false;
							readingAI = false;
							readingCardBacks = false;
							readingTableImgs = true;
							readingClientOpts = false;
							readingAppletOpts = false;
							readingDealer = false;
							readingLogging = false;
						}
						if (line.matches(".*CLIENTOPTIONS")) {
							if (log_writer != null) {
								log_writer.println("  Reading client options");
							}
							readingRules = false;
							readingGames = false;
							readingAI = false;
							readingCardBacks = false;
							readingTableImgs = false;
							readingClientOpts = true;
							readingAppletOpts = false;
							readingDealer = false;
							readingLogging = false;
						}
						if (line.matches(".*APPLETOPTIONS")) {
							readingRules = false;
							readingGames = false;
							readingAI = false;
							readingCardBacks = false;
							readingTableImgs = false;
							readingClientOpts = false;
							readingAppletOpts = true;
							readingDealer = false;
							readingLogging = false;
						}
						if (line.matches(".*DEALER")) {
							readingRules = false;
							readingGames = false;
							readingAI = false;
							readingCardBacks = false;
							readingTableImgs = false;
							readingClientOpts = false;
							readingAppletOpts = false;
							readingDealer = true;
							readingLogging = false;
						}
						if (line.matches(".*LOGGING")) {
							if (log_writer != null) {
								log_writer
										.println("  Reading logging variables");
							}
							readingRules = false;
							readingGames = false;
							readingAI = false;
							readingCardBacks = false;
							readingTableImgs = false;
							readingClientOpts = false;
							readingAppletOpts = false;
							readingDealer = false;
							readingLogging = true;
						}
					}
				} catch (NullPointerException x) {
					EOF = true;
				} catch (IOException x) {
					EOF = true;
				}
			}
		} catch (FileNotFoundException x) {
			displayError("File Read Error",
					"Critical Error reading input file: " + INPUT_FNAME, true);
			if (log_writer != null) {
				log_writer
						.println("ERROR - reading input file: " + INPUT_FNAME);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/***********************
	 * connectSocket() is used to connect to a server
	 * 
	 * @param name
	 *            The players name who is trying to connect
	 * @param host
	 *            The string representation of the host - either and IP address
	 *            or acceptable hostname
	 * @param p
	 *            The port number on which to connect
	 * 
	 **/
	public void connectSocket(String name, String host, String p) {
		log("PokerApp.connectSocket( " + name + ", " + host + ", " + p + " )",
				3);
		hostName = host;
		try {
			port = Integer.parseInt(p);
			if (isApplet && (appletApp != null)) {
				clientSocket = appletApp.connect(port);
			} else {
				clientSocket = new Socket(hostName, port);
			}
			if (clientSocket == null) {
				log("ERROR : unable to connect to the socket - " + hostName
						+ " port = " + port);
				mainWindow
						.sendMessage("Error - unable to connect to the socket");
			} else {
				mainWindow.sendMessage("Connection to " + hostName
						+ " successful.  Choose a seat to join the game.");
				log("Connection to " + hostName + " successful.", 1);
				thisPlayer = new Player(name);
				client = new PokerClient(this);
			}
		} catch (java.net.UnknownHostException e) {
			mainWindow.sendMessage("Can't find host " + hostName);
			log("Can't find host " + hostName, 1);
		} catch (java.io.IOException e) {
			mainWindow.sendMessage("Error - unable to open socket");
			log("ERROR : unable to open socket");
			logStackTrace(e);
		} catch (NumberFormatException e) {
			mainWindow.sendMessage("Error - bad port number");
			log("Bad port number " + port, 1);
		} catch (Exception e) {
			log("Warning : caught exception in PokerApp.connectSocket()");
			logStackTrace(e);
		}

		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
		}
		messageToServer("REQUEST DATA");
	}

	/***********************
	 * connectSocket() is used to connect to a server
	 * 
	 * @param redraw
	 *            Whether or not the client window should be redrawn
	 * 
	 **/
	public void disconnectSocket(boolean redraw) {
		log("PokerApp.disconnectSocket( " + redraw + " )", 3);
		try {
			if (clientSocket != null) {
				if (thisPlayer != null) {
					if (thisPlayer.seat >= 0) {
						messageToServer("BET FOLD");
					}
				}
				messageToServer("LEAVE");
				client.stopRunning();
				clientSocket.close();
				if (redraw && (mainWindow != null))
					mainWindow.sendMessage("Disconnected from " + hostName);
			}
			clientSocket = null;
			if (redraw) {
				resetTable();
				turnToBet = false;
				inGame = false;
				structuredRaiseEnable = false;
				if (mainWindow != null)
					mainWindow.menuRedisplay();
				if (mainWindow != null)
					mainWindow.disableButtons();
				gameLabels = new ArrayList();
				gameClasses = new ArrayList();
				ruleNames = new ArrayList();
				ruleValues = new ArrayList();
				playerList = new ArrayList();
				if (thisPlayer != null)
					thisPlayer.seat = -9;
			}
		} catch (Exception e) {
			log("Warning : Caught exception in PokerApp.disconnectSocket()");
			logStackTrace(e);
		}
	}

	/***********************
	 * messageToServer() is used to send a message to the server.
	 * 
	 * @param m
	 *            The String message to send to the server
	 * 
	 **/
	public void messageToServer(String m) {
		if (client != null) {
			client.sendMessage(m);
		}
	}

	/***********************
	 * The resetTable() function is normally called in between games to clean up
	 * the table.
	 **/
	public void resetTable() {
		log("PokerApp.resetTable()", 3);
		playersRemoved = new ArrayList();
		turnToBet = false;
		inGame = false;
		structuredRaiseEnable = false;

		java.util.Iterator pictures = model.getIterator();
		while (pictures.hasNext()) {
			pictures.next();
			pictures.remove();
		}
		view.repaint();
		if (thisPlayer != null) {
			thisPlayer.in = false;
		}
	}

	/***********************
	 * clearPlayerPics() gets rid of player's graphics if player folded
	 * 
	 * @param name
	 *            The name of the player who folder
	 * 
	 **/
	public void clearPlayerPics(String name) {
		log("PokerApp.clearPlayerPics( " + name + " )", 3);
		int i = playerIndex(name);
		if ((i >= 0) && (i < PokerGame.MAX_PLAYERS)) {
			for (int j = 0; j < 10; j++) {
				if (cardPics[i][j] != null) {
					model.remove(cardPics[i][j]);
				}
			}
		}
	}

	/***********************
	 * getThisPlayer() is used to access the private class variable
	 * 
	 * return The player who is using this instance of PokerApp
	 * 
	 **/
	public Player getThisPlayer() {
		return thisPlayer;
	}

	/***********************
	 * getClient() is used to access the private class variable
	 * 
	 * return The client communicating with the server
	 * 
	 **/
	public PokerClient getClient() {
		return client;
	}

	/***********************
	 * getWindow() is used to access the private class variable
	 * 
	 * return The main window of the PokerApp client
	 * 
	 **/
	public PokerFrame getWindow() {
		return mainWindow;
	}

	/***********************
	 * getView() is used to access the private class variable
	 * 
	 * return The view of the table
	 * 
	 **/
	public PokerView getView() {
		return view;
	}

	/***********************
	 * getModel() is used to access the private class variable
	 * 
	 * return The model of the view of the table and the pictures on it
	 * 
	 **/
	public PokerModel getModel() {
		return model;
	}

	/***********************
	 * getSocekt() is used to access the private class variable
	 * 
	 * return The socket which was used to connect to the server
	 * 
	 **/
	public Socket getSocket() {
		return clientSocket;
	}

	/***********************
	 * getCardBackLabels() is used to access the private class variable
	 * 
	 * return The list of labels that could be used for card backs
	 * 
	 **/
	public ArrayList getCardBackLabels() {
		return cardBackLabels;
	}

	/***********************
	 * getCardBackFiles() is used to access the private class variable
	 * 
	 * return The list of image files for the card backs
	 **/
	public ArrayList getCardBackFiles() {
		return cardBackFiles;
	}

	/***********************
	 * getTableImgLabels() is used to access the private class variable
	 * 
	 * return The list of labels for the table images
	 **/
	public ArrayList getTableImgLabels() {
		return tableImgLabels;
	}

	/***********************
	 * getTableImgFiles() is used to access the private class variable
	 * 
	 * return The list of image files for the table background
	 **/
	public ArrayList getTableImgFiles() {
		return tableImgFiles;
	}

	/***********************
	 * getAppletServer() is used to access the private class variable
	 * 
	 * return The server string of the applet (if this is being run as an
	 * applet)
	 **/
	public String getAppletServer() {
		return appletServer;
	}

	/***********************
	 * getAppletPort() is used to access the private class variable
	 * 
	 * return The port that this applet is connected to (if this is being run as
	 * an applet)
	 **/
	public String getAppletPort() {
		return appletPort;
	}

	/***********************
	 * displayError() is used to display an error message
	 * 
	 * @param title
	 *            The title of the error box window
	 * @param text
	 *            The text of the error message
	 * @param exit
	 *            Whether or not this error will result in the application
	 *            exiting after error box is OK'ed
	 * 
	 **/
	public void displayError(String title, String text, boolean exit) {
		PokerErrorBox errorWindow = new PokerErrorBox(title, text, exit);
		errorWindow.setBounds(60, 200, 540, 100);
		errorWindow
				.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		errorWindow.setVisible(true);
	}

	/***********************
	 * PokerErrorBox class defines the error box that will be displayed by the
	 * displayError() function.
	 **/
	class PokerErrorBox extends JDialog {

		boolean exiting; // Whether or not the application will be killed when
							// OK button is pressed.

		// ----------------------
		// Constructor
		//
		PokerErrorBox(String title, String text, boolean exit) {
			super(mainWindow, title);
			exiting = exit;
			getContentPane().setLayout(new BorderLayout());
			JLabel label = new JLabel(text, SwingConstants.CENTER);
			JPanel buttonPanel = new JPanel();
			buttonPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
			JButton button = new JButton("OK");
			button.addActionListener(new OKAction());
			buttonPanel.add(button);
			getContentPane().add(label, BorderLayout.CENTER);
			getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		}

		// ----------------------
		// processWindowEvent() processes the close window event
		//
		protected void processWindowEvent(WindowEvent e) {
			if (e.getID() == WindowEvent.WINDOW_CLOSING) {
				dispose();
				if (exiting) {
					enditol();
				}
			}
			super.processWindowEvent(e);
		}

		// ----------------------
		// OKAction class is used to define the action that occurs when the OK
		// button is pressed.
		//
		class OKAction extends AbstractAction {

			// ----------------------
			// Constructor
			//
			OKAction() {
				super();
			}

			// ----------------------
			// The actionPerformed() function is called when the button is
			// pressed
			//
			public void actionPerformed(ActionEvent e) {
				dispose();
				if (exiting) {
					enditol();
				}
			}
		}
	}

	/***********************
	 * displaySidePotMessages() is used to display the results of splitting up
	 * the side pots.
	 **/
	public void displaySidePotMessages() {
		if (messageWindow == null) {
			SidePotMessageBox sidePotWindow = new SidePotMessageBox(
					sidePotMessages);
			sidePotWindow.setBounds(60, 200, 400,
					75 + 20 * sidePotMessages.size());
			sidePotWindow
					.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
			sidePotWindow.setVisible(true);
		}
	}

	/***********************
	 * SidePotMessageBox class defines the message box that can be displayed to
	 * announce side pot winners.
	 **/
	class SidePotMessageBox extends JDialog {

		// ----------------------
		// Constructor
		//
		SidePotMessageBox(ArrayList messages) {
			super(mainWindow, "Side pot results");
			getContentPane().setLayout(new BorderLayout());
			JPanel messagePanel = new JPanel();
			messagePanel.setLayout(new GridLayout(messages.size(), 1));
			for (int i = 0; i < messages.size(); i++) {
				messagePanel.add(new JLabel((String) messages.get(i)));
			}

			JPanel buttonPanel = new JPanel();
			buttonPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
			JButton button = new JButton("OK");
			button.addActionListener(new OKAction());
			buttonPanel.add(button);
			getContentPane().add(messagePanel, BorderLayout.CENTER);
			getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		}

		// ----------------------
		// processWindowEvent() processes the close window event
		//
		protected void processWindowEvent(WindowEvent e) {
			if (e.getID() == WindowEvent.WINDOW_CLOSING) {
				dispose();
			}
			super.processWindowEvent(e);
		}

		// ----------------------
		// OKAction class is used to define the action that occurs when the OK
		// button is pressed.
		//
		class OKAction extends AbstractAction {

			// ----------------------
			// Constructor
			//
			OKAction() {
				super();
			}

			// ----------------------
			// The actionPerformed() function is called when the button is
			// pressed
			//
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		}
	}

	/***********************
	 * enditol() is used to kill PokerApp if necessary.
	 **/
	private void enditol() {
		log("PokerApp.enditol()", 3);
		if (clientSocket != null) {
			disconnectSocket(false);
		}
		closeLogFile();
		if (startUpApp == null) {
			if (mainWindow != null) {
				mainWindow.dispose();
			}
		} else {
			startUpApp.KillClient();
		}

		System.exit(1);
	}
}
