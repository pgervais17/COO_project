package Connection;

import Models.User;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class TestConnexions {
    public static void main(String[] args) throws IOException, InterruptedException {
        User paul = new User("Paul",1234);
        User eva = new User("Eva",3333);
        /*UDPConnect udp1 = new UDPConnect(paul);
        UDPConnect udp2 = new UDPConnect(eva);
        udp1.start();
        udp2.start();
        udp1.sendMessageBroadcast("Connected,Paul,1234");
        udp2.sleep(1000);
        //System.out.println(udp2.getConnectedUsers().get("Paul").GetPort()); */

        TCPConnect tcp_paul = new TCPConnect(paul);
        TCPConnect tcp_eva = new TCPConnect(eva);
        tcp_eva.start();
        tcp_paul.start();
        tcp_eva.sendMessage("Coucou paul!", InetAddress.getLocalHost(),paul.getPort());
    }

}
