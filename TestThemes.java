import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ============================================================
 *                    GAMESMANIA THEME TEST SUITE
 * ============================================================
 *
 * Dedicated test suite and interactive live visual tester for GamesMania themes.
 *
 * Capabilities:
 *  1. Automated test suite (assertions for colors, aliases, listeners, persistence).
 *  2. Real-time visual playground to inspect and test all 5 themes.
 *  3. Live component test (cards, buttons, typography, inputs, swatches).
 *  4. Quick launchers to test themes in SettingFrame, HomeFrame, and the full app.
 *
 * Can be run from terminal, IDE, or automated build tools.
 * ============================================================
 */
public class TestThemes extends JFrame {

    private JTextArea logArea;
    private JLabel statusBadge;
    private JPanel swatchContainer;
    private JPanel previewCard;
    private JLabel previewCardTitle;
    private JLabel previewCardDesc;
    private JButton primaryPreviewBtn;
    private JButton secondaryPreviewBtn;
    private JTextField sampleInput;
    private JCheckBox sampleCheckbox;
    private JLabel currentThemeLabel;

    public TestThemes() {
        this(true);
    }

    public TestThemes(boolean isGui) {
        if (isGui) {
            setTitle("GamesMania • Theme Test Suite & Visual Showcase");
            setSize(960, 720);
            setMinimumSize(new Dimension(840, 600));
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            buildUI();
            applyTheme(ThemeManager.getCurrentTheme());

            // Listen for live updates
            ThemeManager.addListener(this::applyTheme);
        }
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout(0, 16));
        root.setBorder(new EmptyBorder(18, 20, 18, 20));

        // 1. Top Bar: Title & Theme Switchers
        JPanel topBar = new JPanel(new BorderLayout(12, 10));
        topBar.setOpaque(false);

        JPanel titleBox = new JPanel(new GridLayout(2, 1, 0, 4));
        titleBox.setOpaque(false);
        JLabel title = new JLabel("🎨  GAMESMANIA THEME TEST SUITE");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        currentThemeLabel = new JLabel("Active Theme: " + ThemeManager.getCurrentTheme().name);
        currentThemeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        titleBox.add(title);
        titleBox.add(currentThemeLabel);
        topBar.add(titleBox, BorderLayout.WEST);

        // Theme buttons bar
        JPanel themeBtnBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 4));
        themeBtnBar.setOpaque(false);

        for (String themeName : ThemeManager.getThemeNames()) {
            JButton tBtn = new JButton(themeName);
            tBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
            tBtn.setFocusPainted(false);
            tBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            tBtn.addActionListener(e -> {
                ThemeManager.setTheme(themeName);
                log("[USER] Selected theme: " + themeName);
            });
            themeBtnBar.add(tBtn);
        }

        JButton cycleBtn = new JButton("🔄 Cycle Next");
        cycleBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        cycleBtn.setFocusPainted(false);
        cycleBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cycleBtn.addActionListener(e -> {
            String[] names = ThemeManager.getThemeNames();
            String curr = ThemeManager.getCurrentTheme().name;
            int nextIdx = 0;
            for (int i = 0; i < names.length; i++) {
                if (names[i].equalsIgnoreCase(curr)) {
                    nextIdx = (i + 1) % names.length;
                    break;
                }
            }
            ThemeManager.setTheme(names[nextIdx]);
            log("[USER] Cycled to theme: " + names[nextIdx]);
        });
        themeBtnBar.add(cycleBtn);

        topBar.add(themeBtnBar, BorderLayout.EAST);
        root.add(topBar, BorderLayout.NORTH);

        // 2. Center: Split Pane with Live Visual Preview & Test Log
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.52);
        splitPane.setDividerSize(6);
        splitPane.setOpaque(false);

        // Left Panel: Live Component Showcase
        JPanel leftPanel = new JPanel(new BorderLayout(0, 14));
        leftPanel.setOpaque(false);

        // Color Swatches Box
        JPanel swatchesWrapper = new JPanel(new BorderLayout(0, 6));
        swatchesWrapper.setOpaque(false);
        JLabel swatchesTitle = new JLabel("COLOR PALETTE SWATCHES");
        swatchesTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        swatchesWrapper.add(swatchesTitle, BorderLayout.NORTH);

        swatchContainer = new JPanel(new GridLayout(2, 4, 8, 8));
        swatchContainer.setOpaque(false);
        swatchesWrapper.add(swatchContainer, BorderLayout.CENTER);
        leftPanel.add(swatchesWrapper, BorderLayout.NORTH);

        // Component Preview Box
        previewCard = new JPanel(new BorderLayout(0, 14));
        previewCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255, 30), 1, true),
                new EmptyBorder(16, 16, 16, 16)
        ));

        JPanel cardHeader = new JPanel(new GridLayout(2, 1, 0, 4));
        cardHeader.setOpaque(false);
        previewCardTitle = new JLabel("🎮 GamesMania Interactive Preview");
        previewCardTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        previewCardDesc = new JLabel("All UI components adapt in real time when you change themes.");
        previewCardDesc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cardHeader.add(previewCardTitle);
        cardHeader.add(previewCardDesc);
        previewCard.add(cardHeader, BorderLayout.NORTH);

        // Interactive sample components
        JPanel interactiveGrid = new JPanel(new GridLayout(3, 1, 0, 10));
        interactiveGrid.setOpaque(false);

        JPanel inputRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        inputRow.setOpaque(false);
        sampleInput = new JTextField("Sample Player Alias", 14);
        sampleInput.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        sampleCheckbox = new JCheckBox("Live Animation Active", true);
        sampleCheckbox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sampleCheckbox.setOpaque(false);
        inputRow.add(sampleInput);
        inputRow.add(sampleCheckbox);
        interactiveGrid.add(inputRow);

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        btnRow.setOpaque(false);
        primaryPreviewBtn = new JButton("PRIMARY ACTION");
        primaryPreviewBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        primaryPreviewBtn.setFocusPainted(false);
        primaryPreviewBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        secondaryPreviewBtn = new JButton("SECONDARY ACTION");
        secondaryPreviewBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        secondaryPreviewBtn.setFocusPainted(false);
        secondaryPreviewBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRow.add(primaryPreviewBtn);
        btnRow.add(secondaryPreviewBtn);
        interactiveGrid.add(btnRow);

        // Mini status bar
        JPanel miniStatus = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        miniStatus.setOpaque(false);
        JLabel badge1 = new JLabel(" ● Online ");
        badge1.setOpaque(true);
        badge1.setBackground(new Color(16, 185, 129, 40));
        badge1.setForeground(new Color(52, 211, 153));
        badge1.setFont(new Font("Segoe UI", Font.BOLD, 11));
        badge1.setBorder(BorderFactory.createLineBorder(new Color(16, 185, 129), 1, true));

        JLabel badge2 = new JLabel(" 10 Games Loaded ");
        badge2.setOpaque(true);
        badge2.setBackground(new Color(56, 189, 248, 40));
        badge2.setForeground(new Color(56, 189, 248));
        badge2.setFont(new Font("Segoe UI", Font.BOLD, 11));
        badge2.setBorder(BorderFactory.createLineBorder(new Color(56, 189, 248), 1, true));

        miniStatus.add(badge1);
        miniStatus.add(badge2);
        interactiveGrid.add(miniStatus);

        previewCard.add(interactiveGrid, BorderLayout.CENTER);
        leftPanel.add(previewCard, BorderLayout.CENTER);

        // Quick Launch Buttons
        JPanel launcherPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        launcherPanel.setOpaque(false);

        JButton launchSettingsBtn = new JButton("⚙ Open Settings");
        launchSettingsBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        launchSettingsBtn.addActionListener(e -> new SettingFrame());

        JButton launchHomeBtn = new JButton("🏠 Open Home");
        launchHomeBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        launchHomeBtn.addActionListener(e -> new HomeFrame("Tester"));

        JButton launchMainBtn = new JButton("🚀 Launch Main App");
        launchMainBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        launchMainBtn.addActionListener(e -> SwingUtilities.invokeLater(() -> Main.main(new String[0])));

        launcherPanel.add(launchSettingsBtn);
        launcherPanel.add(launchHomeBtn);
        launcherPanel.add(launchMainBtn);
        leftPanel.add(launcherPanel, BorderLayout.SOUTH);

        splitPane.setLeftComponent(leftPanel);

        // Right Panel: Automated Test Log & Controller
        JPanel rightPanel = new JPanel(new BorderLayout(0, 10));
        rightPanel.setOpaque(false);

        JPanel logHeader = new JPanel(new BorderLayout());
        logHeader.setOpaque(false);
        JLabel logTitle = new JLabel("TEST EXECUTION CONSOLE");
        logTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));

        statusBadge = new JLabel(" READY ");
        statusBadge.setFont(new Font("Segoe UI", Font.BOLD, 11));
        statusBadge.setOpaque(true);
        statusBadge.setBackground(new Color(100, 116, 139, 40));
        statusBadge.setForeground(new Color(148, 163, 184));
        statusBadge.setBorder(BorderFactory.createLineBorder(new Color(100, 116, 139), 1, true));

        logHeader.add(logTitle, BorderLayout.WEST);
        logHeader.add(statusBadge, BorderLayout.EAST);
        rightPanel.add(logHeader, BorderLayout.NORTH);

        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        logArea.setBackground(new Color(15, 23, 42));
        logArea.setForeground(new Color(226, 232, 240));
        logArea.setBorder(new EmptyBorder(8, 8, 8, 8));

        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(51, 65, 85), 1));
        rightPanel.add(scrollPane, BorderLayout.CENTER);

        // Test action buttons
        JPanel testActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        testActions.setOpaque(false);

        JButton runTestsBtn = new JButton("▶ Run All Tests");
        runTestsBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        runTestsBtn.addActionListener(e -> runAutomatedTests());

        JButton stressTestBtn = new JButton("⚡ Rapid Cycle (50x)");
        stressTestBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        stressTestBtn.addActionListener(e -> runStressTest());

        JButton clearLogBtn = new JButton("Clear Log");
        clearLogBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        clearLogBtn.addActionListener(e -> logArea.setText(""));

        testActions.add(clearLogBtn);
        testActions.add(stressTestBtn);
        testActions.add(runTestsBtn);
        rightPanel.add(testActions, BorderLayout.SOUTH);

        splitPane.setRightComponent(rightPanel);
        root.add(splitPane, BorderLayout.CENTER);

        setContentPane(root);
    }

    private void updateSwatches(ThemeManager.Theme theme) {
        if (swatchContainer == null) return;
        swatchContainer.removeAll();

        Object[][] items = {
                {"Background", theme.bg},
                {"Header Bg", theme.headerBg},
                {"Card Bg", theme.cardBg},
                {"Card Border", theme.cardBorder},
                {"Text Primary", theme.textPrimary},
                {"Text Muted", theme.textMuted},
                {"Accent 1", theme.accent},
                {"Accent 2", theme.accentSecondary}
        };

        for (Object[] item : items) {
            String label = (String) item[0];
            Color c = (Color) item[1];
            String hex = String.format("#%02X%02X%02X", c.getRed(), c.getGreen(), c.getBlue());

            JPanel swatch = new JPanel(new BorderLayout(6, 0));
            swatch.setOpaque(true);
            swatch.setBackground(theme.cardBg);
            swatch.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(theme.cardBorder, 1, true),
                    new EmptyBorder(4, 6, 4, 6)
            ));

            JPanel colorBox = new JPanel();
            colorBox.setBackground(c);
            colorBox.setPreferredSize(new Dimension(20, 20));
            colorBox.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 60), 1));

            JPanel textBox = new JPanel(new GridLayout(2, 1, 0, 1));
            textBox.setOpaque(false);
            JLabel nameLbl = new JLabel(label);
            nameLbl.setFont(new Font("Segoe UI", Font.BOLD, 10));
            nameLbl.setForeground(theme.textPrimary);
            JLabel hexLbl = new JLabel(hex);
            hexLbl.setFont(new Font("Segoe UI", Font.PLAIN, 9));
            hexLbl.setForeground(theme.textMuted);
            textBox.add(nameLbl);
            textBox.add(hexLbl);

            swatch.add(colorBox, BorderLayout.WEST);
            swatch.add(textBox, BorderLayout.CENTER);
            swatchContainer.add(swatch);
        }

        swatchContainer.revalidate();
        swatchContainer.repaint();
    }

    private void applyTheme(ThemeManager.Theme theme) {
        getContentPane().setBackground(theme.bg);
        currentThemeLabel.setText("Active Theme: " + theme.name + (theme.isDark ? " (Dark Mode)" : " (Light Mode)"));
        currentThemeLabel.setForeground(theme.accent);

        if (previewCard != null) {
            previewCard.setBackground(theme.cardBg);
            previewCard.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(theme.cardBorder, 1, true),
                    new EmptyBorder(16, 16, 16, 16)
            ));
            previewCardTitle.setForeground(theme.textPrimary);
            previewCardDesc.setForeground(theme.textMuted);

            sampleInput.setBackground(theme.bg);
            sampleInput.setForeground(theme.textPrimary);
            sampleInput.setCaretColor(theme.textPrimary);
            sampleInput.setBorder(BorderFactory.createLineBorder(theme.cardBorder, 1));

            sampleCheckbox.setForeground(theme.textPrimary);

            primaryPreviewBtn.setBackground(theme.accent);
            primaryPreviewBtn.setForeground(Color.WHITE);
            primaryPreviewBtn.setBorder(BorderFactory.createLineBorder(theme.accent, 1, true));

            secondaryPreviewBtn.setBackground(theme.cardBg);
            secondaryPreviewBtn.setForeground(theme.textPrimary);
            secondaryPreviewBtn.setBorder(BorderFactory.createLineBorder(theme.cardBorder, 1, true));
        }

        updateSwatches(theme);
        repaint();
    }

    private void log(String msg) {
        if (logArea != null) {
            logArea.append(msg + "\n");
            logArea.setCaretPosition(logArea.getDocument().getLength());
        }
        System.out.println(msg);
    }

    // =========================================================
    // AUTOMATED TESTS
    // =========================================================
    public boolean runAutomatedTests() {
        log("\n==================================================");
        log("      RUNNING GAMESMANIA THEME TEST SUITE        ");
        log("==================================================");

        String initialTheme = ThemeManager.getCurrentTheme().name;
        int passed = 0;
        int total = 0;

        try {
            total++;
            if (testThemeDefinitions()) passed++;

            total++;
            if (testThemeColorsNonNull()) passed++;

            total++;
            if (testThemeSwitching()) passed++;

            total++;
            if (testAliasesAndCaseInsensitivity()) passed++;

            total++;
            if (testListenerDispatch()) passed++;

            total++;
            if (testPreferencePersistence()) passed++;
        } finally {
            ThemeManager.setTheme(initialTheme);
        }

        log("--------------------------------------------------");
        log(String.format("SUMMARY: %d/%d test suites PASSED.", passed, total));
        log("==================================================\n");

        boolean allOk = (passed == total);
        if (statusBadge != null) {
            if (allOk) {
                statusBadge.setText(" ✓ ALL " + total + " TESTS PASSED ");
                statusBadge.setBackground(new Color(16, 185, 129, 50));
                statusBadge.setForeground(new Color(52, 211, 153));
                statusBadge.setBorder(BorderFactory.createLineBorder(new Color(16, 185, 129), 1, true));
            } else {
                statusBadge.setText(" ✗ SOME TESTS FAILED ");
                statusBadge.setBackground(new Color(239, 68, 68, 50));
                statusBadge.setForeground(new Color(248, 113, 113));
                statusBadge.setBorder(BorderFactory.createLineBorder(new Color(239, 68, 68), 1, true));
            }
        }
        return allOk;
    }

    private boolean testThemeDefinitions() {
        log("[TEST 1/6] Verifying all standard theme instances...");
        ThemeManager.Theme[] all = ThemeManager.getAllThemes();
        if (all == null || all.length < 5) {
            log("  ✗ Failed: Expected at least 5 themes, got " + (all == null ? 0 : all.length));
            return false;
        }

        String[] expected = {"Dark", "Light", "Vintage", "Blue", "Emerald", "Purple"};
        for (String exp : expected) {
            boolean found = false;
            for (ThemeManager.Theme t : all) {
                if (t.name.equalsIgnoreCase(exp)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                log("  ✗ Failed: Missing expected theme " + exp);
                return false;
            }
        }
        log("  ✓ All 6 theme instances verified (Dark, Light, Vintage, Blue, Emerald, Purple).");
        return true;
    }

    private boolean testThemeColorsNonNull() {
        log("[TEST 2/6] Validating color palette completeness & non-nullness...");
        for (ThemeManager.Theme t : ThemeManager.getAllThemes()) {
            if (t.bg == null || t.headerBg == null || t.cardBg == null || t.cardBorder == null ||
                t.textPrimary == null || t.textMuted == null || t.accent == null || t.accentSecondary == null) {
                log("  ✗ Failed: Theme " + t.name + " contains null color fields!");
                return false;
            }
        }
        log("  ✓ All theme colors are completely defined and non-null.");
        return true;
    }

    private boolean testThemeSwitching() {
        log("[TEST 3/6] Testing dynamic theme switching...");
        String initial = ThemeManager.getCurrentTheme().name;

        for (String name : ThemeManager.getThemeNames()) {
            ThemeManager.setTheme(name);
            if (!ThemeManager.getCurrentTheme().name.equalsIgnoreCase(name)) {
                log("  ✗ Failed: Expected theme " + name + ", but active is " + ThemeManager.getCurrentTheme().name);
                return false;
            }
        }

        // Restore initial
        ThemeManager.setTheme(initial);
        log("  ✓ Successfully switched through all 5 themes with accurate state.");
        return true;
    }

    private boolean testAliasesAndCaseInsensitivity() {
        log("[TEST 4/6] Testing case-insensitivity and friendly alias mappings...");

        ThemeManager.setTheme("light");
        if (!ThemeManager.getCurrentTheme().name.equalsIgnoreCase("Light")) return false;

        ThemeManager.setTheme("BLUE CYBER");
        if (!ThemeManager.getCurrentTheme().name.equalsIgnoreCase("Blue")) return false;

        ThemeManager.setTheme("emerald neon");
        if (!ThemeManager.getCurrentTheme().name.equalsIgnoreCase("Emerald")) return false;

        ThemeManager.setTheme("purple synth");
        if (!ThemeManager.getCurrentTheme().name.equalsIgnoreCase("Purple")) return false;

        ThemeManager.setTheme("sunset");
        if (!ThemeManager.getCurrentTheme().name.equalsIgnoreCase("Purple")) return false;

        ThemeManager.setTheme("vintage");
        if (!ThemeManager.getCurrentTheme().name.equalsIgnoreCase("Vintage")) return false;

        ThemeManager.setTheme("amber");
        if (!ThemeManager.getCurrentTheme().name.equalsIgnoreCase("Vintage")) return false;

        ThemeManager.setTheme("non_existent_random_theme");
        if (!ThemeManager.getCurrentTheme().name.equalsIgnoreCase("Dark")) return false;

        log("  ✓ Aliases ('vintage', 'amber', 'blue cyber', 'emerald neon', 'sunset') & fallback properly handled.");
        return true;
    }

    private boolean testListenerDispatch() {
        log("[TEST 5/6] Testing listener registration, immediate dispatch, and notifications...");
        AtomicInteger count = new AtomicInteger(0);

        java.util.function.Consumer<ThemeManager.Theme> listener = t -> count.incrementAndGet();

        // Adding listener should trigger immediate dispatch with current theme
        ThemeManager.addListener(listener);
        if (count.get() != 1) {
            log("  ✗ Failed: Listener did not receive immediate dispatch upon registration.");
            ThemeManager.removeListener(listener);
            return false;
        }

        // Changing theme should trigger notification
        ThemeManager.setTheme("Blue");
        if (count.get() != 2) {
            log("  ✗ Failed: Listener did not receive notification on setTheme.");
            ThemeManager.removeListener(listener);
            return false;
        }

        // Removing listener should stop notifications
        ThemeManager.removeListener(listener);
        ThemeManager.setTheme("Emerald");
        if (count.get() != 2) {
            log("  ✗ Failed: Removed listener was still notified!");
            return false;
        }

        log("  ✓ Listener lifecycle (register, immediate callback, updates, unregister) verified.");
        return true;
    }

    private boolean testPreferencePersistence() {
        log("[TEST 6/6] Testing preference persistence to preferences.properties...");
        ThemeManager.setTheme("Purple");

        File file = new File("preferences.properties");
        if (!file.exists()) {
            log("  ✗ Failed: preferences.properties file was not created.");
            return false;
        }

        try (InputStream in = new FileInputStream(file)) {
            Properties props = new Properties();
            props.load(in);
            String saved = props.getProperty("theme");
            if (!"Purple".equalsIgnoreCase(saved)) {
                log("  ✗ Failed: preferences.properties has theme=" + saved + ", expected 'Purple'.");
                return false;
            }
        } catch (Exception e) {
            log("  ✗ Error reading preferences: " + e.getMessage());
            return false;
        }

        log("  ✓ Theme choice successfully persisted and verified on disk.");
        return true;
    }

    private void runStressTest() {
        log("\n⚡ Starting 50x rapid theme cycle stress test...");
        long start = System.currentTimeMillis();
        String[] names = ThemeManager.getThemeNames();

        for (int i = 0; i < 50; i++) {
            ThemeManager.setTheme(names[i % names.length]);
        }

        long elapsed = System.currentTimeMillis() - start;
        log(String.format("✓ Completed 50 rapid theme switches in %d ms (avg %.2f ms/switch).", elapsed, elapsed / 50.0));
    }

    // =========================================================
    // MAIN ENTRY POINT
    // =========================================================
    public static void main(String[] args) {
        // Handle headless mode if requested
        if (GraphicsEnvironment.isHeadless() || (args.length > 0 && "--cli".equalsIgnoreCase(args[0]))) {
            System.out.println("Running GamesMania Theme Test Suite in CLI mode...");
            TestThemes suite = new TestThemes(false);
            boolean success = suite.runAutomatedTests();
            System.exit(success ? 0 : 1);
            return;
        }

        // Standard Swing GUI launch
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            TestThemes window = new TestThemes(true);
            window.setVisible(true);
            window.runAutomatedTests();
        });
    }
}
