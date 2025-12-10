package myPackage;
import java.awt.*;

public class Constants {
    public static final Color baylorGreen = new Color(0, 71, 56);
    public static final Color baylorGold = new Color(255, 199, 44);
    public static final Color LIGHT_GREEN = new Color(0, 100, 80);
    public static final Color BACKGROUND_COLOR = new Color(240, 255, 250);
    public static final DatabaseManager DB_MANAGER = new DatabaseManager();

    /**
     * Closes the database connection.
     * @author Owen Chipman
     */
    public static void exitSequence(){
        DB_MANAGER.closeConnection();
    }


}
