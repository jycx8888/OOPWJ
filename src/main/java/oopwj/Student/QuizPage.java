package oopwj.Student;

import oopwj.Model.User;
import oopwj.Model.StudentService;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuizPage extends JFrame {

    private User currentUser;
    private String moduleID;
    private String quizID; 
    private StudentService service;

    private List<String[]> allQuestions; 
    private Map<String, String> userAnswers; 
    private int currentIndex = 0; 

    private JLabel sectionHeaderLabel;
    private JLabel progressLabel;
    private JPanel contentArea;
    private JButton prevBtn;
    private JButton nextBtn;
    private JButton finishBtn;
    private JComponent currentInputComponent;
    private String currentQuestionType;

    public QuizPage(User user, String moduleID, String moduleName, String quizID, String quizTitle) {
        this.currentUser = user;
        this.moduleID = moduleID;
        this.quizID = quizID;
        this.service = new StudentService();
        this.userAnswers = new HashMap<>();

        setTitle("Quiz: " + quizTitle);
        setSize(1000, 750); 
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        this.allQuestions = service.getQuizDetails(quizID);
        
        if (allQuestions.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Error: No questions found for " + quizID);
            dispose();
            return;
        }

        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(new Color(245, 247, 250));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(255, 255, 255));
        topPanel.setBorder(new EmptyBorder(15, 30, 15, 30));
        
        JLabel courseLabel = new JLabel(moduleID + ": " + quizTitle);
        courseLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        courseLabel.setForeground(new Color(50, 50, 50));
        
        sectionHeaderLabel = new JLabel("Loading...");
        sectionHeaderLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        sectionHeaderLabel.setForeground(new Color(0, 120, 215)); 
        
        JPanel titleContainer = new JPanel(new GridLayout(2, 1, 0, 5));
        titleContainer.setOpaque(false);
        titleContainer.add(courseLabel);
        titleContainer.add(sectionHeaderLabel);
        
        progressLabel = new JLabel("1 / " + allQuestions.size());
        progressLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        progressLabel.setForeground(new Color(200, 200, 200));

        topPanel.add(titleContainer, BorderLayout.CENTER);
        topPanel.add(progressLabel, BorderLayout.EAST);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        contentArea = new JPanel();
        contentArea.setLayout(new BoxLayout(contentArea, BoxLayout.Y_AXIS));
        contentArea.setOpaque(false);
        contentArea.setBorder(new EmptyBorder(30, 50, 30, 50));
        mainPanel.add(contentArea, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 15));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(new LineBorder(new Color(230, 230, 230), 1));
        
        prevBtn = createNavButton("Previous", false);
        nextBtn = createNavButton("Next", true);
        finishBtn = createNavButton("Finish", true);
        finishBtn.setBackground(new Color(40, 167, 69));
        finishBtn.setVisible(false);
        
        bottomPanel.add(prevBtn);
        bottomPanel.add(nextBtn);
        bottomPanel.add(finishBtn);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        nextBtn.addActionListener(e -> navigate(1));
        prevBtn.addActionListener(e -> navigate(-1));
        finishBtn.addActionListener(e -> submitTest());

        renderQuestion();
        this.add(mainPanel);
        setVisible(true);
    }

    private JButton createNavButton(String text, boolean filled) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(120, 40));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        if (filled) {
            btn.setBackground(new Color(0, 120, 215));
            btn.setForeground(Color.WHITE);
        } else {
            btn.setBackground(Color.WHITE);
            btn.setForeground(new Color(80, 80, 80));
            btn.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        }
        return btn;
    }

    private void navigate(int direction) {
        saveCurrentAnswer();
        currentIndex += direction;
        renderQuestion();
    }

    private void saveCurrentAnswer() {
        if (currentInputComponent == null) return;
        String[] qData = allQuestions.get(currentIndex);
        String qID = qData[0];
        String answer = "";
        
        if ("Objective".equalsIgnoreCase(currentQuestionType)) {
            Container container = (Container) currentInputComponent;
            answer = findSelectedToggle(container);
        } else {
            JScrollPane scroll = (JScrollPane) currentInputComponent;
            JTextArea area = (JTextArea) scroll.getViewport().getView();
            answer = area.getText().trim();
        }
        
        if (!answer.isEmpty()) userAnswers.put(qID, answer);
        else userAnswers.remove(qID);
    }

    private String findSelectedToggle(Container container) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JToggleButton) {
                JToggleButton btn = (JToggleButton) comp;
                if (btn.isSelected()) return btn.getActionCommand();
            }
        }
        return "";
    }

    private void renderQuestion() {
        contentArea.removeAll();
        String[] qData = allQuestions.get(currentIndex);
        
        String qID = qData[0];
        String qText = qData[1];
        String qType = qData[2];
        this.currentQuestionType = qType;
        
        if (qType.equalsIgnoreCase("Objective")) sectionHeaderLabel.setText("Section A: Objective");
        else sectionHeaderLabel.setText("Section B: Subjective");
        
        progressLabel.setText((currentIndex + 1) + " / " + allQuestions.size());
        
        JPanel questionCard = new JPanel(new BorderLayout());
        questionCard.setBackground(new Color(255, 235, 156)); 
        questionCard.setBackground(Color.WHITE); 
        questionCard.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(220, 220, 220), 1, true),
            new EmptyBorder(30, 30, 30, 30)
        ));
        questionCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        questionCard.setPreferredSize(new Dimension(800, 180));
        
        JTextArea qLabel = new JTextArea(qText);
        qLabel.setWrapStyleWord(true); 
        qLabel.setLineWrap(true); 
        qLabel.setEditable(false);
        qLabel.setOpaque(false);
        qLabel.setFont(new Font("Segoe UI", Font.BOLD, 22)); 
        qLabel.setForeground(new Color(50, 50, 50));
        qLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        questionCard.add(qLabel, BorderLayout.CENTER);
        contentArea.add(questionCard);
        
        contentArea.add(Box.createRigidArea(new Dimension(0, 30)));
        
        String savedAnswer = userAnswers.getOrDefault(qID, "");

        if (qType.equalsIgnoreCase("Objective")) {
            String optA = qData[3], optB = qData[4], optC = qData[5], optD = qData[6];
            
            JPanel gridPanel = new JPanel(new GridLayout(2, 2, 20, 20));
            gridPanel.setOpaque(false);
            gridPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));
            
            ButtonGroup bg = new ButtonGroup();
            
            gridPanel.add(createOptionCard("A", optA, savedAnswer, bg));
            gridPanel.add(createOptionCard("B", optB, savedAnswer, bg));
            gridPanel.add(createOptionCard("C", optC, savedAnswer, bg));
            gridPanel.add(createOptionCard("D", optD, savedAnswer, bg));
            
            contentArea.add(gridPanel);
            currentInputComponent = gridPanel;

        } else {
            JTextArea t = new JTextArea(12, 20); 
            t.setLineWrap(true); t.setWrapStyleWord(true);
            t.setText(savedAnswer);
            t.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            t.setBorder(new EmptyBorder(15, 15, 15, 15));
            
            JScrollPane s = new JScrollPane(t); 
            s.setBorder(new LineBorder(new Color(200, 200, 200)));
            s.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            contentArea.add(s); 
            currentInputComponent = s;
        }
        
        prevBtn.setEnabled(currentIndex > 0);
        if (currentIndex == allQuestions.size() - 1) { nextBtn.setVisible(false); finishBtn.setVisible(true); }
        else { nextBtn.setVisible(true); finishBtn.setVisible(false); }
        
        contentArea.revalidate(); contentArea.repaint();
    }

    private JToggleButton createOptionCard(String letter, String text, String saved, ButtonGroup bg) {
        JToggleButton btn = new JToggleButton("<html><b style='font-size:14px; color:#555'>" + letter + ".</b>&nbsp;&nbsp;" + text + "</html>");
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        btn.setForeground(new Color(60, 60, 60));
        btn.setBackground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 2, true),
            new EmptyBorder(10, 20, 10, 20)
        ));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setActionCommand(letter);
        
        if (saved.equals(letter)) {
            btn.setSelected(true);
            btn.setBackground(new Color(230, 242, 255)); 
            btn.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(0, 120, 215), 2, true),
                new EmptyBorder(10, 20, 10, 20)
            ));
        }

        btn.addActionListener(e -> {
            userAnswers.put(allQuestions.get(currentIndex)[0], letter);
            updateButtonStyles((Container) btn.getParent());
        });

        bg.add(btn);
        return btn;
    }

    private void updateButtonStyles(Container parent) {
        for (Component c : parent.getComponents()) {
            if (c instanceof JToggleButton) {
                JToggleButton b = (JToggleButton) c;
                if (b.isSelected()) {
                    b.setBackground(new Color(230, 242, 255));
                    b.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(new Color(0, 120, 215), 2, true),
                        new EmptyBorder(10, 20, 10, 20)
                    ));
                } else {
                    b.setBackground(Color.WHITE);
                    b.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(new Color(200, 200, 200), 2, true),
                        new EmptyBorder(10, 20, 10, 20)
                    ));
                }
            }
        }
    }

    private void submitTest() {
        saveCurrentAnswer();
        
        if (userAnswers.size() < allQuestions.size()) {
            int c = JOptionPane.showConfirmDialog(this, "Not all questions answered. Submit anyway?", "Confirm", JOptionPane.YES_NO_OPTION);
            if(c != JOptionPane.YES_OPTION) return;
        }

        List<String[]> finalData = new ArrayList<>();
        for (String[] q : allQuestions) {
            String qID = q[0];
            String type = (q.length >= 3) ? q[2] : "";
            String answer = userAnswers.getOrDefault(qID, "NO_ANSWER");
            finalData.add(new String[]{qID, answer, type});
        }

        boolean success = service.submitQuizAnswers(currentUser.getUserID(), moduleID, quizID, finalData);

        if (success) {
            JOptionPane.showMessageDialog(this, "The test has been uploaded to the lecturer.");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Error uploading test.");
        }
    }
}