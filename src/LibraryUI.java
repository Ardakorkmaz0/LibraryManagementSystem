package librarymanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LibraryUI extends JFrame {

    LibraryManager lib;

    public LibraryUI(LibraryManager lib, int option) {
        this.lib = lib;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());

        if (option == 1) {
            setTitle("Add Book"); // Option for add book
            setSize(300, 300);
            initAddBookUI();
        } else if (option == 2) { // Option for remove book
            setTitle("Remove Book");
            setSize(300, 200);
            initRemoveBookUI();
        } else if (option == 3) { // Option for search book
            setTitle("Search Book");
            setSize(350, 150); // Adjusted size for two buttons
            initSearchBookUI();
        }
        setVisible(true);
    }

    private void initAddBookUI() {

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

    private void initRemoveBookUI() {
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
    private void initSearchBookUI() {
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
            public void actionPerformed(ActionEvent e) {
                String author = JOptionPane.showInputDialog(null, "Enter Author Name:");

                if (author != null && !author.trim().isEmpty()) {
                    // Call the new author search method
                    String result = lib.searchByAuthor(author);

                    if (result != null) {
                        String message = "Books by " + author + ":\n\n" + result;
                        JOptionPane.showMessageDialog(null, message, "Search Result", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "No books found for this author.", "Result", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        });

        add(lInfo);
        add(bSearchTitle);
        add(bSearchAuthor);
    }
}