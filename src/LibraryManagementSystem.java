package librarymanagementsystem;

import javax.swing.SwingUtilities;
import java.util.Scanner;

public class LibraryManagementSystem {

    public static void main(String[] args) {
        LibraryManager lib = new LibraryManager(); // construct a library
        Scanner sc = new Scanner(System.in);
        UndoManager undoManager = lib.undoManager;
        UserManager userManager = lib.userManager;

        boolean isRunning = true;

        // Loop to keep the menu active
        while (isRunning) {
            System.out.println("--- Main Menu ---");
            System.out.println("1 - Add Book (Open GUI)");
            System.out.println("2 - Remove Book (Open GUI)");
            System.out.println("3 - Search Book (Open GUI)");
            System.out.println("4 - Show Library (Open GUI)");
            System.out.println("5 - Register New User (Open GUI)");
            System.out.println("6 - Login (Open GUI)");
            System.out.println("7 - Logout (Now Console)");
            System.out.println("8 - Remove User (Open GUI)");
            System.out.println("9 - Undo Operation (Open GUI)");
            System.out.println("10 - Search User (Open GUI)");
            System.out.println("12 - Borrow Book (Open GUI)");
            System.out.println("13 - Return Book (Open GUI)");
            System.out.println("0 - Exit");
            System.out.print("Select an option: ");

            // Validate if the input is an integer
            if (sc.hasNextInt()) {
                int option = sc.nextInt();

                if (option == 1) {
                    System.out.println("Launching Add Book Window...");
                    // Open UI in mode 1
                    SwingUtilities.invokeLater(() -> {
                        new LibraryGUI(lib, 1);
                    });

                }
                else if (option == 2) {
                    System.out.println("Launching Remove Book Window...");
                    // Open UI in mode 2
                    SwingUtilities.invokeLater(() -> {
                        new LibraryGUI(lib, 2);
                    });

                }
                else if (option == 3) {
                    System.out.println("Launching Search Book Window...");
                    // Open UI in mode 3
                    SwingUtilities.invokeLater(() -> {
                        new LibraryGUI(lib, 3);
                    });
                }
                else if(option == 4) {
                    System.out.println("Launching Show Library Window...");
                    // Open UI in mode 4
                    lib.showLibrary();
                    SwingUtilities.invokeLater(() -> {
                        new LibraryGUI(lib, 4);
                    });
                }
                else if (option == 5) {
                    System.out.println("Launching Register New User Window...");
                    // Open UI in mode 5
                    SwingUtilities.invokeLater(() -> {
                        new LibraryGUI(lib, 5);
                    });
                }
                else if (option == 6) {
                    System.out.println("Launching Login Window...");
                    // Open UI in mode 6
                    SwingUtilities.invokeLater(() -> {
                        new LibraryGUI(lib, 6);
                    });
                }
                else if (option == 7) {
                    System.out.println("Loging out...");
                    userManager.logout();

                }
                else if(option == 8) {
                    System.out.println("Launching Delete User Window...");
                    // Open UI in mode 8
                    SwingUtilities.invokeLater(() -> {
                        new LibraryGUI(lib, 8);
                    });
                }
                else if(option == 9) {
                    System.out.println("Launching Undo Window...");
                    // Open UI in mode 9
                    SwingUtilities.invokeLater(() -> {
                        new LibraryGUI(lib, 9);
                    });
                }
                else if (option == 10) {
                    System.out.println("Launching Search User Window...");
                    // Open UI in mode 10
                    SwingUtilities.invokeLater(() -> {
                        new LibraryGUI(lib, 10);
                    });
                }
                else if(option == 12) {
                    if(lib.userManager.getActiveUser() == null){
                        System.out.println("ERROR: You must LOGIN first (Option 6).");
                    } else {
                        SwingUtilities.invokeLater(() -> {
                            new LibraryGUI(lib, 12);
                        });
                    }
                }
                else if(option == 13) {
                    if (lib.userManager.getActiveUser() == null) {
                        System.out.println("ERROR: You must LOGIN first (Option 6).");
                    } else {
                        SwingUtilities.invokeLater(() -> {
                            new LibraryGUI(lib, 13);
                        });
                    }
                }
                else if (option == 0) {
                    System.out.println("Exiting system...");
                    isRunning = false; // Break the loop
                }
                else {
                    System.out.println("Invalid option. Please try again.");
                }
            }
            else {
                System.out.println("Please enter a valid number!");
                sc.next(); // Consume invalid input to prevent infinite loop
            }
            System.out.println("------------------------------------------------");
        }
        sc.close();
    }
}