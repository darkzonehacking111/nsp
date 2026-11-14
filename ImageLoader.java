import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * ============================================================
 *                      GAMESMANIA
 *                      IMAGE LOADER
 * ============================================================
 *
 * Central image utility for the GamesMania Swing application.
 *
 * Features:
 * - Loads images from resources
 * - Loads images from file paths
 * - Automatically resizes images
 * - Caches images
 * - Creates placeholder images when missing
 * - Creates rounded images
 * - Creates circular profile images
 * - Safe error handling
 *
 * Recommended resource structure:
 *
 * src/
 * ├── Main.java
 * ├── ImageLoader.java
 * └── resources/
 *     ├── logo.png
 *     ├── background.png
 *     ├── snake.png
 *     ├── archer.png
 *     └── ...
 *
 * ============================================================
 */
public final class ImageLoader {

    // =========================================================
    // IMAGE CACHE
    // =========================================================

    private static final Map<String, BufferedImage> CACHE =
            new HashMap<>();


    // =========================================================
    // PRIVATE CONSTRUCTOR
    // =========================================================

    private ImageLoader() {
        // Utility class.
    }


    // =========================================================
    // LOAD IMAGE FROM RESOURCE
    // =========================================================

    /**
     * Loads an image from the classpath.
     *
     * Example:
     *
     * BufferedImage image =
     *         ImageLoader.load("/resources/logo.png");
     */
    public static BufferedImage load(
            String resourcePath) {

        if (resourcePath == null ||
                resourcePath.trim().isEmpty()) {

            return null;
        }


        // Check cache first.
        if (CACHE.containsKey(resourcePath)) {

            return CACHE.get(
                    resourcePath
            );
        }


        try {

            InputStream stream =
                    ImageLoader.class
                            .getResourceAsStream(
                                    resourcePath
                            );


            if (stream == null) {

                // Try without leading slash.
                String path =
                        resourcePath.startsWith("/")
                                ? resourcePath.substring(1)
                                : resourcePath;

                stream =
                        ImageLoader.class
                                .getClassLoader()
                                .getResourceAsStream(
                                        path
                                );
            }


            if (stream == null) {

                System.out.println(
                        "Image not found: "
                                + resourcePath
                );

                return null;
            }


            BufferedImage image =
                    ImageIO.read(stream);

            stream.close();


            if (image != null) {

                CACHE.put(
                        resourcePath,
                        image
                );
            }


            return image;

        } catch (IOException e) {

            System.out.println(
                    "Could not load image: "
                            + resourcePath
            );

            return null;
        }
    }


    // =========================================================
    // LOAD IMAGE FROM FILE
    // =========================================================

    /**
     * Loads an image from a normal computer path.
     *
     * Example:
     *
     * ImageLoader.loadFile(
     *     "C:/GamesMania/images/logo.png"
     * );
     */
    public static BufferedImage loadFile(
            String filePath) {

        if (filePath == null ||
                filePath.trim().isEmpty()) {

            return null;
        }


        if (CACHE.containsKey(filePath)) {

            return CACHE.get(
                    filePath
            );
        }


        try {

            File file =
                    new File(
                            filePath
                    );


            if (!file.exists()) {

                System.out.println(
                        "File not found: "
                                + filePath
                );

                return null;
            }


            BufferedImage image =
                    ImageIO.read(
                            file
                    );


            if (image != null) {

                CACHE.put(
                        filePath,
                        image
                );
            }


            return image;

        } catch (IOException e) {

            System.out.println(
                    "Could not read image: "
                            + filePath
            );

            return null;
        }
    }


    // =========================================================
    // LOAD IMAGE FROM URL
    // =========================================================

    /**
     * Loads an image from an internet URL.
     *
     * Example:
     *
     * ImageLoader.loadURL(
     *     "https://example.com/image.png"
     * );
     */
    public static BufferedImage loadURL(
            String urlString) {

        if (urlString == null ||
                urlString.trim().isEmpty()) {

            return null;
        }


        if (CACHE.containsKey(urlString)) {

            return CACHE.get(
                    urlString
            );
        }


        try {

            URL url =
                    new URL(
                            urlString
                    );


            BufferedImage image =
                    ImageIO.read(
                            url
                    );


            if (image != null) {

                CACHE.put(
                        urlString,
                        image
                );
            }


            return image;

        } catch (Exception e) {

            System.out.println(
                    "Could not load URL image: "
                            + urlString
            );

            return null;
        }
    }


    // =========================================================
    // IMAGE ICON
    // =========================================================

    /**
     * Returns an ImageIcon.
     */
    public static ImageIcon icon(
            String resourcePath) {

        BufferedImage image =
                load(
                        resourcePath
                );


        if (image == null) {

            return null;
        }


        return new ImageIcon(
                image
        );
    }


    // =========================================================
    // RESIZED IMAGE
    // =========================================================

    /**
     * Loads and resizes an image.
     */
    public static ImageIcon icon(
            String resourcePath,
            int width,
            int height) {

        BufferedImage image =
                load(
                        resourcePath
                );


        if (image == null) {

            return null;
        }


        Image resized =
                image.getScaledInstance(
                        width,
                        height,
                        Image.SCALE_SMOOTH
                );


        return new ImageIcon(
                resized
        );
    }


    // =========================================================
    // RESIZE BUFFERED IMAGE
    // =========================================================

    public static BufferedImage resize(
            BufferedImage original,
            int width,
            int height) {

        if (original == null) {

            return null;
        }


        if (width <= 0 ||
                height <= 0) {

            return original;
        }


        BufferedImage resized =
                new BufferedImage(
                        width,
                        height,
                        BufferedImage.TYPE_INT_ARGB
                );


        Graphics2D g2 =
                resized.createGraphics();


        g2.setRenderingHint(
                RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BICUBIC
        );

        g2.setRenderingHint(
                RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY
        );

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );


        g2.drawImage(
                original,
                0,
                0,
                width,
                height,
                null
        );


        g2.dispose();


        return resized;
    }


    // =========================================================
    // RESIZED ICON
    // =========================================================

    public static ImageIcon resizedIcon(
            String resourcePath,
            int width,
            int height) {

        BufferedImage image =
                load(
                        resourcePath
                );


        if (image == null) {

            return null;
        }


        BufferedImage resized =
                resize(
                        image,
                        width,
                        height
                );


        return new ImageIcon(
                resized
        );
    }


    // =========================================================
    // SCALED TO FIT
    // =========================================================

    /**
     * Keeps original aspect ratio while fitting
     * inside the requested dimensions.
     */
    public static BufferedImage fit(
            BufferedImage original,
            int maxWidth,
            int maxHeight) {

        if (original == null) {

            return null;
        }


        int originalWidth =
                original.getWidth();

        int originalHeight =
                original.getHeight();


        if (originalWidth <= 0 ||
                originalHeight <= 0) {

            return original;
        }


        double widthRatio =
                maxWidth /
                        (double) originalWidth;


        double heightRatio =
                maxHeight /
                        (double) originalHeight;


        double ratio =
                Math.min(
                        widthRatio,
                        heightRatio
                );


        int newWidth =
                Math.max(
                        1,
                        (int)
                                (originalWidth *
                                        ratio)
                );


        int newHeight =
                Math.max(
                        1,
                        (int)
                                (originalHeight *
                                        ratio)
                );


        return resize(
                original,
                newWidth,
                newHeight
        );
    }


    // =========================================================
    // COVER
    // =========================================================

    /**
     * Scales image so it completely covers
     * the requested dimensions.
     */
    public static BufferedImage cover(
            BufferedImage original,
            int width,
            int height) {

        if (original == null) {

            return null;
        }


        double scaleX =
                width /
                        (double) original.getWidth();


        double scaleY =
                height /
                        (double) original.getHeight();


        double scale =
                Math.max(
                        scaleX,
                        scaleY
                );


        int newWidth =
                (int)
                        (original.getWidth()
                                * scale);


        int newHeight =
                (int)
                        (original.getHeight()
                                * scale);


        BufferedImage scaled =
                resize(
                        original,
                        newWidth,
                        newHeight
                );


        BufferedImage result =
                new BufferedImage(
                        width,
                        height,
                        BufferedImage.TYPE_INT_ARGB
                );


        Graphics2D g2 =
                result.createGraphics();


        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );


        int x =
                (width - newWidth) / 2;

        int y =
                (height - newHeight) / 2;


        g2.drawImage(
                scaled,
                x,
                y,
                null
        );


        g2.dispose();


        return result;
    }


    // =========================================================
    // ROUND IMAGE
    // =========================================================

    /**
     * Creates a rounded-corner version.
     */
    public static BufferedImage rounded(
            BufferedImage original,
            int width,
            int height,
            int radius) {

        if (original == null) {

            return null;
        }


        BufferedImage resized =
                cover(
                        original,
                        width,
                        height
                );


        BufferedImage result =
                new BufferedImage(
                        width,
                        height,
                        BufferedImage.TYPE_INT_ARGB
                );


        Graphics2D g2 =
                result.createGraphics();


        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );


        Shape clip =
                new java.awt.geom.RoundRectangle2D.Double(
                        0,
                        0,
                        width,
                        height,
                        radius,
                        radius
                );


        g2.setClip(
                clip
        );


        g2.drawImage(
                resized,
                0,
                0,
                null
        );


        g2.dispose();


        return result;
    }


    // =========================================================
    // ROUND ICON
    // =========================================================

    public static ImageIcon roundedIcon(
            String resourcePath,
            int width,
            int height,
            int radius) {

        BufferedImage image =
                load(
                        resourcePath
                );


        if (image == null) {

            return null;
        }


        BufferedImage rounded =
                rounded(
                        image,
                        width,
                        height,
                        radius
                );


        return new ImageIcon(
                rounded
        );
    }


    // =========================================================
    // CIRCLE IMAGE
    // =========================================================

    /**
     * Creates a circular image.
     *
     * Useful for player profiles.
     */
    public static BufferedImage circle(
            BufferedImage original,
            int size) {

        if (original == null) {

            return null;
        }


        BufferedImage square =
                cover(
                        original,
                        size,
                        size
                );


        BufferedImage result =
                new BufferedImage(
                        size,
                        size,
                        BufferedImage.TYPE_INT_ARGB
                );


        Graphics2D g2 =
                result.createGraphics();


        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );


        g2.setClip(
                new java.awt.geom.Ellipse2D.Double(
                        0,
                        0,
                        size,
                        size
                )
        );


        g2.drawImage(
                square,
                0,
                0,
                null
        );


        g2.dispose();


        return result;
    }


    // =========================================================
    // CIRCLE ICON
    // =========================================================

    public static ImageIcon circleIcon(
            String resourcePath,
            int size) {

        BufferedImage image =
                load(
                        resourcePath
                );


        if (image == null) {

            return null;
        }


        BufferedImage circle =
                circle(
                        image,
                        size
                );


        return new ImageIcon(
                circle
        );
    }


    // =========================================================
    // PLACEHOLDER
    // =========================================================

    /**
     * Creates a beautiful GamesMania placeholder
     * when an image doesn't exist.
     */
    public static BufferedImage placeholder(
            int width,
            int height) {

        BufferedImage image =
                new BufferedImage(
                        width,
                        height,
                        BufferedImage.TYPE_INT_ARGB
                );


        Graphics2D g2 =
                image.createGraphics();


        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );


        // Background.
        g2.setColor(
                new Color(
                        237,
                        234,
                        255
                )
        );

        g2.fillRect(
                0,
                0,
                width,
                height
        );


        // Controller icon.
        g2.setColor(
                new Color(
                        92,
                        83,
                        220
                )
        );


        int iconSize =
                Math.min(
                        width,
                        height
                ) / 3;


        int x =
                (width - iconSize) / 2;


        int y =
                (height - iconSize) / 2;


        g2.fillRoundRect(
                x,
                y,
                iconSize,
                iconSize / 2,
                20,
                20
        );


        // Small circles.
        g2.setColor(
                Color.WHITE
        );


        int circleSize =
                Math.max(
                        5,
                        iconSize / 8
                );


        g2.fillOval(
                x + iconSize / 4,
                y + iconSize / 4,
                circleSize,
                circleSize
        );


        g2.fillOval(
                x + iconSize * 3 / 5,
                y + iconSize / 4,
                circleSize,
                circleSize
        );


        g2.dispose();


        return image;
    }


    // =========================================================
    // SAFE ICON
    // =========================================================

    /**
     * Returns a valid ImageIcon even when
     * the requested image is missing.
     */
    public static ImageIcon safeIcon(
            String resourcePath,
            int width,
            int height) {

        BufferedImage image =
                load(
                        resourcePath
                );


        if (image == null) {

            image =
                    placeholder(
                            width,
                            height
                    );
        }


        image =
                resize(
                        image,
                        width,
                        height
                );


        return new ImageIcon(
                image
        );
    }


    // =========================================================
    // CLEAR CACHE
    // =========================================================

    public static void clearCache() {

        CACHE.clear();
    }


    // =========================================================
    // REMOVE ONE IMAGE
    // =========================================================

    public static void removeFromCache(
            String path) {

        if (path != null) {

            CACHE.remove(
                    path
            );
        }
    }


    // =========================================================
    // IMAGE EXISTS
    // =========================================================

    public static boolean exists(
            String resourcePath) {

        if (resourcePath == null) {

            return false;
        }


        InputStream stream =
                ImageLoader.class
                        .getResourceAsStream(
                                resourcePath
                        );


        if (stream == null) {

            return false;
        }


        try {

            stream.close();

        } catch (IOException ignored) {
        }


        return true;
    }
}