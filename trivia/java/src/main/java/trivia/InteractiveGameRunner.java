
package trivia;

import java.util.Scanner;


public class InteractiveGameRunner {

    public static void main(String[] args) {
        boolean notAWinner = false;
        Game aGame = new Game();

        aGame.startWithPlayers("Chet", "Pat");

        do {
            aGame.nextTurn();

            if (aGame.roll(readNumber())) {

                if ("y".equals(readAnswer())) {
                    notAWinner = aGame.wasCorrectlyAnswered();
                } else {
                    notAWinner = aGame.wrongAnswer();
                }
            }

        } while (notAWinner);

    }

    private static String readAnswer() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine().toLowerCase();
    }

    private static int readNumber() {
        Scanner scanner = new Scanner(System.in);
        int number = scanner.nextInt();
        scanner.nextLine();
        return number;
    }
}
