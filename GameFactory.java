import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Constructor;

/**
 * ============================================================
 *                       GAMESMANIA
 *                       GAME FACTORY
 * ============================================================
 *
 * Central class used to open all games.
 *
 * Supports:
 *      User
 *      String player name
 *      Difficulty
 *
 * Uses reflection so GameFactory does not fail to compile
 * when an optional game class is not present.
 *
 * ============================================================
 */
public class GameFactory {

    private GameFactory() {
        // Utility class
    }


    // =========================================================
    // CREATE GAME - USER
    // =========================================================

    public static JFrame createGame(
            String gameName,
            User user) {

        String playerName =
                getUserName(user);

        return createGame(
                gameName,
                playerName
        );
    }


    // =========================================================
    // CREATE GAME - PLAYER NAME
    // =========================================================

    public static JFrame createGame(
            String gameName,
            String playerName) {

        return createGame(
                gameName,
                playerName,
                "Medium"
        );
    }


    // =========================================================
    // CREATE GAME - USER + DIFFICULTY
    // =========================================================

    public static JFrame createGame(
            String gameName,
            User user,
            String difficulty) {

        String playerName =
                getUserName(user);

        return createGame(
                gameName,
                playerName,
                difficulty
        );
    }


    // =========================================================
    // MAIN CREATE METHOD
    // =========================================================

    public static JFrame createGame(
            String gameName,
            String playerName,
            String difficulty) {

        if (
                gameName == null ||
                        gameName.trim().isEmpty()
        ) {

            return errorFrame(
                    "Game name cannot be empty."
            );
        }


        if (
                playerName == null ||
                        playerName.trim().isEmpty()
        ) {

            playerName =
                    "Player";
        }


        if (
                difficulty == null ||
                        difficulty.trim().isEmpty()
        ) {

            difficulty =
                    "Medium";
        }


        String className =
                getClassName(gameName);


        if (
                className == null
        ) {

            return errorFrame(
                    "Unknown game:\n" +
                            gameName
            );
        }


        JFrame game =
                createByReflection(
                        className,
                        playerName,
                        difficulty
                );


        if (
                game == null
        ) {

            return errorFrame(
                    "Could not open:\n" +
                            gameName +
                            "\n\nExpected class:\n" +
                            className
            );
        }


        return game;
    }


    // =========================================================
    // OPEN GAME
    // =========================================================

    public static void openGame(
            String gameName,
            String playerName) {

        JFrame game =
                createGame(
                        gameName,
                        playerName
                );


        if (
                game != null
        ) {

            game.setLocationRelativeTo(
                    null
            );


            game.setVisible(
                    true
            );
        }
    }


    // =========================================================
    // OPEN GAME - USER
    // =========================================================

    public static void openGame(
            String gameName,
            User user) {

        JFrame game =
                createGame(
                        gameName,
                        user
                );


        if (
                game != null
        ) {

            game.setLocationRelativeTo(
                    null
            );


            game.setVisible(
                    true
            );
        }
    }


    // =========================================================
    // OPEN GAME - USER + DIFFICULTY
    // =========================================================

    public static void openGame(
            String gameName,
            User user,
            String difficulty) {

        JFrame game =
                createGame(
                        gameName,
                        user,
                        difficulty
                );


        if (
                game != null
        ) {

            game.setLocationRelativeTo(
                    null
            );


            game.setVisible(
                    true
            );
        }
    }


    // =========================================================
    // CLASS NAME MAPPING
    // =========================================================

    private static String getClassName(
            String gameName) {

        String name =
                gameName
                        .trim()
                        .toLowerCase();


        switch (name) {

            // -------------------------------------------------
            // ARCHER
            // -------------------------------------------------

            case "archer":
            case "archer game":
            case "🏹 archer":
                return "ArcherGame";


            // -------------------------------------------------
            // ASTEROID
            // -------------------------------------------------

            case "asteroid":
            case "asteroids":
            case "asteroid game":
            case "☄ asteroid":
                return "AsteroidGame";


            // -------------------------------------------------
            // COIN FLIP
            // -------------------------------------------------

            case "coin flip":
            case "coinflip":
            case "coin flip game":
                return "CoinFlipGame";


            // -------------------------------------------------
            // COLOR MATCH
            // -------------------------------------------------

            case "color match":
            case "colormatch":
            case "color match game":
                return "ColorMatchGame";


            // -------------------------------------------------
            // DINO
            // -------------------------------------------------

            case "dino":
            case "dino game":
            case "dinosaur":
                return "DinoGame";


            // -------------------------------------------------
            // FRUIT
            // -------------------------------------------------

            case "fruit":
            case "fruit game":
            case "fruit catcher":
            case "fruit catcher game":
                return "FruitGame";


            // -------------------------------------------------
            // 2048
            // -------------------------------------------------

            case "2048":
            case "game2048":
            case "2048 game":
                return "Game2048";


            // -------------------------------------------------
            // GUESSING
            // -------------------------------------------------

            case "guess":
            case "guess game":
            case "number guessing":
            case "number guessing game":
            case "guessing game":
                return "GuessGame";


            // -------------------------------------------------
            // LUCKY SPIN
            // -------------------------------------------------

            case "lucky spin":
            case "luckyspin":
            case "lucky spin game":
                return "LuckySpinGame";


            // -------------------------------------------------
            // PONG
            // -------------------------------------------------

            case "pong":
            case "pong game":
                return "PongGame";


            // -------------------------------------------------
            // RACING
            // -------------------------------------------------

            case "racing":
            case "racing game":
            case "car racing":
                return "RacingGame";


            // -------------------------------------------------
            // REACTION
            // -------------------------------------------------

            case "reaction":
            case "reaction game":
            case "reaction test":
                return "ReactionGame";


            // -------------------------------------------------
            // SLIDING PUZZLE
            // -------------------------------------------------

            case "sliding puzzle":
            case "sliding puzzle game":
            case "puzzle":
                return "SlidingPuzzleGame";


            // -------------------------------------------------
            // SNAKE
            // -------------------------------------------------

            case "snake":
            case "snake game":
                return "SnakeGame";


            // -------------------------------------------------
            // SUDOKU
            // -------------------------------------------------

            case "sudoku":
            case "sudoku game":
                return "SudokuGame";


            // -------------------------------------------------
            // TIC TAC TOE
            // -------------------------------------------------

            case "tic tac toe":
            case "tictactoe":
            case "tic tac toe game":
            case "xo":
            case "xo game":
                return "TicTacToeGame";


            // -------------------------------------------------
            // DEFAULT
            // -------------------------------------------------

            default:
                return null;
        }
    }


    // =========================================================
    // REFLECTION CREATOR
    // =========================================================

    private static JFrame createByReflection(
            String className,
            String playerName,
            String difficulty) {

        try {

            Class<?> clazz =
                    Class.forName(
                            className
                    );


            // =================================================
            // 1. Try String + String
            // =================================================

            try {

                Constructor<?> constructor =
                        clazz.getConstructor(
                                String.class,
                                String.class
                        );


                Object object =
                        constructor.newInstance(
                                playerName,
                                difficulty
                        );


                if (
                        object instanceof JFrame
                ) {

                    return (JFrame) object;
                }

            } catch (
                    NoSuchMethodException ignored
            ) {
                // Try next constructor
            }


            // =================================================
            // 2. Try String
            // =================================================

            try {

                Constructor<?> constructor =
                        clazz.getConstructor(
                                String.class
                        );


                Object object =
                        constructor.newInstance(
                                playerName
                        );


                if (
                        object instanceof JFrame
                ) {

                    return (JFrame) object;
                }

            } catch (
                    NoSuchMethodException ignored
            ) {
                // Try next constructor
            }


            // =================================================
            // 3. Try no-argument constructor
            // =================================================

            try {

                Constructor<?> constructor =
                        clazz.getConstructor();


                Object object =
                        constructor.newInstance();


                if (
                        object instanceof JFrame
                ) {

                    return (JFrame) object;
                }

            } catch (
                    NoSuchMethodException ignored
            ) {
                // No compatible constructor
            }


        } catch (
                ClassNotFoundException e
        ) {

            System.out.println(
                    "Game class not found: " +
                            className
            );

        } catch (
                Exception e
        ) {

            System.out.println(
                    "Could not create game: " +
                            className
            );


            e.printStackTrace();
        }


        return null;
    }


    // =========================================================
    // GET USERNAME
    // =========================================================

    private static String getUserName(
            User user) {

        if (
                user == null
        ) {

            return "Player";
        }


        /*
         * Try getUsername()
         *
         * This is compatible with the User/Player style
         * used elsewhere in the project.
         */

        try {

            java.lang.reflect.Method method =
                    user.getClass()
                            .getMethod(
                                    "getUsername"
                            );


            Object value =
                    method.invoke(
                            user
                    );


            if (
                    value != null
            ) {

                String name =
                        value.toString()
                                .trim();


                if (
                        !name.isEmpty()
                ) {

                    return name;
                }
            }

        } catch (
                Exception ignored
        ) {
            // Try getName()
        }


        // =====================================================
        // Try getName()
        // =====================================================

        try {

            java.lang.reflect.Method method =
                    user.getClass()
                            .getMethod(
                                    "getName"
                            );


            Object value =
                    method.invoke(
                            user
                    );


            if (
                    value != null
            ) {

                String name =
                        value.toString()
                                .trim();


                if (
                        !name.isEmpty()
                ) {

                    return name;
                }
            }

        } catch (
                Exception ignored
        ) {
            // Use fallback
        }


        return "Player";
    }


    // =========================================================
    // ERROR FRAME
    // =========================================================

    private static JFrame errorFrame(
            String message) {

        JFrame frame =
                new JFrame(
                        "GamesMania"
                );


        frame.setSize(
                420,
                220
        );


        frame.setLocationRelativeTo(
                null
        );


        frame.setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );


        JPanel panel =
                new JPanel(
                        new BorderLayout(
                                15,
                                15
                        )
                );


        panel.setBorder(
                BorderFactory
                        .createEmptyBorder(
                                25,
                                25,
                                25,
                                25
                        )
        );


        JLabel title =
                new JLabel(
                        "⚠ Game Error",
                        SwingConstants.CENTER
                );


        title.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        22
                )
        );


        JLabel text =
                new JLabel(
                        "<html><center>" +
                                message.replace(
                                        "\n",
                                        "<br>"
                                ) +
                                "</center></html>",
                        SwingConstants.CENTER
                );


        JButton close =
                new JButton(
                        "Close"
                );


        close.addActionListener(
                e -> frame.dispose()
        );


        panel.add(
                title,
                BorderLayout.NORTH
        );


        panel.add(
                text,
                BorderLayout.CENTER
        );


        panel.add(
                close,
                BorderLayout.SOUTH
        );


        frame.add(
                panel
        );


        return frame;
    }


    // =========================================================
    // TEST MAIN
    // =========================================================

    public static void main(
            String[] args) {

        SwingUtilities.invokeLater(
                () -> {

                    JFrame game =
                            GameFactory.createGame(
                                    "Tic Tac Toe",
                                    "Player"
                            );


                    if (
                            game != null
                    ) {

                        game.setVisible(
                                true
                        );
                    }
                }
        );
    }
}