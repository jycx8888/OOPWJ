/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package oopwj.Lecturer;

/**
 *
 * @author User
 */
public class View_Grade extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(View_Grade.class.getName());
    private String lecturerID;
    private String moduleID;
    private String quizID;
    private String studentID;
    private java.util.Map<String, Integer> maxMarksMap = new java.util.HashMap<>();
    private java.util.Map<String, Integer> subjectiveGrades = new java.util.HashMap<>(); // Stores questionID -> marks for subjective questions

    /**
     * Creates new form View_Grade
     */
    public View_Grade() {
        initComponents();
        setLocationRelativeTo(null);
        jButton1.addActionListener(this::jButton1ActionPerformed);
    }
    
    /**
     * Creates new form View_Grade with moduleID, quizID, studentID, and lecturerID
     */
    public View_Grade(String moduleID, String quizID, String studentID, String lecturerID) {
        this.lecturerID = lecturerID;
        this.moduleID = moduleID;
        this.quizID = quizID;
        this.studentID = studentID;
        initComponents();
        setLocationRelativeTo(null);
        jButton1.addActionListener(this::jButton1ActionPerformed);
        jButton2.addActionListener(this::jButton2ActionPerformed);
        jButton3.addActionListener(this::jButton3ActionPerformed);
        jButton4.addActionListener(this::jButton4ActionPerformed);
        loadMaxMarks(moduleID, quizID);
        setModuleAndStudentInfo(moduleID, studentID);
        loadQuestionIDs(moduleID, quizID);
        loadExistingGrades(); // Load already saved grades from Grade.txt
        setupSpinnerValidation();
        setupSpinnerChangeListener(); // Listen for mark changes
    }
    
    /**
     * Sets the module ID, quiz ID, and student ID on the labels
     */
    public void setModuleAndStudentInfo(String moduleID, String studentID) {
        jLabel7.setText("Module ID: " + moduleID);
        jLabel8.setText("Student ID: " + studentID);
        jLabel9.setText("Quiz ID: " + quizID);
    }
    
    /**
     * Loads maximum marks for each question from TotalQuizMark.txt
     */
    private void loadMaxMarks(String moduleID, String quizID) {
        String projectRoot = System.getProperty("user.dir");
        String totalQuizMarkPath = projectRoot + "/src/main/java/oopwj/TotalQuizMark.txt";
        
        java.io.File totalQuizMarkFile = new java.io.File(totalQuizMarkPath);
        if (!totalQuizMarkFile.exists()) {
            logger.log(java.util.logging.Level.WARNING, "TotalQuizMark.txt not found at: " + totalQuizMarkFile.getAbsolutePath());
            return;
        }
        
        maxMarksMap.clear();
        
        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(totalQuizMarkFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                
                // Format: ModuleID,QuizID,QuestionID,TotalMarks
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    String fileModuleID = parts[0].trim();
                    String fileQuizID = parts[1].trim();
                    String fileQuestionID = parts[2].trim();
                    String totalMarks = parts[3].trim();
                    
                    if (fileModuleID.equals(moduleID) && fileQuizID.equals(quizID)) {
                        try {
                            int marks = Integer.parseInt(totalMarks);
                            maxMarksMap.put(fileQuestionID, marks);
                            logger.log(java.util.logging.Level.INFO, "Loaded max marks for " + fileQuestionID + ": " + marks);
                        } catch (NumberFormatException e) {
                            logger.log(java.util.logging.Level.WARNING, "Invalid marks format for question " + fileQuestionID + ": " + totalMarks);
                        }
                    }
                }
            }
            logger.log(java.util.logging.Level.INFO, "Loaded " + maxMarksMap.size() + " max marks entries");
        } catch (java.io.IOException e) {
            logger.log(java.util.logging.Level.SEVERE, "Error reading TotalQuizMark.txt: " + e.getMessage(), e);
        }
    }
    
    /**
     * Sets up validation for the marks spinner to prevent exceeding maximum marks
     */
    private void setupSpinnerValidation() {
        jSpinner2.addChangeListener(e -> {
            String selectedQuestionID = (String) jComboBox1.getSelectedItem();
            if (selectedQuestionID != null && maxMarksMap.containsKey(selectedQuestionID)) {
                int maxMarks = maxMarksMap.get(selectedQuestionID);
                int currentValue = (Integer) jSpinner2.getValue();
                
                if (currentValue > maxMarks) {
                    jSpinner2.setValue(maxMarks);
                    javax.swing.JOptionPane.showMessageDialog(this, 
                        "Marks cannot exceed maximum marks of " + maxMarks + " for this question.", 
                        "Invalid Marks", 
                        javax.swing.JOptionPane.WARNING_MESSAGE);
                } else if (currentValue < 0) {
                    jSpinner2.setValue(0);
                    javax.swing.JOptionPane.showMessageDialog(this, 
                        "Marks cannot be negative.", 
                        "Invalid Marks", 
                        javax.swing.JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }
    
    /**
     * Sets up listener to save marks when spinner value changes
     */
    private void setupSpinnerChangeListener() {
        jSpinner2.addChangeListener(e -> {
            String selectedQuestionID = (String) jComboBox1.getSelectedItem();
            if (selectedQuestionID != null && isSubjectiveQuestion(selectedQuestionID)) {
                int marks = (Integer) jSpinner2.getValue();
                subjectiveGrades.put(selectedQuestionID, marks);
                logger.log(java.util.logging.Level.INFO, "Stored grade for " + selectedQuestionID + ": " + marks);
            }
        });
    }
    
    /**
     * Loads existing grades from Grade.txt for this student/module/quiz
     */
    private void loadExistingGrades() {
        String projectRoot = System.getProperty("user.dir");
        String gradeFilePath = projectRoot + "/src/main/java/oopwj/Grade.txt";
        
        java.io.File gradeFile = new java.io.File(gradeFilePath);
        if (!gradeFile.exists()) {
            logger.log(java.util.logging.Level.INFO, "Grade.txt not found, starting fresh");
            return;
        }
        
        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(gradeFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                
                // Format: studentID,moduleID,quizID,questionID,QuestionType,CorrectAnswer,mark
                String[] parts = line.split(",");
                if (parts.length >= 7) {
                    String fileStudentID = parts[0].trim();
                    String fileModuleID = parts[1].trim();
                    String fileQuizID = parts[2].trim();
                    String fileQuestionID = parts[3].trim();
                    String questionType = parts[4].trim();
                    String markStr = parts[6].trim();
                    
                    // Load subjective grades for this student/module/quiz
                    if (fileStudentID.equals(studentID) && 
                        fileModuleID.equals(moduleID) && 
                        fileQuizID.equals(quizID) &&
                        "Subjective".equalsIgnoreCase(questionType)) {
                        
                        try {
                            int mark = Integer.parseInt(markStr);
                            subjectiveGrades.put(fileQuestionID, mark);
                            logger.log(java.util.logging.Level.INFO, "Loaded existing grade for " + fileQuestionID + ": " + mark);
                        } catch (NumberFormatException e) {
                            logger.log(java.util.logging.Level.WARNING, "Invalid mark format: " + markStr);
                        }
                    }
                }
            }
        } catch (java.io.IOException e) {
            logger.log(java.util.logging.Level.SEVERE, "Error reading Grade.txt: " + e.getMessage(), e);
        }
    }
    
    /**
     * Loads all QuestionIDs for the given moduleID and quizID from question.txt
     */
    private void loadQuestionIDs(String moduleID, String quizID) {
        String projectRoot = System.getProperty("user.dir");
        String quizFilePath = projectRoot + "/src/main/java/oopwj/question.txt";
        
        java.io.File quizFile = new java.io.File(quizFilePath);
        if (!quizFile.exists()) {
            logger.log(java.util.logging.Level.SEVERE, "question.txt not found at: " + quizFile.getAbsolutePath());
            javax.swing.JOptionPane.showMessageDialog(this, 
                "question.txt not found at: " + quizFile.getAbsolutePath(), 
                "File Not Found", 
                javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Clear existing items
        jComboBox1.removeAllItems();
        
        java.util.Set<String> questionIDs = new java.util.LinkedHashSet<>();
        
        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(quizFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String questionID = parts[0].trim();
                    String fileQuizID = parts[1].trim();
                    String fileModuleID = parts[2].trim();
                    
                    if (fileModuleID.equals(moduleID) && fileQuizID.equals(quizID)) {
                        questionIDs.add(questionID);
                    }
                }
            }
            
            // Populate combo box
            for (String qid : questionIDs) {
                jComboBox1.addItem(qid);
            }
            
            logger.log(java.util.logging.Level.INFO, "Loaded " + questionIDs.size() + " questions for module: " + moduleID + ", quiz: " + quizID);
            
        } catch (java.io.IOException e) {
            logger.log(java.util.logging.Level.SEVERE, "Error reading question.txt: " + e.getMessage(), e);
            javax.swing.JOptionPane.showMessageDialog(this, 
                "Error reading question.txt: " + e.getMessage(), 
                "File Error", 
                javax.swing.JOptionPane.ERROR_MESSAGE);
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

        jSpinner1 = new javax.swing.JSpinner();
        jPanel4 = new javax.swing.JPanel();
        jLayeredPane2 = new javax.swing.JLayeredPane();
        Objective = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jTextArea1 = new javax.swing.JTextArea();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jLabel1 = new javax.swing.JLabel();
        Subjective = new javax.swing.JPanel();
        jTextArea2 = new javax.swing.JTextArea();
        jLabel4 = new javax.swing.JLabel();
        jTextArea3 = new javax.swing.JTextArea();
        jLabel5 = new javax.swing.JLabel();
        jSpinner2 = new javax.swing.JSpinner();
        jLabel6 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximumSize(null);
        setMinimumSize(null);
        setSize(new java.awt.Dimension(734, 617));

        jPanel4.setBackground(new java.awt.Color(255, 255, 204));
        jPanel4.setForeground(new java.awt.Color(255, 255, 255));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLayeredPane2.setLayout(new java.awt.CardLayout());

        Objective.setBackground(new java.awt.Color(255, 255, 255));
        Objective.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jTextField1.setEditable(false);
        jTextField1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jTextField1.addActionListener(this::jTextField1ActionPerformed);

        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("Answer:");

        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("Correct Answer:");

        jTextField2.setEditable(false);
        jTextField2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jRadioButton1.setForeground(new java.awt.Color(0, 0, 0));
        jRadioButton1.setText("Incorrect");
        jRadioButton1.addActionListener(this::jRadioButton1ActionPerformed);

        jRadioButton2.setForeground(new java.awt.Color(0, 0, 0));
        jRadioButton2.setText("Correct");

        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Question:");

        javax.swing.GroupLayout ObjectiveLayout = new javax.swing.GroupLayout(Objective);
        Objective.setLayout(ObjectiveLayout);
        ObjectiveLayout.setHorizontalGroup(
            ObjectiveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ObjectiveLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jRadioButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jRadioButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(108, 108, 108))
            .addGroup(ObjectiveLayout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(ObjectiveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addGroup(ObjectiveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jTextField2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE)
                        .addComponent(jTextField1, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(ObjectiveLayout.createSequentialGroup()
                .addContainerGap(30, Short.MAX_VALUE)
                .addGroup(ObjectiveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextArea1, javax.swing.GroupLayout.PREFERRED_SIZE, 356, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(30, Short.MAX_VALUE))
        );
        ObjectiveLayout.setVerticalGroup(
            ObjectiveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ObjectiveLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextArea1, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 44, Short.MAX_VALUE)
                .addGroup(ObjectiveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton1)
                    .addComponent(jRadioButton2))
                .addGap(28, 28, 28))
        );

        jLayeredPane2.add(Objective, "card3");

        Subjective.setBackground(new java.awt.Color(255, 255, 255));
        Subjective.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jTextArea2.setEditable(false);
        jTextArea2.setColumns(20);
        jTextArea2.setRows(5);
        jTextArea2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("Question:");

        jTextArea3.setEditable(false);
        jTextArea3.setColumns(20);
        jTextArea3.setRows(5);
        jTextArea3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("Answer:");

        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setText("Marks Given:");

        javax.swing.GroupLayout SubjectiveLayout = new javax.swing.GroupLayout(Subjective);
        Subjective.setLayout(SubjectiveLayout);
        SubjectiveLayout.setHorizontalGroup(
            SubjectiveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SubjectiveLayout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(SubjectiveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(SubjectiveLayout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSpinner2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(SubjectiveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jTextArea3, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTextArea2, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(51, Short.MAX_VALUE))
        );
        SubjectiveLayout.setVerticalGroup(
            SubjectiveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SubjectiveLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextArea2, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextArea3, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 48, Short.MAX_VALUE)
                .addGroup(SubjectiveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jSpinner2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGap(41, 41, 41))
        );

        jLayeredPane2.add(Subjective, "card2");

        jPanel4.add(jLayeredPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 150, 420, 420));

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.addActionListener(this::jComboBox1ActionPerformed);
        jPanel4.add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 100, 210, 30));

        jButton1.setText("Exit");
        jPanel4.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 650, -1, -1));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setText("Module ID:");
        jPanel4.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 40, 190, -1));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 0, 0));
        jLabel8.setText("Student ID:");
        jPanel4.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 70, 200, -1));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 0, 0));
        jLabel9.setText("Quiz ID: ");
        jPanel4.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 10, 170, -1));

        jButton2.setText("Previous");
        jPanel4.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 580, -1, -1));

        jButton3.setText("Next");
        jPanel4.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 580, -1, -1));

        jButton4.setText("Save");
        jPanel4.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 580, -1, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 501, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 704, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jRadioButton1ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // Get the selected question ID from the combo box
        String selectedQuestionID = (String) jComboBox1.getSelectedItem();
        if (selectedQuestionID == null || selectedQuestionID.isEmpty()) {
            return;
        }
        
        // Load question data from question.txt
        loadQuestionData(selectedQuestionID);
        
        // Load student answer from answers.txt
        loadStudentAnswer(selectedQuestionID);
        
        // Update spinner max value and label for subjective questions
        updateMaxMarksDisplay(selectedQuestionID);
    }//GEN-LAST:event_jComboBox1ActionPerformed
    
    /**
     * Loads question data from question.txt and displays it in the appropriate panel
     */
    private void loadQuestionData(String questionID) {
        String projectRoot = System.getProperty("user.dir");
        String questionFilePath = projectRoot + "/src/main/java/oopwj/question.txt";
        
        java.io.File questionFile = new java.io.File(questionFilePath);
        if (!questionFile.exists()) {
            logger.log(java.util.logging.Level.SEVERE, "question.txt not found");
            return;
        }
        
        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(questionFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String fileQuestionID = parts[0].trim();
                    String fileQuizID = parts[1].trim();
                    String fileModuleID = parts[2].trim();
                    
                    if (fileQuestionID.equals(questionID) && fileQuizID.equals(quizID) && fileModuleID.equals(moduleID)) {
                        if (parts.length >= 10) {
                            // Objective question format: QuestionID,QuizID,ModuleID,Question,A,B,C,D,CorrectAnswer,Type
                            String question = parts[3].trim().replaceAll("^\\\"|\\\"$", "");
                            String answerA = parts[4].trim().replaceAll("^\\\"|\\\"$", "");
                            String answerB = parts[5].trim().replaceAll("^\\\"|\\\"$", "");
                            String answerC = parts[6].trim().replaceAll("^\\\"|\\\"$", "");
                            String answerD = parts[7].trim().replaceAll("^\\\"|\\\"$", "");
                            String correctAnswer = parts[8].trim().replaceAll("^\\\"|\\\"$", "");
                            
                            // Fill objective panel
                            jTextArea1.setText(question);
                            jTextField1.setText(answerA + " / " + answerB + " / " + answerC + " / " + answerD);
                            jTextField2.setText(correctAnswer);
                            
                            // Show objective panel
                            swapPanels("Objective");
                        } else if (parts.length >= 5) {
                            // Subjective question format: QuestionID,QuizID,ModuleID,Question,Type
                            String question = parts[3].trim().replaceAll("^\\\"|\\\"$", "");
                            
                            // Fill subjective panel
                            jTextArea2.setText(question);
                            
                            // Note: Spinner value will be set by updateMaxMarksDisplay() which is called after this
                            
                            // Show subjective panel
                            swapPanels("Subjective");
                        }
                        return;
                    }
                }
            }
        } catch (java.io.IOException e) {
            logger.log(java.util.logging.Level.SEVERE, "Error reading question.txt: " + e.getMessage(), e);
        }
    }
    
    /**
     * Updates the display of maximum marks for the selected question
     */
    private void updateMaxMarksDisplay(String questionID) {
        if (maxMarksMap.containsKey(questionID)) {
            int maxMarks = maxMarksMap.get(questionID);
            
            // Get the saved mark for this question (if exists)
            int savedMark = subjectiveGrades.getOrDefault(questionID, 0);
            
            // Set spinner model with proper range and saved value
            javax.swing.SpinnerNumberModel spinnerModel = new javax.swing.SpinnerNumberModel(savedMark, 0, maxMarks, 1);
            jSpinner2.setModel(spinnerModel);
            
            logger.log(java.util.logging.Level.INFO, "Max marks for " + questionID + " set to: " + maxMarks + ", current value: " + savedMark);
        } else {
            logger.log(java.util.logging.Level.WARNING, "No max marks found for question: " + questionID);
        }
    }
    
    /**
     * Loads student answer from answers.txt and displays it
     */
    private void loadStudentAnswer(String questionID) {
        String projectRoot = System.getProperty("user.dir");
        String answersFilePath = projectRoot + "/src/main/java/oopwj/answers.txt";
        
        java.io.File answersFile = new java.io.File(answersFilePath);
        if (!answersFile.exists()) {
            logger.log(java.util.logging.Level.SEVERE, "answers.txt not found");
            return;
        }
        
        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(answersFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                
                // Format: StudentID,ModuleID,QuizID,QuestionID,QuestionType,Answer
                String[] parts = line.split(",");
                if (parts.length >= 6) {
                    String fileStudentID = parts[0].trim();
                    String fileModuleID = parts[1].trim();
                    String fileQuizID = parts[2].trim();
                    String fileQuestionID = parts[3].trim();
                    String questionType = parts[4].trim();
                    String answer = parts[5].trim();
                    
                    if (fileStudentID.equals(studentID) && fileModuleID.equals(moduleID) && 
                        fileQuizID.equals(quizID) && fileQuestionID.equals(questionID)) {
                        
                        if ("Objective".equalsIgnoreCase(questionType)) {
                            // Show student's objective answer
                            jTextField1.setText(answer);
                            // Set objective answer radio buttons
                            if ("A".equalsIgnoreCase(answer)) {
                                jRadioButton1.setSelected(true);
                            } else if ("B".equalsIgnoreCase(answer)) {
                                jRadioButton2.setSelected(true);
                            }
                            // Add more radio buttons if you have C and D
                        } else if ("Subjective".equalsIgnoreCase(questionType)) {
                            // Set subjective answer
                            jTextArea3.setText(answer);
                        }
                        return;
                    }
                }
            }
        } catch (java.io.IOException e) {
            logger.log(java.util.logging.Level.SEVERE, "Error reading answers.txt: " + e.getMessage(), e);
        }
    }
    
    /**
     * Swaps the visible panel between Objective and Subjective based on question type
     * @param questionType the type of question (Subjective or Objective)
     */
    private void swapPanels(String questionType) {
        java.awt.CardLayout cl = (java.awt.CardLayout) jLayeredPane2.getLayout();
        
        if ("Subjective".equalsIgnoreCase(questionType)) {
            cl.show(jLayeredPane2, "card2");
            logger.log(java.util.logging.Level.INFO, "Showing Subjective panel");
        } else if ("Objective".equalsIgnoreCase(questionType)) {
            cl.show(jLayeredPane2, "card3");
            logger.log(java.util.logging.Level.INFO, "Showing Objective panel");
        } else {
            logger.log(java.util.logging.Level.WARNING, "Unknown question type: " + questionType);
        }
    }
    
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        // Navigate back to Grade_Assessment
        Grade_Assessment gradeAssessment = new Grade_Assessment(lecturerID);
        gradeAssessment.setVisible(true);
        this.dispose();
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        // Previous button - navigate to previous question
        int currentIndex = jComboBox1.getSelectedIndex();
        if (currentIndex > 0) {
            jComboBox1.setSelectedIndex(currentIndex - 1);
        } else {
            javax.swing.JOptionPane.showMessageDialog(this, 
                "This is the first question.", 
                "Information", 
                javax.swing.JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {
        // Next button - navigate to next question
        int currentIndex = jComboBox1.getSelectedIndex();
        int totalQuestions = jComboBox1.getItemCount();
        if (currentIndex < totalQuestions - 1) {
            jComboBox1.setSelectedIndex(currentIndex + 1);
        } else {
            javax.swing.JOptionPane.showMessageDialog(this, 
                "This is the last question.", 
                "Information", 
                javax.swing.JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {
        // Save button - save ALL subjective question grades to Grade.txt
        
        // Check if there are any subjective grades to save
        if (subjectiveGrades.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, 
                "No subjective questions have been graded yet.", 
                "No Grades to Save", 
                javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Save all subjective grades to Grade.txt
        saveAllSubjectiveGrades();
        
        // Calculate and save final grade to FinalGrade.txt
        calculateAndSaveFinalGrade();
        
        // Show confirmation with count
        int gradedCount = subjectiveGrades.size();
        StringBuilder message = new StringBuilder("Successfully saved " + gradedCount + " subjective grade(s):\n\n");
        for (java.util.Map.Entry<String, Integer> entry : subjectiveGrades.entrySet()) {
            message.append(entry.getKey()).append(": ").append(entry.getValue()).append(" marks\n");
        }
        
        javax.swing.JOptionPane.showMessageDialog(this, 
            message.toString(), 
            "Grades Saved Successfully", 
            javax.swing.JOptionPane.INFORMATION_MESSAGE);
        
        logger.log(java.util.logging.Level.INFO, "Saved " + gradedCount + " subjective grades to Grade.txt");
    }
    
    /**
     * Checks if the given question is a subjective question
     */
    private boolean isSubjectiveQuestion(String questionID) {
        String projectRoot = System.getProperty("user.dir");
        String questionFilePath = projectRoot + "/src/main/java/oopwj/question.txt";
        
        java.io.File questionFile = new java.io.File(questionFilePath);
        if (!questionFile.exists()) {
            logger.log(java.util.logging.Level.SEVERE, "question.txt not found");
            return false;
        }
        
        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(questionFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String fileQuestionID = parts[0].trim();
                    String fileQuizID = parts[1].trim();
                    String fileModuleID = parts[2].trim();
                    
                    if (fileQuestionID.equals(questionID) && fileQuizID.equals(quizID) && fileModuleID.equals(moduleID)) {
                        // Check question type - subjective has fewer columns (5) vs objective (10)
                        String questionType = parts[parts.length - 1].trim();
                        return "Subjective".equalsIgnoreCase(questionType);
                    }
                }
            }
        } catch (java.io.IOException e) {
            logger.log(java.util.logging.Level.SEVERE, "Error reading question.txt: " + e.getMessage(), e);
        }
        
        return false;
    }
    
    /**
     * Compares two questionIDs numerically (e.g., Q002 < Q010)
     */
    private int compareQuestionIDs(String q1, String q2) {
        try {
            // Extract numeric part from question IDs (e.g., "Q006" -> 6)
            int num1 = Integer.parseInt(q1.replaceAll("[^0-9]", ""));
            int num2 = Integer.parseInt(q2.replaceAll("[^0-9]", ""));
            return Integer.compare(num1, num2);
        } catch (NumberFormatException e) {
            // If parsing fails, fall back to string comparison
            return q1.compareTo(q2);
        }
    }
    
    /**
     * Loads subjective question answers from answers.txt for the current student/module/quiz
     * Returns map of questionID -> answer
     */
    private java.util.Map<String, String> loadSubjectiveAnswers() {
        java.util.Map<String, String> answers = new java.util.HashMap<>();
        String projectRoot = System.getProperty("user.dir");
        String answersFilePath = projectRoot + "/src/main/java/oopwj/answers.txt";
        
        java.io.File answersFile = new java.io.File(answersFilePath);
        if (!answersFile.exists()) {
            logger.log(java.util.logging.Level.WARNING, "answers.txt not found at: " + answersFilePath);
            return answers;
        }
        
        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(answersFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                
                // Format: StudentID,ModuleID,QuizID,QuestionID,QuestionType,Answer
                String[] parts = line.split(",", 6); // Split into max 6 parts to preserve commas in answer
                if (parts.length >= 6) {
                    String fileStudentID = parts[0].trim();
                    String fileModuleID = parts[1].trim();
                    String fileQuizID = parts[2].trim();
                    String fileQuestionID = parts[3].trim();
                    String questionType = parts[4].trim();
                    String answer = parts[5].trim(); // Answer may contain commas
                    
                    // Match student, module, quiz, and question type
                    if (fileStudentID.equals(studentID) && 
                        fileModuleID.equals(moduleID) && 
                        fileQuizID.equals(quizID) &&
                        "Subjective".equalsIgnoreCase(questionType)) {
                        answers.put(fileQuestionID, answer);
                    }
                }
            }
            logger.log(java.util.logging.Level.INFO, "Loaded " + answers.size() + " subjective answers from answers.txt");
        } catch (java.io.IOException e) {
            logger.log(java.util.logging.Level.SEVERE, "Error reading answers.txt: " + e.getMessage(), e);
        }
        
        return answers;
    }
    
    /**
     * Saves all subjective question grades in Grade.txt
     * Format: studentID,moduleID,quizID,questionID,Subjective,Answer,mark
     */
    private void saveAllSubjectiveGrades() {
        String projectRoot = System.getProperty("user.dir");
        String gradeFilePath = projectRoot + "/src/main/java/oopwj/Grade.txt";
        
        java.io.File gradeFile = new java.io.File(gradeFilePath);
        
        // Load student answers for subjective questions from answers.txt
        java.util.Map<String, String> subjectiveAnswers = loadSubjectiveAnswers();
        
        // Separate lists for different types of lines
        java.util.List<String> otherLines = new java.util.ArrayList<>(); // Lines not related to this student/module/quiz
        java.util.List<String> currentQuizGrades = new java.util.ArrayList<>(); // Grades for current student/module/quiz
        java.util.Set<String> processedQuestions = new java.util.HashSet<>(); // Track which questions we've already handled
        
        if (gradeFile.exists()) {
            try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(gradeFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String originalLine = line;
                    line = line.trim();
                    
                    if (line.isEmpty() || line.startsWith("#")) {
                        otherLines.add(originalLine);
                        continue;
                    }
                    
                    // Format: studentID,moduleID,quizID,questionID,QuestionType,CorrectAnswer,mark
                    String[] parts = line.split(",");
                    if (parts.length >= 7) {
                        String fileStudentID = parts[0].trim();
                        String fileModuleID = parts[1].trim();
                        String fileQuizID = parts[2].trim();
                        String fileQuestionID = parts[3].trim();
                        String questionType = parts[4].trim();
                        
                        // Check if this grade belongs to current student/module/quiz
                        if (fileStudentID.equals(studentID) && 
                            fileModuleID.equals(moduleID) && 
                            fileQuizID.equals(quizID)) {
                            
                            // Prevent duplicates - skip if we already processed this question
                            if (processedQuestions.contains(fileQuestionID)) {
                                logger.log(java.util.logging.Level.WARNING, "Skipping duplicate entry for question: " + fileQuestionID);
                                continue;
                            }
                            
                            // If it's a subjective question we're updating, use new value
                            if ("Subjective".equalsIgnoreCase(questionType) &&
                                subjectiveGrades.containsKey(fileQuestionID)) {
                                
                                int newMark = subjectiveGrades.get(fileQuestionID);
                                String answer = subjectiveAnswers.getOrDefault(fileQuestionID, "N/A");
                                String updatedLine = studentID + "," + moduleID + "," + quizID + "," + 
                                                   fileQuestionID + ",Subjective," + answer + "," + newMark;
                                currentQuizGrades.add(updatedLine);
                                processedQuestions.add(fileQuestionID);
                                logger.log(java.util.logging.Level.INFO, "Updated existing grade: " + updatedLine);
                            } else {
                                // Keep existing grade (objective or not updated subjective)
                                currentQuizGrades.add(originalLine);
                                processedQuestions.add(fileQuestionID);
                            }
                        } else {
                            // Different student/module/quiz - keep in other lines
                            otherLines.add(originalLine);
                        }
                    } else {
                        otherLines.add(originalLine);
                    }
                }
            } catch (java.io.IOException e) {
                logger.log(java.util.logging.Level.SEVERE, "Error reading Grade.txt: " + e.getMessage(), e);
                javax.swing.JOptionPane.showMessageDialog(this,
                    "Error reading grades file: " + e.getMessage(),
                    "File Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        
        // Add new subjective grades that weren't in the file
        for (java.util.Map.Entry<String, Integer> entry : subjectiveGrades.entrySet()) {
            String questionID = entry.getKey();
            int marks = entry.getValue();
            
            if (!processedQuestions.contains(questionID)) {
                String answer = subjectiveAnswers.getOrDefault(questionID, "N/A");
                String newGrade = studentID + "," + moduleID + "," + quizID + "," + 
                                questionID + ",Subjective," + answer + "," + marks;
                currentQuizGrades.add(newGrade);
                logger.log(java.util.logging.Level.INFO, "Added new grade: " + newGrade);
            }
        }
        
        // Sort all grades for this quiz by questionID (numerically)
        currentQuizGrades.sort((g1, g2) -> {
            String q1 = g1.split(",")[3]; // Extract questionID
            String q2 = g2.split(",")[3];
            return compareQuestionIDs(q1, q2);
        });
        
        // Combine all lines: other lines + sorted current quiz grades
        java.util.List<String> allLines = new java.util.ArrayList<>();
        allLines.addAll(otherLines);
        allLines.addAll(currentQuizGrades);
        
        // Write all lines back to file
        try (java.io.BufferedWriter bw = new java.io.BufferedWriter(new java.io.FileWriter(gradeFile))) {
            for (String line : allLines) {
                bw.write(line);
                bw.newLine();
            }
            logger.log(java.util.logging.Level.INFO, "Successfully saved all subjective grades to Grade.txt");
        } catch (java.io.IOException e) {
            logger.log(java.util.logging.Level.SEVERE, "Error writing to Grade.txt: " + e.getMessage(), e);
            javax.swing.JOptionPane.showMessageDialog(this,
                "Error saving grades: " + e.getMessage(),
                "File Error",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Calculates the final grade percentage for the student and saves to FinalGrade.txt
     * Formula: (Student's total marks / Quiz total marks) * 100%
     */
    private void calculateAndSaveFinalGrade() {
        String projectRoot = System.getProperty("user.dir");
        String totalQuizMarkPath = projectRoot + "/src/main/java/oopwj/TotalQuizMark.txt";
        String gradeFilePath = projectRoot + "/src/main/java/oopwj/Grade.txt";
        String finalGradeFilePath = projectRoot + "/src/main/java/oopwj/FinalGrade.txt";
        
        // Step 1: Calculate total possible marks for this quiz from TotalQuizMark.txt
        int totalPossibleMarks = 0;
        java.io.File totalQuizMarkFile = new java.io.File(totalQuizMarkPath);
        
        if (totalQuizMarkFile.exists()) {
            try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(totalQuizMarkFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    if (line.isEmpty() || line.startsWith("#")) continue;
                    
                    // Format: ModuleID,QuizID,QuestionID,TotalMarks
                    String[] parts = line.split(",");
                    if (parts.length >= 4) {
                        String fileModuleID = parts[0].trim();
                        String fileQuizID = parts[1].trim();
                        String marks = parts[3].trim();
                        
                        if (fileModuleID.equals(moduleID) && fileQuizID.equals(quizID)) {
                            try {
                                totalPossibleMarks += Integer.parseInt(marks);
                            } catch (NumberFormatException e) {
                                logger.log(java.util.logging.Level.WARNING, "Invalid marks format: " + marks);
                            }
                        }
                    }
                }
            } catch (java.io.IOException e) {
                logger.log(java.util.logging.Level.SEVERE, "Error reading TotalQuizMark.txt: " + e.getMessage(), e);
                return;
            }
        }
        
        if (totalPossibleMarks == 0) {
            logger.log(java.util.logging.Level.WARNING, "Total possible marks is 0, cannot calculate percentage");
            return;
        }
        
        // Step 2: Calculate student's total marks from Grade.txt
        int studentTotalMarks = 0;
        java.io.File gradeFile = new java.io.File(gradeFilePath);
        
        if (gradeFile.exists()) {
            try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(gradeFile))) {
                String line;
                java.util.Set<String> processedQuestions = new java.util.HashSet<>();
                
                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    if (line.isEmpty() || line.startsWith("#")) continue;
                    
                    // Format: StudentID,ModuleID,QuizID,QuestionID,QuestionType,Answer,MarkObtained
                    String[] parts = line.split(",");
                    if (parts.length >= 7) {
                        String fileStudentID = parts[0].trim();
                        String fileModuleID = parts[1].trim();
                        String fileQuizID = parts[2].trim();
                        String fileQuestionID = parts[3].trim();
                        String marksObtained = parts[6].trim();
                        
                        if (fileStudentID.equals(studentID) && 
                            fileModuleID.equals(moduleID) && 
                            fileQuizID.equals(quizID)) {
                            
                            // Avoid counting duplicate entries for the same question
                            if (!processedQuestions.contains(fileQuestionID)) {
                                try {
                                    studentTotalMarks += Integer.parseInt(marksObtained);
                                    processedQuestions.add(fileQuestionID);
                                } catch (NumberFormatException e) {
                                    logger.log(java.util.logging.Level.WARNING, "Invalid marks obtained format: " + marksObtained);
                                }
                            }
                        }
                    }
                }
            } catch (java.io.IOException e) {
                logger.log(java.util.logging.Level.SEVERE, "Error reading Grade.txt: " + e.getMessage(), e);
                return;
            }
        }
        
        // Step 3: Calculate percentage (normalize to 100%)
        double percentage = ((double) studentTotalMarks / totalPossibleMarks) * 100.0;
        
        // Step 3.5: Determine letter grade based on grading.txt
        String letterGrade = determineLetterGrade(percentage);
        
        // Step 4: Save to FinalGrade.txt
        // First, read existing file to update or add the record (preserve feedback column)
        java.util.Map<String, String> finalGrades = new java.util.LinkedHashMap<>();
        String existingFeedback = "";
        java.io.File finalGradeFile = new java.io.File(finalGradeFilePath);
        
        if (finalGradeFile.exists()) {
            try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(finalGradeFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String trimmedLine = line.trim();
                    if (trimmedLine.isEmpty() || trimmedLine.startsWith("#")) continue;
                    
                    // Format: StudentID,ModuleID,QuizID,Mark,Feedback (old 5-col format)
                    //    or: StudentID,ModuleID,QuizID,Mark,Grade,Feedback (new 6-col format)
                    String[] parts = trimmedLine.split(",", 6);
                    if (parts.length >= 4) {
                        String fileStudentID = parts[0].trim();
                        String fileModuleID = parts[1].trim();
                        String fileQuizID = parts[2].trim();
                        String key = fileStudentID + "," + fileModuleID + "," + fileQuizID;
                        
                        // Check if this is the current student's record
                        if (fileStudentID.equals(studentID) &&
                            fileModuleID.equals(moduleID) &&
                            fileQuizID.equals(quizID)) {
                            // Preserve existing feedback
                            // In old format (5 columns): feedback is at index 4
                            // In new format (6 columns): feedback is at index 5
                            if (parts.length == 5) {
                                existingFeedback = parts[4]; // Old format
                            } else if (parts.length >= 6) {
                                existingFeedback = parts[5]; // New format
                            }
                        } else {
                            finalGrades.put(key, line);
                        }
                    }
                }
            } catch (java.io.IOException e) {
                logger.log(java.util.logging.Level.SEVERE, "Error reading FinalGrade.txt: " + e.getMessage(), e);
            }
        }
        
        // Add or update current student's final grade
        String key = studentID + "," + moduleID + "," + quizID;
        // Use existing feedback as-is (already formatted), or empty string if none
        String feedbackValue = existingFeedback.isEmpty() ? "\"\"" : existingFeedback;
        String finalGradeEntry = String.format("%s,%s,%s,%d,%s,%s",
            studentID, moduleID, quizID, (int)percentage, letterGrade, feedbackValue);
        finalGrades.put(key, finalGradeEntry);
        
        // Write all grades back to file
        try (java.io.BufferedWriter bw = new java.io.BufferedWriter(new java.io.FileWriter(finalGradeFile))) {
            for (String gradeEntry : finalGrades.values()) {
                bw.write(gradeEntry);
                bw.newLine();
            }
            logger.log(java.util.logging.Level.INFO, 
                String.format("Final grade saved: %s - %d%% (%d/%d) - Grade: %s", 
                    studentID, (int)percentage, studentTotalMarks, totalPossibleMarks, letterGrade));
        } catch (java.io.IOException e) {
            logger.log(java.util.logging.Level.SEVERE, "Error writing to FinalGrade.txt: " + e.getMessage(), e);
            javax.swing.JOptionPane.showMessageDialog(this,
                "Error saving final grade: " + e.getMessage(),
                "File Error",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }


    
    /**
     * Determines the letter grade based on the percentage score using grading.txt
     */
    private String determineLetterGrade(double percentage) {
        String projectRoot = System.getProperty("user.dir");
        String gradingFilePath = projectRoot + "/src/main/java/oopwj/grading.txt";
        
        java.io.File gradingFile = new java.io.File(gradingFilePath);
        if (!gradingFile.exists()) {
            logger.log(java.util.logging.Level.WARNING, "grading.txt not found, defaulting to F");
            return "F";
        }
        
        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(gradingFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                
                // Format: Grade,MinPercentage,MaxPercentage
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String grade = parts[0].trim();
                    try {
                        double minPercentage = Double.parseDouble(parts[1].trim());
                        double maxPercentage = Double.parseDouble(parts[2].trim());
                        
                        // Check if percentage falls within this grade range (inclusive)
                        if (percentage >= minPercentage && percentage <= maxPercentage) {
                            return grade;
                        }
                    } catch (NumberFormatException e) {
                        logger.log(java.util.logging.Level.WARNING, "Invalid grading range format: " + line);
                    }
                }
            }
        } catch (java.io.IOException e) {
            logger.log(java.util.logging.Level.SEVERE, "Error reading grading.txt: " + e.getMessage(), e);
        }
        
        // Default to F if no grade range matches
        return "F";
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
        java.awt.EventQueue.invokeLater(() -> new View_Grade().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Objective;
    private javax.swing.JPanel Subjective;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLayeredPane jLayeredPane2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JSpinner jSpinner2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextArea jTextArea3;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration//GEN-END:variables
}
