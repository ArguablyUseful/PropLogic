package Format;

import java.util.LinkedList;
import java.util.List;

/*
 * turn stream of text into stream of tokens
 */
/**
 * 
 * @author Corentin
 *
 * This class aims to turn a stream of text representing a PL Sentence into a stream of Tokens representing the exact same sentence.
 * The main method is "tokenize"
 * 
 * a valid sentence is composed of PL logical operators, symbol names, parenthesis and spaces
 * valids logical operators are (in reverse precedence)
 * <=> equivalence
 * => implication
 * || or
 * && and
 * ! not
 * parenthesis are normal ones : " ( ) ". note that "tokenize" do not check for consistency. 
 * valid name for a symbol use only alphanumerical characters (a-z, A-Z, 0-9) and underscore ( _ ). There is no length maximum.
 * 
 * example : "house && dog <=> !robbery"
 * adding more parenthesis than needed is not a problem (in AST form, all parenthesis are eliminated)
 * "(house && dog) <=> !(robbery)" is equivalent.
 */
public class Tokenizer {
	
	/**
	 * This method checks wheter or not the String containing the remaining of the input starts with the string representing a given operator. 
	 * @param operator the string representation of the operator
	 * @param str the string containing what is left of the input 
	 * @return true if the string str start with "operator". false otherwise.
	 */
	public static boolean checkOperator(String operator, String str)
	{
		if (str.length() < operator.length())
			return false;
		String subs = str.substring(0, operator.length());
		return subs.equals(operator);
	}
	
	/**
	 * This methods count the quantity of characters used by a stream of text representing a symbol
	 * 
	 * there is no limit in length.
	 * 
	 * @param str the string containing what is left of the input.
	 * @return the quantity of characters that are assumed to be part of the symbol name.
	 */
	public static int countAlphanum(String str)
	{
		int count = 0;
		while(str.length() >= count+1 && str.substring(count, count+1).matches("[a-zA-Z0-9_]"))
			count++;
		return count;
	}
	
	/**
	 * This method turns a PL sentence in text format into a stream of tokens representing the same PL sentence
	 * 
	 * note : this method do not check for any consistency in the tokens. It simply transform a text stream into tokens.
	 * the only "failure" detected is the use of illegal substrings (=substrings that do not represent any operator or symbol or parenthesis) 
	 * @param stream the text input
	 * @return a List of tokens or null if some text are neither valid symbols nor valid operators.
	 */
	public static List<Tokens> tokenize(String stream)
	{
		LinkedList<Tokens> result = new LinkedList<Tokens>();
		while(!stream.isEmpty())
		{
			Tokens tks = null;
			if ( checkOperator(" ", stream))
			{
				stream = stream.substring(1);
				continue;
			} else if ( checkOperator("(", stream))
			{
				tks = new Tokens("(", Tokens.TokenType.LeftParenthesis);
				stream = stream.substring(1);
			} else if ( checkOperator(")",stream))
			{
				tks = new Tokens(")", Tokens.TokenType.RightParenthesis);
				stream = stream.substring(1);
			} else if ( checkOperator("&&", stream))
			{
				tks = new Tokens("&&", Tokens.TokenType.OperatorAnd);
				stream = stream.substring(2);
			} else if ( checkOperator("||", stream))
			{
				tks = new Tokens("||", Tokens.TokenType.OperatorOr);
				stream = stream.substring(2);
			} else if ( checkOperator("!",stream))
			{
				tks = new Tokens("!", Tokens.TokenType.OperatorNot);
				stream = stream.substring(1);
			} else if ( checkOperator("=>", stream))
			{
				tks = new Tokens("=>", Tokens.TokenType.OperatorImply);
				stream = stream.substring(2);
			} else if ( checkOperator("<=>", stream))
			{
				tks = new Tokens("<=>", Tokens.TokenType.OperatorEqui);
				stream = stream.substring(3);
			} else {
				int count = Tokenizer.countAlphanum(stream);
				if ( count == 0)
				{
					System.out.println("Error : invalid sentence");
					return null;
				}
				tks = new Tokens(stream.substring(0,count), Tokens.TokenType.Symbol);
				stream = stream.substring(count);
			}
			result.add(tks);
		}
		return result;
	}
	

}
