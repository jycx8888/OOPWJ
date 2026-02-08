package oopwj.Student;

import oopwj.Model.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import oopwj.LoginFrame;

public class StudentFrame extends JFrame {

    private User user; 
    private JLabel welcomeLabel; 

    public StudentFrame(User user) {
        this.user = user; 

        setSize(1000, 1000);
        setTitle("Student Dashboard - " + user.getUserID());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(Box.createVerticalGlue());

        String name = (user != null) ? user.getUserName() : "User";
        welcomeLabel = new JLabel("Hi, " + name);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 30)); 
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(welcomeLabel);

        panel.add(Box.createRigidArea(new Dimension(0, 40))); 

       
        JButton profileBtn = new JButton("Edit Profile");
        setupButton(profileBtn);
        panel.add(profileBtn);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        
        JButton timetableBtn = new JButton("My Timetable");
        setupButton(timetableBtn);
        panel.add(timetableBtn);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton registerBtn = new JButton("Register for Classes");
        setupButton(registerBtn);
        panel.add(registerBtn);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton quizBtn = new JButton("Attempt Quiz");
        setupButton(quizBtn);
        panel.add(quizBtn);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton resultBtn = new JButton("Check Results");
        setupButton(resultBtn);
        panel.add(resultBtn);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton feedbackBtn = new JButton("Provide Feedback");
        setupButton(feedbackBtn);
        panel.add(feedbackBtn);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton logoutButton = new JButton("Logout");
        setupButton(logoutButton);
        panel.add(logoutButton);

        panel.add(Box.createVerticalGlue());

        this.add(panel);
        setVisible(true);

        

        profileBtn.addActionListener(e -> {
            ProfileFrame pFrame = new ProfileFrame(user);
            pFrame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    welcomeLabel.setText("Hi, " + user.getUserName());
                }
            });
        });

        
        timetableBtn.addActionListener(e -> new TimetableFrame(user));

        registerBtn.addActionListener(e -> new RegisterClassFrame(user));
        quizBtn.addActionListener(e -> new QuizMenu(user));
        resultBtn.addActionListener(e -> new StudentResultFrame(user));
        feedbackBtn.addActionListener(e -> new FeedbackFrame(user));

        logoutButton.addActionListener(e -> {
            dispose(); 
            new LoginFrame(); 
        });
    }

    private void setupButton(JButton btn) {
        btn.setMaximumSize(new Dimension(200, 50)); 
        btn.setAlignmentX(Component.CENTER_ALIGNMENT); 
    }
}