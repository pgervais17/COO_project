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
import java.util.regex.Pattern;

import models.User;
import views.ChatWindow;
import views.UserInterface;

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
		//cacher le chat jusqu'à vérification si il est déjà ouvert ou non
		this.chat.hide();
		this.waitforUser = true;
		this.session_tcp = tcp;
		this.current_user = this.session_tcp.getCurrentUser();
		}
	public void setCurrentChat(ChatWindow c) {
		this.chat = c;
	}
	public void setReceiver(String s) {
		this.receiver = s;
	}
	public void close(){
		try {
			this.socket.close();
		} catch (SocketException se){
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
		     //FORMATAGE
		     String ip1 = this.current_user.getAddress().toString();
		     String[] tokensVal = ip1.split("/");
		     ip1 = "/"+tokensVal[1];
		     String ip2 = this.socket.getInetAddress().toString();
		     String[] tokensVal2 = ip2.split("/");
		     ip2 = "/"+tokensVal2[1];
		     this.session_tcp.getDatabase().appendHistory( ip2,ip1, message);
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
						//message = name of the receiver
						UserInterface ui = this.session_tcp.getUserInterface();
						if(!ui.isChatStarted(message)) {
							//le chat n'a pas déjà été lancé avant donc on peut le montrer
							chat.setReceiver(message);
							chat.retrieveHistory();
							this.chat.show();
							ui.addChatStarted(getChat());
							this.receiver = message;
							this.chat.setCurrentThread(this);
							this.waitforUser = false;
						}
						
					}
					else {
						Timestamp date = new Timestamp(System.currentTimeMillis());
						this.chat.displayMessage(receiver, message,date);
						
					}
				}

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
