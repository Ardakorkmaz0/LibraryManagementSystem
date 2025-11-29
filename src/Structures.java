package librarymanagementsystem;

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

class idBst extends bst {
    void addById(User user) {
        root = insertUserId(root, user);
        // in here, push a stack of reverse this method for undo
    }
    private bstNode insertUserId(bstNode node, User user){
        if(node == null) {
            return new bstNode(user);
        }
        else{
            User current = (User) node.data;
            if(user.getId().compareToIgnoreCase(current.getId()) < 0) {
                node.left = insertUserId(node.left, user);
            }
            else{
                node.right = insertUserId(node.right, user);
            }
        }
        return node;
    }
}
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
        // in here, push a stack of reverse this method for undo
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

    // --- NEW: Delete method for Author Tree ---
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

    // --- NEW: Delete method for Title Tree ---
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
}

class queue { // queue operations

    sllNode front;
    sllNode rear;

    public queue(){
        front = null;
        rear = null;
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
}