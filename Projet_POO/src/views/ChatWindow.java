package views;

import java.awt.EventQueue;

import javax.swing.JFrame;

import models.User;
import javax.swing.JLabel;

import connection.TCPConnect;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ChatWindow {

	private JFrame frmChat;
	private static User sender;
	private static User receiver;
	private static TCPConnect tcp_session;
	private JTextField textField;
	//for now we only consider messages as strings
	private String message;
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
		TCPConnect tcp_receiver_session = new TCPConnect(receiver);
		tcp_receiver_session.start();
		initialize();
	}
	
	public void setSender(User u) {
		sender = u;
	}
	public void setReceiver(User u) {
		receiver = u;
	}
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmChat = new JFrame();
		frmChat.setTitle("Chat with " + receiver.getLogin());
		frmChat.setBounds(100, 100, 450, 300);
		frmChat.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frmChat.getContentPane().setLayout(null);
		
		JLabel lblAddress = new JLabel("Address: " + receiver.getAddress());
		lblAddress.setBounds(159, 48, 170, 14);
		frmChat.getContentPane().add(lblAddress);
		
		JLabel lblPort = new JLabel("Port: "+ receiver.getPort());
		lblPort.setBounds(159, 85, 170, 14);
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
						tcp_session.sendMessage(message, receiver.getAddress(), receiver.getPort());
						textField.setText(null);
						message = "";
					}
				} catch (Exception ex) {
					
				}
			}
		});
		btnNewButton.setBounds(310, 217, 89, 23);
		frmChat.getContentPane().add(btnNewButton);
		frmChat.setVisible(true);
		
	}
}
