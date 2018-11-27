package Format;


/**
 * @author Corentin
 * 
 * This class represent one token of a PL sentence.
 * It is used by the Tokenizer to turn a string of text representing a PL Sentence into a string of tokens representing the same PL Sentence.
 * It also is used by the shuntingYard algorithm to build an AST (Abstract syntax tree) representation of the PL Sentence.
 */
public class Tokens {
	public enum TokenType { Symbol, LeftParenthesis, RightParenthesis, OperatorNot,
		OperatorAnd, OperatorOr, OperatorImply, OperatorEqui };
		
	public String symbol;
	public TokenType type;
	/**
	 * 
	 * @param symbol can be either an alphanum name (including underscore) or the text representation of an operator. See class Tokenizer
	 * @param type a type. see TokenType in this class.
	 */
	public Tokens(String symbol, TokenType type)
	{
		this.symbol = symbol;
		this.type = type;
	}
	public String toString()
	{
		return symbol;
	}
	
	/**
	 * Is used to quickly discriminate operators from non operators tokens
	 * @param A a tokenType
	 * @return true if the operator is an operator (not, and, or, imply or equivalent) false otherwise.
	 */
	public static boolean IsOperator(TokenType A)
	{
		return A.ordinal() >= TokenType.OperatorNot.ordinal();
	}
	
	/**
	 * Compare two tokens to determine the precedence of operations.
	 * @param A The first token
	 * @param B The second token
	 * @return 1 if A comes first, 0 if A and B share the same precedence, -1 if B comes first.
	 */
	public static int GetPrecedence(TokenType A, TokenType B)
	{
		int valA = A.ordinal() - TokenType.OperatorNot.ordinal();
		int valB = B.ordinal() - TokenType.OperatorNot.ordinal();
		if ( valA < valB) return 1;
		else if (valA == valB) return 0;
		else return -1;
	}
}
