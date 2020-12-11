package Connection;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPConnect extends Thread{
    //client TCP qui initie une communication sur demande pour envoyer un message à un autre utilisateur
   private static InetAddress senderAddress;
    private InetAddress receiverAddress;
    private static Integer senderPort;
    private static Integer receiverPort;
    private static String message;

    public TCPConnect(InetAddress s, Integer sport, InetAddress r, Integer rport, String m) throws IOException {
        this.senderAddress = s;
        this.receiverAddress = r;
        this.senderPort = sport;
        this.receiverPort = rport;
        this.message = m;
    }

    public static void main(String[] args) {
        try {
            //Socket socket = new Socket(senderAddress, receiverPort);
            Socket socket = new Socket(InetAddress.getLocalHost(), 1234);
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            writer.write("Hello there");
            writer.flush();
            socket.close();
        } catch (IOException e) {
            System.exit(1);
        }

    }
}