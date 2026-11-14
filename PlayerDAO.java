import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ============================================================
 *                     GAMESMANIA
 *                      PLAYER DAO
 * ============================================================
 *
 * Handles player data in the SQLite database.
 *
 * Table:
 * players
 *
 * Columns:
 * id
 * name
 * total_score
 * games_played
 * created_at
 *
 * ============================================================
 */
public class PlayerDAO {

    // =========================================================
    // DATABASE
    // =========================================================

    private static final String DB_URL =
            "jdbc:sqlite:gamesmania.db";


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public PlayerDAO() {

        createTable();
    }


    // =========================================================
    // CONNECTION
    // =========================================================

    private Connection getConnection()
            throws SQLException {

        return DBConnection.getConnection();
    }


    // =========================================================
    // CREATE TABLE
    // =========================================================

    public void createTable() {
        boolean isMySQL = DBConnection.isUsingMySQL();
        String sql = isMySQL
                ? "CREATE TABLE IF NOT EXISTS players (" +
                  "id INT AUTO_INCREMENT PRIMARY KEY, " +
                  "name VARCHAR(100) NOT NULL UNIQUE, " +
                  "total_score INT DEFAULT 0, " +
                  "games_played INT DEFAULT 0, " +
                  "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                  ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4"
                : "CREATE TABLE IF NOT EXISTS players (" +
                  "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                  "name TEXT NOT NULL UNIQUE COLLATE NOCASE, " +
                  "total_score INTEGER DEFAULT 0, " +
                  "games_played INTEGER DEFAULT 0, " +
                  "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                  ")";


        try (
                Connection connection =
                        getConnection();

                Statement statement =
                        connection.createStatement()
        ) {

            statement.executeUpdate(
                    sql
            );

        } catch (SQLException e) {

            System.out.println(
                    "Player table error: "
                            + e.getMessage()
            );
        }
    }


    // =========================================================
    // ADD PLAYER
    // =========================================================

    public boolean addPlayer(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        if (playerExists(name)) {
            return true;
        }

        String sql = "INSERT INTO players (name, total_score, games_played) VALUES (?, 0, 0)";


        try (
                Connection connection =
                        getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(
                                sql
                        )
        ) {

            statement.setString(
                    1,
                    name.trim()
            );


            statement.executeUpdate();

            return true;

        } catch (SQLException e) {

            System.out.println(
                    "Could not add player: "
                            + e.getMessage()
            );

            return false;
        }
    }


    // =========================================================
    // GET OR CREATE PLAYER
    // =========================================================

    /**
     * Useful for LoginFrame.
     *
     * If player already exists, their old data remains.
     * If player is new, a new record is created.
     */
    public boolean getOrCreatePlayer(
            String name) {

        if (name == null ||
                name.trim().isEmpty()) {

            return false;
        }


        if (playerExists(name)) {

            return true;
        }


        return addPlayer(name);
    }


    // =========================================================
    // PLAYER EXISTS
    // =========================================================

    public boolean playerExists(
            String name) {

        if (name == null ||
                name.trim().isEmpty()) {

            return false;
        }


        String sql =
                "SELECT id FROM players WHERE LOWER(name) = LOWER(?)";


        try (
                Connection connection =
                        getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(
                                sql
                        )
        ) {

            statement.setString(
                    1,
                    name.trim()
            );


            try (
                    ResultSet result =
                            statement.executeQuery()
            ) {

                return result.next();
            }

        } catch (SQLException e) {

            System.out.println(
                    "Player check error: "
                            + e.getMessage()
            );

            return false;
        }
    }


    // =========================================================
    // UPDATE SCORE
    // =========================================================

    /**
     * Records a game played and updates total score and games played.
     */
    public void recordGame(String name, int score) {
        addScore(name, score);
    }

    /**
     * Adds a score to the player's total score.
     */
    public void addScore(
            String name,
            int score) {

        if (name == null ||
                name.trim().isEmpty()) {

            return;
        }


        getOrCreatePlayer(
                name
        );


        String sql =
                "UPDATE players " +
                        "SET total_score = total_score + ?, " +
                        "games_played = games_played + 1 " +
                        "WHERE LOWER(name) = LOWER(?)";


        try (
                Connection connection =
                        getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(
                                sql
                        )
        ) {

            statement.setInt(
                    1,
                    Math.max(
                            0,
                            score
                    )
            );

            statement.setString(
                    2,
                    name.trim()
            );


            statement.executeUpdate();

        } catch (SQLException e) {

            System.out.println(
                    "Score update error: "
                            + e.getMessage()
            );
        }
    }


    // =========================================================
    // SET TOTAL SCORE
    // =========================================================

    public void setScore(
            String name,
            int score) {

        if (name == null ||
                name.trim().isEmpty()) {

            return;
        }


        getOrCreatePlayer(
                name
        );


        String sql =
                "UPDATE players " +
                        "SET total_score = ? " +
                        "WHERE name = ?";


        try (
                Connection connection =
                        getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(
                                sql
                        )
        ) {

            statement.setInt(
                    1,
                    Math.max(
                            0,
                            score
                    )
            );

            statement.setString(
                    2,
                    name.trim()
            );


            statement.executeUpdate();

        } catch (SQLException e) {

            System.out.println(
                    "Could not set score: "
                            + e.getMessage()
            );
        }
    }


    // =========================================================
    // GET SCORE
    // =========================================================

    public int getScore(
            String name) {

        if (name == null ||
                name.trim().isEmpty()) {

            return 0;
        }


        String sql =
                "SELECT total_score " +
                        "FROM players " +
                        "WHERE LOWER(name) = LOWER(?)";


        try (
                Connection connection =
                        getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(
                                sql
                        )
        ) {

            statement.setString(
                    1,
                    name.trim()
            );


            try (
                    ResultSet result =
                            statement.executeQuery()
            ) {

                if (result.next()) {

                    return result.getInt(
                            "total_score"
                    );
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Could not get score: "
                            + e.getMessage()
            );
        }


        return 0;
    }


    // =========================================================
    // GET GAMES PLAYED
    // =========================================================

    public int getGamesPlayed(
            String name) {

        if (name == null ||
                name.trim().isEmpty()) {

            return 0;
        }


        String sql =
                "SELECT games_played " +
                        "FROM players " +
                        "WHERE LOWER(name) = LOWER(?)";


        try (
                Connection connection =
                        getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(
                                sql
                        )
        ) {

            statement.setString(
                    1,
                    name.trim()
            );


            try (
                    ResultSet result =
                            statement.executeQuery()
            ) {

                if (result.next()) {

                    return result.getInt(
                            "games_played"
                    );
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Could not get games played: "
                            + e.getMessage()
            );
        }


        return 0;
    }


    // =========================================================
    // GET PLAYER ID
    // =========================================================

    public int getPlayerId(
            String name) {

        if (name == null ||
                name.trim().isEmpty()) {

            return -1;
        }


        String sql =
                "SELECT id FROM players " +
                        "WHERE name = ?";


        try (
                Connection connection =
                        getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(
                                sql
                        )
        ) {

            statement.setString(
                    1,
                    name.trim()
            );


            try (
                    ResultSet result =
                            statement.executeQuery()
            ) {

                if (result.next()) {

                    return result.getInt(
                            "id"
                    );
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Could not get player ID: "
                            + e.getMessage()
            );
        }


        return -1;
    }


    // =========================================================
    // GET ALL PLAYERS
    // =========================================================

    public List<String> getAllPlayers() {

        List<String> players =
                new ArrayList<>();


        String sql =
                "SELECT name FROM players " +
                        "ORDER BY name ASC";


        try (
                Connection connection =
                        getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(
                                sql
                        );

                ResultSet result =
                        statement.executeQuery()
        ) {

            while (result.next()) {

                players.add(
                        result.getString(
                                "name"
                        )
                );
            }

        } catch (SQLException e) {

            System.out.println(
                    "Could not load players: "
                            + e.getMessage()
            );
        }


        return players;
    }


    // =========================================================
    // GET PLAYER COUNT
    // =========================================================

    public int getPlayerCount() {

        String sql =
                "SELECT COUNT(*) FROM players";


        try (
                Connection connection =
                        getConnection();

                Statement statement =
                        connection.createStatement();

                ResultSet result =
                        statement.executeQuery(
                                sql
                        )
        ) {

            if (result.next()) {

                return result.getInt(
                        1
                );
            }

        } catch (SQLException e) {

            System.out.println(
                    "Could not count players: "
                            + e.getMessage()
            );
        }


        return 0;
    }


    // =========================================================
    // DELETE PLAYER
    // =========================================================

    public boolean deletePlayer(
            String name) {

        if (name == null ||
                name.trim().isEmpty()) {

            return false;
        }


        String sql =
                "DELETE FROM players " +
                        "WHERE name = ?";


        try (
                Connection connection =
                        getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(
                                sql
                        )
        ) {

            statement.setString(
                    1,
                    name.trim()
            );


            return statement.executeUpdate() > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Could not delete player: "
                            + e.getMessage()
            );

            return false;
        }
    }


    // =========================================================
    // CLOSE / RESET DATABASE
    // =========================================================

    /**
     * Deletes all player records.
     *
     * Use carefully.
     */
    public void clearPlayers() {

        String sql =
                "DELETE FROM players";


        try (
                Connection connection =
                        getConnection();

                Statement statement =
                        connection.createStatement()
        ) {

            statement.executeUpdate(
                    sql
            );

        } catch (SQLException e) {

            System.out.println(
                    "Could not clear players: "
                            + e.getMessage()
            );
        }
    }


    // =========================================================
    // TEST
    // =========================================================

    public static void main(
            String[] args) {

        PlayerDAO dao =
                new PlayerDAO();


        dao.getOrCreatePlayer(
                "Rishabh"
        );


        dao.addScore(
                "Rishabh",
                500
        );


        System.out.println(
                "Player: Rishabh"
        );


        System.out.println(
                "Score: "
                        + dao.getScore(
                        "Rishabh"
                )
        );


        System.out.println(
                "Games: "
                        + dao.getGamesPlayed(
                        "Rishabh"
                )
        );


        System.out.println(
                "Players: "
                        + dao.getPlayerCount()
        );
    }
}