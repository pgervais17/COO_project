package connection;

import models.User;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPConnect extends Thread{
    //client TCP qui initie une communication sur demande pour envoyer un message � un autre utilisateur
    private InetAddress address;
    private Integer port;
    private String login;
    private ServerSocket server;

    public TCPConnect(User user) {
        this.address = user.getAddress();
        this.port = user.getPort();
        this.login = user.getLogin();
        try {
			server = new ServerSocket(getPort());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public InetAddress getAddress() {
        return address;
    }

    public void sendMessage(String message, InetAddress ipdest, Integer portdest) {
        Socket socket_client;
		try {
			socket_client = new Socket(ipdest,portdest);
			 PrintWriter writer = new PrintWriter(socket_client.getOutputStream(),true);
		     writer.write(message);
		     writer.flush();
		     socket_client.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
    }

    public void run() {
        try {
        	while(true) {
        		Socket socket_server = server.accept();
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket_server.getInputStream()));
                System.out.println(reader.readLine());
        	}
            
         } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Integer getPort() {
        return port;
    }
}