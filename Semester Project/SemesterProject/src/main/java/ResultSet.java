import java.io.IOException;

import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.*;

public class ResultSet extends Table {

	//Just to explain my result set briefly:
	//It extends the Table class, so it has all the methods the table class has, most importantly getTableList() which returns
	//the ArrayList of DBEntries (aka rows) which really comprises the column. For a CreateTableQuery, as per the instructions, 
	//the ResultSet will basically be a copy of the table created. For Update, Insert, Delete, and CreateIndex Queries, if the query was 
	//successful it will be a Table with one column and one entry and the value of the one DBBox of the one DBEntry will be Boolean true.
	//If false, they will be a table with two columns and one Entry: in the first DBBox of the one entry will be Boolean false, and in the
	//second will be a String explanation of what went wrong. For a SelectRowsQuery: it will be a table with however many columns the user
	//is looking for, and however many DBEntries match the search (as per the instructions - "A single table of the values that match the query).
	//For selectFunctionQueries it will be a table with one column and an amount of rows corresponding to the amount of values the user asks for.
	//the first row's DBBox will contain the first SelectQuery's value, the second will contain the second, etc.
	
	protected ResultSet (int size, String name, ColumnDescription[] colDescs, ColumnDescription pKey) throws IOException
	{
		super(size, name, colDescs, pKey);
	}

} 