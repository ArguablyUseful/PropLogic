package PropLogicEquivalences;

import Sentences.ComplexSentence;
import Sentences.Sentence;
import Sentences.Utils;

public class DeMorganORtoAND extends LogicalEquivalence
{

	public DeMorganORtoAND() {
		super("De Morgan OR to AND","!(a || b) equi (!a && !b)");
		// TODO Auto-generated constructor stub
	}
	@Override
	public boolean IsEligible(Sentence s) {
		if ( Utils.CheckForNOT(s))
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
		if ( Utils.CheckForAND(s))
		{
			//no matter if left and right are "not", (a && b) still equi !(!a || !b)
			return true;
		}
		return false;
	}

	@Override
	public Sentence GetEquivalence(Sentence s) {
		if ( IsEligible(s))
		{
			ComplexSentence cs = (ComplexSentence)s;
			ComplexSentence csOR = (ComplexSentence)cs.GetLeftSentence();
			Sentence a = csOR.GetLeftSentence();
			Sentence b = csOR.GetRightSentence();
			ComplexSentence newA = new ComplexSentence(a, ComplexSentence.ConnectiveTypes.NOT, null);
			ComplexSentence newB = new ComplexSentence(b, ComplexSentence.ConnectiveTypes.NOT, null);
			ComplexSentence result = new ComplexSentence(newA, ComplexSentence.ConnectiveTypes.AND, newB);
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
			ComplexSentence csOR = new ComplexSentence(a, ComplexSentence.ConnectiveTypes.OR, b);
			ComplexSentence result = new ComplexSentence(csOR, ComplexSentence.ConnectiveTypes.NOT, null);
			return result;
		} else
			return null;
	}
}
