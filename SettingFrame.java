import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.*;
import java.util.Properties;

/**
 * ============================================================
 *                    GAMESMANIA SETTINGS & PREFERENCES
 * ============================================================
 *
 * Modern, theme-aware, persistent Settings Management for GamesMania.
 * Fully working preferences engine:
 *  - Visual Theme Selection (Light, Dark, Blue, Emerald, Purple)
 *  - AI Game Difficulty (Easy, Medium, Hard)
 *  - Player Alias Profile
 *  - Background Music & Sound Effects Audio Toggles
 *  - Desktop Alert Notifications
 *  - Live Sound FX Test Button
 *  - Dynamic Difficulty Preview Card
 *  - Full Persistence across application restarts via preferences.properties
 * ============================================================
 */
public class SettingFrame extends JFrame {

    private static final String PREF_FILE = "preferences.properties";

    // Global persistent settings in memory
    private static String currentDifficulty = "Medium";
    private static String currentPlayerName = "Player";
    private static boolean currentMusicEnabled = true;
    private static boolean currentSoundEnabled = true;
    private static boolean currentNotificationEnabled = true;

    static {
        loadSettingsFromDisk();
    }

    // UI Components
    private JComboBox<String> themeBox;
    private JComboBox<String> difficultyBox;
    private JLabel difficultyDescLabel;
    private JCheckBox musicBox;
    private JCheckBox soundBox;
    private JCheckBox notificationBox;
    private JTextField playerNameField;
    private JLabel namePreviewBadge;

    private JPanel mainPanel;
    private JPanel settingsPanel;
    private JPanel swatchPanel;
    private JLabel titleLabel;
    private JLabel previewLabel;

    public SettingFrame() {
        setTitle("GamesMania • Settings & Preferences");
        setSize(820, 680);
        setMinimumSize(new Dimension(750, 600));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        // Load latest saved values
        loadSettingsFromDisk();

        createUI();
        applyTheme(ThemeManager.getCurrentTheme());

        // Register listener for global theme updates
        ThemeManager.addListener(this::applyTheme);

        setVisible(true);
    }

    // =========================================================
    // STATIC GLOBAL GETTERS (For Games & Frames)
    // =========================================================

    public static String getDifficulty() {
        return currentDifficulty;
    }

    public static String getPlayerName() {
        return currentPlayerName;
    }

    public static boolean isMusicEnabled() {
        return currentMusicEnabled;
    }

    public static boolean isSoundEnabled() {
        return currentSoundEnabled;
    }

    public static boolean isNotificationEnabled() {
        return currentNotificationEnabled;
    }

    public static void playTestSound() {
        if (currentSoundEnabled) {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    // =========================================================
    // PERSISTENCE (preferences.properties)
    // =========================================================

    private static void loadSettingsFromDisk() {
        File file = new File(PREF_FILE);
        if (!file.exists()) return;

        try (InputStream in = new FileInputStream(file)) {
            Properties props = new Properties();
            props.load(in);

            currentDifficulty = props.getProperty("difficulty", "Medium").trim();
            currentPlayerName = props.getProperty("playerName", "Player").trim();
            currentMusicEnabled = Boolean.parseBoolean(props.getProperty("music", "true").trim());
            currentSoundEnabled = Boolean.parseBoolean(props.getProperty("sound", "true").trim());
            currentNotificationEnabled = Boolean.parseBoolean(props.getProperty("notifications", "true").trim());
        } catch (Exception e) {
            System.err.println("[Settings] Error loading preferences: " + e.getMessage());
        }
    }

    private static void saveSettingsToDisk(String theme, String difficulty, String playerName,
                                           boolean music, boolean sound, boolean notifications) {
        File file = new File(PREF_FILE);
        Properties props = new Properties();

        if (file.exists()) {
            try (InputStream in = new FileInputStream(file)) {
                props.load(in);
            } catch (Exception ignored) {}
        }

        currentDifficulty = difficulty;
        currentPlayerName = playerName;
        currentMusicEnabled = music;
        currentSoundEnabled = sound;
        currentNotificationEnabled = notifications;

        props.setProperty("theme", theme);
        props.setProperty("difficulty", difficulty);
        props.setProperty("playerName", playerName);
        props.setProperty("music", String.valueOf(music));
        props.setProperty("sound", String.valueOf(sound));
        props.setProperty("notifications", String.valueOf(notifications));

        try (OutputStream out = new FileOutputStream(file)) {
            props.store(out, "GamesMania User Preferences & Configurations");
        } catch (Exception e) {
            System.err.println("[Settings] Error saving preferences: " + e.getMessage());
        }
    }

    // =========================================================
    // UI CONSTRUCTION
    // =========================================================

    private void createUI() {
        mainPanel = new JPanel(new BorderLayout(0, 16));
        mainPanel.setBorder(new EmptyBorder(24, 32, 24, 32));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        titleLabel = new JLabel("⚙  GAMESMANIA SETTINGS");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));

        JLabel subtitle = new JLabel("Customize theme, gameplay experience, audio, and player profile");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JPanel titleBox = new JPanel(new GridLayout(2, 1, 0, 4));
        titleBox.setOpaque(false);
        titleBox.add(titleLabel);
        titleBox.add(subtitle);
        headerPanel.add(titleBox, BorderLayout.CENTER);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Settings Content Panel
        settingsPanel = new JPanel(new GridBagLayout());
        settingsPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 12, 10, 12);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        // 1. Visual Theme Selector
        themeBox = new JComboBox<>(ThemeManager.getThemeNames());
        themeBox.setSelectedItem(ThemeManager.getCurrentTheme().name);
        themeBox.addActionListener(e -> {
            String sel = (String) themeBox.getSelectedItem();
            if (sel != null) {
                ThemeManager.setTheme(sel);
                updateSwatches();
            }
        });

        addFormRow(settingsPanel, gbc, row++, "Visual Theme", "Select look & feel across all windows:", themeBox);

        // Theme Live Swatch Box
        swatchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 6));
        swatchPanel.setOpaque(false);
        previewLabel = new JLabel("Live Theme Palette:");
        previewLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        swatchPanel.add(previewLabel);
        updateSwatches();

        gbc.gridx = 1;
        gbc.gridy = row++;
        gbc.gridwidth = 1;
        settingsPanel.add(swatchPanel, gbc);

        // 2. Game Difficulty Selector
        difficultyBox = new JComboBox<>(new String[]{"Easy", "Medium", "Hard"});
        difficultyBox.setSelectedItem(currentDifficulty);

        difficultyDescLabel = new JLabel();
        difficultyDescLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        updateDifficultyDescription();

        difficultyBox.addActionListener(e -> updateDifficultyDescription());

        JPanel diffContainer = new JPanel(new BorderLayout(0, 4));
        diffContainer.setOpaque(false);
        diffContainer.add(difficultyBox, BorderLayout.NORTH);
        diffContainer.add(difficultyDescLabel, BorderLayout.SOUTH);

        addFormRow(settingsPanel, gbc, row++, "Game Difficulty", "Default AI & game speed challenge:", diffContainer);

        // 3. Player Name Field & Live Badge
        JPanel playerContainer = new JPanel(new BorderLayout(10, 0));
        playerContainer.setOpaque(false);

        playerNameField = new JTextField(currentPlayerName, 12);
        playerNameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        namePreviewBadge = new JLabel("👾 " + currentPlayerName);
        namePreviewBadge.setFont(new Font("Segoe UI", Font.BOLD, 12));
        namePreviewBadge.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(99, 102, 241), 1, true),
                new EmptyBorder(4, 8, 4, 8)
        ));

        playerNameField.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                String name = playerNameField.getText().trim();
                namePreviewBadge.setText("👾 " + (name.isEmpty() ? "Player" : name));
            }
        });

        playerContainer.add(playerNameField, BorderLayout.CENTER);
        playerContainer.add(namePreviewBadge, BorderLayout.EAST);

        addFormRow(settingsPanel, gbc, row++, "Player Alias", "Name displayed on leaderboards and games:", playerContainer);

        // 4. Audio & Notification Toggles
        JPanel togglesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        togglesPanel.setOpaque(false);

        musicBox = new JCheckBox("Background Music", currentMusicEnabled);
        soundBox = new JCheckBox("Sound FX", currentSoundEnabled);
        notificationBox = new JCheckBox("Alert Notifications", currentNotificationEnabled);

        JButton testSoundBtn = new JButton("🔊 Test Audio");
        testSoundBtn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        testSoundBtn.setFocusPainted(false);
        testSoundBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        testSoundBtn.addActionListener(e -> {
            if (soundBox.isSelected()) {
                Toolkit.getDefaultToolkit().beep();
                JOptionPane.showMessageDialog(this, "♪ Audio chime test successful! Sound FX is active.", "Audio Test", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Sound FX is currently disabled. Check 'Sound FX' to enable audio.", "Audio Disabled", JOptionPane.WARNING_MESSAGE);
            }
        });

        for (JCheckBox cb : new JCheckBox[]{musicBox, soundBox, notificationBox}) {
            cb.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            cb.setOpaque(false);
            togglesPanel.add(cb);
        }
        togglesPanel.add(testSoundBtn);

        addFormRow(settingsPanel, gbc, row++, "Audio & Alerts", "Configure system audio and notifications:", togglesPanel);

        mainPanel.add(settingsPanel, BorderLayout.CENTER);

        // Bottom Action Buttons
        JPanel bottomBar = new JPanel(new BorderLayout());
        bottomBar.setOpaque(false);
        bottomBar.setBorder(new EmptyBorder(16, 0, 0, 0));

        JButton resetBtn = createStyledButton("RESET DEFAULTS", false);
        resetBtn.addActionListener(e -> resetDefaults());

        JPanel rightBtns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        rightBtns.setOpaque(false);

        JButton closeBtn = createStyledButton("CLOSE", false);
        closeBtn.addActionListener(e -> dispose());

        JButton saveBtn = createStyledButton("APPLY & SAVE", true);
        saveBtn.addActionListener(e -> saveSettings());

        rightBtns.add(closeBtn);
        rightBtns.add(saveBtn);

        bottomBar.add(resetBtn, BorderLayout.WEST);
        bottomBar.add(rightBtns, BorderLayout.EAST);
        mainPanel.add(bottomBar, BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }

    private void updateDifficultyDescription() {
        if (difficultyDescLabel == null || difficultyBox == null) return;
        String sel = (String) difficultyBox.getSelectedItem();
        if ("Easy".equalsIgnoreCase(sel)) {
            difficultyDescLabel.setText("• Easy: Forgiving reaction timers, slower game loops & relaxed AI.");
            difficultyDescLabel.setForeground(new Color(16, 185, 129));
        } else if ("Hard".equalsIgnoreCase(sel)) {
            difficultyDescLabel.setText("• Hard: Rapid game speed, aggressive AI & bonus 1.5x score multiplier.");
            difficultyDescLabel.setForeground(new Color(239, 68, 68));
        } else {
            difficultyDescLabel.setText("• Medium: Standard balanced arcade challenge and timing.");
            difficultyDescLabel.setForeground(new Color(245, 158, 11));
        }
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row, String labelText, String descText, JComponent comp) {
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.35;

        JPanel labelBox = new JPanel(new GridLayout(2, 1, 0, 2));
        labelBox.setOpaque(false);
        JLabel title = new JLabel(labelText);
        title.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JLabel desc = new JLabel(descText);
        desc.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        labelBox.add(title);
        labelBox.add(desc);
        panel.add(labelBox, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.65;
        if (comp instanceof JComboBox || comp instanceof JTextField) {
            comp.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            comp.setPreferredSize(new Dimension(300, 36));
        }
        panel.add(comp, gbc);
    }

    private JButton createStyledButton(String text, boolean primary) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                ThemeManager.Theme theme = ThemeManager.getCurrentTheme();
                Color bg;
                if (primary) {
                    bg = getModel().isRollover() ? theme.accent.brighter() : theme.accent;
                } else {
                    bg = getModel().isRollover() ? theme.cardBorder : theme.cardBg;
                }
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                if (!primary) {
                    g2.setColor(theme.cardBorder);
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(primary ? Color.WHITE : ThemeManager.getCurrentTheme().textPrimary);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(primary ? 150 : 130, 38));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void updateSwatches() {
        if (swatchPanel == null) return;
        swatchPanel.removeAll();
        ThemeManager.Theme theme = ThemeManager.getCurrentTheme();

        Color[] colors = {theme.bg, theme.headerBg, theme.cardBg, theme.accent, theme.accentSecondary};
        String[] tags = {"Background", "Header", "Cards", "Accent 1", "Accent 2"};

        for (int i = 0; i < colors.length; i++) {
            final Color c = colors[i];
            final String tag = tags[i];
            JPanel swatch = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(c);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                    g2.setColor(new Color(255, 255, 255, 60));
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                    g2.dispose();
                }
            };
            swatch.setPreferredSize(new Dimension(32, 22));
            swatch.setToolTipText(tag + " (" + String.format("#%02X%02X%02X", c.getRed(), c.getGreen(), c.getBlue()) + ")");
            swatchPanel.add(swatch);
        }
        swatchPanel.revalidate();
        swatchPanel.repaint();
    }

    private void applyTheme(ThemeManager.Theme theme) {
        getContentPane().setBackground(theme.bg);
        mainPanel.setBackground(theme.bg);
        titleLabel.setForeground(theme.textPrimary);
        if (previewLabel != null) previewLabel.setForeground(theme.textMuted);

        // Update subcomponents
        updateComponentTreeColors(settingsPanel, theme);
        updateSwatches();
        repaint();
    }

    private void updateComponentTreeColors(Container container, ThemeManager.Theme theme) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JLabel) {
                JLabel lbl = (JLabel) comp;
                if (lbl != difficultyDescLabel) {
                    if (lbl.getFont().isBold()) {
                        lbl.setForeground(theme.textPrimary);
                    } else {
                        lbl.setForeground(theme.textMuted);
                    }
                }
            } else if (comp instanceof JCheckBox) {
                ((JCheckBox) comp).setForeground(theme.textPrimary);
            } else if (comp instanceof JComboBox) {
                comp.setBackground(theme.cardBg);
                comp.setForeground(theme.textPrimary);
            } else if (comp instanceof JTextField) {
                comp.setBackground(theme.cardBg);
                comp.setForeground(theme.textPrimary);
                ((JTextField) comp).setCaretColor(theme.textPrimary);
            } else if (comp instanceof Container) {
                updateComponentTreeColors((Container) comp, theme);
            }
        }
    }

    private void saveSettings() {
        String selectedTheme = (String) themeBox.getSelectedItem();
        String selectedDiff = (String) difficultyBox.getSelectedItem();
        String name = playerNameField.getText().trim();
        if (name.isEmpty()) name = "Player";

        boolean music = musicBox.isSelected();
        boolean sound = soundBox.isSelected();
        boolean notify = notificationBox.isSelected();

        // 1. Update theme
        ThemeManager.setTheme(selectedTheme);

        // 2. Persist all settings
        saveSettingsToDisk(selectedTheme, selectedDiff, name, music, sound, notify);

        // 3. Play sound confirmation if enabled
        playTestSound();

        JOptionPane.showMessageDialog(
                this,
                "✓ All Settings & Preferences saved successfully!\n\n"
                        + "• Visual Theme: " + selectedTheme + "\n"
                        + "• AI Difficulty: " + selectedDiff + "\n"
                        + "• Player Alias: " + name + "\n"
                        + "• Background Music: " + (music ? "Enabled" : "Disabled") + "\n"
                        + "• Sound FX: " + (sound ? "Enabled" : "Disabled") + "\n"
                        + "• Notifications: " + (notify ? "Enabled" : "Disabled") + "\n\n"
                        + "Settings have been persisted to preferences.properties.",
                "Settings Saved",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void resetDefaults() {
        themeBox.setSelectedItem("Light");
        difficultyBox.setSelectedItem("Medium");
        playerNameField.setText("Player");
        namePreviewBadge.setText("👾 Player");
        musicBox.setSelected(true);
        soundBox.setSelected(true);
        notificationBox.setSelected(true);

        ThemeManager.setTheme("Light");
        saveSettingsToDisk("Light", "Medium", "Player", true, true, true);
        playTestSound();

        JOptionPane.showMessageDialog(this, "Settings have been reset to factory defaults (Light Theme, Medium Difficulty).", "Defaults Restored", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SettingFrame::new);
    }
}