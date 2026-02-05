package oopwj.Student;

import oopwj.Model.User;
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

        // 1. 创建主面板
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // 2. 标题
        JLabel titleLabel = new JLabel("<html>Select a Quiz for<br/>" + moduleID + ": " + moduleName + "</html>", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // 3. 列表面板
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));

        // 填充试卷按钮
        if (quizzes == null || quizzes.isEmpty()) {
            listPanel.add(new JLabel("No quizzes available (List is empty)."));
        } else {
            for (String[] quiz : quizzes) {
                String quizID = quiz[0];    // QZ001
                String quizTitle = quiz[1]; // Title

                JButton btn = new JButton(quizTitle + " (" + quizID + ")");
                btn.setMaximumSize(new Dimension(400, 40));
                btn.setAlignmentX(Component.CENTER_ALIGNMENT);
                btn.setFont(new Font("Arial", Font.PLAIN, 14));

                // 点击进入考试
                btn.addActionListener(e -> {
                    new QuizPage(user, moduleID, moduleName, quizID, quizTitle);
                    dispose(); 
                });

                listPanel.add(btn);
                listPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }

        // 4. 将列表放入滚动窗格，并加入主面板
        mainPanel.add(new JScrollPane(listPanel), BorderLayout.CENTER);
        
        // 5. 底部返回按钮
        JButton backBtn = new JButton("Back to Courses");
        backBtn.addActionListener(e -> {
            new QuizMenu(user);
            dispose();
        });
        mainPanel.add(backBtn, BorderLayout.SOUTH);

        // 【关键修复】: 之前漏了这行，导致面板没加到窗口上！
        this.add(mainPanel);

        setVisible(true);
    }
}