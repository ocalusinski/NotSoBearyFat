package myPackage;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * DatabaseManager handles all database operations using JDBC
 * Uses SQLite database (no server needed, all data stored locally)
 */
public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:notsobearyfat.db";
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private Connection connection;

    /**
     * Constructor - creates database connection and initializes tables
     */
    public DatabaseManager() {
        try {
            connection = DriverManager.getConnection(DB_URL);
            // If another connection is writing, wait up to 5 seconds instead of failing immediately
            try (Statement busyStmt = connection.createStatement()) {
                busyStmt.execute("PRAGMA busy_timeout = 5000");
            }
            
            // Check database integrity
            if (!checkDatabaseIntegrity()) {
                System.err.println("Database corruption detected. Attempting to recover...");
                closeConnection();
                recoverDatabase();
                // Reconnect after recovery
                connection = DriverManager.getConnection(DB_URL);
                try (Statement busyStmt = connection.createStatement()) {
                    busyStmt.execute("PRAGMA busy_timeout = 5000");
                }
            }
            
            createTables();
            createOGAdmin();
            System.out.println("Database connected successfully!");
        } catch (SQLException e) {
            System.err.println("Error connecting to database: " + e.getMessage());
            // If connection failed due to corruption, try to recover
            if (e.getMessage() != null && (e.getMessage().contains("malformed") || e.getMessage().contains("corrupt"))) {
                System.err.println("Attempting to recover corrupted database...");
                recoverDatabase();
                // Try connecting again
                try {
                    connection = DriverManager.getConnection(DB_URL);
                    try (Statement busyStmt = connection.createStatement()) {
                        busyStmt.execute("PRAGMA busy_timeout = 5000");
                    }
                    createTables();
                    createOGAdmin();
                    System.out.println("Database recovered and reconnected successfully!");
                } catch (SQLException e2) {
                    System.err.println("Failed to recover database: " + e2.getMessage());
                }
            }
        }
    }
    
    /**
     * Checks database integrity
     * @return true if database is valid, false if corrupted
     */
    private boolean checkDatabaseIntegrity() {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("PRAGMA integrity_check");
            if (rs.next()) {
                String result = rs.getString(1);
                return "ok".equals(result.toLowerCase());
            }
        } catch (SQLException e) {
            // If we can't even run integrity check, database is likely corrupted
            return false;
        }
        return false;
    }
    
    /**
     * Recovers from database corruption by backing up and recreating the database
     */
    private void recoverDatabase() {
        try {
            java.io.File dbFile = new java.io.File("notsobearyfat.db");
            if (dbFile.exists()) {
                // Backup the corrupted database
                java.io.File backupFile = new java.io.File("notsobearyfat.db.corrupted.backup");
                if (backupFile.exists()) {
                    backupFile.delete();
                }
                dbFile.renameTo(backupFile);
                System.out.println("Corrupted database backed up to: notsobearyfat.db.corrupted.backup");
                System.out.println("A new database will be created automatically.");
            }
        } catch (Exception e) {
            System.err.println("Error during database recovery: " + e.getMessage());
            // If backup fails, try to delete the corrupted file
            try {
                java.io.File dbFile = new java.io.File("notsobearyfat.db");
                if (dbFile.exists()) {
                    dbFile.delete();
                    System.out.println("Corrupted database file deleted. A new one will be created.");
                }
            } catch (Exception e2) {
                System.err.println("Could not delete corrupted database file: " + e2.getMessage());
            }
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
            "email TEXT UNIQUE NOT NULL, " +
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

        String createClassesTable =
            "CREATE TABLE IF NOT EXISTS classes (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "trainer_username TEXT NOT NULL, " +
            "class_type TEXT NOT NULL, " +
            "description TEXT, " +
            "start_time TEXT NOT NULL, " +
            "end_time TEXT NOT NULL, " +
            "max_participants INTEGER NOT NULL, " +
            "cost REAL NOT NULL" +
            ")";

        String createClassEnrollmentsTable =
            "CREATE TABLE IF NOT EXISTS class_enrollments (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "class_id INTEGER NOT NULL, " +
            "user_id INTEGER NOT NULL, " +
            "enrolled_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
            "FOREIGN KEY (class_id) REFERENCES classes(id), " +
            "FOREIGN KEY (user_id) REFERENCES users(id)" +
            ")";

        String createFriendsTable =
            "CREATE TABLE IF NOT EXISTS friends (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "requester_id INTEGER NOT NULL, " +
            "receiver_id INTEGER NOT NULL, " +
            "status TEXT NOT NULL DEFAULT 'pending', " +
            "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
            "FOREIGN KEY (requester_id) REFERENCES users(id), " +
            "FOREIGN KEY (receiver_id) REFERENCES users(id), " +
            "UNIQUE(requester_id, receiver_id)" +
            ")";

        try {
            Statement stmt = connection.createStatement();
            stmt.execute(createUsersTable);
            stmt.execute(createDataTable);
            stmt.execute(createClassesTable);
            stmt.execute(createClassEnrollmentsTable);
            stmt.execute(createFriendsTable);
            System.out.println("Tables created successfully!");
            System.out.println("Friends table created/verified.");
        } catch (SQLException e) {
            System.err.println("Error creating tables: " + e.getMessage());
            e.printStackTrace();
        }
    }
    private void createOGAdmin() {
        String createOGAdmin = "INSERT INTO users (username, password, email, user_type, first_name, last_name) " +
                "values('admin', 'secretAdminPassword', 'admin@nsfb.com', 'admin', 'admin', 'admin')";
        try {
            Statement stmt = connection.createStatement();
            stmt.execute(createOGAdmin);
        } catch (SQLException e) {
            if(e.getErrorCode() != 19) {
                System.err.println("Error creating OG admin: " + e.getErrorCode());
            }
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

    public boolean updateUser(String username, String password, String email) {
        String sql = "UPDATE users SET password = ?, username = ? WHERE email = ?";
        int rowsUpdated = 0;
        try{
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, password);
            pstmt.setString(2, username);
            pstmt.setString(3, email);
            rowsUpdated = pstmt.executeUpdate();
            if( rowsUpdated == 1){
                return true;
            }
        }catch (SQLException e){
            System.err.println("Error updating user: " + e.getMessage());
        }
        //implied else
        System.out.println("Error updating users: " + rowsUpdated);
        return false;
    }

    public boolean removeUser(String username, String password) {
        String sql = "DELETE FROM users WHERE username = ? AND password = ?";
        int rowsUpdated = 0;
        try{
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            rowsUpdated = pstmt.executeUpdate();
            if( rowsUpdated == 1){
                return true;
            }
        }catch (SQLException e){
            System.err.println("Error deleting user: " + e.getMessage());
        }
        System.out.println("Error deleting users: " + rowsUpdated);
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
     * Gets user ID from username
     * @return user ID if found, -1 if not found
     */
    public int getUserIdByUsername(String username) {
        String sql = "SELECT id FROM users WHERE username = ?";
        
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            System.err.println("Error getting user ID: " + e.getMessage());
        }
        return -1;
    }

    /**
     * Gets user ID from first name (for backward compatibility)
     * @return user ID if found, -1 if not found
     */
    public int getUserIdByFirstName(String firstName) {
        String sql = "SELECT id FROM users WHERE first_name = ? LIMIT 1";
        
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, firstName);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            System.err.println("Error getting user ID by first name: " + e.getMessage());
        }
        return -1;
    }

    /**
     * Gets the latest user data entry with weight and sleep as doubles
     * @return array with [caloriesConsumed, weight, sleepHours, totalCaloriesBurned] or null if no data
     */
    public double[] getLatestUserDataDouble(int userId) {
        String sql = "SELECT calories_consumed, weight, sleep_hours, total_calories_burned " +
                     "FROM user_data WHERE user_id = ? " +
                     "ORDER BY date DESC, id DESC LIMIT 1";
        
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                int caloriesConsumed = rs.getInt("calories_consumed");
                double weight = rs.getDouble("weight");
                double sleepHours = rs.getDouble("sleep_hours");
                int totalCaloriesBurned = rs.getInt("total_calories_burned");
                
                return new double[]{
                    caloriesConsumed,
                    weight,
                    sleepHours,
                    totalCaloriesBurned
                };
            }
        } catch (SQLException e) {
            System.err.println("Error getting latest user data: " + e.getMessage());
        }
        return null;
    }

    /**
     * Checks if user has data entries within the last 7 days
     * @return true if recent data exists, false otherwise
     */
    public boolean hasRecentData(int userId) {
        String sql = "SELECT COUNT(*) FROM user_data " +
                     "WHERE user_id = ? AND date >= date('now', '-7 days')";
        
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking recent data: " + e.getMessage());
        }
        return false;
    }

    /**
     * Gets user type from user ID
     * @return user type string (e.g., "trainer" or "client") or null if not found
     */
    public String getUserType(int userId) {
        String sql = "SELECT user_type FROM users WHERE id = ?";
        
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getString("user_type");
            }
        } catch (SQLException e) {
            System.err.println("Error getting user type: " + e.getMessage());
        }
        return null;
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

    /**
     * Saves a workout class created by a trainer
     */
    public boolean saveClass(String trainerUsername, String classType, String description,
                             LocalDateTime startTime, LocalDateTime endTime, int maxParticipants, double cost) {
        String sql = "INSERT INTO classes (trainer_username, class_type, description, start_time, end_time, max_participants, cost) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, trainerUsername);
            pstmt.setString(2, classType);
            pstmt.setString(3, description);
            pstmt.setString(4, startTime.format(dtf));
            pstmt.setString(5, endTime.format(dtf));
            pstmt.setInt(6, maxParticipants);
            pstmt.setDouble(7, cost);
            pstmt.executeUpdate();
            System.out.println("Class saved for trainer: " + trainerUsername);
            return true;
        } catch (SQLException e) {
            System.err.println("Error saving class: " + e.getMessage());
            return false;
        }
    }

    /**
     * Returns all classes created by any trainer
     */
    public java.util.List<WorkoutClass> getAllClasses() {
        java.util.List<WorkoutClass> classes = new java.util.ArrayList<>();
        String sql = "SELECT * FROM classes ORDER BY start_time ASC";

        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                WorkoutClass wc = new WorkoutClass(
                    rs.getInt("id"),
                    rs.getString("trainer_username"),
                    rs.getString("class_type"),
                    rs.getString("description"),
                    LocalDateTime.parse(rs.getString("start_time"), dtf),
                    LocalDateTime.parse(rs.getString("end_time"), dtf),
                    rs.getInt("max_participants"),
                    rs.getDouble("cost")
                );
                classes.add(wc);
            }
        } catch (SQLException e) {
            System.err.println("Error getting classes: " + e.getMessage());
        }
        return classes;
    }

    /**
     * Returns all classes created by a specific trainer 
     * If the trainer has no classes this returns an empty list
     */
    public List<WorkoutClass> getClassesForTrainer(String trainerUsername) {
        List<WorkoutClass> classes = new ArrayList<>();
        String sql = "SELECT * FROM classes WHERE trainer_username = ? ORDER BY start_time ASC";

        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, trainerUsername);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                WorkoutClass wc = new WorkoutClass(
                        rs.getInt("id"),
                        rs.getString("trainer_username"),
                        rs.getString("class_type"),
                        rs.getString("description"),
                        LocalDateTime.parse(rs.getString("start_time"), dtf),
                        LocalDateTime.parse(rs.getString("end_time"), dtf),
                        rs.getInt("max_participants"),
                        rs.getDouble("cost")
                );
                classes.add(wc);
            }
        } catch (SQLException e) {
            System.err.println("Error getting classes for trainer: " + e.getMessage());
        }
        return classes;
    }

    /**
     * Updates the details of an existing class in the database
     */
    public boolean updateClass(int id,
                               String classType,
                               String description,
                               LocalDateTime startTime,
                               LocalDateTime endTime,
                               int maxParticipants,
                               double cost) {
        String sql = "UPDATE classes SET " +
                "class_type = ?, " +
                "description = ?, " +
                "start_time = ?, " +
                "end_time = ?, " +
                "max_participants = ?, " +
                "cost = ? " +
                "WHERE id = ?";

        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, classType);
            pstmt.setString(2, description);
            pstmt.setString(3, startTime.format(dtf));
            pstmt.setString(4, endTime.format(dtf));
            pstmt.setInt(5, maxParticipants);
            pstmt.setDouble(6, cost);
            pstmt.setInt(7, id);

            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.err.println("Error updating class: " + e.getMessage());
            return false;
        }
    }

    /**
     * Returns list of user display names enrolled in a given class
     */
    public java.util.List<String> getUsersForClass(int classId) {
        java.util.List<String> users = new java.util.ArrayList<>();
        String sql = "SELECT u.first_name, u.last_name, u.username " +
                     "FROM class_enrollments ce " +
                     "JOIN users u ON ce.user_id = u.id " +
                     "WHERE ce.class_id = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, classId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String first = rs.getString("first_name");
                String last = rs.getString("last_name");
                String username = rs.getString("username");
                String display;
                if (first != null && last != null) {
                    display = first + " " + last + " (" + username + ")";
                } else if (first != null) {
                    display = first + " (" + username + ")";
                } else {
                    display = username;
                }
                users.add(display);
            }
        } catch (SQLException e) {
            System.err.println("Error getting users for class: " + e.getMessage());
        }
        return users;
    }

    /**
     * Gets the current number of enrolled participants for a class
     * @return count of enrolled participants
     */
    public int getCurrentEnrollmentCount(int classId) {
        String sql = "SELECT COUNT(*) FROM class_enrollments WHERE class_id = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, classId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting enrollment count: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Checks if a user is already enrolled in a class
     * @return true if enrolled, false otherwise
     */
    public boolean isUserEnrolled(int userId, int classId) {
        String sql = "SELECT COUNT(*) FROM class_enrollments WHERE user_id = ? AND class_id = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, userId);
            pstmt.setInt(2, classId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking enrollment: " + e.getMessage());
        }
        return false;
    }

    /**
     * Enrolls a user in a class, but only if the class hasn't reached max capacity
     * @return true if enrollment successful, false if class is full or user already enrolled
     */
    public boolean enrollUserInClass(int userId, int classId) {
        // First check if user is already enrolled
        if (isUserEnrolled(userId, classId)) {
            System.err.println("User already enrolled in this class");
            return false;
        }

        // Get the class to check max participants
        String getClassSql = "SELECT max_participants FROM classes WHERE id = ?";
        try {
            PreparedStatement getClassStmt = connection.prepareStatement(getClassSql);
            getClassStmt.setInt(1, classId);
            ResultSet rs = getClassStmt.executeQuery();
            
            if (!rs.next()) {
                System.err.println("Class not found");
                return false;
            }
            
            int maxParticipants = rs.getInt("max_participants");
            int currentCount = getCurrentEnrollmentCount(classId);
            
            if (currentCount >= maxParticipants) {
                System.err.println("Class is full. Current: " + currentCount + ", Max: " + maxParticipants);
                return false;
            }
            
            // Enroll the user
            String enrollSql = "INSERT INTO class_enrollments (class_id, user_id) VALUES (?, ?)";
            PreparedStatement enrollStmt = connection.prepareStatement(enrollSql);
            enrollStmt.setInt(1, classId);
            enrollStmt.setInt(2, userId);
            enrollStmt.executeUpdate();
            
            System.out.println("User " + userId + " enrolled in class " + classId);
            return true;
        } catch (SQLException e) {
            System.err.println("Error enrolling user in class: " + e.getMessage());
            return false;
        }
    }

    /**
     * Gets all classes that a user is enrolled in
     * @return list of WorkoutClass objects the user is enrolled in
     */
    public java.util.List<WorkoutClass> getUserEnrolledClasses(int userId) {
        java.util.List<WorkoutClass> classes = new java.util.ArrayList<>();
        String sql = "SELECT c.* FROM classes c " +
                     "JOIN class_enrollments ce ON c.id = ce.class_id " +
                     "WHERE ce.user_id = ? " +
                     "ORDER BY c.start_time ASC";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                WorkoutClass wc = new WorkoutClass(
                    rs.getInt("id"),
                    rs.getString("trainer_username"),
                    rs.getString("class_type"),
                    rs.getString("description"),
                    LocalDateTime.parse(rs.getString("start_time"), dtf),
                    LocalDateTime.parse(rs.getString("end_time"), dtf),
                    rs.getInt("max_participants"),
                    rs.getDouble("cost")
                );
                classes.add(wc);
            }
        } catch (SQLException e) {
            System.err.println("Error getting user enrolled classes: " + e.getMessage());
        }
        return classes;
    }

    /**
     * Gets historical user data for a specific user, optionally filtered by number of days
     * @param userId The user ID
     * @param days Number of days to retrieve (0 or negative for all data)
     * @return List of arrays containing [date, caloriesConsumed, weight, sleepHours, totalCaloriesBurned]
     */
    public java.util.List<Object[]> getHistoricalUserData(int userId, int days) {
        java.util.List<Object[]> data = new java.util.ArrayList<>();
        String sql;
        
        if (days > 0) {
            sql = "SELECT date, calories_consumed, weight, sleep_hours, total_calories_burned " +
                  "FROM user_data WHERE user_id = ? AND date >= date('now', '-' || ? || ' days') " +
                  "ORDER BY date ASC";
        } else {
            sql = "SELECT date, calories_consumed, weight, sleep_hours, total_calories_burned " +
                  "FROM user_data WHERE user_id = ? ORDER BY date ASC";
        }
        
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, userId);
            if (days > 0) {
                pstmt.setInt(2, days);
            }
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Object[] row = new Object[]{
                    rs.getString("date"),
                    rs.getInt("calories_consumed"),
                    rs.getDouble("weight"),
                    rs.getDouble("sleep_hours"),
                    rs.getInt("total_calories_burned")
                };
                data.add(row);
            }
        } catch (SQLException e) {
            System.err.println("Error getting historical user data: " + e.getMessage());
        }
        return data;
    }

    /**
     * Searches for users by username (partial match)
     * @param searchTerm The username or partial username to search for
     * @param excludeUserId User ID to exclude from results (typically the current user)
     * @return List of User objects matching the search
     */
    public java.util.List<User> searchUsersByUsername(String searchTerm, int excludeUserId) {
        java.util.List<User> users = new java.util.ArrayList<>();
        String sql = "SELECT id, username, email, user_type, first_name, last_name " +
                     "FROM users WHERE username LIKE ? AND id != ? " +
                     "ORDER BY username LIMIT 20";
        
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, "%" + searchTerm + "%");
            pstmt.setInt(2, excludeUserId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                User user = new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("user_type"),
                    rs.getString("first_name"),
                    rs.getString("last_name")
                );
                users.add(user);
            }
        } catch (SQLException e) {
            System.err.println("Error searching users: " + e.getMessage());
        }
        return users;
    }

    /**
     * Sends a friend request from one user to another
     * @param requesterId The user sending the request
     * @param receiverId The user receiving the request
     * @return true if successful, false if request already exists or error occurred
     */
    public boolean sendFriendRequest(int requesterId, int receiverId) {
        if (requesterId == receiverId) {
            return false; // Can't friend yourself
        }
        
        // Check if request already exists
        String checkSql = "SELECT id FROM friends WHERE " +
                         "(requester_id = ? AND receiver_id = ?) OR " +
                         "(requester_id = ? AND receiver_id = ?)";
        try {
            PreparedStatement checkStmt = connection.prepareStatement(checkSql);
            checkStmt.setInt(1, requesterId);
            checkStmt.setInt(2, receiverId);
            checkStmt.setInt(3, receiverId);
            checkStmt.setInt(4, requesterId);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                return false; // Request already exists
            }
        } catch (SQLException e) {
            System.err.println("Error checking existing friend request: " + e.getMessage());
            return false;
        }
        
        String sql = "INSERT INTO friends (requester_id, receiver_id, status) VALUES (?, ?, 'pending')";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, requesterId);
            pstmt.setInt(2, receiverId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error sending friend request: " + e.getMessage());
            return false;
        }
    }

    /**
     * Accepts a friend request
     * @param requesterId The user who sent the request
     * @param receiverId The user accepting the request
     * @return true if successful, false otherwise
     */
    public boolean acceptFriendRequest(int requesterId, int receiverId) {
        String sql = "UPDATE friends SET status = 'accepted' " +
                     "WHERE requester_id = ? AND receiver_id = ? AND status = 'pending'";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, requesterId);
            pstmt.setInt(2, receiverId);
            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.err.println("Error accepting friend request: " + e.getMessage());
            return false;
        }
    }

    /**
     * Rejects or cancels a friend request
     * @param requesterId The user who sent the request
     * @param receiverId The user rejecting/canceling the request
     * @return true if successful, false otherwise
     */
    public boolean rejectFriendRequest(int requesterId, int receiverId) {
        String sql = "DELETE FROM friends " +
                     "WHERE requester_id = ? AND receiver_id = ? AND status = 'pending'";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, requesterId);
            pstmt.setInt(2, receiverId);
            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.err.println("Error rejecting friend request: " + e.getMessage());
            return false;
        }
    }

    /**
     * Gets pending friend requests received by a user
     * @param userId The user ID
     * @return List of User objects who have sent pending requests
     */
    public java.util.List<User> getPendingFriendRequests(int userId) {
        java.util.List<User> users = new java.util.ArrayList<>();
        String sql = "SELECT u.id, u.username, u.email, u.user_type, u.first_name, u.last_name " +
                     "FROM friends f " +
                     "JOIN users u ON f.requester_id = u.id " +
                     "WHERE f.receiver_id = ? AND f.status = 'pending'";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                User user = new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("user_type"),
                    rs.getString("first_name"),
                    rs.getString("last_name")
                );
                users.add(user);
            }
        } catch (SQLException e) {
            System.err.println("Error getting pending friend requests: " + e.getMessage());
        }
        return users;
    }

    /**
     * Gets pending friend requests sent by a user
     * @param userId The user ID
     * @return List of User objects who have received pending requests
     */
    public java.util.List<User> getSentFriendRequests(int userId) {
        java.util.List<User> users = new java.util.ArrayList<>();
        String sql = "SELECT u.id, u.username, u.email, u.user_type, u.first_name, u.last_name " +
                     "FROM friends f " +
                     "JOIN users u ON f.receiver_id = u.id " +
                     "WHERE f.requester_id = ? AND f.status = 'pending'";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                User user = new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("user_type"),
                    rs.getString("first_name"),
                    rs.getString("last_name")
                );
                users.add(user);
            }
        } catch (SQLException e) {
            System.err.println("Error getting sent friend requests: " + e.getMessage());
        }
        return users;
    }

    /**
     * Gets all accepted friends of a user
     * @param userId The user ID
     * @return List of User objects who are friends
     */
    public java.util.List<User> getFriends(int userId) {
        java.util.List<User> friends = new java.util.ArrayList<>();
        String sql = "SELECT u.id, u.username, u.email, u.user_type, u.first_name, u.last_name " +
                     "FROM friends f " +
                     "JOIN users u ON (CASE WHEN f.requester_id = ? THEN f.receiver_id ELSE f.requester_id END) = u.id " +
                     "WHERE (f.requester_id = ? OR f.receiver_id = ?) AND f.status = 'accepted'";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, userId);
            pstmt.setInt(2, userId);
            pstmt.setInt(3, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                User user = new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("user_type"),
                    rs.getString("first_name"),
                    rs.getString("last_name")
                );
                friends.add(user);
            }
        } catch (SQLException e) {
            System.err.println("Error getting friends: " + e.getMessage());
        }
        return friends;
    }

    /**
     * Gets classes that a friend is enrolled in
     * @param friendId The friend's user ID
     * @return List of WorkoutClass objects the friend is enrolled in
     */
    public java.util.List<WorkoutClass> getFriendEnrolledClasses(int friendId) {
        return getUserEnrolledClasses(friendId);
    }
}

