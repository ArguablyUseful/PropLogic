package PropLogicEquivalences;

import Sentences.Sentence;
import Sentences.Utils;

public class CommutatitvityOfAnd extends LogicalEquivalence
{

	public CommutatitvityOfAnd() 
	{
		super("Commutativity of &&", "(a && b) equi (b && a)");
	}
	
	@Override
	public boolean IsEligible(Sentence s) {
		return Utils.CheckForAND(s);
	}

	@Override
	public boolean IsInverseEligible(Sentence s) { 
		return IsEligible(s);
	}

	@Override
	public Sentence GetEquivalence(Sentence s) {
		if ( this.IsEligible(s) )
			return Utils.InvertAwithB(s);
		else return null;
	}

	@Override
	public Sentence GetInverseEquivalence(Sentence s) {
		return GetEquivalence(s);
	}

}
