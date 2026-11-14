import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class AsteroidGame extends JFrame {

    private final String playerName;

    public AsteroidGame() {
        this("Guest");
    }

    public AsteroidGame(String playerName) {
        this.playerName =
                (playerName == null || playerName.trim().isEmpty())
                        ? "Guest"
                        : playerName.trim();

        setTitle("GamesMania - Asteroid Game");
        setSize(650, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        add(new GamePanel());

        setVisible(true);
    }

    private class GamePanel extends JPanel
            implements ActionListener, KeyListener {

        private final Random random = new Random();

        private final Timer timer;

        private int playerX = 300;
        private final int playerY = 580;

        private int score = 0;
        private int lives = 3;

        private boolean gameOver = false;

        private boolean leftPressed = false;
        private boolean rightPressed = false;

        private final ArrayList<Rectangle> bullets =
                new ArrayList<>();

        private final ArrayList<Rectangle> asteroids =
                new ArrayList<>();

        private int asteroidTimer = 0;

        GamePanel() {

            setBackground(Color.BLACK);

            setFocusable(true);

            addKeyListener(this);

            timer = new Timer(25, this);

            timer.start();

            SwingUtilities.invokeLater(
                    () -> requestFocusInWindow()
            );
        }

        @Override
        protected void paintComponent(Graphics g) {

            super.paintComponent(g);

            Graphics2D g2 =
                    (Graphics2D) g;

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            // ================= STARS =================

            g2.setColor(Color.WHITE);

            for (int i = 0; i < 80; i++) {

                int x =
                        (i * 83) % getWidth();

                int y =
                        (i * 137) % getHeight();

                g2.fillOval(
                        x,
                        y,
                        2,
                        2
                );
            }

            // ================= HEADER =================

            g2.setFont(
                    new Font(
                            "Arial",
                            Font.BOLD,
                            18
                    )
            );

            g2.setColor(Color.WHITE);

            g2.drawString(
                    "Player: " + playerName,
                    15,
                    25
            );

            g2.drawString(
                    "Score: " + score,
                    15,
                    50
            );

            g2.drawString(
                    "Lives: " + lives,
                    520,
                    25
            );

            // ================= PLAYER =================

            drawPlayer(
                    g2,
                    playerX,
                    playerY
            );

            // ================= BULLETS =================

            g2.setColor(Color.YELLOW);

            for (Rectangle bullet : bullets) {

                g2.fillRect(
                        bullet.x,
                        bullet.y,
                        bullet.width,
                        bullet.height
                );
            }

            // ================= ASTEROIDS =================

            for (Rectangle asteroid : asteroids) {

                drawAsteroid(
                        g2,
                        asteroid
                );
            }

            // ================= CONTROLS =================

            g2.setFont(
                    new Font(
                            "Arial",
                            Font.PLAIN,
                            14
                    )
            );

            g2.setColor(Color.LIGHT_GRAY);

            g2.drawString(
                    "← → Move     SPACE Shoot",
                    205,
                    getHeight() - 20
            );

            // ================= GAME OVER =================

            if (gameOver) {

                g2.setColor(
                        new Color(
                                0,
                                0,
                                0,
                                190
                        )
                );

                g2.fillRect(
                        0,
                        0,
                        getWidth(),
                        getHeight()
                );

                g2.setColor(Color.RED);

                g2.setFont(
                        new Font(
                                "Arial",
                                Font.BOLD,
                                45
                        )
                );

                g2.drawString(
                        "GAME OVER",
                        195,
                        300
                );

                g2.setColor(Color.WHITE);

                g2.setFont(
                        new Font(
                                "Arial",
                                Font.BOLD,
                                22
                        )
                );

                g2.drawString(
                        "Score: " + score,
                        270,
                        345
                );

                g2.setFont(
                        new Font(
                                "Arial",
                                Font.PLAIN,
                                18
                        )
                );

                g2.drawString(
                        "Press R to restart",
                        235,
                        390
                );
            }
        }

        // =====================================================
        // PLAYER
        // =====================================================

        private void drawPlayer(
                Graphics2D g2,
                int x,
                int y
        ) {

            int[] xPoints = {
                    x + 25,
                    x,
                    x + 50
            };

            int[] yPoints = {
                    y,
                    y + 50,
                    y + 50
            };

            g2.setColor(Color.CYAN);

            g2.fillPolygon(
                    xPoints,
                    yPoints,
                    3
            );

            g2.setColor(Color.BLUE);

            g2.fillRect(
                    x + 15,
                    y + 35,
                    20,
                    15
            );

            g2.setColor(Color.ORANGE);

            g2.fillOval(
                    x + 18,
                    y + 45,
                    14,
                    15
            );
        }

        // =====================================================
        // ASTEROID
        // =====================================================

        private void drawAsteroid(
                Graphics2D g2,
                Rectangle asteroid
        ) {

            int x = asteroid.x;
            int y = asteroid.y;
            int size = asteroid.width;

            g2.setColor(
                    new Color(
                            130,
                            130,
                            130
                    )
            );

            int[] xPoints = {
                    x + size / 2,
                    x + size,
                    x + size - 8,
                    x + 10,
                    x,
                    x + 8
            };

            int[] yPoints = {
                    y,
                    y + 10,
                    y + size - 5,
                    y + size,
                    y + size / 2,
                    y + 8
            };

            g2.fillPolygon(
                    xPoints,
                    yPoints,
                    6
            );

            g2.setColor(
                    new Color(
                            80,
                            80,
                            80
                    )
            );

            g2.fillOval(
                    x + size / 3,
                    y + size / 3,
                    8,
                    8
            );

            g2.fillOval(
                    x + size / 2,
                    y + size / 2,
                    6,
                    6
            );
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

            // ================= PLAYER MOVE =================

            if (leftPressed) {
                playerX -= 7;
            }

            if (rightPressed) {
                playerX += 7;
            }

            if (playerX < 10) {
                playerX = 10;
            }

            if (playerX > getWidth() - 60) {
                playerX = getWidth() - 60;
            }

            // ================= BULLETS =================

            Iterator<Rectangle> bulletIterator =
                    bullets.iterator();

            while (bulletIterator.hasNext()) {

                Rectangle bullet =
                        bulletIterator.next();

                bullet.y -= 12;

                if (bullet.y < 0) {

                    bulletIterator.remove();
                }
            }

            // ================= CREATE ASTEROIDS =================

            asteroidTimer++;

            if (asteroidTimer >= 30) {

                asteroidTimer = 0;

                createAsteroid();
            }

            // ================= MOVE ASTEROIDS =================

            Iterator<Rectangle> asteroidIterator =
                    asteroids.iterator();

            while (asteroidIterator.hasNext()) {

                Rectangle asteroid =
                        asteroidIterator.next();

                asteroid.y += 5;

                // Asteroid escaped
                if (asteroid.y > getHeight()) {

                    asteroidIterator.remove();

                    lives--;

                    if (lives <= 0) {

                        endGame();

                        return;
                    }
                }
            }

            // ================= BULLET COLLISION =================

            checkBulletCollisions();

            // ================= PLAYER COLLISION =================

            checkPlayerCollision();

            repaint();
        }

        // =====================================================
        // CREATE ASTEROID
        // =====================================================

        private void createAsteroid() {

            int size =
                    30 + random.nextInt(25);

            int x =
                    random.nextInt(
                            Math.max(
                                    1,
                                    getWidth() - size
                            )
                    );

            asteroids.add(
                    new Rectangle(
                            x,
                            -size,
                            size,
                            size
                    )
            );
        }

        // =====================================================
        // SHOOT
        // =====================================================

        private void shoot() {

            if (gameOver) {
                return;
            }

            bullets.add(
                    new Rectangle(
                            playerX + 23,
                            playerY - 15,
                            5,
                            15
                    )
            );
        }

        // =====================================================
        // BULLET COLLISIONS
        // =====================================================

        private void checkBulletCollisions() {

            Iterator<Rectangle> bulletIterator =
                    bullets.iterator();

            while (bulletIterator.hasNext()) {

                Rectangle bullet =
                        bulletIterator.next();

                boolean hit = false;

                Iterator<Rectangle> asteroidIterator =
                        asteroids.iterator();

                while (asteroidIterator.hasNext()) {

                    Rectangle asteroid =
                            asteroidIterator.next();

                    if (bullet.intersects(asteroid)) {

                        asteroidIterator.remove();

                        score += 10;

                        hit = true;

                        break;
                    }
                }

                if (hit) {

                    bulletIterator.remove();
                }
            }
        }

        // =====================================================
        // PLAYER COLLISION
        // =====================================================

        private void checkPlayerCollision() {

            Rectangle player =
                    new Rectangle(
                            playerX,
                            playerY,
                            50,
                            60
                    );

            Iterator<Rectangle> iterator =
                    asteroids.iterator();

            while (iterator.hasNext()) {

                Rectangle asteroid =
                        iterator.next();

                if (player.intersects(asteroid)) {

                    iterator.remove();

                    lives--;

                    playerX =
                            getWidth() / 2 - 25;

                    if (lives <= 0) {

                        endGame();
                    }

                    break;
                }
            }
        }

        // =====================================================
        // GAME OVER
        // =====================================================

        private void endGame() {

            if (gameOver) {
                return;
            }

            gameOver = true;

            timer.stop();

            saveScore();

            JOptionPane.showMessageDialog(
                    this,
                    "Game Over!\n\n"
                            + "Player: "
                            + playerName
                            + "\nScore: "
                            + score,
                    "Asteroid Game",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }

        // =====================================================
        // RESTART
        // =====================================================

        private void restartGame() {

            playerX = 300;

            score = 0;

            lives = 3;

            asteroidTimer = 0;

            bullets.clear();

            asteroids.clear();

            gameOver = false;

            timer.start();

            requestFocusInWindow();

            repaint();
        }

        // =====================================================
        // SAVE SCORE
        // =====================================================

        private void saveScore() {

            try {

                ScoreDAO dao =
                        new ScoreDAO();

                dao.saveScore(
                        playerName,
                        "Asteroid Game",
                        "Easy",
                        score
                );

            } catch (Exception e) {

                System.out.println(
                        "Score could not be saved: "
                                + e.getMessage()
                );
            }
        }

        // =====================================================
        // KEYBOARD
        // =====================================================

        @Override
        public void keyPressed(KeyEvent e) {

            if (e.getKeyCode()
                    == KeyEvent.VK_LEFT) {

                leftPressed = true;
            }

            if (e.getKeyCode()
                    == KeyEvent.VK_RIGHT) {

                rightPressed = true;
            }

            if (e.getKeyCode()
                    == KeyEvent.VK_SPACE) {

                shoot();
            }

            if (e.getKeyCode()
                    == KeyEvent.VK_R
                    && gameOver) {

                restartGame();
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {

            if (e.getKeyCode()
                    == KeyEvent.VK_LEFT) {

                leftPressed = false;
            }

            if (e.getKeyCode()
                    == KeyEvent.VK_RIGHT) {

                rightPressed = false;
            }
        }

        @Override
        public void keyTyped(KeyEvent e) {
        }
    }

    // =========================================================
    // MAIN
    // =========================================================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(
                () -> new AsteroidGame("Guest")
        );
    }
}