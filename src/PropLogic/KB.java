package PropLogic;
import java.util.List;

import Format.ShuntingYard;
import Format.Tokenizer;
import Format.Tokens;
import Sentences.ComplexSentence;
import Sentences.Sentence;

public class KB {
	
	Sentence kb = null;
	
	public KB()
	{
		
	}
	public Sentence GetKBContent() { return this.kb; }
	public void Tell(Sentence toTell)
	{
		if ( toTell != null )
		{
			if ( this.kb == null)
			{
				this.kb = toTell;
			}
			else
			{
				ComplexSentence cs = new ComplexSentence(kb, ComplexSentence.ConnectiveTypes.AND, toTell);
				this.kb = cs;
			}
		}
	}
	public void Tell(String toTell)
	{
		this.Tell(Sentence.GetSentenceFromString(toTell));
	}
	
}
