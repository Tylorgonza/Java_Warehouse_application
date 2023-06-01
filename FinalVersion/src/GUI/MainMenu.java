package GUI;
import billing_services.*;
import java.awt.EventQueue;
import java.sql.*;
import javax.swing.JFrame;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.Font;
import java.text.MessageFormat;
import javax.swing.SwingConstants;


public class MainMenu  {
	private String[] columnNames = {"id","email","Password"};
	private Object[][] data = {{null,null,null}};
	private Connection con;
	private JFrame frame;
	private DefaultTableModel dm;
	private JButton btnNewButtonAccess;
	private JButton btnNewButtonReg;
	static protected JTextField emailTF;
	static protected JTextField passTF;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainMenu window = new MainMenu();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
 MainMenu() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			//This allows us to access the database 
//			con = DriverManager.getConnection("jdbc:mysql://34.70.87.189:3306/userauth","root","access");
			con = DriverManager.getConnection("jdbc:mysql://34.70.87.189:3306/userauth", "root", "access"); // Connecting to local database instance

			//Statement st = con.createStatement();
			// result set allows us to sort through and use the data from the database
			//ResultSet rs = st.executeQuery("Select * from cars");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		frame = new JFrame();
		frame.setBounds(100,100,835,562);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		dm = new DefaultTableModel(data,columnNames);
		
		btnNewButtonAccess= new JButton("Access");
		btnNewButtonAccess.setBounds(387,419,124,30);
		btnNewButtonAccess.addActionListener(new Access());
		frame.getContentPane().add(btnNewButtonAccess);
		
		btnNewButtonReg = new JButton("Register");
		btnNewButtonReg.setBounds(387,364,124,30);
		btnNewButtonReg.addActionListener(new Register());
		frame.getContentPane().add(btnNewButtonReg);
		
		emailTF = new JTextField();
		emailTF.setBounds(380, 251, 153, 19);
		frame.getContentPane().add(emailTF);
		emailTF.setColumns(10);
		
		passTF = new JTextField();
		passTF.setBounds(380, 295, 153, 19);
		frame.getContentPane().add(passTF);
		passTF.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Email");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 19));
		lblNewLabel_1.setBounds(312, 248, 45, 19);
		frame.getContentPane().add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Password");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 19));
		lblNewLabel_2.setBounds(276, 291, 81, 20);
		frame.getContentPane().add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("ASAN Inventory Management");
		lblNewLabel_3.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_3.setFont(new Font("Tahoma", Font.PLAIN, 35));
		lblNewLabel_3.setBounds(109, 48, 585, 177);
		frame.getContentPane().add(lblNewLabel_3);
	}
	private class Access implements ActionListener {
		
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		try {
		//	boolean valid = UserAuth.accountAccess(con, emailTF.getText(), passTF.getText());
			//new UserAuth();
			if(emailTF.getText().isEmpty()||passTF.getText().isEmpty()) {
				JOptionPane.showMessageDialog(null, "Input Credentials","Problem",JOptionPane.INFORMATION_MESSAGE);
			}else {
				if(!UserAuth.accountAccess(con, emailTF.getText(), passTF.getText())){
					JOptionPane.showMessageDialog(null, "Wrong user or pass","Problem",JOptionPane.INFORMATION_MESSAGE);

				
				}else {
					JOptionPane.showMessageDialog(null, MessageFormat.format("Welcome {0}", MainMenu.emailTF.getText()),"Problem",JOptionPane.INFORMATION_MESSAGE);

						AccessMaterials access = new AccessMaterials();
						access.setVisible(true);
						System.out.println(UserAuth.accountAccess(con,  emailTF.getText(), passTF.getText()));}
				
			}

		}catch(Exception e1) {
			e1.printStackTrace();
		}
	
	}
	
	}
	private class Register  implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
		        //This allows us to access the database
//		        Connection con = DriverManager.getConnection("jdbc:mysql://34.70.87.189:3306/userauth","root","access");
				con = DriverManager.getConnection("jdbc:mysql://34.70.87.189:3306/userauth", "root", "access"); // Connecting to local database instance

		        if(MainMenu.emailTF.getText().isBlank()||MainMenu.passTF.getText().isBlank())
		        {
					JOptionPane.showMessageDialog(null, "Input Credentials","Problem",JOptionPane.INFORMATION_MESSAGE);
		        }
		        else {
		        	if(UserAuth.userExists(con, emailTF.getText()))
						JOptionPane.showMessageDialog(null, "User already exists","Problem",JOptionPane.INFORMATION_MESSAGE);

				UserAuth.createUser(con, MainMenu.emailTF.getText(), MainMenu.passTF.getText());

		        }
			}catch(Exception e1)
			{
				e1.printStackTrace();
			}
		}
		
		
	}	
	}