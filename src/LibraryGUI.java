package librarymanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LibraryGUI extends JFrame {

    LibraryManager lib;
    UndoManager undo;



    public LibraryGUI(LibraryManager lib, int option) {
        this.lib = lib;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());

        if (option == 1) { // Option for add book
            setTitle("Add Book");
            setSize(300, 300);
            initAddBookGUI();
        } else if (option == 2) { // Option for remove book
            setTitle("Remove Book");
            setSize(300, 200);
            initRemoveBookGUI();
        } else if (option == 3) { // Option for search book
            setTitle("Search Book");
            setSize(350, 150);
            initSearchBookGUI();
        }
        else if(option == 4){ // Option for showing inventory
            setTitle("Inventory");
            setSize(600, 350);
            initShowLibraryGUI();
        }
        else if (option == 5) { // Option for registering new member
            setTitle("Register New Member");
            setSize(300, 300);
            initRegisterGUI();
        }
        else if (option == 6) { // Option for user login
            setTitle("User Login");
            setSize(300, 150);
            initLoginGUI();
        }
        else if (option == 8) { // Option for deleting user
            setTitle("Delete User");
            setSize(300, 150);
            initDeleteUserGUI();
        }
        else if(option == 9){ // Option for Undo Operation
            setTitle("Undo Operation");
            setSize(200, 150);
            initUndoGUI();
        }
        else if (option == 10) { // Option for search User
            setTitle("Search User");
            setSize(350, 150);
            initSearchUserGUI();
        }
        else if (option == 12) { // Option for borrow Book
            setTitle("Borrow Book");
            setSize(300, 200);
            initBorrowGUI();
        }
        else if (option == 13) { // Option for return Book
            setTitle("Return Book");
            setSize(300, 200);
            initReturnGUI();
        }
        setVisible(true);
    }

//-------------------------------------------------------------------------------------------------------------------------------------------



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

//-------------------------------------------------------------------------------------------------------------------------------------------



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

//-------------------------------------------------------------------------------------------------------------------------------------------


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
                    Book foundBook = lib.searchBook(title);

                    if (foundBook != null) {
                        // Show Status Details
                        String status = foundBook.isAvailable() ? "[AVAILABLE]" : "[BORROWED BY ID: " + foundBook.getCurrentHolderId() + "]";

                        String message = "Book Found!\n" +
                                "Title: " + foundBook.getTitle() + "\n" +
                                "Author: " + foundBook.getAuthor() + "\n" +
                                "Status: " + status;

                        JOptionPane.showMessageDialog(null, message, "Search Result", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Error: Book not found.", "Search Result", JOptionPane.ERROR_MESSAGE);
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


//-------------------------------------------------------------------------------------------------------------------------------------------

    private void initShowLibraryGUI() {
        // Use BorderLayout to place buttons at the top and text in the center
        setLayout(new BorderLayout());

        // Top Panel for Buttons
        JPanel pButtons = new JPanel();
        JButton bAlphabetic = new JButton("Sort by Title (A-Z)");
        JButton bPopularity = new JButton("Sort by Popularity");

        // Add buttons to the panel
        pButtons.add(bAlphabetic);
        pButtons.add(bPopularity);

        // Center Area for Text
        JTextArea tArea = new JTextArea();
        tArea.setEditable(false);
        tArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        // Load default view (Alphabetic)
        tArea.setText(lib.showLibraryAlphabetic());

        JScrollPane scrollPane = new JScrollPane(tArea);

        // Button Actions

        // Show Alphabetic List
        bAlphabetic.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tArea.setText(lib.showLibraryAlphabetic());
                // Scroll back to top
                tArea.setCaretPosition(0);
            }
        });

        // Action: Show Popularity List
        bPopularity.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tArea.setText(lib.showLibraryPopularity());
                tArea.setCaretPosition(0);
            }
        });

        // Add components to the main frame
        add(pButtons, BorderLayout.NORTH); // Buttons at the top
        add(scrollPane, BorderLayout.CENTER); // Text area in the middle
    }

//-------------------------------------------------------------------------------------------------------------------------------------------




    public void initUndoGUI() {
        // 1. Get the reference to the UndoManager from the LibraryManager
        UndoManager manager = lib.undoManager;

        // 2. Create a Label to show the last action name
        JLabel lStatus = new JLabel();

        // Check the current status immediately when window opens
        String lastAction = manager.getLastActionName();
        if (lastAction.isEmpty()) {
            lStatus.setText("No actions to undo.");
        } else {
            lStatus.setText("Last Action: " + lastAction);
        }

        // Create the Undo Button
        JButton bUndo = new JButton("Confirm Undo");

        // Creat the Close Button
        JButton bClose = new JButton("close");

        // Add Action Listener to handle the click
        bUndo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Check if there is actually something to undo
                if (manager.getLastActionName().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "There is no action left to undo!", "Warning", JOptionPane.WARNING_MESSAGE);
                    lStatus.setText("No actions to undo.");
                    return;
                }

                // Perform the undo logic
                manager.undo();

                // Show success message
                JOptionPane.showMessageDialog(null, "Undo operation completed successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);

                // Refresh the label to show the *next* available undo action (if any)
                String nextAction = manager.getLastActionName();
                if (nextAction.isEmpty()) {
                    lStatus.setText("No actions to undo.");
                } else {
                    lStatus.setText("Next Undo: " + nextAction);
                }
            }
        });
        bClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Closes the current window (Undo Window) but keeps the main app running
                dispose();
            }
        });
        // Add components to the layout
        // Using a vertical box layout or flow layout from constructor
        add(lStatus);
        add(bUndo);
        add(bClose);
    }

//-------------------------------------------------------------------------------------------------------------------------------------------



    private void initRegisterGUI() {
        JLabel lName = new JLabel("Name:");
        JTextField tName = new JTextField(20);

        JLabel lSurname = new JLabel("Surname:");
        JTextField tSurname = new JTextField(20);

        JLabel lAge = new JLabel("Age:");
        JTextField tAge = new JTextField(20);

        JButton bRegister = new JButton("Register");

        bRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = tName.getText().trim();
                String surname = tSurname.getText().trim();
                String ageText = tAge.getText().trim();

                // check if any field is empty
                if(name.isEmpty() || surname.isEmpty() || ageText.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill all fields!");
                    return;
                }

                try {
                    int age = Integer.parseInt(ageText);
                    // call the register method and get the created user
                    User newUser = lib.userManager.registerUser(name, surname, age);

                    // prepare the success message with the unique ID
                    String message = "Registration Successful!\n\n" +
                            "Welcome, " + newUser.getName() + "\n" +
                            "YOUR ID: " + newUser.getId() + "\n\n" +
                            "Please save this ID to login.";

                    // using JTextArea to allow copying the ID
                    JTextArea textArea = new JTextArea(message);
                    textArea.setEditable(false);
                    JOptionPane.showMessageDialog(null, textArea);

                    dispose(); // close the window after registration
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Age must be a valid number!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        add(lName); add(tName);
        add(lSurname); add(tSurname);
        add(lAge); add(tAge);
        add(bRegister);
    }

//-------------------------------------------------------------------------------------------------------------------------------------------




    // METHOD: Login GUI Initialization
    private void initLoginGUI() {
        JLabel lId = new JLabel("Enter User ID:");
        JTextField tId = new JTextField(20);
        JButton bLogin = new JButton("Login");

        bLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = tId.getText().trim();

                if(id.isEmpty()){
                    JOptionPane.showMessageDialog(null, "Please enter an ID.");
                    return;
                }

                // try to login with the given ID
                boolean success = lib.userManager.login(id);

                if(success) {
                    User activeUser = lib.userManager.getActiveUser();
                    JOptionPane.showMessageDialog(null, "Login Successful!\nWelcome back, " + activeUser.getName());
                    dispose(); // close the window after successful login
                } else {
                    JOptionPane.showMessageDialog(null, "Login Failed: ID not found.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        add(lId); add(tId);
        add(bLogin);
    }
//-------------------------------------------------------------------------------------------------------------------------------------------



    // METHOD: Delete User GUI Initialization
    private void initDeleteUserGUI() {
        JLabel lId = new JLabel("Enter User ID to Delete:");
        JTextField tId = new JTextField(20);
        JButton bDelete = new JButton("Delete User");

        bDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = tId.getText().trim();
                if (id.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter an ID!", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                // Confirmation dialog to prevent accidental deletion
                int confirm = JOptionPane.showConfirmDialog(null,
                        "Are you sure you want to delete user " + id + "?",
                        "Confirm Deletion",
                        JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    // Call the logic from UserManager
                    boolean success = lib.userManager.deleteUser(id);

                    if (success) {
                        JOptionPane.showMessageDialog(null, "User Deleted Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        tId.setText(""); // Clear the field
                    } else {
                        JOptionPane.showMessageDialog(null, "Error: User ID not found.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        add(lId);
        add(tId);
        add(bDelete);
    }
    //-------------------------------------------------------------------------------------------------------------------------------------------

    private void initBorrowGUI() {
        JLabel lInfo = new JLabel("Enter Book Title to Borrow:");
        JTextField tTitle = new JTextField(20);
        JButton bBorrow = new JButton("Borrow");

        bBorrow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = tTitle.getText();

                // Ensure a user is logged in
                User activeUser = lib.userManager.getActiveUser();
                if(activeUser == null) {
                    JOptionPane.showMessageDialog(null, "You must Login first!", "Error", JOptionPane.ERROR_MESSAGE);
                    dispose();
                    return;
                }

                if (!title.isEmpty()) {
                    // Call the Manager
                    String result = lib.borrowBook(title, activeUser.getId());
                    JOptionPane.showMessageDialog(null, result);
                    if(result.startsWith("Success") || result.startsWith("Book is currently")) {
                        dispose();
                    }
                }
            }
        });

        add(lInfo);
        add(tTitle);
        add(bBorrow);
    }
    //-------------------------------------------------------------------------------------------------------------------------------------------

    private void initReturnGUI() {
        JLabel lInfo = new JLabel("Enter Book Title to Return:");
        JTextField tTitle = new JTextField(20);
        JButton bReturn = new JButton("Return Book");

        bReturn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = tTitle.getText();

                User activeUser = lib.userManager.getActiveUser();
                if(activeUser == null) {
                    JOptionPane.showMessageDialog(null, "You must Login first!", "Error", JOptionPane.ERROR_MESSAGE);
                    dispose();
                    return;
                }

                if (!title.isEmpty()) {
                    String result = lib.returnBook(title, activeUser.getId());
                    JOptionPane.showMessageDialog(null, result);
                    dispose();
                }
            }
        });
        add(lInfo);
        add(tTitle);
        add(bReturn);
    }

    //-------------------------------------------------------------------------------------------------------------------------------------------


    private void initSearchUserGUI() {
        JLabel lInfo = new JLabel("Select Search Method:");

        // Button 1: Search by ID
        JButton bSearchId = new JButton("Search by ID");
        bSearchId.setPreferredSize(new Dimension(140, 40));

        // Button 2: Search by Name
        JButton bSearchName = new JButton("Search by Name");
        bSearchName.setPreferredSize(new Dimension(140, 40));

        // Action: Search by ID
        bSearchId.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = JOptionPane.showInputDialog(null, "Enter User ID:");
                if (id != null && !id.trim().isEmpty()) {
                    String result = lib.userManager.searchByIdFormatted(id.trim());

                    if (result != null) {
                        JOptionPane.showMessageDialog(null, result, "Search Result", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "User ID not found!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // Action: Search by Name and Surname
        bSearchName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Prompt user for full name
                String fullName = JOptionPane.showInputDialog(null, "Enter Name and Surname (e.g. Arda Korkmaz):");

                if (fullName != null && !fullName.trim().isEmpty()) {
                    // Call the search method with the full string
                    String result = lib.userManager.searchByName(fullName.trim());

                    if (result != null) {
                        // Display results
                        JTextArea textArea = new JTextArea(result);
                        textArea.setEditable(false);
                        JScrollPane scrollPane = new JScrollPane(textArea);
                        scrollPane.setPreferredSize(new Dimension(300, 200));

                        JOptionPane.showMessageDialog(null, scrollPane, "Search Results", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "No user found with this name.", "Result", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        });

        add(lInfo);
        add(bSearchId);
        add(bSearchName);
    }



}