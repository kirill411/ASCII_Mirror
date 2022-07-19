package tictactoe;


import java.util.Scanner;
import java.util.Arrays;

public class Main {

    static Scanner scanner = new Scanner(System.in);
    static boolean endProgram = false;

    static Player[] players = new Player[2];
    static char[] gameArray = new char[9];

    public static void main(String[] args) {

        while (true) {
            requestParameters();
            if (endProgram) {
                break;
            }
            printField();
            gameOn();
        }

    }

    private static void gameOn() {
        while (true) {
            for (Player player : players) {
                player.makeMove(gameArray);
                printField();
                if (endOfTheGame()) {
                    return;
                }
            }
        }
    }

    private static void requestParameters() {

        while (true) {
            System.out.print("Input command: ");
            String[] input = scanner.nextLine().trim().split("\\s+", 3);

            if (input.length == 1 && input[0].isEmpty()) {
                continue;
            }

            if (input.length == 1 && "exit".equals(input[0])) {
                endProgram = true;
                return;
            }

            if ( input.length != 3
                    || !input[0].matches("start")
                    || !input[1].matches("user|easy|medium|hard")
                    || !input[2].matches("user|easy|medium|hard")) {
                System.out.println("Bad parameters!");
                continue;
            }


            Arrays.fill(gameArray, ' ');
            addPlayers(input);
            break;
        }
    }

    private static void addPlayers(String[] input) {
        char c = 'X';

        for (int i = 1; i < input.length; i++) {
            int index = i - 1;
            if ("user".equals(input[i])) {
                players[index] = new User(scanner, c);
            }
            if ("easy".equals(input[i])) {
                players[index] = new EasyAIPlayer(c);
            }
            if ("medium".equals(input[i])) {
                players[index] = new MediumAIPlayer(c);
            }
            if ("hard".equals(input[i])) {
                players[index] = new HardAIPlayer(c);
            }
            c = 'O';
        }
    }

    private static boolean endOfTheGame() {
        int[][] winCombinations =
                {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, {0, 4, 8}, {2, 4, 6}};

        for (int[] winCombo : winCombinations) {
            if (gameArray[winCombo[0]] == gameArray[winCombo[1]]
                    && gameArray[winCombo[1]] == gameArray[winCombo[2]]
                    && gameArray[winCombo[0]] != ' ') {
                System.out.printf("%c wins\n", gameArray[winCombo[0]]);
                return true;
            }
        }

        for (char c : gameArray) {
            if (c == ' ') {
                return false;
            }
        }

        System.out.println("Draw");
        return true;
    }

    private static void printField() {
        System.out.println("---------");
        for (int i = 0; i < 3; i++) {
            int n = i * 3;
            System.out.printf("| %c %c %c |\n", gameArray[n], gameArray[n + 1], gameArray[n + 2]);
        }
        System.out.println("---------");
    }
}
