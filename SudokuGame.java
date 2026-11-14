import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class SudokuGame extends JFrame {

    private final String playerName;

    public SudokuGame() {
        this("Guest");
    }

    public SudokuGame(String playerName) {
        this.playerName =
                (playerName == null || playerName.trim().isEmpty())
                        ? "Guest"
                        : playerName.trim();

        setTitle("GamesMania - Sudoku");
        setSize(650, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        add(new SudokuPanel());

        setVisible(true);
    }

    private class SudokuPanel extends JPanel {

        private final int[][] solution = new int[9][9];
        private final int[][] puzzle = new int[9][9];

        private final JTextField[][] cells =
                new JTextField[9][9];

        private JLabel statusLabel;
        private JLabel scoreLabel;

        private int score = 0;
        private int mistakes = 0;

        private final Random random = new Random();

        SudokuPanel() {

            setLayout(new BorderLayout(10, 10));

            setBackground(new Color(245, 245, 245));

            setBorder(
                    BorderFactory.createEmptyBorder(
                            15, 15, 15, 15
                    )
            );

            createTop();
            createBoard();
            createBottom();

            newGame();
        }

        // =====================================================
        // TOP
        // =====================================================

        private void createTop() {

            JPanel top =
                    new JPanel(new GridLayout(3, 1));

            top.setBackground(
                    new Color(245, 245, 245)
            );

            JLabel title =
                    new JLabel(
                            "SUDOKU",
                            SwingConstants.CENTER
                    );

            title.setFont(
                    new Font(
                            "Arial",
                            Font.BOLD,
                            34
                    )
            );

            title.setForeground(
                    new Color(50, 50, 50)
            );

            JLabel player =
                    new JLabel(
                            "Player: " + playerName,
                            SwingConstants.CENTER
                    );

            player.setFont(
                    new Font(
                            "Arial",
                            Font.PLAIN,
                            17
                    )
            );

            statusLabel =
                    new JLabel(
                            "Fill the Sudoku grid",
                            SwingConstants.CENTER
                    );

            statusLabel.setFont(
                    new Font(
                            "Arial",
                            Font.BOLD,
                            16
                    )
            );

            statusLabel.setForeground(
                    new Color(40, 100, 180)
            );

            top.add(title);
            top.add(player);
            top.add(statusLabel);

            add(
                    top,
                    BorderLayout.NORTH
            );
        }

        // =====================================================
        // BOARD
        // =====================================================

        private void createBoard() {

            JPanel board =
                    new JPanel(
                            new GridLayout(
                                    9,
                                    9,
                                    2,
                                    2
                            )
                    );

            board.setBackground(Color.BLACK);

            for (int row = 0; row < 9; row++) {

                for (int col = 0; col < 9; col++) {

                    JTextField field =
                            new JTextField();

                    field.setHorizontalAlignment(
                            JTextField.CENTER
                    );

                    field.setFont(
                            new Font(
                                    "Arial",
                                    Font.BOLD,
                                    24
                            )
                    );

                    field.setPreferredSize(
                            new Dimension(
                                    55,
                                    55
                            )
                    );

                    final int r = row;
                    final int c = col;

                    field.addKeyListener(
                            new KeyAdapter() {

                                @Override
                                public void keyTyped(
                                        KeyEvent e
                                ) {

                                    char ch =
                                            e.getKeyChar();

                                    if (!Character.isDigit(ch)
                                            || ch == '0') {

                                        e.consume();
                                    }

                                    if (field.getText()
                                            .length() >= 1) {

                                        e.consume();
                                    }
                                }
                            }
                    );

                    field.addActionListener(
                            e -> checkCell(r, c)
                    );

                    cells[row][col] = field;

                    board.add(field);
                }
            }

            add(
                    board,
                    BorderLayout.CENTER
            );
        }

        // =====================================================
        // BOTTOM
        // =====================================================

        private void createBottom() {

            JPanel bottom =
                    new JPanel(
                            new BorderLayout(
                                    10,
                                    10
                            )
                    );

            bottom.setBackground(
                    new Color(245, 245, 245)
            );

            scoreLabel =
                    new JLabel(
                            "Score: 0    Mistakes: 0",
                            SwingConstants.CENTER
                    );

            scoreLabel.setFont(
                    new Font(
                            "Arial",
                            Font.BOLD,
                            17
                    )
            );

            bottom.add(
                    scoreLabel,
                    BorderLayout.NORTH
            );

            JPanel buttons =
                    new JPanel(
                            new FlowLayout(
                                    FlowLayout.CENTER,
                                    10,
                                    5
                            )
                    );

            buttons.setBackground(
                    new Color(245, 245, 245)
            );

            JButton checkButton =
                    new JButton("CHECK");

            JButton solveButton =
                    new JButton("SOLVE");

            JButton newGameButton =
                    new JButton("NEW GAME");

            JButton exitButton =
                    new JButton("EXIT");

            checkButton.addActionListener(
                    e -> checkBoard()
            );

            solveButton.addActionListener(
                    e -> showSolution()
            );

            newGameButton.addActionListener(
                    e -> newGame()
            );

            exitButton.addActionListener(
                    e -> {
                        saveScore();
                        dispose();
                    }
            );

            buttons.add(checkButton);
            buttons.add(solveButton);
            buttons.add(newGameButton);
            buttons.add(exitButton);

            bottom.add(
                    buttons,
                    BorderLayout.CENTER
            );

            add(
                    bottom,
                    BorderLayout.SOUTH
            );
        }

        // =====================================================
        // NEW GAME
        // =====================================================

        private void newGame() {

            score = 0;
            mistakes = 0;

            generateSudoku();

            for (int row = 0; row < 9; row++) {

                for (int col = 0; col < 9; col++) {

                    JTextField field =
                            cells[row][col];

                    field.setText("");

                    field.setBackground(
                            Color.WHITE
                    );

                    field.setForeground(
                            Color.BLACK
                    );

                    if (puzzle[row][col] != 0) {

                        field.setText(
                                String.valueOf(
                                        puzzle[row][col]
                                )
                        );

                        field.setEditable(false);

                        field.setBackground(
                                new Color(
                                        225,
                                        225,
                                        225
                                )
                        );

                        field.setForeground(
                                new Color(
                                        30,
                                        30,
                                        30
                                )
                        );

                    } else {

                        field.setEditable(true);
                    }
                }
            }

            statusLabel.setText(
                    "Fill the Sudoku grid"
            );

            statusLabel.setForeground(
                    new Color(40, 100, 180)
            );

            updateScore();

            cells[0][0].requestFocusInWindow();
        }

        // =====================================================
        // GENERATE SUDOKU
        // =====================================================

        private void generateSudoku() {

            for (int row = 0; row < 9; row++) {

                for (int col = 0; col < 9; col++) {

                    solution[row][col] = 0;
                    puzzle[row][col] = 0;
                }
            }

            fillBoard(0, 0);

            // Copy solution
            for (int row = 0; row < 9; row++) {

                for (int col = 0; col < 9; col++) {

                    puzzle[row][col] =
                            solution[row][col];
                }
            }

            // Remove numbers
            int removeCount = 45;

            while (removeCount > 0) {

                int row =
                        random.nextInt(9);

                int col =
                        random.nextInt(9);

                if (puzzle[row][col] != 0) {

                    puzzle[row][col] = 0;

                    removeCount--;
                }
            }
        }

        // =====================================================
        // SOLVE BOARD GENERATION
        // =====================================================

        private boolean fillBoard(
                int row,
                int col
        ) {

            if (row == 9) {
                return true;
            }

            int nextRow = row;
            int nextCol = col + 1;

            if (nextCol == 9) {

                nextCol = 0;
                nextRow++;
            }

            int[] numbers =
                    shuffledNumbers();

            for (int number : numbers) {

                if (isSafe(
                        solution,
                        row,
                        col,
                        number
                )) {

                    solution[row][col] =
                            number;

                    if (fillBoard(
                            nextRow,
                            nextCol
                    )) {

                        return true;
                    }

                    solution[row][col] = 0;
                }
            }

            return false;
        }

        private int[] shuffledNumbers() {

            int[] numbers =
                    {
                            1, 2, 3, 4, 5,
                            6, 7, 8, 9
                    };

            for (int i = numbers.length - 1;
                 i > 0;
                 i--) {

                int j =
                        random.nextInt(i + 1);

                int temp =
                        numbers[i];

                numbers[i] =
                        numbers[j];

                numbers[j] =
                        temp;
            }

            return numbers;
        }

        private boolean isSafe(
                int[][] board,
                int row,
                int col,
                int number
        ) {

            // Row
            for (int c = 0; c < 9; c++) {

                if (board[row][c] == number) {
                    return false;
                }
            }

            // Column
            for (int r = 0; r < 9; r++) {

                if (board[r][col] == number) {
                    return false;
                }
            }

            // 3x3 box
            int startRow =
                    row - row % 3;

            int startCol =
                    col - col % 3;

            for (int r = 0; r < 3; r++) {

                for (int c = 0; c < 3; c++) {

                    if (board[
                            startRow + r
                            ][
                            startCol + c
                            ] == number) {

                        return false;
                    }
                }
            }

            return true;
        }

        // =====================================================
        // CHECK SINGLE CELL
        // =====================================================

        private void checkCell(
                int row,
                int col
        ) {

            if (puzzle[row][col] != 0) {
                return;
            }

            String text =
                    cells[row][col]
                            .getText()
                            .trim();

            if (text.isEmpty()) {
                return;
            }

            int number;

            try {

                number =
                        Integer.parseInt(text);

            } catch (Exception e) {

                cells[row][col].setText("");

                return;
            }

            if (number ==
                    solution[row][col]) {

                cells[row][col].setForeground(
                        new Color(
                                20,
                                130,
                                60
                        )
                );

                score += 10;

                statusLabel.setText(
                        "Correct! +10 points"
                );

            } else {

                cells[row][col].setForeground(
                        Color.RED
                );

                mistakes++;

                score -= 5;

                if (score < 0) {
                    score = 0;
                }

                statusLabel.setText(
                        "Wrong number! Try again."
                );
            }

            updateScore();

            checkCompleted();
        }

        // =====================================================
        // CHECK COMPLETE BOARD
        // =====================================================

        private void checkBoard() {

            boolean complete = true;

            boolean correct = true;

            for (int row = 0; row < 9; row++) {

                for (int col = 0; col < 9; col++) {

                    String text =
                            cells[row][col]
                                    .getText()
                                    .trim();

                    if (text.isEmpty()) {

                        complete = false;

                        continue;
                    }

                    try {

                        int number =
                                Integer.parseInt(text);

                        if (number !=
                                solution[row][col]) {

                            correct = false;
                        }

                    } catch (Exception e) {

                        correct = false;
                    }
                }
            }

            if (complete && correct) {

                score += 100;

                updateScore();

                statusLabel.setText(
                        "🎉 Sudoku Completed!"
                );

                statusLabel.setForeground(
                        new Color(
                                20,
                                140,
                                60
                        )
                );

                saveScore();

                JOptionPane.showMessageDialog(
                        this,
                        "Congratulations, "
                                + playerName
                                + "!\n\n"
                                + "Sudoku completed!\n"
                                + "Final Score: "
                                + score,
                        "You Win!",
                        JOptionPane.INFORMATION_MESSAGE
                );

            } else if (!correct) {

                statusLabel.setText(
                        "There are some incorrect numbers."
                );

                statusLabel.setForeground(
                        Color.RED
                );

            } else {

                statusLabel.setText(
                        "Keep going! Complete the grid."
                );
            }
        }

        private void checkCompleted() {

            boolean complete = true;

            for (int row = 0; row < 9; row++) {

                for (int col = 0; col < 9; col++) {

                    if (cells[row][col]
                            .getText()
                            .trim()
                            .isEmpty()) {

                        complete = false;

                        break;
                    }
                }
            }

            if (complete) {
                checkBoard();
            }
        }

        // =====================================================
        // SHOW SOLUTION
        // =====================================================

        private void showSolution() {

            int choice =
                    JOptionPane.showConfirmDialog(
                            this,
                            "Show the complete solution?",
                            "Sudoku",
                            JOptionPane.YES_NO_OPTION
                    );

            if (choice != JOptionPane.YES_OPTION) {
                return;
            }

            for (int row = 0; row < 9; row++) {

                for (int col = 0; col < 9; col++) {

                    cells[row][col].setText(
                            String.valueOf(
                                    solution[row][col]
                            )
                    );

                    cells[row][col].setEditable(
                            false
                    );

                    cells[row][col].setForeground(
                            new Color(
                                    70,
                                    70,
                                    180
                            )
                    );
                }
            }

            statusLabel.setText(
                    "Solution displayed."
            );
        }

        // =====================================================
        // SCORE
        // =====================================================

        private void updateScore() {

            scoreLabel.setText(
                    "Score: "
                            + score
                            + "    Mistakes: "
                            + mistakes
            );
        }

        private void saveScore() {

            try {

                ScoreDAO dao =
                        new ScoreDAO();

                dao.saveScore(
                        playerName,
                        "Sudoku",
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

    // =========================================================
    // MAIN
    // =========================================================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(
                () -> new SudokuGame("Guest")
        );
    }
}