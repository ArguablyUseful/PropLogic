package PropLogicEquivalences;

import Sentences.ComplexSentence;
import Sentences.Sentence;
import Sentences.Utils;

public class Factoring extends LogicalEquivalence{

	public Factoring() {
		super("Factoring", "transform A <any connective> A into A.");
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean IsEligible(Sentence s) {
		if ( Utils.IsComplexSentence(s))
		{
			ComplexSentence cs = (ComplexSentence)s;
			if ( cs.GetConnective() != ComplexSentence.ConnectiveTypes.NONE
					&& cs.GetConnective() != ComplexSentence.ConnectiveTypes.NOT)
			{
				return Sentence.SentenceAreTheSame(cs.GetLeftSentence(), cs.GetRightSentence());
			}
		}
		return false;
	}

	@Override
	public boolean IsInverseEligible(Sentence s) { //meaningless
		return false;
	}

	@Override
	public Sentence GetEquivalence(Sentence s) {
		if ( IsEligible(s))
		{
			ComplexSentence cs = (ComplexSentence)s;
			return cs.GetLeftSentence();
		} else
			return null;
	}

	@Override
	public Sentence GetInverseEquivalence(Sentence s) { //meaningless
		return null;
	}

}
