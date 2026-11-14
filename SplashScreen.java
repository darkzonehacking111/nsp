import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Random;

/**
 * ============================================================
 *                       GAMESMANIA
 *                      SPLASH SCREEN
 * ============================================================
 *
 * Animated startup screen.
 *
 * Flow:
 *
 * SplashScreen
 *      ↓
 * LoginFrame
 *      ↓
 * HomeFrame
 *      ↓
 * GamesMania
 *
 * Features:
 *
 *  ✓ Animated background
 *  ✓ Floating particles
 *  ✓ Animated logo
 *  ✓ Loading progress
 *  ✓ Loading messages
 *  ✓ Smooth fade-like animation
 *  ✓ Automatic LoginFrame launch
 *
 * ============================================================
 */
public class SplashScreen extends JFrame {

    // =========================================================
    // COLORS
    // =========================================================

    private static final Color BACKGROUND =
            new Color(246, 248, 252);

    private static final Color PURPLE =
            new Color(92, 83, 220);

    private static final Color PURPLE_LIGHT =
            new Color(145, 137, 245);

    private static final Color MINT =
            new Color(47, 190, 167);

    private static final Color BLUE =
            new Color(75, 130, 220);

    private static final Color TEXT =
            new Color(35, 38, 52);

    private static final Color MUTED =
            new Color(110, 115, 130);


    // =========================================================
    // SPLASH SETTINGS
    // =========================================================

    private static final int WIDTH =
            900;

    private static final int HEIGHT =
            600;

    private static final int TOTAL_LOADING =
            100;


    // =========================================================
    // UI
    // =========================================================

    private SplashPanel splashPanel;

    private JLabel loadingLabel;

    private JLabel percentLabel;

    private JProgressBar progressBar;


    // =========================================================
    // ANIMATION
    // =========================================================

    private Timer animationTimer;

    private Timer loadingTimer;


    private int progress =
            0;

    private int animationStep =
            0;


    // =========================================================
    // RANDOM
    // =========================================================

    private final Random random =
            new Random();


    // =========================================================
    // PARTICLES
    // =========================================================

    private final ArrayList<Particle> particles =
            new ArrayList<>();


    // =========================================================
    // LOADING MESSAGES
    // =========================================================

    private final String[] loadingMessages = {

            "Preparing your arena...",

            "Loading mini games...",

            "Connecting leaderboard...",

            "Preparing player profile...",

            "Loading game engines...",

            "Setting up challenges...",

            "Almost ready...",

            "Welcome to GamesMania!"

    };


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public SplashScreen() {

        setTitle(
                "GamesMania"
        );


        setSize(
                WIDTH,
                HEIGHT
        );


        setLocationRelativeTo(
                null
        );


        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );


        setUndecorated(
                true
        );


        setResizable(
                false
        );


        createParticles();


        buildUI();


        startAnimations();


        startLoading();
    }


    // =========================================================
    // BUILD UI
    // =========================================================

    private void buildUI() {

        splashPanel =
                new SplashPanel();


        splashPanel.setLayout(
                null
        );


        setContentPane(
                splashPanel
        );


        // -----------------------------------------------------
        // BRAND
        // -----------------------------------------------------

        JLabel brand =
                new JLabel(
                        "GAMESMANIA"
                );


        brand.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        15
                )
        );


        brand.setForeground(
                PURPLE
        );


        brand.setHorizontalAlignment(
                SwingConstants.CENTER
        );


        brand.setBounds(
                0,
                32,
                WIDTH,
                30
        );


        splashPanel.add(
                brand
        );


        // -----------------------------------------------------
        // LOGO
        // -----------------------------------------------------

        JLabel logo =
                new JLabel(
                        "🎮"
                );


        logo.setFont(
                new Font(
                        "Segoe UI Emoji",
                        Font.PLAIN,
                        76
                )
        );


        logo.setHorizontalAlignment(
                SwingConstants.CENTER
        );


        logo.setBounds(
                WIDTH / 2 - 80,
                125,
                160,
                100
        );


        splashPanel.add(
                logo
        );


        // -----------------------------------------------------
        // TITLE
        // -----------------------------------------------------

        JLabel title =
                new JLabel(
                        "GAMESMANIA"
                );


        title.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        46
                )
        );


        title.setForeground(
                TEXT
        );


        title.setHorizontalAlignment(
                SwingConstants.CENTER
        );


        title.setBounds(
                0,
                220,
                WIDTH,
                60
        );


        splashPanel.add(
                title
        );


        // -----------------------------------------------------
        // SUBTITLE
        // -----------------------------------------------------

        JLabel subtitle =
                new JLabel(
                        "PLAY • COMPETE • CONQUER"
                );


        subtitle.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );


        subtitle.setForeground(
                PURPLE
        );


        subtitle.setHorizontalAlignment(
                SwingConstants.CENTER
        );


        subtitle.setBounds(
                0,
                282,
                WIDTH,
                30
        );


        splashPanel.add(
                subtitle
        );


        // -----------------------------------------------------
        // DESCRIPTION
        // -----------------------------------------------------

        JLabel description =
                new JLabel(
                        "Your ultimate collection of mini games"
                );


        description.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        12
                )
        );


        description.setForeground(
                MUTED
        );


        description.setHorizontalAlignment(
                SwingConstants.CENTER
        );


        description.setBounds(
                0,
                312,
                WIDTH,
                25
        );


        splashPanel.add(
                description
        );


        // -----------------------------------------------------
        // PROGRESS BAR
        // -----------------------------------------------------

        progressBar =
                new JProgressBar(
                        0,
                        TOTAL_LOADING
                );


        progressBar.setValue(
                0
        );


        progressBar.setStringPainted(
                false
        );


        progressBar.setBorderPainted(
                false
        );


        progressBar.setBackground(
                new Color(
                        230,
                        232,
                        240
                )
        );


        progressBar.setForeground(
                PURPLE
        );


        progressBar.setBounds(
                250,
                405,
                400,
                8
        );


        splashPanel.add(
                progressBar
        );


        // -----------------------------------------------------
        // LOADING TEXT
        // -----------------------------------------------------

        loadingLabel =
                new JLabel(
                        "Preparing your arena..."
                );


        loadingLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        11
                )
        );


        loadingLabel.setForeground(
                MUTED
        );


        loadingLabel.setHorizontalAlignment(
                SwingConstants.CENTER
        );


        loadingLabel.setBounds(
                200,
                430,
                500,
                25
        );


        splashPanel.add(
                loadingLabel
        );


        // -----------------------------------------------------
        // PERCENT
        // -----------------------------------------------------

        percentLabel =
                new JLabel(
                        "0%"
                );


        percentLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        11
                )
        );


        percentLabel.setForeground(
                PURPLE
        );


        percentLabel.setHorizontalAlignment(
                SwingConstants.CENTER
        );


        percentLabel.setBounds(
                350,
                455,
                200,
                25
        );


        splashPanel.add(
                percentLabel
        );


        // -----------------------------------------------------
        // FOOTER
        // -----------------------------------------------------

        JLabel footer =
                new JLabel(
                        "NSP Project • GamesMania"
                );


        footer.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        9
                )
        );


        footer.setForeground(
                new Color(
                        145,
                        148,
                        160
                )
        );


        footer.setHorizontalAlignment(
                SwingConstants.CENTER
        );


        footer.setBounds(
                0,
                HEIGHT - 38,
                WIDTH,
                20
        );


        splashPanel.add(
                footer
        );
    }


    // =========================================================
    // PARTICLES
    // =========================================================

    private void createParticles() {

        for (
                int i = 0;
                i < 55;
                i++
        ) {

            particles.add(
                    new Particle(
                            random.nextInt(
                                    WIDTH
                            ),
                            random.nextInt(
                                    HEIGHT
                            )
                    )
            );
        }
    }


    // =========================================================
    // START ANIMATIONS
    // =========================================================

    private void startAnimations() {

        animationTimer =
                new Timer(
                        30,
                        e -> {

                            animationStep++;


                            for (
                                    Particle p :
                                    particles
                            ) {

                                p.update();
                            }


                            splashPanel.repaint();
                        }
                );


        animationTimer.start();
    }


    // =========================================================
    // START LOADING
    // =========================================================

    private void startLoading() {

        loadingTimer =
                new Timer(
                        55,
                        e -> {

                            progress++;


                            progressBar.setValue(
                                    progress
                            );


                            percentLabel.setText(
                                    progress +
                                            "%"
                            );


                            updateLoadingMessage();


                            if (
                                    progress >=
                                            TOTAL_LOADING
                            ) {

                                loadingTimer.stop();


                                if (
                                        animationTimer != null
                                ) {

                                    animationTimer.stop();
                                }


                                finishSplash();
                            }
                        }
                );


        loadingTimer.start();
    }


    // =========================================================
    // LOADING MESSAGE
    // =========================================================

    private void updateLoadingMessage() {

        int index =
                progress /
                        (
                                TOTAL_LOADING /
                                        loadingMessages.length
                        );


        index =
                Math.min(
                        loadingMessages.length - 1,
                        index
                );


        loadingLabel.setText(
                loadingMessages[index]
        );
    }


    // =========================================================
    // FINISH SPLASH
    // =========================================================

    private void finishSplash() {

        loadingLabel.setText(
                "Welcome to GamesMania!"
        );


        percentLabel.setText(
                "100%"
        );


        /*
         * Small delay makes the final
         * 100% screen visible.
         */

        Timer finishTimer =
                new Timer(
                        650,
                        e -> {

                            openLogin();
                        }
                );


        finishTimer.setRepeats(
                false
        );


        finishTimer.start();
    }


    // =========================================================
    // OPEN LOGIN
    // =========================================================

    private void openLogin() {

        dispose();


        SwingUtilities.invokeLater(
                () -> {

                    try {

                        LoginFrame login =
                                new LoginFrame();


                        login.setVisible(
                                true
                        );


                    } catch (
                            Exception e
                    ) {

                        /*
                         * If LoginFrame has a different
                         * constructor in your project,
                         * change only this section.
                         */

                        System.out.println(
                                "Unable to open LoginFrame:"
                        );


                        e.printStackTrace();
                    }
                }
        );
    }


    // =========================================================
    // PARTICLE CLASS
    // =========================================================

    private class Particle {

        double x;

        double y;

        double speed;

        double size;

        double opacity;

        double phase;


        Particle(
                double x,
                double y) {

            this.x =
                    x;

            this.y =
                    y;


            speed =
                    0.15 +
                            random.nextDouble() *
                                    0.5;


            size =
                    2 +
                            random.nextDouble() *
                                    4;


            opacity =
                    0.12 +
                            random.nextDouble() *
                                    0.25;


            phase =
                    random.nextDouble() *
                            Math.PI *
                            2;
        }


        void update() {

            y -=
                    speed;


            x +=
                    Math.sin(
                            animationStep *
                                    0.015 +
                                    phase
                    ) *
                            0.15;


            if (
                    y < -10
            ) {

                y =
                        HEIGHT +
                                10;


                x =
                        random.nextInt(
                                WIDTH
                        );
            }
        }


        void draw(
                Graphics2D g2) {

            int alpha =
                    (int)
                            (
                                    opacity *
                                            255
                            );


            alpha =
                    Math.max(
                            10,
                            Math.min(
                                    90,
                                    alpha
                            )
                    );


            Color particleColor;


            if (
                    ((int) x +
                            (int) y) %
                            3 == 0
            ) {

                particleColor =
                        new Color(
                                PURPLE.getRed(),
                                PURPLE.getGreen(),
                                PURPLE.getBlue(),
                                alpha
                        );

            } else if (
                    ((int) x +
                            (int) y) %
                            3 == 1
            ) {

                particleColor =
                        new Color(
                                MINT.getRed(),
                                MINT.getGreen(),
                                MINT.getBlue(),
                                alpha
                        );

            } else {

                particleColor =
                        new Color(
                                BLUE.getRed(),
                                BLUE.getGreen(),
                                BLUE.getBlue(),
                                alpha
                        );
            }


            g2.setColor(
                    particleColor
            );


            g2.fill(
                    new Ellipse2D.Double(
                            x,
                            y,
                            size,
                            size
                    )
            );
        }
    }


    // =========================================================
    // SPLASH PANEL
    // =========================================================

    private class SplashPanel
            extends JPanel {

        @Override
        protected void paintComponent(
                Graphics g) {

            super.paintComponent(
                    g
            );


            Graphics2D g2 =
                    (Graphics2D)
                            g.create();


            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );


            g2.setRenderingHint(
                    RenderingHints.KEY_RENDERING,
                    RenderingHints.VALUE_RENDER_QUALITY
            );


            int width =
                    getWidth();


            int height =
                    getHeight();


            // -------------------------------------------------
            // BACKGROUND
            // -------------------------------------------------

            GradientPaint gradient =
                    new GradientPaint(
                            0,
                            0,
                            new Color(
                                    250,
                                    251,
                                    255
                            ),
                            width,
                            height,
                            new Color(
                                    241,
                                    244,
                                    252
                            )
                    );


            g2.setPaint(
                    gradient
            );


            g2.fillRect(
                    0,
                    0,
                    width,
                    height
            );


            // -------------------------------------------------
            // SOFT CIRCLES
            // -------------------------------------------------

            drawGlow(
                    g2,
                    110,
                    100,
                    180,
                    new Color(
                            92,
                            83,
                            220,
                            20
                    )
            );


            drawGlow(
                    g2,
                    width - 150,
                    height - 140,
                    220,
                    new Color(
                            47,
                            190,
                            167,
                            18
                    )
            );


            drawGlow(
                    g2,
                    width - 90,
                    100,
                    140,
                    new Color(
                            75,
                            130,
                            220,
                            15
                    )
            );


            // -------------------------------------------------
            // PARTICLES
            // -------------------------------------------------

            for (
                    Particle p :
                    particles
            ) {

                p.draw(
                        g2
                );
            }


            // -------------------------------------------------
            // LOGO PULSE
            // -------------------------------------------------

            double pulse =
                    1 +
                            Math.sin(
                                    animationStep *
                                            0.06
                            ) *
                                    0.035;


            int logoSize =
                    (int)
                            (
                                    76 *
                                            pulse
                            );


            /*
             * The actual emoji JLabel is painted
             * by Swing, while this soft glow sits
             * underneath it.
             */

            int glowSize =
                    logoSize +
                            30;


            int glowX =
                    width / 2 -
                            glowSize / 2;


            int glowY =
                    160 -
                            glowSize / 2 +
                            2;


            g2.setColor(
                    new Color(
                            92,
                            83,
                            220,
                            16
                    )
            );


            g2.fillOval(
                    glowX,
                    glowY,
                    glowSize,
                    glowSize
            );


            // -------------------------------------------------
            // PROGRESS TRACK
            // -------------------------------------------------

            if (
                    progressBar != null
            ) {

                int barX =
                        250;

                int barY =
                        405;

                int barWidth =
                        400;

                int barHeight =
                        8;


                g2.setColor(
                        new Color(
                                230,
                                232,
                                240
                        )
                );


                g2.fillRoundRect(
                        barX,
                        barY,
                        barWidth,
                        barHeight,
                        8,
                        8
                );


                int fillWidth =
                        (int)
                                (
                                        barWidth *
                                                progress /
                                                100.0
                                );


                if (
                        fillWidth > 0
                ) {

                    GradientPaint progressGradient =
                            new GradientPaint(
                                    barX,
                                    barY,
                                    PURPLE,
                                    barX +
                                            barWidth,
                                    barY,
                                    MINT
                            );


                    g2.setPaint(
                            progressGradient
                    );


                    g2.fillRoundRect(
                            barX,
                            barY,
                            fillWidth,
                            barHeight,
                            8,
                            8
                    );
                }
            }


            g2.dispose();
        }


        // =====================================================
        // GLOW
        // =====================================================

        private void drawGlow(
                Graphics2D g2,
                int centerX,
                int centerY,
                int size,
                Color color) {

            g2.setColor(
                    color
            );


            g2.fillOval(
                    centerX -
                            size / 2,
                    centerY -
                            size / 2,
                    size,
                    size
            );
        }
    }


    // =========================================================
    // MAIN
    // =========================================================

    public static void main(
            String[] args) {

        SwingUtilities.invokeLater(
                () -> {

                    SplashScreen splash =
                            new SplashScreen();


                    splash.setVisible(
                            true
                    );
                }
        );
    }
}