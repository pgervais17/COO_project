package models;

import java.security.Timestamp;


public class Message{
	private Timestamp timestamp;
	private int sender;
	private int receiver;
	private String text;
	
	public Message(int u1, int u2, String content, Timestamp date) {
		Set_Sender(u1);
		Set_Receiver(u2);
		Set_Content(content);
		Set_Timestamp(date);
			
			
	
	}
	public void Set_Sender(int s) {
		this.sender=s;
	}
	
	public void Set_Receiver(int r) {
		this.receiver=r;
	}
	
	public void Set_Timestamp(Timestamp t) {
		this.timestamp=t;
	}
	
	public void Set_Content(String content) {
		this.text=content;
	}
	
	public int Get_Sender() {
		return sender;
	}
	
	public int Get_Receiver() {
		return receiver;
	}
		
	public Timestamp Get_Timestamp() {
		return timestamp;
	}
	
	public String Get_Content() {
		return text;
	}
}
