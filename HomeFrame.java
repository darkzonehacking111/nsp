import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Constructor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * ============================================================
 *                 GAMESMANIA • ARCADE VAULT
 * ============================================================
 *
 * Standout, unique, highly interactive Games Dashboard for GamesMania.
 * Key Upgrades:
 *  - 100% VERTICAL SCROLL: All games scroll down strictly, ZERO horizontal scrollbar!
 *  - WORKING PLAYER PROFILE ("hg"): Interactive profile pill with career stats,
 *    win rates, personal high scores, and live alias changer.
 *  - TOP GAMES SPOTLIGHT SHELF: 4 Flagship Arcade Games (#1-#4) featured prominently
 *  - ALL ARCADE VAULT GAMES: All remaining games arranged cleanly below in wrapped grid
 *  - 60 FPS MULTI-LAYER BACKGROUND ANIMATION:
 *      * Floating ambient plasma orbs (soft luminous pastels in light theme)
 *      * Dynamic flowing harmonic sine wave lines
 *      * Rising stardust particles with gentle oscillation
 *      * Interactive mouse proximity aura trailing the cursor
 *  - LIVE ARCADE TICKER MARQUEE: Continuous smooth ticker with game tips & records
 *  - INTERACTIVE SURPRISE SHUFFLE MODAL: Slot-machine animation before launching game
 *  - NO BROKEN TOFU GLYPHS: All navigation buttons use crisp universal icons
 * ============================================================
 */
public class HomeFrame extends JFrame {

    private Object user;
    private final List<GameEntry> allGames = new ArrayList<>();
    private final List<GameEntry> topGames = new ArrayList<>();
    private final List<GameCard> activeCards = new ArrayList<>();

    // UI Panels
    private ArcadeBackgroundPanel backgroundPanel;
    private JPanel headerPanel;
    private ArcadeTickerPanel tickerPanel;
    private JPanel heroPanel;
    private ScrollablePanel gamesContainerPanel;
    private JScrollPane scrollPane;
    private JPanel footerPanel;

    // Interactive Controls
    private JTextField searchField;
    private JButton clearSearchBtn;
    private JLabel countLabel;
    private JLabel dbStatusLabel;
    private JButton themeQuickToggle;
    private JComboBox<String> sortBox;
    private JButton gridViewBtn;
    private JButton sectionViewBtn;
    private JButton playerPillBtn;
    private JLabel playerNameLbl;

    private String selectedCategory = "ALL";
    private String searchQuery = "";
    private boolean isSectionedView = false;
    private String currentSort = "Featured";

    public static class GameEntry {
        public final String name;
        public final String description;
        public final String className;
        public final String icon;
        public final String category;
        public final String difficulty;
        public final Color accent;
        public final boolean isTop;
        public final String topRank;

        public GameEntry(String name, String description, String className, String icon,
                         String category, String difficulty, Color accent, boolean isTop, String topRank) {
            this.name = name;
            this.description = description;
            this.className = className;
            this.icon = icon;
            this.category = category;
            this.difficulty = difficulty;
            this.accent = accent;
            this.isTop = isTop;
            this.topRank = topRank;
        }
    }

    public HomeFrame() {
        this(null);
    }

    public HomeFrame(Object user) {
        this.user = user;
        initGameRegistry();
        initializeUI();

        // Subscribe to global theme updates
        applyTheme(ThemeManager.getCurrentTheme());
        ThemeManager.addListener(this::applyTheme);
    }

    private void initGameRegistry() {
        // Top 3 Flagship Showcase Games (Just Top 3 Games)
        topGames.add(new GameEntry("2048 Fusion", "Merge matching numbered tiles into the elusive 2048 tile", "Game2048", "🔢", "PUZZLE", "Medium", new Color(234, 179, 8), true, "#1 TOP"));
        topGames.add(new GameEntry("Asteroids Storm", "Survive asteroid waves & blast cosmic space rock debris", "AsteroidGame", "☄️", "ACTION", "Hard", new Color(99, 102, 241), true, "#2 TOP"));
        topGames.add(new GameEntry("Classic Snake", "Retro neon snake with grid controls and fruit spawning", "SnakeGame", "🐍", "RETRO", "Medium", new Color(16, 185, 129), true, "#3 TOP"));

        // All 16 Games in the Vault
        allGames.addAll(topGames);
        allGames.add(new GameEntry("Dino Runner", "Endless obstacle running sprint with responsive jumping", "DinoGame", "🦖", "ACTION", "Medium", new Color(20, 184, 166), false, ""));
        allGames.add(new GameEntry("Fruit Catcher", "Catch falling apples into the moving basket with physics", "FruitGame", "🍎", "ACTION", "Easy", new Color(244, 63, 94), false, ""));
        allGames.add(new GameEntry("Master Archer", "Hit moving bullseye targets with realistic wind physics", "ArcherGame", "🏹", "ACTION", "Hard", new Color(239, 68, 68), false, ""));
        allGames.add(new GameEntry("Racing Storm", "High-speed highway obstacle dodging racer", "RacingGame", "🏎", "ACTION", "Hard", new Color(249, 115, 22), false, ""));
        allGames.add(new GameEntry("Sliding Puzzle", "8-tile sliding number puzzle with verified solvability", "SlidingPuzzleGame", "🧩", "PUZZLE", "Medium", new Color(6, 182, 212), false, ""));
        allGames.add(new GameEntry("Secret Number", "Deduce secret numbers with hot and cold temperature clues", "GuessGame", "🎯", "PUZZLE", "Easy", new Color(168, 85, 247), false, ""));
        allGames.add(new GameEntry("Sudoku Master", "Classic 9x9 logic grid challenge with conflict verification", "SudokuGame", "🔢", "PUZZLE", "Hard", new Color(132, 204, 22), false, ""));
        allGames.add(new GameEntry("Color Reflex", "Fast-paced brain Stroop reflex reaction challenge", "ColorMatchGame", "🎨", "PUZZLE", "Medium", new Color(236, 72, 153), false, ""));
        allGames.add(new GameEntry("Mini Pong", "Retro table tennis duel against an adaptive AI paddle", "PongGame", "🏓", "RETRO", "Medium", new Color(59, 130, 246), false, ""));
        allGames.add(new GameEntry("Tic-Tac-Toe", "Strategic 3x3 battle against unbeatable minimax computer", "TicTacToeGame", "❌⭕", "RETRO", "Easy", new Color(217, 70, 239), false, ""));
        allGames.add(new GameEntry("Lucky Roulette", "Simulated casino wheel spin with live payout calculation", "LuckySpinGame", "🎡", "CASINO", "Easy", new Color(245, 158, 11), false, ""));
        allGames.add(new GameEntry("Coin Flip Arena", "Probability betting arena with streak multipliers", "CoinFlipGame", "🪙", "CASINO", "Easy", new Color(251, 191, 36), false, ""));
        allGames.add(new GameEntry("Reaction Benchmark", "Measure human reaction time down to the millisecond", "ReactionGame", "⚡", "ACTION", "Medium", new Color(250, 204, 21), false, ""));
    }

    private void initializeUI() {
        setTitle("GamesMania • Arcade Vault");
        setSize(1280, 840);
        setMinimumSize(new Dimension(1080, 720));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 60 FPS Ambient Background Panel with mouse interaction & wave lines
        backgroundPanel = new ArcadeBackgroundPanel();

        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setOpaque(false);

        // 1. TOP HEADER & TICKER
        JPanel topContainer = new JPanel(new BorderLayout(0, 0));
        topContainer.setOpaque(false);

        headerPanel = createHeader();
        tickerPanel = new ArcadeTickerPanel();

        topContainer.add(headerPanel, BorderLayout.NORTH);
        topContainer.add(tickerPanel, BorderLayout.SOUTH);
        root.add(topContainer, BorderLayout.NORTH);

        // 2. HERO / SEARCH & FILTER SECTION
        JPanel centerWrapper = new JPanel(new BorderLayout(0, 10));
        centerWrapper.setOpaque(false);
        centerWrapper.setBorder(new EmptyBorder(12, 28, 8, 28));

        heroPanel = createHeroAndFilterBar();
        centerWrapper.add(heroPanel, BorderLayout.NORTH);

        // 3. GAME CONTAINER (SCROLLABLE - STRICTLY VERTICAL SCROLL DOWN)
        gamesContainerPanel = new ScrollablePanel();
        gamesContainerPanel.setLayout(new BoxLayout(gamesContainerPanel, BoxLayout.Y_AXIS));

        scrollPane = new JScrollPane(gamesContainerPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(26);

        centerWrapper.add(scrollPane, BorderLayout.CENTER);
        root.add(centerWrapper, BorderLayout.CENTER);

        // 4. FOOTER STATUS BAR
        footerPanel = createFooter();
        root.add(footerPanel, BorderLayout.SOUTH);

        backgroundPanel.add(root, BorderLayout.CENTER);
        setContentPane(backgroundPanel);

        // Initial render & live theme/score listeners
        ThemeManager.addListener(this::applyTheme);
        ScoreDAO.addScoreListener((u, g, s) -> {
            SwingUtilities.invokeLater(() -> {
                if (playerNameLbl != null) {
                    playerNameLbl.setText(getUserName());
                }
                if (tickerPanel != null) {
                    tickerPanel.repaint();
                }
            });
        });
        renderGames();
        setVisible(true);
    }

    // =========================================================
    // TOP HEADER
    // =========================================================
    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                ThemeManager.Theme theme = ThemeManager.getCurrentTheme();
                g2.setColor(theme.headerBg);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(theme.cardBorder);
                g2.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
                g2.dispose();
            }
        };
        header.setBorder(new EmptyBorder(12, 28, 12, 28));

        // Left Branding (Compact & Bold, Never Overlaps!)
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        left.setOpaque(false);

        JLabel logoIcon = new JLabel("🎮");
        logoIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));

        JPanel brandText = new JPanel(new GridLayout(2, 1, 0, 1));
        brandText.setOpaque(false);
        JLabel brandTitle = new JLabel("GAMESMANIA");
        brandTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        brandTitle.setForeground(ThemeManager.getCurrentTheme().textPrimary);

        JLabel brandBadge = new JLabel("✦ VINTAGE VAULT • 16 CABINETS");
        brandBadge.setFont(new Font("Segoe UI", Font.BOLD, 10));
        brandBadge.setForeground(ThemeManager.getCurrentTheme().accent);

        brandText.add(brandTitle);
        brandText.add(brandBadge);
        left.add(logoIcon);
        left.add(brandText);
        header.add(left, BorderLayout.WEST);

        // Right Navigation & User Badge (Compact, Clean Spacing)
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        right.setOpaque(false);

        // =========================================================
        // WORKING PLAYER PROFILE PILL ("hg")
        // =========================================================
        playerPillBtn = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                ThemeManager.Theme theme = ThemeManager.getCurrentTheme();

                Color bg = getModel().isRollover()
                        ? new Color(theme.accent.getRed(), theme.accent.getGreen(), theme.accent.getBlue(), 35)
                        : theme.cardBg;
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);

                Color border = getModel().isRollover() ? theme.accent : theme.cardBorder;
                g2.setColor(border);
                g2.setStroke(new BasicStroke(getModel().isRollover() ? 1.5f : 1.0f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        playerPillBtn.setLayout(new FlowLayout(FlowLayout.LEFT, 6, 3));
        playerPillBtn.setOpaque(false);
        playerPillBtn.setContentAreaFilled(false);
        playerPillBtn.setBorderPainted(false);
        playerPillBtn.setFocusPainted(false);
        playerPillBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        playerPillBtn.setPreferredSize(new Dimension(115, 32));
        playerPillBtn.setToolTipText("Click to view Career Stats, Scores & change Alias");

        JLabel avatar = new JLabel("👾");
        avatar.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));

        playerNameLbl = new JLabel(getUserName());
        playerNameLbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        playerNameLbl.setForeground(ThemeManager.getCurrentTheme().textPrimary);

        JLabel proBadge = new JLabel(" PRO ");
        proBadge.setFont(new Font("Segoe UI", Font.BOLD, 9));
        proBadge.setOpaque(true);
        proBadge.setBackground(new Color(16, 185, 129, 35));
        proBadge.setForeground(new Color(16, 185, 129));
        proBadge.setBorder(BorderFactory.createLineBorder(new Color(16, 185, 129), 1, true));

        playerPillBtn.add(avatar);
        playerPillBtn.add(playerNameLbl);
        playerPillBtn.add(proBadge);
        playerPillBtn.addActionListener(e -> showPlayerProfileDialog());
        right.add(playerPillBtn);

        // Nav Action Buttons (Crisp, compact & proportional to prevent any overlap!)
        JButton surpriseBtn = createNavButton("✦ Surprise", new Color(245, 158, 11), e -> startSurpriseShuffleAnimation());
        JButton leaderBtn = createNavButton("★ Scores", new Color(16, 185, 129), e -> showLeaderboard());
        JButton settingsBtn = createNavButton("⚙ Settings", new Color(99, 102, 241), e -> openFrame("SettingFrame"));
        JButton aboutBtn = createNavButton("🏛 System", new Color(6, 182, 212), e -> openFrame("AboutFrame"));
        JButton logoutBtn = createNavButton("⏻ Exit", new Color(239, 68, 68), e -> logout());

        right.add(surpriseBtn);
        right.add(leaderBtn);
        right.add(settingsBtn);
        right.add(aboutBtn);
        right.add(logoutBtn);

        header.add(right, BorderLayout.EAST);
        return header;
    }

    private JButton createNavButton(String text, Color accent, ActionListener action) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                ThemeManager.Theme theme = ThemeManager.getCurrentTheme();
                if (getModel().isRollover()) {
                    g2.setColor(accent);
                    setForeground(Color.WHITE);
                } else {
                    g2.setColor(theme.cardBg);
                    setForeground(theme.textPrimary);
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(theme.cardBorder);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btn.setForeground(ThemeManager.getCurrentTheme().textPrimary);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(text.length() > 8 ? 96 : 76, 32));
        btn.addActionListener(action);
        return btn;
    }

    // =========================================================
    // HERO & FILTER BAR
    // =========================================================
    private JPanel createHeroAndFilterBar() {
        JPanel hero = new JPanel(new BorderLayout(0, 10));
        hero.setOpaque(false);

        // Top Row: Welcome Banner, Search, and View Controls
        JPanel topRow = new JPanel(new BorderLayout(16, 0));
        topRow.setOpaque(false);

        JPanel titleBox = new JPanel(new GridLayout(2, 1, 0, 2));
        titleBox.setOpaque(false);
        JLabel title = new JLabel("✨ ARCADE VAULT SPOTLIGHT");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        countLabel = new JLabel("Top Games Featured Below • 16 Complete Games Available");
        countLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        titleBox.add(title);
        titleBox.add(countLabel);
        topRow.add(titleBox, BorderLayout.WEST);

        // Search Box & View Mode Toggles
        JPanel searchAndControls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        searchAndControls.setOpaque(false);

        // Search Panel
        JPanel searchBoxPanel = new JPanel(new BorderLayout(6, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                ThemeManager.Theme theme = ThemeManager.getCurrentTheme();
                g2.setColor(theme.cardBg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(theme.cardBorder);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        searchBoxPanel.setOpaque(false);
        searchBoxPanel.setBorder(new EmptyBorder(4, 10, 4, 10));

        JLabel searchIcon = new JLabel("🔍");
        searchIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        searchBoxPanel.add(searchIcon, BorderLayout.WEST);

        searchField = new JTextField(16);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        searchField.setBorder(null);
        searchField.setOpaque(false);
        searchField.setToolTipText("Search games by title, genre or description");

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { onSearch(); }
            public void removeUpdate(DocumentEvent e) { onSearch(); }
            public void changedUpdate(DocumentEvent e) { onSearch(); }
            private void onSearch() {
                searchQuery = searchField.getText().trim().toLowerCase();
                clearSearchBtn.setVisible(!searchQuery.isEmpty());
                renderGames();
            }
        });
        searchBoxPanel.add(searchField, BorderLayout.CENTER);

        clearSearchBtn = new JButton("✕");
        clearSearchBtn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        clearSearchBtn.setBorder(null);
        clearSearchBtn.setContentAreaFilled(false);
        clearSearchBtn.setFocusPainted(false);
        clearSearchBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        clearSearchBtn.setVisible(false);
        clearSearchBtn.addActionListener(e -> {
            searchField.setText("");
            searchField.requestFocus();
        });
        searchBoxPanel.add(clearSearchBtn, BorderLayout.EAST);
        searchAndControls.add(searchBoxPanel);

        // Sort By ComboBox
        sortBox = new JComboBox<>(new String[]{"Featured", "Name (A-Z)", "Category"});
        sortBox.setFont(new Font("Segoe UI", Font.BOLD, 12));
        sortBox.setPreferredSize(new Dimension(130, 36));
        sortBox.addActionListener(e -> {
            currentSort = (String) sortBox.getSelectedItem();
            renderGames();
        });
        searchAndControls.add(sortBox);

        // View Mode Toggles: Grid vs Sectioned
        gridViewBtn = createViewModeButton("▦ Grid", true);
        sectionViewBtn = createViewModeButton("☰ Groups", false);

        gridViewBtn.addActionListener(e -> {
            isSectionedView = false;
            updateViewModeButtons();
            renderGames();
        });
        sectionViewBtn.addActionListener(e -> {
            isSectionedView = true;
            updateViewModeButtons();
            renderGames();
        });

        searchAndControls.add(gridViewBtn);
        searchAndControls.add(sectionViewBtn);

        topRow.add(searchAndControls, BorderLayout.EAST);
        hero.add(topRow, BorderLayout.NORTH);

        // Bottom Area: Category Chips + Pro Telemetry HUD Bar
        JPanel centerFilters = new JPanel(new GridLayout(2, 1, 0, 8));
        centerFilters.setOpaque(false);

        JPanel filterRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        filterRow.setOpaque(false);

        String[] categories = {"ALL", "PRO", "ACTION", "PUZZLE", "RETRO", "CASINO"};
        String[] labels = {"🌟 All (16)", "⚡ Top 3 Picks (3)", "🔥 Action (6)", "🧩 Puzzle (5)", "🕹 Retro (3)", "🎰 Casino (2)"};

        for (int i = 0; i < categories.length; i++) {
            final String cat = categories[i];
            final String lbl = labels[i];
            JButton chip = createCategoryChip(lbl, cat);
            filterRow.add(chip);
        }

        JPanel telemetryBar = createProTelemetryBar();
        centerFilters.add(filterRow);
        centerFilters.add(telemetryBar);

        hero.add(centerFilters, BorderLayout.SOUTH);
        return hero;
    }

    private JPanel createProTelemetryBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                ThemeManager.Theme theme = ThemeManager.getCurrentTheme();
                g2.setColor(new Color(theme.accent.getRed(), theme.accent.getGreen(), theme.accent.getBlue(), theme.isDark ? 22 : 14));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(new Color(theme.accent.getRed(), theme.accent.getGreen(), theme.accent.getBlue(), theme.isDark ? 65 : 40));
                g2.setStroke(new BasicStroke(1.0f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        bar.setOpaque(false);
        bar.setBorder(new EmptyBorder(5, 10, 5, 10));

        bar.add(createHudBadge("🕹 1980s VINTAGE ENGINE", new Color(245, 158, 11)));
        bar.add(createHudBadge("🪙 INSERT 1 COIN TO PLAY", new Color(16, 185, 129)));
        bar.add(createHudBadge("🏆 16 MASTER CABINETS", new Color(99, 102, 241)));
        bar.add(createHudBadge("⭐ ALL-TIME HI-SCORE: 99,990", new Color(6, 182, 212)));
        bar.add(createHudBadge("🔥 RETRO MARQUEE ACTIVE", new Color(239, 68, 68)));

        return bar;
    }

    private JLabel createHudBadge(String text, Color accent) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lbl.setForeground(accent);
        lbl.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 60), 1, true),
                new EmptyBorder(2, 6, 2, 6)
        ));
        return lbl;
    }

    private JButton createViewModeButton(String text, boolean initialActive) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                ThemeManager.Theme theme = ThemeManager.getCurrentTheme();

                boolean active = (text.contains("Grid") && !isSectionedView) || (text.contains("Groups") && isSectionedView);
                if (active) {
                    g2.setColor(theme.accent);
                } else {
                    g2.setColor(theme.cardBg);
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(active ? theme.accent : theme.cardBorder);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(85, 36));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void updateViewModeButtons() {
        ThemeManager.Theme theme = ThemeManager.getCurrentTheme();
        gridViewBtn.setForeground(!isSectionedView ? Color.WHITE : theme.textPrimary);
        sectionViewBtn.setForeground(isSectionedView ? Color.WHITE : theme.textPrimary);
        gridViewBtn.repaint();
        sectionViewBtn.repaint();
    }

    private JButton createCategoryChip(String label, String categoryKey) {
        JButton chip = new JButton(label) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                ThemeManager.Theme theme = ThemeManager.getCurrentTheme();

                boolean isSelected = categoryKey.equals(selectedCategory);
                if (isSelected) {
                    g2.setColor(theme.accent);
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(theme.accent.getRed(), theme.accent.getGreen(), theme.accent.getBlue(), 45));
                } else {
                    g2.setColor(theme.cardBg);
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.setColor(isSelected ? theme.accent : theme.cardBorder);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        chip.setFont(new Font("Segoe UI", Font.BOLD, 12));
        chip.setFocusPainted(false);
        chip.setContentAreaFilled(false);
        chip.setBorderPainted(false);
        chip.setCursor(new Cursor(Cursor.HAND_CURSOR));
        chip.setPreferredSize(new Dimension(categoryKey.equals("ALL") ? 88 : (categoryKey.equals("PRO") ? 130 : 138), 34));

        chip.addActionListener(e -> {
            selectedCategory = categoryKey;
            heroPanel.repaint();
            renderGames();
        });
        return chip;
    }

    // =========================================================
    // GAMES RENDERING: TOP GAMES + ALL OTHER GAMES (100% VERTICAL SCROLL)
    // =========================================================
    private void renderGames() {
        gamesContainerPanel.removeAll();
        activeCards.clear();

        List<GameEntry> filtered = new ArrayList<>();
        for (GameEntry entry : allGames) {
            boolean matchesCategory = "ALL".equals(selectedCategory)
                    || ("PRO".equals(selectedCategory) && entry.isTop)
                    || entry.category.equalsIgnoreCase(selectedCategory);
            boolean matchesSearch = searchQuery.isEmpty()
                    || entry.name.toLowerCase().contains(searchQuery)
                    || entry.description.toLowerCase().contains(searchQuery);

            if (matchesCategory && matchesSearch) {
                filtered.add(entry);
            }
        }

        // Apply Sorting
        if ("Name (A-Z)".equalsIgnoreCase(currentSort)) {
            filtered.sort(Comparator.comparing(a -> a.name));
        } else if ("Category".equalsIgnoreCase(currentSort)) {
            filtered.sort(Comparator.comparing(a -> a.category));
        }

        if (filtered.isEmpty()) {
            renderEmptyState();
            return;
        }

        // SHOW TOP GAMES SPOTLIGHT (Only if not searching and ALL category selected)
        if (searchQuery.isEmpty() && "ALL".equals(selectedCategory)) {
            JPanel topGamesSection = createTopGamesSection();
            gamesContainerPanel.add(topGamesSection);
            gamesContainerPanel.add(Box.createRigidArea(new Dimension(0, 16)));
        }

        // ALL GAMES SECTION HEADER
        JPanel allHeader = new JPanel(new BorderLayout(12, 0));
        allHeader.setOpaque(false);
        allHeader.setBorder(new EmptyBorder(10, 8, 8, 8));

        JPanel allTitleGroup = new JPanel(new GridLayout(2, 1, 0, 2));
        allTitleGroup.setOpaque(false);

        String titleString;
        if (!searchQuery.isEmpty()) {
            titleString = "🔍 SEARCH RESULTS (" + filtered.size() + " MATCHES)";
        } else if ("PRO".equals(selectedCategory)) {
            titleString = "⚡ VINTAGE TOP 3 SHOWCASE (3 Flagship Master Titles)";
        } else {
            titleString = "🎮 ALL ARCADE GAMES (The Complete Vault)";
        }
        JLabel allTitleL = new JLabel(titleString);
        allTitleL.setFont(new Font("Segoe UI", Font.BOLD, 17));
        allTitleL.setForeground(ThemeManager.getCurrentTheme().textPrimary);

        JLabel allDescL = new JLabel("Fast responsive gameplay • Scroll down to browse all 16 games");
        allDescL.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        allDescL.setForeground(ThemeManager.getCurrentTheme().textMuted);
        allTitleGroup.add(allTitleL);
        allTitleGroup.add(allDescL);

        JLabel allCountBadge = new JLabel(" " + filtered.size() + " GAMES READY ");
        allCountBadge.setFont(new Font("Segoe UI", Font.BOLD, 11));
        allCountBadge.setOpaque(true);
        allCountBadge.setBackground(new Color(99, 102, 241, 35));
        allCountBadge.setForeground(new Color(99, 102, 241));
        allCountBadge.setBorder(BorderFactory.createLineBorder(new Color(99, 102, 241), 1, true));

        allHeader.add(allTitleGroup, BorderLayout.WEST);
        allHeader.add(allCountBadge, BorderLayout.EAST);
        gamesContainerPanel.add(allHeader);

        // Render Cards: WrapLayout ensures ALL cards wrap vertically with ZERO horizontal scroll!
        if (isSectionedView && "ALL".equals(selectedCategory) && searchQuery.isEmpty()) {
            renderSectionedSubView(filtered);
        } else {
            JPanel grid = new JPanel(new WrapLayout(FlowLayout.LEFT, 16, 16));
            grid.setOpaque(false);
            for (GameEntry entry : filtered) {
                GameCard card = new GameCard(entry.name, entry.description, entry.icon, entry.difficulty, entry.accent, false, "");
                card.setClickAction(() -> launchGame(entry.className));
                grid.add(card);
                activeCards.add(card);
            }
            gamesContainerPanel.add(grid);
        }

        countLabel.setText("Showing " + filtered.size() + " of " + allGames.size() + " Games • All games scroll down smoothly");
        gamesContainerPanel.revalidate();
        gamesContainerPanel.repaint();
    }

    /**
     * TOP FEATURED GAMES SHELF (Flagship 4 Games Wrapped Vertically)
     */
    private JPanel createTopGamesSection() {
        JPanel section = new JPanel(new BorderLayout(0, 10));
        section.setOpaque(false);

        // Top Section Header Banner
        JPanel header = new JPanel(new BorderLayout(12, 0));
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(4, 8, 4, 8));

        JPanel titleBox = new JPanel(new GridLayout(2, 1, 0, 2));
        titleBox.setOpaque(false);
        JLabel titleL = new JLabel("🏆 TOP 3 VINTAGE ARCADE CHAMPIONSHIPS");
        titleL.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleL.setForeground(new Color(217, 119, 6));

        JLabel descL = new JLabel("Flagship titles • Highest community scores, reflex arcades & master logic");
        descL.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descL.setForeground(ThemeManager.getCurrentTheme().textMuted);
        titleBox.add(titleL);
        titleBox.add(descL);

        JLabel hotPill = new JLabel(" ★ TOP 3 PICKS ");
        hotPill.setFont(new Font("Segoe UI", Font.BOLD, 11));
        hotPill.setOpaque(true);
        hotPill.setBackground(new Color(217, 119, 6, 35));
        hotPill.setForeground(new Color(217, 119, 6));
        hotPill.setBorder(BorderFactory.createLineBorder(new Color(217, 119, 6), 1, true));

        header.add(titleBox, BorderLayout.WEST);
        header.add(hotPill, BorderLayout.EAST);
        section.add(header, BorderLayout.NORTH);

        // Top Games Showcase Cards wrapped cleanly with WrapLayout
        JPanel topShelf = new JPanel(new WrapLayout(FlowLayout.LEFT, 16, 12));
        topShelf.setOpaque(false);

        for (GameEntry tg : topGames) {
            GameCard card = new GameCard(tg.name, tg.description, tg.icon, tg.difficulty, tg.accent, true, tg.topRank);
            card.setClickAction(() -> launchGame(tg.className));
            topShelf.add(card);
            activeCards.add(card);
        }

        section.add(topShelf, BorderLayout.CENTER);
        return section;
    }

    private void renderSectionedSubView(List<GameEntry> list) {
        Object[][] sections = {
                {"ACTION", "🔥 Action & Reflex Arcades", "Fast-paced physics, dodge asteroids, master wind archery, and race", new Color(239, 68, 68)},
                {"PUZZLE", "🧩 Puzzle & Logic Challenges", "Brain-teasing math fusions, 2048 merges, deductive riddles, and 9x9 Sudoku", new Color(6, 182, 212)},
                {"RETRO", "🕹 Classic & Vintage Games", "Timeless retro neon Snake, competitive Pong AI, and classic Tic-Tac-Toe", new Color(16, 185, 129)},
                {"CASINO", "🎰 Casino & Probability Arenas", "Wheel of fortune roulette simulation and streak-multiplier coin flips", new Color(245, 158, 11)}
        };

        for (Object[] sec : sections) {
            String catKey = (String) sec[0];
            String secTitle = (String) sec[1];
            String secDesc = (String) sec[2];
            Color accent = (Color) sec[3];

            List<GameEntry> subList = new ArrayList<>();
            for (GameEntry ge : list) {
                if (ge.category.equalsIgnoreCase(catKey)) subList.add(ge);
            }

            if (subList.isEmpty()) continue;

            // Section Header Banner
            JPanel secHeader = new JPanel(new BorderLayout(12, 0));
            secHeader.setOpaque(false);
            secHeader.setBorder(new EmptyBorder(14, 8, 8, 8));

            JPanel titleGroup = new JPanel(new GridLayout(2, 1, 0, 2));
            titleGroup.setOpaque(false);
            JLabel titleL = new JLabel(secTitle);
            titleL.setFont(new Font("Segoe UI", Font.BOLD, 16));
            titleL.setForeground(ThemeManager.getCurrentTheme().textPrimary);
            JLabel descL = new JLabel(secDesc);
            descL.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            descL.setForeground(ThemeManager.getCurrentTheme().textMuted);
            titleGroup.add(titleL);
            titleGroup.add(descL);

            JLabel countPill = new JLabel(" " + subList.size() + " GAMES ");
            countPill.setFont(new Font("Segoe UI", Font.BOLD, 11));
            countPill.setOpaque(true);
            countPill.setBackground(new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 35));
            countPill.setForeground(accent);
            countPill.setBorder(BorderFactory.createLineBorder(accent, 1, true));

            secHeader.add(titleGroup, BorderLayout.WEST);
            secHeader.add(countPill, BorderLayout.EAST);
            gamesContainerPanel.add(secHeader);

            // Sub grid wrapped vertically with WrapLayout
            JPanel subGrid = new JPanel(new WrapLayout(FlowLayout.LEFT, 16, 12));
            subGrid.setOpaque(false);
            for (GameEntry entry : subList) {
                GameCard card = new GameCard(entry.name, entry.description, entry.icon, entry.difficulty, entry.accent, false, "");
                card.setClickAction(() -> launchGame(entry.className));
                subGrid.add(card);
                activeCards.add(card);
            }
            gamesContainerPanel.add(subGrid);
            gamesContainerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }
    }

    private void renderEmptyState() {
        gamesContainerPanel.setLayout(new BorderLayout());
        JPanel empty = new JPanel(new GridLayout(3, 1, 0, 8));
        empty.setOpaque(false);
        empty.setBorder(new EmptyBorder(80, 20, 80, 20));

        JLabel emptyTitle = new JLabel("No games match your search query: \"" + searchQuery + "\"", SwingConstants.CENTER);
        emptyTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        emptyTitle.setForeground(ThemeManager.getCurrentTheme().textPrimary);

        JLabel emptySub = new JLabel("Try searching with different keywords or click below to reset your filter.", SwingConstants.CENTER);
        emptySub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        emptySub.setForeground(ThemeManager.getCurrentTheme().textMuted);

        JPanel resetBox = new JPanel(new FlowLayout(FlowLayout.CENTER));
        resetBox.setOpaque(false);
        JButton resetBtn = new JButton("Reset Filters & View All");
        resetBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        resetBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        resetBtn.addActionListener(e -> {
            searchField.setText("");
            selectedCategory = "ALL";
            heroPanel.repaint();
            renderGames();
        });
        resetBox.add(resetBtn);

        empty.add(emptyTitle);
        empty.add(emptySub);
        empty.add(resetBox);
        gamesContainerPanel.add(empty, BorderLayout.CENTER);
    }

    // =========================================================
    // FOOTER STATUS BAR
    // =========================================================
    private JPanel createFooter() {
        JPanel footer = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                ThemeManager.Theme theme = ThemeManager.getCurrentTheme();
                g2.setColor(theme.headerBg);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(theme.cardBorder);
                g2.drawLine(0, 0, getWidth(), 0);
                g2.dispose();
            }
        };
        footer.setBorder(new EmptyBorder(10, 28, 10, 28));

        // Left Status Indicators
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 0));
        left.setOpaque(false);

        dbStatusLabel = new JLabel("● Database: Connected (Auto-Failover Ready)");
        dbStatusLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        dbStatusLabel.setForeground(new Color(16, 185, 129));

        JLabel engineLabel = new JLabel("⚡ Engine: 60 FPS Java2D Double Buffered");
        engineLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        engineLabel.setForeground(new Color(99, 102, 241));

        JLabel creatorLabel = new JLabel("👨‍💻 By Rishabh Nandi (PIET25cs135 • Batch C1)");
        creatorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        left.add(dbStatusLabel);
        left.add(engineLabel);
        left.add(creatorLabel);
        footer.add(left, BorderLayout.WEST);

        // Right Theme Quick Toggle
        themeQuickToggle = new JButton("🎨 Theme: " + ThemeManager.getCurrentTheme().name + " (Cycle)") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                ThemeManager.Theme theme = ThemeManager.getCurrentTheme();
                g2.setColor(theme.cardBg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(theme.cardBorder);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        themeQuickToggle.setFont(new Font("Segoe UI", Font.BOLD, 11));
        themeQuickToggle.setFocusPainted(false);
        themeQuickToggle.setContentAreaFilled(false);
        themeQuickToggle.setBorderPainted(false);
        themeQuickToggle.setCursor(new Cursor(Cursor.HAND_CURSOR));
        themeQuickToggle.setPreferredSize(new Dimension(240, 28));

        themeQuickToggle.addActionListener(e -> {
            String[] themes = ThemeManager.getThemeNames();
            String curr = ThemeManager.getCurrentTheme().name;
            int nextIdx = 0;
            for (int i = 0; i < themes.length; i++) {
                if (themes[i].equalsIgnoreCase(curr)) {
                    nextIdx = (i + 1) % themes.length;
                    break;
                }
            }
            ThemeManager.setTheme(themes[nextIdx]);
        });

        footer.add(themeQuickToggle, BorderLayout.EAST);
        return footer;
    }

    // =========================================================
    // THEME EVENT HANDLER
    // =========================================================
    private void applyTheme(ThemeManager.Theme theme) {
        if (searchField != null) {
            searchField.setForeground(theme.textPrimary);
            searchField.setCaretColor(theme.textPrimary);
        }
        if (themeQuickToggle != null) {
            themeQuickToggle.setText("🎨 Theme: " + theme.name + " (Cycle)");
            themeQuickToggle.setForeground(theme.textPrimary);
        }
        if (sortBox != null) {
            sortBox.setBackground(theme.cardBg);
            sortBox.setForeground(theme.textPrimary);
        }
        updateViewModeButtons();
        updateComponentTextColors(headerPanel, theme);
        updateComponentTextColors(heroPanel, theme);
        updateComponentTextColors(footerPanel, theme);
        if (tickerPanel != null) tickerPanel.applyTheme(theme);
        backgroundPanel.repaint();
        renderGames();
        repaint();
    }

    private void updateComponentTextColors(Container container, ThemeManager.Theme theme) {
        if (container == null) return;
        for (Component comp : container.getComponents()) {
            if (comp instanceof JLabel) {
                JLabel lbl = (JLabel) comp;
                if (lbl != dbStatusLabel && lbl.getForeground() != theme.accent) {
                    if (lbl.getFont().isBold()) {
                        lbl.setForeground(theme.textPrimary);
                    } else {
                        lbl.setForeground(theme.textMuted);
                    }
                }
            } else if (comp instanceof JButton) {
                JButton btn = (JButton) comp;
                if (!btn.getText().equals(selectedCategory) && btn != playerPillBtn) {
                    btn.setForeground(theme.textPrimary);
                }
            } else if (comp instanceof Container) {
                updateComponentTextColors((Container) comp, theme);
            }
        }
    }

    // =========================================================
    // INTERACTIVE PLAYER PROFILE MODAL ("hg" WORKING)
    // =========================================================
    private void showPlayerProfileDialog() {
        SettingFrame.playTestSound();
        String currentName = getUserName();

        JDialog dialog = new JDialog(this, "Player Profile • " + currentName, true);
        dialog.setSize(520, 560);
        dialog.setLocationRelativeTo(this);

        ThemeManager.Theme theme = ThemeManager.getCurrentTheme();

        JPanel root = new JPanel(new BorderLayout(0, 16));
        root.setBackground(theme.bg);
        root.setBorder(new EmptyBorder(22, 24, 22, 24));

        // 1. Header Profile Banner
        JPanel profileHeader = new JPanel(new BorderLayout(14, 0));
        profileHeader.setOpaque(false);

        JLabel avatarLbl = new JLabel("👾");
        avatarLbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 46));

        JPanel nameBox = new JPanel(new GridLayout(3, 1, 0, 2));
        nameBox.setOpaque(false);
        JLabel nameHeading = new JLabel(currentName);
        nameHeading.setFont(new Font("Segoe UI", Font.BOLD, 22));
        nameHeading.setForeground(theme.textPrimary);

        JLabel rankTag = new JLabel("RANK: ARCADE CHAMPION • LEVEL 1");
        rankTag.setFont(new Font("Segoe UI", Font.BOLD, 11));
        rankTag.setForeground(theme.accent);

        JLabel memberSince = new JLabel("Account Status: Active Player • Scores Saved");
        memberSince.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        memberSince.setForeground(theme.textMuted);

        nameBox.add(nameHeading);
        nameBox.add(rankTag);
        nameBox.add(memberSince);

        profileHeader.add(avatarLbl, BorderLayout.WEST);
        profileHeader.add(nameBox, BorderLayout.CENTER);
        root.add(profileHeader, BorderLayout.NORTH);

        // 2. Fetch User Stats from UserDAO / DB
        int totalScore = 0;
        int gamesPlayed = 0;
        int gamesWon = 0;
        double winRate = 0.0;

        try {
            UserDAO userDAO = new UserDAO();
            User u = userDAO.getUserByName(currentName);
            if (u != null) {
                totalScore = u.getTotalScore();
                gamesPlayed = u.getGamesPlayed();
                gamesWon = u.getGamesWon();
                winRate = u.getWinRate();
            }
        } catch (Exception ignored) {}

        // 3. Stats 4-Card Grid
        JPanel statsGrid = new JPanel(new GridLayout(2, 2, 12, 12));
        statsGrid.setOpaque(false);

        statsGrid.add(createProfileStatCard("🏆 TOTAL SCORE", String.valueOf(totalScore), new Color(245, 158, 11)));
        statsGrid.add(createProfileStatCard("🎮 GAMES PLAYED", String.valueOf(gamesPlayed), new Color(99, 102, 241)));
        statsGrid.add(createProfileStatCard("🥇 GAMES WON", String.valueOf(gamesWon), new Color(16, 185, 129)));
        statsGrid.add(createProfileStatCard("📈 WIN RATE", String.format("%.1f%%", winRate), new Color(6, 182, 212)));

        JPanel centerPanel = new JPanel(new BorderLayout(0, 14));
        centerPanel.setOpaque(false);
        centerPanel.add(statsGrid, BorderLayout.NORTH);

        // 4. Personal High Scores Box
        JPanel scoreBox = new JPanel(new BorderLayout(0, 8));
        scoreBox.setOpaque(true);
        scoreBox.setBackground(theme.cardBg);
        scoreBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(theme.cardBorder, 1, true),
                new EmptyBorder(12, 14, 12, 14)
        ));

        JLabel scoreTitle = new JLabel("⭐ YOUR HIGH SCORES BY GAME");
        scoreTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        scoreTitle.setForeground(theme.textPrimary);
        scoreBox.add(scoreTitle, BorderLayout.NORTH);

        JTextArea scoresArea = new JTextArea();
        scoresArea.setEditable(false);
        scoresArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        scoresArea.setBackground(theme.cardBg);
        scoresArea.setForeground(theme.textMuted);

        // Fetch top scores from ScoreDAO
        StringBuilder sb = new StringBuilder();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT game_name, MAX(score) as top_score FROM scores WHERE username = ? GROUP BY game_name ORDER BY top_score DESC LIMIT 6"
             )) {
            ps.setString(1, currentName);
            ResultSet rs = ps.executeQuery();
            int rank = 1;
            while (rs.next()) {
                sb.append(String.format(" #%d  %-22s : %d pts\n", rank++, rs.getString("game_name"), rs.getInt("top_score")));
            }
        } catch (Exception ignored) {}

        if (sb.length() == 0) {
            scoresArea.setText("No game scores recorded yet.\nPlay Asteroids, Snake, 2048, or Dino Runner to log your first high score!");
        } else {
            scoresArea.setText(sb.toString());
        }
        scoreBox.add(new JScrollPane(scoresArea), BorderLayout.CENTER);
        centerPanel.add(scoreBox, BorderLayout.CENTER);
        root.add(centerPanel, BorderLayout.CENTER);

        // 5. Action Buttons (Change Alias, Leaderboard, Close)
        JPanel btnBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnBar.setOpaque(false);

        JButton changeNameBtn = new JButton("✏ Change Alias");
        changeNameBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        changeNameBtn.setFocusPainted(false);
        changeNameBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        changeNameBtn.addActionListener(e -> {
            String newAlias = JOptionPane.showInputDialog(dialog, "Enter new player alias:", currentName);
            if (newAlias != null && !newAlias.trim().isEmpty()) {
                String trimmed = newAlias.trim();
                try {
                    UserDAO udao = new UserDAO();
                    User existing = udao.loginOrCreate(trimmed);
                    this.user = existing;
                } catch (Exception ignored) {
                    this.user = trimmed;
                }
                playerNameLbl.setText(trimmed);
                nameHeading.setText(trimmed);
                dialog.setTitle("Player Profile • " + trimmed);
                JOptionPane.showMessageDialog(dialog, "Player alias updated to: " + trimmed, "Alias Updated", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JButton closeBtn = new JButton("✕ Close");
        closeBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        closeBtn.setFocusPainted(false);
        closeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeBtn.addActionListener(e -> dialog.dispose());

        btnBar.add(changeNameBtn);
        btnBar.add(closeBtn);
        root.add(btnBar, BorderLayout.SOUTH);

        dialog.setContentPane(root);
        dialog.setVisible(true);
    }

    private JPanel createProfileStatCard(String label, String value, Color accent) {
        JPanel card = new JPanel(new GridLayout(2, 1, 0, 4));
        card.setOpaque(true);
        card.setBackground(ThemeManager.getCurrentTheme().cardBg);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ThemeManager.getCurrentTheme().cardBorder, 1, true),
                new EmptyBorder(10, 14, 10, 14)
        ));

        JLabel l = new JLabel(label);
        l.setFont(new Font("Segoe UI", Font.BOLD, 10));
        l.setForeground(ThemeManager.getCurrentTheme().textMuted);

        JLabel v = new JLabel(value);
        v.setFont(new Font("Segoe UI", Font.BOLD, 18));
        v.setForeground(accent);

        card.add(l);
        card.add(v);
        return card;
    }

    // =========================================================
    // INTERACTIVE SURPRISE SHUFFLE ANIMATION MODAL
    // =========================================================
    private void startSurpriseShuffleAnimation() {
        if (allGames.isEmpty()) return;

        JDialog dialog = new JDialog(this, "Rolling Random Game...", true);
        dialog.setSize(440, 240);
        dialog.setLocationRelativeTo(this);
        dialog.setUndecorated(true);

        ThemeManager.Theme theme = ThemeManager.getCurrentTheme();

        JPanel p = new JPanel(new BorderLayout(0, 14)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(theme.cardBg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);
                g2.setColor(theme.accent);
                g2.setStroke(new BasicStroke(2.0f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 24, 24);
                g2.dispose();
            }
        };
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(24, 24, 24, 24));

        JLabel titleLbl = new JLabel("✦ ROLLING SURPRISE GAME...", SwingConstants.CENTER);
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLbl.setForeground(theme.accent);
        p.add(titleLbl, BorderLayout.NORTH);

        JLabel iconLbl = new JLabel("🎰", SwingConstants.CENTER);
        iconLbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));

        JLabel nameLbl = new JLabel("Shuffling arcade vault...", SwingConstants.CENTER);
        nameLbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        nameLbl.setForeground(theme.textPrimary);

        JPanel centerBox = new JPanel(new GridLayout(2, 1, 0, 4));
        centerBox.setOpaque(false);
        centerBox.add(iconLbl);
        centerBox.add(nameLbl);
        p.add(centerBox, BorderLayout.CENTER);

        JProgressBar progress = new JProgressBar(0, 100);
        progress.setForeground(theme.accent);
        progress.setBorderPainted(false);
        progress.setPreferredSize(new Dimension(300, 8));
        p.add(progress, BorderLayout.SOUTH);

        dialog.setContentPane(p);

        // 1.2s rapid shuffle animation
        Random rand = new Random();
        final int[] step = {0};
        final GameEntry[] chosen = {allGames.get(rand.nextInt(allGames.size()))};

        Timer shuffleTimer = new Timer(55, null);
        shuffleTimer.addActionListener(e -> {
            step[0]++;
            GameEntry ge = allGames.get(rand.nextInt(allGames.size()));
            iconLbl.setText(ge.icon);
            nameLbl.setText(ge.name);
            progress.setValue(step[0] * 5);
            SettingFrame.playTestSound();

            if (step[0] >= 20) {
                shuffleTimer.stop();
                chosen[0] = allGames.get(rand.nextInt(allGames.size()));
                iconLbl.setText(chosen[0].icon);
                nameLbl.setText("✨ " + chosen[0].name + " ✨");
                titleLbl.setText("🎯 GAME SELECTED!");

                Timer closeTimer = new Timer(400, ev -> {
                    dialog.dispose();
                    launchGame(chosen[0].className);
                });
                closeTimer.setRepeats(false);
                closeTimer.start();
            }
        });

        shuffleTimer.start();
        dialog.setVisible(true);
    }

    // =========================================================
    // GAME LAUNCHER & HELPERS
    // =========================================================
    private void launchGame(String className) {
        SettingFrame.playTestSound();
        try {
            Class<?> clazz = Class.forName(className);
            for (Constructor<?> c : clazz.getConstructors()) {
                if (c.getParameterCount() == 1 && c.getParameterTypes()[0] == String.class) {
                    c.newInstance(getUserName());
                    return;
                }
            }
            clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Could not launch " + className + ":\n" + e.getMessage(), "Launch Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showLeaderboard() {
        new LeaderBoardFrame();
    }

    private void openFrame(String frameClass) {
        try {
            Class<?> clazz = Class.forName(frameClass);
            clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void logout() {
        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to log out of GamesMania?",
                "Logout Confirmation",
                JOptionPane.YES_NO_OPTION
        );
        if (choice == JOptionPane.YES_OPTION) {
            dispose();
            SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
        }
    }

    private String getUserName() {
        if (user instanceof User) {
            return ((User) user).getName();
        } else if (user instanceof String) {
            return (String) user;
        }
        return SettingFrame.getPlayerName();
    }

    // =========================================================
    // WRAP LAYOUT (STANDARD SWING MULTI-COLUMN AUTO-WRAPPING)
    // Ensures cards wrap to exact available width & strictly scrolls DOWN!
    // =========================================================
    public static class WrapLayout extends FlowLayout {
        public WrapLayout() {
            super();
        }

        public WrapLayout(int align) {
            super(align);
        }

        public WrapLayout(int align, int hgap, int vgap) {
            super(align, hgap, vgap);
        }

        @Override
        public Dimension preferredLayoutSize(Container target) {
            return layoutSize(target, true);
        }

        @Override
        public Dimension minimumLayoutSize(Container target) {
            Dimension minimum = layoutSize(target, false);
            minimum.width -= (getHgap() + 1);
            return minimum;
        }

        private Dimension layoutSize(Container target, boolean preferred) {
            synchronized (target.getTreeLock()) {
                int targetWidth = target.getWidth();
                if (targetWidth == 0) {
                    Container p = target.getParent();
                    if (p != null) targetWidth = p.getWidth();
                }
                if (targetWidth <= 0) targetWidth = 1050;

                int hgap = getHgap();
                int vgap = getVgap();
                Insets insets = target.getInsets();
                int horizontalInsetsAndGap = insets.left + insets.right + (hgap * 2);
                int maxWidth = Math.max(100, targetWidth - horizontalInsetsAndGap);

                Dimension dim = new Dimension(0, 0);
                int rowWidth = 0;
                int rowHeight = 0;

                int nmembers = target.getComponentCount();
                for (int i = 0; i < nmembers; i++) {
                    Component m = target.getComponent(i);
                    if (m.isVisible()) {
                        Dimension d = preferred ? m.getPreferredSize() : m.getMinimumSize();
                        if (rowWidth + d.width > maxWidth) {
                            dim.width = Math.max(dim.width, rowWidth);
                            dim.height += rowHeight + vgap;
                            rowWidth = 0;
                            rowHeight = 0;
                        }
                        if (rowWidth != 0) {
                            rowWidth += hgap;
                        }
                        rowWidth += d.width;
                        rowHeight = Math.max(rowHeight, d.height);
                    }
                }
                dim.width = Math.max(dim.width, rowWidth);
                dim.height += rowHeight + vgap + insets.top + insets.bottom;
                return dim;
            }
        }
    }

    // =========================================================
    // SCROLLABLE PANEL (STRICTLY LOCKS VIEWPORT WIDTH)
    // =========================================================
    public static class ScrollablePanel extends JPanel implements Scrollable {
        public ScrollablePanel() {
            super();
            setOpaque(false);
        }

        @Override
        public Dimension getPreferredScrollableViewportSize() {
            return getPreferredSize();
        }

        @Override
        public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
            return 26;
        }

        @Override
        public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
            return 130;
        }

        @Override
        public boolean getScrollableTracksViewportWidth() {
            return true; // STRICTLY FORCES VIEWPORT WIDTH - NO HORIZONTAL SCROLL!
        }

        @Override
        public boolean getScrollableTracksViewportHeight() {
            return false; // SCROLLS VERTICALLY ONLY!
        }
    }

    // =========================================================
    // LIVE TICKER PANEL (ARCADE MARQUEE)
    // =========================================================
    static class ArcadeTickerPanel extends JPanel {
        private final String tickerText = "🔥 TRENDING NOW: 2048 Fusion • High Score: 16,384   |   ⭐ RECORD HOLDER: Rishabh Nandi (PIET25cs135)   |   ⚡ 60 FPS ARCADE CANVAS ENGINE ACTIVE   |   🏆 NEW REACTION RECORD: 184ms   |   ✦ SURPRISE ME: Try your luck with a random game!   |   🎨 LIVE OBSERVER THEMES: Light, Dark, Blue, Emerald, Purple";
        private int textX = 800;
        private final Timer timer;

        ArcadeTickerPanel() {
            setOpaque(true);
            setPreferredSize(new Dimension(1000, 26));

            timer = new Timer(30, e -> {
                textX -= 2;
                if (textX < -1500) {
                    textX = getWidth();
                }
                repaint();
            });
            timer.start();
        }

        void applyTheme(ThemeManager.Theme theme) {
            setBackground(theme.headerBg);
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            ThemeManager.Theme theme = ThemeManager.getCurrentTheme();
            g2.setColor(theme.headerBg);
            g2.fillRect(0, 0, getWidth(), getHeight());

            g2.setColor(theme.cardBorder);
            g2.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);

            g2.setColor(theme.accent);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
            g2.drawString(tickerText, textX, 17);

            g2.dispose();
        }
    }

    // =========================================================
    // 60 FPS ARCADE BACKGROUND (ORBS, WAVES, STARDUST, MOUSE HALO)
    // =========================================================
    static class ArcadeBackgroundPanel extends JPanel {
        private final Random random = new Random();
        private final int PARTICLE_COUNT = 36;
        private final float[] px = new float[PARTICLE_COUNT];
        private final float[] py = new float[PARTICLE_COUNT];
        private final float[] ps = new float[PARTICLE_COUNT];
        private final float[] pspd = new float[PARTICLE_COUNT];

        // Ambient floating gradient orbs
        private final int ORB_COUNT = 5;
        private final float[] ox = new float[ORB_COUNT];
        private final float[] oy = new float[ORB_COUNT];
        private final float[] ovx = new float[ORB_COUNT];
        private final float[] ovy = new float[ORB_COUNT];
        private final float[] orad = new float[ORB_COUNT];

        // Wave animation phase
        private float wavePhase = 0f;
        private int mouseX = -500;
        private int mouseY = -500;

        // Cyber Perspective Grid & Shockwave
        private float gridScroll = 0f;
        private int rippleX = -200;
        private int rippleY = -200;
        private float rippleRadius = 0f;
        private float rippleAlpha = 0f;

        public ArcadeBackgroundPanel() {
            super(new BorderLayout(0, 0));

            // Initialize stardust particles
            for (int i = 0; i < PARTICLE_COUNT; i++) {
                px[i] = random.nextInt(1280);
                py[i] = random.nextInt(850);
                ps[i] = random.nextFloat() * 4f + 2f;
                pspd[i] = random.nextFloat() * 0.7f + 0.3f;
            }

            // Initialize ambient gradient orbs
            for (int i = 0; i < ORB_COUNT; i++) {
                ox[i] = random.nextInt(1200);
                oy[i] = random.nextInt(800);
                ovx[i] = (random.nextFloat() - 0.5f) * 0.9f;
                ovy[i] = (random.nextFloat() - 0.5f) * 0.9f;
                orad[i] = random.nextFloat() * 160f + 220f;
            }

            // Mouse motion tracking for interactive lighting
            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    mouseX = e.getX();
                    mouseY = e.getY();
                }
            });

            // Mouse click tracking for interactive shockwave ripples
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    rippleX = e.getX();
                    rippleY = e.getY();
                    rippleRadius = 10f;
                    rippleAlpha = 0.85f;
                }
            });

            // 60 FPS double-buffered animation loop
            Timer timer = new Timer(16, e -> {
                int w = Math.max(1, getWidth());
                int h = Math.max(1, getHeight());

                // Update particles
                for (int i = 0; i < PARTICLE_COUNT; i++) {
                    py[i] -= pspd[i];
                    if (py[i] < -10) {
                        py[i] = h + 10;
                        px[i] = random.nextInt(w);
                    }
                }

                // Update orbs
                for (int i = 0; i < ORB_COUNT; i++) {
                    ox[i] += ovx[i];
                    oy[i] += ovy[i];
                    if (ox[i] < -120 || ox[i] > w + 120) ovx[i] = -ovx[i];
                    if (oy[i] < -120 || oy[i] > h + 120) ovy[i] = -ovy[i];
                }

                wavePhase += 0.025f;
                gridScroll = (gridScroll + 0.65f) % 24f;

                if (rippleRadius > 0) {
                    rippleRadius += 9f;
                    rippleAlpha = Math.max(0f, rippleAlpha - 0.035f);
                    if (rippleAlpha <= 0f) rippleRadius = 0f;
                }

                repaint();
            });
            timer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            ThemeManager.Theme theme = ThemeManager.getCurrentTheme();
            int w = getWidth();
            int h = getHeight();

            // 1. Background Fill
            g2.setColor(theme.bg);
            g2.fillRect(0, 0, w, h);

            // 2. Animated 3D Cyber Perspective Grid Horizon
            int horizonY = (int) (h * 0.62);
            int vanishingX = w / 2 + (int) ((mouseX - w / 2) * 0.05f);

            int gridAlpha = theme.isDark ? 28 : 20;
            g2.setColor(new Color(theme.accent.getRed(), theme.accent.getGreen(), theme.accent.getBlue(), gridAlpha));
            g2.setStroke(new BasicStroke(1.0f));

            // Radial perspective lines
            int numRadialLines = 24;
            for (int i = 0; i <= numRadialLines; i++) {
                int bottomX = (int) ((i - numRadialLines / 2.0) * (w / 9.0) + vanishingX);
                g2.drawLine(vanishingX, horizonY, bottomX, h);
            }

            // Moving horizontal grid rungs
            int rungs = 10;
            for (int i = 0; i < rungs; i++) {
                float norm = (i + (gridScroll / 24f)) / rungs;
                int ry = (int) (horizonY + Math.pow(norm, 2.3) * (h - horizonY));
                if (ry >= horizonY && ry <= h) {
                    g2.drawLine(0, ry, w, ry);
                }
            }

            // Horizon Glow Line
            g2.setColor(new Color(theme.accent.getRed(), theme.accent.getGreen(), theme.accent.getBlue(), theme.isDark ? 55 : 35));
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawLine(0, horizonY, w, horizonY);

            // 3. Ambient Floating Gradient Orbs
            Color[] orbColors;
            if (theme.name.equalsIgnoreCase("Vintage")) {
                // Warm retro coin-op parlor aura (Amber Gold, Cherry Neon, Antique Brass)
                orbColors = new Color[]{
                        new Color(245, 158, 11, 38), // Neon Amber Gold
                        new Color(239, 68, 68, 30),  // Retro Cherry Neon
                        new Color(217, 119, 6, 32),  // Warm Antique Brass
                        new Color(180, 83, 9, 28),   // Burnt Copper
                        new Color(254, 240, 138, 22) // Golden Stardust Ember
                };
            } else if (theme.isDark) {
                orbColors = new Color[]{
                        new Color(99, 102, 241, 30),
                        new Color(16, 185, 129, 25),
                        new Color(6, 182, 212, 25),
                        new Color(244, 63, 94, 22),
                        new Color(245, 158, 11, 20)
                };
            } else {
                // Classy vintage light ambiance (warm champagne gold, aged brass amber, antique peach)
                orbColors = new Color[]{
                        new Color(254, 240, 138, 55), // Warm Champagne Gold
                        new Color(253, 230, 138, 50), // Soft Vintage Amber
                        new Color(254, 215, 170, 45), // Antique Parchment Peach
                        new Color(220, 185, 155, 40), // Aged Brass Glow
                        new Color(224, 231, 255, 35)  // Soft Royal Silk
                };
            }

            for (int i = 0; i < ORB_COUNT; i++) {
                Color oc = orbColors[i % orbColors.length];
                int rad = (int) orad[i];
                int x = (int) (ox[i] - rad / 2);
                int y = (int) (oy[i] - rad / 2);

                g2.setColor(oc);
                g2.fillOval(x, y, rad, rad);
            }

            // 4. Dynamic Sine Wave Lines across the background
            g2.setColor(new Color(theme.accent.getRed(), theme.accent.getGreen(), theme.accent.getBlue(), theme.isDark ? 18 : 25));
            g2.setStroke(new BasicStroke(1.5f));

            int waveY1 = h / 3;
            int waveY2 = (2 * h) / 3;
            for (int x = 0; x < w; x += 12) {
                int y1 = (int) (waveY1 + Math.sin((x * 0.005) + wavePhase) * 25);
                int y2 = (int) (waveY2 + Math.cos((x * 0.006) + wavePhase) * 30);
                g2.drawLine(x, y1, x + 6, y1);
                g2.drawLine(x, y2, x + 6, y2);
            }

            // 5. Interactive Mouse Proximity Halo
            if (mouseX > 0 && mouseY > 0) {
                int haloRad = 280;
                int hx = mouseX - haloRad / 2;
                int hy = mouseY - haloRad / 2;
                int alpha = theme.isDark ? 25 : 35;
                g2.setColor(new Color(theme.accent.getRed(), theme.accent.getGreen(), theme.accent.getBlue(), alpha));
                g2.fillOval(hx, hy, haloRad, haloRad);
            }

            // 6. Rising Floating Stardust Particles
            for (int i = 0; i < PARTICLE_COUNT; i++) {
                int alpha = theme.isDark ? 55 : 40;
                g2.setColor(new Color(theme.accent.getRed(), theme.accent.getGreen(), theme.accent.getBlue(), alpha));
                g2.fillOval((int) px[i], (int) py[i], (int) ps[i], (int) ps[i]);
            }

            // 7. Interactive Click Shockwave Ripple
            if (rippleRadius > 0 && rippleAlpha > 0) {
                g2.setColor(new Color(theme.accent.getRed(), theme.accent.getGreen(), theme.accent.getBlue(), (int) (rippleAlpha * 160)));
                g2.setStroke(new BasicStroke(2.0f));
                int rx = (int) (rippleX - rippleRadius);
                int ry = (int) (rippleY - rippleRadius);
                int rd = (int) (rippleRadius * 2);
                g2.drawOval(rx, ry, rd, rd);
            }

            g2.dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HomeFrame("hg"));
    }
}