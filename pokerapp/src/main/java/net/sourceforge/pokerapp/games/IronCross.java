/******************************************************************************************
 * IronCross.java                  PokerApp                                               *
 *                                                                                        *
 *   Revision History                                                                     *
 * +---------+----------+---------------------------------------------------------------+ *
 * | Version | DATE     | Description                                                   | *
 * +---------+----------+---------------------------------------------------------------+ *
 * |  0.99   | 05/23/05 | Initial documented release                                    | *
 * |  1.00   | 06/06/05 | Added logging.                                                | *
 * |  1.00   | 08/10/07 | Prepare for open source.  Header/comments/package/etc...      | *
 * +---------+----------+---------------------------------------------------------------+ *
 *                                                                                        *
 ******************************************************************************************/

package net.sourceforge.pokerapp.games;

import net.sourceforge.pokerapp.*;

/****************************************************
 * Iron Cross game definition.
 *
 * @author Dan Puperi
 * @version 1.00
 *
 **/
public class IronCross extends PokerGame {

/***********************
 * Constructor creates an instance of a game of Iron Cross
 *
 * @param a The StartPoker instance to which this game belongs
 *
 **/
    public IronCross( StartPoker a ) {
        super( a, "Iron Cross", false );
        maxActionNum = 5;
        if ( theApp.getPlayerList().size() < 2 ) {
            return;
        }
        theApp.log( "Constructing a game of Iron Cross", 3 );
        deal();
    }
    
/***********************
 * Constructor creates an instance of a game of Iron Cross
 * This constructor is used by other games which can extends this game
 *
 * @param a The StartPoker instance to which this game belongs
 * @param gameName The name of the game
 * @param selfInit Whether or not the game can initialize itself.
 *
 **/
    public IronCross( StartPoker a, String gameName, boolean selfInit ) {
        super( a, gameName, selfInit );
        maxActionNum = 5;
        if ( theApp.getPlayerList().size() < 2 ) {
            return;
        }
        theApp.log( "Constructing a game of Iron Cross", 3 );
    }

/***********************
 * deal() deals the initial cards
 **/
    protected void deal() {
        theApp.log( "IronCross.deal()", 3 );
        Card  c = new Card();
        bestPossible = calcBestPossible();    

//   First card down
        for ( int i = 0; i < theApp.getPlayerList().size(); i++ ) {
            c = theApp.getDeck().deal();
            ( (Player)theApp.getPlayerList().get(i) ).getHand().addHoleCard( c );
            theApp.messageToPlayer( ((Player)theApp.getPlayerList().get(i) ).getName(), "CARD HOLE  &" + ( (Player)theApp.getPlayerList().get(i) ).getName() + "&0&" + c );
            theApp.broadcastMessage( "CARD HOLE  &" + ( (Player)theApp.getPlayerList().get(i) ).getName() + "&0" );      
        }

//   Second card down
        for ( int i = 0; i < theApp.getPlayerList().size(); i++ ) {
            c = theApp.getDeck().deal();
            ( (Player)theApp.getPlayerList().get(i) ).getHand().addHoleCard( c );
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

        theApp.broadcastMessage( "MESSAGE  &Playing Iron Cross, with the A-Mod.  " + ( (Player)theApp.getPlayerList().get(currPlayerIndex) ).getName() + "'s bet.");  
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
        theApp.log( "IronCross.nextAction() - previous actionNum = " + actionNum, 3 );
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
 * flop() deals the Flop - 2 shared cards
 **/
    private void flop() {
        theApp.log( "IronCross.flop()", 3 );
        Card  c = new Card();
        theApp.getDeck().deal();

        c = theApp.getDeck().deal();
        theApp.broadcastMessage( "CARD ICSHARED  &Table&0&" + c );
        for ( int i = 0; i < theApp.getPlayerList().size(); i++ ) {
            ( (Player)theApp.getPlayerList().get(i) ).getHand().addSharedCard( c );
        }

        c = theApp.getDeck().deal();
        theApp.broadcastMessage( "CARD ICSHARED  &Table&1&" + c );
        for ( int i = 0; i < theApp.getPlayerList().size(); i++ ) {
            ( (Player)theApp.getPlayerList().get(i) ).getHand().addSharedCard( c );
        }

        theApp.broadcastMessage( "MESSAGE  &" + ( (Player)theApp.getPlayerList().get(currPlayerIndex) ).getName() + "'s bet.");
        theApp.broadcastMessage( "PLAYER TURN &" + ( (Player)theApp.getPlayerList().get(currPlayerIndex) ).getName() );
    }

/***********************
 * turn() deals the next 2 shared cards
 **/
    private void turn() {
        theApp.log( "IronCross.turn()", 3 );
        Card  c = new Card();
        theApp.getDeck().deal();
        c = theApp.getDeck().deal();
        theApp.broadcastMessage( "CARD ICSHARED  &Table&2&" + c );
        for ( int i = 0; i < theApp.getPlayerList().size(); i++ ) {
            ( (Player)theApp.getPlayerList().get(i) ).getHand().addSharedCard( c );
        }

        c = theApp.getDeck().deal();
        theApp.broadcastMessage( "CARD ICSHARED  &Table&3&" + c );
        for ( int i = 0; i < theApp.getPlayerList().size(); i++ ) {
            ( (Player)theApp.getPlayerList().get(i) ).getHand().addSharedCard( c );
        }

        theApp.broadcastMessage( "MESSAGE  &" + ( (Player)theApp.getPlayerList().get(currPlayerIndex) ).getName() + "'s bet.");
        theApp.broadcastMessage( "PLAYER TURN &" + ( (Player)theApp.getPlayerList().get(currPlayerIndex) ).getName() );
    }

/***********************
 * river() deals the 5th and final shared card
 **/
    private void river() {
        theApp.log( "IronCross.river()", 3 );
        Card  c = new Card();
        theApp.getDeck().deal();

        c = theApp.getDeck().deal();
        theApp.broadcastMessage( "CARD ICSHARED  &Table&4&" + c );
        for ( int i = 0; i < theApp.getPlayerList().size(); i++ ) {
            ( (Player)theApp.getPlayerList().get(i) ).getHand().addSharedCard( c );
        }

        theApp.broadcastMessage( "MESSAGE  &" + ( (Player)theApp.getPlayerList().get(currPlayerIndex) ).getName() + "'s bet.");
        theApp.broadcastMessage( "PLAYER TURN &" + ( (Player)theApp.getPlayerList().get(currPlayerIndex) ).getName() );
    }

/***********************
 * firstToBet() determines who gets to bet first - calls the function common to all HoldEm type games in PokerGame class
 *
 * @return The player index of the play who will bet first this round
 *
 **/
    protected int firstToBet() {
        return firstToBet_HoldEm();
    }

/***********************
 * showPlayer() defines how a player shows their cards
 *
 * @param p The player who is going to show
 * @param b Whether or not this player has to turn his cards face up
 * 
 **/
    protected void showPlayer( Player p, boolean b ) {
        if ( b ) {
            theApp.broadcastMessage( "SHOW CARD &" + p.getName() + "&0&" + p.getHand().getHoleCard(0) );
            theApp.broadcastMessage( "SHOW CARD &" + p.getName() + "&1&" + p.getHand().getHoleCard(1) );
            theApp.broadcastMessage( "SHOW CARD &" + p.getName() + "&2&" + p.getHand().getHoleCard(2) );
            theApp.broadcastMessage( "SHOW CARD &" + p.getName() + "&3&" + p.getHand().getHoleCard(3) );
        } else {
            theApp.broadcastMessage( "SHOW CARD &" + p.getName() + "&0" );
            theApp.broadcastMessage( "SHOW CARD &" + p.getName() + "&1" );
            theApp.broadcastMessage( "SHOW CARD &" + p.getName() + "&2" );
            theApp.broadcastMessage( "SHOW CARD &" + p.getName() + "&3" );
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
        theApp.log( "IronCross.bestHand( " + h + " )", 3 );
        int numHoleCards = h.getNumHole();
        int numShareCards = h.getNumShared();
        Card[] chole= new Card[numHoleCards];
        Card[] cshare = new Card[numShareCards];
        float best = 0.0f;
        float test = 0.0f;
        HandEvaluator he = new HandEvaluator();

        for ( int i = 0; i < numHoleCards; i++ ) { 
            if ( h.getHoleCard( i ) != null ) {
                chole[i] = h.getHoleCard( i );
            }
        }
    
        for ( int i = 0; i < numShareCards; i++ ) {
            if ( h.getSharedCard( i ) != null ) {
                cshare[i] = h.getSharedCard( i );
            }
        }
    
        best = 0.0f;
        if ( numShareCards < 5 ) {
            return best;
        }
//
//   Iron Cross hand can be formed by shared cards 1,3,5 or 2,4,5
//
        for ( int i = 0; i < numHoleCards-1; i++ ) {
            for ( int j = i+1; j < numHoleCards; j++ ) {
                test = he.rankHand( chole[i],chole[j],cshare[0],cshare[2],cshare[4] );
                if ( test > best ) { 
                    best = test;
                }

                test = he.rankHand( chole[i],chole[j],cshare[1],cshare[3],cshare[4] );
                if ( test > best ) { 
                    best = test;
                }
            }
        }
   
        return best;
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
 * mouseClick() handles mouse click events - none for this game.
 **/
    protected void mouseClick( String name, int x, int y ) {}

}
