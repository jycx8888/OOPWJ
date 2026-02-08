package oopwj;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import oopwj.AcademicLeader.academicLeader;
import oopwj.AdministrativeStaff.AdminMainPage;
import oopwj.Lecturer.Lecturer_menu;
import oopwj.Model.AuthService;
import oopwj.Model.User;
import oopwj.Student.StudentFrame;

public class LoginFrame extends JFrame {
    
    
    Color bgLightBlue = new Color(235, 245, 251); 
    Color btnBlue = new Color(51, 122, 183);      
    Color btnRed = new Color(217, 83, 79);        
    Color textDark = new Color(50, 50, 50);       
    
    public LoginFrame(){

        
        setSize(900, 550); 
        setTitle("APU Login Portal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        
        JPanel mainPanel = new JPanel(new GridLayout(1, 2));
        this.add(mainPanel);

        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(Color.WHITE); 
        leftPanel.setLayout(new GridBagLayout()); 
        
        GridBagConstraints gbcLeft = new GridBagConstraints();
        gbcLeft.gridx = 0;
        gbcLeft.gridy = 0;
        gbcLeft.insets = new Insets(10, 10, 20, 10); 

        
        ImageIcon originalLogo = new ImageIcon(getClass().getResource("/oopwj/image/APU_Logo.png"));
        Image img = originalLogo.getImage();
        Image newImg = img.getScaledInstance(300, 300, Image.SCALE_SMOOTH); 
        ImageIcon scaledLogo = new ImageIcon(newImg);

        JLabel logoLabel = new JLabel(scaledLogo);
        leftPanel.add(logoLabel, gbcLeft);

        
        gbcLeft.gridy = 1;
        JLabel titleLabel = new JLabel("Login Here!");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(0, 0, 102)); 
        leftPanel.add(titleLabel, gbcLeft);

        mainPanel.add(leftPanel);

        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(bgLightBlue); 
        rightPanel.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); 
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel idLabel = new JLabel("User ID:");
        idLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        idLabel.setForeground(textDark);
        rightPanel.add(idLabel, gbc);

        
        gbc.gridy = 1;
        JTextField userIDtextfield = new JTextField();
        userIDtextfield.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userIDtextfield.setPreferredSize(new Dimension(250, 40)); // Bigger box
        rightPanel.add(userIDtextfield, gbc);

        
        gbc.gridy = 2;
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        passLabel.setForeground(textDark);
        rightPanel.add(passLabel, gbc);

        
        gbc.gridy = 3;
        JPasswordField userPasswordtextfield = new JPasswordField();
        userPasswordtextfield.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userPasswordtextfield.setPreferredSize(new Dimension(250, 40));
        rightPanel.add(userPasswordtextfield, gbc);

        
        gbc.gridy = 4;
        gbc.insets = new Insets(30, 10, 10, 10); 
        
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0)); 
        buttonPanel.setBackground(bgLightBlue); 
        buttonPanel.setOpaque(false);

        JButton enterButton = new JButton("Login");
        styleButton(enterButton, btnBlue);

        JButton backButton = new JButton("Exit");
        styleButton(backButton, btnRed);

        buttonPanel.add(enterButton);
        buttonPanel.add(backButton);
        
        rightPanel.add(buttonPanel, gbc);

        mainPanel.add(rightPanel);


        backButton.addActionListener(e -> System.exit(0)); 

        enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                String userID = userIDtextfield.getText().trim().toUpperCase(); 
                String password = new String(userPasswordtextfield.getPassword());

                if(userID.isEmpty() || password.isEmpty()){
                    JOptionPane.showMessageDialog(null, "Please enter ID and Password.");
                    return;
                }

                
                AuthService auth = new AuthService();
                User user = auth.LoginUser(userID, password);
                
                if(user != null){
                    String role = user.getUserRole();
                    JOptionPane.showMessageDialog(null, "Login Successful! \nWelcome, " + role + " " + user.getUserName());
                    
                    dispose(); 

                    switch (role) {
                        case "Student":
                            new StudentFrame(user);
                            break;
                        case "Lecturer":
                            Lecturer_menu menu = new Lecturer_menu(userID);
                            menu.setVisible(true);
                            break;
                        case "Academic Leader":
                            new academicLeader(userID).setVisible(true);
                            break;
                        case "Admin":
                            new AdminMainPage(userID).setVisible(true);
                            break;
                        default:
                            JOptionPane.showMessageDialog(null, "Role not recognized: " + role);
                            break;
                    }

                } else {
                    JOptionPane.showMessageDialog(null, "Invalid ID or Password.");
                }
            }
        });
        
        setVisible(true);
    }
    
    
    private void styleButton(JButton btn, Color bgColor) {
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->{
            new LoginFrame();
        });
    }
}