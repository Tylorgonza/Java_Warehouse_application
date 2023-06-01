package billing_services;
import java.util.ArrayList;
import java.util.UUID;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.MessageFormat;

public class Database {
	
	
	static ArrayList<String> cartArr = new ArrayList<String>();
	
	//Creates/inserts record or item to database
	public static  void insertItem(Connection connection, String name, double unitprice, int inventory) throws ClassNotFoundException{
		try{
			String id=UUID.randomUUID().toString().substring(0, 5); //Creates unique id 
			DecimalFormat up= new DecimalFormat(); //Formats decimal value
			up.setRoundingMode(RoundingMode.UNNECESSARY); //specifies no rounding
			
			if(!itemExistsWithName(connection, name)) {
				String query= "INSERT into rawmaterials (id, name, unitprice, inventory) VALUES (?,?,?,?)"; //Insert query 
				PreparedStatement statement= connection.prepareStatement(query); // Preparing query to insert
				statement.setString(1,id); //Setting field id
				statement.setString(2, name);//Setting field name
				statement.setDouble(3,unitprice);//Setting field unitprice
				statement.setInt(4,inventory);//Setting field inventory
				statement.execute(); //executing query
				System.out.print(MessageFormat.format("{0} was successfully inserted with id {1}.", name, id)); //Success message for debugging
			}else {
				System.out.println("Item already exists");
			}
//			connection.close();
			
		}catch(SQLException e){
			e.printStackTrace(); //catches any error

			
		}
		
		
	}
	
	//Updates item name given an id
	public static void updateItemWithId(Connection connection, String id, String newName) throws ClassNotFoundException{
	      try {
	    	  
	    	  String query = "update rawmaterials set name = ?  where id = ?";  //Updating name using an id query 
	    	  PreparedStatement preparedStmt = connection.prepareStatement(query); //Preparing query
	      
		    	  if(itemExists(connection,id)) { //Accessing itemExists function, accesses db and returns boolean
		    		  preparedStmt.setString(2, id); //Sets parameter id to first placeholder in query
		    		  preparedStmt.setString(1,newName);//Sets parameter newName to second placeholder in query
		    		  preparedStmt.executeUpdate(); //Executes query
		    		  }else {
		    			  System.out.println("Record does not exists"); //Prints message if function itemExists returns false  
		    		 }
//					connection.close();


	      }catch(SQLException e){
				e.printStackTrace();//Catches any SQL exception
	      }	
		
	}
	

	
	
	//Gets all rawmaterials in the database
	public static void getrawmaterials(Connection connection)throws ClassNotFoundException{
		try {
			Statement st = connection.createStatement(); // Creating statement to execute
			ResultSet rs = st.executeQuery("Select * from rawmaterials"); // Query that selects everything in the table rawmaterials
			
			while (rs.next()) {
				// Printing all properties of each row to the screen
				System.out.println(rs.getString("id") + " " + rs.getString("name") + " " + rs.getDouble("unitprice") + " "
						+ rs.getInt("inventory"));
				}
//			connection.close();

		}catch(SQLException e){
			e.printStackTrace(); //catches any SQLerror
			
		}	
		
	}
	
	//Checks if item exists in database
	public static boolean itemExists(Connection connection, String id)throws ClassNotFoundException {
		try {
		String query="SELECT * FROM rawmaterials WHERE id=?"; //Selects everything in table rawmaterials where id matches with parameter id 
		
		PreparedStatement statement= connection.prepareStatement(query); // Preparing query to insert
		
		statement.setString(1, id); //Sets parameter id to placeholder in query
		
		ResultSet rs = statement.executeQuery(); // Statement result stored in result set object
		
		//Checks if collection is empty, return boolean accordingly
		if (rs.next() == false) {
			return false;
		} else {
			return true;
		}
		
		

		}catch(SQLException e){
			e.printStackTrace(); //catches any SQL errors	
		}
		
			return true;	
			
		}
	
	//Checks if item exists in database
	public static boolean itemExistsWithName(Connection connection, String name)throws ClassNotFoundException {
		try {
		String query="SELECT * FROM rawmaterials WHERE name=?"; //Selects everything in table rawmaterials where id matches with parameter id 
		
		PreparedStatement statement= connection.prepareStatement(query); // Preparing query to insert
		
		statement.setString(1, name); //Sets parameter id to placeholder in query
		
		ResultSet rs = statement.executeQuery(); // Statement result stored in result set object
		
		//Checks if collection is empty, return boolean accordingly
		if (rs.next() == false) { 
			return false;
		} else { 
			return true;
		}
		}catch(SQLException e){
			e.printStackTrace(); //catches any SQL errors	
		}
			return true;	
			
		}
	
	public static void getItemWithName(Connection connection, String name)throws ClassNotFoundException {
		try {
		String query="SELECT * FROM rawmaterials WHERE name=?";
		PreparedStatement statement= connection.prepareStatement(query); // Preparing query to insert
		statement.setString(1,name);
		ResultSet rs = statement.executeQuery(); // Statement result stored in result set object
		
		if (rs.next() == false) { 
			System.out.println("Item does not exist");
		} else { 
			do { 
				System.out.println(rs.getString("name")+" " + rs.getDouble("unitprice") + " "+ rs.getInt("inventory"));	
				
			} while (rs.next()); 
		}

		
	}catch(SQLException e){
		e.printStackTrace();

		
	}	
		
	}
	public static void getItemPartialSearch(Connection connection, String name)throws ClassNotFoundException {
		try {
		String query="SELECT * FROM rawmaterials WHERE name=?";
		PreparedStatement statement= connection.prepareStatement(query); // Preparing query to insert
		statement.setString(1,name);
		ResultSet rs = statement.executeQuery(); // Statement result stored in result set object
		
		if (rs.next() == false) { 
			System.out.println("Item does not exist");
		} else { 
			do { 
				System.out.println(rs.getString("name")+" " + rs.getDouble("unitprice") + " "+ rs.getInt("inventory"));	
				
			} while (rs.next()); 
		}

		
	}catch(SQLException e){
		e.printStackTrace();

		
	}	
		
	}
	
	
		
	//Retrieves single item using an id
//	public static void getItemWithId(Connection connection, String id)throws ClassNotFoundException {
//		try {
//			String query="SELECT * FROM rawmaterials WHERE id=?"; //Selecting everything in table table rawmaterials where id matches parameter id
//			PreparedStatement statement= connection.prepareStatement(query); // Preparing query
//			statement.setString(1, id);//Setting id to the fist placeholder in query
//			ResultSet rs = statement.executeQuery(); // Statement result stored in result set object
//		
//		//Prints whether product exists and gets existing records
//		if (rs.next() == false) { 
//			System.out.println("Item does not exist");
//		} else { 
//			do { 
//				System.out.println(rs.getString("id") + " " + rs.getString("name") + " " + rs.getDouble("unitprice") + " "
//						+ rs.getInt("inventory"));			
//				
//			} while (rs.next()); 
//		}
////		connection.close();
//		
//	}catch(SQLException e){
//		e.printStackTrace(); //Catches SQL errors
//	
//	}	
//
// }
	
	public static String getItemWithId(Connection connection, String id)throws ClassNotFoundException {
		String item="";
		try {
			String query="SELECT * FROM rawmaterials WHERE id=?"; //Selecting everything in table table rawmaterials where id matches parameter id
			PreparedStatement statement= connection.prepareStatement(query); // Preparing query
			statement.setString(1, id);//Setting id to the fist placeholder in query
			ResultSet rs = statement.executeQuery(); // Statement result stored in result set object
			if(rs.next()!=false) {
				String q=rs.getString("id") + " " + rs.getString("name") + " " + rs.getDouble("unitprice") + " "
						+ rs.getInt("inventory");
				item=q;
			}else {
				System.out.println("exit");
			}
		
	}catch(SQLException e){
		e.printStackTrace(); //Catches SQL errors
	
	}
		return item;

 }
	public static String getItemWithIdCart(Connection connection, String id, int amountID)throws ClassNotFoundException {
		String item="";
		try {
			String query="SELECT * FROM rawmaterials WHERE id=?"; //Selecting everything in table table rawmaterials where id matches parameter id
			PreparedStatement statement= connection.prepareStatement(query); // Preparing query
			statement.setString(1, id);//Setting id to the fist placeholder in query
			ResultSet rs = statement.executeQuery(); // Statement result stored in result set object
			if(rs.next()!=false) {
				String q=rs.getString("id") + " " + rs.getString("name") + " " + rs.getDouble("unitprice") + " "
						+ amountID;
				item=q;
			}else {
				System.out.println("exit");
			}
		
	}catch(SQLException e){
		e.printStackTrace(); //Catches SQL errors
	
	}
		return item;

 }
	
	
	//Deletes item given id
	public static void deleteItemWithId(Connection connection, String id)throws ClassNotFoundException {
		try {
		String query="Delete FROM rawmaterials WHERE id=?";
		PreparedStatement statement= connection.prepareStatement(query); // Preparing query
		statement.setString(1, id);//Setting parameter id to first placeholder in query
		//Checks if record exists
		if(itemExists(connection,id)) {
			statement.executeUpdate(); // Statement result stored in result set object
		    System.out.println("Record deleted successfully");//Success message 			
			
		}else {
		     System.out.println("Record does not exist"); //Fail message
		}	      
	}catch(SQLException e){
		e.printStackTrace(); //Catches SQL errors
	}	
		
	}
	
	//Returns formatted list of records that is later process for invoice creation by PDF_maker class
	public static ArrayList<String> addToCart(Connection connection,ArrayList<String>ids, int order) throws ClassNotFoundException {
		
		try {
			String query="SELECT * FROM rawmaterials WHERE id=?"; //Checks field where id matches
			PreparedStatement statement= connection.prepareStatement(query); // Preparing query
			
			for (int index=0; index<ids.size(); index++) { //iterates over parameter ArrayList ids
				
				
				
				statement.setString(1,ids.get(index)); //Sets each id from parameter ArrayList to the first placeholder in the query
				
				ResultSet rs = statement.executeQuery(); // Statement result stored in result set object
				
				//Iterates over result set, gets each record, adds it to cartArr ArrayList or says that the item does not exist if id is not in database
				if (rs.next()== true) { 
					do { 
						String data=rs.getString("id") + " " + rs.getString("name").strip() + " " + rs.getDouble("unitprice") + " "+order;
						cartArr.add(data);
					} while (rs.next()); 
				} else { 
					System.out.println("Item does not exist");
				}

			}	
		}catch(SQLException e){
			e.printStackTrace();//Catches any SQL errors

		}	
			return cartArr; //returns formatted ArrayList
		}
		

//Program's entry point
	public static void main(String[] args) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver"); // Calling jdbc driver jar
			Connection con = DriverManager.getConnection("jdbc:mysql://34.70.87.189:3306/materials", "root", "access"); // Connecting to local database instance

//Testing each function 			
//			insertItem(con,"Table saw 3", 2000.00, 170);
//			insertItem(con,"Power drill",(double) 80, 120);
			insertItem(con,"Bolt", (double)250, 90);
//			System.out.print(itemExists(con, "3296d"));
//			insertItem(con," T bolts ", 3.52, 100);
//			insertItem(con," T bolts ", 3.52, 100);
//			insertItem(con," T bolts ", 3.52, 100);
//			insertItem(con," T bolts ", 3.52, 100);
//			insertItem(con," T bolts ", 3.52, 100);
//			insertItem(con," T bolts ", 3.52, 100);

//			getrawmaterials(con);
			System.out.println(getItemWithId(con,"d67f9"));
//			System.out.println(itemExists(con, "f1597"));
//			deleteItemWithId(con,"6325c");
//			deleteItemWithId(con,"8555d");
//			deleteItemWithId(con,"c44cf");
//			deleteItemWithId(con,"e5982");

//			getrawmaterials(con);		
//			updateItemWithId(con, "5127", "newScrews");
//Creating sampleArraylist for addToCart function
			ArrayList<String> samplerawmaterials=new ArrayList<String>();
			samplerawmaterials.add("f134");
//			samplerawmaterials.add("52e9b");
//			samplerawmaterials.add("57663");
//			samplerawmaterials.add("9eb95");
//			samplerawmaterials.add("a7613");
//			samplerawmaterials.add("b8dae");
//			samplerawmaterials.add("e2615");
			
//			getItemWithName(con, "bolts");
			System.out.println(addToCart(con, samplerawmaterials, 123));
			
			con.close();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();//Catches any Class or SQL error

		}

	}

}
