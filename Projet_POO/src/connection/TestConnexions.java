package connection;

import models.User;

import java.io.IOException;
import java.net.InetAddress;

public class TestConnexions {
    public static void main(String[] args) throws IOException, InterruptedException {
        User paul = new User("Paul");
        User eva = new User("Eva");
        System.out.println(paul.getLogin()+": "+ paul.getPort()+ ", " + paul.getAddress());
		System.out.println(eva.getLogin()+": "+eva.getPort() + ", "+ eva.getAddress());
        /*UDPConnect udp1 = new UDPConnect(paul);
        UDPConnect udp2 = new UDPConnect(eva);
        udp1.start();
        udp2.start();
        udp1.sendMessageBroadcast("Connected,Paul,"+paul.getPort(),eva.getPort());
        udp2.sleep(5000);
        udp1.sleep(5000);
        System.out.println("Table des utilisateurs connect�s de paul :");
        udp1.printConnectedUsers();
        System.out.println("Table des utilisateurs connect�s de Eva :");
        udp2.printConnectedUsers();
        //System.out.println(udp2.getConnectedUsers().get("Paul").GetPort()); 
        udp1.closeSession();
        udp2.closeSession();*/
    	TCPConnect tcp_paul = new TCPConnect(paul);
        TCPConnect tcp_eva = new TCPConnect(eva);
        tcp_eva.start();
        tcp_paul.start();
        tcp_eva.sendMessage("Coucou paul!", InetAddress.getLocalHost(),paul.getPort()); 
    }

}
