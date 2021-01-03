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
	private ChatWindow chat;
	
	public TCPThread(Socket s) {
		this.socket = s;
		
	}
	public void setCurrentChat(ChatWindow c) {
		this.chat = c;
		System.out.println("Chat set to " + c);
	}
	public void close(){
		try {
			this.socket.close();
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
	
	public void sendMessage(String message,User sender) {
		try {
			 PrintWriter writer = new PrintWriter(socket.getOutputStream(),true);
		     writer.write(message+"\n");
		     writer.flush();
		     this.chat.displayMessage(sender.getLogin(), message);
		} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();	
		}
			
       
    }
	
	public void run(){
		try {
			while(this.running) {
				//Socket socket_server = server.accept();
				BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String message = reader.readLine();
				if (!message.equals(null)) {
					System.out.println(message);
					this.chat.displayMessage("", message);
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
		
}
