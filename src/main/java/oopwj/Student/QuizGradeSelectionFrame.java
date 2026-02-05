package oopwj.Student;

import oopwj.Model.User;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class QuizGradeSelectionFrame extends JFrame {

    public QuizGradeSelectionFrame(User user, String moduleID, String moduleName, List<String[]> grades) {
        setTitle("Grade Summary - " + moduleID);
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("<html>Grade Summary for<br/>" + moduleID + ": " + moduleName + "</html>", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // List Panel
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));

        for (String[] g : grades) {
            String qzID = g[0];
            String score = g[1];
            String grade = g[2];
            String comment = g[3];

            // Card Panel for each grade entry
            JPanel card = new JPanel(new BorderLayout());
            card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));
            card.setMaximumSize(new Dimension(500, 80));
            card.setBackground(Color.WHITE);

            JLabel infoLabel = new JLabel("<html><b>Quiz: " + qzID + "</b><br/>Score: " + score + " (" + grade + ")</html>");
            infoLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            
            JButton viewBtn = new JButton("View Details");
            viewBtn.addActionListener(e -> {
                new ResultDetailsFrame(user, moduleID, moduleName, qzID);
            });

            card.add(infoLabel, BorderLayout.CENTER);
            card.add(viewBtn, BorderLayout.EAST);
            
            // Add comment tooltip or small text if needed
            card.setToolTipText("Lecturer Comment: " + comment);

            listPanel.add(card);
            listPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        mainPanel.add(new JScrollPane(listPanel), BorderLayout.CENTER);

        // Back Button
        JButton backBtn = new JButton("Back");
        backBtn.addActionListener(e -> {
            new StudentResultFrame(user);
            dispose();
        });
        mainPanel.add(backBtn, BorderLayout.SOUTH);

        this.add(mainPanel);
        setVisible(true);
    }
}