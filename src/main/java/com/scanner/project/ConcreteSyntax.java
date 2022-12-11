package com.scanner.project;
// ConcreteSyntax.java

// Implementation of the Recursive Descent Parser algorithm

//  Each method corresponds to a concrete syntax grammar rule, 
// which appears as a comment at the beginning of the method.

// This code DOES NOT implement a parser for KAY. You have to complete
// the code and also make sure it implements a parser for KAY - not something
// else, not more, not less.

public class ConcreteSyntax {

    // READ THE COMPLETE FILE FIRST

    // Instance variables
    public Token token; // current token that is considered from the input stream
    public TokenStream input; // stream of tokens generated in by the lexical analysis

    // Constructor
    public ConcreteSyntax(TokenStream ts) { // Open the source program
        input = ts; // as a TokenStream, and
        token = input.nextToken(); // retrieve its first Token
    }

    // Method that prints a syntax error message
    private String SyntaxError(String tok) {
        String s = "Syntax error - Expecting: " + tok + " But saw: "
                + token.getType() + " = " + token.getValue();
        System.out.println(s);
        return s;
        // System.exit(0);
    }

    // Match a string with the value of a token. If no problem, go to the next
    // token otherwise generate an error message
    private void match(String s) {
        if (token.getValue().equals(s))
            token = input.nextToken();
        else
            throw new RuntimeException(SyntaxError(s));
    }

    // Implementation of the Recursive Descent Parser

    public Program program() {
        // TO DO TO BE COMPLETED
        // Program --> main '{' Declarations Statements '}'
        String[] header = {"main"};
        Program pr = new Program();
        for (int i = 0; i < header.length; i++)
            // bypass " main { "
            match(header[i]);
        match("{");
        pr.body = statements();
        pr.decpart = declarations();
        match("}");
        return pr;
    }

    private Declarations declarations() {
        // TO DO TO BE COMPLETED
        // Declarations --> { Declaration }*
        Declarations ds = new Declarations();
        while (token.getValue().equals("int")
                || token.getValue().equals("bool") || token.getValue().equals("undef")) {
            declaration(ds);
        }
        return ds;
    }

    private void declaration(Declarations ds) {
        // Declaration --> Type Identifiers ;
        Type t = type();
        identifiers(ds, t);
        match(";");
    }

    private Type type() {
        // TO DO TO BE COMPLETED
        // Type --> integer | bool
        Type t = null;
        if (token.getValue().equals("int"))
            t = new Type(token.getValue());
        else if (token.getValue().equals("bool"))
            t = new Type(token.getValue());
        else
            throw new RuntimeException(SyntaxError("int | boolean"));
        token = input.nextToken(); // pass over the type
        return t;
    }

    private void identifiers(Declarations ds, Type t) {
        // Identifiers --> Identifier { , Identifier }*
        Declaration d = new Declaration(); // first declaration
        d.t = t; // its type
        if (token.getType().equals("Identifier")) {
            d.v = new Variable();
            d.v.id = token.getValue(); // its value
            ds.addElement(d);
            token = input.nextToken();
            while (token.getValue().equals(",")) {
                d = new Declaration(); // next declaration
                d.t = t; // its type
                token = input.nextToken();
                if (token.getType().equals("Identifier")) {
                    d.v = new Variable(); // its value
                    d.v.id = token.getValue();
                    ds.addElement(d);
                    token = input.nextToken(); // get "," or ";"
                } else
                    throw new RuntimeException(SyntaxError("Identifier"));
            }
        } else
            throw new RuntimeException(SyntaxError("Identifier"));
    }

    private Statement statement() {
        // Statement --> ; | Block | Assignment | IfStatement | WhileStatement
        Statement s = new Skip();
        if (token.getValue().equals(";")) { 
            token = input.nextToken();
            return s;
        } else if (token.getValue().equals("{")) { 
            token = input.nextToken();
            s = statements();
            match("}");
        } else if (token.getType().equals("Identifier")) { 
            s = assignment();
        } else if (token.getValue().equals("if")) 
            s = ifStatement();
        else if (token.getValue().equals("while")) {
            s = whileStatement();
        } else
            throw new RuntimeException(SyntaxError("Statement"));
        return s;
    }

    private Block statements() {
        // Block --> '{' Statements '}'
        Block bl = new Block();
        while (!token.getValue().equals("}")) {
            bl.blockmembers.addElement(statement());
        }
        return bl;
    }

    private Assignment assignment() {
        // Assignment --> Identifier := Expression ;
        Assignment as = new Assignment();
        if (token.getType().equals("Identifier")) {
            // TO DO TO BE COMPLETED
            as.target = new Variable();
            as.target.id = token.getValue();
            token = input.nextToken();
            match(":=");
            as.source = expression();
            match(";");
        } else
            throw new RuntimeException(SyntaxError("Identifier"));
        return as;
    }

    private Expression expression() {
        // Expression --> Conjunction { || Conjunction }*
        Binary bi;
        Expression ex;
        ex = conjunction();
        while (token.getValue().equals("||")) {
            bi = new Binary();
            bi.term1 = ex;
            bi.op = new Operator(token.getValue());
            token = input.nextToken();
            bi.term2 = conjunction();
            ex = bi;
        }
        return ex;
    }

    private Expression conjunction() {
        // Conjunction --> Relation { && Relation }*
        Binary bi;
        Expression ex;
        ex = relation();
        while (token.getValue().equals("&&")) {
            bi = new Binary();
            // TO DO TO BE COMPLETED
            bi.term1 = ex;
            bi.op = new Operator(token.getValue());
            token = input.nextToken();
            bi.term2 = relation();
            ex = bi;
        }
        return ex;
    }

    private Expression relation() {
        // Relation --> Addition [ < | <= | > | >= | == | <> ] Addition }*
        Binary bi;
        Expression ex;
        ex = addition();
        // TO DO TO BE COMPLETED
        while (token.getValue().equals("<") || token.getValue().equals("<=")
                || token.getValue().equals(">=")
                || token.getValue().equals("==")
                || token.getValue().equals("!=")) {
            bi = new Binary();
            // TO DO TO BE COMPLETED
            bi.term1 = ex;
            bi.op = new Operator(token.getValue());
            token = input.nextToken();
            bi.term2 = addition();
            ex = bi;
        }
        return ex;
    }

    private Expression addition() {
        // Addition --> Term { [ + | - ] Term }*
        Binary bi;
        Expression ex;
        ex = term();
        while (token.getValue().equals("+") || token.getValue().equals("-")) {
            // TO DO TO BE COMPLETED
            bi = new Binary();
            bi.term1 = ex;
            if(token.getValue().equals("+")) {
                Operator op = new Operator("+");
                bi.op = op;
                bi.term2 = term();
                token = input.nextToken();
            }
            else if(token.getValue().equals("-")) {
                Operator op = new Operator("-");
                bi.op = op;
                bi.term2 = term();
                token = input.nextToken();
            }
        }
        return ex;
    }

    private Expression term() {
        // Term --> Negation { [ '*' | / ] Negation }*
        Binary bi;
        Expression ex;
        ex = negation();
        while (token.getValue().equals("*") || token.getValue().equals("/")) {
            bi = new Binary();
            bi.term1 = ex;
            if(token.getValue().equals("*")) {
                Operator op = new Operator("*");
                bi.op = op;
                bi.term2 = negation();
                token = input.nextToken();
            }
            else if(token.getValue().equals("/")) {
                Operator op = new Operator("/");
                bi.op = op;
                bi.term2 = negation();
                token = input.nextToken();
            }
            ex = bi;
        }
        return ex;
    }

    private Expression negation() {
        // Negation --> { ! }opt Factor
        Unary un;
        if (token.getValue().equals("!")) {
            un = new Unary();
            un.op = new Operator(token.getValue());
            token = input.nextToken();
            un.term = factor();
            return un;
        } else
            return factor();
    }

    private Expression factor() {
        // Factor --> Identifier | Literal | ( Expression )
        Expression ex = null;
        if (token.getType().equals("Identifier")) {
            Variable va = new Variable();
            va.id = token.getValue();
            ex = va;
            token = input.nextToken();
        } else if (token.getType().equals("Literal")) {
            Value v = null;
            if (isInteger(token.getValue()))
                v = new Value((new Integer(token.getValue())).intValue());
            else if (token.getValue().equals("true"))
                v = new Value(true);
            else if (token.getValue().equals("false"))
                v = new Value(false);
            else
                throw new RuntimeException(SyntaxError("Literal"));
            ex = v;
            token = input.nextToken();
        } else if (token.getValue().equals("(")) {
            token = input.nextToken();
            ex = expression();
            match(")");
        } else
            throw new RuntimeException(SyntaxError("Identifier | Literal | ("));
        return ex;
    }

    private Conditional ifStatement() {
        // IfStatement --> if ( Expression ) Statement { else Statement }opt
        Conditional co = new Conditional();
        // TO DO TO BE COMPLETED
        if(token.getValue().equals("if")) {
            co.test = expression();
            co.thenbranch = statement();
            token = input.nextToken();
        }
        if(token.getValue().equals("else")) {
            co.elsebranch = statement();
            token = input.nextToken();
        }
        return co;
    }

    private Loop whileStatement() {
        // WhileStatement --> while ( Expression ) Statement
        Loop lo = new Loop();
        // TO DO TO BE COMPLETED
        if(token.getValue().equals("while")) {
            token = input.nextToken();
            while(lo.body == null && lo.test == null) {
                if(token.getValue().equals("(") || token.getValue().equals(")")) {
                    token = input.nextToken();
                }
                else if(token.getType().equals("Expression")){
                    Expression temp = expression();
                    lo.test = temp;
                    token = input.nextToken();
                }
                else if(token.getType().equals("Statement")) {
                    Statement temp = statement();
                    lo.body = temp;
                    token = input.nextToken();
                }
            }
        }
        return lo;
    }

    private boolean isInteger(String s) {
        boolean result = true;
        for (int i = 0; i < s.length(); i++)
            if ('0' > s.charAt(i) || '9' < s.charAt(i))
                result = false;
        return result;
    }
}
