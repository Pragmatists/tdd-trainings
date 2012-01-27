/******************************************************************************************
 * FiveCardDraw.java                PokerApp                                              *
 *                                                                                        *
 *   Revision History                                                                     *
 * +---------+----------+---------------------------------------------------------------+ *
 * | Version | DATE     | Description                                                   | *
 * +---------+----------+---------------------------------------------------------------+ *
 * |  0.97   | 11/01/04 | Initial release                                               | *
 * |  0.99   | 05/17/05 | Added logic to PLAYER TURN call for structured betting games  | *
 * |  0.99   | 05/19/05 | Define showPlayer() and move showCards() to PokerGame class   | *
 * |  0.99   | 05/23/05 | Set requiresInteraction flag to true.                         | *
 * |  1.00   | 06/06/05 | Added logging.                                                | *
 * |  1.00   | 12/08/05 | Removed System.out.println line left in by accident           | *
 * |  1.00   | 07/31/07 | Prepare for open source.  Header/comments/package/etc...      | *
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

package net.sourceforge.pokerapp.games;

import net.sourceforge.pokerapp.*;

/****************************************************
 * Five Card Draw game definition.
 *
 * @author Dan Puperi
 * @version 1.00
 *
 **/
public class FiveCardDraw extends PokerGame {

    private boolean  drawn[];              // Defines who has drawn their cards
    private boolean  allDrawn;             // Has everybody had a chance to draw their cards?
    private boolean  drawSelections[];     // Which cards are selected to be drawn

/***********************
 * Constructor creates an instance of a game of Five Card Draw
 *
 * @param a The StartPoker instance to which this game belongs
 *
 **/
    public FiveCardDraw( StartPoker a ) {
        super( a, "FiveCardDraw", false );
        maxActionNum = 3;
        if ( theApp.getPlayerList().size() < 2 ) {
            return;
        }
        theApp.log( "Constructing a game of Five Card Draw", 3 );
        drawn = new boolean[MAX_PLAYERS];
        drawSelections = new boolean[5];
        for ( int i = 0; i < theApp.getPlayerList().size(); i++ ) {
            drawn[i] = false;
        }
        allDrawn = false;
        requiresInteraction = true;
        deal();
    }

/***********************
 * deal() deals the initial cards
 **/
    protected void deal() {
        theApp.log( "FiveCardDraw.deal()", 3 );
        Card  c = new Card();
        bestPossible = calcBestPossible();    

//   First card down
        for ( int i = 0; i < theApp.getPlayerList().size(); i++ ) {
            c = theApp.getDeck().deal();
            ((Player)theApp.getPlayerList().get(i)).getHand().addHoleCard( c );
            theApp.messageToPlayer( ((Player)theApp.getPlayerList().get(i) ).getName(), "CARD HOLE  &" + ( (Player)theApp.getPlayerList().get(i) ).getName() + "&0&" + c );
            theApp.broadcastMessage( "CARD HOLE  &" + ( (Player)theApp.getPlayerList().get(i) ).getName() + "&0" );      
        }

//   Second card down
        for ( int i = 0; i < theApp.getPlayerList().size(); i++ ) {
            c = theApp.getDeck().deal();
            ((Player)theApp.getPlayerList().get(i)).getHand().addHoleCard( c );
            theApp.messageToPlayer( ((Player)theApp.getPlayerList().get(i) ).getName(), "CARD HOLE  &" + ( (Player)theApp.getPlayerList().get(i) ).getName() + "&1&" + c );
            theApp.broadcastMessage( "CARD HOLE  &" + ( (Player)theApp.getPlayerList().get(i) ).getName() + "&1" );
        }

//   Third card down
        for ( int i = 0; i < theApp.getPlayerList().size(); i++ ) {
            c = theApp.getDeck().deal();  
            ( (Player)theApp.getPlayerList().get(i) ).getHand().addHoleCard( c );
            theApp.messageToPlayer( ((Player)theApp.getPlayerList().get(i) ).getName(), "CARD HOLE  &" + ( (Player)theApp.getPlayerList().get(i) ).getName() + "&2&" + c );
            theApp.broadcastMessage( "CARD HOLE  &" + ( (Player)theApp.getPlayerList().get(i) ).getName() + "&2" );
        }

//   Fourth card down
        for ( int i = 0; i < theApp.getPlayerList().size(); i++ ) {
            c = theApp.getDeck().deal();
            ( (Player)theApp.getPlayerList().get(i) ).getHand().addHoleCard( c );
            theApp.messageToPlayer( ((Player)theApp.getPlayerList().get(i) ).getName(), "CARD HOLE  &" + ( (Player)theApp.getPlayerList().get(i) ).getName() + "&3&" + c );
            theApp.broadcastMessage( "CARD HOLE  &" + ( (Player)theApp.getPlayerList().get(i) ).getName() + "&3" );
        }

//   Fifth card down
        for ( int i = 0; i < theApp.getPlayerList().size(); i++ ) {
            c = theApp.getDeck().deal();
            ( (Player)theApp.getPlayerList().get(i) ).getHand().addHoleCard( c );
            theApp.messageToPlayer( ((Player)theApp.getPlayerList().get(i) ).getName(), "CARD HOLE  &" + ( (Player)theApp.getPlayerList().get(i) ).getName() + "&4&" + c );
            theApp.broadcastMessage( "CARD HOLE  &" + ( (Player)theApp.getPlayerList().get(i) ).getName() + "&4" );
        }

        theApp.broadcastMessage( "MESSAGE  &Playing Five Card Draw.  " + ( (Player)theApp.getPlayerList().get(currPlayerIndex) ).getName() + "'s bet.");  
        if ( ( theApp.blinds ) && !( theApp.noLimit || theApp.potLimit || theApp.betLimit ) ) {
            theApp.broadcastMessage( "PLAYER TURN &" + ((Player)theApp.getPlayerList().get(currPlayerIndex)).getName() + "&enable raise" );
        } else {
            theApp.broadcastMessage( "PLAYER TURN &" + ((Player)theApp.getPlayerList().get(currPlayerIndex)).getName() );
        }
    }

/***********************
 * nextAction() determines what happens next
 **/
    protected void nextAction() {
        theApp.log( "FiveCardDraw.nextAction() - previous actionNum = " + actionNum, 3 );
        actionNum++;
        for ( int i = 0; i < theApp.getPlayerList().size(); i++ ) {
            ( (Player)theApp.getPlayerList().get(i) ).potOK = false;
            ( (Player)theApp.getPlayerList().get(i) ).setBet( 0.0f );
            ( (Player)theApp.getPlayerList().get(i) ).setPrevBet( 0.0f );
        }
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
        theApp.updateMoneyLine( currBet, highBettor.getName() );
        switch ( actionNum ) {
            case 2 :
                theApp.broadcastMessage( "DISABLE BUTTONS" );
                draw();
                break;
            case 3 :
                show();
                break;
            default :
                break;  
        }
    }

/***********************
 * draw() defines what happens when its time for players to draw their new cards.
 **/
    private void draw() {
        theApp.log( "FiveCardDraw.draw()", 3 );
        Player currPlayer = (Player)theApp.getPlayerList().get(currPlayerIndex);
        if ( !allDrawn ) {
            theApp.broadcastMessage( "DISPLAY NOTHING" );
            if ( !drawn[currPlayerIndex] ) {
                theApp.broadcastMessage( "MESSAGE &" + currPlayer.getName() + " is deciding how many cards to draw..." );
                theApp.messageToPlayer( currPlayer.getName(), "DISPLAY DRAW BUTTON" );
                theApp.messageToPlayer( currPlayer.getName(), "MESSAGE &Please select cards to draw for and press Draw Cards button on table when ready." );
            } else {
                allDrawn = true;
                for ( int i = 0; i < theApp.getPlayerList().size(); i++ ) {
                    if ( ( ((Player)theApp.getPlayerList().get(i)).in ) && ( !drawn[i] ) ) {
                        allDrawn = false;
                    }
                }

                if ( !allDrawn ) {
                    int prevSeat = currPlayer.seat;     
                    boolean foundNext = false;
                    while ( !foundNext ) {
                        prevSeat = theApp.nextSeat( prevSeat );
                        boolean waitingToLeave = false;
                        for ( int j = 0; j < theApp.leavingPlayerList.size(); j++ ) {
                            if ( ((String)theApp.leavingPlayerList.get(j)).equals( ((Player)theApp.getPlayerList().get(theApp.getPlayerInSeat(prevSeat))).getName() ) ) {
                                waitingToLeave = true;
                            }
                        }

                        if ( ( ((Player)theApp.getPlayerList().get(theApp.getPlayerInSeat(prevSeat))).in ) && ( !waitingToLeave ) ) {
                            foundNext = true;
                            currPlayerIndex = theApp.getPlayerInSeat( prevSeat );
                        }
                    }
                }
                draw();
            }
        } else {
            currBet = new PokerMoney( 0.0f );
            currPlayerIndex = firstToBet();
            if ( ( currPlayerIndex < 0 ) || ( currPlayerIndex >= theApp.getPlayerList().size() ) ) {    
                actionNum = maxActionNum;
                skipShow = true;
            } else {
                highBettor = (Player)theApp.getPlayerList().get(currPlayerIndex);
            }
            theApp.updateMoneyLine( currBet, highBettor.getName() );

            allDrawn = false;
            for ( int i = 0; i < theApp.getPlayerList().size(); i++ ) {
                drawn[i] = false;
            }

            theApp.broadcastMessage( "MESSAGE  &" + ( (Player)theApp.getPlayerList().get(currPlayerIndex) ).getName() + "'s bet.");
            theApp.broadcastMessage( "PLAYER TURN &" + ( (Player)theApp.getPlayerList().get(currPlayerIndex) ).getName() );
        }
    }

/***********************
 * firstToBet() determines who gets to bet first - calls the function common to all HoldEm type games in PokerGame class
 *
 * @return The player index who will be first to bet.
 *
 **/
    protected int firstToBet() {
        return firstToBet_HoldEm();
    }

/***********************
 * showPlayer() defines how a player shows their cards
 *
 * @param p The player who is showing their cards
 * @param b Whether this player has to turn his cards face up.
 *
 **/
    protected void showPlayer( Player p, boolean b ) {
        if ( b ) {
            theApp.broadcastMessage( "SHOW CARD &" + p.getName() + "&0&" + p.getHand().getHoleCard(0) );
            theApp.broadcastMessage( "SHOW CARD &" + p.getName() + "&1&" + p.getHand().getHoleCard(1) );
            theApp.broadcastMessage( "SHOW CARD &" + p.getName() + "&2&" + p.getHand().getHoleCard(2) );
            theApp.broadcastMessage( "SHOW CARD &" + p.getName() + "&3&" + p.getHand().getHoleCard(3) );
            theApp.broadcastMessage( "SHOW CARD &" + p.getName() + "&4&" + p.getHand().getHoleCard(4) );
        } else {
            theApp.broadcastMessage( "SHOW CARD &" + p.getName() + "&0" );
            theApp.broadcastMessage( "SHOW CARD &" + p.getName() + "&1" );
            theApp.broadcastMessage( "SHOW CARD &" + p.getName() + "&2" );
            theApp.broadcastMessage( "SHOW CARD &" + p.getName() + "&3" );
            theApp.broadcastMessage( "SHOW CARD &" + p.getName() + "&4" );
        }
    }

/***********************
 * bestHand() returns the float value of the best possible hand that can be made from given hand h.
 *
 * @param h The hand to evaluate
 * @return The float value of the best five card hand out of this hand.
 *
 **/
    public float bestHand( Hand h ) {
        theApp.log( "FiveCardDraw.bestHand( " + h + " )", 3 );
        int numCards = h.getNumHole();
        Card[] c = new Card[numCards];
        float best = 0.0f;
        float test = 0.0f;
        HandEvaluator he = new HandEvaluator();

        for ( int i = 0; i < h.getNumHole(); i++ ) { 
            if ( h.getHoleCard( i ) != null ) {
                c[i] = h.getHoleCard( i );
            }
        }

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
        } else {
            return 0.0f;
        }
    }  

/***********************
 * calcBestPossible() determines the best possible hand available based on the shared cards.
 * This function is mianly to aid the computer AI logic.
 *
 * @return The float value of the best possible hand somebody can have.
 *
 **/
    protected float calcBestPossible() {
        return 150.0f;
    }

/***********************
 * mouseClick() handles mouse click events.
 *
 * @param name The name of the player making the mouse click
 * @param x The x location of the mouse click
 * @param y The y location of the mouse click
 *
 **/
    protected void mouseClick( String name, int x, int y ) {
        theApp.log( "FiveCardDraw.mouseClick( " + name + ", " + x + ", " + y + " )", 3 );
        Player currPlayer = (Player)theApp.getPlayerList().get(currPlayerIndex);
        int loc = currPlayer.seat;
        if ( ( name.equals( currPlayer.getName() ) ) && ( actionNum == 2 ) ) {
            boolean ace = false;
            boolean aceSel = false;
            int numSel = 0;
            for ( int i = 0; i < 5; i++ ) {
                if ( currPlayer.getHand().getHoleCard( i ).getRank() == Card.ACE ) {
                    ace = true;
                }
                if ( drawSelections[i] ) {
                    numSel++;
                    if ( currPlayer.getHand().getHoleCard( i ).getRank() == Card.ACE ) {
                        aceSel = true;
                    }
                }
            }

//      Player has clicked the "Draw Cards" button in the middle of the screen.
//
            if ( ( x >= 340 ) && ( x <= 340+150 ) && ( y >= 200+23 ) && ( y <= 200+23+50 ) ) {
                theApp.log( name + " has selected to draw " + numSel + " cards.", 1 );
                drawn[currPlayerIndex] = true;
                theApp.broadcastMessage( "PLAYER CALL  &" + currPlayer.getName() + "&Drew " + numSel + " cards&" + currPlayer.getBankroll().amount() );
                Hand tempHand = new Hand();
                for ( int i = 0; i < 5; i++ ) {
                    tempHand.addHoleCard( currPlayer.getHand().getHoleCard(i) );
                }     
                ((Player)theApp.getPlayerList().get(currPlayerIndex)).getHand().clearHand();
                theApp.messageToPlayer( currPlayer.getName(), "PLAYER CLEAR HAND &" + currPlayer.getName() );
                for ( int i = 0; i < 5; i++ ) {
                    Card c = new Card();
                    if ( drawSelections[i] ) {
                        c = theApp.getDeck().deal();
                    } else {
                        c = tempHand.getHoleCard( i );
                    }
                    ((Player)theApp.getPlayerList().get(currPlayerIndex)).getHand().addHoleCard( c );
                    theApp.messageToPlayer( currPlayer.getName(), "CARD HOLE  &" + currPlayer.getName() + "&" + i + "&" + c );
                    drawSelections[i] = false;
                }
                draw();
                return;
            }

//      Check to see if player has clicked to select or deselect any of his cards.
//
            for ( int cardNum = 0; cardNum < 5; cardNum++ ) {
                int width = 29;
                if ( cardNum == 4 ) {
                    width = 71;
                }
                if ( ( x >= PokerApp.LOCATION[loc][0]+30*cardNum ) && ( x <= PokerApp.LOCATION[loc][0]+30*cardNum+width ) &&
                          ( y >= PokerApp.LOCATION[loc][1]+15 ) && ( y <= PokerApp.LOCATION[loc][1]+23+96 ) ) {
                    if ( drawSelections[cardNum] ) {
                        theApp.messageToPlayer( currPlayer.getName(), "DESELECT CARD &" + cardNum );
                        drawSelections[cardNum] = !drawSelections[cardNum];
                    } else if ( ( numSel < 3 ) || ( ace && ( numSel < 4 ) && 
                            ( currPlayer.getHand().getHoleCard( cardNum ).getRank() != Card.ACE ) && ( !aceSel ) ) ) {
                        theApp.messageToPlayer( currPlayer.getName(), "SELECT CARD &" + cardNum );
                        drawSelections[cardNum] = !drawSelections[cardNum];
                    }
                }
            }
        }
    }
}