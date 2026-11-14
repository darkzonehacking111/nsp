import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class RacingGame extends JFrame {

    private final String playerName;

    public RacingGame() {
        this("Guest");
    }

    public RacingGame(String playerName) {
        this.playerName =
                (playerName == null || playerName.trim().isEmpty())
                        ? "Guest"
                        : playerName.trim();

        setTitle("GamesMania - Racing Game");
        setSize(600, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        add(new RacingPanel());

        setVisible(true);
    }

    private class RacingPanel extends JPanel
            implements ActionListener, KeyListener {

        private final Random random = new Random();

        private final Timer timer;

        private int carX = 275;
        private final int carY = 550;

        private int enemyX;
        private int enemyY = -120;

        private int enemySpeed = 7;

        private int score = 0;
        private int lives = 3;

        private boolean gameOver = false;

        private boolean leftPressed = false;
        private boolean rightPressed = false;

        RacingPanel() {

            setBackground(new Color(60, 60, 60));

            setFocusable(true);
            addKeyListener(this);

            createEnemy();

            timer = new Timer(16, this);
            timer.start();

            SwingUtilities.invokeLater(
                    () -> requestFocusInWindow()
            );
        }

        private void createEnemy() {

            int[] lanes = {
                    175,
                    275,
                    375
            };

            enemyX =
                    lanes[random.nextInt(lanes.length)];

            enemyY = -120;
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

            int roadX = 125;
            int roadWidth = 350;

            // ================= ROAD =================

            g2.setColor(
                    new Color(45, 45, 45)
            );

            g2.fillRect(
                    roadX,
                    0,
                    roadWidth,
                    getHeight()
            );

            // Road borders
            g2.setColor(Color.WHITE);

            g2.fillRect(
                    roadX,
                    0,
                    5,
                    getHeight()
            );

            g2.fillRect(
                    roadX + roadWidth - 5,
                    0,
                    5,
                    getHeight()
            );

            // Lane lines
            g2.setStroke(
                    new BasicStroke(
                            5,
                            BasicStroke.CAP_BUTT,
                            BasicStroke.JOIN_MITER,
                            10,
                            new float[]{30, 25},
                            0
                    )
            );

            g2.setColor(Color.YELLOW);

            g2.drawLine(
                    250,
                    0,
                    250,
                    getHeight()
            );

            g2.drawLine(
                    350,
                    0,
                    350,
                    getHeight()
            );

            // ================= PLAYER CAR =================

            drawCar(
                    g2,
                    carX,
                    carY,
                    Color.BLUE
            );

            // ================= ENEMY CAR =================

            drawCar(
                    g2,
                    enemyX,
                    enemyY,
                    Color.RED
            );

            // ================= INFORMATION =================

            g2.setColor(Color.WHITE);

            g2.setFont(
                    new Font(
                            "Arial",
                            Font.BOLD,
                            18
                    )
            );

            g2.drawString(
                    "Player: " + playerName,
                    15,
                    30
            );

            g2.drawString(
                    "Score: " + score,
                    15,
                    58
            );

            g2.drawString(
                    "Lives: " + lives,
                    470,
                    30
            );

            g2.setFont(
                    new Font(
                            "Arial",
                            Font.PLAIN,
                            15
                    )
            );

            g2.drawString(
                    "← → Move",
                    15,
                    getHeight() - 25
            );

            // ================= GAME OVER =================

            if (gameOver) {

                g2.setColor(
                        new Color(
                                0,
                                0,
                                0,
                                180
                        )
                );

                g2.fillRect(
                        0,
                        0,
                        getWidth(),
                        getHeight()
                );

                g2.setColor(Color.WHITE);

                g2.setFont(
                        new Font(
                                "Arial",
                                Font.BOLD,
                                42
                        )
                );

                g2.drawString(
                        "GAME OVER",
                        175,
                        300
                );

                g2.setFont(
                        new Font(
                                "Arial",
                                Font.BOLD,
                                20
                        )
                );

                g2.drawString(
                        "Score: " + score,
                        250,
                        340
                );

                g2.drawString(
                        "Press R to restart",
                        205,
                        385
                );
            }
        }

        private void drawCar(
                Graphics2D g2,
                int x,
                int y,
                Color color
        ) {

            // Main body
            g2.setColor(color);

            g2.fillRoundRect(
                    x,
                    y,
                    50,
                    90,
                    10,
                    10
            );

            // Windows
            g2.setColor(
                    new Color(
                            180,
                            220,
                            240
                    )
            );

            g2.fillRect(
                    x + 8,
                    y + 12,
                    34,
                    25
            );

            // Front window
            g2.setColor(
                    new Color(
                            100,
                            160,
                            190
                    )
            );

            g2.fillRect(
                    x + 8,
                    y + 48,
                    34,
                    20
            );

            // Wheels
            g2.setColor(Color.BLACK);

            g2.fillRoundRect(
                    x - 7,
                    y + 10,
                    10,
                    25,
                    5,
                    5
            );

            g2.fillRoundRect(
                    x + 47,
                    y + 10,
                    10,
                    25,
                    5,
                    5
            );

            g2.fillRoundRect(
                    x - 7,
                    y + 58,
                    10,
                    25,
                    5,
                    5
            );

            g2.fillRoundRect(
                    x + 47,
                    y + 58,
                    10,
                    25,
                    5,
                    5
            );

            // Lights
            g2.setColor(Color.YELLOW);

            g2.fillOval(
                    x + 7,
                    y + 3,
                    10,
                    8
            );

            g2.fillOval(
                    x + 33,
                    y + 3,
                    10,
                    8
            );
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            if (gameOver) {
                repaint();
                return;
            }

            // ================= PLAYER MOVEMENT =================

            if (leftPressed) {

                carX -= 8;

            }

            if (rightPressed) {

                carX += 8;
            }

            // Keep car on road
            if (carX < 145) {
                carX = 145;
            }

            if (carX > 405) {
                carX = 405;
            }

            // ================= ENEMY MOVEMENT =================

            enemyY += enemySpeed;

            if (enemyY > getHeight()) {

                score++;

                // Increase difficulty
                if (score % 5 == 0) {
                    enemySpeed++;
                }

                createEnemy();
            }

            // ================= COLLISION =================

            Rectangle playerCar =
                    new Rectangle(
                            carX,
                            carY,
                            50,
                            90
                    );

            Rectangle enemyCar =
                    new Rectangle(
                            enemyX,
                            enemyY,
                            50,
                            90
                    );

            if (playerCar.intersects(enemyCar)) {

                lives--;

                createEnemy();

                carX = 275;

                if (lives <= 0) {

                    endGame();

                } else {

                    // Small pause after collision
                    timer.stop();

                    JOptionPane.showMessageDialog(
                            this,
                            "Crash!\nLives remaining: "
                                    + lives,
                            "Collision",
                            JOptionPane.WARNING_MESSAGE
                    );

                    timer.start();

                    requestFocusInWindow();
                }
            }

            repaint();
        }

        private void endGame() {

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
                    "Racing Game",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }

        private void restartGame() {

            carX = 275;

            score = 0;

            lives = 3;

            enemySpeed = 7;

            gameOver = false;

            createEnemy();

            timer.start();

            requestFocusInWindow();

            repaint();
        }

        private void saveScore() {

            try {

                ScoreDAO dao =
                        new ScoreDAO();

                dao.saveScore(
                        playerName,
                        "Racing Game",
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

        // ================= KEYBOARD =================

        @Override
        public void keyPressed(KeyEvent e) {

            if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
                leftPressed = true;
            }

            if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
                rightPressed = true;
            }

            if (e.getKeyCode() == KeyEvent.VK_R && gameOver) {
                restartGame();
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {

            if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
                leftPressed = false;
            }

            if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
                rightPressed = false;
            }
        }

        @Override
        public void keyTyped(KeyEvent e) {
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(
                () -> new RacingGame("Guest")
        );
    }
}