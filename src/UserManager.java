package librarymanagementsystem;

import java.io.*;
import java.util.Scanner;

public class UserManager {

    // Hash Table for looking up Members by ID
    private HashTable userTable;

    // BST for managing members by name
    private nameBst userTree;

    private User activeUser;
    private static final String USER_FILE = "library_users.txt"; // File to store users

    public UserManager() {
        userTable = new HashTable(100);
        userTree = new nameBst();
        activeUser = null;
        loadUsersFromFile(); // Load data when system starts
    }

    // -Register User Method
    public User registerUser(String name, String surname, int age) {
        User newUser = new User(name, surname, age);

        // Add to memory structures
        userTable.put(newUser.getId(), newUser);
        userTree.addByName(newUser);

        // Save to file immediately
        saveUserToFile(newUser);

        System.out.println("User Registered: " + newUser.getName() + " (ID: " + newUser.getId() + ")");
        return newUser;
    }

    // -File Operation: Save Single User
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
        if (!file.exists()) return; // No users yet

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String name = parts[0].trim();
                    String surname = parts[1].trim();
                    int age = Integer.parseInt(parts[2].trim());
                    String id = parts[3].trim();

                    // Re-create user using the specific constructor
                    User user = new User(name, surname, age, id);

                    // Add to memory (Hash Table & BST)
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

    public User getActiveUser() {
        return activeUser;
    }


    public void handleConsoleRegistration() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Name: "); String n = sc.nextLine();
        System.out.print("Surname: "); String s = sc.nextLine();
        System.out.print("Age: "); int a = sc.nextInt();
        registerUser(n, s, a);
    }

    public void handleConsoleLogin() {
        Scanner sc = new Scanner(System.in);
        System.out.print("User ID: "); String id = sc.nextLine();
        login(id);
    }
    // Method: Delete User (Returns true if succesful)
    public boolean deleteUser(String id) {
        // Check if user exist in Hash Table(memory)
        if(userTable.get(id) == null) {
            return false;
        }
        // Remove from Hash Table
        userTable.remove(id);

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
                        continue;
                    }
                }
                writer.write(line);
                writer.newLine();
            }
        }
        catch (IOException e) {
            return false;
        }
        if(inputFile.delete()) {
            tempFile.renameTo(inputFile);
        }
        return isFound;
    }
}