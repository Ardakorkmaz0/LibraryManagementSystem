
# Library Management System (Java)
## Screenshots
<table>
  <tr>
    <td>
      <img src="https://github.com/user-attachments/assets/d81613b4-ab7b-484d-b866-c704b3b84f5b" width="250" height="250">
    </td>
    <td>
      <img src="https://github.com/user-attachments/assets/d7dc817f-739b-4491-844d-085141ddc84f" width="250" height="250">
    </td>
  </tr>
  <tr>
    <td>
      <img src="https://github.com/user-attachments/assets/4af6303e-55cc-404b-bbf5-b4b1a60036fc" width="250" height="250">
    </td>
    <td>
      <img src="https://github.com/user-attachments/assets/bee16c7e-0ebf-4ee0-b68c-119eccb64f52" width="250" height="250">
    </td>
  </tr>
</table>

## About the Project

This project is a **console and gui-based Library Management System** developed using Java. The main goal is to manage books and users by using basic data structures and to practice Object-Oriented Programming concepts in a simple and clear way.

---

##  What Does the Project Do?

* Add, delete, and search books
* Add and manage users
* Create a waiting list when a book is not available
* Undo the last operation

All features are designed with a modular class structure and clean code logic.

---

##  Used Data Structures

* **Binary Search Tree (BST):**
  Books and users are stored in BSTs to allow fast searching and organized data management.

* **Queue:**
  A queue is used as a waiting list when a book is already borrowed.

* **Stack:**
  A stack is used to store actions that can be undone.

---

##  Undo Mechanism

The project includes an **Undo mechanism** for important operations. Each action is saved as an `UndoAction` object and pushed into a Stack. When the user selects undo, the last action is reverted safely.

This mechanism helps to:

* Keep data consistent
* Reduce user mistakes
* Simulate undo logic used in real-world applications

---

## How to Run

### Windows (PowerShell)
```powershell
# 1. Clone the repository from GitHub
git clone https://github.com/Ardakorkmaz0/LibraryManagementSystem.git

# 2. Navigate into the project directory
cd LibraryManagementSystem

# 3. Create a bin directory for compiled files
New-Item -ItemType Directory -Force -Path bin

# 4. Compile the Java source files
javac -d bin src/librarymanagementsystem/*.java

# 5. Run the application
java -cp bin librarymanagementsystem.LibraryManagementSystem
```

### macOS / Linux (Bash)
```bash
# 1. Clone the repository from GitHub
git clone https://github.com/Ardakorkmaz0/LibraryManagementSystem.git

# 2. Navigate into the project directory
cd LibraryManagementSystem

# 3. Create a bin directory for compiled files
mkdir -p bin

# 4. Compile the Java source files
javac -d bin src/librarymanagementsystem/*.java

# 5. Run the application
java -cp bin librarymanagementsystem.LibraryManagementSystem
```
