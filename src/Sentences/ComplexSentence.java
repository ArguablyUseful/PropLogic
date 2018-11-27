package Sentences;

/**
 * 
 * @author Corentin
 * a complex sentence is made out of sentences that can be either atomic or complex.
 * a complex sentence always has a connective.
 * there is only one unary connective (NOT), the others are binary.
 * NONE is an invalid connective.
 * see Sentence
 */
public class ComplexSentence extends Sentence
{
	public enum ConnectiveTypes{ NONE, NOT, AND, OR, IMPLY, EQUI};
	
	private ConnectiveTypes connective;
	private Sentence left;
	private Sentence right;
	public ComplexSentence(Sentence A, ConnectiveTypes C, Sentence B) 
	{
		super(Sentence.SentenceType.COMPLEX);
		this.connective = C;
		this.left = A;
		this.right = B;
	}
	public Sentence GetLeftSentence() { return this.left; }
	public Sentence GetRightSentence() { return this.right; }
	public ConnectiveTypes GetConnective() { return this.connective; }
	

}
