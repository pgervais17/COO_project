package connection;

import models.User;
import views.ChatWindow;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;


	
public class TCPConnect extends Thread{
    //client TCP qui initie une communication sur demande pour envoyer un message à un autre utilisateur
    private InetAddress address;
    private User currentUser;
    private Integer port;
    private String login;
    private ServerSocket server;
    private ArrayList<TCPThread> threads = new ArrayList<TCPThread>();
  
    public TCPConnect(User user) {
        this.address = user.getAddress();
        this.port = user.getPort();
        this.login = user.getLogin();
        this.currentUser = user;
    }
    public void closeSession(){
    	
			try {
				server.close();
				for (TCPThread t : threads) {
					//close all opened threads
					t.close();
				}
				System.out.println("Session tcp closed");
			} catch (SocketException se){
				
			} catch (NullPointerException nullexc) {
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				
			}
			
		
    }
    public InetAddress getAddress() {
        return address;
    }
 
    public Integer getPort(){
    	return this.port;
    }
    
    //function used to start a TCP connection with a user
    public void connectTo(User u){
    	Socket s;
		try {
			s = new Socket(u.getAddress(),u.getPort());
			threads.add(new TCPThread(s));
			threads.get(threads.size()-1).start();
			System.out.println("Connection between " + this.login + " and " + u.getLogin());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    
    public void sendMessage(String message, User receiver) {
    	TCPThread thread = null;
    	for (TCPThread t : threads) {
    		if ((t.getAddress().equals(receiver.getAddress())) && (t.getPort().equals(receiver.getPort()))) {
    			//we found the good thread
    			thread = t;
    		}
    	}
    	thread.sendMessage(message,currentUser);

    }
    
    public TCPThread getTCPThreadWith(User u ){
    	TCPThread thread = null;
    	for (TCPThread t : threads) {
    		if ((t.getAddress().equals(u.getAddress())) && (t.getPort().equals(u.getPort()))) {
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
    			threads.add(new TCPThread(server.accept()));
    			threads.get(threads.size()-1).start();
    		}
    	} catch (SocketException se) {
    	} catch (IOException e) {
    		// TODO Auto-generated catch block
        	e.printStackTrace();
        }
    }
    	
       
} 	   	
