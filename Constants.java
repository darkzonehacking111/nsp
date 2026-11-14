import java.awt.*;

public final class Constants {

    private Constants() {
        // Prevent object creation
    }

    // =========================================================
    // APP INFORMATION
    // =========================================================

    public static final String APP_NAME =
            "GamesMania";

    public static final String APP_VERSION =
            "1.0";

    public static final String PROJECT_TYPE =
            "NSP Project";

    public static final String DEVELOPER =
            "Rishabh Nandi";

    public static final String REG_NO =
            "PIET25cs135";

    public static final String ROLL_NO =
            "EPT25CS134";

    public static final String BATCH =
            "C1";


    // =========================================================
    // WINDOW
    // =========================================================

    public static final int WINDOW_WIDTH =
            1100;

    public static final int WINDOW_HEIGHT =
            720;

    public static final int GAME_WIDTH =
            950;

    public static final int GAME_HEIGHT =
            650;


    // =========================================================
    // MAIN THEME COLORS
    // =========================================================

    // Main background
    public static final Color BG =
            new Color(246, 248, 252);

    // Card background
    public static final Color CARD =
            new Color(255, 255, 255);

    // Primary purple
    public static final Color PRIMARY =
            new Color(92, 83, 220);

    // Dark purple
    public static final Color PRIMARY_DARK =
            new Color(70, 61, 175);

    // Mint / teal
    public static final Color ACCENT =
            new Color(47, 190, 167);

    // Pink
    public static final Color PINK =
            new Color(245, 111, 160);

    // Soft blue
    public static final Color BLUE =
            new Color(90, 145, 235);

    // Yellow
    public static final Color YELLOW =
            new Color(245, 190, 70);

    // Orange
    public static final Color ORANGE =
            new Color(245, 135, 65);

    // Red
    public static final Color RED =
            new Color(235, 80, 90);

    // Green
    public static final Color GREEN =
            new Color(65, 185, 120);


    // =========================================================
    // TEXT COLORS
    // =========================================================

    public static final Color TEXT =
            new Color(34, 38, 52);

    public static final Color TEXT_DARK =
            new Color(25, 28, 40);

    public static final Color MUTED =
            new Color(110, 115, 130);

    public static final Color LIGHT_TEXT =
            new Color(145, 149, 165);

    public static final Color WHITE =
            Color.WHITE;


    // =========================================================
    // BORDER / SHADOW
    // =========================================================

    public static final Color BORDER =
            new Color(229, 232, 240);

    public static final Color SHADOW =
            new Color(
                    0,
                    0,
                    0,
                    20
            );

    public static final Color SOFT_SHADOW =
            new Color(
                    0,
                    0,
                    0,
                    12
            );


    // =========================================================
    // BUTTON COLORS
    // =========================================================

    public static final Color BUTTON_PRIMARY =
            PRIMARY;

    public static final Color BUTTON_HOVER =
            PRIMARY_DARK;

    public static final Color BUTTON_SUCCESS =
            ACCENT;

    public static final Color BUTTON_DANGER =
            RED;


    // =========================================================
    // DIFFICULTY COLORS
    // =========================================================

    public static final Color EASY =
            new Color(
                    65,
                    185,
                    120
            );

    public static final Color MEDIUM =
            new Color(
                    245,
                    190,
                    70
            );

    public static final Color HARD =
            new Color(
                    235,
                    80,
                    90
            );


    // =========================================================
    // FONT NAMES
    // =========================================================

    public static final String FONT_MAIN =
            "Segoe UI";

    public static final String FONT_TITLE =
            "Segoe UI";

    public static final String FONT_MONO =
            "Consolas";


    // =========================================================
    // FONT SIZES
    // =========================================================

    public static final int FONT_SMALL =
            11;

    public static final int FONT_NORMAL =
            14;

    public static final int FONT_MEDIUM =
            16;

    public static final int FONT_LARGE =
            22;

    public static final int FONT_TITLE_SIZE =
            34;

    public static final int FONT_BIG_TITLE =
            42;


    // =========================================================
    // APPLICATION FONTS
    // =========================================================

    public static final Font FONT_NORMAL_PLAIN =
            new Font(
                    FONT_MAIN,
                    Font.PLAIN,
                    FONT_NORMAL
            );

    public static final Font FONT_NORMAL_BOLD =
            new Font(
                    FONT_MAIN,
                    Font.BOLD,
                    FONT_NORMAL
            );

    public static final Font FONT_MEDIUM_BOLD =
            new Font(
                    FONT_MAIN,
                    Font.BOLD,
                    FONT_MEDIUM
            );

    public static final Font FONT_TITLE_BOLD =
            new Font(
                    FONT_TITLE,
                    Font.BOLD,
                    FONT_TITLE_SIZE
            );

    public static final Font FONT_BIG_TITLE_BOLD =
            new Font(
                    FONT_TITLE,
                    Font.BOLD,
                    FONT_BIG_TITLE
            );


    // =========================================================
    // CORNER RADIUS
    // =========================================================

    public static final int RADIUS_SMALL =
            10;

    public static final int RADIUS_MEDIUM =
            16;

    public static final int RADIUS_LARGE =
            24;

    public static final int RADIUS_XL =
            30;


    // =========================================================
    // ANIMATION SETTINGS
    // =========================================================

    public static final int ANIMATION_DELAY =
            30;

    public static final int HOVER_ANIMATION_DELAY =
            15;

    public static final int LOADING_DELAY =
            35;

    public static final int SPLASH_DURATION =
            2200;


    // =========================================================
    // GAME SETTINGS
    // =========================================================

    public static final String EASY_MODE =
            "Easy";

    public static final String MEDIUM_MODE =
            "Medium";

    public static final String HARD_MODE =
            "Hard";

    public static final String DEFAULT_MODE =
            MEDIUM_MODE;


    // =========================================================
    // GAME NAMES
    // =========================================================

    public static final String SNAKE =
            "Snake Game";

    public static final String COIN_FLIP =
            "Coin Flip";

    public static final String LUCKY_SPIN =
            "Lucky Spin";

    public static final String ARCHER_AIM =
            "Archer Aim";

    public static final String FRUIT_CATCHER =
            "Fruit Catcher";

    public static final String DINO_RUN =
            "Dino Run";

    public static final String SUDOKU =
            "Sudoku";

    public static final String MINI_RACING =
            "Mini Racing";

    public static final String MINI_PONG =
            "Mini Pong";

    public static final String COLOR_MATCH =
            "Color Match";

    public static final String GAME_2048 =
            "2048";

    public static final String REACTION_TIME =
            "Reaction Time";

    public static final String XO =
            "X-O";

    public static final String NUMBER_GUESS =
            "Number Guessing";

    public static final String ASTEROID_SHOOTER =
            "Asteroid Shooter";

    public static final String SLIDING_PUZZLE =
            "Sliding Puzzle";


    // =========================================================
    // DATABASE
    // =========================================================

    public static final String DATABASE_NAME =
            "gamesmania.db";

    public static final String DATABASE_URL =
            "jdbc:sqlite:" + DATABASE_NAME;


    // =========================================================
    // DATABASE TABLES
    // =========================================================

    public static final String USERS_TABLE =
            "users";

    public static final String SCORES_TABLE =
            "scores";


    // =========================================================
    // APPLICATION PADDING
    // =========================================================

    public static final int PADDING_SMALL =
            8;

    public static final int PADDING =
            15;

    public static final int PADDING_MEDIUM =
            20;

    public static final int PADDING_LARGE =
            30;


    // =========================================================
    // GAME ICONS
    // =========================================================

    public static final String ICON_SNAKE =
            "🐍";

    public static final String ICON_COIN =
            "🪙";

    public static final String ICON_SPIN =
            "🎡";

    public static final String ICON_ARCHER =
            "🏹";

    public static final String ICON_FRUIT =
            "🍎";

    public static final String ICON_DINO =
            "🦖";

    public static final String ICON_SUDOKU =
            "🔢";

    public static final String ICON_RACING =
            "🏎";

    public static final String ICON_PONG =
            "🏓";

    public static final String ICON_COLOR =
            "🎨";

    public static final String ICON_2048 =
            "🔢";

    public static final String ICON_REACTION =
            "⚡";

    public static final String ICON_XO =
            "⭕";

    public static final String ICON_GUESS =
            "❓";

    public static final String ICON_ASTEROID =
            "☄";

    public static final String ICON_PUZZLE =
            "🧩";


    // =========================================================
    // UTILITY METHODS
    // =========================================================

    public static Color difficultyColor(
            String difficulty) {

        if (difficulty == null) {
            return MEDIUM;
        }

        switch (difficulty) {

            case EASY_MODE:
                return EASY;

            case HARD_MODE:
                return HARD;

            default:
                return MEDIUM;
        }
    }


    public static Font boldFont(
            int size) {

        return new Font(
                FONT_MAIN,
                Font.BOLD,
                size
        );
    }


    public static Font plainFont(
            int size) {

        return new Font(
                FONT_MAIN,
                Font.PLAIN,
                size
        );
    }
}