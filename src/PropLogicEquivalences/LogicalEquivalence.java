package PropLogicEquivalences;

import Sentences.Sentence;

public abstract class LogicalEquivalence 
{
	/*
	 * Sentence s' equi GetEquivalence(s)
	 * Sentence s equi GetInvertedEquivalence(s')
	 * where "equi" means "logically equivalent"
	 * note that the methods return new object built from the sentence passed as argument
	 * the parameters for those new objects are shared with the sentence passed as argument
	 * I.E. (a || b) equi (b || a)
	 * will return a new object s' when the GetEquivalence(s) is called
	 * but the a object and b object within s' are shared with s
	 * 
	 * In some logical equivalence using "InverseEquivalence" will produce double negations.
	 * 
	 */
	String name;
	String description;
	public LogicalEquivalence(String name, String description)
	{
		this.name = name;
		this.description = description;
	}
	public String GetEquivalenceName() { return this.name; }
	public String GetEquivalenceDescription() { return this.description; }
	public abstract boolean IsEligible(Sentence s); //wheter or not s is eligible for the equivalence
	public abstract boolean IsInverseEligible(Sentence s); //wheter or not s is eligible for the inverted equivalence
	public abstract Sentence GetEquivalence(Sentence s); //null if not eligible, Sentence otherwise
	public abstract Sentence GetInverseEquivalence(Sentence s); // null if not eligible, Sentence otherwise
	/*
	 * the list is
	 * 
	 * Factoring For any connective that is not "not", A connective A is equivalent to A.
	 * 
	 * Double negation !(!A) equi A
	 * De Morgan (AND) !(A && B) equi (!A || !B)
	 * De Morgan (OR) !(A || B) equi (!A && !B)
	 * Contraposition (A => B) equi (!B => !A)
	 * 
	 * Associativity of OR (A || (B || C)) equi ( (A || B) || C)
	 * Associativity of AND (A && (B && C)) equi ( (A && B) && C)
	 * 
	 * Commutativity of OR (A || B) equi (B || A)
	 * Commutativity of AND (A && B) equi (B && A)
	 * 
	 * Distributivity of AND over OR A && (B || C) equi (A && B) || (A && C)
	 * Distributivity of OR over AND A || (B && C) equi (A || B) && (A || C)
	 * 
	 * Implication Elimination A => B equi !A || B
	 * Biconditionnal Elimination A <=> B equi (A => B) && (B => A)
	 * 
	 */
}
