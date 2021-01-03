package views;

import java.awt.EventQueue;

import javax.swing.JFrame;

import models.User;
import javax.swing.JLabel;

import connection.TCPConnect;
import connection.TCPThread;

import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import javax.swing.JTextArea;

public class ChatWindow {

	private JFrame frmChat;
	private static User sender;
	private static User receiver;
	private static TCPConnect tcp_session;
	private JTextField textField;
	//for now we only consider messages as strings
	private String message;
	
	private WindowAdapter windowAdapter = null;
	private JTextArea textArea;
	private TCPConnect tcp_receiver_session;
	private TCPThread currentThread;
	private TCPThread currentThread2;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChatWindow window = new ChatWindow(sender,receiver);
					window.frmChat.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ChatWindow(User s, User r) {
		setSender(s);
		setReceiver(r);
		tcp_session = new TCPConnect(sender);
		tcp_session.start();
		System.out.println("Session tcp started");
		//TEST
		tcp_receiver_session = new TCPConnect(receiver);
		tcp_receiver_session.start();
		
		tcp_session.connectTo(receiver);
		tcp_receiver_session.connectTo(sender);
		currentThread = tcp_session.getTCPThreadWith(receiver);
		System.out.println("found thread");
		currentThread.setCurrentChat(this);
		
		currentThread2 = tcp_receiver_session.getTCPThreadWith(sender);
		System.out.println("found thread");
		currentThread2.setCurrentChat(this);
		initialize();
	}
	
	
	
	public void setSender(User u) {
		sender = u;
	}
	public void setReceiver(User u) {
		receiver = u;
	}
	
	public void displayMessage(String whoSent,String m){
		textArea.append(whoSent + ": " + m + "\n");
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmChat = new JFrame();
		frmChat.setTitle("Chat with " + receiver.getLogin());
		frmChat.setBounds(100, 100, 450, 300);
		//frmChat.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frmChat.getContentPane().setLayout(null);
		
		this.windowAdapter = new WindowAdapter() {
	        // WINDOW_CLOSING event handler
	        @Override
	        public void windowClosing(WindowEvent e) {
	            super.windowClosing(e);
	            tcp_session.closeSession();
				tcp_receiver_session.closeSession();                   
	        }

	        // WINDOW_CLOSED event handler
	        @Override
	        public void windowClosed(WindowEvent e) {
	            super.windowClosed(e);
	            // Close application if you want to with System.exit(0)
	            // but don't forget to dispose of all resources 
	            // like child frames, threads, ...
	            // System.exit(0);
	        }
	    };
	    
	    frmChat.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	    frmChat.addWindowListener(this.windowAdapter);
	    
		JLabel lblAddress = new JLabel("Address: " + receiver.getAddress());
		lblAddress.setBounds(10, 11, 170, 14);
		frmChat.getContentPane().add(lblAddress);
		
		JLabel lblPort = new JLabel("Port: "+ receiver.getPort());
		lblPort.setBounds(10, 31, 170, 14);
		frmChat.getContentPane().add(lblPort);
		
		textField = new JTextField();
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				message = textField.getText();
			}
		});
		
		
		
		
		textField.setBounds(161, 207, 130, 43);
		frmChat.getContentPane().add(textField);
		textField.setColumns(10);
		JButton btnNewButton = new JButton("Send");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (message.length() > 0) {
						tcp_session.sendMessage(message, receiver);
						//displayMessage(sender.getLogin(),message);
						textField.setText(null);
						message = "";
					}
				} catch (Exception ex) {
					
				}
			}
		});
		btnNewButton.setBounds(310, 217, 89, 23);
		frmChat.getContentPane().add(btnNewButton); 
				
		textArea = new JTextArea();
		textArea.setBounds(123, 36, 192, 108);
		frmChat.getContentPane().add(textArea);
		
		JButton btnNewButton_1 = new JButton("test");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				tcp_receiver_session.sendMessage("Réponse!",sender);
			}
		});
		btnNewButton_1.setBounds(10, 180, 89, 23);
		frmChat.getContentPane().add(btnNewButton_1); 
		frmChat.setVisible(true);
		
	}
}
