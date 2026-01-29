/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package oopwj.AdministrativeStaff;

import java.awt.HeadlessException;
import java.io.*;
import java.util.*;
import java.util.regex.*;
import javax.swing.*;
import javax.swing.table.*;

/**
 *
 * @author kwany
 */
public class ManageUserPage extends javax.swing.JFrame {

    private TableRowSorter<DefaultTableModel> sorter;
    private final DefaultTableModel model;
    

    public ManageUserPage() {
        initComponents();
        setLocationRelativeTo(null);
        
        model = (DefaultTableModel) UserList.getModel();
        sorter = new TableRowSorter<>(model);
        UserList.setRowSorter(sorter);

        txtSearch.getDocument().addDocumentListener(
            new javax.swing.event.DocumentListener() {

                @Override
                public void insertUpdate(javax.swing.event.DocumentEvent e) {
                    search();
                }

                @Override
                public void removeUpdate(javax.swing.event.DocumentEvent e) {
                    search();
                }

                @Override
                public void changedUpdate(javax.swing.event.DocumentEvent e) {
                    search();
                }

                private void search() {
                    String text = txtSearch.getText().trim();

                    if (text.length() == 0) {
                        sorter.setRowFilter(null);
                    } else {
                        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(text)));
                    }
                }
            }
        );
        
        chkStudent.addActionListener(e -> applyFilters());
        chkAdmin.addActionListener(e -> applyFilters());
        chkLecturer.addActionListener(e -> applyFilters());
        chkAcademic.addActionListener(e -> applyFilters());

    }

public void loadUsers() {
    DefaultTableModel table = (DefaultTableModel) UserList.getModel();
    table.setRowCount(0);

    loadFromFile("src\\main\\java\\oopwj\\student.txt", "Student", table);
    loadFromFile("src\\main\\java\\oopwj\\admin.txt", "Admin", table);
    loadFromFile("src\\main\\java\\oopwj\\lecturer.txt", "Lecturer", table);
    loadFromFile("src\\main\\java\\oopwj\\academicLeader.txt", "Academic Leader", table);
}

private void loadFromFile(String fileName, String role, DefaultTableModel model) {
    File file = new File(fileName);
    if (!file.exists()) return;

    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] data = line.split(",");

            Object[] row = {
                data[1],
                data[0],
                data[2],
                data[3],
                role
            };
            model.addRow(row);
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this,
                "Failed to load " + role + " users.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }
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



private void applyFilters() {
    String searchText = txtSearch.getText().trim();

    List<String> selectedRoles = new ArrayList<>();
    if (chkStudent.isSelected()) selectedRoles.add("Student");
    if (chkAdmin.isSelected()) selectedRoles.add("Admin");
    if (chkLecturer.isSelected()) selectedRoles.add("Lecturer");
    if (chkAcademic.isSelected()) selectedRoles.add("Academic Leader");

    RowFilter<DefaultTableModel, Object> roleFilter;
    if (selectedRoles.isEmpty()) {
        roleFilter = RowFilter.regexFilter("NothingMatches", 4);
    } else {
        List<RowFilter<Object,Object>> roleFilters = new ArrayList<>();
        for (String role : selectedRoles) {
            roleFilters.add(RowFilter.regexFilter("^\\s*" + Pattern.quote(role) + "\\s*$", 4));
        }
        roleFilter = RowFilter.orFilter(roleFilters);
    }

    RowFilter<DefaultTableModel, Object> searchFilter = null;
    if (!searchText.isEmpty()) {
        List<RowFilter<Object,Object>> searchFilters = new ArrayList<>();
        searchFilters.add(RowFilter.regexFilter("(?i)" + Pattern.quote(searchText), 0));
        searchFilters.add(RowFilter.regexFilter("(?i)" + Pattern.quote(searchText), 1));
        searchFilter = RowFilter.orFilter(searchFilters);
    }

    if (searchFilter != null) {
        sorter.setRowFilter(RowFilter.andFilter(Arrays.asList(roleFilter, searchFilter)));
    } else {
        sorter.setRowFilter(roleFilter);
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

        btnBack = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        UserList = new javax.swing.JTable();
        btnCreate = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnRefresh = new javax.swing.JButton();
        txtSearch = new javax.swing.JTextField();
        chkStudent = new javax.swing.JCheckBox();
        chkAdmin = new javax.swing.JCheckBox();
        chkLecturer = new javax.swing.JCheckBox();
        chkAcademic = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Manage User");

        btnBack.setText("Back");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        UserList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Username", "User ID", "Password", "Email", "Role"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        UserList.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        UserList.setFillsViewportHeight(true);
        UserList.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(UserList);
        UserList.getAccessibleContext().setAccessibleName("");

        btnCreate.setText("Create");
        btnCreate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreateActionPerformed(evt);
            }
        });

        btnUpdate.setText("Update");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        btnDelete.setText("Delete");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnRefresh.setText("Refresh");
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });

        chkStudent.setSelected(true);
        chkStudent.setText("Student");
        chkStudent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkStudentActionPerformed(evt);
            }
        });

        chkAdmin.setSelected(true);
        chkAdmin.setText("Admin");

        chkLecturer.setSelected(true);
        chkLecturer.setText("Lecturer");

        chkAcademic.setSelected(true);
        chkAcademic.setText("Academic Leader");

        jLabel1.setText("Search:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnBack)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(chkStudent))
                            .addComponent(chkLecturer))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(chkAdmin)
                                .addGap(48, 48, 48)
                                .addComponent(btnRefresh))
                            .addComponent(chkAcademic)))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(btnCreate)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnUpdate)
                            .addGap(126, 126, 126)
                            .addComponent(btnDelete))
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 473, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(43, 43, 43))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBack)
                    .addComponent(btnRefresh)
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chkStudent)
                    .addComponent(chkAdmin)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chkLecturer)
                    .addComponent(chkAcademic))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 19, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCreate)
                    .addComponent(btnUpdate)
                    .addComponent(btnDelete))
                .addGap(29, 29, 29))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        new AdminMainPage().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnBackActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        loadUsers();
        applyFilters();
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void chkStudentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkStudentActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkStudentActionPerformed

    private void btnCreateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateActionPerformed
        CreateUserPage createUser = new CreateUserPage(this, true);
        createUser.setVisible(true);
        loadUsers();
        applyFilters();
    }//GEN-LAST:event_btnCreateActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        int selectedRow = UserList.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user to delete.", 
                                          "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = UserList.convertRowIndexToModel(selectedRow);
        DefaultTableModel list = (DefaultTableModel) UserList.getModel();

        String username = (String) list.getValueAt(modelRow, 0);
        String userId = (String) list.getValueAt(modelRow, 1);
        String role = (String) list.getValueAt(modelRow, 4);
        
        String fileName = getFileByRole(role);

        int option = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to delete this user?\n\nUsername: " + username +
                "\nUser ID: " + userId + "\nRole: " + role, 
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (option == JOptionPane.YES_OPTION) {
            try {
                File inputFile = new File(fileName);
                File tempFile = new File("users_temp.txt");

                try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                     BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split(",");
                        if (!parts[0].equals(userId) || !parts[1].equals(username)) {
                            writer.write(line);
                            writer.newLine();
                        }
                    }
                }

                if (!inputFile.delete() || !tempFile.renameTo(inputFile)) {
                    throw new IOException("Failed to delete user from file.");
                }

                loadUsers();

                JOptionPane.showMessageDialog(this, "User deleted successfully.", 
                                              "Deleted", JOptionPane.INFORMATION_MESSAGE);

            } catch (HeadlessException | IOException ex) {
                JOptionPane.showMessageDialog(this, "Error deleting user: " + ex.getMessage(), 
                                              "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        int selectedRow = UserList.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user to update.", 
                                          "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = UserList.convertRowIndexToModel(selectedRow);
        DefaultTableModel list = (DefaultTableModel) UserList.getModel();

        String username = (String) list.getValueAt(modelRow, 0);
        String userId   = (String) list.getValueAt(modelRow, 1);
        String password = (String) list.getValueAt(modelRow, 2);
        String email    = (String) list.getValueAt(modelRow, 3);
        String role     = (String) list.getValueAt(modelRow, 4);

        UpdateUserPage updateDialog = new UpdateUserPage(this, true, username, userId, password, email, role);
        updateDialog.setVisible(true);

        loadUsers();
    }//GEN-LAST:event_btnUpdateActionPerformed

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
            java.util.logging.Logger.getLogger(ManageUserPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new ManageUserPage().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable UserList;
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnCreate;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JCheckBox chkAcademic;
    private javax.swing.JCheckBox chkAdmin;
    private javax.swing.JCheckBox chkLecturer;
    private javax.swing.JCheckBox chkStudent;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
}
