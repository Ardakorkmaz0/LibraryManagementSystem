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
        this.id = title.substring(0, 2) + author.substring(0, 2) + (Integer.toString(230316058 / Integer.parseInt(Long.toString(System.currentTimeMillis()).substring(9))).substring(1, 5));
    }   
    
    public String getTitle(){
        return this.title;
    }
    public String getAuthor(){
        return this.author;
    }
    
}