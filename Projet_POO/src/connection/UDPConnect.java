package connection;

import models.User;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.net.*;
import java.net.NetworkInterface;

//classe qui permet de cr�er un type contenant une adresse ip et un port
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

//Classe utilis�e pour connaitre les utilisateurs en ligne
public class UDPConnect extends Thread {
    private String login;
    private Integer port;
    //port utilis� par tous les serveurs UDP pour �couter le broadcast
    private  InetAddress address;
    //private Hashtable<String, InetAddress> connectedUsers = new Hashtable();
    private Map<String,InfoMachine> connectedUsers = new HashMap<>();
    //private BroadcastUDP br = new BroadcastUDP();
    private DatagramSocket socket_envoi; 
    private DatagramSocket socket_reception;
    private byte[] buffer;
    private Integer broadcastPort = 6666;

    public UDPConnect(User user){
        this.login = user.getLogin();
        this.port = user.getPort();
        this.address = user.getAddress();

        try {
            socket_envoi = new DatagramSocket();
            socket_reception = new DatagramSocket(this.port);
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
    public Integer getPort(){
    	return port;
    }
    public DatagramSocket getSocketEnvoi(){
    	return socket_envoi;
    }
    public DatagramSocket getSocketReception(){
    	return socket_reception;
    }
    public void sendLogin(InetAddress ipdest, Integer portdest) throws IOException {
    	String m = "ToVerify,"+getLogin()+","+port.toString(); 
        DatagramPacket message = new DatagramPacket(m.getBytes(),m.length(),ipdest,portdest);
        socket_envoi.send(message);
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


    public void sendMessageBroadcast(String message,Integer portdest,UDPConnect udp){
    	System.out.println(getLogin()+" is sending this: "+message+ " to port dest "+portdest);
        try {
                //envoi d'un message en broadcast pour connaitre les users connect�s
                //br.broadcast(message,getBroadcastAddress() );
        	System.out.println("bloc try");
        	System.out.println(udp.getSocketEnvoi().toString());
        	
            udp.getSocketEnvoi().setBroadcast(true);
            System.out.println(udp.getSocketEnvoi().getBroadcast()+"*****");
            if(udp.getSocketEnvoi().getBroadcast()){
            	System.out.println("The broadcast is activated");
            	        } else{
            	System.out.println("The broadcast is not activated");
            	        };
            	  
            System.out.println("broadcast activated");
            byte[] buffer = message.getBytes();
            System.out.println("buffer created");
            InetAddress broadcastaddress = InetAddress.getByName("255.255.255.255");
            System.out.println(broadcastaddress);
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length,broadcastaddress ,portdest);
            System.out.println("packet created");
            udp.getSocketEnvoi().send(packet);
            System.out.println("message sent !");

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*DatagramPacket message1 = new DatagramPacket(message.getBytes(),message.length(),InetAddress.getLocalHost(),3333);
        socket.send(message1);*/
    }

    public void run() {
    	System.out.println(getLogin() +": server running");
        while(true) {
            DatagramPacket in = new DatagramPacket(buffer,buffer.length);
            try {
                //socket2 = new DatagramSocket(this.broadcastPort);
                this.getSocketReception().receive(in);
                InetAddress client = in.getAddress();
                Integer clientPort = in.getPort();
                // le message doit contenir deux �l�ments (le but du message et le login de l'envoyeur) s�par�s par une virgule
                String message = new String(in.getData(),0,in.getLength());
                // on d�finit le d�limiteur
                String delim = "[,]";
                //on s�pare le message et on cr�e un tableau de 3 �l�ments contenant chaque partie du message
                //tokens[0]: but du message
                //tokens[1]: login de l'envoyeur
                //tokens[2]: port utilis� par l'envoyeur (servira pour une future connexion tcp)
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
                if (tokens[0].equals("ToVerify")){
                	if (tokens[1].equals(getLogin())){
                		System.out.println("This login is already used");
                	}
                }
                System.out.println(this.login + ": J'ai re�u un message du port " +tokens[2] + ": " + message);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}