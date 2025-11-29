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
            setTitle("Add Book");
            setSize(300, 300);
            initAddBookUI();
        } else if (option == 2) {
            setTitle("Remove Book");
            setSize(300, 200);
            initRemoveBookUI();
        } else if (option == 3) { // New option for Search
            setTitle("Search Book");
            setSize(300, 200);
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
                    // UPDATED: Now calling the unified addBook method which returns boolean
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

    // --- NEW: Search Book UI Initialization ---
    private void initSearchBookUI() {
        JLabel lTitle = new JLabel("Enter Title to Search:");
        JTextField tTitle = new JTextField(20);
        JButton bSearch = new JButton("Search Book");

        bSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = tTitle.getText();
                if (title.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter a title!");
                }
                else {
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
        add(lTitle);
        add(tTitle);
        add(bSearch);
    }
}