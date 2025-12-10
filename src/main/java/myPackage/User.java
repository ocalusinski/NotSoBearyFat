package myPackage;
/**
 * User model class to represent user data
 */
public class User {
    private int id;
    private String username;
    private String email;
    private String userType; // "client" or "trainer"
    private String firstName;
    private String lastName;
    private Goal goal;


    /**
     * Constructs a new User object.
     * @author zachtaylorcsc
     */
    public User(int id, String username, String email, String userType, 
                String firstName, String lastName) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.userType = userType;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    // Getters
    /**
     * Returns the user's ID.
     * @author zachtaylorcsc
     * @return The user's ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the user's username.
     * @author zachtaylorcsc
     * @return The user's username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the user's email.
     * @author zachtaylorcsc
     * @return The user's email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Returns the user's type (e.g., "client" or "trainer").
     * @author zachtaylorcsc
     * @return The user's type.
     */
    public String getUserType() {
        return userType;
    }

    /**
     * Returns the user's first name.
     * @author zachtaylorcsc
     * @return The user's first name.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Returns the user's last name.
     * @author zachtaylorcsc
     * @return The user's last name.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Returns the user's current goal.
     * @author Oluwalademi Aromolaran
     * @return The user's current Goal object.
     */
    public Goal getGoal() {
        return goal;
    }

    // Setters
    /**
     * Sets the user's ID.
     * @author zachtaylorcsc
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Sets the user's username.
     * @author zachtaylorcsc
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Sets the user's email.
     * @author zachtaylorcsc
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Sets the user's type.
     * @author zachtaylorcsc
     */
    public void setUserType(String userType) {
        this.userType = userType;
    }

    /**
     * Sets the user's first name.
     * @author zachtaylorcsc
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Sets the user's last name.
     * @author zachtaylorcsc
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Sets the user's current goal.
     * @author Oluwalademi Aromolaran
     */
    public void setGoal(Goal goal) {
        this.goal = goal;
    }

    /**
     * Returns a string representation of the User object.
     * @author zachtaylorcsc
     * @return A string representation of the User object.
     */
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", userType='" + userType + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", goal=" + (goal != null ? goal.getGoalName() : "No Active Goal") +
                '}';
    }
}

