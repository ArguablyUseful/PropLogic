package Sentences;

import java.util.List;
import java.util.TreeSet;

import Format.ShuntingYard;
import Format.Tokenizer;
import Format.Tokens;

/*
 * Sentence -> AtomicSentence | ComplexSentence
 * AtomicSentence -> True | False | sym
 * ComplexSentence -> (Sentence)
 * 					| [Sentence]
 * 					| !Sentence (not)
 * 					| Sentence && Sentence (and)
 * 					| Sentence || Sentence (or)
 * 					| Sentence => Sentence (imply)
 * 					| Sentence <=> Sentence (equi)
 * 
 */
public abstract class Sentence implements Comparable<Sentence> {
	public enum SentenceType { ATOMIC, COMPLEX };
	
	private SentenceType type;
	public Sentence(SentenceType type)
	{
		this.type = type;
	}
	public SentenceType GetSentenceType() { return this.type; }
	
	/*
	 * Get a set of all the symbols of the sentence and its components.
	 * this function guarantee that each symbol only appear once (hence the set datastructure)
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
	public static boolean SentenceAreTheSame(Sentence A, Sentence B)
	{
		if  ( A == null || B == null)
			return false;
		return A.toString().equals(B.toString());
	}
	public static Sentence GetSentenceFromString(String s)
	{
		List<Tokens> sentenceTokenized = Tokenizer.tokenize(s);
		List<Tokens> sortedSentenceTokenized = ShuntingYard.PostfixTokens(sentenceTokenized);
		Sentence sentenceASTForm = ShuntingYard.ASTFromSortedTokens(sortedSentenceTokenized, true);
		return sentenceASTForm;
	}
	@Override
	public int compareTo(Sentence that)
	{
		return this.toString().compareTo(that.toString());
	}
}
