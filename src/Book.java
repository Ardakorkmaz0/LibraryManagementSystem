package librarymanagementsystem;

public class Book {

    private String title;
    private String author;
    private String id;
    private int borrowCount;

    // New fields for Circulation
    private boolean isAvailable;
    private String currentHolderId; // ID of the user who has the book

    // Existing waitlist (using your custom queue class)
    queue waitList = new queue();
    public Book(){
        this.title = null;
        this.author = null;
        this.id = null;
        this.isAvailable = true;
        this.borrowCount = 0;
    }

    public Book(String title, String author){
        this.title = title;
        this.author = author;
        // Generate ID
        this.id = title.substring(0, 2) + author.substring(0, 2) + 
        (Integer.toString(230316058 / Integer.parseInt(Long.toString(System.currentTimeMillis()).substring(9))).substring(1, 5));
        this.borrowCount = 0;
        // Default state: Available
        this.isAvailable = true;
        this.currentHolderId = null;
    }

    public String getTitle(){
        return this.title;
    }
    public String getAuthor(){
        return this.author;
    }

    public int getBorrowCount(){
        return borrowCount;
    }
    public void setBorrowCount(int borrowCount){
        this.borrowCount = borrowCount;
    }
    public void incrementBorrowCount(){
        this.borrowCount++;
    }

    //  Getters and Setters for Circulation
    public boolean isAvailable() {
        return isAvailable;
    }
    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public String getCurrentHolderId() {
        return currentHolderId;
    }
    public void setCurrentHolderId(String currentHolderId) {
        this.currentHolderId = currentHolderId;
    }
}