/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package oopwj.AcademicLeader;

import java.io.*;
import java.util.*;
import javax.swing.*;

/**
 *
 * @author Justin Yong
 */
public class editProfileAC extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(editProfileAC.class.getName());
    private String loggedInUserID;
    private javax.swing.JFrame parentFrame;
    private String currentPassword;

    /**
     * Creates new form editProfileAC
     */
    public editProfileAC() {
        initComponents();
    }
    
    /**
     * Constructor with user session
     * @param userID - The logged-in user's ID
     * @param parent - The parent frame
     */
    public editProfileAC(String userID, javax.swing.JFrame parent) {
        this.loggedInUserID = userID;
        this.parentFrame = parent;
        initComponents();
        loadUserData();
    }
    
    /**
     * Load user data from academicLeader.txt
     */
    private void loadUserData() {
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/java/oopwj/Data/academicLeader.txt"))) {
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
    
    /**
     * Validate password format
     * Requirements:
     * - Length: 7-14 characters
     * - At least one lowercase letter
     * - At least one uppercase letter
     * - At least one number
     * - At least one symbol (!@#$%^)
     * @param password - The password to validate
     * @return true if valid, false otherwise
     */
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
                return false; // Comma not allowed
            }
        }
        
        return hasLowerCase && hasUpperCase && hasDigit && hasSymbol;
    }
    
    /**
     * Validate name format
     * @param name - The name to validate
     * @return true if valid (only alphabets and spaces), false otherwise
     */
    private boolean isValidName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        // Only allow alphabets and spaces
        return name.matches("^[a-zA-Z ]+$");
    }
    
    /**
     * Update password in academicLeader.txt
     * @param newPassword - The new password
     * @return true if successful, false otherwise
     */
    private boolean updatePassword(String newPassword) {
        List<String> lines = new ArrayList<>();
        boolean updated = false;
        
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/java/oopwj/Data/academicLeader.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4 && parts[0].equals(loggedInUserID)) {
                    lines.add(parts[0] + "," + parts[1] + "," + newPassword + "," + parts[3]);
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
            try (BufferedWriter bw = new BufferedWriter(new FileWriter("src/main/java/oopwj/Data/academicLeader.txt"))) {
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
    
    /**
     * Update name in academicLeader.txt
     * @param newName - The new name
     * @return true if successful, false otherwise
     */
    private boolean updateName(String newName) {
        List<String> lines = new ArrayList<>();
        boolean updated = false;
        
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/java/oopwj/Data/academicLeader.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4 && parts[0].equals(loggedInUserID)) {
                    lines.add(parts[0] + "," + newName + "," + parts[2] + "," + parts[3]);
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
            try (BufferedWriter bw = new BufferedWriter(new FileWriter("src/main/java/oopwj/Data/academicLeader.txt"))) {
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
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        changePassword = new javax.swing.JButton();
        editProfile = new javax.swing.JButton();
        Exit = new javax.swing.JToggleButton();
        IDLabel = new javax.swing.JLabel();
        NameLabel = new javax.swing.JLabel();
        EmailLabel = new javax.swing.JLabel();

        jTextField1.setText("jTextField1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("Profile");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("Name:");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("Email:");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setText("ID:");

        changePassword.setText("Change Password");
        changePassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changePasswordActionPerformed(evt);
            }
        });

        editProfile.setText("Edit Profile");
        editProfile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editProfileActionPerformed(evt);
            }
        });

        Exit.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        Exit.setForeground(new java.awt.Color(255, 51, 102));
        Exit.setText("X");
        Exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ExitActionPerformed(evt);
            }
        });

        IDLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        IDLabel.setText("jLabel5");

        NameLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        NameLabel.setText("jLabel5");

        EmailLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        EmailLabel.setText("jLabel5");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(150, 150, 150)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(changePassword)
                        .addGap(38, 38, 38)
                        .addComponent(editProfile))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 51, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(IDLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE)
                            .addComponent(NameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(EmailLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(186, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(239, 239, 239)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Exit)
                .addGap(32, 32, 32))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(Exit)))
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(IDLabel))
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(NameLabel))
                .addGap(35, 35, 35)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(EmailLabel))
                .addGap(36, 36, 36)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(changePassword)
                    .addComponent(editProfile))
                .addContainerGap(162, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExitActionPerformed
        this.dispose();
        if (parentFrame != null) {
            parentFrame.setVisible(true);
        }
    }//GEN-LAST:event_ExitActionPerformed

    private void changePasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changePasswordActionPerformed
        // Step 1: Confirm current password
        JPasswordField passwordField = new JPasswordField();
        Object[] message = {
            "Enter your current password:", passwordField
        };
        
        int option = JOptionPane.showConfirmDialog(this, message, "Confirm Password", 
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (option == JOptionPane.OK_OPTION) {
            String enteredPassword = new String(passwordField.getPassword());
            
            // Verify current password
            if (!enteredPassword.equals(currentPassword)) {
                JOptionPane.showMessageDialog(this, "Wrong password!", "Error", JOptionPane.ERROR_MESSAGE);
                return; // Return to allow re-entry
            }
            
            // Step 2: Enter new password
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
                    
                    // Validate password format
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
                        continue; // Loop again for re-entry
                    }
                    
                    // Check if passwords match
                    if (!newPassword.equals(confirmPassword)) {
                        JOptionPane.showMessageDialog(this, 
                                "Passwords do not match!", 
                                "Failed", JOptionPane.ERROR_MESSAGE);
                        continue; // Loop again for re-entry
                    }
                    
                    // Update password
                    if (updatePassword(newPassword)) {
                        JOptionPane.showMessageDialog(this, 
                                "Password changed successfully!", 
                                "Success", JOptionPane.INFORMATION_MESSAGE);
                        passwordChanged = true;
                    } else {
                        JOptionPane.showMessageDialog(this, 
                                "Error updating password. Please try again.", 
                                "Error", JOptionPane.ERROR_MESSAGE);
                        passwordChanged = true; // Exit to prevent infinite loop
                    }
                } else {
                    break; // User cancelled
                }
            }
        }
    }//GEN-LAST:event_changePasswordActionPerformed

    private void editProfileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editProfileActionPerformed
        // Show popup to change name
        String currentName = NameLabel.getText();
        String newName = JOptionPane.showInputDialog(this, "Enter new name:", currentName);
        
        if (newName != null && !newName.trim().isEmpty()) {
            // Validate name (only alphabets and spaces)
            if (!isValidName(newName)) {
                JOptionPane.showMessageDialog(this, 
                        "Invalid name! Name can only contain alphabets and spaces.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Update name in file
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

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new editProfileAC().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel EmailLabel;
    private javax.swing.JToggleButton Exit;
    private javax.swing.JLabel IDLabel;
    private javax.swing.JLabel NameLabel;
    private javax.swing.JButton changePassword;
    private javax.swing.JButton editProfile;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
