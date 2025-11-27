package librarymanagementsystem;

public class Book {
    
    private String title;
    private String author;
    private String id;
    queue waitList = new queue();
    
    public Book(){ // default constructor
        this.title = null;
        this.author = null;
        this.id = null;
    }
    
    public Book(String title, String author){
        this.title = title;
        this.author = author;
        this.id = Integer.toString(230316058 / (Integer.valueOf(title.charAt(0))) + Integer.valueOf(author.charAt(1)) + Integer.valueOf(author.charAt(0)) + (int)(Math.random() * 10));
    }
    
    public String getTitle(){
        return this.title;
    }
    public String getAuthor(){
        return this.author;
    }
    
}
