package librarymanagementsystem;

public interface UndoAction { // the goal of this interface is pushing recovable actions to the stack
    void undo(); // all class that implements this interface have to have undo method.
    String getName();
}

class undoAddBook implements UndoAction{
    
    private LibraryManager lib;
    private Book book;
    private final String name = "Adding book";
    
    public undoAddBook(LibraryManager lib, Book book){
        this.lib = lib;
        this.book = book;
    }
    
    @Override
    public void undo() {
        lib.removeBook(book);
        System.out.println("The operation undo is completed.");
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
} 

class undoRemoveBook implements UndoAction{
    
    private LibraryManager lib;
    private Book book;
    private final String name = "Removing book";
    
    public undoRemoveBook(LibraryManager lib, Book book){
        this.lib = lib;
        this.book = book;
    }
    
    @Override
    public void undo() {
        if(book == null){
            System.out.println("The undo operation is not successfuly.");
            return;
        }
        lib.addBookForUndo(book.getTitle(), book.getAuthor());
    }
    
    @Override
    public String getName(){
        return this.name;
    }
}

class undoRegisterNewUser implements UndoAction{
    
    private LibraryManager lib;
    private User user;
    private final String name = "Register new user";
    
    public undoRegisterNewUser(LibraryManager lib, User user){
        this.lib = lib;
        this.user = user;
    }
    
    @Override
    public void undo(){
        lib.userManager.deleteUser(user.getId());
    }
    
    @Override
    public String getName(){
        return this.name;
    }
}

class undoRemoveUser implements UndoAction{
    
    private LibraryManager lib;
    private final String name = "Remove user";
    User user;
    
    public undoRemoveUser(LibraryManager lib, String id){
        this.lib = lib;
        this.user = lib.userManager.getUser(id);
    }
    @Override
    public void undo(){
        lib.userManager.registerUser(user.getName(), user.getSurname(), Integer.parseInt(user.getId()));
    }
    @Override
    public String getName(){
        return this.name;
    }
}