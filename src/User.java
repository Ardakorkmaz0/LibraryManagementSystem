package librarymanagementsystem;

public class User {
    
    private String name;
    private String surname;
    private String id;
    private int age;
    sll history = new sll();
    
    private static int counter = 0;
    
    public User(){ // default constructor
        this.name = null;
        this.surname = null;
        this.age = 0;
    }
    public User(String name, String surname, int age){ // parameter passing
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.id = defId();
    }
    
    private String defId(){
        counter++;
        return Integer.toString(230316024 / Integer.valueOf(this.name.charAt(0)) + Integer.valueOf(this.surname.charAt(0)) + counter);
    }
    
    public String getName(){
        return this.name;
    }
    public String getId(){
        return this.id;
    }
}
