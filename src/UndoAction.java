package librarymanagementsystem;

public interface UndoAction { // the goal of this interface is pushing recovable actions to the stack
    void undo(); // all class that implements this interface have to have undo method.
}

class undoAddBook implements UndoAction{
    @Override
    public void undo() {
        
    }
}
