import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class LoginFrame extends JFrame {

    private JTextField nameField;
    private JButton startButton;
    private AnimatedPanel background;

    // ================= COLORS =================

    private static final Color BG = new Color(247, 249, 253);
    private static final Color CARD = new Color(255, 255, 255);
    private static final Color DARK = new Color(30, 41, 59);
    private static final Color MUTED = new Color(100, 116, 139);
    private static final Color PRIMARY = new Color(99, 102, 241);
    private static final Color PRIMARY_HOVER = new Color(79, 70, 229);
    private static final Color BORDER = new Color(220, 226, 238);

    public LoginFrame() {

        setTitle("GamesMania - Enter Name");
        setSize(900, 650);
        setMinimumSize(new Dimension(750, 550));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);

        background = new AnimatedPanel();
        background.setLayout(new BorderLayout());

        // =========================================================
        // HEADER
        // =========================================================

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(
                new EmptyBorder(18, 35, 18, 35)
        );

        JLabel logo = new JLabel("🎮  GAMESMANIA");
        logo.setFont(
                new Font("Arial", Font.BOLD, 27)
        );
        logo.setForeground(DARK);

        JLabel slogan = new JLabel(
                "PLAY  •  COMPETE  •  CONQUER"
        );
        slogan.setFont(
                new Font("Arial", Font.BOLD, 15)
        );
        slogan.setForeground(MUTED);

        header.add(logo, BorderLayout.WEST);
        header.add(slogan, BorderLayout.EAST);

        JPanel headerBackground =
                new JPanel(new BorderLayout());

        headerBackground.setBackground(
                new Color(255, 255, 255, 225)
        );

        headerBackground.setBorder(
                BorderFactory.createMatteBorder(
                        0, 0, 1, 0,
                        new Color(226, 232, 240)
                )
        );

        headerBackground.add(
                header,
                BorderLayout.CENTER
        );

        background.add(
                headerBackground,
                BorderLayout.NORTH
        );

        // =========================================================
        // CENTER
        // =========================================================

        JPanel center = new JPanel();
        center.setOpaque(false);

        center.setLayout(
                new BoxLayout(
                        center,
                        BoxLayout.Y_AXIS
                )
        );

        center.setBorder(
                new EmptyBorder(
                        25,
                        40,
                        25,
                        40
                )
        );

        center.add(Box.createVerticalGlue());

        // ================= GAME ICON =================

        JLabel gameIcon =
                new JLabel("🎮");

        gameIcon.setFont(
                new Font(
                        "Segoe UI Emoji",
                        Font.PLAIN,
                        58
                )
        );

        gameIcon.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        center.add(gameIcon);

        center.add(
                Box.createVerticalStrut(5)
        );

        // ================= TITLE =================

        JLabel title =
                new JLabel("GAMESMANIA");

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        58
                )
        );

        title.setForeground(DARK);

        title.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        center.add(title);

        center.add(
                Box.createVerticalStrut(8)
        );

        // ================= SUBTITLE =================

        JLabel subtitle =
                new JLabel(
                        "ENTER YOUR NAME TO PLAY"
                );

        subtitle.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        23
                )
        );

        subtitle.setForeground(
                new Color(71, 85, 105)
        );

        subtitle.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        center.add(subtitle);

        center.add(
                Box.createVerticalStrut(32)
        );

        // =========================================================
        // FORM CARD
        // =========================================================

        JPanel formCard =
                new JPanel();

        formCard.setLayout(
                new BoxLayout(
                        formCard,
                        BoxLayout.Y_AXIS
                )
        );

        formCard.setBackground(CARD);

        formCard.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER,
                                1
                        ),
                        new EmptyBorder(
                                25,
                                35,
                                25,
                                35
                        )
                )
        );

        formCard.setMaximumSize(
                new Dimension(620, 210)
        );

        formCard.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        // ================= LABEL =================

        JLabel nameLabel =
                new JLabel("PLAYER NAME");

        nameLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        15
                )
        );

        nameLabel.setForeground(DARK);

        nameLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        formCard.add(nameLabel);

        formCard.add(
                Box.createVerticalStrut(10)
        );

        // ================= INPUT =================

        nameField =
                new JTextField();

        nameField.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        19
                )
        );

        nameField.setForeground(DARK);

        nameField.setBackground(
                new Color(248, 250, 252)
        );

        nameField.setCaretColor(PRIMARY);

        nameField.setHorizontalAlignment(
                JTextField.CENTER
        );

        nameField.setPreferredSize(
                new Dimension(500, 50)
        );

        nameField.setMaximumSize(
                new Dimension(500, 50)
        );

        nameField.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER,
                                2
                        ),
                        BorderFactory.createEmptyBorder(
                                5,
                                15,
                                5,
                                15
                        )
                )
        );

        formCard.add(nameField);

        formCard.add(
                Box.createVerticalStrut(20)
        );

        // ================= BUTTON =================

        startButton =
                new JButton(
                        "START PLAYING   →"
                );

        startButton.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        19
                )
        );

        startButton.setForeground(Color.WHITE);

        startButton.setBackground(PRIMARY);

        startButton.setFocusPainted(false);
        startButton.setBorderPainted(false);

        startButton.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        startButton.setPreferredSize(
                new Dimension(500, 52)
        );

        startButton.setMaximumSize(
                new Dimension(500, 52)
        );

        startButton.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        formCard.add(startButton);

        center.add(formCard);

        center.add(
                Box.createVerticalStrut(20)
        );

        // ================= MESSAGE =================

        JLabel message =
                new JLabel(
                        "Enter your name and begin your gaming journey!"
                );

        message.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        15
                )
        );

        message.setForeground(MUTED);

        message.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        center.add(message);

        center.add(
                Box.createVerticalGlue()
        );

        background.add(
                center,
                BorderLayout.CENTER
        );

        // =========================================================
        // FOOTER
        // =========================================================

        JPanel footer =
                new JPanel(
                        new BorderLayout()
                );

        footer.setBackground(
                new Color(255, 255, 255, 225)
        );

        footer.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(
                                1,
                                0,
                                0,
                                0,
                                new Color(226, 232, 240)
                        ),
                        new EmptyBorder(
                                10,
                                35,
                                10,
                                35
                        )
                )
        );

        JLabel footerLeft =
                new JLabel("GamesMania");

        footerLeft.setForeground(MUTED);

        footerLeft.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        JLabel footerRight =
                new JLabel(
                        "Mini Games Collection"
                );

        footerRight.setForeground(MUTED);

        footerRight.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        footer.add(
                footerLeft,
                BorderLayout.WEST
        );

        footer.add(
                footerRight,
                BorderLayout.EAST
        );

        background.add(
                footer,
                BorderLayout.SOUTH
        );

        // =========================================================
        // INPUT FOCUS EFFECT
        // =========================================================

        nameField.addFocusListener(
                new FocusAdapter() {

                    @Override
                    public void focusGained(
                            FocusEvent e
                    ) {

                        nameField.setBackground(
                                Color.WHITE
                        );

                        nameField.setBorder(
                                BorderFactory.createCompoundBorder(
                                        BorderFactory.createLineBorder(
                                                PRIMARY,
                                                2
                                        ),
                                        BorderFactory.createEmptyBorder(
                                                5,
                                                15,
                                                5,
                                                15
                                        )
                                )
                        );
                    }

                    @Override
                    public void focusLost(
                            FocusEvent e
                    ) {

                        nameField.setBackground(
                                new Color(
                                        248,
                                        250,
                                        252
                                )
                        );

                        nameField.setBorder(
                                BorderFactory.createCompoundBorder(
                                        BorderFactory.createLineBorder(
                                                BORDER,
                                                2
                                        ),
                                        BorderFactory.createEmptyBorder(
                                                5,
                                                15,
                                                5,
                                                15
                                        )
                                )
                        );
                    }
                }
        );

        // =========================================================
        // BUTTON HOVER
        // =========================================================

        startButton.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseEntered(
                            MouseEvent e
                    ) {

                        startButton.setBackground(
                                PRIMARY_HOVER
                        );

                        startButton.setFont(
                                new Font(
                                        "Arial",
                                        Font.BOLD,
                                        20
                                )
                        );
                    }

                    @Override
                    public void mouseExited(
                            MouseEvent e
                    ) {

                        startButton.setBackground(
                                PRIMARY
                        );

                        startButton.setFont(
                                new Font(
                                        "Arial",
                                        Font.BOLD,
                                        19
                                )
                        );
                    }
                }
        );

        // =========================================================
        // START ACTION
        // =========================================================

        startButton.addActionListener(
                e -> startGame()
        );

        // ENTER KEY
        nameField.addActionListener(
                e -> startGame()
        );

        // Focus input
        SwingUtilities.invokeLater(
                () -> nameField.requestFocusInWindow()
        );

        setContentPane(background);

        setVisible(true);
    }

    // =========================================================
    // START GAME
    // =========================================================

    private void startGame() {

        String name =
                nameField.getText().trim();

        if (name.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter your name!",
                    "Name Required",
                    JOptionPane.WARNING_MESSAGE
            );

            nameField.requestFocus();

            return;
        }

        JOptionPane.showMessageDialog(
                this,
                "Welcome, " + name + "!",
                "GamesMania",
                JOptionPane.INFORMATION_MESSAGE
        );

        dispose();

        try {
            UserDAO userDAO = new UserDAO();
            User user = userDAO.loginOrCreate(name);
            new HomeFrame(user != null ? user : name);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                    null,
                    "Could not open HomeFrame.\n\n"
                            + ex.getMessage(),
                    "Game Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================================
    // ANIMATED BACKGROUND
    // =========================================================

    class AnimatedPanel extends JPanel {

        private final Random random =
                new Random();

        private final Particle[] particles =
                new Particle[35];

        private float animation = 0;

        AnimatedPanel() {

            setBackground(BG);

            for (int i = 0;
                 i < particles.length;
                 i++) {

                particles[i] =
                        new Particle(
                                random.nextInt(1000),
                                random.nextInt(700),
                                3 + random.nextInt(8),
                                0.2f
                                        + random.nextFloat()
                                        * 0.7f
                        );
            }

            Timer timer =
                    new Timer(
                            30,
                            e -> {

                                animation += 0.02f;

                                for (
                                        Particle p
                                        : particles
                                ) {

                                    p.y -= p.speed;

                                    p.x +=
                                            Math.sin(
                                                    animation
                                                            + p.offset
                                            )
                                                    * 0.25;

                                    if (
                                            p.y < -20
                                    ) {

                                        p.y =
                                                getHeight()
                                                        + 20;

                                        p.x =
                                                random.nextInt(
                                                        Math.max(
                                                                1,
                                                                getWidth()
                                                        )
                                                );
                                    }
                                }

                                repaint();
                            }
                    );

            timer.start();
        }

        @Override
        protected void paintComponent(
                Graphics g
        ) {

            super.paintComponent(g);

            Graphics2D g2 =
                    (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            int width = getWidth();
            int height = getHeight();

            // =====================================================
            // SOFT LIGHT BACKGROUND
            // =====================================================

            GradientPaint gradient =
                    new GradientPaint(
                            0,
                            0,
                            new Color(
                                    248,
                                    250,
                                    255
                            ),
                            width,
                            height,
                            new Color(
                                    240,
                                    245,
                                    255
                            )
                    );

            g2.setPaint(gradient);

            g2.fillRect(
                    0,
                    0,
                    width,
                    height
            );

            // =====================================================
            // LARGE PASTEL GLOWS
            // =====================================================

            drawGlow(
                    g2,
                    100,
                    150,
                    260,
                    new Color(
                            129,
                            140,
                            248
                    )
            );

            drawGlow(
                    g2,
                    width - 100,
                    height - 140,
                    300,
                    new Color(
                            167,
                            243,
                            208
                    )
            );

            drawGlow(
                    g2,
                    width / 2,
                    100,
                    220,
                    new Color(
                            196,
                            181,
                            253
                    )
            );

            // =====================================================
            // FLOATING PARTICLES
            // =====================================================

            for (
                    Particle p
                    : particles
            ) {

                int alpha = 70;

                g2.setColor(
                        new Color(
                                99,
                                102,
                                241,
                                alpha
                        )
                );

                g2.fillOval(
                        (int) p.x,
                        (int) p.y,
                        p.size,
                        p.size
                );
            }

            // =====================================================
            // DECORATIVE GAMING OBJECTS
            // =====================================================

            drawGamingObjects(
                    g2,
                    width,
                    height
            );

            g2.dispose();
        }

        // =========================================================
        // GLOW
        // =========================================================

        private void drawGlow(
                Graphics2D g2,
                int x,
                int y,
                int size,
                Color color
        ) {

            int alpha = 30;

            g2.setColor(
                    new Color(
                            color.getRed(),
                            color.getGreen(),
                            color.getBlue(),
                            alpha
                    )
            );

            g2.fillOval(
                    x - size / 2,
                    y - size / 2,
                    size,
                    size
            );
        }

        // =========================================================
        // GAMING OBJECTS
        // =========================================================

        private void drawGamingObjects(
                Graphics2D g2,
                int width,
                int height
        ) {

            int alpha = 75;

            g2.setStroke(
                    new BasicStroke(
                            2.0f
                    )
            );

            g2.setColor(
                    new Color(
                            99,
                            102,
                            241,
                            alpha
                    )
            );

            // =====================================================
            // CONTROLLER
            // =====================================================

            int cx = 65;
            int cy = height - 135;

            g2.drawRoundRect(
                    cx,
                    cy,
                    100,
                    55,
                    28,
                    28
            );

            // D-pad
            g2.fillRect(
                    cx + 20,
                    cy + 23,
                    25,
                    7
            );

            g2.fillRect(
                    cx + 29,
                    cy + 14,
                    7,
                    25
            );

            // Buttons
            g2.fillOval(
                    cx + 68,
                    cy + 15,
                    9,
                    9
            );

            g2.fillOval(
                    cx + 82,
                    cy + 27,
                    9,
                    9
            );

            // =====================================================
            // DICE
            // =====================================================

            int dx = width - 125;
            int dy = 135;

            g2.drawRoundRect(
                    dx,
                    dy,
                    58,
                    58,
                    12,
                    12
            );

            g2.fillOval(
                    dx + 14,
                    dy + 14,
                    8,
                    8
            );

            g2.fillOval(
                    dx + 36,
                    dy + 36,
                    8,
                    8
            );

            g2.fillOval(
                    dx + 36,
                    dy + 14,
                    8,
                    8
            );

            // =====================================================
            // SMALL CIRCLES
            // =====================================================

            g2.drawOval(
                    145,
                    180,
                    30,
                    30
            );

            g2.drawOval(
                    width - 245,
                    height - 220,
                    35,
                    35
            );

            // =====================================================
            // STAR
            // =====================================================

            drawStar(
                    g2,
                    width - 80,
                    height - 100,
                    25
            );
        }

        // =========================================================
        // STAR
        // =========================================================

        private void drawStar(
                Graphics2D g2,
                int x,
                int y,
                int radius
        ) {

            Polygon star =
                    new Polygon();

            for (
                    int i = 0;
                    i < 10;
                    i++
            ) {

                double angle =
                        Math.PI / 2
                                + i * Math.PI / 5;

                int r =
                        (i % 2 == 0)
                                ? radius
                                : radius / 2;

                int px =
                        x
                                + (int)
                                (
                                        Math.cos(angle)
                                                * r
                                );

                int py =
                        y
                                - (int)
                                (
                                        Math.sin(angle)
                                                * r
                                );

                star.addPoint(
                        px,
                        py
                );
            }

            g2.drawPolygon(star);
        }
    }

    // =========================================================
    // PARTICLE
    // =========================================================

    class Particle {

        float x;
        float y;
        int size;
        float speed;
        float offset;

        Particle(
                float x,
                float y,
                int size,
                float speed
        ) {

            this.x = x;
            this.y = y;
            this.size = size;
            this.speed = speed;

            this.offset =
                    (float)
                            (
                                    Math.random()
                                            * Math.PI
                                            * 2
                            );
        }
    }

    // =========================================================
    // MAIN
    // =========================================================

    public static void main(
            String[] args
    ) {

        SwingUtilities.invokeLater(
                () -> new LoginFrame()
        );
    }
}