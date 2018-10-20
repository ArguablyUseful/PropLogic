package PropLogicEquivalences;

import Sentences.ComplexSentence;
import Sentences.Sentence;
import Sentences.Utils;

public class AssociatitivtyOfOr extends LogicalEquivalence {

	public AssociatitivtyOfOr() {
		super("Associativity of ||", "((a || b) || c) equi (a || (b || c))");
	}
	
	@Override
	public boolean IsEligible(Sentence s) {
		if ( Utils.CheckForOR(s))
		{
			ComplexSentence cs = (ComplexSentence)s;
			if ( Utils.CheckForOR(cs.GetLeftSentence()))
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
			if ( Utils.CheckForOR(cs.GetRightSentence()))
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public Sentence GetEquivalence(Sentence s) {
		if ( IsEligible(s))
		{
			ComplexSentence cs = (ComplexSentence)s;
			ComplexSentence aORb = (ComplexSentence)cs.GetLeftSentence();
			Sentence a = aORb.GetLeftSentence();
			Sentence b = aORb.GetRightSentence();
			Sentence c = cs.GetRightSentence();
			ComplexSentence bORc = new ComplexSentence(b, ComplexSentence.ConnectiveTypes.OR, c);
			ComplexSentence result = new ComplexSentence(a, ComplexSentence.ConnectiveTypes.OR, bORc);
			return result;
		} else
			return null;
	}

	@Override
	public Sentence GetInverseEquivalence(Sentence s) {
		if ( IsInverseEligible(s) )
		{
			ComplexSentence cs = (ComplexSentence)s;
			ComplexSentence bORc = (ComplexSentence)cs.GetRightSentence();
			Sentence b = bORc.GetLeftSentence();
			Sentence c = bORc.GetRightSentence();
			Sentence a = cs.GetLeftSentence();
			ComplexSentence aORb = new ComplexSentence(a, ComplexSentence.ConnectiveTypes.OR, b);
			ComplexSentence result = new ComplexSentence(aORb, ComplexSentence.ConnectiveTypes.OR, c);
			return result;
		} else
			return null;
	}
}
