package connection;

import models.User;

import java.io.IOException;
import java.net.InetAddress;

public class TestConnexions {
    public static void main(String[] args) throws IOException, InterruptedException {
        User cel = new User("Celestin");
        User trump = new User("Trump");
        User bitch = new User("Bitch");
        UDPConnect udp1 = new UDPConnect(cel);
        UDPConnect udp2 = new UDPConnect(trump);
        UDPConnect udp3 = new UDPConnect(bitch);
        udp1.start();
        udp2.start();
        udp3.start();
        udp2.sleep(1000);
        udp1.sleep(1000);
        udp2.printConnectedUsers();
        udp1.printConnectedUsers();
        //System.out.println(udp2.getConnectedUsers().get("Paul").GetPort()); 
        udp2.sleep(5000);
        udp1.sleep(5000);
        udp1.closeSession();
        udp2.closeSession();
    	/*TCPConnect tcp_paul = new TCPConnect(paul);
        TCPConnect tcp_eva = new TCPConnect(eva);
        tcp_eva.start();
        tcp_paul.start();
        tcp_eva.sendMessage("Coucou paul!", InetAddress.getLocalHost(),paul.getPort()); 
        tcp_eva.sendMessage("ça va ?", InetAddress.getLocalHost(),paul.getPort());*/
    	
    	/*User eva = new User("Eva");
    	UDPConnect udp = new UDPConnect(eva);
    	udp.start();
    	udp.sendMessageBroadcast("Connected,Eva,"+eva.getPort(), 6666);*/
    }

}
