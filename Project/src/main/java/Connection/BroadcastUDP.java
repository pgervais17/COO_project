package Connection;
import java.io.IOException;
import java.net.*;
import java.lang.Exception;

public class BroadcastUDP {
    private static DatagramSocket socket = null;
    private static InetAddress machineAddress;
    private static Integer machinePort;
    private static String machineLogin;
    public static void main(String[] args) {
        try {
            broadcast("Hello", InetAddress.getByName("255.255.255.255"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void broadcast(String broadcastMessage, InetAddress address) throws IOException {
        socket = new DatagramSocket();
        socket.setBroadcast(true);

        byte[] buffer = broadcastMessage.getBytes();

        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, 4445);
        DatagramPacket in = new DatagramPacket(buffer,buffer.length);
        socket.send(packet);
        //réponse recue
        socket.receive(in);
        machineAddress = in.getAddress();
        machinePort = in.getPort();
        machineLogin = new String(in.getData(),0,in.getLength());
        socket.close();

    }

    public InetAddress GetMachineAddress(){
        return this.machineAddress;
    }
    public Integer GetMachinePort(){
        return this.machinePort;
    }
    public String GetMachineLogin(){
        return this.machineLogin;
    }
}
