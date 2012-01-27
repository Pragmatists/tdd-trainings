/******************************************************************************************
 * AIApp.java                 PokerApp                                                    *
 *                                                                                        *
 *   Revision History                                                                     *
 * +---------+----------+---------------------------------------------------------------+ *
 * | Version | DATE     | Description                                                   | *
 * +---------+----------+---------------------------------------------------------------+ *
 * |  0.95   | 09/15/04 | Initial documented release                                    | *
 * |  0.96   | 09/17/04 | Check to see if in a game before sending BET FOLD command     | *
 * |  0.96   | 09/23/04 | Larger AI window to fit additional information                | *
 * |  0.98   | 12/06/04 | Check for startUpApp.getMainWindow() not to be null accessing | *
 * |  1.00   | 06/06/05 | Added logging.                                                | *
 * |  1.00   | 10/26/05 | Changed size of AIFrame window.                               | *
 * |  1.00   | 07/14/07 | Prepare for open source.  Header/comments/package/etc...      | *
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

package net.sourceforge.pokerapp.ai;

import net.sourceforge.pokerapp.*;
import java.util.ArrayList;
import java.util.Collections;
import java.net.Socket;

/****************************************************
 * AIApp is the application that controls an AI player in the game.  It acts and 
 * interfaces with the server almost exactly the same as a human player. Unlike PokerApp,
 * which controls human players, this class does not have a main function and therefore
 * can't be started independant of the server.
 *
 * @author Dan Puperi
 * @version 1.00
 *
 **/
public class AIApp extends PApp {

/**
 * The instance of the StartPoker class that started this class
 **/
    public         StartPoker         startUpApp;
 
    private        AIFrame            mainWindow;                    // The AIApp main window instance
    private        AIApp              theApp;                        // The instance of this AIApp class
    private        Player             thisPlayer;                    // The Player represented by this AIApp
    private        Socket             clientSocket;                  // Socket to the server 
    private        String             hostName;                      // The host name of the server
    private        PokerClient        client;                        // The client for this player
    private        int                port;                          // Port used to connect to the server 
    private        AIAction           action;                        // The class that describes how this AI acts.

/***********************
 * Constructor creates a new AIApp
 * 
 * @param a The StartPoker instance to which this AI belongs
 *
 **/
    public AIApp( StartPoker a ) {
        super( "ai" );
        theApp = this;
        startUpApp = a;
        theApp.init();
    }

/***********************
 * init() function is the first thing done when the AIApp class is created.  It initializes all of the variable and 
 *  displays the AIFrame window.
 **/
    public void init() {
        logging = startUpApp.logging;
        keepLogFile = startUpApp.keepLogFile;
        logLevel = startUpApp.logLevel;
        if ( logging ) {
            createLogFile();
        }
        gameLabels = startUpApp.getGameLabels();
        gameClasses = startUpApp.getGameClasses();
        aiLabels = startUpApp.getAILabels();
        aiClasses = startUpApp.getAIClasses();
        mainWindow = new AIFrame( "Java Poker - AI Player", theApp );
        java.awt.Toolkit tk = mainWindow.getToolkit();
        java.awt.Dimension screensize = tk.getScreenSize();    
        mainWindow.setBounds( 50,50,650,400 );
        mainWindow.setDefaultCloseOperation( javax.swing.WindowConstants.DISPOSE_ON_CLOSE );
        mainWindow.setVisible( true );
        if ( startUpApp.getMainWindow() != null ) {
            startUpApp.getMainWindow().sendMessage( "AI Window opened." );
        } else {
            System.out.println( "AI Window opened." );
        }
        log( "AI Window opened", 1 );
        startUpApp.log( "AI Window opened", 1 );
    }

/***********************
 * connectSocket() function is called when this AIApp is trying to connect to the server
 *
 * @param name The name of this AI player
 * @param The type of AI player
 *
 **/
    public void connectSocket( String name, String type ) {
        log( "AIApp.connectSocket( " + name + ", " + type + " )", 3 );
        hostName = startUpApp.getServerListener().getServerSocket().getInetAddress().getHostName();
        action = new AIAction( theApp, type );      
        try {
            port = startUpApp.getPort();
            clientSocket = new Socket( hostName, port );
            mainWindow.sendMessage( "Connection to " + hostName + " successful.  Will join next game when it starts." );
            thisPlayer = new Player( name, true );
            client = new PokerClient( this );
        } catch ( java.net.UnknownHostException e ) {
            mainWindow.sendMessage( "Can't find host " + hostName );
            log( "ERROR : Can't find host " + hostName );
            logStackTrace( e );
        } catch ( java.io.IOException e ) {
            mainWindow.sendMessage( "Error - unable to open socket" );
            log( "ERROR : Unable to open socket" );
            logStackTrace( e );
        } catch ( NumberFormatException e ) {
            mainWindow.sendMessage( "Error - bad port number" );
            log( "ERROR : bad port number " );
            logStackTrace( e );
        } catch ( Exception e ) {
            log( "Warning : caught exception trying to connect the AI to the server socket" );
            logStackTrace( e );
        }
        try { 
            Thread.sleep( 500 );
        } catch ( InterruptedException e )  { }
        messageToServer( "REQUEST DATA" );
    }

/***********************
 * disconnectSocket() function is called whenever this AIApp disconnects from the server.
 **/
    public void disconnectSocket() {
        log( "AIApp.disconnectSocket()", 3 );
        try {
            if ( clientSocket != null ) {
                if ( thisPlayer != null ) {
                    if ( ( thisPlayer.seat >= 0 ) && ( startUpApp.getGame() != null ) ) {
                        messageToServer( "BET FOLD" );
                    }
                }
                messageToServer( "LEAVE" );
                client.stopRunning();
                clientSocket.close();
                mainWindow.sendMessage( "Disconnected from " + hostName );
            }
            clientSocket = null;
            action.getAI().stopLogic();
            startUpApp.KillAIClient( theApp );
            getWindow().dispose();
        } catch ( Exception e ) {
            log( "Warning : caught exception while trying to disconnect from server" );
            logStackTrace( e );
        }
    }

/***********************
 * messageToServer() function is used to send a message to the server.
 *
 * @param m The message to send to the server
 *
 **/
    public void messageToServer( String m ) {
        if ( client != null ) {
            client.sendMessage( m );
        }
    }

/***********************
 * resetTable() function is called whenever a new game is about to start.  For the AI, all it does it reset the
 *  list of players in the game.
 **/
    public void resetTable() {
        log( "AIApp.resetTable()", 3 );
        playersRemoved = new ArrayList();
        if ( thisPlayer != null ) {
            thisPlayer.in = false;
        }
    }

/***********************
 * getPlayer() is used to access the private class variable
 * 
 * @return The Player class which belongs to this AIApp
 *
 **/
    public Player getThisPlayer() {
        return thisPlayer;
    }
    
/***********************
 * getClient() is used to access the private class variable
 * 
 * @return The client that this AIApp uses to connect to the server
 *
 **/  
    public PokerClient getClient() {
        return client;
    }
  
/***********************
 * getAIAction() is used to access the private class variable
 * 
 * @return The AIAction class which dictates how this AI acts when it is his turn
 *
 **/  
    public AIAction getAIAction() {
        return action;
    }
  
/***********************
 * getWindow() is used to access the private class variable
 * 
 * @return The AIFrame which is used to display data about this AIApp
 *
 **/  
    public AIFrame getWindow() {
        return mainWindow;
    }
  
/***********************
 * getSocker() is used to access the private class variable
 * 
 * @return The socket this AIApp is using to connect to the server
 *
 **/  
    public Socket getSocket() {
        return clientSocket;
    }
}
