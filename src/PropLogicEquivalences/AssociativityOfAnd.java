package PropLogicEquivalences;

import Sentences.ComplexSentence;
import Sentences.Sentence;
import Sentences.Utils;

public class AssociativityOfAnd extends LogicalEquivalence
{

	public AssociativityOfAnd() {
		super("Associativity of &&", "((a && b) && c) equi (a && (b && c))");
	}

	@Override
	public boolean IsEligible(Sentence s) {
		if ( Utils.CheckForAND(s))
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
		if ( Utils.CheckForAND(s))
		{
			ComplexSentence cs = (ComplexSentence)s;
			if ( Utils.CheckForAND(cs.GetRightSentence()))
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
			ComplexSentence aANDb = (ComplexSentence)cs.GetLeftSentence();
			Sentence a = aANDb.GetLeftSentence();
			Sentence b = aANDb.GetRightSentence();
			Sentence c = cs.GetRightSentence();
			ComplexSentence bANDc = new ComplexSentence(b, ComplexSentence.ConnectiveTypes.AND, c);
			ComplexSentence result = new ComplexSentence(a, ComplexSentence.ConnectiveTypes.AND, bANDc);
			return result;
		} else
			return null;
	}

	@Override
	public Sentence GetInverseEquivalence(Sentence s) {
		if ( IsInverseEligible(s) )
		{
			ComplexSentence cs = (ComplexSentence)s;
			ComplexSentence bANDc = (ComplexSentence)cs.GetRightSentence();
			Sentence b = bANDc.GetLeftSentence();
			Sentence c = bANDc.GetRightSentence();
			Sentence a = cs.GetLeftSentence();
			ComplexSentence aANDb = new ComplexSentence(a, ComplexSentence.ConnectiveTypes.AND, b);
			ComplexSentence result = new ComplexSentence(aANDb, ComplexSentence.ConnectiveTypes.AND, c);
			return result;
		} else
			return null;
	}

}
