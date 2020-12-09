package Models;

public class User {
	private String pseudo;
	private String password;
	private Boolean status;
	
	public User(String login, String pwd) {
		// TODO Auto-generated constructor stub
		this.pseudo=login;
		this.password=pwd;		
	}
	
	public void Set_Pseudo(String p) {
		this.pseudo=p;
	}
	
	public void Set_Password(String p) {
		this.password=p;
	}

	public void Set_Status(Boolean s) {
		this.status=s;
	}
	
	public String Get_Pseudo() {
		return pseudo;
	}
	
	public String Get_Password() {
		return password;
	}
	
	public Boolean Get_Status() {
		return status;
	}
}
