package Connection;

import Models.User;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.net.*;
import java.net.NetworkInterface;

//classe qui permet de créer un type contenant une adresse ip et un port
class InfoMachine {
    private InetAddress address;
    private Integer port;

    public InfoMachine(InetAddress ip, Integer p) {
        this.address = ip;
        this.port = p;
    }
    public InetAddress GetAddress() {
        return this.address;
    }
    public Integer GetPort(){
        return this.port;
    }
    public void SetAddress(InetAddress ip) {
        this.address = ip;
    }
    public void SetPort(Integer p) {
        this.port = p;
    }
}

//Classe utilisée pour connaitre les utilisateurs en ligne
public class UDPConnect extends Thread {
    private String login;
    private Integer port;
    //port utilisé par tous les serveurs UDP pour écouter le broadcast
    private  InetAddress address;
    //private Hashtable<String, InetAddress> connectedUsers = new Hashtable();
    private Map<String,InfoMachine> connectedUsers = new HashMap<>();
    //private BroadcastUDP br = new BroadcastUDP();
    private DatagramSocket socket1; //envoi broadcast
    private DatagramSocket socket2; //réception broadcast
    private byte[] buffer;
    private Integer broadcastPort = 6666;

    public UDPConnect(User user){
        this.login = user.getLogin();
        this.port = user.getPort();
        this.address = user.getAddress();

        try {
            socket1 = new DatagramSocket(this.port);
            buffer = new byte[256];
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public Map<String, InfoMachine> getConnectedUsers() {
        return connectedUsers;
    }

    public String getLogin() {
        return login;
    }

    public void sendLogin(InetAddress ipdest, Integer portdest) throws IOException {
        DatagramPacket message = new DatagramPacket(getLogin().getBytes(StandardCharsets.UTF_8),getLogin().length(),ipdest,portdest);
        socket1.send(message);
    }

    public InetAddress getBroadcastAddress() throws SocketException {
        InetAddress broadcastAddress = null;
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements())
        {
            NetworkInterface networkInterface = interfaces.nextElement();
            if (networkInterface.isLoopback())
                continue;    // Do not want to use the loopback interface.
            for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses())
            {
                InetAddress broadcast = interfaceAddress.getBroadcast();
                if (broadcast == null)
                    continue;

                broadcastAddress = broadcast;
            }
        }
        return broadcastAddress;
    }


    public void sendMessageBroadcast(String message) throws IOException {
        try {
                //envoi d'un message en broadcast pour connaitre les users connectés
                //br.broadcast(message,getBroadcastAddress() );
            this.socket1.setBroadcast(true);

            byte[] buffer = message.getBytes();

            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, getBroadcastAddress(),this.port);
            socket1.send(packet);
            System.out.println("OK");

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*DatagramPacket message1 = new DatagramPacket(message.getBytes(),message.length(),InetAddress.getLocalHost(),3333);
        socket.send(message1);*/
    }

    public void run() {
        while(true) {
            DatagramPacket in = new DatagramPacket(buffer,buffer.length);
            try {
                //socket2 = new DatagramSocket(this.broadcastPort);
                socket1.receive(in);
                InetAddress client = in.getAddress();
                Integer clientPort = in.getPort();
                // le message doit contenir deux éléments (le but du message et le login de l'envoyeur) séparés par une virgule
                String message = new String(in.getData(),0,in.getLength());
                // on définit le délimiteur
                String delim = "[,]";
                //on sépare le message et on crée un tableau de 3 éléments contenant chaque partie du message
                //tokens[0]: but du message
                //tokens[1]: login de l'envoyeur
                //tokens[2]: port utilisé par l'envoyeur (servira pour une future connexion tcp)
                String[] tokens = message.split(delim);
                if(tokens[0].equals("Connected")) {
                    //ajout de l'utilisateur venant de se connecter
                    connectedUsers.put(tokens[1], new InfoMachine(client, Integer.parseInt(tokens[2])));
                }
                if (tokens[0].equals("Disconnected")) {
                    connectedUsers.remove(tokens[1]);
                }
                if (tokens[0].equals("Verify") ) {
                    sendLogin(client,clientPort);
                }
                System.out.println(tokens[1] + "J'ai reçu un message du port " +tokens[2] + ": " + message);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}