import java.util.HashSet;
import java.util.List;

import Format.CNF;
import Format.ShuntingYard;
import Format.Tokenizer;
import Format.Tokens;
import PropLogic.KB;
import PropLogic.PropositionLogicTruth;
import Sentences.Clause;
import Sentences.Sentence;

public class main_entry_point {
	public static void testing_2() throws Exception
	{
		//testing resolution over a simple example
		String str_sentence = "B11 <=> (P12 || P21)";
		KB kb = new KB();
		kb.Tell(str_sentence);
		Sentence a = Sentence.GetSentenceFromString("!B11");
		boolean result = PropositionLogicTruth.PL_Resolution(kb, a);
		System.out.println("result = " + result + "should be true");
	}
	
	public static void testing_1() throws Exception
	{
		String sentenceStringForm = "A=>B || C && !(D <=> E  )";
		List<Tokens> sentenceTokenized = Tokenizer.tokenize(sentenceStringForm);
		
		//printing it
		for(Tokens individualToken : sentenceTokenized)
			System.out.println("+" + individualToken.toString());
		
		List<Tokens> sortedSentenceTokenized = ShuntingYard.PostfixTokens(sentenceTokenized);
		
		//printing it
		for(Tokens individualToken : sortedSentenceTokenized)
			System.out.println("|" + individualToken.toString());
		
		Sentence sentenceASTForm = ShuntingYard.ASTFromSortedTokens(sortedSentenceTokenized, true);
		System.out.println(sentenceASTForm.toString());
		
		Sentence sentenceASTCNFForm = CNF.ToCNF(sentenceASTForm);
		System.out.println(sentenceASTCNFForm.toString());
		
		HashSet<Clause> sentenceClausesForm = Clause.CNFClauses(sentenceASTCNFForm);
		
		int clauseCount = sentenceClausesForm.size();
		for(Clause individualClause : sentenceClausesForm)
		{
			clauseCount--;
			System.out.print(individualClause.toString());
			if ( clauseCount > 0)
				System.out.print(" AND ");
		}
		System.out.println("\n");
		
		HashSet<Clause> cleanedClauses = new HashSet<Clause>();
		for(Clause c : sentenceClausesForm)
		{
			cleanedClauses.add(c.removeComplementaryLiterals());
		}
		
		clauseCount = cleanedClauses.size();
		for(Clause individualClause : cleanedClauses)
		{
			clauseCount--;
			System.out.print(individualClause.toString());
			if ( clauseCount > 0)
				System.out.print(" AND ");
		}
		System.out.println("\n");
	}
	public static void main(String []args) throws Exception
	{
		//testing_1();
		testing_2();
	}
}
