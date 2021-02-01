package connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.sql.SQLException;
import java.sql.Timestamp;

import models.User;
import views.ChatWindow;

public class TCPThread extends Thread{
	private Socket socket;
	private Boolean running = true;
	private Boolean waitforUser;
	private ChatWindow chat;
	private String receiver;
	private User current_user;
	private TCPConnect session_tcp;
	
	public TCPThread(TCPConnect tcp, Socket s,ChatWindow c, String r) {
		this.socket = s;
		this.chat = c;
		this.receiver = r;
		this.waitforUser = false;
		this.session_tcp = tcp;
		this.current_user = this.session_tcp.getCurrentUser();
		}
	public TCPThread(TCPConnect tcp,Socket s,ChatWindow c) {
		this.socket = s;
		this.chat = c;
		this.waitforUser = true;
		this.session_tcp = tcp;
		this.current_user = this.session_tcp.getCurrentUser();
		}
	public void setCurrentChat(ChatWindow c) {
		this.chat = c;
		System.out.println("Chat set to " + c);
	}
	public void close(){
		try {
			this.socket.close();
			System.out.println("Thread closed");
		} catch (SocketException se){
			System.out.println(""); 
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.running = false;
	}
	
	public InetAddress getAddress(){
		return socket.getInetAddress();
	}
	
	public Integer getPort(){
		return socket.getPort();
	}
	
	public void sendNickname(String message) {
		try {
			 PrintWriter writer = new PrintWriter(socket.getOutputStream(),true);
		     writer.write(message+"\n");
		     writer.flush();
		} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();	
		}
	}
	
	public void sendMessage(String message) {
		try {
			 PrintWriter writer = new PrintWriter(socket.getOutputStream(),true);
		     writer.write(message+"\n");
		     writer.flush();
		     //System.out.println(this.chat);
		     Timestamp date = new Timestamp(System.currentTimeMillis());
		     this.chat.displayMessage(this.current_user.getLogin(), message,date);
		     this.session_tcp.getDatabase().appendHistory( this.socket.getInetAddress().toString(),this.current_user.getAddress().toString(), message);
		} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();	
		
	  } catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
       
    }
	
	public ChatWindow getChat() {
		return this.chat;
	}
	public void run(){
		try {
			while(this.running) {
				//Socket socket_server = server.accept();
				BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String message = reader.readLine();
				
				if (!message.equals(null)) {
					if (this.waitforUser) {
						System.out.println("Received a nickname for ChatWindow config : " + message);
						//message = name of the receiver
						chat.setReceiver(message);
						chat.retrieveHistory();
						this.receiver = message;
						this.waitforUser = false;
					}
					else {
						System.out.println("message recu : " + message);
						System.out.println(getChat());
						Timestamp date = new Timestamp(System.currentTimeMillis());
						this.chat.displayMessage(receiver, message,date);
						
					}
				}
				//String messageReceived = reader.readLine();
				//chat.displayMessage(this.login,messageReceived);
				
             
			}
         
      } catch (SocketException se) {
     	 
      } catch (NullPointerException nullexc) {
    	  
      } catch (IOException e) {
    	  e.printStackTrace();
    
      } 
      
}
	public String getReceiver() {
		return this.receiver;
	}
		
}
