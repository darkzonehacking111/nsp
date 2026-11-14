import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ============================================================
 *                       GAMESMANIA
 *                         USER DAO
 * ============================================================
 *
 * Handles database operations for Users.
 *
 * Database table:
 *
 * users
 * ------------------------------------------------------------
 * id
 * name
 * total_score
 * games_played
 * games_won
 *
 * ============================================================
 */
public class UserDAO {

    // =========================================================
    // GET USER BY NAME
    // =========================================================

    public User getUserByName(String name) {

        String sql =
                "SELECT id, name, total_score, games_played, games_won " +
                        "FROM users WHERE LOWER(name) = LOWER(?)";

        try (
                Connection con =
                        DBConnection.getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql)
        ) {

            ps.setString(
                    1,
                    name
            );

            ResultSet rs =
                    ps.executeQuery();

            if (rs.next()) {

                return new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("total_score"),
                        rs.getInt("games_played"),
                        rs.getInt("games_won")
                );
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error finding user."
            );

            e.printStackTrace();
        }

        return null;
    }


    // =========================================================
    // CREATE USER
    // =========================================================

    public boolean createUser(User user) {

        String sql =
                "INSERT INTO users " +
                        "(name, total_score, games_played, games_won) " +
                        "VALUES (?, ?, ?, ?)";

        try (
                Connection con =
                        DBConnection.getConnection();

                PreparedStatement ps =
                        con.prepareStatement(
                                sql,
                                Statement.RETURN_GENERATED_KEYS
                        )
        ) {

            ps.setString(
                    1,
                    user.getName()
            );

            ps.setInt(
                    2,
                    user.getTotalScore()
            );

            ps.setInt(
                    3,
                    user.getGamesPlayed()
            );

            ps.setInt(
                    4,
                    user.getGamesWon()
            );


            int rows =
                    ps.executeUpdate();


            if (rows > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        user.setId(keys.getInt(1));
                    }
                } catch (Exception ignored) {}

                if (user.getId() <= 0) {
                    User fetched = getUserByName(user.getName());
                    if (fetched != null) {
                        user.setId(fetched.getId());
                    }
                }

                return true;
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error creating user."
            );

            e.printStackTrace();
        }

        return false;
    }


    // =========================================================
    // LOGIN / GET OR CREATE USER
    // =========================================================
    //
    // Since GamesMania does NOT use passwords,
    // entering a name is enough.
    //
    // If the player already exists:
    //      return existing player
    //
    // Otherwise:
    //      create a new player
    //
    // =========================================================

    public User loginOrCreate(
            String name) {

        if (
                name == null ||
                        name.trim().isEmpty()
        ) {

            return null;
        }


        name =
                name.trim();


        User existingUser =
                getUserByName(name);


        if (
                existingUser != null
        ) {

            return existingUser;
        }


        User newUser =
                new User(name);


        if (
                createUser(newUser)
        ) {

            return newUser;
        }


        return null;
    }


    // =========================================================
    // UPDATE USER
    // =========================================================

    public boolean updateUser(
            User user) {

        if (
                user == null ||
                        user.getId() <= 0
        ) {

            return false;
        }


        String sql =
                "UPDATE users SET " +
                        "name = ?, " +
                        "total_score = ?, " +
                        "games_played = ?, " +
                        "games_won = ? " +
                        "WHERE id = ?";


        try (
                Connection con =
                        DBConnection.getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql)
        ) {

            ps.setString(
                    1,
                    user.getName()
            );

            ps.setInt(
                    2,
                    user.getTotalScore()
            );

            ps.setInt(
                    3,
                    user.getGamesPlayed()
            );

            ps.setInt(
                    4,
                    user.getGamesWon()
            );

            ps.setInt(
                    5,
                    user.getId()
            );


            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Error updating user."
            );

            e.printStackTrace();
        }


        return false;
    }


    // =========================================================
    // UPDATE SCORE
    // =========================================================

    public boolean updateScore(
            int userId,
            int score) {

        String sql =
                "UPDATE users " +
                        "SET total_score = total_score + ? " +
                        "WHERE id = ?";


        try (
                Connection con =
                        DBConnection.getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql)
        ) {

            ps.setInt(
                    1,
                    Math.max(
                            0,
                            score
                    )
            );

            ps.setInt(
                    2,
                    userId
            );


            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Error updating score."
            );

            e.printStackTrace();
        }


        return false;
    }


    // =========================================================
    // RECORD GAME
    // =========================================================
    //
    // Call this after a game finishes.
    //
    // score  = score earned in that game
    // won    = true if player won
    //
    // =========================================================

    public boolean recordGame(
            int userId,
            int score,
            boolean won) {

        String sql =
                "UPDATE users SET " +
                        "total_score = total_score + ?, " +
                        "games_played = games_played + 1, " +
                        "games_won = games_won + ? " +
                        "WHERE id = ?";


        try (
                Connection con =
                        DBConnection.getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql)
        ) {

            ps.setInt(
                    1,
                    Math.max(
                            0,
                            score
                    )
            );

            ps.setInt(
                    2,
                    won ? 1 : 0
            );

            ps.setInt(
                    3,
                    userId
            );


            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Error recording game: " + e.getMessage()
            );

            e.printStackTrace();
        }

        return false;
    }

    /**
     * Record a game by username directly.
     * Ensures user is created if not yet in database and updates total_score, games_played, games_won.
     */
    public boolean recordGame(String username, int score, boolean won) {
        if (username == null || username.trim().isEmpty()) {
            username = "Guest";
        }
        User user = loginOrCreate(username.trim());
        if (user != null) {
            return recordGame(user.getId(), score, won);
        }
        return false;
    }


    // =========================================================
    // CHECK USER EXISTS
    // =========================================================

    public boolean userExists(
            String name) {

        String sql =
                "SELECT id FROM users WHERE LOWER(name) = LOWER(?)";


        try (
                Connection con =
                        DBConnection.getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql)
        ) {

            ps.setString(
                    1,
                    name
            );


            ResultSet rs =
                    ps.executeQuery();


            return rs.next();

        } catch (SQLException e) {

            System.out.println(
                    "Error checking user."
            );

            e.printStackTrace();
        }


        return false;
    }


    // =========================================================
    // GET ALL USERS
    // =========================================================

    public List<User> getAllUsers() {

        List<User> users =
                new ArrayList<>();


        String sql =
                "SELECT id, name, total_score, " +
                        "games_played, games_won " +
                        "FROM users " +
                        "ORDER BY total_score DESC";


        try (
                Connection con =
                        DBConnection.getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql);

                ResultSet rs =
                        ps.executeQuery()
        ) {

            while (
                    rs.next()
            ) {

                User user =
                        new User(
                                rs.getInt("id"),
                                rs.getString("name"),
                                rs.getInt("total_score"),
                                rs.getInt("games_played"),
                                rs.getInt("games_won")
                        );


                users.add(
                        user
                );
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error loading users."
            );

            e.printStackTrace();
        }


        return users;
    }


    // =========================================================
    // DELETE USER
    // =========================================================

    public boolean deleteUser(
            int userId) {

        String sql =
                "DELETE FROM users WHERE id = ?";


        try (
                Connection con =
                        DBConnection.getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql)
        ) {

            ps.setInt(
                    1,
                    userId
            );


            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Error deleting user."
            );

            e.printStackTrace();
        }


        return false;
    }


    // =========================================================
    // RESET USER STATS
    // =========================================================

    public boolean resetStats(
            int userId) {

        String sql =
                "UPDATE users SET " +
                        "total_score = 0, " +
                        "games_played = 0, " +
                        "games_won = 0 " +
                        "WHERE id = ?";


        try (
                Connection con =
                        DBConnection.getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql)
        ) {

            ps.setInt(
                    1,
                    userId
            );


            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Error resetting statistics."
            );

            e.printStackTrace();
        }


        return false;
    }
}