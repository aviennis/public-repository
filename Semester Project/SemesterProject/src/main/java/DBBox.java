
public class DBBox <T> {
	
	private T t;
	
	protected DBBox(T t)
	{
		this.t = t;
	}
	
	protected T getValue()
	{
		return t;
	}
	
	
}
