package connection;

import models.User;

import java.io.IOException;
import java.net.InetAddress;

public class TestConnexions {
    public static void main(String[] args) throws IOException, InterruptedException {
        User paul = new User("Paul");
        User eva = new User("Eva");
        UDPConnect udp1 = new UDPConnect(paul);
        UDPConnect udp2 = new UDPConnect(eva);
        udp1.start();
        udp2.start();
        udp1.sendMessageBroadcast("Connected,Paul,"+paul.getPort(),eva.getPort(),udp1);
        udp2.sleep(1000);
        //System.out.println(udp2.getConnectedUsers().get("Paul").GetPort()); 

       /*TCPConnect tcp_paul = new TCPConnect(paul);
        TCPConnect tcp_eva = new TCPConnect(eva);
        tcp_eva.start();
        tcp_paul.start();
        tcp_eva.sendMessage("Coucou paul!", InetAddress.getLocalHost(),paul.getPort()); */
    }

}