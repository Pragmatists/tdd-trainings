
package trivia;

import java.util.Random;


public class GameRunner {


    public static void main(String[] args) {
        boolean notAWinner = false;
        Game aGame = new Game();

        aGame.startWithPlayers("Chet", "Pat", "Sue");

        Random rand = new Random();

        do {

            aGame.nextTurn();
            if (aGame.roll(rand.nextInt(5) + 1)) {

                if (rand.nextInt(5) == 4) {
                    notAWinner = aGame.wrongAnswer();
                } else {
                    notAWinner = aGame.wasCorrectlyAnswered();
                }
            }


        } while (notAWinner);

    }
}
