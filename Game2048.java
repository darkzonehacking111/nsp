import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class Game2048 extends JFrame {

    private final String playerName;
    private GamePanel gamePanel;

    public Game2048() {
        this("Guest");
    }

    public Game2048(String playerName) {
        this.playerName = (playerName == null || playerName.trim().isEmpty())
                ? "Guest"
                : playerName.trim();

        setTitle("GamesMania - 2048");
        setSize(520, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        gamePanel = new GamePanel();
        add(gamePanel);

        setVisible(true);

        SwingUtilities.invokeLater(() ->
                gamePanel.requestFocusInWindow()
        );
    }

    private class GamePanel extends JPanel implements KeyListener {

        private int[][] board = new int[4][4];

        private int score = 0;
        private boolean gameOver = false;
        private boolean won = false;

        private final Random random = new Random();

        GamePanel() {
            setBackground(new Color(250, 248, 239));
            setFocusable(true);
            addKeyListener(this);

            startGame();
        }

        private void startGame() {

            board = new int[4][4];
            score = 0;
            gameOver = false;
            won = false;

            addRandomTile();
            addRandomTile();

            repaint();
        }

        private void addRandomTile() {

            int emptyCount = 0;

            for (int r = 0; r < 4; r++) {
                for (int c = 0; c < 4; c++) {
                    if (board[r][c] == 0) {
                        emptyCount++;
                    }
                }
            }

            if (emptyCount == 0) {
                return;
            }

            int target = random.nextInt(emptyCount);

            int count = 0;

            for (int r = 0; r < 4; r++) {
                for (int c = 0; c < 4; c++) {

                    if (board[r][c] == 0) {

                        if (count == target) {
                            board[r][c] =
                                    random.nextDouble() < 0.9 ? 2 : 4;
                            return;
                        }

                        count++;
                    }
                }
            }
        }

        // =====================================================
        // MOVE
        // =====================================================

        private boolean moveLeft() {

            boolean changed = false;

            for (int r = 0; r < 4; r++) {

                int[] oldRow = board[r].clone();

                int[] newRow = mergeLine(oldRow);

                for (int c = 0; c < 4; c++) {

                    if (board[r][c] != newRow[c]) {
                        changed = true;
                    }

                    board[r][c] = newRow[c];
                }
            }

            return changed;
        }

        private boolean moveRight() {

            boolean changed = false;

            for (int r = 0; r < 4; r++) {

                int[] reversed = new int[4];

                for (int c = 0; c < 4; c++) {
                    reversed[c] = board[r][3 - c];
                }

                int[] merged = mergeLine(reversed);

                for (int c = 0; c < 4; c++) {

                    int value = merged[3 - c];

                    if (board[r][c] != value) {
                        changed = true;
                    }

                    board[r][c] = value;
                }
            }

            return changed;
        }

        private boolean moveUp() {

            boolean changed = false;

            for (int c = 0; c < 4; c++) {

                int[] column = new int[4];

                for (int r = 0; r < 4; r++) {
                    column[r] = board[r][c];
                }

                int[] merged = mergeLine(column);

                for (int r = 0; r < 4; r++) {

                    if (board[r][c] != merged[r]) {
                        changed = true;
                    }

                    board[r][c] = merged[r];
                }
            }

            return changed;
        }

        private boolean moveDown() {

            boolean changed = false;

            for (int c = 0; c < 4; c++) {

                int[] reversed = new int[4];

                for (int r = 0; r < 4; r++) {
                    reversed[r] = board[3 - r][c];
                }

                int[] merged = mergeLine(reversed);

                for (int r = 0; r < 4; r++) {

                    int value = merged[3 - r];

                    if (board[r][c] != value) {
                        changed = true;
                    }

                    board[r][c] = value;
                }
            }

            return changed;
        }

        // =====================================================
        // MERGE
        // =====================================================

        private int[] mergeLine(int[] line) {

            int[] result = new int[4];

            int position = 0;

            for (int value : line) {

                if (value != 0) {

                    result[position] = value;
                    position++;
                }
            }

            for (int i = 0; i < 3; i++) {

                if (result[i] != 0 &&
                        result[i] == result[i + 1]) {

                    result[i] *= 2;

                    score += result[i];

                    result[i + 1] = 0;
                }
            }

            int[] finalLine = new int[4];

            position = 0;

            for (int value : result) {

                if (value != 0) {
                    finalLine[position] = value;
                    position++;
                }
            }

            return finalLine;
        }

        // =====================================================
        // HANDLE MOVE
        // =====================================================

        private void makeMove(char direction) {

            if (gameOver) {
                return;
            }

            boolean changed = false;

            if (direction == 'L') {
                changed = moveLeft();
            } else if (direction == 'R') {
                changed = moveRight();
            } else if (direction == 'U') {
                changed = moveUp();
            } else if (direction == 'D') {
                changed = moveDown();
            }

            if (changed) {

                addRandomTile();

                checkWin();

                if (!canMove()) {
                    gameOver = true;
                    saveScore();
                }

                repaint();
            }
        }

        // =====================================================
        // CHECK WIN
        // =====================================================

        private void checkWin() {

            for (int r = 0; r < 4; r++) {
                for (int c = 0; c < 4; c++) {

                    if (board[r][c] == 2048 && !won) {

                        won = true;

                        JOptionPane.showMessageDialog(
                                GamePanel.this,
                                "🎉 You reached 2048!\n\nScore: "
                                        + score,
                                "Congratulations!",
                                JOptionPane.INFORMATION_MESSAGE
                        );

                        return;
                    }
                }
            }
        }

        // =====================================================
        // CHECK POSSIBLE MOVES
        // =====================================================

        private boolean canMove() {

            // Empty cell
            for (int r = 0; r < 4; r++) {
                for (int c = 0; c < 4; c++) {

                    if (board[r][c] == 0) {
                        return true;
                    }
                }
            }

            // Horizontal matches
            for (int r = 0; r < 4; r++) {
                for (int c = 0; c < 3; c++) {

                    if (board[r][c] == board[r][c + 1]) {
                        return true;
                    }
                }
            }

            // Vertical matches
            for (int r = 0; r < 3; r++) {
                for (int c = 0; c < 4; c++) {

                    if (board[r][c] == board[r + 1][c]) {
                        return true;
                    }
                }
            }

            return false;
        }

        // =====================================================
        // PAINT
        // =====================================================

        @Override
        protected void paintComponent(Graphics g) {

            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            // Title
            g2.setColor(new Color(119, 110, 101));
            g2.setFont(new Font("Arial", Font.BOLD, 40));

            g2.drawString(
                    "2048",
                    25,
                    50
            );

            // Player
            g2.setFont(new Font("Arial", Font.PLAIN, 16));

            g2.drawString(
                    "Player: " + playerName,
                    25,
                    78
            );

            // Score
            g2.setFont(new Font("Arial", Font.BOLD, 18));

            g2.drawString(
                    "Score: " + score,
                    370,
                    50
            );

            // Board
            int boardX = 25;
            int boardY = 100;

            int cellSize = 110;
            int gap = 8;

            g2.setColor(new Color(187, 173, 160));

            g2.fillRoundRect(
                    boardX,
                    boardY,
                    4 * cellSize + 3 * gap,
                    4 * cellSize + 3 * gap,
                    10,
                    10
            );

            for (int r = 0; r < 4; r++) {

                for (int c = 0; c < 4; c++) {

                    int x =
                            boardX + c * (cellSize + gap);

                    int y =
                            boardY + r * (cellSize + gap);

                    drawTile(
                            g2,
                            board[r][c],
                            x,
                            y,
                            cellSize
                    );
                }
            }

            // Instructions
            g2.setColor(new Color(119, 110, 101));

            g2.setFont(
                    new Font("Arial", Font.BOLD, 16)
            );

            g2.drawString(
                    "Use ARROW KEYS to move",
                    25,
                    565
            );

            g2.drawString(
                    "Press R to restart",
                    25,
                    590
            );

            if (gameOver) {

                g2.setColor(
                        new Color(255, 255, 255, 210)
                );

                g2.fillRect(
                        boardX,
                        boardY,
                        4 * cellSize + 3 * gap,
                        4 * cellSize + 3 * gap
                );

                g2.setColor(
                        new Color(119, 110, 101)
                );

                g2.setFont(
                        new Font("Arial", Font.BOLD, 42)
                );

                g2.drawString(
                        "GAME OVER",
                        115,
                        320
                );

                g2.setFont(
                        new Font("Arial", Font.BOLD, 20)
                );

                g2.drawString(
                        "Press R to play again",
                        145,
                        360
                );
            }
        }

        // =====================================================
        // DRAW TILE
        // =====================================================

        private void drawTile(
                Graphics2D g2,
                int value,
                int x,
                int y,
                int size
        ) {

            if (value == 0) {

                g2.setColor(
                        new Color(205, 193, 180)
                );

            } else {

                g2.setColor(getTileColor(value));
            }

            g2.fillRoundRect(
                    x,
                    y,
                    size,
                    size,
                    10,
                    10
            );

            if (value != 0) {

                String text = String.valueOf(value);

                if (value < 100) {
                    g2.setFont(
                            new Font(
                                    "Arial",
                                    Font.BOLD,
                                    36
                            )
                    );
                } else if (value < 1000) {
                    g2.setFont(
                            new Font(
                                    "Arial",
                                    Font.BOLD,
                                    30
                            )
                    );
                } else {
                    g2.setFont(
                            new Font(
                                    "Arial",
                                    Font.BOLD,
                                    24
                            )
                    );
                }

                FontMetrics fm = g2.getFontMetrics();

                int textWidth =
                        fm.stringWidth(text);

                int textHeight =
                        fm.getAscent();

                if (value <= 4) {
                    g2.setColor(
                            new Color(119, 110, 101)
                    );
                } else {
                    g2.setColor(Color.WHITE);
                }

                g2.drawString(
                        text,
                        x + (size - textWidth) / 2,
                        y + (size + textHeight) / 2 - 5
                );
            }
        }

        private Color getTileColor(int value) {

            switch (value) {

                case 2:
                    return new Color(238, 228, 218);

                case 4:
                    return new Color(237, 224, 200);

                case 8:
                    return new Color(242, 177, 121);

                case 16:
                    return new Color(245, 149, 99);

                case 32:
                    return new Color(246, 124, 95);

                case 64:
                    return new Color(246, 94, 59);

                case 128:
                    return new Color(237, 207, 114);

                case 256:
                    return new Color(237, 204, 97);

                case 512:
                    return new Color(237, 200, 80);

                case 1024:
                    return new Color(237, 197, 63);

                case 2048:
                    return new Color(237, 194, 46);

                default:
                    return new Color(60, 58, 50);
            }
        }

        // =====================================================
        // KEYBOARD
        // =====================================================

        @Override
        public void keyPressed(KeyEvent e) {

            switch (e.getKeyCode()) {

                case KeyEvent.VK_LEFT:
                    makeMove('L');
                    break;

                case KeyEvent.VK_RIGHT:
                    makeMove('R');
                    break;

                case KeyEvent.VK_UP:
                    makeMove('U');
                    break;

                case KeyEvent.VK_DOWN:
                    makeMove('D');
                    break;

                case KeyEvent.VK_R:

                    if (gameOver) {

                        startGame();

                    }

                    break;
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }

        @Override
        public void keyTyped(KeyEvent e) {
        }

        // =====================================================
        // SAVE SCORE
        // =====================================================

        private void saveScore() {

            try {

                ScoreDAO dao = new ScoreDAO();

                dao.saveScore(
                        playerName,
                        "2048",
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
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() ->
                new Game2048("Guest")
        );
    }
}