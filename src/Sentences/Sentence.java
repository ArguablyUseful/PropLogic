package Sentences;

import java.util.TreeSet;

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
public abstract class Sentence {
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
	
	public static boolean SentenceAreTheSame(Sentence A, Sentence B)
	{
		if  ( A == null || B == null)
			return false;
		if ( A.GetSentenceType() == B.GetSentenceType())
		{
			if ( A.GetSentenceType() == Sentence.SentenceType.ATOMIC)
			{
				AtomicSentence aA = (AtomicSentence)A;
				AtomicSentence aB = (AtomicSentence)B;
				return aA.GetSymbol().equals(aB.GetSymbol());
			} else 
			{
				ComplexSentence csA = (ComplexSentence)A;
				ComplexSentence csB = (ComplexSentence)B;
				return csA.GetConnective() == csB.GetConnective() 
						&& Sentence.SentenceAreTheSame(csA.GetLeftSentence(),csB.GetLeftSentence())
						&& Sentence.SentenceAreTheSame(csA.GetRightSentence(),csB.GetRightSentence());
			}
		} else return false;
	}
}
