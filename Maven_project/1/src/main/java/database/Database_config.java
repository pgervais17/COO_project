package database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class Database_config {

	private static String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver"; 
	private static Connection con;
	
	public void configureDatabase() {
		createDatabase();
		createUserTable();
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

	        	con=DriverManager.getConnection("jdbc:mysql://srv-bdens.insa-toulouse.fr:3306/tp_servlet_021","tp_servlet_021","tooPei9P");     
	        	
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
	     * Create the user table in the local database
	     */
	     public static void createUserTable() {

	         // SQL statement for creating a new table
	         String sql = "CREATE TABLE IF NOT EXISTS users (\n"
	                 + "	id INTEGER PRIMARY KEY NOT NULL,\n"
	                 + " capacity real \n"
	                 + ");";
	         
	         try {
	             Statement stmt = con.createStatement();
	             // creation of a new table
	             stmt.execute(sql);
	             stmt.close();
	         } catch (SQLException e) {
	             System.out.println(e.getMessage());
	         }
	     }


/*
 * Create the message table in the local database
 */
 public static void createMessageTable() {

     // SQL statement for creating a new table
     String sql = "CREATE TABLE IF NOT EXISTS messages (\n"
             + "	receiver INTEGER NOT NULL,\n"
             + "	sender INTEGER NOT NULL,\n"
             + "	content TEXT NOT NULL,\n"
             + "	message_date DATETIME NOT NULL,\n"
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

