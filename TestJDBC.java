package hw10;


/*
 	Alessandro Garcia-Alario
 	4/13/18
 	CIS4301	
 	HW10
 	
 	7776  
 */

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Scanner;

class TestJDBC {
	public static void main(String[] args) {
		
		DBConnection dbc = new DBConnection();				// instantiate db connection
		System.out.println("Would you like to specify the Db, Username, and Password (default: Mall)\n");  // prompt the user for db info, will default to malls, root, root
		System.out.println("[1] Yes \n[2] No");
		Scanner scan = new Scanner(System.in);
		String input = scan.nextLine().toString();
	
		if (input.equals("yes") ||input.equals("Yes") || input.equals("1"))  
{
	System.out.println("Db name:");
	String db = scan.nextLine();
	System.out.println("Username:");
	String un = scan.nextLine();	
	System.out.println("Password:");
	String pass = scan.nextLine();
	
	dbc = new DBConnection(db,un,pass);      // use parametized constructor
}
		

		String sql, list[];
		ResultSet rs;

		dbc.openConnection();   // start connection

		boolean isExit = false;
		while ((!isExit)) {
			System.out.println("\n**************************************");       // menu for prompting user choices
			System.out.println("Please make a choice:");
			Scanner sc = new Scanner(System.in);
			System.out.print("[1] Entering a complete SQL command.\n"
					+ "[2] Selecting a table from the list of relations. \n"
					+ "[3] Exit\n:");

			int s = sc.nextInt();
			switch (s) {
			case 1: {  // here user inputs full SQL query 
				Scanner sic = new Scanner(System.in);
				String SQLStatement;
				System.out.print("Please enter a complete SQL Statement:\n");
				SQLStatement = sic.nextLine();

				try {
					rs = dbc.query(SQLStatement, null);
					printResultSet(rs);              // method for printing results set

				}

				catch (SQLException sqle) {
				}

			}
				break;
			case 2:  // For user to use GUI
				System.out.println("\nThese are the tables in the database:\n");    
				Scanner sc2 = new Scanner(System.in);
				rs = dbc.query("show tables", null);              // display table for user to choose 
				try {
					ResultSetMetaData rsmd = rs.getMetaData();  // get meta data 
					int columnsNumber = rsmd.getColumnCount();
					int c = 1;
					while (rs.next()) {                      // parse the results set

						for (int i = 1; i <= columnsNumber; i++) {    // would iterate over all columns but columns here is one
							System.out.print("[" + c++ + "]");
							if (i > 1)
								System.out.print(" | ");
							System.out.print(" " + rs.getString(i));

						}
						System.out.println("");
					}

					System.out.println("Please choose a table to see its attributes: ");
					String choice = sc2.nextLine().toString();
					rs = dbc.query("show tables", null);
					c = 0;
					while (rs.next()) {

						for (int i = 1; i <= columnsNumber; i++) {
							c++;
							if (rs.getString(i).equals(choice) || choice.equals(String.valueOf(c))) { // check if number or word is in the list of tables
								choice = rs.getString(i); // if it is change int to string representation
							}
						}
					}
					System.out.println("");
					
					System.out.println("These are the attributes in the table " + choice);
					rs = dbc.query("DESCRIBE "+choice,null);        // run SQL query on their table choice 
					
					c = 1;
					while (rs.next()) {   // display results

						for (int i = 1; i <= columnsNumber; i++) {
							System.out.print("[" + c++ + "] ");
							System.out.println(rs.getString(i));
							}
						}
					
					System.out.println("Please choose an attribute to see the data in its column: \n");
					String colName = sc2.nextLine().toString();
					c = 0;
					rs = dbc.query("DESCRIBE "+choice,null);          
					while (rs.next()) {             /// 

						for (int i = 1; i <= columnsNumber; i++) {
							c++;
							if (rs.getString(i).equals(colName) || colName.equals(String.valueOf(c))) { // check if number or word is in the list of tables
								colName = rs.getString(i);                                             // if it is change int to string respresentation
							}
						}
					}
					System.out.println("\nThese are the columns: "+ colName + "\nfrom the table: " + choice);
					
					rs = dbc.query("SELECT " + colName + " FROM " + choice + " GROUP BY " + colName, null);
					printResultSet(rs);
					System.out.println("\nYour SQL query is: \n" + "SELECT " + colName +" FROM " + choice+ " GROUP BY " + colName);  // Show user their compiled SQL query
					
				} catch (SQLException e) {
				}
				break;
			case 3:
				isExit = true;
				System.out.println("Good-bye");  // exit condition
				break;
			default:
				System.out.println("Please enter a valid command.");
				break;
			}
		}
		}

		// sc.close();


	final public static void printResultSet(ResultSet rs) throws SQLException { // method for printing result set
		ResultSetMetaData rsmd = rs.getMetaData();                             // get meta data
		int columnsNumber = rsmd.getColumnCount();                            // get column number 
		while (rs.next()) {
			for (int i = 1; i <= columnsNumber; i++) {                      // iterate over columns
				if (i > 1)                                                 // vertical bar if more that one column
					System.out.print(" | ");
				System.out.print(rs.getString(i));
			}
			System.out.println("");
		}
	}
}
