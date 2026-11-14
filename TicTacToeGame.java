
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

public class TicTacToeGame extends JFrame {

    // =========================================================
    // COLORS
    // =========================================================

    private static final Color BG =
            new Color(246, 248, 252);

    private static final Color WHITE =
            Color.WHITE;

    private static final Color TEXT =
            new Color(35, 38, 52);

    private static final Color MUTED =
            new Color(105, 110, 125);

    private static final Color PURPLE =
            new Color(92, 83, 220);

    private static final Color PURPLE_LIGHT =
            new Color(238, 236, 255);

    private static final Color MINT =
            new Color(47, 190, 167);

    private static final Color ORANGE =
            new Color(245, 175, 65);

    private static final Color RED =
            new Color(235, 80, 90);

    private static final Color BLUE =
            new Color(75, 130, 220);

    private static final Color GRID =
            new Color(205, 207, 218);


    // =========================================================
    // GAME DATA
    // =========================================================

    private final char[][] board = new char[3][3];

    private String playerName = "Player";
    private String difficulty = "Medium";

    private int score = 0;
    private int wins = 0;
    private int losses = 0;
    private int draws = 0;
    private int seconds = 0;

    private boolean running = false;
    private boolean paused = false;
    private boolean gameFinished = false;
    private boolean playerTurn = true;
    private boolean computerThinking = false;

    private int winningRow = -1;
    private int winningCol = -1;
    private boolean winningDiagonal = false;
    private boolean reverseDiagonal = false;


    // =========================================================
    // UI
    // =========================================================

    private TicTacToePanel boardPanel;

    private JLabel playerLabel;
    private JLabel modeLabel;

    private JLabel statusLabel;
    private JLabel scoreLabel;
    private JLabel recordLabel;
    private JLabel timeLabel;

    private JButton startButton;
    private JButton restartButton;
    private JButton backButton;


    // =========================================================
    // TIMERS
    // =========================================================

    private Timer clockTimer;
    private Timer computerTimer;

    private final Random random = new Random();


    // =========================================================
    // CONSTRUCTORS
    // =========================================================

    public TicTacToeGame() {
        this("Player", "Medium");
    }

    public TicTacToeGame(String playerName) {
        this(playerName, "Medium");
    }

    public TicTacToeGame(String playerName, String difficulty) {

        if (playerName != null &&
                !playerName.trim().isEmpty()) {

            this.playerName = playerName.trim();
        }

        if (difficulty != null &&
                !difficulty.trim().isEmpty()) {

            this.difficulty = difficulty;
        }

        setTitle("GamesMania • Tic Tac Toe");

        /*
         * IMPORTANT:
         * This size gives enough room for:
         * Header + board + footer
         */
        setSize(850, 700);

        setMinimumSize(new Dimension(760, 620));

        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        setResizable(false);

        buildUI();

        initializeBoard();

        createTimers();
    }


    // =========================================================
    // BUILD UI
    // =========================================================

    private void buildUI() {

        JPanel root =
                new JPanel(new BorderLayout());

        root.setBackground(BG);

        setContentPane(root);

        createHeader(root);

        createGameArea(root);

        createFooter(root);
    }


    // =========================================================
    // HEADER
    // =========================================================

    private void createHeader(JPanel root) {

        JPanel header =
                new JPanel(new BorderLayout());

        header.setBackground(WHITE);

        header.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(
                                0, 0, 1, 0,
                                new Color(225, 227, 235)
                        ),
                        BorderFactory.createEmptyBorder(
                                14, 22, 14, 22
                        )
                )
        );


        // ---------------- LEFT ----------------

        JPanel left =
                new JPanel();

        left.setOpaque(false);

        left.setLayout(
                new BoxLayout(
                        left,
                        BoxLayout.Y_AXIS
                )
        );


        JLabel title =
                new JLabel("⭕  TIC TAC TOE");

        title.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        28
                )
        );

        title.setForeground(TEXT);


        JLabel subtitle =
                new JLabel(
                        "Beat the computer • Three in a row wins"
                );

        subtitle.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        14
                )
        );

        subtitle.setForeground(MUTED);


        left.add(title);

        left.add(
                Box.createVerticalStrut(4)
        );

        left.add(subtitle);


        header.add(
                left,
                BorderLayout.WEST
        );


        // ---------------- RIGHT ----------------

        JPanel right =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                12,
                                2
                        )
                );

        right.setOpaque(false);


        playerLabel =
                new JLabel(
                        "♟ " + playerName
                );

        playerLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        16
                )
        );

        playerLabel.setForeground(PURPLE);


        modeLabel =
                new JLabel(
                        difficulty.toUpperCase()
                );

        modeLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        14
                )
        );

        modeLabel.setForeground(
                getModeColor()
        );

        modeLabel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                getModeColor()
                        ),
                        BorderFactory.createEmptyBorder(
                                7, 14, 7, 14
                        )
                )
        );


        right.add(playerLabel);
        right.add(modeLabel);


        header.add(
                right,
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

    private void createGameArea(JPanel root) {

        /*
         * BorderLayout is used instead of GridBagLayout.
         *
         * This is the main fix for your disappearing board.
         */

        JPanel area =
                new JPanel(
                        new GridBagLayout()
                );

        area.setBackground(BG);

        area.setBorder(
                BorderFactory.createEmptyBorder(
                        15, 20, 12, 20
                )
        );


        boardPanel =
                new TicTacToePanel();


        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;

        gbc.weightx = 1;
        gbc.weighty = 1;

        gbc.anchor =
                GridBagConstraints.CENTER;

        gbc.fill =
                GridBagConstraints.NONE;


        area.add(
                boardPanel,
                gbc
        );


        root.add(
                area,
                BorderLayout.CENTER
        );
    }


    // =========================================================
    // FOOTER
    // =========================================================

    private void createFooter(JPanel root) {

        JPanel footer =
                new JPanel(
                        new BorderLayout()
                );

        footer.setBackground(WHITE);

        footer.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(
                                1, 0, 0, 0,
                                new Color(225, 227, 235)
                        ),
                        BorderFactory.createEmptyBorder(
                                8, 15, 8, 15
                        )
                )
        );


        // ---------------- STATUS ----------------

        statusLabel =
                new JLabel(
                        "Press START to begin"
                );

        statusLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        13
                )
        );

        statusLabel.setForeground(MUTED);


        footer.add(
                statusLabel,
                BorderLayout.WEST
        );


        // ---------------- STATS ----------------

        JPanel stats =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                20,
                                2
                        )
                );

        stats.setOpaque(false);


        scoreLabel =
                createStat(
                        "SCORE  0",
                        PURPLE
                );


        recordLabel =
                createStat(
                        "W/L/D  0/0/0",
                        MINT
                );


        timeLabel =
                createStat(
                        "TIME  00:00",
                        BLUE
                );


        stats.add(scoreLabel);
        stats.add(recordLabel);
        stats.add(timeLabel);


        footer.add(
                stats,
                BorderLayout.CENTER
        );


        // ---------------- BUTTONS ----------------

        JPanel buttons =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                8,
                                0
                        )
                );

        buttons.setOpaque(false);


        startButton =
                createButton(
                        "▶ START",
                        PURPLE
                );

        restartButton =
                createButton(
                        "↻ RESTART",
                        MINT
                );

        backButton =
                createButton(
                        "← BACK",
                        RED
                );


        startButton.addActionListener(
                e -> toggleStart()
        );

        restartButton.addActionListener(
                e -> restartRound()
        );

        backButton.addActionListener(
                e -> dispose()
        );


        buttons.add(startButton);
        buttons.add(restartButton);
        buttons.add(backButton);


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
    // STAT
    // =========================================================

    private JLabel createStat(
            String text,
            Color color) {

        JLabel label =
                new JLabel(text);

        label.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        13
                )
        );

        label.setForeground(color);

        return label;
    }


    // =========================================================
    // BUTTON
    // =========================================================

    private JButton createButton(
            String text,
            Color color) {

        JButton button =
                new JButton(text);

        button.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        13
                )
        );

        button.setForeground(color);

        button.setBackground(WHITE);

        button.setFocusPainted(false);

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
                                8, 15, 8, 15
                        )
                )
        );


        button.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseEntered(
                            MouseEvent e) {

                        button.setBackground(
                                new Color(248, 248, 255)
                        );
                    }


                    @Override
                    public void mouseExited(
                            MouseEvent e) {

                        button.setBackground(
                                WHITE
                        );
                    }
                }
        );


        return button;
    }


    // =========================================================
    // INITIALIZE BOARD
    // =========================================================

    private void initializeBoard() {

        for (int r = 0; r < 3; r++) {

            for (int c = 0; c < 3; c++) {

                board[r][c] = ' ';
            }
        }


        running = false;

        paused = false;

        gameFinished = false;

        playerTurn = true;

        computerThinking = false;

        seconds = 0;


        winningRow = -1;
        winningCol = -1;
        winningDiagonal = false;
        reverseDiagonal = false;


        updateStats();


        if (statusLabel != null) {

            statusLabel.setText(
                    "Press START • You are X"
            );
        }


        if (startButton != null) {

            startButton.setText(
                    "▶ START"
            );
        }


        if (boardPanel != null) {

            boardPanel.repaint();
        }
    }


    // =========================================================
    // TIMERS
    // =========================================================

    private void createTimers() {

        clockTimer =
                new Timer(
                        1000,
                        e -> {

                            if (running &&
                                    !paused &&
                                    !gameFinished) {

                                seconds++;

                                updateStats();
                            }
                        }
                );


        computerTimer =
                new Timer(
                        500,
                        e -> {

                            computerTimer.stop();

                            if (running &&
                                    !paused &&
                                    !gameFinished &&
                                    !playerTurn) {

                                computerThinking =
                                        false;

                                computerMove();
                            }
                        }
                );
    }


    // =========================================================
    // START / PAUSE
    // =========================================================

    private void toggleStart() {

        if (gameFinished) {

            restartRound();

            return;
        }


        if (!running) {

            running = true;

            paused = false;

            clockTimer.start();

            startButton.setText(
                    "Ⅱ PAUSE"
            );

            statusLabel.setText(
                    "Your turn • You are X"
            );

            boardPanel.repaint();

            return;
        }


        paused = !paused;


        if (paused) {

            startButton.setText(
                    "▶ RESUME"
            );

            statusLabel.setText(
                    "Game paused"
            );

        } else {

            startButton.setText(
                    "Ⅱ PAUSE"
            );

            statusLabel.setText(
                    playerTurn
                            ? "Your turn • X"
                            : "Computer's turn • O"
            );
        }


        boardPanel.repaint();
    }


    // =========================================================
    // PLAYER MOVE
    // =========================================================

    private void playerMove(
            int row,
            int col) {

        if (!running ||
                paused ||
                gameFinished ||
                !playerTurn ||
                computerThinking) {

            return;
        }


        if (board[row][col] != ' ') {

            statusLabel.setText(
                    "That cell is already occupied"
            );

            return;
        }


        board[row][col] = 'X';

        playerTurn = false;


        boardPanel.repaint();


        if (checkWinner('X')) {

            finishRound("WIN");

            return;
        }


        if (isBoardFull()) {

            finishRound("DRAW");

            return;
        }


        statusLabel.setText(
                "Computer is thinking..."
        );

        computerThinking = true;

        computerTimer.start();
    }


    // =========================================================
    // COMPUTER MOVE
    // =========================================================

    private void computerMove() {

        if (!running ||
                paused ||
                gameFinished) {

            return;
        }


        int[] move;


        if (difficulty.equalsIgnoreCase("Easy")) {

            move = easyMove();

        } else {

            /*
             * Medium:
             * Win if possible
             * Block player
             * Take center
             * Take corner
             * Otherwise random
             */
            move = mediumMove();
        }


        if (move[0] == -1) {

            return;
        }


        board[move[0]][move[1]] = 'O';

        playerTurn = true;


        boardPanel.repaint();


        if (checkWinner('O')) {

            finishRound("LOSS");

            return;
        }


        if (isBoardFull()) {

            finishRound("DRAW");

            return;
        }


        statusLabel.setText(
                "Your turn • You are X"
        );
    }


    // =========================================================
    // EASY AI
    // =========================================================

    private int[] easyMove() {

        int[][] empty =
                getEmptyCells();


        if (empty.length == 0) {

            return new int[]{-1, -1};
        }


        return empty[
                random.nextInt(
                        empty.length
                )
                ];
    }


    // =========================================================
    // MEDIUM AI
    // =========================================================

    private int[] mediumMove() {

        // Computer winning move
        int[] move =
                findWinningMove('O');

        if (move[0] != -1) {

            return move;
        }


        // Block player's winning move
        move =
                findWinningMove('X');

        if (move[0] != -1) {

            return move;
        }


        // Center
        if (board[1][1] == ' ') {

            return new int[]{1, 1};
        }


        // Corners
        int[][] corners = {

                {0, 0},
                {0, 2},
                {2, 0},
                {2, 2}
        };


        for (int[] corner : corners) {

            if (board[
                    corner[0]
                    ][
                    corner[1]
                    ] == ' ') {

                return corner;
            }
        }


        return easyMove();
    }


    // =========================================================
    // FIND WINNING MOVE
    // =========================================================

    private int[] findWinningMove(
            char player) {

        for (int r = 0; r < 3; r++) {

            for (int c = 0; c < 3; c++) {

                if (board[r][c] == ' ') {

                    board[r][c] = player;

                    boolean wins =
                            checkWinner(player);

                    board[r][c] = ' ';


                    if (wins) {

                        return new int[]{
                                r,
                                c
                        };
                    }
                }
            }
        }


        return new int[]{
                -1,
                -1
        };
    }


    // =========================================================
    // GET EMPTY CELLS
    // =========================================================

    private int[][] getEmptyCells() {

        int count = 0;


        for (int r = 0; r < 3; r++) {

            for (int c = 0; c < 3; c++) {

                if (board[r][c] == ' ') {

                    count++;
                }
            }
        }


        int[][] result =
                new int[count][2];


        int index = 0;


        for (int r = 0; r < 3; r++) {

            for (int c = 0; c < 3; c++) {

                if (board[r][c] == ' ') {

                    result[index][0] = r;

                    result[index][1] = c;

                    index++;
                }
            }
        }


        return result;
    }


    // =========================================================
    // CHECK WINNER
    // =========================================================

    private boolean checkWinner(
            char player) {

        // Reset winning information
        winningRow = -1;
        winningCol = -1;
        winningDiagonal = false;
        reverseDiagonal = false;


        // Rows
        for (int r = 0; r < 3; r++) {

            if (board[r][0] == player &&
                    board[r][1] == player &&
                    board[r][2] == player) {

                winningRow = r;

                return true;
            }
        }


        // Columns
        for (int c = 0; c < 3; c++) {

            if (board[0][c] == player &&
                    board[1][c] == player &&
                    board[2][c] == player) {

                winningCol = c;

                return true;
            }
        }


        // Main diagonal
        if (board[0][0] == player &&
                board[1][1] == player &&
                board[2][2] == player) {

            winningDiagonal = true;

            return true;
        }


        // Reverse diagonal
        if (board[0][2] == player &&
                board[1][1] == player &&
                board[2][0] == player) {

            reverseDiagonal = true;

            return true;
        }


        return false;
    }


    // =========================================================
    // BOARD FULL
    // =========================================================

    private boolean isBoardFull() {

        for (int r = 0; r < 3; r++) {

            for (int c = 0; c < 3; c++) {

                if (board[r][c] == ' ') {

                    return false;
                }
            }
        }


        return true;
    }


    // =========================================================
    // FINISH ROUND
    // =========================================================

    private void finishRound(
            String result) {

        gameFinished = true;

        running = false;

        computerThinking = false;


        clockTimer.stop();

        computerTimer.stop();


        if (result.equals("WIN")) {

            wins++;

            score +=
                    350 +
                            Math.max(
                                    0,
                                    200 - seconds
                            );


            statusLabel.setText(
                    "🏆 YOU WIN!"
            );


        } else if (result.equals("LOSS")) {

            losses++;

            score =
                    Math.max(
                            0,
                            score - 100
                    );


            statusLabel.setText(
                    "💻 COMPUTER WINS"
            );


        } else {

            draws++;

            score += 100;


            statusLabel.setText(
                    "🤝 DRAW"
            );
        }


        updateStats();

        boardPanel.repaint();

        try {
            ScoreDAO dao = new ScoreDAO();
            dao.saveScore(playerName, "Tic Tac Toe", difficulty, score);
        } catch (Exception e) {
            e.printStackTrace();
        }

        showResultDialog(result);
    }


    // =========================================================
    // RESULT DIALOG
    // =========================================================

    private void showResultDialog(
            String result) {

        String title;

        String message;


        if (result.equals("WIN")) {

            title =
                    "🏆 You Win!";

            message =
                    playerName +
                            ", congratulations!";

        } else if (result.equals("LOSS")) {

            title =
                    "💻 Computer Wins";

            message =
                    "Good try, " +
                            playerName +
                            "!";

        } else {

            title =
                    "🤝 It's a Draw!";

            message =
                    "Well played, " +
                            playerName +
                            "!";
        }


        JDialog dialog =
                new JDialog(
                        this,
                        title,
                        true
                );


        dialog.setSize(
                360,
                230
        );


        dialog.setResizable(false);

        dialog.setLocationRelativeTo(this);


        JPanel panel =
                new JPanel();

        panel.setBackground(WHITE);

        panel.setLayout(
                new BoxLayout(
                        panel,
                        BoxLayout.Y_AXIS
                )
        );


        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        20, 20, 20, 20
                )
        );


        JLabel titleLabel =
                new JLabel(title);

        titleLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        22
                )
        );

        titleLabel.setForeground(PURPLE);

        titleLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );


        JLabel messageLabel =
                new JLabel(message);

        messageLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        14
                )
        );

        messageLabel.setForeground(MUTED);

        messageLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );


        JLabel scoreText =
                new JLabel(
                        "Score: " +
                                score +
                                "     W/L/D: " +
                                wins +
                                "/" +
                                losses +
                                "/" +
                                draws
                );

        scoreText.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        13
                )
        );

        scoreText.setForeground(TEXT);

        scoreText.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );


        JButton newRound =
                new JButton(
                        "PLAY AGAIN"
                );

        newRound.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        13
                )
        );

        newRound.setForeground(WHITE);

        newRound.setBackground(PURPLE);

        newRound.setFocusPainted(false);

        newRound.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );


        newRound.addActionListener(
                e -> {

                    dialog.dispose();

                    restartRound();

                    toggleStart();
                }
        );


        panel.add(titleLabel);

        panel.add(
                Box.createVerticalStrut(12)
        );

        panel.add(messageLabel);

        panel.add(
                Box.createVerticalStrut(10)
        );

        panel.add(scoreText);

        panel.add(
                Box.createVerticalStrut(18)
        );

        panel.add(newRound);


        dialog.setContentPane(panel);

        dialog.setVisible(true);
    }


    // =========================================================
    // RESTART
    // =========================================================

    private void restartRound() {

        clockTimer.stop();

        computerTimer.stop();

        initializeBoard();
    }


    // =========================================================
    // STATS
    // =========================================================

    private void updateStats() {

        if (scoreLabel != null) {

            scoreLabel.setText(
                    "SCORE  " + score
            );
        }


        if (recordLabel != null) {

            recordLabel.setText(
                    "W/L/D  " +
                            wins +
                            "/" +
                            losses +
                            "/" +
                            draws
            );
        }


        if (timeLabel != null) {

            int minutes =
                    seconds / 60;

            int secs =
                    seconds % 60;


            timeLabel.setText(
                    String.format(
                            "TIME  %02d:%02d",
                            minutes,
                            secs
                    )
            );
        }
    }


    // =========================================================
    // MODE COLOR
    // =========================================================

    private Color getModeColor() {

        if (difficulty.equalsIgnoreCase("Easy")) {

            return MINT;
        }

        if (difficulty.equalsIgnoreCase("Hard")) {

            return RED;
        }

        return ORANGE;
    }


    // =========================================================
    // BOARD PANEL
    // =========================================================

    private class TicTacToePanel
            extends JPanel {


        private int hoverRow = -1;

        private int hoverCol = -1;


        TicTacToePanel() {

            setPreferredSize(
                    new Dimension(
                            500,
                            500
                    )
            );

            setMinimumSize(
                    new Dimension(
                            450,
                            450
                    )
            );

            setBackground(
                    WHITE
            );

            setBorder(
                    BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(
                                    new Color(
                                            220,
                                            222,
                                            232
                                    ),
                                    2
                            ),
                            BorderFactory.createEmptyBorder(
                                    8, 8, 8, 8
                            )
                    )
            );


            // =================================================
            // MOUSE ONLY
            // =================================================

            addMouseListener(
                    new MouseAdapter() {

                        @Override
                        public void mousePressed(
                                MouseEvent e) {

                            int[] cell =
                                    getCell(
                                            e.getX(),
                                            e.getY()
                                    );


                            if (cell[0] != -1) {

                                playerMove(
                                        cell[0],
                                        cell[1]
                                );
                            }
                        }


                        @Override
                        public void mouseExited(
                                MouseEvent e) {

                            hoverRow = -1;

                            hoverCol = -1;

                            repaint();
                        }
                    }
            );


            addMouseMotionListener(
                    new java.awt.event.MouseMotionAdapter() {

                        @Override
                        public void mouseMoved(
                                MouseEvent e) {

                            int[] cell =
                                    getCell(
                                            e.getX(),
                                            e.getY()
                                    );


                            hoverRow =
                                    cell[0];

                            hoverCol =
                                    cell[1];

                            repaint();
                        }
                    }
            );
        }


        // =====================================================
        // GET CELL
        // =====================================================

        private int[] getCell(
                int mouseX,
                int mouseY) {

            int size =
                    Math.min(
                            getWidth(),
                            getHeight()
                    ) - 30;


            size =
                    Math.min(
                            size,
                            480
                    );


            if (size <= 0) {

                return new int[]{
                        -1,
                        -1
                };
            }


            int startX =
                    (getWidth() - size) / 2;

            int startY =
                    (getHeight() - size) / 2;


            if (mouseX < startX ||
                    mouseX >= startX + size ||
                    mouseY < startY ||
                    mouseY >= startY + size) {

                return new int[]{
                        -1,
                        -1
                };
            }


            int cellSize =
                    size / 3;


            int col =
                    (mouseX - startX)
                            / cellSize;

            int row =
                    (mouseY - startY)
                            / cellSize;


            if (row < 0 ||
                    row > 2 ||
                    col < 0 ||
                    col > 2) {

                return new int[]{
                        -1,
                        -1
                };
            }


            return new int[]{
                    row,
                    col
            };
        }


        // =====================================================
        // PAINT
        // =====================================================

        @Override
        protected void paintComponent(
                Graphics g) {

            super.paintComponent(g);


            Graphics2D g2 =
                    (Graphics2D)
                            g.create();


            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );


            int size =
                    Math.min(
                            getWidth(),
                            getHeight()
                    ) - 30;


            size =
                    Math.min(
                            size,
                            480
                    );


            int startX =
                    (getWidth() - size) / 2;

            int startY =
                    (getHeight() - size) / 2;


            int cellSize =
                    size / 3;


            // =================================================
            // BOARD
            // =================================================

            g2.setColor(WHITE);

            g2.fillRoundRect(
                    startX,
                    startY,
                    size,
                    size,
                    22,
                    22
            );


            // =================================================
            // HOVER CELL
            // =================================================

            if (hoverRow >= 0 &&
                    hoverCol >= 0 &&
                    running &&
                    !paused &&
                    playerTurn &&
                    !computerThinking &&
                    board[hoverRow][hoverCol] == ' ') {

                int x =
                        startX +
                                hoverCol * cellSize;

                int y =
                        startY +
                                hoverRow * cellSize;


                g2.setColor(
                        PURPLE_LIGHT
                );


                g2.fillRoundRect(
                        x + 5,
                        y + 5,
                        cellSize - 10,
                        cellSize - 10,
                        18,
                        18
                );
            }


            // =================================================
            // GRID
            // =================================================

            g2.setColor(GRID);

            g2.setStroke(
                    new BasicStroke(
                            4f,
                            BasicStroke.CAP_ROUND,
                            BasicStroke.JOIN_ROUND
                    )
            );


            for (int i = 1; i < 3; i++) {

                int x =
                        startX +
                                i * cellSize;

                int y =
                        startY +
                                i * cellSize;


                g2.drawLine(
                        x,
                        startY + 10,
                        x,
                        startY + size - 10
                );


                g2.drawLine(
                        startX + 10,
                        y,
                        startX + size - 10,
                        y
                );
            }


            // =================================================
            // X AND O
            // =================================================

            for (int r = 0; r < 3; r++) {

                for (int c = 0; c < 3; c++) {

                    if (board[r][c] == 'X') {

                        drawX(
                                g2,
                                startX +
                                        c * cellSize,
                                startY +
                                        r * cellSize,
                                cellSize
                        );

                    } else if (board[r][c] == 'O') {

                        drawO(
                                g2,
                                startX +
                                        c * cellSize,
                                startY +
                                        r * cellSize,
                                cellSize
                        );
                    }
                }
            }


            // =================================================
            // WINNING LINE
            // =================================================

            drawWinningLine(
                    g2,
                    startX,
                    startY,
                    cellSize
            );


            // =================================================
            // READY OVERLAY
            // =================================================

            if (!running &&
                    !gameFinished) {

                drawOverlay(
                        g2,
                        "⭕ READY?",
                        "Click START to play"
                );
            }


            // =================================================
            // PAUSE OVERLAY
            // =================================================

            if (paused) {

                drawOverlay(
                        g2,
                        "Ⅱ PAUSED",
                        "Click RESUME to continue"
                );
            }


            g2.dispose();
        }


        // =====================================================
        // DRAW X
        // =====================================================

        private void drawX(
                Graphics2D g2,
                int x,
                int y,
                int cellSize) {

            int padding =
                    Math.max(
                            28,
                            cellSize / 5
                    );


            g2.setColor(PURPLE);


            g2.setStroke(
                    new BasicStroke(
                            10f,
                            BasicStroke.CAP_ROUND,
                            BasicStroke.JOIN_ROUND
                    )
            );


            g2.drawLine(
                    x + padding,
                    y + padding,
                    x + cellSize - padding,
                    y + cellSize - padding
            );


            g2.drawLine(
                    x + cellSize - padding,
                    y + padding,
                    x + padding,
                    y + cellSize - padding
            );
        }


        // =====================================================
        // DRAW O
        // =====================================================

        private void drawO(
                Graphics2D g2,
                int x,
                int y,
                int cellSize) {

            int padding =
                    Math.max(
                            28,
                            cellSize / 5
                    );


            g2.setColor(MINT);


            g2.setStroke(
                    new BasicStroke(
                            10f,
                            BasicStroke.CAP_ROUND,
                            BasicStroke.JOIN_ROUND
                    )
            );


            g2.drawOval(
                    x + padding,
                    y + padding,
                    cellSize - padding * 2,
                    cellSize - padding * 2
            );
        }


        // =====================================================
        // WINNING LINE
        // =====================================================

        private void drawWinningLine(
                Graphics2D g2,
                int startX,
                int startY,
                int cellSize) {

            char winner =
                    getWinner();


            if (winner == ' ') {

                return;
            }


            g2.setColor(ORANGE);


            g2.setStroke(
                    new BasicStroke(
                            7f,
                            BasicStroke.CAP_ROUND,
                            BasicStroke.JOIN_ROUND
                    )
            );


            // Row
            if (winningRow != -1) {

                int y =
                        startY +
                                winningRow * cellSize +
                                cellSize / 2;


                g2.drawLine(
                        startX + 18,
                        y,
                        startX +
                                3 * cellSize -
                                18,
                        y
                );


                return;
            }


            // Column
            if (winningCol != -1) {

                int x =
                        startX +
                                winningCol * cellSize +
                                cellSize / 2;


                g2.drawLine(
                        x,
                        startY + 18,
                        x,
                        startY +
                                3 * cellSize -
                                18
                );


                return;
            }


            // Main diagonal
            if (winningDiagonal) {

                g2.drawLine(
                        startX + 25,
                        startY + 25,
                        startX +
                                3 * cellSize -
                                25,
                        startY +
                                3 * cellSize -
                                25
                );


                return;
            }


            // Reverse diagonal
            if (reverseDiagonal) {

                g2.drawLine(
                        startX +
                                3 * cellSize -
                                25,
                        startY + 25,
                        startX + 25,
                        startY +
                                3 * cellSize -
                                25
                );
            }
        }


        // =====================================================
        // GET WINNER
        // =====================================================

        private char getWinner() {

            if (checkWinnerWithoutReset('X')) {

                return 'X';
            }


            if (checkWinnerWithoutReset('O')) {

                return 'O';
            }


            return ' ';
        }


        // =====================================================
        // CHECK WINNER WITHOUT RESETTING LINE
        // =====================================================

        private boolean checkWinnerWithoutReset(
                char player) {

            for (int r = 0; r < 3; r++) {

                if (board[r][0] == player &&
                        board[r][1] == player &&
                        board[r][2] == player) {

                    return true;
                }
            }


            for (int c = 0; c < 3; c++) {

                if (board[0][c] == player &&
                        board[1][c] == player &&
                        board[2][c] == player) {

                    return true;
                }
            }


            if (board[0][0] == player &&
                    board[1][1] == player &&
                    board[2][2] == player) {

                return true;
            }


            return board[0][2] == player &&
                    board[1][1] == player &&
                    board[2][0] == player;
        }


        // =====================================================
        // SMALL READY / PAUSE OVERLAY
        // =====================================================

        private void drawOverlay(
                Graphics2D g2,
                String title,
                String subtitle) {

            int overlayWidth = 300;

            int overlayHeight = 105;


            int x =
                    getWidth() / 2 -
                            overlayWidth / 2;


            int y =
                    getHeight() / 2 -
                            overlayHeight / 2;


            // Shadow
            g2.setColor(
                    new Color(
                            0,
                            0,
                            0,
                            25
                    )
            );


            g2.fillRoundRect(
                    x + 5,
                    y + 7,
                    overlayWidth,
                    overlayHeight,
                    20,
                    20
            );


            // White box
            g2.setColor(
                    new Color(
                            255,
                            255,
                            255,
                            245
                    )
            );


            g2.fillRoundRect(
                    x,
                    y,
                    overlayWidth,
                    overlayHeight,
                    20,
                    20
            );


            // Border
            g2.setColor(
                    new Color(
                            220,
                            222,
                            232
                    )
            );


            g2.drawRoundRect(
                    x,
                    y,
                    overlayWidth - 1,
                    overlayHeight - 1,
                    20,
                    20
            );


            // Title
            g2.setFont(
                    new Font(
                            "SansSerif",
                            Font.BOLD,
                            21
                    )
            );


            FontMetrics fm =
                    g2.getFontMetrics();


            g2.setColor(PURPLE);


            g2.drawString(
                    title,
                    getWidth() / 2 -
                            fm.stringWidth(title) / 2,
                    y + 43
            );


            // Subtitle
            g2.setFont(
                    new Font(
                            "SansSerif",
                            Font.PLAIN,
                            13
                    )
            );


            fm =
                    g2.getFontMetrics();


            g2.setColor(MUTED);


            g2.drawString(
                    subtitle,
                    getWidth() / 2 -
                            fm.stringWidth(subtitle) / 2,
                    y + 72
            );
        }
    }


    // =========================================================
    // MAIN
    // =========================================================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            TicTacToeGame game =
                    new TicTacToeGame(
                            "Guest",
                            "Medium"
                    );

            game.setVisible(true);
        });
    }
}