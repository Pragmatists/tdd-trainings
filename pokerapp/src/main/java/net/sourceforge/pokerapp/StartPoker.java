/******************************************************************************************
 * StartPoker.java                 PokerApp                                               *
 *                                                                                        *
 *   Revision History                                                                     *
 * +---------+----------+---------------------------------------------------------------+ *
 * | Version | DATE     | Description                                                   | *
 * +---------+----------+---------------------------------------------------------------+ *
 * |  0.95   | 09/15/04 | Initial documented release                                    | *
 * |  0.96   | 09/17/04 | Additional check when client app is closed to not generate a  | *
 * |         |          | null pointer exception.                                       | *
 * |  0.96   | 09/20/04 | Fixed so that when a player leaves the server, his reg info   | *
 * |         |          | will not continued to be saved on the server. He can join     | *
 * |         |          | the server again without getting denied because of reg.       | *
 * |  0.96   | 09/20/04 | Fixed player getting kicked off of server because of taking   | *
 * |         |          | too long to respond.                                          | *
 * |  0.97   | 10/25/04 | Use encrypted communications to send all pokerapp data across | *
 * |         |          | the network.                                                  | *
 * |  0.98   | 12/06/04 | Add possibility of starting a dedicated server via command    | *
 * |         |          | line options. This includes lots of changes because it was    | *
 * |         |          | assumed the start window would always be there.               | *
 * |  0.98   | 12/06/04 | Added to loadInputFile so that it would read the new options. | *
 * |  0.98   | 12/07/04 | Take restart out of nullifyGame.                              | *
 * |  0.98   | 12/08/04 | Update initGame for bet limit and pot limit games.            | *
 * |  0.98   | 12/09/04 | Fixed error by making dealer = null if server couldn't find   | *
 * |         |          | the next dealer.                                              | *
 * |  0.98   | 12/13/04 | Fixed many of the problems in nextDealer() by making it       | *
 * |         |          | synchronized.                                                 | *
 * |  0.98   | 12/13/04 | Added variables playedInLastGame and firstBlindGamePlayed to  | *
 * |         |          | accomodate making all newcomers pay the big blind their       | *
 * |         |          | first game in.                                                | *
 * |  0.99   | 12/18/04 | Use class NoPreferenceFactory to solve a problem with         | *
 * |         |          | java.util.prefs warnings on Linux and Unix systems.           | *
 * |  0.99   | 05/17/05 | Send END GAME message in nullifyGame function.                | *
 * |  1.00   | 05/26/05 | Modify so that a client can be started as a Java Applet       | *
 * |  1.00   | 05/26/05 | Remove init() call when a client is started.  Unnecessary.    | *
 * |  1.00   | 06/05/05 | Add logging functions.                                        | * 
 * |  1.00   | 08/19/05 | Allow blinds and antes to be selected at the same time.       | *
 * |  1.00   | 10/25/05 | Log stack trace if messageToPlayer fails.                     | *
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

import net.sourceforge.pokerapp.ai.*;
import java.util.ArrayList;
import java.io.*;

/****************************************************
 * StartPoker is the application that will start the Poker server. It can also
 * be used to start a single poker client - however, if only the client is to be
 * be run, PokerApp should be started instead.
 * 
 * @author Dan Puperi
 * @version 1.00
 * 
 **/
public final class StartPoker extends PApp {

	/**
	 * Which players are showing their cards if they lost. Indexed by seat.
	 **/
	public boolean showingLostCards[];
	/**
	 * List of player sitting at table, waiting to join the game.
	 **/
	public ArrayList waitingPlayerList;
	/**
	 * List of players in the game, but will be gone when the game ends.
	 **/
	public ArrayList leavingPlayerList;
	/**
	 * List of players who were playing in the last game
	 **/
	public ArrayList playedInLastGame;
	/**
	 * Who is the dealer
	 **/
	public Player dealer;
	/**
	 * Who was the last dealer
	 **/
	public Player lastDealer;
	/**
	 * Are we playing?
	 **/
	public boolean inGame;
	/**
	 * The timer counting
	 **/
	public int timeCounter;
	/**
	 * The dealer left, therefore we need another
	 **/
	public boolean needNewDealer;
	/**
	 * Whether a game with blinds has been played on this server.
	 **/
	public boolean firstBlindGamePlayed;
	/**
	 * How this class was started.
	 **/
	public String startMethod;

	private static StartFrame startWindow; // The StartPoker main window
											// instance
	private static StartPoker startPokerApp; // The instance of this StartPoker
												// class
	private PokerServerListener serverListener; // Listens for connections to
												// the server
	private ServerFrame serverWindow; // The server window
	private PokerApp clientApp; // A PokerApp instance, if one is started from
								// the main window.
	private ArrayList serverThreadList; // A list of Threads to all the
										// connected players
	private PokerGame game; // The current game
	private Deck deck; // The deck
	private Dealer serverDealer; // The "house" dealer - used to autodeal games
	private PokerMoney pot; // The pot
	private javax.swing.Timer timer; // Timer used for timeouts
	private int portNum; // The port number the server is on.
	private ArrayList aiApps; // List of current running AIApps.
	private ArrayList persons; // List of registered players currently in the
								// game
	private ArrayList numbers; // List of registration numbers in the game.
	private ArrayList names; // List of player names that correspond to reg
								// numbers.
	private static File startLogFile; // The log file for startup.
	private static PrintWriter startLogWriter; // PrintWriter that writes to the
												// startup log file.

	/**********************
	 * usage() function is used to display the proper command line options.
	 **/
	private static void usage() {
		ArrayList info = new ArrayList();
		info.add(" ");
		info.add("PokerApp command line usage:");
		info.add("  java -jar PokerApp.jar [options]");
		info.add(" ");
		info.add("  If no options are given, PokerApp will start with the client/server GUI.");
		info.add("  The possible command line options are: ");
		info.add("    --client : start the PokerApp client");
		info.add("    --server : start the PokerApp server.  Must specify a port.");
		info.add("        --port=port number  : port specified to start the server on");
		info.add("        --nogui             : server will run without any interaction.");
		info.add("    --file=input_filename : Indicates file to read when starting PokerApp.");
		info.add("                            default is poker_inputs.txt");
		info.add(" ");

		for (int i = 0; i < info.size(); i++) {
			System.out.println(((String) info.get(i)));
			startLog(((String) info.get(i)));
		}
	}

	/**********************
	 * main() is called when StartPoker is involked from the command line. This
	 * is the only way to run StartPoker, so this is the entry point into the
	 * program.
	 * 
	 * @param args
	 *            Command line arguments to StartPoker
	 * 
	 **/
	public static void main(String[] args) {
		try {
			startLogFile = new File("pokerapp_startup_log.txt");
			startLogWriter = new PrintWriter(new FileOutputStream(startLogFile));
			startLog("Starting PokerApp...");
			System.setProperty("java.util.prefs.PreferencesFactory",
					"NoPreferencesFactory");
		} catch (Throwable t) {
			t.printStackTrace();
			startLogStackTrace(t);
			if (startLogWriter != null)
				startLogWriter.close();
			System.exit(1);
		}
		String fname = new String();
		if (args.length == 0) {
			startLog("No command line arguments specified...starting PokerApp in normal mode");
			startLogWriter.close();
			new StartPoker("normal");
		} else {
			System.out.println();
			String cs = new String("none");
			for (int i = 0; i < args.length; i++) {
				startLog("Processing argument: " + args[i]);
				if (args[i].equals("--applet")) {
					cs = new String("applet");
				}
				if (args[i].equals("--client")) {
					if (cs.equals("none")) {
						cs = new String("client");
					} else {
						System.out
								.println("ERROR : Cannot specify both client and server from the same command line");
						startLog("ERROR : Cannot specify both client and server from the same command line");
						usage();
						startLogWriter.close();
						System.exit(1);
					}
				} else if (args[i].equals("--server")) {
					if (cs.equals("none")) {
						cs = new String("server");
					} else {
						System.out
								.println("ERROR : Cannot specify both client and server from the same command line");
						startLog("ERROR : Cannot specify both client and server from the same command line");
						usage();
						startLogWriter.close();
						System.exit(1);
					}
				}
				if (args[i].startsWith("--file")) {
					int j = args[i].indexOf('=');
					fname = args[i].substring(j + 1, args[i].length());
				}
			}
			if (cs.equals("applet")) {
				startLog("PokerApp will be started as an applet.");
				startLogWriter.close();
				new StartPoker("applet");
			} else if (cs.equals("client")) {
				startLog("PokerApp will be started as a client.");
				if (fname.length() < 1) {
					startLog("  " + fname + " will be used as the input file.");
				}
				startLogWriter.close();
				new StartPoker("client" + " " + fname);
			} else if (cs.equals("server")) {
				startLog("PokerApp will be started as a server");
				String m = new String("server");
				int port = 0;
				for (int i = 1; i < args.length; i++) {
					if (args[i].equals("--nogui")) {
						m = new String("server_nogui");
						startLog("  The PokerApp server will not use a graphical interface");
					}
					if (args[i].startsWith("--port")) {
						int j = args[i].indexOf('=');
						port = Integer.parseInt(args[i].substring(j + 1,
								args[i].length()));
						startLog("  The PokerApp server will start on port "
								+ port);
					}
				}
				if (port == 0) {
					System.out
							.println("ERROR : must specify a port when starting a server");
					startLog("ERROR : must specify a port when starting a server");
					usage();
					startLogWriter.close();
					System.exit(1);
				} else {
					if (fname.length() < 1) {
						startLog("  " + fname
								+ " will be used as the input file.");
					}
					startLogWriter.close();
					new StartPoker(m + " " + port + " " + fname);
				}
			} else if (args[0].startsWith("--file")) {
				int j = args[0].indexOf('=');
				fname = args[0].substring(j + 1, args[0].length());
				startLog("PokerApp will start in normal mode");
				if (fname.length() < 1) {
					startLog("  " + fname + " will be used as the input file.");
				}
				startLogWriter.close();
				new StartPoker("normal " + fname);
			} else {
				System.out.println("ERROR : Bad command line options.");
				startLog("ERROR : Bad command line options.");
				usage();
				startLogWriter.close();
				System.exit(1);
			}
		}
	}

	/***********************
	 * Constructor to Start PokerApp as an Applet
	 * 
	 * @param a
	 *            The PokerApplet class which wants to create a new StartPoker
	 *            class
	 * 
	 **/
	public StartPoker(PokerApplet a) {
		super("applet");
		StartClient(a);
	}

	/***********************
	 * Constructor to Start PokerApp
	 * 
	 * @param method
	 *            A string description of how PoerApp is to be started.
	 * 
	 **/
	public StartPoker(String method) {
		super();
		startPokerApp = this;
		startMethod = method.trim();

		if (startMethod.startsWith("applet")) {
			StartClient((PokerApplet) null);
		}

		if (startMethod.startsWith("normal")) {
			int j = startMethod.indexOf(' ');
			if (j != -1) {
				INPUT_FNAME = startMethod
						.substring(j + 1, startMethod.length());
			}
			init();
		}

		if (startMethod.startsWith("client")) {
			int j = startMethod.indexOf(' ');
			if (j != -1) {
				INPUT_FNAME = startMethod
						.substring(j + 1, startMethod.length());
			}
			StartClient();
		}

		if (startMethod.startsWith("server")) {
			int j = startMethod.indexOf(' ');
			int k = startMethod.indexOf(' ', j + 1);
			int port;
			if (k > j) {
				port = Integer.parseInt(startMethod.substring(j + 1, k));
				INPUT_FNAME = startMethod
						.substring(k + 1, startMethod.length());
			} else {
				port = Integer.parseInt(startMethod.substring(j + 1,
						startMethod.length()));
			}

			init();

			if (startMethod.indexOf("nogui") != -1) {
				StartServer(port, false);
			} else {
				StartServer(port, true);
			}
		}
	}

	/***********************
	 * The init() function is called immediately after creating the StartPoker
	 * class. It is used to initialize all of the StartPoker variables and set
	 * up the application.
	 **/
	public void init() {
		PrintWriter log_writer = null;
		try {
			File log_file = new File(STARTUP_LOG_FNAME);
			log_writer = new PrintWriter(new FileOutputStream(log_file), true);
			if (log_writer == null) {
				System.out
						.println("ERROR : log_writer is null.  Cannot record startup messages for some reason.");
			}
		} catch (Exception x) {
			x.printStackTrace();
			System.exit(1);
		}
		log_writer.println("Initializing PokerApp");
		//
		// Read input data from input file.
		//
		loadInputData(log_writer);
		log_writer.println("Successful startup of PokerApp.");
		log_writer.close();
		parseRules();
		//
		// Initialize some server variables
		//
		deck = new Deck();
		pot = new PokerMoney();
		serverListener = null;
		serverThreadList = new ArrayList();
		aiApps = new ArrayList();
		waitingPlayerList = new ArrayList();
		leavingPlayerList = new ArrayList();
		playedInLastGame = new ArrayList();
		firstBlindGamePlayed = false;
		persons = new ArrayList();
		numbers = new ArrayList();
		names = new ArrayList();
		showingLostCards = new boolean[PokerGame.MAX_PLAYERS];
		for (int i = 0; i < PokerGame.MAX_PLAYERS; i++) {
			showingLostCards[i] = true;
		}
		inGame = false;
		needNewDealer = false;
		//
		// Create and display the main StartPoker main window
		//
		if (startMethod.startsWith("normal")) {
			startWindow = new StartFrame("PokerApp", startPokerApp);
			java.awt.Toolkit tk = startWindow.getToolkit();
			java.awt.Dimension screensize = tk.getScreenSize();
			startWindow.setBounds(10, 10, 320, 135);
			startWindow
					.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
			startWindow.setVisible(true);
		}
	}

	/***********************
	 * loadInputData() - The first thing that must be done is read the input
	 * file. This file contains information about the various options available
	 * for the game.
	 * 
	 * @param low_writer
	 *            The writer to the log_file so that data can be written to the
	 *            start up log
	 * 
	 **/
	private void loadInputData(PrintWriter log_writer) {
		File inputFile;
		BufferedReader inputDataReader;
		boolean EOF = false;
		String line;

		try {
			log_writer.println("Loading inputs from " + INPUT_FNAME);
			inputFile = new File(INPUT_FNAME);
			inputDataReader = new BufferedReader(new InputStreamReader(
					new FileInputStream(inputFile)));

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
						if (readingRules) {
							int i = line.indexOf('=');
							ruleNames.add(line.substring(0, i).trim());
							ruleValues.add(line.substring(i + 1).trim());
						}
						if (readingGames) {
							int i = line.indexOf(',');
							gameLabels.add(line.substring(0, i).trim());
							gameClasses.add(line.substring(i + 1).trim());
						}
						if (readingAI) {
							int i = line.indexOf(',');
							aiLabels.add(line.substring(0, i).trim());
							aiClasses.add(line.substring(i + 1).trim());
						}
						if (readingDealer) {
							int i = line.indexOf('=');
							if ((line.substring(0, i).trim())
									.equals("autoDealing")) {
								ruleNames.add(line.substring(0, i).trim());
								ruleValues.add(line.substring(i + 1).trim());
							}
							if ((line.substring(0, i).trim())
									.equals("dealGame")) {
								dealingGames.add(line.substring(i + 1).trim());
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
									if (logging)
										createLogFile();
								} catch (Exception x) {
									System.out
											.println("Warning : Could not set logLevel.");
									log_writer
											.println("Warning : Could not set logLevel.");
									x.printStackTrace();
								}
							} else {
								System.out
										.println("Warning : Bad variable in input file in the LOGGING block.");
								log_writer
										.println("Warning : Bad variable in input file in the LOGGING block.");
							}
						}
					} else {
						if (line.matches(".*RULES")) {
							log_writer.println("  Reading rules");
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
							log_writer.println("  Reading games available");
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
							log_writer.println("  Reading AIs available");
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
							log_writer.println("  Reading dealer variables");
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
							log_writer.println("  Reading logging variables");
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
			System.out.println("ERROR - reading input file: " + INPUT_FNAME);
			log_writer.println("ERROR - reading input file: " + INPUT_FNAME);
			log_writer.close();
			System.exit(1);
		} catch (Exception e) {
			e.printStackTrace();
			log_writer.close();
			System.exit(1);
		}
	}

	/***********************
	 * StartServer() function starts the poker server on the specified port
	 * number.
	 * 
	 * @param p
	 *            The port number on which to start a server
	 * @param gui
	 *            A flag indicating whether the server will have a graphical
	 *            user interface or not
	 * 
	 **/
	public void StartServer(int p, boolean gui) {
		log("StartPoker.StartServer( " + p + ", " + gui + " )", 3);
		if (startWindow != null) {
			startWindow.startServerButton.setEnabled(false);
		}
		//
		// Create the server listener and start listening for connections from
		// clients.
		//
		portNum = p;
		serverListener = new PokerServerListener(this, portNum);
		//
		// Create the dealer and start dealing if he's supposed to
		//
		serverDealer = new Dealer(startPokerApp);
		if (autoDealing) {
			serverDealer.startDealing();
		}
		//
		// Create the timer that can be used to kick a non-responsive player
		//
		timeCounter = 0;
		timer = new javax.swing.Timer(1000, new timerAction());
		timer.start();

		try {
			Thread.sleep(100);
		} catch (InterruptedException x) {
		}
		//
		// Create and display the poker server window
		//
		if (serverListener.getStatus()) {
			if (gui) {
				serverWindow = new ServerFrame("PokerApp Server", startPokerApp);
				java.awt.Toolkit tk = serverWindow.getToolkit();
				java.awt.Dimension screensize = tk.getScreenSize();
				serverWindow.setBounds(300, 300, 400, 400);
				serverWindow
						.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
				serverWindow.setVisible(true);
			}
		} else {
			if (startWindow != null)
				startWindow.startServerButton.setEnabled(true);
		}
	}

	/***********************
	 * KillServer() function is called when the poker server is shut down.
	 **/
	public void KillServer() {
		log("StartPoker.KillServer()", 3);
		if (serverListener != null) {
			broadcastMessage("SERVER KILLED.");
			timer.stop();
			serverListener.stopListening();
			serverListener = null;
			serverDealer.killDealer();
			serverDealer = null;
			serverWindow.dispose();
			log("Server closed.", 1);
			if (startWindow != null) {
				startWindow.startServerButton.setEnabled(true);
				startWindow.sendMessage("Server closed.");
			} else {
				closeLogFile();
				System.out.println("Server closed.");
				System.exit(0);
			}
		}
	}

	/***********************
	 * StartClient() function is called when the Join game button is pushed. It
	 * starts an instance of the PokerApp class.
	 **/
	public void StartClient() {
		if (startWindow != null) {
			startWindow.startClientButton.setEnabled(false);
		}
		clientApp = new PokerApp(this, INPUT_FNAME);
	}

	/***********************
	 * StartClient() function is called when the PokerApplet is started. It
	 * starts an instance of the PokerApp class
	 * 
	 * @param a
	 *            The PokerApplet to which the client belongs
	 * 
	 **/
	public void StartClient(PokerApplet a) {
		clientApp = new PokerApp(a, APPLET_INPUTS);
	}

	/***********************
	 * KillClient() function is called when the client started using StartPoker
	 * is closed.
	 **/
	public void KillClient() {
		log("StartPoker.KillClient()", 3);
		if (clientApp != null) {
			if (clientApp.getWindow() != null) {
				clientApp.getWindow().dispose();
			}
			clientApp.closeLogFile();
			clientApp = null;
			if (startWindow != null) {
				startWindow.sendMessage("Client closed.");
				startWindow.startClientButton.setEnabled(true);
			} else {
				System.out.println("Client closed.");
			}
			log("Client closed", 1);
		}
	}

	/***********************
	 * StartAIClient() function is called when the server adds an AI player.
	 **/
	public void StartAIClient() {
		log("StartPoker().StartAIClient()", 3);
		aiApps.add(new AIApp(this));
	}

	/***********************
	 * KillAIClient() function is called when an AI player is shut down.
	 * 
	 * @param a
	 *            The AIApp which is to be killed
	 * 
	 **/
	public void KillAIClient(AIApp a) {
		log("StartPoker().KillAIClient( " + a + " )", 3);
		if (aiApps.contains(a)) {
			log("Removing AIApp " + a + " from server.", 1);
			a.closeLogFile();
			aiApps.remove(aiApps.indexOf(a));
			aiApps.trimToSize();
			a = null;
		}
	}

	/***********************
	 * getAIHand() is used by the AI to get their own hand from the server.
	 * 
	 * @param a
	 *            The AIApp whose hand is requested
	 * @return The Hand of the requested AIApp
	 * 
	 **/
	public Hand getAIHand(AIApp a) {
		log("StartPoker.getAIHand( " + a + " )", 3);
		if (aiApps.contains(a)) {
			String name = a.getThisPlayer().getName();
			int pindex = playerIndex(name);

			if ((pindex < 0) || (pindex >= playerList.size())) {
				return null;
			}

			return ((Player) playerList.get(pindex)).getHand();
		}

		log("Warning : tried to call StartPoker.getAIHand(), but AIApp wasn't in server list.");
		log("          From " + StartPoker.getCallerClassName() + "."
				+ StartPoker.getCallerMethodName());
		log("          returning null");
		return null;
	}

	/***********************
	 * broadcastMessage() function sends out a string message to all players who
	 * have connected to this server
	 * 
	 * @param m
	 *            The message to send out
	 * 
	 **/
	public void broadcastMessage(String m) {
		for (int i = 0; i < serverThreadList.size(); i++) {
			try {
				((PokerMultiServerThread) (serverThreadList.get(i)))
						.getStreamOut().println(PokerProtocol.makeString(m));
			} catch (NullPointerException e) {
			}
		}
	}

	/***********************
	 * messageToPlayer() function is used to send a message to a single player
	 * whose name is specified in the first argument.
	 * 
	 * @param n
	 *            The name of the player to send the message
	 * @param m
	 *            The message to send
	 * 
	 **/
	public void messageToPlayer(String n, String m) {
		for (int i = 0; i < serverThreadList.size(); i++) {
			try {
				String sname = ((PokerMultiServerThread) serverThreadList
						.get(i)).getPlayerName();
				if (n.equals(sname)) {
					((PokerMultiServerThread) serverThreadList.get(i))
							.getStreamOut()
							.println(PokerProtocol.makeString(m));
				}
			} catch (NullPointerException e) {
				logStackTrace(e);
			}
		}
	}

	/***********************
	 * getCallerClassName() returns a string that was the class name of the
	 * calling class This is useful for not only debugging, but security
	 * purposes.
	 * 
	 * @return The String name of the class which called the function.
	 * 
	 **/
	public static String getCallerClassName() {
		Throwable t = new Throwable();
		t.fillInStackTrace();
		StackTraceElement[] trace = t.getStackTrace();
		return trace[2].getClassName();
	}

	/***********************
	 * getCallerMethodName() returns a string that was the method in the class
	 * which called this function This is useful for not only debugging, but
	 * security purposes.
	 * 
	 * @return The String name of the function/method which call the current
	 *         function
	 * 
	 **/
	public static String getCallerMethodName() {
		Throwable t = new Throwable();
		t.fillInStackTrace();
		StackTraceElement[] trace = t.getStackTrace();
		return trace[2].getMethodName();
	}

	/***********************
	 * initGame() function is called when a new game is started. It initializes
	 * the server variables required for a new game.
	 * 
	 * @param gameName
	 *            The name of the game about the be started.
	 * 
	 **/
	public void initGame(String gameName) {
		log("StartPoker.initGame( " + gameName + " )", 3);
		String gameTitle = new String();
		if (noLimit) {
			gameTitle = "No Limit " + gameName;
		} else if (betLimit) {
			gameTitle = "" + maximumBet + " limit " + gameName;
		} else if (potLimit) {
			gameTitle = "Pot limit " + gameName;
		} else {
			PokerMoney d = new PokerMoney(2.0f * minimumBet.amount());
			gameTitle = "" + minimumBet + "/" + d + " " + gameName;
		}
		if (serverWindow != null) {
			serverWindow.sendMessage("Starting a game of " + gameTitle);
		} else {
			System.out.println("Starting a game of " + gameTitle);
		}
		log("Starting a game of " + gameTitle, 1);
		broadcastMessage("MESSAGE &Starting a game of " + gameTitle);
		deck.shuffle();
		resetPot();
		inGame = true;
	}

	/***********************
	 * updateMoneyLine() function tells each of the clients that something
	 * happened and they should update their displays.
	 * 
	 * @param b
	 *            The current bet to place each client display
	 * @param name
	 *            The player name who has the highest bet
	 * 
	 **/
	public void updateMoneyLine(PokerMoney b, String name) {
		log("StartPoker.updateMoneyLine( " + b + ", " + name + " )", 3);
		for (int i = 0; i < serverThreadList.size(); i++) {
			int pindex = playerFromServerThreadIndex(i);
			if (pindex != -1) {
				String message = PokerProtocol.makeString(new String(
						"MONEYLINE  &"
								+ ((Player) playerList.get(pindex))
										.getBankroll().amount()
								+ "&"
								+ ((Player) playerList.get(pindex)).getBet()
										.amount() + "&" + pot.amount() + "&"
								+ b.amount() + "&" + name));
				((PokerMultiServerThread) serverThreadList.get(i))
						.getStreamOut().println(message);
			} else {
				int windex = -1;
				for (int j = 0; j < waitingPlayerList.size(); j++) {
					if (name.equals(((Player) waitingPlayerList.get(j))
							.getName())) {
						windex = j;
					}
				}
				if (windex != -1) {
					String message = PokerProtocol.makeString(new String(
							"MONEYLINE  &"
									+ ((Player) waitingPlayerList.get(windex))
											.getBankroll().amount()
									+ "&"
									+ ((Player) waitingPlayerList.get(windex))
											.getBet().amount() + "&"
									+ pot.amount() + "&" + b.amount() + "&"
									+ name));
					((PokerMultiServerThread) serverThreadList.get(i))
							.getStreamOut().println(message);
				} else {
					String message = PokerProtocol.makeString(new String(
							"MONEYLINE  &" + new PokerMoney().amount() + "&"
									+ new PokerMoney().amount() + "&"
									+ pot.amount() + "&" + b.amount() + "&"
									+ name));
					((PokerMultiServerThread) serverThreadList.get(i))
							.getStreamOut().println(message);
				}
			}
		}
	}

	/***********************
	 * nullifyGame() function is used to make the StartPoker variable game=null.
	 * This does not stop the game cleanly.
	 **/
	public void nullifyGame() {
		log("StartPoker.nullifyGame()", 3);
		game = null;
		inGame = false;
		broadcastMessage("END GAME");
	}

	/***********************
	 * setGame() function is used to start a new game. If the game doesn't start
	 * cleanly, it is nullified.
	 * 
	 * @param g
	 *            The PokerGame to be started.
	 * 
	 **/
	public void setGame(PokerGame g) {
		log("StartPoker.setGame( " + g + " )", 3);
		game = g;
		if (game.getGameError()) {
			nullifyGame();
		}
	}

	/***********************
	 * resetPot() function is used to set the pot back to 0.00
	 **/
	public void resetPot() {
		pot = new PokerMoney();
	}

	/***********************
	 * broadcastRules() function is called whenever the server changes the
	 * rules. It sends the new rules to all the players.
	 **/
	public void broadcastRules() {
		log("StartPoker.broadcaseRules()", 3);
		broadcastMessage("RESET RULES");
		for (int k = 0; k < ruleNames.size(); k++) {
			broadcastMessage("RULE ADD &" + ruleNames.get(k) + "="
					+ ruleValues.get(k));
		}
		if (autoDealing) {
			for (int k = 0; k < dealingGames.size(); k++) {
				broadcastMessage("DEALING GAME &" + dealingGames.get(k));
			}
		}
		broadcastMessage("REFRESH");
	}

	/***********************
	 * serverThreadFromPlayerIndex() will return the index in serverThreadList
	 * given the index in player list
	 * 
	 * @param pindex
	 *            The player index to look up
	 * @return The server thread index of that player
	 * 
	 **/
	public int serverThreadFromPlayerIndex(int pindex) {
		String pname = ((Player) playerList.get(pindex)).getName();
		for (int i = 0; i < serverThreadList.size(); i++) {
			String sname = ((PokerMultiServerThread) serverThreadList.get(i))
					.getPlayerName();
			if (sname.equals(pname)) {
				log("Player index " + pindex + " is serverThread index " + i, 3);
				return i;
			}
		}
		log("Warning : couldn't find player index " + pindex
				+ " in serverThread list.");
		log("          From " + getCallerClassName() + "."
				+ getCallerMethodName());
		log("          Returning -1 for serverThread index");
		return -1;
	}

	/***********************
	 * playerFromServerThreadIndex() Will return the index in serverThreadList
	 * given the index in player list
	 * 
	 * @param The
	 *            server thread index to lookup
	 * @return The player index of that server thread.
	 * 
	 **/
	public int playerFromServerThreadIndex(int sindex) {
		String sname = ((PokerMultiServerThread) serverThreadList.get(sindex))
				.getPlayerName();
		log("serverThread index " + sindex + " is player " + sname, 3);
		return playerIndex(sname);
	}

	/***********************
	 * nextDealer() finds the next available dealer.
	 **/
	public synchronized void nextDealer() {
		log("StartPoker.nextDealer()", 3);
		lastDealer = dealer;
		boolean dealerLeaving = true;
		int attempts = 0;
		needNewDealer = false;
		if (dealer != null) {
			while (dealerLeaving) {
				attempts++;
				dealerLeaving = false;
				int dealerSeat = nextSeat(dealer.seat);
				if (dealerSeat < 0) {
					dealerSeat = dealer.seat;
				}
				dealerIndex = getPlayerInSeat(dealerSeat);
				if ((dealerIndex >= playerList.size()) || (dealerIndex < 0)) {
					dealerLeaving = true;
				} else {
					dealer = (Player) playerList.get(dealerIndex);
					float mincash = smallBlind.amount();
					if (blinds) {
						mincash = 2.0f * smallBlind.amount();
					}
					if (antes) {
						mincash = mincash + ante.amount();
					}

					if (dealer.getBankroll().amount() < mincash) {
						dealerLeaving = true;
						broadcastMessage("MESSAGE &"
								+ dealer.getName()
								+ " would be the next dealer, but doesn't have enough money to continue");
						if (serverWindow != null) {
							serverWindow
									.sendMessage(dealer.getName()
											+ " would be the next dealer, but doesn't have enough money to continue");
						} else {
							System.out
									.println(dealer.getName()
											+ " would be the next dealer, but doesn't have enough money to continue");
						}
						log(""
								+ dealer.getName()
								+ " would be the next dealer, but doesn't have enough money to continue",
								1);
					}
				}
				if (attempts >= PokerGame.MAX_PLAYERS) {
					dealerLeaving = false;
				}
			}
			if (attempts >= PokerGame.MAX_PLAYERS) {
				log("Warning : Could not find a next dealer.");
				dealer = null;
				lastDealer = null;
				return;
			}
			broadcastMessage("DEALER  &" + dealer.getName());
			broadcastMessage("REFRESH");
		}
	}

	/***********************
	 * nextSeat() is used by nextDealer to find the next occupied seat
	 * 
	 * @param currSeat
	 *            The seat from which to find the next
	 * @return The next occupied seat after currSeat
	 * 
	 **/
	public int nextSeat(int currSeat) {
		ArrayList leavingList = leavingPlayerList;
		ArrayList plist = playerList;
		if ((currSeat < 0) || (currSeat >= PokerGame.MAX_PLAYERS)) {
			return -9;
		}
		for (int i = currSeat + 1; i < PokerGame.MAX_PLAYERS; i++) {
			int pindex = getPlayerInSeat(i);
			if (pindex != -9) {
				boolean playerLeaving = false;
				for (int k = 0; k < leavingList.size(); k++) {
					if (((Player) plist.get(pindex)).getName().equals(
							(String) leavingList.get(k))) {
						playerLeaving = true;
					}
				}
				if (!playerLeaving) {
					log("Next seat from seat " + currSeat + " is " + i, 3);
					return i;
				}
			}
		}
		for (int j = 0; j <= currSeat; j++) {
			int pindex = getPlayerInSeat(j);
			if (pindex != -9) {
				boolean playerLeaving = false;
				for (int k = 0; k < leavingList.size(); k++) {
					if (((Player) plist.get(pindex)).getName().equals(
							(String) leavingList.get(k))) {
						playerLeaving = true;
					}
				}
				if (!playerLeaving) {
					log("Next seat from seat " + currSeat + " is " + j, 3);
					return j;
				}
			}
		}
		log("Warning : could not find a next seat from seat " + currSeat);
		log("          From " + getCallerClassName() + "."
				+ getCallerMethodName());
		log("          Returning -9 nex seat");
		return -9;
	}

	/***********************
	 * getGame() is used to access the private class variable
	 * 
	 * @return The current game.
	 * 
	 **/
	public PokerGame getGame() {
		return game;
	}

	/***********************
	 * getPot() is used to access the private class variable
	 * 
	 * @return The current pot value
	 * 
	 **/
	public PokerMoney getPot() {
		return pot;
	}

	/***********************
	 * getPort() is used to access the private class variable
	 * 
	 * @return The port that this game is connecting on
	 * 
	 **/
	public int getPort() {
		return portNum;
	}

	/***********************
	 * getServerThreadList() is used to access the private class variable
	 * 
	 * @return The list of current threads atteched to this server
	 * 
	 **/
	public ArrayList getServerThreadList() {
		return serverThreadList;
	}

	/***********************
	 * getAIApps() is used to access the private class variable
	 * 
	 * @return The list of AIApps playing in this game
	 * 
	 **/
	public ArrayList getAIApps() {
		return aiApps;
	}

	/***********************
	 * getDeck() is used to access the private class variable
	 * 
	 * @return The deck of cards
	 * 
	 **/
	public Deck getDeck() {
		return deck;
	}

	/***********************
	 * getMainWindow() is used to access the private class variable
	 * 
	 * @return The main window of the StartPoker class.
	 * 
	 **/
	public StartFrame getMainWindow() {
		return startWindow;
	}

	/***********************
	 * getServerWindow() is used to access the private class variable
	 * 
	 * @return The main server window if one was opened
	 * 
	 **/
	public ServerFrame getServerWindow() {
		return serverWindow;
	}

	/***********************
	 * getServerListener() is used to access the private class variable
	 * 
	 * @return The PokerServerListener class this server is using
	 * 
	 **/
	public PokerServerListener getServerListener() {
		return serverListener;
	}

	/***********************
	 * getClientApp() is used to access the private class variable
	 * 
	 * @return The PokerApp client that was started by this class (if
	 *         applicable)
	 * 
	 **/
	public PokerApp getClientApp() {
		return clientApp;
	}

	/***********************
	 * getServerDealer() is used to access the private class variable
	 * 
	 * @return The dealer (in case of the server automatically dealing)
	 * 
	 **/
	public Dealer getServerDealer() {
		return serverDealer;
	}

	/***********************
	 * getShowingLostCards() is used to figure out which players will show their
	 * cards even if they lost. A little more complicated than the standard
	 * 'get' function as a name must be provided
	 * 
	 * @param name
	 *            The player being inquired about
	 * @return Whether or not the player is showing his/her lost cards
	 * 
	 **/
	public boolean getShowingLostCards(String name) {
		for (int i = 0; i < PokerGame.MAX_PLAYERS; i++) {
			if (name.equals(((Player) playerList.get(i)).getName())) {
				if ((seatIndex(name) >= 0)
						&& (seatIndex(name) < PokerGame.MAX_PLAYERS)) {
					return showingLostCards[seatIndex(name)];
				}
			}
		}
		return true;
	}

	/***********************
	 * startLog() Record a line to the startup log file.
	 * 
	 * @param s
	 *            The line of text to record to the log file.
	 * 
	 **/
	public static void startLog(String s) {
		if (startLogFile == null) {
			System.out
					.println("ERROR : Tried to write to startup log file, but the log file is null.");
			return;
		}
		if (startLogWriter == null) {
			System.out
					.println("ERROR : Tried to write to startup log file, but the log file is null.");
			return;
		}
		startLogWriter.println(s);
		startLogWriter.flush();
	}

	/***********************
	 * startLogStackTrace() is used to write stackTrace to log file rather than
	 * System.out
	 * 
	 * @param t
	 *            The Throwable which contains the stack trace to write to the
	 *            log file
	 * 
	 **/
	public static void startLogStackTrace(Throwable t) {
		StackTraceElement[] trace = t.getStackTrace();
		if ((startLogFile != null) && (startLogWriter != null)) {
			for (int i = 0; i < trace.length; i++) {
				startLog(trace[i].toString());
			}
		} else {
			System.out
					.println("Warning : tried to write the stack trace to the startup log file, but could not");
			t.printStackTrace();
		}
	}

	/***********************
	 * Class timerAction is a 1 second timer - used to keep track of when a
	 * player is getting lazy
	 **/
	class timerAction implements java.awt.event.ActionListener {

		// ----------------------
		// Constructor
		//
		timerAction() {
			super();
		}

		// ----------------------
		// actionPerformed() function is called every time the timer reaches its
		// timeout value (every second).
		//
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (!inGame && (waitingPlayerList.size() > 0)) {
				log("Time to allow players to join game", 3);
				for (int i = 0; i < waitingPlayerList.size(); i++) {
					playerList.add((Player) waitingPlayerList.get(i));
				}
				waitingPlayerList = new ArrayList();
				updateMoneyLine(new PokerMoney(), new String());
				broadcastMessage("REFRESH");
			}
			if (!inGame && (leavingPlayerList.size() > 0)) {
				log("Time to remove players from playerList", 3);
				for (int i = 0; i < leavingPlayerList.size(); i++) {
					String name = (String) leavingPlayerList.get(i);
					if (dealer != null) {
						if ((name.equals(dealer.getName()))
								|| (dealerIndex == playerIndex(name))) {
							needNewDealer = true;
						}
					}
					dropPlayer(name);
					broadcastMessage("PLAYER REMOVE &" + name);
					if (serverWindow != null) {
						serverWindow.sendMessage(name + " left the table.");
					} else {
						System.out.println(name + " left the table.");
					}
					log(name + " left the table.", 1);
					for (int j = 0; j < aiApps.size(); j++) {
						if (name.equals(((AIApp) aiApps.get(j)).getThisPlayer()
								.getName())) {
							((AIApp) aiApps.get(j)).disconnectSocket();
						}
					}
				}
				if (dealer != null) {
					dealerIndex = playerIndex(dealer.getName());
				}
				if (dealerIndex >= playerList.size()) {
					needNewDealer = true;
				}
				leavingPlayerList = new ArrayList();
				playersRemoved = new ArrayList();
				updateMoneyLine(new PokerMoney(), new String());
				broadcastMessage("REFRESH");
			}
			if (!inGame && needNewDealer) {
				log("Time to look for a new dealer", 3);
				Player prevDealer = lastDealer;
				nextDealer();
				lastDealer = prevDealer;
				needNewDealer = false;
			}
			if (inGame && kickPlayer) {
				timeCounter++;
				Player player = (Player) playerList.get(game.currPlayerIndex);
				if (timeCounter >= kickTimeout) {
					messageToPlayer(player.getName(), "KICKED");
					timeCounter = 0;
				} else {
					if (timeCounter >= kickTimeout - 1) {
						broadcastMessage("MESSAGE  &" + player.getName()
								+ " has 1 second to respond or will be folded.");
					} else if (timeCounter >= kickTimeout - 2) {
						broadcastMessage("MESSAGE  &"
								+ player.getName()
								+ " has 2 seconds to respond or will be folded.");
					} else if (timeCounter >= kickTimeout - 3) {
						broadcastMessage("MESSAGE  &"
								+ player.getName()
								+ " has 3 seconds to respond or will be folded.");
					} else if (timeCounter >= kickTimeout - 4) {
						broadcastMessage("MESSAGE  &"
								+ player.getName()
								+ " has 4 seconds to respond or will be folded.");
					} else if (timeCounter >= kickTimeout - 5) {
						broadcastMessage("MESSAGE  &"
								+ player.getName()
								+ " has 5 seconds to respond or will be folded.");
					}
				}
			}
		}
	}
}