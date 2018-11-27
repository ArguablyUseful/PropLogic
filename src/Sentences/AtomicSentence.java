package Sentences;

/**
 * 
 * @author Corentin
 * an Atomic sentence is simply a symbol without connective.
 * see Sentence
 */
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
