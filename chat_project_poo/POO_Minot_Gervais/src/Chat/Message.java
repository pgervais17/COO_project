package Chat;

import java.util.Date;

public class Message {
	private Date timestamp;
	private User sender;
	private User receiver;
	//private type ? content;
	
	public Message(User u1, User u2) {
		Set_Sender(u1);
		Set_Receiver(u2);
		Set_Timestamp(new Date());
			
			
		// TODO Auto-generated constructor stub
	}
	public void Set_Sender(User s) {
		this.sender=s;
	}
	
	public void Set_Receiver(User r) {
		this.receiver=r;
	}
	
	public void Set_Timestamp(Date t) {
		this.timestamp=t;
	}
	
	public User Get_Sender() {
		return sender;
	}
	
	public User Get_Receiver() {
		return receiver;
	}
		
	public Date Get_Date() {
		return timestamp;
	}
}
