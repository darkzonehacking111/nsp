import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class CoinFlipGame extends JFrame {

    private JLabel resultLabel;
    private JLabel scoreLabel;
    private JLabel streakLabel;
    private JLabel flipLabel;

    private CoinPanel coinPanel;

    private int score = 0;
    private int streak = 0;
    private int totalFlips = 0;

    private boolean flipping = false;

    private String playerName = "Player";
    private final Random random = new Random();

    public CoinFlipGame() {
        this("Player");
    }

    public CoinFlipGame(String playerName) {
        if (playerName != null && !playerName.trim().isEmpty()) {
            this.playerName = playerName.trim();
        }

        setTitle("Coin Flip Arena • " + this.playerName);
        setSize(650, 760);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel main = new JPanel();
        main.setBackground(new Color(12, 15, 24));
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setBorder(
                BorderFactory.createEmptyBorder(25, 45, 25, 45)
        );

        // ================= TITLE =================

        JLabel title = new JLabel("COIN FLIP");
        title.setFont(new Font("Arial", Font.BOLD, 40));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("MAKE YOUR CALL");
        subtitle.setFont(new Font("Arial", Font.BOLD, 14));
        subtitle.setForeground(new Color(140, 150, 175));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ================= FLIP COUNTER =================

        flipLabel = new JLabel("FLIPS: 0");
        flipLabel.setFont(new Font("Arial", Font.BOLD, 14));
        flipLabel.setForeground(new Color(120, 200, 255));
        flipLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ================= COIN =================

        coinPanel = new CoinPanel();
        coinPanel.setPreferredSize(new Dimension(300, 300));
        coinPanel.setMaximumSize(new Dimension(300, 300));
        coinPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ================= RESULT =================

        resultLabel = new JLabel("Choose Heads or Tails");
        resultLabel.setFont(new Font("Arial", Font.BOLD, 23));
        resultLabel.setForeground(Color.WHITE);
        resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ================= SCORE =================

        scoreLabel = new JLabel("SCORE: 0");
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 20));
        scoreLabel.setForeground(new Color(255, 210, 70));
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        streakLabel = new JLabel("STREAK: 0");
        streakLabel.setFont(new Font("Arial", Font.BOLD, 18));
        streakLabel.setForeground(new Color(70, 230, 150));
        streakLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ================= BUTTONS =================

        JButton headsButton =
                createButton("HEADS", new Color(225, 170, 40));

        JButton tailsButton =
                createButton("TAILS", new Color(65, 110, 220));

        JButton resetButton =
                createButton("RESET", new Color(75, 80, 95));

        headsButton.addActionListener(
                e -> flip("Heads")
        );

        tailsButton.addActionListener(
                e -> flip("Tails")
        );

        resetButton.addActionListener(
                e -> resetGame()
        );

        // ================= ADD =================

        main.add(title);
        main.add(Box.createVerticalStrut(4));

        main.add(subtitle);
        main.add(Box.createVerticalStrut(5));

        main.add(flipLabel);
        main.add(Box.createVerticalStrut(5));

        main.add(coinPanel);
        main.add(Box.createVerticalStrut(5));

        main.add(resultLabel);
        main.add(Box.createVerticalStrut(12));

        main.add(scoreLabel);
        main.add(Box.createVerticalStrut(4));

        main.add(streakLabel);
        main.add(Box.createVerticalStrut(20));

        main.add(headsButton);
        main.add(Box.createVerticalStrut(10));

        main.add(tailsButton);
        main.add(Box.createVerticalStrut(12));

        main.add(resetButton);

        add(main);

        setVisible(true);
    }

    // =========================================================
    // BUTTON
    // =========================================================

    private JButton createButton(
            String text,
            Color color
    ) {

        JButton button = new JButton(text);

        button.setFont(
                new Font("Arial", Font.BOLD, 18)
        );

        button.setForeground(Color.WHITE);
        button.setBackground(color);

        button.setOpaque(true);
        button.setContentAreaFilled(true);

        button.setBorderPainted(false);
        button.setFocusPainted(false);

        button.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        button.setMaximumSize(
                new Dimension(320, 50)
        );

        button.setPreferredSize(
                new Dimension(320, 50)
        );

        button.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        // Hover animation
        button.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseEntered(MouseEvent e) {

                        button.setBackground(
                                color.brighter()
                        );

                        button.setFont(
                                new Font(
                                        "Arial",
                                        Font.BOLD,
                                        20
                                )
                        );
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {

                        button.setBackground(color);

                        button.setFont(
                                new Font(
                                        "Arial",
                                        Font.BOLD,
                                        18
                                )
                        );
                    }
                }
        );

        return button;
    }

    // =========================================================
    // FLIP LOGIC
    // =========================================================

    private void flip(String playerChoice) {

        if (flipping) {
            return;
        }

        flipping = true;

        totalFlips++;

        flipLabel.setText(
                "FLIPS: " + totalFlips
        );

        resultLabel.setText(
                "THE COIN IS FLIPPING..."
        );

        resultLabel.setForeground(
                new Color(180, 190, 210)
        );

        /*
         * TRUE RANDOM OUTCOME
         */
        String result =
                random.nextBoolean()
                        ? "Heads"
                        : "Tails";

        /*
         * Random number of rotations.
         * This makes every flip feel different.
         */
        int rotations =
                5 + random.nextInt(5);

        /*
         * Random animation speed.
         */
        int duration =
                1400 + random.nextInt(900);

        coinPanel.startFlip(
                result,
                rotations,
                duration,
                () -> {

                    if (playerChoice.equals(result)) {

                        score += 10;
                        streak++;

                        resultLabel.setText(
                                "✓ YOU WIN! " +
                                        result.toUpperCase()
                        );

                        resultLabel.setForeground(
                                new Color(70, 235, 145)
                        );

                    } else {

                        streak = 0;

                        resultLabel.setText(
                                "✗ YOU LOSE! " +
                                        result.toUpperCase()
                        );

                        resultLabel.setForeground(
                                new Color(245, 80, 90)
                        );
                    }

                    scoreLabel.setText(
                            "SCORE: " + score
                    );

                    streakLabel.setText(
                            "STREAK: " + streak
                    );

                    if (score > 0) {
                        try {
                            ScoreDAO dao = new ScoreDAO();
                            dao.saveScore(playerName, "Coin Flip", "Streak " + streak, score);
                        } catch (Exception ignored) {}
                    }

                    flipping = false;
                }
        );
    }

    // =========================================================
    // RESET
    // =========================================================

    private void resetGame() {

        if (flipping) {
            return;
        }

        score = 0;
        streak = 0;
        totalFlips = 0;

        scoreLabel.setText(
                "SCORE: 0"
        );

        streakLabel.setText(
                "STREAK: 0"
        );

        flipLabel.setText(
                "FLIPS: 0"
        );

        resultLabel.setText(
                "Choose Heads or Tails"
        );

        resultLabel.setForeground(Color.WHITE);

        coinPanel.reset();
    }

    // =========================================================
    // COIN PANEL
    // =========================================================

    class CoinPanel extends JPanel {

        private String side = "Heads";

        private double rotation = 0;
        private double scaleX = 1;

        private double bounce = 0;

        private Timer timer;

        public CoinPanel() {
            setOpaque(false);
        }

        // =====================================================
        // START ANIMATION
        // =====================================================

        public void startFlip(
                String finalSide,
                int rotations,
                int duration,
                Runnable finished
        ) {

            if (timer != null) {
                timer.stop();
            }

            long startTime =
                    System.currentTimeMillis();

            timer = new Timer(16, e -> {

                long elapsed =
                        System.currentTimeMillis()
                                - startTime;

                double progress =
                        Math.min(
                                1.0,
                                elapsed / (double) duration
                        );

                /*
                 * Smooth ease-out.
                 *
                 * Starts fast and gradually slows down.
                 */
                double eased =
                        1 -
                                Math.pow(
                                        1 - progress,
                                        3
                                );

                rotation =
                        eased *
                                rotations *
                                Math.PI * 2;

                /*
                 * Coin becomes thin while rotating.
                 */
                scaleX =
                        Math.abs(
                                Math.cos(rotation)
                        );

                if (scaleX < 0.05) {
                    scaleX = 0.05;
                }

                /*
                 * Bounce effect.
                 */
                bounce =
                        Math.sin(
                                progress * Math.PI
                        ) * 35;

                /*
                 * Change visible side while spinning.
                 */
                if (
                        ((int)
                                (rotation / Math.PI))
                                % 2 == 0
                ) {

                    side = "Heads";

                } else {

                    side = "Tails";
                }

                /*
                 * Final frame.
                 */
                if (progress >= 1) {

                    timer.stop();

                    side = finalSide;

                    scaleX = 1;
                    bounce = 0;

                    repaint();

                    if (finished != null) {
                        finished.run();
                    }

                    return;
                }

                repaint();
            });

            timer.start();
        }

        // =====================================================
        // RESET
        // =====================================================

        public void reset() {

            if (timer != null) {
                timer.stop();
            }

            side = "Heads";
            rotation = 0;
            scaleX = 1;
            bounce = 0;

            repaint();
        }

        // =====================================================
        // DRAW COIN
        // =====================================================

        @Override
        protected void paintComponent(Graphics g) {

            super.paintComponent(g);

            Graphics2D g2 =
                    (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            int centerX =
                    getWidth() / 2;

            int centerY =
                    getHeight() / 2;

            int coinSize = 190;

            int width =
                    (int)
                            (coinSize * scaleX);

            int x =
                    centerX - width / 2;

            int y =
                    centerY -
                            coinSize / 2 -
                            (int) bounce;

            // =================================================
            // GLOW
            // =================================================

            int glowSize =
                    coinSize + 35;

            for (int i = 5; i >= 0; i--) {

                int size =
                        glowSize + i * 10;

                int gx =
                        centerX - size / 2;

                int gy =
                        centerY -
                                size / 2 -
                                (int) bounce;

                g2.setColor(
                        new Color(
                                255,
                                205,
                                60,
                                10
                        )
                );

                g2.fillOval(
                        gx,
                        gy,
                        size,
                        size
                );
            }

            // =================================================
            // SHADOW
            // =================================================

            int shadowWidth =
                    Math.max(
                            30,
                            width + 25
                    );

            g2.setColor(
                    new Color(
                            0,
                            0,
                            0,
                            100
                    )
            );

            g2.fillOval(
                    centerX -
                            shadowWidth / 2,
                    centerY +
                            coinSize / 2 -
                            5,
                    shadowWidth,
                    25
            );

            // =================================================
            // COIN COLOR
            // =================================================

            if (side.equals("Heads")) {

                g2.setColor(
                        new Color(
                                255,
                                205,
                                55
                        )
                );

            } else {

                g2.setColor(
                        new Color(
                                235,
                                170,
                                35
                        )
                );
            }

            // =================================================
            // COIN
            // =================================================

            g2.fillOval(
                    x,
                    y,
                    Math.max(1, width),
                    coinSize
            );

            // =================================================
            // OUTER EDGE
            // =================================================

            g2.setStroke(
                    new BasicStroke(8)
            );

            g2.setColor(
                    new Color(
                            255,
                            225,
                            100
                    )
            );

            g2.drawOval(
                    x,
                    y,
                    Math.max(1, width),
                    coinSize
            );

            // =================================================
            // INNER EDGE
            // =================================================

            if (width > 40) {

                g2.setStroke(
                        new BasicStroke(3)
                );

                g2.setColor(
                        new Color(
                                175,
                                120,
                                20
                        )
                );

                g2.drawOval(
                        x + 15,
                        y + 15,
                        Math.max(
                                1,
                                width - 30
                        ),
                        coinSize - 30
                );
            }

            // =================================================
            // COIN LETTER
            // =================================================

            if (width > 50) {

                String letter =
                        side.equals("Heads")
                                ? "H"
                                : "T";

                g2.setColor(Color.WHITE);

                g2.setFont(
                        new Font(
                                "Arial",
                                Font.BOLD,
                                65
                        )
                );

                FontMetrics fm =
                        g2.getFontMetrics();

                int textX =
                        centerX -
                                fm.stringWidth(letter) / 2;

                int textY =
                        centerY -
                                (fm.getAscent()
                                        + fm.getDescent()) / 2
                                -
                                (int) bounce;

                g2.drawString(
                        letter,
                        textX,
                        textY
                );
            }

            // =================================================
            // SHINE
            // =================================================

            if (width > 60) {

                g2.setColor(
                        new Color(
                                255,
                                255,
                                255,
                                90
                        )
                );

                g2.fillOval(
                        x + width / 5,
                        y + coinSize / 7,
                        Math.max(
                                1,
                                width / 4
                        ),
                        Math.max(
                                1,
                                coinSize / 5
                        )
                );
            }

            g2.dispose();
        }
    }

    // =========================================================
    // MAIN
    // =========================================================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(
                CoinFlipGame::new
        );
    }
}