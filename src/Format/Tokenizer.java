package Format;

import java.util.LinkedList;
import java.util.List;

/*
 * turn stream of text into stream of tokens
 */
public class Tokenizer {
	
	public static boolean checkOperator(String operator, String str)
	{
		if (str.length() < operator.length())
			return false;
		String subs = str.substring(0, operator.length());
		return subs.equals(operator);
	}
	public static int countAlphanum(String str)
	{
		int count = 0;
		while(str.length() >= count+1 && str.substring(count, count+1).matches("[a-zA-Z0-9_]"))
			count++;
		return count;
	}
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
