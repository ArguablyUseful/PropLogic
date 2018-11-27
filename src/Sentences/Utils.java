package Sentences;

import java.util.List;

/**
 * 
 * @author Corentin
 * This class contains many utility methods.
 */
public class Utils {

	/**
	 * @param s the sentence to check
	 * @return true if the sentence is complex
	 */
	public static boolean IsComplexSentence(Sentence s)
	{
		return s.GetSentenceType() == Sentence.SentenceType.COMPLEX;
	}
	
	/**
	 * @param s the sentence to check
	 * @return true if the sentence is atomic
	 */
	public static boolean IsAtomicSentence(Sentence s)
	{
		return s.GetSentenceType() == Sentence.SentenceType.ATOMIC;
	}
	
	/**
	 * purpose is to manipulate a complex sentence so that A (operator) B becomes B (operator) A 
	 * @param s the sentence to invert
	 * @return a new sentence that has inverted the left and right parts of the complex sentence, or null
	 * note : null is returned if the sentence is not complex, or if the sentence is an operator "not" that is unary and cannot be inverted
	 */
	public static Sentence InvertAwithB(Sentence s)
	{
		if ( Utils.IsComplexSentence(s) && !Utils.CheckForNOT(s))
		{
			ComplexSentence cs = (ComplexSentence)s;
			ComplexSentence result = new ComplexSentence(cs.GetRightSentence(), cs.GetConnective(), cs.GetLeftSentence());
			return result;
		} else 
		{
			return null;
		}
	}
	
	/**
	 * purpose is to check if the sentence has a connective of a specific type.
	 * @param s the sentence to check
	 * @param connective the connective to check
	 * @return true if the sentence has the connective, false otherwise.
	 * note : a true result imply a complex sentence.
	 */
	public static boolean CheckForCONNECTIVE(Sentence s, ComplexSentence.ConnectiveTypes connective)
	{
		if ( Utils.IsComplexSentence(s))
		{
			ComplexSentence cs = (ComplexSentence)s;
			return cs.GetConnective() == connective;
		}
		return false;
	}
	/**
	 * shortcut for CheckForCONNECTIVE using AND as the connective
	 * @param s see CheckForCONNECTIVE
	 * @return see CheckForCONNECTIVE
	 */
	public static boolean CheckForAND(Sentence s)
	{
		return Utils.CheckForCONNECTIVE(s, ComplexSentence.ConnectiveTypes.AND);
	}
	
	/**
	 * shortcut for CheckForCONNECTIVE using OR as the connective
	 * @param s see CheckForCONNECTIVE
	 * @return see CheckForCONNECTIVE
	 */
	public static boolean CheckForOR(Sentence s)
	{
		return Utils.CheckForCONNECTIVE(s, ComplexSentence.ConnectiveTypes.OR);
	}
	
	/**
	 * shortcut for CheckForCONNECTIVE using NOT as the connective
	 * @param s see CheckForCONNECTIVE
	 * @return see CheckForCONNECTIVE
	 */
	public static boolean CheckForNOT(Sentence s)
	{
		return Utils.CheckForCONNECTIVE(s, ComplexSentence.ConnectiveTypes.NOT);
	}
	
	/**
	 * shortcut for CheckForCONNECTIVE using IMPLY as the connective
	 * @param s see CheckForCONNECTIVE
	 * @return see CheckForCONNECTIVE
	 */
	public static boolean CheckForIMPLY(Sentence s)
	{
		return Utils.CheckForCONNECTIVE(s, ComplexSentence.ConnectiveTypes.IMPLY);
	}
	
	/**
	 * shortcut for CheckForCONNECTIVE using EQUI as the connective
	 * @param s see CheckForCONNECTIVE
	 * @return see CheckForCONNECTIVE
	 */
	public static boolean CheckForEQUI(Sentence s)
	{
		return Utils.CheckForCONNECTIVE(s, ComplexSentence.ConnectiveTypes.EQUI);
	}
	
	/**
	 * purpose is to determine wheter or not a sentence is a literal
	 * reminder : a literal is either an atomic sentence or a complex sentence with a "not" connective that apply to an atomic sentence
	 * A is a literal
	 * !A is a literal
	 * !(A OR B) is not a literal
	 * @param s the sentence to check
	 * @return true if the sentence is a literal, false otherwise
	 */
	public static boolean IsLiteral(Sentence s)
	{
		if ( Utils.IsAtomicSentence(s) ) //case of a literal
			return true;
		else if ( Utils.CheckForNOT(s)) //case of a "not"
		{
			ComplexSentence cs = (ComplexSentence)s;
			if ( Utils.IsAtomicSentence(cs.GetLeftSentence())) //if it is a not literal, it is the same as a literal.
				return true;
			else // means that "not" is applied to a complex sentence, not a literal. means not a CNF.
				return false;
		} else
			return false;
	}
	
	/**
	 * purpose is to provide a sentence that is the complementary of the provided sentence
	 * @param A the provided sentence from which we want to get a complementary
	 * @return a new sentence that is the complementary of A
	 */
	public static Sentence GetComplementaryLiteral(Sentence A)
	{
		if ( Utils.CheckForNOT(A))
		{
			ComplexSentence cs = (ComplexSentence)A;
			return cs.GetLeftSentence();
		} else
		{
			ComplexSentence cs = new ComplexSentence(A, ComplexSentence.ConnectiveTypes.NOT, null);
			return cs;
		}
	}
	/**
	 * purpose is to check if two sentence are complementary.
	 * @param A first sentence
	 * @param B second sentence
	 * @return true if sentence A is a complementary of setnence B
	 * note : it only checks if A is !(B) OR !(A) is B
	 */
	public static boolean AreComplementaryLiterals(Sentence A, Sentence B)
	{
		if ( Utils.IsLiteral(A) && Utils.IsLiteral(B))
		{
			ComplexSentence cs;
			if ( Utils.CheckForNOT(A) && !Utils.CheckForNOT(B))
			{
				cs = (ComplexSentence)A;
				if ( Sentence.SentenceAreTheSame(cs.GetLeftSentence(), B))
					return true;
			} else if ( Utils.CheckForNOT(B) && !Utils.CheckForNOT(A))
			{
				cs = (ComplexSentence)B;
				if ( Sentence.SentenceAreTheSame(cs.GetLeftSentence(), A))
					return true;
			}
		}
		return false;
	}
	
	/**
	 * purpose is to get the list of literals used in a sentence
	 * @param s the sentence from which we wish to get all the literals
	 * @param lst a list to which we will adds all the literals found in s
	 * note : it is a list, so redundant literals might occurs.
	 */
	public static void ExtractLiterals(Sentence s, List<Sentence> lst)
	{
		if ( Utils.IsLiteral(s))
		{
			lst.add(s);
		} else
		{
			ComplexSentence cs = (ComplexSentence)s;
			Utils.ExtractLiterals(cs.GetLeftSentence(), lst);
			Utils.ExtractLiterals(cs.GetRightSentence(), lst);
		}
	}
}
