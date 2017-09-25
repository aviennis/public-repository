import java.util.HashSet;

import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.ColumnValuePair;

public class DBEntry {
	
	private DBBox[] Entry;
	
	
	protected DBEntry(int size)
	{
		Entry = new DBBox[size];
	

	}
	
	protected int getSize()
	{
		return Entry.length;
	}
	
	protected DBBox[] getEntry()
	{
		return Entry;
	}

}
