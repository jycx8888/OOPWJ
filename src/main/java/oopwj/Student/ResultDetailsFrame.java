package oopwj.Student;

import oopwj.Model.User;
import oopwj.Model.StudentService;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ResultDetailsFrame extends JFrame {

    public ResultDetailsFrame(User user, String moduleID, String moduleName, String quizID) {
        setTitle("Detailed Results: " + quizID);
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Header
        JLabel titleLabel = new JLabel("<html>Details for " + moduleID + "<br/>Quiz: " + quizID + "</html>", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Data
        StudentService service = new StudentService();
        List<String[]> results = service.getDetailedResults(user.getUserID(), moduleID, quizID);

        // Table
        String[] columnNames = {"Q ID", "Type", "Your Answer", "Score / Feedback"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        if (results.isEmpty()) {
            model.addRow(new String[]{"-", "-", "No detailed records found", "-"});
        } else {
            for (String[] row : results) {
                model.addRow(row);
            }
        }

        JTable table = new JTable(model);
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        
        table.getColumnModel().getColumn(0).setPreferredWidth(60);  // QID
        table.getColumnModel().getColumn(1).setPreferredWidth(80);  // Type
        table.getColumnModel().getColumn(2).setPreferredWidth(350); // Answer
        table.getColumnModel().getColumn(3).setPreferredWidth(100); // Score

        mainPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        // Close Button
        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> dispose());
        mainPanel.add(closeBtn, BorderLayout.SOUTH);

        this.add(mainPanel);
        setVisible(true);
    }
}