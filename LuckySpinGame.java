import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class LuckySpinGame extends JFrame {

    // =========================
    // GAME DATA
    // =========================
    private int coins = 960;
    private int score = 0;

    private int betAmount = 40;
    private String betType = "RED";
    private int selectedNumber = -1;

    private int winningNumber = -1;

    private boolean spinning = false;

    private final Random random = new Random();

    // =========================
    // UI
    // =========================
    private RoulettePanel roulettePanel;

    private JLabel coinsLabel;
    private JLabel scoreLabel;
    private JLabel resultLabel;
    private JLabel betLabel;
    private JLabel statusLabel;

    private JSpinner betSpinner;

    private JButton redButton;
    private JButton blackButton;
    private JButton evenButton;
    private JButton oddButton;

    private JButton minusButton;
    private JButton plusButton;
    private JButton numberButton;

    private JButton spinButton;
    private JButton backButton;

    // =========================
    // COLORS
    // =========================
    private final Color BACKGROUND = new Color(15, 19, 27);
    private final Color PANEL = new Color(25, 30, 41);
    private final Color PANEL2 = new Color(31, 37, 50);

    private final Color GOLD = new Color(245, 190, 55);
    private final Color GREEN = new Color(25, 155, 95);
    private final Color RED = new Color(200, 45, 55);
    private final Color BLUE = new Color(65, 105, 160);

    private final Color TEXT = new Color(245, 245, 245);
    private final Color MUTED = new Color(170, 180, 195);

    private String playerName = "Player";

    // =========================
    // CONSTRUCTOR
    // =========================
    public LuckySpinGame() {
        this("Player");
    }

    public LuckySpinGame(String playerName) {
        if (playerName != null && !playerName.trim().isEmpty()) {
            this.playerName = playerName.trim();
        }

        setTitle("GamesMania • Roulette • " + this.playerName);
        setSize(1200, 760);
        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        createUI();

        setVisible(true);
    }

    // =========================
    // CREATE UI
    // =========================
    private void createUI() {

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(BACKGROUND);

        // -------------------------
        // TOP BAR
        // -------------------------
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(BACKGROUND);
        topBar.setBorder(
                BorderFactory.createEmptyBorder(20, 22, 15, 22)
        );

        JLabel title = new JLabel("♠  ROULETTE");

        title.setFont(
                new Font("SansSerif", Font.BOLD, 38)
        );

        title.setForeground(GOLD);

        JLabel subtitle = new JLabel(
                "Classic European Roulette  •  Single Player"
        );

        subtitle.setFont(
                new Font("SansSerif", Font.PLAIN, 17)
        );

        subtitle.setForeground(MUTED);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(
                new BoxLayout(titlePanel, BoxLayout.Y_AXIS)
        );

        titlePanel.setBackground(BACKGROUND);

        titlePanel.add(title);
        titlePanel.add(Box.createVerticalStrut(4));
        titlePanel.add(subtitle);

        topBar.add(titlePanel, BorderLayout.WEST);

        // -------------------------
        // PLAYER INFO
        // -------------------------
        JPanel infoPanel = new JPanel(
                new FlowLayout(
                        FlowLayout.RIGHT,
                        25,
                        5
                )
        );

        infoPanel.setBackground(BACKGROUND);

        JLabel playerLabel = new JLabel("♟  Guest");

        playerLabel.setFont(
                new Font("SansSerif", Font.BOLD, 20)
        );

        playerLabel.setForeground(TEXT);

        coinsLabel = new JLabel("🪙 " + coins);

        coinsLabel.setFont(
                new Font("SansSerif", Font.BOLD, 20)
        );

        coinsLabel.setForeground(GOLD);

        scoreLabel = new JLabel("SCORE  " + score);

        scoreLabel.setFont(
                new Font("SansSerif", Font.BOLD, 20)
        );

        scoreLabel.setForeground(
                new Color(80, 170, 255)
        );

        infoPanel.add(playerLabel);
        infoPanel.add(coinsLabel);
        infoPanel.add(scoreLabel);

        topBar.add(infoPanel, BorderLayout.EAST);

        main.add(topBar, BorderLayout.NORTH);

        // -------------------------
        // CENTER
        // -------------------------
        roulettePanel = new RoulettePanel();

        JPanel wheelContainer = new JPanel(
                new BorderLayout()
        );

        wheelContainer.setBackground(BACKGROUND);

        wheelContainer.setBorder(
                BorderFactory.createEmptyBorder(
                        5, 20, 10, 20
                )
        );

        wheelContainer.add(
                roulettePanel,
                BorderLayout.CENTER
        );

        main.add(
                wheelContainer,
                BorderLayout.CENTER
        );

        // -------------------------
        // RIGHT BETTING PANEL
        // -------------------------
        JPanel rightPanel = createBetPanel();

        main.add(
                rightPanel,
                BorderLayout.EAST
        );

        setContentPane(main);
    }

    // =========================
    // BET PANEL
    // =========================
    private JPanel createBetPanel() {

        JPanel panel = new JPanel();
        panel.setPreferredSize(
                new Dimension(340, 0)
        );

        panel.setBackground(PANEL);

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        20, 20, 20, 20
                )
        );

        panel.setLayout(
                new BoxLayout(
                        panel,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel heading = new JLabel(
                "PLACE YOUR BET",
                SwingConstants.CENTER
        );

        heading.setAlignmentX(Component.CENTER_ALIGNMENT);

        heading.setFont(
                new Font("SansSerif", Font.BOLD, 28)
        );

        heading.setForeground(TEXT);

        panel.add(heading);

        panel.add(Box.createVerticalStrut(22));

        // BET AMOUNT
        JLabel amountLabel = new JLabel(
                "BET AMOUNT"
        );

        amountLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        amountLabel.setFont(
                new Font("SansSerif", Font.BOLD, 17)
        );

        amountLabel.setForeground(MUTED);

        panel.add(amountLabel);

        panel.add(Box.createVerticalStrut(8));

        betSpinner = new JSpinner(
                new SpinnerNumberModel(
                        40,
                        10,
                        500,
                        10
                )
        );

        betSpinner.setMaximumSize(
                new Dimension(260, 42)
        );

        betSpinner.setFont(
                new Font("SansSerif", Font.BOLD, 18)
        );

        ((JSpinner.DefaultEditor) betSpinner.getEditor())
                .getTextField()
                .setHorizontalAlignment(
                        JTextField.CENTER
                );

        panel.add(betSpinner);

        panel.add(Box.createVerticalStrut(20));

        // CHOOSE BET
        JLabel chooseLabel = new JLabel(
                "CHOOSE BET"
        );

        chooseLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        chooseLabel.setFont(
                new Font("SansSerif", Font.BOLD, 17)
        );

        chooseLabel.setForeground(MUTED);

        panel.add(chooseLabel);

        panel.add(Box.createVerticalStrut(10));

        redButton = createButton(
                "RED",
                RED
        );

        blackButton = createButton(
                "BLACK",
                Color.BLACK
        );

        evenButton = createButton(
                "EVEN",
                BLUE
        );

        oddButton = createButton(
                "ODD",
                BLUE
        );

        JPanel row1 = new JPanel(
                new GridLayout(1, 2, 10, 10)
        );

        row1.setBackground(PANEL);

        row1.add(redButton);
        row1.add(blackButton);

        JPanel row2 = new JPanel(
                new GridLayout(1, 2, 10, 10)
        );

        row2.setBackground(PANEL);

        row2.add(evenButton);
        row2.add(oddButton);

        panel.add(row1);
        panel.add(Box.createVerticalStrut(10));
        panel.add(row2);

        // -------------------------
        // NUMBER BET
        // -------------------------
        panel.add(Box.createVerticalStrut(25));

        JLabel numberLabel = new JLabel(
                "NUMBER BET"
        );

        numberLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        numberLabel.setFont(
                new Font("SansSerif", Font.BOLD, 17)
        );

        numberLabel.setForeground(MUTED);

        panel.add(numberLabel);

        panel.add(Box.createVerticalStrut(10));

        JPanel numberPanel = new JPanel(
                new BorderLayout(8, 0)
        );

        numberPanel.setBackground(PANEL);

        minusButton = createButton(
                "−",
                new Color(65, 72, 87)
        );

        plusButton = createButton(
                "+",
                new Color(65, 72, 87)
        );

        numberButton = createButton(
                "NO NUMBER",
                new Color(55, 62, 76)
        );

        numberPanel.add(
                minusButton,
                BorderLayout.WEST
        );

        numberPanel.add(
                numberButton,
                BorderLayout.CENTER
        );

        numberPanel.add(
                plusButton,
                BorderLayout.EAST
        );

        panel.add(numberPanel);

        // -------------------------
        // BET DISPLAY
        // -------------------------
        panel.add(Box.createVerticalStrut(20));

        betLabel = new JLabel(
                "BET: RED",
                SwingConstants.CENTER
        );

        betLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        betLabel.setFont(
                new Font("SansSerif", Font.BOLD, 20)
        );

        betLabel.setForeground(GOLD);

        panel.add(betLabel);

        // -------------------------
        // RESULT
        // -------------------------
        panel.add(Box.createVerticalStrut(15));

        statusLabel = new JLabel(
                "Choose your bet and spin!",
                SwingConstants.CENTER
        );

        statusLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        statusLabel.setFont(
                new Font("SansSerif", Font.BOLD, 17)
        );

        statusLabel.setForeground(TEXT);

        panel.add(statusLabel);

        panel.add(Box.createVerticalStrut(8));

        resultLabel = new JLabel(
                "—",
                SwingConstants.CENTER
        );

        resultLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        resultLabel.setFont(
                new Font("SansSerif", Font.BOLD, 25)
        );

        resultLabel.setForeground(GOLD);

        panel.add(resultLabel);

        // -------------------------
        // SPIN
        // -------------------------
        panel.add(Box.createVerticalStrut(18));

        spinButton = createButton(
                "SPIN",
                GREEN
        );

        spinButton.setMaximumSize(
                new Dimension(300, 55)
        );

        spinButton.setFont(
                new Font("SansSerif", Font.BOLD, 25)
        );

        panel.add(spinButton);

        panel.add(Box.createVerticalStrut(10));

        // -------------------------
        // BACK
        // -------------------------
        backButton = createButton(
                "←  BACK TO HOME",
                new Color(58, 65, 79)
        );

        backButton.setMaximumSize(
                new Dimension(300, 48)
        );

        panel.add(backButton);

        panel.add(Box.createVerticalGlue());

        // -------------------------
        // RULES
        // -------------------------
        JLabel rules = new JLabel(
                "<html><center>" +
                        "<b>RULES</b><br>" +
                        "RED / BLACK = 2×<br>" +
                        "EVEN / ODD = 2×<br>" +
                        "NUMBER = 36×<br>" +
                        "0 is GREEN and loses color bets" +
                        "</center></html>"
        );

        rules.setFont(
                new Font("SansSerif", Font.PLAIN, 13)
        );

        rules.setForeground(MUTED);

        rules.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        panel.add(rules);

        // =========================
        // ACTIONS
        // =========================

        redButton.addActionListener(e ->
                selectBet("RED")
        );

        blackButton.addActionListener(e ->
                selectBet("BLACK")
        );

        evenButton.addActionListener(e ->
                selectBet("EVEN")
        );

        oddButton.addActionListener(e ->
                selectBet("ODD")
        );

        minusButton.addActionListener(e -> {

            if (selectedNumber == -1) {
                selectedNumber = 36;
            }

            selectedNumber--;

            if (selectedNumber < 0) {
                selectedNumber = 36;
            }

            selectBet("NUMBER");
        });

        plusButton.addActionListener(e -> {

            if (selectedNumber == -1) {
                selectedNumber = 0;
            } else {
                selectedNumber++;

                if (selectedNumber > 36) {
                    selectedNumber = 0;
                }
            }

            selectBet("NUMBER");
        });

        spinButton.addActionListener(
                e -> spinRoulette()
        );

        backButton.addActionListener(
                e -> goBackHome()
        );

        return panel;
    }

    // =========================
    // BUTTON
    // =========================
    private JButton createButton(
            String text,
            Color color
    ) {

        JButton button = new JButton(text);

        button.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        16
                )
        );

        button.setForeground(Color.WHITE);
        button.setBackground(color);

        button.setFocusPainted(false);
        button.setBorderPainted(false);

        button.setPreferredSize(
                new Dimension(145, 52)
        );

        button.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        return button;
    }

    // =========================
    // SELECT BET
    // =========================
    private void selectBet(String type) {

        if (spinning) {
            return;
        }

        betType = type;

        if (type.equals("NUMBER")) {

            if (selectedNumber < 0) {
                selectedNumber = 0;
            }

            betLabel.setText(
                    "BET: NUMBER " + selectedNumber
            );

        } else {

            betLabel.setText(
                    "BET: " + type
            );
        }

        updateButtonSelection();
    }

    // =========================
    // BUTTON SELECTION
    // =========================
    private void updateButtonSelection() {

        redButton.setBorder(
                BorderFactory.createEmptyBorder()
        );

        blackButton.setBorder(
                BorderFactory.createEmptyBorder()
        );

        evenButton.setBorder(
                BorderFactory.createEmptyBorder()
        );

        oddButton.setBorder(
                BorderFactory.createEmptyBorder()
        );

        if (betType.equals("RED")) {

            redButton.setBorder(
                    BorderFactory.createLineBorder(
                            GOLD,
                            3
                    )
            );

        } else if (betType.equals("BLACK")) {

            blackButton.setBorder(
                    BorderFactory.createLineBorder(
                            GOLD,
                            3
                    )
            );

        } else if (betType.equals("EVEN")) {

            evenButton.setBorder(
                    BorderFactory.createLineBorder(
                            GOLD,
                            3
                    )
            );

        } else if (betType.equals("ODD")) {

            oddButton.setBorder(
                    BorderFactory.createLineBorder(
                            GOLD,
                            3
                    )
            );
        }
    }

    // =========================
    // SPIN
    // =========================
    private void spinRoulette() {

        if (spinning) {
            return;
        }

        betAmount =
                ((Number) betSpinner.getValue()).intValue();

        if (betAmount > coins) {

            JOptionPane.showMessageDialog(
                    this,
                    "Not enough coins!",
                    "Insufficient Coins",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        if (betAmount <= 0) {
            return;
        }

        // Remove bet from balance first
        coins -= betAmount;

        updateCoins();

        spinning = true;

        spinButton.setEnabled(false);

        statusLabel.setText(
                "Roulette is spinning..."
        );

        resultLabel.setText(
                "GOOD LUCK!"
        );

        // IMPORTANT:
        // Completely RANDOM result every spin
        winningNumber = random.nextInt(37);

        // Start wheel animation
        roulettePanel.startSpin(
                winningNumber
        );
    }

    // =========================
    // SPIN FINISHED
    // =========================
    private void spinFinished() {

        spinning = false;

        spinButton.setEnabled(true);

        String color =
                getNumberColor(winningNumber);

        boolean win = checkWin();

        if (win) {

            int winnings;

            if (betType.equals("NUMBER")) {

                winnings = betAmount * 36;

            } else {

                winnings = betAmount * 2;
            }

            coins += winnings;

            score += winnings - betAmount;

            statusLabel.setForeground(
                    new Color(60, 220, 130)
            );

            statusLabel.setText(
                    "🎉 YOU WIN!"
            );

            resultLabel.setForeground(
                    new Color(60, 220, 130)
            );

            resultLabel.setText(
                    "WIN  +"
                            + (winnings - betAmount)
                            + " coins"
            );

        } else {

            score -= betAmount / 2;

            if (score < 0) {
                score = 0;
            }

            statusLabel.setForeground(
                    new Color(240, 90, 90)
            );

            statusLabel.setText(
                    "BET LOST"
            );

            resultLabel.setForeground(
                    new Color(240, 90, 90)
            );

            resultLabel.setText(
                    "LOSS  -" + betAmount + " coins"
            );
        }

        coinsLabel.setText(
                "🪙 " + coins
        );

        scoreLabel.setText(
                "SCORE  " + score
        );

        if (score > 0) {
            try {
                ScoreDAO dao = new ScoreDAO();
                dao.saveScore(playerName, "Lucky Spin", "Coins: " + coins, score);
            } catch (Exception ignored) {}
        }

        JOptionPane.showMessageDialog(
                this,
                "Winning Number: "
                        + winningNumber
                        + "\nColor: "
                        + color
                        + "\n\n"
                        + (win
                        ? "YOU WON!"
                        : "YOU LOST!")
        );
    }

    // =========================
    // CHECK WIN
    // =========================
    private boolean checkWin() {

        // Number bet
        if (betType.equals("NUMBER")) {

            return winningNumber ==
                    selectedNumber;
        }

        // 0 does not count as even/odd
        if (winningNumber == 0) {
            return false;
        }

        if (betType.equals("RED")) {

            return isRed(winningNumber);
        }

        if (betType.equals("BLACK")) {

            return !isRed(winningNumber);
        }

        if (betType.equals("EVEN")) {

            return winningNumber % 2 == 0;
        }

        if (betType.equals("ODD")) {

            return winningNumber % 2 != 0;
        }

        return false;
    }

    // =========================
    // EUROPEAN RED NUMBERS
    // =========================
    private boolean isRed(int number) {

        int[] redNumbers = {

                1, 3, 5, 7, 9,
                12, 14, 16, 18,
                19, 21, 23, 25,
                27, 30, 32, 34, 36
        };

        for (int n : redNumbers) {

            if (n == number) {
                return true;
            }
        }

        return false;
    }

    private String getNumberColor(int number) {

        if (number == 0) {
            return "GREEN";
        }

        return isRed(number)
                ? "RED"
                : "BLACK";
    }

    // =========================
    // UPDATE COINS
    // =========================
    private void updateCoins() {

        coinsLabel.setText(
                "🪙 " + coins
        );
    }

    // =========================
    // BACK HOME
    // =========================
    private void goBackHome() {

        if (spinning) {

            int answer = JOptionPane.showConfirmDialog(
                    this,
                    "Roulette is still spinning.\nGo back to Home?",
                    "Back to Home",
                    JOptionPane.YES_NO_OPTION
            );

            if (answer != JOptionPane.YES_OPTION) {
                return;
            }
        }

        dispose();

        /*
         * If your HomeFrame class exists,
         * use:
         *
         * new HomeFrame().setVisible(true);
         *
         * Otherwise just closing this game
         * returns to the existing HomeFrame.
         */
    }

    // ==========================================================
    // CUSTOM ROULETTE WHEEL
    // ==========================================================
    private class RoulettePanel extends JPanel {

        private double angle = 0;

        private double targetAngle;

        private int currentNumber = 0;

        private Timer spinTimer;

        private long spinStart;

        private int spinDuration = 4200;

        private boolean spinningWheel = false;

        // European wheel order
        private final int[] numbers = {

                0, 32, 15, 19, 4, 21, 2,
                25, 17, 34, 6, 27, 13,
                36, 11, 30, 8, 23, 10,
                5, 24, 16, 33, 1, 20,
                14, 31, 9, 22, 18, 29,
                7, 28, 12, 35, 3, 26
        };

        public RoulettePanel() {

            setBackground(BACKGROUND);

            setPreferredSize(
                    new Dimension(800, 650)
            );
        }

        // =========================
        // START SPIN
        // =========================
        public void startSpin(int result) {

            if (spinTimer != null &&
                    spinTimer.isRunning()) {
                return;
            }

            spinningWheel = true;

            spinStart =
                    System.currentTimeMillis();

            int resultIndex = 0;

            for (int i = 0;
                 i < numbers.length;
                 i++) {

                if (numbers[i] == result) {

                    resultIndex = i;
                    break;
                }
            }

            double section =
                    2 * Math.PI /
                            numbers.length;

            /*
             * Pointer is at top.
             * Add multiple full rotations
             * so the wheel looks natural.
             */
            double rotations =
                    6 * 2 * Math.PI;

            targetAngle =
                    rotations
                            + (-Math.PI / 2)
                            - resultIndex * section;

            double current =
                    angle % (2 * Math.PI);

            targetAngle += current;

            spinTimer =
                    new Timer(
                            16,
                            e -> animateSpin()
                    );

            spinTimer.start();
        }

        // =========================
        // ANIMATE
        // =========================
        private void animateSpin() {

            long elapsed =
                    System.currentTimeMillis()
                            - spinStart;

            double progress =
                    Math.min(
                            1.0,
                            elapsed /
                                    (double) spinDuration
                    );

            // Smooth ease-out
            double eased =
                    1 -
                            Math.pow(
                                    1 - progress,
                                    4
                            );

            angle =
                    targetAngle * eased;

            repaint();

            if (progress >= 1.0) {

                spinTimer.stop();

                angle = targetAngle;

                spinningWheel = false;

                repaint();

                Timer finish =
                        new Timer(
                                500,
                                e -> spinFinished()
                        );

                finish.setRepeats(false);

                finish.start();
            }
        }

        // =========================
        // PAINT WHEEL
        // =========================
        @Override
        protected void paintComponent(
                Graphics g
        ) {

            super.paintComponent(g);

            Graphics2D g2 =
                    (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            int w = getWidth();
            int h = getHeight();

            int cx = w / 2 - 80;
            int cy = h / 2;

            int radius =
                    Math.min(
                            260,
                            Math.min(w, h) / 2 - 35
                    );

            // -------------------------
            // SHADOW
            // -------------------------
            g2.setColor(
                    new Color(0, 0, 0, 80)
            );

            g2.fillOval(
                    cx - radius - 12,
                    cy - radius - 2,
                    radius * 2 + 24,
                    radius * 2 + 24
            );

            // -------------------------
            // OUTER GOLD RING
            // -------------------------
            g2.setColor(GOLD);

            g2.fillOval(
                    cx - radius - 10,
                    cy - radius - 10,
                    radius * 2 + 20,
                    radius * 2 + 20
            );

            // -------------------------
            // WHEEL
            // -------------------------
            int wheelRadius =
                    radius - 5;

            int diameter =
                    wheelRadius * 2;

            g2.setColor(
                    new Color(40, 45, 50)
            );

            g2.fillOval(
                    cx - wheelRadius,
                    cy - wheelRadius,
                    diameter,
                    diameter
            );

            // -------------------------
            // NUMBER SECTIONS
            // -------------------------
            double section =
                    360.0 /
                            numbers.length;

            for (int i = 0;
                 i < numbers.length;
                 i++) {

                int number =
                        numbers[i];

                if (number == 0) {

                    g2.setColor(
                            new Color(
                                    25,
                                    145,
                                    85
                            )
                    );

                } else if (
                        isRed(number)
                ) {

                    g2.setColor(
                            new Color(
                                    190,
                                    35,
                                    48
                            )
                    );

                } else {

                    g2.setColor(
                            new Color(
                                    25,
                                    28,
                                    33
                            )
                    );
                }

                double start =
                        Math.toDegrees(angle)
                                - 90
                                + i * section;

                g2.fillArc(
                        cx - wheelRadius + 15,
                        cy - wheelRadius + 15,
                        diameter - 30,
                        diameter - 30,
                        (int) -start,
                        (int) -section - 1
                );
            }

            // -------------------------
            // NUMBER TEXT
            // -------------------------
            g2.setColor(Color.WHITE);

            g2.setFont(
                    new Font(
                            "SansSerif",
                            Font.BOLD,
                            15
                    )
            );

            FontMetrics fm =
                    g2.getFontMetrics();

            int textRadius =
                    wheelRadius - 47;

            for (int i = 0;
                 i < numbers.length;
                 i++) {

                double a =
                        angle
                                + i * 2 * Math.PI /
                                numbers.length
                                - Math.PI / 2;

                int x =
                        (int)
                                (cx +
                                        Math.cos(a)
                                                * textRadius);

                int y =
                        (int)
                                (cy +
                                        Math.sin(a)
                                                * textRadius);

                String text =
                        String.valueOf(
                                numbers[i]
                        );

                g2.drawString(
                        text,
                        x -
                                fm.stringWidth(text)
                                        / 2,
                        y +
                                fm.getAscent()
                                        / 2
                );
            }

            // -------------------------
            // INNER CIRCLE
            // -------------------------
            int inner =
                    105;

            g2.setColor(
                    new Color(22, 25, 31)
            );

            g2.fillOval(
                    cx - inner,
                    cy - inner,
                    inner * 2,
                    inner * 2
            );

            g2.setColor(GOLD);

            g2.setStroke(
                    new BasicStroke(4)
            );

            g2.drawOval(
                    cx - inner,
                    cy - inner,
                    inner * 2,
                    inner * 2
            );

            // -------------------------
            // CENTER TEXT
            // -------------------------
            String centerText =
                    spinningWheel
                            ? "SPIN"
                            : winningNumber >= 0
                            ? String.valueOf(
                            winningNumber
                    )
                            : "ROULETTE";

            g2.setFont(
                    new Font(
                            "SansSerif",
                            Font.BOLD,
                            28
                    )
            );

            g2.setColor(GOLD);

            FontMetrics centerFm =
                    g2.getFontMetrics();

            g2.drawString(
                    centerText,
                    cx -
                            centerFm.stringWidth(
                                    centerText
                            ) / 2,
                    cy +
                            centerFm.getAscent()
                                    / 3
            );

            // -------------------------
            // BALL
            // -------------------------
            if (spinningWheel) {

                double ballAngle =
                        angle * 1.8
                                + System.currentTimeMillis()
                                * 0.0008;

                int ballRadius =
                        wheelRadius - 75;

                int bx =
                        (int)
                                (cx +
                                        Math.cos(
                                                ballAngle
                                        ) *
                                                ballRadius);

                int by =
                        (int)
                                (cy +
                                        Math.sin(
                                                ballAngle
                                        ) *
                                                ballRadius);

                // Ball shadow
                g2.setColor(
                        new Color(
                                0,
                                0,
                                0,
                                100
                        )
                );

                g2.fillOval(
                        bx - 10,
                        by - 7,
                        20,
                        20
                );

                // Ball
                g2.setColor(Color.WHITE);

                g2.fillOval(
                        bx - 8,
                        by - 8,
                        16,
                        16
                );

                g2.setColor(
                        new Color(
                                220,
                                220,
                                220
                        )
                );

                g2.drawOval(
                        bx - 8,
                        by - 8,
                        16,
                        16
                );
            }

            // -------------------------
            // POINTER
            // -------------------------
            int pointerY =
                    cy - wheelRadius - 23;

            Polygon pointer =
                    new Polygon();

            pointer.addPoint(
                    cx,
                    pointerY + 28
            );

            pointer.addPoint(
                    cx - 14,
                    pointerY
            );

            pointer.addPoint(
                    cx + 14,
                    pointerY
            );

            g2.setColor(GOLD);

            g2.fillPolygon(pointer);

            g2.setColor(Color.WHITE);

            g2.drawPolygon(pointer);

            // -------------------------
            // SPINNING TEXT
            // -------------------------
            if (spinningWheel) {

                g2.setFont(
                        new Font(
                                "SansSerif",
                                Font.BOLD,
                                18
                        )
                );

                g2.setColor(MUTED);

                String text =
                        "ROULETTE SPINNING...";

                g2.drawString(
                        text,
                        cx -
                                g2.getFontMetrics()
                                        .stringWidth(
                                                text
                                        ) / 2,
                        cy +
                                radius +
                                35
                );
            }

            g2.dispose();
        }
    }

    // =========================
    // MAIN
    // =========================
    public static void main(
            String[] args
    ) {

        SwingUtilities.invokeLater(
                () -> new LuckySpinGame()
        );
    }
}