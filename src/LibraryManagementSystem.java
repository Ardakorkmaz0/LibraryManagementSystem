package librarymanagementsystem;

import javax.swing.SwingUtilities;

public class LibraryManagementSystem {

    public static void main(String[] args) {
        LibraryManager lib = new LibraryManager(); // construct a library
        System.out.printf("%35s" , "Main menu\nOperations :\n");
        System.out.println("1 - Adding book to library");


        // Start the UI Thread safely
        SwingUtilities.invokeLater(() -> {
            LibraryUI gui = new LibraryUI(lib);
            gui.setVisible(true);
        });

    }

}