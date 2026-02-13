package oopwj.Student;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import oopwj.LoginFrame;
import oopwj.Model.User;

public class StudentFrame extends JFrame {

    private User user;
    private JLabel welcomeLabel;

    
    private Color headerBlue = new Color(0, 60, 100);
    private Color bgGrey = new Color(240, 242, 245);
    private Color cardBg = Color.WHITE;
    private Color textTitle = new Color(33, 37, 41);
    private Color textDesc = new Color(108, 117, 125);
    private Color hoverBorder = new Color(0, 123, 255);

    public StudentFrame(User user) {
        this.user = user;

        
        setSize(1200, 800);
        setTitle("Student Dashboard - " + user.getUserID());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

    
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(headerBlue);
        headerPanel.setPreferredSize(new Dimension(getWidth(), 80));
        headerPanel.setBorder(new EmptyBorder(0, 40, 0, 40));

        
        String name = (user != null) ? user.getUserName() : "Student";
        welcomeLabel = new JLabel("Welcome, " + name);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        welcomeLabel.setForeground(Color.WHITE);
        headerPanel.add(welcomeLabel, BorderLayout.WEST);

        
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBackground(new Color(220, 53, 69));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });

        
        JPanel btnContainer = new JPanel(new GridBagLayout());
        btnContainer.setOpaque(false);
        btnContainer.add(logoutBtn);
        headerPanel.add(btnContainer, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        
        
        
        JPanel cardsPanel = new JPanel(new GridLayout(0, 3, 40, 40));
        cardsPanel.setBackground(bgGrey);
        cardsPanel.setBorder(new EmptyBorder(40, 40, 40, 40)); 

        

        
        cardsPanel.add(createCard(
            "Edit Profile", 
            "Update personal details & contact info.", 
            "👤", 
            new Color(70, 130, 180), 
            e -> {
                ProfileFrame pFrame = new ProfileFrame(user);
                pFrame.addWindowListener(new WindowAdapter() {
                    public void windowClosed(WindowEvent e) {
                        welcomeLabel.setText("Welcome, " + user.getUserName());
                    }
                });
            }
        ));

        
        cardsPanel.add(createCard(
            "My Timetable", 
            "View weekly schedule & room locations.", 
            "📅", 
            new Color(255, 165, 0), 
            e -> new TimetableFrame(user)
        ));

        
        cardsPanel.add(createCard(
            "Register Classes", 
            "Enroll in modules for new semesters.", 
            "📝", 
            new Color(40, 167, 69),
            e -> new RegisterClassFrame(user)
        ));

        cardsPanel.add(createCard(
            "Attempt Quiz", 
            "Take pending quizzes & assessments.", 
            "❓", 
            new Color(111, 66, 193), 
            e -> new QuizMenu(user)
        ));

        
        cardsPanel.add(createCard(
            "Check Results", 
            "View grades, CGPA & transcripts.", 
            "📊", 
            new Color(220, 53, 69), 
            e -> new StudentResultFrame(user)
        ));

        
        cardsPanel.add(createCard(
            "Provide Feedback", 
            "Rate courses and lecturers.", 
            "💬", 
            new Color(23, 162, 184), 
            e -> new FeedbackFrame(user)
        ));

        
        JScrollPane scrollPane = new JScrollPane(cardsPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

   
    private JPanel createCard(String title, String description, String iconSymbol, Color headerColor, ActionListener action) {
        
        
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(cardBg);
        card.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        
        JPanel headerPanel = new JPanel(new GridBagLayout());
        headerPanel.setBackground(headerColor);
        headerPanel.setPreferredSize(new Dimension(100, 100));

        
        JLabel iconLabel = new JLabel(iconSymbol);
        iconLabel.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 40));
        iconLabel.setForeground(Color.WHITE);
        headerPanel.add(iconLabel);

        card.add(headerPanel, BorderLayout.NORTH);

        
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(cardBg);
        textPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        
        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLbl.setForeground(textTitle);
        titleLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        
        textPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        
        JLabel descLbl = new JLabel("<html>" + description + "</html>");
        descLbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        descLbl.setForeground(textDesc);
        descLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        textPanel.add(titleLbl);
        textPanel.add(descLbl);

        card.add(textPanel, BorderLayout.CENTER);

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                action.actionPerformed(null);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBorder(BorderFactory.createLineBorder(hoverBorder, 2));
                textPanel.setBackground(new Color(248, 249, 250)); 
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
                textPanel.setBackground(Color.WHITE);
            }
        });

        return card;
    }
}