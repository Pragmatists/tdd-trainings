/******************************************************************************************
 * AILogic_simple.java             PokerApp                                               *
 *                                                                                        *
 * AILogic_simple is an example of what the AILogic class should entail.  It isn't a very *
 * good AI player, but is meant only as a guidline for writing future AILogic classes.    *
 *                                                                                        *
 *   Revision History                                                                     *
 * +---------+----------+---------------------------------------------------------------+ *
 * | Version | DATE     | Description                                                   | *
 * +---------+----------+---------------------------------------------------------------+ *
 * |  0.95   | 09/15/04 | Initial documented release                                    | *
 * |  0.96   | 09/16/04 | Fixes to be compatible with upgrades to AIAction              | *
 * |  0.97   | 11/10/04 | Fold for games which require interaction                      | *
 * |  0.99   | 02/04/05 | Update (mostly comments) for newest AILogic enhancements.     | *
 * |  0.99   | 05/23/05 | Look at PokerGame.requiresInteraction flag - fold if true     | *
 * |  1.00   | 08/11/07 | Prepare for open source.  Header/comments/package/etc...      | *
 * +---------+----------+---------------------------------------------------------------+ *
 *                                                                                        *
 ******************************************************************************************/

// Any AILogic class must be part of the Poker.AI package.
//
package net.sourceforge.pokerapp.ai;

// Must import the entire Poker package.  Also need to import ArrayList because it is used often.
//
import net.sourceforge.pokerapp.*;
import java.util.ArrayList;

/****************************************************
 * AILogic_simple class definition - make sure the name of the class is the
 * exact same as the source filename without the .java Must extend the basic
 * AILogic class (which really doesn't do too much by itself)
 * 
 * @author Dan Puperi
 * @version 1.00
 * 
 **/
public class AILogic_simple extends AILogic {

	/**********************
	 * AILogic_simple class variables. Place any class variables that may be
	 * needed here. These variable will be accessible by the entire class. This
	 * may include things like previous results.
	 **/

	/**********************
	 * Constructor
	 * 
	 * Must have this function in the AILogic class. The only constructor
	 * accepts the AIApp as an argument so different server variables are
	 * accessible. The constructor must contain a super(a) call - which will run
	 * the base AILogic constructor and it must set the gamePreference variables
	 * for the games. Constructor name must be the same as tha class name.
	 * 
	 * @param a
	 *            The AIApp to which this logic class belongs.
	 * 
	 **/
	public AILogic_simple(AIApp a) {

		// Call to AILogic constructor - required.
		super(a);

		// Set game preferences - required. The gamePreference variable is an
		// array list with preference correcsponding to
		// games in the gameLabels or gameClasses arrays lists in AIApp. This
		// variable is used to determine
		// which game the AI likes to play when it deals. It must consist of an
		// equal number of integers as there
		// are games available. For equal preference to deal all games just put
		// an integer 1 in all array spots.
		// In this example, the AI only likes to play Texas Hold'Em.

		for (int i = 0; i < theAIApp.getGameClasses().size(); i++) {
			if (new String("TexasHoldEm").equals((String) theAIApp
					.getGameClasses().get(i))) {
				gamePreference.add(new Integer(1));
			} else {
				gamePreference.add(new Integer(0));
			}
		}

		// If you decide to create any class specific variables (below) - they
		// must be initialize here as well
		// This might include things like remembering previous actions by
		// players, previous results, etc...

	}

	/**********************
	 * newGame()
	 * 
	 * The newGame() function is called whenver a new game is about to start.
	 * This function should contain any new game initialization that needs done
	 * for this AI. For the simple AI, there is nothing special. Even if it does
	 * nothing, this fuction still must be present.
	 **/
	public void newGame() {

		// Do this so the prevActions list gets reset for every game.
		prevActions = new ArrayList();
	}

	/**********************
	 * timeToBet()
	 * 
	 * The timeToBet() function is called when it is this AI player's turn to
	 * bet. The actual bet will be placed by making a call to
	 * theAIApp.getAIAction().action( ArrayList a, ArrayList b ). The two
	 * arguments are used to define the percentage of actions. The first
	 * ArrayList is a list of strings that define the action. The second
	 * ArrayList is a list of integers that define the percentage for that
	 * corresponding action.
	 * 
	 * The following table shows the possible actions that can be passed to the
	 * action() function:
	 * 
	 * "FOLD" - will fold the current hand. "CHECK" - will first try to check
	 * the hand if possible. Careful: if check is not an option, the hand will
	 * be folded. "CALL" - will call any bet made. If not bet is made, this will
	 * be a check. "BET" - will bet on a hand - if a no limit game, must specify
	 * amount as described below. If previous bet is not met, hand will be
	 * folded. If the bet is higher than the previous bet, it will be a raise by
	 * the appropriate amount. "BET ONE" - will bet the value of the smallest
	 * button available to non-AI players. "BET TWO" - will bet the value of the
	 * second button. "BET THREE" - will be the value of the third button.
	 * "BET FOUR" - will bet the value of the fourth button. "BET POT" - will
	 * bet an amount equal to the pot. "BET ONE ONE THREE" - the 5 bet options
	 * from abouve can be combined to produce different bets. For example, to
	 * bet twice the pot you should do the next line "BET POT POT" - bet twice
	 * the pot. "RAISE" - will raise if its an option. If not an option, the AI
	 * will call. If a no limit game, then the amount must be specified the same
	 * way the bet amount is specified. The difference is the raise will start
	 * with the previous bet and increase. "ALLIN" - go All-In in a no limit
	 * game.
	 * 
	 * A string will be returned from the action() call, telling the AI logic
	 * class what actually happened. The returned string will be one of "ALLIN",
	 * "RAISE", "CALL", "CHECK", or "FOLD".
	 * 
	 **/
	protected void timeToBet() {
		ArrayList act = new ArrayList();
		ArrayList val = new ArrayList();

		// In this very simplified example, the AI will randomly check/fold 10
		// percent of the time, call 60 percent of the time, and raise by the
		// smallest
		// value 30 percent of the time. Not a very smart strategy, but for
		// example.
		// This AI only likes Texas Hold'Em, but so it can be used for testing
		// it will call most other games.

		if (gameClass.equals("TexasHoldEm")) {
			act.add(new String("CHECK"));
			val.add(new Integer(10));
			act.add(new String("CALL"));
			val.add(new Integer(60));
			act.add(new String("RAISE ONE"));
			val.add(new Integer(30));

			// All AI's must do the next piece of logic : if the game requires
			// interaction (such as trading cards or something), the AI cannot
			// play so it must FOLD.
			// An AI which doesn't do this will hang up the game if one required
			// interaction.

		} else if (server.getGame().requiresInteraction) {
			act.add(new String("FOLD"));
			val.add(new Integer(100));
		} else {
			act.add(new String("CALL"));
			val.add(new Integer(100));
		}

		// The next line is the actual line that passes the arguments to
		// determine the actual action that will be performed.
		// It can be included multiple times in the function depending on logic
		// trees, but it must be called once and only once per turn
		// or unexpected results will happen. A return String will be sent back
		// letting the AI know exactly what it did.

		prevActions.add(theAIApp.getAIAction().action(act, val));
	}

	/**********************
	 * otherPlayerAction()
	 * 
	 * The otherPlayerAction() function is called whenver a player (besides this
	 * AI) does something. The string will contain the players name and the
	 * action taken by the player. This data can be saved for whatever use the
	 * AI needs. The simple AI is not smart enough to think about what the other
	 * players are doing. Even if it does nothing, this fuction still must be
	 * present.
	 * 
	 * @param m
	 *            The string that describes the other player's action
	 * 
	 **/
	protected void otherPlayerAction(String m) {
	}

	/**********************
	 * stopLogic()
	 * 
	 * The stopLogic() function must be defined. This is called when the AI is
	 * closed. It is used so the AILogic class can shut down any special windows
	 * or Threads or other processes that were created for this AI class.
	 **/
	public void stopLogic() {
	}
}

/**************************************************************************************
 * List of useful functions and variables for the AI:
 * +------------+--------------
 * ---------------------------------+----------------
 * ----------------------------------------------------------- | class type |
 * fuction/variable name | description
 * +------------+----------------------------
 * -------------------+----------------
 * ----------------------------------------------------------- | AIApp |
 * theAIApp | the instance of this AI application - an instance of this AILogic
 * class is a variable in theAIApp. | StartPoker | server | the instance of the
 * server that started this AI. | ArrayList | gamePreference | preference of
 * whaich game this AI player will deal on its turn to deal. Must be set in this
 * class constructor. | String | gameClass | the name of the game that is
 * currently being played. This variable is set externally when a new game
 * starts. | Hand | myHand | this player's current hand - including shared
 * cards. More on the Hand class later. | int | myPosition | this player's
 * position - # of seats from the dealer. The dealer's position will be the
 * number of players in the game. | int | numPlayersIn | the number of players
 * who are still in (able to win) the current game. | int | numPlayersDealt |
 * the number of players who were dealt to in this game | int | actionNum | the
 * action number the game is currently on. What this means depends on the game
 * being played. | int | prevActionNum | this is how the AI can determine that a
 * new action has begun - rather than the betting getting around again in the
 * same round. | float | handRank | the current numerical rank assigned to this
 * players best hand so far. More on hand rankings later. | float | bestPossible
 * | the best possible hand rank value that can be made given the cards shown.
 * If handRank = bestPossible, the AI had the best hand. | ArrayList |
 * prevActions | list of actions this AI has done during this game. It is set
 * and reset in this class as shown above.
 * +------------+------------------------
 * -----------------------+----------------
 * -----------------------------------------------------------
 * 
 * The AIApp class has some useful information the AI might want to access:
 * +----
 * --------+-----------------------------------------------+----------------
 * ----------------------------------------------------------- | class type |
 * fuction/variable name typical use | description
 * +------------+----------------
 * -------------------------------+----------------
 * ----------------------------------------------------------- | ArrayList |
 * theAIApp.getGameLabels() | returns a list of String classes of possible games
 * that might be played. | ArrayList | theAIApp.getPlayerList() | returns a list
 * of Player classes of players currently sitting at the table. The list is
 * ordered by the order by which people joined | | | the game - not their seat
 * order. | int | theAIApp.dealerIndex | the index in the player list who is the
 * dealer. | boolean | theAIApp.antes | if true, there will be antes. This is
 * set by the server. | boolean | theAIApp.blinds | if true, the initial bet
 * will be blinds. This is set by the server. | boolean | theAIApp.noLimit | if
 * true, the game will be No Limit. This is set by the server. | boolean |
 * theAIApp.potLimit | if true, the game will be Pot Limit. This is set by the
 * server. | boolean | theAIApp.betLimit | if true, the game will be Bet Limit.
 * This is set by the server. | | | If the all three of above terms are false,
 * the game will be structured betting. | PokerMoney | theAIApp.smallBlind | the
 * value of the smallBlind | PokerMoney | theAIApp.ante | the value of the ante
 * | PokerMoney | theAIApp.minimumBet | the minimum bet in structured betting
 * games | PokerMoney | theAIApp.maximumBet | the maximum bet in Bet Limit games
 * | PokerMoney | theAIApp.button1Val | the value attached to the 1st betting
 * button (for human players) | PokerMoney | theAIApp.button2Val | the value
 * attached to the 1st betting button (for human players) | PokerMoney |
 * theAIApp.button3Val | the value attached to the 1st betting button (for human
 * players) | PokerMoney | theAIApp.button4Val | the value attached to the 1st
 * betting button (for human players) | Player | theAIApp.getThisPlayer() | gets
 * the instance of this AI player | AIFrame | theAIApp.getWindow() | gets the
 * instance of the mail window for this AI player.
 * +------------+----------------
 * -------------------------------+----------------
 * -----------------------------------------------------------
 * 
 * The Player class contains some informations about each Player. Most of it
 * cannot be accessed by another player, but some things can be:
 * +------------+--
 * -----------------------------------------------------------+--
 * ----------------------------------------------------------------------- |
 * class type | fuction/variable name typical use | description
 * +------------+----
 * ---------------------------------------------------------+--
 * ----------------------------------------------------------------------- |
 * String | ((Player)theAIApp.getPlayerList().get(index)).getName() | returns a
 * String which is the players name. | PokerMoney |
 * ((Player)theAIApp.getPlayerList().get(index)).getPrevBet() | returns the
 * PokerMoney class of the players most recent bet during the current round. |
 * PokerMoney | ((Player)theAIApp.getPlayerList().get(index)).getBankroll() |
 * returns the PokerMoney class of the palyers bankroll.
 * +------------+----------
 * ---------------------------------------------------+--
 * -----------------------------------------------------------------------
 * 
 * The StartPoker class is the server and contains some useful information:
 * +----
 * --------+-----------------------------------------------+----------------
 * ---------------------------------------------------------- | class type |
 * function/variable name typical use | description
 * +------------+----------------
 * -------------------------------+----------------
 * ---------------------------------------------------------- | PokerMoney |
 * server.getPot() | the money currently in the pot. | PokerGame |
 * server.getGame() | the instance of the current game. If not in a game, this
 * will return null.
 * +------------+----------------------------------------------
 * -+--------------------------------------------------------------------------
 * 
 * To actually use a PokerMoney class, you can use the following functions
 * function:
 * +------------+-----------------------------------------------+------
 * -------------------------------------------------------------------- | class
 * type | function/variable name typical use | description
 * +------------+--------
 * ---------------------------------------+----------------
 * ---------------------------------------------------------- | float |
 * server.getPot.amount() | the amount() function will return the float value
 * stored in any PokerMoney class instance.
 * +------------+------------------------
 * -----------------------+----------------
 * ----------------------------------------------------------
 * 
 * The Hand class contains the players hand:
 * +------------+----------------------
 * -------------------------+----------------
 * ---------------------------------------------------------- | class type |
 * function/variable name typical use | description
 * +------------+----------------
 * -------------------------------+----------------
 * ---------------------------------------------------------- | int |
 * myHand.getNumHole() | number of cards in this player hand which are down |
 * int | myHand.getNumUp() | number of cards in this player hand which are up |
 * int | myHand.getNumShared() | number of cards in this player hand which are
 * shared with all other players | Card | myHand.getHoleCard(index) | get a
 * specific card from the hole - specified by index | Card |
 * myHand.getUpCard(index) | get a specific card facing up - specified by index
 * | Card | myHand.getSharedCard(index) | get a specific shared card - specified
 * by index
 * +------------+-----------------------------------------------+--------
 * ------------------------------------------------------------------
 * 
 * The PokerGame class has some useful information as well:
 * +------------+--------
 * ---------------------------------------+----------------
 * ---------------------------------------------------------- | class type |
 * function/variable name typical use | description
 * +------------+----------------
 * -------------------------------+----------------
 * ---------------------------------------------------------- | PokerMoney |
 * server.getGame().getCurrBet() | the current bet that must be met | String |
 * server.getGame().getHighBetName() | the name of the player who posted the
 * current bet - not those who called it afterwards. | boolena |
 * server.getGame().requiresInteraction | if this game requires and special
 * interaction that the AI can't handle. Fold if this is set true. | static int
 * | PokerGame.MAX_PLAYERS = 8 | maximum number of players allowed to connect to
 * a game.
 * +------------+-----------------------------------------------+--------
 * ------------------------------------------------------------------
 * 
 * 
 * There is also a hand evaluator class which can be used to get a floating
 * point value of the player hand. Its use can be summarized as follows:
 * 
 * Construct an instance of the class with the following statement:
 * HandEvaluator he = new HandEvaluator();
 * 
 * The following overloaded fucntion can be used: float he.rankHand( Card c1 );
 * float he.rankHand( Card c1, Card c2 ); float he.rankHand( Card c1, Card c2,
 * Card c3 ); float he.rankHand( Card c1, Card c2, Card c3, Card c4 ); float
 * he.rankHand( Card c1, Card c2, Card c3, Card c4, Card c5 );
 * 
 * Generally, you should use the Hand.get*Card() functions to pass the cards as
 * arguments. All of these functions will give the cards passed as arguments,
 * and floating point score which can be broken down as follows.
 * 
 * Royal Straight Flush = 150.0f Five of a kind = 135.0f - 149.99999f Straight
 * flush = 120.0f - 134.99999f Four of a kind = 105.0f - 119.99999f Full House =
 * 90.0f - 104.99999f Flush = 75.0f - 89.99999f Straight = 60.0f - 74.99999f
 * Three of a kind = 45.0f - 59.99999f Two Pair = 30.0f - 44.99999f Pair = 15.0f
 * - 29.99999f High Card = 0.0f - 14.99999f
 * 
 * The higher the number is in the range, the better of that type of hand it is.
 * The decimal places make up the kicker(s)
 * 
 * 
 * A static function can also be used to actually print out the name of the hand
 * (including card names) based on the passed float value: static String
 * HandEvaluator.nameHand( float f ); for example: HandEvaluator.nameHand( 74.0f
 * ) returns the String "Straight: Ace high"
 * 
 * 
 * 
 * One last note...for debugging:
 * 
 * System.out.println( String s ); can concatenate strings by use of the + sign.
 * Most everything will print to screen nicely without formatting needed. EX -
 * prints out name, pot, and two hole cards. System.out.println( name +
 * "'s bet.  " + server.getPot() + "    " + hand.getHoleCard(0) + "  " +
 * hand.getHoleCard(1) );
 ***************************************************************************************/
