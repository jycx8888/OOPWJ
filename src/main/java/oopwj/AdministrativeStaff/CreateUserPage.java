/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package oopwj.AdministrativeStaff;

import java.awt.*;
import javax.swing.*;
import java.io.*;

/**
 *
 * @author kwany
 */
public class CreateUserPage extends javax.swing.JDialog {

    /**
     * Creates new form CreateUserPage
     * @param parent
     * @param modal
     */
    private static final char Bullet = '\u2022';
    private static final Color Placeholder_colour = Color.GRAY;
    private static final Color Text_colour = Color.BLACK;
    
    public CreateUserPage(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        txtEmail.setEditable(false);
        txtEmail.setFocusable(false);
        setLocationRelativeTo(parent);
        
        ButtonGroup roleGroup = new ButtonGroup();
        roleGroup.add(radioStudent);
        roleGroup.add(radioLecturer);
        roleGroup.add(radioAcademic);
        roleGroup.add(radioAdmin);
        
        PlaceHolder.apply(txtUsername, "Enter Username");
        PlaceHolder.apply(txtId, "Enter User ID");
        PlaceHolder.apply(txtPassword, "Enter Password");

        chkPassword.addActionListener(e -> {
            if (chkPassword.isSelected()) {
                txtPassword.setEchoChar((char) 0);
            } else {
                if (!txtPassword.getText().equals("Enter Password")){
                    txtPassword.setEchoChar(Bullet); 
                }
            }
        });
        
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
        
    }

    private String getFileByRole(String role) {
    switch (role) {
        case "Student" -> {
            return "src\\main\\java\\oopwj\\student.txt";
            }
        case "Admin" -> {
            return "src\\main\\java\\oopwj\\admin.txt";
            }
        case "Lecturer" -> {
            return "src\\main\\java\\oopwj\\lecturer.txt";
            }
        case "Academic Leader" -> {
            return "src\\main\\java\\oopwj\\academicLeader.txt";
            }
        default -> throw new IllegalArgumentException("Unknown role");
    }
}
    private boolean idExists(String userId) {
    String[] files = {
        "src\\main\\java\\oopwj\\student.txt",
        "src\\main\\java\\oopwj\\lecturer.txt",
        "src\\main\\java\\oopwj\\academicLeader.txt",
        "src\\main\\java\\oopwj\\admin.txt"
    };

    for (String file : files) {
        File f = new File(file);
        if (!f.exists()) continue;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length > 0 && parts[0].trim().equals(userId)) {
                    return true;
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                "Error reading file: " + file,
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    return false;
}
    
    private void updateEmailField() {
    String userId = txtId.getText().trim();

    if (!userId.isEmpty() && !userId.equals("Enter User ID")) {
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


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        radioStudent = new javax.swing.JRadioButton();
        txtUsername = new javax.swing.JTextField();
        txtId = new javax.swing.JTextField();
        txtPassword = new javax.swing.JPasswordField();
        txtEmail = new javax.swing.JTextField();
        radioLecturer = new javax.swing.JRadioButton();
        radioAcademic = new javax.swing.JRadioButton();
        radioAdmin = new javax.swing.JRadioButton();
        btnSave = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        chkPassword = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Create User");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("Username:");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setText("User ID:");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setText("Password:");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setText("Email:");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setText("Role:");

        radioStudent.setText("Student");

        radioLecturer.setText("Lecturer");

        radioAcademic.setText("Academic Leader");

        radioAdmin.setText("Admin");

        btnSave.setText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        chkPassword.setText("Show Password");
        chkPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkPasswordActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(btnSave))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(radioAdmin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(radioAcademic, javax.swing.GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE)
                    .addComponent(radioLecturer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(radioStudent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtEmail)
                    .addComponent(txtPassword)
                    .addComponent(txtId)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnCancel))
                    .addComponent(txtUsername))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chkPassword)
                .addGap(14, 14, 14))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chkPassword))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(radioStudent))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(radioLecturer)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(radioAcademic)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(radioAdmin)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSave)
                    .addComponent(btnCancel))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void chkPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkPasswordActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkPasswordActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        boolean isValid;
        String username = txtUsername.getText().trim();
        String userId = txtId.getText().trim();
        String password = String.valueOf(txtPassword.getPassword()).trim();
        String email = txtEmail.getText().trim();

        String role = "";
        if (radioStudent.isSelected()) role = "Student";
        else if (radioLecturer.isSelected()) role = "Lecturer";
        else if (radioAcademic.isSelected()) role = "Academic Leader";
        else if (radioAdmin.isSelected()) role = "Admin";
        

        if (username.isEmpty() || username.equals("Enter Username") ||
            userId.isEmpty() || userId.equals("Enter User ID") ||
            password.isEmpty() || password.equals("Enter Password") ||
            role.isEmpty()) 
        {
            JOptionPane.showMessageDialog(this, "Please fill in all fields and select a role.", 
                                          "Error", JOptionPane.ERROR_MESSAGE);
            return; 
        }

        if (!isValidPassword(password)) {
            showPasswordValidationMessage();
            return;
        }
        
        if (idExists(userId)) {
            JOptionPane.showMessageDialog(this,
                "User ID already exists. Please use a different ID.",
                "Duplicate ID",
                JOptionPane.ERROR_MESSAGE);
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
        
        String fileName = getFileByRole(role);

        try (FileWriter fw = new FileWriter(fileName, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw)) {
            
            if ("Lecturer".equals(role)) {
                out.println(userId + "," + username + "," + password + "," + email + "," + "None");
            } else {
                out.println(userId + "," + username + "," + password + "," + email);
            }

            JOptionPane.showMessageDialog(this, "User saved successfully!", 
                                          "Success", JOptionPane.INFORMATION_MESSAGE);
        
        this.dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error saving user: " + ex.getMessage(), 
                                          "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        boolean hasChanges = false;

        if (!txtUsername.getText().equals("Enter Username") && !txtUsername.getText().isEmpty()) hasChanges = true;
        if (!txtId.getText().equals("Enter User ID") && !txtId.getText().isEmpty()) hasChanges = true;
        if (!String.valueOf(txtPassword.getPassword()).equals("Enter Password") && txtPassword.getPassword().length > 0) hasChanges = true;
        if (!txtEmail.getText().equals("Enter Email") && !txtEmail.getText().isEmpty()) hasChanges = true;

        if (radioStudent.isSelected() || radioLecturer.isSelected() || radioAcademic.isSelected() || radioAdmin.isSelected()) {
            hasChanges = true;
        }

        if (hasChanges) {
            int option = JOptionPane.showConfirmDialog(this,
                    "You have unsaved changes. Are you sure you want to cancel?",
                    "Confirm Cancel",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (option == JOptionPane.YES_OPTION) {
                this.dispose();
            }
        } else {
            this.dispose();
        }
    }//GEN-LAST:event_btnCancelActionPerformed

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
            java.util.logging.Logger.getLogger(CreateUserPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(() -> {
            CreateUserPage dialog = new CreateUserPage(new javax.swing.JFrame(), true);
            dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    System.exit(0);
                }
            });
            dialog.setVisible(true);
        });
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
