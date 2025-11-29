package librarymanagementsystem;

import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;

public class LibraryManager {

    private titleBst titleTree = new titleBst();
    private authorBst authorTree = new authorBst();
    private static final String FILE_NAME = "library_books.txt"; // File to save books

    Scanner input = new Scanner(System.in);

    // --- METHOD 1: CONSOLE INTERFACE ---
    // This method handles the console input and then calls the main logic below
    void addBook(){
        System.out.println("Adding Book System");
        System.out.println("---------------------------------");

        String title;
        System.out.println("Enter book's title : ");
        while(true){
            title = input.nextLine().trim().toLowerCase();
            if(!title.isEmpty()) break;
            else System.out.println("The title is invalid. Try again : ");
        }

        String author;
        System.out.println("Enter book's author : ");
        while(true){
            author = input.nextLine().trim().toLowerCase();
            if(author.length() > 1) break;
            else System.out.println("The author name is invalid. Try again : ");
        }

        // REUSE: Call the main logic method (Method 2)
        addBook(title, author);
    }

    // --- METHOD 2: CORE LOGIC (USED BY GUI AND CONSOLE) ---
    // This method takes parameters directly.
    // It updates the BSTs and saves to the file.
    // Returns true if added successfully, false if duplicate
    public boolean addBook(String title, String author) {
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

        System.out.println("Success: Book added -> " + cleanTitle);
        return true;
    }

    //  --- METHOD 3 : RemoveBook from tree
    // --- REMOVE METHOD (Reads all lines, skips the one to delete, rewrites file) ---
    public boolean removeBookFromFile(String titleInput) {
        String titleToRemove = titleInput.trim().toLowerCase();
        ArrayList<String> bookList = new ArrayList<>();
        boolean isFound = false;

        // Step A: Read file and filter
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String currentTitle = parts[0].trim();

                if (currentTitle.equals(titleToRemove)) {
                    isFound = true;
                    // Also remove from Memory/BSTs immediately
                    titleTree.delete(currentTitle);
                    if (parts.length > 1) authorTree.delete(parts[1], currentTitle);
                } else {
                    bookList.add(line); // Keep this book
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file.");
            return false;
        }

        // Step B: Rewrite file if book was found
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

    // --- METHOD 3:  SearchBook from tree
    public Book searchBook(String title) {
        // Search in the BST
        return titleTree.search(title.trim());
    }
}