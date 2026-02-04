/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package oopwj.AdministrativeStaff;

import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import com.toedter.calendar.JDateChooser;



/**
 *
 * @author kwany
 */
public class AssignClassAndSchedule extends javax.swing.JFrame {

    private final String loggedInUserID;
    private TableRowSorter<DefaultTableModel> scheduleDaySorter;



    /**
     * Creates new form CreateClassPage
     */
    public AssignClassAndSchedule() {
        this.loggedInUserID = null;
        initComponents();
        setLocationRelativeTo(null);
        
        loadModules();
        loadClassrooms();
        enableSearch();
    }

    public AssignClassAndSchedule(String userID) {
        this.loggedInUserID = userID;
        initComponents();
        setLocationRelativeTo(null);

        loadModules();
        loadClassrooms();
        loadSchedule();
        enableSearch();
    }
    
    private void loadModules() {
    DefaultTableModel model = (DefaultTableModel) moduleTable.getModel();
    model.setRowCount(0);

    try (BufferedReader br = new BufferedReader(new FileReader("src\\main\\java\\oopwj\\modules.txt"))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] data = line.split(",");
            if (data.length >= 2) {
                model.addRow(new Object[]{data[0], data[1]});
            }
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Failed to load modules.");
    }
}
    
    private void loadClassrooms() {
        DefaultTableModel model = (DefaultTableModel) classroomTable.getModel();
        model.setRowCount(0);

        Map<String, Set<String>> scheduleMap = loadScheduleMap();

        try (BufferedReader br = new BufferedReader(
                new FileReader("src\\main\\java\\oopwj\\class.txt"))) {

            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                if (data.length >= 2) {
                    String classId = data[0];
                    String className = data[1];

                    String scheduleDisplay = "None";
                    if (scheduleMap.containsKey(classId)) {
                        scheduleDisplay = String.join(", ", scheduleMap.get(classId));
                    }

                    model.addRow(new Object[]{
                        classId,
                        className,
                        scheduleDisplay
                    });
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to load classrooms.");
        }
    }
    
    private void loadSchedule() {
        DefaultTableModel model = (DefaultTableModel) scheduleTable.getModel();
        model.setRowCount(0);

        try (BufferedReader br = new BufferedReader(
                new FileReader("src\\main\\java\\oopwj\\class_schedule.txt"))) {

            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                if (data.length >= 5) {
                    model.addRow(new Object[]{
                        data[0],
                        data[1],
                        data[2],
                        data[3],
                        data[4]
                    });
                }
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to load class schedule.");
        }
    }

    private void enableSearch() {
    TableRowSorter<DefaultTableModel> moduleSorter =
            new TableRowSorter<>((DefaultTableModel) moduleTable.getModel());
    moduleTable.setRowSorter(moduleSorter);

    searchModule.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
        @Override
        public void insertUpdate(javax.swing.event.DocumentEvent e) { filter(); }
        @Override
        public void removeUpdate(javax.swing.event.DocumentEvent e) { filter(); }
        @Override
        public void changedUpdate(javax.swing.event.DocumentEvent e) { filter(); }

        private void filter() {
            String text = searchModule.getText();
            moduleSorter.setRowFilter(text.isEmpty() ? null :
                    RowFilter.regexFilter("(?i)" + text));
        }
    });

    TableRowSorter<DefaultTableModel> classSorter =
            new TableRowSorter<>((DefaultTableModel) classroomTable.getModel());
    classroomTable.setRowSorter(classSorter);

    searchClassroom.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
        @Override
        public void insertUpdate(javax.swing.event.DocumentEvent e) { filter(); }
        @Override
        public void removeUpdate(javax.swing.event.DocumentEvent e) { filter(); }
        @Override
        public void changedUpdate(javax.swing.event.DocumentEvent e) { filter(); }

        private void filter() {
            String text = searchClassroom.getText();
            classSorter.setRowFilter(text.isEmpty() ? null :
                    RowFilter.regexFilter("(?i)" + text));
        }
    });
    
    scheduleDaySorter = new TableRowSorter<>((DefaultTableModel) scheduleTable.getModel());
    scheduleTable.setRowSorter(scheduleDaySorter);

    // Text search
    searchSchedule.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
        @Override
        public void insertUpdate(javax.swing.event.DocumentEvent e) { applyScheduleFilter(); }
        @Override
        public void removeUpdate(javax.swing.event.DocumentEvent e) { applyScheduleFilter(); }
        @Override
        public void changedUpdate(javax.swing.event.DocumentEvent e) { applyScheduleFilter(); }
    });


}
    
    private void applyScheduleFilter() {
    List<RowFilter<Object, Object>> filters = new ArrayList<>();

    String selectedDay = boxDay.getSelectedItem().toString();
    if (!selectedDay.equalsIgnoreCase("All")) {
        filters.add(RowFilter.regexFilter("^" + selectedDay + "$", 2));
    }

    String text = searchSchedule.getText().trim();
    if (!text.isEmpty()) {
        filters.add(RowFilter.regexFilter("(?i)" + text));
    }

    if (filters.isEmpty()) {
        scheduleDaySorter.setRowFilter(null);
    } else {
        scheduleDaySorter.setRowFilter(RowFilter.andFilter(filters));
    }
}


    
    private Map<String, Set<String>> loadScheduleMap() {
        Map<String, Set<String>> scheduleMap = new HashMap<>();

        File file = new File("src\\main\\java\\oopwj\\class_schedule.txt");
        if (!file.exists()) return scheduleMap;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                if (data.length >= 5) {
                    String classId = data[0];
                    String moduleId = data[1];

                    scheduleMap
                        .computeIfAbsent(classId, k -> new LinkedHashSet<>())
                        .add(moduleId);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to load class schedule.");
        }

        return scheduleMap;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDateChooserBeanInfo1 = new com.toedter.calendar.JDateChooserBeanInfo();
        btnBack = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        moduleTable = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        classroomTable = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        btnAssign = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        searchModule = new javax.swing.JTextField();
        searchClassroom = new javax.swing.JTextField();
        btnUpdate = new javax.swing.JButton();
        btnRemove = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        scheduleTable = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        searchSchedule = new javax.swing.JTextField();
        boxDay = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btnBack.setText("Back");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        moduleTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Module ID", "Module Name"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        moduleTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(moduleTable);

        classroomTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Classroom ID", "Classroom", "Assigned Modulel"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        classroomTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(classroomTable);

        jLabel1.setText("Assign To ->");

        btnAssign.setText("Assign");
        btnAssign.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAssignActionPerformed(evt);
            }
        });

        jLabel2.setText("Search:");

        jLabel3.setText("Search:");

        searchModule.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchModuleActionPerformed(evt);
            }
        });

        btnUpdate.setText("Update");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        btnRemove.setText("Remove");
        btnRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveActionPerformed(evt);
            }
        });

        scheduleTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Classroom ID", "Module ID", "Day", "Start Time", "End Time"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        scheduleTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane3.setViewportView(scheduleTable);

        jLabel4.setText("Search:");

        boxDay.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday" }));
        boxDay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boxDayActionPerformed(evt);
            }
        });

        jLabel5.setText("Day:");

        jLabel6.setText("Assign To ->");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(searchSchedule))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(searchModule)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, Short.MAX_VALUE)
                                .addComponent(jLabel1))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel5)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(boxDay, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addGap(26, 26, 26)
                                        .addComponent(jLabel6)))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnBack)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel3)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(searchClassroom)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(btnRemove)
                                .addGap(18, 18, 18)
                                .addComponent(btnUpdate)
                                .addGap(18, 18, 18)
                                .addComponent(btnAssign))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(368, 368, 368)))
                .addGap(15, 15, 15))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(260, 260, 260))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(searchClassroom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnAssign)
                            .addComponent(btnUpdate)
                            .addComponent(btnRemove)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnBack)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(searchModule, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(72, 72, 72)
                                .addComponent(jLabel1))
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(96, 96, 96)
                                .addComponent(jLabel6)))))
                .addGap(58, 58, 58)
                .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(97, 97, 97)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(searchSchedule, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(boxDay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        this.dispose();
        AdminMainPage adminMain = new AdminMainPage(loggedInUserID);
        adminMain.setVisible(true);
    }//GEN-LAST:event_btnBackActionPerformed

    private void btnAssignActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAssignActionPerformed
        int moduleRow = moduleTable.getSelectedRow();
        int classRow = classroomTable.getSelectedRow();

        if (moduleRow == -1 || classRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select both a Module and a Classroom.");
            return;
        }

        int mRow = moduleTable.convertRowIndexToModel(moduleRow);
        int cRow = classroomTable.convertRowIndexToModel(classRow);

        DefaultTableModel mModel = (DefaultTableModel) moduleTable.getModel();
        DefaultTableModel cModel = (DefaultTableModel) classroomTable.getModel();

        String moduleId = mModel.getValueAt(mRow, 0).toString();
        String classroomId = cModel.getValueAt(cRow, 0).toString();
        String classroomName = cModel.getValueAt(cRow, 1).toString();
        String currentModule = cModel.getValueAt(cRow, 2).toString();

        if (!currentModule.equals("None")) {
            JOptionPane.showMessageDialog(this,
                    "This classroom is already assigned.\nUse Update instead.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Assign module " + moduleId + " to " + classroomName + "?",
                "Confirm Assignment",
                JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) return;

        File inputFile = new File("src\\main\\java\\oopwj\\class.txt");
        File tempFile = new File("src\\main\\java\\oopwj\\class_temp.txt");

        try (BufferedReader br = new BufferedReader(new FileReader(inputFile));
             BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                if (data[0].equals(classroomId)) {
                    bw.write(classroomId + "," + classroomName + "," + moduleId);
                } else {
                    bw.write(line);
                }
                bw.newLine();
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Assignment failed.");
            return;
        }

        inputFile.delete();
        tempFile.renameTo(inputFile);

        loadClassrooms();
        moduleTable.clearSelection();

        JOptionPane.showMessageDialog(this,
                "Module assigned successfully!");
    }//GEN-LAST:event_btnAssignActionPerformed

    private void btnRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveActionPerformed
        int row = classroomTable.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a classroom.");
            return;
        }

        int modelRow = classroomTable.convertRowIndexToModel(row);
        DefaultTableModel model =
                (DefaultTableModel) classroomTable.getModel();

        String classroomId = model.getValueAt(modelRow, 0).toString();
        String classroomName = model.getValueAt(modelRow, 1).toString();

        File inputFile = new File("src\\main\\java\\oopwj\\class.txt");
        File tempFile = new File("src\\main\\java\\oopwj\\class_temp.txt");

        try (BufferedReader br = new BufferedReader(new FileReader(inputFile));
             BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                
                if (data[0].equals(classroomId)) {
                    bw.write(classroomId + "," + classroomName + "," + "None");
                } else {
                    bw.write(line);
                }
                bw.newLine();
                
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to remove assignment.");
            return;
        }

        inputFile.delete();
        tempFile.renameTo(inputFile);

        loadClassrooms();

        JOptionPane.showMessageDialog(this, "Module unassigned successfully.");
    }//GEN-LAST:event_btnRemoveActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        int classroomRow = classroomTable.getSelectedRow();
        int moduleRow = moduleTable.getSelectedRow();

        if (classroomRow == -1 || moduleRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select both a classroom and a module.");
            return;
        }

        int cRow = classroomTable.convertRowIndexToModel(classroomRow);
        int mRow = moduleTable.convertRowIndexToModel(moduleRow);

        DefaultTableModel cModel =
                (DefaultTableModel) classroomTable.getModel();
        DefaultTableModel mModel =
                (DefaultTableModel) moduleTable.getModel();

        String classroomId = cModel.getValueAt(cRow, 0).toString();
        String classroomName = cModel.getValueAt(cRow, 1).toString();
        String moduleId = mModel.getValueAt(mRow, 0).toString();
        String currentModule = cModel.getValueAt(cRow, 2).toString();

        File inputFile = new File("src\\main\\java\\oopwj\\class.txt");
        File tempFile = new File("src\\main\\java\\oopwj\\class_temp.txt");

        if (currentModule.equals("None")) {
            JOptionPane.showMessageDialog(this, """
                                                This classroom has no module assigned yet.
                                                Please use Assign instead.""",
            "Update Not Allowed",
            JOptionPane.WARNING_MESSAGE);
    return;
        }
        
        try (BufferedReader br = new BufferedReader(new FileReader(inputFile));
             BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith(classroomId + ",")) {
                    bw.write(classroomId + "," + classroomName + "," + moduleId);
                } else {
                    bw.write(line);
                }
                bw.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Update failed.");
            return;
        }

        inputFile.delete();
        tempFile.renameTo(inputFile);

        loadClassrooms();
        moduleTable.clearSelection();

        JOptionPane.showMessageDialog(this, "Assignment updated successfully.");
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void searchModuleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchModuleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchModuleActionPerformed

    private void boxDayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boxDayActionPerformed
        applyScheduleFilter();
    }//GEN-LAST:event_boxDayActionPerformed

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
            java.util.logging.Logger.getLogger(AssignClassAndSchedule.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        
        //</editor-fold>

        /* Create and display the form - Must login first */
        java.awt.EventQueue.invokeLater(() -> new oopwj.LoginFrame());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> boxDay;
    private javax.swing.JButton btnAssign;
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnRemove;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JTable classroomTable;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooserBeanInfo jDateChooserBeanInfo1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable moduleTable;
    private javax.swing.JTable scheduleTable;
    private javax.swing.JTextField searchClassroom;
    private javax.swing.JTextField searchModule;
    private javax.swing.JTextField searchSchedule;
    // End of variables declaration//GEN-END:variables
}
