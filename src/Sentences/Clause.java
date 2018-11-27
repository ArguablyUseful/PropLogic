package Sentences;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import Format.CNF;

/** 
 * @author Corentin
 * This is a helper class.
 * It simply contains a HashSet of sentences supposed to be literals.
 * It's purpose is to make the PL_resolution algorithm easier.
 * It is important to note that the value of the hashcode is computed at creation.
 * The user should never modify the hashSet content.
 * 
 * The set guarantee that there is no redundant literals (as in the clause "(A OR A)")
 * 
 * Method countComplementaryLiterals count the quantity of PAIRS of complementary literals inside a Clause object
 * Method removeComplementaryLiterals creates a new clause where all the complementary literals of a clause are removed.
 * (note : a clause with complementary literals is alway true because (A OR !A OR C OR D) is equivalent to (TRUE OR ...) which is equivalent to TRUE.
 */
public class Clause {
	public HashSet<Sentence> literals; //this object should NEVER be modified outside of constructor.
	final int hashcode;

	/**
	 * The constructor takes a list of Sentence that must be literals (atomic sentence or complex sentence with connective "not" and left sentence is an atomic sentence.
	 * @param literals the list of literals that makes this clause
	 * @throws Exception if the provided list of sentence contains non literals sentence.
	 * note : if you provide redundants literals in the list, only one of them will be taken.
	 */
	public Clause(List<Sentence> literals) throws Exception
	{
		this.literals = new HashSet<Sentence>();
				
		for (Sentence s : literals)
		{
			if ( !Utils.IsLiteral(s))
				throw new Exception("only list of literals are valid for a clause");
			/*if ( literals.contains(s))
				System.out.println("Clause.java : constructor() => removed one duplicate");*/
			this.literals.add(s);
		}
		Object[] sortedList = (Object[]) this.literals.toArray();
		Arrays.sort(sortedList);
		
		final int prime = 31;
		int result = 1;
		for(Object s: sortedList)
			result = prime * result + ((Sentence)s).hashCode();
		this.hashcode = result;
	}
	@Override
	public int hashCode() {
		return hashcode;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Clause other = (Clause) obj;
		if ( this.literals.size() != other.literals.size())
			return false;
		for(Sentence s : other.literals)
			if (!this.literals.contains(s))
				return false;
				
		return true;
	}

	/**
	 * Count the quantity of PAIRS of complementary in the clause
	 * @param A the clause
	 * @return the quantity of pairs of complementary literals. Complementary literals are literals that together are always true : A OR !A is always true.
	 * note : this method is used by the resolution rule in resolution algorithm. 
	 * note 2 : if a clause contains two complementary literals, the clause is always true.
	 */
	public static int countComplementaryLiterals(Clause A)
	{
		int count = 0;
		for(Sentence s : A.literals)
		{
			Sentence complementary = Utils.GetComplementaryLiteral(s);
			if ( A.literals.contains(complementary))
			{
				count++;
			}
		}
		return count/2; // counting the pairs of complementary.
	}
	
	//create a new clause from the literals of this clause
	//remove all complementary literals 
	/**
	 * This method returns a new clause where all complementary were discarded.
	 * This is used by the resolution algorithm.
	 * @return a clause without any complementary literals
	 * @throws Exception can be caused if this clause contains non literals.
	 */
	public Clause removeComplementaryLiterals() throws Exception
	{
		LinkedList<Sentence> lst = new LinkedList<Sentence>();
		for(Sentence A : this.literals)
		{
			Sentence complementary = Utils.GetComplementaryLiteral(A);
			
			
			if ( !this.literals.contains(complementary))
			{
				lst.add(A); //A do not have any complementary in the literals set.
				
			} else
			{
				
			}
			
		}
		return new Clause(lst);
	}
	
	/**
	 * This method return a new clause that contains all the literals from this clause and all the literals from the clause given in argument
	 * this is used by the resolution algorithm.
	 * @param with the clause to merge this clause with
	 * @return a new clause containing all literals from "this" clause and "with" clause
	 * @throws Exception if any sentence in this clause or "with" clause is not a literal.
	 * note : if two literals are shared by this clause and "with" clause, only one is kept. 
	 */
	public Clause mergeClauses(Clause with) throws Exception
	{
		LinkedList<Sentence> literals = new LinkedList<Sentence>();
		for(Sentence s : this.literals)
			literals.add(s);
		for(Sentence s : with.literals)
			literals.add(s);
		return new Clause(literals);
	}
	
	/**
	 * Create a set of clause from a CNF sentence in AST format.
	 * @param original the CNF sentence in AST format 
	 * @return a set of clause
	 * @throws Exception if the sentence is not in CNF form.
	 */
	public static HashSet<Clause> CNFClauses(Sentence original) throws Exception
	{
		HashSet<Clause> result = new HashSet<Clause>();
		if ( CNF.IsCNF(original))
		{
			Clause.CNFSentenceToClauses(original, result);
		} else throw new Exception("Sentence provided as argument is not CNF.");
		return result;
	}
	
	/**
	 * this is a private methods used by the CNFClauses() method.
	 * the public version is used to hide the set passed in argument used by the recursion
	 * @param original the sentence
	 * @param clauses a set of clauses
	 * @throws Exception 
	 */
	private static void CNFSentenceToClauses(Sentence original, HashSet<Clause> clauses) throws Exception
	{
		//we are assuming that "original" is a valid CNF.
		if ( Clause.IsClause(original))
		{
			List<Sentence> literals = new LinkedList<Sentence>();
			Utils.ExtractLiterals(original, literals);
			Clause newClause = new Clause(literals);
			clauses.add(newClause);
		} else
		{
			if ( Utils.CheckForAND(original))
			{
				ComplexSentence cs = (ComplexSentence) original;
				Clause.CNFSentenceToClauses(cs.GetLeftSentence(), clauses);
				Clause.CNFSentenceToClauses(cs.GetRightSentence(), clauses);
			}
		}
	}
	
	/**
	 * purpose is to quickly know if a sentence is a clause
	 * @param original is the sentence. 
	 * @return true if the sentence is a clause (=a disjunction of literals)
	 */
	public static boolean IsClause(Sentence original)
	{
		//a clause only contains "OR" over literals or complex sentence with OR.
		if ( Utils.IsLiteral(original ))
			return true;
		else
		{
			ComplexSentence cs = (ComplexSentence)original;
			if ( Utils.CheckForOR(original))
			{
				return Clause.IsClause(cs.GetLeftSentence()) && Clause.IsClause(cs.GetRightSentence());
			} else 
				return false;
		}
	}
	/**
	 * 
	 * @return the quantity of literals inside the set of literals composing this clause
	 */
	public int getCountLiterals()
	{
		return this.literals.size();
	}
	public String toString()
	{
		String res = "(";
		int literalCount = literals.size();
		for(Sentence literal : literals)
		{
			literalCount--;
			res += literal.toString();
			if ( literalCount > 0)
				res += " || ";
		}
		res += ")";
		return res;
	}
	
}
