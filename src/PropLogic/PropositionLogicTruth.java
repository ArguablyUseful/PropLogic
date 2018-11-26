package PropLogic;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import Format.CNF;
import Sentences.AtomicSentence;
import Sentences.Clause;
import Sentences.ComplexSentence;
import Sentences.Sentence;
import Sentences.Utils;

public class PropositionLogicTruth {
	public static Boolean EvaluateSentence(Sentence s, Model m)
	{
		Boolean evaluation = null;
		if ( s != null)
		{
			if ( Utils.IsComplexSentence(s))
			{
				ComplexSentence cs = (ComplexSentence)s;
				Boolean leftEval = EvaluateSentence(cs.GetLeftSentence(), m);
				Boolean rightEval = EvaluateSentence(cs.GetRightSentence(), m);
				switch(cs.GetConnective())
				{
					case NONE:
						evaluation = leftEval;
						break;
					case NOT:
						evaluation = (leftEval != null)?!leftEval:null;
						break;
					case AND: 
						// true if leftEval && rightEval, false if not
						//false if either leftEval or right Eval is false, even if the other is unknown
						//null if either leftEval or right Eval is true but the other is unknown or both are unknown
						if ( leftEval != null && rightEval != null )
							evaluation = leftEval && rightEval;
						else if ( leftEval != null && rightEval == null)
							evaluation = (leftEval==false)?false:null;
						else if ( leftEval == null && rightEval != null)
							evaluation = (rightEval==false)?false:null;
						else
							evaluation = null;
						break;
					case OR:
						//true if leftEval || rightEval, false is not
						//also true if one is null but the other is true
						//null if either both are null or one is false and the other is null
						if ( leftEval != null && rightEval != null)
							evaluation = leftEval || rightEval;
						else if ( leftEval != null && rightEval == null)
							evaluation = (leftEval==true)?true:null;
						else if ( leftEval == null && rightEval != null)
							evaluation = (rightEval==true)?true:null;
						else
							evaluation = null;
						break;
					case IMPLY: // (P => Q) true except when P is true and Q is false
						//hence P�=> Q has the truth of !(P && !Q)
						if ( leftEval != null && rightEval != null )
							evaluation = !(leftEval && !rightEval);
						else if ( leftEval != null && rightEval == null)
							evaluation = (leftEval==false)?true:null;
						else if ( leftEval == null && rightEval != null)
							evaluation = (rightEval==true)?true:null;
						else
							evaluation = null;
						break;
					case EQUI:
						if ( leftEval == null || rightEval == null)
							evaluation = null;
						else
							evaluation = leftEval == rightEval;
						break;
				}
			} else
			{
				AtomicSentence as = (AtomicSentence)s;
				evaluation = m.GetSymbolAssignatedValue(as.GetSymbol());
			}	
		}
		return evaluation;
	}
	
	/*
	 * should be 2^symQty in time and symQty in space
	 */
	public static boolean TT_Entails_ModelChecking(KB kb, Sentence a)
	{
		LinkedList<String> symbols = new LinkedList<String>();
		
		//using a treeset guarantee to have each symbol only once
		TreeSet<String> symSet = new TreeSet<String>();
		symSet = kb.GetKBContent().ExtractSymbols();
		symSet.addAll(a.ExtractSymbols());
		
		for(String sym : symSet)
			symbols.add(sym);
		
		return TT_Check_All(kb, a, symbols, new Model());
	}
	
	
	public static boolean TT_Check_All(KB kb, Sentence a, LinkedList<String> symbols, Model m)
	{
		if ( symbols.isEmpty())
		{
			/*
			 * despite the existence of case where EvaluateSentence returns null, this should never occurs
			 * here because the function is only called once every symbol has been assigned.
			 */
			if ( PropositionLogicTruth.EvaluateSentence(kb.GetKBContent(),  m))
			{
				return PropositionLogicTruth.EvaluateSentence(a, m);
			} else
				return true; // if KB is false, this doesn't draw any conclusion about entailment
		} else
		{
			String nextSym = symbols.pop(); // /!\ symbols is now changed
			m.AssignSymbolValue(nextSym, true); // /!\ model is now changed
			boolean nextSymBeingTrue = TT_Check_All(kb, a, symbols, m);
			
			m.AssignSymbolValue(nextSym, false); 
			boolean nextSymBeingFalse = TT_Check_All(kb, a, symbols, m);
			
			//we reset linkedlist and model
			symbols.addFirst(nextSym);
			m.RemoveSymbolAssignment(nextSym);
			
			return nextSymBeingTrue && nextSymBeingFalse;
		}
	}
	
	
	public static boolean PL_Resolution(KB kb, Sentence a)
	{
		//we want to prove that there is no way that kb AND not A can be true (which means kb AND a is true) 
		//we'll try to find an empty clause that means the sentence is false. 
		
		//1 we create sentence KB && !a
		//2we turn it into CNF
		//3we create list of clauses from that CNF sentence
		//we apply resolution rules	 
		ComplexSentence notA = new ComplexSentence(a, ComplexSentence.ConnectiveTypes.NOT, null);
		Sentence toWorkOn = new ComplexSentence(kb.GetKBContent(), ComplexSentence.ConnectiveTypes.AND,notA);
		Sentence toWorkOnCNF = CNF.ToCNF(toWorkOn);
		try {
			HashSet<Clause> clauses = Clause.CNFClauses(toWorkOnCNF); 
			//from here we have a set of clauses saying ( KB && !a ).
			while(true)
			{
				HashSet<Clause> resolvents = new HashSet<Clause>();
				for( Clause clauseA : clauses )
				{
					for(Clause clauseB : clauses)
					{
						if ( clauseA.equals(clauseB))
							continue;
						Clause clauseC = clauseA.mergeClauses(clauseB);
						clauseC = clauseC.removeComplementaryLiterals();
						if ( clauseC.getCountLiterals() == 0)
							return true; // we have the empty clause.
						if ( !resolvents.contains(clauseC) && !clauses.contains(clauseC))
							resolvents.add(clauseC);
						//resolve clauseA with clauseB to create clauseC 
						//if clauseC is empty clause => return true
						//adds clauseC to resolvents
					}
				}
				//if resolvents is empty or all clauses in resolvents are already in "clauses", return false
				if ( resolvents.size() == 0)
					return false;
				//otherwise adds resolvents to clauses
				clauses.addAll(resolvents);
			}
		} catch (Exception e) {
			System.out.println("Clause exception-toWorkOnCNF is not CNF ?! : " + e.getMessage());
			e.printStackTrace();
		}
		
		return false;
	}
}
