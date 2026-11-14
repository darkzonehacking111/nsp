import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * GameFrame
 *
 * Common frame used by GamesMania games.
 *
 * Features:
 *  - Modern light UI
 *  - Rounded header
 *  - Game title
 *  - Difficulty badge
 *  - Score display
 *  - Back button
 *  - Restart button
 *  - Pause button
 *  - Hover effects
 */
public class GameFrame extends JFrame {

    // =========================================================
    // COLORS
    // =========================================================

    public static final Color BACKGROUND =
            new Color(246, 248, 252);

    public static final Color WHITE =
            Color.WHITE;

    public static final Color TEXT =
            new Color(34, 38, 52);

    public static final Color MUTED =
            new Color(110, 115, 130);

    public static final Color PURPLE =
            new Color(92, 83, 220);

    public static final Color MINT =
            new Color(47, 190, 167);

    public static final Color RED =
            new Color(235, 80, 90);

    // =========================================================
    // COMPONENTS
    // =========================================================

    private JPanel mainPanel;
    private JPanel headerPanel;

    private JLabel titleLabel;
    private JLabel subtitleLabel;

    private JLabel scoreLabel;
    private JLabel difficultyLabel;

    private JButton backButton;
    private JButton restartButton;
    private JButton pauseButton;

    private JPanel gameContent;

    // =========================================================
    // STATE
    // =========================================================

    private boolean paused = false;

    private Runnable backAction;
    private Runnable restartAction;
    private Runnable pauseAction;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public GameFrame(
            String title,
            String difficulty) {

        this(
                title,
                "",
                difficulty
        );
    }

    public GameFrame(
            String title,
            String subtitle,
            String difficulty) {

        setTitle(
                "GamesMania • " + title
        );

        setSize(
                1000,
                720
        );

        setMinimumSize(
                new Dimension(
                        850,
                        600
                )
        );

        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        setResizable(false);

        buildUI(
                title,
                subtitle,
                difficulty
        );
    }

    // =========================================================
    // BUILD UI
    // =========================================================

    private void buildUI(
            String title,
            String subtitle,
            String difficulty) {

        mainPanel =
                new JPanel(
                        new BorderLayout()
                );

        mainPanel.setBackground(
                BACKGROUND
        );

        setContentPane(mainPanel);

        createHeader(
                title,
                subtitle,
                difficulty
        );

        gameContent =
                new JPanel(
                        new BorderLayout()
                );

        gameContent.setOpaque(false);

        mainPanel.add(
                gameContent,
                BorderLayout.CENTER
        );
    }

    // =========================================================
    // HEADER
    // =========================================================

    private void createHeader(
            String title,
            String subtitle,
            String difficulty) {

        headerPanel =
                new JPanel(
                        new BorderLayout()
                );

        headerPanel.setOpaque(false);

        headerPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        15,
                        20,
                        10,
                        20
                )
        );

        // -----------------------------------------------------
        // LEFT SIDE
        // -----------------------------------------------------

        JPanel leftPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                12,
                                0
                        )
                );

        leftPanel.setOpaque(false);

        // Back button
        backButton =
                createButton(
                        "←",
                        MUTED
                );

        backButton.setToolTipText(
                "Back to GamesMania"
        );

        backButton.addActionListener(
                e -> {

                    if (backAction != null) {

                        backAction.run();

                    } else {

                        dispose();
                    }
                }
        );

        leftPanel.add(
                backButton
        );

        // Title area
        JPanel titlePanel =
                new JPanel();

        titlePanel.setOpaque(false);

        titlePanel.setLayout(
                new BoxLayout(
                        titlePanel,
                        BoxLayout.Y_AXIS
                )
        );

        titleLabel =
                new JLabel(
                        title
                );

        titleLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        25
                )
        );

        titleLabel.setForeground(
                TEXT
        );

        titlePanel.add(
                titleLabel
        );

        if (subtitle != null &&
                !subtitle.isEmpty()) {

            subtitleLabel =
                    new JLabel(
                            subtitle
                    );

            subtitleLabel.setFont(
                    new Font(
                            "SansSerif",
                            Font.PLAIN,
                            11
                    )
            );

            subtitleLabel.setForeground(
                    MUTED
            );

            subtitleLabel.setBorder(
                    BorderFactory.createEmptyBorder(
                            3,
                            0,
                            0,
                            0
                    )
            );

            titlePanel.add(
                    subtitleLabel
            );
        }

        leftPanel.add(
                titlePanel
        );

        headerPanel.add(
                leftPanel,
                BorderLayout.WEST
        );

        // -----------------------------------------------------
        // CENTER
        // -----------------------------------------------------

        JPanel centerPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                20,
                                8
                        )
                );

        centerPanel.setOpaque(false);

        difficultyLabel =
                new JLabel(
                        difficulty.toUpperCase()
                );

        difficultyLabel.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        difficultyLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        11
                )
        );

        difficultyLabel.setForeground(
                getDifficultyColor(
                        difficulty
                )
        );

        difficultyLabel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                getDifficultyColor(
                                        difficulty
                                ),
                                1
                        ),
                        BorderFactory.createEmptyBorder(
                                7,
                                14,
                                7,
                                14
                        )
                )
        );

        centerPanel.add(
                difficultyLabel
        );

        scoreLabel =
                new JLabel(
                        "SCORE  0"
                );

        scoreLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        14
                )
        );

        scoreLabel.setForeground(
                PURPLE
        );

        centerPanel.add(
                scoreLabel
        );

        headerPanel.add(
                centerPanel,
                BorderLayout.CENTER
        );

        // -----------------------------------------------------
        // RIGHT SIDE
        // -----------------------------------------------------

        JPanel rightPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                8,
                                0
                        )
                );

        rightPanel.setOpaque(false);

        restartButton =
                createButton(
                        "↻",
                        PURPLE
                );

        restartButton.setToolTipText(
                "Restart"
        );

        restartButton.addActionListener(
                e -> {

                    if (restartAction != null) {

                        restartAction.run();
                    }
                }
        );

        rightPanel.add(
                restartButton
        );

        pauseButton =
                createButton(
                        "Ⅱ",
                        MINT
                );

        pauseButton.setToolTipText(
                "Pause"
        );

        pauseButton.addActionListener(
                e -> togglePause()
        );

        rightPanel.add(
                pauseButton
        );

        headerPanel.add(
                rightPanel,
                BorderLayout.EAST
        );

        mainPanel.add(
                headerPanel,
                BorderLayout.NORTH
        );
    }

    // =========================================================
    // BUTTON
    // =========================================================

    private JButton createButton(
            String text,
            Color color) {

        JButton button =
                new JButton(text) {

                    @Override
                    protected void paintComponent(
                            Graphics g) {

                        Graphics2D g2 =
                                (Graphics2D)
                                        g.create();

                        g2.setRenderingHint(
                                RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON
                        );

                        if (getModel().isRollover()) {

                            g2.setColor(
                                    new Color(
                                            color.getRed(),
                                            color.getGreen(),
                                            color.getBlue(),
                                            25
                                    )
                            );

                        } else {

                            g2.setColor(
                                    new Color(
                                            235,
                                            237,
                                            244
                                    )
                            );
                        }

                        g2.fillRoundRect(
                                0,
                                0,
                                getWidth(),
                                getHeight(),
                                15,
                                15
                        );

                        super.paintComponent(
                                g
                        );

                        g2.dispose();
                    }
                };

        button.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        17
                )
        );

        button.setForeground(
                color
        );

        button.setFocusPainted(
                false
        );

        button.setBorderPainted(
                false
        );

        button.setContentAreaFilled(
                false
        );

        button.setOpaque(
                false
        );

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        button.setPreferredSize(
                new Dimension(
                        42,
                        38
                )
        );

        return button;
    }

    // =========================================================
    // GAME CONTENT
    // =========================================================

    public void setGameContent(
            JComponent component) {

        gameContent.removeAll();

        gameContent.add(
                component,
                BorderLayout.CENTER
        );

        gameContent.revalidate();

        gameContent.repaint();
    }

    public JPanel getGameContent() {

        return gameContent;
    }

    // =========================================================
    // SCORE
    // =========================================================

    public void setScore(
            int score) {

        scoreLabel.setText(
                "SCORE  " + score
        );
    }

    public int getScore() {

        String text =
                scoreLabel.getText();

        try {

            return Integer.parseInt(
                    text.replace(
                            "SCORE",
                            ""
                    ).trim()
            );

        } catch (Exception e) {

            return 0;
        }
    }

    // =========================================================
    // TITLE
    // =========================================================

    public void setGameTitle(
            String title) {

        titleLabel.setText(
                title
        );
    }

    public String getGameTitle() {

        return titleLabel.getText();
    }

    // =========================================================
    // DIFFICULTY
    // =========================================================

    public void setDifficulty(
            String difficulty) {

        difficultyLabel.setText(
                difficulty.toUpperCase()
        );

        Color color =
                getDifficultyColor(
                        difficulty
                );

        difficultyLabel.setForeground(
                color
        );

        difficultyLabel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                color,
                                1
                        ),
                        BorderFactory.createEmptyBorder(
                                7,
                                14,
                                7,
                                14
                        )
                )
        );
    }

    // =========================================================
    // DIFFICULTY COLOR
    // =========================================================

    private Color getDifficultyColor(
            String difficulty) {

        if (difficulty == null) {

            return MUTED;
        }

        switch (
                difficulty.toLowerCase()
        ) {

            case "easy":

                return new Color(
                        65,
                        185,
                        120
                );

            case "hard":

                return RED;

            case "medium":

                return new Color(
                        245,
                        175,
                        65
                );

            default:

                return PURPLE;
        }
    }

    // =========================================================
    // PAUSE
    // =========================================================

    public void togglePause() {

        paused =
                !paused;

        if (paused) {

            pauseButton.setText(
                    "▶"
            );

            pauseButton.setToolTipText(
                    "Resume"
            );

        } else {

            pauseButton.setText(
                    "Ⅱ"
            );

            pauseButton.setToolTipText(
                    "Pause"
            );
        }

        if (pauseAction != null) {

            pauseAction.run();
        }
    }

    public boolean isPaused() {

        return paused;
    }

    public void setPaused(
            boolean paused) {

        this.paused = paused;

        if (paused) {

            pauseButton.setText(
                    "▶"
            );

        } else {

            pauseButton.setText(
                    "Ⅱ"
            );
        }
    }

    // =========================================================
    // ACTIONS
    // =========================================================

    public void setBackAction(
            Runnable action) {

        this.backAction = action;
    }

    public void setRestartAction(
            Runnable action) {

        this.restartAction = action;
    }

    public void setPauseAction(
            Runnable action) {

        this.pauseAction = action;
    }

    // =========================================================
    // ENABLE / DISABLE CONTROLS
    // =========================================================

    public void setRestartEnabled(
            boolean enabled) {

        restartButton.setEnabled(
                enabled
        );
    }

    public void setPauseEnabled(
            boolean enabled) {

        pauseButton.setEnabled(
                enabled
        );
    }

    public void setBackEnabled(
            boolean enabled) {

        backButton.setEnabled(
                enabled
        );
    }

    // =========================================================
    // EXAMPLE MAIN
    // =========================================================

    public static void main(
            String[] args) {

        SwingUtilities.invokeLater(() -> {

            GameFrame frame =
                    new GameFrame(
                            "Demo Game",
                            "GamesMania",
                            "Medium"
                    );

            JPanel panel =
                    new JPanel();

            panel.setBackground(
                    BACKGROUND
            );

            JLabel label =
                    new JLabel(
                            "GAME AREA"
                    );

            label.setFont(
                    new Font(
                            "SansSerif",
                            Font.BOLD,
                            35
                    )
            );

            label.setForeground(
                    PURPLE
            );

            panel.add(
                    label
            );

            frame.setGameContent(
                    panel
            );

            frame.setScore(1250);

            frame.setRestartAction(
                    () ->
                            JOptionPane.showMessageDialog(
                                    frame,
                                    "Game restarted!"
                            )
            );

            frame.setPauseAction(
                    () -> {

                        if (frame.isPaused()) {

                            JOptionPane.showMessageDialog(
                                    frame,
                                    "Game Paused"
                            );

                        } else {

                            JOptionPane.showMessageDialog(
                                    frame,
                                    "Game Resumed"
                            );
                        }
                    }
            );

            frame.setVisible(true);
        });
    }
}