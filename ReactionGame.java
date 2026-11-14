import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class ReactionGame extends JFrame {

    private JPanel mainPanel;

    private JLabel reactionLabel;
    private JLabel scoreLabel;
    private JLabel bestLabel;
    private JLabel messageLabel;

    private CircleButton tapButton;
    private JButton startButton;

    private JPanel[] lights;

    private Timer lightTimer;
    private Timer greenTimer;

    private Random random = new Random();

    private long startTime;

    private int score = 0;
    private int bestReaction = 0;
    private int lightNumber = 0;

    private boolean gameRunning = false;
    private boolean greenSignal = false;


    private String playerName = "Player";

    public ReactionGame() {
        this("Player");
    }

    public ReactionGame(String playerName) {
        if (playerName != null && !playerName.trim().isEmpty()) {
            this.playerName = playerName.trim();
        }

        setTitle("GamesMania • Reaction Game • " + this.playerName);
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        createUI();
        setVisible(true);
    }


    // =========================================================
    // CREATE UI
    // =========================================================

    private void createUI() {

        mainPanel = new JPanel(null);

        mainPanel.setBackground(
                new Color(245, 247, 251)
        );

        setContentPane(mainPanel);


        // =====================================================
        // HEADER
        // =====================================================

        JPanel header = new JPanel(null);

        header.setBackground(Color.WHITE);

        header.setBounds(
                0, 0, 1000, 90
        );

        mainPanel.add(header);


        JLabel icon = new JLabel("⚡");

        icon.setFont(
                new Font("SansSerif", Font.BOLD, 28)
        );

        icon.setForeground(
                new Color(65, 85, 220)
        );

        icon.setBounds(
                30, 20, 40, 40
        );

        header.add(icon);


        JLabel title = new JLabel(
                "REACTION GAME"
        );

        title.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        30
                )
        );

        title.setForeground(
                new Color(25, 45, 75)
        );

        title.setBounds(
                70, 17, 350, 40
        );

        header.add(title);


        JLabel subtitle = new JLabel(
                "Test your reflexes like an F1 driver"
        );

        subtitle.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        15
                )
        );

        subtitle.setForeground(
                new Color(100, 115, 135)
        );

        subtitle.setBounds(
                72, 52, 350, 25
        );

        header.add(subtitle);


        JLabel player = new JLabel(
                "PLAYER"
        );

        player.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        13
                )
        );

        player.setForeground(
                new Color(80, 90, 110)
        );

        player.setBounds(
                850, 20, 100, 25
        );

        header.add(player);


        // =====================================================
        // LEFT GAME AREA
        // =====================================================

        JPanel gameCard = new JPanel(null);

        gameCard.setBackground(Color.WHITE);

        gameCard.setBounds(
                35, 115, 650, 475
        );

        mainPanel.add(gameCard);


        JLabel readyText = new JLabel(
                "GET READY",
                SwingConstants.CENTER
        );

        readyText.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        16
                )
        );

        readyText.setForeground(
                new Color(90, 105, 125)
        );

        readyText.setBounds(
                100, 20, 450, 30
        );

        gameCard.add(readyText);


        // =====================================================
        // F1 LIGHTS
        // =====================================================

        JPanel lightBox = new JPanel(
                new FlowLayout(
                        FlowLayout.CENTER,
                        13,
                        10
                )
        );

        lightBox.setBackground(
                new Color(30, 34, 42)
        );

        lightBox.setBounds(
                170, 55, 310, 65
        );

        gameCard.add(lightBox);


        lights = new JPanel[5];


        for (int i = 0; i < 5; i++) {

            lights[i] = new JPanel();

            lights[i].setPreferredSize(
                    new Dimension(40, 40)
            );

            lights[i].setBackground(
                    new Color(90, 25, 30)
            );

            lightBox.add(lights[i]);
        }


        // =====================================================
        // MESSAGE
        // =====================================================

        messageLabel = new JLabel(
                "Press START and wait for GREEN",
                SwingConstants.CENTER
        );

        messageLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        17
                )
        );

        messageLabel.setForeground(
                new Color(75, 90, 115)
        );

        messageLabel.setBounds(
                80, 130, 490, 35
        );

        gameCard.add(messageLabel);


        // =====================================================
        // CIRCLE TAP BUTTON
        // =====================================================

        tapButton = new CircleButton();

        tapButton.setBounds(
                245, 175, 160, 160
        );

        gameCard.add(tapButton);


        tapButton.addActionListener(
                e -> buttonClicked()
        );


        // =====================================================
        // REACTION
        // =====================================================

        reactionLabel = new JLabel(
                "Reaction: -- ms",
                SwingConstants.CENTER
        );

        reactionLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        22
                )
        );

        reactionLabel.setForeground(
                new Color(35, 55, 85)
        );

        reactionLabel.setBounds(
                70, 350, 510, 35
        );

        gameCard.add(reactionLabel);


        // =====================================================
        // STATS
        // =====================================================

        scoreLabel = new JLabel(
                "SCORE  0",
                SwingConstants.CENTER
        );

        scoreLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        17
                )
        );

        scoreLabel.setForeground(
                new Color(75, 85, 220)
        );

        scoreLabel.setBounds(
                100, 390, 200, 30
        );

        gameCard.add(scoreLabel);


        bestLabel = new JLabel(
                "BEST  -- ms",
                SwingConstants.CENTER
        );

        bestLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        17
                )
        );

        bestLabel.setForeground(
                new Color(30, 170, 100)
        );

        bestLabel.setBounds(
                350, 390, 200, 30
        );

        gameCard.add(bestLabel);


        // =====================================================
        // START BUTTON
        // =====================================================

        startButton = new JButton(
                "START TEST"
        );

        startButton.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        17
                )
        );

        startButton.setForeground(Color.WHITE);

        startButton.setBackground(
                new Color(70, 90, 225)
        );

        startButton.setFocusPainted(false);

        startButton.setBorderPainted(false);

        startButton.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        startButton.setBounds(
                210, 425, 230, 40
        );

        gameCard.add(startButton);


        startButton.addActionListener(
                e -> startGame()
        );


        // =====================================================
        // RIGHT RULES PANEL
        // =====================================================

        JPanel rulesCard = new JPanel(null);

        rulesCard.setBackground(Color.WHITE);

        rulesCard.setBounds(
                710, 115, 255, 475
        );

        mainPanel.add(rulesCard);


        JLabel rulesTitle = new JLabel(
                "HOW TO PLAY"
        );

        rulesTitle.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        21
                )
        );

        rulesTitle.setForeground(
                new Color(25, 45, 75)
        );

        rulesTitle.setBounds(
                25, 25, 205, 35
        );

        rulesCard.add(rulesTitle);


        JLabel line = new JLabel(
                "━━━━━━━━━━━━"
        );

        line.setForeground(
                new Color(220, 225, 235)
        );

        line.setBounds(
                25, 52, 205, 20
        );

        rulesCard.add(line);


        addRule(
                rulesCard,
                "1",
                "Click START TEST",
                90
        );

        addRule(
                rulesCard,
                "2",
                "Wait for all red lights",
                145
        );

        addRule(
                rulesCard,
                "3",
                "Lights will turn GREEN",
                200
        );

        addRule(
                rulesCard,
                "4",
                "Tap the circle immediately",
                255
        );

        addRule(
                rulesCard,
                "5",
                "Don't click before GREEN",
                310
        );


        JLabel scoring = new JLabel(
                "<html><b>SCORING</b><br>" +
                        "<br>" +
                        "Under 200 ms  = +30<br>" +
                        "Under 300 ms  = +20<br>" +
                        "Under 450 ms  = +15<br>" +
                        "Under 700 ms  = +10<br>" +
                        "Over 700 ms   = +5<br>" +
                        "<br>" +
                        "False start = -10</html>"
        );

        scoring.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        13
                )
        );

        scoring.setForeground(
                new Color(85, 100, 120)
        );

        scoring.setBounds(
                25, 365, 215, 105
        );

        rulesCard.add(scoring);
    }


    // =========================================================
    // ADD RULE
    // =========================================================

    private void addRule(
            JPanel parent,
            String number,
            String text,
            int y
    ) {

        JLabel num = new JLabel(
                number,
                SwingConstants.CENTER
        );

        num.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        13
                )
        );

        num.setForeground(Color.WHITE);

        num.setOpaque(true);

        num.setBackground(
                new Color(70, 90, 225)
        );

        num.setBounds(
                25, y, 28, 28
        );

        parent.add(num);


        JLabel rule = new JLabel(text);

        rule.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        13
                )
        );

        rule.setForeground(
                new Color(70, 85, 105)
        );

        rule.setBounds(
                65, y, 175, 28
        );

        parent.add(rule);
    }


    // =========================================================
    // START GAME
    // =========================================================

    private void startGame() {

        if (gameRunning)
            return;


        gameRunning = true;
        greenSignal = false;

        lightNumber = 0;


        reactionLabel.setText(
                "Reaction: -- ms"
        );

        messageLabel.setText(
                "Get ready..."
        );


        startButton.setEnabled(false);

        startButton.setText(
                "WAIT..."
        );


        tapButton.setText(
                "WAIT"
        );

        tapButton.setGreen(false);


        resetLights();


        // F1 lights

        lightTimer = new Timer(
                500,
                e -> activateNextLight()
        );

        lightTimer.start();
    }


    // =========================================================
    // ACTIVATE LIGHT
    // =========================================================

    private void activateNextLight() {

        if (lightNumber < 5) {

            lights[lightNumber]
                    .setBackground(
                            new Color(
                                    235,
                                    35,
                                    45
                            )
                    );

            lightNumber++;

        } else {

            lightTimer.stop();

            messageLabel.setText(
                    "WAIT FOR GREEN..."
            );


            int delay =
                    900 +
                            random.nextInt(1800);


            greenTimer = new Timer(
                    delay,
                    e -> showGreen()
            );

            greenTimer.setRepeats(false);

            greenTimer.start();
        }
    }


    // =========================================================
    // GREEN SIGNAL
    // =========================================================

    private void showGreen() {

        greenSignal = true;


        messageLabel.setText(
                "TAP NOW!"
        );


        tapButton.setGreen(true);

        tapButton.setText(
                "TAP!"
        );


        // Change lights to green

        for (JPanel light : lights) {

            light.setBackground(
                    new Color(
                            35,
                            200,
                            95
                    )
            );
        }


        startTime =
                System.nanoTime();
    }


    // =========================================================
    // BUTTON CLICK
    // =========================================================

    private void buttonClicked() {

        if (!gameRunning)
            return;


        // False start

        if (!greenSignal) {

            falseStart();

            return;
        }


        // Correct click

        long time =
                (
                        System.nanoTime()
                                -
                                startTime
                )
                        / 1_000_000;


        finishGame(time);
    }


    // =========================================================
    // FALSE START
    // =========================================================

    private void falseStart() {

        gameRunning = false;


        if (lightTimer != null)
            lightTimer.stop();


        if (greenTimer != null)
            greenTimer.stop();


        score -= 10;


        if (score < 0)
            score = 0;


        messageLabel.setText(
                "FALSE START!"
        );


        reactionLabel.setText(
                "Too early!"
        );


        scoreLabel.setText(
                "SCORE  " + score
        );


        tapButton.setGreen(false);

        tapButton.setText(
                "TOO EARLY"
        );


        startButton.setEnabled(true);

        startButton.setText(
                "TRY AGAIN"
        );


        resetLights();
    }


    // =========================================================
    // FINISH GAME
    // =========================================================

    private void finishGame(
            long reactionTime
    ) {

        gameRunning = false;

        greenSignal = false;


        reactionLabel.setText(
                "Reaction: " +
                        reactionTime +
                        " ms"
        );


        // Best reaction

        if (bestReaction == 0 ||
                reactionTime < bestReaction) {

            bestReaction =
                    (int) reactionTime;

            bestLabel.setText(
                    "BEST  " +
                            bestReaction +
                            " ms"
            );
        }


        // Score

        if (reactionTime < 200) {

            score += 30;

            messageLabel.setText(
                    "⚡ LIGHTNING FAST!"
            );

        } else if (reactionTime < 300) {

            score += 20;

            messageLabel.setText(
                    "🔥 EXCELLENT!"
            );

        } else if (reactionTime < 450) {

            score += 15;

            messageLabel.setText(
                    "GREAT REFLEX!"
            );

        } else if (reactionTime < 700) {

            score += 10;

            messageLabel.setText(
                    "GOOD JOB!"
            );

        } else {

            score += 5;

            messageLabel.setText(
                    "KEEP PRACTICING!"
            );
        }


        scoreLabel.setText(
                "SCORE  " + score
        );

        if (score > 0) {
            try {
                ScoreDAO dao = new ScoreDAO();
                dao.saveScore(playerName, "Reaction Game", bestReaction + " ms", score);
            } catch (Exception ignored) {}
        }


        tapButton.setGreen(false);

        tapButton.setText(
                "READY"
        );


        startButton.setEnabled(true);

        startButton.setText(
                "START AGAIN"
        );


        resetLights();
    }


    // =========================================================
    // RESET
    // =========================================================

    private void resetLights() {

        for (JPanel light : lights) {

            light.setBackground(
                    new Color(
                            90,
                            25,
                            30
                    )
            );
        }
    }


    // =========================================================
    // CIRCULAR TAP BUTTON
    // =========================================================

    private class CircleButton
            extends JButton {

        private boolean isGreen = false;

        private boolean hover = false;


        public CircleButton() {

            setContentAreaFilled(false);

            setBorderPainted(false);

            setFocusPainted(false);

            setOpaque(false);

            setForeground(Color.WHITE);

            setFont(
                    new Font(
                            "SansSerif",
                            Font.BOLD,
                            22
                    )
            );

            setCursor(
                    new Cursor(
                            Cursor.HAND_CURSOR
                    )
            );


            addMouseListener(
                    new MouseAdapter() {

                        @Override
                        public void mouseEntered(
                                MouseEvent e
                        ) {

                            hover = true;

                            repaint();
                        }


                        @Override
                        public void mouseExited(
                                MouseEvent e
                        ) {

                            hover = false;

                            repaint();
                        }
                    }
            );
        }


        public void setGreen(
                boolean value
        ) {

            isGreen = value;

            repaint();
        }


        @Override
        protected void paintComponent(
                Graphics g
        ) {

            Graphics2D g2 =
                    (Graphics2D) g.create();


            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );


            int size =
                    Math.min(
                            getWidth(),
                            getHeight()
                    );


            int x =
                    (getWidth() - size) / 2;

            int y =
                    (getHeight() - size) / 2;


            // Shadow

            g2.setColor(
                    new Color(
                            0,
                            0,
                            0,
                            25
                    )
            );

            g2.fillOval(
                    x + 5,
                    y + 8,
                    size,
                    size
            );


            // Button color

            Color buttonColor;


            if (isGreen) {

                buttonColor =
                        new Color(
                                35,
                                195,
                                90
                        );

            } else {

                buttonColor =
                        new Color(
                                215,
                                45,
                                50
                        );
            }


            if (hover) {

                buttonColor =
                        buttonColor.brighter();
            }


            // Outer circle

            g2.setColor(
                    buttonColor
            );

            g2.fillOval(
                    x,
                    y,
                    size,
                    size
            );


            // Inner circle

            g2.setColor(
                    new Color(
                            255,
                            255,
                            255,
                            35
                    )
            );

            g2.fillOval(
                    x + 12,
                    y + 12,
                    size - 24,
                    size - 24
            );


            // Border

            g2.setColor(Color.WHITE);

            g2.setStroke(
                    new BasicStroke(4)
            );

            g2.drawOval(
                    x + 5,
                    y + 5,
                    size - 10,
                    size - 10
            );


            // Text

            FontMetrics fm =
                    g2.getFontMetrics();


            String text =
                    getText();


            int textX =
                    getWidth() / 2
                            -
                            fm.stringWidth(text) / 2;


            int textY =
                    getHeight() / 2
                            -
                            (
                                    fm.getAscent()
                                            +
                                            fm.getDescent()
                            ) / 2;


            g2.setColor(Color.WHITE);

            g2.drawString(
                    text,
                    textX,
                    textY
            );


            g2.dispose();
        }
    }


    // =========================================================
    // MAIN
    // =========================================================

    public static void main(
            String[] args
    ) {

        SwingUtilities.invokeLater(
                () -> new ReactionGame()
        );
    }
}