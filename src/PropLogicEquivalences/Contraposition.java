package PropLogicEquivalences;

import Sentences.ComplexSentence;
import Sentences.Sentence;
import Sentences.Utils;

public class Contraposition extends LogicalEquivalence
{

	public Contraposition() 
	{
		super("Contraposition", "(a => b) equi (!a => !b)");
	}

	@Override
	public boolean IsEligible(Sentence s) 
	{
		/*
		 * (a => b) equi (!b => !a)
		 * (!a => b) equi (!b) => !(!a))
		 * (!a => !b) equi (!(!b) => !(!a))
		 * (a => !b) equi (!(!b) => !a)
		 */
		return Utils.CheckForIMPLY(s);
	}

	@Override
	public boolean IsInverseEligible(Sentence s) // meaningless since the eligible format already accept the 4 cases
	{
		return IsEligible(s);
	}

	@Override
	public Sentence GetEquivalence(Sentence s) {
		if ( this.IsEligible(s))
		{
			ComplexSentence cs = (ComplexSentence)s;
			Sentence left = cs.GetLeftSentence();
			Sentence right = cs.GetRightSentence();
			ComplexSentence newLeft = new ComplexSentence(left, ComplexSentence.ConnectiveTypes.NOT, null);
			ComplexSentence newRight = new ComplexSentence(right, ComplexSentence.ConnectiveTypes.NOT, null);
			ComplexSentence result = new ComplexSentence(newRight, ComplexSentence.ConnectiveTypes.IMPLY, newLeft);
			return result;
		} else
			return null;
	}

	@Override
	public Sentence GetInverseEquivalence(Sentence s) {
		return GetEquivalence(s);
	}

}
