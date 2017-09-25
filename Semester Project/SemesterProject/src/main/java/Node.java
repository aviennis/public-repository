public class Node <K , V> {
	
	private Entry<K, V>[] node;
	private int height;
	private Entry<K, Node<K, V>> parentEntry;
	private Node<K, Node<K, V>> parentNode;
	private int entries;
	private Node leftSibling;
	private Node rightSibling;

	
	protected Node(int size, int height, Entry<K, Node<K, V>> parentEntry, Node<K, Node<K , V>> parentNode)
	{
		node = new Entry[size];
		this.height = height;
		this.parentEntry = parentEntry;
		this.parentNode = parentNode;
		entries = 0;
		leftSibling = null;
		rightSibling = null;
	}
	
	
	protected void setLeftSibling(Node n)
	{
		leftSibling = n;
	}
	protected void setRightSibling(Node n)
	{
		rightSibling = n;
	}
	protected Node getLeftSibling()
	{
		return leftSibling;
	}
	protected Node getRightSibling()
	{
		return rightSibling;
	}
	
	protected int getHeight()
	{
		return height;
	}
	
	protected Entry<K, V>[] getNode()
	{
		return node;
	}
	
	protected Entry<K, Node<K, V>> getParentEntry()
	{
		return parentEntry;
	}
	
	protected void setParentEntry(Entry<K, Node<K , V>> parentEntryToBe)
	{
		parentEntry = parentEntryToBe;
	}
	
	protected void incrementHeight()
	{
		height++;
	}
	
	protected Node<K, Node<K, V>> getParentNode()
	{
		return parentNode;
	}
	
	protected void setParentNode(Node<K, Node<K, V>> parentNodeToBe)
	{
		parentNode = parentNodeToBe;
	}
	
	protected void incrementEntries()
	{
		entries++;
	}
	
	protected int getAmountOfEntries()
	{
		return entries;
	}
	protected void setAmountOfEntires(int num)
	{
		entries = num;
	}
	
	public String toString()
	{
		String s = "";
		for(Entry e : this.getNode())
		{
			s += (e.toString() + " ");
		}
		return s;
	}
}

