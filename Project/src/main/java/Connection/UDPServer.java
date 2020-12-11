package Connection;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UDPServer {
    private byte[] buffer;
    private DatagramPacket in;
    private  DatagramPacket out;
    private InetAddress clientAddress;
    private Integer clientPort;

    public static void main(String[] args) {
        try {
            DatagramSocket socket = new DatagramSocket(1234);
            byte[] buffer = new byte[256];
        } catch (SocketException e) {
            e.printStackTrace();
        }

    }
}
