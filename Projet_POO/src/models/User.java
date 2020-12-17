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
	
	public User(String l) throws UnknownHostException {
		// TODO Auto-generated constructor stub
		this.login=l;
		this.address = InetAddress.getLocalHost();
		this.port = obtainPort();
	}

	public Integer obtainPort(){
		 Random rand = new Random();
		 int randomPort = rand.nextInt((65535 - 1024) + 1) + 1024;
		 return randomPort;
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