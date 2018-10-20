package PropLogic;
import java.util.TreeMap;



public class Model {

	protected TreeMap<String, Boolean> truthTable;
	
	public Model()
	{
		this.truthTable = new TreeMap<String, Boolean>();
	}
	public void AssignSymbolValue(String symbol, boolean value)
	{
		this.truthTable.put(symbol, value);
	}
	public Boolean GetSymbolAssignatedValue(String symbol)
	{
		return this.truthTable.get(symbol);
	}
	public void RemoveSymbolAssignment(String symbol)
	{
		this.truthTable.remove(symbol);
	}
	public Model DeepCopy()
	{
		Model toReturn = new Model();
		for ( String sym : this.truthTable.keySet())
		{
			toReturn.AssignSymbolValue(sym, this.truthTable.get(sym));
		}
		return toReturn;
	}
	
}
