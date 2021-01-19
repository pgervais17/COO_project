package connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

import models.User;
import views.ChatWindow;

public class TCPThread extends Thread{
	private Socket socket;
	private Boolean running = true;
	private Boolean waitforUser;
	private ChatWindow chat;
	private String receiver;
	
	public TCPThread(Socket s,ChatWindow c, String r) {
		this.socket = s;
		this.chat = c;
		this.receiver = r;
		this.waitforUser = false;
		}
	public TCPThread(Socket s,ChatWindow c) {
		this.socket = s;
		this.chat = c;
		this.waitforUser = true;
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
	
	public void sendMessage(String message,User sender) {
		try {
			 PrintWriter writer = new PrintWriter(socket.getOutputStream(),true);
		     writer.write(message+"\n");
		     writer.flush();
		     //System.out.println(this.chat);
		     this.chat.displayMessage(sender.getLogin(), message);
		} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();	
		}
			
       
    }
	
	public void afficher(String m){
		this.chat.displayMessage("", m);
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
						this.receiver = message;
						this.waitforUser = false;
					}
					else {
						System.out.println("message recu : " + message);
						System.out.println(getChat());
						chat.displayMessage(receiver, message);
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
