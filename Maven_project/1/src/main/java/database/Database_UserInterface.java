package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class Database_UserInterface {

    /*
    *   Check if a user with the given id exists in the database
    *
    *   @param id, user id
    *
    *   @return true if the user exists, false otherwise
     */
    private boolean doesUserExist(int id) throws SQLException {

        // check if ID already exists in the database
        Statement statement = Database_config.con.createStatement(); // create the statement object
        statement.setQueryTimeout(10);  // set timeout to 10 sec.

        ResultSet res = statement.executeQuery("SELECT * FROM users WHERE id = '" + Integer.toString(id) + "'");

        if(res.next()) { // Result set is not empty
        	statement.close();
            return true; // Notify that user id already exist
        } else {
        	statement.close();
            return false; // Notify that the user id does not exist
        }
    }


    /*
    *   Create connection with the distant database,
    *   Save connection in class attributes.
    *
    *   @throws ClassNotFoundException if driver haven't been loaded successfully.
    *
    *   @throws SQLException if SQL error.
     */
    /*public static void doConnect() throws ClassNotFoundException, SQLException {

        try {
        		Class.forName("com.mysql.jdbc.Driver");

        		Connection con=DriverManager.getConnection("jdbc:mysql://srv-bdens.insa-toulouse.fr:3306/tp_servlet_021","tp_servlet_021","tooPei9P");
        		//here sonoo is the database name, root is the username and root is the password

        }
        catch (SQLException e) {
            System.out.println("Impossible de se connecter à la base de données");
            e.printStackTrace();
        }
    }*/


    /*
    *   Create a new user with the given id
    *   If a user with this id already exist, does not create the user.
    *
    *   @ return true if user id is available and user have been created into the database,
    *            false if user id is already used.
    *
    *   @throws SQLException if SQL error.
     */
    public boolean createUser(int id) throws SQLException, ClassNotFoundException {
        // check if ID already exists in the database
        Statement statement = Database_config.con.createStatement(); // create the statement object
        statement.setQueryTimeout(10);  // set timeout to 10 sec.

        if(!doesUserExist(id)){
            statement.executeUpdate("INSERT INTO users (id) VALUES ('" + Integer.toString(id));
            statement.close();
            return true;
        } else {
        	statement.close();
            return false;
        }
    }


    /*
    *   Authentifies user with id.
    *
    *   @param id, user id.
    *
    *   @return true if user with id, false otherwise.
    *
    *   @throws SQLException if SQL error.
     */
   /* public boolean signIn(int id) throws SQLException, ClassNotFoundException {

        doConnect();

        // check if ID already exists in the database
        Statement statement = connect.createStatement(); // create the statement object
        statement.setQueryTimeout(10);  // set timeout to 10 sec.

        ResultSet res = statement.executeQuery("SELECT * FROM users WHERE id = '" + Integer.toString(id));

        if(res.next()) {
        	statement.close();
            return true; // user exists
        } else {
        	statement.close();
            return false; // user does not exists
        }
    }*/
    
    
}