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
    private String moduleName;
    private String quizName;
    private java.util.Map<String, Integer> maxMarksMap = new java.util.HashMap<>();
    private java.util.Map<String, Integer> subjectiveGrades = new java.util.HashMap<>(); // Stores questionID -> marks for subjective questions

    /**
     * Creates new form View_Grade
     */
    public View_Grade() {
        initComponents();
        setLocationRelativeTo(null);
        configureQuestionComboBox();
        configureObjectiveAutoMark();
        jButton1.addActionListener(this::jButton1ActionPerformed);
        jButton5.addActionListener(this::jButton5ActionPerformed);
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
        configureQuestionComboBox();
        configureObjectiveAutoMark();
        jButton1.addActionListener(this::jButton1ActionPerformed);
        jButton2.addActionListener(this::jButton2ActionPerformed);
        jButton3.addActionListener(this::jButton3ActionPerformed);
        jButton4.addActionListener(this::jButton4ActionPerformed);
        jButton5.addActionListener(this::jButton5ActionPerformed);
        loadMaxMarks(moduleID, quizID);
        moduleName = lookupModuleName(moduleID);
        quizName = lookupQuizName(moduleID, quizID);
        setModuleAndStudentInfo(moduleName, quizName, studentID);
        loadQuestionIDs(moduleID, quizID);
        loadExistingGrades(); 
        refreshCurrentQuestionDisplay();
        setupSpinnerValidation();
        setupSpinnerChangeListener(); 
    }
    
    /**
     * Sets the module ID, quiz ID, and student ID on the labels
     */
    public void setModuleAndStudentInfo(String moduleName, String quizName, String studentID) {
        jLabel7.setText("Module: " + moduleName);
        jLabel8.setText("Student ID: " + studentID);
        jLabel9.setText("Quiz: " + quizName);
    }

    private void configureObjectiveAutoMark() {
        jRadioButton1.setEnabled(false);
        jRadioButton2.setEnabled(false);
        jRadioButton1.setFocusable(false);
        jRadioButton2.setFocusable(false);
    }

    private void configureQuestionComboBox() {
        jComboBox1.setRenderer(new javax.swing.DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(javax.swing.JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof String) {
                    setText(buildQuestionDisplayText((String) value));
                }
                return this;
            }
        });
    }

    private void updateObjectiveAutoMark(String correctAnswer, String studentAnswer) {
        jRadioButton1.setSelected(false);
        jRadioButton2.setSelected(false);

        if (studentAnswer == null || studentAnswer.isEmpty() || correctAnswer == null || correctAnswer.isEmpty()) {
            return;
        }

        boolean isCorrect = correctAnswer.equalsIgnoreCase(studentAnswer);
        jRadioButton2.setSelected(isCorrect);
        jRadioButton1.setSelected(!isCorrect);
    }

    private String lookupModuleName(String moduleID) {
        String projectRoot = System.getProperty("user.dir");
        String modulesFilePath = projectRoot + "\\src\\main\\java\\oopwj\\data\\modules.txt";

        java.io.File modulesFile = new java.io.File(modulesFilePath);
        if (!modulesFile.exists()) {
            logger.log(java.util.logging.Level.WARNING, "modules.txt not found at: " + modulesFile.getAbsolutePath());
            return moduleID;
        }

        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(modulesFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;

                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String fileModuleID = parts[0].trim();
                    if (fileModuleID.equals(moduleID)) {
                        return parts[1].trim();
                    }
                }
            }
        } catch (java.io.IOException e) {
            logger.log(java.util.logging.Level.SEVERE, "Error reading modules.txt: " + e.getMessage(), e);
        }

        return moduleID;
    }

    private String lookupQuizName(String moduleID, String quizID) {
        String projectRoot = System.getProperty("user.dir");
        String quizFilePath = projectRoot + "\\src\\main\\java\\oopwj\\data\\Quiz.txt";

        java.io.File quizFile = new java.io.File(quizFilePath);
        if (!quizFile.exists()) {
            logger.log(java.util.logging.Level.WARNING, "Quiz.txt not found at: " + quizFile.getAbsolutePath());
            return quizID;
        }

        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(quizFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;

                String[] parts = line.split(",", 3);
                if (parts.length >= 2) {
                    String fileQuizID = parts[0].trim();
                    String fileModuleID = parts[1].trim();
                    if (fileQuizID.equals(quizID) && fileModuleID.equals(moduleID)) {
                        if (parts.length == 3) {
                            return parts[2].trim();
                        }
                        return quizID;
                    }
                }
            }
        } catch (java.io.IOException e) {
            logger.log(java.util.logging.Level.SEVERE, "Error reading Quiz.txt: " + e.getMessage(), e);
        }

        return quizID;
    }
    

    private void loadMaxMarks(String moduleID, String quizID) {
        String projectRoot = System.getProperty("user.dir");
        String totalQuizMarkPath = projectRoot + "\\src\\main\\java\\oopwj\\data\\TotalQuizMark.txt";
        
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

    private void setupSpinnerValidation() {
        jSpinner2.addChangeListener(e -> {
            String selectedQuestionID = getSelectedQuestionId();
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
    

    private void setupSpinnerChangeListener() {
        jSpinner2.addChangeListener(e -> {
            String selectedQuestionID = getSelectedQuestionId();
            if (selectedQuestionID != null && isSubjectiveQuestion(selectedQuestionID)) {
                int marks = (Integer) jSpinner2.getValue();
                subjectiveGrades.put(selectedQuestionID, marks);
                logger.log(java.util.logging.Level.INFO, "Stored grade for " + selectedQuestionID + ": " + marks);
            }
        });
    }
    

    private void loadExistingGrades() {
        String projectRoot = System.getProperty("user.dir");
        String gradeFilePath = projectRoot + "\\src\\main\\java\\oopwj\\data\\Grade.txt";
        
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
                
                String[] parts = line.split(",");
                if (parts.length >= 7) {
                    String fileStudentID = parts[0].trim();
                    String fileModuleID = parts[1].trim();
                    String fileQuizID = parts[2].trim();
                    String fileQuestionID = parts[3].trim();
                    String questionType = parts[4].trim();
                    String markStr = parts[6].trim();

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

    private void loadQuestionIDs(String moduleID, String quizID) {
        String projectRoot = System.getProperty("user.dir");
        String quizFilePath = projectRoot + "\\src\\main\\java\\oopwj\\data\\question.txt";
        
        java.io.File quizFile = new java.io.File(quizFilePath);
        if (!quizFile.exists()) {
            logger.log(java.util.logging.Level.SEVERE, "question.txt not found at: " + quizFile.getAbsolutePath());
            javax.swing.JOptionPane.showMessageDialog(this, 
                "question.txt not found at: " + quizFile.getAbsolutePath(), 
                "File Not Found", 
                javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

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
        Subjective = new javax.swing.JPanel();
        jTextArea2 = new javax.swing.JTextArea();
        jLabel4 = new javax.swing.JLabel();
        jTextArea3 = new javax.swing.JTextArea();
        jLabel5 = new javax.swing.JLabel();
        jSpinner2 = new javax.swing.JSpinner();
        jLabel6 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        Objective = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jTextArea1 = new javax.swing.JTextArea();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximumSize(null);
        setMinimumSize(null);
        setSize(new java.awt.Dimension(734, 617));

        jPanel4.setBackground(new java.awt.Color(255, 255, 204));
        jPanel4.setForeground(new java.awt.Color(255, 255, 255));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLayeredPane2.setLayout(new java.awt.CardLayout());

        Subjective.setBackground(new java.awt.Color(255, 255, 255));
        Subjective.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jTextArea2.setEditable(false);
        jTextArea2.setColumns(20);
        jTextArea2.setRows(5);
        jTextArea2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel4.setText("Question:");

        jTextArea3.setEditable(false);
        jTextArea3.setColumns(20);
        jTextArea3.setRows(5);
        jTextArea3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel5.setText("Answer:");

        jLabel6.setText("Mark:");

        jLabel10.setText("Marks Given:");

        javax.swing.GroupLayout SubjectiveLayout = new javax.swing.GroupLayout(Subjective);
        Subjective.setLayout(SubjectiveLayout);
        SubjectiveLayout.setHorizontalGroup(
            SubjectiveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SubjectiveLayout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(SubjectiveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(SubjectiveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jTextArea3, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(SubjectiveLayout.createSequentialGroup()
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSpinner2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(SubjectiveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jTextArea2, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                .addComponent(jLabel6)
                .addGap(18, 18, 18)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextArea3, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addGroup(SubjectiveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jSpinner2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addGap(25, 25, 25))
        );

        jLayeredPane2.add(Subjective, "card2");

        Objective.setBackground(new java.awt.Color(255, 255, 255));
        Objective.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jTextField1.setEditable(false);
        jTextField1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jTextField1.addActionListener(this::jTextField1ActionPerformed);

        jLabel2.setText("Answer:");

        jLabel3.setText("Correct Answer:");

        jTextField2.setEditable(false);
        jTextField2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jRadioButton1.setText("Incorrect");
        jRadioButton1.addActionListener(this::jRadioButton1ActionPerformed);

        jRadioButton2.setText("Correct");

        jLabel1.setText("Question:");

        jLabel11.setText("Mark:");

        javax.swing.GroupLayout ObjectiveLayout = new javax.swing.GroupLayout(Objective);
        Objective.setLayout(ObjectiveLayout);
        ObjectiveLayout.setHorizontalGroup(
            ObjectiveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ObjectiveLayout.createSequentialGroup()
                .addContainerGap(30, Short.MAX_VALUE)
                .addGroup(ObjectiveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ObjectiveLayout.createSequentialGroup()
                        .addComponent(jRadioButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jRadioButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(108, 108, 108))
                    .addGroup(ObjectiveLayout.createSequentialGroup()
                        .addGroup(ObjectiveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(ObjectiveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel3)
                                .addGroup(ObjectiveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jTextField2, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextField1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jTextArea1, javax.swing.GroupLayout.PREFERRED_SIZE, 356, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(30, Short.MAX_VALUE))))
        );
        ObjectiveLayout.setVerticalGroup(
            ObjectiveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ObjectiveLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextArea1, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addGroup(ObjectiveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton1)
                    .addComponent(jRadioButton2))
                .addGap(28, 28, 28))
        );

        jLayeredPane2.add(Objective, "card3");

        jPanel4.add(jLayeredPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 160, 420, 420));

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.addActionListener(this::jComboBox1ActionPerformed);
        jPanel4.add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 110, 210, 30));

        jButton1.setText("Exit");
        jPanel4.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 650, -1, -1));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setText("Module:");
        jPanel4.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, 400, -1));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel8.setText("Student ID:");
        jPanel4.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 80, 200, -1));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel9.setText("Quiz: ");
        jPanel4.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 50, 330, -1));

        jButton2.setText("Previous");
        jPanel4.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 590, -1, -1));

        jButton3.setText("Next");
        jPanel4.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 590, -1, -1));

        jButton4.setText("Save");
        jPanel4.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 590, -1, -1));

        jButton5.setText("Full Mark");
        jPanel4.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 590, -1, -1));

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
        String selectedQuestionID = getSelectedQuestionId();
        if (selectedQuestionID == null || selectedQuestionID.isEmpty()) {
            return;
        }

        loadQuestionData(selectedQuestionID);
        
        loadStudentAnswer(selectedQuestionID);

        updateMaxMarksDisplay(selectedQuestionID);
    }//GEN-LAST:event_jComboBox1ActionPerformed
    

    private void loadQuestionData(String questionID) {
        String projectRoot = System.getProperty("user.dir");
        String questionFilePath = projectRoot + "\\src\\main\\java\\oopwj\\data\\question.txt";
        
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
                            String question = parts[3].trim().replaceAll("^\\\"|\\\"$", "");
                            String answerA = parts[4].trim().replaceAll("^\\\"|\\\"$", "");
                            String answerB = parts[5].trim().replaceAll("^\\\"|\\\"$", "");
                            String answerC = parts[6].trim().replaceAll("^\\\"|\\\"$", "");
                            String answerD = parts[7].trim().replaceAll("^\\\"|\\\"$", "");
                            String correctAnswer = parts[8].trim().replaceAll("^\\\"|\\\"$", "");

                            jTextArea1.setText(question);
                            jTextField1.setText(answerA + " / " + answerB + " / " + answerC + " / " + answerD);
                            jTextField2.setText(correctAnswer);

                            swapPanels("Objective");
                        } else if (parts.length >= 5) {
                            String question = parts[3].trim().replaceAll("^\\\"|\\\"$", "");

                            jTextArea2.setText(question);
                            jRadioButton1.setSelected(false);
                            jRadioButton2.setSelected(false);

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
    

    private void updateMaxMarksDisplay(String questionID) {
        if (maxMarksMap.containsKey(questionID)) {
            int maxMarks = maxMarksMap.get(questionID);
            

            int savedMark = subjectiveGrades.getOrDefault(questionID, 0);

            javax.swing.SpinnerNumberModel spinnerModel = new javax.swing.SpinnerNumberModel(savedMark, 0, maxMarks, 1);
            jSpinner2.setModel(spinnerModel);
            javax.swing.JSpinner.NumberEditor editor = new javax.swing.JSpinner.NumberEditor(jSpinner2, "0");
            jSpinner2.setEditor(editor);
            jLabel6.setText("Mark: " + maxMarks);

            jLabel11.setText("Mark: " + maxMarks);
            
            logger.log(java.util.logging.Level.INFO, "Max marks for " + questionID + " set to: " + maxMarks + ", current value: " + savedMark);
        } else {
            logger.log(java.util.logging.Level.WARNING, "No max marks found for question: " + questionID);
            jLabel6.setText("Mark:");
            jLabel11.setText("Mark:");
        }
    }
    

    private void loadStudentAnswer(String questionID) {
        String projectRoot = System.getProperty("user.dir");
        String answersFilePath = projectRoot + "\\src\\main\\java\\oopwj\\data\\student_answers.txt";
        
        java.io.File answersFile = new java.io.File(answersFilePath);
        if (!answersFile.exists()) {
            logger.log(java.util.logging.Level.SEVERE, "student_answers.txt not found");
            return;
        }
        
        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(answersFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;

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
                            jTextField1.setText(answer);
                            String correctAnswer = jTextField2.getText().trim();
                            updateObjectiveAutoMark(correctAnswer, answer);
                        } else if ("Subjective".equalsIgnoreCase(questionType)) {
                            jTextArea3.setText(answer);
                        }
                        return;
                    }
                }
            }
        } catch (java.io.IOException e) {
            logger.log(java.util.logging.Level.SEVERE, "Error reading student_answers.txt: " + e.getMessage(), e);
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
        Grade_Assessment gradeAssessment = new Grade_Assessment(lecturerID);
        gradeAssessment.setVisible(true);
        this.dispose();
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
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

        autoGradeObjectiveQuestions(studentID, moduleID, quizID);
        if (subjectiveGrades.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, 
                "No subjective questions have been graded yet.", 
                "No Grades to Save", 
                javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        saveAllSubjectiveGrades();

        calculateAndSaveFinalGrade();

        int subjectiveCount = subjectiveGrades.size();
        int totalSubjectiveCount = countSubjectiveQuestions();

        java.util.Map<String, Integer> objectiveGrades = loadObjectiveGradesFromGradeFile();
        int objectiveCount = objectiveGrades.size();
        int totalObjectiveCount = countObjectiveQuestions();
        
        int totalMarked = subjectiveCount + objectiveCount;
        int totalQuestions = totalSubjectiveCount + totalObjectiveCount;
        
        StringBuilder message = new StringBuilder();
        message.append("Questions Marked: ").append(totalMarked).append(" out of ").append(totalQuestions).append("\n\n");

        if (objectiveCount > 0) {
            message.append("Objective Questions Marked:\n");
            for (java.util.Map.Entry<String, Integer> entry : objectiveGrades.entrySet()) {
                message.append("- ").append(entry.getKey()).append(": ").append(entry.getValue()).append(" marks\n");
            }
            message.append("\n");
        }

        if (subjectiveCount > 0) {
            message.append("Subjective Questions Marked:\n");
            for (java.util.Map.Entry<String, Integer> entry : subjectiveGrades.entrySet()) {
                message.append("- ").append(entry.getKey()).append(": ").append(entry.getValue()).append(" marks\n");
            }
            message.append("\n");
        }
        

        if (totalMarked < totalQuestions) {
            message.append("Pending Questions to Grade:\n");
            java.util.Set<String> gradedQuestions = new java.util.HashSet<>();
            gradedQuestions.addAll(subjectiveGrades.keySet());
            gradedQuestions.addAll(objectiveGrades.keySet());
            
            for (int i = 0; i < jComboBox1.getItemCount(); i++) {
                String questionID = getQuestionIdAt(i);
                if (!gradedQuestions.contains(questionID)) {
                    message.append("- ").append(questionID).append("\n");
                }
            }
        }
        
        javax.swing.JOptionPane.showMessageDialog(this, 
            message.toString(), 
            "Grades Saved Successfully", 
            javax.swing.JOptionPane.INFORMATION_MESSAGE);
        
        logger.log(java.util.logging.Level.INFO, "Saved " + totalMarked + " questions to Grade.txt");
    }

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {
        String selectedQuestionID = getSelectedQuestionId();
        if (selectedQuestionID == null || selectedQuestionID.isEmpty()) {
            return;
        }

        if (!isSubjectiveQuestion(selectedQuestionID)) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "Full Mark applies only to subjective questions.",
                "Not a Subjective Question",
                javax.swing.JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        Integer maxMarks = maxMarksMap.get(selectedQuestionID);
        if (maxMarks == null) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "Maximum marks not found for this question.",
                "Missing Max Marks",
                javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        jSpinner2.setValue(maxMarks);
        subjectiveGrades.put(selectedQuestionID, maxMarks);
        logger.log(java.util.logging.Level.INFO, "Set full marks for " + selectedQuestionID + ": " + maxMarks);
    }

    private void refreshCurrentQuestionDisplay() {
        String selectedQuestionID = getSelectedQuestionId();
        if (selectedQuestionID == null || selectedQuestionID.isEmpty()) {
            return;
        }

        loadQuestionData(selectedQuestionID);
        loadStudentAnswer(selectedQuestionID);
        updateMaxMarksDisplay(selectedQuestionID);
    }
    

    private boolean isSubjectiveQuestion(String questionID) {
        String projectRoot = System.getProperty("user.dir");
        String questionFilePath = projectRoot + "\\src\\main\\java\\oopwj\\data\\question.txt";
        
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
    

    private int countSubjectiveQuestions() {
        String projectRoot = System.getProperty("user.dir");
        String questionFilePath = projectRoot + "\\src\\main\\java\\oopwj\\data\\question.txt";
        
        int count = 0;
        java.io.File questionFile = new java.io.File(questionFilePath);
        if (!questionFile.exists()) {
            logger.log(java.util.logging.Level.WARNING, "question.txt not found");
            return count;
        }
        
        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(questionFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String fileQuizID = parts[1].trim();
                    String fileModuleID = parts[2].trim();
                    
                    if (fileQuizID.equals(quizID) && fileModuleID.equals(moduleID)) {
                        String questionType = parts[parts.length - 1].trim();
                        if ("Subjective".equalsIgnoreCase(questionType)) {
                            count++;
                        }
                    }
                }
            }
        } catch (java.io.IOException e) {
            logger.log(java.util.logging.Level.SEVERE, "Error reading question.txt: " + e.getMessage(), e);
        }
        
        return count;
    }
    

    private int countObjectiveQuestions() {
        String projectRoot = System.getProperty("user.dir");
        String questionFilePath = projectRoot + "\\src\\main\\java\\oopwj\\data\\question.txt";
        
        int count = 0;
        java.io.File questionFile = new java.io.File(questionFilePath);
        if (!questionFile.exists()) {
            logger.log(java.util.logging.Level.WARNING, "question.txt not found");
            return count;
        }
        
        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(questionFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String fileQuizID = parts[1].trim();
                    String fileModuleID = parts[2].trim();
                    
                    if (fileQuizID.equals(quizID) && fileModuleID.equals(moduleID)) {
                        String questionType = parts[parts.length - 1].trim();
                        if ("Objective".equalsIgnoreCase(questionType)) {
                            count++;
                        }
                    }
                }
            }
        } catch (java.io.IOException e) {
            logger.log(java.util.logging.Level.SEVERE, "Error reading question.txt: " + e.getMessage(), e);
        }
        
        return count;
    }

    private java.util.Map<String, Integer> loadObjectiveGradesFromGradeFile() {
        java.util.Map<String, Integer> objectiveGrades = new java.util.LinkedHashMap<>();
        
        String projectRoot = System.getProperty("user.dir");
        String gradeFilePath = projectRoot + "\\src\\main\\java\\oopwj\\data\\Grade.txt";
        
        java.io.File gradeFile = new java.io.File(gradeFilePath);
        if (!gradeFile.exists()) {
            logger.log(java.util.logging.Level.WARNING, "Grade.txt not found");
            return objectiveGrades;
        }
        
        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(gradeFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;

                String[] parts = line.split(",");
                if (parts.length >= 7) {
                    String fileStudentID = parts[0].trim();
                    String fileModuleID = parts[1].trim();
                    String fileQuizID = parts[2].trim();
                    String fileQuestionID = parts[3].trim();
                    String questionType = parts[4].trim();
                    String markStr = parts[6].trim();

                    if (fileStudentID.equals(studentID) &&
                        fileModuleID.equals(moduleID) &&
                        fileQuizID.equals(quizID) &&
                        "Objective".equalsIgnoreCase(questionType)) {
                        try {
                            int mark = Integer.parseInt(markStr);
                            objectiveGrades.put(fileQuestionID, mark);
                        } catch (NumberFormatException e) {
                            logger.log(java.util.logging.Level.WARNING, "Invalid mark format: " + markStr);
                        }
                    }
                }
            }
        } catch (java.io.IOException e) {
            logger.log(java.util.logging.Level.SEVERE, "Error reading Grade.txt: " + e.getMessage(), e);
        }
        
        return objectiveGrades;
    }
    

    private int compareQuestionIDs(String q1, String q2) {
        try {

            int num1 = Integer.parseInt(q1.replaceAll("[^0-9]", ""));
            int num2 = Integer.parseInt(q2.replaceAll("[^0-9]", ""));
            return Integer.compare(num1, num2);
        } catch (NumberFormatException e) {

            return q1.compareTo(q2);
        }
    }

    private String getSelectedQuestionId() {
        Object selected = jComboBox1.getSelectedItem();
        if (selected instanceof String) {
            return (String) selected;
        }
        return null;
    }

    private String getQuestionIdAt(int index) {
        Object item = jComboBox1.getItemAt(index);
        if (item instanceof String) {
            return (String) item;
        }
        return null;
    }

    private String buildQuestionDisplayText(String questionID) {
        String numericPart = questionID.replaceAll("[^0-9]", "");
        if (numericPart.isEmpty()) {
            return questionID;
        }

        try {
            int number = Integer.parseInt(numericPart);
            return "Question " + number;
        } catch (NumberFormatException e) {
            return questionID;
        }
    }
    
    private java.util.Map<String, String> loadSubjectiveAnswers() {
        java.util.Map<String, String> answers = new java.util.HashMap<>();
        String projectRoot = System.getProperty("user.dir");
        String answersFilePath = projectRoot + "\\src\\main\\java\\oopwj\\data\\student_answers.txt";
        
        java.io.File answersFile = new java.io.File(answersFilePath);
        if (!answersFile.exists()) {
            logger.log(java.util.logging.Level.WARNING, "student_answers.txt not found at: " + answersFilePath);
            return answers;
        }
        
        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(answersFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;

                String[] parts = line.split(",", 6);
                if (parts.length >= 6) {
                    String fileStudentID = parts[0].trim();
                    String fileModuleID = parts[1].trim();
                    String fileQuizID = parts[2].trim();
                    String fileQuestionID = parts[3].trim();
                    String questionType = parts[4].trim();
                    String answer = parts[5].trim(); 

                    if (fileStudentID.equals(studentID) && 
                        fileModuleID.equals(moduleID) && 
                        fileQuizID.equals(quizID) &&
                        "Subjective".equalsIgnoreCase(questionType)) {
                        answers.put(fileQuestionID, answer);
                    }
                }
            }
            logger.log(java.util.logging.Level.INFO, "Loaded " + answers.size() + " subjective answers from student_answers.txt");
        } catch (java.io.IOException e) {
            logger.log(java.util.logging.Level.SEVERE, "Error reading student_answers.txt: " + e.getMessage(), e);
        }
        
        return answers;
    }

    private void saveAllSubjectiveGrades() {
        String projectRoot = System.getProperty("user.dir");
        String gradeFilePath = projectRoot + "\\src\\main\\java\\oopwj\\data\\Grade.txt";
        
        java.io.File gradeFile = new java.io.File(gradeFilePath);

        java.util.Map<String, String> subjectiveAnswers = loadSubjectiveAnswers();

        java.util.List<String> otherLines = new java.util.ArrayList<>(); 
        java.util.List<String> currentQuizGrades = new java.util.ArrayList<>(); 
        java.util.Set<String> processedQuestions = new java.util.HashSet<>();
        
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

                    String[] parts = line.split(",");
                    if (parts.length >= 7) {
                        String fileStudentID = parts[0].trim();
                        String fileModuleID = parts[1].trim();
                        String fileQuizID = parts[2].trim();
                        String fileQuestionID = parts[3].trim();
                        String questionType = parts[4].trim();

                        if (fileStudentID.equals(studentID) && 
                            fileModuleID.equals(moduleID) && 
                            fileQuizID.equals(quizID)) {

                            if (processedQuestions.contains(fileQuestionID)) {
                                logger.log(java.util.logging.Level.WARNING, "Skipping duplicate entry for question: " + fileQuestionID);
                                continue;
                            }

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

                                currentQuizGrades.add(originalLine);
                                processedQuestions.add(fileQuestionID);
                            }
                        } else {

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
        

        currentQuizGrades.sort((g1, g2) -> {
            String q1 = g1.split(",")[3]; 
            String q2 = g2.split(",")[3];
            return compareQuestionIDs(q1, q2);
        });

        java.util.List<String> allLines = new java.util.ArrayList<>();
        allLines.addAll(otherLines);
        allLines.addAll(currentQuizGrades);

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

    private void autoGradeObjectiveQuestions(String studentID, String moduleID, String quizID) {
        System.out.println("DEBUG: autoGradeObjectiveQuestions() START");
        System.out.println("StudentID: " + studentID + ", ModuleID: " + moduleID + ", QuizID: " + quizID);
        
        String projectRoot = System.getProperty("user.dir");
        String questionFilePath = projectRoot + "\\src\\main\\java\\oopwj\\data\\question.txt";
        String answersFilePath = projectRoot + "\\src\\main\\java\\oopwj\\data\\student_answers.txt";
        String totalQuizMarkPath = projectRoot + "\\src\\main\\java\\oopwj\\data\\TotalQuizMark.txt";
        String gradeFilePath = projectRoot + "\\src\\main\\java\\oopwj\\data\\Grade.txt";

        java.util.Map<String, String[]> questionData = loadQuestionData(questionFilePath, moduleID, quizID);

        java.util.Map<String, String> studentAnswers = loadStudentAnswers(answersFilePath, studentID, moduleID, quizID);

        java.util.Map<String, Integer> maxMarks = loadMaxMarksMap(totalQuizMarkPath, moduleID, quizID);

        java.util.List<String> gradesToAdd = new java.util.ArrayList<>();

        java.util.List<String> sortedQuestionIDs = new java.util.ArrayList<>(questionData.keySet());
        sortedQuestionIDs.sort((q1, q2) -> {

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

            if ("Objective".equals(questionType)) {
                String studentAnswer = studentAnswers.getOrDefault(questionID, "");
                Integer maxMarkObj = maxMarks.getOrDefault(questionID, 0);
                int maxMark = (maxMarkObj != null) ? maxMarkObj : 0;

                int mark = correctAnswer.equalsIgnoreCase(studentAnswer) ? maxMark : 0;

                String gradeEntry = studentID + "," + moduleID + "," + quizID + "," + questionID + ",Objective," + correctAnswer + "," + mark;
                gradesToAdd.add(gradeEntry);
                
                System.out.println("Graded Q" + questionID + ": Student=" + studentAnswer + ", Correct=" + correctAnswer + ", Mark=" + mark);
            }
        }

        if (!gradesToAdd.isEmpty()) {
            appendGradesToFile(gradeFilePath, gradesToAdd);
            System.out.println("DEBUG: Saved " + gradesToAdd.size() + " grades to Grade.txt");
        }
        
        System.out.println("DEBUG: autoGradeObjectiveQuestions() END");
    }

    private java.util.Map<String, String[]> loadQuestionData(String questionFilePath, String moduleID, String quizID) {
        java.util.Map<String, String[]> questionData = new java.util.HashMap<>();
        
        java.io.File questionFile = new java.io.File(questionFilePath);
        if (!questionFile.exists()) {
            logger.log(java.util.logging.Level.WARNING, "question.txt not found at: " + questionFilePath);
            return questionData;
        }
        
        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(questionFile))) {
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
                    
                    if (quizIDFromFile.equals(quizID) && moduleIDFromFile.equals(moduleID)) {
                        String correctAnswer = "";
                        
                        if ("Objective".equals(questionType) && parts.length >= 9) {

                            correctAnswer = parts[8].trim(); 
                        }
                        
                        questionData.put(questionID, new String[]{correctAnswer, questionType});
                    }
                }
            }
        } catch (java.io.IOException e) {
            logger.log(java.util.logging.Level.SEVERE, "Error reading question.txt: " + e.getMessage(), e);
        }
        
        return questionData;
    }
    

    private java.util.Map<String, String> loadStudentAnswers(String answersFilePath, String studentID, String moduleID, String quizID) {
        java.util.Map<String, String> studentAnswers = new java.util.HashMap<>();
        
        java.io.File answersFile = new java.io.File(answersFilePath);
        if (!answersFile.exists()) {
            logger.log(java.util.logging.Level.WARNING, "student_answers.txt not found at: " + answersFilePath);
            return studentAnswers;
        }
        
        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(answersFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                
                String[] parts = line.split(",", 6);
                if (parts.length >= 6) {
                    String studentIDFromFile = parts[0].trim();
                    String moduleIDFromFile = parts[1].trim();
                    String quizIDFromFile = parts[2].trim();
                    String questionID = parts[3].trim();
                    String answer = parts[5].trim(); 
                    
                    if (studentIDFromFile.equals(studentID) && 
                        moduleIDFromFile.equals(moduleID) && 
                        quizIDFromFile.equals(quizID)) {
                        studentAnswers.put(questionID, answer);
                    }
                }
            }
        } catch (java.io.IOException e) {
            logger.log(java.util.logging.Level.SEVERE, "Error reading student_answers.txt: " + e.getMessage(), e);
        }
        
        return studentAnswers;
    }
    

    private java.util.Map<String, Integer> loadMaxMarksMap(String totalQuizMarkPath, String moduleID, String quizID) {
        java.util.Map<String, Integer> maxMarks = new java.util.HashMap<>();
        
        java.io.File totalQuizMarkFile = new java.io.File(totalQuizMarkPath);
        if (!totalQuizMarkFile.exists()) {
            logger.log(java.util.logging.Level.WARNING, "TotalQuizMark.txt not found at: " + totalQuizMarkPath);
            return maxMarks;
        }
        
        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(totalQuizMarkFile))) {
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

                    if (moduleIDFromFile.equals(moduleID) && quizIDFromFile.equals(quizID)) {
                        maxMarks.put(questionID, maxMark);
                    }
                }
            }
        } catch (java.io.IOException | NumberFormatException e) {
            logger.log(java.util.logging.Level.SEVERE, "Error reading TotalQuizMark.txt: " + e.getMessage(), e);
        }
        
        return maxMarks;
    }

    private void appendGradesToFile(String gradeFilePath, java.util.List<String> gradesToAdd) {
        try (java.io.BufferedWriter bw = new java.io.BufferedWriter(new java.io.FileWriter(gradeFilePath, true))) {
            for (String gradeEntry : gradesToAdd) {
                bw.write(gradeEntry);
                bw.newLine();
                System.out.println("Written to Grade.txt: " + gradeEntry);
            }
        } catch (java.io.IOException e) {
            logger.log(java.util.logging.Level.SEVERE, "Error writing to Grade.txt: " + e.getMessage(), e);
            javax.swing.JOptionPane.showMessageDialog(this,
                "Error writing grades to file: " + e.getMessage(),
                "File Error",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void calculateAndSaveFinalGrade() {
        String projectRoot = System.getProperty("user.dir");
        String totalQuizMarkPath = projectRoot + "\\src\\main\\java\\oopwj\\data\\TotalQuizMark.txt";
        String gradeFilePath = projectRoot + "\\src\\main\\java\\oopwj\\data\\Grade.txt";
        String finalGradeFilePath = projectRoot + "\\src\\main\\java\\oopwj\\data\\FinalGrade.txt";
        
        int totalPossibleMarks = 0;
        java.io.File totalQuizMarkFile = new java.io.File(totalQuizMarkPath);
        
        if (totalQuizMarkFile.exists()) {
            try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(totalQuizMarkFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    if (line.isEmpty() || line.startsWith("#")) continue;

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

        int studentTotalMarks = 0;
        java.io.File gradeFile = new java.io.File(gradeFilePath);
        
        if (gradeFile.exists()) {
            try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(gradeFile))) {
                String line;
                java.util.Set<String> processedQuestions = new java.util.HashSet<>();
                
                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    if (line.isEmpty() || line.startsWith("#")) continue;

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

        double percentage = ((double) studentTotalMarks / totalPossibleMarks) * 100.0;

        String letterGrade = determineLetterGrade(percentage);

        java.util.Map<String, String> finalGrades = new java.util.LinkedHashMap<>();
        String existingFeedback = "";
        java.io.File finalGradeFile = new java.io.File(finalGradeFilePath);
        
        if (finalGradeFile.exists()) {
            try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(finalGradeFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String trimmedLine = line.trim();
                    if (trimmedLine.isEmpty() || trimmedLine.startsWith("#")) continue;

                    String[] parts = trimmedLine.split(",", 6);
                    if (parts.length >= 4) {
                        String fileStudentID = parts[0].trim();
                        String fileModuleID = parts[1].trim();
                        String fileQuizID = parts[2].trim();
                        String key = fileStudentID + "," + fileModuleID + "," + fileQuizID;

                        if (fileStudentID.equals(studentID) &&
                            fileModuleID.equals(moduleID) &&
                            fileQuizID.equals(quizID)) {

                            if (parts.length == 5) {
                                existingFeedback = parts[4]; 
                            } else if (parts.length >= 6) {
                                existingFeedback = parts[5]; 
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

        String key = studentID + "," + moduleID + "," + quizID;

        String feedbackValue = existingFeedback.isEmpty() ? "\"\"" : existingFeedback;
        String finalGradeEntry = String.format("%s,%s,%s,%d,%s,%s",
            studentID, moduleID, quizID, (int)percentage, letterGrade, feedbackValue);
        finalGrades.put(key, finalGradeEntry);

        try (java.io.BufferedWriter bw = new java.io.BufferedWriter(new java.io.FileWriter(finalGradeFile))) {

            finalGrades.entrySet().stream()
                .sorted(java.util.Map.Entry.comparingByKey())
                .forEach(entry -> {
                    try {
                        bw.write(entry.getValue());
                        bw.newLine();
                    } catch (java.io.IOException e) {
                        logger.log(java.util.logging.Level.SEVERE, "Error writing to FinalGrade.txt: " + e.getMessage(), e);
                    }
                });
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



    private String determineLetterGrade(double percentage) {
        String projectRoot = System.getProperty("user.dir");
        String gradingFilePath = projectRoot + "\\src\\main\\java\\oopwj\\data\\grading.txt";
        
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
                
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String grade = parts[0].trim();
                    try {
                        double minPercentage = Double.parseDouble(parts[1].trim());
                        double maxPercentage = Double.parseDouble(parts[2].trim());

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
    private javax.swing.JButton jButton5;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
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
