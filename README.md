
# Library Management System (Java)

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
