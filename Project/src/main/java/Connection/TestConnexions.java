package Connection;

import Models.User;

import java.io.IOException;
import java.net.UnknownHostException;

public class TestConnexions {
    public static void main(String[] args) throws IOException, InterruptedException {
        User paul = new User("Paul",1234);
        User eva = new User("Eva",3333);
        UDPConnect udp1 = new UDPConnect(paul);
        UDPConnect udp2 = new UDPConnect(eva);
        udp1.start();
        udp2.start();
        udp1.sendMessageBroadcast("Connected,Paul");
        udp2.sleep(1000);
        System.out.println(udp2.getConnectedUsers().get("Paul").GetPort());

    }

}
