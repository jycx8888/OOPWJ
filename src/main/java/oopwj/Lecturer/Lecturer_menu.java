package oopwj.Lecturer;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JButton;


public class Lecturer_menu extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Lecturer_menu.class.getName());
    private String lecturerID;
    private static final String LECTURER_FILE = "src/main/java/oopwj/Data/lecturer.txt";

    public Lecturer_menu() {
        this(null);
    }
    
    public Lecturer_menu(String lecturerID) {
        this.lecturerID = lecturerID;
        initComponents();
        centerWindow();
        setupListeners();
        updateLNameLabel();
        addHoverEffect(editProfile,new Color(70,130,180),new Color(70,130,180));
        addHoverEffect(Assessmentsbutton,new Color(70,130,180),new Color(70,130,180));
        addHoverEffect(GradeAssessmentButton,new Color(70,130,180),new Color(70,130,180));
        addHoverEffect(jButton1,new Color(70,130,180),new Color(70,130,180));
        addHoverEffect(logOut,new Color(220,80,80),new Color(220,80,80));
    }

    private void centerWindow() {
        setLocationRelativeTo(null);
    }
    
    private void setupListeners() {
        Assessmentsbutton.addActionListener(this::AssessmentsbuttonActionPerformed);
    }
    
    private void updateLNameLabel() {
        String name = getLecturerNameById(lecturerID);
        if (name == null || name.isEmpty()) {
            LName.setText("Academic Leader");
        } else {
            LName.setText("Logged in as: " + name);
        }
    }

    public void refreshUserDisplay() {
        updateLNameLabel();
    }
    
    private String getLecturerNameById(String userID) {
        if (userID == null || userID.isEmpty()) {
            return null;
        }

        try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(LECTURER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] parts = line.split(",");
                if (parts.length < 2) {
                    continue;
                }
                if (parts[0].trim().equalsIgnoreCase(userID.trim())) {
                    return parts[1].trim();
                }
            }
        } catch (java.io.IOException e) {
            logger.warning("Failed to read academic leader data: " + e.getMessage());
        }
        return null;
    }
    
    public void clearSession() {
        this.lecturerID = null;
        logger.info("Academic Leader session cleared");
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

    
    @SuppressWarnings("unchecked")
    private void initComponents() {

        jMenuItem1 = new javax.swing.JMenuItem();
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
        editProfile = new javax.swing.JButton();
        Assessmentsbutton = new javax.swing.JButton();
        GradeAssessmentButton = new javax.swing.JButton();
        logOut = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        welcome = new javax.swing.JLabel();
        LName = new javax.swing.JLabel();
        welcome1 = new javax.swing.JLabel();

        jMenuItem1.setText("jMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(204, 255, 255));
        jPanel1.setOpaque(false);
        jPanel1.setPreferredSize(new java.awt.Dimension(450, 510));

        editProfile.setFont(new java.awt.Font("Segoe UI", 1, 14));
        editProfile.setText("Edit Profile");
        editProfile.setAlignmentX(0.5F);
        editProfile.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        editProfile.addActionListener(this::editProfileActionPerformed);

        Assessmentsbutton.setFont(new java.awt.Font("Segoe UI", 1, 14));
        Assessmentsbutton.setText("Assessments");
        Assessmentsbutton.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        GradeAssessmentButton.setFont(new java.awt.Font("Segoe UI", 1, 14));
        GradeAssessmentButton.setText("Grade Assessment");
        GradeAssessmentButton.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        GradeAssessmentButton.addActionListener(this::GradeAssessmentButtonActionPerformed);

        logOut.setFont(new java.awt.Font("Segoe UI", 1, 14));
        logOut.setText("Log Out");
        logOut.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        logOut.addActionListener(this::logOutActionPerformed);

        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 14));
        jButton1.setText("Timetable");
        jButton1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jButton1.addActionListener(this::jButton1ActionPerformed);

        welcome.setFont(new java.awt.Font("Segoe UI", 1, 24));
        welcome.setText("Welcome");

        LName.setFont(new java.awt.Font("Segoe UI", 1, 14));
        LName.setText("jLabel1");

        welcome1.setFont(new java.awt.Font("Segoe UI", 1, 24));
        welcome1.setText("Lecturer Dashboard");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(63, 63, 63)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(logOut, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(editProfile, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(Assessmentsbutton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(GradeAssessmentButton, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(welcome)
                    .addComponent(LName, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(welcome1))
                .addContainerGap(328, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(56, 56, 56)
                .addComponent(welcome1)
                .addGap(18, 18, 18)
                .addComponent(welcome)
                .addGap(18, 18, 18)
                .addComponent(LName)
                .addGap(18, 18, 18)
                .addComponent(editProfile)
                .addGap(18, 18, 18)
                .addComponent(Assessmentsbutton)
                .addGap(18, 18, 18)
                .addComponent(GradeAssessmentButton)
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addGap(18, 18, 18)
                .addComponent(logOut)
                .addContainerGap(64, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 612, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        new Lecturer_schedule(lecturerID, this).setVisible(true);
        this.setVisible(false);
    }

    private void editProfileActionPerformed(java.awt.event.ActionEvent evt) {
        new editProfileL(lecturerID,this).setVisible(true);
        this.setVisible(false);
    }

    private void GradeAssessmentButtonActionPerformed(java.awt.event.ActionEvent evt) {
        new Grade_Assessment(lecturerID, this).setVisible(true);
        this.setVisible(false);
    }                                                     

    private void CreateAssessmentsbuttonActionPerformed(java.awt.event.ActionEvent evt) {
        new Assessments().setVisible(true);
        this.dispose();
    }

    private void AssessmentsbuttonActionPerformed(java.awt.event.ActionEvent evt) {
        new Assessments(lecturerID, this).setVisible(true);
        this.setVisible(false);
    }

    private void logOutActionPerformed(java.awt.event.ActionEvent evt) {
        int confirm = javax.swing.JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to log out?",
            "Confirm Logout",
            javax.swing.JOptionPane.YES_NO_OPTION,
            javax.swing.JOptionPane.QUESTION_MESSAGE
        );
        
        if (confirm == javax.swing.JOptionPane.YES_OPTION) {
            clearSession();
            this.dispose();
            new oopwj.LoginFrame();
        }
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

        java.awt.EventQueue.invokeLater(() -> new Lecturer_menu().setVisible(true));
    }

    private javax.swing.JButton Assessmentsbutton;
    private javax.swing.JButton GradeAssessmentButton;
    private javax.swing.JLabel LName;
    private javax.swing.JButton editProfile;
    private javax.swing.JButton jButton1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton logOut;
    private javax.swing.JLabel welcome;
    private javax.swing.JLabel welcome1;
}
