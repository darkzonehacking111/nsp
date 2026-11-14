import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class FruitGame extends JFrame {

    private final String playerName;

    public FruitGame() {
        this("Guest");
    }

    public FruitGame(String playerName) {
        this.playerName = (playerName == null || playerName.trim().isEmpty())
                ? "Guest" : playerName;

        setTitle("Fruit Game");
        setSize(700, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        add(new FruitPanel());

        setVisible(true);
    }

    private class FruitPanel extends JPanel implements ActionListener, KeyListener {

        private int basketX = 300;
        private final int basketY = 500;

        private int fruitX;
        private int fruitY = 50;

        private final int basketWidth = 100;
        private final int basketHeight = 30;

        private final int fruitSize = 35;

        private int score = 0;
        private int lives = 3;

        private boolean gameOver = false;

        private final Random random = new Random();
        private final Timer timer;

        FruitPanel() {
            setBackground(new Color(230, 245, 255));

            setFocusable(true);
            addKeyListener(this);

            newFruit();

            timer = new Timer(16, this);
            timer.start();
        }

        private void newFruit() {
            fruitX = random.nextInt(620) + 20;
            fruitY = 50;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            // Sky & Orchard Gradient
            GradientPaint skyGrad = new GradientPaint(
                    0, 0, new Color(210, 235, 255),
                    0, h, new Color(235, 250, 240)
            );
            g2.setPaint(skyGrad);
            g2.fillRect(0, 0, w, h);

            // Ground Grass
            g2.setColor(new Color(110, 195, 120));
            g2.fillRect(0, h - 45, w, 45);
            g2.setColor(new Color(85, 165, 95));
            g2.fillRect(0, h - 48, w, 4);

            // Glassmorphic Top HUD
            g2.setColor(new Color(255, 255, 255, 220));
            g2.fillRoundRect(15, 12, w - 30, 48, 14, 14);
            g2.setColor(new Color(200, 220, 240));
            g2.setStroke(new BasicStroke(1.2f));
            g2.drawRoundRect(15, 12, w - 30, 48, 14, 14);

            g2.setFont(new Font("Segoe UI", Font.BOLD, 15));
            g2.setColor(new Color(40, 60, 90));
            g2.drawString("PLAYER: " + playerName, 30, 42);

            g2.setColor(new Color(230, 90, 40));
            g2.drawString("SCORE: " + score, 260, 42);

            // Lives Hearts
            g2.setFont(new Font("Segoe UI", Font.BOLD, 15));
            g2.setColor(new Color(40, 60, 90));
            g2.drawString("LIVES: ", w - 170, 42);
            for (int i = 0; i < 3; i++) {
                if (i < lives) {
                    g2.setColor(new Color(245, 60, 85));
                } else {
                    g2.setColor(new Color(190, 200, 215));
                }
                g2.fillOval(w - 110 + (i * 24), 28, 16, 16);
            }

            // Juicy Fruit Rendering with Glow and Highlights
            g2.setColor(new Color(255, 80, 80, 60));
            g2.fillOval(fruitX - 3, fruitY - 3, fruitSize + 6, fruitSize + 6);

            GradientPaint fruitGrad = new GradientPaint(
                    fruitX + 5, fruitY + 5, new Color(255, 110, 90),
                    fruitX + fruitSize, fruitY + fruitSize, new Color(200, 25, 45)
            );
            g2.setPaint(fruitGrad);
            g2.fillOval(fruitX, fruitY, fruitSize, fruitSize);

            // Glossy shine
            g2.setColor(new Color(255, 255, 255, 180));
            g2.fillOval(fruitX + 7, fruitY + 6, 9, 6);

            // Fruit Stem & Leaf
            g2.setColor(new Color(110, 60, 25));
            g2.fillRect(fruitX + fruitSize / 2 - 1, fruitY - 4, 3, 6);
            g2.setColor(new Color(75, 185, 60));
            g2.fillOval(fruitX + fruitSize / 2 + 1, fruitY - 5, 8, 5);

            // Woven Wooden Basket with handles and shadow
            g2.setColor(new Color(0, 0, 0, 35));
            g2.fillRoundRect(basketX + 5, basketY + basketHeight - 2, basketWidth - 10, 10, 10, 10);

            GradientPaint basketGrad = new GradientPaint(
                    basketX, basketY, new Color(210, 140, 70),
                    basketX, basketY + basketHeight, new Color(145, 85, 30)
            );
            g2.setPaint(basketGrad);
            g2.fillRoundRect(basketX, basketY, basketWidth, basketHeight, 14, 14);

            // Woven weave lines
            g2.setColor(new Color(120, 65, 20, 160));
            g2.setStroke(new BasicStroke(1.5f));
            for (int bx = basketX + 12; bx < basketX + basketWidth - 5; bx += 14) {
                g2.drawLine(bx, basketY + 2, bx + 4, basketY + basketHeight - 2);
            }
            g2.setColor(new Color(245, 185, 110));
            g2.drawRoundRect(basketX, basketY, basketWidth, basketHeight, 14, 14);

            if (gameOver) {
                g2.setColor(new Color(15, 25, 40, 210));
                g2.fillRect(0, 0, w, h);

                g2.setColor(new Color(255, 75, 75));
                g2.setFont(new Font("Segoe UI", Font.BOLD, 46));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString("GAME OVER", (w - fm.stringWidth("GAME OVER")) / 2, 230);

                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 24));
                fm = g2.getFontMetrics();
                String scoreMsg = "Final Score: " + score;
                g2.drawString(scoreMsg, (w - fm.stringWidth(scoreMsg)) / 2, 280);

                g2.setColor(new Color(100, 220, 255));
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 18));
                fm = g2.getFontMetrics();
                String restMsg = "Press R to Restart   •   ESC to Exit";
                g2.drawString(restMsg, (w - fm.stringWidth(restMsg)) / 2, 330);
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            if (gameOver) {
                repaint();
                return;
            }

            fruitY += 6;

            Rectangle fruit = new Rectangle(
                    fruitX,
                    fruitY,
                    fruitSize,
                    fruitSize
            );

            Rectangle basket = new Rectangle(
                    basketX,
                    basketY,
                    basketWidth,
                    basketHeight
            );

            // Catch
            if (fruit.intersects(basket)) {

                score += 10;

                newFruit();
            }

            // Miss
            if (fruitY > getHeight()) {

                lives--;

                if (lives <= 0) {
                    endGame();
                } else {
                    newFruit();
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
                    "Game Over!\nFinal Score: " + score
            );
        }

        private void restart() {

            score = 0;
            lives = 3;
            basketX = 300;
            gameOver = false;

            newFruit();

            timer.start();

            requestFocusInWindow();

            repaint();
        }

        private void saveScore() {
            try {
                ScoreDAO dao = new ScoreDAO();

                dao.saveScore(
                        playerName,
                        "Fruit Game",
                        "Easy",
                        score
                );

            } catch (Exception e) {
                System.out.println(
                        "Score could not be saved: " + e.getMessage()
                );
            }
        }

        @Override
        public void keyPressed(KeyEvent e) {

            int k = e.getKeyCode();
            if (k == KeyEvent.VK_LEFT || k == KeyEvent.VK_A) {
                basketX -= 25;
                if (basketX < 10) basketX = 10;
            }

            if (k == KeyEvent.VK_RIGHT || k == KeyEvent.VK_D) {
                basketX += 25;
                if (basketX > getWidth() - basketWidth - 10) {
                    basketX = getWidth() - basketWidth - 10;
                }
            }

            if (k == KeyEvent.VK_R && gameOver) {
                restart();
            }

            if (k == KeyEvent.VK_ESCAPE) {
                dispose();
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }

        @Override
        public void keyTyped(KeyEvent e) {
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FruitGame());
    }
}