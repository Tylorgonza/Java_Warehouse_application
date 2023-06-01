package billing_services;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.MessageFormat;

public class LargeComponentsDatabase {
	static ArrayList<String> cartArr = new ArrayList<String>();
	
	
	public static  void insertPart(Connection connection, String name, double price) throws ClassNotFoundException{
		try{
			String id=UUID.randomUUID().toString().substring(0, 4); //Creates unique id 
			String query= "INSERT into largecomponents (DefaultPart, Price) VALUES (?,?)"; //Insert query 
			PreparedStatement statement= connection.prepareStatement(query); // Preparing query to insert
			statement.setString(1,name); //Setting field id
			statement.setDouble(2,price);//Setting field price
			statement.execute(); //executing query
			System.out.print(MessageFormat.format("{0} was successfully inserted with price {1}.", name, price)); //Success message for debugging

			
		}catch(SQLException e){
			e.printStackTrace(); //catches any error

			
		}
		
	}
	
	public static void updatePartWithId(Connection connection,String name, String newName) throws ClassNotFoundException{
	      try {String query = "update largecomponents set DefaultPart= ?  where DefaultPart= ?";
	      PreparedStatement preparedStmt = connection.prepareStatement(query);
	      if(partExists(connection,name)) {
		      preparedStmt.setString(2, name);
		      preparedStmt.setString(1,newName);
		      preparedStmt.executeUpdate();
			     System.out.println("Record updated successfully");


	      }else {
			     System.out.println("Record does not exists");

	    	  
	      }

	      }catch(SQLException e){
				e.printStackTrace();

	    	  
	      }
		
		
		
	}
	
	public static void getParts(Connection connection)throws ClassNotFoundException{
		try {
			Statement st = connection.createStatement(); // Creating statement to execute queries
			ResultSet rs = st.executeQuery("Select * from largecomponents"); // Statement result stored in result set object
			while (rs.next()) {
				// Printing all properties of each row to the screen
				System.out.println(rs.getString("DefaultPart") + " "+ rs.getDouble("Price"));
				}
		}catch(SQLException e){
			e.printStackTrace();

			
		}	
		
	}
	
	public static boolean partExists(Connection connection, String dfp)throws ClassNotFoundException {
		try {
		String query="SELECT * FROM largecomponents WHERE DefaultPart=?";
		PreparedStatement statement= connection.prepareStatement(query); // Preparing query to insert
		statement.setString(1,dfp);
		ResultSet rs = statement.executeQuery(); // Statement result stored in result set object
		
		if (rs.next() == false) { 
			return false;
		} else { 
			return true;
		}

		
	}catch(SQLException e){
		e.printStackTrace();

		
	}
		return true;	
		
	}
	
	public static void getPartWithName(Connection connection, String name)throws ClassNotFoundException {
		try {
		String query="SELECT * FROM largecomponents WHERE DefaultPart=?";
		PreparedStatement statement= connection.prepareStatement(query); // Preparing query to insert
		statement.setString(1,name);
		ResultSet rs = statement.executeQuery(); // Statement result stored in result set object
		
		if (rs.next() == false) { 
			System.out.println("Item does not exist");
		} else { 
			do { 
				System.out.println(rs.getString("DefaultPart")+" "  + rs.getDouble("Price"));			
				
			} while (rs.next()); 
		}

		
	}catch(SQLException e){
		e.printStackTrace();

		
	}	
		
	}
	
	
	
	
	public static void deletePartWithName(Connection connection, String name)throws ClassNotFoundException {
		try {
		String query="Delete FROM largecomponents WHERE DefaultPart=?";
		PreparedStatement statement= connection.prepareStatement(query); // Preparing query to insert
		statement.setString(1, name);
		
		if(partExists(connection,name)) {
			statement.executeUpdate(); // Statement result stored in result set object
		     System.out.println("Record deleted successfully");			
			
		}else {
		     System.out.println("Record does not exist");
		}
	      
	}catch(SQLException e){
		e.printStackTrace();

		
	}	
		
	}
	public static ArrayList<String> addToCart(Connection connection,ArrayList<String>parts) throws ClassNotFoundException {
		
		try {
			String query="SELECT * FROM largecomponents WHERE DefaultPart=?"; //Checks field where id matches
			PreparedStatement statement= connection.prepareStatement(query); // Preparing query to search
			
			for (int index=0; index<parts.size(); index++) {
//				System.out.println(ids.get(index));
				int randomOrder=(int)((Math.random()*10000)%250)+1;
				statement.setString(1,parts.get(index));
				
				ResultSet rs = statement.executeQuery(); // Statement result stored in result set object
				
				//Iterates over result set, gets each record,adds it to cartArr ArrayList or says that the item does not exist if id is not in database
				if (rs.next()== true) { 
					do { 
						String data=rs.getString("DefaultPart") + " " + rs.getFloat("Price");
//						System.out.println(data);
						cartArr.add(data);
					} while (rs.next()); 
				} else { 
					System.out.println("Item does not exist");

					
				}

			}
		  
			
		}catch(SQLException e){
			e.printStackTrace();

		}	

			
			return cartArr;
		}
		
	

	public static void main(String[] args) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver"); // Calling driver
			Connection con = DriverManager.getConnection("jdbc:mysql://34.70.87.189:3306/materials", "root", "access"); // Connecting to local database instance
//			insertPart(con,"Turbo booster",2345454);
//			getPartWithName(con,"Turbo turbo booster");
//			updatePartWithId(con,"Turbo booster","Turbo turbo booster");
//			deletePartWithName(con,"Turbo turbo booster");
//			getParts(con);
//			System.out.println(partExists(con, "Boosters"));
			ArrayList<String> sampleItems=new ArrayList<String>();
//			sampleItems.add("Boosters");
//			sampleItems.add("bhw4");
			sampleItems.add("n45r");
			sampleItems.add("9eb95");
//			sampleItems.add("a7613");
//			sampleItems.add("b8dae");
//			sampleItems.add("e2615");
			System.out.print(addToCart(con, sampleItems));
			
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();

		}

	}

}
