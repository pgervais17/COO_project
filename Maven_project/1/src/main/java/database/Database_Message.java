package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import models.Message;

public class Database_Message {

    /*
    *   Create connection with the distant database,
    *   Save connection in class attributes.
    *
    *   @throws ClassNotFoundException if driver haven't been loaded successfully.
    *
    *   @throws SQLException if SQL error.
     */
    /*private void doConnect() throws ClassNotFoundException, SQLException {

        try {
            connect = DriverManager.getConnection(JDBC_DRIVER);
        }
        catch (SQLException e) {
            System.out.println("Erreur lors de la connexion à  la base de données");
            e.printStackTrace();
        }
    }*/
    
    
    /*
    *   Get the message history from the two given users
    *
    *   @throws ClassNotFoundException if driver haven't been loaded successfully.
    *   
    *   @throws SQLException if SQL error.
     */
    public ArrayList<Message> getHistory(String local, String distant) throws SQLException, ClassNotFoundException {
    	//local et distant sont des adresses ip
    	Message message;
    	ArrayList<Message> history = new ArrayList<Message>();
    	
    	
        Statement statement = Database_config.con.createStatement(); // create the statement object
        statement.setQueryTimeout(10);  // set timeout to 10 sec.

        ResultSet rs = statement.executeQuery("SELECT * FROM messages WHERE (receiver = '" + local + "' and sender = '" + distant + "')" +
        		"or where (receiver = '" + distant +"' and sender = '" + local + "')");

        while(rs.next()) {
        	message = new Message(rs.getString("sender"), rs.getString("receiver"), rs.getString("content"), rs.getTimestamp("timestamp")); 
        	System.out.println(message);
        	history.add(message);
        }
    	
        statement.close();
        
    	return history;
    }
    
    
    public void appendHistory(String receiver, String sender, String content) throws SQLException, ClassNotFoundException {

        Statement statement = Database_config.con.createStatement(); // create the statement object
        statement.setQueryTimeout(10);  // set timeout to 10 sec.
        
        statement.executeUpdate(
        	"INSERT INTO messages (receiver, sender, content, timestamp) VALUES ('" + 
        	receiver + "', '" + 
        	sender + "', '" +
        	content + "', '" +
        	"NOW()" +
        	"')"
        );
        
        System.out.println("Ajout du message " + content + " à l'historique");
        statement.close();
    }
}
