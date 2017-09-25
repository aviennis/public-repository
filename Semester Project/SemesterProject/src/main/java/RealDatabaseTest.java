import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import net.sf.jsqlparser.JSQLParserException;

public class RealDatabaseTest {
	
	public static void main(String[] args) throws JSQLParserException, IOException
	{
		//create the Database Manager to prefore our database queries
		DatabaseManager dm = new DatabaseManager();
		
		//---------CREATE TABLE TEST--------------
		System.out.println("------------------STARTING CREATE TABLE TEST---------------------");
		//Make a new table, with all the different variable types and and limitations and column qualities
		String createTableQuery = "CREATE TABLE YCStudent"
				+ "("
				+ " BannerID int,"
				+ " SSNum int UNIQUE,"
				+ " FirstName varchar(255),"
				+ " GPA decimal(1, 2) DEFAULT 0.00,"
				+ " LastName varchar(255) NOT NULL,"
				+ " CurrentStudent boolean DEFAULT true,"
				+ " PRIMARY KEY (BannerID)"
				+ ");";
		ResultSet createTableSet = dm.execute(createTableQuery);
		//print out the various column's properties to show the table creation worked
		//and all the info was successfully captured in the ResultSet.
		printColumnInfo(createTableSet);
		//explanation of my resultSet class in the result set class		
		System.out.println("-------------DONE WITH CREATE TABLE TEST--------------");
		
		//----------CREATE INDEX TEST--------------
		System.out.println("-------------STARTING CREATE INDEX TESTING-----------");
		String createIndexQuery = "CREATE INDEX SSNum_Index on YCStudent (SSNum);";
		ResultSet createIndexResultSet = dm.execute(createIndexQuery);
		//the test should be successful so the following should print true:
		System.out.println("");
		System.out.println(createIndexResultSet.getTableList().get(0).getEntry()[0].getValue().toString());
		System.out.println("");
		//now lets make sure the name of the Index was successfully added:
		//there should be an index named SSNum_Index there
		for(Table t : dm.tables)
		{
			printColumnInfo(t);
		}
		System.out.println("-------------DONE WITH CREATE INDEX TEST--------------");
		
		//---------INSERT ROWS TEST--------------
		System.out.println("---------------STARTING INSERT ROWS TEST----------------");
		//insert five valid rows to show which kinds or row insertions are legal
		//inserting five will also show us the BTree is working to store the values with the splitting
		
		//first few insertions have every column filled, column Value pairs in different orders to show the pairing works and it doesn't have to be the same order.
		String insertOne = "INSERT INTO YCStudent (FirstName, LastName, BannerID, CurrentStudent, SSNum, GPA) VALUES ('Avi', 'Ennis', 800324355, true, 123456789, 3.95);";
		String insertTwo = "INSERT INTO YCStudent (LastName,FirstName, CurrentStudent, BannerID, SSNum, GPA) VALUES ('Shmo', 'Joe', false, 800123456, 121212121, 4.00);";
		String insertThree = "INSERT INTO YCStudent (SSNum, FirstName, BannerID, CurrentStudent, GPA, LastName) VALUES (123123123, 'Ploni', 800654321, true, 1.03, 'Almoni');";
		
		//let the DEFAULT value of CurrentStudent kick in...
		String insertFour = "INSERT INTO YCStudent (LastName, FirstName, BannerID, SSNum, GPA) VALUES ('Person', 'Random', 800987654, 987654321, 3.05);";
		//let the NULL value for first Name kick in...
		String insertFive = "INSERT INTO YCStudent (LastName, CurrentStudent, BannerID, SSNum, GPA) VALUES ('DataStructers-stien', false, 800456789, 987987987, 3.15);";
		
		//execute all the queries
		ResultSet insertSetOne = dm.execute(insertOne);
		ResultSet insertSetTwo = dm.execute(insertTwo);
		ResultSet insertSetThree = dm.execute(insertThree);
		ResultSet insertSetFour = dm.execute(insertFour);
		ResultSet insertSetFive = dm.execute(insertFive);
		//make sure the ResultSets are true
		System.out.println();
		System.out.println(insertSetOne.getTableList().get(0).getEntry()[0].getValue().toString());
		System.out.println(insertSetTwo.getTableList().get(0).getEntry()[0].getValue().toString());
		System.out.println(insertSetThree.getTableList().get(0).getEntry()[0].getValue().toString());
		System.out.println(insertSetFour.getTableList().get(0).getEntry()[0].getValue().toString());
		System.out.println(insertSetFive.getTableList().get(0).getEntry()[0].getValue().toString());
		System.out.println();
		
		//print out all the five entries' information
		
		for(Table t : dm.tables)
		{
			printRows(t);
		}
		System.out.println("DONE WITH SUCCESSFUL INSERTIONS");
		
		//put in some error inducing insertios and print the error message to make sure we're getting the right error message
		//the first two will not meet the required value constraints
		String badInsertOne = "INSERT INTO YCStudent (FirstName, LastName, BannerID, CurrentStudent, SSNum, GPA) VALUES ('Bad', 'Insertaion Insertaion Insertaion Insertaion Insertaion Insertaion Insertaion Insertaion Insertaion Insertaion Insertaion Insertaion Insertaion Insertaion Insertaion Insertaion Insertaion Insertaion Insertaion Insertaion Insertaion Insertaion Insertaion Insertaion Insertaion Insertaion ', 800000001, true, 00000000, 3.95);";
		String badInsertTwo = "INSERT INTO YCStudent (FirstName, LastName, BannerID, CurrentStudent, SSNum, GPA) VALUES('Bad', 'Insertion', 800000002, true, 00000000, 11.0);";
		//a null value in a NOT NULL column
		String badInsertThree = "INSERT INTO YCStudent (FirstName, BannerID, CurrentStudent, SSNum, GPA) VALUES('Bad', 800000003, true, 00000000, 1.00);";
		//invalid column name
		String badInsertFour = "INSERT INTO YCStudent (FirstName, LastName, BannerID, CurrentStudent, SSN, GPA) VALUES('Bad', 'Insertion', 800000004, true, 00000000, 1.00);";
		//invalid table name
		String badInsertFive = "INSERT INTO YCSudent (FirstName, LastName, BannerID, CurrentStudent, SSNum, GPA) VALUES('Bad', 'Insertion', 800000005, true, 00000000, 1.00);";
		
		ResultSet badInsertSetOne = dm.execute(badInsertOne);
		ResultSet badInsertSetTwo = dm.execute(badInsertTwo);
		ResultSet badInsertSetThree = dm.execute(badInsertThree);
		ResultSet badInsertSetFour = dm.execute(badInsertFour);
		ResultSet badInsertSetFive = dm.execute(badInsertFive);
		
		
		//print out the error messages
		System.out.println("");
		System.out.println(((DBEntry)badInsertSetOne.getTableList().get(0)).getEntry()[1].getValue());
		System.out.println(((DBEntry)badInsertSetTwo.getTableList().get(0)).getEntry()[1].getValue());
		System.out.println(((DBEntry)badInsertSetThree.getTableList().get(0)).getEntry()[1].getValue());
		System.out.println(((DBEntry)badInsertSetFour.getTableList().get(0)).getEntry()[1].getValue());
		System.out.println(((DBEntry)badInsertSetFive.getTableList().get(0)).getEntry()[1].getValue());
		System.out.println("");
		
		System.out.println("------------------DONE WITH INSERT TESTING---------------");
		System.out.println("----------------STARTING BTREE TESTING-----------------");
		//Now we'll test if the BTrees successfully did all the insertion
		for(Table t : dm.tables)
		{
			for(BTreeImplementation bti : t.getTrees())
			{
				//we know there should be two entries in the node, and three in each of the "children" nodes"
				Node root = bti.root;
				Node node1 = (Node)root.getNode()[0].getValue();
				Node node2 = (Node)root.getNode()[1].getValue();
				
				Object[] rootkeys = new Object[6];
				Object[] node1keys = new Object[6];
				Object[] node2keys = new Object[6];
				Object[] node1values = new Object[6];
				Object[] node2values = new Object[6];
				
				for(int i = 0; i < 2; i++)
				{
					rootkeys[i] = root.getNode()[i].getKey();
				}
				for(int i = 0; i < 3; i++)
				{
					node1keys[i] = node1.getNode()[i].getKey();
					node2keys[i] = node2.getNode()[i].getKey();
					
					node1values[i] = (DBEntry)node1.getNode()[i].getListOfValues().get(0);
					node2values[i] = (DBEntry)node2.getNode()[i].getListOfValues().get(0);
					
				}
				for(int i = 0; i < 3; i++)
				{
					if(node1values[i] != null)
					{
						node1values[i] = ((DBEntry)node1values[i]).getEntry()[2].getValue().toString();
					}
					if(node2values[i] != null)
					{
						if(((DBEntry)node2values[i]).getEntry()[2].getValue() == null)
						{
							node2values[i] = null;
						}
						else
						{
							//Entry[2] is the first name. See comments below.
							node2values[i] = ((DBEntry)node2values[i]).getEntry()[2].getValue().toString();
						}
					}
				}
				
				//this will print out the keys of the node (which will have the SENTINAL and fourt entries
				System.out.println(Arrays.toString(rootkeys));
				//the next few lines will print out the info of the BTrees. node1keys and node2keys will have the keys of the Entries
				//the node1values and node2values I made it have the first name of the person so we can easily check to make sure
				//the key value pairs are matched up properly.
				System.out.println(Arrays.toString(node1keys));
				System.out.println(Arrays.toString(node1values));
				System.out.println(Arrays.toString(node2keys));
				System.out.println(Arrays.toString(node2values));
				System.out.println("Done with BTree");
				System.out.println("");
				
			}
		}
		
		System.out.println("-----------DONE WITH BTREE TESTING-----------");
		
		//---------------UPDATE TEST----------------------
		System.out.println("-------------------STARTING UPDATE TEST------------------");
		//test a basic update with a compound condition, One condition from the BTree, the other From the list of Entries
		String updateOne = "UPDATE YCStudent SET FirstName='Avery' WHERE BannerID=800324355 AND LastName='Ennis';";
		ResultSet updateSet1 = dm.execute(updateOne);
		//test >= for the BTree and the list
		String updateTwo = "UPDATE YCStudent SET GPA=4.0 WHERE BannerID>=800400000;";
		ResultSet UpdateSetTwo = dm.execute(updateTwo);
		//now that Avery Ennis is the only one without a 4.0 we can test the <> operator, 
		//also tests changing multiple things with one update
		String updateThree = "UPDATE YCStudent SET LastName='Enis', CurrentStudent=false, GPA=3.99 WHERE GPA <> 4.00;";
		ResultSet UpdateSetThree = dm.execute(updateThree);
		//test the <= operator
		String updateFour = "UPDATE YCStudent SET SSNum=99999999 WHERE GPA<=3.99;";
		ResultSet UpdateSetFour = dm.execute(updateFour);
				
		//After all these updates, Avi's FirstName should be Avery, last name should be Ennis, SSnum should be 99999999, GPA should be 3.99,
		//everyone else should have a 4.00 GPA
		//print out the table info to check
		for(Table t : dm.tables)
		{
			printRows(t);
		}
		
		//since we changed Avi/Avery's SSNum, we need to make sure the corresponding changes were made in tht BTree
		//the New value for the SSNum would be the smallest value (excluding the Sentinal Value of course), so the value of the First Non-sentinal Entry
		//of the first Node should be 999999999. In addition, the old value should have been deleted so the old SSNum (123456789) should have an empty
		//list as it's value. Furthermore, nothing should change in the BannerID BTree - so this should still print out 800123456 during that iteration.
		for(Table table : dm.tables)
		{
			for(BTreeImplementation bti : table.getTrees())
			{
				Node root = bti.root;
				
				Node node1 = (Node)root.getNode()[0].getValue();
				Node node2 = (Node)root.getNode()[1].getValue();
				
				Entry e1 = node1.getNode()[1];//should be 9999999
				Entry e2 = node2.getNode()[0];//should be null
				
				System.out.println("BTree updates check:");
				System.out.println((e1.getKey().toString()));//should be 800123456 on the first iteration of the loop (nothing changed from before)
															 //and should be 99999999 on the second iteration of the loop
				
				if(e2.getListOfValues().isEmpty())
				{
					System.out.println("the first Entry of the second Node has an empty list as it's value");
				}
			}
		}
		//Now let's test a few invalid updates
		//invalid column name
		String badUpdateOne = "UPDATE YCStudent SET SSNUM=10 WHERE FirstName='Avi';";
		ResultSet badUpdateSetOne = dm.execute(badUpdateOne);
		//invalid table name
		String badUpdateTwo = "UPDATE SYMSStudent SET GPA=4.00 WHERE FirstName='Test' AND LastName='Banks';";
		ResultSet badUpdateSetTwo = dm.execute(badUpdateTwo);
		//proposed entries don't meet the data parameters
		String badUpdateThree = "UPDATE YCStudent SET GPA=40.00 WHERE FirstName='Joe';";
		String badUpdateFour = "UPDATE YCStudent SET FirstName='aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa' WHERE LastName='Shmo';";
		ResultSet badUpdateSetThree = dm.execute(badUpdateThree);
		ResultSet badUpdateSetFour = dm.execute(badUpdateFour);
		//print out the error messages
		System.out.println("");
		System.out.println(((DBEntry)badUpdateSetOne.getTableList().get(0)).getEntry()[1].getValue());
		System.out.println(((DBEntry)badUpdateSetTwo.getTableList().get(0)).getEntry()[1].getValue());
		System.out.println(((DBEntry)badUpdateSetThree.getTableList().get(0)).getEntry()[1].getValue());
		System.out.println(((DBEntry)badUpdateSetFour.getTableList().get(0)).getEntry()[1].getValue());
		System.out.println("");
		System.out.println("----------------DONE WITH UPDATE TESTING-----------------");
		//--------------------DELETE TEST----------------------------
		System.out.println("---------------STARTING DELETE TESTING------------------");
		//now lets delete various entries, and along with it test some more conditions
		String deleteOne = "DELETE FROM YCStudent WHERE FirstName='Random';";
		String deleteTwo = "DELETE FROM YCStudent WHERE BannerID=800456789;";
		ResultSet deleteSetOne = dm.execute(deleteOne);
		ResultSet deleteSetTwo = dm.execute(deleteTwo);
		//should have delete "Joe Shmo" and "null Data-Structures-Stein"
		for(Table table : dm.tables)
		{
			printRows(table);
		}
		System.out.println("-------------DONE WITH DELETE TESTING---------------");
		
		
		//----------------------------SELECT TESTING-------------------------------
		//in order to show the more complicated examples of select testing, and to show that the database can handle more
		//than one table, I will make a new table to test selecting. Because the logic in the DatabaseManager class is the
		//same for every type of Query, we can assume if it successfully picks the right table for these, it would work for
		//all other commands as well. By the same logic, I don't need to create a new complicated table and have new complicated
		//Insert/Update/Delete/CreateIndex Queries, because I have already tested them on one table, and if I show here that the
		//DatabaseManager can support multiple tables, logically it can do the more complicated Queries on more complicated
		//tables also. The following is a very simple table and various select queries.
		
		System.out.println("---------------STARTING SELECT TESTING---------------");
		String createTable2 = "CREATE TABLE selecttest"
				+ "("
				+ " one int,"
				+ " two int,"
				+ " three int,"
				+ " name varchar(200),"
				+ " PRIMARY KEY (name)"
				+ ");";
		ResultSet rs = dm.execute(createTable2);
		
		String a = "INSERT INTO selecttest (one, two, three, name) VALUES(1, 1, 1, 'a');";
		String b = "INSERT INTO selecttest (one, two, three, name) VALUES(1, 1, 2, 'b');";
		String c = "INSERT INTO selecttest (one, two, three, name) VALUES(1, 1, 3, 'c');";
		String d = "INSERT INTO selecttest (one, two, three, name) VALUES(1, 2, 1, 'd');";
		String e = "INSERT INTO selecttest (one, two, three, name) VALUES(1, 2, 2, 'e');";
		String f = "INSERT INTO selecttest (one, two, three, name) VALUES(2, 1, 3, 'f');";
		String g = "INSERT INTO selecttest (one, two, three, name) VALUES(2, 1, 2, 'g');";
		String h = "INSERT INTO selecttest (one, two, three, name) VALUES(2, 2, 1, 'h');";
		
		ResultSet rsa = dm.execute(a);
		ResultSet rsb = dm.execute(b);
		ResultSet rsc = dm.execute(c);
		ResultSet rsd = dm.execute(d);
		ResultSet rse = dm.execute(e);
		ResultSet rsf = dm.execute(f);
		ResultSet rsg = dm.execute(g);
		ResultSet rsh = dm.execute(h);
		
		//first things first, I will check that the system can support multiple tables:
		//each table's info should print out under the correct table name:
		for(Table t: dm.tables)
		{
			System.out.println("Printing info from: " + t.getName());
			printRows(t);
			System.out.println("Done with Table:" + t.getName());
			System.out.println("");
		}
		
		//Now that we know the system supports multiple tables, onto checking select:
		//first lets check SELECT FUNCTIONS:
		String selectFunction1 = "SELECT COUNT (three) FROM selecttest;";
		String selectFunction2 = "SELECT COUNT (DISTINCT three) FROM selecttest;";
		String selectFunction3 = "SELECT MAX (two) FROM selecttest;";
		//lets throw in one test for using the BTree on an indexed row (NOTE: FROM YCStudent Table)
		String selectFunction4 = "SELECT MIN (SSNum) FROM YCStudent;";
		String selectFunction5 = "SELECT SUM (one) FROM selecttest;";
		String selectFunction6 = "SELECT SUM (DISTINCT one) FROM selecttest;";
		String selectFunction7 = "SELECT SUM (DISTINCT one), AVG(three) FROM selecttest;";//test getting two values
		
		ResultSet selectFunctionSet1 = dm.execute(selectFunction1);
		ResultSet selectFunctionSet2 = dm.execute(selectFunction2);
		ResultSet selectFunctionSet3 = dm.execute(selectFunction3);
		ResultSet selectFunctionSet4 = dm.execute(selectFunction4);
		ResultSet selectFunctionSet5 = dm.execute(selectFunction5);
		ResultSet selectFunctionSet6 = dm.execute(selectFunction6);
		ResultSet selectFunctionSet7 = dm.execute(selectFunction7);
		
		System.out.println("Select Function Test:");
		System.out.println(selectFunctionSet4.getTableList().get(0).getEntry()[0].getValue().toString());//should be 99999999
		System.out.println(selectFunctionSet1.getTableList().get(0).getEntry()[0].getValue().toString());//should be 8
		System.out.println(selectFunctionSet2.getTableList().get(0).getEntry()[0].getValue().toString());//should be 3
		System.out.println(selectFunctionSet3.getTableList().get(0).getEntry()[0].getValue().toString());//should be 2
		System.out.println(selectFunctionSet5.getTableList().get(0).getEntry()[0].getValue().toString());//should be 11.0
		System.out.println(selectFunctionSet6.getTableList().get(0).getEntry()[0].getValue().toString());//should be 3.0
		System.out.println(selectFunctionSet7.getTableList().get(1).getEntry()[0].getValue().toString() + " " +
				selectFunctionSet7.getTableList().get(0).getEntry()[0].getValue().toString());//should be 3.0 1.875
		
		System.out.println("DONE WITH SELECT FUNCTIONS");
		System.out.println("TESTING SELECT ROWS");
		//because we've already tested the conditions in update and delete, I don't think I need to test them more here. 
		//I'm instead going to focus on the features specific to select.
		//Lets start with selecting the whole table
		String selectRows1 = "SELECT one, two, three, name FROM selecttest;";//NOTE: the 
		//now lets test taking only some of the rows (with a simple condition, why not)
		String selectRows2 = "SELECT three, name FROM selecttest WHERE one>1;";
		//lets try with distinct
		String selectRows3 = "SELECT DISTINCT one FROM selecttest;";
		//distinct with two columns
		String selectRows4 = "SELECT DISTINCT one, two FROM selecttest;";
		//Finally, lets put it all together with a very complicated, layered condition
		String selectRows5 = "SELECT name FROM selecttest ORDER BY one ASC, two DESC, three ASC;";
		
		ResultSet selectRowsSet1 = dm.execute(selectRows1);
		ResultSet selectRowsSet2 = dm.execute(selectRows2);
		ResultSet selectRowsSet3 = dm.execute(selectRows3);
		ResultSet selectRowsSet4 = dm.execute(selectRows4);
		ResultSet selectRowsSet5 = dm.execute(selectRows5);
		
		printRows(selectRowsSet1);//should print out all columns for every row
		System.out.println("done with select one");
		printRows(selectRowsSet2);//should print columns three and one from rows f, g, h
		System.out.println("done with select two");
		printRows(selectRowsSet3);//should print 1, 2 (the only two distinct values)
		System.out.println("done with select three");
		printRows(selectRowsSet4);//should print four "pairs" of values 11, 12, 21, 22
		System.out.println("done with select four");
		printRows(selectRowsSet5);//Should print in the following order: D E A B C H G F
		System.out.println("done with select five");
		
		System.out.println("----------------------DONE WITH SELECT TESTING--------------------");
	}	
	
	private static void printRows(Table table)
	{
		ArrayList<DBEntry> rows = table.getTableList();
		for(DBEntry dbe : rows)
		{
			DBBox[] dbb = dbe.getEntry();
			for(DBBox box : dbb)
			{
				if(box.getValue() == null)
				{
					System.out.println("null");
				}
				else
				{
					System.out.println(box.getValue());
				}
			}
			System.out.println("");
		}
	}
	private static void printColumnInfo(Table table)
	{
		for(int i = 0; i < table.getSize(); i++)
		{
			System.out.println("Column " + (i + 1) + ": ");
			System.out.println("column name: " + table.getColumnList()[i]);
			System.out.println("Index name: " + table.getNameOfIndex()[i]);
			System.out.println("not null: " + table.getNotNull()[i]);
			System.out.println("is unique: " + table.getUnique()[i]);
			System.out.println("is primary key: " + table.getPrimaryKey()[i]);
			System.out.println("");

		}
		
	}	
	
}
	
	
	

