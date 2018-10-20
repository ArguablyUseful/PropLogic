package Sentences;

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
}
