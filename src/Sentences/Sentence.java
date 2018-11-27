package Sentences;

import java.util.List;
import java.util.TreeSet;

import Format.ShuntingYard;
import Format.Tokenizer;
import Format.Tokens;

/**
 * 
 * @author Corentin
 * Sentence is a node in the abstract tree syntax that represent a full PL proposition.
 * It can only be implemented as an Atomic or a Complex Sentence. 
 * "equals" ,"hashcode" and "compareTo" use the string representation of the Sentence (given by toString())
 * 
		Sentence -> AtomicSentence | ComplexSentence
		AtomicSentence -> True | False | symbol
		ComplexSentence -> 	(Sentence)
							| !Sentence (not)
							| Sentence && Sentence (and)
							| Sentence || Sentence (or)
							| Sentence => Sentence (imply)
							| Sentence <=> Sentence (equi)
 */
public abstract class Sentence implements Comparable<Sentence> {
	public enum SentenceType { ATOMIC, COMPLEX };
	
	private SentenceType type;
	public Sentence(SentenceType type)
	{
		this.type = type;
	}
	public SentenceType GetSentenceType() { return this.type; }

	/**
	 * Get a set of all the symbols of the sentence and its components.
	 * this function guarantee that each symbol only appear once (hence the set datastructure)
	 * @return the set of all symbols used by this sentence.
	 */
	public TreeSet<String> ExtractSymbols()
	{
		TreeSet<String> symSet = new TreeSet<String>();
		if ( this.GetSentenceType() == SentenceType.ATOMIC)
		{
			symSet.add(((AtomicSentence)this).GetSymbol());
		} else
		{
			ComplexSentence cs = (ComplexSentence) this;
			symSet.addAll(cs.GetLeftSentence().ExtractSymbols());
			symSet.addAll(cs.GetRightSentence().ExtractSymbols());
		}
		return symSet;
	}
	public String toString()
	{
		String result = null;
		if ( this.GetSentenceType() == SentenceType.ATOMIC)
		{
			result = ((AtomicSentence)this).GetSymbol();
		} else
		{
			ComplexSentence cs = (ComplexSentence)this;
			
			switch(cs.GetConnective())
			{
			case NONE:
				result += cs.GetLeftSentence().toString();
				break;
			case NOT:
				result = "!";
				result += cs.GetLeftSentence().toString();
				break;
			case AND:
				result = "(";
				result += cs.GetLeftSentence().toString();
				result += " && ";
				result += cs.GetRightSentence().toString();
				result += ")";
				break;
			case OR:
				result = "(";
				result += cs.GetLeftSentence().toString();
				result += " || ";
				result += cs.GetRightSentence().toString();
				result += ")";
				break;
			case IMPLY:
				result = "(";
				result += cs.GetLeftSentence().toString();
				result += " => ";
				result += cs.GetRightSentence().toString();
				result += ")";
				break;
			case EQUI:
				result = "(";
				result += cs.GetLeftSentence().toString();
				result += " <=> ";
				result += cs.GetRightSentence().toString();
				result += ")";
				break;			
			}
		}
		
		return result;
	}
	
	@Override
	public int hashCode() {
		return this.toString().hashCode();
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Sentence other = (Sentence) obj;
		if (type != other.type)
			return false;
		return Sentence.SentenceAreTheSame(this, other);
	}
	@Override
	public int compareTo(Sentence that)
	{
		return this.toString().compareTo(that.toString());
	}
	
	/**
	 * simple way to compare two Sentences.
	 * @param A sentence A
	 * @param B sentence B
	 * @return wheter or not the two sentences have the same syntax.
	 * note : it is a cheap way to check that 2 sentences are the same in the context of AST structure that can represent two identical sentences with different trees. 
	 */
	public static boolean SentenceAreTheSame(Sentence A, Sentence B)
	{
		if  ( A == null || B == null)
			return false;
		return A.toString().equals(B.toString());
	}
	/**
	 * Convert a strnig into a Sentence using Tokenizer and shunting yard
	 * @param s the string representing a PL sentence
	 * @return the AST root node for the sentence.
	 */
	public static Sentence GetSentenceFromString(String s)
	{
		List<Tokens> sentenceTokenized = Tokenizer.tokenize(s);
		List<Tokens> sortedSentenceTokenized = ShuntingYard.PostfixTokens(sentenceTokenized);
		Sentence sentenceASTForm = ShuntingYard.ASTFromPostfixTokens(sortedSentenceTokenized);
		return sentenceASTForm;
	}
	
}
