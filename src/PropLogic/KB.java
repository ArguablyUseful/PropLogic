package PropLogic;
import Sentences.ComplexSentence;
import Sentences.Sentence;

/**
 * 
 * @author Corentin
 * This class is the knowledge base (KB) containing one single PL sentence.
 * new Sentence can be added using the "Tell" method. (KB becomes KB && new sentence)
 */
public class KB {
	
	Sentence kb = null;
	
	public Sentence GetKBContent() { return this.kb; }
	/**
	 * KB becomes KB && toTell
	 * @param toTell the sentence to add to the KB
	 */
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
	/**
	 * KB becomes KB && toTell
	 * @param toTell the sentence to add to the KB
	 */
	public void Tell(String toTell)
	{
		this.Tell(Sentence.GetSentenceFromString(toTell));
	}
	
}
