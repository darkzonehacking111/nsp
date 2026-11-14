import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * ============================================================
 *                       GAMESMANIA
 *                  DATABASE CONNECTION
 * ============================================================
 *
 * Robust, resilient database manager that supports:
 * 1. MySQL (auto-creates database 'gamesmania' & tables)
 * 2. SQLite local database (gamesmania.db) as seamless fallback
 * 3. Configuration via db.properties file
 *
 * ============================================================
 */
public class DBConnection {

    private static String dbType = "auto";
    private static String host = "localhost";
    private static int port = 3306;
    private static String dbName = "gamesmania";
    private static String user = "root";
    private static String password = "";
    private static String sqliteFile = "gamesmania.db";



    // Load settings from db.properties if available
    static {
        loadConfig();
    }

    private DBConnection() {
    }

    private static void loadConfig() {
        Properties props = new Properties();
        File configFile = new File("db.properties");

        if (configFile.exists()) {
            try (InputStream in = new FileInputStream(configFile)) {
                props.load(in);
                dbType = props.getProperty("db.type", dbType).trim();
                host = props.getProperty("db.host", host).trim();
                port = Integer.parseInt(props.getProperty("db.port", String.valueOf(port)).trim());
                dbName = props.getProperty("db.name", dbName).trim();
                user = props.getProperty("db.user", user).trim();
                password = props.getProperty("db.password", password);
                sqliteFile = props.getProperty("db.sqlite.file", sqliteFile).trim();
            } catch (Exception e) {
                System.out.println("[DB] Using default configuration: " + e.getMessage());
            }
        }
    }

    private static volatile boolean isUsingMySQL = false;
    private static volatile String activeDatabase = "Unknown";
    private static volatile boolean initialized = false;
    private static final Object INIT_LOCK = new Object();

    /**
     * Get a valid, open SQL database connection.
     * Thread-safe and designed to work cleanly with try-with-resources.
     * Uses MySQL if available, with seamless immediate fallback to local SQLite WAL database.
     */
    public static Connection getConnection() {
        ensureInitialized();

        if (isUsingMySQL) {
            Connection con = tryMySQLConnection();
            if (con != null) return con;
        }

        return trySQLiteConnection();
    }

    private static void ensureInitialized() {
        if (initialized) return;
        synchronized (INIT_LOCK) {
            if (initialized) return;

            // Attempt MySQL if mode is 'auto' or 'mysql'
            if ("auto".equalsIgnoreCase(dbType) || "mysql".equalsIgnoreCase(dbType)) {
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    String serverUrl = "jdbc:mysql://" + host + ":" + port
                            + "/?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&connectTimeout=1000&socketTimeout=1500";
                    try (Connection serverCon = DriverManager.getConnection(serverUrl, user, password);
                         Statement stmt = serverCon.createStatement()) {
                        stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS `" + dbName + "` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci");
                    } catch (Exception ignored) {}

                    Connection mysqlCon = tryMySQLConnection();
                    if (mysqlCon != null) {
                        try {
                            initTables(mysqlCon, true);
                            isUsingMySQL = true;
                            activeDatabase = "MySQL (" + dbName + ")";
                            initialized = true;
                            System.out.println("[DB] Connected to MySQL database '" + dbName + "' successfully.");
                            return;
                        } finally {
                            try { mysqlCon.close(); } catch (Exception ignored) {}
                        }
                    }
                } catch (Exception e) {
                    System.out.println("[DB] MySQL connection not available (" + e.getMessage() + "). Switching to SQLite...");
                }
            }

            // Fallback to SQLite
            Connection sqliteCon = trySQLiteConnection();
            if (sqliteCon != null) {
                try {
                    initTables(sqliteCon, false);
                    isUsingMySQL = false;
                    activeDatabase = "SQLite (" + sqliteFile + ")";
                    initialized = true;
                    System.out.println("[DB] Connected to local SQLite database '" + sqliteFile + "' successfully.");
                } finally {
                    try { sqliteCon.close(); } catch (Exception ignored) {}
                }
            }
        }
    }

    /**
     * Attempt MySQL connection with fast socket/connect timeout.
     */
    private static Connection tryMySQLConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String dbUrl = "jdbc:mysql://" + host + ":" + port + "/" + dbName
                    + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&connectTimeout=1000&socketTimeout=1500";
            return DriverManager.getConnection(dbUrl, user, password);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Attempt SQLite connection with WAL mode and 5s busy timeout.
     */
    private static Connection trySQLiteConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            String sqliteUrl = "jdbc:sqlite:" + sqliteFile;
            Connection con = DriverManager.getConnection(sqliteUrl);
            try (Statement pragma = con.createStatement()) {
                pragma.execute("PRAGMA journal_mode=WAL;");
                pragma.execute("PRAGMA busy_timeout=5000;");
                pragma.execute("PRAGMA synchronous=NORMAL;");
            } catch (SQLException ignored) {}
            return con;
        } catch (Exception e) {
            System.err.println("[DB] SQLite connection failed: " + e.getMessage());
            return null;
        }
    }

    /**
     * Automatically create required tables (users, scores, players) if they do not exist.
     */
    private static void initTables(Connection con, boolean isMySQL) {
        if (con == null) return;

        try (Statement stmt = con.createStatement()) {
            if (isMySQL) {
                // MySQL schema
                stmt.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS users (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(100) NOT NULL UNIQUE,
                        total_score INT DEFAULT 0,
                        games_played INT DEFAULT 0,
                        games_won INT DEFAULT 0,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
                """);

                stmt.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS scores (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        username VARCHAR(100) NOT NULL,
                        game_name VARCHAR(100) NOT NULL,
                        difficulty VARCHAR(50) DEFAULT 'Normal',
                        score INT DEFAULT 0,
                        played_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
                """);

                stmt.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS players (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(100) NOT NULL UNIQUE,
                        total_score INT DEFAULT 0,
                        games_played INT DEFAULT 0,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
                """);
            } else {
                // SQLite schema
                stmt.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS users (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        name TEXT NOT NULL UNIQUE COLLATE NOCASE,
                        total_score INTEGER DEFAULT 0,
                        games_played INTEGER DEFAULT 0,
                        games_won INTEGER DEFAULT 0,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                    )
                """);

                stmt.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS scores (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        username TEXT NOT NULL COLLATE NOCASE,
                        game_name TEXT NOT NULL,
                        difficulty TEXT DEFAULT 'Normal',
                        score INTEGER DEFAULT 0,
                        played_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                    )
                """);

                stmt.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS players (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        name TEXT NOT NULL UNIQUE COLLATE NOCASE,
                        total_score INTEGER DEFAULT 0,
                        games_played INTEGER DEFAULT 0,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                    )
                """);
            }
            System.out.println("[DB] Tables 'users', 'scores', and 'players' verified in " + (isMySQL ? "MySQL" : "SQLite") + ".");
        } catch (SQLException e) {
            System.err.println("[DB] Warning: Table initialization error: " + e.getMessage());
        }
    }

    /**
     * Returns the name of the active database backend (MySQL or SQLite).
     */
    public static String getDatabaseType() {
        ensureInitialized();
        return activeDatabase;
    }

    /**
     * Checks if current connection is using MySQL.
     */
    public static boolean isUsingMySQL() {
        ensureInitialized();
        return isUsingMySQL;
    }

    /**
     * Close connection compatibility helper.
     */
    public static void closeConnection() {
        // Safe no-op as connections are managed per try-with-resources
    }

    /**
     * Compatibility helper method for older code.
     */
    public static void closeConnection(Connection con) {
        if (con == null) return;
        try {
            if (!con.isClosed()) {
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Tests if the database connection succeeds.
     */
    public static boolean testConnection() {
        Connection con = getConnection();
        return con != null;
    }

    /**
     * Main method to test database connectivity directly from command line.
     */
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("  Testing GamesMania SQL Database Link  ");
        System.out.println("========================================");

        Connection con = getConnection();
        if (con != null) {
            System.out.println("SUCCESS: GamesMania database is linked & ready!");
            System.out.println("Active Engine: " + getDatabaseType());
        } else {
            System.err.println("FAILURE: Database connection could not be established.");
        }
        System.out.println("========================================");
    }
}