package Connection;
import java.io.IOException;
import java.net.*;
import java.lang.Exception;

public class BroadcastUDP {
    private static DatagramSocket socket = null;


    public static void broadcast(String broadcastMessage, InetAddress address) throws IOException {
        socket = new DatagramSocket();
        socket.setBroadcast(true);

        byte[] buffer = broadcastMessage.getBytes();

        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, 3333);
        DatagramPacket in = new DatagramPacket(buffer,buffer.length);
        socket.send(packet);
        socket.close();

    }
}

