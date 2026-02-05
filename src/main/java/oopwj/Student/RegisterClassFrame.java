package oopwj.Student;

import oopwj.Model.User;
import oopwj.Model.StudentService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class RegisterClassFrame extends JFrame {

    private User currentUser;
    private StudentService service;
    

    private DefaultListModel<String> availableModel;
    private DefaultListModel<String> enrolledModel;

    public RegisterClassFrame(User user) {
        this.currentUser = user;
        this.service = new StudentService();

        
        setTitle("Register for Classes");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        setLocationRelativeTo(null);

        
        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20)); 

        
        JPanel leftPanel = new JPanel(new BorderLayout(0, 10));
        leftPanel.add(new JLabel("Available Courses (From modules.txt)"), BorderLayout.NORTH);

        availableModel = new DefaultListModel<>();
        JList<String> leftList = new JList<>(availableModel);
        leftPanel.add(new JScrollPane(leftList), BorderLayout.CENTER);

        JButton addButton = new JButton("Add Course ->");
        leftPanel.add(addButton, BorderLayout.SOUTH);

        JPanel rightPanel = new JPanel(new BorderLayout(0, 10));
        rightPanel.add(new JLabel("Your Enrolled Courses (Saved in enrollment.txt)"), BorderLayout.NORTH);

        enrolledModel = new DefaultListModel<>();
        JList<String> rightList = new JList<>(enrolledModel);
        rightList.setEnabled(false); 
        rightPanel.add(new JScrollPane(rightList), BorderLayout.CENTER);

        loadData();

        
        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);

        this.add(mainPanel);
        setVisible(true);

        
        addButton.addActionListener(e -> {
            String selectedCourse = leftList.getSelectedValue();
            
            if (selectedCourse == null) {
                JOptionPane.showMessageDialog(this, "Please select a course from the left first.");
                return;
            }

            boolean success = service.enrollCourse(currentUser.getUserID(), selectedCourse);

            if (success) {
                String[] parts = selectedCourse.split(",");
                String displayString = parts[0] + "," + parts[1];
                
                enrolledModel.addElement(displayString);
                JOptionPane.showMessageDialog(this, "Successfully enrolled in: " + parts[1]);
            } else {
                JOptionPane.showMessageDialog(this, "You have already enrolled in this course or error occurred.");
            }
        });
    }

    private void loadData() {
        List<String> allModules = service.getAllModules();
        for (String m : allModules) {
            availableModel.addElement(m);
        }

        
        List<String> myCourses = service.getEnrolledCourses(currentUser.getUserID());
        for (String c : myCourses) {
            enrolledModel.addElement(c);
        }
    }
}