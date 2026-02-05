/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package oopwj.AdministrativeStaff;

import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import java.text.SimpleDateFormat;



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
        
        enableSearch();
        dateChooseClass.setDate(new Date());
        dateChooseSchedule.setDate(new Date());
        
        loadModules();
        loadClassrooms();
        loadSchedule();
    }

    public AssignClassAndSchedule(String userID) {
        this.loggedInUserID = userID;
        initComponents();
        setLocationRelativeTo(null);
        
        enableSearch();
        dateChooseClass.setDate(new Date());
        dateChooseSchedule.setDate(new Date());
        
        loadModules();
        loadClassrooms();
        loadSchedule();
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
    
    private void loadClassroomsByDate(Date selectedDate) {
    DefaultTableModel model = (DefaultTableModel) classroomTable.getModel();
    model.setRowCount(0);

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    String dateStr = sdf.format(selectedDate);

    Map<String, Set<String>> scheduleMap = new HashMap<>();

    File file = new File("src\\main\\java\\oopwj\\class_schedule.txt");

    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] data = line.split(",");

            String classId = data[0];
            String moduleId = data[1];
            String date = data[2];

            if (date.equals(dateStr)) {
                scheduleMap
                    .computeIfAbsent(classId, k -> new HashSet<>())
                    .add(moduleId);
            }
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Failed to load classrooms by date.");
        return;
    }

    try (BufferedReader br = new BufferedReader(
            new FileReader("src\\main\\java\\oopwj\\class.txt"))) {

        String line;
        while ((line = br.readLine()) != null) {
            String[] data = line.split(",");

            String classId = data[0];
            String className = data[1];
            String scheduleDisplay = "None";
            
            if (scheduleMap.containsKey(classId)) {
                scheduleDisplay = String.join(",",scheduleMap.get(classId));
            }
            model.addRow(new Object[]{
               classId,
               className,
               scheduleDisplay
            });
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

                if (data.length >= 6) {
                    model.addRow(new Object[]{
                        data[0],
                        data[1],
                        data[2],
                        data[3],
                        data[4],
                        data[5]
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
        public void insertUpdate(javax.swing.event.DocumentEvent e) { applyClassSearch(); }
        @Override
        public void removeUpdate(javax.swing.event.DocumentEvent e) { applyClassSearch(); }
        @Override
        public void changedUpdate(javax.swing.event.DocumentEvent e) { applyClassSearch(); }

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
    
    private void applyClassSearch() {
    TableRowSorter<DefaultTableModel> classSorter =
        (TableRowSorter<DefaultTableModel>) classroomTable.getRowSorter();
    String text = searchClassroom.getText().trim();
    if (text.isEmpty()) {
        classSorter.setRowFilter(null);
    } else {
        classSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
    }
}

    
    private void applyScheduleFilter() {
    List<RowFilter<Object, Object>> filters = new ArrayList<>();

    Date selectedDate = dateChooseSchedule.getDate();
    if (selectedDate != null) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dateStr = sdf.format(selectedDate);
        filters.add(RowFilter.regexFilter("^" + dateStr + "$", 2));
    }
    
    String selectedDay = boxDay.getSelectedItem().toString();
    if (!selectedDay.equalsIgnoreCase("All")) {
        filters.add(RowFilter.regexFilter("^" + selectedDay + "$", 3));
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
        dateChooseClass = new com.toedter.calendar.JDateChooser();
        dateChooseSchedule = new com.toedter.calendar.JDateChooser();
        spinnerMin = new JSpinner(new SpinnerDateModel())
        ;
        JSpinner.DateEditor startEditor = new JSpinner.DateEditor(spinnerMin, "HH:mm");
        spinnerMin.setEditor(startEditor);
        spinnerMax = new JSpinner(new SpinnerDateModel())
        ;
        JSpinner.DateEditor endEditor = new JSpinner.DateEditor(spinnerMax, "HH:mm");
        spinnerMax.setEditor(endEditor);
        jLabel1 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();

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
                "Classroom ID", "Classroom", "Assigned Module"
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
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Classroom ID", "Module ID", "Date", "Day", "Start Time", "End Time"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
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

        dateChooseClass.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                dateChooseClassPropertyChange(evt);
            }
        });

        dateChooseSchedule.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                dateChooseSchedulePropertyChange(evt);
            }
        });

        jLabel1.setText("Start Time:");

        jLabel7.setText("End Time:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(searchSchedule, javax.swing.GroupLayout.PREFERRED_SIZE, 414, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(boxDay, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(dateChooseSchedule, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnUpdate))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(btnBack)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 402, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel6)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(searchModule, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(spinnerMin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(spinnerMax, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 399, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(searchClassroom, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(dateChooseClass, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(btnRemove)
                                .addGap(18, 18, 18)
                                .addComponent(btnAssign)))))
                .addContainerGap(9, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(btnBack)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(dateChooseClass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(searchClassroom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel3)))
                        .addGap(10, 10, 10)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(spinnerMax, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel2)
                                .addComponent(searchModule, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(spinnerMin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel1)
                                .addComponent(jLabel7)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(109, 109, 109)
                                .addComponent(jLabel6)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAssign)
                    .addComponent(btnRemove))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel4)
                                .addComponent(searchSchedule, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel5)
                                .addComponent(boxDay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(dateChooseSchedule, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnUpdate))
                .addContainerGap(21, Short.MAX_VALUE))
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

        Date selectedDate = dateChooseClass.getDate();
        if (selectedDate == null) {
            JOptionPane.showMessageDialog(this, "Please select a date first.");
            return;
        }

        Date startTime = (Date) spinnerMin.getValue();
        Date endTime = (Date) spinnerMax.getValue();

        if (!startTime.before(endTime)) {
            JOptionPane.showMessageDialog(this, "Start time must be before end time.");
            return;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        String dateStr = dateFormat.format(selectedDate);
        String startStr = timeFormat.format(startTime);
        String endStr = timeFormat.format(endTime);

        File scheduleFile = new File("src\\main\\java\\oopwj\\class_schedule.txt");
        boolean conflictFound = false;

        try (BufferedReader br = new BufferedReader(new FileReader(scheduleFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 6) {
                    String sClassId = data[0];
                    String sModuleId = data[1];
                    String sDate = data[2];
                    String sStart = data[4];
                    String sEnd = data[5];

                    if (sClassId.equals(classroomId) && sDate.equals(dateStr)) {
                        Date existingStart = timeFormat.parse(sStart);
                        Date existingEnd = timeFormat.parse(sEnd);

                        if (!endTime.before(existingStart) && !startTime.after(existingEnd)) {
                            conflictFound = true;
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to read schedule file.");
            return;
        }

        if (conflictFound) {
            JOptionPane.showMessageDialog(this,
                    "There is a time conflict with another module for this classroom.\nPlease choose a different time or use Update.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Assign module " + moduleId + " to " + classroomName + "\non " + dateStr +
                " from " + startStr + " to " + endStr + "?",
                "Confirm Assignment",
                JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) return;

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(scheduleFile, true))) {
            String dayOfWeek = new SimpleDateFormat("EEEE").format(selectedDate);
            bw.write(classroomId + "," + moduleId + "," + dateStr + "," + dayOfWeek + "," + startStr + "," + endStr);
            bw.newLine();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to save assignment.");
            return;
        }

        loadClassrooms();
        loadSchedule();
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
        DefaultTableModel list = (DefaultTableModel) classroomTable.getModel();
        
        String classroomId = (String) list.getValueAt(modelRow, 0);
        String classroom = (String) list.getValueAt(modelRow, 1);
        

        Date selectedDate = dateChooseClass.getDate();
        if (selectedDate == null) {
            JOptionPane.showMessageDialog(this, "Please select a date first.");
            return;
        }
        
        String classIdToLoad = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dateStr = sdf.format(selectedDate);

        File scheduleFile = new File("src\\main\\java\\oopwj\\class_schedule.txt");
        File classFile = new File("src\\main\\java\\oopwj\\class.txt");

        try (BufferedReader br = new BufferedReader(new FileReader(classFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 2 && data[1].equals(classroom)) {
                    classIdToLoad = data[0];
                    break;
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to load class info.");
            return;
        }

        if (classIdToLoad == null) {
            JOptionPane.showMessageDialog(this, "Classroom not found.");
            return;
        }

        boolean hasModule = false;
        try (BufferedReader br = new BufferedReader(new FileReader(scheduleFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 6 && data[0].equals(classIdToLoad) && data[2].equals(dateStr)) {
                    hasModule = true;
                    break;
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to load schedule.");
            return;
        }

        if (!hasModule) {
            JOptionPane.showMessageDialog(this, "No module assigned for this classroom on the selected date.");
            return;
        }

        RemoveModule removeDialog = new RemoveModule(this, true, selectedDate, classroomId,classroom);
        removeDialog.setVisible(true);
        
        loadClassrooms();
        loadSchedule();
    }//GEN-LAST:event_btnRemoveActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        int row = scheduleTable.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a schedule to update.");
            return;
        }

        int modelRow = scheduleTable.convertRowIndexToModel(row);
        DefaultTableModel model = (DefaultTableModel) scheduleTable.getModel();

        String classId   = model.getValueAt(modelRow, 0).toString();
        String moduleId  = model.getValueAt(modelRow, 1).toString();
        String date      = model.getValueAt(modelRow, 2).toString();
        String startTime = model.getValueAt(modelRow, 4).toString();
        String endTime   = model.getValueAt(modelRow, 5).toString();

        UpdateSchedule dialog = new UpdateSchedule(this, true, classId, moduleId, date, startTime, endTime);

        dialog.setVisible(true);

        loadSchedule();
        loadClassrooms();
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void searchModuleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchModuleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchModuleActionPerformed

    private void boxDayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boxDayActionPerformed
        applyScheduleFilter();
    }//GEN-LAST:event_boxDayActionPerformed

    private void dateChooseClassPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_dateChooseClassPropertyChange
        if ("date".equals(evt.getPropertyName())) {
            Date selectedDate = dateChooseClass.getDate();
            if (selectedDate != null) {
                loadClassroomsByDate(selectedDate);
            } else {
                loadClassrooms();
            }
            applyClassSearch();
        }
    }//GEN-LAST:event_dateChooseClassPropertyChange

    private void dateChooseSchedulePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_dateChooseSchedulePropertyChange
        if ("date".equals(evt.getPropertyName())) {
            applyScheduleFilter();
        }
    }//GEN-LAST:event_dateChooseSchedulePropertyChange

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
    private com.toedter.calendar.JDateChooser dateChooseClass;
    private com.toedter.calendar.JDateChooser dateChooseSchedule;
    private com.toedter.calendar.JDateChooserBeanInfo jDateChooserBeanInfo1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable moduleTable;
    private javax.swing.JTable scheduleTable;
    private javax.swing.JTextField searchClassroom;
    private javax.swing.JTextField searchModule;
    private javax.swing.JTextField searchSchedule;
    private javax.swing.JSpinner spinnerMax;
    private javax.swing.JSpinner spinnerMin;
    // End of variables declaration//GEN-END:variables
}
