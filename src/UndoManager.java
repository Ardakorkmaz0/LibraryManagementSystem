package librarymanagementsystem;

public class UndoManager {
    
    private stack lastActions = new stack();
    final int capacity = 5;
    
    void addAction(UndoAction action) {
        if(lastActions.size() < capacity){
            lastActions.push(action);
        }
    }
}
