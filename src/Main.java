
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.lang.System;
import java.net.URLDecoder;


public class Main {

    //------------------------ Look & Feel  Settings ----------------------
    static String SAFE_FIELD_CHARACTER = " . ";
    static String MINE_CHARACTER = " X ";
    static String HIDDEN_FIELD_CHARACTER = " []";
    static String FLAG_CHARACTER = " P ";

    //--------- Commands
    final static String CLAIM_FIELD_COMMAND = "free";
    final static String FLAG_FIELD_COMMAND = "mine";

    //--------- Messages
    final static String DEFEAT_MESSAGE = "KABOOM! You exploded. Smoke and bloody vapor cover what left from your body. " +
            "The air is saturated with metallic smell of blood mixed with smell of dirt." + "It's so quiet now...";
    final static String VICTORY_MESSAGE = "Congratulations! You found all mines!";

    //--------- Gameplay
    static boolean SAFE_START = true;
    static int boardWidth = 9;
    static int boardHeight = 9;

    //--------- Debug
    final static boolean MENU_ON = true;
    static Boolean SHOW_MINE_MAP = false;
    final static String makeItReadable = " "; // space FTW
    public static String systemName = System.getProperty("os.name").toLowerCase();

    //---------------------------------------------------------------------

    public static Scanner scanner = new Scanner(System.in);

    public static boolean isGameOver = false;

    static int[] mineFiller(int minesNumber, int tableSize) {
        Random random = new Random();
        int[] mineSpotTable = new int[minesNumber];
        int mineSpot;

        for (int i = 0; i < minesNumber; i++) {
            mineSpot = random.nextInt(tableSize + 1);
            if (!isSpotOccupied(mineSpotTable, mineSpot)) {
                mineSpotTable[i] = mineSpot;
            } else {
                i--;
            }
        }
        return mineSpotTable;
    }

    static boolean isSpotOccupied(int[] mineSpotTable, int mineSpot) {
        for (int j : mineSpotTable) {
            if (j == mineSpot) {
                return true;
            }
        }
        return false;
    }

    static void fillSpot(int spot, String[][] table2D) {
        int place = 1;
        boolean isSpotFound = false;

        for (int i = 0; i < table2D.length; i++) {
            if (isSpotFound) {
                break;
            }
            for (int j = 0; j < table2D[i].length; j++) {
                if (place == spot) {
                    table2D[i][j] = MINE_CHARACTER;
                    isSpotFound = true;
                    break;
                } else {
                    place++;
                }
            }
        }
    }

    static void putMinesOnField(int[] mineSpotTable, String[][] mineField) {
        Arrays.sort(mineSpotTable);
        for (int j : mineSpotTable) {
            fillSpot(j, mineField);
        }
    }

    static void printMineField(String[][] mineField, boolean HIDE_MINES) {
        for (int i = 0; i < mineField.length; i++) {

            if (i == 0) {
                for (int k = 0; k <= mineField[0].length + 1; k++) {
                    if (k == 0) {
                        System.out.print(makeItReadable + makeItReadable + " |");
                    } else if (k == mineField[0].length + 1) {
                        System.out.print(makeItReadable + "|");
                    } else {
                        if (k < 10) {
                            System.out.print(makeItReadable + k + makeItReadable);
                        } else {
                            System.out.print(makeItReadable + k);
                        }

                    }
                }
                System.out.println();
                int multiply = 1;
                if (makeItReadable.equals(" ")) {
                    multiply = 3;

                }
                for (int k = 0; k <= multiply * (mineField[0].length + 1); k++) {
                    if (k == 0) {
                        System.out.print(makeItReadable + makeItReadable + "-|");
                    } else if (k == multiply * (mineField[0].length + 1)) {
                        System.out.print("|");
                    } else if (k == 3) { // it always printed one "-" too much, so i put it here xD
                        System.out.print("");
                    }
                    else {
                        System.out.print("-");
                    }
                }
                System.out.println();
            }

            for (int j = 0; j < mineField[i].length; j++) {

                if (j == 0) {
                    if (i < 10) {
                        System.out.print(i + 1 + makeItReadable + makeItReadable + "|");
                    } else {
                        System.out.print(i + 1 + makeItReadable + "|");
                    }
                }
                if (HIDE_MINES) {
                    if (mineField[i][j].equals(MINE_CHARACTER)) {
                        System.out.print(SAFE_FIELD_CHARACTER);
                    } else {
                        System.out.print(mineField[i][j]);
                    }
                } else {
                    System.out.print(mineField[i][j]);
                }

                if (j == mineField[i].length - 1)
                    System.out.print(makeItReadable + "|");
            }
            System.out.println();

            if (i == mineField.length - 1) {
                int multiply = 1;
                if (makeItReadable.equals(" ")) {
                    multiply = 3;

                }
                for (int k = 0; k <= multiply * (mineField[0].length + 1); k++) {
                    if (k == 0) {
                        System.out.print(makeItReadable + makeItReadable + "-|");
                    } else if (k == multiply * (mineField[0].length + 1)) {
                        System.out.print("|");
                    } else if (k == 3) { // it always printed one "-" too much, so i put it here xD
                        System.out.print("");
                    }
                    else {
                        System.out.print("-");
                    }
                }
                System.out.println();
            }

        }
    }

    static void fillTable(String[][] table, final String symbol) {
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[i].length; j++) {
                table[i][j] = symbol;
            }
        }
    }

    static int minesAround(String[][] mineField, int boardWidth, int boardHeight, int x, int y) {
        int minesAround = 0;

        if (x > 0) {
            if (y > 0 && mineField[x - 1][y - 1].equals(MINE_CHARACTER)) { // #1
                minesAround++;
            }
            if (mineField[x - 1][y].equals(MINE_CHARACTER)) { // #2
                minesAround++;
            }
            if (y < (boardWidth - 1) && mineField[x - 1][y + 1].equals(MINE_CHARACTER)) { // #3
                minesAround++;
            }
        }

        if (y > 0 && mineField[x][y - 1].equals(MINE_CHARACTER)) { // #4
            minesAround++;
        }
        if (y < (boardWidth - 1) && mineField[x][y + 1].equals(MINE_CHARACTER)) { // #5
            minesAround++;
        }
        if (x < (boardHeight - 1)) {
            if (y > 0 && mineField[x + 1][y - 1].equals(MINE_CHARACTER)) { // #6
                minesAround++;
            }
            if (mineField[x + 1][y].equals(MINE_CHARACTER)) { // #7
                minesAround++;
            }
            if (y < (boardWidth - 1) && mineField[x + 1][y + 1].equals(MINE_CHARACTER)) { // #8
                minesAround++;
            }
        }
        return minesAround;
    }

    static void putNumbersOnField(String[][] mineField, int boardWidth, int boardHeight) {
        int numberHolder;
        for (int i = 0; i < boardHeight; i++) {
            for (int j = 0; j < boardWidth; j++) {
                if (!mineField[i][j].equals(MINE_CHARACTER)) {
                    numberHolder = minesAround(mineField, boardWidth, boardHeight, i, j);
                    if (numberHolder != 0) {
                        mineField[i][j] = makeItReadable + numberHolder + makeItReadable;
                    }
                }
            }
        }
    }

    static void flagOrFreeField(final String[][] mineField, String[][] gameMineField, String[][] referenceMineFiled) {
        System.out.println("Set/delete mines marks or claim a cell as free: ");
        int x;
        int y;
        String command;

        try {
            y = scanner.nextInt() - 1;
            x = scanner.nextInt() - 1;
            command = scanner.next();


            if (SAFE_START) {
                if (mineField[x][y].equals(MINE_CHARACTER) && command.equals(CLAIM_FIELD_COMMAND)) {
                    safeFirstMove(mineField, referenceMineFiled, x, y);
                    SAFE_START = false;
                }
            }


            switch (command) {
                case FLAG_FIELD_COMMAND: {

                    if (!mineField[x][y].equals(SAFE_FIELD_CHARACTER) && !mineField[x][y].equals(MINE_CHARACTER) && !mineField[x][y].equals(FLAG_CHARACTER)) {
                        System.out.println("There is a number here!");
                    } else if (mineField[x][y].equals(FLAG_CHARACTER)) {
                        mineField[x][y] = SAFE_FIELD_CHARACTER;
                        gameMineField[x][y] = HIDDEN_FIELD_CHARACTER;
                    } else {
                        mineField[x][y] = FLAG_CHARACTER;
                        gameMineField[x][y] = FLAG_CHARACTER;
                    }
                    break;
                }
                case CLAIM_FIELD_COMMAND: {
                    if (gameMineField[x][y].equals(HIDDEN_FIELD_CHARACTER)) {
                        checkField(mineField, gameMineField, x, y);
                        break;
                    }
                }
                default: {
                    System.out.println("Unknown command.");
                }
            }
        } catch (Exception e) {
            System.out.println("Invalid input. Numbers range from 1 to " + mineField.length + " for first number,\nand from 1 to " + mineField[0].length + " for second. Please enter correct number: \n");
        }
    }

    static void checkField(final String[][] mineField, String[][] gameMineField, final int column, final int row) {
        if (mineField[column][row].equals(MINE_CHARACTER)) {
            isGameOver = true;
        } else {
            revealFields(mineField, gameMineField, column, row);
        }
    }

    static boolean victoryCheck(String[][] mineField, String[][] referenceMineField, int numberOfMines) {
        int minesDetected = 0;
        boolean isSafeSpotFlagged = false;

        for (int i = 0; i < mineField.length; i++) {
            for (int j = 0; j < mineField[i].length; j++) {
                if (mineField[i][j].equals(FLAG_CHARACTER)) {
                    if (referenceMineField[i][j].equals(MINE_CHARACTER)) {
                        minesDetected++;
                    } else if (referenceMineField[i][j].equals(SAFE_FIELD_CHARACTER)) {
                        isSafeSpotFlagged = true;
                    }
                }
            }
        }

        return !isSafeSpotFlagged && numberOfMines == minesDetected;
    }

    static void copyField(String[][] mineField, String[][] referenceMineField) {
        for (int i = 0; i < mineField.length; i++) {
            for (int j = 0; j < mineField[i].length; j++) {
                referenceMineField[i][j] = mineField[i][j];
            }
        }
    }

    static void revealFields(final String[][] referenceMineField, String[][] gameMineField, final int column, final int row) {

        if (!gameMineField[column][row].equals(MINE_CHARACTER) && !gameMineField[column][row].equals(HIDDEN_FIELD_CHARACTER)) {
            return;
        }

        if (!referenceMineField[column][row].equals(MINE_CHARACTER)) {
            gameMineField[column][row] = referenceMineField[column][row];

            if (column != 0) {
                revealFields(referenceMineField, gameMineField, (column - 1), row);
            }
            if (row != 0) {
                revealFields(referenceMineField, gameMineField, (column), (row - 1));
            }
            if (column < referenceMineField.length - 1) {
                revealFields(referenceMineField, gameMineField, (column + 1), row);
            }
            if (row < referenceMineField[column].length - 1) {
                revealFields(referenceMineField, gameMineField, column, (row + 1));
            }
        }
    }

    static void safeFirstMove(String[][] mineFiled, String[][] referenceMineFiled, int x, int y) {

        Random random = new Random();
        int newX;
        int newY;

        // find new random spot for mine
        while (true) {
            newX = random.nextInt(mineFiled.length);
            newY = random.nextInt(mineFiled[0].length);
            if (!mineFiled[newX][newY].equals(MINE_CHARACTER)) {
                mineFiled[newX][newY] = MINE_CHARACTER;
                referenceMineFiled[newX][newY] = MINE_CHARACTER;
                break;
            }
        }
        // delete old mine
        mineFiled[x][y] = SAFE_FIELD_CHARACTER;
        referenceMineFiled[x][y] = SAFE_FIELD_CHARACTER;

        regenerateMineField(mineFiled);
    }

    static void regenerateMineField(String[][] mineField) {
        // part 1: clear field from old numbers
        for (int i = 0; i < mineField.length; i++) {
            for (int j = 0; j < mineField[i].length; j++) {
                if (!mineField[i][j].equals(MINE_CHARACTER)) {
                    mineField[i][j] = SAFE_FIELD_CHARACTER;
                }
            }
        }

        //part 2: generate new numbers
        putNumbersOnField(mineField, mineField.length, mineField[0].length);

    }

    static void gameplaySettings() {
        boolean isChoiceOK = false;
        int choice;
        do {
            System.out.println("=> Gameplay Settings");
            System.out.println("[1] Change mine field size. Current size: " + boardHeight + "x" + boardWidth);
            System.out.println("[2] Safe Start: " + SAFE_START + " (Makes sure your first move won't be your last)");
            System.out.println("[3] Back\n");
            System.out.print("> ");
            choice = scanner.nextInt();
            if (choice > 3 || choice < 1) {
                System.out.println("Unknown option.");
            } else {
                isChoiceOK = true;
            }
        } while (!isChoiceOK);

        switch (choice) {
            case 1: {
                int newHeight;
                int newWidth;
                System.out.println("\n Enter Height and then Width separated by space. Both of the numbers have to be integers.");
                try {
                    newHeight = scanner.nextInt();
                    newWidth = scanner.nextInt();
                    boardWidth = newWidth;
                    boardHeight = newHeight;
                } catch (Exception e) {
                    System.out.println("Input does not meet requirements.");
                }
                break;
            }
            case 2: {
                System.out.println("\n Do you want the Safe Start to be on? Y/ N");
                try {
                    String input = scanner.next();
                    SAFE_START = input.equals("Y") || input.equals("y");
                } catch (Exception e) {
                    System.out.println("Input does not meet the requirements.");
                }
                break;
            }
            case 3: {
                break;
            }
        }
    }

    static void lookAndFeelSettings() {
        boolean isChoiceOK = false;
        int choice;
        do {
            System.out.println("=> Look & Feel Settings");
            System.out.println("[1] Change safe spot symbol");
            System.out.println("[2] Change mine symbol");
            System.out.println("[3] Change hidden spot symbol");
            System.out.println("[4] Change flag symbol");
            System.out.println("[5] Back\n");
            System.out.print("> ");
            choice = scanner.nextInt();
            if (choice > 5 || choice < 1) {
                System.out.println("Unknown option.");
            } else {
                isChoiceOK = true;
            }
        } while (!isChoiceOK);

        switch (choice) {
            case 1: {
                System.out.println("\n Enter safe spot symbol: ");
                try {
                    SAFE_FIELD_CHARACTER = makeItReadable + scanner.next() + makeItReadable;
                } catch (Exception e) {
                    System.out.println("Input does not meet requirements.");
                }
                break;
            }
            case 2: {
                System.out.println("\n Enter mine symbol: ");
                try {
                    MINE_CHARACTER = makeItReadable + scanner.next() + makeItReadable;
                } catch (Exception e) {
                    System.out.println("Input does not meet requirements.");
                }
                break;
            }
            case 3: {
                System.out.println("\n Enter hidden spot symbol: ");
                try {
                    HIDDEN_FIELD_CHARACTER = makeItReadable + scanner.next() + makeItReadable;
                } catch (Exception e) {
                    System.out.println("Input does not meet requirements.");
                }
                break;
            }
            case 4: {
                System.out.println("\n Enter flag symbol: ");
                try {
                    FLAG_CHARACTER = makeItReadable + scanner.next() + makeItReadable;
                } catch (Exception e) {
                    System.out.println("Input does not meet requirements.");
                }
                break;
            }
            case 5: {
                break;
            }
        }
    }

    static void commandSettings() {

        System.out.println("=> Commands Settings");
        System.out.println("Work in progress.");

        /*
        boolean isChoiceOK = false;
        int choice;
        do {
            System.out.println("\n[1] Look & Feel ");
            System.out.println("[2] Gameplay ");
            System.out.println("[3] Commands");
            System.out.println("[4] Back\n");
            System.out.print("> ");
            choice =  scanner.nextInt();
            if (choice > 4 || choice < 1) {
                System.out.println("Unknown option.");
            }
            else {
                isChoiceOK = true;
            }
        } while (!isChoiceOK);

         */
    }

    public static void main(String[] args) throws Exception {

        // check if an argument was passed on jar execution
        if (false) {
            // get the path of the currently running jar
            final String jarPath = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            final String decodedPath = URLDecoder.decode(jarPath, StandardCharsets.UTF_8);
            // Setting for the terminal window title (Linux/Windows)
            final String windowTitle = "SaperCLI";
            // Check the current platform...
            if (systemName.contains("windows")) {
                // then start the new process with the OS or terminal dependent commands
                new ProcessBuilder("cmd", "/k", "start", "\"" + windowTitle + "\"", "java", "-jar", decodedPath.substring(1), "run").start();
            } else if (systemName.contains("mac")) {
                new ProcessBuilder("/bin/bash", "-c", "java", "-jar", decodedPath, "run").start();
            } else if (systemName.contains("linux")) {
                // TODO: add support for other Linux terminals
                new ProcessBuilder("xfce4-terminal", "--title=" + windowTitle, "--hold", "-x", "java", "-jar", decodedPath, "run").start();
            } else {
                // No OS could be detected
                System.err.println("OS could not be detected.");
            }
            // destroy the original process
            System.exit(0);
        } else {

            while (true) {
                SAFE_START = true;
                if (MENU_ON) {
                    int whichOption;
                    Menu.greeting_1();
                    whichOption = Menu.displayMenu();

                    if (whichOption < 0) {
                        System.out.println("Something went wrong...");
                    } else {

                        switch (whichOption) {
                            case 1: {
                                lookAndFeelSettings();
                                Menu.displayOptions();
                                break;
                            }
                            case 2: {
                                gameplaySettings();
                                Menu.displayOptions();
                                break;
                            }
                            case 3: {
                                commandSettings();
                                Menu.displayOptions();
                                break;
                            }
                            case 4: {
                                Menu.displayOptions();
                                break;
                            }
                        }
                    }
                }

                // final int boardWidth = 9;
                // final int boardHeight = 9;
                System.out.print("How many mines do you want on the field? ");
                final int numberOfMines = scanner.nextInt();

                System.out.println(); // REMOVE BEFORE FLIGHT

                int[] mineSpotTable = mineFiller(numberOfMines, boardHeight * boardWidth);

                String[][] mineField = new String[boardHeight][boardWidth];
                String[][] referenceMineField = new String[boardHeight][boardWidth];
                String[][] gameMineField = new String[boardHeight][boardWidth];

                fillTable(gameMineField, HIDDEN_FIELD_CHARACTER);

                fillTable(mineField, SAFE_FIELD_CHARACTER);
                putMinesOnField(mineSpotTable, mineField);

                copyField(mineField, referenceMineField);
                putNumbersOnField(mineField, boardWidth, boardHeight);

                // game loop
                printMineField(gameMineField, true);
                boolean victory = false;
                while (!victory) {

                    if (SHOW_MINE_MAP) {
                        System.out.println("Mine map: ");
                        printMineField(mineField, false);
                        System.out.println();
                    }

                    flagOrFreeField(mineField, gameMineField, referenceMineField);
                    if (isGameOver) {
                        break;
                    }
                    System.out.println();
                    printMineField(gameMineField, true);
                    victory = victoryCheck(mineField, referenceMineField, numberOfMines);
                }

                if (isGameOver) {
                    System.out.println(DEFEAT_MESSAGE);
                } else {
                    System.out.println(VICTORY_MESSAGE);
                }

                System.out.println("Do you want to play again? ");
                String playAgain = scanner.next();
                if (playAgain.equals("Y") || playAgain.equals("y")) {
                } else {
                    break;
                }
            }
        }
    }

}
