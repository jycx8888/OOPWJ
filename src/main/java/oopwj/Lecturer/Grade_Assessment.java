/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package oopwj.Lecturer;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author User
 */
public class Grade_Assessment extends javax.swing.JFrame {

    private String lecturerID;
    private Lecturer_menu lecturerMenu;
    private TableRowSorter<TableModel> tableRowSorter;
    private javax.swing.JLabel searchResultLabel;

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Grade_Assessment.class.getName());

    /**
     * Creates new form Grade_Assessment
     */
    public Grade_Assessment() {
        initComponents();
        initSearchResultLabel();
        resetPanelLayoutForLabel();
        setLocationRelativeTo(null);
        clearTable();
        // No need for initLoadOnShow() here
    }

    public Grade_Assessment(String lecturerID) {
        this(lecturerID, null);
    }
    
    public Grade_Assessment(String lecturerID, Lecturer_menu lecturerMenu) {
        this.lecturerID = lecturerID;
        this.lecturerMenu = lecturerMenu;
        
        System.out.println("Grade_Assessment constructor - lecturerID: " + lecturerID);
        logger.log(java.util.logging.Level.INFO, "Grade_Assessment created with lecturerID: " + lecturerID);
        
        initComponents();
        initSearchResultLabel();
        resetPanelLayoutForLabel();
        setLocationRelativeTo(null);
        clearTable();
        
        // Attach action listeners for buttons
        jButton2.addActionListener(this::jButton2ActionPerformed);
        jButton3.addActionListener(this::jButton3ActionPerformed);
        jButton4.addActionListener(this::jButton4ActionPerformed);
        setupSearchHandlers();
        
        // Add double-click listener to table
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int row = jTable1.rowAtPoint(evt.getPoint());
                    if (row >= 0) {
                        showRowDetailsPopup(row);
                    }
                }
            }
        });
        
        initLoadOnShow();
        // REMOVED: loadAssessmentAnswers() - will be called after window is shown
    }

    public void setLecturerID(String lecturerID) {
        this.lecturerID = lecturerID;
        if (isVisible()) {
            reloadAssessmentAnswers();
        }
    }

    private void reloadAssessmentAnswers() {
        clearTable();
        if (lecturerID != null && !lecturerID.isEmpty()) {
            loadAssessmentAnswers();
        }
    }

    private void clearTable() {
        jTable1.setModel(new DefaultTableModel(
            new Object[]{"Module name", "Quiz Name", "StudentID", "Total Grade", "Grade", "Feedback", "ModuleID", "QuizID"}, 0
        ));
        resetTableSorter();
        hideHiddenColumns();
    }

    private void initSearchResultLabel() {
        searchResultLabel = new javax.swing.JLabel("All Assessments");
        searchResultLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        searchResultLabel.setForeground(new java.awt.Color(0, 0, 0));
    }

    private void resetPanelLayoutForLabel() {
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jButton2)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton3)
                            .addGap(168, 168, 168)
                            .addComponent(jButton1))
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 585, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(55, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(searchResultLabel)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(30, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton4)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(searchResultLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 376, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3))
                .addGap(28, 28, 28))
        );
    }

    private void setTableLabel(String labelText) {
        if (searchResultLabel == null) {
            return;
        }
        if (labelText == null || labelText.trim().isEmpty()) {
            searchResultLabel.setText("All Assessments");
        } else {
            searchResultLabel.setText(labelText);
        }
    }

    private void hideHiddenColumns() {
        int columnCount = jTable1.getColumnCount();
        if (columnCount < 2) {
            return;
        }
        // Hide ModuleID and QuizID columns at the end.
        for (int colIndex = columnCount - 2; colIndex < columnCount; colIndex++) {
            javax.swing.table.TableColumn column = jTable1.getColumnModel().getColumn(colIndex);
            column.setMinWidth(0);
            column.setMaxWidth(0);
            column.setPreferredWidth(0);
        }
    }

    private void initLoadOnShow() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                System.out.println("DEBUG: windowOpened called");
                System.out.println("DEBUG: lecturerID = " + lecturerID);
                System.out.println("DEBUG: isVisible = " + isVisible());
                
                if (lecturerID != null && !lecturerID.isEmpty()) {
                    System.out.println("DEBUG: About to load data in background thread");
                    // Load data in background to avoid blocking UI
                    new Thread(() -> {
                        try {
                            Thread.sleep(500); // Wait for window to fully render
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                        javax.swing.SwingUtilities.invokeLater(() -> {
                            System.out.println("DEBUG: Calling loadAssessmentAnswers");
                            loadAssessmentAnswers();
                        });
                    }).start();
                } else {
                    System.out.println("DEBUG: lecturerID is null or empty, skipping load");
                }
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

        jDialog1 = new javax.swing.JDialog();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();

        javax.swing.GroupLayout jDialog1Layout = new javax.swing.GroupLayout(jDialog1.getContentPane());
        jDialog1.getContentPane().setLayout(jDialog1Layout);
        jDialog1Layout.setHorizontalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jDialog1Layout.setVerticalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
        ));
        jScrollPane1.setViewportView(jTable1);

        jButton1.setText("Back");
        jButton1.addActionListener(this::jButton1ActionPerformed);

        jButton2.setText("Grade");

        jButton3.setText("Feedback");

        jButton4.setText("Search");

        jTextField1.setText("Search");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jButton2)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton3)
                            .addGap(168, 168, 168)
                            .addComponent(jButton1))
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 585, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(55, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(30, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton4)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 376, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3))
                .addGap(28, 28, 28))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // Navigate back to lecturer menu
        if (lecturerMenu != null) {
            lecturerMenu.setVisible(true);
        } else {
            Lecturer_menu menu = new Lecturer_menu(lecturerID);
            menu.setVisible(true);
        }
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // Check if a row is selected
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow == -1) {
            javax.swing.JOptionPane.showMessageDialog(this, 
                "Please select a student to grade.", 
                "No Selection", 
                javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Get moduleID, quizID, and studentID from selected row
        String moduleID = jTable1.getValueAt(selectedRow, 6).toString();
        String quizID = jTable1.getValueAt(selectedRow, 7).toString();
        String studentID = jTable1.getValueAt(selectedRow, 2).toString();
        
        View_Grade viewGrade = new View_Grade(moduleID, quizID, studentID, lecturerID);
        viewGrade.setLocationRelativeTo(null);
        viewGrade.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed
    
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {
        // Check if a row is selected
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow == -1) {
            javax.swing.JOptionPane.showMessageDialog(this, 
                "Please select a student to provide feedback.", 
                "No Selection", 
                javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Get moduleID, quizID, and studentID from selected row
        String moduleID = jTable1.getValueAt(selectedRow, 6).toString();
        String quizID = jTable1.getValueAt(selectedRow, 7).toString();
        String studentID = jTable1.getValueAt(selectedRow, 2).toString();
        
        // Open Feedback window
        Feedback feedback = new Feedback(moduleID, quizID, studentID, lecturerID);
        feedback.setVisible(true);
        this.dispose();
    }
    
    /**
     * Shows a popup dialog with all details of the selected row
     */
    private void showRowDetailsPopup(int row) {
        // Get data from the selected row
        String moduleName = jTable1.getValueAt(row, 0).toString();
        String quizName = jTable1.getValueAt(row, 1).toString();
        String studentID = jTable1.getValueAt(row, 2).toString();
        String totalGrade = jTable1.getValueAt(row, 3).toString();
        String grade = jTable1.getValueAt(row, 4).toString();
        String feedback = jTable1.getValueAt(row, 5).toString();
        
        // Create a JDialog for the popup
        javax.swing.JDialog detailsDialog = new javax.swing.JDialog(this, "Student Details", true);
        detailsDialog.setDefaultCloseOperation(javax.swing.JDialog.DISPOSE_ON_CLOSE);
        detailsDialog.setSize(400, 300);
        detailsDialog.setLocationRelativeTo(this);
        
        // Create a panel for the content
        javax.swing.JPanel panel = new javax.swing.JPanel();
        panel.setLayout(new javax.swing.BoxLayout(panel, javax.swing.BoxLayout.Y_AXIS));
        panel.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setBackground(new java.awt.Color(255, 255, 255));
        
        // Add labels with the data
        addDetailRow(panel, "Module:", moduleName);
        addDetailRow(panel, "Quiz:", quizName);
        addDetailRow(panel, "Student ID:", studentID);
        addDetailRow(panel, "Total Grade:", totalGrade);
        addDetailRow(panel, "Grade:", grade);
        
        // Add feedback with a text area
        panel.add(new javax.swing.JLabel("Feedback:"));
        javax.swing.JTextArea feedbackArea = new javax.swing.JTextArea(feedback);
        feedbackArea.setLineWrap(true);
        feedbackArea.setWrapStyleWord(true);
        feedbackArea.setEditable(false);
        feedbackArea.setBackground(new java.awt.Color(240, 240, 240));
        javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane(feedbackArea);
        scrollPane.setPreferredSize(new java.awt.Dimension(350, 80));
        panel.add(scrollPane);
        
        panel.add(javax.swing.Box.createVerticalStrut(10));
        
        // Add close button
        javax.swing.JButton closeButton = new javax.swing.JButton("Close");
        closeButton.addActionListener(e -> detailsDialog.dispose());
        javax.swing.JPanel buttonPanel = new javax.swing.JPanel();
        buttonPanel.setBackground(new java.awt.Color(255, 255, 255));
        buttonPanel.add(closeButton);
        panel.add(buttonPanel);
        
        detailsDialog.add(panel);
        detailsDialog.setVisible(true);
    }
    
    /**
     * Helper method to add a detail row with label and value
     */
    private void addDetailRow(javax.swing.JPanel panel, String label, String value) {
        javax.swing.JPanel rowPanel = new javax.swing.JPanel();
        rowPanel.setLayout(new java.awt.BorderLayout());
        rowPanel.setBackground(new java.awt.Color(255, 255, 255));
        rowPanel.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 30));
        
        javax.swing.JLabel labelComponent = new javax.swing.JLabel(label);
        labelComponent.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
        
        javax.swing.JLabel valueComponent = new javax.swing.JLabel(value);
        valueComponent.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12));
        
        rowPanel.add(labelComponent, java.awt.BorderLayout.WEST);
        rowPanel.add(valueComponent, java.awt.BorderLayout.CENTER);
        
        panel.add(rowPanel);
        panel.add(javax.swing.Box.createVerticalStrut(5));
    }
    
    private void loadAssessmentAnswers() {
        System.out.println("DEBUG: loadAssessmentAnswers() START");
        
        // Use absolute paths based on project root
        String projectRoot = System.getProperty("user.dir");
        String modulesFilePath = projectRoot + "\\src\\main\\java\\oopwj\\data\\modules.txt";
        String answersFilePath = projectRoot + "\\src\\main\\java\\oopwj\\data\\answers.txt";
        String quizFilePath = projectRoot + "\\src\\main\\java\\oopwj\\data\\Quiz.txt";
        String finalGradeFilePath = projectRoot + "\\src\\main\\java\\oopwj\\data\\FinalGrade.txt";
        Set<String> allowedModuleIDs = new HashSet<>();
        Map<String, String> moduleIdToName = new HashMap<>();
        Map<String, String> quizKeyToName = new HashMap<>();
        Map<String, List<String>> quizByModule = new HashMap<>();
        Map<String, String[]> finalGrades = loadFinalGrades(finalGradeFilePath);

        // Debug: Check if lecturerID is set
        if (lecturerID == null || lecturerID.isEmpty()) {
            logger.log(java.util.logging.Level.WARNING, "lecturerID is null or empty");
            javax.swing.JOptionPane.showMessageDialog(this, 
                "lecturerID is null or empty", 
                "Error", 
                javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validate file existence
        java.io.File modulesFile = new java.io.File(modulesFilePath);
        java.io.File answersFile = new java.io.File(answersFilePath);
        java.io.File quizFile = new java.io.File(quizFilePath);
        
        if (!modulesFile.exists()) {
            logger.log(java.util.logging.Level.SEVERE, "modules.txt not found at: " + modulesFile.getAbsolutePath());
            javax.swing.JOptionPane.showMessageDialog(this, 
                "modules.txt not found at: " + modulesFile.getAbsolutePath(), 
                "File Not Found", 
                javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!answersFile.exists()) {
            logger.log(java.util.logging.Level.SEVERE, "answers.txt not found at: " + answersFile.getAbsolutePath());
            javax.swing.JOptionPane.showMessageDialog(this, 
                "answers.txt not found at: " + answersFile.getAbsolutePath(), 
                "File Not Found", 
                javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!quizFile.exists()) {
            logger.log(java.util.logging.Level.SEVERE, "Quiz.txt not found at: " + quizFile.getAbsolutePath());
            javax.swing.JOptionPane.showMessageDialog(this, 
                "Quiz.txt not found at: " + quizFile.getAbsolutePath(), 
                "File Not Found", 
                javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        logger.log(java.util.logging.Level.INFO, "Starting load for lecturer: " + lecturerID);
        System.out.println("Project root: " + projectRoot);
        System.out.println("Modules file path: " + modulesFilePath);
        System.out.println("Answers file path: " + answersFilePath);
        System.out.println("Quiz file path: " + quizFilePath);
        System.out.println("LecturerID: '" + lecturerID + "'");
        
        // Build set of moduleIDs for this lecturer
        try (BufferedReader br = new BufferedReader(new FileReader(modulesFile))) {
            String line;
            int lineCount = 0;
            while ((line = br.readLine()) != null) {
                lineCount++;
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                
                String[] parts = line.split(",");
                if (parts.length > 3) {
                    String moduleID = parts[0].trim();
                    String moduleName = parts[1].trim();
                    String fileLecturerID = parts[3].trim();
                    
                    System.out.println("Comparing: '" + fileLecturerID + "' == '" + lecturerID + "'");
                    if (fileLecturerID.equals(lecturerID)) {
                        allowedModuleIDs.add(moduleID);
                        moduleIdToName.put(moduleID, moduleName);
                        System.out.println("✓ Found matching module: " + moduleID);
                        logger.log(java.util.logging.Level.INFO, "Found module: " + moduleID);
                    }
                } else {
                    logger.log(java.util.logging.Level.WARNING, "Malformed line in modules.txt (line " + lineCount + "): " + line);
                }
            }
            System.out.println("Total modules read: " + lineCount + ", Allowed: " + allowedModuleIDs.size());
            logger.log(java.util.logging.Level.INFO, "Allowed modules: " + allowedModuleIDs);
        } catch (IOException e) {
            logger.log(java.util.logging.Level.SEVERE, "Error reading modules.txt: " + e.getMessage(), e);
            javax.swing.JOptionPane.showMessageDialog(this, 
                "Error reading modules.txt: " + e.getMessage(), 
                "File Error", 
                javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (allowedModuleIDs.isEmpty()) {
            logger.log(java.util.logging.Level.WARNING, "No modules found for lecturer: " + lecturerID);
            javax.swing.JOptionPane.showMessageDialog(this, 
                "No modules assigned to lecturer: " + lecturerID, 
                "No Data", 
                javax.swing.JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Build map of moduleID -> quizIDs
        try (BufferedReader br = new BufferedReader(new FileReader(quizFile))) {
            String line;
            int lineCount = 0;
            while ((line = br.readLine()) != null) {
                lineCount++;
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;

                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String quizID = parts[0].trim();
                    String moduleID = parts[1].trim();
                    String quizName = parts.length >= 3 ? parts[2].trim() : quizID;
                    String quizKey = moduleID + "|" + quizID;
                    quizKeyToName.put(quizKey, quizName);

                    if (!quizByModule.containsKey(moduleID)) {
                        quizByModule.put(moduleID, new ArrayList<>());
                    }
                    quizByModule.get(moduleID).add(quizID);
                } else {
                    logger.log(java.util.logging.Level.WARNING, "Malformed line in Quiz.txt (line " + lineCount + "): " + line);
                }
            }
            logger.log(java.util.logging.Level.INFO, "Quiz map size: " + quizByModule.size());
        } catch (IOException e) {
            logger.log(java.util.logging.Level.SEVERE, "Error reading Quiz.txt: " + e.getMessage(), e);
            javax.swing.JOptionPane.showMessageDialog(this, 
                "Error reading Quiz.txt: " + e.getMessage(), 
                "File Error", 
                javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Show unique moduleID, quizID, and studentID pairs from answers.txt
        Set<String> uniqueTriples = new HashSet<>();
        List<String[]> tableData = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(answersFile))) {
            String line;
            int lineCount = 0;
            while ((line = br.readLine()) != null) {
                lineCount++;
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String studentID = parts[0].trim();
                    String moduleID = parts[1].trim();
                    String quizID = parts[2].trim();
                    
                    if (allowedModuleIDs.contains(moduleID)) {
                        String tripleKey = moduleID + "|" + quizID + "|" + studentID;
                        if (!uniqueTriples.contains(tripleKey)) {
                            uniqueTriples.add(tripleKey);
                            String moduleName = moduleIdToName.getOrDefault(moduleID, moduleID);
                            String quizKey = moduleID + "|" + quizID;
                            String quizName = quizKeyToName.getOrDefault(quizKey, quizID);
                            tableData.add(new String[]{moduleName, quizName, studentID, moduleID, quizID});
                            System.out.println("✓ Added unique entry - Module: " + moduleID + ", Quiz: " + quizID + ", Student: " + studentID);
                        }
                    }
                } else {
                    logger.log(java.util.logging.Level.WARNING, "Malformed line in answers.txt (line " + lineCount + "): " + line);
                }
            }
            System.out.println("Total answers read: " + lineCount + ", Unique entries: " + tableData.size());
            logger.log(java.util.logging.Level.INFO, "Total unique entries loaded: " + tableData.size());
        } catch (IOException e) {
            logger.log(java.util.logging.Level.SEVERE, "Error reading answers.txt: " + e.getMessage(), e);
            javax.swing.JOptionPane.showMessageDialog(this, 
                "Error reading answers.txt: " + e.getMessage(), 
                "File Error", 
                javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        DefaultTableModel model = new DefaultTableModel(
            new Object[]{"Module name", "Quiz Name", "StudentID", "Total Grade", "Grade", "Feedback", "ModuleID", "QuizID"}, 0
        );
        for (String[] row : tableData) {
            String[] expandedRow = new String[8];
            expandedRow[0] = row[0]; // Module name
            expandedRow[1] = row[1]; // Quiz name
            expandedRow[2] = row[2]; // Student ID
            expandedRow[6] = row[3]; // Module ID (hidden)
            expandedRow[7] = row[4]; // Quiz ID (hidden)
            String key = row[2] + "|" + row[3] + "|" + row[4];
            String[] finalGradeRecord = finalGrades.get(key);
            if (finalGradeRecord != null) {
                expandedRow[3] = finalGradeRecord[0]; // Total Grade (mark)
                expandedRow[4] = finalGradeRecord[1]; // Grade (letter)
                expandedRow[5] = finalGradeRecord[2]; // Feedback
            } else {
                expandedRow[3] = "Pending";  // Total Grade column
                expandedRow[4] = "Pending";  // Grade column
                expandedRow[5] = "Pending";  // Feedback column
            }
            model.addRow(expandedRow);
            System.out.println("DEBUG: Added row - " + java.util.Arrays.toString(expandedRow));
        }
        System.out.println("DEBUG: Setting model with " + model.getRowCount() + " rows");
        jTable1.setModel(model);
        resetTableSorter();
        hideHiddenColumns();
        applySearchFilter();
        updateSearchResultLabel(jTextField1.getText());
        
        System.out.println("DEBUG: loadAssessmentAnswers() END");
    }

    private void setupSearchHandlers() {
        jTextField1.addActionListener(e -> applySearchFilter());
        jTextField1.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                applySearchFilter();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                applySearchFilter();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                applySearchFilter();
            }
        });

        jTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if ("Search".equals(jTextField1.getText())) {
                    jTextField1.setText("");
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (jTextField1.getText().trim().isEmpty()) {
                    jTextField1.setText("Search");
                }
            }
        });
    }

    private void resetTableSorter() {
        tableRowSorter = new TableRowSorter<>(jTable1.getModel());
        jTable1.setRowSorter(tableRowSorter);
    }

    private void applySearchFilter() {
        if (tableRowSorter == null) {
            return;
        }

        String query = jTextField1.getText();
        if (query == null) {
            tableRowSorter.setRowFilter(null);
            updateSearchResultLabel(null);
            return;
        }

        query = query.trim();
        if (query.isEmpty() || "Search".equalsIgnoreCase(query)) {
            tableRowSorter.setRowFilter(null);
            updateSearchResultLabel("");
            return;
        }

        String escaped = Pattern.quote(query);
        javax.swing.RowFilter<TableModel, Object> filter = javax.swing.RowFilter.regexFilter(
            "(?i)" + escaped, 0, 1, 2, 3, 4, 5
        );
        tableRowSorter.setRowFilter(filter);
        updateSearchResultLabel(query);
    }

    private void updateSearchResultLabel(String query) {
        if (query == null || query.trim().isEmpty() || "Search".equalsIgnoreCase(query.trim())) {
            setTableLabel("All Assessments");
        } else {
            setTableLabel("Search result of: \"" + query.trim() + "\"");
        }
    }

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {
        applySearchFilter();
    }

    /**
     * Loads final grade data from FinalGrade.txt
     * Map key: studentID|moduleID|quizID -> [mark, grade, feedback]
     */
    private Map<String, String[]> loadFinalGrades(String finalGradeFilePath) {
        Map<String, String[]> finalGrades = new HashMap<>();

        java.io.File finalGradeFile = new java.io.File(finalGradeFilePath);
        if (!finalGradeFile.exists()) {
            logger.log(java.util.logging.Level.WARNING, "FinalGrade.txt not found at: " + finalGradeFilePath);
            return finalGrades;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(finalGradeFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

                String[] parts = line.split(",", 6);
                if (parts.length >= 5) {
                    String studentID = parts[0].trim();
                    String moduleID = parts[1].trim();
                    String quizID = parts[2].trim();
                    String mark = parts[3].trim();
                    String grade = parts[4].trim();
                    String feedback = "";
                    
                    if (parts.length >= 6) {
                        feedback = parts[5].trim();
                        // Remove surrounding quotes if present
                        if (feedback.startsWith("\"") && feedback.endsWith("\"")) {
                            feedback = feedback.substring(1, feedback.length() - 1);
                            feedback = feedback.replace("\"\"", "\"");
                        }
                    }
                    
                    // Set to "Pending" if feedback is empty
                    if (feedback.isEmpty()) {
                        feedback = "Pending";
                    }

                    String key = studentID + "|" + moduleID + "|" + quizID;
                    finalGrades.put(key, new String[]{mark, grade, feedback});
                }
            }
        } catch (IOException e) {
            logger.log(java.util.logging.Level.SEVERE, "Error reading FinalGrade.txt: " + e.getMessage(), e);
        }

        return finalGrades;
    }
    
    /**
     * Auto-grades all objective questions for a student in a specific quiz
     */
    private void autoGradeObjectiveQuestions(String studentID, String moduleID, String quizID) {
        System.out.println("DEBUG: autoGradeObjectiveQuestions() START");
        System.out.println("StudentID: " + studentID + ", ModuleID: " + moduleID + ", QuizID: " + quizID);
        
        String projectRoot = System.getProperty("user.dir");
        String questionFilePath = projectRoot + "\\src\\main\\java\\oopwj\\data\\question.txt";
        String answersFilePath = projectRoot + "\\src\\main\\java\\oopwj\\data\\answers.txt";
        String totalQuizMarkPath = projectRoot + "\\src\\main\\java\\oopwj\\data\\TotalQuizMark.txt";
        String gradeFilePath = projectRoot + "\\src\\main\\java\\oopwj\\data\\Grade.txt";
        
        // Load question data (map of questionID -> [correctAnswer, questionType])
        Map<String, String[]> questionData = loadQuestionData(questionFilePath, moduleID, quizID);
        
        // Load student answers (map of questionID -> studentAnswer)
        Map<String, String> studentAnswers = loadStudentAnswers(answersFilePath, studentID, moduleID, quizID);
        
        // Load max marks (map of questionID -> maxMark)
        Map<String, Integer> maxMarks = loadMaxMarks(totalQuizMarkPath, moduleID, quizID);
        
        // Grade each objective question and save to Grade.txt
        List<String> gradesToAdd = new ArrayList<>();
        
        // Sort question IDs numerically for ordered output
        List<String> sortedQuestionIDs = new ArrayList<>(questionData.keySet());
        sortedQuestionIDs.sort((q1, q2) -> {
            // Extract numeric part from question IDs (e.g., "Q001" -> 1)
            try {
                int num1 = Integer.parseInt(q1.replaceAll("[^0-9]", ""));
                int num2 = Integer.parseInt(q2.replaceAll("[^0-9]", ""));
                return Integer.compare(num1, num2);
            } catch (NumberFormatException e) {
                return q1.compareTo(q2);
            }
        });
        
        for (String questionID : sortedQuestionIDs) {
            String[] qData = questionData.get(questionID);
            String correctAnswer = qData[0];
            String questionType = qData[1];
            
            // Only grade objective questions
            if ("Objective".equals(questionType)) {
                String studentAnswer = studentAnswers.getOrDefault(questionID, "");
                Integer maxMarkObj = maxMarks.getOrDefault(questionID, 0);
                int maxMark = (maxMarkObj != null) ? maxMarkObj : 0;
                
                // Compare answers (case-insensitive)
                int mark = correctAnswer.equalsIgnoreCase(studentAnswer) ? maxMark : 0;
                
                // Format: studentID,moduleID,quizID,questionID,Objective,correctAnswer,mark
                String gradeEntry = studentID + "," + moduleID + "," + quizID + "," + questionID + ",Objective," + correctAnswer + "," + mark;
                gradesToAdd.add(gradeEntry);
                
                System.out.println("Graded Q" + questionID + ": Student=" + studentAnswer + ", Correct=" + correctAnswer + ", Mark=" + mark);
            }
        }
        
        // Save grades to Grade.txt
        if (!gradesToAdd.isEmpty()) {
            appendGradesToFile(gradeFilePath, gradesToAdd);
            System.out.println("DEBUG: Saved " + gradesToAdd.size() + " grades to Grade.txt");
        }
        
        System.out.println("DEBUG: autoGradeObjectiveQuestions() END");
    }
    
    /**
     * Loads question data from question.txt for a specific module and quiz
     * Returns map of questionID -> [correctAnswer, questionType]
     */
    private Map<String, String[]> loadQuestionData(String questionFilePath, String moduleID, String quizID) {
        Map<String, String[]> questionData = new HashMap<>();
        
        java.io.File questionFile = new java.io.File(questionFilePath);
        if (!questionFile.exists()) {
            logger.log(java.util.logging.Level.WARNING, "question.txt not found at: " + questionFilePath);
            return questionData;
        }
        
        try (BufferedReader br = new BufferedReader(new FileReader(questionFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    String questionID = parts[0].trim();
                    String quizIDFromFile = parts[1].trim();
                    String moduleIDFromFile = parts[2].trim();
                    String questionType = parts[parts.length - 1].trim(); // Last column is type
                    
                    // Check if this question belongs to the target quiz and module
                    if (quizIDFromFile.equals(quizID) && moduleIDFromFile.equals(moduleID)) {
                        String correctAnswer = "";
                        
                        if ("Objective".equals(questionType) && parts.length >= 9) {
                            // For objective: format is Q,QZ,M,question,opt1,opt2,opt3,opt4,correctAns,Type
                            correctAnswer = parts[8].trim(); // Correct answer is at index 8
                        }
                        
                        questionData.put(questionID, new String[]{correctAnswer, questionType});
                    }
                }
            }
        } catch (IOException e) {
            logger.log(java.util.logging.Level.SEVERE, "Error reading question.txt: " + e.getMessage(), e);
        }
        
        return questionData;
    }
    
    /**
     * Loads student answers from answers.txt
     * Returns map of questionID -> studentAnswer
     */
    private Map<String, String> loadStudentAnswers(String answersFilePath, String studentID, String moduleID, String quizID) {
        Map<String, String> studentAnswers = new HashMap<>();
        
        java.io.File answersFile = new java.io.File(answersFilePath);
        if (!answersFile.exists()) {
            logger.log(java.util.logging.Level.WARNING, "answers.txt not found at: " + answersFilePath);
            return studentAnswers;
        }
        
        try (BufferedReader br = new BufferedReader(new FileReader(answersFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                
                String[] parts = line.split(",", 6); // Split into max 6 parts
                if (parts.length >= 6) {
                    String studentIDFromFile = parts[0].trim();
                    String moduleIDFromFile = parts[1].trim();
                    String quizIDFromFile = parts[2].trim();
                    String questionID = parts[3].trim();
                    String answer = parts[5].trim(); // Answer is at index 5
                    
                    // Match student, module, and quiz
                    if (studentIDFromFile.equals(studentID) && 
                        moduleIDFromFile.equals(moduleID) && 
                        quizIDFromFile.equals(quizID)) {
                        studentAnswers.put(questionID, answer);
                    }
                }
            }
        } catch (IOException e) {
            logger.log(java.util.logging.Level.SEVERE, "Error reading answers.txt: " + e.getMessage(), e);
        }
        
        return studentAnswers;
    }
    
    /**
     * Loads max marks from TotalQuizMark.txt
     * Returns map of questionID -> maxMark
     */
    private Map<String, Integer> loadMaxMarks(String totalQuizMarkPath, String moduleID, String quizID) {
        Map<String, Integer> maxMarks = new HashMap<>();
        
        java.io.File totalQuizMarkFile = new java.io.File(totalQuizMarkPath);
        if (!totalQuizMarkFile.exists()) {
            logger.log(java.util.logging.Level.WARNING, "TotalQuizMark.txt not found at: " + totalQuizMarkPath);
            return maxMarks;
        }
        
        try (BufferedReader br = new BufferedReader(new FileReader(totalQuizMarkFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    String moduleIDFromFile = parts[0].trim();
                    String quizIDFromFile = parts[1].trim();
                    String questionID = parts[2].trim();
                    int maxMark = Integer.parseInt(parts[3].trim());
                    
                    // Match module and quiz
                    if (moduleIDFromFile.equals(moduleID) && quizIDFromFile.equals(quizID)) {
                        maxMarks.put(questionID, maxMark);
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            logger.log(java.util.logging.Level.SEVERE, "Error reading TotalQuizMark.txt: " + e.getMessage(), e);
        }
        
        return maxMarks;
    }
    
    /**
     * Appends grade entries to Grade.txt file
     */
    private void appendGradesToFile(String gradeFilePath, List<String> gradesToAdd) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(gradeFilePath, true))) {
            for (String gradeEntry : gradesToAdd) {
                bw.write(gradeEntry);
                bw.newLine();
                System.out.println("Written to Grade.txt: " + gradeEntry);
            }
        } catch (IOException e) {
            logger.log(java.util.logging.Level.SEVERE, "Error writing to Grade.txt: " + e.getMessage(), e);
            javax.swing.JOptionPane.showMessageDialog(this,
                "Error writing grades to file: " + e.getMessage(),
                "File Error",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }
    

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
        java.awt.EventQueue.invokeLater(() -> 
            new Grade_Assessment("L101").setVisible(true) // Use a valid lecturerID for testing
        );
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
