package librarymanagementsystem;

public class User {

    private String name;
    private String surname;
    private String id;
    private int age;
    sll history = new sll(); // Linked List for borrow history

    private static int counter = 0;

    public User(){ // Default constructor
        this.name = null;
        this.surname = null;
        this.age = 0;
        this.id = null;
    }

    //  CONSTRUCTOR FOR NEW REGISTRATIONS (Generates ID)
    public User(String name, String surname, int age){
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.id = defId(); // Auto-generate ID
    }

    //  CONSTRUCTOR FOR LOADING FROM FILE (Uses existing ID)
    public User(String name, String surname, int age, String id){
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.id = id; // Use stored ID
    }

    // Unique ID Generation Logic (Student ID Integration)
    private String defId(){
        counter++;
        if (name == null || surname == null){
            return "UNKNOWN" + counter;
        }

        return Integer.toString(230316024 / Integer.valueOf(this.name.charAt(0)) + Integer.valueOf(this.surname.charAt(0)) + counter);
    }

    // GETTERS
    public String getName(){
        return this.name;
    }

    public String getSurname(){
        return this.surname;
    }

    public int getAge(){
        return this.age;
    }

    public String getId(){
        return this.id;
    }

    // Called by LibraryManager when a user borrows a book
    public void addToHistory(String bookTitle) {
        sllNode newNode = new sllNode(bookTitle);

        // Insert at the end of the list
        if (history.head == null) {
            history.head = newNode;
        } else {
            // Find the end of the list
            sllNode current = history.head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
    }

    // Returns the borrow history formatted for display
    // Used by Structures.java and UserManager.java for search results
    public String getHistoryString() {
        StringBuilder sb = new StringBuilder();

        if (history.head == null) {
            sb.append(" - No book records.\n");
        } else {
            sllNode current = history.head;
            while (current != null) {
                sb.append(" -> ").append(current.data).append("\n");
                current = current.next;
            }
        }
        return sb.toString();
    }
}