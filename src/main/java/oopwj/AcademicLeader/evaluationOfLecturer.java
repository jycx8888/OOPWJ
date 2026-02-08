/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package oopwj.AcademicLeader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import javax.swing.JButton;

/**
 *
 * @author Justin Yong
 */
public class evaluationOfLecturer extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(evaluationOfLecturer.class.getName());
    private String loggedInUserID;
    private academicLeader parentFrame;
    private final Map<String, String> lecturerDisplayToId = new HashMap<>();
    private final Map<String, String> studentIdToName = new HashMap<>();
    private static final String LECTURER_PLACEHOLDER = "Select Lecturer";

    public evaluationOfLecturer() {
        initComponents();
        configureWindowBehavior();
        initializeData();
    }

    public evaluationOfLecturer(String userID, academicLeader parentFrame) {
        this.loggedInUserID = userID;
        this.parentFrame = parentFrame;
        initComponents();
        setLocationRelativeTo(null);
        configureWindowBehavior();
        initializeData();
        logger.info("Lecturer Review opened for user: " + userID);
        addHoverEffect(viewStudentsFeedback, new Color(70,130,180), new Color(100,149,237));
        addHoverEffect(exit, new Color(220,80,80), new Color(255,120,120));
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

    private void initializeData() {
        loadStudentNamesFromFile();
        loadLecturersFromFile();
        setupTableModel();
        lecturer.addActionListener(evt -> loadFeedbackForSelectedLecturer());
    }

    private void configureWindowBehavior() {
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent evt) {
                returnToParent();
            }
        });
    }

    private void returnToParent() {
        if (parentFrame != null) {
            parentFrame.setVisible(true);
        }
    }

    private void setupTableModel() {
        DefaultTableModel model = new DefaultTableModel(new Object[][] {}, new String[] {
            "Student ID", "Student Name", "Feedback"
        }) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        jTable1.setModel(model);
    }

    private void loadLecturersFromFile() {
        lecturerDisplayToId.clear();
        lecturer.removeAllItems();
        lecturer.addItem(LECTURER_PLACEHOLDER);
        try (BufferedReader reader = new BufferedReader(new FileReader("src\\main\\java\\oopwj\\Data\\lecturer.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim();
                if (trimmed.isEmpty()) {
                    continue;
                }
                String[] parts = trimmed.split(",");
                if (parts.length >= 5) {
                    String lecturerId = parts[0].trim();
                    String lecturerName = parts[1].trim();
                    String lecturerACID = parts[4].trim();
                    if (loggedInUserID != null && !loggedInUserID.equals(lecturerACID)) {
                        continue;
                    }
                    String display = lecturerName + " - " + lecturerId;
                    lecturer.addItem(display);
                    lecturerDisplayToId.put(display, lecturerId);
                }
            }
        } catch (IOException e) {
            logger.log(java.util.logging.Level.WARNING, "Unable to load lecturers list", e);
            lecturer.addItem("Error loading lecturers");
        }
        lecturer.setSelectedIndex(0);
    }

    private void loadStudentNamesFromFile() {
        studentIdToName.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader("src\\main\\java\\oopwj\\Data\\student.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim();
                if (trimmed.isEmpty()) {
                    continue;
                }
                String[] parts = trimmed.split(",");
                if (parts.length >= 2) {
                    String studentId = parts[0].trim();
                    String studentName = parts[1].trim();
                    studentIdToName.put(studentId, studentName);
                }
            }
        } catch (IOException e) {
            logger.log(java.util.logging.Level.WARNING, "Unable to load student list", e);
        }
    }

    private void loadFeedbackForSelectedLecturer() {
        String display = (String) lecturer.getSelectedItem();
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);
        if (display == null || display.equals(LECTURER_PLACEHOLDER)) {
            return;
        }
        String lecturerId = lecturerDisplayToId.get(display);
        if (lecturerId == null) {
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader("src\\main\\java\\oopwj\\Data\\studentFeedback.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim();
                if (trimmed.isEmpty()) {
                    continue;
                }
                String[] parts = trimmed.split(",", 4);
                if (parts.length >= 4) {
                    String studentId = parts[0].trim();
                    String lecturerIdInFile = parts[2].trim();
                    String feedback = parts[3].trim();
                    if (!lecturerId.equals(lecturerIdInFile)) {
                        continue;
                    }
                    String studentName = studentIdToName.getOrDefault(studentId, "Unknown");
                    model.addRow(new Object[] { studentId, studentName, feedback });
                }
            }
        } catch (IOException e) {
            logger.log(java.util.logging.Level.WARNING, "Unable to load feedback", e);
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
        viewStudentsFeedback = new javax.swing.JButton();
        exit = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        lecturer = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setOpaque(false);

        viewStudentsFeedback.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        viewStudentsFeedback.setText("View");
        viewStudentsFeedback.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewStudentsFeedbackActionPerformed(evt);
            }
        });

        exit.setBackground(new java.awt.Color(220, 80, 80));
        exit.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        exit.setForeground(new java.awt.Color(255, 255, 255));
        exit.setText("X");
        exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("Evaluation of Lecturers");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setText("Feedback From Students");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Student ID", "Student Name", "Feedback"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        lecturer.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(79, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(lecturer, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(78, 78, 78))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(23, 23, 23))
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(98, 98, 98)
                        .addComponent(exit)
                        .addGap(47, 47, 47))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(viewStudentsFeedback)
                        .addGap(268, 268, 268))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(exit)
                        .addGap(80, 80, 80))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addGap(28, 28, 28)))
                .addComponent(lecturer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(viewStudentsFeedback)
                .addContainerGap(54, Short.MAX_VALUE))
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

    private void viewStudentsFeedbackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewStudentsFeedbackActionPerformed
        // TODO add your handling code here:
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a student row first.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String studentId = jTable1.getValueAt(selectedRow, 0).toString();
        String studentName = jTable1.getValueAt(selectedRow, 1).toString();
        String feedback = jTable1.getValueAt(selectedRow, 2).toString();

        JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(12, 12, 12, 12));
        panel.setLayout(new javax.swing.BoxLayout(panel, javax.swing.BoxLayout.Y_AXIS));
        panel.add(new JLabel("Student ID: " + studentId));
        panel.add(javax.swing.Box.createVerticalStrut(4));
        panel.add(new JLabel("Student Name: " + studentName));
        panel.add(javax.swing.Box.createVerticalStrut(6));
        panel.add(new JLabel("Feedback:"));
        panel.add(javax.swing.Box.createVerticalStrut(4));

        JTextArea feedbackArea = new JTextArea(feedback, 6, 28);
        feedbackArea.setEditable(false);
        feedbackArea.setLineWrap(true);
        feedbackArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(feedbackArea);
        panel.add(scrollPane);

        JDialog dialog = new JDialog(this, "Student Feedback", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setContentPane(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }//GEN-LAST:event_viewStudentsFeedbackActionPerformed

    private void exitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitActionPerformed
        // TODO add your handling code here:
        this.dispose();
        if (parentFrame != null) {
            parentFrame.setVisible(true);
        }
    }//GEN-LAST:event_exitActionPerformed

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

        java.awt.EventQueue.invokeLater(() -> new evaluationOfLecturer().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton exit;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JComboBox<String> lecturer;
    private javax.swing.JButton viewStudentsFeedback;
    // End of variables declaration//GEN-END:variables
}
