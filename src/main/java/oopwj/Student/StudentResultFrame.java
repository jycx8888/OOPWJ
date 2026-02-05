package oopwj.Student;

import oopwj.Model.User;
import oopwj.Model.StudentService;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class StudentResultFrame extends JFrame {

    private User currentUser;
    private StudentService service;

    public StudentResultFrame(User user) {
        this.currentUser = user;
        this.service = new StudentService();

        setTitle("Check Results - Select Course");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Select a Course to View Results:", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel coursePanel = new JPanel();
        coursePanel.setLayout(new BoxLayout(coursePanel, BoxLayout.Y_AXIS));

        List<String> myCourses = service.getEnrolledCourses(currentUser.getUserID());

        if (myCourses.isEmpty()) {
            coursePanel.add(new JLabel("No enrolled courses found."));
        } else {
            for (String course : myCourses) {
                String[] parts = course.split(",");
                String courseID = parts[0].trim();
                String courseName = (parts.length > 1) ? parts[1].trim() : "";

                JButton btn = new JButton(courseID + " - " + courseName);
                btn.setMaximumSize(new Dimension(400, 40));
                btn.setAlignmentX(Component.CENTER_ALIGNMENT);
                
                // [Updated] Click -> Open Quiz Grade Selection
                btn.addActionListener(e -> {
                    // Check if there are results
                    List<String[]> grades = service.getFinalGradesForModule(currentUser.getUserID(), courseID);
                    
                    if (grades.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "No grades available yet for " + courseID);
                    } else {
                        new QuizGradeSelectionFrame(currentUser, courseID, courseName, grades);
                        dispose();
                    }
                });

                coursePanel.add(btn);
                coursePanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }

        mainPanel.add(new JScrollPane(coursePanel), BorderLayout.CENTER);
        this.add(mainPanel);
        setVisible(true);
    }
}