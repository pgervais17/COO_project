package Connection;

import Models.User;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPConnect extends Thread{
    //client TCP qui initie une communication sur demande pour envoyer un message à un autre utilisateur
    private InetAddress address;
    private Integer port;
    private String login;
    private ServerSocket server;

    public TCPConnect(User user) throws IOException {
        this.address = user.getAddress();
        this.port = user.getPort();
        this.login = user.getLogin();
        server = new ServerSocket(getPort());
    }

    public InetAddress getAddress() {
        return address;
    }

    public void sendMessage(String message, InetAddress ipdest, Integer portdest) throws IOException {
        Socket socket_client = new Socket(ipdest,portdest);
        PrintWriter writer = new PrintWriter(socket_client.getOutputStream(),true);
        writer.write(message);
        writer.flush();
        socket_client.close();
    }

    public void run() {
        try {
            Socket socket_server = server.accept();
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket_server.getInputStream()));
            System.out.println(reader.readLine());
         } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Integer getPort() {
        return port;
    }
}