import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.function.Consumer;

/**
 * Global Theme Manager for GamesMania.
 * Manages themes, persists user selection to preferences.properties,
 * and notifies active listeners for real-time live theme updates across all windows.
 */
public class ThemeManager {

    public static class Theme {
        public final String name;
        public final Color bg;
        public final Color headerBg;
        public final Color cardBg;
        public final Color cardBorder;
        public final Color textPrimary;
        public final Color textMuted;
        public final Color accent;
        public final Color accentSecondary;
        public final boolean isDark;

        public Theme(String name, Color bg, Color headerBg, Color cardBg, Color cardBorder,
                     Color textPrimary, Color textMuted, Color accent, Color accentSecondary, boolean isDark) {
            this.name = name;
            this.bg = bg;
            this.headerBg = headerBg;
            this.cardBg = cardBg;
            this.cardBorder = cardBorder;
            this.textPrimary = textPrimary;
            this.textMuted = textMuted;
            this.accent = accent;
            this.accentSecondary = accentSecondary;
            this.isDark = isDark;
        }
    }

    private static final String PREF_FILE = "preferences.properties";
    private static final List<Consumer<Theme>> listeners = new ArrayList<>();

    // Available Themes
    public static final Theme THEME_DARK = new Theme(
            "Dark",
            new Color(15, 23, 42),       // Slate 900
            new Color(30, 41, 59),       // Slate 800
            new Color(30, 41, 59),       // Slate 800
            new Color(51, 65, 85),       // Slate 700
            new Color(248, 250, 252),    // Slate 50
            new Color(148, 163, 184),    // Slate 400
            new Color(56, 189, 248),     // Sky 400
            new Color(129, 140, 248),    // Indigo 400
            true
    );

    public static final Theme THEME_LIGHT = new Theme(
            "Light",
            new Color(248, 245, 238),    // Warm Antique Parchment / Alabaster
            new Color(255, 253, 248),    // Warm Ivory Header
            new Color(255, 255, 255),    // Pure Crisp Alabaster Card
            new Color(220, 195, 155),    // Aged Antique Brass & Gold Border
            new Color(28, 25, 23),       // Deep Bold Espresso Text
            new Color(125, 100, 80),     // Warm Antique Sepia Text
            new Color(217, 119, 6),      // Rich Amber Gold
            new Color(180, 83, 9),       // Antique Burnished Bronze
            false
    );

    public static final Theme THEME_BLUE = new Theme(
            "Blue",
            new Color(10, 25, 47),       // Deep Navy
            new Color(15, 34, 64),       // Navy Accent
            new Color(17, 45, 78),       // Ocean Card
            new Color(30, 75, 120),      // Blue Border
            new Color(230, 241, 255),    // Pale Blue Text
            new Color(136, 146, 176),    // Muted Blue
            new Color(100, 255, 218),    // Mint Cyan
            new Color(56, 189, 248),     // Sky Accent
            true
    );

    public static final Theme THEME_EMERALD = new Theme(
            "Emerald",
            new Color(6, 32, 23),        // Dark Forest
            new Color(10, 46, 34),       // Emerald Header
            new Color(14, 61, 45),       // Emerald Card
            new Color(22, 101, 74),      // Green Border
            new Color(236, 253, 245),    // Mint Text
            new Color(110, 231, 183),    // Soft Green
            new Color(52, 211, 153),     // Neon Green
            new Color(16, 185, 129),     // Jade
            true
    );

    public static final Theme THEME_PURPLE = new Theme(
            "Purple",
            new Color(24, 12, 40),       // Deep Violet
            new Color(38, 16, 62),       // Purple Header
            new Color(48, 22, 78),       // Purple Card
            new Color(88, 28, 135),      // Purple Border
            new Color(250, 245, 255),    // Light Lilac
            new Color(216, 180, 254),    // Muted Lavender
            new Color(244, 63, 94),      // Rose Pink
            new Color(192, 132, 252),    // Bright Violet
            true
    );

    public static final Theme THEME_VINTAGE = new Theme(
            "Vintage",
            new Color(26, 20, 16),       // Antique Dark Walnut / Espresso
            new Color(38, 30, 24),       // Vintage Mahogany Header
            new Color(44, 35, 28),       // Vintage Arcade Chassis Card
            new Color(98, 76, 56),       // Aged Bronze / Brass Border
            new Color(254, 243, 199),    // Warm Amber Parchment Cream
            new Color(217, 180, 130),    // Muted Antique Gold
            new Color(245, 158, 11),     // Neon Amber Gold
            new Color(239, 68, 68),      // Retro Cherry Red
            true
    );

    private static Theme currentTheme = THEME_LIGHT;

    static {
        loadPreference();
    }

    public static Theme getCurrentTheme() {
        return currentTheme;
    }

    public static Theme[] getAllThemes() {
        return new Theme[]{THEME_LIGHT, THEME_DARK, THEME_VINTAGE, THEME_BLUE, THEME_EMERALD, THEME_PURPLE};
    }

    public static String[] getThemeNames() {
        return new String[]{"Light", "Dark", "Vintage", "Blue", "Emerald", "Purple"};
    }

    public static void setTheme(String themeName) {
        if (themeName == null) return;
        switch (themeName.trim().toLowerCase()) {
            case "light":
                currentTheme = THEME_LIGHT;
                break;
            case "vintage":
            case "retro":
            case "vintage arcade":
            case "amber":
                currentTheme = THEME_VINTAGE;
                break;
            case "blue":
            case "blue cyber":
                currentTheme = THEME_BLUE;
                break;
            case "emerald":
            case "emerald neon":
                currentTheme = THEME_EMERALD;
                break;
            case "purple":
            case "purple synth":
            case "sunset":
                currentTheme = THEME_PURPLE;
                break;
            case "dark":
            default:
                currentTheme = THEME_DARK;
                break;
        }

        savePreference();
        notifyListeners();
    }

    public static void addListener(Consumer<Theme> listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
            listener.accept(currentTheme);
        }
    }

    public static void removeListener(Consumer<Theme> listener) {
        listeners.remove(listener);
    }

    private static void notifyListeners() {
        for (Consumer<Theme> listener : new ArrayList<>(listeners)) {
            try {
                listener.accept(currentTheme);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void loadPreference() {
        File file = new File(PREF_FILE);
        if (!file.exists()) return;
        try (InputStream in = new FileInputStream(file)) {
            Properties props = new Properties();
            props.load(in);
            String saved = props.getProperty("theme", "Light");
            setTheme(saved);
        } catch (Exception ignored) {}
    }

    private static void savePreference() {
        File file = new File(PREF_FILE);
        Properties props = new Properties();
        if (file.exists()) {
            try (InputStream in = new FileInputStream(file)) {
                props.load(in);
            } catch (Exception ignored) {}
        }
        try (OutputStream out = new FileOutputStream(file)) {
            props.setProperty("theme", currentTheme.name);
            props.store(out, "GamesMania User Preferences");
        } catch (Exception ignored) {}
    }
}
