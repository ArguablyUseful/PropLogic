package Sentences;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import Format.CNF;


/*
 * A clause is a disjunction of literals
 * I.E. (A OR B OR C) AND (C OR A OR ...) AND ...
 * The Literals are stored in a HashSet to prevent duplicate (A OR A) is always strictly equivalent to (A) )
 * The hashcode of this clause is computed at construction time. Modification of the content of the HAshSet of literals violate the structure of data. 
 * 
 * 
 * There is a method to turn any CNF sentence into an object that is a list of clauses.
 * this is used by the resolution algorithm.
 */
public class Clause {
	public HashSet<Sentence> literals; //this object should NEVER be modified outside of constructor.
	final int hashcode;
	//constructor
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
	public static List<Sentence> removeDuplicates(LinkedList<Sentence> lst)
	{
		List<Sentence> result = new LinkedList<Sentence>();
		HashSet<Sentence> removeDups = new HashSet<Sentence>();
		for (Sentence s : lst)
		{
			if ( removeDups.contains(s))
				continue;
			removeDups.add(s);
			result.add(s);
		}
		return result;
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

	//create a new clause from the literals of this clause
	//remove all complementary literals 
	//I.E. (A OR B OR !A OR C) becomes (B OR C)
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
	
	//create a new clause that adds
	//+all the literal from "this" clause
	//+all the literals from "with" clause
	//remove all duplicate.
	public Clause mergeClauses(Clause with) throws Exception
	{
		LinkedList<Sentence> literals = new LinkedList<Sentence>();
		for(Sentence s : this.literals)
			literals.add(s);
		for(Sentence s : with.literals)
			literals.add(s);
		return new Clause(literals);
	}
	
	//the method that turns any CNF sentence into a set of Clause objects
	public static HashSet<Clause> CNFClauses(Sentence original) throws Exception
	{
		HashSet<Clause> result = new HashSet<Clause>();
		if ( CNF.IsCNF(original))
		{
			Clause.CNFSentenceToClauses(original, result);
		} else throw new Exception("Sentence provided as argument is not CNF.");
		return result;
	}
	//this method does the CNF to clause conversion work
	//original is the CNF sentence in AST format. 
	//clauses is the hashset of clauses in clause format.
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
	//if a sentence is a clause, returns true. False otherwise.
	//this method is used by other classes
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
