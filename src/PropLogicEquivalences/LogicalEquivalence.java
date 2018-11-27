package PropLogicEquivalences;

import Sentences.Sentence;
/**
 * 
 * @author Corentin
 * This class represent the "blueprint" for all logical equivalence.
 * The idea is that we needs some logical identities.
 * It contains methods to check IF the logical equivalence apply to a given sentence
 * and methods to get the identity out of a given sentence.
 * There are also cases where we want to "invert" logical equivalence.
 * The list of equivalence is provided here
 *
 * Factoring For any connective that is not "not", A connective A is equivalent to A.
 * Double negation !(!A) equi A
 * De Morgan (AND) !(A && B) equi (!A || !B)
 * De Morgan (OR) !(A || B) equi (!A && !B)
 * Contraposition (A => B) equi (!B => !A)
 * Associativity of OR (A || (B || C)) equi ( (A || B) || C)
 * Associativity of AND (A && (B && C)) equi ( (A && B) && C)
 * Commutativity of OR (A || B) equi (B || A)
 * Commutativity of AND (A && B) equi (B && A)
 * Distributivity of AND over OR A && (B || C) equi (A && B) || (A && C)
 * Distributivity of OR over AND A || (B && C) equi (A || B) && (A || C)
 * Implication Elimination A => B equi !A || B
 * Biconditionnal Elimination A <=> B equi (A => B) && (B => A)
 */
public abstract class LogicalEquivalence 
{
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
	
}
