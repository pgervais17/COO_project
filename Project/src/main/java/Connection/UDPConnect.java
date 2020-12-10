package Connection;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Hashtable;
import java.net.*;
import java.net.NetworkInterface;
import java.util.List;

//Classe utilisée pour connaitre les utilisateurs en ligne
public class UDPConnect extends Thread {
    private String login;
    private Hashtable<String, InetAddress> connectedUsers = new Hashtable();
    private BroadcastUDP br = new BroadcastUDP();

    private NetworkInterface nif;

    {
        try {
            nif = NetworkInterface.getByIndex(1);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    private List<InterfaceAddress> list = nif.getInterfaceAddresses();

    public void run(){
        while(true) {
            try {
                for(InterfaceAddress iaddr : list) {
                    //envoi d'un message en broadcast pour connaitre les users connectés
                    br.broadcast(login,iaddr.getBroadcast() );
                    //remplissage de la table avec les users ayant répondu
                    connectedUsers.putIfAbsent(br.GetMachineLogin(),br.GetMachineAddress());
                }
                sleep(10000);


            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
