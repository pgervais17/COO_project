package Connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

//Server TCP qui reçoit des messages
public class TCPServer {
    private static Integer receiverPort;


    public TCPServer(Integer rport) throws IOException {

        this.receiverPort = rport;
    }

    public static void main(String[] args){
        try{
            //ServerSocket server = new ServerSocket(receiverPort);
            ServerSocket server = new ServerSocket(1234);
            Socket socket = server.accept();
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println(reader.readLine());
        } catch(IOException e) {
            System.exit(1);
        }
    }
}
