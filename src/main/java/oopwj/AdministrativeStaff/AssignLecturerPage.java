/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package oopwj.AdministrativeStaff;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.*;
import javax.swing.*;
import static javax.swing.SwingConstants.CENTER;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author kwany
 */
public class AssignLecturerPage extends javax.swing.JFrame {

    private final TableRowSorter<DefaultTableModel> lecturerSorter;
    private final TableRowSorter<DefaultTableModel> academicSorter;
    private final String loggedInUserID;

    
    public AssignLecturerPage() {
        this.loggedInUserID = null;
        initComponents();
        setLocationRelativeTo(null);
        
        loadUsers(LecturerList,"src\\main\\java\\oopwj\\Data\\lecturer.txt", "Lecturer");
        loadUsers(AcademicList,"src\\main\\java\\oopwj\\Data\\academicLeader.txt", "Academic Leader");
        
        lecturerSorter = new TableRowSorter<>(
            (DefaultTableModel) LecturerList.getModel());
        LecturerList.setRowSorter(lecturerSorter);

        academicSorter = new TableRowSorter<>(
                (DefaultTableModel) AcademicList.getModel());
        AcademicList.setRowSorter(academicSorter);

        setupSearch();
        addHoverEffect(btnAssign,new Color(70,130,180),new Color(100,149,237));
        addHoverEffect(btnUpdate,new Color(70,130,180),new Color(100,149,237));
        addHoverEffect(btnBack,new Color(200,200,200),new Color(170,170,170));
        
        styleTables(LecturerList);
        styleTables(AcademicList);
    }

    public AssignLecturerPage(String userID) {
        this.loggedInUserID = userID;
        initComponents();
        setLocationRelativeTo(null);

        loadUsers(LecturerList,"src\\main\\java\\oopwj\\Data\\lecturer.txt", "Lecturer");
        loadUsers(AcademicList,"src\\main\\java\\oopwj\\Data\\academicLeader.txt", "Academic Leader");

        lecturerSorter = new TableRowSorter<>(
            (DefaultTableModel) LecturerList.getModel());
        LecturerList.setRowSorter(lecturerSorter);

        academicSorter = new TableRowSorter<>(
                (DefaultTableModel) AcademicList.getModel());
        AcademicList.setRowSorter(academicSorter);

        setupSearch();
        addHoverEffect(btnAssign,new Color(70,130,180),new Color(100,149,237));
        addHoverEffect(btnUpdate,new Color(70,130,180),new Color(100,149,237));
        addHoverEffect(btnBack,new Color(200,200,200),new Color(170,170,170));
        
        styleTables(LecturerList);
        styleTables(AcademicList);
    }
    
    private void styleTables(JTable table) {
        JTableHeader header = table.getTableHeader();
        header.setFont(header.getFont().deriveFont(Font.BOLD));
        table.setRowHeight(22);

        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table,
                                                           Object value,
                                                           boolean isSelected,
                                                           boolean hasFocus,
                                                           int row,
                                                           int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBackground(new Color(230, 235, 245));
                setForeground(Color.BLACK);
                setHorizontalAlignment(CENTER);
                return this;
            }
        });
    }
    
    public static void loadUsers(JTable table, String file, String role) {
    DefaultTableModel list = (DefaultTableModel) table.getModel();
    list.setRowCount(0);

    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] values = line.split(",");
            
            if (file.equals("src\\main\\java\\oopwj\\Data\\academicLeader.txt")) {
                Object[] row = {
                    values[1].trim(),
                    values[0].trim(),
                    role
                };
                list.addRow(row);
            }

            else if (file.equals("src\\main\\java\\oopwj\\Data\\lecturer.txt")) {
                Object[] row = {
                    values[1].trim(),
                    values[0].trim(),
                    role,
                    values[4].trim()
                };
                list.addRow(row);
            }
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(null, "Failed to load users.", "Error", JOptionPane.ERROR_MESSAGE);
    }
}
    
    private void setupSearch() {

    // Search for Lecturer table
    searchLecturer.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
        @Override
        public void insertUpdate(javax.swing.event.DocumentEvent e) {
            filterLecturer();
        }

        @Override
        public void removeUpdate(javax.swing.event.DocumentEvent e) {
            filterLecturer();
        }

        @Override
        public void changedUpdate(javax.swing.event.DocumentEvent e) {
            filterLecturer();
        }
    });

    // Search for Academic Leader table
    searchAcademic.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
        @Override
        public void insertUpdate(javax.swing.event.DocumentEvent e) {
            filterAcademic();
        }

        @Override
        public void removeUpdate(javax.swing.event.DocumentEvent e) {
            filterAcademic();
        }

        @Override
        public void changedUpdate(javax.swing.event.DocumentEvent e) {
            filterAcademic();
        }
    });
}
    
    private void filterLecturer() {
    String text = searchLecturer.getText();

    if (text.trim().length() == 0) {
        lecturerSorter.setRowFilter(null);
    } else {
        lecturerSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
    }
}

    private void filterAcademic() {
        String text = searchAcademic.getText();

        if (text.trim().length() == 0) {
            academicSorter.setRowFilter(null);
        } else {
            academicSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
        }
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
                Graphics2D g2 = (Graphics2D) g;

                GradientPaint gp = new GradientPaint(
                    0, 0, new Color(245, 248, 252),
                    0, getHeight(), new Color(255, 255, 255)
                );

                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        btnBack = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        searchLecturer = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        searchAcademic = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        AcademicList = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        LecturerList = new javax.swing.JTable();
        btnUpdate = new javax.swing.JButton();
        btnAssign = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Assign Lecturer Page");

        btnBack.setText("Back");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        jLabel1.setText("Search:");

        jLabel2.setText("Search:");

        AcademicList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Username", "User ID", "Role"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(AcademicList);

        jLabel3.setText("Assign To ->");

        LecturerList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Username", "User ID", "Role", "Assigned Academic Leader ID"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        LecturerList.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(LecturerList);

        btnUpdate.setText("Update");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        btnAssign.setText("Assign");
        btnAssign.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAssignActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnBack)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 556, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(searchLecturer))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(24, 24, 24)
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(btnUpdate)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnAssign))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(searchAcademic, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addGap(36, 36, 36))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(9, Short.MAX_VALUE)
                .addComponent(btnBack)
                .addGap(19, 19, 19)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(searchLecturer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchAcademic, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 312, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 312, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(140, 140, 140)
                        .addComponent(jLabel3)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnAssign)
                    .addComponent(btnUpdate))
                .addGap(15, 15, 15))
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

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        this.dispose();
        new AdminMainPage(loggedInUserID).setVisible(true);
    }//GEN-LAST:event_btnBackActionPerformed

    private void btnAssignActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAssignActionPerformed
        int lecturerViewRow = LecturerList.getSelectedRow();
        int academicViewRow = AcademicList.getSelectedRow();
        
        if (lecturerViewRow == -1 || academicViewRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a Lecturer and Academic Leader to assign.", 
                                          "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int lecturerRow = LecturerList.convertRowIndexToModel(lecturerViewRow);
        int academicRow = AcademicList.convertRowIndexToModel(academicViewRow);

        DefaultTableModel lecturerModel = (DefaultTableModel) LecturerList.getModel();
        DefaultTableModel academicModel = (DefaultTableModel) AcademicList.getModel();

        String lecturerName = lecturerModel.getValueAt(lecturerRow, 0).toString();
        String lecturerId   = lecturerModel.getValueAt(lecturerRow, 1).toString();
        String assignedAL   = lecturerModel.getValueAt(lecturerRow, 3) == null
                                ? "" : lecturerModel.getValueAt(lecturerRow, 3).toString();

        String academicId   = academicModel.getValueAt(academicRow, 1).toString();

        if (!assignedAL.equals("None")) {
            JOptionPane.showMessageDialog(this,
                    "This lecturer is already assigned to an Academic Leader.",
                    "Already Assigned",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Assign Lecturer:\n\n" +
                "Lecturer: " + lecturerName + " (" + lecturerId + ")\n" +
                "Academic Leader ID: " + academicId,    
                "Confirm Assignment",
                JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        File inputFile = new File("src\\main\\java\\oopwj\\Data\\lecturer.txt");
        File tempFile  = new File("src\\main\\java\\oopwj\\Data\\lecturer_temp.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts[0].trim().equals(lecturerId)) {
                    writer.write(parts[0] + "," + parts[1] + "," + parts[2] + "," + parts[3] + "," + academicId);
                } else {
                    writer.write(line);
                }
                writer.newLine();
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Failed to assign lecturer.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!inputFile.delete() || !tempFile.renameTo(inputFile)) {
            JOptionPane.showMessageDialog(this,
                    "Failed to update lecturer file.",
                    "File Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        loadUsers(LecturerList, "src\\main\\java\\oopwj\\Data\\lecturer.txt", "Lecturer");
        AcademicList.clearSelection();

        JOptionPane.showMessageDialog(this,
                "Lecturer assigned successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
        
    }//GEN-LAST:event_btnAssignActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        int lecturerViewRow = LecturerList.getSelectedRow();
        int academicViewRow = AcademicList.getSelectedRow();

        if (lecturerViewRow == -1 || academicViewRow == -1) {
            JOptionPane.showMessageDialog(this, 
                    "Please select both a Lecturer and an Academic Leader to update.", 
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int lecturerRow = LecturerList.convertRowIndexToModel(lecturerViewRow);
        int academicRow = AcademicList.convertRowIndexToModel(academicViewRow);

        DefaultTableModel lecturerModel = (DefaultTableModel) LecturerList.getModel();
        DefaultTableModel academicModel = (DefaultTableModel) AcademicList.getModel();

        String lecturerName = lecturerModel.getValueAt(lecturerRow, 0).toString();
        String lecturerId   = lecturerModel.getValueAt(lecturerRow, 1).toString();
        String oldAL        = lecturerModel.getValueAt(lecturerRow, 3) == null 
                                ? "" : lecturerModel.getValueAt(lecturerRow, 3).toString();

        String newAL        = academicModel.getValueAt(academicRow, 1).toString();

        if (oldAL.equals("None")) {
            JOptionPane.showMessageDialog(this, 
                    "This lecturer is not yet assigned. Use 'Assign' button instead.", 
                    "Not Assigned", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Update Lecturer:\n\n" +
                "Lecturer: " + lecturerName + " (" + lecturerId + ")\n" +
                "Old Academic Leader ID: " + oldAL + "\n" +
                "New Academic Leader ID: " + newAL,
                "Confirm Update",
                JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) return;

        File inputFile = new File("src\\main\\java\\oopwj\\Data\\lecturer.txt");
        File tempFile  = new File("src\\main\\java\\oopwj\\Data\\lecturer_temp.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].trim().equals(lecturerId)) {
                    parts[4] = newAL; 
                }
                writer.write(String.join(",", parts));
                writer.newLine();
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Failed to update lecturer: " + ex.getMessage(),
                                          "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!inputFile.delete() || !tempFile.renameTo(inputFile)) {
            JOptionPane.showMessageDialog(this, "Failed to update lecturer file.", "File Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        loadUsers(LecturerList, "src\\main\\java\\oopwj\\Data\\lecturer.txt", "Lecturer");
        AcademicList.clearSelection();

        JOptionPane.showMessageDialog(this, "Lecturer updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
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
            java.util.logging.Logger.getLogger(AssignLecturerPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        //</editor-fold>

        /* Create and display the form - Must login first */
        java.awt.EventQueue.invokeLater(() -> new oopwj.LoginFrame());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable AcademicList;
    private javax.swing.JTable LecturerList;
    private javax.swing.JButton btnAssign;
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField searchAcademic;
    private javax.swing.JTextField searchLecturer;
    // End of variables declaration//GEN-END:variables
}
