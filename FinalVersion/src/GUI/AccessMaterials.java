package GUI;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import billing_services.Database;
import billing_services.PDF_maker;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.JTextPane;
import javax.swing.JList;
import java.awt.ScrollPane;
import javax.swing.ScrollPaneConstants;
import java.util.List;


public class AccessMaterials extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private String[] columnNames = {"id","item","unitprice","inventory"};
	private Object data[][]= {{null,null,null,null}};
	private Object Cart[][]= {{null,null,null,null}};
	private DefaultTableModel dm;

	private JScrollPane scrollPane;
	private JTable table;
	private static Connection con;
	private JTextField itemCartId;
	private JTextField newEntryMaterial;
	private JTextField newEntryUP;
	private JTextField newEntryInv;
	private JLabel ID;
	private JLabel lblNewLabel_3;
	private JTextField amountID;
	private ArrayList<String>currentCart=new ArrayList();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AccessMaterials frame = new AccessMaterials();
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
	public AccessMaterials() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			//This allows us to access the database 
//			con = DriverManager.getConnection("jdbc:mysql://34.70.87.189:3306/materials","root","access");
			 con = DriverManager.getConnection("jdbc:mysql://34.70.87.189:3306/materials", "root", "access"); // Connecting to local database instance

			//Statement st = con.createStatement();
			// result set allows us to sort through and use the data from the database
			//ResultSet rs = st.executeQuery("Select * from cars");
		}catch(Exception e) {
			e.printStackTrace();
		}
		setBounds(100, 100, 990, 583);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 964, 309);
		contentPane.add(scrollPane);
		
		table = new JTable();
		scrollPane.setViewportView(table);
		dm = new DefaultTableModel(data,columnNames);
		table.setModel(dm);
		
		JButton show = new JButton("Show Materials");
		show.setBounds(10, 319, 134, 23);
		show.addActionListener(new Search());
		contentPane.add(show);
		
		JLabel lblNewLabel = new JLabel("Add to cart by \"id\"");
		lblNewLabel.setBounds(10, 369, 185, 14);
		contentPane.add(lblNewLabel);
		
		itemCartId = new JTextField();
		itemCartId.setBounds(58, 394, 86, 20);
		contentPane.add(itemCartId);
		itemCartId.setColumns(10);
		
		JButton addToCart = new JButton("Add");
		addToCart.setBounds(58, 477, 86, 23);
		addToCart.addActionListener(new AddToCart());
		contentPane.add(addToCart);
		
		JLabel lblNewLabel_1 = new JLabel("Cart");
		lblNewLabel_1.setBounds(779, 369, 107, 14);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Add new material(material/unitprice/inventory)");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblNewLabel_2.setBounds(292, 312, 296, 35);
		contentPane.add(lblNewLabel_2);
		
		JButton addDB = new JButton("ADD");
		addDB.setBounds(875, 320, 89, 23);
		//addDB.addActionListener(new AddMaterials());
		contentPane.add(addDB);
		
		newEntryMaterial = new JTextField();
		newEntryMaterial.setBounds(587, 320, 86, 20);
		contentPane.add(newEntryMaterial);
		newEntryMaterial.setColumns(10);
		
		newEntryUP = new JTextField();
		newEntryUP.setBounds(683, 320, 86, 20);
		contentPane.add(newEntryUP);
		newEntryUP.setColumns(10);
		
		newEntryInv = new JTextField();
		newEntryInv.setBounds(779, 321, 86, 20);
		contentPane.add(newEntryInv);
		newEntryInv.setColumns(10);
		
		ID = new JLabel("ID");
		ID.setBounds(20, 397, 46, 14);
		contentPane.add(ID);
		
		lblNewLabel_3 = new JLabel("Quantity");
		lblNewLabel_3.setBounds(2, 449, 46, 14);
		contentPane.add(lblNewLabel_3);
		
		amountID = new JTextField();
		amountID.setText("0");
		amountID.setBounds(58, 446, 86, 20);
		contentPane.add(amountID);
		amountID.setColumns(10);
		
		JButton btnNewButton = new JButton("Invoice");
		btnNewButton.setBounds(745, 393, 89, 23);
		btnNewButton.addActionListener(new Invoice());
		contentPane.add(btnNewButton);
		
		
		JButton invoiceBtn = new JButton("Invoice");
		invoiceBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		invoiceBtn.setBounds(758, 506, 89, 23);
		
		

	}
	
	private class AddToCart implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {

			try {
				//NEED to grab current INV
				int inv= HelperMethods.currentInventory(con,itemCartId.getText() );
				
				ArrayList<String>individualMaterial=new ArrayList();

				
				//THis is the update part
					Statement st;
					
					st = con.createStatement();
					st.executeUpdate("UPDATE rawmaterials SET `inventory` = '"+(inv-Integer.valueOf(amountID.getText().toString()))+"' WHERE (`id` = '"+itemCartId.getText()+"');"); // Statement result stored in result set object
					 ResultSet rs2 = st.executeQuery("SELECT * FROM rawmaterials");
					int i;
					
					data = new Object[100][4];
					for(i=0;rs2.next();i++) {
						data[i][0] = rs2.getString("id");
						data[i][1] = rs2.getString("name");
						data[i][2] = rs2.getString("unitprice");
						data[i][3] = rs2.getString("inventory");
						inv= Integer.valueOf( rs2.getString("inventory"));
						
					}
					try {
						individualMaterial.add(Database.getItemWithIdCart(con,itemCartId.getText(),Integer.valueOf(amountID.getText())));
						currentCart.addAll(individualMaterial);	
						for (int index=0; index<currentCart.size(); index++) {
							
							String current_element=currentCart.get(index);
							String[] elems=current_element.split(" ");
							List<String>data=Arrays.asList(elems); 
							ArrayList<String>listS=new ArrayList<String>(data);
							System.out.println(listS);
							}
					} catch (ClassNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					//i->number of rows
					Object data2[][]= new Object[i][4];
					for(int j=0;j<i;j++) {
						for(int k = 0; k<4;k++)
							data2[j][k]=data[j][k];
					}
					data=data2;//data now points to same object as data2
					
					dm.setDataVector(data, columnNames);
					dm.fireTableDataChanged();
					
			}catch(SQLException e1) {
				e1.printStackTrace();
			}
		}
		
	}
	private class Search implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			Statement st;
			
			try {
				Connection con = DriverManager.getConnection("jdbc:mysql://34.70.87.189:3306/materials", "root", "access"); // Connecting to local database instance
				st = con.createStatement();
				// result set allows us to sort through and use the data from the database
				ResultSet rs = st.executeQuery("Select * from rawmaterials");
				data = new Object[100][5];
				int i;
				for(i=0;rs.next();i++) {
					data[i][0] = rs.getString("id");
					data[i][1] = rs.getString("name");
					data[i][2] = rs.getString("unitprice");
					data[i][3] = rs.getString("inventory");
					//...
				}
				//i->number of rows
				Object data2[][]= new Object[i][5];
				for(int j=0;j<i;j++) {
					for(int k =0; k<5;k++)
						data2[j][k]=data[j][k];
				}
				data=data2;//data now points to same object as data2
				
				dm.setDataVector(data, columnNames);
				dm.fireTableDataChanged();
				
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	
	}
	
	
	//Update inventory after selling some
	//this goes to the add button by add to cart
	/*private class AddtoCart implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			try {
				HelperMethods.updateInventory(con, itemCartId.getText(),Integer.valueOf(amountID.getText()));
			} catch (NumberFormatException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
		
	}*/
	
	
	/*private class AddMaterials implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			try {
				Database.insertItem(con, newEntryMaterial.getText(),Double.valueOf(newEntryUP.getText()) , Integer.valueOf(newEntryInv.getText()));
				new Search();
				
			} catch (NumberFormatException | ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				}
			}
		
		}*/
	
	private class Invoice implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			PDF_maker pm= new PDF_maker(currentCart, MainMenu.emailTF.getText());
		}
		
		
	}
	
	
	private class HelperMethods{
		
		public static JList listPopulator(String itemName) {
			DefaultListModel<String> listModel = new DefaultListModel<>();
			listModel.addElement(itemName);
			
			JList list = new JList(listModel);
			list.setVisible(true);
			list.setVisibleRowCount(4);
			list.setBounds(655, 396, 249, 77);
			
			return list;
		}
		//Update inventory after selling some
		public static int updateInventory  (Connection connection, String id, int soldItems) throws ClassNotFoundException {
			int inventory=0;
			try {
				String query= "SELECT inventory FROM rawmaterials WHERE id=?";
				PreparedStatement statement= connection.prepareStatement(query); // Preparing query
				statement.setString(1,id);
				ResultSet rs = statement.executeQuery();
				if (rs.next()== true) { 
					do { 
						inventory=rs.getInt("inventory")-soldItems;
						System.out.println(inventory);
					} while (rs.next()); 
				} else { 
					System.out.println("Item does not exist");
				}

				
				
			}catch(SQLException ev){
				ev.printStackTrace();
			}
			
			return inventory;

			
		}
		
		public static int  currentInventory(Connection connection, String id) {
			int inventory=0;
			try {
				String query= "SELECT inventory FROM rawmaterials WHERE id=?";
				PreparedStatement statement= connection.prepareStatement(query); // Preparing query
				statement.setString(1,id);
				ResultSet rs = statement.executeQuery();
				if (rs.next()== true) { 
					do { 
						inventory=rs.getInt("inventory");
						System.out.println(inventory);
					} while (rs.next()); 
				} else { 
					System.out.println("Item does not exist");
				}

				
				
			}catch(SQLException ev){
				ev.printStackTrace();
			}
			
			return inventory;

			
		}
	}
	}
