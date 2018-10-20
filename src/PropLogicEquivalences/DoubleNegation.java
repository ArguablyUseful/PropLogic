package PropLogicEquivalences;

import Sentences.ComplexSentence;
import Sentences.Sentence;
import Sentences.Utils;

public class DoubleNegation extends LogicalEquivalence
{

	public DoubleNegation() {
		super("double negation", "!(!a) equi a");
	}

	@Override
	public boolean IsEligible(Sentence s) {
		if ( Utils.CheckForNOT(s))
		{
			Sentence left = ((ComplexSentence)s).GetLeftSentence();
			if ( Utils.CheckForNOT(left))
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean IsInverseEligible(Sentence s) { 
		//we can argue that the identity !(!a) equi a can be thought of as !(!(!a)) equi !a but we are not doing this here.
		//reason is that double negation usually is use to "clear" useless negations rather than going into convoluted sentences
		return !Utils.CheckForNOT(s);
	}

	@Override
	public Sentence GetEquivalence(Sentence s) {
		if ( this.IsEligible(s))
		{
			ComplexSentence left = (ComplexSentence)((ComplexSentence)s).GetLeftSentence();
			return left.GetLeftSentence();
		} else 
			return null;
	}

	@Override
	public Sentence GetInverseEquivalence(Sentence s) 
	{
		if ( this.IsInverseEligible(s))
		{
			ComplexSentence newCS = new ComplexSentence(s, ComplexSentence.ConnectiveTypes.NOT, null);
			ComplexSentence result = new ComplexSentence(newCS, ComplexSentence.ConnectiveTypes.NOT, null);
			return result;
		} else 
			return null;
	}
}
