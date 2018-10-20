package Format;

public class Tokens {
	public enum TokenType { Symbol, LeftParenthesis, RightParenthesis, OperatorNot,
		OperatorAnd, OperatorOr, OperatorImply, OperatorEqui };
		
	public String symbol;
	public TokenType type;
	public Tokens(String symbol, TokenType type)
	{
		this.symbol = symbol;
		this.type = type;
	}
	public String toString()
	{
		return symbol;
	}
	/*
	 * return true if the token is an operator (not, and, or, imply or equi)
	 * false otherwise (sentence, left parenthesis, right parenthesis)
	 */
	public static boolean IsOperator(TokenType A)
	{
		return A.ordinal() >= TokenType.OperatorNot.ordinal();
	}
	/*
	 * return 1 if A come first
	 * return 0 if for same precedence
	 * return -1 if A come second
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
