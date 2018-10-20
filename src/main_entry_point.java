import java.util.List;

import Format.CNF;
import Format.ShuntingYard;
import Format.Tokenizer;
import Format.Tokens;
import Sentences.Sentence;

public class main_entry_point {
	public static void main(String []args)
	{
		String s = "A=>B || C && !(D <=> E  )";
		List<Tokens> ttif = Tokenizer.tokenize(s);
		for(Tokens t : ttif)
			System.out.println("+" + t.toString());
		List<Tokens> ttpf = ShuntingYard.PostfixTokens(ttif);
		for(Tokens t : ttpf)
			System.out.println("|" + t.toString());
		
		Sentence sent = ShuntingYard.ASTFromSortedTokens(ttpf, true);
		System.out.println(sent.toString());
		Sentence cnf = CNF.ToCNF(sent);
		System.out.println(cnf.toString());
	}
}
