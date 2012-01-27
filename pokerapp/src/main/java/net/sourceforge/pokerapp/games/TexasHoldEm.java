/******************************************************************************************
 * TexasHoldEm.java                PokerApp                                               *
 *                                                                                        *
 *   Revision History                                                                     *
 * +---------+----------+---------------------------------------------------------------+ *
 * | Version | DATE     | Description                                                   | *
 * +---------+----------+---------------------------------------------------------------+ *
 * |  0.95   | 09/15/04 | Initial documented release                                    | *
 * |  0.96   | 09/27/04 | Add calcBestPossible routine to help the AI logic             | *
 * |  0.99   | 05/17/05 | Added logic to PLAYER TURN call for structured betting games  | *
 * |  0.99   | 05/19/05 | Define showPlayer() and move showCards() to PokerGame class   | *
 * |  0.99   | 05/22/05 | Add another construstor to make varients possible.  Also make | *
 * |         |          | private functions protected.                                  | *
 * |  1.00   | 06/07/05 | Added logging.                                                | *
 * |  1.00   | 08/19/05 | Added lots of comments so that this code might be readable by | *
 * |         |          | somebody else to create new game types.                       | *
 * |  1.00   | 07/16/07 | Prepare for open source.  Header/comments/package/etc...      | *
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

// Any game must be part of the Poker.Games package
//
package net.sourceforge.pokerapp.games;

// Must import the entire Poker package.
// 
import net.sourceforge.pokerapp.*;


/****************************************************
 * Texas Hold'em game definition.  This code is also released as an example game code.
 * I tried to document the code inline as much as possible.  Also at the end is a long 
 * writeup of possible functions and messages that can be used for the game.
 *
 * Make sure the name of the class is the exact same as the source filename without the .java
 * Must extend either the PokerGame class (which has mostly common functions) or can extend 
 * another game if that game was set up to be extended.
 *
 * @author Dan Puperi
 * @version 1.00
 *
 **/
public class TexasHoldEm extends PokerGame {

/**********************
 * TexasHoldEm class variables
 *
 * Place any class variables that may be needed here.  These variable will be accessible by the entire class.
 *
 **/


/***********************
 * Constructor
 *
 *  Must have a constructor in the game class.  There are two constructors because Texas Hold'Em can be extened with other game variations.
 *  In either case the StartPoker class must be given.  The game name must match the game name given in the poker_inputs.txt file.
 *  The selfInit flag designates whether the basic initialization defined in the PokerGame class should be used or not.  Many games can set
 *  selfInit to false, but for complete control of what happens on game startup, set selfInit to true and indicate all steps that need to
 *  be taken by the game to collect blinds/antes and initialize the game.  The constructor must do the following:
 *    1. A call to super (which does most of the work).
 *    2. Set maxActionNum - which is the number of rounds in the game.  (For Texas Hold'Em the rounds are: preflop, flop, turn, river, show for 5 actions).
 *    3. The third line is mandatory so that the game quits if there are not enough players.
 *    4. The status of the game is logged - not necessary, but useful for troubleshooting.
 *    5. The deal() function is called to deal the initial cards.  (notice that deal() is not called for the second constructor because the final
 *       game constructor should be what calls the deal function.
 **/
    public TexasHoldEm( StartPoker a ) {
        super( a, "Texas Hold'Em", false );
        maxActionNum = 5;
        if ( theApp.getPlayerList().size() < 2 ) {
            return;
        }
        theApp.log( "Constructing a game of Texas Hold'Em", 3 );
        deal();
    }

    public TexasHoldEm( StartPoker a, String gameName, boolean selfInit ) {
        super( a, gameName, selfInit );
        maxActionNum = 5;
        if ( theApp.getPlayerList().size() < 2 ) {
            return;
        }
        theApp.log( "Constructing a game of Texas Hold'Em", 3 );
    }

/***********************
 * deal() deals the initial cards.  This function must be included in the game definition.  
 **/
    protected void deal() {

//    Log that the program made it to this function.
//
        theApp.log( "TexasHoldEm.deal()", 3 );

//    Initialize variable c - which is a Card.
//
        Card  c = new Card();

//    Set the bestPossible variable - this is not necessary, but is used by the AI to help figure out the best possible hand that can be made. 
//
        bestPossible = calcBestPossible();

//   First card down
//     Loop through all players and give them their first card.
//
        for ( int i = 0; i < theApp.getPlayerList().size(); i++ ) {
        
//     Set c to the top card from the deck and add it to the players hand (in the hole).
//     Can also add it to their hand face up by calling addUpCard( c ) instead of addHoleCard( c ).
//   
            c = theApp.getDeck().deal();
            ((Player)theApp.getPlayerList().get(i) ).getHand().addHoleCard( c );

//     Now let this player know they just got a card in their hole.  The '0' indicated graphical position - 0 being the farthest left card displayed.
//
            theApp.messageToPlayer( ((Player)theApp.getPlayerList().get(i) ).getName(), "CARD HOLE  &" + ( (Player)theApp.getPlayerList().get(i) ).getName() + "&0&" + c );

//     Let the other players know that this player got a card in position 0 - but don't tell them which card it is.  (as opposed to the previous line where c is sent
//     as part of the message to the player.
//
            theApp.broadcastMessage( "CARD HOLE  &" + ((Player)theApp.getPlayerList().get(i)).getName() + "&0" );      
        }

//   Second card down
//     Again loop through all players and give them their second card.  Same thing happens as the first card except the second card is placed in position 1.
//
        for ( int i = 0; i < theApp.getPlayerList().size(); i++ ) {
            c = theApp.getDeck().deal();
            ((Player)theApp.getPlayerList().get(i)).getHand().addHoleCard( c );
            theApp.messageToPlayer( ((Player)theApp.getPlayerList().get(i) ).getName(), "CARD HOLE  &" + ( (Player)theApp.getPlayerList().get(i) ).getName() + "&1&" + c );
            theApp.broadcastMessage( "CARD HOLE  &" + ((Player)theApp.getPlayerList().get(i)).getName() + "&1" );
        }

//     Send out a message to everybody which will be displayed on their "message line".
// 
        theApp.broadcastMessage( "MESSAGE  &Playing Texas Hold'Em.  " + ( (Player)theApp.getPlayerList().get(currPlayerIndex) ).getName() + "'s bet.");

//     Must let the players know whose turn it is.  If the game is a structured betting game and blinds are being played, then the raise button should be enabled,
//     because anybody can raise the big blind.  All bet values are enabled for the other game types so its not needed.
//
        if ( ( theApp.blinds ) && !( theApp.noLimit || theApp.potLimit || theApp.betLimit ) ) {
            theApp.broadcastMessage( "PLAYER TURN &" + ((Player)theApp.getPlayerList().get(currPlayerIndex)).getName() + "&enable raise" );
        } else {
            theApp.broadcastMessage( "PLAYER TURN &" + ((Player)theApp.getPlayerList().get(currPlayerIndex)).getName() );
        }
    }

/***********************
 * nextAction() determines what happens next.  The next action function gets called by the main PokerGame class at the end of a "round"
 **/
    protected void nextAction() {

//     Log that the program made it to this function.
//    
        theApp.log( "TexasHoldEm.nextAction() - previous actionNum = " + actionNum, 3 );

//     Increment the actionNum variable - required.
//
        actionNum++;

//     Reset some betting variables for all of the players - required.
//
        for ( int i = 0; i < theApp.getPlayerList().size(); i++ ) {
            ( (Player)theApp.getPlayerList().get(i) ).potOK = false;
            ( (Player)theApp.getPlayerList().get(i) ).setBet( 0.0f );
            ( (Player)theApp.getPlayerList().get(i) ).setPrevBet( 0.0f );
        }

//     Reset some game variables if this game is not finished.  If actionNum = maxActionNum, all thats left to do is the showdown.
//
        if ( actionNum != maxActionNum ) {
            currBet = new PokerMoney( 0.0f );
            currPlayerIndex = firstToBet();
            if ( ( currPlayerIndex < 0 ) || ( currPlayerIndex >= theApp.getPlayerList().size() ) ) {    
                actionNum = maxActionNum;
                skipShow = true;
            } else {
                highBettor = (Player)theApp.getPlayerList().get(currPlayerIndex);
            }
        }

//     Resets the "money line" on all players display with their current bet and which player is the high bettor.
//
        theApp.updateMoneyLine( currBet, highBettor.getName() );

//     This switch statement is the important piece of code which designates what to do for each action.  actionNum is initialized = 1, so the switch must
//     check for the cases where actionNum goes from 2 to maxActionNum (defined in the constructor above).  For each case it calls the required functions.
//     Setting bestPossible is not necessary, but can be helpful for any AI players that use that variable.  The show() function should always be called when
//     actionNum = maxActionNum.
//
        switch ( actionNum ) {
            case 2 :
                flop();
                bestPossible = calcBestPossible();
                break;
            case 3 :
                turn();
                bestPossible = calcBestPossible();
                break;
            case 4 :
                river();
                bestPossible = calcBestPossible();
                break;
            case 5 :
                show();
                break;
            default :
                break;  
        }
    }

/***********************
 * flop() deals the Flop - 3 shared cards
 **/
    protected void flop() {

//     Logs that the game made it into the flop function.
// 
        theApp.log( "TexasHoldEm.flop()", 3 );

//     This is very similar to the deal() function in that it deals cards from the deck, gives them to players, and also indicates where to display them on the table.
//     Create a new blank card.
//
        Card  c = new Card();

//     Burn a card - really unnecessary because the deck is sufficiently randomized, but here for style.
//
        theApp.getDeck().deal();

//     Set c equal to the next card in the deck.
//
        c = theApp.getDeck().deal();

//     Indicate to all players that a shared card was dealt at table position 0.
//
        theApp.broadcastMessage( "CARD SHARED  &Table&0&" + c );

//     Loop through all the players and add the card to their hands.
//
        for ( int i = 0; i < theApp.getPlayerList().size(); i++ ) {
            ((Player)theApp.getPlayerList().get(i)).getHand().addSharedCard( c );
        }

//     Repeat for second flop card.
//
        c = theApp.getDeck().deal();
        theApp.broadcastMessage( "CARD SHARED  &Table&1&" + c );
        for ( int i = 0; i < theApp.getPlayerList().size(); i++ ) {
            ( (Player)theApp.getPlayerList().get(i) ).getHand().addSharedCard( c );
        }

//     Repeat for third flop card
//
        c = theApp.getDeck().deal();
        theApp.broadcastMessage( "CARD SHARED  &Table&2&" + c );
        for ( int i = 0; i < theApp.getPlayerList().size(); i++ ) {
            ((Player)theApp.getPlayerList().get(i)).getHand().addSharedCard( c );
        }

//     Send a message out to be displayed on the "message line"
//
        theApp.broadcastMessage( "MESSAGE  &" + ( (Player)theApp.getPlayerList().get(currPlayerIndex) ).getName() + "'s bet.");

//     Let the players know whose turn it is (this will enable and disable buttons)
//
        theApp.broadcastMessage( "PLAYER TURN &" + ( (Player)theApp.getPlayerList().get(currPlayerIndex) ).getName() );
    }

/***********************
 * turn() deals the 4th shared card
 * Very similar to the flop() logic - exception the position has changed to positon 3.
 **/
    protected void turn() {
        theApp.log( "TexasHoldEm.turn()", 3 );
        Card  c = new Card();
        theApp.getDeck().deal();
        c = theApp.getDeck().deal();
        theApp.broadcastMessage( "CARD SHARED  &Table&3&" + c );
        for ( int i = 0; i < theApp.getPlayerList().size(); i++ ) {
            ((Player)theApp.getPlayerList().get(i)).getHand().addSharedCard( c );
        }
        theApp.broadcastMessage( "MESSAGE  &" + ( (Player)theApp.getPlayerList().get(currPlayerIndex) ).getName() + "'s bet.");
        theApp.broadcastMessage( "PLAYER TURN &" + ( (Player)theApp.getPlayerList().get(currPlayerIndex) ).getName() );
    }

/***********************
 * river() deals the 5th and final shared card
 * same as the turn() logic, exception card is shown at position 4
 **/
    protected void river() {
        theApp.log( "TexasHoldEm.river()", 3 );
        Card  c = new Card();
        theApp.getDeck().deal();
        c = theApp.getDeck().deal();
        theApp.broadcastMessage( "CARD SHARED  &Table&4&" + c );
        for ( int i = 0; i < theApp.getPlayerList().size(); i++ ) {
            ((Player)theApp.getPlayerList().get(i)).getHand().addSharedCard( c );
        }
        theApp.broadcastMessage( "MESSAGE  &" + ( (Player)theApp.getPlayerList().get(currPlayerIndex) ).getName() + "'s bet.");
        theApp.broadcastMessage( "PLAYER TURN &" + ( (Player)theApp.getPlayerList().get(currPlayerIndex) ).getName() );
    }

/***********************
 * firstToBet() determines who gets to bet first - calls the function common to all HoldEm type games in PokerGame class
 **/
    protected int firstToBet() {
        return firstToBet_HoldEm();
    }

/***********************
 * showPlayer() defines how a player shows their cards.  
 * This is defined for all games and is called by the PokerGame class.  PokerGame handles all the logic of
 *  who shows and if they show, but this function must basically list all the hole cards to be flipped over.  If b is false, it means the player
 *  doesn't have to show, thus the second condition where the card is not shown.
 **/
    protected void showPlayer( Player p, boolean b ) {
        if ( b ) {
            theApp.broadcastMessage( "SHOW CARD &" + p.getName() + "&0&" + p.getHand().getHoleCard(0) );
            theApp.broadcastMessage( "SHOW CARD &" + p.getName() + "&1&" + p.getHand().getHoleCard(1) );
        } else {
            theApp.broadcastMessage( "SHOW CARD &" + p.getName() + "&0" );
            theApp.broadcastMessage( "SHOW CARD &" + p.getName() + "&1" );
        }
    }

/***********************
 * bestHand() returns the float value of the best possible hand that can be made from given hand h.
 * This function is called at the showdown for each player to determine which hand they can make.
 * The HandEvaluator class is used to figure out the best possible hand the player can make.
 * This function is required for all games, but may look very different depending from this depending
 * on what the rules of the game are.
 **/ 
    public float bestHand( Hand h ) {

//     Log that the game made it into this function.
//
        theApp.log( "TexasHoldEm.bestHand( " + h + " )", 3 );

//     Count total number of cards available and set of the c array to contain that many cards.
//     This won't always work if the game does not allow all cards to be used equally (ie Omaha HoldEm)
//
        int numCards = h.getNumHole() + h.getNumShared();
        Card[] c = new Card[numCards];   

//     Initialize the best and test variables - used for comparing ranks of hands.
//
        float best = 0.0f;
        float test = 0.0f;

//     ci is the array of 5 cards that are going to be played as the actual hand.
//
        int[] ci = new int[5];

//     Initialize a hand evaluator to figure out which hand is best.
//
        HandEvaluator he = new HandEvaluator();

//     Loop through all hole and shared cards and add them to the single array of available cards.
//
        for ( int i = 0; i < h.getNumHole(); i++ ) { 
            if ( h.getHoleCard( i ) != null ) {
                c[i] = h.getHoleCard( i );
            }
        }

        for ( int i = 0; i < h.getNumShared(); i++ ) {
            if ( h.getSharedCard( i ) != null ) {
                c[h.getNumHole()+i] = h.getSharedCard( i );
            }
        }

//     If there are only 5 or less cards available, only one hand can be made, so return a numerical value for these 5 cards.  This is necessary so the AI
//     can determine its hand strength - also required if betting order depends on shown hand strength (7 card stud).
//
        if ( numCards == 5 ) {
            return he.rankHand( c[0], c[1], c[2], c[3], c[4] );
        } else if ( numCards == 4 ) {
            return he.rankHand( c[0], c[1], c[2], c[3] );
        } else if ( numCards == 3 ) {
            return he.rankHand( c[0], c[1], c[2] );
        } else if ( numCards == 2 ) {
            return he.rankHand( c[0], c[1] );
        } else if ( numCards == 1 ) {
            return he.rankHand( c[0] );
        } else if ( numCards == 0 ) {
            return 0.0f;

//     If there are 6 or more cards, loop through all possible combinations and find the best possible 5 card hand that can be made.
// 
        } else if ( numCards >= 6 ) { 
            for ( int i = 0; i < numCards-4; i++ ) {
                for ( int j = i+1; j < numCards-3; j++ ) {
                    for ( int k = j+1; k < numCards-2; k++ ) {
                        for ( int l = k+1; l < numCards-1; l++ ) {
                            for ( int m = l+1; m < numCards; m++ ) {             

//     This is where the hand is ranked.  If this hand is better than all previous hands that could be made, then set it as the best hand.
//     Use the ci array to store card locations for the best hand.
//
                                test = he.rankHand( c[i],c[j],c[k],c[l],c[m] );
                                if ( test > best ) {
                                    best = test;
                                    ci[0] = i; ci[1] = j; ci[2] = k; ci[3] = l; ci[4] = m;
                                }
                            }
                        }
                    }
                }
            }
        }

//     Use the rankHand function along with the ci array to return the numerical ranking of this hand.
//     (Could just return the best variable here, but in some other games using this method is better so its done this way)
//    
        return he.rankHand( c[ci[0]], c[ci[1]], c[ci[2]], c[ci[3]], c[ci[4]] );
    }  

/***********************
 * calcBestPossible() determines the best possible hand available based on the shared cards.
 * This function is mainly to aid the computer AI logic.  It is not necessary unless an AI is written to take advantage of it, 
 * therefore it is not commented in detail.
 **/
    protected float calcBestPossible() {
        float best = 29.9f;
        float test = 0.0f;

//    First need to find shared cards.
//    If can't find any - the high pair is the best possible hand.
//
        int numShareCards = 0;
        int playerNum = 0;
        for ( int i = 0; i < theApp.getPlayerList().size(); i++ ) {
            if ( ((Player)theApp.getPlayerList().get(i)).getHand() != null ) {
                int n = ((Player)theApp.getPlayerList().get(i)).getHand().getNumShared();
                if ( n > 0 ) {
                    numShareCards = n;
                    playerNum = i;
                }
            }
        }
        if ( numShareCards == 0 ) {
            return best;
        }

        Card[] cshare = new Card[numShareCards];
        HandEvaluator he = new HandEvaluator();

        for ( int i = 0; i < numShareCards; i++ ) {
            cshare[i] = ((Player)theApp.getPlayerList().get(playerNum)).getHand().getSharedCard(i);
        }

//    Now start checking for best possible hands
//
//        Straight flush
//
        for ( int i = 0; i < numShareCards-2; i++ ) {
            for ( int j = i+1; j < numShareCards-1; j++ ) {
                for ( int k = j+1; k < numShareCards; k++ ) {       
                    float straight = he.isStraight( cshare[i], cshare[j], cshare[k] );
                    if ( he.isFlush( cshare[i], cshare[j], cshare[k] ) && ( straight > 0.0f ) ) {
                        test = 120.0f + straight;
                        if ( test > best ) {
                            best = test;
                        }
                    }                
                }
            }
        }
        if ( best >= 120.0f ) {
            return best;
        }

//      Four of a kind
//
        for ( int i = 0; i < numShareCards-1; i++ ) {
            for ( int j = i+1; j < numShareCards; j++ ) {
                if ( cshare[i].getRank() == cshare[j].getRank() ) {
                    for ( int k = 0; k < numShareCards; k++ ) {       
                        if ( ( k != i ) && ( k != j ) ) {
                            test = 105.0f + he.rankHand( cshare[i] ) + he.kicker( cshare[k] );
                            if ( test > best ) { 
                                best = test;
                            }
                        }            
                    }
                }
                for ( int k = 0; k < numShareCards; k++ ) {
                    if ( ( k != i ) && ( k != j ) && ( cshare[j].getRank() == cshare[k].getRank() ) ) {
                        test = 105.0f + he.rankHand( cshare[j] ) + he.kicker( cshare[i] );
                        if ( test > best ) { 
                            best = test;
                        }
                    }
                }
            }
        }
        if ( best >= 105.0f ) {
            return best;
        }

//
//      If the full house is possible, then the 4 of a kind is possible - so skip the full house test.
//
//      Flush
//
        for ( int i = 0; i < numShareCards-2; i++ ) {
            for ( int j = i+1; j < numShareCards-1; j++ ) {
                for ( int k = j+1; k < numShareCards; k++ ) {       
                    if ( he.isFlush( cshare[i], cshare[j], cshare[k] ) ) {
                        test = 89.0f + he.kicker( new Card( Card.KING, cshare[i].getSuit() ), cshare[i],cshare[j],cshare[k] );
                        if ( test > best ) { 
                            best = test;
                        }
                    }              
                }
            }
        }
        if ( best >= 75.0f ) {
            return best;
        }

//      Straight
//
        for ( int i = 0; i < numShareCards-2; i++ ) {    
            for ( int j = i+1; j < numShareCards-1; j++ ) {
                for ( int k = j+1; k < numShareCards; k++ ) {    
                    float straight = he.isStraight( cshare[i], cshare[j], cshare[k] );
                    if ( straight > 0.0f ) {
                        test = 60.0f + straight;
                        if ( test > best ) { 
                            best = test;
                        }
                    }
                }
            }
        }
        if ( best >= 60.0f ) {
            return best;
        }
 
//      Three of a kind
//
        for ( int i = 0; i < numShareCards-2; i++ ) {
            for ( int j = i+1; j < numShareCards-1; j++ ) {
                for ( int k = j+1; k < numShareCards; k++ ) {
                    if ( ( he.rankHand( cshare[i] ) >= he.rankHand( cshare[j] ) ) && ( he.rankHand( cshare[i] ) >= he.rankHand( cshare[k] ) ) ) {
                        test = 45.0f + he.rankHand( cshare[i] ) + he.kicker( cshare[j],cshare[k] );
                        if ( test > best ) { 
                            best = test;
                        }
                    } else if ( he.rankHand( cshare[j] ) >= he.rankHand( cshare[k] ) ) {
                        test = 45.0f + he.rankHand( cshare[j] ) + he.kicker( cshare[i],cshare[k] );
                        if ( test > best ) { 
                            best = test;
                        }
                    } else {
                        test = 45.0f + he.rankHand( cshare[k] ) + he.kicker( cshare[i],cshare[j] );
                        if ( test > best ) { 
                            best = test;
                        }
                    }                
                }
            }
        }
 
        return best;
    }

/***********************
 * mouseClick() handles mouse click events - none for this game.
 *
 * @param name The player's name who clicked the mouse button
 * @param x The x location of the mouse button click
 * @param y The y location of the mouse button click
 *
 **/
    protected void mouseClick( String name, int x, int y ) {}
}



/**************************************************************************************
  Now some detail on all the functions and messages available to derived PokerGame classes.

List of useful functions and variables for the game:
+------------+-----------------------------------------------+---------------------------------------------------------------------------
| class type |  fuction/variable name                        |   description
+------------+-----------------------------------------------+---------------------------------------------------------------------------
| StartPoker | theApp                                        | the instance of the server that started this game.
| static int | MAX_PLAYERS                                   | set to 8
| PokerMoney | currBet                                       | the current high bet in this game
| PokerMoney | initialBet                                    | initial bet for this round of betting
| Player     | highBettor                                    | the player who made the current high bet
| int        | actionNum                                     | The current action number describing how the game is progressing
| int        | maxActionNum                                  | Maximum number of actions in this specific game type
| int        | currPlayerIndex                               | the index in the servers list of players corresponding to the current player
| int        | currSeatIndex                                 | the seat number the current player is sitting in
| float      | playerHandValues[MAX_PLAYERS]                 | The value of each players best possible hand - only after they are calculated at the showdown.
| float      | bestPossible                                  | The best possible hand available based on what cards are showing.
| boolean    | requiresInteraction                           | Whether this game requires interaction - must be set to true for games which require mouse click so AI knows to fold
| void       | show()                                        | Function that performs the showdown  - shows cards and declares winner(s) based on bestHand() function in this file.
| int        | firstToBet_HoldEm()                           | Function that will determine which player is first to bet for HoldEm type games = based on position relative to dealer
| int        | firstToBet_Stud()                             | Function that will determing which player is first to bet for Stud type games = player with best hand showing
+------------+-----------------------------------------------+---------------------------------------------------------------------------
 
The Player class contains some informations about each Player.
+------------+-----------------------------------------------------------------+-------------------------------------------------------------------------
| class type |  fuction/variable name typical use                              |   description
+------------+-----------------------------------------------------------------+-------------------------------------------------------------------------
| String     | ((Player)theApp.getPlayerList().get( int )).getName()           | returns a String which is the players name.
| PokerMoney | ((Player)theApp.getPlayerList().get( int )).getBet()            | returns the PokerMoney class of the players current bet in the current betting round
| PokerMoney | ((Player)theApp.getPlayerList().get( int )).getPrevBet()        | returns the PokerMoney class of the players most recent bet during the current round.
| PokerMoney | ((Player)theApp.getPlayerList().get( int )).getBankroll()       | returns the PokerMoney class of the players bankroll.  
| boolean    | ((Player)theApp.getPlayerList().get( int )).potOK               | if this player is paid up to call the bet
| boolean    | ((Player)theApp.getPlayerList().get( int )).in                  | if this player is in the current game
| boolean    | ((Player)theApp.getPlayerList().get( int )).allin               | if this player is all-in
| Hand       | ((Player)theApp.getPlayerList().get( int )).getHand()           | returns this player's Hand.
| void       | ((Player)theApp.getPlayerList().get( int )).add( float )        | add float amount of money to the players bankroll
| void       | ((Player)theApp.getPlayerList().get( int )).setBet( float )     | set the players current bet to the float value
| void       | ((Player)theApp.getPlayerList().get( int )).setPrevBet( float ) | set the players previous bet in this round to the float value
+------------+-----------------------------------------------------------------+------------------------------------------------------------------------- 

The StartPoker class is the server and contains some useful information:
+------------+-----------------------------------------------+--------------------------------------------------------------------------
| class type |  function/variable name typical use           |   description
+------------+-----------------------------------------------+--------------------------------------------------------------------------
| PokerMoney | theApp.getPot()                               | returns the PokerMoney class representing the amount of money in the pot.
| void       | theApp.log( String )                          | writes a string to the log file
| void       | theApp.log( String, int )                     | writes a string to the log file if the int argmument <= logLevel as defined in poker_inputs.txt
| ArrayList  | theApp.getPlayerList()                        | returns an ArrayList of Player types of all the players who are sitting at the table.
| Card       | theApp.getDeck().deal()                       | returns the next card off the top of the deck
| void       | theApp.messageToPlayer( String, String )      | sends a message (the second argument) to the player with the name in the first argument
| void       | theApp.broadcastMessage( String )             | sends a message out to all players at the table
+------------+-----------------------------------------------+--------------------------------------------------------------------------

To actually use a PokerMoney class, you can use the following functions function:
+------------+-----------------------------------------------+--------------------------------------------------------------------------
| class type |  function/variable name typical use           |   description
+------------+-----------------------------------------------+--------------------------------------------------------------------------
| float      | theApp.getPot().aunt()                        | the amount() function will return the float value stored in any PokerMoney class instance.
+------------+-----------------------------------------------+--------------------------------------------------------------------------

The Hand class contains the players hand:
+------------+-----------------------------------------------+--------------------------------------------------------------------------
| class type |  function/variable name typical use           |   description
+------------+-----------------------------------------------+--------------------------------------------------------------------------
| int        | aHand.getNumHole()                           | number of cards in this player hand which are down
| int        | aHand.getNumUp()                             | number of cards in this player hand which are up
| int        | aHand.getNumShared()                         | number of cards in this player hand which are shared with all other players
| Card       | aHand.getHoleCard( int )                     | get a specific card from the hole - specified by int index
| Card       | aHand.getUpCard( int )                       | get a specific card facing up - specified by int index
| Card       | aHand.getSharedCard( int )                   | get a specific shared card - specified by int index
| void       | aHand.addHoleCard( Card )                    | places the argument Card into the hand (in the hole)
| void       | aHand.addUpCard( Card )                      | places the argument Card into the hand (showing up to all players)
| void       | aHand.addSharedCard( Card )                  | places the argument Card into the hand (card is shared amongst all the players)
+------------+-----------------------------------------------+--------------------------------------------------------------------------

The most important thing the game has to do is send out messages to all the players to let all the clients know what
is going on in the game.  This is done with either the theApp.broadcastMessage( String m ) function which sends out
a message to all players, or with the theApp.messageToPlayer( String name, String m ) function which sends out a 
message to just one player.  Messages are sent in a certain format and if that format is not followed, errors will
result.  In general, the ampersand (&) is used to split up the message into parts.
Necessary messages for the games are shown below :

MESSAGE &Message text                         - displays a message on the player's "message line"
PLAYER NOPICS                                 - removes all the card pictures for a player - presumably, pictures of new cards will be displayed
PLAYER CLEAR HAND                             - clears out a player's hand - again, presumably, a new hand will be given to the player
PLAYER TURN &Name                             - lets player know it is now Name's turn to bet
PLAYER TURN &Name&true                        - sets the flag structuredRaiseEnable - to let player know if their raise button should be on for structured betting games.
PLAYER CALL &Name&Call text&bankroll          - lets everyone know what this player did - the text gets displayed under the players name on the table - and also updates their cash display.
CARD HOLE &Name&card pos                      - lets everyone know that a player got a hole card in the given position
CARD HOLE &Name&card pos&card                 - tells a player they got a hole card and tells them exactly what the card is 
CARD UP &Name&card pos&card                   - lets everyone know that a player got an up card in the given position
CARD SHARED &Table&card pos&card              - lets everyone know that a shared card was dealt in the given position
CARD ICSHARED &Table&card pos&card            - lets everyone know that a shared card was dealt for iron cross - slightly different care positions on the table.
SHOW CARD &Name&position&card                 - shows a card in the given position that wasn't showing before.
SHOW CARD &Name&postion                       - removes a card in the given position from view (rather than showing it at the showdown)
SELECT CARD &card pos                         - designate a card in the given position to be selected to be dropped or passed or something
DESELECT CARD &card pos                       - designate a card in the given position to be deselected
DISPLAY DRAW BUTTON                           - shows a button on the table with the text "Draw"
DISPLAY PASS BUTTON                           - shows a button on the table with the text "Pass"
DISPLAY DONE BUTTON                           - shows a button on the table with the text "Done"
DISPLAY YESNO BUTTON                          - shows "yes" and "no" buttons on the table
DISPLAY NOTHING                               - used to remove the above buttons.

Thats all the supported messages and functions for now.  If something else is required for a new game type, let the developer know and maybe it
will get in the next release.



There is also a hand evaluator class which can be used to get a floating point value of the player hand.
Its use can be summarized as follows:

Construct an instance of the class with the following statement:
HandEvaluator he = new HandEvaluator();

The following overloaded fucntion can be used:
float  he.rankHand( Card c1 );
float  he.rankHand( Card c1, Card c2 );
float  he.rankHand( Card c1, Card c2, Card c3 );
float  he.rankHand( Card c1, Card c2, Card c3, Card c4 );
float  he.rankHand( Card c1, Card c2, Card c3, Card c4, Card c5 );

Generally, you should use the Hand.get*Card() functions to pass the cards as arguments.
All of these functions will give the cards passed as arguments, and floating point score which can be broken down as follows.
 
Royal Straight Flush = 150.0f
Five of a kind       = 135.0f - 149.99999f
Straight flush       = 120.0f - 134.99999f
Four of a kind       = 105.0f - 119.99999f
Full House           =  90.0f - 104.99999f
Flush                =  75.0f -  89.99999f
Straight             =  60.0f -  74.99999f
Three of a kind      =  45.0f -  59.99999f
Two Pair             =  30.0f -  44.99999f
Pair                 =  15.0f -  29.99999f  
High Card            =   0.0f -  14.99999f

The higher the number is in the range, the better of that type of hand it is.  The decimal places make up the kicker(s)

A static function can also be used to actually print out the name of the hand (including card names) based on the passed float value:
static String HandEvaluator.nameHand( float f );
for example:  HandEvaluator.nameHand( 74.0f ) returns the String "Straight: Ace high"






One last note...for debugging:

System.out.println( String s );        can concatenate strings by use of the + sign.  Most everything will print to screen nicely without formatting needed.
    EX - prints out name, pot, and two hole cards.
      System.out.println( name + "'s bet.  " + server.getPot() + "    " + hand.getHoleCard(0) + "  " + hand.getHoleCard(1) );

Can also use the log functions (as demonstrated above) to save the debugging data into a file, but this file is also cluttered with lots of other information.
***************************************************************************************/
