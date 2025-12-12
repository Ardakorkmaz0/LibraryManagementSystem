package librarymanagementsystem;

import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LibraryManager {

    private titleBst titleTree = new titleBst();
    private authorBst authorTree = new authorBst();

    public PopularityHeap popularityHeap = new PopularityHeap(1000); // Maximum capacity for the popularity list
    //If the library has 1500 books and you reduce the capacity to 1000, when the 1001st book is borrowed, it cannot be added to the popularity list because there is no more space.

    public UndoManager undoManager = new UndoManager();
    public UserManager userManager = new UserManager(this, undoManager);
    
    private static final String FILE_NAME = "library_books.txt"; // File to save books
    

    Scanner input = new Scanner(System.in);
    public LibraryManager() {
        loadBooksFromFile();
    }

    // Load books from file
    private void loadBooksFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String title = parts[0].trim();
                    String author = parts[1].trim();

                    Book book = new Book(title, author);

                    // Check for holder ID
                    if (parts.length >= 3) {
                        String holderId = parts[2].trim();
                        if (!holderId.equals("null") && !holderId.isEmpty()) {
                            book.setAvailable(false);
                            book.setCurrentHolderId(holderId);
                        }
                    }

                    //  Check for Borrow Count
                    if (parts.length >= 4) {
                        try {
                            int count = Integer.parseInt(parts[3].trim());
                            book.setBorrowCount(count);
                        } catch (NumberFormatException e) {
                            // ignore
                        }
                    }

                    titleTree.addByTitle(book);
                    authorTree.addByAuthor(book);

                    //If it's borrowed once, it adds it to the heap.
                    if (book.getBorrowCount() > 0) {
                        popularityHeap.addExisting(book);
                    }
                }
            }
            System.out.println("System: Data loaded successfully.");
        } catch (IOException e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }
    // Main add book logic
    // This method takes parameters directly.
    // It updates the BSTs and saves to the file.
    // Returns true if added successfully, false if duplicate
    // Main add book logic
    // This method takes parameters directly.
    // It updates the BSTs and saves to the file.
    // Returns true if added successfully, false if duplicate
    public boolean addBook(String title, String author) {
        String cleanTitle = title.trim();
        String cleanAuthor = author.trim();

        if (cleanTitle.isEmpty() || cleanAuthor.isEmpty()) {
            System.out.println("Error: Title or Author cannot be empty.");
            return false;
        }

        // Since we want unique titles, we search the titleTree first.
        if (titleTree.search(cleanTitle) != null) {
            System.out.println("Error: A book with the title '" + cleanTitle + "' already exists.");
            return false;
        }

        // Create Object
        Book book = new Book(cleanTitle, cleanAuthor);

        // Add to Memory (BSTs)
        titleTree.addByTitle(book);
        authorTree.addByAuthor(book);
        popularityHeap.addExisting(book);

        // Save to File
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(cleanTitle + "," + cleanAuthor);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error saving to file: " + e.getMessage());
            return false;
        }

        undoManager.addAction(new undoAddBook(this, book));
        System.out.println("Success: Book added -> " + cleanTitle);
        return true;
    }
    
    public boolean addBookForUndo(String title, String author){
        String cleanTitle = title.trim().toLowerCase();
        String cleanAuthor = author.trim().toLowerCase();

        // 1. Check if book already exists
        // NOTE: Make sure you added the 'search' method to titleBst as discussed before.
        if (titleTree.search(cleanTitle) != null) {
            System.out.println("Error: Book '" + cleanTitle + "' already exists.");
            return false; // Duplicate found, do not add
        }

        // 2. Create Object
        Book book = new Book(cleanTitle, cleanAuthor);

        // 3. Add to Memory (BSTs)
        titleTree.addByTitle(book);
        authorTree.addByAuthor(book);

        // 4. Save to File (Appends to the end of txt)
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(cleanTitle + "," + cleanAuthor);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error saving to file: " + e.getMessage());
            return false;
        }

        return true;
    }

    //   RemoveBook from tree
    //  REMOVE METHOD (Reads all lines, skips the one to delete, rewrites file)
    // Method to remove a book from the file and memory
    public boolean removeBookFromFile(String titleInput) {
        String titleToRemove = titleInput.trim().toLowerCase();
        ArrayList<String> bookList = new ArrayList<>();
        boolean isFound = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String currentTitle = parts[0].trim();
                // Check if this is the book we want to remove
                if (currentTitle.toLowerCase().equals(titleToRemove.toLowerCase())) {
                    isFound = true;

                    // Retrieve the book object from memory before deleting it
                    Book bookToRemove = titleTree.search(currentTitle);

                    if (bookToRemove != null) {
                        // Remove from Popularity Heap to keep statistics accurate
                        popularityHeap.remove(bookToRemove);

                        // Add this action to the Undo Manager
                        undoManager.addAction(new undoRemoveBook(this, bookToRemove));
                    }

                    // Remove from Memory/BSTs
                    titleTree.delete(currentTitle);
                    if (parts.length > 1) {
                        authorTree.delete(parts[1], currentTitle);
                    }
                }
                else {
                    bookList.add(line); // Keep this book
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file.");
            return false;
        }

        // Rewrite the file if the book was found and removed
        if (isFound) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
                for (String bookLine : bookList) {
                    writer.write(bookLine);
                    writer.newLine();
                }
                return true;
            } catch (IOException e) {
                System.out.println("Error writing file.");
            }
        }
        return false;
    }

    //  SearchBook from tree
    public Book searchBook(String title) {
        // Search in the BST
        return titleTree.search(title.trim());
    }
    public String searchByAuthor(String author) {
        return authorTree.searchBooks(author.trim());
    }

    void removeBook(Book book){
        if (book == null){
            return;
        }
        // Remove from Popularity Heap
        popularityHeap.remove(book);

        // Remove from Binary Search Trees
        titleTree.delete(book.getTitle().toLowerCase());
        authorTree.delete(book.getAuthor(), book.getTitle().toLowerCase());
    }
    
    // Inorder showing (sorted alphabetic) the books
    public String showLibrary(){
         return titleTree.showAlphabetic();
    }


    /* - Logic:
     - 1. Check if book exists.
     - 2. If available > Assign to user, set unavailable.
     - 3. If unavailable > Add user to waitlist.

     */
    public String borrowBook(String title, String userId) {
        Book book = titleTree.search(title.trim());

        if (book == null) {
            return "Error: Book not found.";
        }

        if (book.isAvailable()) {
            // Success: User takes the book
            book.setAvailable(false);
            book.setCurrentHolderId(userId);

            popularityHeap.incrementPopularity(book);

            // Add to User's history
            User user = userManager.getUser(userId);
            if(user != null) {
                // Date and time logic
                // Get current date and time
                LocalDateTime now = LocalDateTime.now();
                // Format the date (e.g., 2025-10-29 14:30)
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                String formattedDate = now.format(formatter);
                // Create a combined string: Title + Date
                String historyEntry = book.getTitle() + " [Borrowed: " + formattedDate + "]";
                // Add this combined string to history
                user.addToHistory(historyEntry);
            }
            updateLibraryFile();
            userManager.rewriteUserFile();

            return "Success: You have borrowed '" + book.getTitle() + "'.";
        } else {
            // Rest of the else block remains the same
            // First check if user is already holding it
            if (userId.equals(book.getCurrentHolderId())) {
                return "Error: You already have this book!";
            }

            sllNode newNode = new sllNode(userId);

            if (book.waitList.rear != null) {
                book.waitList.rear.next = newNode;
            }
            book.waitList.rear = newNode;
            if (book.waitList.front == null) {
                book.waitList.front = newNode;
            }

            return "Book is currently unavailable. You have been added to the waitlist. Position: " + book.waitList.size();
        }
    }

    /*
     - Return a book.
     - Logic:
     - 1. Check waitlist.
     - 2. If waitlist has people -> Assign to next person.
     - 3. If empty -> Make book available.
     */
    public String returnBook(String title, String userId) {
        Book book = titleTree.search(title.trim());

        if (book == null) return "Error: Book not found.";

        // Check if this user actually has the book
        if (!userId.equals(book.getCurrentHolderId())) {
            return "Error: You do not have this book checked out.";
        }

        // Log the return for the current user
        User returningUser = userManager.getUser(userId);
        if (returningUser != null) {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            String formattedDate = now.format(formatter);

            // Add "Returned" entry to history
            returningUser.addToHistory(book.getTitle() + " [Returned: " + formattedDate + "]");
        }
        // ------------------------------------------------

        // Logic: Check Waitlist
        if (book.waitList.size() > 0) {
            // Dequeue logic
            sllNode nextNode = book.waitList.front;
            String nextUserId = (String) nextNode.data;

            // Move front pointer
            book.waitList.front = book.waitList.front.next;
            if (book.waitList.front == null) {
                book.waitList.rear = null;
            }

            // Assign book to next user
            book.setCurrentHolderId(nextUserId);
            book.setAvailable(false); // Still unavailable

            //  LOG BORROW FOR THE NEXT USER (FROM WAITLIST)
            User nextUser = userManager.getUser(nextUserId);
            if (nextUser != null) {
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                String formattedDate = now.format(formatter);

                // Add "Borrowed" entry for the new owner
                nextUser.addToHistory(book.getTitle() + " [Borrowed: " + formattedDate + "]");
            }
            // --------------------------------------------------------

            updateLibraryFile(); // Save books
            userManager.rewriteUserFile(); // Save users

            return "Book returned. It has been automatically assigned to the next user in waitlist (ID: " + nextUserId + ").";
        } else {
            // No one waiting, book goes to shelf
            book.setAvailable(true);
            book.setCurrentHolderId(null);

            updateLibraryFile(); // Save books
            userManager.rewriteUserFile(); // Save users

            return "Success: Book returned to the library.";
        }
    }
    public void updateLibraryFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            // We need to traverse the tree and write all books.
            // Calling a helper method from titleTree to write to this writer.
            titleTree.saveTreeToFile(writer);
        } catch (IOException e) {
            System.out.println("Error updating file: " + e.getMessage());
        }
    }
    public String showLibraryAlphabetic(){
        return titleTree.showAlphabetic();
    }

    // Returns the list sorted by popularity (Most borrowed first)
    public String showLibraryPopularity(){
        return popularityHeap.getSortedList();
    }

    
}