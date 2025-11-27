package librarymanagementsystem;

import java.util.Scanner;

public class LibraryManager {
    
    private titleBst titleTree = new titleBst();
    private authorBst authorTree = new authorBst();
    
    void addBook(){ // adding book to library
        System.out.println("Adding Book System");
        System.out.println("---------------------------------");
        Scanner input = new Scanner(System.in);
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
    
}