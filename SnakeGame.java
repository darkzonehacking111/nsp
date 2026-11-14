import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class SnakeGame extends JFrame {

    private String playerName;
    private String difficulty = "Normal";

    public SnakeGame() {
        this("Guest");
    }

    public SnakeGame(String playerName) {
        this.playerName =
                (playerName == null || playerName.trim().isEmpty())
                        ? "Guest"
                        : playerName.trim();

        setTitle("GamesMania - Snake Game");
        setSize(620, 680);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        add(new SnakePanel());

        setVisible(true);
    }

    // =========================================================
    // SNAKE PANEL
    // =========================================================

    private class SnakePanel extends JPanel implements ActionListener {

        private static final int WIDTH = 600;
        private static final int HEIGHT = 600;
        private static final int UNIT = 20;
        private static final int MAX_UNITS =
                (WIDTH * HEIGHT) / (UNIT * UNIT);

        private final int[] x = new int[MAX_UNITS];
        private final int[] y = new int[MAX_UNITS];

        private int bodyParts = 5;
        private int applesEaten = 0;

        private int appleX;
        private int appleY;

        private char direction = 'R';

        private boolean running = false;
        private boolean paused = false;

        private Timer timer;

        private final Random random = new Random();

        private final int speed;

        SnakePanel() {

            if ("Easy".equalsIgnoreCase(difficulty)) {
                speed = 140;
            } else if ("Hard".equalsIgnoreCase(difficulty)) {
                speed = 70;
            } else {
                speed = 100;
            }

            setPreferredSize(new Dimension(WIDTH, HEIGHT));
            setBackground(Color.BLACK);
            setFocusable(true);

            addKeyListener(new SnakeKeyListener());

            startGame();
        }

        // =====================================================
        // START GAME
        // =====================================================

        private void startGame() {

            bodyParts = 5;
            applesEaten = 0;
            direction = 'R';
            running = true;
            paused = false;

            for (int i = 0; i < bodyParts; i++) {
                x[i] = 300 - (i * UNIT);
                y[i] = 300;
            }

            newApple();

            timer = new Timer(speed, this);
            timer.start();

            SwingUtilities.invokeLater(() -> requestFocusInWindow());
        }

        // =====================================================
        // PAINT
        // =====================================================

        @Override
        protected void paintComponent(Graphics g) {

            super.paintComponent(g);

            draw(g);
        }

        private void draw(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            // Subtle Cyber Grid Pattern
            g2.setColor(new Color(15, 20, 30));
            g2.fillRect(0, 0, WIDTH, HEIGHT);

            g2.setColor(new Color(25, 34, 48));
            for (int gx = 0; gx < WIDTH; gx += UNIT) {
                g2.drawLine(gx, 0, gx, HEIGHT);
            }
            for (int gy = 0; gy < HEIGHT; gy += UNIT) {
                g2.drawLine(0, gy, WIDTH, gy);
            }

            if (running) {
                // Apple Glow & Body
                g2.setColor(new Color(255, 60, 60, 60));
                g2.fillOval(appleX - 4, appleY - 4, UNIT + 8, UNIT + 8);

                GradientPaint appleGrad = new GradientPaint(
                        appleX + 2, appleY + 2, new Color(255, 90, 80),
                        appleX + UNIT, appleY + UNIT, new Color(190, 20, 30)
                );
                g2.setPaint(appleGrad);
                g2.fillOval(appleX + 1, appleY + 1, UNIT - 2, UNIT - 2);

                // Apple stem & leaf
                g2.setColor(new Color(120, 70, 30));
                g2.fillRect(appleX + UNIT / 2 - 1, appleY - 2, 2, 5);
                g2.setColor(new Color(60, 200, 80));
                g2.fillOval(appleX + UNIT / 2 + 1, appleY - 3, 5, 3);

                // Snake Body
                for (int i = bodyParts - 1; i >= 0; i--) {
                    if (i == 0) {
                        // Head
                        GradientPaint headGrad = new GradientPaint(
                                x[0], y[0], new Color(0, 255, 140),
                                x[0] + UNIT, y[0] + UNIT, new Color(0, 190, 90)
                        );
                        g2.setPaint(headGrad);
                        g2.fillRoundRect(x[0], y[0], UNIT, UNIT, 10, 10);

                        // Animated Eyes looking in direction
                        g2.setColor(Color.WHITE);
                        int ex1 = x[0] + 4, ey1 = y[0] + 4;
                        int ex2 = x[0] + 11, ey2 = y[0] + 4;
                        int pupilOx = 0, pupilOy = 0;

                        if (direction == 'R') {
                            ex1 = x[0] + 11; ey1 = y[0] + 4;
                            ex2 = x[0] + 11; ey2 = y[0] + 11;
                            pupilOx = 1;
                        } else if (direction == 'L') {
                            ex1 = x[0] + 4; ey1 = y[0] + 4;
                            ex2 = x[0] + 4; ey2 = y[0] + 11;
                            pupilOx = -1;
                        } else if (direction == 'U') {
                            ex1 = x[0] + 4; ey1 = y[0] + 4;
                            ex2 = x[0] + 11; ey2 = y[0] + 4;
                            pupilOy = -1;
                        } else if (direction == 'D') {
                            ex1 = x[0] + 4; ey1 = y[0] + 11;
                            ex2 = x[0] + 11; ey2 = y[0] + 11;
                            pupilOy = 1;
                        }

                        g2.fillOval(ex1, ey1, 5, 5);
                        g2.fillOval(ex2, ey2, 5, 5);

                        g2.setColor(Color.BLACK);
                        g2.fillOval(ex1 + 1 + pupilOx, ey1 + 1 + pupilOy, 3, 3);
                        g2.fillOval(ex2 + 1 + pupilOx, ey2 + 1 + pupilOy, 3, 3);

                    } else {
                        // Body Segments with smooth emerald gradient
                        float ratio = (float) i / bodyParts;
                        Color segColor = new Color(
                                (int) (10 + ratio * 20),
                                (int) (210 - ratio * 70),
                                (int) (120 - ratio * 40)
                        );
                        g2.setColor(segColor);
                        g2.fillRoundRect(x[i] + 1, y[i] + 1, UNIT - 2, UNIT - 2, 7, 7);
                    }
                }

                // Top Glass HUD Bar
                g2.setColor(new Color(15, 23, 38, 220));
                g2.fillRoundRect(10, 8, WIDTH - 20, 36, 12, 12);
                g2.setColor(new Color(0, 255, 140, 100));
                g2.setStroke(new BasicStroke(1.2f));
                g2.drawRoundRect(10, 8, WIDTH - 20, 36, 12, 12);

                g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
                g2.setColor(new Color(170, 210, 245));
                g2.drawString("PLAYER: " + playerName, 24, 31);

                g2.setColor(new Color(255, 215, 0));
                g2.drawString("APPLES: " + applesEaten + "  (" + (applesEaten * 10) + " PTS)", 240, 31);

                g2.setColor(new Color(0, 255, 170));
                g2.drawString("DIFF: " + difficulty, 470, 31);

                if (paused) {
                    g2.setColor(new Color(0, 0, 0, 170));
                    g2.fillRect(0, 0, WIDTH, HEIGHT);

                    g2.setColor(Color.WHITE);
                    g2.setFont(new Font("Segoe UI", Font.BOLD, 44));
                    drawCenteredString(g2, "PAUSED", WIDTH, HEIGHT / 2 - 10);
                    g2.setFont(new Font("Segoe UI", Font.PLAIN, 18));
                    drawCenteredString(g2, "Press SPACE to Resume", WIDTH, HEIGHT / 2 + 35);
                }

            } else {
                gameOver(g2);
            }
        }

        private void gameOver(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(new Color(10, 14, 24, 220));
            g2.fillRect(0, 0, WIDTH, HEIGHT);

            g2.setColor(new Color(255, 75, 75));
            g2.setFont(new Font("Segoe UI", Font.BOLD, 48));
            drawCenteredString(g2, "GAME OVER", WIDTH, 220);

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 26));
            drawCenteredString(g2, "Score: " + (applesEaten * 10) + " pts", WIDTH, 280);

            g2.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            g2.setColor(new Color(180, 205, 230));
            drawCenteredString(g2, "Apples Eaten: " + applesEaten, WIDTH, 318);

            g2.setColor(new Color(0, 255, 160));
            drawCenteredString(g2, "Press ENTER to Play Again", WIDTH, 380);

            g2.setColor(new Color(160, 175, 200));
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            drawCenteredString(g2, "Press ESC to Exit", WIDTH, 415);
        }

        private void drawCenteredString(
                Graphics g,
                String text,
                int width,
                int y) {

            FontMetrics metrics =
                    g.getFontMetrics();

            int textWidth =
                    metrics.stringWidth(text);

            g.drawString(
                    text,
                    (width - textWidth) / 2,
                    y
            );
        }

        // =====================================================
        // NEW APPLE
        // =====================================================

        private void newApple() {

            appleX =
                    random.nextInt(WIDTH / UNIT) * UNIT;

            appleY =
                    random.nextInt(HEIGHT / UNIT) * UNIT;

            // Don't put apple on snake
            for (int i = 0; i < bodyParts; i++) {

                if (appleX == x[i] &&
                        appleY == y[i]) {

                    newApple();
                    return;
                }
            }
        }

        // =====================================================
        // MOVE
        // =====================================================

        private void move() {

            for (int i = bodyParts; i > 0; i--) {

                x[i] = x[i - 1];
                y[i] = y[i - 1];
            }

            switch (direction) {

                case 'U':
                    y[0] -= UNIT;
                    break;

                case 'D':
                    y[0] += UNIT;
                    break;

                case 'L':
                    x[0] -= UNIT;
                    break;

                case 'R':
                    x[0] += UNIT;
                    break;
            }
        }

        // =====================================================
        // CHECK APPLE
        // =====================================================

        private void checkApple() {

            if (x[0] == appleX &&
                    y[0] == appleY) {

                bodyParts++;
                applesEaten++;

                newApple();
            }
        }

        // =====================================================
        // CHECK COLLISIONS
        // =====================================================

        private void checkCollisions() {

            // Head touching body
            for (int i = bodyParts; i > 0; i--) {

                if (x[0] == x[i] &&
                        y[0] == y[i]) {

                    running = false;
                    break;
                }
            }

            // Left wall
            if (x[0] < 0) {
                running = false;
            }

            // Right wall
            if (x[0] >= WIDTH) {
                running = false;
            }

            // Top wall
            if (y[0] < 0) {
                running = false;
            }

            // Bottom wall
            if (y[0] >= HEIGHT) {
                running = false;
            }

            if (!running) {

                if (timer != null) {
                    timer.stop();
                }

                saveScore();
            }
        }

        // =====================================================
        // GAME LOOP
        // =====================================================

        @Override
        public void actionPerformed(ActionEvent e) {

            if (running && !paused) {

                move();
                checkApple();
                checkCollisions();
            }

            repaint();
        }

        // =====================================================
        // SAVE SCORE
        // =====================================================

        private void saveScore() {

            try {

                ScoreDAO dao =
                        new ScoreDAO();

                /*
                 * This matches the overloaded method
                 * in the corrected ScoreDAO:
                 *
                 * saveScore(
                 *     String username,
                 *     String gameName,
                 *     String difficulty,
                 *     int score
                 * )
                 */

                dao.saveScore(
                        playerName,
                        "Snake",
                        difficulty,
                        applesEaten
                );

            } catch (Exception e) {

                System.out.println(
                        "Unable to save Snake score."
                );

                e.printStackTrace();
            }
        }

        // =====================================================
        // KEYBOARD
        // =====================================================

        private class SnakeKeyListener
                extends KeyAdapter {

            @Override
            public void keyPressed(KeyEvent e) {

                int key =
                        e.getKeyCode();

                // UP
                if (key == KeyEvent.VK_UP ||
                        key == KeyEvent.VK_W) {

                    if (direction != 'D') {
                        direction = 'U';
                    }
                }

                // DOWN
                else if (
                        key == KeyEvent.VK_DOWN ||
                                key == KeyEvent.VK_S) {

                    if (direction != 'U') {
                        direction = 'D';
                    }
                }

                // LEFT
                else if (
                        key == KeyEvent.VK_LEFT ||
                                key == KeyEvent.VK_A) {

                    if (direction != 'R') {
                        direction = 'L';
                    }
                }

                // RIGHT
                else if (
                        key == KeyEvent.VK_RIGHT ||
                                key == KeyEvent.VK_D) {

                    if (direction != 'L') {
                        direction = 'R';
                    }
                }

                // PAUSE
                else if (key == KeyEvent.VK_SPACE) {

                    if (running) {

                        paused = !paused;

                        if (paused) {
                            timer.stop();
                        } else {
                            timer.start();
                        }

                        repaint();
                    }
                }

                // RESTART
                else if (
                        key == KeyEvent.VK_ENTER &&
                                !running) {

                    if (timer != null) {
                        timer.stop();
                    }

                    startGame();
                    repaint();
                }

                // EXIT
                else if (key == KeyEvent.VK_ESCAPE) {

                    if (timer != null) {
                        timer.stop();
                    }

                    dispose();
                }
            }
        }
    }


    // =========================================================
    // MAIN - OPTIONAL
    // =========================================================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(
                () -> new SnakeGame("Player")
        );
    }
}