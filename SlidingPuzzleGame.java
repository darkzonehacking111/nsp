import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;

public class SlidingPuzzleGame extends JFrame {

    private final String playerName;
    private final JButton[] buttons = new JButton[9];
    private final int[] tiles = new int[9];

    private int moves = 0;
    private JLabel movesLabel;
    private JButton shuffleButton;

    public SlidingPuzzleGame() {
        this("Player");
    }

    public SlidingPuzzleGame(String playerName) {
        this.playerName = (playerName != null && !playerName.trim().isEmpty()) ? playerName.trim() : "Player";

        setTitle("GamesMania • Sliding Puzzle");
        setSize(520, 620);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        initUI();
        shuffle();
        setVisible(true);
    }

    private void initUI() {
        JPanel root = new JPanel(new BorderLayout(0, 16));
        root.setBackground(new Color(15, 23, 42));
        root.setBorder(new EmptyBorder(24, 28, 24, 28));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("SLIDING PUZZLE", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(new Color(248, 250, 252));

        JLabel subtitle = new JLabel("Order tiles from 1 to 8 • Player: " + playerName, SwingConstants.CENTER);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(new Color(148, 163, 184));

        JPanel titleBox = new JPanel(new GridLayout(2, 1, 0, 4));
        titleBox.setOpaque(false);
        titleBox.add(titleLabel);
        titleBox.add(subtitle);
        headerPanel.add(titleBox, BorderLayout.CENTER);

        root.add(headerPanel, BorderLayout.NORTH);

        // Board
        JPanel boardContainer = new JPanel(new BorderLayout());
        boardContainer.setOpaque(false);
        boardContainer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(51, 65, 85), 2, true),
                new EmptyBorder(8, 8, 8, 8)
        ));

        JPanel board = new JPanel(new GridLayout(3, 3, 8, 8));
        board.setBackground(new Color(30, 41, 59));

        for (int i = 0; i < 9; i++) {
            final int index = i;
            JButton btn = new JButton() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    boolean isEmpty = getText().isEmpty();
                    if (isEmpty) {
                        g2.setColor(new Color(15, 23, 42, 180));
                        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                    } else {
                        // Glossy tile gradient
                        GradientPaint gp = new GradientPaint(
                                0, 0, new Color(59, 130, 246),
                                0, getHeight(), new Color(37, 99, 235)
                        );
                        if (getModel().isRollover()) {
                            gp = new GradientPaint(
                                    0, 0, new Color(96, 165, 250),
                                    0, getHeight(), new Color(59, 130, 246)
                            );
                        }
                        g2.setPaint(gp);
                        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);

                        // Highlight border
                        g2.setColor(new Color(147, 197, 253, 100));
                        g2.setStroke(new BasicStroke(1.5f));
                        g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 12, 12);
                    }
                    g2.dispose();
                    super.paintComponent(g);
                }
            };

            btn.setFont(new Font("Segoe UI", Font.BOLD, 36));
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setContentAreaFilled(false);
            btn.setBorderPainted(false);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.addActionListener(e -> move(index));

            buttons[i] = btn;
            board.add(btn);
        }
        boardContainer.add(board, BorderLayout.CENTER);
        root.add(boardContainer, BorderLayout.CENTER);

        // Footer / Stats
        JPanel footer = new JPanel(new BorderLayout(16, 0));
        footer.setOpaque(false);

        movesLabel = new JLabel("Moves: 0", SwingConstants.LEFT);
        movesLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        movesLabel.setForeground(new Color(56, 189, 248));

        shuffleButton = new JButton("SHUFFLE") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg = getModel().isRollover() ? new Color(16, 185, 129) : new Color(5, 150, 105);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        shuffleButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        shuffleButton.setForeground(Color.WHITE);
        shuffleButton.setFocusPainted(false);
        shuffleButton.setContentAreaFilled(false);
        shuffleButton.setBorderPainted(false);
        shuffleButton.setPreferredSize(new Dimension(130, 42));
        shuffleButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        shuffleButton.addActionListener(e -> shuffle());

        footer.add(movesLabel, BorderLayout.WEST);
        footer.add(shuffleButton, BorderLayout.EAST);
        root.add(footer, BorderLayout.SOUTH);

        add(root);
    }

    private void shuffle() {
        ArrayList<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            numbers.add(i);
        }

        // Shuffle with guarantee of solvability (even number of inversions)
        boolean valid = false;
        while (!valid) {
            Collections.shuffle(numbers);

            // Compute inversions
            int inversions = 0;
            for (int i = 0; i < 9; i++) {
                int a = numbers.get(i);
                if (a == 0) continue;
                for (int j = i + 1; j < 9; j++) {
                    int b = numbers.get(j);
                    if (b != 0 && a > b) {
                        inversions++;
                    }
                }
            }

            // Solvable if inversions is even and not already solved
            if (inversions % 2 == 0 && inversions > 0) {
                valid = true;
            }
        }

        for (int i = 0; i < 9; i++) {
            tiles[i] = numbers.get(i);
        }

        moves = 0;
        movesLabel.setText("Moves: 0");
        updateBoard();
    }

    private void move(int index) {
        int emptyIndex = -1;
        for (int i = 0; i < 9; i++) {
            if (tiles[i] == 0) {
                emptyIndex = i;
                break;
            }
        }

        int row = index / 3;
        int col = index % 3;
        int emptyRow = emptyIndex / 3;
        int emptyCol = emptyIndex % 3;

        boolean adjacent = (Math.abs(row - emptyRow) == 1 && col == emptyCol)
                || (Math.abs(col - emptyCol) == 1 && row == emptyRow);

        if (adjacent) {
            tiles[emptyIndex] = tiles[index];
            tiles[index] = 0;

            moves++;
            movesLabel.setText("Moves: " + moves);
            updateBoard();

            if (isSolved()) {
                int finalScore = Math.max(1200 - (moves * 12), 100);

                // Save to Database
                try {
                    ScoreDAO dao = new ScoreDAO();
                    dao.saveScore(playerName, "Sliding Puzzle", "Normal", finalScore);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                JOptionPane.showMessageDialog(
                        this,
                        "🎉 Congratulations, " + playerName + "!\n\n"
                                + "Puzzle Solved in " + moves + " moves!\n"
                                + "Score Awarded: " + finalScore + "\n"
                                + "Score has been saved to the Leaderboard!",
                        "Puzzle Complete",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        }
    }

    private void updateBoard() {
        for (int i = 0; i < 9; i++) {
            if (tiles[i] == 0) {
                buttons[i].setText("");
            } else {
                buttons[i].setText(String.valueOf(tiles[i]));
            }
            buttons[i].repaint();
        }
    }

    private boolean isSolved() {
        for (int i = 0; i < 8; i++) {
            if (tiles[i] != i + 1) {
                return false;
            }
        }
        return tiles[8] == 0;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SlidingPuzzleGame("Player"));
    }
}