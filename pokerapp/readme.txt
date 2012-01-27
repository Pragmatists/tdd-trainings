PokerApp version 1.00

Please go to the PokerApp website for installation instructions and help.
http://pokerapp.sourceforge.net

Changes from version 0.99
-------------------------
- Moved project to SourceForge.net.  Made changes to package names and cleaned up/commented code to support open source development.
- Added log files that record all information so troubleshooting problems will have more data available.
- Added option to make the check and call buttons "one-click"
- Fixed an error where game would freeze if player selected "muck cards" option before sitting down at table.
- Allow both antes and blinds to be used at the same time.
- Added an option to open a "Message window" which will allow player to keep past messages displayed (and scroll back if desired).
- Rearranged the menu options a little to create a new menu "Window" which is used to open the various other windows that
  the player can look at.
- When player first connects to a server, that player will be able to see all cards in play.
- Added an applet class which can be used to play PokerApp through a webpage interface.
- An OK limit Texas Hold'em AI apponent (still in work)
- Other minor error / bug fixes.


Changes from version 0.98
-------------------------
- Fixed java.util.prefs warning messages on some Linux and Unix systems. 
- Added a field on the server window that displays the server IP address.
- If there are only 2 players, the dealer pays the small blind and is the first to act on the initial bet.  This is
  the correct poker rules.
- Added server options to automatically double the blinds after an elapsed time or number of hands.
- Added a better indication of when it is your turn to bet (turn the menu yellow with a message).
- Added a better indication of when it is your turn to deal (turn the menu orange with a message).
- For structured betting games, the "Submit Bet" button no longer has to be pressed.  Betting now works with just one click.
- Fix a looming bug when players go all-in.  Winner used to just get the entire pot, this is now fixed so that
  pot gets distributed fairly.  All possible pot divides and sharing should work correctly now.
- Fix minimum bets and raises for non-structured betting games.  The minimum bet now works correctly and the initial bet
  sets the minimum raise amount for each round.
- Fixed errors in displaying how much money each player has.
- Fixed IndexOutOfBounds exception which can happen when one player at the table runs out of money.
- Fixed error with encoding and decoding (for network communications) certain characters.  This could have caused a variety of problems.
- Added games Omaha 8 Hi/Lo Split, Seven Card Stud Low Chicago, Seven Card Stud High Chicago with Shipwreck, Hide the Salami, Iron Cross,
  Iron Cross with Wild Center, Pineapple, and Crazy Pineapple.


Changes from version 0.97
-------------------------
- Added command line options so that the server can be run from the command line only.
- Improved the server auto-deal significantly.  Should be less buggy and is now easier to select which games to deal.
- Added maximum bet limit and pot limit games to the rules (in addition to no-limit and structured betting games).
- Fixed blind games so that anyone who is new to the table must pay the big blind amount the first time they play.  This is done so
  that people can't take advantage of position by jumping in and out of games.
- Fixed a couple of bugs where the next dealer was chosen incorrectly.
- Several small other bug fixes.
- Change the "Join Game" and "Register" windows to stay on top of the PokerApp client window - so they accidentally aren't hidden.
- Replaced the Java "cup o' coffee" icon on the windows with a (nearly illegible) icon picturing stacks of poker chips.


Changes from version 0.96
-------------------------
- A couple minor bug fixes and performance tweaks
- Fixed a couple of problems in calculating winners and which player had the best hand up to that point in the game.
- Encrypted all communications across the network so they are more secure and more obscure.
- Display bankrolls for all players on the screen.
- Changed 5 game limit for unregistered version to a 10 game limit.
- Added new games Five Card Stud, Five Card Stud with Shipwreck, Five Card Draw, and Anaconda.


Changes from version 0.95
-------------------------
- Fixed problem with 4th raise attempt in limit poker games
- Fixed problem with bet button betting the wrong amount for the small blind on limit poker games
- Fixed problem with server not completely stopping listening for connections.  When the server is killed,
  it can now be restarted on the same port.
- Fixed problem with server not removing your registration information if you disconnect.
- Fixed most of the errors / hangups when a player is kicked out of a game or must leave because he is out of money.
- Fixed error when you beat a computer opponent 1 on 1 down to $0.00
- Fixed the display of labels under the players cards so that now then entire text can be read rather than
  getting chopped off.
- Performance enhancements for calculating the winners of the games - (needed so the new AI logic will be faster)
- A partial Limit Texas Hold'Em computer opponents - it still kind of sucks, and it only plays limit Texas Hold'Em,
  but its a start...

