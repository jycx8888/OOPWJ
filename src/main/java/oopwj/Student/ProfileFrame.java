package oopwj.Student;

import oopwj.Model.User;
import oopwj.Model.StudentService;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Locale;

public class ProfileFrame extends JFrame {

    private static final String PROFILE_IMAGE_DIR = "src/main/java/oopwj/image";
    private static final String[] PROFILE_IMAGE_EXTENSIONS = {"png", "jpg", "jpeg", "gif"};
    
    private JLabel imageLabel; 
    private JLabel nameValueLabel; 
    private User currentUser; 
    private StudentService service;

    // UI Colors
    private Color bgGrey = new Color(245, 247, 250);
    private Color cardWhite = Color.WHITE;
    private Color textDark = new Color(50, 50, 50);
    private Color textLight = new Color(120, 120, 120);
    private Color accentBlue = new Color(0, 123, 255);

    public ProfileFrame(User user) {
        this.currentUser = user;
        this.service = new StudentService();

        setTitle("My Profile");
        setSize(950, 650); 
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        setLocationRelativeTo(null);
        getContentPane().setBackground(bgGrey);
        setLayout(new BorderLayout());

        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(bgGrey);
        contentPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 15, 0, 15);

        // --- Left Column (Avatar) ---
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        gbc.weighty = 1.0;
        contentPanel.add(createAvatarPanel(), gbc);

        // --- Right Column (Info & Courses) ---
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        contentPanel.add(createRightPanel(), gbc);

        add(contentPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    private JPanel createAvatarPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(cardWhite);
        panel.setBorder(new CompoundBorder(
            new LineBorder(new Color(230, 230, 230), 1, true),
            new EmptyBorder(40, 20, 40, 20)
        ));

        // Circular Image Label
        imageLabel = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                if (getIcon() != null) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    Ellipse2D.Double circle = new Ellipse2D.Double(0, 0, getWidth(), getHeight());
                    g2.setClip(circle);
                    getIcon().paintIcon(this, g2, 0, 0);
                    g2.setColor(new Color(230,230,230));
                    g2.setStroke(new BasicStroke(2));
                    g2.draw(circle); // Draw border
                    g2.dispose();
                } else {
                    super.paintComponent(g);
                }
            }
        };
        imageLabel.setPreferredSize(new Dimension(180, 180));
        imageLabel.setMaximumSize(new Dimension(180, 180));
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Load Image
        if (!loadSavedProfileImage()) {
            // Placeholder logic if needed, or default
             ImageIcon defaultIcon = loadAndScaleImage("src/main/java/oopwj/image/default_profile.png", 180, 180);
             if(defaultIcon != null) imageLabel.setIcon(defaultIcon);
        }

        panel.add(imageLabel);
        panel.add(Box.createVerticalStrut(20));

        // Text Button for Upload
        JLabel uploadLink = new JLabel("Change Avatar");
        uploadLink.setFont(new Font("Segoe UI", Font.BOLD, 14));
        uploadLink.setForeground(accentBlue);
        uploadLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        uploadLink.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        uploadLink.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                handleImageUpload();
            }
        });

        panel.add(uploadLink);
        
        // Push everything to top/center
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    private JPanel createRightPanel() {
        JPanel rightContainer = new JPanel();
        rightContainer.setLayout(new BoxLayout(rightContainer, BoxLayout.Y_AXIS));
        rightContainer.setBackground(bgGrey);

        // 1. Personal Info Card
        JPanel infoCard = new JPanel(new GridBagLayout());
        infoCard.setBackground(cardWhite);
        infoCard.setBorder(new CompoundBorder(
            new LineBorder(new Color(230, 230, 230), 1, true),
            new EmptyBorder(25, 30, 25, 30)
        ));
        infoCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(8, 0, 8, 15); // Gap between label and value

        // -- ID --
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        infoCard.add(createLabel("Student ID:"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        JLabel idVal = createValueLabel(currentUser.getUserID());
        infoCard.add(idVal, gbc);

        // -- Name --
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        infoCard.add(createLabel("Full Name:"), gbc);

        gbc.gridx = 1; gbc.weightx = 1.0;
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        namePanel.setBackground(cardWhite);
        
        nameValueLabel = createValueLabel(currentUser.getUserName());
        namePanel.add(nameValueLabel);
        
        JLabel editLink = new JLabel("  ✎ Edit");
        editLink.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        editLink.setForeground(accentBlue);
        editLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        editLink.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                handleNameEdit();
            }
        });
        namePanel.add(editLink);
        infoCard.add(namePanel, gbc);

        // -- Email --
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        infoCard.add(createLabel("Email:"), gbc);

        gbc.gridx = 1; gbc.weightx = 1.0;
        infoCard.add(createValueLabel(currentUser.getEmail()), gbc);

        // -- Separator --
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 0, 20, 0);
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(240, 240, 240));
        infoCard.add(sep, gbc);

        // -- Change Password Button --
        gbc.gridy = 4; gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 0, 0);
        JButton changePassBtn = new JButton("Change Password");
        changePassBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        changePassBtn.setForeground(Color.WHITE);
        changePassBtn.setBackground(new Color(100, 100, 100)); // Dark grey button
        changePassBtn.setFocusPainted(false);
        changePassBtn.setBorder(new EmptyBorder(8, 15, 8, 15));
        changePassBtn.addActionListener(e -> handlePasswordChange());
        
        JPanel btnWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        btnWrapper.setBackground(cardWhite);
        btnWrapper.add(changePassBtn);
        infoCard.add(btnWrapper, gbc);

        rightContainer.add(infoCard);
        rightContainer.add(Box.createVerticalStrut(20)); // Gap between cards

        // 2. Courses Card
        JPanel coursesCard = new JPanel(new BorderLayout());
        coursesCard.setBackground(cardWhite);
        coursesCard.setBorder(new CompoundBorder(
            new LineBorder(new Color(230, 230, 230), 1, true),
            new EmptyBorder(25, 30, 25, 30)
        ));
        coursesCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel courseTitle = new JLabel("Enrolled Courses");
        courseTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        courseTitle.setForeground(textDark);
        courseTitle.setBorder(new EmptyBorder(0, 0, 15, 0));
        coursesCard.add(courseTitle, BorderLayout.NORTH);

        // Course List Container
        JPanel courseListPanel = new JPanel();
        courseListPanel.setLayout(new BoxLayout(courseListPanel, BoxLayout.Y_AXIS));
        courseListPanel.setBackground(cardWhite);

        List<String> myCourses = service.getEnrolledCourses(currentUser.getUserID());
        if (myCourses.isEmpty()) {
            JLabel noCourse = new JLabel("No courses enrolled yet.");
            noCourse.setFont(new Font("Segoe UI", Font.ITALIC, 13));
            noCourse.setForeground(textLight);
            courseListPanel.add(noCourse);
        } else {
            for (String c : myCourses) {
                courseListPanel.add(createCoursePill(c));
                courseListPanel.add(Box.createVerticalStrut(10));
            }
        }

        // Scroll for courses if too many
        JScrollPane scroll = new JScrollPane(courseListPanel);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(10);
        coursesCard.add(scroll, BorderLayout.CENTER);

        // Make sure the course card takes up remaining space nicely
        coursesCard.setPreferredSize(new Dimension(0, 250));

        rightContainer.add(coursesCard);

        return rightContainer;
    }

    // --- Helper UI Components ---

    private JLabel createLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        l.setForeground(textLight);
        return l;
    }

    private JLabel createValueLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 15));
        l.setForeground(textDark);
        return l;
    }

    private JPanel createCoursePill(String courseInfo) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(new Color(248, 249, 250)); // Very light grey
        p.setBorder(new LineBorder(new Color(230, 230, 230), 1, true));
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        p.setPreferredSize(new Dimension(0, 50));

        JLabel icon = new JLabel("📚"); // Book Emoji as Icon
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        icon.setBorder(new EmptyBorder(0, 15, 0, 10));
        
        // Parse course info (Code, Name) if comma separated
        String display = courseInfo;
        String[] parts = courseInfo.split(",");
        if(parts.length >= 2) {
            display = "<html><b>" + parts[1] + "</b> <span style='color:gray'>(" + parts[0] + ")</span></html>";
        }

        JLabel text = new JLabel(display);
        text.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        text.setForeground(textDark);
        
        p.add(icon, BorderLayout.WEST);
        p.add(text, BorderLayout.CENTER);
        
        return p;
    }

    // --- Logic Implementation (Same as before, just wired to new UI) ---

    private void handleImageUpload() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Images", "jpg", "jpeg", "png", "gif");
        fileChooser.setFileFilter(filter);
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String extension = getFileExtension(selectedFile.getName());
            if (extension.isEmpty()) return;

            try {
                File imageDir = new File(PROFILE_IMAGE_DIR);
                if (!imageDir.exists()) imageDir.mkdirs();

                deleteExistingProfileImages();
                File destination = new File(imageDir, getProfileImageBaseName() + "." + extension);
                Files.copy(selectedFile.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
                
                ImageIcon newIcon = loadAndScaleImage(destination.getAbsolutePath(), 180, 180);
                imageLabel.setIcon(newIcon);
                JOptionPane.showMessageDialog(this, "Avatar updated!");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error saving image.");
            }
        }
    }

    private void handleNameEdit() {
        String currentName = currentUser.getUserName();
        String newName = JOptionPane.showInputDialog(this, "Enter new name:", currentName);
        
        if (newName != null && !newName.trim().isEmpty()) {
            if (!service.isValidName(newName)) { 
                JOptionPane.showMessageDialog(this, "Invalid name!");
                return;
            }
            if (service.updateName(currentUser.getUserID(), newName.trim())) {
                currentUser.setUserName(newName.trim()); 
                nameValueLabel.setText(newName.trim());
                JOptionPane.showMessageDialog(this, "Name updated successfully!");
            }
        }
    }

    private void handlePasswordChange() {
        JPasswordField pf = new JPasswordField();
        int okCfm = JOptionPane.showConfirmDialog(this, new Object[]{"Current password:", pf}, "Verify", JOptionPane.OK_CANCEL_OPTION);
        
        if (okCfm == JOptionPane.OK_OPTION) {
            String currentPassInput = new String(pf.getPassword());
            if (!currentPassInput.equals(currentUser.getPassword())) {
                JOptionPane.showMessageDialog(this, "Wrong password!");
                return;
            }

            while (true) {
                JPasswordField newPf = new JPasswordField();
                JPasswordField confPf = new JPasswordField();
                JLabel reqLbl = new JLabel("<html><small>Length: 7-14. Must include Upper, Lower, Number, Symbol.</small></html>");

                int opt = JOptionPane.showConfirmDialog(this, 
                    new Object[]{"New password:", newPf, "Confirm:", confPf, " ", reqLbl}, 
                    "Change Password", JOptionPane.OK_CANCEL_OPTION);

                if (opt != JOptionPane.OK_OPTION) break;

                String newPass = new String(newPf.getPassword());
                String confPass = new String(confPf.getPassword());

                if (!service.isValidPassword(newPass)) { 
                    JOptionPane.showMessageDialog(this, "Invalid format!");
                    continue;
                }
                if (!newPass.equals(confPass)) {
                    JOptionPane.showMessageDialog(this, "Passwords do not match!");
                    continue;
                }
                
                if (service.updatePassword(currentUser.getUserID(), newPass)) {
                    currentUser.setPassword(newPass); 
                    JOptionPane.showMessageDialog(this, "Success!");
                    break;
                }
            }
        }
    }

    // --- Utility Methods ---

    private ImageIcon loadAndScaleImage(String path, int w, int h) {
         try {
            File imgFile = new File(path);
            if (imgFile.exists()) {
                BufferedImage img = javax.imageio.ImageIO.read(imgFile);
                Image scaled = img.getScaledInstance(w, h, Image.SCALE_SMOOTH);
                return new ImageIcon(scaled);
            }
        } catch (Exception e) { return null; }
        return null;
    }

    private boolean loadSavedProfileImage() {
        File imageFile = findExistingProfileImage();
        if (imageFile == null || !imageFile.exists()) return false;
        ImageIcon savedIcon = loadAndScaleImage(imageFile.getAbsolutePath(), 180, 180);
        if (savedIcon != null) {
            imageLabel.setIcon(savedIcon);
            return true;
        }
        return false;
    }

    private File findExistingProfileImage() {
        File dir = new File(PROFILE_IMAGE_DIR);
        if (!dir.exists()) return null;
        String baseName = getProfileImageBaseName();
        for (String ext : PROFILE_IMAGE_EXTENSIONS) {
            File candidate = new File(dir, baseName + "." + ext);
            if (candidate.exists()) return candidate;
        }
        return null;
    }

    private String getProfileImageBaseName() { return "profile_" + currentUser.getUserID(); }

    private void deleteExistingProfileImages() {
        File dir = new File(PROFILE_IMAGE_DIR);
        if (!dir.exists()) return;
        String baseName = getProfileImageBaseName();
        for (String ext : PROFILE_IMAGE_EXTENSIONS) {
            File candidate = new File(dir, baseName + "." + ext);
            if (candidate.exists()) candidate.delete();
        }
    }

    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1).toLowerCase(Locale.ROOT);
    }
}