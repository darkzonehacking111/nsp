import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

/**
 * ============================================================
 *                       GAMESMANIA
 *                         PONG GAME
 * ============================================================
 *
 * Controls:
 *
 * W       = Move paddle up
 * S       = Move paddle down
 * SPACE   = Start / Pause
 * R       = Restart
 * ESC     = Pause
 *
 * ============================================================
 */
public class PongGame extends JFrame {

    // =========================================================
    // COLORS
    // =========================================================

    private static final Color BACKGROUND =
            new Color(246, 248, 252);

    private static final Color WHITE =
            new Color(255, 255, 255);

    private static final Color TEXT =
            new Color(35, 38, 52);

    private static final Color MUTED =
            new Color(110, 115, 130);

    private static final Color PURPLE =
            new Color(92, 83, 220);

    private static final Color MINT =
            new Color(47, 190, 167);

    private static final Color ORANGE =
            new Color(245, 175, 65);

    private static final Color RED =
            new Color(235, 80, 90);


    // =========================================================
    // GAME SETTINGS
    // =========================================================

    private static final int WIDTH =
            900;

    private static final int HEIGHT =
            550;

    private static final int PADDLE_WIDTH =
            14;

    private static final int PADDLE_HEIGHT =
            90;

    private static final int BALL_SIZE =
            18;


    // =========================================================
    // GAME DATA
    // =========================================================

    private String playerName =
            "Player";

    private String difficulty =
            "Medium";

    private int playerScore =
            0;

    private int aiScore =
            0;

    private int maxScore =
            5;


    // =========================================================
    // POSITIONS
    // =========================================================

    private double playerY;

    private double aiY;

    private double ballX;

    private double ballY;

    private double ballDX;

    private double ballDY;


    // =========================================================
    // GAME STATE
    // =========================================================

    private boolean running =
            false;

    private boolean gameOver =
            false;

    private boolean upPressed =
            false;

    private boolean downPressed =
            false;


    // =========================================================
    // RANDOM
    // =========================================================

    private final Random random =
            new Random();


    // =========================================================
    // COMPONENTS
    // =========================================================

    private PongPanel gamePanel;

    private JLabel playerScoreLabel;

    private JLabel aiScoreLabel;

    private JLabel statusLabel;

    private JButton pauseButton;

    private Timer gameTimer;


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public PongGame() {
        this("Player", "Medium");
    }

    public PongGame(String playerName) {
        this(playerName, "Medium");
    }


    public PongGame(
            String playerName,
            String difficulty) {

        if (playerName != null &&
                !playerName.trim().isEmpty()) {

            this.playerName =
                    playerName.trim();
        }

        if (difficulty != null &&
                !difficulty.trim().isEmpty()) {

            this.difficulty =
                    difficulty;
        }


        setTitle(
                "GamesMania • Mini Pong"
        );

        setSize(
                WIDTH,
                HEIGHT
        );

        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        setResizable(false);


        buildUI();

        setupKeyboard();

        initializeGame();

        startGameLoop();
    }


    // =========================================================
    // BUILD UI
    // =========================================================

    private void buildUI() {

        JPanel root =
                new JPanel(
                        new BorderLayout()
                );

        root.setBackground(
                BACKGROUND
        );

        setContentPane(
                root
        );


        createHeader(root);

        createGameArea(root);

        createFooter(root);
    }


    // =========================================================
    // HEADER
    // =========================================================

    private void createHeader(
            JPanel root) {

        JPanel header =
                new JPanel(
                        new BorderLayout()
                );

        header.setBackground(
                WHITE
        );

        header.setBorder(
                BorderFactory.createCompoundBorder(

                        BorderFactory.createMatteBorder(
                                0,
                                0,
                                1,
                                0,
                                new Color(
                                        230,
                                        232,
                                        240
                                )
                        ),

                        BorderFactory.createEmptyBorder(
                                12,
                                20,
                                12,
                                20
                        )
                )
        );


        // -----------------------------------------------------
        // TITLE
        // -----------------------------------------------------

        JLabel title =
                new JLabel(
                        "🏓  MINI PONG"
                );

        title.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        23
                )
        );

        title.setForeground(
                TEXT
        );


        header.add(
                title,
                BorderLayout.WEST
        );


        // -----------------------------------------------------
        // SCORE
        // -----------------------------------------------------

        JPanel scorePanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                15,
                                0
                        )
                );

        scorePanel.setOpaque(
                false
        );


        playerScoreLabel =
                new JLabel(
                        playerName +
                                "  0"
                );

        playerScoreLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        18
                )
        );

        playerScoreLabel.setForeground(
                PURPLE
        );


        JLabel versus =
                new JLabel(
                        "VS"
                );

        versus.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        10
                )
        );

        versus.setForeground(
                MUTED
        );


        aiScoreLabel =
                new JLabel(
                        "AI  0"
                );

        aiScoreLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        18
                )
        );

        aiScoreLabel.setForeground(
                MINT
        );


        scorePanel.add(
                playerScoreLabel
        );

        scorePanel.add(
                versus
        );

        scorePanel.add(
                aiScoreLabel
        );


        header.add(
                scorePanel,
                BorderLayout.CENTER
        );


        // -----------------------------------------------------
        // MODE
        // -----------------------------------------------------

        JLabel mode =
                new JLabel(
                        difficulty.toUpperCase()
                );

        mode.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        10
                )
        );

        mode.setForeground(
                getModeColor()
        );

        mode.setBorder(
                BorderFactory.createCompoundBorder(

                        BorderFactory.createLineBorder(
                                getModeColor()
                        ),

                        BorderFactory.createEmptyBorder(
                                6,
                                12,
                                6,
                                12
                        )
                )
        );


        header.add(
                mode,
                BorderLayout.EAST
        );


        root.add(
                header,
                BorderLayout.NORTH
        );
    }


    // =========================================================
    // GAME AREA
    // =========================================================

    private void createGameArea(
            JPanel root) {

        JPanel area =
                new JPanel(
                        new BorderLayout()
                );

        area.setBackground(
                BACKGROUND
        );

        area.setBorder(
                BorderFactory.createEmptyBorder(
                        15,
                        20,
                        10,
                        20
                )
        );


        gamePanel =
                new PongPanel();


        area.add(
                gamePanel,
                BorderLayout.CENTER
        );


        root.add(
                area,
                BorderLayout.CENTER
        );
    }


    // =========================================================
    // FOOTER
    // =========================================================

    private void createFooter(
            JPanel root) {

        JPanel footer =
                new JPanel(
                        new BorderLayout()
                );

        footer.setBackground(
                WHITE
        );

        footer.setBorder(
                BorderFactory.createEmptyBorder(
                        8,
                        20,
                        8,
                        20
                )
        );


        // -----------------------------------------------------
        // STATUS
        // -----------------------------------------------------

        statusLabel =
                new JLabel(
                        "Press SPACE to start"
                );

        statusLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        10
                )
        );

        statusLabel.setForeground(
                MUTED
        );


        footer.add(
                statusLabel,
                BorderLayout.WEST
        );


        // -----------------------------------------------------
        // BUTTONS
        // -----------------------------------------------------

        JPanel buttons =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                8,
                                0
                        )
                );

        buttons.setOpaque(
                false
        );


        pauseButton =
                createButton(
                        "▶ START",
                        PURPLE
                );


        pauseButton.addActionListener(
                e -> toggleGame()
        );


        JButton restart =
                createButton(
                        "↻ RESTART",
                        ORANGE
                );


        restart.addActionListener(
                e -> restartGame()
        );


        JButton back =
                createButton(
                        "← BACK",
                        RED
                );


        back.addActionListener(
                e -> dispose()
        );


        buttons.add(
                pauseButton
        );

        buttons.add(
                restart
        );

        buttons.add(
                back
        );


        footer.add(
                buttons,
                BorderLayout.EAST
        );


        root.add(
                footer,
                BorderLayout.SOUTH
        );
    }


    // =========================================================
    // INITIALIZE
    // =========================================================

    private void initializeGame() {

        playerY =
                HEIGHT / 2.0 -
                        PADDLE_HEIGHT / 2.0;


        aiY =
                HEIGHT / 2.0 -
                        PADDLE_HEIGHT / 2.0;


        resetBall();
    }


    // =========================================================
    // RESET BALL
    // =========================================================

    private void resetBall() {

        ballX =
                WIDTH / 2.0 -
                        BALL_SIZE / 2.0;


        ballY =
                270;


        double speed =
                getBallSpeed();


        ballDX =
                random.nextBoolean()
                        ? speed
                        : -speed;


        ballDY =
                random.nextBoolean()
                        ? speed * 0.65
                        : -speed * 0.65;
    }


    // =========================================================
    // BALL SPEED
    // =========================================================

    private double getBallSpeed() {

        if (difficulty.equalsIgnoreCase(
                "Easy"
        )) {

            return 4.0;
        }


        if (difficulty.equalsIgnoreCase(
                "Hard"
        )) {

            return 7.0;
        }


        return 5.5;
    }


    // =========================================================
    // AI SPEED
    // =========================================================

    private double getAISpeed() {

        if (difficulty.equalsIgnoreCase(
                "Easy"
        )) {

            return 3.0;
        }


        if (difficulty.equalsIgnoreCase(
                "Hard"
        )) {

            return 7.0;
        }


        return 5.0;
    }


    // =========================================================
    // GAME LOOP
    // =========================================================

    private void startGameLoop() {

        gameTimer =
                new Timer(
                        16,
                        e -> {

                            if (running &&
                                    !gameOver) {

                                updateGame();
                            }

                            gamePanel.repaint();
                        }
                );


        gameTimer.start();
    }


    // =========================================================
    // UPDATE GAME
    // =========================================================

    private void updateGame() {

        // -----------------------------------------------------
        // PLAYER MOVEMENT
        // -----------------------------------------------------

        if (upPressed) {

            playerY -=
                    getPaddleSpeed();
        }


        if (downPressed) {

            playerY +=
                    getPaddleSpeed();
        }


        playerY =
                Math.max(
                        10,
                        Math.min(
                                gamePanel.getHeight()
                                        - PADDLE_HEIGHT
                                        - 10,
                                playerY
                        )
                );


        // -----------------------------------------------------
        // AI MOVEMENT
        // -----------------------------------------------------

        double aiCenter =
                aiY +
                        PADDLE_HEIGHT / 2.0;


        double ballCenter =
                ballY +
                        BALL_SIZE / 2.0;


        double aiSpeed =
                getAISpeed();


        if (aiCenter <
                ballCenter - 8) {

            aiY +=
                    aiSpeed;

        } else if (
                aiCenter >
                        ballCenter + 8) {

            aiY -=
                    aiSpeed;
        }


        aiY =
                Math.max(
                        10,
                        Math.min(
                                gamePanel.getHeight()
                                        - PADDLE_HEIGHT
                                        - 10,
                                aiY
                        )
                );


        // -----------------------------------------------------
        // BALL
        // -----------------------------------------------------

        ballX +=
                ballDX;

        ballY +=
                ballDY;


        // -----------------------------------------------------
        // TOP / BOTTOM
        // -----------------------------------------------------

        if (ballY <= 10) {

            ballY =
                    10;

            ballDY =
                    Math.abs(
                            ballDY
                    );
        }


        if (ballY +
                BALL_SIZE >=
                gamePanel.getHeight() -
                        10) {

            ballY =
                    gamePanel.getHeight()
                            - BALL_SIZE
                            - 10;

            ballDY =
                    -Math.abs(
                            ballDY
                    );
        }


        // -----------------------------------------------------
        // PLAYER PADDLE
        // -----------------------------------------------------

        Rectangle playerPaddle =
                new Rectangle(
                        20,
                        (int) playerY,
                        PADDLE_WIDTH,
                        PADDLE_HEIGHT
                );


        Rectangle ball =
                new Rectangle(
                        (int) ballX,
                        (int) ballY,
                        BALL_SIZE,
                        BALL_SIZE
                );


        if (ball.intersects(
                playerPaddle
        ) &&
                ballDX < 0) {

            ballX =
                    20 +
                            PADDLE_WIDTH;


            ballDX =
                    Math.abs(
                            ballDX
                    );


            changeBallAngle(
                    playerY
            );
        }


        // -----------------------------------------------------
        // AI PADDLE
        // -----------------------------------------------------

        Rectangle aiPaddle =
                new Rectangle(
                        gamePanel.getWidth()
                                - 20
                                - PADDLE_WIDTH,

                        (int) aiY,

                        PADDLE_WIDTH,

                        PADDLE_HEIGHT
                );


        if (ball.intersects(
                aiPaddle
        ) &&
                ballDX > 0) {

            ballX =
                    gamePanel.getWidth()
                            - 20
                            - PADDLE_WIDTH
                            - BALL_SIZE;


            ballDX =
                    -Math.abs(
                            ballDX
                    );


            changeBallAngle(
                    aiY
            );
        }


        // -----------------------------------------------------
        // LEFT GOAL
        // -----------------------------------------------------

        if (ballX < -BALL_SIZE) {

            aiScore++;

            updateScoreLabels();

            resetBall();

            checkWinner();
        }


        // -----------------------------------------------------
        // RIGHT GOAL
        // -----------------------------------------------------

        if (ballX >
                gamePanel.getWidth()) {

            playerScore++;

            updateScoreLabels();

            resetBall();

            checkWinner();
        }
    }


    // =========================================================
    // BALL ANGLE
    // =========================================================

    private void changeBallAngle(
            double paddleY) {

        double paddleCenter =
                paddleY +
                        PADDLE_HEIGHT / 2.0;


        double ballCenter =
                ballY +
                        BALL_SIZE / 2.0;


        double difference =
                ballCenter -
                        paddleCenter;


        double normalized =
                difference /
                        (PADDLE_HEIGHT / 2.0);


        ballDY =
                normalized *
                        getBallSpeed();


        // Make ball faster after hit.
        double horizontal =
                Math.abs(
                        ballDX
                );


        horizontal +=
                0.25;


        horizontal =
                Math.min(
                        horizontal,
                        10
                );


        ballDX =
                ballDX > 0
                        ? horizontal
                        : -horizontal;
    }


    // =========================================================
    // PADDLE SPEED
    // =========================================================

    private double getPaddleSpeed() {

        if (difficulty.equalsIgnoreCase(
                "Easy"
        )) {

            return 6;
        }


        if (difficulty.equalsIgnoreCase(
                "Hard"
        )) {

            return 9;
        }


        return 7.5;
    }


    // =========================================================
    // CHECK WINNER
    // =========================================================

    private void checkWinner() {

        if (playerScore >=
                maxScore) {

            endGame(
                    "🎉 YOU WIN!"
            );

            saveScore();

            return;
        }


        if (aiScore >=
                maxScore) {

            endGame(
                    "🤖 AI WINS!"
            );

            saveScore();
        }
    }


    // =========================================================
    // END GAME
    // =========================================================

    private void endGame(
            String message) {

        gameOver =
                true;

        running =
                false;


        statusLabel.setText(
                message +
                        "  Press RESTART"
        );


        pauseButton.setText(
                "▶ START"
        );
    }


    // =========================================================
    // TOGGLE
    // =========================================================

    private void toggleGame() {

        if (gameOver) {

            return;
        }


        running =
                !running;


        if (running) {

            statusLabel.setText(
                    "W / S to move • ESC to pause"
            );

            pauseButton.setText(
                    "Ⅱ PAUSE"
            );

        } else {

            statusLabel.setText(
                    "Game paused"
            );

            pauseButton.setText(
                    "▶ RESUME"
            );
        }
    }


    // =========================================================
    // RESTART
    // =========================================================

    private void restartGame() {

        playerScore =
                0;

        aiScore =
                0;

        gameOver =
                false;

        running =
                false;


        initializeGame();

        updateScoreLabels();


        statusLabel.setText(
                "Press SPACE to start"
        );


        pauseButton.setText(
                "▶ START"
        );


        gamePanel.repaint();
    }


    // =========================================================
    // UPDATE SCORE
    // =========================================================

    private void updateScoreLabels() {

        playerScoreLabel.setText(
                playerName +
                        "  " +
                        playerScore
        );


        aiScoreLabel.setText(
                "AI  " +
                        aiScore
        );
    }


    // =========================================================
    // KEYBOARD
    // =========================================================

    private void setupKeyboard() {

        setFocusable(
                true
        );


        addKeyListener(
                new KeyAdapter() {

                    @Override
                    public void keyPressed(
                            KeyEvent e) {

                        switch (
                                e.getKeyCode()
                        ) {

                            case KeyEvent.VK_W:
                            case KeyEvent.VK_UP:
                                upPressed = true;
                                break;

                            case KeyEvent.VK_S:
                            case KeyEvent.VK_DOWN:
                                downPressed = true;
                                break;

                            case KeyEvent.VK_SPACE:
                                toggleGame();
                                break;

                            case KeyEvent.VK_ESCAPE:
                                if (!gameOver) {
                                    toggleGame();
                                }
                                break;

                            case KeyEvent.VK_R:
                                restartGame();
                                break;
                        }
                    }

                    @Override
                    public void keyReleased(KeyEvent e) {
                        switch (e.getKeyCode()) {
                            case KeyEvent.VK_W:
                            case KeyEvent.VK_UP:
                                upPressed = false;
                                break;

                            case KeyEvent.VK_S:
                            case KeyEvent.VK_DOWN:
                                downPressed = false;
                                break;
                        }
                    }
                }
        );
    }


    // =========================================================
    // SAVE SCORE
    // =========================================================

    private void saveScore() {

        /*
         * Connect this to your existing ScoreDAO.
         *
         * Example:
         *
         * ScoreDAO dao =
         *      new ScoreDAO();
         *
         * dao.saveScore(
         *      playerName,
         *      "Mini Pong",
         *      difficulty,
         *      playerScore
         * );
         */

        try {
            ScoreDAO dao = new ScoreDAO();
            dao.saveScore(playerName, "Pong", difficulty, playerScore);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // =========================================================
    // MODE COLOR
    // =========================================================

    private Color getModeColor() {

        if (difficulty.equalsIgnoreCase(
                "Easy"
        )) {

            return MINT;
        }


        if (difficulty.equalsIgnoreCase(
                "Hard"
        )) {

            return RED;
        }


        return ORANGE;
    }


    // =========================================================
    // BUTTON
    // =========================================================

    private JButton createButton(
            String text,
            Color color) {

        JButton button =
                new JButton(
                        text
                );


        button.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        9
                )
        );


        button.setForeground(
                color
        );


        button.setBackground(
                WHITE
        );


        button.setFocusPainted(
                false
        );


        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );


        button.setBorder(
                BorderFactory.createCompoundBorder(

                        BorderFactory.createLineBorder(
                                color
                        ),

                        BorderFactory.createEmptyBorder(
                                6,
                                12,
                                6,
                                12
                        )
                )
        );


        button.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseEntered(
                            MouseEvent e) {

                        button.setBackground(
                                color
                        );

                        button.setForeground(
                                WHITE
                        );
                    }


                    @Override
                    public void mouseExited(
                            MouseEvent e) {

                        button.setBackground(
                                WHITE
                        );

                        button.setForeground(
                                color
                        );
                    }
                }
        );


        return button;
    }


    // =========================================================
    // PONG PANEL
    // =========================================================

    private class PongPanel
            extends JPanel {

        PongPanel() {

            setBackground(
                    new Color(
                            250,
                            251,
                            254
                    )
            );

            setBorder(
                    BorderFactory.createLineBorder(
                            new Color(
                                    225,
                                    227,
                                    235
                            ),
                            1
                    )
            );
        }


        @Override
        protected void paintComponent(
                Graphics g) {

            super.paintComponent(
                    g
            );


            Graphics2D g2 =
                    (Graphics2D)
                            g.create();


            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );


            int width =
                    getWidth();

            int height =
                    getHeight();


            // -------------------------------------------------
            // ARENA
            // -------------------------------------------------

            g2.setColor(
                    new Color(
                            248,
                            249,
                            253
                    )
            );


            g2.fillRoundRect(
                    0,
                    0,
                    width,
                    height,
                    18,
                    18
            );


            // -------------------------------------------------
            // CENTER LINE
            // -------------------------------------------------

            g2.setColor(
                    new Color(
                            92,
                            83,
                            220,
                            35
                    )
            );


            for (
                    int y = 15;
                    y < height - 15;
                    y += 25
            ) {

                g2.fillRoundRect(
                        width / 2 - 2,
                        y,
                        4,
                        14,
                        3,
                        3
                );
            }


            // -------------------------------------------------
            // TOP / BOTTOM BORDERS
            // -------------------------------------------------

            g2.setColor(
                    new Color(
                            92,
                            83,
                            220,
                            25
                    )
            );


            g2.fillRoundRect(
                    15,
                    10,
                    width - 30,
                    3,
                    3,
                    3
            );


            g2.fillRoundRect(
                    15,
                    height - 13,
                    width - 30,
                    3,
                    3,
                    3
            );


            // -------------------------------------------------
            // PLAYER PADDLE
            // -------------------------------------------------

            drawGlowPaddle(
                    g2,
                    20,
                    (int) playerY,
                    PURPLE
            );


            // -------------------------------------------------
            // AI PADDLE
            // -------------------------------------------------

            drawGlowPaddle(
                    g2,
                    width - 20 -
                            PADDLE_WIDTH,
                    (int) aiY,
                    MINT
            );


            // -------------------------------------------------
            // BALL GLOW
            // -------------------------------------------------

            int bx =
                    (int) ballX;

            int by =
                    (int) ballY;


            g2.setColor(
                    new Color(
                            92,
                            83,
                            220,
                            25
                    )
            );


            g2.fillOval(
                    bx - 9,
                    by - 9,
                    BALL_SIZE + 18,
                    BALL_SIZE + 18
            );


            // -------------------------------------------------
            // BALL
            // -------------------------------------------------

            g2.setColor(
                    ORANGE
            );


            g2.fillOval(
                    bx,
                    by,
                    BALL_SIZE,
                    BALL_SIZE
            );


            g2.setColor(
                    WHITE
            );


            g2.fillOval(
                    bx + 5,
                    by + 4,
                    5,
                    5
            );


            // -------------------------------------------------
            // SCORE TARGET
            // -------------------------------------------------

            g2.setFont(
                    new Font(
                            "SansSerif",
                            Font.BOLD,
                            55
                    )
            );


            g2.setColor(
                    new Color(
                            92,
                            83,
                            220,
                            12
                    )
            );


            String left =
                    String.valueOf(
                            playerScore
                    );


            String right =
                    String.valueOf(
                            aiScore
                    );


            g2.drawString(
                    left,
                    width / 2 - 95,
                    75
            );


            g2.drawString(
                    right,
                    width / 2 + 65,
                    75
            );


            // -------------------------------------------------
            // PAUSE OVERLAY
            // -------------------------------------------------

            if (!running &&
                    !gameOver) {

                g2.setColor(
                        new Color(
                                255,
                                255,
                                255,
                                205
                        )
                );


                g2.fillRoundRect(
                        width / 2 - 130,
                        height / 2 - 45,
                        260,
                        90,
                        20,
                        20
                );


                g2.setColor(
                        PURPLE
                );


                g2.setFont(
                        new Font(
                                "SansSerif",
                                Font.BOLD,
                                20
                        )
                );


                String text =
                        "PRESS SPACE";


                FontMetrics fm =
                        g2.getFontMetrics();


                g2.drawString(
                        text,
                        width / 2
                                - fm.stringWidth(
                                text
                        ) / 2,
                        height / 2
                );


                g2.setFont(
                        new Font(
                                "SansSerif",
                                Font.PLAIN,
                                10
                        )
                );


                String sub =
                        "to start the match";


                fm =
                        g2.getFontMetrics();


                g2.setColor(
                        MUTED
                );


                g2.drawString(
                        sub,
                        width / 2
                                - fm.stringWidth(
                                sub
                        ) / 2,
                        height / 2 + 20
                );
            }


            // -------------------------------------------------
            // GAME OVER
            // -------------------------------------------------

            if (gameOver) {

                g2.setColor(
                        new Color(
                                255,
                                255,
                                255,
                                220
                        )
                );


                g2.fillRoundRect(
                        width / 2 - 160,
                        height / 2 - 65,
                        320,
                        130,
                        22,
                        22
                );


                String winner;


                if (playerScore >=
                        maxScore) {

                    winner =
                            "🎉 YOU WIN!";

                } else {

                    winner =
                            "🤖 AI WINS!";
                }


                g2.setFont(
                        new Font(
                                "SansSerif",
                                Font.BOLD,
                                26
                        )
                );


                g2.setColor(
                        playerScore >=
                                maxScore
                                ? PURPLE
                                : RED
                );


                FontMetrics fm =
                        g2.getFontMetrics();


                g2.drawString(
                        winner,
                        width / 2
                                - fm.stringWidth(
                                winner
                        ) / 2,
                        height / 2 - 5
                );


                g2.setFont(
                        new Font(
                                "SansSerif",
                                Font.PLAIN,
                                11
                        )
                );


                String restart =
                        "Press R or click RESTART";


                fm =
                        g2.getFontMetrics();


                g2.setColor(
                        MUTED
                );


                g2.drawString(
                        restart,
                        width / 2
                                - fm.stringWidth(
                                restart
                        ) / 2,
                        height / 2 + 22
                );
            }


            g2.dispose();
        }


        // -----------------------------------------------------
        // DRAW PADDLE
        // -----------------------------------------------------

        private void drawGlowPaddle(
                Graphics2D g2,
                int x,
                int y,
                Color color) {

            g2.setColor(
                    new Color(
                            color.getRed(),
                            color.getGreen(),
                            color.getBlue(),
                            25
                    )
            );


            g2.fillRoundRect(
                    x - 7,
                    y - 7,
                    PADDLE_WIDTH + 14,
                    PADDLE_HEIGHT + 14,
                    14,
                    14
            );


            g2.setColor(
                    color
            );


            g2.fillRoundRect(
                    x,
                    y,
                    PADDLE_WIDTH,
                    PADDLE_HEIGHT,
                    9,
                    9
            );


            g2.setColor(
                    new Color(
                            255,
                            255,
                            255,
                            110
                    )
            );


            g2.fillRoundRect(
                    x + 3,
                    y + 5,
                    3,
                    PADDLE_HEIGHT - 10,
                    3,
                    3
            );
        }
    }


    // =========================================================
    // MAIN
    // =========================================================

    public static void main(
            String[] args) {

        SwingUtilities.invokeLater(
                () -> {

                    PongGame game =
                            new PongGame(
                                    "Rishabh",
                                    "Medium"
                            );

                    game.setVisible(
                            true
                    );

                    game.requestFocusInWindow();
                }
        );
    }
}