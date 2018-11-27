package PropLogic;
import java.util.TreeMap;


/**
 * 
 * @author Corentin
 * This class holds the truth value of symbols
 */
public class Model {

	protected TreeMap<String, Boolean> truthTable; //key is the symbol, value is the truth.
	
	public Model()
	{
		this.truthTable = new TreeMap<String, Boolean>();
	}
	/**
	 * Adds the symbol with its associated truth value
	 * @param symbol the symbol name
	 * @param value true or false
	 */
	public void AssignSymbolValue(String symbol, boolean value)
	{
		this.truthTable.put(symbol, value);
	}
	/**
	 * Gives the truth value associated with a given symbol. 
	 * @param symbol the symbol we are checking for
	 * @return true, false or null. 
	 * note : null exists in only one case : the symbol is not in the model. use this to check wheter or not a symbol exists.
	 */
	public Boolean GetSymbolAssignatedValue(String symbol)
	{
		return this.truthTable.get(symbol);
	}
	/**
	 * Remove the symbol from the model
	 * @param symbol the symbol name
	 */
	public void RemoveSymbolAssignment(String symbol)
	{
		this.truthTable.remove(symbol);
	}
	/**
	 * create a deep copy of this instance.
	 * @return a new "Model" object that is the exact copy of "this" 
	 * note : exact copy means here that all symbol in "this" and their associated truth value will be in the returned model.
	 */
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
