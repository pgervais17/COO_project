package models;

import java.sql.Timestamp;


public class Message{
	private Timestamp timestamp;
	private String sender;
	private String receiver;
	private String content;
	
	public Message(String u1, String u2, String content, Timestamp date) {
		Set_Sender(u1);
		Set_Receiver(u2);
		Set_Content(content);
		Set_Timestamp(date);
			
			
	
	}
	public void Set_Sender(String s) {
		this.sender=s;
	}
	
	public void Set_Receiver(String r) {
		this.receiver=r;
	}
	
	public void Set_Timestamp(Timestamp t) {
		this.timestamp=t;
	}
	
	public void Set_Content(String c) {
		this.content=c;
	}
	
	public String Get_Sender() {
		return sender;
	}
	
	public String Get_Receiver() {
		return receiver;
	}
		
	public Timestamp Get_Timestamp() {
		return timestamp;
	}
	
	public String Get_Content() {
		return content;
	}
}
