package database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class Database_config {

	private static String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver"; 
	public static Connection con;
	
	public void configureDatabase() {
		createDatabase();
		createMessageTable();
	}
	
	/*
	    *   Create a new database ( first connection to the app )
	    *
	    *   @param filename, name of the database
	     */
	    public void createDatabase() {
	  	
	        try {
	        	Class.forName(JDBC_DRIVER);

	        	con=DriverManager.getConnection("jdbc:mysql://db4free.net:3306/chat_project","evaminot","12345678");     
	        	
	            DatabaseMetaData meta = con.getMetaData();
	            System.out.println("The driver name is " + meta.getDriverName());
	            System.out.println("A new database has been created."); 
	            
	        } catch (SQLException e) {    	
	            System.out.println(e.getMessage()); 
	        } catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    


/*
 * Create the message table in the local database
 */
 public static void createMessageTable() {

     // SQL statement for creating a new table
     String sql = "CREATE TABLE IF NOT EXISTS messages (\n"
             + "	receiver TEXT NOT NULL,\n"
             + "	sender TEXT NOT NULL,\n"
             + "	content TEXT NOT NULL,\n"
             + "	timestamp DATETIME NOT NULL,\n"
             + " capacity real \n"
             + ");";
     
     try {
         Statement stmt = con.createStatement() ;
         // create a new table
         stmt.execute(sql);
         stmt.close();
     } catch (SQLException e) {
         System.out.println(e.getMessage());
     }
 }
}

