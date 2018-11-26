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
- the code to turn txt propositional logic sentence into sequences of tokens (Tokenizer)
- the code to turn a sequence of tokens into AST (shuntingyard)
- the code to turn a PL sentence into a CNF sentence (Conjunctive normal form)

directory "PropLogic" contains
- the KB class (conjunction of propositional logic sentence in AST format)
- the Model class (the truth value for symbols)
- the PLTruth class that determine the truth value of sentence based on a model
- and a model check algorithm
- in PLTruth class : resolution algorithm based on a CNF formatted Sentence (using clauses)

directory "ProplogicEquivalences" contains
- the abstract class "LogicalEquivalence" used as an interface for every LogicalEquivalence
- logical equivalence instances (associativity, de morgan, commutativity, etc including Factoring)

directory "Sentences" contains
- the classes used to represent PL sentence in AST format
- clause class (see "proplogic")
- turn a CNF sentence into a set of clauses to allow Resolution algorithm
- a util class for various common sentence operations

