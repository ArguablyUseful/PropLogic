package PropLogicEquivalences;

import Sentences.ComplexSentence;
import Sentences.Sentence;
import Sentences.Utils;

public class ImplicationElimination extends LogicalEquivalence
{

	public ImplicationElimination() 
	{
		super("Implication elimination", "(a => b) equi (!(a) || b. Inverse is (a || b) equi (!a => b)");
	}

	@Override
	public boolean IsEligible(Sentence s) {
		return Utils.CheckForIMPLY(s);
	}

	@Override
	public boolean IsInverseEligible(Sentence s) {
		return Utils.CheckForOR(s);
	}

	@Override
	public Sentence GetEquivalence(Sentence s) {
		if ( IsEligible(s))
		{
			ComplexSentence cs = (ComplexSentence)s;
			Sentence left = cs.GetLeftSentence();
			ComplexSentence newLeft = new ComplexSentence(left, ComplexSentence.ConnectiveTypes.NOT, null);
			Sentence right = cs.GetRightSentence();
			ComplexSentence result = new ComplexSentence(newLeft, ComplexSentence.ConnectiveTypes.OR, right);
			return result;
		} else
			return null;
	}

	@Override
	public Sentence GetInverseEquivalence(Sentence s) {
		if ( IsInverseEligible(s))
		{
			ComplexSentence cs = (ComplexSentence)s;
			Sentence left = cs.GetLeftSentence();
			ComplexSentence newLeft = new ComplexSentence(left, ComplexSentence.ConnectiveTypes.NOT, null);
			Sentence right = cs.GetRightSentence();
			ComplexSentence result = new ComplexSentence(newLeft, ComplexSentence.ConnectiveTypes.IMPLY, right);
			return result;
		} else 
			return null;
	}

}
