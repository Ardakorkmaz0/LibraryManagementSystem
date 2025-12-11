package librarymanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UnifiedLibraryGUI extends JFrame {

    LibraryManager lib;

    public UnifiedLibraryGUI(LibraryManager lib) {
        this.lib = lib;

        // Basic window setup
        setTitle("Library Management System - Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Centers the window on screen
        setLayout(new BorderLayout());

        // Main container for all our tabs
        JTabbedPane mainTabs = new JTabbedPane();

        // Section 1: Book Management
        JTabbedPane bookTabs = new JTabbedPane();
        bookTabs.addTab("Add Book", createAddBookPanel());
        bookTabs.addTab("Remove Book", createRemoveBookPanel());
        bookTabs.addTab("Search Book", createSearchBookPanel());
        bookTabs.addTab("Inventory", createInventoryPanel()); // Shows the list of books

        mainTabs.addTab("Book Management", bookTabs);

        //  Section 2: User/Member Management
        JTabbedPane userTabs = new JTabbedPane();
        userTabs.addTab("Register", createRegisterPanel());
        userTabs.addTab("Search User", createSearchUserPanel());
        userTabs.addTab("Delete User", createDeleteUserPanel());

        mainTabs.addTab("Member Management", userTabs);

        // Section 3: Circulation (Borrow/Return)
        JTabbedPane circulationTabs = new JTabbedPane();
        circulationTabs.addTab("Borrow Book", createBorrowPanel());
        circulationTabs.addTab("Return Book", createReturnPanel());

        mainTabs.addTab("Circulation", circulationTabs);

        // Section 4: System (Login/Undo)
        JTabbedPane systemTabs = new JTabbedPane();
        systemTabs.addTab("Login", createLoginPanel());
        systemTabs.addTab("Undo", createUndoPanel());

        mainTabs.addTab("System", systemTabs);

        // Add everything to the frame
        add(mainTabs, BorderLayout.CENTER);

        setVisible(true);
        setAlwaysOnTop(true);
        toFront();
        requestFocus();
        setAlwaysOnTop(false);

    }

    // Tab: Add a new book to the library
    private JPanel createAddBookPanel() {
        JPanel panel = new JPanel(new FlowLayout());

        JLabel lTitle = new JLabel("Book Title:");
        JTextField tTitle = new JTextField(15);
        JLabel lAuthor = new JLabel("Author:");
        JTextField tAuthor = new JTextField(15);
        JButton bAdd = new JButton("Add Book");

        bAdd.addActionListener(e -> {
            String title = tTitle.getText();
            String author = tAuthor.getText();

            // Basic validation
            if(title.isEmpty() || author.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Fill all fields!");
            } else {
                // Try to add to the backend
                boolean isAdded = lib.addBook(title, author);
                if (isAdded) {
                    JOptionPane.showMessageDialog(this, "Book Added!");
                    // Clear fields after success
                    tTitle.setText(""); tAuthor.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "Error: Duplicate Book!");
                }
            }
        });

        panel.add(lTitle); panel.add(tTitle);
        panel.add(lAuthor); panel.add(tAuthor);
        panel.add(bAdd);
        return panel;
    }

    // Tab: Remove a book by title
    private JPanel createRemoveBookPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        JLabel lTitle = new JLabel("Title to Remove:");
        JTextField tTitle = new JTextField(20);
        JButton bRemove = new JButton("Remove");

        bRemove.addActionListener(e -> {
            boolean success = lib.removeBookFromFile(tTitle.getText());
            if(success){
                JOptionPane.showMessageDialog(this, "Book Removed!");
                tTitle.setText("");
            }
            else{
                JOptionPane.showMessageDialog(this, "Book not found.");
            }
        });

        panel.add(lTitle); panel.add(tTitle); panel.add(bRemove);
        return panel;
    }

    // Tab: Search by Title OR Author
    private JPanel createSearchBookPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        JButton bTitle = new JButton("Search by Title");
        JButton bAuthor = new JButton("Search by Author");

        // Logic for Title Search
        bTitle.addActionListener(e -> {
            String title = JOptionPane.showInputDialog("Enter Title:");
            if(title != null) {
                Book b = lib.searchBook(title);
                if(b != null){
                    JOptionPane.showMessageDialog(this, "Found: " + b.getTitle() + " - " + b.getAuthor());
                }
                else{
                    JOptionPane.showMessageDialog(this, "Not Found.");
                }
            }
        });

        // Logic for Author Search
        bAuthor.addActionListener(e -> {
            String author = JOptionPane.showInputDialog("Enter Author:");
            if(author != null) {
                String res = lib.searchByAuthor(author);
                JOptionPane.showMessageDialog(this, (res != null ? res : "No books found."));
            }
        });

        panel.add(bTitle); panel.add(bAuthor);
        return panel;
    }

    // Tab: Show all books (A-Z or Popularity)
    private JPanel createInventoryPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel topPanel = new JPanel();
        JButton bAlpha = new JButton("Sort A-Z");
        JButton bPop = new JButton("Sort Popularity");
        topPanel.add(bAlpha); topPanel.add(bPop);

        JTextArea area = new JTextArea();
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));
        area.setText(lib.showLibraryAlphabetic()); // Default view on load

        bAlpha.addActionListener(e -> {
            area.setText(lib.showLibraryAlphabetic());
            area.setCaretPosition(0); // Reset scroll to top
        });

        bPop.addActionListener(e -> {
            area.setText(lib.showLibraryPopularity());
            area.setCaretPosition(0); // Reset scroll to top
        });

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(area), BorderLayout.CENTER);
        return panel;
    }

    // Tab: Register a new user
    private JPanel createRegisterPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setLayout(new GridLayout(4, 2, 5, 5)); // Grid for cleaner alignment

        panel.add(new JLabel("Name:"));
        JTextField tName = new JTextField();
        panel.add(tName);

        panel.add(new JLabel("Surname:"));
        JTextField tSurname = new JTextField();
        panel.add(tSurname);

        panel.add(new JLabel("Age:"));
        JTextField tAge = new JTextField();
        panel.add(tAge);

        JButton bReg = new JButton("Register");
        panel.add(new JLabel("")); // Placeholder to align button
        panel.add(bReg);

        bReg.addActionListener(e -> {
            try {
                // Parse age safely
                int age = Integer.parseInt(tAge.getText());
                User u = lib.userManager.registerUser(tName.getText(), tSurname.getText(), age);
                JOptionPane.showMessageDialog(this, "Registered! ID: " + u.getId());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid Age!");
            }
        });
        return panel;
    }

    // Tab: Find user info
    private JPanel createSearchUserPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        JButton bID = new JButton("Search ID");
        JButton bName = new JButton("Search Name");

        bID.addActionListener(e -> {
            String id = JOptionPane.showInputDialog("Enter User ID:");
            if(id != null) {
                String res = lib.userManager.searchByIdFormatted(id);
                JOptionPane.showMessageDialog(this, res != null ? res : "Not Found");
            }
        });

        bName.addActionListener(e -> {
            String name = JOptionPane.showInputDialog("Enter Name Surname:");
            if(name != null) {
                String res = lib.userManager.searchByName(name);
                JOptionPane.showMessageDialog(this, res != null ? res : "Not Found");
            }
        });

        panel.add(bID); panel.add(bName);
        return panel;
    }

    // Tab: Delete a user by their unique ID
    private JPanel createDeleteUserPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        JTextField tID = new JTextField(15);
        JButton bDel = new JButton("Delete User (ID)");

        bDel.addActionListener(e -> {
            if(lib.userManager.deleteUser(tID.getText()))
                JOptionPane.showMessageDialog(this, "User Deleted.");
            else
                JOptionPane.showMessageDialog(this, "User Not Found.");
        });

        panel.add(new JLabel("User ID:")); panel.add(tID); panel.add(bDel);
        return panel;
    }

    // Tab: Borrowing logic
    private JPanel createBorrowPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        JTextField tTitle = new JTextField(20);
        JButton bBorrow = new JButton("Borrow Book");

        bBorrow.addActionListener(e -> {
            // Check if someone is logged in first
            if(lib.userManager.getActiveUser() == null) {
                JOptionPane.showMessageDialog(this, "Please LOGIN first (System Tab).");
                return;
            }
            String res = lib.borrowBook(tTitle.getText(), lib.userManager.getActiveUser().getId());
            JOptionPane.showMessageDialog(this, res);
        });

        panel.add(new JLabel("Book Title:")); panel.add(tTitle); panel.add(bBorrow);
        return panel;
    }

    // Tab: Returning logic
    private JPanel createReturnPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        JTextField tTitle = new JTextField(20);
        JButton bReturn = new JButton("Return Book");

        bReturn.addActionListener(e -> {
            // Ensure login
            if(lib.userManager.getActiveUser() == null) {
                JOptionPane.showMessageDialog(this, "Please LOGIN first.");
                return;
            }
            String res = lib.returnBook(tTitle.getText(), lib.userManager.getActiveUser().getId());
            JOptionPane.showMessageDialog(this, res);
        });

        panel.add(new JLabel("Book Title:"));
        panel.add(tTitle);
        panel.add(bReturn);
        return panel;
    }

    // Tab: Handle User Login/Logout
    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        JTextField tID = new JTextField(15);
        JButton bLogin = new JButton("Login");
        JButton bLogout = new JButton("Logout");
        JLabel lStatus = new JLabel("Status: Not Logged In");

        // 1. Login Action
        bLogin.addActionListener(e -> {
            boolean success = lib.userManager.login(tID.getText());
            if(success){
                lStatus.setText("Status: Logged in as " + lib.userManager.getActiveUser().getName());
                tID.setText("");
            }
            else{
                JOptionPane.showMessageDialog(this, "Login Failed.");
            }
        });

        // 2. Logout Action
        bLogout.addActionListener(e -> {
            if (lib.userManager.getActiveUser() != null) {
                lib.userManager.logout(); // Call the manager's logout logic
                lStatus.setText("Status: Not Logged In"); // Update GUI label
                JOptionPane.showMessageDialog(this, "Logged out successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "No user is currently logged in.");
            }
        });

        panel.add(new JLabel("User ID:"));
        panel.add(tID);
        panel.add(bLogin);
        panel.add(bLogout);
        panel.add(lStatus);
        return panel;
    }
    // Tab: Undo the last transaction
    private JPanel createUndoPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        JButton bUndo = new JButton("UNDO Last Action");
        JLabel lLast = new JLabel("Last Action: " + lib.undoManager.getLastActionName());

        bUndo.addActionListener(e -> {
            lib.undoManager.undo();
            // Update label to show what comes next in the stack
            lLast.setText("Last Action: " + lib.undoManager.getLastActionName());
            JOptionPane.showMessageDialog(this, "Undo Performed.");
        });

        panel.add(bUndo);
        panel.add(lLast);
        return panel;
    }
}