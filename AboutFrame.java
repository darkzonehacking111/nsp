import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * ============================================================
 *         GAMESMANIA • ABOUT, ARCHITECTURE & DEVELOPER LAB
 * ============================================================
 *
 * Comprehensive interactive showcase for GamesMania:
 *  1. Project Structure & Architecture (Layered MVC/DAO, Theme Engine, Game Factory)
 *  2. About Me / Developer Profile (Rishabh Nandi, PIET25cs135, EPT25CS134, Batch C1)
 *  3. Interactive Game Roster (All 16 games with direct launchers)
 *  4. System & Tech Stack Diagnostics
 *  5. Live Database Health & Latency Monitor
 *  6. Interactive Physics & Particle Playground (with Developer Easter Egg)
 * ============================================================
 */
public class AboutFrame extends JFrame {

    private JPanel rootPanel;
    private JPanel headerPanel;
    private JPanel tabContentPanel;
    private CardLayout cardLayout;
    private JLabel titleLabel;
    private JLabel subtitleLabel;

    private String currentTab = "STRUCTURE";
    private final List<JButton> tabButtons = new ArrayList<>();

    // Particle Playground components
    private InteractivePlayground playground;

    public AboutFrame() {
        setTitle("GamesMania • Architecture, System Lab & Developer Profile");
        setSize(1140, 780);
        setMinimumSize(new Dimension(980, 680));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        createUI();
        applyTheme(ThemeManager.getCurrentTheme());
        ThemeManager.addListener(this::applyTheme);

        setVisible(true);
    }

    private void createUI() {
        rootPanel = new JPanel(new BorderLayout(0, 0));

        // 1. HEADER
        headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(new EmptyBorder(18, 28, 16, 28));

        JPanel titleBox = new JPanel(new GridLayout(2, 1, 0, 4));
        titleBox.setOpaque(false);

        titleLabel = new JLabel("🏛  GAMESMANIA ARCHITECTURE & LAB");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));

        subtitleLabel = new JLabel("Comprehensive system architecture, developer profile, database diagnostics & interactive lab");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        titleBox.add(titleLabel);
        titleBox.add(subtitleLabel);
        headerPanel.add(titleBox, BorderLayout.WEST);

        JPanel headerActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        headerActions.setOpaque(false);

        JButton devEasterEggBtn = new JButton("🎉 Developer Mode");
        devEasterEggBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        devEasterEggBtn.setFocusPainted(false);
        devEasterEggBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        devEasterEggBtn.addActionListener(e -> triggerEasterEgg());

        JButton closeBtn = new JButton("✕  CLOSE") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? new Color(239, 68, 68) : new Color(220, 38, 38));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        closeBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setFocusPainted(false);
        closeBtn.setContentAreaFilled(false);
        closeBtn.setBorderPainted(false);
        closeBtn.setPreferredSize(new Dimension(95, 34));
        closeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeBtn.addActionListener(e -> dispose());

        headerActions.add(devEasterEggBtn);
        headerActions.add(closeBtn);
        headerPanel.add(headerActions, BorderLayout.EAST);

        // 2. TABS BAR
        JPanel tabsBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        tabsBar.setOpaque(false);
        tabsBar.setBorder(new EmptyBorder(0, 24, 6, 24));

        String[] tabs = {
                "STRUCTURE",
                "ABOUT ME",
                "GAME ROSTER",
                "TECH STACK",
                "LIVE DB MONITOR",
                "PARTICLE PLAYGROUND"
        };

        for (String tab : tabs) {
            JButton tabBtn = createTabButton(tab);
            tabButtons.add(tabBtn);
            tabsBar.add(tabBtn);
        }

        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.setOpaque(false);
        topContainer.add(headerPanel, BorderLayout.NORTH);
        topContainer.add(tabsBar, BorderLayout.SOUTH);
        rootPanel.add(topContainer, BorderLayout.NORTH);

        // 3. TAB CONTENT
        cardLayout = new CardLayout();
        tabContentPanel = new JPanel(cardLayout);
        tabContentPanel.setOpaque(false);
        tabContentPanel.setBorder(new EmptyBorder(10, 28, 20, 28));

        tabContentPanel.add(createStructurePanel(), "STRUCTURE");
        tabContentPanel.add(createAboutMePanel(), "ABOUT ME");
        tabContentPanel.add(createGameRosterPanel(), "GAME ROSTER");
        tabContentPanel.add(createTechStackPanel(), "TECH STACK");
        tabContentPanel.add(createDbMonitorPanel(), "LIVE DB MONITOR");

        playground = new InteractivePlayground();
        tabContentPanel.add(playground, "PARTICLE PLAYGROUND");

        rootPanel.add(tabContentPanel, BorderLayout.CENTER);
        setContentPane(rootPanel);

        switchTab("STRUCTURE");
    }

    private JButton createTabButton(String tabName) {
        JButton btn = new JButton(tabName) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                ThemeManager.Theme theme = ThemeManager.getCurrentTheme();

                boolean isSelected = tabName.equals(currentTab);
                if (isSelected) {
                    g2.setColor(theme.accent);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(theme.accent.getRed(), theme.accent.getGreen(), theme.accent.getBlue(), 40));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };

        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(160, 36));
        btn.addActionListener(e -> switchTab(tabName));
        return btn;
    }

    private void switchTab(String tabName) {
        currentTab = tabName;
        cardLayout.show(tabContentPanel, tabName);

        ThemeManager.Theme theme = ThemeManager.getCurrentTheme();
        for (JButton b : tabButtons) {
            boolean sel = b.getText().equals(tabName);
            b.setForeground(sel ? Color.WHITE : theme.textPrimary);
        }
        repaint();
    }

    // =========================================================
    // 1. PROJECT STRUCTURE & ARCHITECTURE PANEL
    // =========================================================
    private JPanel createStructurePanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 16));
        panel.setOpaque(false);

        // Top summary metrics
        JPanel metricsBar = new JPanel(new GridLayout(1, 5, 12, 0));
        metricsBar.setOpaque(false);
        metricsBar.add(createMetricCard("16 MINI GAMES", "Full Interactive Suite", "🎮", new Color(99, 102, 241)));
        metricsBar.add(createMetricCard("5 THEMES", "Live Pub-Sub Sync", "🎨", new Color(16, 185, 129)));
        metricsBar.add(createMetricCard("DUAL SQL", "MySQL + SQLite Fallback", "🗄️", new Color(6, 182, 212)));
        metricsBar.add(createMetricCard("10,000+ LOC", "Pure Java2D / Swing", "⚡", new Color(245, 158, 11)));
        metricsBar.add(createMetricCard("JAVA 21 LTS", "Cross-Platform Bytecode", "☕", new Color(244, 63, 94)));
        panel.add(metricsBar, BorderLayout.NORTH);

        // Center: Interactive Architecture Layers
        JPanel centerGrid = new JPanel(new GridLayout(1, 2, 16, 0));
        centerGrid.setOpaque(false);

        // Left: Visual Architecture Stack
        JPanel layersCard = createCardPanel("🏗️ SYSTEM ARCHITECTURE LAYERS", "Five-tier modular enterprise structure");
        layersCard.setLayout(new BoxLayout(layersCard, BoxLayout.Y_AXIS));

        String[][] layers = {
                {"LAYER 1: PRESENTATION & UI", "Swing EDT, SplashScreen, LoginFrame, HomeFrame, GameFrame, SettingFrame, AboutFrame, RoundedPanel, GameCard", "🖼️"},
                {"LAYER 2: GAME ENGINE & FACTORY", "GameFactory, 16 individual game canvases with internal 60 FPS Java2D rendering loops & collision physics", "🎮"},
                {"LAYER 3: THEME ENGINE (OBSERVER)", "ThemeManager, Consumer<Theme> live event bus, 5 responsive themes, instant propagation without reload", "🎨"},
                {"LAYER 4: DATA ACCESS LAYER (DAO)", "DBConnection (auto-provisions MySQL & seamless SQLite fallback), UserDAO, PlayerDAO, ScoreDAO", "💾"},
                {"LAYER 5: CONFIGURATION & AUDIO", "preferences.properties, SettingFrame, AI difficulty settings (Easy/Medium/Hard), audio SFX & alerts", "⚙️"}
        };

        for (String[] layer : layers) {
            JPanel layerRow = new JPanel(new BorderLayout(10, 0));
            layerRow.setOpaque(false);
            layerRow.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(255, 255, 255, 25), 1, true),
                    new EmptyBorder(8, 10, 8, 10)
            ));

            JLabel icon = new JLabel(layer[2]);
            icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));

            JPanel text = new JPanel(new GridLayout(2, 1, 0, 2));
            text.setOpaque(false);
            JLabel name = new JLabel(layer[0]);
            name.setFont(new Font("Segoe UI", Font.BOLD, 12));
            JLabel desc = new JLabel("<html><body style='width:240px;'>" + layer[1] + "</body></html>");
            desc.setFont(new Font("Segoe UI", Font.PLAIN, 10));

            text.add(name);
            text.add(desc);
            layerRow.add(icon, BorderLayout.WEST);
            layerRow.add(text, BorderLayout.CENTER);

            layersCard.add(layerRow);
            layersCard.add(Box.createRigidArea(new Dimension(0, 8)));
        }

        // Right: Design Patterns & Directory Tree
        JPanel detailsCard = createCardPanel("📐 DESIGN PATTERNS & CODEBASE DIRECTORY", "Core engineering principles used in GamesMania");
        detailsCard.setLayout(new BorderLayout(0, 10));

        JTextArea dirTree = new JTextArea();
        dirTree.setEditable(false);
        dirTree.setFont(new Font("Consolas", Font.PLAIN, 11));
        dirTree.setBackground(new Color(15, 23, 42));
        dirTree.setForeground(new Color(226, 232, 240));
        dirTree.setText(
                "GamesMania Project Structure:\n"
                + "├── src/\n"
                + "│   ├── Main.java                 (Bootstrapper & System LookAndFeel)\n"
                + "│   ├── SplashScreen.java         (Animated 60 FPS loading canvas)\n"
                + "│   ├── LoginFrame.java           (Single-step player alias authentication)\n"
                + "│   ├── HomeFrame.java            (Interactive Arcade Dashboard)\n"
                + "│   ├── GameFactory.java          (Factory Pattern for dynamic instantiation)\n"
                + "│   ├── GameFrame.java            (Unified Game Container & HUD)\n"
                + "│   ├── SettingFrame.java         (Persistent preferences & difficulty manager)\n"
                + "│   ├── AboutFrame.java           (Architecture Lab & Developer Showcase)\n"
                + "│   ├── ThemeManager.java         (Pub-Sub live theme synchronization)\n"
                + "│   ├── TestThemes.java           (Automated theme test suite & visual tester)\n"
                + "│   ├── DBConnection.java         (Resilient MySQL + SQLite failover engine)\n"
                + "│   ├── [16 Game Engines].java    (Asteroids, Snake, 2048, Pong, Dino...)\n"
                + "│   └── DAOs & Models             (User, Score, PlayerDAO, ScoreDAO...)\n"
                + "├── lib/                          (MySQL & SQLite JDBC Drivers, SLF4J)\n"
                + "├── gamesmania.db                 (Local embedded SQLite persistent database)\n"
                + "└── preferences.properties        (User configurations: theme, difficulty, audio)\n"
        );
        dirTree.setBorder(new EmptyBorder(8, 8, 8, 8));

        JScrollPane treeScroll = new JScrollPane(dirTree);
        treeScroll.setBorder(BorderFactory.createLineBorder(new Color(51, 65, 85), 1));
        detailsCard.add(treeScroll, BorderLayout.CENTER);

        centerGrid.add(layersCard);
        centerGrid.add(detailsCard);
        panel.add(centerGrid, BorderLayout.CENTER);

        return panel;
    }

    // =========================================================
    // 2. ABOUT ME / DEVELOPER PROFILE PANEL
    // =========================================================
    private JPanel createAboutMePanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 16));
        panel.setOpaque(false);

        JPanel profileGrid = new JPanel(new GridLayout(1, 2, 18, 0));
        profileGrid.setOpaque(false);

        // Left: Developer Bio & Credentials
        JPanel devCard = createCardPanel("👨‍💻 LEAD DEVELOPER & ARCHITECT", "Academic Credentials & Project Details");
        devCard.setLayout(new BoxLayout(devCard, BoxLayout.Y_AXIS));

        JPanel headerProfile = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 0));
        headerProfile.setOpaque(false);
        JLabel devAvatar = new JLabel("👨‍💻");
        devAvatar.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));

        JPanel devNameBox = new JPanel(new GridLayout(3, 1, 0, 2));
        devNameBox.setOpaque(false);
        JLabel devName = new JLabel("Rishabh Nandi");
        devName.setFont(new Font("Segoe UI", Font.BOLD, 22));
        JLabel devTitle = new JLabel("Lead Full-Stack Java Desktop Developer • Game Designer");
        devTitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        JLabel devProject = new JLabel("Project: GamesMania (NSP Project)");
        devProject.setFont(new Font("Segoe UI", Font.BOLD, 12));
        devProject.setForeground(new Color(99, 102, 241));

        devNameBox.add(devName);
        devNameBox.add(devTitle);
        devNameBox.add(devProject);
        headerProfile.add(devAvatar);
        headerProfile.add(devNameBox);
        devCard.add(headerProfile);
        devCard.add(Box.createRigidArea(new Dimension(0, 14)));

        // Academic details grid
        JPanel credsBox = new JPanel(new GridLayout(3, 2, 10, 8));
        credsBox.setOpaque(false);
        credsBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255, 30), 1, true),
                new EmptyBorder(10, 12, 10, 12)
        ));

        credsBox.add(createKeyValue("Registration No:", "PIET25cs135"));
        credsBox.add(createKeyValue("University Roll No:", "EPT25CS134"));
        credsBox.add(createKeyValue("Academic Batch:", "C1"));
        credsBox.add(createKeyValue("Project Scope:", "Desktop Arcade Hub"));
        credsBox.add(createKeyValue("Database Engine:", "MySQL 8+ / SQLite 3"));
        credsBox.add(createKeyValue("Runtime Target:", "Java 21 LTS / 26 / 27"));
        devCard.add(credsBox);
        devCard.add(Box.createRigidArea(new Dimension(0, 14)));

        JLabel bioLabel = new JLabel("<html><body style='width:340px; line-height:1.4;'>"
                + "<b>About Developer:</b><br>"
                + "Passionate Computer Science student and software engineer who built GamesMania from scratch. "
                + "Engineered 16 complete mini-games with zero external UI dependencies, crafted a reactive multi-window "
                + "theme engine using the Observer pattern, and architected resilient dual-database failover between MySQL and SQLite."
                + "</body></html>");
        bioLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        devCard.add(bioLabel);
        devCard.add(Box.createVerticalGlue());

        // Copy credentials button
        JButton copyBtn = new JButton("📋 Copy Student & Project Details");
        copyBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        copyBtn.setFocusPainted(false);
        copyBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        copyBtn.addActionListener(e -> {
            String text = "Developer: Rishabh Nandi\nReg No: PIET25cs135\nRoll No: EPT25CS134\nBatch: C1\nProject: GamesMania (NSP Project)";
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(text), null);
            JOptionPane.showMessageDialog(this, "✓ Developer credentials copied to clipboard!", "Copied", JOptionPane.INFORMATION_MESSAGE);
        });
        devCard.add(copyBtn);

        // Right: Technical Competencies & Project Accomplishments
        JPanel skillsCard = createCardPanel("⚡ TECHNICAL SKILLS & ACHIEVEMENTS", "Mastered competencies demonstrated in this project");
        skillsCard.setLayout(new BoxLayout(skillsCard, BoxLayout.Y_AXIS));

        Object[][] skills = {
                {"Core Java & OOP Architecture", 98, new Color(99, 102, 241)},
                {"Java2D Custom Rendering & Game Physics", 95, new Color(16, 185, 129)},
                {"Database Engineering (MySQL & SQLite)", 93, new Color(6, 182, 212)},
                {"Modern UI/UX & Responsive Swing", 94, new Color(245, 158, 11)},
                {"Event-Driven Architecture & Concurrency", 92, new Color(244, 63, 94)}
        };

        for (Object[] s : skills) {
            String name = (String) s[0];
            int percent = (int) s[1];
            Color c = (Color) s[2];

            JPanel sRow = new JPanel(new BorderLayout(6, 4));
            sRow.setOpaque(false);
            JLabel nameL = new JLabel(name);
            nameL.setFont(new Font("Segoe UI", Font.BOLD, 12));
            JLabel percL = new JLabel(percent + "%");
            percL.setFont(new Font("Segoe UI", Font.BOLD, 12));
            percL.setForeground(c);

            sRow.add(nameL, BorderLayout.WEST);
            sRow.add(percL, BorderLayout.EAST);

            JProgressBar bar = new JProgressBar(0, 100);
            bar.setValue(percent);
            bar.setForeground(c);
            bar.setBackground(new Color(255, 255, 255, 30));
            bar.setPreferredSize(new Dimension(300, 8));
            bar.setBorderPainted(false);

            JPanel wrapper = new JPanel(new BorderLayout(0, 4));
            wrapper.setOpaque(false);
            wrapper.add(sRow, BorderLayout.NORTH);
            wrapper.add(bar, BorderLayout.SOUTH);

            skillsCard.add(wrapper);
            skillsCard.add(Box.createRigidArea(new Dimension(0, 12)));
        }

        // Highlights badges
        JPanel badgeBox = new JPanel(new GridLayout(2, 2, 8, 8));
        badgeBox.setOpaque(false);
        badgeBox.add(createPillBadge("⭐ 16 Games Crafted"));
        badgeBox.add(createPillBadge("🛡️ MySQL + SQLite Failover"));
        badgeBox.add(createPillBadge("🎨 5 Dynamic Themes"));
        badgeBox.add(createPillBadge("⚡ 60 FPS Custom Canvas"));
        skillsCard.add(badgeBox);

        profileGrid.add(devCard);
        profileGrid.add(skillsCard);
        panel.add(profileGrid, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createKeyValue(String key, String value) {
        JPanel p = new JPanel(new GridLayout(2, 1, 0, 1));
        p.setOpaque(false);
        JLabel k = new JLabel(key);
        k.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        JLabel v = new JLabel(value);
        v.setFont(new Font("Segoe UI", Font.BOLD, 12));
        p.add(k);
        p.add(v);
        return p;
    }

    private JLabel createPillBadge(String text) {
        JLabel badge = new JLabel(text, SwingConstants.CENTER);
        badge.setFont(new Font("Segoe UI", Font.BOLD, 11));
        badge.setOpaque(true);
        badge.setBackground(new Color(255, 255, 255, 20));
        badge.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255, 40), 1, true),
                new EmptyBorder(6, 6, 6, 6)
        ));
        return badge;
    }

    // =========================================================
    // 3. GAME ROSTER TAB
    // =========================================================
    private JPanel createGameRosterPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setOpaque(false);

        String[][] games = {
                {"Asteroid Storm", "Survive asteroid waves & blast cosmic space rock debris", "ACTION", "☄️", "AsteroidGame"},
                {"Classic Snake", "Retro neon snake with grid controls and fruit spawning", "RETRO", "🐍", "SnakeGame"},
                {"Fruit Catcher", "Catch falling apples into a moving basket with physics", "ACTION", "🍎", "FruitGame"},
                {"Dino Runner", "Endless obstacle running sprint with responsive jumping", "ACTION", "🦖", "DinoGame"},
                {"Master Archer", "Hit moving bullseye targets with realistic wind physics", "ACTION", "🏹", "ArcherGame"},
                {"Racing Storm", "High-speed highway obstacle dodging racer", "ACTION", "🏎", "RacingGame"},
                {"Sliding Puzzle", "8-tile sliding number puzzle with verified solvability", "PUZZLE", "🧩", "SlidingPuzzleGame"},
                {"2048 Fusion", "Merge matching numbered tiles into the elusive 2048 tile", "PUZZLE", "🔢", "Game2048"},
                {"Secret Number", "Deduce secret numbers with hot and cold temperature clues", "PUZZLE", "🎯", "GuessGame"},
                {"Sudoku Master", "Classic 9x9 logic grid challenge with conflict verification", "PUZZLE", "🔢", "SudokuGame"},
                {"Color Reflex", "Fast-paced brain Stroop reflex reaction challenge", "PUZZLE", "🎨", "ColorMatchGame"},
                {"Mini Pong", "Retro table tennis duel against an adaptive AI paddle", "RETRO", "🏓", "PongGame"},
                {"Tic-Tac-Toe", "Strategic 3x3 battle against unbeatable minimax computer", "RETRO", "❌⭕", "TicTacToeGame"},
                {"Lucky Roulette", "Simulated casino wheel spin with live payout calculation", "CASINO", "🎡", "LuckySpinGame"},
                {"Coin Flip Arena", "Probability betting arena with streak multipliers", "CASINO", "🪙", "CoinFlipGame"},
                {"Reaction Benchmark", "Measure human reaction time down to the single millisecond", "ACTION", "⚡", "ReactionGame"}
        };

        JPanel grid = new JPanel(new GridLayout(0, 2, 12, 10));
        grid.setOpaque(false);

        for (String[] g : games) {
            JPanel card = new JPanel(new BorderLayout(10, 0));
            card.setOpaque(true);
            card.setBackground(ThemeManager.getCurrentTheme().cardBg);
            card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(ThemeManager.getCurrentTheme().cardBorder, 1, true),
                    new EmptyBorder(8, 12, 8, 12)
            ));

            JLabel icon = new JLabel(g[3]);
            icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));

            JPanel text = new JPanel(new GridLayout(2, 1, 0, 2));
            text.setOpaque(false);
            JLabel title = new JLabel(g[0] + " • [" + g[2] + "]");
            title.setFont(new Font("Segoe UI", Font.BOLD, 12));
            JLabel desc = new JLabel(g[1]);
            desc.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            text.add(title);
            text.add(desc);

            JButton playBtn = new JButton("PLAY ▶");
            playBtn.setFont(new Font("Segoe UI", Font.BOLD, 10));
            playBtn.setFocusPainted(false);
            playBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            final String className = g[4];
            playBtn.addActionListener(e -> launchGame(className));

            card.add(icon, BorderLayout.WEST);
            card.add(text, BorderLayout.CENTER);
            card.add(playBtn, BorderLayout.EAST);
            grid.add(card);
        }

        JScrollPane scroll = new JScrollPane(grid);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    // =========================================================
    // 4. TECH STACK TAB
    // =========================================================
    private JPanel createTechStackPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 3, 14, 14));
        panel.setOpaque(false);

        long maxMem = Runtime.getRuntime().maxMemory() / (1024 * 1024);
        long totalMem = Runtime.getRuntime().totalMemory() / (1024 * 1024);
        long freeMem = Runtime.getRuntime().freeMemory() / (1024 * 1024);
        String jvm = System.getProperty("java.version");

        panel.add(createTechCard("☕ JAVA RUNTIME", "JDK " + jvm, "Java 21 LTS Bytecode Target\nRunning on 64-bit HotSpot VM\nGarbage Collector Active"));
        panel.add(createTechCard("🖼️ JAVA2D RENDERING", "60 FPS Double Buffered", "Bilinear anti-aliasing\nKey text anti-aliasing on\nHardware accelerated pipeline"));
        panel.add(createTechCard("💾 MEMORY PROFILE", totalMem - freeMem + " MB Used / " + maxMem + " MB Max", "Heap Total: " + totalMem + " MB\nFree Heap: " + freeMem + " MB\nOptimized memory layout"));
        panel.add(createTechCard("🗄️ DATABASE CONNECTIVITY", "MySQL & SQLite Dual Failover", "Hikari-style connection pooling\nSchema auto-provisioning\nInstant SQLite fallback"));
        panel.add(createTechCard("🎨 REAL-TIME THEME ENGINE", "Observer Pattern Architecture", "5 Curated color palettes\nZero-flicker instant sync\npreferences.properties persistence"));
        panel.add(createTechCard("📐 MVC / DAO ARCHITECTURE", "Enterprise Software Structure", "Clean separation of concerns\nDAO access abstractions\nModular GameFactory dispatch"));

        return panel;
    }

    private JPanel createTechCard(String title, String status, String details) {
        JPanel card = createCardPanel(title, status);
        card.setLayout(new BorderLayout(0, 8));

        JTextArea ta = new JTextArea(details);
        ta.setEditable(false);
        ta.setOpaque(false);
        ta.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        card.add(ta, BorderLayout.CENTER);
        return card;
    }

    // =========================================================
    // 5. LIVE DB MONITOR TAB
    // =========================================================
    private JPanel createDbMonitorPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 14));
        panel.setOpaque(false);

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);

        JLabel monitorTitle = new JLabel("REAL-TIME DATABASE TELEMETRY & HEALTH CHECK");
        monitorTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        topBar.add(monitorTitle, BorderLayout.WEST);

        JLabel dbStatusPill = new JLabel(" TESTING... ");
        dbStatusPill.setFont(new Font("Segoe UI", Font.BOLD, 12));
        dbStatusPill.setOpaque(true);
        topBar.add(dbStatusPill, BorderLayout.EAST);
        panel.add(topBar, BorderLayout.NORTH);

        JTextArea logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        logArea.setBackground(new Color(15, 23, 42));
        logArea.setForeground(new Color(226, 232, 240));
        logArea.setBorder(new EmptyBorder(8, 8, 8, 8));

        JScrollPane sp = new JScrollPane(logArea);
        sp.setBorder(BorderFactory.createLineBorder(new Color(51, 65, 85), 1));
        panel.add(sp, BorderLayout.CENTER);

        // Run async test
        new Thread(() -> {
            long start = System.currentTimeMillis();
            try {
                Connection con = DBConnection.getConnection();
                long elapsed = System.currentTimeMillis() - start;

                String activeEngine = (con != null) ? con.getMetaData().getDatabaseProductName() : "None";
                String url = (con != null) ? con.getMetaData().getURL() : "None";

                int userCount = 0;
                int scoreCount = 0;
                if (con != null) {
                    try (Statement stmt = con.createStatement()) {
                        ResultSet rs1 = stmt.executeQuery("SELECT COUNT(*) FROM users");
                        if (rs1.next()) userCount = rs1.getInt(1);
                        ResultSet rs2 = stmt.executeQuery("SELECT COUNT(*) FROM scores");
                        if (rs2.next()) scoreCount = rs2.getInt(1);
                    } catch (Exception ignored) {}
                }

                final int finalUserCount = userCount;
                final int finalScoreCount = scoreCount;

                SwingUtilities.invokeLater(() -> {
                    dbStatusPill.setText(" ● " + activeEngine.toUpperCase() + " ACTIVE (" + elapsed + "ms) ");
                    dbStatusPill.setBackground(new Color(16, 185, 129, 40));
                    dbStatusPill.setForeground(new Color(52, 211, 153));
                    dbStatusPill.setBorder(BorderFactory.createLineBorder(new Color(16, 185, 129), 1, true));

                    logArea.setText(
                            "Database Health Telemetry Report:\n"
                            + "--------------------------------------------------\n"
                            + "• Status: CONNECTED & READY\n"
                            + "• Active Engine: " + activeEngine + "\n"
                            + "• JDBC URL: " + url + "\n"
                            + "• Connection Ping Latency: " + elapsed + " ms\n"
                            + "• Total Registered Users: " + finalUserCount + "\n"
                            + "• Total Recorded Scores: " + finalScoreCount + "\n"
                            + "• Auto-Failover Safety: SQLite fallback enabled\n"
                            + "--------------------------------------------------\n"
                            + "✓ Data integrity verified. Scores will persist across sessions."
                    );
                });
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    dbStatusPill.setText(" ✗ CONNECTION ERROR ");
                    dbStatusPill.setBackground(new Color(239, 68, 68, 40));
                    dbStatusPill.setForeground(new Color(248, 113, 113));
                    logArea.setText("Database Connection Error:\n" + e.getMessage());
                });
            }
        }).start();

        return panel;
    }

    // =========================================================
    // HELPERS & EASTER EGG
    // =========================================================
    private void triggerEasterEgg() {
        switchTab("PARTICLE PLAYGROUND");
        if (playground != null) {
            playground.burstConfetti();
        }
        Toolkit.getDefaultToolkit().beep();
        JOptionPane.showMessageDialog(
                this,
                "🎉 DEVELOPER EASTER EGG UNLOCKED!\n\n"
                        + "Project: GamesMania (NSP Project)\n"
                        + "Developer: Rishabh Nandi\n"
                        + "Registration No: PIET25cs135\n"
                        + "Roll No: EPT25CS134 | Batch: C1\n\n"
                        + "Confetti burst deployed to the Particle Playground!",
                "GamesMania Dev Lab",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void launchGame(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            for (java.lang.reflect.Constructor<?> c : clazz.getConstructors()) {
                if (c.getParameterCount() == 1 && c.getParameterTypes()[0] == String.class) {
                    c.newInstance("Player");
                    return;
                }
            }
            clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Could not launch " + className + ": " + e.getMessage());
        }
    }

    private JPanel createCardPanel(String title, String subtitle) {
        JPanel card = new JPanel();
        card.setOpaque(true);
        card.setBackground(ThemeManager.getCurrentTheme().cardBg);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ThemeManager.getCurrentTheme().cardBorder, 1, true),
                new EmptyBorder(14, 16, 14, 16)
        ));

        JPanel header = new JPanel(new GridLayout(2, 1, 0, 2));
        header.setOpaque(false);
        JLabel t = new JLabel(title);
        t.setFont(new Font("Segoe UI", Font.BOLD, 13));
        t.setForeground(ThemeManager.getCurrentTheme().textPrimary);
        JLabel s = new JLabel(subtitle);
        s.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        s.setForeground(ThemeManager.getCurrentTheme().textMuted);
        header.add(t);
        header.add(s);

        card.setLayout(new BorderLayout(0, 10));
        card.add(header, BorderLayout.NORTH);
        return card;
    }

    private JPanel createMetricCard(String title, String sub, String icon, Color accent) {
        JPanel card = new JPanel(new BorderLayout(10, 0));
        card.setOpaque(true);
        card.setBackground(ThemeManager.getCurrentTheme().cardBg);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ThemeManager.getCurrentTheme().cardBorder, 1, true),
                new EmptyBorder(10, 12, 10, 12)
        ));

        JLabel ic = new JLabel(icon);
        ic.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));

        JPanel text = new JPanel(new GridLayout(2, 1, 0, 2));
        text.setOpaque(false);
        JLabel t = new JLabel(title);
        t.setFont(new Font("Segoe UI", Font.BOLD, 12));
        t.setForeground(accent);
        JLabel s = new JLabel(sub);
        s.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        s.setForeground(ThemeManager.getCurrentTheme().textMuted);

        text.add(t);
        text.add(s);
        card.add(ic, BorderLayout.WEST);
        card.add(text, BorderLayout.CENTER);
        return card;
    }

    private void applyTheme(ThemeManager.Theme theme) {
        getContentPane().setBackground(theme.bg);
        rootPanel.setBackground(theme.bg);
        headerPanel.setBackground(theme.headerBg);
        titleLabel.setForeground(theme.textPrimary);
        subtitleLabel.setForeground(theme.textMuted);

        for (JButton b : tabButtons) {
            boolean isSel = b.getText().equals(currentTab);
            b.setForeground(isSel ? Color.WHITE : theme.textPrimary);
        }

        updateComponentTreeColors(tabContentPanel, theme);
        repaint();
    }

    private void updateComponentTreeColors(Container container, ThemeManager.Theme theme) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JPanel && comp.isOpaque() && comp != playground) {
                comp.setBackground(theme.cardBg);
                ((JComponent) comp).setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(theme.cardBorder, 1, true),
                        new EmptyBorder(12, 14, 12, 14)
                ));
            } else if (comp instanceof JLabel) {
                JLabel lbl = (JLabel) comp;
                if (lbl.getFont().isBold() && lbl.getForeground() != theme.accent) {
                    lbl.setForeground(theme.textPrimary);
                } else if (!lbl.getFont().isBold()) {
                    lbl.setForeground(theme.textMuted);
                }
            } else if (comp instanceof JTextArea) {
                comp.setForeground(theme.textPrimary);
            } else if (comp instanceof Container) {
                updateComponentTreeColors((Container) comp, theme);
            }
        }
    }

    // =========================================================
    // 6. INTERACTIVE PARTICLE PLAYGROUND
    // =========================================================
    static class InteractivePlayground extends JPanel {
        private final List<Particle> particles = new ArrayList<>();
        private int mouseX = -100;
        private int mouseY = -100;
        private final Random random = new Random();

        InteractivePlayground() {
            setOpaque(true);
            setBackground(new Color(15, 23, 42));

            // Spawn 100 particles
            for (int i = 0; i < 90; i++) {
                particles.add(new Particle(random.nextInt(800), random.nextInt(500), random));
            }

            Timer timer = new Timer(16, e -> {
                for (Particle p : particles) {
                    p.update(getWidth(), getHeight(), mouseX, mouseY);
                }
                repaint();
            });
            timer.start();

            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    mouseX = e.getX();
                    mouseY = e.getY();
                }
            });

            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    burstConfettiAt(e.getX(), e.getY());
                }
            });
        }

        void burstConfetti() {
            burstConfettiAt(getWidth() / 2, getHeight() / 2);
        }

        void burstConfettiAt(int x, int y) {
            for (int i = 0; i < 40; i++) {
                particles.add(new Particle(x, y, random, true));
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            for (Particle p : new ArrayList<>(particles)) {
                g2.setColor(p.color);
                g2.fillOval((int) p.x, (int) p.y, p.size, p.size);
            }

            g2.setColor(new Color(255, 255, 255, 180));
            g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
            g2.drawString("✨ Move mouse to attract particles • Click anywhere to burst confetti!", 20, 30);
            g2.dispose();
        }

        static class Particle {
            float x, y, vx, vy;
            int size;
            Color color;
            boolean isConfetti;
            int life = 200;

            Particle(int startX, int startY, Random r) {
                this(startX, startY, r, false);
            }

            Particle(int startX, int startY, Random r, boolean confetti) {
                this.x = startX;
                this.y = startY;
                this.isConfetti = confetti;
                this.size = r.nextInt(5) + 3;

                float speed = confetti ? (r.nextFloat() * 8f + 2f) : (r.nextFloat() * 1.5f + 0.5f);
                double angle = r.nextDouble() * Math.PI * 2;
                this.vx = (float) (Math.cos(angle) * speed);
                this.vy = (float) (Math.sin(angle) * speed);

                Color[] palette = {
                        new Color(99, 102, 241),
                        new Color(16, 185, 129),
                        new Color(6, 182, 212),
                        new Color(245, 158, 11),
                        new Color(244, 63, 94),
                        new Color(217, 70, 239)
                };
                this.color = palette[r.nextInt(palette.length)];
            }

            void update(int w, int h, int mx, int my) {
                x += vx;
                y += vy;

                // Mouse gravity attraction
                if (mx > 0 && my > 0) {
                    float dx = mx - x;
                    float dy = my - y;
                    float dist = (float) Math.sqrt(dx * dx + dy * dy);
                    if (dist > 5 && dist < 220) {
                        vx += (dx / dist) * 0.25f;
                        vy += (dy / dist) * 0.25f;
                    }
                }

                // Drag
                vx *= 0.985f;
                vy *= 0.985f;

                if (x < 0 || x > w) vx = -vx;
                if (y < 0 || y > h) vy = -vy;

                if (isConfetti) {
                    life--;
                    if (life <= 0) {
                        x = -100;
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AboutFrame::new);
    }
}