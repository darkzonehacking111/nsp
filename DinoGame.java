import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class DinoGame extends JFrame {

    private GamePanel gamePanel;
    private String playerName = "Player";

    public DinoGame() {
        this("Player");
    }

    public DinoGame(String playerName) {
        if (playerName != null && !playerName.trim().isEmpty()) {
            this.playerName = playerName.trim();
        }

        setTitle("GamesMania • Dino Runner");
        setSize(900, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        gamePanel = new GamePanel();
        add(gamePanel);

        setVisible(true);
        gamePanel.startGame();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DinoGame::new);
    }

    // =========================================================
    // GAME PANEL
    // =========================================================

    class GamePanel extends JPanel implements ActionListener, KeyListener {

        private Timer timer;

        private int dinoX = 100;
        private int dinoY = 390;

        private int dinoWidth = 55;
        private int dinoHeight = 65;

        private int groundY = 455;

        private double velocityY = 0;
        private final double gravity = 0.9;
        private final double jumpPower = -16;

        private boolean jumping = false;
        private boolean gameOver = false;

        private int obstacleX = 900;
        private int obstacleWidth = 35;
        private int obstacleHeight = 55;

        private int speed = 8;

        private int score = 0;

        private Random random = new Random();

        private long lastScoreTime = System.currentTimeMillis();

        GamePanel() {
            setBackground(new Color(245, 245, 245));
            setFocusable(true);
            addKeyListener(this);
        }

        void startGame() {
            timer = new Timer(20, this);
            timer.start();
            requestFocusInWindow();
        }

        // =====================================================
        // GAME LOOP
        // =====================================================

        @Override
        public void actionPerformed(ActionEvent e) {

            if (gameOver) {
                repaint();
                return;
            }

            // Dino physics
            if (jumping) {
                velocityY += gravity;
                dinoY += (int) velocityY;

                if (dinoY >= 390) {
                    dinoY = 390;
                    velocityY = 0;
                    jumping = false;
                }
            }

            // Move obstacle
            obstacleX -= speed;

            if (obstacleX < -obstacleWidth) {
                obstacleX = getWidth() + random.nextInt(250);

                obstacleHeight = 40 + random.nextInt(35);

                score++;

                // Increase speed slowly
                if (score % 5 == 0 && speed < 15) {
                    speed++;
                }
            }

            // Score based on time
            if (System.currentTimeMillis() - lastScoreTime > 1000) {
                score++;
                lastScoreTime = System.currentTimeMillis();
            }

            checkCollision();

            repaint();
        }

        // =====================================================
        // COLLISION
        // =====================================================

        private void checkCollision() {

            Rectangle dinoRect = new Rectangle(
                    dinoX + 8,
                    dinoY + 8,
                    dinoWidth - 16,
                    dinoHeight - 10
            );

            Rectangle obstacleRect = new Rectangle(
                    obstacleX,
                    groundY - obstacleHeight,
                    obstacleWidth,
                    obstacleHeight
            );

            if (dinoRect.intersects(obstacleRect)) {
                gameOver = true;

                if (timer != null) {
                    timer.stop();
                }

                showGameOver();
            }
        }

        // =====================================================
        // GAME OVER
        // =====================================================

        private void showGameOver() {
            saveScore(playerName);

            SwingUtilities.invokeLater(() -> {
                int choice = JOptionPane.showOptionDialog(
                        this,
                        "Game Over!\n\nPlayer: " + playerName +
                                "\nFinal Score: " + score,
                        "Dino Runner",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        new Object[]{"Play Again", "Close"},
                        "Play Again"
                );

                if (choice == JOptionPane.YES_OPTION) {
                    restartGame();
                } else {
                    dispose();
                }
            });
        }

        // =====================================================
        // SCORE SAVING
        // =====================================================

        private void saveScore(String name) {
            try {
                ScoreDAO dao = new ScoreDAO();
                dao.saveScore(name, "Dino Game", "Normal", score);
                System.out.println("Dino Score saved -> Player: " + name + ", Score: " + score);
            } catch (Exception ex) {
                System.err.println("Could not save Dino score: " + ex.getMessage());
            }
        }

        // =====================================================
        // RESTART
        // =====================================================

        private void restartGame() {

            dinoY = 390;
            velocityY = 0;

            jumping = false;
            gameOver = false;

            obstacleX = 900;
            obstacleHeight = 55;

            speed = 8;
            score = 0;

            lastScoreTime = System.currentTimeMillis();

            if (timer != null) {
                timer.start();
            } else {
                timer = new Timer(20, this);
                timer.start();
            }

            requestFocusInWindow();
            repaint();
        }

        // =====================================================
        // DRAW GAME
        // =====================================================

        @Override
        protected void paintComponent(Graphics g) {

            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            // Sky
            g2.setColor(new Color(245, 245, 245));
            g2.fillRect(0, 0, getWidth(), getHeight());

            // Clouds
            drawCloud(g2, 150, 90);
            drawCloud(g2, 500, 130);
            drawCloud(g2, 750, 70);

            // Title
            g2.setColor(Color.DARK_GRAY);
            g2.setFont(new Font("Arial", Font.BOLD, 24));
            g2.drawString("DINO GAME", 25, 40);

            // Score
            g2.setFont(new Font("Arial", Font.BOLD, 20));
            g2.drawString("Score: " + score, 730, 40);

            // Ground
            g2.setColor(new Color(70, 70, 70));
            g2.fillRect(0, groundY, getWidth(), 5);

            // Ground marks
            g2.setColor(new Color(150, 150, 150));

            for (int x = 0; x < getWidth(); x += 80) {
                g2.fillRect(x, groundY + 15, 40, 3);
            }

            // Dino
            drawDino(g2);

            // Obstacle
            drawCactus(g2);

            // Instructions
            if (!gameOver) {
                g2.setColor(Color.GRAY);
                g2.setFont(new Font("Arial", Font.PLAIN, 16));
                g2.drawString(
                        "Press SPACE or UP ARROW to jump",
                        25,
                        510
                );
            }
        }

        // =====================================================
        // DRAW DINO
        // =====================================================

        private void drawDino(Graphics2D g2) {

            int x = dinoX;
            int y = dinoY;

            g2.setColor(new Color(50, 50, 50));

            // Body
            g2.fillRoundRect(
                    x + 10,
                    y + 20,
                    40,
                    35,
                    10,
                    10
            );

            // Head
            g2.fillRoundRect(
                    x + 25,
                    y,
                    35,
                    35,
                    8,
                    8
            );

            // Neck
            g2.fillRect(
                    x + 20,
                    y + 15,
                    15,
                    25
            );

            // Tail
            Polygon tail = new Polygon();

            tail.addPoint(x + 15, y + 25);
            tail.addPoint(x - 15, y + 40);
            tail.addPoint(x + 15, y + 45);

            g2.fillPolygon(tail);

            // Legs
            g2.fillRect(
                    x + 15,
                    y + 50,
                    10,
                    15
            );

            g2.fillRect(
                    x + 38,
                    y + 50,
                    10,
                    15
            );

            // Eye
            g2.setColor(Color.WHITE);
            g2.fillOval(
                    x + 48,
                    y + 8,
                    7,
                    7
            );

            // Mouth
            g2.setColor(Color.WHITE);
            g2.drawLine(
                    x + 48,
                    y + 27,
                    x + 59,
                    y + 27
            );
        }

        // =====================================================
        // DRAW CACTUS
        // =====================================================

        private void drawCactus(Graphics2D g2) {

            int x = obstacleX;
            int y = groundY - obstacleHeight;

            g2.setColor(new Color(40, 140, 70));

            // Main cactus
            g2.fillRoundRect(
                    x,
                    y,
                    obstacleWidth,
                    obstacleHeight,
                    10,
                    10
            );

            // Left arm
            if (obstacleHeight > 45) {

                g2.fillRoundRect(
                        x - 15,
                        y + 18,
                        15,
                        12,
                        8,
                        8
                );

                g2.fillRoundRect(
                        x - 15,
                        y + 8,
                        10,
                        25,
                        8,
                        8
                );
            }

            // Right arm
            if (obstacleHeight > 50) {

                g2.fillRoundRect(
                        x + obstacleWidth,
                        y + 25,
                        15,
                        12,
                        8,
                        8
                );

                g2.fillRoundRect(
                        x + obstacleWidth + 5,
                        y + 15,
                        10,
                        25,
                        8,
                        8
                );
            }
        }

        // =====================================================
        // CLOUD
        // =====================================================

        private void drawCloud(Graphics2D g2, int x, int y) {

            g2.setColor(Color.WHITE);

            g2.fillOval(x, y, 50, 25);
            g2.fillOval(x + 20, y - 15, 45, 40);
            g2.fillOval(x + 45, y, 50, 25);
        }

        // =====================================================
        // KEYBOARD
        // =====================================================

        @Override
        public void keyPressed(KeyEvent e) {

            if (gameOver) {
                return;
            }

            if (e.getKeyCode() == KeyEvent.VK_SPACE ||
                    e.getKeyCode() == KeyEvent.VK_UP) {

                jump();
            }
        }

        private void jump() {

            if (!jumping) {

                jumping = true;
                velocityY = jumpPower;
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }

        @Override
        public void keyTyped(KeyEvent e) {
        }
    }
}