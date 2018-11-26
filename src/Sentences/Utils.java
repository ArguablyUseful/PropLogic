package Sentences;

import java.util.List;

public class Utils {

	public static boolean IsComplexSentence(Sentence s)
	{
		return s.GetSentenceType() == Sentence.SentenceType.COMPLEX;
	}
	public static boolean IsAtomicSentence(Sentence s)
	{
		return s.GetSentenceType() == Sentence.SentenceType.ATOMIC;
	}
	public static Sentence InvertAwithB(Sentence s)
	{
		if ( Utils.IsComplexSentence(s))
		{
			ComplexSentence cs = (ComplexSentence)s;
			ComplexSentence result = new ComplexSentence(cs.GetRightSentence(), cs.GetConnective(), cs.GetLeftSentence());
			return result;
		} else 
		{
			return null;
		}
	}
	public static boolean CheckForCONNECTIVE(Sentence s, ComplexSentence.ConnectiveTypes connective)
	{
		if ( Utils.IsComplexSentence(s))
		{
			ComplexSentence cs = (ComplexSentence)s;
			return cs.GetConnective() == connective;
		}
		return false;
	}
	/*
	 * note that a true result imply that s is a complexSentence
	 */
	public static boolean CheckForAND(Sentence s)
	{
		return Utils.CheckForCONNECTIVE(s, ComplexSentence.ConnectiveTypes.AND);
	}
	/*
	 * note that a true result imply that s is a complexSentence
	 */
	public static boolean CheckForOR(Sentence s)
	{
		return Utils.CheckForCONNECTIVE(s, ComplexSentence.ConnectiveTypes.OR);
	}
	/*
	 * note that a true result imply that s is a complexSentence
	 */
	public static boolean CheckForNOT(Sentence s)
	{
		return Utils.CheckForCONNECTIVE(s, ComplexSentence.ConnectiveTypes.NOT);
	}
	/*
	 * note that a true result imply that s is a complexSentence
	 */
	public static boolean CheckForIMPLY(Sentence s)
	{
		return Utils.CheckForCONNECTIVE(s, ComplexSentence.ConnectiveTypes.IMPLY);
	}
	/*
	 * note that a true result imply that s is a complexSentence
	 */
	public static boolean CheckForEQUI(Sentence s)
	{
		return Utils.CheckForCONNECTIVE(s, ComplexSentence.ConnectiveTypes.EQUI);
	}
	
	/*
	 * a literal is either an atomic sentence or a complex sentence with connective "not" over an atomic sentence.
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
	
	//return a new sentence that is the complementary of A
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
	/*
	 * finds each literals inside setnence s and adds them to list "lst"
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
