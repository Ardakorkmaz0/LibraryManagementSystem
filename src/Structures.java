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
    // INSERTION LOGIC (for Name + Surname)
    void addByName(User user){
        root = insertUserName(root, user);
    }

    private bstNode insertUserName(bstNode node, User user){
        if(node == null) {
            return new bstNode(user);
        }
        else{
            User current = (User) node.data;

            // Combine Name + Surname for comparison
            String userFull = user.getName() + " " + user.getSurname();
            String currentFull = current.getName() + " " + current.getSurname();

            // Compare full names alphabetically
            if(userFull.compareToIgnoreCase(currentFull) < 0) {
                node.left = insertUserName(node.left, user);
            }
            else{
                node.right = insertUserName(node.right, user);
            }
        }
        return node;
    }

    //  SEARCH LOGIC (Updated for Name + Surname & History)
    public String searchUserByName(String fullNameInput) {
        StringBuilder result = new StringBuilder();
        // Trim input when searching
        searchRec(root, fullNameInput.trim(), result);

        if (result.length() == 0) return null; // No users found
        return result.toString();
    }

    private void searchRec(bstNode root, String searchInput, StringBuilder sb) {
        if (root == null) return;

        User current = (User) root.data;

        // Full name of the current user
        String currentFull = current.getName() + " " + current.getSurname();

        // Compare search input with current full name
        int cmp = searchInput.compareToIgnoreCase(currentFull);

        if (cmp < 0) {
            // Go left
            searchRec(root.left, searchInput, sb);
        } else if (cmp > 0) {
            // Go right
            searchRec(root.right, searchInput, sb);
        } else {
            // --- MATCH FOUND ---
            sb.append("User Found:\n");
            sb.append("Full Name: ").append(current.getName()).append(" ").append(current.getSurname()).append("\n");
            sb.append("ID: ").append(current.getId()).append("\n");
            sb.append("Age: ").append(current.getAge()).append("\n");

            // Show History
            sb.append("Book History:\n");
            sb.append(current.getHistoryString());
            sb.append("-----------------\n");

            // Continue searching right for duplicates
            searchRec(root.right, searchInput, sb);
        }
    }
    // Traverses the tree and writes user data + history to the file
    public void saveUsersToWriter(BufferedWriter writer) throws IOException {
        writeUserNode(root, writer);
    }
    private void writeUserNode(bstNode node, BufferedWriter writer) throws IOException {
        if (node == null) return;
        // Traverse left
        writeUserNode(node.left, writer);
        // Process User
        User user = (User) node.data;
        // Basic Info
        StringBuilder line = new StringBuilder();
        line.append(user.getName()).append(",")
                .append(user.getSurname()).append(",")
                .append(user.getAge()).append(",")
                .append(user.getId());

        // Append History (separated by %)
        sllNode temp = user.history.head;
        if (temp != null) {
            line.append(","); // Add a comma before starting history
            while (temp != null) {
                line.append(temp.data).append("%"); // Use % as separator for books
                temp = temp.next;
            }
        }
        writer.write(line.toString());
        writer.newLine();
        // Traverse right
        writeUserNode(node.right, writer);
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

    // Delete method for Author Tree
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
        searchRecForAuthor(root, author, result);

        if (result.length() == 0) return null; // No books found
        return result.toString();
    }

    private void searchRecForAuthor(bstNode root, String author, StringBuilder sb) {
        if(root == null){
            return;
        }
        
        Book current = (Book) root.data;
        
        int cmp = author.compareToIgnoreCase(current.getAuthor());
        
        if (cmp < 0){
            searchRecForAuthor(root.left, author, sb);
        }
        else if (cmp > 0){
            searchRecForAuthor(root.right, author, sb);
        }
        else {
            sb.append("Book Found:\n");
            sb.append("Title: ").append(current.getTitle());
            sb.append(" Author : ").append(current.getAuthor());
            // Check availability status
            if(current.isAvailable()){
                sb.append(" [AVAILABLE]");
            } else {
                sb.append(" [BORROWED BY ID: ").append(current.getCurrentHolderId()).append("]");
            }
            sb.append("\n");
            searchRecForAuthor(root.right, author, sb);
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




    //  Delete method for Title Tree
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
        writer.write(book.getTitle() + "," + book.getAuthor() + "," + holder + "," + book.getBorrowCount());
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
// Keeps track of the "Most Popular Book" using a Max-Heap
class PopularityHeap {
    private Book[] heap;
    private int size;
    private int capacity;

    public PopularityHeap(int capacity) {
        this.capacity = capacity;
        this.size = 0;
        this.heap = new Book[capacity];
    }

    private int parent(int i) { return (i - 1) / 2; }
    private int leftChild(int i) { return (2 * i) + 1; }
    private int rightChild(int i) { return (2 * i) + 2; }

    private void swap(int i, int j) {
        Book temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }

    // Moves the element up to maintain Max-Heap property
    private void heapifyUp(int i) {
        while (i > 0 && heap[parent(i)].getBorrowCount() < heap[i].getBorrowCount()) {
            swap(i, parent(i));
            i = parent(i);
        }
    }

    // Moves the element down to maintain Max-Heap property
    private void heapifyDown(int i) {
        int maxIndex = i;
        int left = leftChild(i);
        int right = rightChild(i);

        if (left < size && heap[left].getBorrowCount() > heap[maxIndex].getBorrowCount()) {
            maxIndex = left;
        }

        if (right < size && heap[right].getBorrowCount() > heap[maxIndex].getBorrowCount()) {
            maxIndex = right;
        }

        if (i != maxIndex) {
            swap(i, maxIndex);
            heapifyDown(maxIndex);
        }
    }

    // Called when loading books from file (Restore state)
    public void addExisting(Book book) {
        if (size < capacity) {
            heap[size] = book;
            heapifyUp(size);
            size++;
        }
    }

    // Called when a user borrows a book
    public void incrementPopularity(Book book) {
        int index = -1;

        // Find the book in the heap
        for (int i = 0; i < size; i++) {
            if (heap[i] == book) { // Compare references
                index = i;
                break;
            }
        }

        if (index != -1) {
            // Book is already tracked, just update count
            book.incrementBorrowCount();
            // Since count increased, it might need to go UP
            heapifyUp(index);
        } else {
            // New book entering the popularity contest
            if (size < capacity) {
                book.incrementBorrowCount();
                heap[size] = book;
                heapifyUp(size);
                size++;
            }
        }
    }

    // Returns the top most popular book
    public Book getMostPopular() {
        if (size == 0) return null;
        return heap[0];
    }
    // Returns a string of all books sorted by popularity (High to Low)
    // Does not affect the actual Heap structure.
    public String getSortedList() {
        if (size == 0) return "No books have been borrowed yet.";

        //  Create a shallow copy of the heap array to avoid messing up the original
        Book[] tempArray = new Book[size];
        for (int i = 0; i < size; i++) {
            tempArray[i] = heap[i];
        }

        // Sort the temporary array (Bubble Sort for simplicity)
        // Sorting from Highest Borrow Count to Lowest
        for (int i = 0; i < size - 1; i++) {
            for (int j = 0; j < size - i - 1; j++) {
                if (tempArray[j].getBorrowCount() < tempArray[j + 1].getBorrowCount()) {
                    // Swap
                    Book temp = tempArray[j];
                    tempArray[j] = tempArray[j + 1];
                    tempArray[j + 1] = temp;
                }
            }
        }

        // Build the String
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-30s %-10s\n", "Title", "Author"));
        sb.append("--------------------------------------------------------------\n");

        for (int i = 0; i < size; i++) {
            Book b = tempArray[i];
            sb.append(String.format("%-30s %-10s\n",b.getTitle(), b.getAuthor()));
        }
        return sb.toString();
    }
}