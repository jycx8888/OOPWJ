package oopwj.Student;

import oopwj.Model.User;
import oopwj.Model.StudentService; 
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class QuizPaperSelectionFrame extends JFrame {

    public QuizPaperSelectionFrame(User user, String moduleID, String moduleName, List<String[]> quizzes) {
        setTitle("Select Quiz Paper - " + moduleID);
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("<html>Select a Quiz for<br/>" + moduleID + ": " + moduleName + "</html>", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));

        // Create service to check attempts
        StudentService service = new StudentService();

        if (quizzes == null || quizzes.isEmpty()) {
            listPanel.add(new JLabel("No quizzes available (List is empty)."));
        } else {
            for (String[] quiz : quizzes) {
                String quizID = quiz[0];    
                String quizTitle = quiz[1]; 

                JButton btn = new JButton(quizTitle + " (" + quizID + ")");
                btn.setMaximumSize(new Dimension(400, 40));
                btn.setAlignmentX(Component.CENTER_ALIGNMENT);
                btn.setFont(new Font("Arial", Font.PLAIN, 14));

                // FIX: Check attempts using StudentID AND ModuleID AND QuizID
                if (service.hasAttemptedQuiz(user.getUserID(), moduleID, quizID)) {
                    btn.setText(quizTitle + " (" + quizID + ") - Completed");
                    btn.setEnabled(false); 
                } else {
                    btn.addActionListener(e -> {
                        new QuizPage(user, moduleID, moduleName, quizID, quizTitle);
                        dispose(); 
                    });
                }

                listPanel.add(btn);
                listPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }

        mainPanel.add(new JScrollPane(listPanel), BorderLayout.CENTER);
        
        JButton backBtn = new JButton("Back to Courses");
        backBtn.addActionListener(e -> {
            new QuizMenu(user);
            dispose();
        });
        mainPanel.add(backBtn, BorderLayout.SOUTH);

        this.add(mainPanel);

        setVisible(true);
    }
}