package PropLogicEquivalences;

import Sentences.ComplexSentence;
import Sentences.Sentence;
import Sentences.Utils;

public class DistributivityANDtoOR extends LogicalEquivalence
{

	public DistributivityANDtoOR() {
		super("Distributivity of And over Or", "(a && (b || c) equi (a && b) || (a && c))");
	}

	@Override
	public boolean IsEligible(Sentence s) {
		if ( Utils.CheckForAND(s))
		{
			ComplexSentence cs = (ComplexSentence)s;
			if ( Utils.CheckForOR(cs.GetRightSentence() ))
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean IsInverseEligible(Sentence s) {
		if ( Utils.CheckForOR(s))
		{
			ComplexSentence cs = (ComplexSentence)s;
			Sentence left = cs.GetLeftSentence();
			Sentence right = cs.GetRightSentence();
			if ( Utils.CheckForAND(left) && Utils.CheckForAND(right))
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
			ComplexSentence aANDb = new ComplexSentence(A, ComplexSentence.ConnectiveTypes.AND, B);
			ComplexSentence aANDc = new ComplexSentence(A, ComplexSentence.ConnectiveTypes.AND, C);
			ComplexSentence result = new ComplexSentence(aANDb, ComplexSentence.ConnectiveTypes.OR, aANDc);
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
			ComplexSentence bORc = new ComplexSentence(B, ComplexSentence.ConnectiveTypes.OR, C);
			ComplexSentence result = new ComplexSentence(A, ComplexSentence.ConnectiveTypes.AND, bORc);
			return result;
		} else
			return null;
	}

}
