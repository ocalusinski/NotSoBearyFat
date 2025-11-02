import java.sql.*;

/**
 * DatabaseManager handles all database operations using JDBC
 * Uses SQLite database (no server needed, all data stored locally)
 */
public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:notsobearyfat.db";
    private Connection connection;

    /**
     * Constructor - creates database connection and initializes tables
     */
    public DatabaseManager() {
        try {
            connection = DriverManager.getConnection(DB_URL);
            createTables();
            System.out.println("Database connected successfully!");
        } catch (SQLException e) {
            System.err.println("Error connecting to database: " + e.getMessage());
        }
    }

    /**
     * Creates necessary tables if they don't exist
     */
    private void createTables() {
        String createUsersTable = 
            "CREATE TABLE IF NOT EXISTS users (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "username TEXT UNIQUE NOT NULL, " +
            "password TEXT NOT NULL, " +
            "email TEXT NOT NULL, " +
            "user_type TEXT NOT NULL, " +
            "first_name TEXT, " +
            "last_name TEXT, " +
            "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
            ")";

        String createDataTable = 
            "CREATE TABLE IF NOT EXISTS user_data (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "user_id INTEGER NOT NULL, " +
            "date TEXT NOT NULL, " +
            "calories_consumed INTEGER, " +
            "weight DOUBLE, " +
            "sleep_hours DOUBLE, " +
            "total_calories_burned INTEGER, " +
            "FOREIGN KEY (user_id) REFERENCES users(id)" +
            ")";

        try {
            Statement stmt = connection.createStatement();
            stmt.execute(createUsersTable);
            stmt.execute(createDataTable);
            System.out.println("Tables created successfully!");
        } catch (SQLException e) {
            System.err.println("Error creating tables: " + e.getMessage());
        }
    }

    /**
     * Registers a new user in the database
     * @return true if successful, false if user already exists
     */
    public boolean registerUser(String username, String password, String email, 
                                String userType, String firstName, String lastName) {
        String sql = "INSERT INTO users (username, password, email, user_type, first_name, last_name) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, email);
            pstmt.setString(4, userType);
            pstmt.setString(5, firstName);
            pstmt.setString(6, lastName);
            
            pstmt.executeUpdate();
            System.out.println("User registered: " + username);
            return true;
        } catch (SQLException e) {
            System.err.println("Error registering user: " + e.getMessage());
            return false;
        }
    }

    /**
     * Authenticates a user login
     * @return User object if successful, null if credentials are invalid
     */
    public User loginUser(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                User user = new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("user_type"),
                    rs.getString("first_name"),
                    rs.getString("last_name")
                );
                System.out.println("User logged in: " + username);
                return user;
            }
        } catch (SQLException e) {
            System.err.println("Error during login: " + e.getMessage());
        }
        return null;
    }

    /**
     * Checks if a username already exists
     */
    public boolean usernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking username: " + e.getMessage());
        }
        return false;
    }

    /**
     * Checks if an email already exists
     */
    public boolean emailExists(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking email: " + e.getMessage());
        }
        return false;
    }

    /**
     * Stores user fitness data
     */
    public boolean saveUserData(int userId, String date, int caloriesConsumed, 
                                double weight, double sleepHours, int totalCaloriesBurned) {
        String sql = "INSERT INTO user_data (user_id, date, calories_consumed, weight, sleep_hours, total_calories_burned) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, userId);
            pstmt.setString(2, date);
            pstmt.setInt(3, caloriesConsumed);
            pstmt.setDouble(4, weight);
            pstmt.setDouble(5, sleepHours);
            pstmt.setInt(6, totalCaloriesBurned);
            
            pstmt.executeUpdate();
            System.out.println("Data saved for user ID: " + userId);
            return true;
        } catch (SQLException e) {
            System.err.println("Error saving user data: " + e.getMessage());
            return false;
        }
    }

    /**
     * Closes the database connection
     */
    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }
}

