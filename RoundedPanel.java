import javax.swing.*;
import java.awt.*;

/**
 * ============================================================
 *                     GAMESMANIA
 *                    ROUNDED PANEL
 * ============================================================
 *
 * Custom JPanel with:
 *
 * ✓ Rounded corners
 * ✓ Soft shadow
 * ✓ Custom background
 * ✓ Custom border
 * ✓ Adjustable corner radius
 * ✓ Smooth anti-aliasing
 *
 * Example:
 *
 * RoundedPanel panel =
 *         new RoundedPanel(
 *                 20,
 *                 Color.WHITE
 *         );
 *
 * ============================================================
 */
public class RoundedPanel extends JPanel {

    // =========================================================
    // SETTINGS
    // =========================================================

    private int radius =
            20;

    private Color panelColor =
            Color.WHITE;

    private Color borderColor =
            new Color(
                    230,
                    232,
                    240
            );

    private boolean shadow =
            true;

    private int shadowSize =
            5;


    // =========================================================
    // DEFAULT CONSTRUCTOR
    // =========================================================

    public RoundedPanel() {

        this(
                20,
                Color.WHITE
        );
    }


    // =========================================================
    // RADIUS CONSTRUCTOR
    // =========================================================

    public RoundedPanel(
            int radius) {

        this(
                radius,
                Color.WHITE
        );
    }


    // =========================================================
    // RADIUS + COLOR CONSTRUCTOR
    // =========================================================

    public RoundedPanel(
            int radius,
            Color color) {

        this.radius =
                Math.max(
                        0,
                        radius
                );

        this.panelColor =
                color != null
                        ? color
                        : Color.WHITE;


        setOpaque(
                false
        );


        setBorder(
                BorderFactory.createEmptyBorder(
                        shadowSize + 5,
                        shadowSize + 5,
                        shadowSize + 5,
                        shadowSize + 5
                )
        );
    }


    // =========================================================
    // SET RADIUS
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


    // =========================================================
    // GET RADIUS
    // =========================================================

    public int getRadius() {

        return radius;
    }


    // =========================================================
    // SET PANEL COLOR
    // =========================================================

    public void setPanelColor(
            Color color) {

        if (color != null) {

            this.panelColor =
                    color;
        }

        repaint();
    }


    // =========================================================
    // GET PANEL COLOR
    // =========================================================

    public Color getPanelColor() {

        return panelColor;
    }


    // =========================================================
    // SET BORDER COLOR
    // =========================================================

    public void setBorderColor(
            Color color) {

        if (color != null) {

            this.borderColor =
                    color;
        }

        repaint();
    }


    // =========================================================
    // GET BORDER COLOR
    // =========================================================

    public Color getBorderColor() {

        return borderColor;
    }


    // =========================================================
    // ENABLE / DISABLE SHADOW
    // =========================================================

    public void setShadow(
            boolean shadow) {

        this.shadow =
                shadow;

        repaint();
    }


    // =========================================================
    // GET SHADOW
    // =========================================================

    public boolean hasShadow() {

        return shadow;
    }


    // =========================================================
    // SET SHADOW SIZE
    // =========================================================

    public void setShadowSize(
            int size) {

        this.shadowSize =
                Math.max(
                        0,
                        size
                );


        setBorder(
                BorderFactory.createEmptyBorder(
                        shadowSize + 5,
                        shadowSize + 5,
                        shadowSize + 5,
                        shadowSize + 5
                )
        );


        revalidate();

        repaint();
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


        // -----------------------------------------------------
        // ANTI ALIASING
        // -----------------------------------------------------

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );


        g2.setRenderingHint(
                RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY
        );


        g2.setRenderingHint(
                RenderingHints.KEY_STROKE_CONTROL,
                RenderingHints.VALUE_STROKE_PURE
        );


        int width =
                getWidth();

        int height =
                getHeight();


        // -----------------------------------------------------
        // SHADOW
        // -----------------------------------------------------

        if (shadow &&
                shadowSize > 0) {

            for (
                    int i = shadowSize;
                    i > 0;
                    i--
            ) {

                int alpha =
                        Math.max(
                                3,
                                18 -
                                        i * 2
                        );


                g2.setColor(
                        new Color(
                                40,
                                45,
                                60,
                                alpha
                        )
                );


                g2.fillRoundRect(
                        i,
                        i,
                        width -
                                i * 2,
                        height -
                                i * 2,
                        radius,
                        radius
                );
            }
        }


        // -----------------------------------------------------
        // MAIN PANEL
        // -----------------------------------------------------

        int padding =
                shadow
                        ? shadowSize
                        : 0;


        int panelWidth =
                width -
                        padding * 2;


        int panelHeight =
                height -
                        padding * 2;


        g2.setColor(
                panelColor
        );


        g2.fillRoundRect(
                padding,
                padding,
                panelWidth,
                panelHeight,
                radius,
                radius
        );


        // -----------------------------------------------------
        // BORDER
        // -----------------------------------------------------

        if (borderColor != null) {

            g2.setColor(
                    borderColor
            );


            g2.setStroke(
                    new BasicStroke(
                            1f
                    )
            );


            g2.drawRoundRect(
                    padding,
                    padding,
                    panelWidth - 1,
                    panelHeight - 1,
                    radius,
                    radius
            );
        }


        g2.dispose();


        // -----------------------------------------------------
        // CHILD COMPONENTS
        // -----------------------------------------------------

        super.paintComponent(
                g
        );
    }


    // =========================================================
    // FACTORY METHODS
    // =========================================================

    /**
     * Creates a standard white GamesMania card.
     */
    public static RoundedPanel card() {

        RoundedPanel panel =
                new RoundedPanel(
                        20,
                        Color.WHITE
                );


        panel.setShadow(
                true
        );


        return panel;
    }


    /**
     * Creates a flat panel without shadow.
     */
    public static RoundedPanel flat() {

        RoundedPanel panel =
                new RoundedPanel(
                        20,
                        Color.WHITE
                );


        panel.setShadow(
                false
        );


        return panel;
    }


    /**
     * Creates a colored accent card.
     */
    public static RoundedPanel accent(
            Color color) {

        RoundedPanel panel =
                new RoundedPanel(
                        20,
                        color
                );


        panel.setShadow(
                true
        );


        return panel;
    }


    // =========================================================
    // TEST
    // =========================================================

    public static void main(
            String[] args) {

        SwingUtilities.invokeLater(
                () -> {

                    JFrame frame =
                            new JFrame(
                                    "GamesMania • Rounded Panel"
                            );


                    frame.setSize(
                            650,
                            450
                    );


                    frame.setLocationRelativeTo(
                            null
                    );


                    frame.setDefaultCloseOperation(
                            JFrame.EXIT_ON_CLOSE
                    );


                    JPanel background =
                            new JPanel(
                                    new GridBagLayout()
                            );


                    background.setBackground(
                            new Color(
                                    246,
                                    248,
                                    252
                            )
                    );


                    RoundedPanel card =
                            new RoundedPanel(
                                    25,
                                    Color.WHITE
                            );


                    card.setPreferredSize(
                            new Dimension(
                                    420,
                                    250
                            )
                    );


                    JPanel content =
                            new JPanel();

                    content.setOpaque(
                            false
                    );


                    content.setLayout(
                            new BoxLayout(
                                    content,
                                    BoxLayout.Y_AXIS
                            )
                    );


                    JLabel title =
                            new JLabel(
                                    "🎮 GamesMania"
                            );


                    title.setFont(
                            new Font(
                                    "SansSerif",
                                    Font.BOLD,
                                    28
                            )
                    );


                    title.setForeground(
                            new Color(
                                    35,
                                    38,
                                    52
                            )
                    );


                    title.setAlignmentX(
                            Component.CENTER_ALIGNMENT
                    );


                    JLabel subtitle =
                            new JLabel(
                                    "RoundedPanel Preview"
                            );


                    subtitle.setFont(
                            new Font(
                                    "SansSerif",
                                    Font.PLAIN,
                                    13
                            )
                    );


                    subtitle.setForeground(
                            new Color(
                                    110,
                                    115,
                                    130
                            )
                    );


                    subtitle.setAlignmentX(
                            Component.CENTER_ALIGNMENT
                    );


                    content.add(
                            Box.createVerticalGlue()
                    );


                    content.add(
                            title
                    );


                    content.add(
                            Box.createVerticalStrut(
                                    8
                            )
                    );


                    content.add(
                            subtitle
                    );


                    content.add(
                            Box.createVerticalGlue()
                    );


                    card.setLayout(
                            new BorderLayout()
                    );


                    card.add(
                            content,
                            BorderLayout.CENTER
                    );


                    background.add(
                            card
                    );


                    frame.add(
                            background
                    );


                    frame.setVisible(
                            true
                    );
                }
        );
    }
}