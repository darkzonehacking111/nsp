import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * ============================================================
 *                       GAMESMANIA
 *                        SCORE DAO
 * ============================================================
 *
 * Handles saving and retrieving scores from the database.
 * Supports both MySQL and SQLite seamlessly.
 *
 * ============================================================
 */
public class ScoreDAO {

    public interface ScoreListener {
        void onScoreSaved(String username, String gameName, int score);
    }

    private static final java.util.List<ScoreListener> listeners = new java.util.concurrent.CopyOnWriteArrayList<>();

    public static void addScoreListener(ScoreListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public static void removeScoreListener(ScoreListener listener) {
        listeners.remove(listener);
    }

    private static void notifyScoreListeners(String username, String gameName, int score) {
        for (ScoreListener l : listeners) {
            try {
                l.onScoreSaved(username, gameName, score);
            } catch (Exception ignored) {}
        }
    }

    // =========================================================
    // SAVE SCORE (PRIMARY METHOD)
    // =========================================================

    public boolean saveScore(String username,
                             String gameName,
                             String difficulty,
                             int score) {

        if (username == null || username.trim().isEmpty()) {
            username = "Guest";
        } else {
            username = username.trim();
        }

        if (gameName == null || gameName.trim().isEmpty()) {
            gameName = "Unknown Game";
        } else {
            gameName = gameName.trim();
        }

        if (difficulty == null || difficulty.trim().isEmpty()) {
            difficulty = "Normal";
        }

        int finalScore = Math.max(0, score);
        boolean saved = false;

        String sql = """
                INSERT INTO scores (username, game_name, difficulty, score)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, gameName);
            ps.setString(3, difficulty);
            ps.setInt(4, finalScore);

            ps.executeUpdate();
            saved = true;
            System.out.println("[ScoreDAO] Saved score: " + username + " | " + gameName + " | " + finalScore);

        } catch (SQLException e) {
            System.err.println("[ScoreDAO] Error saving score: " + e.getMessage());
            e.printStackTrace();
        }

        // =========================================================
        // GUARANTEED DATABASE UPDATES ON EVERY SINGLE ENTRY:
        // 1. Update User Profile in `users` (total_score, games_played, games_won)
        // 2. Update Player Stats in `players` (total_score, games_played)
        // 3. Notify live listeners (HUD, HomeFrame, Leaderboard)
        // =========================================================
        try {
            UserDAO userDAO = new UserDAO();
            userDAO.recordGame(username, finalScore, finalScore > 0);
        } catch (Exception ex) {
            System.err.println("[ScoreDAO] Error updating User stats: " + ex.getMessage());
        }

        try {
            PlayerDAO playerDAO = new PlayerDAO();
            playerDAO.getOrCreatePlayer(username);
            playerDAO.recordGame(username, finalScore);
        } catch (Exception ex) {
            System.err.println("[ScoreDAO] Error updating Player stats: " + ex.getMessage());
        }

        notifyScoreListeners(username, gameName, finalScore);
        return saved;
    }

    // =========================================================
    // SAVE SCORE - COMPATIBILITY OVERLOADS
    // =========================================================

    public boolean save(int score,
                        String gameName,
                        int userId,
                        String difficulty) {
        return saveScore("Guest", gameName, difficulty, score);
    }

    public boolean save(int score,
                        String gameName,
                        int userId,
                        String difficulty,
                        String username) {
        return saveScore(username, gameName, difficulty, score);
    }

    public boolean saveScore(int score,
                             String gameName,
                             int difficulty,
                             String playerName) {
        String diffStr = switch (difficulty) {
            case 0 -> "Easy";
            case 2 -> "Hard";
            default -> "Normal";
        };
        return saveScore(playerName, gameName, diffStr, score);
    }

    // =========================================================
    // GET ALL SCORES
    // =========================================================

    public List<String> getAllScores() {
        List<String> scores = new ArrayList<>();
        String sql = """
                SELECT username, game_name, difficulty, score, played_at
                FROM scores
                ORDER BY score DESC
                """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String username = rs.getString("username");
                String gameName = rs.getString("game_name");
                String difficulty = rs.getString("difficulty");
                int score = rs.getInt("score");
                String dateStr = "";
                try {
                    Timestamp ts = rs.getTimestamp("played_at");
                    if (ts != null) dateStr = ts.toString();
                } catch (Exception e) {
                    dateStr = rs.getString("played_at");
                }

                scores.add(username + " | " + gameName + " | " + difficulty + " | " + score + " | " + dateStr);
            }

        } catch (SQLException e) {
            System.err.println("[ScoreDAO] Error loading scores: " + e.getMessage());
        }

        return scores;
    }

    // =========================================================
    // GET TOP SCORES AS STRINGS
    // =========================================================

    public List<String> getTopScores(int limit) {
        List<String> scores = new ArrayList<>();
        String sql = """
                SELECT username, game_name, difficulty, score
                FROM scores
                ORDER BY score DESC
                LIMIT ?
                """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, Math.max(1, limit));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    scores.add(
                            rs.getString("username") + " | " +
                            rs.getString("game_name") + " | " +
                            rs.getString("difficulty") + " | " +
                            rs.getInt("score")
                    );
                }
            }

        } catch (SQLException e) {
            System.err.println("[ScoreDAO] Error loading top scores: " + e.getMessage());
        }

        return scores;
    }

    // =========================================================
    // GET TOP SCORES AS SCORE OBJECTS
    // =========================================================

    public List<Score> getTopScoreObjects(int limit) {
        List<Score> scores = new ArrayList<>();
        String sql = """
                SELECT id, username, game_name, difficulty, score, played_at
                FROM scores
                ORDER BY score DESC
                LIMIT ?
                """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, Math.max(1, limit));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String username = rs.getString("username");
                    String gameName = rs.getString("game_name");
                    String difficulty = rs.getString("difficulty");
                    int score = rs.getInt("score");

                    LocalDateTime dt = LocalDateTime.now();
                    try {
                        Timestamp ts = rs.getTimestamp("played_at");
                        if (ts != null) dt = ts.toLocalDateTime();
                    } catch (Exception ignored) {
                    }

                    scores.add(new Score(id, username, gameName, difficulty, score, dt));
                }
            }

        } catch (SQLException e) {
            System.err.println("[ScoreDAO] Error loading score objects: " + e.getMessage());
        }

        return scores;
    }

    // =========================================================
    // GET SCORES FOR A GAME
    // =========================================================

    public List<String> getScoresByGame(String gameName) {
        List<String> scores = new ArrayList<>();
        String sql = """
                SELECT username, game_name, difficulty, score, played_at
                FROM scores
                WHERE game_name = ?
                ORDER BY score DESC
                """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, gameName);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    scores.add(
                            rs.getString("username") + " | " +
                            rs.getString("game_name") + " | " +
                            rs.getString("difficulty") + " | " +
                            rs.getInt("score")
                    );
                }
            }

        } catch (SQLException e) {
            System.err.println("[ScoreDAO] Error loading game scores: " + e.getMessage());
        }

        return scores;
    }

    // =========================================================
    // DELETE ALL SCORES
    // =========================================================

    public boolean deleteAllScores() {
        String sql = "DELETE FROM scores";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.executeUpdate();
            System.out.println("[ScoreDAO] All scores deleted.");
            return true;

        } catch (SQLException e) {
            System.err.println("[ScoreDAO] Error deleting scores: " + e.getMessage());
            return false;
        }
    }
}