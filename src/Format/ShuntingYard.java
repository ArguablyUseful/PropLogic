package Format;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import Sentences.AtomicSentence;
import Sentences.ComplexSentence;
import Sentences.ComplexSentence.ConnectiveTypes;
import Sentences.Sentence;

/**
 * 
 * @author Corentin
 * This class is used to turn a stream of Token (provided by Tokenizer from text stream) into an Abstract Syntax Tree (AST) of Sentence (see Sentences/Sentence).
 * the method PostfixTokens turns an infix token stream into a postfix token stream
 * the private method ASTFromSortedTokens then use this postfix sorted stream of tokens to generate the AST representation of the PL Sentence.
 * the method "ASTFromPostfixTokens()" is used to gives the user access to ASTFromSortedTokens without exposing the boolean used by the recursion. 
 */
public class ShuntingYard {
	
	/**
	 * implementation of Dijskra's Shunting Yard algorithm.
	 * note : this do not check for the validity of the stream of tokens. If the list of token provided is not valid, behavior is undefined.
	 * @param postfixTokens the list of tokens representing the PL Sentence in post fix order
	 * @return the root node of an AST representing the PL sentence or null.
	 */
	public static Sentence ASTFromPostfixTokens(List<Tokens> postfixTokens)
	{
		try {
			return ShuntingYard.ASTFromSortedTokens(postfixTokens, true);
		} catch (Exception e) {
			return null;
		}
	}
	/**
	 * 
	 * @param tokenspf a list of tokens in post fix order
	 * @param firstCall a boolean value used by the recursion
	 * @return the root node of an AST or null. 
	 * @throws Exception for errors. Note that only one error is detected : a token list starting with a closing parenthesis.
	 */
	private static Sentence ASTFromSortedTokens(List<Tokens> tokenspf, boolean firstCall) throws Exception
	{
		Stack<Sentence> left, right;
		left = new Stack<Sentence>();
		right = new Stack<Sentence>();
		while(!tokenspf.isEmpty())
		{
			//use recursive call to manage parenthesis with firstCall set to false
			Tokens t = tokenspf.get(0);
			tokenspf.remove(0);
			ConnectiveTypes ct = ConnectiveTypes.NONE;
			switch(t.type)
			{
			case Symbol:
				{
					Sentence s = new AtomicSentence(t.symbol);
					if ( left.size() == right.size())
						left.add(s);
					else
						right.add(s);
				}
				break;
			case OperatorNot:
				{
					ct = ConnectiveTypes.NOT;
					ComplexSentence cs = null;
					if ( left.size() == right.size())
						cs = new ComplexSentence(right.pop(), ct ,null);
					else
						cs = new ComplexSentence(left.pop(), ct ,null);
					if ( left.size() == right.size())
						left.add(cs);
					else
						right.add(cs);
				}
				break;
			case OperatorAnd:
				{
					ct = ConnectiveTypes.AND;
					ComplexSentence cs = new ComplexSentence(left.pop(), ct ,right.pop());
					if ( left.size() == right.size())
						left.add(cs);
					else
						right.add(cs);
				}
				break;
			case OperatorOr:
				{
					ct = ConnectiveTypes.OR;
					ComplexSentence cs = new ComplexSentence(left.pop(), ct ,right.pop());
					if ( left.size() == right.size())
						left.add(cs);
					else
						right.add(cs);
				}
				break;
			case OperatorImply:
				{
					ct = ConnectiveTypes.IMPLY;
					ComplexSentence cs = new ComplexSentence(left.pop(), ct ,right.pop());
					if ( left.size() == right.size())
						left.add(cs);
					else
						right.add(cs);
				}
				break;
			case OperatorEqui:
				{
					ct = ConnectiveTypes.EQUI;
					ComplexSentence cs = new ComplexSentence(left.pop(), ct ,right.pop());
					if ( left.size() == right.size())
						left.add(cs);
					else
						right.add(cs);
				}
				break;
			case LeftParenthesis:
				{
					Sentence s = ShuntingYard.ASTFromSortedTokens(tokenspf, false);
					if ( s != null)
					{
						if ( left.size() == right.size())
							left.add(s);
						else
							right.add(s);	
					}
				}
				break;
			case RightParenthesis:
				{
					if ( !firstCall)
					{
						if ( left.isEmpty())
							return null;
						else
							return left.pop();
					} else
					{
						throw new Exception("A valid PL Sentence cannot start with closing parenthesis ')'");
					}
				}
			}
		}
		return left.pop();
	}
	/*
	 * turn infix token stream into postfix token stream
	 */
	@SuppressWarnings("incomplete-switch")
	public static LinkedList<Tokens> PostfixTokens(List<Tokens> tokens)
	{
		LinkedList<Tokens> output = new LinkedList<Tokens>();
		Stack<Tokens> operators = new Stack<Tokens>();
		
		while(!tokens.isEmpty())
		{
			Tokens newlyReadToken = tokens.get(0);
			tokens.remove(0);
			if ( Tokens.IsOperator(newlyReadToken.type))
			{
				while( !operators.isEmpty() && Tokens.GetPrecedence(operators.peek().type,newlyReadToken.type) >= 1 && operators.peek().type != Tokens.TokenType.LeftParenthesis)
				{
					Tokens top = operators.pop();
					output.addLast(top);
				}
				operators.add(newlyReadToken);
			} else
			{
				switch(newlyReadToken.type)
				{
				case LeftParenthesis:
					operators.add(newlyReadToken);
					break;
				case RightParenthesis:
					while(operators.peek().type != Tokens.TokenType.LeftParenthesis)
						output.addLast(operators.pop());
					if ( operators.peek().type != Tokens.TokenType.LeftParenthesis)
					{
						//invalid expression : missing a parenthesis
						System.out.println("Error");
						return null;
					} else
							output.addLast(operators.pop());
					output.addLast(newlyReadToken);
					break;
				case Symbol:
					output.addLast(newlyReadToken);
					break;
				}
			}
		}
		while(!operators.isEmpty())
		{
			output.addLast(operators.pop());
		}
		return output;
	}
	
	
	
}
