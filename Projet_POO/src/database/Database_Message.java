package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import models.Message;

public class Database_Message {

    private Connection connect;
    private final static String url = "jdbc:mysql://localhost:3306/mabiblio";

    
    /*
    *   Create connection with the distant database,
    *   Save connection in class attributes.
    *
    *   @throws ClassNotFoundException if driver haven't been loaded successfully.
    *
    *   @throws SQLException if SQL error.
     */
    private void doConnect() throws ClassNotFoundException, SQLException {

        try {
            connect = DriverManager.getConnection(url);
        }
        catch (SQLException e) {
            System.out.println("Erreur lors de la connexion � la base de donn�es");
            e.printStackTrace();
        }
    }
    
    
    /*
    *   Get the message history from the two given users
    *
    *   @throws ClassNotFoundException if driver haven't been loaded successfully.
    *   
    *   @throws SQLException if SQL error.
     */
    public ArrayList<Message> getHistory(int idLocal, int idDistant) throws SQLException, ClassNotFoundException {
    	
    	Message message;
    	ArrayList<Message> history = new ArrayList<Message>();
    	
    	doConnect();
    	
        Statement statement = connect.createStatement(); // create the statement object
        statement.setQueryTimeout(10);  // set timeout to 10 sec.

        ResultSet rs = statement.executeQuery("select * from messages where (recipient = '" + Integer.toString(idLocal) + "' and transmitter = '" + Integer.toString(idDistant) + "')" +
        		"or where (recipient = '" + Integer.toString(idDistant) + "' and transmitter = '" + Integer.toString(idLocal) + "')");

        while(rs.next()) {
        	message = new Message(rs.getInt("u1"), rs.getInt("u2"), rs.getString("content"), rs.getTimestamp("message_date")); 
        	System.out.println(message);
        	history.add(message);
        }
    	
        statement.close();
        
    	return history;
    }
    
    
    public void appendHistory(int recipient, int transmitter, String content) throws SQLException, ClassNotFoundException {
    	
    	doConnect();
    	
        Statement statement = connect.createStatement(); // create the statement object
        statement.setQueryTimeout(10);  // set timeout to 10 sec.
        
        statement.executeUpdate(
        	"insert into messages (recipient, transmitter, content, message_date) values ('" + 
        	recipient + "', '" + 
        	transmitter + "', '" +
        	content + "', '" +
        	"NOW()" +
        	"')"
        );
        statement.close();
    }
}
