package GUI;

import Model.User;
import Services.StudentService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class FeedbackFrame extends JFrame {

    private User currentUser;
    private StudentService service;
    private JLabel lecturerLabel; 
    private JComboBox<String> courseBox; 

    public FeedbackFrame(User user) {
        this.currentUser = user;
        this.service = new StudentService();

        setTitle("Course Feedback");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        List<String> myCourses = service.getEnrolledCourses(currentUser.getUserID());

        if (myCourses.isEmpty()) {
            JOptionPane.showMessageDialog(null, "You have not registered for any courses yet!\nPlease register first.");
            // 注意：这里 return 后构造函数结束，frame 不会显示，这是正确行为
            return; 
        }

        JPanel topPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        
        topPanel.add(new JLabel("Select Course:"));
        
        courseBox = new JComboBox<>(myCourses.toArray(new String[0]));
        topPanel.add(courseBox);

        topPanel.add(new JLabel("Lecturer Handling this Module:"));
        
        lecturerLabel = new JLabel("Loading..."); 
        lecturerLabel.setFont(new Font("Arial", Font.BOLD, 14));
        lecturerLabel.setForeground(Color.BLUE);
        topPanel.add(lecturerLabel);

        panel.add(topPanel, BorderLayout.NORTH);

        courseBox.addActionListener(e -> updateLecturerID());

        // 初始化 Lecturer ID
        updateLecturerID();

        JTextArea commentArea = new JTextArea();
        commentArea.setLineWrap(true);
        commentArea.setWrapStyleWord(true);
        
        JScrollPane scrollPane = new JScrollPane(commentArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Your Feedback / Comments"));
        
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton submitBtn = new JButton("Submit Feedback");
        submitBtn.setPreferredSize(new Dimension(100, 40));
        panel.add(submitBtn, BorderLayout.SOUTH);

        submitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String comment = commentArea.getText().trim();
                
                if (comment.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please write your comments before submitting.");
                    return;
                }

                String selectedString = (String) courseBox.getSelectedItem();
                String courseID = selectedString.split(",")[0].trim();
                // 确保这里去掉前缀，只保留 ID
                String lecturerID = lecturerLabel.getText().replace("Lecturer ID: ", "").trim();
                
                // 现在 StudentService 是完全修复版，这里会真正保存文件
                boolean success = service.submitFeedback(currentUser.getUserID(), courseID, lecturerID, comment);

                if (success) {
                    JOptionPane.showMessageDialog(null, "Feedback submitted successfully for " + lecturerID);
                    dispose(); 
                } else {
                    JOptionPane.showMessageDialog(null, "System Error: Could not save feedback.");
                }
            }
        });

        this.add(panel);
        setVisible(true);
    }

    private void updateLecturerID() {
        String selectedString = (String) courseBox.getSelectedItem();
        if (selectedString != null) {
            String courseID = selectedString.split(",")[0].trim();
            String lecID = service.getLecturerIdByCourse(courseID);
            lecturerLabel.setText("Lecturer ID: " + lecID);
        }
    }
}