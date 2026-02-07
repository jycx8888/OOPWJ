package oopwj.Student;

import oopwj.Model.User;
import oopwj.Model.StudentService;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class QuizMenu extends JFrame {

    private User currentUser;
    private StudentService service;

    public QuizMenu(User user) {
        this.currentUser = user;
        this.service = new StudentService();

        setTitle("Quiz Menu - Select Course");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Select a Course:", SwingConstants.CENTER);
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

                
                btn.addActionListener(e -> {
                    System.out.println("[DEBUG] Clicked course: " + courseID);
                    
                    
                    List<String[]> quizzes = service.getQuizzesForModule(courseID);
                    
                    
                    if (quizzes != null && !quizzes.isEmpty()) {
                        new QuizPaperSelectionFrame(currentUser, courseID, courseName, quizzes);
                        dispose(); 
                    } else {
                        
                        JOptionPane.showMessageDialog(this, 
                            "No active quizzes found for " + courseID + ".\n(Check Console for details)", 
                            "No Quizzes", JOptionPane.INFORMATION_MESSAGE);
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