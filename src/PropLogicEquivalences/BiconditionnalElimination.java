package PropLogicEquivalences;

import Sentences.ComplexSentence;
import Sentences.Sentence;
import Sentences.Utils;

public class BiconditionnalElimination extends LogicalEquivalence
{

	public BiconditionnalElimination() {
		super("Biconditionnal elimination","(a <=>b) equi ((a =>b) && (b => a))");
	}

	@Override
	public boolean IsEligible(Sentence s) {
		if ( Utils.CheckForEQUI(s))
		{
			return true;
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
			if ( Utils.CheckForIMPLY(left) && Utils.CheckForIMPLY(right))
			{
				ComplexSentence left_cs = (ComplexSentence)left;
				ComplexSentence right_cs = (ComplexSentence)right;
				return Sentence.SentenceAreTheSame(left_cs.GetLeftSentence(), right_cs.GetRightSentence())
						&& Sentence.SentenceAreTheSame(left_cs.GetRightSentence(), right_cs.GetLeftSentence());
			}
		}
		return false;
	}

	@Override
	public Sentence GetEquivalence(Sentence s) {
		if ( IsEligible(s ))
		{
			ComplexSentence cs = (ComplexSentence)s;
			Sentence a = cs.GetLeftSentence();
			Sentence b = cs.GetRightSentence();
			ComplexSentence aIMPLYb = new ComplexSentence(a, ComplexSentence.ConnectiveTypes.IMPLY, b);
			ComplexSentence bIMPLYa = new ComplexSentence(b, ComplexSentence.ConnectiveTypes.IMPLY, a);
			ComplexSentence result = new ComplexSentence(aIMPLYb, ComplexSentence.ConnectiveTypes.AND, bIMPLYa);
			return result;
		} else
			return null;
	}

	@Override
	public Sentence GetInverseEquivalence(Sentence s) {
		if ( IsInverseEligible(s))
		{
			ComplexSentence cs = (ComplexSentence)s;
			ComplexSentence csLeft = (ComplexSentence)cs.GetLeftSentence();
			ComplexSentence csA = (ComplexSentence)csLeft.GetLeftSentence();
			ComplexSentence csB = (ComplexSentence)csLeft.GetRightSentence();
			ComplexSentence result = new ComplexSentence(csA, ComplexSentence.ConnectiveTypes.EQUI, csB);
			return result;
		} else
			return null;
	}

}
