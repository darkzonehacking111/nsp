import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * ============================================================
 *                    GAMESMANIA
 *                    HOVER BUTTON
 * ============================================================
 *
 * Reusable animated button for the GamesMania Swing project.
 *
 * Features:
 * - Rounded corners
 * - Hover effect
 * - Press effect
 * - Smooth color animation
 * - Shadow
 * - Hand cursor
 * - Custom font
 * - Works with normal ActionListeners
 */
public class HoverButton extends JButton {

    // =========================================================
    // COLORS
    // =========================================================

    private Color normalColor =
            new Color(92, 83, 220);

    private Color hoverColor =
            new Color(72, 63, 200);

    private Color pressedColor =
            new Color(55, 47, 165);

    private Color textColor =
            Color.WHITE;

    private Color currentColor =
            normalColor;

    // =========================================================
    // SETTINGS
    // =========================================================

    private int radius = 16;

    private boolean hovering = false;

    private boolean pressed = false;

    private Timer animationTimer;


    // =========================================================
    // CONSTRUCTORS
    // =========================================================

    public HoverButton(
            String text) {

        super(text);

        setup();
    }


    public HoverButton(
            String text,
            Color color) {

        super(text);

        normalColor = color;

        hoverColor =
                color.brighter();

        pressedColor =
                color.darker();

        currentColor =
                normalColor;

        setup();
    }


    public HoverButton(
            String text,
            Color normal,
            Color hover,
            Color pressed) {

        super(text);

        normalColor =
                normal;

        hoverColor =
                hover;

        pressedColor =
                pressed;

        currentColor =
                normalColor;

        setup();
    }


    // =========================================================
    // SETUP
    // =========================================================

    private void setup() {

        setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        13
                )
        );

        setForeground(
                textColor
        );

        setFocusPainted(
                false
        );

        setBorderPainted(
                false
        );

        setContentAreaFilled(
                false
        );

        setOpaque(
                false
        );

        setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        setBorder(
                BorderFactory.createEmptyBorder(
                        10,
                        20,
                        10,
                        20
                )
        );


        addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseEntered(
                            MouseEvent e) {

                        hovering =
                                true;

                        pressed =
                                false;

                        animateTo(
                                hoverColor
                        );
                    }


                    @Override
                    public void mouseExited(
                            MouseEvent e) {

                        hovering =
                                false;

                        pressed =
                                false;

                        animateTo(
                                normalColor
                        );
                    }


                    @Override
                    public void mousePressed(
                            MouseEvent e) {

                        pressed =
                                true;

                        animateTo(
                                pressedColor
                        );
                    }


                    @Override
                    public void mouseReleased(
                            MouseEvent e) {

                        pressed =
                                false;

                        if (hovering) {

                            animateTo(
                                    hoverColor
                            );

                        } else {

                            animateTo(
                                    normalColor
                            );
                        }
                    }
                }
        );
    }


    // =========================================================
    // ANIMATION
    // =========================================================

    private void animateTo(
            Color target) {

        if (animationTimer != null &&
                animationTimer.isRunning()) {

            animationTimer.stop();
        }

        final Color start =
                currentColor;

        final int steps =
                12;

        final int[] step =
                {0};

        animationTimer =
                new Timer(
                        15,
                        null
                );

        animationTimer.addActionListener(
                e -> {

                    step[0]++;

                    float progress =
                            step[0] /
                                    (float) steps;

                    if (progress > 1f) {

                        progress = 1f;
                    }

                    int red =
                            (int)
                                    (start.getRed()
                                            + (target.getRed()
                                            - start.getRed())
                                            * progress);

                    int green =
                            (int)
                                    (start.getGreen()
                                            + (target.getGreen()
                                            - start.getGreen())
                                            * progress);

                    int blue =
                            (int)
                                    (start.getBlue()
                                            + (target.getBlue()
                                            - start.getBlue())
                                            * progress);

                    currentColor =
                            new Color(
                                    red,
                                    green,
                                    blue
                            );

                    repaint();

                    if (progress >= 1f) {

                        animationTimer.stop();

                        currentColor =
                                target;

                        repaint();
                    }
                }
        );

        animationTimer.start();
    }


    // =========================================================
    // PAINT
    // =========================================================

    @Override
    protected void paintComponent(
            Graphics g) {

        Graphics2D g2 =
                (Graphics2D)
                        g.create();

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        g2.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON
        );


        // -----------------------------------------------------
        // SHADOW
        // -----------------------------------------------------

        if (!pressed) {

            g2.setColor(
                    new Color(
                            0,
                            0,
                            0,
                            hovering
                                    ? 35
                                    : 20
                    )
            );

            g2.fillRoundRect(
                    2,
                    4,
                    getWidth() - 4,
                    getHeight() - 2,
                    radius,
                    radius
            );
        }


        // -----------------------------------------------------
        // BUTTON
        // -----------------------------------------------------

        g2.setColor(
                currentColor
        );

        int offset =
                pressed
                        ? 2
                        : 0;

        g2.fillRoundRect(
                0,
                offset,
                getWidth(),
                getHeight() - offset,
                radius,
                radius
        );


        // -----------------------------------------------------
        // TEXT
        // -----------------------------------------------------

        FontMetrics fm =
                g2.getFontMetrics(
                        getFont()
                );

        String text =
                getText();

        int textWidth =
                fm.stringWidth(
                        text
                );

        int textHeight =
                fm.getAscent();

        int x =
                (getWidth() -
                        textWidth) / 2;

        int y =
                (getHeight() -
                        fm.getHeight()) / 2
                        + textHeight;


        g2.setFont(
                getFont()
        );

        g2.setColor(
                getForeground()
        );

        g2.drawString(
                text,
                x,
                y
        );

        g2.dispose();
    }


    // =========================================================
    // SET COLORS
    // =========================================================

    public void setNormalColor(
            Color color) {

        if (color == null) {
            return;
        }

        normalColor =
                color;

        if (!hovering) {

            currentColor =
                    color;

            repaint();
        }
    }


    public void setHoverColor(
            Color color) {

        if (color == null) {
            return;
        }

        hoverColor =
                color;
    }


    public void setPressedColor(
            Color color) {

        if (color == null) {
            return;
        }

        pressedColor =
                color;
    }


    // =========================================================
    // GET COLORS
    // =========================================================

    public Color getNormalColor() {

        return normalColor;
    }


    public Color getHoverColor() {

        return hoverColor;
    }


    public Color getPressedColor() {

        return pressedColor;
    }


    // =========================================================
    // RADIUS
    // =========================================================

    public void setRadius(
            int radius) {

        this.radius =
                Math.max(
                        0,
                        radius
                );

        repaint();
    }


    public int getRadius() {

        return radius;
    }


    // =========================================================
    // TEXT COLOR
    // =========================================================

    public void setTextColor(
            Color color) {

        if (color == null) {
            return;
        }

        textColor =
                color;

        setForeground(
                color
        );

        repaint();
    }


    public Color getTextColor() {

        return textColor;
    }
}