package PropLogicEquivalences;

import Sentences.ComplexSentence;
import Sentences.Sentence;
import Sentences.Utils;

public class DeMorganANDtoOR extends LogicalEquivalence
{

	public DeMorganANDtoOR() {
		super("De Morgan AND to OR", "not(a && b) equi (not(a) || not(b))");
	}

	@Override
	public boolean IsEligible(Sentence s) {
		if ( Utils.CheckForNOT(s))
		{
			ComplexSentence cs = (ComplexSentence)s;
			if ( Utils.CheckForAND(cs.GetLeftSentence()))
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
			//no matter if left and right are "not", (a || b) still equi !(!a && !b)de morgan still works
			return true;
		}
		return false;
	}

	@Override
	public Sentence GetEquivalence(Sentence s) {
		if ( IsEligible(s))
		{
			ComplexSentence cs = (ComplexSentence)s;
			ComplexSentence csAND = (ComplexSentence)cs.GetLeftSentence();
			Sentence a = csAND.GetLeftSentence();
			Sentence b = csAND.GetRightSentence();
			ComplexSentence newA = new ComplexSentence(a, ComplexSentence.ConnectiveTypes.NOT, null);
			ComplexSentence newB = new ComplexSentence(b, ComplexSentence.ConnectiveTypes.NOT, null);
			ComplexSentence result = new ComplexSentence(newA, ComplexSentence.ConnectiveTypes.OR, newB);
			return result;
		} else
			return null;
	}

	@Override
	public Sentence GetInverseEquivalence(Sentence s) {
		if ( IsInverseEligible(s))
		{
			ComplexSentence cs = (ComplexSentence)s;
			Sentence a = cs.GetLeftSentence();
			Sentence b = cs.GetRightSentence();
			if ( Utils.CheckForNOT(a))
			{
				a = ((ComplexSentence)a).GetLeftSentence();
			} else
			{
				a = new ComplexSentence(a, ComplexSentence.ConnectiveTypes.NOT, null);
			}
			if ( Utils.CheckForNOT(b))
			{
				b = ((ComplexSentence)b).GetLeftSentence();
			} else
			{
				b = new ComplexSentence(b, ComplexSentence.ConnectiveTypes.NOT, null);
			}
			ComplexSentence csAND = new ComplexSentence(a, ComplexSentence.ConnectiveTypes.AND, b);
			ComplexSentence result = new ComplexSentence(csAND, ComplexSentence.ConnectiveTypes.NOT, null);
			return result;
		} else
			return null;
	}

}
