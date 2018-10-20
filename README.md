# PropLogic
propositional logic software

file "main" (in the root directory) shows an example of use

directory "Format" contains 
- the code to turn txt propositional logic sentence into sequences of tokens (Tokenizer)
- the code to turn a sequence of tokens into AST (shuntingyard)
- the code to turn a PL sentence into a CNF sentence (Conjunctive normal form)
- (TODO) turn a CNF sentence into a set of clauses to allow Resolution algorithm

directory "PropLogic" contains
- the KB class (conjunction of propositional logic sentece in AST format)
- the Model class (the truth value for symbols)
- the PLTruth class that determine the truth value of sentence based on a model
- and a model check algorithm
- TODO in PLTruth class : resolution algorithm based on a CNF formatted Sentence

directory "ProplogicEquivalences" contains
- the abstract class "LogicalEquivalence" used as an interface for every LogicalEquivalence
- logical equivalence instances (associativity, de morgan, commutativity, etc including Factoring)

directory "Sentences" contains
- the classes used to represent PL sentence in AST format
- TODO clause class (see "format" and "proplogic" TODO)
- a util class for various common sentence operations

