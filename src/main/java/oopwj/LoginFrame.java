package oopwj;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
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
    
    public LoginFrame(){

        setSize(1000, 1000); 
        setTitle("Login Page");

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        JLabel titlelabel = new JLabel("Login Here!");
        titlelabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titlelabel.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(titlelabel);
        
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        ImageIcon Logo = new ImageIcon(getClass().getResource("/oopwj/image/APU_Logo.png"));
        JLabel logolabel = new JLabel(Logo);
        logolabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(logolabel);
        
        
        panel.add(Box.createRigidArea(new Dimension(0, 30)));

        JLabel userIDlabel = new JLabel("Enter your ID here:");
        JTextField userIDtextfield = new JTextField();
        userIDlabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        userIDtextfield.setAlignmentX(Component.CENTER_ALIGNMENT);
        userIDtextfield.setMaximumSize(new Dimension(200, 30));
        panel.add(userIDlabel);
        panel.add(userIDtextfield);
        
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel userPasswordlabel = new JLabel("Enter your Password here:");
        JPasswordField userPasswordtextfield = new JPasswordField(); 
        userPasswordlabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        userPasswordtextfield.setAlignmentX(Component.CENTER_ALIGNMENT);
        userPasswordtextfield.setMaximumSize(new Dimension(200, 30));
        panel.add(userPasswordlabel);
        panel.add(userPasswordtextfield);
        
        
        panel.add(Box.createRigidArea(new Dimension(0, 30)));

        JButton enterButton = new JButton("Login");
        enterButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        enterButton.setMaximumSize(new Dimension(100, 40));
        panel.add(enterButton);

        
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        JButton backButton = new JButton("Exit"); 
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(backButton);

        this.add(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        setVisible(true);

        

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
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() ->{
            new LoginFrame();
        });
    }

}