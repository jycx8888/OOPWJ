package GUI;

import Model.User;
import Services.StudentService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class TimetableFrame extends JFrame {

    private User currentUser;
    private StudentService service;
    private JPanel contentPanel;
    private JComboBox<String> daySelector;

    public TimetableFrame(User user) {
        this.currentUser = user;
        this.service = new StudentService();

        setTitle("My Class Schedule");
        setSize(600, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // ==========================================
        // Top Panel: Title Middle, Select Box Right
        // ==========================================
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(new EmptyBorder(0, 0, 20, 0)); // 底部留白

        // 1. 中间标题
        JLabel titleLabel = new JLabel("Timetable", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(50, 50, 50));
        
        // 2. 右侧选择区域
        JPanel rightSelectPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        
        JLabel viewLabel = new JLabel("View: ");
        viewLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        daySelector = new JComboBox<>(days);
        daySelector.setFont(new Font("Arial", Font.PLAIN, 12));
        daySelector.setPreferredSize(new Dimension(120, 25));
        daySelector.addActionListener(e -> updateSchedule());

        rightSelectPanel.add(viewLabel);
        rightSelectPanel.add(daySelector);

        // 组装 Top Panel
        topPanel.add(titleLabel, BorderLayout.CENTER);
        topPanel.add(rightSelectPanel, BorderLayout.EAST);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // ==========================================
        // Center: Content
        // ==========================================
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // ==========================================
        // Bottom: Close Button
        // ==========================================
        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> dispose());
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(closeBtn);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // 初始加载
        updateSchedule();

        this.add(mainPanel);
        setVisible(true);
    }

    private void updateSchedule() {
        contentPanel.removeAll(); 
        
        String selectedDay = (String) daySelector.getSelectedItem();
        List<String[]> classes = service.getStudentTimetable(currentUser.getUserID(), selectedDay);

        if (classes.isEmpty()) {
            // --- 无课显示 (去掉 Emoji) ---
            contentPanel.add(Box.createVerticalGlue());
            
            JLabel enjoyLabel = new JLabel("No class today, enjoy it!", SwingConstants.CENTER);
            enjoyLabel.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 24));
            enjoyLabel.setForeground(new Color(39, 174, 96)); // 绿色
            enjoyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            contentPanel.add(enjoyLabel);
            
            contentPanel.add(Box.createVerticalGlue());
            
        } else {
            // --- 有课显示 (卡片样式) ---
            for (String[] cls : classes) {
                String modID = cls[0];
                String dateStr = cls[1];
                String timeStr = cls[2] + " - " + cls[3];
                String venueStr = cls[4];

                JPanel card = new JPanel(new BorderLayout(10, 5));
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(5, 5, 5, 5),
                    BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                        BorderFactory.createEmptyBorder(15, 15, 15, 15)
                    )
                ));
                card.setMaximumSize(new Dimension(550, 100)); 
                card.setBackground(Color.WHITE);

                JLabel modLabel = new JLabel(modID);
                modLabel.setFont(new Font("Arial", Font.BOLD, 18));
                modLabel.setForeground(new Color(41, 128, 185));
                
                JLabel venueLabel = new JLabel("Venue: " + venueStr);
                venueLabel.setFont(new Font("Arial", Font.BOLD, 14));
                venueLabel.setForeground(new Color(44, 62, 80));
                
                try {
                    venueLabel.setIcon(UIManager.getIcon("FileView.computerIcon")); 
                } catch(Exception ignored){}

                JLabel timeLabel = new JLabel(timeStr);
                timeLabel.setFont(new Font("Arial", Font.BOLD, 16));
                timeLabel.setForeground(new Color(192, 57, 43)); 
                
                JLabel dateLabel = new JLabel(dateStr, SwingConstants.RIGHT);
                dateLabel.setFont(new Font("Arial", Font.PLAIN, 12));
                dateLabel.setForeground(Color.GRAY);

                JPanel leftInfo = new JPanel(new GridLayout(2, 1, 0, 5));
                leftInfo.setOpaque(false);
                leftInfo.add(modLabel);
                leftInfo.add(venueLabel);

                JPanel rightInfo = new JPanel(new GridLayout(2, 1, 0, 5));
                rightInfo.setOpaque(false);
                rightInfo.add(timeLabel);
                rightInfo.add(dateLabel);

                card.add(leftInfo, BorderLayout.CENTER);
                card.add(rightInfo, BorderLayout.EAST);

                contentPanel.add(card);
                contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }

        contentPanel.revalidate();
        contentPanel.repaint();
    }
}