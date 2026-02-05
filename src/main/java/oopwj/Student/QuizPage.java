package oopwj.Student;

import oopwj.Model.User;
import oopwj.Model.StudentService;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
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
    private JPanel questionContainer;
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
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        this.allQuestions = service.getQuizDetails(quizID);
        
        if (allQuestions.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Error: No questions found for " + quizID);
            dispose();
            return;
        }

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // --- TOP PANEL ---
        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        JLabel courseLabel = new JLabel("Quiz: " + quizTitle + " (" + moduleID + ")", SwingConstants.CENTER);
        courseLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        sectionHeaderLabel = new JLabel("Loading...", SwingConstants.LEFT);
        sectionHeaderLabel.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 14));
        sectionHeaderLabel.setForeground(new Color(0, 102, 204)); // Dark Blue
        
        topPanel.add(courseLabel);
        topPanel.add(sectionHeaderLabel);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // --- CENTER: Question Container ---
        questionContainer = new JPanel();
        questionContainer.setLayout(new BoxLayout(questionContainer, BoxLayout.Y_AXIS));
        mainPanel.add(questionContainer, BorderLayout.CENTER);

        // --- BOTTOM: Navigation ---
        JPanel bottomPanel = new JPanel(new BorderLayout());
        progressLabel = new JLabel("Question 1 / " + allQuestions.size());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        prevBtn = new JButton("<< Previous");
        nextBtn = new JButton("Next >>");
        finishBtn = new JButton("Finish Test");
        finishBtn.setVisible(false);
        finishBtn.setBackground(new Color(200, 255, 200)); // Light Green
        
        buttonPanel.add(prevBtn); buttonPanel.add(nextBtn); buttonPanel.add(finishBtn);
        bottomPanel.add(progressLabel, BorderLayout.WEST);
        bottomPanel.add(buttonPanel, BorderLayout.EAST);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        nextBtn.addActionListener(e -> navigate(1));
        prevBtn.addActionListener(e -> navigate(-1));
        finishBtn.addActionListener(e -> submitTest());

        renderQuestion();
        this.add(mainPanel);
        setVisible(true);
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
        
        if (currentQuestionType.equalsIgnoreCase("Objective")) {
            // Find selected RadioButton from the container
            Container container = (Container) currentInputComponent;
            answer = findSelectedRadio(container);
        } else {
            JScrollPane scroll = (JScrollPane) currentInputComponent;
            JTextArea area = (JTextArea) scroll.getViewport().getView();
            answer = area.getText().trim();
        }
        
        if (!answer.isEmpty()) userAnswers.put(qID, answer);
        else userAnswers.remove(qID);
    }

    // Helper to find selected radio in nested panels
    private String findSelectedRadio(Container container) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JRadioButton) {
                JRadioButton rb = (JRadioButton) comp;
                if (rb.isSelected()) return rb.getActionCommand();
            } else if (comp instanceof Container) {
                String result = findSelectedRadio((Container) comp);
                if (!result.isEmpty()) return result;
            }
        }
        return "";
    }

    private void renderQuestion() {
        questionContainer.removeAll();
        String[] qData = allQuestions.get(currentIndex);
        String qID = qData[0];
        String qText = qData[1];
        String qType = qData[2];
        this.currentQuestionType = qType;
        
        if (qType.equalsIgnoreCase("Objective")) sectionHeaderLabel.setText("Section A: Objective");
        else sectionHeaderLabel.setText("Section B: Subjective");
        
        progressLabel.setText("Question " + (currentIndex + 1) + " / " + allQuestions.size());
        
        // 1. Question Text Area (Limited Height & White Background)
        JTextArea qLabel = new JTextArea("Q" + (currentIndex + 1) + ". " + qText);
        qLabel.setWrapStyleWord(true); 
        qLabel.setLineWrap(true); 
        qLabel.setEditable(false);
        // [CHANGE] Make background WHITE
        qLabel.setBackground(Color.WHITE); 
        // [CHANGE] Use Bold font for better contrast on white
        qLabel.setFont(new Font("SansSerif", Font.BOLD, 16)); 
        qLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); 
        
        // Wrap in ScrollPane to limit height (Max 150px)
        JScrollPane qScroll = new JScrollPane(qLabel);
        // [CHANGE] Add a light gray border to define the white box
        qScroll.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        qScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        qScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150)); 
        qScroll.setPreferredSize(new Dimension(800, 100)); // Default height
        
        questionContainer.add(qScroll); 
        questionContainer.add(Box.createRigidArea(new Dimension(0, 20)));
        
        String savedAnswer = userAnswers.getOrDefault(qID, "");

        if (qType.equalsIgnoreCase("Objective")) {
            // --- LAYOUT: 2 Columns ---
            String optA = qData[3], optB = qData[4], optC = qData[5], optD = qData[6];
            
            // Main Options Panel
            JPanel optionsPanel = new JPanel(new GridLayout(1, 2, 20, 0)); // 1 Row, 2 Cols
            optionsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            optionsPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

            // Left Column (A, B)
            JPanel leftCol = new JPanel(new GridLayout(2, 1, 0, 15)); // Vertical spacing
            JRadioButton rbA = new JRadioButton("A. " + optA);
            JRadioButton rbB = new JRadioButton("B. " + optB);
            leftCol.add(rbA); leftCol.add(rbB);

            // Right Column (C, D)
            JPanel rightCol = new JPanel(new GridLayout(2, 1, 0, 15));
            JRadioButton rbC = new JRadioButton("C. " + optC);
            JRadioButton rbD = new JRadioButton("D. " + optD);
            rightCol.add(rbC); rightCol.add(rbD);

            optionsPanel.add(leftCol);
            optionsPanel.add(rightCol);

            // Logic
            rbA.setActionCommand("A"); rbB.setActionCommand("B"); 
            rbC.setActionCommand("C"); rbD.setActionCommand("D");
            if(savedAnswer.equals("A")) rbA.setSelected(true); 
            if(savedAnswer.equals("B")) rbB.setSelected(true);
            if(savedAnswer.equals("C")) rbC.setSelected(true); 
            if(savedAnswer.equals("D")) rbD.setSelected(true);
            
            ButtonGroup g = new ButtonGroup(); g.add(rbA); g.add(rbB); g.add(rbC); g.add(rbD);
            
            optionsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
            
            questionContainer.add(optionsPanel); 
            currentInputComponent = optionsPanel;

        } else {
            // --- LAYOUT: Larger Answer Box ---
            JTextArea t = new JTextArea(10, 20); // 10 rows
            t.setLineWrap(true); t.setWrapStyleWord(true);
            t.setText(savedAnswer);
            t.setFont(new Font("SansSerif", Font.PLAIN, 14));
            
            JScrollPane s = new JScrollPane(t); 
            s.setAlignmentX(Component.LEFT_ALIGNMENT);
            s.setBorder(BorderFactory.createTitledBorder("Type your answer here:"));
            
            questionContainer.add(s); 
            currentInputComponent = s;
        }
        
        prevBtn.setEnabled(currentIndex > 0);
        if (currentIndex == allQuestions.size() - 1) { nextBtn.setVisible(false); finishBtn.setVisible(true); }
        else { nextBtn.setVisible(true); finishBtn.setVisible(false); }
        
        questionContainer.revalidate(); questionContainer.repaint();
    }

    private void submitTest() {
        saveCurrentAnswer();
        if (userAnswers.size() < allQuestions.size()) {
            int c = JOptionPane.showConfirmDialog(this, "Not all answered. Submit?", "Confirm", JOptionPane.YES_NO_OPTION);
            if(c != JOptionPane.YES_OPTION) return;
        }

        List<String[]> finalData = new ArrayList<>();
        for (String[] q : allQuestions) {
            String qID = q[0];
            String type = "";
            if (q.length >= 3) type = q[2];
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