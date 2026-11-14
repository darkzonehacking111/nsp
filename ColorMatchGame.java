import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class ColorMatchGame extends JFrame {

    // ================= GAME VARIABLES =================

    private final Random random = new Random();

    private JLabel targetLabel;
    private JLabel scoreLabel;
    private JLabel comboLabel;
    private JLabel timeLabel;
    private JLabel messageLabel;

    private JPanel targetPanel;
    private JPanel cardsPanel;

    private Timer gameTimer;

    private int score = 0;
    private int combo = 0;
    private int timeLeft = 30;

    private boolean roundActive = true;

    private int correctColor;

    // ================= COLORS =================

    private final String[] colorNames = {
            "RED",
            "BLUE",
            "GREEN",
            "YELLOW",
            "ORANGE",
            "PURPLE"
    };

    private final Color[] colors = {
            new Color(235, 70, 70),      // RED
            new Color(65, 125, 235),     // BLUE
            new Color(55, 195, 110),     // GREEN
            new Color(245, 195, 55),     // YELLOW
            new Color(245, 135, 45),     // ORANGE
            new Color(160, 90, 225)      // PURPLE
    };

    private String playerName = "Player";

    // ================= CONSTRUCTOR =================

    public ColorMatchGame() {
        this("Player");
    }

    public ColorMatchGame(String playerName) {
        if (playerName != null && !playerName.trim().isEmpty()) {
            this.playerName = playerName.trim();
        }

        setTitle("Color Match • " + this.playerName);
        setSize(900, 750);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // ================= MAIN PANEL =================

        JPanel mainPanel = new JPanel();

        mainPanel.setBackground(
                new Color(13, 16, 25)
        );

        mainPanel.setLayout(
                new BorderLayout()
        );

        // ================= HEADER =================

        JPanel header = new JPanel(
                new BorderLayout()
        );

        header.setBackground(
                new Color(20, 24, 35)
        );

        header.setBorder(
                new EmptyBorder(
                        15, 25, 15, 25
                )
        );

        JLabel title = new JLabel(
                "COLOR MATCH"
        );

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        34
                )
        );

        title.setForeground(Color.WHITE);

        JLabel player = new JLabel(
                "Player: Guest"
        );

        player.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        20
                )
        );

        player.setForeground(
                new Color(180, 195, 220)
        );

        header.add(
                title,
                BorderLayout.WEST
        );

        header.add(
                player,
                BorderLayout.EAST
        );

        mainPanel.add(
                header,
                BorderLayout.NORTH
        );

        // ================= CENTER =================

        JPanel centerPanel = new JPanel();

        centerPanel.setBackground(
                new Color(13, 16, 25)
        );

        centerPanel.setLayout(
                new BoxLayout(
                        centerPanel,
                        BoxLayout.Y_AXIS
                )
        );

        centerPanel.setBorder(
                new EmptyBorder(
                        30, 45, 25, 45
                )
        );

        // ================= INSTRUCTION =================

        JLabel instruction = new JLabel(
                "Click the card that matches the target color"
        );

        instruction.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        22
                )
        );

        instruction.setForeground(
                new Color(185, 195, 215)
        );

        instruction.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        centerPanel.add(instruction);

        centerPanel.add(
                Box.createVerticalStrut(20)
        );

        // ================= TARGET =================

        targetPanel = new JPanel();

        targetPanel.setPreferredSize(
                new Dimension(600, 250)
        );

        targetPanel.setMaximumSize(
                new Dimension(600, 250)
        );

        targetPanel.setMinimumSize(
                new Dimension(600, 250)
        );

        targetPanel.setBackground(colors[0]);

        targetPanel.setOpaque(true);

        targetPanel.setBorder(
                BorderFactory.createLineBorder(
                        Color.WHITE,
                        4
                )
        );

        targetPanel.setLayout(
                new GridBagLayout()
        );

        targetLabel = new JLabel(
                "RED"
        );

        targetLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        58
                )
        );

        targetLabel.setForeground(
                Color.WHITE
        );

        targetPanel.add(
                targetLabel
        );

        targetPanel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        centerPanel.add(
                targetPanel
        );

        centerPanel.add(
                Box.createVerticalStrut(20)
        );

        // ================= MESSAGE =================

        messageLabel = new JLabel(
                "Choose a card!"
        );

        messageLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        25
                )
        );

        messageLabel.setForeground(
                Color.WHITE
        );

        messageLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        centerPanel.add(
                messageLabel
        );

        centerPanel.add(
                Box.createVerticalStrut(15)
        );

        // ================= STATS =================

        JPanel statsPanel = new JPanel(
                new GridLayout(
                        1,
                        3,
                        35,
                        0
                )
        );

        statsPanel.setOpaque(false);

        scoreLabel = createStatLabel(
                "SCORE: 0",
                new Color(255, 210, 50)
        );

        comboLabel = createStatLabel(
                "COMBO: 0",
                new Color(70, 230, 145)
        );

        timeLabel = createStatLabel(
                "TIME: 30",
                new Color(50, 210, 240)
        );

        statsPanel.add(scoreLabel);
        statsPanel.add(comboLabel);
        statsPanel.add(timeLabel);

        statsPanel.setMaximumSize(
                new Dimension(
                        650,
                        50
                )
        );

        centerPanel.add(
                statsPanel
        );

        centerPanel.add(
                Box.createVerticalStrut(25)
        );

        // ================= COLOR CARDS =================

        cardsPanel = new JPanel(
                new GridLayout(
                        2,
                        3,
                        15,
                        15
                )
        );

        cardsPanel.setOpaque(false);

        cardsPanel.setMaximumSize(
                new Dimension(
                        800,
                        140
                )
        );

        centerPanel.add(
                cardsPanel
        );

        centerPanel.add(
                Box.createVerticalStrut(20)
        );

        // ================= RESET BUTTON =================

        JButton resetButton = new JButton(
                "RESET GAME"
        );

        resetButton.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        16
                )
        );

        resetButton.setForeground(
                Color.WHITE
        );

        resetButton.setBackground(
                new Color(
                        65,
                        70,
                        85
                )
        );

        resetButton.setOpaque(true);
        resetButton.setContentAreaFilled(true);

        resetButton.setFocusPainted(false);
        resetButton.setBorderPainted(false);

        resetButton.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        resetButton.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        resetButton.setPreferredSize(
                new Dimension(
                        180,
                        42
                )
        );

        resetButton.addActionListener(
                e -> resetGame()
        );

        centerPanel.add(
                resetButton
        );

        mainPanel.add(
                centerPanel,
                BorderLayout.CENTER
        );

        add(mainPanel);

        // ================= START GAME =================

        generateNewRound();
        startTimer();

        setVisible(true);
    }

    // =====================================================
    // STAT LABEL
    // =====================================================

    private JLabel createStatLabel(
            String text,
            Color color
    ) {

        JLabel label = new JLabel(
                text,
                SwingConstants.CENTER
        );

        label.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        20
                )
        );

        label.setForeground(color);

        return label;
    }

    // =====================================================
    // CREATE COLOR CARD
    // =====================================================

    private JButton createColorCard(
            String name,
            Color color,
            int index
    ) {

        JButton button = new JButton(
                name
        );

        button.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        20
                )
        );

        // IMPORTANT:
        // Makes background color clearly visible
        button.setOpaque(true);
        button.setContentAreaFilled(true);

        button.setBackground(color);

        button.setForeground(Color.WHITE);

        button.setFocusPainted(false);

        button.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                Color.WHITE,
                                2
                        ),
                        BorderFactory.createEmptyBorder(
                                8,
                                8,
                                8,
                                8
                        )
                )
        );

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        // ================= CLICK =================

        button.addActionListener(
                e -> checkAnswer(index)
        );

        // ================= HOVER =================

        button.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseEntered(
                            MouseEvent e
                    ) {

                        if (!roundActive) {
                            return;
                        }

                        button.setBorder(
                                BorderFactory.createLineBorder(
                                        Color.WHITE,
                                        4
                                )
                        );

                        button.setFont(
                                new Font(
                                        "Arial",
                                        Font.BOLD,
                                        22
                                )
                        );

                        button.setBackground(
                                color.brighter()
                        );
                    }

                    @Override
                    public void mouseExited(
                            MouseEvent e
                    ) {

                        button.setBorder(
                                BorderFactory.createCompoundBorder(
                                        BorderFactory.createLineBorder(
                                                Color.WHITE,
                                                2
                                        ),
                                        BorderFactory.createEmptyBorder(
                                                8,
                                                8,
                                                8,
                                                8
                                        )
                                )
                        );

                        button.setFont(
                                new Font(
                                        "Arial",
                                        Font.BOLD,
                                        20
                                )
                        );

                        button.setBackground(
                                color
                        );
                    }
                }
        );

        return button;
    }

    // =====================================================
    // NEW ROUND
    // =====================================================

    private void generateNewRound() {

        roundActive = true;

        /*
         * Randomly select correct color
         */
        correctColor =
                random.nextInt(
                        colors.length
                );

        targetPanel.setBackground(
                colors[correctColor]
        );

        targetLabel.setText(
                colorNames[correctColor]
        );

        // Make target text clearly visible
        targetLabel.setForeground(
                Color.WHITE
        );

        // Remove previous cards
        cardsPanel.removeAll();

        /*
         * Randomize card positions
         */
        int[] indexes = {
                0, 1, 2, 3, 4, 5
        };

        shuffle(indexes);

        for (int index : indexes) {

            JButton card =
                    createColorCard(
                            colorNames[index],
                            colors[index],
                            index
                    );

            cardsPanel.add(card);
        }

        cardsPanel.revalidate();
        cardsPanel.repaint();

        messageLabel.setText(
                "Choose a card!"
        );

        messageLabel.setForeground(
                Color.WHITE
        );
    }

    // =====================================================
    // SHUFFLE
    // =====================================================

    private void shuffle(
            int[] array
    ) {

        for (
                int i = array.length - 1;
                i > 0;
                i--
        ) {

            int j =
                    random.nextInt(
                            i + 1
                    );

            int temp =
                    array[i];

            array[i] =
                    array[j];

            array[j] =
                    temp;
        }
    }

    // =====================================================
    // CHECK ANSWER
    // =====================================================

    private void checkAnswer(
            int selectedColor
    ) {

        if (!roundActive) {
            return;
        }

        roundActive = false;

        if (
                selectedColor ==
                        correctColor
        ) {

            combo++;

            int points =
                    10 + (combo - 1) * 2;

            score += points;

            messageLabel.setText(
                    "✓ CORRECT! +" + points
            );

            messageLabel.setForeground(
                    new Color(
                            60,
                            235,
                            140
                    )
            );

        } else {

            combo = 0;

            messageLabel.setText(
                    "✗ WRONG! Correct: "
                            + colorNames[correctColor]
            );

            messageLabel.setForeground(
                    new Color(
                            245,
                            80,
                            90
                    )
            );
        }

        scoreLabel.setText(
                "SCORE: " + score
        );

        comboLabel.setText(
                "COMBO: " + combo
        );

        /*
         * Wait briefly before next round
         */
        Timer nextRound =
                new Timer(
                        700,
                        e -> generateNewRound()
                );

        nextRound.setRepeats(false);
        nextRound.start();
    }

    // =====================================================
    // TIMER
    // =====================================================

    private void startTimer() {

        gameTimer =
                new Timer(
                        1000,
                        e -> {

                            timeLeft--;

                            timeLabel.setText(
                                    "TIME: " + timeLeft
                            );

                            if (
                                    timeLeft <= 5
                            ) {

                                timeLabel.setForeground(
                                        new Color(
                                                245,
                                                80,
                                                80
                                        )
                                );
                            }

                            if (
                                    timeLeft <= 0
                            ) {

                                gameTimer.stop();

                                roundActive = false;

                                messageLabel.setText(
                                        "TIME UP! FINAL SCORE: "
                                                + score
                                );

                                messageLabel.setForeground(
                                        new Color(
                                                255,
                                                210,
                                                50
                                        )
                                );

                                try {
                                    ScoreDAO dao = new ScoreDAO();
                                    dao.saveScore(playerName, "Color Match", "Normal", score);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }
                );

        gameTimer.start();
    }

    // =====================================================
    // RESET GAME
    // =====================================================

    private void resetGame() {

        if (gameTimer != null) {
            gameTimer.stop();
        }

        score = 0;
        combo = 0;
        timeLeft = 30;

        scoreLabel.setText(
                "SCORE: 0"
        );

        comboLabel.setText(
                "COMBO: 0"
        );

        timeLabel.setText(
                "TIME: 30"
        );

        timeLabel.setForeground(
                new Color(
                        50,
                        210,
                        240
                )
        );

        generateNewRound();

        startTimer();
    }

    // =====================================================
    // MAIN
    // =====================================================

    public static void main(
            String[] args
    ) {

        SwingUtilities.invokeLater(
                ColorMatchGame::new
        );
    }
}