package librarymanagementsystem;

public interface UndoAction { // the goal of this interface is pushing recovable actions to the stack
    void undo(); // all class that implements this interface have to have undo method.
}

class undoAddBook implements UndoAction{
    
    private LibraryManager lib;
    private Book book;
    
    public undoAddBook(LibraryManager lib, Book book){
        this.lib = lib;
        this.book = book;
    }
    
    @Override
    public void undo() {
        lib.removeBook(book);
        System.out.println("The operation undo is completed.");
    }
} 
