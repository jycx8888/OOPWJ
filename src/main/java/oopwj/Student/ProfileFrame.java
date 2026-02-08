package oopwj.Student;

import oopwj.Model.User;
import oopwj.Model.StudentService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Locale;

public class ProfileFrame extends JFrame {

    private static final String PROFILE_IMAGE_DIR = "src/main/java/oopwj/image";
    private static final String[] PROFILE_IMAGE_EXTENSIONS = {"png", "jpg", "jpeg", "gif"};
    private JLabel imageLabel; 
    private JLabel nameLabel; 
    private User currentUser; 
    private StudentService service;

    public ProfileFrame(User user) {
        this.currentUser = user;
        this.service = new StudentService();

        setTitle("My Profile");
        setSize(900, 600); 
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        setLocationRelativeTo(null);

        JPanel mainContainer = new JPanel(new GridLayout(1, 2, 20, 0));
        mainContainer.setBorder(new EmptyBorder(30, 30, 30, 30));

        JPanel leftPanel = createLeftPanel();
        JPanel rightPanel = createRightPanel();

        mainContainer.add(leftPanel);
        mainContainer.add(rightPanel);

        this.add(mainContainer);
        setVisible(true);
    }

    private JPanel createLeftPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(0, 20)); 
        panel.setBorder(BorderFactory.createTitledBorder("Profile Image"));

        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setPreferredSize(new Dimension(250, 250)); 
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        if (!loadSavedProfileImage()) {
            ImageIcon defaultIcon = loadAndScaleImage("Image/default_profile.png", 250, 250);
            if (defaultIcon != null) {
                imageLabel.setIcon(defaultIcon);
            } else {
                imageLabel.setText("No Image");
            }
        }

        JButton uploadBtn = new JButton("Change Image");
        uploadBtn.setFont(new Font("Arial", Font.PLAIN, 16));

        uploadBtn.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Images", "jpg", "jpeg", "png", "gif");
            fileChooser.setFileFilter(filter);
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                String extension = getFileExtension(selectedFile.getName());
                if (extension.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Unsupported image file.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    File imageDir = new File(PROFILE_IMAGE_DIR);
                    if (!imageDir.exists()) {
                        imageDir.mkdirs();
                    }

                    deleteExistingProfileImages();
                    File destination = new File(imageDir, getProfileImageBaseName() + "." + extension);
                    Files.copy(selectedFile.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    ImageIcon newIcon = loadAndScaleImage(destination.getAbsolutePath(), 250, 250);
                    imageLabel.setIcon(newIcon);
                    imageLabel.setText("");
                    JOptionPane.showMessageDialog(this, "Image updated successfully.");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Error saving image.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        panel.add(imageLabel, BorderLayout.CENTER);
        panel.add(uploadBtn, BorderLayout.SOUTH);
        return panel;
    }

    private ImageIcon loadAndScaleImage(String path, int width, int height) {
         try {
            File imgFile = new File(path);
            if (imgFile.exists()) {
                ImageIcon originalIcon = new ImageIcon(path);
                Image image = originalIcon.getImage();
                return new ImageIcon(image.getScaledInstance(width, height, Image.SCALE_SMOOTH));
            }
        } catch (Exception e) { return null; }
        return null;
    }

    private boolean loadSavedProfileImage() {
        File imageFile = findExistingProfileImage();
        if (imageFile == null || !imageFile.exists()) {
            return false;
        }

        ImageIcon savedIcon = loadAndScaleImage(imageFile.getAbsolutePath(), 250, 250);
        if (savedIcon != null) {
            imageLabel.setIcon(savedIcon);
            imageLabel.setText("");
            return true;
        }
        return false;
    }

    private File findExistingProfileImage() {
        if (currentUser == null || currentUser.getUserID() == null || currentUser.getUserID().trim().isEmpty()) {
            return null;
        }

        File dir = new File(PROFILE_IMAGE_DIR);
        if (!dir.exists()) {
            return null;
        }

        String baseName = getProfileImageBaseName();
        for (String ext : PROFILE_IMAGE_EXTENSIONS) {
            File candidate = new File(dir, baseName + "." + ext);
            if (candidate.exists()) {
                return candidate;
            }
        }
        return null;
    }

    private String getProfileImageBaseName() {
        return "profile_" + currentUser.getUserID();
    }

    private void deleteExistingProfileImages() {
        File dir = new File(PROFILE_IMAGE_DIR);
        if (!dir.exists() || currentUser == null || currentUser.getUserID() == null) {
            return;
        }

        String baseName = getProfileImageBaseName();
        for (String ext : PROFILE_IMAGE_EXTENSIONS) {
            File candidate = new File(dir, baseName + "." + ext);
            if (candidate.exists()) {
                candidate.delete();
            }
        }
    }

    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex <= 0 || dotIndex == fileName.length() - 1) {
            return "";
        }
        return fileName.substring(dotIndex + 1).toLowerCase(Locale.ROOT);
    }

    private JPanel createRightPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder("Student Information"));

        Font labelFont = new Font("Arial", Font.BOLD, 16);
        Font textFont = new Font("Arial", Font.PLAIN, 14);

        panel.add(Box.createVerticalStrut(20)); 
        JLabel idLabel = new JLabel("Student ID: " + currentUser.getUserID());
        idLabel.setFont(labelFont);
        panel.add(idLabel);

        panel.add(Box.createVerticalStrut(20)); 
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        namePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        nameLabel = new JLabel("Name: " + currentUser.getUserName() + "  ");
        nameLabel.setFont(labelFont);
        
        JButton editNameBtn = new JButton("Edit");
        editNameBtn.setMargin(new Insets(2, 5, 2, 5)); 
        editNameBtn.setFont(new Font("Arial", Font.PLAIN, 12));
        
        namePanel.add(nameLabel);
        namePanel.add(editNameBtn);
        panel.add(namePanel);

        panel.add(Box.createVerticalStrut(20)); 
        JLabel emailLabel = new JLabel("Email: " + currentUser.getEmail());
        emailLabel.setFont(labelFont);
        panel.add(emailLabel);

        panel.add(Box.createVerticalStrut(20));
        JButton changePassBtn = new JButton("Change Password");
        changePassBtn.setFont(textFont);
        panel.add(changePassBtn);

        panel.add(Box.createVerticalStrut(30)); 
        JLabel courseTitleLabel = new JLabel("Current Enrolled Courses:");
        courseTitleLabel.setFont(labelFont);
        panel.add(courseTitleLabel);

        panel.add(Box.createVerticalStrut(10));
        JTextArea courseArea = new JTextArea(8, 20);
        courseArea.setFont(textFont);
        courseArea.setEditable(false); 
        courseArea.setBackground(new Color(240, 240, 240)); 
        
        List<String> myCourses = service.getEnrolledCourses(currentUser.getUserID());
        if (myCourses.isEmpty()) {
            courseArea.setText("\n  No courses enrolled yet.");
        } else {
            StringBuilder sb = new StringBuilder("\n");
            for (String c : myCourses) {
                String[] parts = c.split(",");
                if(parts.length >= 2) sb.append(" • ").append(parts[1]).append(" (").append(parts[0]).append(")\n\n");
                else sb.append(" • ").append(c).append("\n\n");
            }
            courseArea.setText(sb.toString());
        }
        
        JScrollPane scrollPane = new JScrollPane(courseArea);
        panel.add(scrollPane);

        idLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        namePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        emailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        changePassBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        courseTitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        
        editNameBtn.addActionListener(e -> {
            String currentName = currentUser.getUserName();
            String newName = JOptionPane.showInputDialog(this, "Enter new name:", currentName);
            
            if (newName != null && !newName.trim().isEmpty()) {
                if (!service.isValidName(newName)) { 
                    JOptionPane.showMessageDialog(this, "Invalid name!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                
                if (service.updateName(currentUser.getUserID(), newName.trim())) {
                    currentUser.setUserName(newName.trim()); 
                    nameLabel.setText("Name: " + newName.trim() + "  ");
                    JOptionPane.showMessageDialog(this, "Name updated successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "Error updating name.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        
        changePassBtn.addActionListener(e -> {
            JPasswordField pf = new JPasswordField();
            int okCfm = JOptionPane.showConfirmDialog(this, new Object[]{"Enter current password:", pf}, 
                    "Confirm Password", JOptionPane.OK_CANCEL_OPTION);
            
            if (okCfm == JOptionPane.OK_OPTION) {
                String currentPassInput = new String(pf.getPassword());
                if (!currentPassInput.equals(currentUser.getPassword())) {
                    JOptionPane.showMessageDialog(this, "Wrong password!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                while (true) {
                    JPasswordField newPf = new JPasswordField();
                    JPasswordField confPf = new JPasswordField();
                    JLabel reqLbl = new JLabel("<html><b>Password Requirements:</b><br/>- Length: 7-14<br/>- Lowercase, Uppercase, Number, Symbol</html>");

                    int opt = JOptionPane.showConfirmDialog(this, 
                        new Object[]{"Enter new password:", newPf, "Confirm new password:", confPf, " ", reqLbl}, 
                        "Change Password", JOptionPane.OK_CANCEL_OPTION);

                    if (opt != JOptionPane.OK_OPTION) break;

                    String newPass = new String(newPf.getPassword());
                    String confPass = new String(confPf.getPassword());

                    if (!service.isValidPassword(newPass)) { 
                        JOptionPane.showMessageDialog(this, "Invalid password format!", "Failed", JOptionPane.ERROR_MESSAGE);
                        continue;
                    }
                    if (!newPass.equals(confPass)) {
                        JOptionPane.showMessageDialog(this, "Passwords do not match!", "Failed", JOptionPane.ERROR_MESSAGE);
                        continue;
                    }

                    
                    if (service.updatePassword(currentUser.getUserID(), newPass)) {
                        currentUser.setPassword(newPass); 
                        JOptionPane.showMessageDialog(this, "Password changed successfully!");
                        break;
                    } else {
                        JOptionPane.showMessageDialog(this, "Error updating password.", "Error", JOptionPane.ERROR_MESSAGE);
                        break;
                    }
                }
            }
        });

        return panel;
    }
}