package oopwj.Lecturer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

import javax.swing.JOptionPane;

public class Quiz extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Quiz.class.getName());
    
    private int sessionQuestionCount = 0;
    
    private boolean isEditMode = false;
    private String originalQuestion = "";
    private String originalA = "";
    private String originalB = "";
    private String originalC = "";
    private String originalD = "";
    private String originalCorrectAnswer = "";

    private javax.swing.JTextField a;
    private javax.swing.JTextField b;
    private javax.swing.JTextField c;
    private javax.swing.JTextField d;
    
    private javax.swing.ButtonGroup answerGroup;
    private String correctAnswer = "";
    
    private String lecturerID;
    private Assessments parentWindow;
    private String currentQuizID = "";
    private String currentQuizTitle = "";
    private java.util.List<String> quizIdsForDropdown = new java.util.ArrayList<>();

    public Quiz() {
        this(null, null);
    }
    
    public Quiz(String lecturerID, Assessments parentWindow) {
        this.lecturerID = lecturerID;
        this.parentWindow = parentWindow;
        initComponents();
        a = jTextField2;
        b = jTextField3;
        c = jTextField4;
        d = jTextField6;
        jButton6.addActionListener(this::jButton6ActionPerformed);
        jButton5.addActionListener(this::jButton5ActionPerformed);
        this.setLocationRelativeTo(null);

        answerGroup = new javax.swing.ButtonGroup();
        answerGroup.add(jRadioButton1);
        answerGroup.add(jRadioButton3);
        answerGroup.add(jRadioButton4);
        answerGroup.add(jRadioButton5);

        populateModulesDropdown();
    }

    public Quiz(String lecturerID, Assessments parentWindow, String moduleId) {
        this.lecturerID = lecturerID;
        this.parentWindow = parentWindow;
        initComponents();
        a = jTextField2;
        b = jTextField3;
        c = jTextField4;
        d = jTextField6;
        jButton6.addActionListener(this::jButton6ActionPerformed);
        jButton5.addActionListener(this::jButton5ActionPerformed);
        this.setLocationRelativeTo(null);

        answerGroup = new javax.swing.ButtonGroup();
        answerGroup.add(jRadioButton1);
        answerGroup.add(jRadioButton3);
        answerGroup.add(jRadioButton4);
        answerGroup.add(jRadioButton5);

        populateModulesDropdown();

        selectModuleById(moduleId);
    }
    
    public Quiz(String question, String ansA, String ansB, String ansC, String ansD, String correctAns) {
        initComponents();
        a = jTextField2;
        b = jTextField3;
        c = jTextField4;
        d = jTextField6;
        this.setLocationRelativeTo(null);

        answerGroup = new javax.swing.ButtonGroup();
        answerGroup.add(jRadioButton1);
        answerGroup.add(jRadioButton3);
        answerGroup.add(jRadioButton4);
        answerGroup.add(jRadioButton5);

        isEditMode = true;
        originalQuestion = question;
        originalA = ansA;
        originalB = ansB;
        originalC = ansC;
        originalD = ansD;
        originalCorrectAnswer = correctAns;

        jTextArea1.setText(question);
        a.setText(ansA);
        b.setText(ansB);
        c.setText(ansC);
        d.setText(ansD);

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

    public Quiz(String lecturerID, Assessments parentWindow, String moduleId, String quizId, String questionId,
                String questionType, String question, String ansA, String ansB, String ansC, String ansD,
                String correctAns, String marks) {
        this.lecturerID = lecturerID;
        this.parentWindow = parentWindow;
        initComponents();
        a = jTextField2;
        b = jTextField3;
        c = jTextField4;
        d = jTextField6;
        this.setLocationRelativeTo(null);

        answerGroup = new javax.swing.ButtonGroup();
        answerGroup.add(jRadioButton1);
        answerGroup.add(jRadioButton3);
        answerGroup.add(jRadioButton4);
        answerGroup.add(jRadioButton5);

        populateModulesDropdown();

        selectModuleById(moduleId);
        populateQuizDropdown(moduleId);
        if (quizId != null && !quizId.isEmpty()) {
            selectQuizById(quizId);
            currentQuizID = quizId;
            String quizTitle = getQuizTitle(quizId);
            if (quizTitle != null && !quizTitle.isEmpty()) {
                jTextField9.setText(quizTitle);
                currentQuizTitle = quizTitle;
                jTextField9.setEditable(true);
            }
        }

        isEditMode = true;
        originalQuestion = question != null ? question : "";
        originalA = ansA != null ? ansA : "";
        originalB = ansB != null ? ansB : "";
        originalC = ansC != null ? ansC : "";
        originalD = ansD != null ? ansD : "";
        originalCorrectAnswer = correctAns != null ? correctAns : "";

        jComboBox1.setEnabled(false);
        jComboBox2.setEnabled(false);
        jButton5.setEnabled(false);
        jButton6.setEnabled(false);
        jTabbedPane1.setEnabled(false);
        jTextField9.setEditable(true);

        if (questionId != null && !questionId.isEmpty()) {
            String questionNumber = String.valueOf(Integer.parseInt(questionId.substring(1)));
            jLabel1.setText("Question: " + questionNumber);
            jLabel7.setText("Question: " + questionNumber);
        }

        if (questionType != null && questionType.equalsIgnoreCase("Subjective")) {
            jTabbedPane1.setSelectedIndex(1);
            jTextArea2.setText(originalQuestion);
            jTextField7.setText(marks != null ? marks : "");
        } else {
            jTabbedPane1.setSelectedIndex(0);
            jTextArea1.setText(originalQuestion);
            a.setText(originalA);
            b.setText(originalB);
            c.setText(originalC);
            d.setText(originalD);
            jTextField1.setText(marks != null ? marks : "");

            correctAnswer = originalCorrectAnswer;
            switch (originalCorrectAnswer.toUpperCase()) {
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
    }

    private void populateModulesDropdown() {
        String projectRoot = System.getProperty("user.dir");
        File modulesFile = new File(projectRoot, "src\\main\\java\\oopwj\\data\\modules.txt");

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

    private void selectModuleById(String moduleId) {
        if (moduleId == null || moduleId.isEmpty()) {
            return;
        }

        for (int i = 0; i < jComboBox1.getItemCount(); i++) {
            String item = jComboBox1.getItemAt(i);
            if (item != null && item.startsWith(moduleId + " - ")) {
                jComboBox1.setSelectedIndex(i);
                return;
            }
        }

        String moduleName = getModuleNameById(moduleId);
        if (moduleName != null && !moduleName.isEmpty()) {
            String display = moduleId + " - " + moduleName;
            jComboBox1.addItem(display);
            jComboBox1.setSelectedItem(display);
        }
    }

    public void preselectQuizSet(String moduleId, String quizId, String quizTitle) {
        if (moduleId == null || moduleId.isEmpty()) {
            return;
        }

        selectModuleById(moduleId);
        populateQuizDropdown(moduleId);

        String resolvedQuizId = quizId;
        if (resolvedQuizId == null || resolvedQuizId.isEmpty()) {
            resolvedQuizId = resolveQuizIdFromTitle(moduleId, quizTitle);
        }

        if (resolvedQuizId != null && !resolvedQuizId.isEmpty()) {
            selectQuizById(resolvedQuizId);
            currentQuizID = resolvedQuizId;
        }

        String titleToShow = quizTitle != null ? quizTitle.trim() : "";
        if (titleToShow.isEmpty() && resolvedQuizId != null && !resolvedQuizId.isEmpty()) {
            titleToShow = getQuizTitle(resolvedQuizId);
        }

        if (titleToShow != null && !titleToShow.isEmpty()) {
            jTextField9.setText(titleToShow);
            currentQuizTitle = titleToShow;
            jTextField9.setEditable(true);
        }

        if (resolvedQuizId != null && !resolvedQuizId.isEmpty()) {
            updateQuestionCountLabels(moduleId, resolvedQuizId);
        }
    }

    private String getSelectedQuizId() {
        int selectedIndex = jComboBox2.getSelectedIndex();
        if (selectedIndex >= 0 && selectedIndex < quizIdsForDropdown.size()) {
            return quizIdsForDropdown.get(selectedIndex);
        }
        Object selected = jComboBox2.getSelectedItem();
        return selected != null ? selected.toString().trim() : "";
    }

    private void selectQuizById(String quizId) {
        if (quizId == null || quizId.isEmpty()) {
            return;
        }

        for (int i = 0; i < quizIdsForDropdown.size(); i++) {
            if (quizId.equals(quizIdsForDropdown.get(i))) {
                jComboBox2.setSelectedIndex(i);
                return;
            }
        }
    }

    private String getModuleNameById(String moduleId) {
        String projectRoot = System.getProperty("user.dir");
        File modulesFile = new File(projectRoot, "src\\main\\java\\oopwj\\data\\modules.txt");

        if (!modulesFile.exists()) {
            return null;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(modulesFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String id = parts[0].trim();
                    String name = parts[1].trim();
                    if (id.equals(moduleId)) {
                        return name;
                    }
                }
            }
        } catch (IOException ex) {
            logger.log(java.util.logging.Level.WARNING, "Error reading modules.txt", ex);
        }

        return null;
    }

    private String getNextQuestionId(String moduleId, File quizFile) {
        int maxId = 0;

        if (quizFile.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(quizFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length > 2 && stripQuotes(parts[2].trim()).equals(moduleId)) {
                        String questionId = stripQuotes(parts[0].trim());
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
                logger.log(java.util.logging.Level.SEVERE, "Error reading question.txt", ex);
            }
        }

        return String.format("Q%03d", maxId + 1);
    }

    private void loadQuizQuestions() {
        String projectRoot = System.getProperty("user.dir");
        File quizFile = new File(projectRoot, "src\\main\\java\\oopwj\\data\\question.txt");

        if (!quizFile.exists()) {
            JOptionPane.showMessageDialog(this, "Quiz file not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(quizFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 10) {
                    String moduleId = stripQuotes(parts[0].trim());
                    String moduleName = stripQuotes(parts[1].trim());
                    String questionId = stripQuotes(parts[2].trim());
                    String question = stripQuotes(parts[3].trim());
                    String ansA = stripQuotes(parts[4].trim());
                    String ansB = stripQuotes(parts[5].trim());
                    String ansC = stripQuotes(parts[6].trim());
                    String ansD = stripQuotes(parts[7].trim());
                    String correctAns = stripQuotes(parts[8].trim());
                    String type = stripQuotes(parts[9].trim());

                    System.out.println("Objective Question: " + question);
                } else if (parts.length == 5) {
                    String moduleId = stripQuotes(parts[0].trim());
                    String moduleName = stripQuotes(parts[1].trim());
                    String questionId = stripQuotes(parts[2].trim());
                    String question = stripQuotes(parts[3].trim());
                    String type = stripQuotes(parts[4].trim());

                    System.out.println("Subjective Question: " + question);
                } else {
                    logger.warning("Invalid question format: " + line);
                }
            }
        } catch (IOException ex) {
            logger.log(java.util.logging.Level.SEVERE, "Error reading question.txt", ex);
            JOptionPane.showMessageDialog(this, "Error reading question.txt: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadQuizQuestionsToTable(javax.swing.JTable table) {
        String projectRoot = System.getProperty("user.dir");
        File quizFile = new File(projectRoot, "src\\main\\java\\oopwj\\data\\question.txt");

        if (!quizFile.exists()) {
            JOptionPane.showMessageDialog(this, "Quiz file not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) table.getModel();
        model.setRowCount(0);

        try (BufferedReader br = new BufferedReader(new FileReader(quizFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 10) {
                    String moduleId = stripQuotes(parts[0].trim());
                    String moduleName = stripQuotes(parts[1].trim());
                    String questionId = stripQuotes(parts[2].trim());
                    String question = stripQuotes(parts[3].trim());
                    String ansA = stripQuotes(parts[4].trim());
                    String ansB = stripQuotes(parts[5].trim());
                    String ansC = stripQuotes(parts[6].trim());
                    String ansD = stripQuotes(parts[7].trim());
                    String correctAns = stripQuotes(parts[8].trim());

                    model.addRow(new Object[]{moduleId, moduleName, questionId, question, "Objective"});
                } else if (parts.length == 5) {
                    String moduleId = stripQuotes(parts[0].trim());
                    String moduleName = stripQuotes(parts[1].trim());
                    String questionId = stripQuotes(parts[2].trim());
                    String question = stripQuotes(parts[3].trim());
                    String type = stripQuotes(parts[4].trim());

                    model.addRow(new Object[]{moduleId, moduleName, questionId, type, question});
                } else {
                    logger.warning("Invalid question format: " + line);
                }
            }
        } catch (IOException ex) {
            logger.log(java.util.logging.Level.SEVERE, "Error reading question.txt", ex);
            JOptionPane.showMessageDialog(this, "Error reading question.txt: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveQuestionMarks(String moduleId, String quizId, String questionId, String marks) {
        String projectRoot = System.getProperty("user.dir");
        File marksFile = new File(projectRoot, "src\\main\\java\\oopwj\\data\\TotalQuizMark.txt");

        String normalizedQuizId = quizId != null ? quizId.trim() : "";
        if (moduleId != null && !moduleId.trim().isEmpty() && !normalizedQuizId.startsWith("QZ")) {
            String resolved = resolveQuizIdFromTitle(moduleId.trim(), normalizedQuizId);
            if (resolved != null && !resolved.isEmpty()) {
                normalizedQuizId = resolved;
            }
        }

        java.util.List<String> allLines = new java.util.ArrayList<>();
        boolean updated = false;
        
        if (marksFile.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(marksFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",", -1);
                    if (parts.length >= 4) {
                        String lineModuleId = parts[0].trim();
                        String lineQuizId = parts[1].trim();
                        String lineQuestionId = parts[2].trim();
                        
                                                if (lineModuleId.equals(moduleId) && lineQuizId.equals(normalizedQuizId) && lineQuestionId.equals(questionId)) {
                            StringBuilder sb = new StringBuilder();
                            sb.append(csvEscape(moduleId)).append(",")
                                                            .append(csvEscape(normalizedQuizId)).append(",")
                              .append(csvEscape(questionId)).append(",")
                              .append(csvEscape(marks));
                            allLines.add(sb.toString());
                            updated = true;
                        } else {
                            allLines.add(line);
                        }
                    } else {
                        allLines.add(line);
                    }
                }
            } catch (IOException ex) {
                logger.log(java.util.logging.Level.SEVERE, null, ex);
            }
        }
        
        if (!updated) {
            StringBuilder sb = new StringBuilder();
            sb.append(csvEscape(moduleId)).append(",")
                            .append(csvEscape(normalizedQuizId)).append(",")
              .append(csvEscape(questionId)).append(",")
              .append(csvEscape(marks));
            allLines.add(sb.toString());
        }
        
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(marksFile))) {
            for (String line : allLines) {
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Error saving marks: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        jRadioButton2 = new javax.swing.JRadioButton();
        jTextField5 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
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
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jLabel7 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTextField9 = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();

        jRadioButton2.setText("jRadioButton2");

        jTextField5.setText("jTextField2");
        jTextField5.addActionListener(this::jTextField5ActionPerformed);

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 17));
        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setText("Answer 1:");

        jTextField8.setText("jTextField8");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 204));

        jButton1.setText("Save");
        jButton1.addActionListener(this::jButton1ActionPerformed);

        jButton2.setText("Exit and Discard");
        jButton2.addActionListener(this::jButton2ActionPerformed);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { }));
        jComboBox1.addActionListener(this::jComboBox1ActionPerformed);

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 17));
        jLabel8.setForeground(new java.awt.Color(0, 0, 0));
        jLabel8.setText("Module:");

        jButton4.setText("Clear");
        jButton4.addActionListener(this::jButton4ActionPerformed);

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.setForeground(new java.awt.Color(0, 0, 0));

        jPanel3.setBackground(new java.awt.Color(255, 255, 204));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 17));
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
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jRadioButton1, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jRadioButton5, javax.swing.GroupLayout.Alignment.TRAILING))))
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
                .addGap(14, 14, 14)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
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

        jTextField1.setBackground(new java.awt.Color(255, 255, 255));
        jTextField1.addActionListener(this::jTextField1ActionPerformed);

        jLabel2.setText("Marks:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(9, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(14, 14, 14))
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

        jTextArea2.setColumns(20);
        jTextArea2.setRows(5);
        jScrollPane2.setViewportView(jTextArea2);

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 17));
        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setText("Question:");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 343, Short.MAX_VALUE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(jLabel7)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(17, Short.MAX_VALUE))
        );

        jTextField7.setBackground(new java.awt.Color(255, 255, 255));
        jTextField7.addActionListener(this::jTextField7ActionPerformed);

        jLabel4.setText("Marks:");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 355, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Subjective", jPanel4);

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { }));
        jComboBox2.addActionListener(this::jComboBox2ActionPerformed);

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 17));
        jLabel9.setForeground(new java.awt.Color(0, 0, 0));
        jLabel9.setText("Quiz set");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 17));
        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("Quiz Title:");

        jTextField9.addActionListener(this::jTextField9ActionPerformed);

        jButton5.setText("Add");

        jButton6.setText("Confirm");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 64, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 370, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel8)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jButton5)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jButton6))
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jComboBox2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, 291, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(64, 64, 64))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton2)
                        .addGap(18, 18, 18)
                        .addComponent(jButton4)
                        .addGap(18, 18, 18)
                        .addComponent(jButton1)
                        .addGap(94, 94, 94))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton5)
                    .addComponent(jButton6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 460, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton4)
                    .addComponent(jButton1))
                .addGap(12, 12, 12))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void jTextField7ActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {
        String selectedQuizTitle = (String) jComboBox2.getSelectedItem();
        String selectedQuizId = getSelectedQuizId();
        String selectedModule = (String) jComboBox1.getSelectedItem();
        if ((selectedQuizId == null || selectedQuizId.isEmpty()) && selectedModule != null && !selectedModule.isEmpty()) {
            String[] moduleParts = selectedModule.split(" - ", 2);
            String moduleId = moduleParts[0].trim();
            selectedQuizId = resolveQuizIdFromTitle(moduleId, selectedQuizTitle);
        }
        if (selectedQuizId != null && !selectedQuizId.isEmpty()) {
            currentQuizID = selectedQuizId;

            String quizTitle = selectedQuizTitle != null ? selectedQuizTitle.trim() : "";
            if (quizTitle.isEmpty()) {
                quizTitle = getQuizTitle(selectedQuizId);
            }
            if (quizTitle != null && !quizTitle.isEmpty()) {
                jTextField9.setText(quizTitle);
                currentQuizTitle = quizTitle;
            }
            
            jTextField9.setEditable(true);
            
            if (selectedModule != null && !selectedModule.isEmpty()) {
                String[] moduleParts = selectedModule.split(" - ", 2);
                String moduleId = moduleParts[0].trim();
                updateQuestionCountLabels(moduleId, selectedQuizId);
            }
        }
    }

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {
        String selectedModule = (String) jComboBox1.getSelectedItem();
        
        if (selectedModule == null || selectedModule.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a module before creating a new quiz.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String existingTitle = jTextField9.getText().trim();
        if (!existingTitle.isEmpty()) {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "A quiz title is already entered. Discard it and create a new quiz?",
                    "Confirm",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }
        }

        jTextField9.setText("");
        jTextField9.setEditable(true);
        currentQuizID = "";
        currentQuizTitle = "";
        jComboBox2.removeAllItems();
        quizIdsForDropdown.clear();
        clearInputFields();

        jLabel1.setText("Question:");
        jLabel7.setText("Question:");
    }

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {
        String quizTitle = jTextField9.getText().trim();
        
        if (quizTitle.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a quiz title.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String selectedModule = (String) jComboBox1.getSelectedItem();
        if (selectedModule == null || selectedModule.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a module.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String[] moduleParts = selectedModule.split(" - ", 2);
        String moduleId = moduleParts[0].trim();

        String existingQuizId = currentQuizID != null ? currentQuizID.trim() : "";
        if (existingQuizId.isEmpty()) {
            String selectedQuizId = getSelectedQuizId();
            if (selectedQuizId != null && !selectedQuizId.trim().isEmpty()) {
                existingQuizId = selectedQuizId.trim();
            }
        }
        if (!existingQuizId.isEmpty()) {
            String previousTitle = currentQuizTitle != null ? currentQuizTitle.trim() : "";
            if (previousTitle.isEmpty()) {
                String storedTitle = getQuizTitle(existingQuizId);
                previousTitle = storedTitle != null ? storedTitle.trim() : "";
            }

            boolean titleChanged = !previousTitle.equals(quizTitle);
            if (titleChanged) {
                int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "Update the quiz title to:\n" + quizTitle,
                        "Confirm Title Update",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );
                if (confirm != JOptionPane.YES_OPTION) {
                    return;
                }
            }

            if (updateQuizTitleInFile(existingQuizId, moduleId, quizTitle)) {
                currentQuizTitle = quizTitle;
                JOptionPane.showMessageDialog(this, "Quiz title updated successfully!\nQuiz ID: " + existingQuizId + "\nTitle: " + quizTitle, "Success", JOptionPane.INFORMATION_MESSAGE);

                populateQuizDropdown(moduleId);
                selectQuizById(existingQuizId);
                updateQuestionCountLabels(moduleId, existingQuizId);
            } else {
                boolean updatedByTitle = false;
                if (!previousTitle.isEmpty()) {
                    String resolvedId = resolveQuizIdFromTitle(moduleId, previousTitle);
                    if (resolvedId != null && !resolvedId.trim().isEmpty()) {
                        existingQuizId = resolvedId.trim();
                    }
                    updatedByTitle = updateQuizTitleByTitle(moduleId, previousTitle, quizTitle);
                }

                if (updatedByTitle) {
                    currentQuizTitle = quizTitle;
                    JOptionPane.showMessageDialog(this, "Quiz title updated successfully!\nQuiz ID: " + existingQuizId + "\nTitle: " + quizTitle, "Success", JOptionPane.INFORMATION_MESSAGE);

                    populateQuizDropdown(moduleId);
                    if (existingQuizId != null && !existingQuizId.trim().isEmpty()) {
                        selectQuizById(existingQuizId);
                    }
                    updateQuestionCountLabels(moduleId, existingQuizId);
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update quiz title.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            return;
        }
        
        String newQuizId = generateNextQuizId(moduleId);
        
        if (saveQuizToFile(newQuizId, moduleId, quizTitle)) {
            currentQuizID = newQuizId;
            currentQuizTitle = quizTitle;
            
            JOptionPane.showMessageDialog(this, "Quiz created successfully!\nQuiz ID: " + newQuizId + "\nTitle: " + quizTitle, "Success", JOptionPane.INFORMATION_MESSAGE);
            
            jTextField9.setText("");
            
            populateQuizDropdown(moduleId);
            selectQuizById(newQuizId);
            
            updateQuestionCountLabels(moduleId, newQuizId);
        } else {
            JOptionPane.showMessageDialog(this, "Failed to create quiz.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void jTextField9ActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        String projectRoot = System.getProperty("user.dir");
        File temp = new File(projectRoot, "src\\main\\java\\oopwj\\data\\TempQues.txt");
        File quizFile = new File(projectRoot, "src\\main\\java\\oopwj\\data\\question.txt");

        int selectedTabIndex = jTabbedPane1.getSelectedIndex();

        if (selectedTabIndex == 0) {
            String question = jTextArea1.getText().trim();
            String a1 = a.getText().trim();
            String a2 = b.getText().trim();
            String a3 = c.getText().trim();
            String a4 = d.getText().trim();
            String marks = jTextField1.getText().trim();

            if (question.isEmpty() || a1.isEmpty() || a2.isEmpty() || a3.isEmpty() || a4.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields for the objective question before saving.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (correctAnswer.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select the correct answer for the objective question.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (marks.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter the marks for this question.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!isValidMarks(marks)) {
                JOptionPane.showMessageDialog(this, "Marks must be a whole number greater than 0.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } else if (selectedTabIndex == 1) {
            String subjectiveQuestion = jTextArea2.getText().trim();
            String marks = jTextField7.getText().trim();

            if (subjectiveQuestion.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please type your subjective question before saving.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (marks.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter the marks for this question.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!isValidMarks(marks)) {
                JOptionPane.showMessageDialog(this, "Marks must be a whole number greater than 0.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String selectedModule = (String) jComboBox1.getSelectedItem();
            if (selectedModule == null || selectedModule.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select a module before saving the question.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        if (currentQuizID == null || currentQuizID.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please create or select a quiz before saving questions.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmResult = JOptionPane.showConfirmDialog(this, "Are you sure you want to save the questions?", "Confirm Save", JOptionPane.YES_NO_OPTION);
        if (confirmResult != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            if (isEditMode) {
                updateExistingQuestion(quizFile);
                JOptionPane.showMessageDialog(this, "Question updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            if (temp.exists()) {
                java.util.List<String> newQuestions = new java.util.ArrayList<>();
                try (BufferedReader br = new BufferedReader(new FileReader(temp))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        newQuestions.add(line);
                    }
                } catch (IOException ex) {
                    logger.log(java.util.logging.Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(this, "Failed to read from TempQues.txt: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                java.util.List<String> assignedIds = insertQuestionsAndRenumber(quizFile, newQuestions, currentQuizID);

                try {
                    Files.deleteIfExists(temp.toPath());
                } catch (IOException ex) {
                    logger.log(java.util.logging.Level.WARNING, null, ex);
                }

                jTextArea1.setText("");
                a.setText("");
                b.setText("");
                c.setText("");
                d.setText("");
                jTextField1.setText("");
                jTextArea2.setText("");
                jTextField7.setText("");

                sessionQuestionCount = 0;

                String selectedModuleForLabel = (String) jComboBox1.getSelectedItem();
                if (selectedModuleForLabel != null && !selectedModuleForLabel.isEmpty() && currentQuizID != null && !currentQuizID.isEmpty()) {
                    String[] modulePartsForLabel = selectedModuleForLabel.split(" - ", 2);
                    String moduleIdForLabel = modulePartsForLabel[0].trim();
                    updateQuestionCountLabels(moduleIdForLabel, currentQuizID);
                }

                JOptionPane.showMessageDialog(this, "Quiz saved successfully", "Saved", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            StringBuilder sb = new StringBuilder();
            String selectedModule = (String) jComboBox1.getSelectedItem();
            String[] moduleParts = selectedModule.split(" - ", 2);
            String moduleId = moduleParts[0].trim();
            String quizIdForSave = resolveQuizIdForSave(moduleId);
            if (quizIdForSave.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select a quiz before saving questions.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }

                        if (selectedTabIndex == 0) {
                                String question = jTextArea1.getText().trim();
                                String a1 = a.getText().trim();
                                String a2 = b.getText().trim();
                                String a3 = c.getText().trim();
                                String a4 = d.getText().trim();
                                String marks = jTextField1.getText().trim();

                                sb.append(csvEscape("TEMP"))
                                    .append(",")
                                    .append(csvEscape(quizIdForSave)).append(",")
                                    .append(csvEscape(moduleId)).append(",")
                                    .append(csvQuote(question)).append(",")
                                    .append(csvEscape(a1)).append(",")
                                    .append(csvEscape(a2)).append(",")
                                    .append(csvEscape(a3)).append(",")
                                    .append(csvEscape(a4)).append(",")
                                    .append(csvEscape(correctAnswer)).append(",")
                                    .append(csvEscape("Objective"));

                        } else if (selectedTabIndex == 1) {
                                String subjectiveQuestion = jTextArea2.getText().trim();
                                String marks = jTextField7.getText().trim();

                                sb.append(csvEscape("TEMP"))
                                    .append(",")
                                    .append(csvEscape(quizIdForSave)).append(",")
                                    .append(csvEscape(moduleId)).append(",")
                                    .append(csvQuote(subjectiveQuestion)).append(",")
                                    .append(csvEscape("Subjective"));
                        }

            java.util.List<String> singleQuestion = new java.util.ArrayList<>();
            singleQuestion.add(sb.toString());
            java.util.List<String> assignedIds = insertQuestionsAndRenumber(quizFile, singleQuestion, quizIdForSave);

            if (!assignedIds.isEmpty()) {
                if (selectedTabIndex == 0) {
                    String marks = jTextField1.getText().trim();
                    saveQuestionMarks(moduleId, quizIdForSave, assignedIds.get(0), marks);
                } else if (selectedTabIndex == 1) {
                    String marks = jTextField7.getText().trim();
                    saveQuestionMarks(moduleId, quizIdForSave, assignedIds.get(0), marks);
                }
            }

            updateQuestionCountLabels(moduleId, quizIdForSave);

        } catch (Exception ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Failed to save quiz: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        jTextArea1.setText("");
        jTextArea2.setText("");
        a.setText("");
        b.setText("");
        c.setText("");
        d.setText("");
        jTextField1.setText("");
        jTextField7.setText("");
        answerGroup.clearSelection();
        correctAnswer = "";

        sessionQuestionCount = 0;

        JOptionPane.showMessageDialog(this, "Quiz saved successfully", "Saved", JOptionPane.INFORMATION_MESSAGE);
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        String projectRoot = System.getProperty("user.dir");
        File temp = new File(projectRoot, "src\\main\\java\\oopwj\\data\\TempQues.txt");
        
        String question = jTextArea1.getText().trim();
        String a1 = a.getText().trim();
        String a2 = b.getText().trim();
        String a3 = c.getText().trim();
        String a4 = d.getText().trim();
        
        boolean needsConfirmation = false;
        
        if (temp.exists()) {
            needsConfirmation = true;
        }
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
        else {
            boolean hasFormData = !question.isEmpty() || !a1.isEmpty() || !a2.isEmpty() || !a3.isEmpty() || !a4.isEmpty();
            if (hasFormData) {
                needsConfirmation = true;
            }
        }
        
        if (needsConfirmation) {
            int confirmResult = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit and discard all changes?", "Confirm Exit", JOptionPane.YES_NO_OPTION);
            if (confirmResult != JOptionPane.YES_OPTION) {
                return;
            }
        }

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

        sessionQuestionCount = 0;

        java.awt.EventQueue.invokeLater(() -> {
            if (parentWindow != null) {
                parentWindow.refreshTableData();
                parentWindow.setVisible(true);
            } else {
                new Assessments(lecturerID, null).setVisible(true);
            }
        });
        this.dispose();
    }

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void jTextField4ActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void jTextField5ActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void jTextField6ActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {
        String selectedModule = (String) jComboBox1.getSelectedItem();
        if (selectedModule != null && !selectedModule.isEmpty()) {
            String[] moduleParts = selectedModule.split(" - ", 2);
            String moduleId = moduleParts[0].trim();
            populateQuizDropdown(moduleId);
            String selectedQuizId = getSelectedQuizId();
            if (selectedQuizId != null && !selectedQuizId.isEmpty()) {
                updateQuestionCountLabels(moduleId, selectedQuizId);
            }
        }
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

    private void clearInputFields() {
        jTextArea1.setText("");
        jTextArea2.setText("");
        a.setText("");
        b.setText("");
        c.setText("");
        d.setText("");
        jTextField1.setText("");
        jTextField7.setText("");
        answerGroup.clearSelection();
        correctAnswer = "";
        jLabel1.setText("Question:");
        jLabel7.setText("Question:");
    }

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {
        boolean hasData = false;
        int selectedTabIndex = jTabbedPane1.getSelectedIndex();
        if (selectedTabIndex == 0) {
            if (!jTextArea1.getText().trim().isEmpty() ||
                !a.getText().trim().isEmpty() ||
                !b.getText().trim().isEmpty() ||
                !c.getText().trim().isEmpty() ||
                !d.getText().trim().isEmpty() ||
                !jTextField1.getText().trim().isEmpty() ||
                answerGroup.getSelection() != null) {
                hasData = true;
            }
        } else if (selectedTabIndex == 1) {
            if (!jTextArea2.getText().trim().isEmpty() ||
                !jTextField7.getText().trim().isEmpty()) {
                hasData = true;
            }
        }
        if (hasData) {
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "There is data in the input fields. Are you sure you want to clear all fields?",
                "Confirm Clear",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }
        }
        clearInputFields();
    }

    private String csvEscape(String s) {
        if (s == null) return "";
        String value = s.replace("\r", "").replace("\n", " ").trim();
        return value.replace("\"", "");
    }

    private String csvQuote(String s) {
        if (s == null) return "\"\"";
        String value = s.replace("\r", "").replace("\n", " ").trim();
        value = value.replace("\"", "");
        return "\"" + value + "\"";
    }

    private String stripQuotes(String s) {
        if (s == null) return "";
        String value = s.trim();
        if (value.length() >= 2 && value.startsWith("\"") && value.endsWith("\"")) {
            return value.substring(1, value.length() - 1);
        }
        return value;
    }

    private boolean isValidMarks(String marks) {
        if (marks == null || marks.trim().isEmpty()) {
            return false;
        }

        String value = marks.trim();
        if (!value.matches("\\d+")) {
            return false;
        }

        try {
            return Integer.parseInt(value) > 0;
        } catch (NumberFormatException ex) {
            return false;
        }
    }
    
    private java.util.List<String> insertQuestionsAndRenumber(File quizFile, java.util.List<String> newQuestions, String targetQuizID) throws IOException {
        java.util.List<String> allLines = new java.util.ArrayList<>();
        java.util.List<String> assignedIds = new java.util.ArrayList<>();
        
        if (quizFile.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(quizFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    allLines.add(line);
                }
            }
        }
        
        String targetModuleID = null;
        if (!newQuestions.isEmpty()) {
            String[] firstNewParts = newQuestions.get(0).split(",", -1);
            if (firstNewParts.length >= 3) {
                targetModuleID = stripQuotes(firstNewParts[2].trim());
            }
        }
        
        int insertionIndex = -1;
        for (int i = allLines.size() - 1; i >= 0; i--) {
            String line = allLines.get(i);
            String[] parts = line.split(",", -1);
            if (parts.length >= 3) {
                String quizId = stripQuotes(parts[1].trim());
                String moduleId = stripQuotes(parts[2].trim());
                if (quizId.equals(targetQuizID) && moduleId.equals(targetModuleID)) {
                    insertionIndex = i + 1;
                    break;
                }
            }
        }
        
        if (insertionIndex == -1) {
            insertionIndex = allLines.size();
        }
        
        allLines.addAll(insertionIndex, newQuestions);
        
        int questionCounter = 0;
        for (int i = 0; i < allLines.size(); i++) {
            String line = allLines.get(i);
            String[] parts = line.split(",", -1);
            if (parts.length >= 3) {
                String quizId = stripQuotes(parts[1].trim());
                String moduleId = stripQuotes(parts[2].trim());
                
                if (quizId.equals(targetQuizID) && moduleId.equals(targetModuleID)) {
                    questionCounter++;
                    String newQuestionId = String.format("Q%03d", questionCounter);
                    parts[0] = csvEscape(newQuestionId);
                    allLines.set(i, String.join(",", parts));
                }
            }
        }
        
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(quizFile))) {
            for (String line : allLines) {
                bw.write(line);
                bw.newLine();
            }
        }

        if (!newQuestions.isEmpty()) {
            int start = Math.max(0, insertionIndex);
            int end = Math.min(allLines.size(), insertionIndex + newQuestions.size());
            for (int i = start; i < end; i++) {
                String[] parts = allLines.get(i).split(",", -1);
                if (parts.length >= 1) {
                    assignedIds.add(stripQuotes(parts[0].trim()));
                }
            }
        }

        return assignedIds;
    }
    
    private void updateExistingQuestion(File quizFile) throws IOException {
        String selectedModule = (String) jComboBox1.getSelectedItem();
        String[] moduleParts = selectedModule.split(" - ", 2);
        String moduleId = moduleParts[0].trim();
        String quizIdForSave = resolveQuizIdForSave(moduleId);
        
        String questionIdLabel = jLabel1.getText();
        String questionNumber = questionIdLabel.replace("Question:", "").trim();
        String questionId = String.format("Q%03d", Integer.parseInt(questionNumber));
        
        int selectedTabIndex = jTabbedPane1.getSelectedIndex();
        
        StringBuilder updatedLine = new StringBuilder();
         updatedLine.append(csvEscape(questionId)).append(",")
             .append(csvEscape(quizIdForSave)).append(",")
             .append(csvEscape(moduleId)).append(",");
        
        if (selectedTabIndex == 0) {
            String question = jTextArea1.getText().trim();
            String a1 = a.getText().trim();
            String a2 = b.getText().trim();
            String a3 = c.getText().trim();
            String a4 = d.getText().trim();
            String marks = jTextField1.getText().trim();
            
            updatedLine.append(csvQuote(question)).append(",")
                       .append(csvEscape(a1)).append(",")
                       .append(csvEscape(a2)).append(",")
                       .append(csvEscape(a3)).append(",")
                       .append(csvEscape(a4)).append(",")
                       .append(csvEscape(correctAnswer)).append(",")
                       .append(csvEscape("Objective"));
            
            saveQuestionMarks(moduleId, quizIdForSave, questionId, marks);
        } else if (selectedTabIndex == 1) {
            String subjectiveQuestion = jTextArea2.getText().trim();
            String marks = jTextField7.getText().trim();
            
            updatedLine.append(csvQuote(subjectiveQuestion)).append(",")
                       .append(csvEscape("Subjective"));
            
            saveQuestionMarks(moduleId, quizIdForSave, questionId, marks);
        }
        
        java.util.List<String> allLines = new java.util.ArrayList<>();
        if (quizFile.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(quizFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    allLines.add(line);
                }
            }
        }
        
        boolean found = false;
        for (int i = 0; i < allLines.size(); i++) {
            String line = allLines.get(i);
            String[] parts = line.split(",", -1);
            if (parts.length >= 3) {
                String lineQuestionId = stripQuotes(parts[0].trim());
                String lineQuizId = stripQuotes(parts[1].trim());
                String lineModuleId = stripQuotes(parts[2].trim());
                
                boolean quizIdMatches = lineQuizId.equals(quizIdForSave)
                    || lineQuizId.equals(currentQuizID)
                    || (!currentQuizTitle.isEmpty() && lineQuizId.equals(currentQuizTitle));
                if (lineQuestionId.equals(questionId) && quizIdMatches && lineModuleId.equals(moduleId)) {
                    allLines.set(i, updatedLine.toString());
                    found = true;
                    break;
                }
            }
        }
        
        if (!found) {
            throw new IOException("Could not find the question to update in question.txt");
        }
        
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(quizFile))) {
            for (String line : allLines) {
                bw.write(line);
                bw.newLine();
            }
        }
    }
    
    private String generateNextQuizId(String moduleId) {
        String projectRoot = System.getProperty("user.dir");
        File quizFile = new File(projectRoot, "src\\main\\java\\oopwj\\data\\Quiz.txt");
        int maxId = 0;
        
        if (quizFile.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(quizFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 2) {
                        String quizId = parts[0].trim();
                        String quizModuleId = parts[1].trim();
                        
                        if (quizModuleId.equals(moduleId) && quizId.startsWith("QZ")) {
                            try {
                                int id = Integer.parseInt(quizId.substring(2));
                                if (id > maxId) {
                                    maxId = id;
                                }
                            } catch (NumberFormatException e) {
                                logger.log(java.util.logging.Level.WARNING, "Invalid quiz ID format: " + quizId, e);
                            }
                        }
                    }
                }
            } catch (IOException ex) {
                logger.log(java.util.logging.Level.SEVERE, "Error reading Quiz.txt", ex);
            }
        }
        
        return String.format("QZ%03d", maxId + 1);
    }
    
    private boolean saveQuizToFile(String quizId, String moduleId, String title) {
        String projectRoot = System.getProperty("user.dir");
        File quizFile = new File(projectRoot, "src\\main\\java\\oopwj\\data\\Quiz.txt");
        
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(quizFile, true))) {
            bw.write(quizId + "," + moduleId + "," + csvEscape(title));
            bw.newLine();
            return true;
        } catch (IOException ex) {
            logger.log(java.util.logging.Level.SEVERE, "Error writing to Quiz.txt", ex);
            return false;
        }
    }

    private boolean updateQuizTitleInFile(String quizId, String moduleId, String title) {
        String projectRoot = System.getProperty("user.dir");
        File quizFile = new File(projectRoot, "src\\main\\java\\oopwj\\data\\Quiz.txt");

        if (!quizFile.exists()) {
            return false;
        }

        java.util.List<String> allLines = new java.util.ArrayList<>();
        String targetQuizId = normalizeToken(quizId);
        String targetModuleId = normalizeToken(moduleId);
        boolean updated = false;
        int quizIdMatchCount = 0;
        int quizIdMatchIndex = -1;
        String quizIdMatchModuleId = "";

        try (BufferedReader br = new BufferedReader(new FileReader(quizFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", 3);
                if (parts.length >= 2) {
                    String lineQuizId = normalizeToken(parts[0]);
                    String lineModuleId = normalizeToken(parts[1]);
                    if (lineQuizId.equals(targetQuizId)) {
                        quizIdMatchCount++;
                        if (quizIdMatchIndex == -1) {
                            quizIdMatchIndex = allLines.size();
                            quizIdMatchModuleId = lineModuleId;
                        }
                    }
                    if (lineQuizId.equals(targetQuizId) && lineModuleId.equals(targetModuleId)) {
                        String newLine = lineQuizId + "," + lineModuleId + "," + csvEscape(title);
                        allLines.add(newLine);
                        updated = true;
                        continue;
                    }
                }
                allLines.add(line);
            }
        } catch (IOException ex) {
            logger.log(java.util.logging.Level.SEVERE, "Error reading Quiz.txt", ex);
            return false;
        }

        if (!updated && quizIdMatchCount == 1 && quizIdMatchIndex >= 0) {
            String fallbackLine = targetQuizId + "," + quizIdMatchModuleId + "," + csvEscape(title);
            allLines.set(quizIdMatchIndex, fallbackLine);
            updated = true;
        }

        if (!updated) {
            return false;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(quizFile))) {
            for (String line : allLines) {
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException ex) {
            logger.log(java.util.logging.Level.SEVERE, "Error writing Quiz.txt", ex);
            return false;
        }

        return true;
    }

    private boolean updateQuizTitleByTitle(String moduleId, String oldTitle, String newTitle) {
        if (oldTitle == null || oldTitle.trim().isEmpty()) {
            return false;
        }

        String projectRoot = System.getProperty("user.dir");
        File quizFile = new File(projectRoot, "src\\main\\java\\oopwj\\data\\Quiz.txt");

        if (!quizFile.exists()) {
            return false;
        }

        String targetModuleId = normalizeToken(moduleId);
        String targetOldTitle = normalizeToken(oldTitle);

        java.util.List<String> allLines = new java.util.ArrayList<>();
        boolean updated = false;

        try (BufferedReader br = new BufferedReader(new FileReader(quizFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", 3);
                if (parts.length >= 3) {
                    String lineQuizId = normalizeToken(parts[0]);
                    String lineModuleId = normalizeToken(parts[1]);
                    String lineTitle = normalizeToken(parts[2]);
                    if (lineModuleId.equals(targetModuleId) && lineTitle.equals(targetOldTitle)) {
                        String newLine = lineQuizId + "," + lineModuleId + "," + csvEscape(newTitle);
                        allLines.add(newLine);
                        updated = true;
                        continue;
                    }
                }
                allLines.add(line);
            }
        } catch (IOException ex) {
            logger.log(java.util.logging.Level.SEVERE, "Error reading Quiz.txt", ex);
            return false;
        }

        if (!updated) {
            return false;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(quizFile))) {
            for (String line : allLines) {
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException ex) {
            logger.log(java.util.logging.Level.SEVERE, "Error writing Quiz.txt", ex);
            return false;
        }

        return true;
    }

    private String normalizeToken(String value) {
        if (value == null) {
            return "";
        }
        String trimmed = value.trim();
        if (!trimmed.isEmpty() && trimmed.charAt(0) == '\uFEFF') {
            return trimmed.substring(1);
        }
        return trimmed;
    }
    
    private void populateQuizDropdown(String moduleId) {
        String projectRoot = System.getProperty("user.dir");
        File quizFile = new File(projectRoot, "src\\main\\java\\oopwj\\data\\Quiz.txt");
        
        jComboBox2.removeAllItems();
        quizIdsForDropdown.clear();
        
        if (!quizFile.exists()) {
            return;
        }
        
        try (BufferedReader br = new BufferedReader(new FileReader(quizFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", 3);
                if (parts.length >= 2) {
                    String quizId = parts[0].trim();
                    String quizModuleId = parts[1].trim();
                    String quizTitle = parts.length >= 3 ? parts[2].trim() : "";
                    
                    if (quizModuleId.equals(moduleId)) {
                        String displayTitle = (quizTitle == null || quizTitle.isEmpty()) ? quizId : quizTitle;
                        jComboBox2.addItem(displayTitle);
                        quizIdsForDropdown.add(quizId);
                    }
                }
            }
        } catch (IOException ex) {
            logger.log(java.util.logging.Level.SEVERE, "Error reading Quiz.txt", ex);
        }
    }

    private String getQuizTitle(String quizId) {
        String projectRoot = System.getProperty("user.dir");
        File quizFile = new File(projectRoot, "src\\main\\java\\oopwj\\data\\Quiz.txt");
        
        if (!quizFile.exists()) {
            return null;
        }
        
        try (BufferedReader br = new BufferedReader(new FileReader(quizFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", 3);
                if (parts.length >= 3) {
                    String id = parts[0].trim();
                    
                    if (id.equals(quizId)) {
                        String title = parts[2].trim();
                        return title;
                    }
                }
            }
        } catch (IOException ex) {
            logger.log(java.util.logging.Level.SEVERE, "Error reading Quiz.txt", ex);
        }
        
        return null;
    }

    private String resolveQuizIdFromTitle(String moduleId, String quizTitle) {
        if (moduleId == null || moduleId.isEmpty() || quizTitle == null || quizTitle.trim().isEmpty()) {
            return "";
        }

        String projectRoot = System.getProperty("user.dir");
        File quizFile = new File(projectRoot, "src\\main\\java\\oopwj\\data\\Quiz.txt");

        if (!quizFile.exists()) {
            return "";
        }

        String targetTitle = quizTitle.trim();

        try (BufferedReader br = new BufferedReader(new FileReader(quizFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", 3);
                if (parts.length >= 2) {
                    String quizId = parts[0].trim();
                    String quizModuleId = parts[1].trim();
                    String title = parts.length >= 3 ? parts[2].trim() : "";

                    if (quizModuleId.equals(moduleId) && (targetTitle.equals(title) || targetTitle.equals(quizId))) {
                        return quizId;
                    }
                }
            }
        } catch (IOException ex) {
            logger.log(java.util.logging.Level.SEVERE, "Error reading Quiz.txt", ex);
        }

        return "";
    }

    private String resolveQuizIdForSave(String moduleId) {
        String quizId = currentQuizID != null ? currentQuizID.trim() : "";
        if (quizId.startsWith("QZ")) {
            return quizId;
        }

        String titleCandidate = currentQuizTitle != null ? currentQuizTitle.trim() : "";
        if (titleCandidate.isEmpty()) {
            titleCandidate = jTextField9.getText().trim();
        }

        String resolved = resolveQuizIdFromTitle(moduleId, titleCandidate.isEmpty() ? quizId : titleCandidate);
        return resolved != null && !resolved.isEmpty() ? resolved : quizId;
    }

    private int countQuestionsFor(String moduleId, String quizId) {
        if (moduleId == null || moduleId.isEmpty() || quizId == null || quizId.isEmpty()) {
            return 0;
        }

        String projectRoot = System.getProperty("user.dir");
        File questionFile = new File(projectRoot, "src\\main\\java\\oopwj\\data\\question.txt");

        if (!questionFile.exists()) {
            return 0;
        }

        int count = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(questionFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",", -1);
                if (fields.length >= 3) {
                    String qQuizId = stripQuotes(fields[1].trim());
                    String qModuleId = stripQuotes(fields[2].trim());
                    if (qModuleId.equals(moduleId) && qQuizId.equals(quizId)) {
                        count++;
                    }
                }
            }
        } catch (IOException ex) {
            logger.log(java.util.logging.Level.SEVERE, "Error reading question.txt", ex);
        }

        return count;
    }

    private void updateQuestionCountLabels(String moduleId, String quizId) {
        if (moduleId == null || moduleId.isEmpty() || quizId == null || quizId.isEmpty()) {
            jLabel1.setText("Question:");
            jLabel7.setText("Question:");
            return;
        }

        int count = countQuestionsFor(moduleId, quizId);
        String labelText = "Question: " + (count + 1);
        jLabel1.setText(labelText);
        jLabel7.setText(labelText);
    }
    
    private String getNextQuestionID(String moduleId, String quizId) {
        String projectRoot = System.getProperty("user.dir");
        File questionFile = new File(projectRoot, "src\\main\\java\\oopwj\\data\\question.txt");
        
        int maxId = 0;
        
        if (questionFile.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(questionFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] fields = line.split(",", -1);
                    if (fields.length >= 3) {
                        String questionId = stripQuotes(fields[0].trim());
                        String qQuizId = stripQuotes(fields[1].trim());
                        String qModuleId = stripQuotes(fields[2].trim());
                        
                        if (qModuleId.equals(moduleId) && qQuizId.equals(quizId)) {
                            if (questionId.startsWith("Q")) {
                                try {
                                    int id = Integer.parseInt(questionId.substring(1));
                                    if (id > maxId) {
                                        maxId = id;
                                    }
                                } catch (NumberFormatException e) {
                                    logger.log(java.util.logging.Level.WARNING, "Invalid QuestionID format: " + questionId);
                                }
                            }
                        }
                    }
                }
            } catch (IOException ex) {
                logger.log(java.util.logging.Level.SEVERE, "Error reading question.txt", ex);
            }
        }
        
        return String.format("Q%03d", maxId + 1);
    }

    public static void main(String args[]) {
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

        java.awt.EventQueue.invokeLater(() -> new Quiz().setVisible(true));
    }

    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
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
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
}
