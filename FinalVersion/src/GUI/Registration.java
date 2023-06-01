package GUI;

import billing_services.*;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

import javax.swing.JTextField;
import javax.swing.JButton;

public class Registration extends JFrame  {

	private Connection con;
	private JPanel contentPane;
	private JTextField emailRTF;
	private JTextField passRTF;
	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					 Window frame = new Registration();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Registration() {
		setBounds(100, 100, 450, 300);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Registration");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 28));
		lblNewLabel.setBounds(140, 11, 146, 65);
		contentPane.add(lblNewLabel);
		
		emailRTF = new JTextField();
		emailRTF.setBounds(163, 118, 146, 20);
		contentPane.add(emailRTF);
		emailRTF.setColumns(10);
		
		passRTF = new JTextField();
		passRTF.setBounds(163, 159, 146, 20);
		contentPane.add(passRTF);
		passRTF.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Email");
		lblNewLabel_1.setBounds(107, 121, 46, 14);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Password");
		lblNewLabel_2.setBounds(107, 162, 46, 14);
		contentPane.add(lblNewLabel_2);
		
		JButton btnNewButtonReg = new JButton("Register");
		btnNewButtonReg.setBounds(197, 208, 89, 23);
		btnNewButtonReg.addActionListener(new createUser());
		contentPane.add(btnNewButtonReg);

	}
	private class createUser implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
		        //This allows us to access the database
//		        Connection con = DriverManager.getConnection("jdbc:mysql://34.70.87.189:3306/userauth","root","access");
				Connection con = DriverManager.getConnection("jdbc:mysql://34.70.87.189:3306/userauth", "root", "access"); // Connecting to local database instance

		        if(emailRTF.getText().isBlank()||passRTF.getText().isBlank())
		        {
					JOptionPane.showMessageDialog(null, "Input Credentials","Problem",JOptionPane.INFORMATION_MESSAGE);
		        }
		        else {
		        	if(UserAuth.userExists(con, emailRTF.getText()))
						JOptionPane.showMessageDialog(null, "User already exists","Problem",JOptionPane.INFORMATION_MESSAGE);

				UserAuth.createUser(con, emailRTF.getText(), passRTF.getText());
		        }
			}catch(Exception e1)
			{
				e1.printStackTrace();
			}
		}
		
	}
	}