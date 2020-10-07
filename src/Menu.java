
import java.util.Scanner;

public class Menu {

    static Scanner scanner = new Scanner(System.in);

    static boolean REALLY_QUIT_DIALOG = true;

    static public void greeting_1(){
        System.out.println("\n\n\n");
        System.out.println(
                " .d8888b.                                      .d8888b.  888      8888888 \n" +
                        "d88P  Y88b                                    d88P  Y88b 888        888   \n" +
                        "Y88b.                                         888    888 888        888   \n" +
                        " \"Y888b.    8888b.  88888b.   .d88b.  888d888 888        888        888   \n" +
                        "    \"Y88b.     \"88b 888 \"88b d8P  Y8b 888P\"   888        888        888   \n" +
                        "      \"888 .d888888 888  888 88888888 888     888    888 888        888   \n" +
                        "Y88b  d88P 888  888 888 d88P Y8b.     888     Y88b  d88P 888        888   \n" +
                        " \"Y8888P\"  \"Y888888 88888P\"   \"Y8888  888      \"Y8888P\"  88888888 8888888 \n" +
                        "                    888                                                   \n" +
                        "                    888                                                   \n" +
                        "                    888                                                   "
        );
        System.out.println("Welcome to SaperCLI v1.0!");
        System.out.println("Game made by Banogrono");
    }

    static public int displayMenu(){
        boolean isChoiceOK = false;
        int choice;
        do {
            System.out.println("\n[1] Start new game ");
            System.out.println("[2] Options ");
            System.out.println("[3] Help ");
            System.out.println("[4] Quit \n");
            System.out.print("> ");
            choice =  scanner.nextInt();
            if (choice > 4 || choice < 1) {
                System.out.println("Unknown option.");
            }
            else {
                isChoiceOK = true;
            }

        } while (!isChoiceOK);

        if (choice == 2) {
           return displayOptions();
        }

        if (choice == 3) {
            helpDialog();
        }

        else if (choice == 4) {
            if (REALLY_QUIT_DIALOG) {
                System.out.println("Do you really want to quit? Y/ N");
                if (scanner.next().equals("y") || scanner.next().equals("Y") ) {
                    System.exit(1); // terminate app
                }
                else {
                    displayMenu();
                }
            }
            System.exit(1); // terminate app
        }
        return -2;
    }

    static public int displayOptions(){
        System.out.println("===================| Change Settings Here |===================");
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

        switch (choice) {
            case 1: {
                return 1;
            }
            case 2: {
                return 2;
            }
            case 3: {
                return 3;
            }
            case 4: {
                displayMenu();
            }
        }
        return 9;
    }

    static void helpDialog() {
        System.out.println(" Your job is to flag all mines on the field and don't kill yourself.");
        System.out.println(" In order to play game, first enter the column, then the row of spot and then command.");
        System.out.println(" The default commands are \"free\", that shows content of the selected cell and \"mine\", that puts flag on selected cell.");
        System.out.println(" For example we can free the cell in column 4 and row 3 by typing in \"4 3 free\".");
    }

    }


