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
import Sentences.Clause;
import Sentences.ComplexSentence;
import Sentences.Sentence;
import Sentences.Utils;

/** 
 * @author Corentin
 * CNF class is a collection of static methods used to convert Propositional Logic sentences into their CNF equivalent.
 * CNF stands for Conjunctive Normal Form.
 * CNF sentences are a conjunction (= "ANDs") of clauses (= "ORs").
 * clauses are disjunction (= "ORs") of literals (atomic sentences or negated atomic sentences).
 * Any PL sentence can be converted into CNF format.
 * CNF format is required for the resolution algorithm.
 * TODO : create a data structure that avoids creating new LogicalEquivalence objects each time one is needed.
 */
public class CNF {	
	
	/**
	 * This method checks wheter or not a given LogicalEquivalence can apply to the PL sentence provided.
	 * a LogicalEquivalence posess a method called "isElligible" that determine if it can be applied to a sentence. 
	 * @param original The PL sentence (any form)
	 * @param equi the logical equivalence. 
	 * @return false if no part of this sentence would return true when the logical equivalence provided calls its method "isEligible" on it. 
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
	/**
	 * This method first check if the logical equivalence can be applied to the provided sentence.
	 * It the logicalEquivalence object's "isEligible" returns true with the provided sentence then
	 * it returns the results of the logicalEquivalence object's method "ApplyEquivalence" using the PL sentence.
	 *  
	 * @param original The sentence used for the logicalEquivalence objet
	 * @param equi the equivalence object
	 * @return the result of applying the logical equivalence to the provided sentence or the original sentence if the sentence is not eligible
	 */
	public static Sentence CheckAndApplyEquivalence(Sentence original, LogicalEquivalence equi)
	{
		if ( CNF.IsEligibleEquivalence(original, equi))
			return CNF.ApplyEquivalence(original, equi);
		else
			return original;
	}
	/**
	 * We apply the logical equivalence everywhere is it possible in the sentence provided and return the results
	 * @param original the sentence we start with
	 * @param equi the equivalence we want to apply to every parts
	 * @return the modified sentence, that can be the same as the one provided if no part of the sentence was eligible for the equivalence
	 * note : providing a null "original" returns null.
	 */
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

	/*
	 * IsCNF return true if the sentence "original" is a CNF form.
	 */
	/**
	 * This method checks wheter or not a PL Sentence is in CNF form
	 * @param original the PL sentence to check
	 * @return true if the sentence provided is in CNF form. False otherwise.
	 */
	public static boolean IsCNF(Sentence original)
	{
		if ( Utils.IsLiteral(original))
			return true;
		else //we have a complex sentence because there is no non-literal that is not a complex sentence
		{
			ComplexSentence cs = (ComplexSentence)original; //cannot be atomic due to first line of this method
			if ( Utils.CheckForAND(original) ) //keep being in a conjunction
			{
				return CNF.IsCNF(cs.GetLeftSentence()) && CNF.IsCNF(cs.GetRightSentence());
			} else if ( Clause.IsClause(original))
			{
				return true;
			} else //either "imply", "equivalent" or a "not" that isn't over a literal which are proofs of this sentence not being CNF
			{
				return false;
			}
		}
	}
	
	/**
	 * This method makes a systemtic uses of LogicalEquivalences to transform any PL Sentence into a Sentence in CNF form.
	 * It should never fail to do so.
	 * The procedure is as follow :
	 * 1) Eliminate all occurence of Equivalence and Implication (<=> and =>) using biconditional elimination equivalence and implication elimination
	 * 2) Move all "not" operators inside parenthesis using De Morgan equivalences so that any "not" operator remaining only apply to atomic sentence
	 * 2.b) Remove any "not" operators where possible with the double negation elimination ( !(!A)) becomes A)
	 * 3) Use distributivity (and commutativity) of OR over AND so that there is no more "AND" inside clauses.
	 * The result is a CNF form of the original Sentence. 
	 * 
	 * note : it is important to know that the equivalences used sometimes create new objects representing parts of sentence
	 * and sometimes re-use already existing one.
	 * Therefore, reference to the original sentence should be discarded, or at the very least, never be modified.
	 * 
	 * @param original the sentence we wish to turn into a CNF form.
	 * @return a Sentence in CNF form or null if the original sentence was null.
	 */
	public static Sentence ToCNF(Sentence original)
	{
		if  ( original == null)
			return null;
		//turns a <=> b into a => b && b => a
		original = CNF.CheckAndApplyEquivalence(original,new BiconditionnalElimination());
		//turns a => b into !a || b
		original = CNF.CheckAndApplyEquivalence(original,new ImplicationElimination());
		
		//moving negation so that is only occurs in literals
		while ( CNF.IsEligibleEquivalence(original, new DoubleNegation())
				||
				CNF.IsEligibleEquivalence(original, new DeMorganANDtoOR())
				||
				CNF.IsEligibleEquivalence(original, new DeMorganORtoAND())
				)
		{
			original = CNF.CheckAndApplyEquivalence(original, new DoubleNegation());
			original = CNF.CheckAndApplyEquivalence(original, new DeMorganANDtoOR());
			original = CNF.CheckAndApplyEquivalence(original, new DeMorganORtoAND());			
		}
		
		
		//clearing up redundant parts of the sentences ( i.e (A OR A) is (A) ..etc)
		original = CNF.CheckAndApplyEquivalence(original, new Factoring());
			
		//distributivity of OR over AND. The idea is to prevent "AND" from occuring inside "ORs"
		//example : A OR (B AND C) <=> (A OR B) AND (A OR C). no more "AND" inside "OR". 
		if ( !CNF.IsEligibleEquivalence(original, new DistributivityORtoAND())) 
			original = CNF.ApplyEquivalence(original, new CommutativityOfOr());
		while ( CNF.IsEligibleEquivalence(original, new DistributivityORtoAND()))
		{
			original = CNF.ApplyEquivalence(original, new DistributivityORtoAND());
			if (!CNF.IsEligibleEquivalence(original, new DistributivityORtoAND()))
			{
				original = CNF.ApplyEquivalence(original, new CommutativityOfOr());
			}
			original = CNF.CheckAndApplyEquivalence(original, new Factoring());
				
		}
				
		/* <= comment this line with "//" to enable the message
		//should be CNF
		boolean result = CNF.IsCNF(original);
		System.out.println("Is cnf should be true : " + result);
		//*/
		
		return original;
	}
}
