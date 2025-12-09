package librarymanagementsystem;

import java.io.*;
import java.util.Scanner;

public class UserManager {

    // Hash Table for looking up Members by ID
    private HashTable userTable;

    // BST for managing members by name
    private nameBst userTree;

    private UndoManager undoManager;
    private LibraryManager lib;
    
    private User activeUser;
    private static final String USER_FILE = "library_users.txt"; // File to store users

    public UserManager(LibraryManager lib, UndoManager undoManager) {
        this.lib = lib;
        this.undoManager = undoManager;
        userTable = new HashTable(100);
        userTree = new nameBst();
        activeUser = null;
        loadUsersFromFile(); // Load data when system starts
    }

    // Register User Method
    public User registerUser(String name, String surname, int age) {
        User newUser = new User(name, surname, age);

        // Add to memory structures (Hash Table & BST)
        userTable.put(newUser.getId(), newUser);
        userTree.addByName(newUser);

        // Save to file immediately
        saveUserToFile(newUser);
        undoManager.addAction(new undoRegisterNewUser(lib, newUser));
        System.out.println("User Registered: " + newUser.getName() + " (ID: " + newUser.getId() + ")");
        return newUser;
    }

    // File Operation: Save Single User
    private void saveUserToFile(User user) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_FILE, true))) {
            // Format: Name,Surname,Age,ID
            writer.write(user.getName() + "," + user.getSurname() + "," + user.getAge() + "," + user.getId());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error saving user: " + e.getMessage());
        }
    }

    // File Operation: Load All Users
    private void loadUsersFromFile() {
        File file = new File(USER_FILE);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    String name = parts[0].trim();
                    String surname = parts[1].trim();
                    int age = Integer.parseInt(parts[2].trim());
                    String id = parts[3].trim();

                    User user = new User(name, surname, age, id);

                    // Load History if exists
                    if (parts.length > 4) {
                        String historyRaw = parts[4];
                        // Split by '%' to get individual books
                        String[] books = historyRaw.split("%");
                        for (String b : books) {
                            if (!b.trim().isEmpty()) {
                                user.addToHistory(b);
                            }
                        }
                    }

                    userTable.put(id, user);
                    userTree.addByName(user);
                }
            }
            System.out.println("System: Users loaded successfully.");
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error loading users: " + e.getMessage());
        }
    }

    //  Login Method
    public boolean login(String id) {
        // Retrieve user from Hash Table using ID
        User user = (User) userTable.get(id);

        if (user != null) {
            this.activeUser = user;
            System.out.println("Login Successful! Welcome, " + user.getName());
            return true;
        } else {
            System.out.println("Login Failed: User ID not found.");
            return false;
        }
    }

    public void logout() {
        if (activeUser != null) {
            System.out.println("Goodbye, " + activeUser.getName());
            activeUser = null;
        }
    }

    //  Getters

    public User getActiveUser() {
        return activeUser;
    }

    // Required for LibraryManager to access User object by ID
    public User getUser(String id) {
        return (User) userTable.get(id);
    }


    //  Delete User Method
    public boolean deleteUser(String id) {
        // Check if user exists in Hash Table (memory)
        if(userTable.get(id) == null) {
            return false;
        }

        // Safety: If the active user is the one being deleted, logout first
        if (activeUser != null && activeUser.getId().equals(id)) {
            activeUser = null;
        }

        // Remove from Hash Table
        userTable.remove(id);
        // ---------------------------------------------------------------------------
        // NOT COMPLETED !!! undoManager.addAction(new undoRemoveUser(lib, id));     |
        // ---------------------------------------------------------------------------
        // It will be gone from login/memory (Hash Table) which is the critical part.
        return removeUserFromFile(id);
    }

    // Remove User from File
    private boolean removeUserFromFile(String idToRemove) {
        File inputFile = new File(USER_FILE);
        File tempFile = new File("user_temp.txt");
        boolean isFound = false;

        try(BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))){

            String line;
            while((line = reader.readLine()) != null){
                String[] parts = line.split(",");
                if(parts.length == 4) {
                    String currentId = parts[3].trim();
                    if(currentId.equals(idToRemove)) {
                        isFound = true;
                        continue; // Skip writing this line (delete logic)
                    }
                }
                writer.write(line);
                writer.newLine();
            }
        }
        catch (IOException e) {
            return false;
        }

        // Delete original file and rename temp file
        if(inputFile.delete()) {
            tempFile.renameTo(inputFile);
        }
        return isFound;
    }
    public String searchByName(String name) {
        return userTree.searchUserByName(name.trim());
    }

    // Search by ID (using Hash Table)
    // We reuse the existing getUser() but format it for display
    public String searchByIdFormatted(String id) {
        User user = (User) userTable.get(id);
        if (user != null) {
            return "User Found:\n" +
                    "Name: " + user.getName() + " " + user.getSurname() + "\n" +
                    "ID: " + user.getId() + "\n" +
                    "Age: " + user.getAge() + "\n" +
                    "Book History:\n" +
                    user.getHistoryString(); // Calling the new helper method
        }
        return null;
    }
    public void rewriteUserFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_FILE))) {
            // Use the BST method
            userTree.saveUsersToWriter(writer);
        } catch (IOException e) {
            System.out.println("Error updating user file: " + e.getMessage());
        }
    }
}