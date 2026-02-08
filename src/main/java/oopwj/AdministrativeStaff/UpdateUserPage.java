/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package oopwj.AdministrativeStaff;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/**
 *
 * @author kwany
 */
public class UpdateUserPage extends javax.swing.JDialog {

    /**
     * Creates new form UpdateUserPage
     * @param parent
     * @param modal
     * @param username
     * @param userId
     * @param password
     * @param email
     * @param role
     */
    
    private final String originalUsername;
    private final String originalId;
    private final String originalRole;
    
    public UpdateUserPage(java.awt.Frame parent, boolean modal, String username, String userId, String password, String email, String role) {
        super(parent, modal);
        initComponents();
        setSize(448, 347);
        setLocationRelativeTo(null);
        txtEmail.setEditable(false);
        txtEmail.setFocusable(false);
        
        txtUsername.setText(username);
        txtId.setText(userId);
        txtPassword.setText(password);
        txtEmail.setText(email);
        
        
        switch (role) {
            case "Student" -> radioStudent.setSelected(true);
            case "Lecturer" -> radioLecturer.setSelected(true);
            case "Academic Leader" -> radioAcademic.setSelected(true);
            case "Admin" -> radioAdmin.setSelected(true);
        }
        
        ButtonGroup roleGroup = new ButtonGroup();
        roleGroup.add(radioStudent);
        roleGroup.add(radioLecturer);
        roleGroup.add(radioAcademic);
        roleGroup.add(radioAdmin);
        
        char Bullet = '\u2022';
        chkPassword.addActionListener(e -> {
            if (chkPassword.isSelected()) txtPassword.setEchoChar((char) 0);
            else txtPassword.setEchoChar(Bullet);
        });
        
        originalUsername = username;
        originalId = userId;
        originalRole = role;
        
        txtId.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                updateEmailField();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                updateEmailField();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                updateEmailField();
            }
        });
        
        addHoverEffect(btnSave, new Color(40, 167, 69), new Color(33, 136, 56));
        addHoverEffect(btnCancel, new Color(108, 117, 125), new Color(90, 98, 104));
    }
    
    private String getFileByRole(String role) {
    switch (role) {
        case "Student" -> {
            return "src\\main\\java\\oopwj\\Data\\student.txt";
            }
        case "Admin" -> {
            return "src\\main\\java\\oopwj\\Data\\admin.txt";
            }
        case "Lecturer" -> {
            return "src\\main\\java\\oopwj\\Data\\lecturer.txt";
            }
        case "Academic Leader" -> {
            return "src\\main\\java\\oopwj\\Data\\academicLeader.txt";
            }
        default -> throw new IllegalArgumentException("Unknown role");
    }
}
    
    private void updateEmailField() {
    String userId = txtId.getText().trim();

    if (!userId.isEmpty()) {
        txtEmail.setText(userId + "@mail.apu.edu.my");
    } else {
        txtEmail.setText("");
    }
}

    /**
     * Validate password format based on editProfileAC
     * Requirements:
     * - Length: 7-14 characters
     * - At least one lowercase letter
     * - At least one uppercase letter
     * - At least one number
     * - At least one symbol (!@#$%^)
     * - Comma is not allowed
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
                return false;
            }
        }

        return hasLowerCase && hasUpperCase && hasDigit && hasSymbol;
    }

    private void showPasswordValidationMessage() {
        JOptionPane.showMessageDialog(this,
            "Invalid password format!\n\n"
            + "Requirements:\n"
            + "- 7 to 14 characters\n"
            + "- At least 1 lowercase letter\n"
            + "- At least 1 uppercase letter\n"
            + "- At least 1 number\n"
            + "- At least 1 symbol (!@#$%^)\n"
            + "- Comma is not allowed",
            "Validation Error",
            JOptionPane.ERROR_MESSAGE);
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
    
    private void removeUserFromOldFile() throws IOException {
        String oldFile = getFileByRole(originalRole);
        File inputFile = new File(oldFile);
        File tempFile = new File("users_temp.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (!(parts[0].equals(originalId) && parts[1].equals(originalUsername))) {
                    writer.write(line);
                    writer.newLine();
                }
            }
        }

        inputFile.delete();
        tempFile.renameTo(inputFile);
    }

    private void addUserToNewFile(String username, String userId,String password, String email, String role)
        throws IOException {

    String newFile = getFileByRole(role);

    try (PrintWriter out = new PrintWriter(new BufferedWriter(
            new FileWriter(newFile, true)))) {

        if ("Lecturer".equals(role)) {
            out.println(userId + "," + username + "," + password + "," + email + ",None");
        } else {
            out.println(userId + "," + username + "," + password + "," + email);
        }
    }
}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                int w = getWidth();
                int h = getHeight();
                Color color1 = new Color(245, 248, 252);
                Color color2 = new Color(255, 255, 255);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        btnCancel = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        chkPassword = new javax.swing.JCheckBox();
        txtUsername = new javax.swing.JTextField();
        txtId = new javax.swing.JTextField();
        txtPassword = new javax.swing.JPasswordField();
        txtEmail = new javax.swing.JTextField();
        radioStudent = new javax.swing.JRadioButton();
        radioLecturer = new javax.swing.JRadioButton();
        jLabel1 = new javax.swing.JLabel();
        radioAcademic = new javax.swing.JRadioButton();
        jLabel2 = new javax.swing.JLabel();
        radioAdmin = new javax.swing.JRadioButton();
        jLabel3 = new javax.swing.JLabel();
        btnSave = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Update User Page");

        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setText("Role:");

        chkPassword.setText("Show Password");
        chkPassword.setContentAreaFilled(false);

        radioStudent.setText("Student");
        radioStudent.setContentAreaFilled(false);

        radioLecturer.setText("Lecturer");
        radioLecturer.setContentAreaFilled(false);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("Username:");

        radioAcademic.setText("Academic Leader");
        radioAcademic.setContentAreaFilled(false);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setText("User ID:");

        radioAdmin.setText("Admin");
        radioAdmin.setContentAreaFilled(false);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setText("Password:");

        btnSave.setText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setText("Email:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(22, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnSave)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(radioAdmin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(radioAcademic, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(radioLecturer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtUsername)
                        .addComponent(txtId)
                        .addComponent(txtPassword)
                        .addComponent(txtEmail)
                        .addComponent(radioStudent, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnCancel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chkPassword)
                .addGap(16, 16, 16))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chkPassword))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(radioStudent))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(radioLecturer)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(radioAcademic)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(radioAdmin)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSave)
                    .addComponent(btnCancel))
                .addContainerGap(29, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        boolean isValid;
        String username = txtUsername.getText().trim();
        String userId = txtId.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();
        String email = txtEmail.getText().trim();

        String role = "";
        if (radioStudent.isSelected()) role = "Student";
        else if (radioLecturer.isSelected()) role = "Lecturer";
        else if (radioAcademic.isSelected()) role = "Academic Leader";
        else if (radioAdmin.isSelected()) role = "Admin";

        
        if (username.isEmpty() || userId.isEmpty() || password.isEmpty() || email.isEmpty() || role.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields and select a role.", 
                                          "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!isValidPassword(password)) {
            showPasswordValidationMessage();
            return;
        }

        switch (role) {
            case "Student" -> isValid = userId.matches("^S\\d{3}$");
            case "Lecturer" -> isValid = userId.matches("^L\\d{3}$");
            case "Academic Leader" -> isValid = userId.matches("^AC\\d{3}$");
            case "Admin" -> isValid = userId.matches("^A\\d{3}$");
            default -> isValid = false;
        }

        if (!isValid) {
            JOptionPane.showMessageDialog(this,
                "Invalid User ID format!\n\n"
                + "Student: S001\n"
                + "Lecturer: L001\n"
                + "Academic Leader: AC001\n"
                + "Admin: A001",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        
        boolean roleChanged = !role.equals(originalRole);
        
        if (roleChanged) {
            try {
                removeUserFromOldFile();
                addUserToNewFile(username, userId, password, email, role);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error updating user: " + ex.getMessage(), 
                                          "Error", JOptionPane.ERROR_MESSAGE);
            }
            JOptionPane.showMessageDialog(this,
                "User updated successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
            return;
        }
        
        String fileName = getFileByRole(role);

        try {
            File inputFile = new File(fileName);
            File tempFile = new File("users_temp.txt");

            try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                 BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts[1].equals(originalUsername) && parts[0].equals(originalId)) {
                        if (role.equals("Lecturer") && parts.length >= 5) {
                            writer.write(userId + "," + username + "," + password + "," + email + "," + parts[4]);
                        } else {
                            writer.write(userId + "," + username + "," + password + "," + email);
                        }
                    } else {
                        writer.write(line);
                    }
                    writer.newLine();
                }
            }

            if (!inputFile.delete() || !tempFile.renameTo(inputFile)) {
                throw new IOException("Failed to update user in file.");
            }

            JOptionPane.showMessageDialog(this, "User updated successfully!", 
                                          "Success", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();

        } catch (HeadlessException | IOException ex) {
            JOptionPane.showMessageDialog(this, "Error updating user: " + ex.getMessage(), 
                                          "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnSaveActionPerformed

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
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(UpdateUserPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form - Must login first */
        java.awt.EventQueue.invokeLater(() -> new oopwj.LoginFrame());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnSave;
    private javax.swing.JCheckBox chkPassword;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JRadioButton radioAcademic;
    private javax.swing.JRadioButton radioAdmin;
    private javax.swing.JRadioButton radioLecturer;
    private javax.swing.JRadioButton radioStudent;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtId;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtUsername;
    // End of variables declaration//GEN-END:variables
}
