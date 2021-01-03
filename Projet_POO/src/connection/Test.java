package connection;

import models.User;

public class Test {
	
	public static void main(String[] args) {
		User eva = new User("eva");
		User paul = new User("paul");
		User john = new User("john");
		
		TCPConnect tcp1 = new TCPConnect(eva);
		tcp1.start();
		TCPConnect tcp2 = new TCPConnect(paul);
		TCPConnect tcp3 = new TCPConnect(john);
		tcp2.start();
		tcp3.start();
		tcp2.connectTo(eva);
		tcp2.sendMessage("hey", eva);
		tcp3.connectTo(eva);
		tcp3.sendMessage("yoo", eva);
		tcp1.sendMessage("heyezd", paul);
	}
	
}
