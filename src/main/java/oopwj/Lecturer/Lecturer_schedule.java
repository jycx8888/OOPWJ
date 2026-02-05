/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package oopwj.Lecturer;

/**
 *
 * @author User
 */
public class Lecturer_schedule extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Lecturer_schedule.class.getName());
    private static final java.nio.file.Path DATA_DIR = java.nio.file.Paths.get("src", "main", "java", "oopwj", "Data");
    private static final java.nio.file.Path DATA_DIR_FALLBACK = java.nio.file.Paths.get("target", "classes", "oopwj", "Data");
    private static final java.time.format.DateTimeFormatter DATE_FORMATTER = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final String WEEK_PREFIX = "Week of ";
    private final String lecturerID;
    private final Lecturer_menu parentWindow;
    private boolean suppressDayFilterEvent = false;
    private boolean suppressWeekFilterEvent = false;

    /**
     * Creates new form Lecturer_schedule
     */
    public Lecturer_schedule() {
        this(null, null);
    }

    public Lecturer_schedule(String lecturerID, Lecturer_menu parentWindow) {
        this.lecturerID = lecturerID;
        this.parentWindow = parentWindow;
        initComponents();
        setupListeners();
        setupDayFilter();
        setupWeekFilter();
        configureScrollPane();
        buildScheduleView();
        centerWindow();
    }

    private void centerWindow() {
        setLocationRelativeTo(null);
    }

    private void setupListeners() {
        jButton1.addActionListener(evt -> handleExit());
    }

    private void setupDayFilter() {
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        suppressDayFilterEvent = true;
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(days));
        jComboBox1.setSelectedIndex(0);
        java.awt.Dimension comboSize = jComboBox1.getPreferredSize();
        jComboBox1.setMinimumSize(comboSize);
        jComboBox1.setMaximumSize(comboSize);
        suppressDayFilterEvent = false;
        jComboBox1.addActionListener(evt -> {
            if (suppressDayFilterEvent) {
                return;
            }
            buildScheduleView();
        });
    }

    private void setupWeekFilter() {
        suppressWeekFilterEvent = true;
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[0]));
        java.awt.Dimension comboSize = jComboBox2.getPreferredSize();
        jComboBox2.setMinimumSize(comboSize);
        jComboBox2.setMaximumSize(comboSize);
        suppressWeekFilterEvent = false;
        jComboBox2.addActionListener(evt -> {
            if (suppressWeekFilterEvent) {
                return;
            }
            buildScheduleView();
        });
    }

    private void handleExit() {
        if (parentWindow != null) {
            parentWindow.setVisible(true);
        } else {
            new Lecturer_menu(lecturerID).setVisible(true);
        }
        this.dispose();
    }

    private void configureScrollPane() {
        javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane(jPanel1);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getVerticalScrollBar().setBlockIncrement(64);
        setContentPane(scrollPane);
        if (jScrollBar1 != null) {
            jScrollBar1.setVisible(false);
        }
        pack();
    }

    private void buildScheduleView() {
        java.nio.file.Path dataDir = resolveDataDir();
        java.util.List<String[]> scheduleRows = readCsvLines(dataDir.resolve("class_schedule.txt"));
        if (lecturerID != null && !lecturerID.isEmpty()) {
            java.util.Set<String> lecturerModuleIds = readLecturerModuleIds(
                    dataDir.resolve("modules.txt"),
                    lecturerID);
            if (!lecturerModuleIds.isEmpty()) {
                scheduleRows = filterScheduleRows(scheduleRows, lecturerModuleIds);
            } else {
                scheduleRows = new java.util.ArrayList<>();
            }
        }
        updateWeekFilterOptions(scheduleRows);
        java.time.LocalDate selectedWeekStart = getSelectedWeekStart();
        if (selectedWeekStart != null) {
            scheduleRows = filterScheduleRowsByWeek(scheduleRows, selectedWeekStart);
        } else {
            String selectedDay = getSelectedDay();
            if (selectedDay != null && !selectedDay.isEmpty()) {
                scheduleRows = filterScheduleRowsByDay(scheduleRows, selectedDay);
            }
        }
        if (scheduleRows.isEmpty()) {
            String emptyDay = selectedWeekStart == null ? getSelectedDay() : null;
            showEmptySchedule(emptyDay);
            return;
        }

        java.util.Map<String, String> classMap = readClassMap(dataDir.resolve("class.txt"));
        java.util.Map<String, String> moduleMap = readModuleMap(dataDir.resolve("modules.txt"));

        jPanel1.removeAll();
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.Y_AXIS));
        jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 41, 20, 41));

        javax.swing.JPanel filterPanel = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 0));
        filterPanel.setBackground(jPanel1.getBackground());
        filterPanel.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        filterPanel.add(jComboBox2);
        filterPanel.add(jComboBox1);
        jPanel1.add(filterPanel);
        jPanel1.add(javax.swing.Box.createVerticalStrut(12));

        javax.swing.JPanel scheduleListPanel = new javax.swing.JPanel();
        scheduleListPanel.setLayout(new javax.swing.BoxLayout(scheduleListPanel, javax.swing.BoxLayout.Y_AXIS));
        scheduleListPanel.setBackground(jPanel1.getBackground());
        scheduleListPanel.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);

        applyScheduleRow(jLabel1, jLabel2, jLabel3, jLabel4, scheduleRows.get(0), classMap, moduleMap);
        jLabel1.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        jPanel2.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        java.awt.Dimension basePanelSize = jPanel2.getPreferredSize();
        if (basePanelSize != null) {
            jPanel2.setPreferredSize(basePanelSize);
            jPanel2.setMinimumSize(basePanelSize);
            jPanel2.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, basePanelSize.height));
        }
        scheduleListPanel.add(jLabel1);
        scheduleListPanel.add(javax.swing.Box.createVerticalStrut(8));
        scheduleListPanel.add(jPanel2);

        for (int i = 1; i < scheduleRows.size(); i++) {
            javax.swing.JLabel dayDateLabel = new javax.swing.JLabel();
            dayDateLabel.setFont(jLabel1.getFont());
            dayDateLabel.setForeground(jLabel1.getForeground());
            dayDateLabel.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);

                ScheduleRowComponents rowComponents = createScheduleDetailPanel();
                rowComponents.classLabel.setFont(jLabel2.getFont());
                rowComponents.classLabel.setForeground(jLabel2.getForeground());
                rowComponents.moduleLabel.setFont(jLabel3.getFont());
                rowComponents.moduleLabel.setForeground(jLabel3.getForeground());
                rowComponents.timeLabel.setFont(jLabel4.getFont());
                rowComponents.timeLabel.setForeground(jLabel4.getForeground());
            applyScheduleRow(
                    dayDateLabel,
                    rowComponents.classLabel,
                    rowComponents.moduleLabel,
                    rowComponents.timeLabel,
                    scheduleRows.get(i),
                    classMap,
                    moduleMap);

            scheduleListPanel.add(javax.swing.Box.createVerticalStrut(12));
            scheduleListPanel.add(dayDateLabel);
            scheduleListPanel.add(javax.swing.Box.createVerticalStrut(8));
            scheduleListPanel.add(rowComponents.panel);
        }

        jPanel1.add(scheduleListPanel);
        jPanel1.add(javax.swing.Box.createVerticalGlue());

        javax.swing.JPanel buttonPanel = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 18, 0));
        buttonPanel.setBackground(jPanel1.getBackground());
        buttonPanel.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        buttonPanel.add(jButton2);
        buttonPanel.add(jButton3);
        buttonPanel.add(jButton1);

        jPanel1.add(javax.swing.Box.createVerticalStrut(16));
        jPanel1.add(buttonPanel);

        jPanel1.revalidate();
        jPanel1.repaint();
    }

    private java.util.Set<String> readLecturerModuleIds(java.nio.file.Path path, String lecturerID) {
        java.util.Set<String> moduleIds = new java.util.HashSet<>();
        try {
            java.util.List<String> lines = java.nio.file.Files.readAllLines(path, java.nio.charset.StandardCharsets.UTF_8);
            for (String line : lines) {
                String trimmed = line.trim();
                if (trimmed.isEmpty()) {
                    continue;
                }
                String[] parts = trimmed.split("\\s*,\\s*");
                if (parts.length > 3) {
                    String moduleId = parts[0];
                    String assignedLecturerId = parts[3];
                    if (assignedLecturerId.equalsIgnoreCase(lecturerID)) {
                        moduleIds.add(moduleId);
                    }
                }
            }
        } catch (java.io.IOException ex) {
            logger.log(java.util.logging.Level.SEVERE, "Failed to read modules file: " + path, ex);
        }
        return moduleIds;
    }

    private java.util.List<String[]> filterScheduleRows(
            java.util.List<String[]> scheduleRows,
            java.util.Set<String> lecturerModuleIds) {
        java.util.List<String[]> filtered = new java.util.ArrayList<>();
        for (String[] row : scheduleRows) {
            if (row.length > 1 && lecturerModuleIds.contains(row[1])) {
                filtered.add(row);
            }
        }
        return filtered;
    }

    private java.util.List<String[]> filterScheduleRowsByDay(
            java.util.List<String[]> scheduleRows,
            String day) {
        java.util.List<String[]> filtered = new java.util.ArrayList<>();
        for (String[] row : scheduleRows) {
            if (row.length > 3 && day.equalsIgnoreCase(row[3].trim())) {
                filtered.add(row);
            }
        }
        return filtered;
    }

    private java.util.List<String[]> filterScheduleRowsByWeek(
            java.util.List<String[]> scheduleRows,
            java.time.LocalDate weekStart) {
        java.util.List<String[]> filtered = new java.util.ArrayList<>();
        java.time.LocalDate weekEnd = weekStart.plusDays(6);
        for (String[] row : scheduleRows) {
            if (row.length > 2) {
                java.time.LocalDate date = parseDate(row[2]);
                if (date != null && !date.isBefore(weekStart) && !date.isAfter(weekEnd)) {
                    filtered.add(row);
                }
            }
        }
        return filtered;
    }

    private String getSelectedDay() {
        Object selected = jComboBox1.getSelectedItem();
        if (selected == null) {
            return null;
        }
        return selected.toString().trim();
    }

    private void showEmptySchedule(String selectedDay) {
        jPanel1.removeAll();
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.Y_AXIS));
        jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 41, 20, 41));

        javax.swing.JPanel filterPanel = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 0));
        filterPanel.setBackground(jPanel1.getBackground());
        filterPanel.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        filterPanel.add(jComboBox2);
        filterPanel.add(jComboBox1);
        jPanel1.add(filterPanel);
        jPanel1.add(javax.swing.Box.createVerticalStrut(12));

        String message = "No timetable available";
        if (selectedDay != null && !selectedDay.isEmpty()) {
            message += " for " + selectedDay;
        }
        message += ".";
        javax.swing.JLabel emptyLabel = new javax.swing.JLabel(message);
        emptyLabel.setFont(emptyLabel.getFont().deriveFont(java.awt.Font.BOLD, 18f));
        emptyLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jPanel2.removeAll();
        jPanel2.setLayout(new java.awt.BorderLayout());
        jPanel2.setBackground(jPanel1.getBackground());
        jPanel2.add(emptyLabel, java.awt.BorderLayout.CENTER);
        jPanel2.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        jPanel1.add(jPanel2);

        jPanel1.add(javax.swing.Box.createVerticalStrut(16));
        javax.swing.JPanel buttonPanel = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 18, 0));
        buttonPanel.setBackground(jPanel1.getBackground());
        buttonPanel.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        buttonPanel.add(jButton1);
        java.awt.Dimension panelBaseSize = jPanel2.getPreferredSize();
        java.awt.Dimension buttonSize = jButton1.getPreferredSize();
        if (panelBaseSize != null && buttonSize != null) {
            buttonPanel.setPreferredSize(new java.awt.Dimension(panelBaseSize.width, buttonSize.height));
            buttonPanel.setMinimumSize(new java.awt.Dimension(panelBaseSize.width, buttonSize.height));
        }
        jPanel1.add(buttonPanel);
        jPanel1.add(javax.swing.Box.createVerticalGlue());

        jPanel1.revalidate();
        jPanel1.repaint();
    }

    private ScheduleRowComponents createScheduleDetailPanel() {
        javax.swing.JPanel panel = new javax.swing.JPanel();
        panel.setLayout(new javax.swing.BoxLayout(panel, javax.swing.BoxLayout.Y_AXIS));
        panel.setBackground(jPanel2.getBackground());
        panel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 15, 10, 0));
        panel.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        java.awt.Dimension panelSize = jPanel2.getPreferredSize();
        if (panelSize != null) {
            panel.setPreferredSize(panelSize);
            panel.setMinimumSize(panelSize);
            panel.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, panelSize.height));
        }

        javax.swing.JLabel classLabel = new javax.swing.JLabel();
        javax.swing.JLabel moduleLabel = new javax.swing.JLabel();
        javax.swing.JLabel timeLabel = new javax.swing.JLabel();

        panel.add(classLabel);
        panel.add(javax.swing.Box.createVerticalStrut(8));
        panel.add(moduleLabel);
        panel.add(javax.swing.Box.createVerticalStrut(8));
        panel.add(timeLabel);

        return new ScheduleRowComponents(panel, classLabel, moduleLabel, timeLabel);
    }

    private static final class ScheduleRowComponents {
        private final javax.swing.JPanel panel;
        private final javax.swing.JLabel classLabel;
        private final javax.swing.JLabel moduleLabel;
        private final javax.swing.JLabel timeLabel;

        private ScheduleRowComponents(
                javax.swing.JPanel panel,
                javax.swing.JLabel classLabel,
                javax.swing.JLabel moduleLabel,
                javax.swing.JLabel timeLabel) {
            this.panel = panel;
            this.classLabel = classLabel;
            this.moduleLabel = moduleLabel;
            this.timeLabel = timeLabel;
        }
    }

    private void applyScheduleRow(
            javax.swing.JLabel dayDateLabel,
            javax.swing.JLabel classLabel,
            javax.swing.JLabel moduleLabel,
            javax.swing.JLabel timeLabel,
            String[] row,
            java.util.Map<String, String> classMap,
            java.util.Map<String, String> moduleMap) {
        if (row.length < 6) {
            return;
        }

        String classId = row[0];
        String moduleId = row[1];
        String date = row[2];
        String day = row[3];
        String start = row[4];
        String end = row[5];

        dayDateLabel.setText(day + ", " + date);
        String classLine = classMap.getOrDefault(classId, classId);
        classLabel.setText("Class: " + classLine);
        String moduleName = moduleMap.getOrDefault(moduleId, moduleId);
        moduleLabel.setText("Module: " + moduleName);
        timeLabel.setText("Time: " + start + " - " + end);
    }

    private java.util.List<String[]> readCsvLines(java.nio.file.Path path) {
        java.util.List<String[]> rows = new java.util.ArrayList<>();
        try {
            java.util.List<String> lines = java.nio.file.Files.readAllLines(path, java.nio.charset.StandardCharsets.UTF_8);
            for (String line : lines) {
                String trimmed = line.trim();
                if (trimmed.isEmpty()) {
                    continue;
                }
                rows.add(trimmed.split("\\s*,\\s*"));
            }
        } catch (java.io.IOException ex) {
            logger.log(java.util.logging.Level.SEVERE, "Failed to read schedule file: " + path, ex);
        }
        return rows;
    }

    private java.util.Map<String, String> readClassMap(java.nio.file.Path path) {
        java.util.Map<String, String> map = new java.util.HashMap<>();
        try {
            java.util.List<String> lines = java.nio.file.Files.readAllLines(path, java.nio.charset.StandardCharsets.UTF_8);
            for (String line : lines) {
                String trimmed = line.trim();
                if (trimmed.isEmpty()) {
                    continue;
                }
                String[] parts = trimmed.split("\\s*,\\s*");
                if (parts.length > 0) {
                    map.put(parts[0], trimmed);
                }
            }
        } catch (java.io.IOException ex) {
            logger.log(java.util.logging.Level.SEVERE, "Failed to read class file: " + path, ex);
        }
        return map;
    }

    private java.util.Map<String, String> readModuleMap(java.nio.file.Path path) {
        java.util.Map<String, String> map = new java.util.HashMap<>();
        try {
            java.util.List<String> lines = java.nio.file.Files.readAllLines(path, java.nio.charset.StandardCharsets.UTF_8);
            for (String line : lines) {
                String trimmed = line.trim();
                if (trimmed.isEmpty()) {
                    continue;
                }
                String[] parts = trimmed.split("\\s*,\\s*");
                if (parts.length > 1) {
                    map.put(parts[0], parts[1]);
                }
            }
        } catch (java.io.IOException ex) {
            logger.log(java.util.logging.Level.SEVERE, "Failed to read modules file: " + path, ex);
        }
        return map;
    }

    private void updateWeekFilterOptions(java.util.List<String[]> scheduleRows) {
        java.util.Set<java.time.LocalDate> weekStarts = new java.util.HashSet<>();
        for (String[] row : scheduleRows) {
            if (row.length > 2) {
                java.time.LocalDate date = parseDate(row[2]);
                if (date != null) {
                    java.time.LocalDate weekStart = date.with(java.time.temporal.TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
                    weekStarts.add(weekStart);
                }
            }
        }

        java.util.List<java.time.LocalDate> sortedWeekStarts = new java.util.ArrayList<>(weekStarts);
        java.util.Collections.sort(sortedWeekStarts);

        java.util.List<String> labels = new java.util.ArrayList<>();
        for (java.time.LocalDate weekStart : sortedWeekStarts) {
            labels.add(WEEK_PREFIX + weekStart.format(DATE_FORMATTER));
        }

        Object selected = jComboBox2.getSelectedItem();
        String selectedLabel = selected == null ? null : selected.toString();
        suppressWeekFilterEvent = true;
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(labels.toArray(new String[0])));
        if (selectedLabel != null && labels.contains(selectedLabel)) {
            jComboBox2.setSelectedItem(selectedLabel);
        } else if (!labels.isEmpty()) {
            jComboBox2.setSelectedIndex(0);
        }
        suppressWeekFilterEvent = false;
    }

    private java.time.LocalDate getSelectedWeekStart() {
        Object selected = jComboBox2.getSelectedItem();
        if (selected == null) {
            return null;
        }
        String label = selected.toString().trim();
        if (label.isEmpty()) {
            return null;
        }
        if (label.startsWith(WEEK_PREFIX)) {
            String dateText = label.substring(WEEK_PREFIX.length()).trim();
            return parseDate(dateText);
        }
        return null;
    }

    private java.time.LocalDate parseDate(String value) {
        try {
            return java.time.LocalDate.parse(value.trim(), DATE_FORMATTER);
        } catch (java.time.format.DateTimeParseException ex) {
            return null;
        }
    }

    private java.nio.file.Path resolveDataDir() {
        if (java.nio.file.Files.exists(DATA_DIR)) {
            return DATA_DIR;
        }
        return DATA_DIR_FALLBACK;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jScrollBar1 = new javax.swing.JScrollBar();
        jComboBox2 = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Day, Date");

        jLabel2.setText("Class:");

        jLabel3.setText("Module:");

        jLabel4.setText("Time:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 391, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(60, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel4)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jButton1.setText("Exit");

        jButton2.setText("Previous");

        jButton3.setText("Next");

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton2)
                        .addGap(18, 18, 18)
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(41, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(jScrollBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 302, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3))
                .addGap(26, 26, 26))
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
        java.awt.EventQueue.invokeLater(() -> new Lecturer_schedule().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollBar jScrollBar1;
    // End of variables declaration//GEN-END:variables
}
