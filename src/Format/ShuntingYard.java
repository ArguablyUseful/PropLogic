package Format;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import Sentences.AtomicSentence;
import Sentences.ComplexSentence;
import Sentences.ComplexSentence.ConnectiveTypes;
import Sentences.Sentence;

public class ShuntingYard {

	//when calling this method, set firstCall to true
	//firstCall is used to detect wheter or not we are inside a recursive call
	public static Sentence ASTFromSortedTokens(List<Tokens> tokenspf, boolean firstCall)
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
						System.out.println("Problem");
					}
				}
				break;
			}
		}
		return left.pop();
	}
	/*
	 * turn infix token stream into postfix token stream
	 */
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
