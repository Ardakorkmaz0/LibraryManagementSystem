package librarymanagementsystem;

import java.util.Scanner;

public class LibraryManager {
    
    private titleBst titleTree = new titleBst();
    private authorBst authorTree = new authorBst();
    
    Scanner input = new Scanner(System.in);
    
    void addBook(){ // adding book to library
        System.out.println("Adding Book System");
        System.out.println("---------------------------------");
        String title;
        System.out.println("Enter book's title : ");
        while(true){
            title = input.nextLine().trim().toLowerCase();
            if(!title.isEmpty()){
                break;
            }
            else {
                System.out.println("The title is invalid. Try again : ");
            }
        }
        String author;
        System.out.println("Enter book's author : ");
        while(true){
            author = input.nextLine().trim().toLowerCase();
            if(author.length() > 1){
                break;
            }
            else{
                System.out.println("The author name is invalid. Try again : ");
            }
        }
        
        Book book = new Book(title, author);
        titleTree.addByTitle(book);
        authorTree.addByAuthor(book);
    }

    public void addBookGUI(String title, String author) {   // this method is for the GUI, it takes parameters directly
        //trimming the spaces and making it lowercase to avoid errors
        String cleanTitle = title.trim().toLowerCase();
        String cleanAuthor = author.trim().toLowerCase();
        // creating a new book object with the given inputs
        Book book = new Book(cleanTitle, cleanAuthor);
        // adding the new book to both title and author BSTs
        titleTree.addByTitle(book);
        authorTree.addByAuthor(book);
        // printing to console just to check if it works
        System.out.println("Book added successfully: " + cleanTitle);
    }
    
    
}