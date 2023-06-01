package billing_services;

import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.UUID;
import org.mindrot.jbcrypt.BCrypt;

public class UserAuth {
	
	//Encrypts plain text passwords
	public static String encryptPass(String password) 
	{
		return BCrypt.hashpw(password, BCrypt.gensalt());
	}
	
	//Compares input password to hashed password
	public static boolean checkPass(String password, String storedPass) 
	{
		return BCrypt.checkpw(password,storedPass);
	}
	
	//Creates/inserts user to database with encrypted password
	public static  void createUser(Connection connection, String email,String password) throws ClassNotFoundException{
		try{
			String id=UUID.randomUUID().toString().substring(0, 5); //Creates unique id 
			DecimalFormat up= new DecimalFormat(); //Formats decimal value
			up.setRoundingMode(RoundingMode.UNNECESSARY); //specifies no rounding
			
			if(!userExists(connection, email)) {
				String query= "INSERT into userdata (id, email, password) VALUES (?,?,?)"; //Insert query 
				PreparedStatement statement= connection.prepareStatement(query); // Preparing query to insert
				statement.setString(1,id); //Setting field id
				statement.setString(2, email);//Setting field name
				statement.setString(3,encryptPass(password));//Setting field inventory
				statement.execute(); //executing query
				System.out.println(MessageFormat.format("{0} was successfully inserted with id {1}.",email, id)); //Success message for debugging
			}else {
				System.out.println("User already exists");
			}
//			connection.close();
			
		}catch(SQLException e){
			e.printStackTrace(); //catches any error

			
		}
		
		
	}
	
	//Checks if user exists in database
	public static boolean userExists(Connection connection, String email)throws ClassNotFoundException {
		try {
		String query="SELECT * FROM userdata WHERE email=?"; //Selects everything in table items where id matches with parameter id 
		
		PreparedStatement statement= connection.prepareStatement(query); // Preparing query to insert
		
		statement.setString(1,email); //Sets parameter id to placeholder in query
		
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
		
	
	//Compares users password to stored password in datbase
	public static boolean accountAccess (Connection connection, String email, String password) throws ClassNotFoundException  {
		
		try {
		
			String query="SELECT * FROM userdata WHERE email=?"; //Selects everything in table items where id matches with parameter id 
		
			PreparedStatement statement= connection.prepareStatement(query); // Preparing query to insert
		
			statement.setString(1, email); //Sets parameter id to placeholder in query
		
			ResultSet rs = statement.executeQuery(); // Statement result stored in result set object
			
			
			if(rs.next()) {
				String savedPass=rs.getString("password");
				if(!userExists(connection, email)||!checkPass(password, savedPass)) {
					System.out.println("Wrong username or pass");
					return false;
					
				}else {
					
					System.out.println(MessageFormat.format("Welcome {0}", rs.getString("email")));

				}
			}
			
		}catch(SQLException e){
			e.printStackTrace();
		}
		
		
		return true;
	}
	
		
		

	public static void main(String[] args) {
		
		try {Class.forName("com.mysql.cj.jdbc.Driver"); // Calling jdbc driver jar
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/users", "root", "root"); // Connecting to local database instance
//		createUser(con, "example2@utrgv.edu", "unecrypted pass");
//		createUser(con, "sample.student10@utrgv.edu", "random");
//		createUser(con, "sample.student11@utrgv.edu", "owowowowo");

		
		accountAccess(con,"example2@utrgv.edu","unecrypted pass0" );
//		accountAccess(con,"example3@utrgv.edu","unecrypted pass" );
//		accountAccess(con, "sample.student10@utrgv.edu", "random");
//		accountAccess(con, "sample.student10@utrgv.edu", "Notrandom");
//		createUser(con, "sample.student11@utrgv.edu", "owowowowo");
//		accountAccess(con, "sample.student11@utrgv.edu", "owowowowo");



		
		}catch(ClassNotFoundException | SQLException e){
			
			e.printStackTrace();
		}

	}

}
