import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.util.Random;

public class ArcherGame extends JFrame {

    private ArcheryPanel gamePanel;

    private JLabel scoreLabel;
    private JLabel arrowsLabel;
    private JLabel accuracyLabel;
    private JLabel messageLabel;

    private JButton startButton;
    private JButton restartButton;
    private JButton backButton;

    private int score = 0;
    private int shots = 0;
    private int hits = 0;
    private boolean gameStarted = false;

    private String playerName = "Player";

    public ArcherGame() {
        this("Player");
    }

    public ArcherGame(String playerName) {
        if (playerName != null && !playerName.trim().isEmpty()) {
            this.playerName = playerName.trim();
        }

        setTitle("GamesMania • Archer");
        setSize(1050, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        createUI();

        setVisible(true);
    }

    private void createUI() {

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(new Color(241, 245, 250));

        // ================= HEADER =================

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(
                BorderFactory.createMatteBorder(
                        0, 0, 1, 0,
                        new Color(220, 225, 232)
                )
        );

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setBorder(
                BorderFactory.createEmptyBorder(
                        15, 25, 12, 10
                )
        );

        JLabel title = new JLabel("ARCHER");
        title.setFont(
                new Font("SansSerif", Font.BOLD, 32)
        );
        title.setForeground(
                new Color(30, 48, 75)
        );

        JLabel subtitle = new JLabel(
                "Aim • Shoot • Hit the Bullseye"
        );
        subtitle.setFont(
                new Font("SansSerif", Font.PLAIN, 15)
        );
        subtitle.setForeground(
                new Color(95, 110, 130)
        );

        titlePanel.add(title);
        titlePanel.add(Box.createVerticalStrut(4));
        titlePanel.add(subtitle);

        header.add(
                titlePanel,
                BorderLayout.WEST
        );

        // ================= STATS =================

        JPanel stats = new JPanel(
                new FlowLayout(
                        FlowLayout.RIGHT,
                        25,
                        23
                )
        );

        stats.setBackground(Color.WHITE);

        scoreLabel = statLabel(
                "SCORE  0",
                new Color(70, 80, 220)
        );

        arrowsLabel = statLabel(
                "ARROWS  10",
                new Color(30, 155, 110)
        );

        accuracyLabel = statLabel(
                "ACCURACY  0%",
                new Color(220, 140, 30)
        );

        stats.add(scoreLabel);
        stats.add(arrowsLabel);
        stats.add(accuracyLabel);

        header.add(
                stats,
                BorderLayout.EAST
        );

        main.add(
                header,
                BorderLayout.NORTH
        );

        // ================= GAME =================

        gamePanel = new ArcheryPanel();

        main.add(
                gamePanel,
                BorderLayout.CENTER
        );

        // ================= BOTTOM =================

        JPanel bottom = new JPanel(
                new BorderLayout()
        );

        bottom.setBackground(Color.WHITE);

        bottom.setBorder(
                BorderFactory.createMatteBorder(
                        1, 0, 0, 0,
                        new Color(220, 225, 232)
                )
        );

        messageLabel = new JLabel(
                "Press START and aim with your mouse",
                SwingConstants.CENTER
        );

        messageLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        17
                )
        );

        messageLabel.setForeground(
                new Color(70, 85, 110)
        );

        bottom.add(
                messageLabel,
                BorderLayout.CENTER
        );

        JPanel buttons = new JPanel(
                new FlowLayout(
                        FlowLayout.RIGHT,
                        10,
                        10
                )
        );

        buttons.setBackground(Color.WHITE);

        startButton = createButton(
                "START",
                new Color(65, 120, 230)
        );

        restartButton = createButton(
                "RESTART",
                new Color(35, 160, 115)
        );

        backButton = createButton(
                "BACK TO HOME",
                new Color(90, 100, 120)
        );

        restartButton.setEnabled(false);

        buttons.add(startButton);
        buttons.add(restartButton);
        buttons.add(backButton);

        bottom.add(
                buttons,
                BorderLayout.EAST
        );

        main.add(
                bottom,
                BorderLayout.SOUTH
        );

        // ================= ACTIONS =================

        startButton.addActionListener(
                e -> startGame()
        );

        restartButton.addActionListener(
                e -> startGame()
        );

        backButton.addActionListener(
                e -> backHome()
        );

        add(main);
    }

    // =====================================================
    // LABEL
    // =====================================================

    private JLabel statLabel(
            String text,
            Color color
    ) {

        JLabel label = new JLabel(text);

        label.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        16
                )
        );

        label.setForeground(color);

        return label;
    }

    // =====================================================
    // BUTTON
    // =====================================================

    private JButton createButton(
            String text,
            Color color
    ) {

        JButton button = new JButton(text);

        button.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        14
                )
        );

        button.setForeground(Color.WHITE);
        button.setBackground(color);

        button.setFocusPainted(false);
        button.setBorderPainted(false);

        button.setPreferredSize(
                new Dimension(140, 42)
        );

        return button;
    }

    // =====================================================
    // START
    // =====================================================

    private void startGame() {

        score = 0;
        shots = 0;
        hits = 0;

        gameStarted = true;

        startButton.setEnabled(false);
        startButton.setText("PLAYING");

        restartButton.setEnabled(true);

        messageLabel.setText(
                "Move your mouse to aim • Click to shoot!"
        );

        messageLabel.setForeground(
                new Color(65, 85, 115)
        );

        gamePanel.resetGame();

        updateStats();
    }

    // =====================================================
    // SHOOT
    // =====================================================

    private void shoot(
            double targetX,
            double targetY
    ) {

        if (!gameStarted) {
            return;
        }

        if (shots >= 10) {
            return;
        }

        shots++;

        gamePanel.shootArrow(
                targetX,
                targetY
        );
    }

    // =====================================================
    // RESULT
    // =====================================================

    public void registerHit(int points) {

        if (points > 0) {

            hits++;
            score += points;

            if (points == 100) {

                messageLabel.setText(
                        "BULLSEYE! +100"
                );

            } else if (points == 75) {

                messageLabel.setText(
                        "PERFECT HIT! +75"
                );

            } else if (points == 50) {

                messageLabel.setText(
                        "GREAT SHOT! +50"
                );

            } else if (points == 25) {

                messageLabel.setText(
                        "GOOD HIT! +25"
                );

            } else {

                messageLabel.setText(
                        "TARGET HIT! +10"
                );
            }

            messageLabel.setForeground(
                    new Color(30, 160, 105)
            );

        } else {

            messageLabel.setText(
                    "MISS! Try aiming closer."
            );

            messageLabel.setForeground(
                    new Color(220, 65, 65)
            );
        }

        updateStats();

        if (shots >= 10) {
            finishGame();
        }
    }

    // =====================================================
    // FINISH
    // =====================================================

    private void finishGame() {

        gameStarted = false;

        int accuracy = 0;

        if (shots > 0) {

            accuracy =
                    (hits * 100) / shots;
        }

        String rank;

        if (score >= 800) {

            rank = "MASTER ARCHER";

        } else if (score >= 600) {

            rank = "EXPERT ARCHER";

        } else if (score >= 400) {

            rank = "GREAT ARCHER";

        } else {

            rank = "KEEP PRACTICING";
        }

        messageLabel.setText(
                rank +
                        "  •  FINAL SCORE: " +
                        score +
                        "  •  ACCURACY: " +
                        accuracy +
                        "%"
        );

        startButton.setEnabled(true);
        startButton.setText("PLAY AGAIN");

        try {
            ScoreDAO dao = new ScoreDAO();
            dao.saveScore(playerName, "Archer", rank, score);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =====================================================
    // STATS
    // =====================================================

    private void updateStats() {

        scoreLabel.setText(
                "SCORE  " + score
        );

        arrowsLabel.setText(
                "ARROWS  " +
                        (10 - shots) +
                        "/10"
        );

        int accuracy = 0;

        if (shots > 0) {

            accuracy =
                    (hits * 100) / shots;
        }

        accuracyLabel.setText(
                "ACCURACY  " +
                        accuracy +
                        "%"
        );
    }

    // =====================================================
    // BACK HOME
    // =====================================================

    private void backHome() {

        dispose();

        /*
         * Change HomeFrame to the exact name
         * of your GamesMania home class.
         *
         * Example:
         *
         * new HomeFrame().setVisible(true);
         */
    }

    // =====================================================
    // GAME PANEL
    // =====================================================

    private class ArcheryPanel extends JPanel {

        private int archerX = 130;
        private int archerY = 300;

        private int targetX = 790;
        private int targetY = 300;

        private int targetDirection = 1;

        private double aimX = 790;
        private double aimY = 300;

        private boolean aiming = false;

        private boolean arrowFlying = false;

        private double arrowX;
        private double arrowY;

        private double arrowDX;
        private double arrowDY;

        private double arrowLength;

        private int arrowPoints = 0;

        private int shotX;
        private int shotY;

        private Timer animationTimer;

        public ArcheryPanel() {

            setBackground(
                    new Color(238, 243, 248)
            );

            setCursor(
                    new Cursor(
                            Cursor.CROSSHAIR_CURSOR
                    )
            );

            // Mouse movement = aim
            addMouseMotionListener(
                    new MouseMotionAdapter() {

                        @Override
                        public void mouseMoved(
                                MouseEvent e
                        ) {

                            aimX = e.getX();
                            aimY = e.getY();

                            aiming = true;

                            repaint();
                        }
                    }
            );

            // Mouse click = shoot
            addMouseListener(
                    new MouseAdapter() {

                        @Override
                        public void mousePressed(
                                MouseEvent e
                        ) {

                            if (!gameStarted) {
                                return;
                            }

                            if (arrowFlying) {
                                return;
                            }

                            shoot(
                                    e.getX(),
                                    e.getY()
                            );
                        }
                    }
            );

            startAnimation();
        }

        // =================================================
        // RESET
        // =================================================

        public void resetGame() {

            targetX = 790;
            targetY = 300;

            targetDirection = 1;

            arrowFlying = false;

            aiming = false;

            repaint();
        }

        // =================================================
        // SHOOT ARROW
        // =================================================

        public void shootArrow(
                double x,
                double y
        ) {

            arrowFlying = true;

            arrowX = archerX + 65;
            arrowY = archerY - 30;

            double dx = x - arrowX;
            double dy = y - arrowY;

            double distance =
                    Math.sqrt(
                            dx * dx +
                                    dy * dy
                    );

            if (distance == 0) {
                distance = 1;
            }

            arrowDX =
                    dx / distance * 14;

            arrowDY =
                    dy / distance * 14;

            arrowLength = 42;

            shotX = (int) x;
            shotY = (int) y;

            repaint();
        }

        // =================================================
        // ANIMATION
        // =================================================

        private void startAnimation() {

            animationTimer =
                    new Timer(
                            25,
                            e -> animate()
                    );

            animationTimer.start();
        }

        private void animate() {

            // -----------------------------
            // Move target
            // -----------------------------

            if (gameStarted &&
                    !arrowFlying) {

                targetY += targetDirection * 2;

                if (targetY < 190) {

                    targetDirection = 1;

                } else if (targetY > 410) {

                    targetDirection = -1;
                }
            }

            // -----------------------------
            // Move arrow
            // -----------------------------

            if (arrowFlying) {

                arrowX += arrowDX;
                arrowY += arrowDY;

                double distanceToTarget =
                        Math.sqrt(
                                Math.pow(
                                        arrowX - targetX,
                                        2
                                )
                                        +
                                        Math.pow(
                                                arrowY - targetY,
                                                2
                                        )
                        );

                if (distanceToTarget < 25) {

                    calculateHit();

                    arrowFlying = false;
                }

                // Arrow went outside screen
                if (arrowX < 0 ||
                        arrowX > getWidth() ||
                        arrowY < 0 ||
                        arrowY > getHeight()) {

                    arrowFlying = false;

                    registerHit(0);
                }
            }

            repaint();
        }

        // =================================================
        // HIT CALCULATION
        // =================================================

        private void calculateHit() {

            double dx =
                    shotX - targetX;

            double dy =
                    shotY - targetY;

            double distance =
                    Math.sqrt(
                            dx * dx +
                                    dy * dy
                    );

            int points;

            if (distance <= 30) {

                points = 100;

            } else if (distance <= 55) {

                points = 75;

            } else if (distance <= 85) {

                points = 50;

            } else if (distance <= 120) {

                points = 25;

            } else if (distance <= 155) {

                points = 10;

            } else {

                points = 0;
            }

            registerHit(points);
        }

        // =================================================
        // PAINT
        // =================================================

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

            drawGround(g2);

            drawAimLine(g2);

            drawTarget(g2);

            drawArcher(g2);

            if (arrowFlying) {
                drawArrow(g2);
            }

            drawAim(g2);

            g2.dispose();
        }

        // =================================================
        // GROUND
        // =================================================

        private void drawGround(
                Graphics2D g2
        ) {

            int groundY =
                    getHeight() - 80;

            g2.setColor(
                    new Color(
                            215,
                            222,
                            213
                    )
            );

            g2.fillRect(
                    0,
                    groundY,
                    getWidth(),
                    80
            );

            g2.setColor(
                    new Color(
                            180,
                            190,
                            180
                    )
            );

            g2.fillRect(
                    0,
                    groundY,
                    getWidth(),
                    3
            );

            // Small grass
            g2.setColor(
                    new Color(
                            125,
                            155,
                            120
                    )
            );

            for (int x = 0;
                 x < getWidth();
                 x += 25) {

                g2.drawLine(
                        x,
                        groundY,
                        x + 5,
                        groundY - 8
                );
            }
        }

        // =================================================
        // AIM TRAJECTORY
        // =================================================

        private void drawAimLine(
                Graphics2D g2
        ) {

            if (!gameStarted ||
                    arrowFlying ||
                    !aiming) {

                return;
            }

            int startX =
                    archerX + 65;

            int startY =
                    archerY - 30;

            g2.setColor(
                    new Color(
                            70,
                            90,
                            115,
                            100
                    )
            );

            g2.setStroke(
                    new BasicStroke(
                            2,
                            BasicStroke.CAP_ROUND,
                            BasicStroke.JOIN_ROUND,
                            0,
                            new float[]{6, 8},
                            0
                    )
            );

            g2.drawLine(
                    startX,
                    startY,
                    (int) aimX,
                    (int) aimY
            );
        }

        // =================================================
        // TARGET
        // =================================================

        private void drawTarget(
                Graphics2D g2
        ) {

            int r = 165;

            // Stand

            g2.setColor(
                    new Color(
                            90,
                            65,
                            45
                    )
            );

            g2.setStroke(
                    new BasicStroke(8)
            );

            g2.drawLine(
                    targetX,
                    targetY + 160,
                    targetX,
                    getHeight() - 80
            );

            // Target shadow

            g2.setColor(
                    new Color(
                            0,
                            0,
                            0,
                            30
                    )
            );

            g2.fillOval(
                    targetX - r + 8,
                    targetY - r + 10,
                    r * 2,
                    r * 2
            );

            // Outer white

            g2.setColor(Color.WHITE);

            g2.fillOval(
                    targetX - r,
                    targetY - r,
                    r * 2,
                    r * 2
            );

            // Black

            drawTargetCircle(
                    g2,
                    targetX,
                    targetY,
                    155,
                    Color.BLACK
            );

            // White

            drawTargetCircle(
                    g2,
                    targetX,
                    targetY,
                    125,
                    Color.WHITE
            );

            // Blue

            drawTargetCircle(
                    g2,
                    targetX,
                    targetY,
                    95,
                    new Color(
                            60,
                            115,
                            210
                    )
            );

            // Red

            drawTargetCircle(
                    g2,
                    targetX,
                    targetY,
                    65,
                    new Color(
                            215,
                            55,
                            60
                    )
            );

            // Gold

            drawTargetCircle(
                    g2,
                    targetX,
                    targetY,
                    32,
                    new Color(
                            245,
                            190,
                            45
                    )
            );

            // Center

            g2.setColor(
                    new Color(
                            255,
                            225,
                            95
                    )
            );

            g2.fillOval(
                    targetX - 7,
                    targetY - 7,
                    14,
                    14
            );

            // Outline

            g2.setColor(
                    new Color(
                            35,
                            40,
                            50
                    )
            );

            g2.setStroke(
                    new BasicStroke(3)
            );

            g2.drawOval(
                    targetX - r,
                    targetY - r,
                    r * 2,
                    r * 2
            );

            // Target label

            g2.setFont(
                    new Font(
                            "SansSerif",
                            Font.BOLD,
                            14
                    )
            );

            g2.setColor(
                    new Color(
                            65,
                            75,
                            90
                    )
            );

            g2.drawString(
                    "TARGET",
                    targetX - 28,
                    targetY + 200
            );
        }

        private void drawTargetCircle(
                Graphics2D g2,
                int x,
                int y,
                int r,
                Color color
        ) {

            g2.setColor(color);

            g2.fillOval(
                    x - r,
                    y - r,
                    r * 2,
                    r * 2
            );
        }

        // =================================================
        // ARCHER / MAN
        // =================================================

        private void drawArcher(
                Graphics2D g2
        ) {

            int x = archerX;
            int y = archerY;

            // Shadow

            g2.setColor(
                    new Color(
                            0,
                            0,
                            0,
                            35
                    )
            );

            g2.fillOval(
                    x - 40,
                    y + 155,
                    115,
                    20
            );

            // Legs

            g2.setColor(
                    new Color(
                            40,
                            55,
                            75
                    )
            );

            g2.setStroke(
                    new BasicStroke(
                            12,
                            BasicStroke.CAP_ROUND,
                            BasicStroke.JOIN_ROUND
                    )
            );

            g2.drawLine(
                    x + 10,
                    y + 75,
                    x - 10,
                    y + 150
            );

            g2.drawLine(
                    x + 35,
                    y + 75,
                    x + 55,
                    y + 150
            );

            // Shoes

            g2.setStroke(
                    new BasicStroke(10)
            );

            g2.drawLine(
                    x - 18,
                    y + 150,
                    x - 2,
                    y + 150
            );

            g2.drawLine(
                    x + 50,
                    y + 150,
                    x + 68,
                    y + 150
            );

            // Body

            g2.setColor(
                    new Color(
                            55,
                            100,
                            125
                    )
            );

            g2.fillRoundRect(
                    x - 8,
                    y + 20,
                    55,
                    75,
                    18,
                    18
            );

            // Belt

            g2.setColor(
                    new Color(
                            100,
                            65,
                            40
                    )
            );

            g2.fillRect(
                    x - 7,
                    y + 78,
                    54,
                    8
            );

            // Head

            g2.setColor(
                    new Color(
                            220,
                            165,
                            120
                    )
            );

            g2.fillOval(
                    x + 2,
                    y - 25,
                    48,
                    48
            );

            // Hair

            g2.setColor(
                    new Color(
                            65,
                            45,
                            35
                    )
            );

            g2.fillArc(
                    x + 1,
                    y - 28,
                    50,
                    38,
                    0,
                    180
            );

            // Face direction

            g2.setColor(
                    new Color(
                            45,
                            40,
                            40
                    )
            );

            g2.fillOval(
                    x + 40,
                    y - 8,
                    4,
                    4
            );

            // Front arm

            g2.setColor(
                    new Color(
                            220,
                            165,
                            120
                    )
            );

            g2.setStroke(
                    new BasicStroke(
                            9,
                            BasicStroke.CAP_ROUND,
                            BasicStroke.JOIN_ROUND
                    )
            );

            g2.drawLine(
                    x + 25,
                    y + 35,
                    x + 70,
                    y + 10
            );

            // Back arm

            g2.drawLine(
                    x + 2,
                    y + 38,
                    x - 28,
                    y + 15
            );

            // Bow

            drawBow(
                    g2,
                    x + 68,
                    y - 30
            );
        }

        // =================================================
        // BOW
        // =================================================

        private void drawBow(
                Graphics2D g2,
                int bx,
                int by
        ) {

            g2.setColor(
                    new Color(
                            105,
                            65,
                            35
                    )
            );

            g2.setStroke(
                    new BasicStroke(
                            6,
                            BasicStroke.CAP_ROUND,
                            BasicStroke.JOIN_ROUND
                    )
            );

            g2.drawArc(
                    bx - 30,
                    by - 45,
                    65,
                    90,
                    -75,
                    150
            );

            // Bow string

            g2.setColor(
                    new Color(
                            70,
                            70,
                            70
                    )
            );

            g2.setStroke(
                    new BasicStroke(2)
            );

            g2.drawLine(
                    bx,
                    by - 43,
                    bx,
                    by + 43
            );
        }

        // =================================================
        // FLYING ARROW
        // =================================================

        private void drawArrow(
                Graphics2D g2
        ) {

            double angle =
                    Math.atan2(
                            arrowDY,
                            arrowDX
                    );

            AffineTransform old =
                    g2.getTransform();

            g2.translate(
                    arrowX,
                    arrowY
            );

            g2.rotate(angle);

            // Shaft

            g2.setColor(
                    new Color(
                            75,
                            55,
                            35
                    )
            );

            g2.setStroke(
                    new BasicStroke(4)
            );

            g2.drawLine(
                    -(int) arrowLength,
                    0,
                    0,
                    0
            );

            // Arrow head

            Polygon head =
                    new Polygon();

            head.addPoint(0, 0);
            head.addPoint(-13, -6);
            head.addPoint(-10, 0);
            head.addPoint(-13, 6);

            g2.setColor(
                    new Color(
                            80,
                            85,
                            90
                    )
            );

            g2.fillPolygon(head);

            // Feathers

            g2.setColor(
                    new Color(
                            210,
                            70,
                            60
                    )
            );

            Polygon feather =
                    new Polygon();

            feather.addPoint(
                    -(int) arrowLength,
                    0
            );

            feather.addPoint(
                    -(int) arrowLength + 15,
                    -8
            );

            feather.addPoint(
                    -(int) arrowLength + 11,
                    0
            );

            feather.addPoint(
                    -(int) arrowLength + 15,
                    8
            );

            g2.fillPolygon(feather);

            g2.setTransform(old);
        }

        // =================================================
        // AIM CROSSHAIR
        // =================================================

        private void drawAim(
                Graphics2D g2
        ) {

            if (!gameStarted ||
                    !aiming ||
                    arrowFlying) {

                return;
            }

            int x = (int) aimX;
            int y = (int) aimY;

            g2.setColor(
                    new Color(
                            35,
                            45,
                            60,
                            180
                    )
            );

            g2.setStroke(
                    new BasicStroke(2)
            );

            g2.drawOval(
                    x - 13,
                    y - 13,
                    26,
                    26
            );

            g2.drawLine(
                    x - 20,
                    y,
                    x - 6,
                    y
            );

            g2.drawLine(
                    x + 6,
                    y,
                    x + 20,
                    y
            );

            g2.drawLine(
                    x,
                    y - 20,
                    x,
                    y - 6
            );

            g2.drawLine(
                    x,
                    y + 6,
                    x,
                    y + 20
            );

            g2.setColor(
                    new Color(
                            220,
                            60,
                            60
                    )
            );

            g2.fillOval(
                    x - 3,
                    y - 3,
                    6,
                    6
            );
        }
    }

    // =====================================================
    // MAIN
    // =====================================================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            try {

                UIManager.setLookAndFeel(
                        UIManager
                                .getSystemLookAndFeelClassName()
                );

            } catch (Exception ignored) {
            }

            new ArcherGame();
        });
    }
}