/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package oopwj.Lecturer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

import javax.swing.JOptionPane;

/**
 *
 * @author User
 */
public class Quiz extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Quiz.class.getName());
    
    // Counts questions written for the current session
    private int sessionQuestionCount = 0;
    
    // Track if this is an edit mode (loaded from Quiz.txt)
    private boolean isEditMode = false;
    // Store original values to detect changes
    private String originalQuestion = "";
    private String originalA = "";
    private String originalB = "";
    private String originalC = "";
    private String originalD = "";
    private String originalCorrectAnswer = "";

    // Shorter variable names for answer fields
    private javax.swing.JTextField a;
    private javax.swing.JTextField b;
    private javax.swing.JTextField c;
    private javax.swing.JTextField d;
    
    // ButtonGroup to ensure only one radio button is selected
    private javax.swing.ButtonGroup answerGroup;
    // Track the correct answer (A, B, C, or D)
    private String correctAnswer = "";
    
    // Session tracking
    private String lecturerID;
    private Assessments parentWindow;


    /**
     * Creates new form Quiz
     */
    public Quiz() {
        this(null, null);
    }
    
    public Quiz(String lecturerID, Assessments parentWindow) {
        this.lecturerID = lecturerID;
        this.parentWindow = parentWindow;
        initComponents();
        // Create aliases for easier access
        a = jTextField2;
        b = jTextField3;
        c = jTextField4;
        d = jTextField6;
        // Add the missing action listener for jButton3
        jButton3.addActionListener(this::jButton3ActionPerformed);
        this.setLocationRelativeTo(null);

        // Initialize ButtonGroup for radio buttons
        answerGroup = new javax.swing.ButtonGroup();
        answerGroup.add(jRadioButton1);
        answerGroup.add(jRadioButton3);
        answerGroup.add(jRadioButton4);
        answerGroup.add(jRadioButton5);

        // Load modules for the logged-in lecturer
        populateModulesDropdown();
    }
    
    /**
     * Constructor for editing an existing quiz question
     */
    public Quiz(String question, String ansA, String ansB, String ansC, String ansD, String correctAns) {
        initComponents();
        // Create aliases for easier access
        a = jTextField2;
        b = jTextField3;
        c = jTextField4;
        d = jTextField6;
        // Add the missing action listener for jButton3
        jButton3.addActionListener(this::jButton3ActionPerformed);
        this.setLocationRelativeTo(null);

        // Initialize ButtonGroup for radio buttons
        answerGroup = new javax.swing.ButtonGroup();
        answerGroup.add(jRadioButton1);
        answerGroup.add(jRadioButton3);
        answerGroup.add(jRadioButton4);
        answerGroup.add(jRadioButton5);

        // Mark as edit mode and store original values
        isEditMode = true;
        originalQuestion = question;
        originalA = ansA;
        originalB = ansB;
        originalC = ansC;
        originalD = ansD;
        originalCorrectAnswer = correctAns;

        // Pre-populate the fields
        jTextArea1.setText(question);
        a.setText(ansA);
        b.setText(ansB);
        c.setText(ansC);
        d.setText(ansD);

        // Set the correct answer radio button
        correctAnswer = correctAns;
        switch (correctAns.toUpperCase()) {
            case "A":
                jRadioButton1.setSelected(true);
                break;
            case "B":
                jRadioButton3.setSelected(true);
                break;
            case "C":
                jRadioButton4.setSelected(true);
                break;
            case "D":
                jRadioButton5.setSelected(true);
                break;
        }
    }

    private void populateModulesDropdown() {
        String projectRoot = System.getProperty("user.dir");
        File modulesFile = new File(projectRoot, "src\\main\\java\\oopwj\\modules.txt");

        if (!modulesFile.exists()) {
            JOptionPane.showMessageDialog(this, "Modules file not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(modulesFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String moduleID = parts[0].trim();
                    String moduleName = parts[1].trim();
                    String lecturerID = parts[3].trim();

                    // Add module to dropdown if it matches the lecturer's ID
                    if (lecturerID.equals(this.lecturerID)) {
                        jComboBox1.addItem(moduleID + " - " + moduleName);
                    }
                }
            }
        } catch (IOException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Failed to load modules: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getNextQuestionId(String moduleId, File quizFile) {
        int maxId = 0;

        if (quizFile.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(quizFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length > 2 && parts[0].trim().equals(moduleId)) {
                        String questionId = parts[1].trim(); // Assuming question ID is the 2nd column
                        if (questionId.startsWith("Q")) {
                            try {
                                int id = Integer.parseInt(questionId.substring(1));
                                if (id > maxId) {
                                    maxId = id;
                                }
                            } catch (NumberFormatException e) {
                                logger.log(java.util.logging.Level.WARNING, "Invalid question ID format: " + questionId, e);
                            }
                        }
                    }
                }
            } catch (IOException ex) {
                logger.log(java.util.logging.Level.SEVERE, "Error reading Quiz.txt", ex);
            }
        }

        // Increment the max ID and return the new question ID
        return String.format("Q%03d", maxId + 1);
    }

    private void loadQuizQuestions() {
        String projectRoot = System.getProperty("user.dir");
        File quizFile = new File(projectRoot, "src\\main\\java\\oopwj\\Quiz.txt");

        if (!quizFile.exists()) {
            JOptionPane.showMessageDialog(this, "Quiz file not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(quizFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 10) {
                    // Objective question
                    String moduleId = parts[0].trim();
                    String moduleName = parts[1].trim();
                    String questionId = parts[2].trim();
                    String question = parts[3].trim();
                    String ansA = parts[4].trim();
                    String ansB = parts[5].trim();
                    String ansC = parts[6].trim();
                    String ansD = parts[7].trim();
                    String correctAns = parts[8].trim();
                    String type = parts[9].trim();

                    // Process the objective question (e.g., add to UI or data structure)
                    System.out.println("Objective Question: " + question);
                } else if (parts.length == 5) {
                    // Subjective question
                    String moduleId = parts[0].trim();
                    String moduleName = parts[1].trim();
                    String questionId = parts[2].trim();
                    String question = parts[3].trim();
                    String type = parts[4].trim();

                    // Process the subjective question (e.g., add to UI or data structure)
                    System.out.println("Subjective Question: " + question);
                } else {
                    logger.warning("Invalid question format: " + line);
                }
            }
        } catch (IOException ex) {
            logger.log(java.util.logging.Level.SEVERE, "Error reading Quiz.txt", ex);
            JOptionPane.showMessageDialog(this, "Error reading Quiz.txt: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadQuizQuestionsToTable(javax.swing.JTable table) {
        String projectRoot = System.getProperty("user.dir");
        File quizFile = new File(projectRoot, "src\\main\\java\\oopwj\\Quiz.txt");

        if (!quizFile.exists()) {
            JOptionPane.showMessageDialog(this, "Quiz file not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) table.getModel();
        model.setRowCount(0); // Clear existing rows

        try (BufferedReader br = new BufferedReader(new FileReader(quizFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 10) {
                    // Objective question
                    String moduleId = parts[0].trim();
                    String moduleName = parts[1].trim();
                    String questionId = parts[2].trim();
                    String question = parts[3].trim();
                    String ansA = parts[4].trim();
                    String ansB = parts[5].trim();
                    String ansC = parts[6].trim();
                    String ansD = parts[7].trim();
                    String correctAns = parts[8].trim();

                    // Add to table
                    model.addRow(new Object[]{moduleId, moduleName, questionId, question, "Objective"});
                } else if (parts.length == 5) {
                    // Subjective question
                    String moduleId = parts[0].trim();
                    String moduleName = parts[1].trim();
                    String questionId = parts[2].trim();
                    String question = parts[3].trim();
                    String type = parts[4].trim();

                    // Add to table with sequence 0,1,2,4,3
                    model.addRow(new Object[]{moduleId, moduleName, questionId, type, question});
                } else {
                    logger.warning("Invalid question format: " + line);
                }
            }
        } catch (IOException ex) {
            logger.log(java.util.logging.Level.SEVERE, "Error reading Quiz.txt", ex);
            JOptionPane.showMessageDialog(this, "Error reading Quiz.txt: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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

        jRadioButton2 = new javax.swing.JRadioButton();
        jTextField5 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jTextField6 = new javax.swing.JTextField();
        jRadioButton5 = new javax.swing.JRadioButton();
        jRadioButton4 = new javax.swing.JRadioButton();
        jRadioButton3 = new javax.swing.JRadioButton();
        jRadioButton1 = new javax.swing.JRadioButton();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        isObjectiveCheckBox = new javax.swing.JCheckBox("Objective Question");
        isObjectiveCheckBox.setSelected(false); // Default to not objective

        jRadioButton2.setText("jRadioButton2");

        jTextField5.setText("jTextField2");
        jTextField5.addActionListener(this::jTextField5ActionPerformed);

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 17)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setText("Answer 1:");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 204));

        jButton1.setText("Save");
        jButton1.addActionListener(this::jButton1ActionPerformed);

        jButton2.setText("Exit and Discard");
        jButton2.addActionListener(this::jButton2ActionPerformed);

        jButton3.setText("Add");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { }));
        jComboBox1.addActionListener(this::jComboBox1ActionPerformed);

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 17)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 0, 0));
        jLabel8.setText("Module:");

        jButton4.setText("Clear");
        jButton4.addActionListener(this::jButton4ActionPerformed);

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.setForeground(new java.awt.Color(0, 0, 0));

        jPanel3.setBackground(new java.awt.Color(255, 255, 204));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 17)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Question:");

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jTextField3.addActionListener(this::jTextField3ActionPerformed);

        jTextField4.addActionListener(this::jTextField4ActionPerformed);

        jTextField6.addActionListener(this::jTextField6ActionPerformed);

        jRadioButton5.setForeground(new java.awt.Color(0, 0, 0));
        jRadioButton5.setText("D");
        jRadioButton5.addActionListener(this::jRadioButton5ActionPerformed);

        jRadioButton4.setForeground(new java.awt.Color(0, 0, 0));
        jRadioButton4.setText("C");
        jRadioButton4.addActionListener(this::jRadioButton4ActionPerformed);

        jRadioButton3.setForeground(new java.awt.Color(0, 0, 0));
        jRadioButton3.setText("B");
        jRadioButton3.addActionListener(this::jRadioButton3ActionPerformed);

        jRadioButton1.setForeground(new java.awt.Color(0, 0, 0));
        jRadioButton1.setText("A");
        jRadioButton1.addActionListener(this::jRadioButton1ActionPerformed);

        isObjectiveCheckBox.setSelected(false); // Default to not objective
        jPanel1.add(isObjectiveCheckBox); // Add to the panel

        // Add a listener to jTabbedPane1 to update the isObjectiveCheckBox state
        jTabbedPane1.addChangeListener(e -> {
            int selectedIndex = jTabbedPane1.getSelectedIndex();
            if (selectedIndex == 0) { // Objective tab
                isObjectiveCheckBox.setSelected(true);
            } else if (selectedIndex == 1) { // Subjective tab
                isObjectiveCheckBox.setSelected(false);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(jRadioButton4))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                                        .addComponent(jRadioButton3)
                                        .addGap(0, 0, Short.MAX_VALUE)))
                                .addGap(1, 1, 1))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jRadioButton1))
                            .addComponent(jRadioButton5, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(4, 4, 4)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel1)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 12, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jTextField4)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(11, 11, 11))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jRadioButton1))
                .addGap(10, 10, 10)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jRadioButton4)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jRadioButton3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jRadioButton5))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(9, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Objective", jPanel2);

        jPanel5.setBackground(new java.awt.Color(0, 0, 0));
        jPanel5.setForeground(new java.awt.Color(0, 0, 0));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 413, Short.MAX_VALUE)
        );

        jPanel6.setBackground(new java.awt.Color(255, 255, 204));
        jPanel6.setPreferredSize(new java.awt.Dimension(370, 425));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("Type your question:");

        jTextArea2.setColumns(20);
        jTextArea2.setRows(5);
        jScrollPane2.setViewportView(jTextArea2);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 343, Short.MAX_VALUE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(17, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 355, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 370, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Subjective", jPanel4);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 58, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton1))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel8)
                            .addGap(31, 31, 31)
                            .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 370, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(60, 60, 60))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(24, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addGap(18, 18, 18)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 460, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton4)
                    .addComponent(jButton1)
                    .addComponent(jButton3))
                .addGap(25, 25, 25))
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

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        // Save: check if TempQuiz.txt exists (from Add button)
        // If it exists, transfer its contents to Quiz.txt
        // Then also save the current form fields if they are not empty
        String projectRoot = System.getProperty("user.dir");
        File temp = new File(projectRoot, "src\\main\\java\\oopwj\\TempQuiz.txt");
        File quizFile = new File(projectRoot, "src\\main\\java\\oopwj\\Quiz.txt");

        // Check which tab is selected
        int selectedTabIndex = jTabbedPane1.getSelectedIndex();

        if (selectedTabIndex == 0) { // Objective tab
            String question = jTextArea1.getText().trim();
            String a1 = a.getText().trim();
            String a2 = b.getText().trim();
            String a3 = c.getText().trim();
            String a4 = d.getText().trim();

            if (question.isEmpty() || a1.isEmpty() || a2.isEmpty() || a3.isEmpty() || a4.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields for the objective question before saving.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (correctAnswer.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select the correct answer for the objective question.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } else if (selectedTabIndex == 1) { // Subjective tab
            String subjectiveQuestion = jTextArea2.getText().trim();

            if (subjectiveQuestion.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please type your subjective question before saving.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Validate module selection
            String selectedModule = (String) jComboBox1.getSelectedItem();
            if (selectedModule == null || selectedModule.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select a module before saving the question.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Get module ID and name
            String[] moduleParts = selectedModule.split(" - ", 2); // Split into ID and name
            String moduleId = moduleParts[0].trim();
            String moduleName = moduleParts.length > 1 ? moduleParts[1].trim() : "";

            // Get the next question ID
            String nextQuestionId = getNextQuestionId(moduleId, quizFile);

            // Append the subjective question to TempQuiz.txt
            try (BufferedWriter tempWriter = new BufferedWriter(new FileWriter(temp, true))) {
                tempWriter.write(moduleId + ", " + nextQuestionId + ", " + subjectiveQuestion + ", Subjective");
                tempWriter.newLine();
            } catch (IOException ex) {
                logger.log(java.util.logging.Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this, "Error writing to TempQuiz.txt: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            } finally {
                // Clear the subjective question field after adding or saving
                jTextArea2.setText("");
            }

            // Check if TempQuiz.txt already contains questions
            if (temp.exists()) {
                try (BufferedReader br = new BufferedReader(new FileReader(temp))) {
                    if (br.readLine() != null) {
                        // TempQuiz.txt is not empty, skip validation
                    }
                } catch (IOException ex) {
                    logger.log(java.util.logging.Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(this, "Error reading TempQuiz.txt: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        }

        // Confirm before saving
        int confirmResult = JOptionPane.showConfirmDialog(this, "Are you sure you want to save the questions?", "Confirm Save", JOptionPane.YES_NO_OPTION);
        if (confirmResult != JOptionPane.YES_OPTION) {
            return;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(quizFile, true))) {
            // First, transfer from TempQuiz.txt to Quiz.txt if it exists
            if (temp.exists()) {
                try (BufferedReader br = new BufferedReader(new FileReader(temp))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        bw.write(line);
                        bw.newLine();
                    }
                } catch (IOException ex) {
                    logger.log(java.util.logging.Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(this, "Failed to transfer from TempQuiz.txt: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // delete temporary file
                try {
                    Files.deleteIfExists(temp.toPath());
                } catch (IOException ex) {
                    logger.log(java.util.logging.Level.WARNING, null, ex);
                }

                // clear all fields including title
                jTextArea1.setText("");
                a.setText("");
                b.setText("");
                c.setText("");
                d.setText("");

                // reset session count after saving
                sessionQuestionCount = 0;

                JOptionPane.showMessageDialog(this, "Quiz saved successfully", "Saved", JOptionPane.INFORMATION_MESSAGE);
                return; // Exit without processing form fields
            }

            // Save the current form fields based on the selected tab
            StringBuilder sb = new StringBuilder();
            String selectedModule = (String) jComboBox1.getSelectedItem();
            String[] moduleParts = selectedModule.split(" - ", 2); // Split into ID and name
            String moduleId = moduleParts[0].trim();

            sb.append(csvEscape(moduleId)).append(", "); // Save the module ID first

            // Get the next question ID
            String nextQuestionId = getNextQuestionId(moduleId, quizFile);

            if (selectedTabIndex == 0) { // Objective tab
                String question = jTextArea1.getText().trim();
                String a1 = a.getText().trim();
                String a2 = b.getText().trim();
                String a3 = c.getText().trim();
                String a4 = d.getText().trim();

                sb.append(csvEscape(nextQuestionId))
                  .append(", ")
                  .append(csvEscape(question)).append(", ")
                  .append(csvEscape(a1)).append(", ")
                  .append(csvEscape(a2)).append(", ")
                  .append(csvEscape(a3)).append(", ")
                  .append(csvEscape(a4)).append(", ")
                  .append(csvEscape(correctAnswer)).append(", ")
                  .append("Objective");
            } else if (selectedTabIndex == 1) { // Subjective tab
                String subjectiveQuestion = jTextArea2.getText().trim();

                sb.append(csvEscape(nextQuestionId))
                  .append(", ")
                  .append(csvEscape(subjectiveQuestion)).append(", ")
                  .append("Subjective");
            }

            bw.write(sb.toString());
            bw.newLine();
        } catch (IOException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Failed to save quiz: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // clear all fields including title
        jTextArea1.setText("");
        jTextArea2.setText("");
        a.setText("");
        b.setText("");
        c.setText("");
        d.setText("");
        answerGroup.clearSelection();
        correctAnswer = "";

        // reset session count after saving
        sessionQuestionCount = 0;

        JOptionPane.showMessageDialog(this, "Quiz saved successfully", "Saved", JOptionPane.INFORMATION_MESSAGE);
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        // Check if there's anything to discard
        String projectRoot = System.getProperty("user.dir");
        File temp = new File(projectRoot, "src\\main\\java\\oopwj\\TempQuiz.txt");
        
        String question = jTextArea1.getText().trim();
        String a1 = a.getText().trim();
        String a2 = b.getText().trim();
        String a3 = c.getText().trim();
        String a4 = d.getText().trim();
        
        boolean needsConfirmation = false;
        
        // Check if TempQuiz.txt exists (new questions added)
        if (temp.exists()) {
            needsConfirmation = true;
        }
        // Check if in edit mode and values have changed
        else if (isEditMode) {
            boolean valuesChanged = !question.equals(originalQuestion) ||
                                    !a1.equals(originalA) ||
                                    !a2.equals(originalB) ||
                                    !a3.equals(originalC) ||
                                    !a4.equals(originalD) ||
                                    !correctAnswer.equals(originalCorrectAnswer);
            if (valuesChanged) {
                needsConfirmation = true;
            }
        }
        // Check if not in edit mode but has form data
        else {
            boolean hasFormData = !question.isEmpty() || !a1.isEmpty() || !a2.isEmpty() || !a3.isEmpty() || !a4.isEmpty();
            if (hasFormData) {
                needsConfirmation = true;
            }
        }
        
        // Only confirm if there's something to discard
        if (needsConfirmation) {
            int confirmResult = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit and discard all changes?", "Confirm Exit", JOptionPane.YES_NO_OPTION);
            if (confirmResult != JOptionPane.YES_OPTION) {
                return;
            }
        }

        // Discard: remove TempQuiz.txt and return to Assessments
        if (temp.exists()) {
            boolean deleted = temp.delete();
            if (!deleted) {
                try {
                    Files.deleteIfExists(temp.toPath());
                } catch (IOException ex) {
                    logger.log(java.util.logging.Level.WARNING, null, ex);
                }
            }
        }

        // reset session count after discarding
        sessionQuestionCount = 0;

        // open Assessments
        java.awt.EventQueue.invokeLater(() -> {
            if (parentWindow != null) {
                parentWindow.setVisible(true);
            } else {
                new Assessments(lecturerID, null).setVisible(true);
            }
        });
        this.dispose();
    }

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {
        // Add: validate fields, append current entry to TemporaryQuiz.txt, then clear fields
        int selectedTabIndex = jTabbedPane1.getSelectedIndex();

        String projectRoot = System.getProperty("user.dir");
        File temp = new File(projectRoot, "src\\main\\java\\oopwj\\TempQuiz.txt");

        if (selectedTabIndex == 0) { // Objective tab
            String question = jTextArea1.getText().trim();
            String a1 = a.getText().trim();
            String a2 = b.getText().trim();
            String a3 = c.getText().trim();
            String a4 = d.getText().trim();

            if (question.isEmpty() || a1.isEmpty() || a2.isEmpty() || a3.isEmpty() || a4.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields before adding.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Check if correct answer is selected
            if (correctAnswer.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select the correct answer.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(temp, true))) {
                sessionQuestionCount++;

                StringBuilder sb = new StringBuilder();
                sb.append(csvEscape(String.format("Q%03d", sessionQuestionCount))).append(", ");
                sb.append(csvEscape(question)).append(", ");
                sb.append(csvEscape(a1)).append(", ");
                sb.append(csvEscape(a2)).append(", ");
                sb.append(csvEscape(a3)).append(", ");
                sb.append(csvEscape(a4)).append(", ");
                sb.append(csvEscape(correctAnswer)).append(", ");
                sb.append("Objective");

                bw.write(sb.toString());
                bw.newLine();
            } catch (IOException ex) {
                logger.log(java.util.logging.Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this, "Failed to append question: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Clear fields except title
            jTextArea1.setText("");
            a.setText("");
            b.setText("");
            c.setText("");
            d.setText("");
            answerGroup.clearSelection();
            correctAnswer = "";

            JOptionPane.showMessageDialog(this, "Objective question added successfully", "Added", JOptionPane.INFORMATION_MESSAGE);
        } else if (selectedTabIndex == 1) { // Subjective tab
            String subjectiveQuestion = jTextArea2.getText().trim();

            if (subjectiveQuestion.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please type your subjective question before adding.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(temp, true))) {
                sessionQuestionCount++;

                StringBuilder sb = new StringBuilder();
                sb.append(csvEscape(String.format("Q%03d", sessionQuestionCount))).append(", ");
                sb.append(csvEscape(subjectiveQuestion)).append(", ");
                sb.append("Subjective");

                bw.write(sb.toString());
                bw.newLine();
            } catch (IOException ex) {
                logger.log(java.util.logging.Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this, "Failed to append question: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Clear the subjective question field after adding or saving
            jTextArea2.setText("");

            JOptionPane.showMessageDialog(this, "Subjective question added successfully", "Added", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void jTextField4ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void jTextField5ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void jTextField6ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        correctAnswer = "A";
    }

    private void jRadioButton3ActionPerformed(java.awt.event.ActionEvent evt) {
        correctAnswer = "B";
    }

    private void jRadioButton4ActionPerformed(java.awt.event.ActionEvent evt) {
        correctAnswer = "C";
    }

    private void jRadioButton5ActionPerformed(java.awt.event.ActionEvent evt) {
        correctAnswer = "D";
    }

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {
        // Clear all form fields
        jTextArea1.setText("");
        a.setText("");
        b.setText("");
        c.setText("");
        d.setText("");
        answerGroup.clearSelection();
        correctAnswer = "";
    }

    // Helper: escape a field for simple CSV (wrap in double quotes if needed)
    private String csvEscape(String s) {
        if (s == null) return "";
        String value = s.replace("\r", "").replace("\n", " ").trim();
        boolean needsQuotes = value.contains(",") || value.contains("\"") || value.contains(" ");
        if (needsQuotes) {
            value = value.replace("\"", "\"\"");
            return "\"" + value + "\"";
        }
        return value;
    }

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
        java.awt.EventQueue.invokeLater(() -> new Quiz().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JRadioButton jRadioButton5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JCheckBox isObjectiveCheckBox;
    // End of variables declaration//GEN-END:variables
}
