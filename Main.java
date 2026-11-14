import javax.swing.*;

/**
 * ============================================================
 *                         GAMESMANIA
 *                         MAIN CLASS
 * ============================================================
 *
 * Main entry point for the complete GamesMania application.
 *
 * Application flow:
 *
 * Main
 *   ↓
 * SplashScreen
 *   ↓
 * LoginFrame
 *   ↓
 * HomeFrame
 *   ↓
 * GameFrame
 *   ↓
 * Individual Games
 *
 * No password is required.
 * The player only enters their name.
 *
 * ============================================================
 */
public class Main {

    public static void main(String[] args) {

        /*
         * Use the system's native look and feel.
         * This keeps Swing controls looking clean and natural.
         */
        try {

            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName()
            );

        } catch (Exception e) {

            System.out.println(
                    "Could not load system look and feel."
            );
        }


        /*
         * Start the application on
         * Swing's Event Dispatch Thread.
         */
        SwingUtilities.invokeLater(() -> {

            try {

                /*
                 * Start GamesMania with
                 * the animated splash screen.
                 *
                 * SplashScreen will automatically
                 * open LoginFrame when loading finishes.
                 */
                SplashScreen splash =
                        new SplashScreen();

                splash.setVisible(true);


            } catch (Exception e) {

                e.printStackTrace();

                /*
                 * If SplashScreen fails,
                 * open LoginFrame directly.
                 */
                try {

                    LoginFrame login =
                            new LoginFrame();

                    login.setVisible(true);

                } catch (Exception loginError) {

                    loginError.printStackTrace();

                    JOptionPane.showMessageDialog(
                            null,
                            "GamesMania could not start.\n\n"
                                    + loginError.getMessage(),
                            "GamesMania",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });
    }
}