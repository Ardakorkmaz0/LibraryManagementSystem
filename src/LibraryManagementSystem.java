package librarymanagementsystem;

import javax.swing.SwingUtilities;
import java.util.Scanner;

public class LibraryManagementSystem {

    public static void main(String[] args) {
        LibraryManager lib = new LibraryManager(); // construct a library
        Scanner sc = new Scanner(System.in);

        lib.showLibrary();
        
        boolean isRunning = true;

        // Loop to keep the menu active
        while (isRunning) {
            System.out.println("--- Main Menu ---");
            System.out.println("1 - Add Book (Open UI)");
            System.out.println("2 - Remove Book (Open UI)");
            System.out.println("3 - Search Book (Open UI)");
            System.out.println("0 - Exit");
            System.out.print("Select an option: ");

            // Validate if the input is an integer
            if (sc.hasNextInt()) {
                int option = sc.nextInt();

                if (option == 1) {
                    System.out.println("Launching Add Book Window...");
                    // Open UI in mode 1
                    SwingUtilities.invokeLater(() -> {
                        new LibraryUI(lib, 1);
                    });

                }
                else if (option == 2) {
                    System.out.println("Launching Remove Book Window...");
                    // Open UI in mode 2
                    SwingUtilities.invokeLater(() -> {
                        new LibraryUI(lib, 2);
                    });

                }
                else if (option == 3) {
                    System.out.println("Launching Search Book Window...");
                    // Open UI in mode 3
                    SwingUtilities.invokeLater(() -> {
                        new LibraryUI(lib, 3);
                    });
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
        }
        
        lib.showLibrary();
        
        sc.close();

    }

}