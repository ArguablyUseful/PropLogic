package Sentences;

public class AtomicSentence extends Sentence 
{
	private String symbol;
	public AtomicSentence(String symbol) 
	{
		super(SentenceType.ATOMIC);
		this.symbol = symbol;
	}
	public String GetSymbol() { return this.symbol; }
	
}
