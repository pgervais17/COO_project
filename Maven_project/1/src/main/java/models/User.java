package models;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

import connection.UDPConnect;

public class User {
	private String login;
	private InetAddress address;
	private Integer port;
	private Boolean status;
	
	//constructor used when we want to give a random port to the created user
	public User(String l) {
		// TODO Auto-generated constructor stub
		this.login=l;
		try {
			this.address = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.port = obtainPort();
	}
	//constructor used when we already know which port needs to be used by the user
	public User(String l, Integer p) throws UnknownHostException {
		// TODO Auto-generated constructor stub
		this.login=l;
		this.address = InetAddress.getLocalHost();
		this.port = p;
	}
	
	public User(String l, Integer p, InetAddress address) throws UnknownHostException {
		// TODO Auto-generated constructor stub
		this.login=l;
		this.address = address;
		this.port = p;
	}

	public Integer obtainPort(){
		 Random rand = new Random();
		 int randomPort = rand.nextInt((65535 - 1024) + 1) + 1024;
		 return randomPort;
	}
	
	public User getUser(String name){
		if (name.equals(login)){
			return this;
		}
		else {
			return null;
		}
	}
	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public void Set_Status(Boolean s) {
		this.status=s;
	}

	
	public Boolean Get_Status() {
		return status;
	}

	public Integer getPort() {
		return port;
	}

	public InetAddress getAddress() {
		return address;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public void setAddress(InetAddress address) {
		this.address = address;
	}
}
