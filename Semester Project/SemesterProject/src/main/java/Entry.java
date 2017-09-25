import java.util.ArrayList;

public class Entry<K, V> {
	
	private K key = null;
	private V value = null;
	boolean isExternal;
	private ArrayList<V> listOfValues;
	
	protected Entry(K key, V value, boolean external)
	{
		isExternal = external;
		if(external)
		{
			this.key = key;
			listOfValues = new ArrayList<V>();
			listOfValues.add(value);
		}
		else
		{
			this.key = key;
			this.value = value;
		}
	}
	
	protected void addValue(V value)
	{
		if(isExternal)
		{
			listOfValues.add(value);
		}
	}
	
	protected void removeValue(V value)
	{
		for(V val : listOfValues)
		{
			if(val.equals(value))
			{
				listOfValues.remove(value);
			}
		}
	}
	
	protected boolean isExternal()
	{
		return isExternal;
	}
	
	protected ArrayList<V> getListOfValues()
	{
		return listOfValues;
	}
	
	protected K getKey()
	{
		return key;
	}
	
	protected V getValue()
	{
		return value;
	}
	
	protected V deleteValue(V valToDelete)
	{
		V oldValue = value;
		value = null;
		return oldValue;
	}
}

