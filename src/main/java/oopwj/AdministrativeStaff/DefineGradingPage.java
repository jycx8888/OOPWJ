/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package oopwj.AdministrativeStaff;

import java.awt.*;
import java.io.*;
import javax.swing.*;
import static javax.swing.SwingConstants.CENTER;
import javax.swing.table.*;

/**
 *
 * @author kwany
 */
public class DefineGradingPage extends javax.swing.JFrame {

    private final String loggedInUserID;

    /**
     * Creates new form DefineGradingPage
     */
    public DefineGradingPage() {
        this.loggedInUserID = null;
        initComponents();
        
        setLocationRelativeTo(null);
        
        PlaceHolder.apply(txtGrade, "Select or enter a grade");
        PlaceHolder.apply(txtMin, "Select or enter a min marks");
        PlaceHolder.apply(txtMax, "Select or enter a max marks");
        
        addHoverEffect(btnCancel,new Color(200,200,200),new Color(170,170,170));
        addHoverEffect(btnClear, new Color(108, 117, 125), new Color(90, 98, 104));
        addHoverEffect(btnDelete,new Color(220,80,80),new Color(255,120,120));
        addHoverEffect(btnAdd,new Color(70,130,180),new Color(100,149,237));
        addHoverEffect(btnUpdate,new Color(70,130,180),new Color(100,149,237));
        
        styleTables(tableGrade);
        
        tableGrade.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) { 
                int selectedRow = tableGrade.getSelectedRow();
                if (selectedRow != -1) {
                    int modelRow = tableGrade.convertRowIndexToModel(selectedRow);
                    DefaultTableModel model = (DefaultTableModel) tableGrade.getModel();

                    String grade = (String) model.getValueAt(modelRow, 0);
                    String min = String.valueOf(model.getValueAt(modelRow, 1));
                    String max = String.valueOf(model.getValueAt(modelRow, 2));

                    txtGrade.setText(grade);
                    txtGrade.setForeground(Color.BLACK);
                    txtMin.setText(min);
                    txtMin.setForeground(Color.BLACK);
                    txtMax.setText(max);
                    txtMax.setForeground(Color.BLACK);
                }
            }
        });
    }

    
    public DefineGradingPage(String userID) {
        this.loggedInUserID = userID;
        initComponents();

        setLocationRelativeTo(null);

        PlaceHolder.apply(txtGrade, "Select or enter a grade");
        PlaceHolder.apply(txtMin, "Select or enter a min marks");
        PlaceHolder.apply(txtMax, "Select or enter a max marks");

        addHoverEffect(btnCancel, new Color(108, 117, 125), new Color(90, 98, 104));
        addHoverEffect(btnClear, new Color(108, 117, 125), new Color(90, 98, 104));
        addHoverEffect(btnDelete,new Color(220,80,80),new Color(255,120,120));
        addHoverEffect(btnAdd,new Color(70,130,180),new Color(100,149,237));
        addHoverEffect(btnUpdate,new Color(70,130,180),new Color(100,149,237));
        
        styleTables(tableGrade);
        
        tableGrade.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = tableGrade.getSelectedRow();
                if (selectedRow != -1) {
                    int modelRow = tableGrade.convertRowIndexToModel(selectedRow);
                    DefaultTableModel model = (DefaultTableModel) tableGrade.getModel();

                    String grade = (String) model.getValueAt(modelRow, 0);
                    String min = String.valueOf(model.getValueAt(modelRow, 1));
                    String max = String.valueOf(model.getValueAt(modelRow, 2));

                    txtGrade.setText(grade);
                    txtGrade.setForeground(Color.BLACK);
                    txtMin.setText(min);
                    txtMin.setForeground(Color.BLACK);
                    txtMax.setText(max);
                    txtMax.setForeground(Color.BLACK);
                }
            }
        });
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
    
    public void loadGrade() {
    DefaultTableModel list = (DefaultTableModel) tableGrade.getModel();
    list.setRowCount(0);

    try (BufferedReader br = new BufferedReader(new FileReader("src\\main\\java\\oopwj\\Data\\grading.txt"))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] values = line.split(",");
            list.addRow(values);
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Failed to load grade.", "Error", JOptionPane.ERROR_MESSAGE);
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
                    0, 0, new Color(230, 240, 250),
                    0, getHeight(), new Color(255, 255, 255)
                );

                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        btnUpdate = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableGrade = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        btnDelete = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        btnCancel = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        txtGrade = new javax.swing.JTextField();
        txtMin = new javax.swing.JTextField();
        txtMax = new javax.swing.JTextField();
        btnAdd = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Define Grading Page");

        btnUpdate.setText("Update");
        btnUpdate.setOpaque(true);
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        btnClear.setText("Clear");
        btnClear.setOpaque(true);
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

        tableGrade.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Grade", "Min Marks", "Max Marks"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableGrade.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tableGrade);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("Grade:");

        btnDelete.setText("Delete");
        btnDelete.setOpaque(true);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setText("Min Marks:");

        btnCancel.setText("Cancel");
        btnCancel.setOpaque(true);
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setText("Max Marks:");

        btnAdd.setText("Add");
        btnAdd.setOpaque(true);
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtGrade)
                                .addComponent(txtMin)
                                .addComponent(txtMax, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(btnAdd)
                                .addGap(74, 74, 74)
                                .addComponent(btnUpdate)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnClear))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(btnDelete)
                                .addGap(226, 226, 226)
                                .addComponent(btnCancel)))))
                .addContainerGap(33, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtGrade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtMin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtMax, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAdd)
                    .addComponent(btnUpdate)
                    .addComponent(btnClear))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDelete)
                    .addComponent(btnCancel))
                .addContainerGap(31, Short.MAX_VALUE))
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

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        int newMin, newMax;
        
        int selectedRow = tableGrade.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a grade to update.", 
                                          "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String newGrade = txtGrade.getText().trim();
        String newMinStr = txtMin.getText().trim();
        String newMaxStr = txtMax.getText().trim();
        
        if (newGrade.isEmpty() || newGrade.equals("Select or enter a grade") ||
            newMinStr.isEmpty() || newMinStr.equals("Select or enter a min marks") ||
            newMaxStr.isEmpty() || newMaxStr.equals("Select or enter a max marks")) {
            JOptionPane.showMessageDialog(this, "All fields are required.",
                                          "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            newMin = Integer.parseInt(newMinStr);
            newMax = Integer.parseInt(newMaxStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Marks must be numbers.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (newMin < 0 || newMax > 100) {
            JOptionPane.showMessageDialog(this,
                    "Marks must be between 0 and 100.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (newMin > newMax) {
            JOptionPane.showMessageDialog(this, "Minimum mark cannot exceed maximum mark.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int modelRow = tableGrade.convertRowIndexToModel(selectedRow);
        DefaultTableModel model = (DefaultTableModel) tableGrade.getModel();

        String oldGrade = (String) model.getValueAt(modelRow, 0);

        File inputFile = new File("src\\main\\java\\oopwj\\Data\\grading.txt");
        File tempFile = new File("src\\main\\java\\oopwj\\Data\\grading_temp.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String existingGrade = parts[0];
                int existingMin = Integer.parseInt(parts[1]);
                int existingMax = Integer.parseInt(parts[2]);

                if (!existingGrade.equals(oldGrade)) {
                    if (existingGrade.equalsIgnoreCase(newGrade)) {
                        JOptionPane.showMessageDialog(this,
                                "Grade already exists: " + existingGrade,
                                "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    boolean overlap = newMin <= existingMax && newMax >= existingMin;
                    if (overlap) {
                        JOptionPane.showMessageDialog(this,
                                "Grade range overlaps with existing grade: " + existingGrade +
                                " (" + existingMin + " - " + existingMax + ")",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                if (existingGrade.equals(oldGrade)) {
                    writer.write(newGrade + "," + newMin + "," + newMax);
                } else {
                    writer.write(line);
                }
                writer.newLine();
            }

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error updating grade: " + ex.getMessage(),
                                          "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!inputFile.delete() || !tempFile.renameTo(inputFile)) {
            JOptionPane.showMessageDialog(this, "Failed to update grading file.",
                                          "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        loadGrade();
        tableGrade.clearSelection();
        
        PlaceHolder.apply(txtGrade, "Select or enter a grade");
        PlaceHolder.apply(txtMin, "Select or enter a min marks");
        PlaceHolder.apply(txtMax, "Select or enter a max marks");
        
        JOptionPane.showMessageDialog(this, "Grade updated successfully!",
                                      "Success", JOptionPane.INFORMATION_MESSAGE);
        
        
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        int min, max;
        
        String grade = txtGrade.getText().trim();
        String minStr = txtMin.getText().trim();
        String maxStr = txtMax.getText().trim();
        
        if (grade.isEmpty() || grade.equals("Select or enter a grade") ||
            minStr.isEmpty() || minStr.equals("Select or enter a min marks") ||
            maxStr.isEmpty() || maxStr.equals("Select or enter a max marks")) {
            JOptionPane.showMessageDialog(this, "All fields are required.",
                                          "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }   
        
        try {
            min = Integer.parseInt(minStr);
            max = Integer.parseInt(maxStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Marks must be numbers.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (min < 0 || max > 100) {
            JOptionPane.showMessageDialog(this,
                    "Marks must be between 0 and 100.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (min > max) {
            JOptionPane.showMessageDialog(this, "Minimum mark cannot exceed maximum mark.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader("src\\main\\java\\oopwj\\Data\\grading.txt"))) {

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");

                String existingGrade = parts[0];
                int existingMin = Integer.parseInt(parts[1]);
                int existingMax = Integer.parseInt(parts[2]);

                if (existingGrade.equalsIgnoreCase(grade)) {
                    JOptionPane.showMessageDialog(this,
                            "Grade already exists.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                boolean overlap = min <= existingMax && max >= existingMin;
                if (overlap) {
                    JOptionPane.showMessageDialog(this,
                            "Grade range overlaps with existing grade: " + existingGrade +
                            " (" + existingMin + " - " + existingMax + ")",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading grading file.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try (PrintWriter out = new PrintWriter(new FileWriter("src\\main\\java\\oopwj\\Data\\grading.txt", true))) {
            out.println(grade + "," + min + "," + max);
            

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Error saving grade.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    
        loadGrade();
        tableGrade.clearSelection();

        PlaceHolder.apply(txtGrade, "Select or enter a grade");
        PlaceHolder.apply(txtMin, "Select or enter a min marks");
        PlaceHolder.apply(txtMax, "Select or enter a max marks");
        JOptionPane.showMessageDialog(this,
                "Grade added successfully!",
                "Success", JOptionPane.INFORMATION_MESSAGE);
    
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        PlaceHolder.apply(txtGrade, "Select or enter a grade");
        PlaceHolder.apply(txtMin, "Select or enter a min marks");
        PlaceHolder.apply(txtMax, "Select or enter a max marks");
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        int selectedRow = tableGrade.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a grade to delete.", 
                                          "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int modelRow = tableGrade.convertRowIndexToModel(selectedRow);
        DefaultTableModel list = (DefaultTableModel) tableGrade.getModel();

        String grade = (String) list.getValueAt(modelRow, 0);
        String min = (String) list.getValueAt(modelRow, 1);
        String max = (String) list.getValueAt(modelRow, 2);

        int option = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to delete this grading?\n\nGrade: " + grade +
                "\nMin Marks: " + min + "\nMax Marks: " + max, 
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (option == JOptionPane.YES_OPTION) {
            try {
                File inputFile = new File("src\\main\\java\\oopwj\\Data\\grading.txt");
                File tempFile = new File("src\\main\\java\\oopwj\\Data\\grading_temp.txt");

                try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                     BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split(",");
                        if (!parts[0].equals(grade)) {
                            writer.write(line);
                            writer.newLine();
                        }
                    }
                }

                if (!inputFile.delete() || !tempFile.renameTo(inputFile)) {
                    throw new IOException("Failed to delete grading from file.");
                }


            } catch (HeadlessException | IOException ex) {
                JOptionPane.showMessageDialog(this, "Error deleting grade: " + ex.getMessage(), 
                                              "Error", JOptionPane.ERROR_MESSAGE);
            }
            
            JOptionPane.showMessageDialog(this, "Grading deleted successfully.", 
                                      "Deleted", JOptionPane.INFORMATION_MESSAGE);
        }
        
        loadGrade();
        tableGrade.clearSelection();

        PlaceHolder.apply(txtGrade, "Select or enter a grade");
        PlaceHolder.apply(txtMin, "Select or enter a min marks");
        PlaceHolder.apply(txtMax, "Select or enter a max marks");
        
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        boolean hasChanges = false;
        AdminMainPage adminPage = new AdminMainPage(loggedInUserID);
        
        if (!txtGrade.getText().equals("Select or enter a grade") && !txtGrade.getText().isEmpty()) hasChanges = true;
        if (!txtMin.getText().equals("Select or enter a min marks") && !txtMin.getText().isEmpty()) hasChanges = true;
        if (!txtMax.getText().equals("Select or enter a max marks") && !txtMax.getText().isEmpty()) hasChanges = true;
        
        if (hasChanges) {
            int option = JOptionPane.showConfirmDialog(this,
                    "You have unsaved changes. Are you sure you want to cancel?",
                    "Confirm Cancel",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (option == JOptionPane.YES_OPTION) {
                adminPage.setVisible(true);
                this.dispose();
            }
        } else {
            adminPage.setVisible(true);
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
            java.util.logging.Logger.getLogger(DefineGradingPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        //</editor-fold>

        /* Create and display the form - Must login first */
        java.awt.EventQueue.invokeLater(() -> new oopwj.LoginFrame());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tableGrade;
    private javax.swing.JTextField txtGrade;
    private javax.swing.JTextField txtMax;
    private javax.swing.JTextField txtMin;
    // End of variables declaration//GEN-END:variables
}
