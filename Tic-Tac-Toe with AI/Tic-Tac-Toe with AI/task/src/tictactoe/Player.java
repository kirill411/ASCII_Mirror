package tictactoe;

import java.util.Random;
import java.util.Scanner;

abstract class Player {
    protected char symbol;
    abstract void makeMove(char[] array);
}

abstract class AIPlayer extends Player {
    protected static final int[][] winCombinations =
            {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, {0, 4, 8}, {2, 4, 6}};

    private final Random random = new Random();

    protected int randomMove(char[] array) {
        int index;

        do {
            index = random.nextInt(array.length);
        } while (array[index] != ' ');

        return index;
    }
}

class User extends Player {
    private static Scanner scanner;

    User(Scanner scanner, char symbol) {
        User.scanner = scanner;
        this.symbol = symbol;
    }

    @Override
    public void makeMove(char[] array) {
        String[] input;

        while (true) {
            System.out.print("Enter the coordinates: ");
            input = scanner.nextLine().split("\\s+", 2);

            if (!validInput(input[0]) || !validInput(input[1])) {
                continue;
            }

            int x = Integer.parseInt(input[0]);
            int y = Integer.parseInt(input[1]);

            int index = (x - 1) * 3 + y - 1;

            if (array[index] != ' ') {
                System.out.println("This cell is occupied! Choose another one!");
                continue;
            }

            array[index] = symbol;
            break;
        }
    }

    private boolean validInput(String input) {
        if (!input.matches("[0-9]+")) {
            System.out.println("You should enter numbers!");
            return false;
        }
        if (!input.matches("[1-3]")) {
            System.out.println("Coordinates should be from 1 to 3!");
            return false;
        }
        return true;
    }
}

class EasyAIPlayer extends AIPlayer {

    EasyAIPlayer(char symbol) {
        this.symbol = symbol;
    }

    @Override
    public void makeMove(char[] array) {
        int index = randomMove(array);
        array[index] = symbol;
        System.out.println("Making move level \"easy\"");
    }
}

class MediumAIPlayer extends AIPlayer {

    MediumAIPlayer(char symbol) {
        this.symbol = symbol;
    }

    @Override
    public void makeMove(char[] array) {
        int index = twoInARow(array);
        if (index < 0) {
            index = randomMove(array);
        }
        array[index] = symbol;
        System.out.println("Making move level \"medium\"");
    }

    private int twoInARow(char[] array) {
        for (int[] winCombo : winCombinations) {
            int thisPlayer = 0;
            int otherPlayer = 0;
            int emptyCellIndex = 0;

            for (int index : winCombo) {
                if (array[index] == symbol) {
                    thisPlayer++;
                } else if (array[index] != ' ') {
                    otherPlayer++;
                } else {
                    emptyCellIndex = index;
                }
            }

            if (thisPlayer == 2 && otherPlayer == 0
                    || otherPlayer == 2 && thisPlayer == 0) {
                return emptyCellIndex;
            }
        }
        return -1;
    }
}

class HardAIPlayer extends AIPlayer {

    HardAIPlayer(char symbol) {
        this.symbol = symbol;
    }

    @Override
    void makeMove(char[] array) {
        int bestScore = Integer.MIN_VALUE;
        int bestMove = randomMove(array);

        int[] emptyCells = getEmptyCells(array);
        for (int i : emptyCells) {
            array[i] = symbol;
            int score = minimax(array, 9, false);
            array[i] = ' ';

            if (score > bestScore) {
                bestScore = score;
                bestMove = i;
            }
        }

        array[bestMove] = symbol;
    }

    int minimax(char[] array, int depth, boolean maximize) {
        int result = score(array);
        if (result != 0 || gameOver(array) || depth == 0) {
            return result;
        }

        int[] emptyCells = getEmptyCells(array);

        if (maximize) {
            int bestScore = Integer.MIN_VALUE;
            for (int i : emptyCells) {
                array[i] = symbol;
                int score = minimax(array, depth--, false);
                array[i] = ' ';
                bestScore = Math.max(bestScore, score);
            }
            return bestScore;
        } else {
            int worstScore = Integer.MAX_VALUE;
            for (int i : emptyCells) {
                array[i] = getOpponent(symbol);
                int score = minimax(array, depth--, true);
                array[i] = ' ';
                worstScore = Math.min(worstScore, score);
            }
            return worstScore;
        }
    }

    private char getOpponent(char symbol) {
        return symbol == 'X' ? 'O' : 'X';
    }

    int score(char[] array) {
        for (int[] winCombo : winCombinations) {
            if (array[winCombo[0]] == array[winCombo[1]]
                    && array[winCombo[1]] == array[winCombo[2]]
                    && array[winCombo[0]] != ' ') {
                return array[winCombo[0]] == symbol ? 10 : -10;
            }
        }
        return 0;
    }

    boolean gameOver(char[] array) {
        for (char c : array) {
            if (c == ' ') {
                return false;
            }
        }
        return true;
    }

    int[] getEmptyCells(char[] array) {
        int length = 0;
        for (char c : array) {
            if (c == ' ') {
                length++;
            }
        }

        int[] result = new int[length];
        int resIndex = 0;

        for (int i = 0; i < array.length; i++) {
            if (array[i] == ' ') {
                result[resIndex++] = i;
            }
        }

        return result;
    }
}
