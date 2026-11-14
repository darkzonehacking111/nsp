import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class HelpFrame extends JFrame {

    private final Color BG = new Color(247, 249, 255);
    private final Color WHITE = Color.WHITE;
    private final Color TEXT = new Color(31, 48, 80);
    private final Color MUTED = new Color(102, 119, 150);
    private final Color PURPLE = new Color(116, 76, 220);
    private final Color LIGHT_PURPLE = new Color(241, 236, 255);
    private final Color BORDER = new Color(225, 229, 240);

    private JPanel contentPanel;
    private JLabel selectedTitle;
    private JLabel selectedDescription;
    private JLabel selectedIcon;

    private final Map<String, GameInfo> games = new LinkedHashMap<>();

    public HelpFrame() {
        setTitle("GamesMania - Help Center");
        setSize(1180, 760);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        loadGames();
        buildUI();
    }

    // ---------------------------------------------------------
    // GAME DATA
    // ---------------------------------------------------------

    private void loadGames() {

        games.put("GamesMania Guide",
                new GameInfo("🎮",
                        "GamesMania Guide",
                        "Everything you need to know about GamesMania.",
                        "Learn how to navigate the application and play all available mini games.",
                        "Enter your name → Start Playing → Choose a game → Read instructions → Play and beat your high score.",
                        "Use the Help Center whenever you are unsure about a game.",
                        "Game collection and navigation"));

        games.put("Coin Flip",
                new GameInfo("🪙",
                        "Coin Flip",
                        "Test your luck with a simple coin toss.",
                        "Predict whether the coin will land on heads or tails.",
                        "Choose Heads or Tails and flip the coin. Your result and score will be displayed.",
                        "There is no strategy here — trust your luck!",
                        "Mouse clicks"));

        games.put("Color Match",
                new GameInfo("🎨",
                        "Color Match",
                        "Match the correct color before time runs out.",
                        "A target color is shown and you must select the matching option.",
                        "Read the target color → choose the matching color → continue through the rounds.",
                        "Focus on the actual color rather than the written word.",
                        "Mouse clicks"));

        games.put("Dino Game",
                new GameInfo("🦖",
                        "Dino Game",
                        "Jump over obstacles and survive as long as possible.",
                        "Control the dinosaur and avoid incoming obstacles.",
                        "Press the jump key when an obstacle approaches. Survive longer to increase your score.",
                        "Do not wait until the last moment to jump.",
                        "Keyboard / Space key"));

        games.put("Fruit Game",
                new GameInfo("🍎",
                        "Fruit Game",
                        "Catch the falling fruits and increase your score.",
                        "Move the catcher and collect fruits while avoiding missed opportunities.",
                        "Move the catcher left and right → position it below falling fruits → collect as many as possible.",
                        "Stay near the center so you can react quickly.",
                        "Keyboard / Arrow Keys"));

        games.put("2048",
                new GameInfo("🔢",
                        "2048",
                        "Combine tiles and reach the legendary 2048 tile.",
                        "Slide numbered tiles across the board and merge matching numbers.",
                        "Use Arrow Keys → combine identical tiles → keep the board organized → reach 2048.",
                        "Keep the highest tile in one corner.",
                        "Arrow Keys"));

        games.put("Guess Game",
                new GameInfo("🎯",
                        "Guess Game",
                        "Guess the hidden number.",
                        "Try to discover the randomly generated number using hints.",
                        "Enter a number → submit your guess → use higher/lower hints → find the answer.",
                        "Start near the middle of the possible range.",
                        "Mouse / Keyboard"));

        games.put("Lucky Spin",
                new GameInfo("🎡",
                        "Lucky Spin",
                        "Spin the wheel and test your luck.",
                        "Spin the lucky wheel and try to land on a high-value reward.",
                        "Press Spin → wait for the wheel → check your result → try again.",
                        "Sometimes luck is all you need!",
                        "Mouse click"));

        games.put("Pong",
                new GameInfo("🏓",
                        "Pong",
                        "Classic paddle-and-ball action.",
                        "Keep the ball away from your side and score against your opponent.",
                        "Move your paddle → hit the ball → defend your side → score points.",
                        "Watch the ball angle instead of only following its position.",
                        "Keyboard controls"));

        games.put("Racing",
                new GameInfo("🏎",
                        "Racing",
                        "Race toward the finish line.",
                        "Control your vehicle and avoid obstacles while racing.",
                        "Use the controls to move your vehicle → avoid obstacles → reach the finish.",
                        "Stay alert and avoid unnecessary movements.",
                        "Keyboard / Arrow Keys"));

        games.put("Reaction",
                new GameInfo("⚡",
                        "Reaction",
                        "Test how quickly you can react.",
                        "Wait for the correct signal and click as quickly as possible.",
                        "Press Start → wait for the target → click immediately → check your reaction time.",
                        "Keep your mouse over the target area.",
                        "Mouse click"));

        games.put("Sliding Puzzle",
                new GameInfo("🧩",
                        "Sliding Puzzle",
                        "Solve the puzzle by arranging the tiles.",
                        "Move numbered tiles into the empty space until they are correctly arranged.",
                        "Move tiles into the empty space → arrange them in order → solve using fewer moves.",
                        "Plan several moves ahead.",
                        "Arrow Keys / Mouse"));

        games.put("Snake",
                new GameInfo("🐍",
                        "Snake",
                        "Classic Snake gameplay.",
                        "Control the snake, collect food and grow without hitting the wall or yourself.",
                        "Move the snake → collect food → grow longer → avoid collisions.",
                        "Do not trap yourself in a corner.",
                        "Arrow Keys"));
    }

    // ---------------------------------------------------------
    // MAIN UI
    // ---------------------------------------------------------

    private void buildUI() {

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG);

        root.add(createHeader(), BorderLayout.NORTH);
        root.add(createMainArea(), BorderLayout.CENTER);
        root.add(createFooter(), BorderLayout.SOUTH);

        setContentPane(root);
    }

    // ---------------------------------------------------------
    // HEADER
    // ---------------------------------------------------------

    private JPanel createHeader() {

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(WHITE);
        header.setBorder(new EmptyBorder(14, 22, 12, 22));

        JPanel titleBox = new JPanel();
        titleBox.setOpaque(false);
        titleBox.setLayout(new BoxLayout(titleBox, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("🎮  GAMESMANIA HELP");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(TEXT);

        JLabel subtitle = new JLabel(
                "Controls  •  Rules  •  Hints  •  Tips  •  Game Guide");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(MUTED);
        subtitle.setBorder(new EmptyBorder(3, 3, 0, 0));

        titleBox.add(title);
        titleBox.add(subtitle);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        right.setOpaque(false);

        JTextField search = new JTextField(18);
        search.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        search.setToolTipText("Search games or topics...");
        search.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(8, 10, 8, 10)));

        JButton close = createButton("✕  CLOSE", PURPLE);

        close.addActionListener(e -> dispose());

        right.add(search);
        right.add(close);

        search.getDocument().addDocumentListener(
                new javax.swing.event.DocumentListener() {

                    private void filter() {

                        String text = search.getText().toLowerCase().trim();

                        for (Component c : ((JPanel) gamesPanel.getViewport().getView()).getComponents()) {

                            if (c instanceof JButton button) {

                                String name = button.getText()
                                        .replaceAll("[^a-zA-Z0-9 ]", "")
                                        .toLowerCase();

                                button.setVisible(
                                        text.isEmpty() || name.contains(text));
                            }
                        }
                    }

                    public void insertUpdate(javax.swing.event.DocumentEvent e) {
                        filter();
                    }

                    public void removeUpdate(javax.swing.event.DocumentEvent e) {
                        filter();
                    }

                    public void changedUpdate(javax.swing.event.DocumentEvent e) {
                        filter();
                    }
                });

        header.add(titleBox, BorderLayout.WEST);
        header.add(right, BorderLayout.EAST);

        return header;
    }

    // ---------------------------------------------------------
    // MAIN AREA
    // ---------------------------------------------------------

    private JScrollPane gamesPanel;
    private JScrollPane contentScroll;

    private JPanel createMainArea() {

        JPanel main = new JPanel(new BorderLayout(14, 0));
        main.setBackground(BG);
        main.setBorder(new EmptyBorder(14, 14, 14, 14));

        main.add(createLeftMenu(), BorderLayout.WEST);
        main.add(createContent(), BorderLayout.CENTER);

        return main;
    }

    // ---------------------------------------------------------
    // LEFT MENU
    // ---------------------------------------------------------

    private JPanel createLeftMenu() {

        JPanel outer = new JPanel(new BorderLayout());
        outer.setBackground(WHITE);
        outer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(12, 10, 12, 10)));

        JLabel heading = new JLabel("GAME GUIDE");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 17));
        heading.setForeground(TEXT);
        heading.setBorder(new EmptyBorder(0, 5, 10, 5));

        JPanel list = new JPanel();
        list.setBackground(WHITE);
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));

        boolean first = true;

        for (String game : games.keySet()) {

            JButton button = createGameButton(game);

            if (first) {
                button.setBackground(new Color(237, 231, 255));
                first = false;
            }

            list.add(button);
            list.add(Box.createVerticalStrut(6));
        }

        gamesPanel = new JScrollPane(list);
        gamesPanel.setBorder(null);
        gamesPanel.setPreferredSize(new Dimension(220, 0));
        gamesPanel.getVerticalScrollBar().setUnitIncrement(15);

        outer.add(heading, BorderLayout.NORTH);
        outer.add(gamesPanel, BorderLayout.CENTER);

        return outer;
    }

    private JButton createGameButton(String gameName) {

        GameInfo info = games.get(gameName);

        JButton button = new JButton(
                info.icon + "  " + gameName);

        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(TEXT);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(9, 10, 9, 10)));

        button.setBackground(WHITE);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        button.addActionListener(e -> {

            showGame(gameName);

            for (Component c :
                    ((JPanel) gamesPanel.getViewport().getView()).getComponents()) {

                if (c instanceof JButton b) {
                    b.setBackground(WHITE);
                }
            }

            button.setBackground(new Color(237, 231, 255));
        });

        button.addMouseListener(new MouseAdapter() {

            public void mouseEntered(MouseEvent e) {

                if (button.getBackground().equals(WHITE)) {
                    button.setBackground(new Color(248, 246, 255));
                }
            }

            public void mouseExited(MouseEvent e) {

                if (button.getBackground().equals(new Color(248, 246, 255))) {
                    button.setBackground(WHITE);
                }
            }
        });

        return button;
    }

    // ---------------------------------------------------------
    // CONTENT
    // ---------------------------------------------------------

    private JPanel createContent() {

        contentPanel = new JPanel();
        contentPanel.setBackground(BG);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(new EmptyBorder(4, 2, 20, 4));

        contentScroll = new JScrollPane(contentPanel);
        contentScroll.setBorder(null);
        contentScroll.setBackground(BG);
        contentScroll.getVerticalScrollBar().setUnitIncrement(18);

        showGame("GamesMania Guide");

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(BG);
        wrapper.add(contentScroll, BorderLayout.CENTER);

        return wrapper;
    }

    private void showGame(String gameName) {

        GameInfo game = games.get(gameName);

        contentPanel.removeAll();

        // Hero card
        JPanel hero = new RoundedPanel(22, WHITE);
        hero.setLayout(new BorderLayout());
        hero.setBorder(new EmptyBorder(20, 22, 20, 22));
        hero.setMaximumSize(new Dimension(Integer.MAX_VALUE, 145));

        selectedIcon = new JLabel(game.icon);
        selectedIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 45));
        selectedIcon.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel heroText = new JPanel();
        heroText.setOpaque(false);
        heroText.setLayout(new BoxLayout(heroText, BoxLayout.Y_AXIS));

        selectedTitle = new JLabel(game.title);
        selectedTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        selectedTitle.setForeground(TEXT);

        selectedDescription = new JLabel(game.description);
        selectedDescription.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        selectedDescription.setForeground(MUTED);

        heroText.add(selectedTitle);
        heroText.add(Box.createVerticalStrut(6));
        heroText.add(selectedDescription);

        hero.add(selectedIcon, BorderLayout.WEST);
        hero.add(heroText, BorderLayout.CENTER);

        contentPanel.add(hero);
        contentPanel.add(Box.createVerticalStrut(14));

        // Three cards
        JPanel cards = new JPanel(new GridLayout(1, 3, 12, 0));
        cards.setOpaque(false);
        cards.setMaximumSize(new Dimension(Integer.MAX_VALUE, 210));

        cards.add(createInfoCard(
                "▶  HOW TO PLAY",
                "①  " + game.howToPlay,
                PURPLE));

        cards.add(createInfoCard(
                "🖱  CONTROLS",
                game.controls,
                new Color(76, 145, 220)));

        cards.add(createInfoCard(
                "💡  PRO TIP",
                game.tip,
                new Color(44, 190, 143)));

        contentPanel.add(cards);
        contentPanel.add(Box.createVerticalStrut(14));

        // Bottom guide
        JPanel guide = new RoundedPanel(22, WHITE);
        guide.setLayout(new BorderLayout());
        guide.setBorder(new EmptyBorder(18, 20, 18, 20));
        guide.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));

        JLabel guideTitle = new JLabel("✨ QUICK GUIDE");
        guideTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        guideTitle.setForeground(PURPLE);

        JTextArea guideText = new JTextArea(
                "Read the instructions carefully, stay focused and try to beat your previous score. "
                        + "Your best performance can be checked from the Leaderboard.");
        guideText.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        guideText.setForeground(MUTED);
        guideText.setLineWrap(true);
        guideText.setWrapStyleWord(true);
        guideText.setEditable(false);
        guideText.setOpaque(false);

        guide.add(guideTitle, BorderLayout.NORTH);
        guide.add(guideText, BorderLayout.CENTER);

        contentPanel.add(guide);

        contentPanel.revalidate();
        contentPanel.repaint();

        SwingUtilities.invokeLater(() ->
                contentScroll.getVerticalScrollBar().setValue(0));
    }

    // ---------------------------------------------------------
    // INFORMATION CARD
    // ---------------------------------------------------------

    private JPanel createInfoCard(
            String title,
            String text,
            Color accent) {

        RoundedPanel card = new RoundedPanel(18, WHITE);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(15, 16, 15, 16));

        JLabel heading = new JLabel(title);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 14));
        heading.setForeground(accent);

        JTextArea body = new JTextArea(text);
        body.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        body.setForeground(TEXT);
        body.setLineWrap(true);
        body.setWrapStyleWord(true);
        body.setEditable(false);
        body.setOpaque(false);
        body.setBorder(new EmptyBorder(10, 0, 0, 0));

        card.add(heading, BorderLayout.NORTH);
        card.add(body, BorderLayout.CENTER);

        return card;
    }

    // ---------------------------------------------------------
    // FOOTER
    // ---------------------------------------------------------

    private JPanel createFooter() {

        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(WHITE);
        footer.setBorder(new EmptyBorder(10, 18, 10, 18));

        JLabel tip = new JLabel(
                "💡 Tip: Practice makes perfect! Choose a game and start playing.");
        tip.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tip.setForeground(MUTED);

        JButton home = createButton("⌂  HOME", new Color(82, 130, 220));

        home.addActionListener(e -> dispose());

        footer.add(tip, BorderLayout.WEST);
        footer.add(home, BorderLayout.EAST);

        return footer;
    }

    // ---------------------------------------------------------
    // BUTTON
    // ---------------------------------------------------------

    private JButton createButton(String text, Color color) {

        JButton button = new JButton(text);

        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(10, 18, 10, 18));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {

            public void mouseEntered(MouseEvent e) {
                button.setBackground(color.brighter());
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(color);
            }
        });

        return button;
    }

    // ---------------------------------------------------------
    // ROUNDED PANEL
    // ---------------------------------------------------------

    private static class RoundedPanel extends JPanel {

        private final int radius;
        private final Color background;

        RoundedPanel(int radius, Color background) {
            this.radius = radius;
            this.background = background;
            setOpaque(false);
        }

        protected void paintComponent(Graphics g) {

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(background);
            g2.fillRoundRect(
                    0,
                    0,
                    getWidth() - 1,
                    getHeight() - 1,
                    radius,
                    radius);

            g2.setColor(new Color(225, 229, 240));

            g2.drawRoundRect(
                    0,
                    0,
                    getWidth() - 1,
                    getHeight() - 1,
                    radius,
                    radius);

            g2.dispose();

            super.paintComponent(g);
        }
    }

    // ---------------------------------------------------------
    // GAME INFO
    // ---------------------------------------------------------

    private static class GameInfo {

        String icon;
        String title;
        String description;
        String details;
        String howToPlay;
        String tip;
        String controls;

        GameInfo(
                String icon,
                String title,
                String description,
                String details,
                String howToPlay,
                String tip,
                String controls) {

            this.icon = icon;
            this.title = title;
            this.description = description;
            this.details = details;
            this.howToPlay = howToPlay;
            this.tip = tip;
            this.controls = controls;
        }
    }

    // ---------------------------------------------------------
    // TEST
    // ---------------------------------------------------------

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            try {
                UIManager.setLookAndFeel(
                        UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }

            new HelpFrame().setVisible(true);
        });
    }
}