package PropLogicEquivalences;

import Sentences.ComplexSentence;
import Sentences.Sentence;
import Sentences.Utils;

public class DistributivityORtoAND extends LogicalEquivalence
{

	public DistributivityORtoAND() {
		super("Distributivity of Or over And", "(a || (b && c) equi (a || b) && (a || c))");
	}

	@Override
	public boolean IsEligible(Sentence s) {
		if ( Utils.CheckForOR(s))
		{
			ComplexSentence cs = (ComplexSentence)s;
			if ( Utils.CheckForAND(cs.GetRightSentence() ))
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean IsInverseEligible(Sentence s) {
		if ( Utils.CheckForAND(s))
		{
			ComplexSentence cs = (ComplexSentence)s;
			Sentence left = cs.GetLeftSentence();
			Sentence right = cs.GetRightSentence();
			if ( Utils.CheckForOR(left) && Utils.CheckForOR(right))
			{
				ComplexSentence cLeft = (ComplexSentence)left;
				ComplexSentence cRight = (ComplexSentence)right;
				if ( Sentence.SentenceAreTheSame(cLeft.GetLeftSentence(), cRight.GetLeftSentence()) )
				{
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public Sentence GetEquivalence(Sentence s) {
		if ( IsEligible(s))
		{
			ComplexSentence cs = (ComplexSentence)s;
			Sentence A = cs.GetLeftSentence();
			ComplexSentence right = (ComplexSentence)cs.GetRightSentence();
			Sentence B = right.GetLeftSentence();
			Sentence C = right.GetRightSentence();
			ComplexSentence aORb = new ComplexSentence(A, ComplexSentence.ConnectiveTypes.OR, B);
			ComplexSentence aORc = new ComplexSentence(A, ComplexSentence.ConnectiveTypes.OR, C);
			ComplexSentence result = new ComplexSentence(aORb, ComplexSentence.ConnectiveTypes.AND, aORc);
			return result;
		} else
			return null;
	}

	@Override
	public Sentence GetInverseEquivalence(Sentence s) {
		if ( IsInverseEligible(s))
		{
			ComplexSentence cs = (ComplexSentence)s;
			ComplexSentence cLeft = (ComplexSentence)cs.GetLeftSentence();
			ComplexSentence cRight = (ComplexSentence)cs.GetRightSentence();
			Sentence A = cLeft.GetLeftSentence();
			Sentence B = cLeft.GetRightSentence();
			Sentence C = cRight.GetRightSentence();
			ComplexSentence bANDc = new ComplexSentence(B, ComplexSentence.ConnectiveTypes.AND, C);
			ComplexSentence result = new ComplexSentence(A, ComplexSentence.ConnectiveTypes.OR, bANDc);
			return result;
		} else
			return null;
	}

}
