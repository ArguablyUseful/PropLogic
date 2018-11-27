# PropLogic
propositional logic software based on "artificial intelligence : a modern approach"

file "main entry point" (in the root directory) shows examples of use

It is a propositional logic engine that allow the building of a knowledge base made of logical sentences. (note: "logical" is always used in the context of propositional logic here.)

1) Create a KB object and feed it with logical sentences.
2) Check for entailments using the resolution algorithm (I.E. can we infer some logical sentence from KB)
3) Create Model of truth; that is assignemnt of truth to symbols used in the logic sentences.
4) Check wheter or not some symbol can be deduced as being true or false based on KB and a Model
5) The engine include conversion methods from string to ast sentences, from ast sentences to CNF sentences and from CNF to "clauses".  Each sentence also has a toString() implemented to make it human readable.

no debug was done yet
code & comments not cleaned yet

directory "Format" contains 
- CNF class. The purpose it to transform PL Sentence into their CNF equivalent.
- Tokens class. The purpose is to have differents tokens for all parts of a PL Sentence in text string format
- Tokenizer class. The purpose is to turn a text stream representing a PL Sentence into a stream of Tokens representing the same sentence.
- ShuntingYard class. The purpose is to transform a sequence of infix Tokens into posfix sequence. Then, turn that postfix sequence into an AST representation using Sentence class (see below in "Sentences").

directory "PropLogic" contains
- KB class. Purpose is to hold PL sentence in a handy way. It also allow to adds new sentences to what is already in the KB.
- PLTruth class. Purpose is to be able to check for the truth of a PL Sentence given a Model of truth and check for entailments using two methods : model checking and Resolution.
- the Model class (the truth value for symbols)

directory "ProplogicEquivalences" contains
- LogicalEquivalence class. All other classes in this directory directly implements this abstract class. The purpose is to be able to check wheter or not a logical equivalence could apply to a sentence, and what its result is.
- Logical Equivalence implementations. Commutativity for OR and AND, associativity for OR and AND, double negation elimination, contraposition, implication elimination, biconditional elimination, De morgan rule for OR and AND, distributivity of OR over AND and of AND over OR.

directory "Sentences" contains
- the classes used to represent PL sentence in AST format
- clause class (see "proplogic")
- turn a CNF sentence into a set of clauses to allow Resolution algorithm
- a util class for various common sentence operations

