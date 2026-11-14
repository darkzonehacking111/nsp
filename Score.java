import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ============================================================
 *                     GAMESMANIA
 *                        SCORE
 * ============================================================
 *
 * Stores one game score.
 *
 * Used by:
 *      ScoreDAO
 *      LeaderBoardFrame
 *      Game classes
 *      SQL database
 *
 * ============================================================
 */
public class Score {

    // =========================================================
    // VARIABLES
    // =========================================================

    private int id;

    private String playerName;

    private String gameName;

    private String difficulty;

    private int score;

    private LocalDateTime playedAt;


    // =========================================================
    // DEFAULT CONSTRUCTOR
    // =========================================================

    public Score() {

        this.playedAt =
                LocalDateTime.now();
    }


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public Score(
            String playerName,
            String gameName,
            String difficulty,
            int score) {

        this.playerName =
                playerName;

        this.gameName =
                gameName;

        this.difficulty =
                difficulty;

        this.score =
                score;

        this.playedAt =
                LocalDateTime.now();
    }


    // =========================================================
    // FULL CONSTRUCTOR
    // =========================================================

    public Score(
            int id,
            String playerName,
            String gameName,
            String difficulty,
            int score,
            LocalDateTime playedAt) {

        this.id =
                id;

        this.playerName =
                playerName;

        this.gameName =
                gameName;

        this.difficulty =
                difficulty;

        this.score =
                score;

        this.playedAt =
                playedAt;
    }


    // =========================================================
    // GET ID
    // =========================================================

    public int getId() {

        return id;
    }


    // =========================================================
    // SET ID
    // =========================================================

    public void setId(
            int id) {

        this.id =
                id;
    }


    // =========================================================
    // GET PLAYER NAME
    // =========================================================

    public String getPlayerName() {

        return playerName;
    }


    // =========================================================
    // SET PLAYER NAME
    // =========================================================

    public void setPlayerName(
            String playerName) {

        this.playerName =
                playerName;
    }


    // =========================================================
    // GET GAME NAME
    // =========================================================

    public String getGameName() {

        return gameName;
    }


    // =========================================================
    // SET GAME NAME
    // =========================================================

    public void setGameName(
            String gameName) {

        this.gameName =
                gameName;
    }


    // =========================================================
    // GET DIFFICULTY
    // =========================================================

    public String getDifficulty() {

        return difficulty;
    }


    // =========================================================
    // SET DIFFICULTY
    // =========================================================

    public void setDifficulty(
            String difficulty) {

        this.difficulty =
                difficulty;
    }


    // =========================================================
    // GET SCORE
    // =========================================================

    public int getScore() {

        return score;
    }


    // =========================================================
    // SET SCORE
    // =========================================================

    public void setScore(
            int score) {

        this.score =
                Math.max(
                        0,
                        score
                );
    }


    // =========================================================
    // GET PLAYED AT
    // =========================================================

    public LocalDateTime getPlayedAt() {

        return playedAt;
    }


    // =========================================================
    // SET PLAYED AT
    // =========================================================

    public void setPlayedAt(
            LocalDateTime playedAt) {

        this.playedAt =
                playedAt;
    }


    // =========================================================
    // FORMATTED DATE
    // =========================================================

    public String getFormattedDate() {

        if (playedAt == null) {

            return "";
        }


        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern(
                        "dd-MM-yyyy HH:mm"
                );


        return playedAt.format(
                formatter
        );
    }


    // =========================================================
    // SHORT DATE
    // =========================================================

    public String getShortDate() {

        if (playedAt == null) {

            return "";
        }


        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern(
                        "dd MMM yyyy"
                );


        return playedAt.format(
                formatter
        );
    }


    // =========================================================
    // SCORE DISPLAY
    // =========================================================

    public String getScoreText() {

        return String.valueOf(
                score
        );
    }


    // =========================================================
    // VALIDATION
    // =========================================================

    public boolean isValid() {

        return playerName != null
                && !playerName.trim().isEmpty()

                && gameName != null
                && !gameName.trim().isEmpty()

                && difficulty != null
                && !difficulty.trim().isEmpty()

                && score >= 0;
    }


    // =========================================================
    // TO STRING
    // =========================================================

    @Override
    public String toString() {

        return "Score{" +

                "id=" +
                id +

                ", playerName='" +
                playerName +
                '\'' +

                ", gameName='" +
                gameName +
                '\'' +

                ", difficulty='" +
                difficulty +
                '\'' +

                ", score=" +
                score +

                ", playedAt=" +
                playedAt +

                '}';
    }
}