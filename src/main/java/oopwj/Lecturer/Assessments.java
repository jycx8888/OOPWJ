/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package oopwj.Lecturer;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author User
 */
public class Assessments extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Assessments.class.getName());
    private String currentDataType = null; 
    private String lecturerID;
    private Lecturer_menu parentWindow;
    private Map<String, String> lecturerModules = new HashMap<>(); 
    private Map<String, String> allModuleNames = new HashMap<>(); 
    private List<String> originalQuizLines = new ArrayList<>(); 
    private List<Integer> displayedQuizLineIndices = new ArrayList<>();
    private List<String> displayedQuizSetKeys = new ArrayList<>(); 


    public Assessments() {
        this(null, null);
    }
    
    public Assessments(String lecturerID, Lecturer_menu parentWindow) {
        this.lecturerID = lecturerID;
        this.parentWindow = parentWindow;
        loadLecturerModules(); 
        initComponents();
        resetPanelLayoutForLabel();
        jLabel1.setText("Filter:");
        centerWindow();
        setupTable();
        setupSearchField();
        loadFilterOptionsToComboBox(); 
        displayAssignedModules(); 
        loadQuizData();
        currentDataType = "question";
        jButton1.addActionListener(this::jButton1ActionPerformed);
        jButton2.addActionListener(this::jButton2ActionPerformed);
        jButton4.addActionListener(this::jButton4ActionPerformed);
        jButton6.addActionListener(this::jButton6ActionPerformed);
        jComboBox1.addActionListener(this::jComboBox1ActionPerformed);
        addHoverEffect(jButton1,new Color(220,80,80),new Color(220,80,80));
        addHoverEffect(jButton2,new Color(70,130,180),new Color(70,130,180));
        addHoverEffect(jButton3,new Color(70,130,180),new Color(70,130,180));
        addHoverEffect(jButton4,new Color(70,130,180),new Color(70,130,180));
        addHoverEffect(jButton5,new Color(70,130,180),new Color(70,130,180));
        addHoverEffect(jButton6,new Color(70,130,180),new Color(70,130,180));
    }

    private void centerWindow() {
        setLocationRelativeTo(null);
    }
    
    private void setupSearchField() {
        jTextField1.setForeground(java.awt.Color.GRAY);
        jTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (jTextField1.getText().equals("Search")) {
                    jTextField1.setText("");
                    jTextField1.setForeground(java.awt.Color.BLACK);
                }
            }
            
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (jTextField1.getText().isEmpty()) {
                    jTextField1.setText("Search");
                    jTextField1.setForeground(java.awt.Color.GRAY);
                }
            }
        });
    }
    
    private void setupTable() {
        String[] columnNames = {};
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object[][] {},
            columnNames
        ));
        centerAlignTable(jTable2);
    }

    private void setTableLabel(String labelText) {
        if (labelText == null || labelText.trim().isEmpty()) {
            jLabel2.setText("Table");
        } else {
            jLabel2.setText(labelText);
        }
        updateLabelSizeToText(jLabel2);
    }

    private void updateLabelSizeToText(javax.swing.JLabel label) {
        if (label == null) {
            return;
        }
        java.awt.Font font = label.getFont();
        java.awt.FontMetrics metrics = label.getFontMetrics(font);
        int textWidth = metrics.stringWidth(label.getText());
        int textHeight = metrics.getHeight();
        java.awt.Insets insets = label.getInsets();
        int width = textWidth + insets.left + insets.right;
        int height = textHeight + insets.top + insets.bottom;

        java.awt.Dimension size = new java.awt.Dimension(width, height);
        label.setPreferredSize(size);
        label.setMinimumSize(size);
        label.revalidate();
    }

    private void resetPanelLayoutForLabel() {
        java.awt.LayoutManager layout = jPanel1.getLayout();
        if (!(layout instanceof javax.swing.GroupLayout)) {
            return;
        }

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(61, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton2)
                        .addGap(59, 59, 59)
                        .addComponent(jButton4)
                        .addGap(60, 60, 60)
                        .addComponent(jButton5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton6)
                        .addGap(52, 52, 52)
                        .addComponent(jButton1))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 614, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(60, 60, 60))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton1)
                    .addComponent(jButton4)
                    .addComponent(jButton5)
                    .addComponent(jButton6))
                .addGap(27, 27, 27))
        );
    }
    
    private void centerAlignTable(javax.swing.JTable table) {
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            table.getColumnModel().getColumn(i).getHeaderRenderer();
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
                    0, 0, new Color(230, 240, 255),
                    0, getHeight(), new Color(245, 247, 250)
                );

                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }

        };
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jTextField1 = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jButton6 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setOpaque(false);

        jButton1.setText("Back");

        jButton2.setText("Add");

        jScrollPane2.setViewportView(jTable2);

        jTextField1.setText("Search");
        jTextField1.addActionListener(this::jTextField1ActionPerformed);

        jButton3.setText("Search");
        jButton3.addActionListener(this::jButton3ActionPerformed);

        jButton4.setText("Edit");

        jButton5.setText("Delete");
        jButton5.addActionListener(this::jButton5ActionPerformed);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Module:");

        jButton6.setText("Feedback");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("Table");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(61, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(170, 170, 170)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton2)
                        .addGap(59, 59, 59)
                        .addComponent(jButton4)
                        .addGap(60, 60, 60)
                        .addComponent(jButton5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton6)
                        .addGap(52, 52, 52)
                        .addComponent(jButton1))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 614, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(60, 60, 60))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(319, 319, 319)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton1)
                    .addComponent(jButton4)
                    .addComponent(jButton5)
                    .addComponent(jButton6))
                .addGap(27, 27, 27))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        performSearch();
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        performSearch();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        if (!"question".equals(currentDataType) && !"quizSets".equals(currentDataType)) {
            JOptionPane.showMessageDialog(this, "Please load questions or quiz sets before deleting.", "Delete", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int[] selectedRows = jTable2.getSelectedRows();
        
        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(this, "Please select a row to delete", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String confirmationMessage = "Are you sure you want to delete " + selectedRows.length + " row(s)?";
        if ("quizSets".equals(currentDataType)) {
            confirmationMessage = "Are you sure you want to delete " + selectedRows.length + " quiz set(s)?\n" +
                "This will also delete related questions in question.txt.";
        }

        int confirmation = JOptionPane.showConfirmDialog(this,
            confirmationMessage,
            "Confirm Deletion",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirmation == JOptionPane.YES_OPTION) {
            deleteSelectedRows(selectedRows);
        }
    }//GEN-LAST:event_jButton5ActionPerformed
    
    private void deleteSelectedRows(int[] selectedRows) {
        DefaultTableModel model = (DefaultTableModel) jTable2.getModel();

        java.util.Arrays.sort(selectedRows);

        if ("question".equals(currentDataType)) {

            java.util.Set<Integer> linesToDelete = new java.util.HashSet<>();
            for (int selectedRow : selectedRows) {
                if (selectedRow >= 0 && selectedRow < model.getRowCount()) {
                    if (selectedRow < displayedQuizLineIndices.size()) {
                        linesToDelete.add(displayedQuizLineIndices.get(selectedRow));
                    }
                }
            }


            for (int i = selectedRows.length - 1; i >= 0; i--) {
                model.removeRow(selectedRows[i]);
            }


            updateQuizFile(linesToDelete);
            JOptionPane.showMessageDialog(this, "Row(s) deleted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        if ("quizSets".equals(currentDataType)) {
            java.util.Set<String> quizSetKeysToDelete = new java.util.HashSet<>();
            for (int selectedRow : selectedRows) {
                if (selectedRow >= 0 && selectedRow < displayedQuizSetKeys.size()) {
                    quizSetKeysToDelete.add(displayedQuizSetKeys.get(selectedRow));
                }
            }

            if (quizSetKeysToDelete.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Unable to determine selected quiz sets.", "Delete", JOptionPane.WARNING_MESSAGE);
                return;
            }

            deleteQuizSets(quizSetKeysToDelete);
            loadQuizSetsData();
            JOptionPane.showMessageDialog(this, "Quiz set(s) deleted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void deleteQuizSets(java.util.Set<String> quizSetKeysToDelete) {
        String projectRoot = System.getProperty("user.dir");
        File quizFile = new File(projectRoot, "src\\main\\java\\oopwj\\data\\Quiz.txt");
        File questionFile = new File(projectRoot, "src\\main\\java\\oopwj\\data\\question.txt");
        File totalMarksFile = new File(projectRoot, "src\\main\\java\\oopwj\\data\\TotalQuizMark.txt");
        java.util.Map<String, String> quizTitlesByKey = new java.util.HashMap<>();


        if (quizFile.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(quizFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] fields = line.split(",", 3);
                    if (fields.length >= 2) {
                        String quizID = fields[0].trim();
                        String moduleID = fields[1].trim();
                        String key = quizID + "|" + moduleID;
                        if (quizSetKeysToDelete.contains(key)) {
                            String quizTitle = fields.length >= 3 ? fields[2].trim() : "";
                            quizTitlesByKey.put(key, quizTitle);
                        }
                    }
                }
            } catch (IOException ex) {
                logger.log(java.util.logging.Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this, "Error reading Quiz.txt: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            List<String> remainingQuizLines = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(quizFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] fields = line.split(",", 3);
                    if (fields.length >= 2) {
                        String quizID = fields[0].trim();
                        String moduleID = fields[1].trim();
                        String key = quizID + "|" + moduleID;
                        if (quizSetKeysToDelete.contains(key)) {
                            continue;
                        }
                    }
                    remainingQuizLines.add(line);
                }
            } catch (IOException ex) {
                logger.log(java.util.logging.Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this, "Error reading Quiz.txt: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try (java.io.FileWriter fw = new java.io.FileWriter(quizFile)) {
                for (String line : remainingQuizLines) {
                    fw.write(line + "\n");
                }
            } catch (IOException ex) {
                logger.log(java.util.logging.Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this, "Error updating Quiz.txt: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }


        if (questionFile.exists()) {
            List<String> remainingQuestionLines = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(questionFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] fields = line.split(",", -1);
                    if (fields.length >= 3) {
                        String quizID = fields[1].trim();
                        String moduleID = fields[2].trim();
                        String key = quizID + "|" + moduleID;
                        boolean matchesDeletedQuiz = quizSetKeysToDelete.contains(key);
                        if (!matchesDeletedQuiz) {
                            for (java.util.Map.Entry<String, String> entry : quizTitlesByKey.entrySet()) {
                                String deletedKey = entry.getKey();
                                String deletedTitleValue = entry.getValue();
                                String[] keyParts = deletedKey.split("\\|");
                                if (keyParts.length == 2) {
                                    String deletedQuizId = keyParts[0];
                                    String deletedModuleId = keyParts[1];
                                    if (deletedModuleId.equals(moduleID) &&
                                        (quizID.equals(deletedQuizId) || quizID.equals(deletedTitleValue))) {
                                        matchesDeletedQuiz = true;
                                        break;
                                    }
                                }
                            }
                        }
                        if (matchesDeletedQuiz) {
                            continue;
                        }
                    }
                    remainingQuestionLines.add(line);
                }
            } catch (IOException ex) {
                logger.log(java.util.logging.Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this, "Error reading question.txt: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Map<String, Integer> questionCounters = new HashMap<>();
            List<String> updatedLines = new ArrayList<>();
            for (String line : remainingQuestionLines) {
                String[] fields = line.split(",", -1);
                if (fields.length >= 3) {
                    String quizID = fields[1].trim();
                    String moduleID = fields[2].trim();
                    String key = quizID + "|" + moduleID;
                    int newCount = questionCounters.getOrDefault(key, 0) + 1;
                    questionCounters.put(key, newCount);
                    fields[0] = String.format("Q%03d", newCount);
                    updatedLines.add(String.join(",", fields));
                } else {
                    updatedLines.add(line);
                }
            }

            try (java.io.FileWriter fw = new java.io.FileWriter(questionFile)) {
                for (String line : updatedLines) {
                    fw.write(line + "\n");
                }
            } catch (IOException ex) {
                logger.log(java.util.logging.Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this, "Error updating question.txt: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

 
        deleteTotalQuizMarks(totalMarksFile, quizSetKeysToDelete, quizTitlesByKey);
    }

    private void deleteTotalQuizMarks(File totalMarksFile,
        java.util.Set<String> quizSetKeysToDelete,
        java.util.Map<String, String> quizTitlesByKey) {
        if (!totalMarksFile.exists()) {
            return;
        }

        List<String> remainingLines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(totalMarksFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",", -1);
                if (fields.length >= 3) {
                    String moduleID = fields[0].trim();
                    String quizID = fields[1].trim();
                    String key = quizID + "|" + moduleID;
                    boolean matchesDeletedQuiz = quizSetKeysToDelete.contains(key);

                    if (!matchesDeletedQuiz) {
                        for (java.util.Map.Entry<String, String> entry : quizTitlesByKey.entrySet()) {
                            String deletedKey = entry.getKey();
                            String deletedTitleValue = entry.getValue();
                            String[] keyParts = deletedKey.split("\\|");
                            if (keyParts.length == 2) {
                                String deletedQuizId = keyParts[0];
                                String deletedModuleId = keyParts[1];
                                if (deletedModuleId.equals(moduleID) &&
                                    (quizID.equals(deletedQuizId) || quizID.equals(deletedTitleValue))) {
                                    matchesDeletedQuiz = true;
                                    break;
                                }
                            }
                        }
                    }

                    if (matchesDeletedQuiz) {
                        continue;
                    }
                }

                remainingLines.add(line);
            }
        } catch (IOException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Error reading TotalQuizMark.txt: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (java.io.FileWriter fw = new java.io.FileWriter(totalMarksFile)) {
            for (String line : remainingLines) {
                fw.write(line + "\n");
            }
        } catch (IOException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Error updating TotalQuizMark.txt: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateQuizFile(java.util.Set<Integer> linesToDelete) {
        String projectRoot = System.getProperty("user.dir");
        File quizFile = new File(projectRoot, "src\\main\\java\\oopwj\\data\\question.txt");
        
        try {

            Map<String, Integer> questionCounters = new HashMap<>();
            List<String> updatedLines = new ArrayList<>();
            
            for (int i = 0; i < originalQuizLines.size(); i++) {
                if (!linesToDelete.contains(i)) {
                    String line = originalQuizLines.get(i);
                    String[] fields = line.split(",", -1); 
                    
                    if (fields.length >= 3) {

                        String quizID = fields[1].trim();
                        String moduleID = fields[2].trim();
                        String key = quizID + "|" + moduleID;
                        
                        int newCount = questionCounters.getOrDefault(key, 0) + 1;
                        questionCounters.put(key, newCount);
                        

                        String newQuestionID = String.format("Q%03d", newCount);
                        

                        fields[0] = newQuestionID;
                        

                        String updatedLine = String.join(",", fields);
                        updatedLines.add(updatedLine);
                    }
                }
            }

            try (java.io.FileWriter fw = new java.io.FileWriter(quizFile)) {
                for (String line : updatedLines) {
                    fw.write(line + "\n");
                }
            }
            

            originalQuizLines.clear();
            originalQuizLines.addAll(updatedLines);
            
        } catch (IOException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Error updating question.txt: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void performSearch() {
        jComboBox1.setSelectedIndex(-1);
        String searchTerm = jTextField1.getText().trim();

        if (searchTerm.isEmpty() || searchTerm.equalsIgnoreCase("Search")) {
            JOptionPane.showMessageDialog(this, "Please enter a search term", "Search", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String projectRoot = System.getProperty("user.dir");
        File questionFile = new File(projectRoot, "src\\main\\java\\oopwj\\data\\question.txt");

        String[] columnNames = {"Module Name", "Quiz Name", "Question ID", "Question", "Total Marks"};
        List<Object[]> matchingRows = new ArrayList<>();

        Map<String, String> quizMarks = loadQuizMarks(projectRoot);
        Map<String, String> quizNames = loadQuizNames(projectRoot);

        originalQuizLines.clear();
        displayedQuizLineIndices.clear();
        displayedQuizSetKeys.clear();

        if (questionFile.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(questionFile))) {
                String line;
                int lineIndex = 0;
                while ((line = br.readLine()) != null) {
                    originalQuizLines.add(line);
                    String[] fields = parseCSV(line);

                    boolean matches = false;
                    for (String field : fields) {
                        if (field.toLowerCase().contains(searchTerm.toLowerCase())) {
                            matches = true;
                            break;
                        }
                    }

                    if (matches) {
                        if (fields.length >= 3 && lecturerID != null) {
                            String moduleID = fields[2].trim();
                            if (!lecturerModules.containsKey(moduleID)) {
                                lineIndex++;
                                continue;
                            }
                        }

                        if (fields.length == 10 || fields.length == 5) {
                            String questionID = fields[0].trim();
                            String quizID = fields[1].trim();
                            String moduleID = fields[2].trim();
                            String questionText = fields[3].trim();
                            String key = moduleID + "|" + quizID + "|" + questionID;
                            String totalMarks = quizMarks.getOrDefault(key, "0");
                            String moduleName = getModuleName(moduleID);
                            String quizName = quizNames.getOrDefault(moduleID + "|" + quizID, "");

                            matchingRows.add(new Object[]{moduleName, quizName, questionID, questionText, totalMarks});
                            displayedQuizLineIndices.add(lineIndex);
                        }
                    }

                    lineIndex++;
                }
            } catch (IOException ex) {
                logger.log(java.util.logging.Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this, "Error reading question.txt: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        DefaultTableModel model = new DefaultTableModel(matchingRows.toArray(new Object[0][]), columnNames);
        jTable2.setModel(model);
        centerAlignTable(jTable2);
        setTableLabel("Search result of: \"" + searchTerm + "\"");
        currentDataType = "question";
    }
    
    private List<Object[]> searchInFile(String filePath, String searchTerm, boolean isQuiz) {
        List<Object[]> results = new ArrayList<>();
        String projectRoot = System.getProperty("user.dir");
        File file = new File(projectRoot, filePath);
        Map<String, String> quizMarks = loadQuizMarks(projectRoot);

        if (!file.exists()) {
            return results;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = parseCSV(line);


                boolean matches = false;
                for (String field : fields) {
                    if (field.toLowerCase().contains(searchTerm.toLowerCase())) {
                        matches = true;
                        break;
                    }
                }

                if (matches && isQuiz) {
                    if (fields.length >= 3 && lecturerID != null) {
                        String moduleID = fields[2].trim();
                        if (!lecturerModules.containsKey(moduleID)) {
                            continue;
                        }
                    }

                    if (fields.length == 10) { // Objective
                        String quizID = fields[1].trim();
                        String moduleID = fields[2].trim();
                        String questionID = fields[0].trim();
                        String key = moduleID + "|" + quizID + "|" + questionID;
                        String totalMarks = quizMarks.getOrDefault(key, "0");
                        results.add(new Object[]{moduleID, quizID, questionID, fields[9].trim(), totalMarks});
                    } else if (fields.length == 5) { // Subjective
                        String quizID = fields[1].trim();
                        String moduleID = fields[2].trim();
                        String questionID = fields[0].trim();
                        String key = moduleID + "|" + quizID + "|" + questionID;
                        String totalMarks = quizMarks.getOrDefault(key, "0");
                        results.add(new Object[]{moduleID, quizID, questionID, fields[4].trim(), totalMarks});
                    }
                }
            }
        } catch (IOException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Error reading file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        return results;
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        if (parentWindow != null) {
            parentWindow.setVisible(true);
        } else {
            new Lecturer_menu(lecturerID).setVisible(true);
        }
        this.dispose();
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        if (currentDataType == null) {
            JOptionPane.showMessageDialog(this, "Please select a filter before clicking Add", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if ("question".equals(currentDataType) || "quizSets".equals(currentDataType)) {
            int selectedRow = jTable2.getSelectedRow();
            String moduleId = null;
            String quizId = null;
            String quizName = "";

            if (selectedRow >= 0) {
                Object quizNameValue = jTable2.getValueAt(selectedRow, 1);
                if (quizNameValue != null) {
                    quizName = quizNameValue.toString().trim();
                }

                if ("question".equals(currentDataType)) {
                    String[] ids = getQuestionIdentifiersFromRow(selectedRow);
                    if (ids != null) {
                        moduleId = ids[0];
                        quizId = ids[1];
                    }
                } else if ("quizSets".equals(currentDataType)) {
                    if (selectedRow < displayedQuizSetKeys.size()) {
                        String key = displayedQuizSetKeys.get(selectedRow);
                        String[] parts = key.split("\\|");
                        if (parts.length >= 2) {
                            quizId = parts[0];
                            moduleId = parts[1];
                        }
                    }
                }
            }

            Quiz quizForm = new Quiz(lecturerID, this);
            if (moduleId != null && !moduleId.isEmpty() && quizId != null && !quizId.isEmpty()) {
                quizForm.preselectQuizSet(moduleId, quizId, quizName);
            }
            quizForm.setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Please load questions or quiz sets before adding.", "Add", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {
        int selectedRow = jTable2.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row to edit", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (currentDataType == null) {
            JOptionPane.showMessageDialog(this, "Please click a button above (Quiz) to view data", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!"question".equals(currentDataType)) {
            JOptionPane.showMessageDialog(this, "Please load questions before editing.", "Edit", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        editQuizRow(selectedRow);
    }
    
    private void editQuizRow(int selectedRow) {
        if (!"question".equals(currentDataType)) {
            JOptionPane.showMessageDialog(this, "Please load questions before editing.", "Edit", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String[] ids = getQuestionIdentifiersFromRow(selectedRow);
        if (ids == null) {
            JOptionPane.showMessageDialog(this, "Unable to resolve the selected question.", "Edit", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String moduleId = ids[0];
        String quizId = ids[1];
        String questionId = ids[2];

        QuizQuestionData questionData = loadQuestionData(moduleId, quizId, questionId);
        if (questionData == null) {
            JOptionPane.showMessageDialog(this, "Unable to find the selected question in question.txt.", "Edit", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String projectRoot = System.getProperty("user.dir");
        Map<String, String> quizMarks = loadQuizMarks(projectRoot);
        String marksKey = moduleId + "|" + quizId + "|" + questionId;
        String marks = quizMarks.getOrDefault(marksKey, "");

        Quiz quizForm = new Quiz(
            lecturerID,
            this,
            moduleId,
            quizId,
            questionId,
            questionData.questionType,
            questionData.question,
            questionData.answerA,
            questionData.answerB,
            questionData.answerC,
            questionData.answerD,
            questionData.correctAnswer,
            marks
        );
        quizForm.setVisible(true);
        this.dispose();
    }

    private String[] getQuestionIdentifiersFromRow(int selectedRow) {
        if (selectedRow < 0 || selectedRow >= displayedQuizLineIndices.size()) {
            return null;
        }
        int lineIndex = displayedQuizLineIndices.get(selectedRow);
        if (lineIndex < 0 || lineIndex >= originalQuizLines.size()) {
            return null;
        }
        String[] fields = parseCSV(originalQuizLines.get(lineIndex));
        if (fields.length >= 3) {
            String questionId = fields[0].trim();
            String quizId = fields[1].trim();
            String moduleId = fields[2].trim();
            return new String[]{moduleId, quizId, questionId};
        }
        return null;
    }

    private QuizQuestionData loadQuestionData(String moduleId, String quizId, String questionId) {
        String projectRoot = System.getProperty("user.dir");
        File quizFile = new File(projectRoot, "src\\main\\java\\oopwj\\data\\question.txt");

        if (!quizFile.exists()) {
            return null;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(quizFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = parseCSV(line);
                if (fields.length >= 5) {
                    String qId = fields[0].trim();
                    String qQuizId = fields[1].trim();
                    String qModuleId = fields[2].trim();

                    if (qId.equals(questionId) && qQuizId.equals(quizId) && qModuleId.equals(moduleId)) {
                        if (fields.length == 10) {
                            return new QuizQuestionData(
                                fields[9].trim(),
                                fields[3].trim(),
                                fields[4].trim(),
                                fields[5].trim(),
                                fields[6].trim(),
                                fields[7].trim(),
                                fields[8].trim()
                            );
                        } else if (fields.length == 5) {
                            return new QuizQuestionData(
                                fields[4].trim(),
                                fields[3].trim(),
                                "", "", "", "", ""
                            );
                        }
                    }
                }
            }
        } catch (IOException ex) {
            logger.log(java.util.logging.Level.SEVERE, "Error reading question.txt", ex);
        }

        return null;
    }

    private static class QuizQuestionData {
        private final String questionType;
        private final String question;
        private final String answerA;
        private final String answerB;
        private final String answerC;
        private final String answerD;
        private final String correctAnswer;

        private QuizQuestionData(String questionType, String question, String answerA, String answerB, String answerC, String answerD, String correctAnswer) {
            this.questionType = questionType;
            this.question = question;
            this.answerA = answerA;
            this.answerB = answerB;
            this.answerC = answerC;
            this.answerD = answerD;
            this.correctAnswer = correctAnswer;
        }
    }
    
    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {
        showFeedbackDialog();
    }
    
    private void showFeedbackDialog() {
        if (tryOpenFeedbackFromSelection()) {
            return;
        }
        javax.swing.JDialog dialog = new javax.swing.JDialog(this, "Provide Feedback", true);
        dialog.setDefaultCloseOperation(javax.swing.JDialog.DISPOSE_ON_CLOSE);
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(this);
        
        javax.swing.JPanel panel = new javax.swing.JPanel();
        panel.setLayout(new java.awt.GridLayout(4, 2, 10, 10));
        panel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        javax.swing.JLabel moduleLabel = new javax.swing.JLabel("Select Module:");
        javax.swing.JComboBox<String> moduleComboBox = new javax.swing.JComboBox<>();
        java.util.Map<String, String> moduleDisplayToId = new java.util.HashMap<>();
        java.util.List<String> sortedModuleIDs = new java.util.ArrayList<>(lecturerModules.keySet());
        java.util.Collections.sort(sortedModuleIDs, String.CASE_INSENSITIVE_ORDER);
        for (String moduleID : sortedModuleIDs) {
            String moduleName = lecturerModules.getOrDefault(moduleID, moduleID);
            String display = moduleName != null && !moduleName.trim().isEmpty() ? moduleName.trim() : moduleID;
            moduleDisplayToId.put(display, moduleID);
            moduleComboBox.addItem(display);
        }
        moduleComboBox.setSelectedIndex(-1); 
        

        javax.swing.JLabel quizLabel = new javax.swing.JLabel("Select Quiz:");
        javax.swing.JComboBox<String> quizComboBox = new javax.swing.JComboBox<>();
        java.util.Map<String, String> quizDisplayToId = new java.util.HashMap<>();
        quizComboBox.setSelectedIndex(-1); 
        
        moduleComboBox.addActionListener(e -> {
            quizComboBox.removeAllItems();
            quizDisplayToId.clear();
            String selectedModuleDisplay = (String) moduleComboBox.getSelectedItem();
            String selectedModuleId = moduleDisplayToId.get(selectedModuleDisplay);
            if (selectedModuleId == null || selectedModuleId.trim().isEmpty()) {
                return;
            }
            loadQuizzesForFeedback(selectedModuleId, quizComboBox, quizDisplayToId);
        });
        

        javax.swing.JButton submitButton = new javax.swing.JButton("Proceed");
        javax.swing.JButton cancelButton = new javax.swing.JButton("Cancel");
        
        submitButton.addActionListener(e -> {
            String selectedModuleDisplay = (String) moduleComboBox.getSelectedItem();
            String selectedQuizDisplay = (String) quizComboBox.getSelectedItem();
            String selectedModuleId = moduleDisplayToId.get(selectedModuleDisplay);
            String selectedQuizId = quizDisplayToId.get(selectedQuizDisplay);
            
            if (selectedModuleId == null || selectedModuleId.trim().isEmpty() ||
                selectedQuizId == null || selectedQuizId.trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog,
                    "Please select both module and quiz.",
                    "Selection Required",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            

            Feedback feedbackWindow = new Feedback(selectedModuleId, selectedQuizId, lecturerID, true, this);
            feedbackWindow.setVisible(true);
            dialog.dispose();

            Assessments.this.setVisible(false);
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        

        panel.add(moduleLabel);
        panel.add(moduleComboBox);
        panel.add(quizLabel);
        panel.add(quizComboBox);
        panel.add(new javax.swing.JLabel());
        panel.add(new javax.swing.JLabel()); 
        panel.add(submitButton);
        panel.add(cancelButton);
        
        dialog.add(panel);
        
        dialog.setVisible(true);
    }

    private boolean tryOpenFeedbackFromSelection() {
        int selectedRow = jTable2.getSelectedRow();
        if (selectedRow < 0) {
            return false;
        }

        String moduleId = null;
        String quizId = null;

        if ("question".equals(currentDataType)) {
            String[] ids = getQuestionIdentifiersFromRow(selectedRow);
            if (ids != null && ids.length >= 2) {
                moduleId = ids[0];
                quizId = ids[1];
            }
        } else if ("quizSets".equals(currentDataType)) {
            String[] ids = getQuizSetIdentifiersFromRow(selectedRow);
            if (ids != null && ids.length >= 2) {
                moduleId = ids[0];
                quizId = ids[1];
            }
        }

        if (moduleId == null || moduleId.isEmpty() || quizId == null || quizId.isEmpty()) {
            return false;
        }

        Feedback feedbackWindow = new Feedback(moduleId, quizId, lecturerID, true, this);
        feedbackWindow.setVisible(true);
        Assessments.this.setVisible(false);
        return true;
    }

    private String[] getQuizSetIdentifiersFromRow(int selectedRow) {
        if (selectedRow < 0 || selectedRow >= displayedQuizSetKeys.size()) {
            return null;
        }

        String key = displayedQuizSetKeys.get(selectedRow);
        if (key == null || key.trim().isEmpty()) {
            return null;
        }

        String[] parts = key.split("\\|");
        if (parts.length < 2) {
            return null;
        }

        String quizId = parts[0].trim();
        String moduleId = parts[1].trim();
        return new String[]{moduleId, quizId};
    }
    
    private void loadQuizzesForFeedback(String moduleID,
        javax.swing.JComboBox<String> quizComboBox,
        java.util.Map<String, String> quizDisplayToId) {
        String projectRoot = System.getProperty("user.dir");
        File quizFile = new File(projectRoot, "src\\main\\java\\oopwj\\data\\Quiz.txt");
        
        quizComboBox.removeAllItems();
        quizDisplayToId.clear();
        
        if (quizFile.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(quizFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] fields = line.split(",", 3); 
                    if (fields.length >= 3) {
                        String quizModuleID = fields[1].trim();
                        if (quizModuleID.equals(moduleID)) {
                            String quizID = fields[0].trim();
                            String quizTitle = fields[2].trim();
                            String display = quizTitle.isEmpty() ? quizID : quizTitle;
                            quizDisplayToId.put(display, quizID);
                            quizComboBox.addItem(display);
                        }
                    }
                }
            } catch (IOException ex) {
                logger.log(java.util.logging.Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this,
                    "Error reading Quiz.txt: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    

    public void refreshTableData() {
        if ("question".equals(currentDataType)) {
            loadQuizData();
        } else if ("quizSets".equals(currentDataType)) {
            loadQuizSetsData();
        }
    }
    
    private void loadLecturerModules() {
        lecturerModules.clear();
        allModuleNames.clear();
        
        String projectRoot = System.getProperty("user.dir");
        File modulesFile = new File(projectRoot, "src\\main\\java\\oopwj\\data\\modules.txt");
        
        if (!modulesFile.exists()) {
            logger.log(java.util.logging.Level.WARNING, "modules.txt not found");
            return;
        }
        
        try (BufferedReader br = new BufferedReader(new FileReader(modulesFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String trimmed = line.trim();
                if (!trimmed.isEmpty()) {
                    String[] parts = trimmed.split(",");
                    if (parts.length >= 4) {
                        String moduleId = parts[0].trim();
                        String moduleName = parts[1].trim();
                        String assignedLecturerId = parts[3].trim();

                        if (!moduleId.isEmpty()) {
                            allModuleNames.put(moduleId, moduleName);
                        }
                        

                        if (lecturerID != null && assignedLecturerId.equals(lecturerID)) {
                            lecturerModules.put(moduleId, moduleName);
                        }
                    }
                }
            }
        } catch (IOException ex) {
            logger.log(java.util.logging.Level.SEVERE, "Error loading modules", ex);
        }

        if (lecturerID != null) {
            if (lecturerModules.isEmpty()) {
                logger.log(java.util.logging.Level.INFO, "No modules assigned to lecturer: " + lecturerID);
            } else {
                logger.log(java.util.logging.Level.INFO, "Lecturer " + lecturerID + " has " + lecturerModules.size() + " modules");
            }
        }
    }

    private String getModuleName(String moduleId) {
        if (moduleId == null) {
            return "";
        }
        String name = allModuleNames.get(moduleId);
        return name != null && !name.trim().isEmpty() ? name : moduleId;
    }
    
    private void displayAssignedModules() {
        if (lecturerID == null) {
            return;
        }
        
        if (lecturerModules.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "You are not assigned to any modules yet.", 
                "Assigned Modules", 
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            StringBuilder message = new StringBuilder("Your Assigned Modules:\n\n");
            for (Map.Entry<String, String> entry : lecturerModules.entrySet()) {
                message.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }
            JOptionPane.showMessageDialog(this, 
                message.toString(), 
                "Assigned Modules", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void loadQuizData() {
        String projectRoot = System.getProperty("user.dir");
        File quizFile = new File(projectRoot, "src\\main\\java\\oopwj\\data\\question.txt");

        Map<String, String> quizMarks = loadQuizMarks(projectRoot);
        Map<String, String> quizNames = loadQuizNames(projectRoot);

        String[] columnNames = {"Module Name", "Quiz Name", "Question ID", "Question", "Total Marks"};
        List<Object[]> rows = new ArrayList<>();
        originalQuizLines.clear(); 
        displayedQuizLineIndices.clear();
        displayedQuizSetKeys.clear();

        if (quizFile.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(quizFile))) {
                String line;
                int lineIndex = 0;
                while ((line = br.readLine()) != null) {
                    String[] fields = parseCSV(line);
                    originalQuizLines.add(line);

                    if (fields.length >= 3 && lecturerID != null) {
                        String moduleID = fields[2].trim();
                        if (!lecturerModules.containsKey(moduleID)) {
                            lineIndex++;
                            continue;
                        }
                    }
                    
                    if (fields.length == 10 || fields.length == 5) {
                        String quizID = fields[1].trim();
                        String moduleID = fields[2].trim();
                        String questionID = fields[0].trim();
                        String questionText = fields[3].trim();
                        String key = moduleID + "|" + quizID + "|" + questionID;
                        String totalMarks = quizMarks.getOrDefault(key, "0");
                        String moduleName = getModuleName(moduleID);
                        String quizName = quizNames.getOrDefault(moduleID + "|" + quizID, "");
                        rows.add(new Object[]{moduleName, quizName, questionID, questionText, totalMarks});
                        displayedQuizLineIndices.add(lineIndex);
                    }
                    lineIndex++;
                }
            } catch (IOException ex) {
                logger.log(java.util.logging.Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this, "Error reading question.txt: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        DefaultTableModel model = new DefaultTableModel(rows.toArray(new Object[0][]), columnNames);
        jTable2.setModel(model);
        centerAlignTable(jTable2);
        setTableLabel("All Questions");
    }

    private Map<String, String> loadQuizNames(String projectRoot) {
        Map<String, String> quizNames = new HashMap<>();
        File quizFile = new File(projectRoot, "src\\main\\java\\oopwj\\data\\Quiz.txt");

        if (quizFile.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(quizFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] fields = line.split(",", 3); // QuizID, ModuleID, QuizName
                    if (fields.length >= 3) {
                        String quizID = fields[0].trim();
                        String moduleID = fields[1].trim();
                        String quizName = fields[2].trim();
                        if (!moduleID.isEmpty() && !quizID.isEmpty()) {
                            quizNames.put(moduleID + "|" + quizID, quizName);
                        }
                    }
                }
            } catch (IOException ex) {
                logger.log(java.util.logging.Level.WARNING, "Error reading Quiz.txt", ex);
            }
        }

        return quizNames;
    }
    
    private Map<String, String> loadQuizMarks(String projectRoot) {
        Map<String, String> quizMarks = new HashMap<>();
        File marksFile = new File(projectRoot, "src\\main\\java\\oopwj\\data\\TotalQuizMark.txt");
        
        if (marksFile.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(marksFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] fields = parseCSV(line);
                    if (fields.length >= 4) {
                        String moduleID = fields[0].trim();
                        String quizID = fields[1].trim();
                        String questionID = fields[2].trim();
                        String totalMarks = fields[3].trim();
                        String key = moduleID + "|" + quizID + "|" + questionID;
                        quizMarks.put(key, totalMarks);
                    }
                }
            } catch (IOException ex) {
                logger.log(java.util.logging.Level.WARNING, "Error reading TotalQuizMark.txt", ex);
            }
        }
        
        return quizMarks;
    }

    private Map<String, String> loadQuizFeedback(String projectRoot) {
        Map<String, String> quizFeedback = new HashMap<>();
        File feedbackFile = new File(projectRoot, "src\\main\\java\\oopwj\\data\\QuizFeedback.txt");

        if (feedbackFile.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(feedbackFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] fields = parseCSV(line);
                    if (fields.length >= 4) {
                        String moduleID = fields[0].trim();
                        String quizID = fields[1].trim();
                        String feedback = fields[3].trim();
                        if (!moduleID.isEmpty() && !quizID.isEmpty()) {
                            quizFeedback.put(moduleID + "|" + quizID, feedback);
                        }
                    }
                }
            } catch (IOException ex) {
                logger.log(java.util.logging.Level.WARNING, "Error reading QuizFeedback.txt", ex);
            }
        }

        return quizFeedback;
    }
    
    private String[] parseCSV(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;
        
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                fields.add(current.toString().trim().replaceAll("^\"|\"$", ""));
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }
        
        fields.add(current.toString().trim().replaceAll("^\"|\"$", ""));
        return fields.toArray(new String[0]);
    }

    private String[] normalizeQuizFields(String[] fields) {
        if (fields.length == 8) {
            return Arrays.copyOfRange(fields, 1, 8);
        }
        return fields;
    }
    
    private void loadFilterOptionsToComboBox() {
        jComboBox1.removeAllItems();
        jComboBox1.addItem("Quiz sets");
        jComboBox1.addItem("Question");
        jComboBox1.setSelectedIndex(-1);
    }
    
    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {
        String selectedFilter = (String) jComboBox1.getSelectedItem();
        if (selectedFilter == null || selectedFilter.isEmpty()) {
            return;
        }

        if ("Quiz sets".equalsIgnoreCase(selectedFilter)) {
            loadQuizSetsData();
        } else if ("Question".equalsIgnoreCase(selectedFilter)) {
            loadQuizData();
            currentDataType = "question";
        }
    }

    private void loadQuizSetsData() {
        String projectRoot = System.getProperty("user.dir");
        File quizFile = new File(projectRoot, "src\\main\\java\\oopwj\\data\\Quiz.txt");
        File questionFile = new File(projectRoot, "src\\main\\java\\oopwj\\data\\question.txt");

        Map<String, Integer> questionCounts = new HashMap<>();

        if (questionFile.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(questionFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] fields = parseCSV(line);
                    if (fields.length >= 3) {
                        String quizID = fields[1].trim();
                        String moduleID = fields[2].trim();
                        String key = quizID + "|" + moduleID;
                        questionCounts.put(key, questionCounts.getOrDefault(key, 0) + 1);
                    }
                }
            } catch (IOException ex) {
                logger.log(java.util.logging.Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this, "Error reading question.txt: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        String[] columnNames = {"Module Name", "Quiz Name", "Total Questions", "Feedback"};
        List<Object[]> rows = new ArrayList<>();

        displayedQuizSetKeys.clear();

        Map<String, String> quizFeedback = loadQuizFeedback(projectRoot);

        if (quizFile.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(quizFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] fields = line.split(",", 3); // QuizID, ModuleID, QuizName
                    if (fields.length >= 3) {
                        String quizID = fields[0].trim();
                        String moduleID = fields[1].trim();
                        String quizName = fields[2].trim();

                        if (lecturerID != null && !lecturerModules.containsKey(moduleID)) {
                            continue;
                        }

                        String moduleName = getModuleName(moduleID);
                        String key = quizID + "|" + moduleID;
                        int totalQuestions = questionCounts.getOrDefault(key, 0);

                        String feedback = quizFeedback.getOrDefault(moduleID + "|" + quizID, "Pending");
                        if (feedback.trim().isEmpty()) {
                            feedback = "Pending";
                        }

                        rows.add(new Object[]{moduleName, quizName, totalQuestions, feedback});
                        displayedQuizSetKeys.add(quizID + "|" + moduleID);
                    }
                }
            } catch (IOException ex) {
                logger.log(java.util.logging.Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this, "Error reading Quiz.txt: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        DefaultTableModel model = new DefaultTableModel(rows.toArray(new Object[0][]), columnNames);
        jTable2.setModel(model);
        centerAlignTable(jTable2);
        currentDataType = "quizSets";
        setTableLabel("Quiz sets");

        originalQuizLines.clear();
        displayedQuizLineIndices.clear();
    }
    
    private void loadQuizzesByModuleID(String moduleID) {
        String projectRoot = System.getProperty("user.dir");
        File quizFile = new File(projectRoot, "src\\main\\java\\oopwj\\data\\Quiz.txt");
        File questionFile = new File(projectRoot, "src\\main\\java\\oopwj\\data\\question.txt");

        displayedQuizSetKeys.clear();
        
        Map<String, Integer> questionCounts = new HashMap<>();
        
        if (questionFile.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(questionFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] fields = parseCSV(line);
                    if (fields.length >= 3) {
                        String quizID = fields[1].trim();
                        String modID = fields[2].trim();
                        String key = quizID + "|" + modID;
                        questionCounts.put(key, questionCounts.getOrDefault(key, 0) + 1);
                    }
                }
            } catch (IOException ex) {
                logger.log(java.util.logging.Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this, "Error reading question.txt: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        
        String[] columnNames = {"ModuleID", "QuizID", "Quiz Title", "Total Questions"};
        List<Object[]> rows = new ArrayList<>();
        
        if (quizFile.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(quizFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] fields = line.split(",", 3); 
                    if (fields.length >= 3) {
                        String quizModuleID = fields[1].trim();
                        if (quizModuleID.equals(moduleID)) {
                            String quizID = fields[0].trim();
                            String quizTitle = fields[2].trim();
                            String key = quizID + "|" + quizModuleID;
                            int totalQuestions = questionCounts.getOrDefault(key, 0);
                            
                            rows.add(new Object[]{quizModuleID, quizID, quizTitle, totalQuestions});
                        }
                    }
                }
            } catch (IOException ex) {
                logger.log(java.util.logging.Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this, "Error reading Quiz.txt: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        
        DefaultTableModel model = new DefaultTableModel(rows.toArray(new Object[0][]), columnNames);
        jTable2.setModel(model);
        centerAlignTable(jTable2);
        currentDataType = "quizByModule";
        String moduleName = lecturerModules.getOrDefault(moduleID, "");
        if (moduleName.isEmpty()) {
            setTableLabel("Module: " + moduleID);
        } else {
            setTableLabel("Module: " + moduleID + " - " + moduleName);
        }
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
        java.awt.EventQueue.invokeLater(() -> new Assessments().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
