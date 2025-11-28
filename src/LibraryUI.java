package librarymanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LibraryUI extends JFrame {

    LibraryManager lib;

    public LibraryUI(LibraryManager lib) {
        this.lib = lib;

        // setting the window properties
        setTitle("Library System");
        setSize(300, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // center the window

        // using flow layout because it is simple
        setLayout(new FlowLayout());

        // creating labels and text fields
        JLabel l1 = new JLabel("Book Title:");
        JTextField t1 = new JTextField(20); // length of the text field

        JLabel l2 = new JLabel("Author Name:");
        JTextField t2 = new JTextField(20);

        JButton b1 = new JButton("Add Book");

        // action listener for the button
        b1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // getting text from fields
                String s1 = t1.getText();
                String s2 = t2.getText();

                // checking if fields are empty
                if(s1.equals("") || s2.equals("")) {
                    JOptionPane.showMessageDialog(null, "Fill all fields");
                } else {
                    // calling the add method
                    lib.addBookGUI(s1, s2);
                    JOptionPane.showMessageDialog(null, "Book Added!");

                    // clearing the inputs
                    t1.setText("");
                    t2.setText("");
                }
            }
        });

        // adding components to the frame
        add(l1);
        add(t1);
        add(l2);
        add(t2);
        add(b1);

        // making the window visible
        setVisible(true);
    }
}