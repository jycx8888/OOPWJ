/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package oopwj.Lecturer;

import oopwj.Lecturer.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;

/**
 *
 * @author Justin Yong
 */
public class editProfileL extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(editProfileL.class.getName());
    private static final String PROFILE_IMAGE_DIR = "src/main/java/oopwj/image";
    private static final String[] PROFILE_IMAGE_EXTENSIONS = {"png", "jpg", "jpeg", "gif"};
    private final javax.swing.JLabel profilePictureLabel = new javax.swing.JLabel();
    private String loggedInUserID;
    private javax.swing.JFrame parentFrame;
    private String currentPassword;

    public editProfileL() {
        initComponents();
        setupProfilePicturePanel();
    }
    
    public editProfileL(String userID, javax.swing.JFrame parent) {
        this.loggedInUserID = userID;
        this.parentFrame = parent;
        initComponents();
        setLocationRelativeTo(null);
        setupProfilePicturePanel();
        loadUserData();
        loadProfilePicture();
        addHoverEffect(editProfilePicture, new Color(70,130,180), new Color(100,149,237));
        addHoverEffect(editProfile, new Color(70,130,180), new Color(100,149,237));
        addHoverEffect(changePassword, new Color(70,130,180), new Color(100,149,237));
        addHoverEffect(exit, new Color(220,80,80), new Color(255,120,120));
    }
    
    private void addHoverEffect(JButton btn, Color normal, Color hover) {
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);

        btn.setBackground(normal);
        btn.setForeground(Color.WHITE);

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(hover);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(normal);
            }
        });
    }
    
    private void loadUserData() {
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/java/oopwj/Data/lecturer.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4 && parts[0].equals(loggedInUserID)) {
                    IDLabel.setText(parts[0]);
                    NameLabel.setText(parts[1]);
                    currentPassword = parts[2];
                    EmailLabel.setText(parts[3]);
                    break;
                }
            }
        } catch (IOException e) {
            logger.log(java.util.logging.Level.SEVERE, "Error loading user data", e);
            JOptionPane.showMessageDialog(this, "Error loading user data", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setupProfilePicturePanel() {
        profilePictureLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        profilePicture.setLayout(new java.awt.BorderLayout());
        profilePicture.add(profilePictureLabel, java.awt.BorderLayout.CENTER);
    }

    private void loadProfilePicture() {
        if (loggedInUserID == null || loggedInUserID.trim().isEmpty()) {
            profilePictureLabel.setIcon(null);
            profilePictureLabel.setText("");
            return;
        }

        File imageFile = findExistingProfileImage();
        if (imageFile != null && imageFile.exists()) {
            setProfilePicture(imageFile);
        } else {
            profilePictureLabel.setIcon(null);
            profilePictureLabel.setText("");
        }
    }

    private File findExistingProfileImage() {
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

    private void setProfilePicture(File imageFile) {
        try {
            ImageIcon originalIcon = new ImageIcon(imageFile.getAbsolutePath());
            int targetWidth = profilePicture.getPreferredSize().width;
            int targetHeight = profilePicture.getPreferredSize().height;
            if (targetWidth <= 0 || targetHeight <= 0) {
                targetWidth = 100;
                targetHeight = 100;
            }
            Image scaled = originalIcon.getImage().getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
            profilePictureLabel.setIcon(new ImageIcon(scaled));
            profilePictureLabel.setText("");
        } catch (Exception e) {
            logger.log(java.util.logging.Level.WARNING, "Unable to load profile picture", e);
            profilePictureLabel.setIcon(null);
            profilePictureLabel.setText("");
        }
    }

    private String getProfileImageBaseName() {
        return "profile_" + loggedInUserID;
    }

    private void deleteExistingProfileImages() {
        File dir = new File(PROFILE_IMAGE_DIR);
        if (!dir.exists()) {
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
    
    private boolean isValidPassword(String password) {
        if (password == null || password.length() < 7 || password.length() > 14) {
            return false;
        }
        
        boolean hasLowerCase = false;
        boolean hasUpperCase = false;
        boolean hasDigit = false;
        boolean hasSymbol = false;
        String allowedSymbols = "!@#$%^";
        
        for (char c : password.toCharArray()) {
            if (Character.isLowerCase(c)) {
                hasLowerCase = true;
            } else if (Character.isUpperCase(c)) {
                hasUpperCase = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else if (allowedSymbols.indexOf(c) != -1) {
                hasSymbol = true;
            } else if (c == ',') {
                return false;
            }
        }
        
        return hasLowerCase && hasUpperCase && hasDigit && hasSymbol;
    }
    
    private boolean isValidName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        return name.matches("^[a-zA-Z ]+$");
    }
    
    private boolean updatePassword(String newPassword) {
        List<String> lines = new ArrayList<>();
        boolean updated = false;
        
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/java/oopwj/Data/lecturer.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5 && parts[0].equals(loggedInUserID)) {
                    lines.add(parts[0] + "," + parts[1] + "," + newPassword + "," + parts[3] + "," + parts[4]);
                    updated = true;
                } else {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            logger.log(java.util.logging.Level.SEVERE, "Error reading file", e);
            return false;
        }
        
        if (updated) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter("src/main/java/oopwj/Data/lecturer.txt"))) {
                for (String line : lines) {
                    bw.write(line);
                    bw.newLine();
                }
                currentPassword = newPassword;
                return true;
            } catch (IOException e) {
                logger.log(java.util.logging.Level.SEVERE, "Error writing file", e);
                return false;
            }
        }
        return false;
    }
    
    private boolean updateName(String newName) {
        List<String> lines = new ArrayList<>();
        boolean updated = false;
        
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/java/oopwj/Data/lecturer.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5 && parts[0].equals(loggedInUserID)) {
                    lines.add(parts[0] + "," + newName + "," + parts[2] + "," + parts[3] + "," + parts[4]);
                    updated = true;
                } else {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            logger.log(java.util.logging.Level.SEVERE, "Error reading file", e);
            return false;
        }
        
        if (updated) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter("src/main/java/oopwj/Data/lecturer.txt"))) {
                for (String line : lines) {
                    bw.write(line);
                    bw.newLine();
                }
                return true;
            } catch (IOException e) {
                logger.log(java.util.logging.Level.SEVERE, "Error writing file", e);
                return false;
            }
        }
        return false;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField1 = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;

                GradientPaint gp = new GradientPaint(
                    0, 0, new Color(230, 240, 255),
                    0, getHeight(), new Color(245, 247, 250)
                );

                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        profilePicture = new java.awt.Panel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        changePassword = new javax.swing.JButton();
        editProfile = new javax.swing.JButton();
        IDLabel = new javax.swing.JLabel();
        NameLabel = new javax.swing.JLabel();
        EmailLabel = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        exit = new javax.swing.JButton();
        editProfilePicture = new javax.swing.JButton();

        jTextField1.setText("jTextField1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setOpaque(false);

        profilePicture.setPreferredSize(new java.awt.Dimension(180, 180));

        javax.swing.GroupLayout profilePictureLayout = new javax.swing.GroupLayout(profilePicture);
        profilePicture.setLayout(profilePictureLayout);
        profilePictureLayout.setHorizontalGroup(
            profilePictureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 180, Short.MAX_VALUE)
        );
        profilePictureLayout.setVerticalGroup(
            profilePictureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 180, Short.MAX_VALUE)
        );

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("Profile");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("Email:");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setText("ID:");

        changePassword.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        changePassword.setText("Change Password");
        changePassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changePasswordActionPerformed(evt);
            }
        });

        editProfile.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        editProfile.setText("Edit Profile");
        editProfile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editProfileActionPerformed(evt);
            }
        });

        IDLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        IDLabel.setText("jLabel5");

        NameLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        NameLabel.setText("jLabel5");

        EmailLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        EmailLabel.setText("jLabel5");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("Name:");

        exit.setBackground(new java.awt.Color(220, 80, 80));
        exit.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        exit.setForeground(new java.awt.Color(255, 255, 255));
        exit.setText("X");
        exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitActionPerformed(evt);
            }
        });

        editProfilePicture.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        editProfilePicture.setText("Edit");
        editProfilePicture.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editProfilePictureActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(146, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel1)
                            .addGap(236, 236, 236))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                            .addComponent(profilePicture, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(195, 195, 195))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(40, 40, 40)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(IDLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(NameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(EmailLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(184, 184, 184))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(changePassword)
                            .addGap(38, 38, 38)
                            .addComponent(editProfile)
                            .addGap(157, 157, 157)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(editProfilePicture)
                        .addGap(249, 249, 249))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(exit)
                        .addGap(39, 39, 39))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(exit)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(profilePicture, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(editProfilePicture)
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(IDLabel))
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(NameLabel))
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(EmailLabel))
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(changePassword)
                    .addComponent(editProfile))
                .addContainerGap(30, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void changePasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changePasswordActionPerformed
        JPasswordField passwordField = new JPasswordField();
        Object[] message = {
            "Enter your current password:", passwordField
        };
        
        int option = JOptionPane.showConfirmDialog(this, message, "Confirm Password", 
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (option == JOptionPane.OK_OPTION) {
            String enteredPassword = new String(passwordField.getPassword());
            
            if (!enteredPassword.equals(currentPassword)) {
                JOptionPane.showMessageDialog(this, "Wrong password!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            boolean passwordChanged = false;
            while (!passwordChanged) {
                JPasswordField newPasswordField = new JPasswordField();
                JPasswordField confirmPasswordField = new JPasswordField();
                JLabel requirementsLabel = new JLabel("<html><b>Password Requirements:</b><br/>" +
                        "- Length: 7-14 characters<br/>" +
                        "- At least one lowercase letter (a-z)<br/>" +
                        "- At least one uppercase letter (A-Z)<br/>" +
                        "- At least one number (0-9)<br/>" +
                        "- At least one symbol (!@#$%^)</html>");
                
                Object[] newPasswordMessage = {
                    "Enter new password:", newPasswordField,
                    "Confirm new password:", confirmPasswordField,
                    " ",
                    requirementsLabel
                };
                
                int newPasswordOption = JOptionPane.showConfirmDialog(this, newPasswordMessage, 
                        "Change Password", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                
                if (newPasswordOption == JOptionPane.OK_OPTION) {
                    String newPassword = new String(newPasswordField.getPassword());
                    String confirmPassword = new String(confirmPasswordField.getPassword());
                    
                    if (!isValidPassword(newPassword)) {
                        JOptionPane.showMessageDialog(this, 
                                "<html>Invalid password format!<br/><br/>" +
                                "<b>Requirements:</b><br/>" +
                                "- Length: 7-14 characters<br/>" +
                                "- At least one lowercase letter (a-z)<br/>" +
                                "- At least one uppercase letter (A-Z)<br/>" +
                                "- At least one number (0-9)<br/>" +
                                "- At least one symbol (!@#$%^)</html>", 
                                "Failed", JOptionPane.ERROR_MESSAGE);
                        continue;
                    }
                    
                    if (!newPassword.equals(confirmPassword)) {
                        JOptionPane.showMessageDialog(this, 
                                "Passwords do not match!", 
                                "Failed", JOptionPane.ERROR_MESSAGE);
                        continue;
                    }
                    
                    if (updatePassword(newPassword)) {
                        JOptionPane.showMessageDialog(this, 
                                "Password changed successfully!", 
                                "Success", JOptionPane.INFORMATION_MESSAGE);
                        passwordChanged = true;
                    } else {
                        JOptionPane.showMessageDialog(this, 
                                "Error updating password. Please try again.", 
                                "Error", JOptionPane.ERROR_MESSAGE);
                        passwordChanged = true;
                    }
                } else {
                    break; 
                }
            }
        }
    }//GEN-LAST:event_changePasswordActionPerformed

    private void editProfileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editProfileActionPerformed
        String currentName = NameLabel.getText();
        String newName = JOptionPane.showInputDialog(this, "Enter new name:", currentName);
        
        if (newName != null && !newName.trim().isEmpty()) {
            if (!isValidName(newName)) {
                JOptionPane.showMessageDialog(this, 
                        "Invalid name! Name can only contain alphabets and spaces.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (updateName(newName.trim())) {
                NameLabel.setText(newName.trim());
                JOptionPane.showMessageDialog(this, 
                        "Name updated successfully!", 
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, 
                        "Error updating name. Please try again.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_editProfileActionPerformed

    private void exitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitActionPerformed
        // TODO add your handling code here:
        this.dispose();
        if (parentFrame != null) {
            parentFrame.setVisible(true);
        }
    }//GEN-LAST:event_exitActionPerformed

    private void editProfilePictureActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editProfilePictureActionPerformed
        // TODO add your handling code here:
                if (loggedInUserID == null || loggedInUserID.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "User ID not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Images", "jpg", "jpeg", "png", "gif"));
        int result = fileChooser.showOpenDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }

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
            loadProfilePicture();
            JOptionPane.showMessageDialog(this, "Profile picture updated.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            logger.log(java.util.logging.Level.SEVERE, "Error updating profile picture", e);
            JOptionPane.showMessageDialog(this, "Error updating profile picture.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_editProfilePictureActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        java.awt.EventQueue.invokeLater(() -> new editProfileL().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel EmailLabel;
    private javax.swing.JLabel IDLabel;
    private javax.swing.JLabel NameLabel;
    private javax.swing.JButton changePassword;
    private javax.swing.JButton editProfile;
    private javax.swing.JButton editProfilePicture;
    private javax.swing.JButton exit;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTextField1;
    private java.awt.Panel profilePicture;
    // End of variables declaration//GEN-END:variables
}
