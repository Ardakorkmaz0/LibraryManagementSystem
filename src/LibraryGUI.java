package librarymanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LibraryGUI extends JFrame {

    LibraryManager lib;



    public LibraryGUI(LibraryManager lib, int option) {
        this.lib = lib;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());

        if (option == 1) {
            setTitle("Add Book"); // Option for add book
            setSize(300, 300);
            initAddBookGUI();
        } else if (option == 2) { // Option for remove book
            setTitle("Remove Book");
            setSize(300, 200);
            initRemoveBookGUI();
        } else if (option == 3) { // Option for search book
            setTitle("Search Book");
            setSize(350, 150); // Adjusted size for two buttons
            initSearchBookGUI();
        }
        else if(option == 4){
            setTitle("Inventory");
            setSize(350, 350);
            initShowLibraryGUI();
        }
        setVisible(true);
    }




    private void initAddBookGUI() {

        JLabel lTitle = new JLabel("Book Title:");
        JTextField tTitle = new JTextField(20);
        JLabel lAuthor = new JLabel("Author Name:");
        JTextField tAuthor = new JTextField(20);
        JButton bAdd = new JButton("Add Book");

        bAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = tTitle.getText();
                String author = tAuthor.getText();

                if(title.isEmpty() || author.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill all fields!");
                }
                else {
                    //Calling the unified addBook method
                    boolean isAdded = lib.addBook(title, author);
                    if (isAdded) {
                        JOptionPane.showMessageDialog(null, "Book Added Successfully!");
                        tTitle.setText("");
                        tAuthor.setText("");
                    } else {
                        // If returns false, it means duplicate exists
                        JOptionPane.showMessageDialog(null, "Error: This book already exists in the library!", "Duplicate Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        add(lTitle); add(tTitle);
        add(lAuthor); add(tAuthor);
        add(bAdd);
    }




    private void initRemoveBookGUI() {
        JLabel lTitle = new JLabel("Enter Title to Delete:");
        JTextField tTitle = new JTextField(20);
        JButton bRemove = new JButton("Remove Book");

        bRemove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = tTitle.getText();

                if (title.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter a title!");
                } else {
                    // Calling the simplified file removal method
                    boolean success = lib.removeBookFromFile(title);

                    if (success) {
                        JOptionPane.showMessageDialog(null, "Book Removed Successfully!");
                        tTitle.setText("");
                    } else {
                        JOptionPane.showMessageDialog(null, "Error: Book not found.");
                    }
                }
            }
        });
        add(lTitle); add(tTitle);
        add(bRemove);
    }




    // Search Book UI Initialization (Split into Title and Author)
    private void initSearchBookGUI() {
        JLabel lInfo = new JLabel("Select Search Method:");

        // Button 1: Search by Title
        JButton bSearchTitle = new JButton("Search by Title");
        bSearchTitle.setPreferredSize(new Dimension(140, 40));

        // Button 2: Search by Author
        JButton bSearchAuthor = new JButton("Search by Author");
        bSearchAuthor.setPreferredSize(new Dimension(140, 40));

        // Action for Title Search
        bSearchTitle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = JOptionPane.showInputDialog(null, "Enter Book Title:");

                if (title != null && !title.trim().isEmpty()) {
                    // Call the search method from LibraryManager
                    Book foundBook = lib.searchBook(title);

                    if (foundBook != null) {
                        // Display book details if found
                        String message = "Book Found!\n" +
                                "Title: " + foundBook.getTitle() + "\n" +
                                "Author: " + foundBook.getAuthor();
                        JOptionPane.showMessageDialog(null, message, "Search Result", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Error: Book not found in the library.", "Search Result", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // Action for Author Search
        bSearchAuthor.addActionListener(new ActionListener() {
            @Override
                public void actionPerformed(ActionEvent e) {             // Open input dialog to get author name
                String author = JOptionPane.showInputDialog(null, "Enter Author Name:");

                    if (author != null && !author.trim().isEmpty()) { // Validate input (check if not null or empty)

                        String result = lib.searchByAuthor(author); // Call the search method from LibraryManager

                        if (result != null) {

                            String message = "Books by " + author + ":\n\n" + result; // Prepare the message with the list of books found

                            // Create a JTextArea to display results (supports multiple lines)
                            JTextArea textArea = new JTextArea(message);
                            textArea.setEditable(false);
                            textArea.setLineWrap(true);
                            textArea.setWrapStyleWord(true);

                            // Add a ScrollPane in case the list is long
                            JScrollPane scrollPane = new JScrollPane(textArea);
                            scrollPane.setPreferredSize(new Dimension(300, 200));

                            // Show the results in a dialog
                            JOptionPane.showMessageDialog(null, scrollPane, "Search Results", JOptionPane.INFORMATION_MESSAGE);

                        } else {
                            // Show warning if no books found
                            JOptionPane.showMessageDialog(null, "No books found for this author.", "Result", JOptionPane.WARNING_MESSAGE);
                        }
                    }
                }
            });
        add(lInfo);
        add(bSearchTitle);
        add(bSearchAuthor);
    }



    private void initShowLibraryGUI() {
        JLabel lTitle = new JLabel("Inventory:");
        // JTextArea is better for multi-line text display
        JTextArea tArea = new JTextArea(15, 30);
        tArea.setText(lib.showLibrary()); // Call the method and fill the area
        tArea.setEditable(false); // Make it read-only
        // Add scroll bar in case of many books
        JScrollPane scrollPane = new JScrollPane(tArea);
        add(lTitle);
        add(scrollPane); // Add the scroll pane containing the text area
    }



}