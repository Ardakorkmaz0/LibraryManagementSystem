package librarymanagementsystem;

public class UndoManager {
    
    public stack lastActions = new stack();
    final int capacity = 5;
    
    public void addAction(UndoAction action) {
        if(lastActions.size() < capacity){
            lastActions.push(action);
        }
        else if(lastActions.size() >= capacity){
            lastActions.deleteFifth();
            lastActions.push(action);
        }
    }
}
