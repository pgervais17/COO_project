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
    //private Map<String,InfoMachine> connectedUsers = new HashMap<>();
    private ArrayList<User> connectedUsers = new ArrayList<User>();
    //private BroadcastUDP br = new BroadcastUDP();
    private DatagramSocket socket_envoi; 
    private DatagramSocket socket_reception;
    private byte[] buffer;
    private Integer broadcastPort = 6666;
    private Boolean running=true;
    private Boolean isLoginValid = true;
    
    public UDPConnect(User user){
        this.login = user.getLogin();
        this.port = user.getPort();
        this.address = user.getAddress();

        try {
            socket_envoi = new DatagramSocket(broadcastPort);
            socket_reception = new DatagramSocket(this.port);
            buffer = new byte[256];
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
    
    public void closeSession(){
    	System.out.println("Closing "+ getLogin()+ " session");
    	socket_envoi.close();
    	socket_reception.close();
    	running = false;
    }
    public ArrayList<User> getConnectedUsers() {
        return connectedUsers;
    }
    
    //method used to find the user connected associated to a certain login
    public User getUserConnected(String login){
    	for (User u : getConnectedUsers()){
    		if (u.getLogin().equals(login)){
    			return u;
    		}
    	}
    	return null;
    }
    public String[] getConnectedUsersName() {
    	String[] result = new String[connectedUsers.size()];
    	int i = 0;
    	for(User u : getConnectedUsers()) {
    		result[i] = u.getLogin();
    		i++;
    	}
    	return result;
    }
 
    public void printConnectedUsers(){
    	int i = 0;
    	System.out.println("Table des utilisateurs connect�s de " + getLogin());
    	while (i < getConnectedUsers().size())
    	{
    	    String name = getConnectedUsers().get(i).getLogin();
    	    InetAddress address = getConnectedUsers().get(i).getAddress();
    	    Integer port =getConnectedUsers().get(i).getPort();;
    	    System.out.println("User: "+ name + ", address: "+ address + ", port : " + port);
    	}
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
    
    public Boolean getIsLoginValid() {
    	return isLoginValid;
    }
    
    public void setIsLoginValid(Boolean b) {
    	this.isLoginValid = b;
    }
    
    public void sendLogin(InetAddress ipdest, Integer portdest){
    	String m = "ToVerify,"+getLogin()+","+port.toString(); 
    	//System.out.println(getLogin()+" is sending this: "+ m + " to address "+ ipdest +" on port "+ portdest);
        DatagramPacket message = new DatagramPacket(m.getBytes(),m.length(),ipdest,portdest);
        try {
			socket_envoi.send(message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void sendConnectedResponse(InetAddress ipdest, Integer portdest) {
    	String m = "ConnectedToo," + getLogin() + "," + getPort().toString();
    	//System.out.println(getLogin()+" is sending this: "+ m + " to address "+ ipdest +" on port "+ portdest);
    	DatagramPacket message = new DatagramPacket(m.getBytes(),m.length(),ipdest,portdest);
        try {
			socket_envoi.send(message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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


    public void sendMessageBroadcast(String message,Integer portdest){
    	//System.out.println(getLogin()+" is sending this: "+message+ " to port dest "+portdest);
        try {
                //envoi d'un message en broadcast pour connaitre les users connect�s
                //br.broadcast(message,getBroadcastAddress() );
     
            getSocketEnvoi().setBroadcast(true);  	  
            byte[] buffer = message.getBytes();
            //InetAddress broadcastaddress = InetAddress.getByName("255.255.255.255");
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length,getBroadcastAddress() ,portdest);
            getSocketEnvoi().send(packet);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*DatagramPacket message1 = new DatagramPacket(message.getBytes(),message.length(),InetAddress.getLocalHost(),3333);
        socket.send(message1);*/
    }

    public void run() {
    	//System.out.println(getLogin() +": server running");
        while(running) {
            DatagramPacket in = new DatagramPacket(buffer,buffer.length);
            try {
                //socket2 = new DatagramSocket(this.broadcastPort);
                this.getSocketReception().receive(in);
                InetAddress clientAddress = in.getAddress();
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
                
                //we create a User with the sender's information
                User client = new User(tokens[1],Integer.parseInt(tokens[2]));
                if(tokens[0].equals("Connected")) {
                    //ajout de l'utilisateur venant de se connecter
                    //connectedUsers.put(tokens[1], new InfoMachine(client, Integer.parseInt(tokens[2])));
                	connectedUsers.add(client);
                    //printConnectedUsers();
                    sendConnectedResponse(clientAddress,Integer.parseInt(tokens[2]));
                }
                if(tokens[0].equals("ConnectedToo")){
                	//connectedUsers.putIfAbsent(tokens[1], new InfoMachine(client, Integer.parseInt(tokens[2])));
                	connectedUsers.add(client);
                	//printConnectedUsers();
                }
                if (tokens[0].equals("Disconnected")) {
                    connectedUsers.remove(client);
                }
                if (tokens[0].equals("Verify") ) {
                    sendLogin(clientAddress,Integer.parseInt(tokens[2]));
                }
                if (tokens[0].equals("ToVerify")){
                	if (tokens[1].equals(getLogin())){
                		//System.out.println("This login is already used");
                		setIsLoginValid(false);
                	}
                	else {
                		//System.out.println("Login valid!");
                	}
                }
                //System.out.println(getLogin() + ": J'ai re�u un message sur le port " + getPort() + ": " + message);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
