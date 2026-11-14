import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GuessGame extends JFrame {

    private final String playerName;

    public GuessGame() {
        this("Guest");
    }

    public GuessGame(String playerName) {
        this.playerName =
                (playerName == null || playerName.trim().isEmpty())
                        ? "Guest"
                        : playerName.trim();

        setTitle("GamesMania - Number Guess");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        add(new GuessPanel());

        setVisible(true);
    }

    private class GuessPanel extends JPanel {

        private final Random random = new Random();

        private int secretNumber;
        private int attempts;
        private int score;

        private JTextField inputField;
        private JLabel messageLabel;
        private JLabel scoreLabel;
        private JLabel attemptsLabel;

        private JButton guessButton;
        private JButton newGameButton;

        GuessPanel() {

            setLayout(new BorderLayout(15, 15));
            setBackground(new Color(25, 30, 40));

            setBorder(
                    BorderFactory.createEmptyBorder(
                            25, 25, 25, 25
                    )
            );

            createGame();
            startNewGame();
        }

        private void createGame() {

            // ---------------- TOP ----------------

            JPanel topPanel =
                    new JPanel(new GridLayout(3, 1));

            topPanel.setOpaque(false);

            JLabel title =
                    new JLabel(
                            "NUMBER GUESS",
                            SwingConstants.CENTER
                    );

            title.setFont(
                    new Font(
                            "Arial",
                            Font.BOLD,
                            34
                    )
            );

            title.setForeground(Color.WHITE);

            JLabel player =
                    new JLabel(
                            "Player: " + playerName,
                            SwingConstants.CENTER
                    );

            player.setFont(
                    new Font(
                            "Arial",
                            Font.PLAIN,
                            18
                    )
            );

            player.setForeground(Color.LIGHT_GRAY);

            JLabel instruction =
                    new JLabel(
                            "Guess a number between 1 and 100",
                            SwingConstants.CENTER
                    );

            instruction.setFont(
                    new Font(
                            "Arial",
                            Font.PLAIN,
                            17
                    )
            );

            instruction.setForeground(Color.CYAN);

            topPanel.add(title);
            topPanel.add(player);
            topPanel.add(instruction);

            add(topPanel, BorderLayout.NORTH);

            // ---------------- CENTER ----------------

            JPanel centerPanel =
                    new JPanel();

            centerPanel.setLayout(
                    new BoxLayout(
                            centerPanel,
                            BoxLayout.Y_AXIS
                    )
            );

            centerPanel.setOpaque(false);

            messageLabel =
                    new JLabel(
                            "Enter your guess!",
                            SwingConstants.CENTER
                    );

            messageLabel.setFont(
                    new Font(
                            "Arial",
                            Font.BOLD,
                            24
                    )
            );

            messageLabel.setForeground(Color.WHITE);

            messageLabel.setAlignmentX(
                    Component.CENTER_ALIGNMENT
            );

            centerPanel.add(
                    Box.createVerticalStrut(25)
            );

            centerPanel.add(messageLabel);

            centerPanel.add(
                    Box.createVerticalStrut(25)
            );

            inputField =
                    new JTextField();

            inputField.setFont(
                    new Font(
                            "Arial",
                            Font.BOLD,
                            28
                    )
            );

            inputField.setHorizontalAlignment(
                    JTextField.CENTER
            );

            inputField.setMaximumSize(
                    new Dimension(250, 55)
            );

            inputField.addActionListener(
                    e -> checkGuess()
            );

            centerPanel.add(inputField);

            centerPanel.add(
                    Box.createVerticalStrut(20)
            );

            guessButton =
                    new JButton("GUESS");

            guessButton.setFont(
                    new Font(
                            "Arial",
                            Font.BOLD,
                            20
                    )
            );

            guessButton.setAlignmentX(
                    Component.CENTER_ALIGNMENT
            );

            guessButton.setFocusPainted(false);

            guessButton.addActionListener(
                    e -> checkGuess()
            );

            centerPanel.add(guessButton);

            centerPanel.add(
                    Box.createVerticalStrut(20)
            );

            scoreLabel =
                    new JLabel(
                            "Score: 0",
                            SwingConstants.CENTER
                    );

            scoreLabel.setFont(
                    new Font(
                            "Arial",
                            Font.BOLD,
                            18
                    )
            );

            scoreLabel.setForeground(Color.YELLOW);

            scoreLabel.setAlignmentX(
                    Component.CENTER_ALIGNMENT
            );

            centerPanel.add(scoreLabel);

            attemptsLabel =
                    new JLabel(
                            "Attempts: 0",
                            SwingConstants.CENTER
                    );

            attemptsLabel.setFont(
                    new Font(
                            "Arial",
                            Font.BOLD,
                            18
                    )
            );

            attemptsLabel.setForeground(Color.WHITE);

            attemptsLabel.setAlignmentX(
                    Component.CENTER_ALIGNMENT
            );

            centerPanel.add(attemptsLabel);

            add(
                    centerPanel,
                    BorderLayout.CENTER
            );

            // ---------------- BOTTOM ----------------

            JPanel bottomPanel =
                    new JPanel(
                            new FlowLayout(
                                    FlowLayout.CENTER,
                                    15,
                                    5
                            )
                    );

            bottomPanel.setOpaque(false);

            newGameButton =
                    new JButton("NEW GAME");

            newGameButton.setFont(
                    new Font(
                            "Arial",
                            Font.BOLD,
                            16
                    )
            );

            newGameButton.addActionListener(
                    e -> startNewGame()
            );

            JButton closeButton =
                    new JButton("CLOSE");

            closeButton.setFont(
                    new Font(
                            "Arial",
                            Font.BOLD,
                            16
                    )
            );

            closeButton.addActionListener(
                    e -> {
                        saveScore();
                        dispose();
                    }
            );

            bottomPanel.add(newGameButton);
            bottomPanel.add(closeButton);

            add(
                    bottomPanel,
                    BorderLayout.SOUTH
            );
        }

        private void startNewGame() {

            secretNumber =
                    random.nextInt(100) + 1;

            attempts = 0;
            score = 100;

            inputField.setText("");

            inputField.setEnabled(true);
            guessButton.setEnabled(true);

            messageLabel.setText(
                    "Enter your guess!"
            );

            messageLabel.setForeground(
                    Color.WHITE
            );

            scoreLabel.setText(
                    "Score: " + score
            );

            attemptsLabel.setText(
                    "Attempts: 0"
            );

            inputField.requestFocusInWindow();
        }

        private void checkGuess() {

            String text =
                    inputField.getText().trim();

            if (text.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please enter a number."
                );

                return;
            }

            int guess;

            try {

                guess = Integer.parseInt(text);

            } catch (NumberFormatException e) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please enter a valid number."
                );

                inputField.selectAll();
                inputField.requestFocusInWindow();

                return;
            }

            if (guess < 1 || guess > 100) {

                JOptionPane.showMessageDialog(
                        this,
                        "Enter a number between 1 and 100."
                );

                inputField.selectAll();
                inputField.requestFocusInWindow();

                return;
            }

            attempts++;

            attemptsLabel.setText(
                    "Attempts: " + attempts
            );

            if (guess == secretNumber) {

                score += 20;

                messageLabel.setText(
                        "🎉 Correct! The number was "
                                + secretNumber
                );

                messageLabel.setForeground(
                        Color.GREEN
                );

                scoreLabel.setText(
                        "Final Score: " + score
                );

                inputField.setEnabled(false);
                guessButton.setEnabled(false);

                saveScore();

                JOptionPane.showMessageDialog(
                        this,
                        "Congratulations, " +
                                playerName +
                                "!\n\n" +
                                "You guessed the number in "
                                + attempts +
                                " attempts.\n" +
                                "Score: " + score,
                        "You Win!",
                        JOptionPane.INFORMATION_MESSAGE
                );

            } else if (guess < secretNumber) {

                score -= 5;

                if (score < 0) {
                    score = 0;
                }

                messageLabel.setText(
                        "Too LOW! Try again."
                );

                messageLabel.setForeground(
                        Color.ORANGE
                );

                scoreLabel.setText(
                        "Score: " + score
                );

                inputField.selectAll();
                inputField.requestFocusInWindow();

            } else {

                score -= 5;

                if (score < 0) {
                    score = 0;
                }

                messageLabel.setText(
                        "Too HIGH! Try again."
                );

                messageLabel.setForeground(
                        Color.RED
                );

                scoreLabel.setText(
                        "Score: " + score
                );

                inputField.selectAll();
                inputField.requestFocusInWindow();
            }

            // Maximum 10 attempts
            if (attempts >= 10 &&
                    guess != secretNumber) {

                inputField.setEnabled(false);
                guessButton.setEnabled(false);

                messageLabel.setText(
                        "Game Over! Number: "
                                + secretNumber
                );

                messageLabel.setForeground(
                        Color.RED
                );

                saveScore();

                JOptionPane.showMessageDialog(
                        this,
                        "Game Over!\n\n" +
                                "The number was: "
                                + secretNumber +
                                "\nFinal Score: "
                                + score,
                        "Number Guess",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        }

        private void saveScore() {

            try {

                ScoreDAO dao =
                        new ScoreDAO();

                dao.saveScore(
                        playerName,
                        "Guess Game",
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

        SwingUtilities.invokeLater(
                () -> new GuessGame("Guest")
        );
    }
}