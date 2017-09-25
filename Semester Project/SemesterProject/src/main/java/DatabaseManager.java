
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.*;
import net.sf.jsqlparser.JSQLParserException;

public class DatabaseManager {

	HashSet<Table> tables;
	public DatabaseManager()
	{
		tables = new HashSet<Table>();
	}
	
	/**
	 * takes a String representing an SQL query and runs the query
	 * @param SQL
	 * @return
	 * @throws JSQLParserException
	 * @throws IOException
	 */
	public ResultSet execute(String SQL) throws JSQLParserException, IOException
	{
		try {
			SQLParser parser = new SQLParser();
			SQLQuery query = parser.parse(SQL);
		
			if (query instanceof SelectQuery)
			{
				SelectQuery q = (SelectQuery) query;
				return select(q);
			}
			else if (query instanceof InsertQuery)
			{
				InsertQuery i = (InsertQuery) query;
				return insert(i);
			}
			else if (query instanceof DeleteQuery)
			{
				DeleteQuery d = (DeleteQuery) query;
				return delete(d);
			}
			else if (query instanceof CreateTableQuery)
			{
				CreateTableQuery ct = (CreateTableQuery) query;
				return createTable(ct);
			}
			else if (query instanceof CreateIndexQuery)
			{
				CreateIndexQuery ci = (CreateIndexQuery) query;
				return createIndex(ci);
			}
			else if (query instanceof UpdateQuery)
			{
				UpdateQuery u = (UpdateQuery) query;
				return update(u);
			}
			else {
				return null;
			}
		}
		catch(IOException e)
		{
			return generateExceptionResultSet(false, e.getMessage());
		}
		
	}
	
	/**
	 * runs the select query
	 * @param sq
	 * @return
	 * @throws IOException
	 */
	private ResultSet select(SelectQuery sq) throws IOException
	{
		String tableName = sq.getFromTableNames()[0];
		for(Table table : tables)
		{
			if(tableName.equals(table.getName()))
			{
				ArrayList<DBEntry> list = new ArrayList<DBEntry>();
				list = table.select(sq);
				ResultSet returnSet = new ResultSet(sq.getSelectedColumnNames().length, "name", null, null);
							
				returnSet.getTableList().addAll(list);
				
				return returnSet;
			}
		}
		throw new IOException("not a valid table name");
		
	}
	
	/**
	 * runs the insert query
	 * @param iq
	 * @return
	 * @throws IOException 
	 */
	private ResultSet insert(InsertQuery iq) throws IOException
	{
		String tableName = iq.getTableName();
		for(Table table: tables)
		{
			if (tableName.equals(table.getName()))
			{
					return generateSingleBoxResultSet(table.addRow(iq.getColumnValuePairs()));
			}
		}
		throw new IOException("not a valid table name");
	}
	
	/**
	 * runs the delete query
	 * @param dq
	 * @return
	 * @throws IOException
	 */
	private ResultSet delete(DeleteQuery dq) throws IOException
	{
		String tableName = dq.getTableName();
		for(Table table : tables)
		{
			if(tableName.equals(table.getName()))
			{
				return generateSingleBoxResultSet(table.deleteRow(dq));
			}
		}
		throw new IOException("not a valid table name");
	}
	
	/**
	 * runs the create table query
	 * @param ctq
	 * @return
	 * @throws IOException 
	 */
	private ResultSet createTable(CreateTableQuery ctq) throws IOException
	{
		Table table = new Table(ctq.getColumnDescriptions().length, ctq.getTableName(), ctq.getColumnDescriptions(), ctq.getPrimaryKeyColumn());
		int a = ctq.getColumnDescriptions().length;
		tables.add(table);
		
		ResultSet rs = new ResultSet(table.getSize(), table.getName(), ctq.getColumnDescriptions(), ctq.getPrimaryKeyColumn()) ;
		return rs;
	}
	
	/**
	 * runs the create index query
	 * @param ciq
	 * @return
	 * @throws IOException 
	 */
	private ResultSet createIndex(CreateIndexQuery ciq) throws IOException
	{
		String tableName = ciq.getTableName();
		for(Table table : tables)
		{
			if (table.getName().equals(tableName))
			{
				return generateSingleBoxResultSet(table.createIndex(ciq));
			}
		}
		throw new IOException("not a valid table name");
	}
	
	/**
	 * runs the update table query
	 * @param uq
	 * @return
	 * @throws IOException
	 */
	private ResultSet update(UpdateQuery uq) throws IOException
	{
		String tableName = uq.getTableName();
		for(Table table : tables)
		{
			if(table.getName().equals(tableName))
			{
				return generateSingleBoxResultSet(table.updateQuery(uq));
			}
		}
		throw new IOException("not a valid table name");
	}
	
	/**
	 * generates a result set when an exception is thrown. The result set is essentially a table. The List of Entries contains
	 * one entry with two DBBox's. The value of the first one is false signifying a false Query. The value of the second one is the String
	 * describing the exception that was thrown.
	 * @param b
	 * @param exception
	 * @return
	 * @throws IOException
	 */
	private ResultSet generateExceptionResultSet(boolean b, String exception) throws IOException
	{
		ResultSet rs = new ResultSet(2, "rs", null, null);
		DBBox<Boolean> boxOne = new DBBox<Boolean>(b);
		DBBox<String> boxTwo = new DBBox<String>(exception);
		DBEntry e = new DBEntry(2);
		e.getEntry()[0] = boxOne;
		e.getEntry()[1] = boxTwo;
		rs.getTableList().add(e);
		return rs;
	}
	/**
	 * generates a result set with one Entry with one box, true if the query was run properly, false if not (but also if there was no
	 * exception, or the generateExceptionResultSet method above would take effect)
	 * @param b
	 * @return
	 * @throws IOException 
	 */
	private ResultSet generateSingleBoxResultSet(boolean b) throws IOException
	{
		ResultSet rs = new ResultSet(1, "rs", null, null);
		DBBox<Object> newBox = new DBBox<Object>(b);
		DBEntry e = new DBEntry(1);
		e.getEntry()[0] = newBox;
		rs.getTableList().add(e);
		return rs;
	}

	
	
	
}
