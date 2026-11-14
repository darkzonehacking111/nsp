/**
 * ============================================================
 *                       GAMESMANIA
 *                         USER CLASS
 * ============================================================
 *
 * Stores player information used throughout the application.
 *
 * Login:
 *     Name only
 *
 * Used by:
 *     LoginFrame
 *     HomeFrame
 *     GameFrame
 *     LeaderBoardFrame
 *     ScoreDAO
 *     PlayerDAO
 *
 * ============================================================
 */
public class User {

    // =========================================================
    // USER DATA
    // =========================================================

    private int id;

    private String name;

    private int totalScore;

    private int gamesPlayed;

    private int gamesWon;


    // =========================================================
    // DEFAULT CONSTRUCTOR
    // =========================================================

    public User() {

        this.id = 0;

        this.name = "";

        this.totalScore = 0;

        this.gamesPlayed = 0;

        this.gamesWon = 0;
    }


    // =========================================================
    // NAME CONSTRUCTOR
    // =========================================================

    public User(String name) {

        this();

        setName(name);
    }


    // =========================================================
    // FULL CONSTRUCTOR
    // =========================================================

    public User(
            int id,
            String name,
            int totalScore,
            int gamesPlayed,
            int gamesWon) {

        this.id = id;

        this.name = name;

        this.totalScore = totalScore;

        this.gamesPlayed = gamesPlayed;

        this.gamesWon = gamesWon;
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

    public void setId(int id) {

        this.id = id;
    }


    // =========================================================
    // GET NAME
    // =========================================================

    public String getName() {

        return name;
    }


    // =========================================================
    // SET NAME
    // =========================================================

    public void setName(String name) {

        if (name == null) {

            this.name = "";

        } else {

            this.name =
                    name.trim();
        }
    }


    // =========================================================
    // GET TOTAL SCORE
    // =========================================================

    public int getTotalScore() {

        return totalScore;
    }


    // =========================================================
    // SET TOTAL SCORE
    // =========================================================

    public void setTotalScore(
            int totalScore) {

        this.totalScore =
                Math.max(
                        0,
                        totalScore
                );
    }


    // =========================================================
    // ADD SCORE
    // =========================================================

    public void addScore(
            int score) {

        if (score > 0) {

            this.totalScore +=
                    score;
        }
    }


    // =========================================================
    // GET GAMES PLAYED
    // =========================================================

    public int getGamesPlayed() {

        return gamesPlayed;
    }


    // =========================================================
    // SET GAMES PLAYED
    // =========================================================

    public void setGamesPlayed(
            int gamesPlayed) {

        this.gamesPlayed =
                Math.max(
                        0,
                        gamesPlayed
                );
    }


    // =========================================================
    // ADD GAME
    // =========================================================

    public void addGamePlayed() {

        gamesPlayed++;
    }


    // =========================================================
    // GET GAMES WON
    // =========================================================

    public int getGamesWon() {

        return gamesWon;
    }


    // =========================================================
    // SET GAMES WON
    // =========================================================

    public void setGamesWon(
            int gamesWon) {

        this.gamesWon =
                Math.max(
                        0,
                        gamesWon
                );
    }


    // =========================================================
    // ADD WIN
    // =========================================================

    public void addWin() {

        gamesWon++;
    }


    // =========================================================
    // WIN RATE
    // =========================================================

    public double getWinRate() {

        if (gamesPlayed == 0) {

            return 0.0;
        }


        return (
                gamesWon * 100.0
        ) /
                gamesPlayed;
    }


    // =========================================================
    // RESET STATS
    // =========================================================

    public void resetStats() {

        totalScore =
                0;

        gamesPlayed =
                0;

        gamesWon =
                0;
    }


    // =========================================================
    // TO STRING
    // =========================================================

    @Override
    public String toString() {

        return "User{" +

                "id=" +
                id +

                ", name='" +
                name +
                '\'' +

                ", totalScore=" +
                totalScore +

                ", gamesPlayed=" +
                gamesPlayed +

                ", gamesWon=" +
                gamesWon +

                '}';
    }
}