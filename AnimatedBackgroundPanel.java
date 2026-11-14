import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AnimatedBackgroundPanel extends JPanel {

    private final List<Orb> orbs = new ArrayList<>();
    private final javax.swing.Timer timer;

    public AnimatedBackgroundPanel() {

        setBackground(Constants.BG);

        Random random = new Random(4);

        // Create floating background particles
        for (int i = 0; i < 14; i++) {

            orbs.add(
                    new Orb(
                            random.nextInt(1000),
                            random.nextInt(700),
                            20 + random.nextInt(45),
                            (random.nextDouble() - 0.5) * 0.8,
                            (random.nextDouble() - 0.5) * 0.8
                    )
            );
        }

        // Animation timer
        timer = new javax.swing.Timer(30, e -> {

            for (Orb orb : orbs) {
                orb.tick(getWidth(), getHeight());
            }

            repaint();
        });

        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        // Draw floating particles
        for (Orb orb : orbs) {

            g2.setColor(
                    new Color(92, 83, 220, 18)
            );

            g2.fillOval(
                    (int) orb.x,
                    (int) orb.y,
                    orb.size,
                    orb.size
            );
        }

        g2.dispose();
    }

    // --------------------------------------------------
    // Floating Orb
    // --------------------------------------------------

    static class Orb {

        double x;
        double y;

        double dx;
        double dy;

        int size;

        Orb(
                double x,
                double y,
                int size,
                double dx,
                double dy
        ) {

            this.x = x;
            this.y = y;

            this.size = size;

            this.dx = dx;
            this.dy = dy;
        }

        void tick(int width, int height) {

            x += dx;
            y += dy;

            // Bounce horizontally
            if (x < -size || x > width) {
                dx = -dx;
            }

            // Bounce vertically
            if (y < -size || y > height) {
                dy = -dy;
            }
        }
    }
}