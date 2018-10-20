package Format;
import PropLogicEquivalences.BiconditionnalElimination;
import PropLogicEquivalences.CommutativityOfOr;
import PropLogicEquivalences.DeMorganANDtoOR;
import PropLogicEquivalences.DeMorganORtoAND;
import PropLogicEquivalences.DistributivityORtoAND;
import PropLogicEquivalences.DoubleNegation;
import PropLogicEquivalences.Factoring;
import PropLogicEquivalences.LogicalEquivalence;
import PropLogicEquivalences.ImplicationElimination;
import Sentences.ComplexSentence;
import Sentences.Sentence;
import Sentences.Utils;

public class CNF {

	/*
	 * first step is to eliminte all "<=>" using the logical equivalence "biconditionnal elimination"
	 * this produce many "=>"
	 * From there, we can eliminate all "=>" using the logical equivalence "implication elimination"
	 * this produce many "||" and "!"
	 * from there, we can use the logical equivalence "double negation" and "De Morgan".
	 * At this point, the only negations left should be on literal
	 * From there, the distributivity allows to form CNF : Conjunctions of clauses (TODO)
	 * 
	 * this allow to use the "resolution" procedure to check for entailment
	 */
	
	public static boolean IsEligibleEquivalence(Sentence original, LogicalEquivalence equi)
	{
		if ( original == null) return false;
		if ( !equi.IsEligible(original))
		{
			if ( Utils.IsComplexSentence(original))
			{
				Sentence left = ((ComplexSentence)original).GetLeftSentence();
				Sentence right = ((ComplexSentence)original).GetRightSentence();
				return CNF.IsEligibleEquivalence(left, equi) || CNF.IsEligibleEquivalence(right, equi);
			}
			return false;
		} else return true;
	}
	public static Sentence CheckAndApplyEquivalence(Sentence original, LogicalEquivalence equi)
	{
		if ( CNF.IsEligibleEquivalence(original, equi))
			return CNF.ApplyEquivalence(original, equi);
		else
			return original;
	}
	public static Sentence ApplyEquivalence(Sentence original, LogicalEquivalence equi)
	{
		if ( original == null) return null;
		Sentence result = original;
		Sentence temp_result = equi.GetEquivalence(result);
		if ( temp_result != null)
			result = temp_result;
		if ( Utils.IsComplexSentence(result))
		{
			ComplexSentence cs = (ComplexSentence)result;
			Sentence left = cs.GetLeftSentence();
			Sentence right = cs.GetRightSentence();
			result = new ComplexSentence(CNF.ApplyEquivalence(left, equi),
					cs.GetConnective(), CNF.ApplyEquivalence(right, equi));
		}
		return result;
	}
	
	public static Sentence ToCNF(Sentence original)
	{
		original = CNF.CheckAndApplyEquivalence(original,new BiconditionnalElimination());
		original = CNF.CheckAndApplyEquivalence(original,new ImplicationElimination());
		original = CNF.CheckAndApplyEquivalence(original, new DoubleNegation());
		original = CNF.CheckAndApplyEquivalence(original, new DeMorganANDtoOR());
		original = CNF.CheckAndApplyEquivalence(original, new DoubleNegation());
		original = CNF.CheckAndApplyEquivalence(original, new DeMorganORtoAND());
		original = CNF.CheckAndApplyEquivalence(original, new DoubleNegation());
		original = CNF.CheckAndApplyEquivalence(original, new Factoring());
			
		while ( CNF.IsEligibleEquivalence(original, new DistributivityORtoAND()))
		{
			original = CNF.ApplyEquivalence(original, new DistributivityORtoAND());
			if (!CNF.IsEligibleEquivalence(original, new DistributivityORtoAND()))
			{
				original = CNF.ApplyEquivalence(original, new CommutativityOfOr());
			}
			original = CNF.CheckAndApplyEquivalence(original, new Factoring());
				
		}
		return original;
	}
}
