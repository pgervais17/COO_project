package connection;

import models.User;
import views.UserInterface;

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
    private User current_user;
    //port utilis� par tous les serveurs UDP pour �couter le broadcast
    private  InetAddress address;
    private ArrayList<User> connectedUsers = new ArrayList<User>();
    private DatagramSocket socket_envoi; 
    private DatagramSocket socket_reception;
    private byte[] buffer;
    private Integer broadcastPort = 6666;
    private Boolean running=true;
    private Boolean isLoginValid = true;
    private UserInterface userInterface = null;
    private String newLogin;
    
    public UDPConnect(User user){
        this.login = user.getLogin();
        this.port = user.getPort();
        this.address = user.getAddress();
        this.current_user = user;

        try {
            socket_envoi = new DatagramSocket(this.port);
            socket_reception = new DatagramSocket(broadcastPort);
            buffer = new byte[256];
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
    
    public void setUserInterface(UserInterface ui){
    	this.userInterface = ui;
    }
    public UserInterface getUserInterface(){
    	return this.userInterface;
    }
    public void closeSession(){
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
    //method used to find a user by its login
    public User getUserByName(String name){
    	User result = null;
    	for (User u : getConnectedUsers()){
    		if (u.getLogin().equals(name)){
    			result = u;
    			break;
    		}
    	}
    	return result;
    }
    public void printConnectedUsers(){
    	for (User u : this.connectedUsers) {
    	    String name = u.getLogin();
    	    InetAddress address = u.getAddress();
    	    Integer port =u.getPort();
    	}
    }
    public String getLogin() {
        return login;
    }
    public void setLogin(String l) {
    	this.current_user.setLogin(l);
    	this.login = l;
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
        DatagramPacket message = new DatagramPacket(m.getBytes(),m.length(),ipdest,portdest);
        try {
			socket_envoi.send(message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    //utilis�e quand on veut changer de pseudo en cours d'application
    public void sendNewLogin(String login,InetAddress ipdest){
    	String m = "ToVerifyNewLogin,"+getLogin()+","+port.toString()+","+login; 
        DatagramPacket message = new DatagramPacket(m.getBytes(),m.length(),ipdest,this.broadcastPort);
        try {
			socket_envoi.send(message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void sendConnectedResponse(InetAddress ipdest) {
    	String m = "ConnectedToo," + getLogin() + "," + getPort().toString();
    	//System.out.println(getLogin()+" is sending this: "+ m + " to address "+ ipdest +" on port "+ portdest);
    	DatagramPacket message = new DatagramPacket(m.getBytes(),m.length(),ipdest,this.broadcastPort);
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


    public void sendMessageBroadcast(String message){
        try {
                //envoi d'un message en broadcast pour connaitre les users connect�s
            getSocketEnvoi().setBroadcast(true);  	  
            byte[] buffer = message.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length,InetAddress.getByName("255.255.255.255") ,this.broadcastPort);
            getSocketEnvoi().send(packet);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {

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
                User client = new User(tokens[1],Integer.parseInt(tokens[2]), clientAddress);
                //System.out.println("Port re�u : " + tokens[2] + ", port de l'utilisateur courant : " + this.port);
                if (!tokens[2].equals(Integer.toString(this.port)) && !clientAddress.equals(this.address)){
                	if(tokens[0].equals("Connected")) {
                		//ajout de l'utilisateur venant de se connecter
                		//connectedUsers.put(tokens[1], new InfoMachine(client, Integer.parseInt(tokens[2])));
                		connectedUsers.add(client);
                		try {
                			this.userInterface.updateListUsersAvailable();
                		} catch (NullPointerException e){
                		
                		};
                		//printConnectedUsers();
                		sendConnectedResponse(clientAddress);
                	}
                	else if(tokens[0].equals("ConnectedToo")){
                		//connectedUsers.putIfAbsent(tokens[1], new InfoMachine(client, Integer.parseInt(tokens[2])));
                		connectedUsers.add(client);
                		try {
                			this.userInterface.updateListUsersAvailable();
                		} catch (NullPointerException e){
                		
                		};
                		//printConnectedUsers();
                	}
                	else if (tokens[0].equals("Disconnected")) {
                		this.userInterface.userDisconnected(client.getLogin());
                		this.connectedUsers.remove(getUserByName(client.getLogin()));
                		try {
                			this.userInterface.updateListUsersAvailable();
                		} catch (NullPointerException e){
                		
                		};
                	}
                	else if (tokens[0].equals("Verify") ) {
                		sendLogin(clientAddress,this.broadcastPort);
                	}
                	else if (tokens[0].equals("ToVerify")){
                		if (tokens[1].equals(getLogin())){
                			setIsLoginValid(false);
                		}
                	}
                	else if (tokens[0].equals("VerifyNewLogin") ){
                		//on envoie ce message pour v�rifier un nouveau login qui est tokens[3]
                		
                		sendNewLogin(tokens[3],clientAddress);
                	}
                	else if (tokens[0].equals("ToVerifyNewLogin")){
                		if (tokens[3].equals(tokens[1])){
                			setIsLoginValid(false);
                			System.out.println(getIsLoginValid());
                		} 
                	}
                	else if (tokens[0].equals("NotifyChange")){
                		//message particulier utilis� pour notifier un changement de pseudo
                		//ici tokens[1] est l'ancien pseudo, tokens[3] est le nouveau pseudo
                		String previousLogin = tokens[1];
                		String newLogin = tokens[3];
                		User u = getUserByName(previousLogin);
                		u.setLogin(newLogin);
                		
                		this.userInterface.popUpChangeLogin(previousLogin, newLogin);
                		try {
                			this.userInterface.updateListUsersAvailable();
                		} catch (NullPointerException e){
                		
                		};
                	}
                	
                        //System.out.println(getLogin() + ": J'ai re�u un message sur le port " + getPort() + ": " + message);
                }
            } catch (SocketException se){
            	
            } catch (IOException e) {
                e.printStackTrace();
            };

        }
    }
}
       


