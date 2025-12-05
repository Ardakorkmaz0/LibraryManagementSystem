package librarymanagementsystem;
import java.io.*;
class sllNode { // single linked list nodes
    sllNode next;
    Object data;
    public sllNode(Object data){
        this.data = data;
        this.next = null;
    }
}
class sll { // single linked list operations
    sllNode head;
    public sll(){
        head = null;
    }
}
// Requirement: Use for high-speed retrieval of unique records (e.g., ID lookups)
class HashNode {
    String key;   // Unique ID (Book ID or User ID)
    Object value; // Stored Object (Book or User)
    HashNode next; // Pointer for handling collisions (Chaining)
    public HashNode(String key, Object value) {
        this.key = key;
        this.value = value;
        this.next = null;
    }
}
class HashTable { // Hash Table operations with Chaining
    private HashNode[] buckets;
    private int capacity; // Size of the array
    private int size;
    public HashTable(int capacity) {
        this.capacity = capacity;
        this.buckets = new HashNode[capacity];
        this.size = 0;
    }
    // Hash function to convert key string to index
    private int getBucketIndex(String key) {
        int hashCode = key.hashCode();
        int index = hashCode % capacity;
        // Ensure index is positive
        return index < 0 ? index * -1 : index;
    }
    // Insert key-value pair (O(1) average case)
    public void put(String key, Object value) {
        int index = getBucketIndex(key);
        HashNode head = buckets[index];

        // Check if key already exists, update value if so
        while (head != null) {
            if (head.key.equals(key)) {
                head.value = value;
                return;
            }
            head = head.next;
        }
        // Insert new node at the beginning of the chain (Collision handling)
        size++;
        head = buckets[index];
        HashNode newNode = new HashNode(key, value);
        newNode.next = head;
        buckets[index] = newNode;
    }
    // Retrieve value by key average case
    public Object get(String key) {
        int index = getBucketIndex(key);
        HashNode head = buckets[index];

        // Traverse the chain to find the key
        while (head != null) {
            if (head.key.equals(key)) {
                return head.value;
            }
            head = head.next;
        }
        return null; // Key not found
    }
    // Remove value by key
    public Object remove(String key) {
        int index = getBucketIndex(key);
        HashNode head = buckets[index];
        HashNode prev = null;

        while (head != null) {
            if (head.key.equals(key)) {
                break;
            }
            prev = head;
            head = head.next;
        }
        if (head == null) return null; // Key not found

        size--;
        if (prev != null) {
            prev.next = head.next;
        } else {
            buckets[index] = head.next;
        }
        return head.value;
    }
}

class bstNode { // binary search tree nodes
    Object data;
    bstNode left;
    bstNode right;
    public bstNode(Object data){
        this.data = data;
        this.left = null;
        this.right = null;
    }
}

class bst { // binary search tree operations
    bstNode root;
    public bst(){
        root = null;
    }
}

// NOTE: idBst class removed because Hash Table is required for ID lookups.
class nameBst extends bst {
    void addByName(User user){
        root = insertUserName(root, user);
        // in here, push a stack of reverse this method for undo
    }
    private bstNode insertUserName(bstNode node, User user){
        if(node == null) {
            return new bstNode(user);
        }
        else{
            User current = (User) node.data;
            if(user.getName().compareToIgnoreCase(current.getName()) < 0) {
                node.left = insertUserName(node.left, user);
            }
            else{
                node.right = insertUserName(node.right, user);
            }
        }
        return node;
    }
}
class authorBst extends bst {
    
    void addByAuthor(Book book){
        root = insertBookAuthor(root, book);
    }
    private bstNode insertBookAuthor(bstNode node, Book book){
        if(node == null) {
            return new bstNode(book);
        }
        else{
            Book current = (Book) node.data;
            if(book.getAuthor().compareToIgnoreCase(current.getAuthor()) < 0) {
                node.left = insertBookAuthor(node.left, book);
            }
            else{
                node.right = insertBookAuthor(node.right, book);
            }
        }
        return node;
    }

    // --- Delete method for Author Tree ---
    // Needed by LibraryManager to remove book by Author
    public void delete(String author, String title) {
        root = deleteRec(root, author, title);
    }

    private bstNode deleteRec(bstNode root, String author, String title) {
        if (root == null) return root;

        Book current = (Book) root.data;

        // Navigate the tree based on Author name
        if (author.compareToIgnoreCase(current.getAuthor()) < 0) {
            root.left = deleteRec(root.left, author, title);
        } else if (author.compareToIgnoreCase(current.getAuthor()) > 0) {
            root.right = deleteRec(root.right, author, title);
        } else {
            // Author matches. Now check if the Title also matches.
            // (Since multiple books can have the same author)
            if (!current.getTitle().equalsIgnoreCase(title)) {
                // If author matches but title doesn't, we look into the right subtree
                // (because duplicates or equal keys were inserted to the right)
                root.right = deleteRec(root.right, author, title);
                return root;
            }
            // Node found. Perform deletion.
            // Case 1: No children or one child
            if (root.left == null) return root.right;
            else if (root.right == null) return root.left;

            // Case 2: Two children
            // Get the smallest value in the right subtree
            root.data = minValue(root.right);
            Book minBook = (Book) root.data;
            // Delete that smallest value from the right subtree
            root.right = deleteRec(root.right, minBook.getAuthor(), minBook.getTitle());
        }
        return root;
    }
    // Helper to find minimum value node
    private Object minValue(bstNode root) {
        Object minv = root.data;
        while (root.left != null) {
            minv = root.left.data;
            root = root.left;
        }
        return minv;
    }
    
    // Returns a formatted string of all books found for that author
    public String searchBooks(String author) {
        StringBuilder result = new StringBuilder();
        searchRec(root, author, result);

        if (result.length() == 0) return null; // No books found
        return result.toString();
    }
    
    private void searchRec(bstNode root, String author, StringBuilder sb) {
        if (root == null) return;

        Book current = (Book) root.data;
        int cmp = author.compareToIgnoreCase(current.getAuthor());

        if (cmp < 0) {
            // Search Author is smaller, go left
            searchRec(root.left, author, sb);
        } else if (cmp > 0) {
            // Search Author is larger, go right
            searchRec(root.right, author, sb);
        } else {
            // Match Found! Add to result list
            sb.append(" - ").append(current.getTitle()).append("\n");

            // Show Status
            if(current.isAvailable()){
                sb.append(" [AVAILABLE]");
            } else {
                sb.append(" [BORROWED BY: ").append(current.getCurrentHolderId()).append("]");
            }
            sb.append("\n");

            // Continue searching right subtree because duplicates (same author)
            // are inserted to the right in our add logic.
            searchRec(root.right, author, sb);
        }
    }
}




class titleBst extends bst {



    void addByTitle(Book book){
        root = insertBookTitle(root, book);
        // in here, push a stack of reverse this method for undo
    }
    private bstNode insertBookTitle(bstNode node, Book book){
        if(node == null) {
            return new bstNode(book);
        }
        else{
            Book current = (Book) node.data;
            if(book.getTitle().compareToIgnoreCase(current.getTitle()) < 0) {
                node.left = insertBookTitle(node.left, book);
            }
            else{
                node.right = insertBookTitle(node.right, book);
            }
        }
        return node;
    }




    // --- Delete method for Title Tree ---
    // Needed by LibraryManager to remove book by Title
    public void delete(String title) {
        root = deleteRec(root, title);
    }
    private bstNode deleteRec(bstNode root, String title) {
        if (root == null) return root;
        Book current = (Book) root.data;
        if (title.compareToIgnoreCase(current.getTitle()) < 0) {
            root.left = deleteRec(root.left, title);
        } else if (title.compareToIgnoreCase(current.getTitle()) > 0) {
            root.right = deleteRec(root.right, title);
        } else {
            // Node found. Perform deletion.
            // Case 1: No children or one child
            if (root.left == null) return root.right;
            else if (root.right == null) return root.left;

            // Case 2: Two children
            // Get the smallest value in the right subtree
            root.data = minValue(root.right);
            Book minBook = (Book) root.data;
            // Delete that smallest value from the right subtree
            root.right = deleteRec(root.right, minBook.getTitle());
        }
        return root;
    }
    // Helper to find minimum value node
    private Object minValue(bstNode root) {
        Object minv = root.data;
        while (root.left != null) {
            minv = root.left.data;
            root = root.left;
        }
        return minv;
    }




    // --- Search Method ---
    public Book search(String title) {
        return searchRec(root, title);
    }
    private Book searchRec(bstNode root, String title) {
        // Base case: root is null or key is present at root
        if (root == null) {
            return null;
        }

        Book current = (Book) root.data;

        // Compare title with root's title
        if (current.getTitle().equalsIgnoreCase(title)) {
            return current;
        }

        // Key is smaller than root's key
        if (title.compareToIgnoreCase(current.getTitle()) < 0) {
            return searchRec(root.left, title);
        }

        // Key is larger than root's key
        return searchRec(root.right, title);
    }

    // Returns a formatted string of all books sorted alphabetically
    public String showAlphabetic(){
        if(root == null){
            return "The library is empty.";
        }
        StringBuilder sb = new StringBuilder();
        inOrder(root, sb);
        return sb.toString(); // Return the constructed string to UI
    }

    private void inOrder(bstNode node, StringBuilder sb){
        if (node == null) return;

        // Traverse left subtree first
        inOrder(node.left, sb);

        // Process current node
        Book book = (Book) node.data;

        sb.append(book.getTitle()).append(" - ").append(book.getAuthor());
        // Add information based on book status
        if(book.isAvailable()) {
            sb.append(" [AVAILABLE]");
        } else {
            // Show who borrowed it
            sb.append(" [BORROWED BY ID: ").append(book.getCurrentHolderId()).append("]");
        }
        sb.append("\n");
        // Traverse right subtree
        inOrder(node.right, sb);
    }
    public void saveTreeToFile(BufferedWriter writer) throws IOException {
        writeNode(root, writer);
    }

    private void writeNode(bstNode node, BufferedWriter writer) throws IOException {
        if (node == null) return;

        // Traverse inorder to write sorted
        writeNode(node.left, writer);

        Book book = (Book) node.data;
        // Format: Title,Author,HolderID
        String holder = (book.getCurrentHolderId() == null) ? "null" : book.getCurrentHolderId();
        writer.write(book.getTitle() + "," + book.getAuthor() + "," + holder);
        writer.newLine();

        writeNode(node.right, writer);
    }


    
}

class queue { // queue operations

    sllNode front;
    sllNode rear;

    public queue(){
        front = null;
        rear = null;
    }
    
    int size(){
        if(front == null && rear == null){
            return 0;
        }
        sllNode temp = front;
        int size = 1;
        while(!temp.equals(rear)){
            size++;
            temp = temp.next;
            }
        return size;
    }
    
}
class stack {
    sllNode top;
    public stack(){
        top = null;
    }
    void push(UndoAction action) {
        sllNode node = new sllNode(action);
        if(top == null){
            top = node;
            return;
        }
        node.next = top;
        top = node;
    }
    int size(){
        if(top == null){
            return 0;
        }
        sllNode curr = top;
        int size = 1;
        while(curr.next != null){
            curr = curr.next;
            size++;
        }
        return size;
    }
    
    void deleteFifth(){
        sllNode curr = top;
        for(int i = 1; i < 4; i++){
            curr = curr.next;
        }
        curr.next = null;
    }
    UndoAction pop() {
        if (top == null){
            return null;
        }
        UndoAction action = (UndoAction) top.data;
        top = top.next;
        return action;
    }

    
    UndoAction peek() {
        if(top == null){
            System.out.println("There is no last action.");
            return null;
        }
        UndoAction action = (UndoAction) top.data;
        return action;
    }

    
}