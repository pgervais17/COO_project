package connection;

import models.User;
import views.ChatWindow;
import views.UserInterface;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.sql.SQLException;
import java.util.ArrayList;

import database.Database_Message;


	
public class TCPConnect extends Thread{
    //client TCP qui initie une communication sur demande pour envoyer un message � un autre utilisateur
    private InetAddress address;
    private User currentUser;
    private Integer port;
    private String login;
    private ServerSocket server;
    private UserInterface userInterface;
    private ArrayList<TCPThread> threads = new ArrayList<TCPThread>();
    private Database_Message db_message;
  
    public TCPConnect(User user, UserInterface ui) {
        this.address = user.getAddress();
        this.port = user.getPort();
        this.login = user.getLogin();
        this.currentUser = user;
        this.userInterface = ui;
        this.db_message = new Database_Message();
    }
    public void closeSession(){
    	
			try {
				for (TCPThread t : threads) {
					//close all opened threads
					t.close();
				}
				server.close();
				
				System.out.println("Session tcp closed");
			} catch (SocketException se){
				
			} catch (NullPointerException nullexc) {
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				
			}
			
		
    }
    
    public UserInterface getUserInterface(){
    	return this.userInterface;
    }
    public void closeThreadWith(User u){
    	getTCPThreadWith(u).close();
    }
    public InetAddress getAddress() {
        return address;
    }
 
    public Integer getPort(){
    	return this.port;
    }
    public User getCurrentUser() {
    	return this.currentUser;
    }
    public Database_Message getDatabase() {
    	return this.db_message;
    }
    
    //function used to start a TCP connection with a user
    public void connectTo(User u,ChatWindow chat){
    	Socket s;
		try {
			System.out.println("Trying to start a tcp session with " + u.getLogin() + " on address " + u.getAddress().toString() + " on port " + u.getPort());
			s = new Socket(u.getAddress(),u.getPort());
			threads.add(new TCPThread(this,s,chat,u.getLogin()));
			threads.get(threads.size()-1).start();
			//Thread.sleep(2000);
			threads.get(threads.size()-1).sendNickname(this.login);
			System.out.println("Connection between " + this.login + " and " + u.getLogin());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} /*catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
    	
    }
    
    public void sendMessage(String message, User receiver) {
    	TCPThread thread = null;
    	System.out.println("Recherche du thread correspondant");
    	for (TCPThread t : threads) {
    		if (receiver.getLogin().equals(t.getReceiver())) {
    			System.out.println("Thread trouv�");
    			//we found the good thread
    			thread = t;
    		}
    	}
    	thread.sendMessage(message);
    }
    
    public TCPThread getTCPThreadWith(User u ){
    	TCPThread thread = null;
    	for (TCPThread t : threads) {
    		if (u.getLogin().equals(t.getReceiver())) {
    			//we found the good thread
    			thread = t;
    		}
    	}
    	return thread;
    }
    
    public void run() {
    	
    	try {
    		server = new ServerSocket(port);
    		while(true){
    			threads.add(new TCPThread(this,server.accept(), new ChatWindow(this.currentUser,this, this.userInterface)));
    			threads.get(threads.size()-1).start();
    		}
    	} catch (SocketException se) {
    	} catch (IOException e) {
    		// TODO Auto-generated catch block
        	e.printStackTrace();
        }
    }
    	
       
} 	   	
