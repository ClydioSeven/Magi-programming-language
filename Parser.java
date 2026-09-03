import java.util.ArrayList;
import java.util.List;

public class Parser {
    private final List<Token> tokens;
    private int current = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    private Token peek() {
        if (isAtEnd()) {
            throw new RuntimeException("Unexpected end of program.");
        }
    return tokens.get(current);
}

    private Token peek(int distance) {
    if (current + distance >= tokens.size()) {
        throw new RuntimeException("Unexpected end of program.");
    }
    return tokens.get(current + distance);
}

    private Token consume(TokenType expected) {
        if (current >= tokens.size()) {
            throw new RuntimeException("Unexpected end, expected: " + expected);
        }

        Token token = tokens.get(current);
        if (token.getType() != expected) {
            throw new RuntimeException("Unexpected token: " + token.getType() + ", expected: " + expected);
        }

        current++;
        return token;
    }
    private boolean isAtEnd() {
        return current >= tokens.size();
    }

    public ProgramNode parseProgram() {
        List<Statement> statements = new java.util.ArrayList<>();
        while (!isAtEnd()) {
            statements.add(parseStatement());
        }
        return new ProgramNode(statements);
    }

    private Statement parseStatement() {

        Token token = peek();

        return switch (token.getType()) {

            case RUNE ->
                parseIntDeclaration();

            case GLYPH ->
                parseDoubleDeclaration();

            case SPELL ->
                parseStringDeclaration();

            case SIGIL ->
                parseBooleanDeclaration();

            case CAST ->
                parsePrint();

            case CHANT ->
                parseWhile();

            case WARD ->
                parseIf();

            case BARRAGE ->
                parseFor();

            case IDENTIFIER ->
                parseIdentifierStatement();

            default ->
                throw new RuntimeException(
                        "Unexpected token: " + token);
        };
    }

    private Statement parseIdentifierStatement() {
    Token next = peek(1);
    return switch (next.getType()) {

        case EQUALS ->
            parseAssignment();

        case PLUS_PLUS ->
            parseIncrement();

        case MINUS_MINUS ->
            parseDecrement();

        default ->
            throw new RuntimeException(
                "Unexpected token after identifier: " + next
            );
    };
}

    private Statement parseDecrement() {
        Token identifier = consume(TokenType.IDENTIFIER);
        consume(TokenType.MINUS_MINUS);
        consume(TokenType.SEMICOLON);

        String name= identifier.getValue();
        return new AssignmentNode(
            name,
            new BinaryOperationNode(
                new IdentifierNode(name),
                TokenType.MINUS,
                new IntLiteralNode(1)
            )
        );
    }

    private Statement parseIncrement() {
        Token identifier = consume(TokenType.IDENTIFIER);
        consume(TokenType.PLUS_PLUS);
        consume(TokenType.SEMICOLON);

        String name= identifier.getValue();
        return new AssignmentNode(
            name,
            new BinaryOperationNode(
                new IdentifierNode(name),
                TokenType.PLUS,
                new IntLiteralNode(1)
            )
        );
    }

    private Statement parseAssignment() {
        Token identifier = consume(TokenType.IDENTIFIER);
        consume(TokenType.EQUALS);
        Expression value = parseExpression();
        consume(TokenType.SEMICOLON);
        return new AssignmentNode(identifier.getValue(), value);
    }

    private Expression parseExpression() {
        Expression expression = parseOrExpression();
        return expression;
    }

    private Expression parseOrExpression() {
        Expression left = parseAndExpression();
        Expression right = OrTail();
        return new BinaryOperationNode(left, TokenType.VERTBARBAR, right);
    }

    private Expression parseAndExpression() {
        Expression left = parseEqualityExpression();
        Expression right = AndTail();
        return new BinaryOperationNode(left, TokenType.AMPAMP, right);
    }
    private Expression parseEqualityExpression() {
        Expression left = parseComparisonExpression();
        Expression right = EqualityTail();
        return new BinaryOperationNode(left, TokenType.EQUALS_EQUALS, right);
    }
    private Expression parseComparisonExpression() {
        Expression left = parseAdditiveExpression();
        Expression right = parseComparisonTail();
        return new BinaryOperationNode(left, null, right);
    }

    private Expression parseAdditiveExpression() {
        Expression left = parseTerm();
        Expression right = parseAdditiveTail();
        return new BinaryOperationNode(left, null, right);
    }

    private Expression parseTerm() {
        Expression left = parseFactor();
        return parseTermTail(left);
        
    }

    private Expression parseTermTail(Expression LeftExpression) {
        Token next = peek();
        switch (next.getType()) {
            case MULTIPLY -> {
                consume(TokenType.MULTIPLY);
                    Expression right = parseFactor();
                    Expression combined = new BinaryOperationNode(LeftExpression, TokenType.MULTIPLY, right);
                return parseTermTail(combined);
                }
            case DIVIDE -> {
                consume(TokenType.DIVIDE);
                    Expression right = parseFactor();
                    Expression combined = new BinaryOperationNode(LeftExpression, TokenType.DIVIDE, right);
                return parseTermTail(combined);
            }
            case MODULO -> {
                consume(TokenType.MODULO);
                    Expression right = parseFactor();
                    Expression combined = new BinaryOperationNode(LeftExpression, TokenType.MODULO, right);
                return parseTermTail(combined);
                
            }
        }
        return LeftExpression;
    }

    private Expression parseFactor() {
        Token next = peek();
        return switch (next.getType()) {
            case INTEGER -> {
                consume(TokenType.INTEGER);
                yield new IntLiteralNode(
                    Integer.parseInt(next.getValue())
                );
            }
            case DOUBLE -> {
                consume(TokenType.DOUBLE);
                yield new DoubleLiteralNode(
                    Double.parseDouble(next.getValue())
                );            
            }
            case STRING -> {
                consume(TokenType.STRING);
                yield new StringLiteralNode(next.getValue());
            }
            case TRUE -> {
                consume(TokenType.TRUE);
                yield new BooleanLiteralNode(true);
            }
            case FALSE -> {
                consume(TokenType.FALSE);
                yield new BooleanLiteralNode(false);
            }
            case IDENTIFIER -> {
                consume(TokenType.IDENTIFIER);
                yield new IdentifierNode(next.getValue());
            }
            case LEFT_PAREN -> {
                consume(TokenType.LEFT_PAREN);
                Expression expression = parseExpression();
                consume(TokenType.RIGHT_PAREN);
                yield expression;
            }
            case NOT -> {
                consume(TokenType.NOT);
                Expression expression = parseFactor();
                yield new UnaryOperationNode(TokenType.NOT, expression);
            }
            case MINUS -> {
                consume(TokenType.MINUS);
                Expression expression = parseFactor();
                yield new UnaryOperationNode(TokenType.MINUS, expression);
            }
            default -> throw new IllegalArgumentException("Unexpected token: " + next.getType());
        };
    }

    private Expression parseAdditiveTail() {
        Token next = peek();
        switch (next.getType()) {
            case PLUS -> {
                consume(TokenType.PLUS);
                Expression right = parseTerm();
                Expression left = parseAdditiveTail();
                return new BinaryOperationNode(left, TokenType.PLUS, right);
            }
            case MINUS -> {
                consume(TokenType.MINUS);
                Expression right = parseTerm();
                Expression left = parseAdditiveTail();
                return new BinaryOperationNode(left, TokenType.MINUS, right);
            }
        }
        return null;
    }

    private Expression parseComparisonTail() {
        Token next = peek();
        switch (next.getType()) {
            case LESS_THAN -> {
                consume(TokenType.LESS_THAN);
                Expression right = parseAdditiveExpression();
                Expression left = parseComparisonTail();
                return new BinaryOperationNode(left, TokenType.LESS_THAN, right);
            }
            case LESS_EQUAL -> {
                consume(TokenType.LESS_EQUAL);
                Expression right = parseAdditiveExpression();
                Expression left = parseComparisonTail();
                return new BinaryOperationNode(left, TokenType.LESS_EQUAL, right);
            }
            case GREATER_THAN -> {
                consume(TokenType.GREATER_THAN);
                Expression right = parseAdditiveExpression();
                Expression left = parseComparisonTail();
                return new BinaryOperationNode(left, TokenType.GREATER_THAN, right);
            }
            case GREATER_EQUAL -> {
                consume(TokenType.GREATER_EQUAL);
                Expression right = parseAdditiveExpression();
                Expression left = parseComparisonTail();
                return new BinaryOperationNode(left, TokenType.GREATER_EQUAL, right);
            }
        }
        return null;
    }

    private Expression EqualityTail() {
        Token next = peek();
        switch (next.getType()) {
            case EQUALS_EQUALS -> {
                consume(TokenType.EQUALS_EQUALS);
                Expression right = parseComparisonExpression();
                Expression left = EqualityTail();
                return new BinaryOperationNode(left, TokenType.EQUALS_EQUALS, right);
            }
            case NOT_EQUAL ->{
                consume(TokenType.NOT_EQUAL);
                Expression right = parseComparisonExpression();
                Expression left = EqualityTail();
                return new BinaryOperationNode(left, TokenType.NOT_EQUAL, right);
            }
            
        };
        return null;
        
    }

    private Expression AndTail() {
        Token next = peek();
        if(next.getType() == TokenType.AMPAMP){
            consume(TokenType.AMPAMP);
            Expression right = parseEqualityExpression();
            Expression left = AndTail();
            return new BinaryOperationNode(left, TokenType.AMPAMP, right);
        }
        return null;
    }

    private Expression OrTail() {
        Token next = peek();
        if(next.getType() == TokenType.VERTBARBAR){
            consume(TokenType.VERTBARBAR);
            Expression right = parseAndExpression();
            Expression left = OrTail();
            return new BinaryOperationNode(left, TokenType.VERTBARBAR, right);
        }
        return null;
    }

    private Statement parseFor() {
        consume(TokenType.BARRAGE);
        consume(TokenType.LEFT_PAREN);
        Token next = peek();
        Statement initialization = parseStatement();
        consume(TokenType.SEMICOLON);
        Expression condition = parseExpression();
        consume(TokenType.SEMICOLON);
        //Increment/Decrement or Expression
        consume(TokenType.RIGHT_PAREN);
        consume(TokenType.LEFT_BRACE);
        List<Statement> body = new ArrayList<>();
        while (peek().getType() != TokenType.RIGHT_BRACE) {
            body.add(parseStatement());
        }
        consume(TokenType.RIGHT_BRACE);
        //Remove later 
        Statement increment = parseIdentifierStatement();
        return new ForNode(initialization, condition, increment, body);


    }

    private Statement parseIf() {
        consume(TokenType.WARD);
        consume(TokenType.LEFT_PAREN);
        Expression condition = parseExpression();
        consume(TokenType.RIGHT_PAREN);
        consume(TokenType.LEFT_BRACE);
        List<Statement> thenBranch = new ArrayList<>();
        List<Statement> elseBranch = new ArrayList<>();
        while (peek().getType() != TokenType.RIGHT_BRACE) {
        
            thenBranch.add(parseStatement());
        }
        consume(TokenType.RIGHT_BRACE);
        if(!isAtEnd()){
           if (peek().getType() == TokenType.ELSE) {
           if(peek(1).getType() == TokenType.LEFT_BRACE){
            consume(TokenType.ELSE);
            consume(TokenType.LEFT_BRACE);
            while (peek().getType() != TokenType.RIGHT_BRACE) {
                elseBranch.add(parseStatement());
            }
            consume(TokenType.RIGHT_BRACE);
        }else if(peek(1).getType() == TokenType.WARD){
            consume(TokenType.ELSE);
            elseBranch.add(parseIf());
        }
        } 
        }
        
        return new IfNode(condition,thenBranch,elseBranch);  
    }

    private Statement parseWhile() {
        consume(TokenType.CHANT);
        consume(TokenType.LEFT_PAREN);
        Expression condition = parseExpression();
        consume(TokenType.RIGHT_PAREN);
        consume(TokenType.LEFT_BRACE);
        List<Statement> body = new ArrayList<>();
        while (peek().getType() != TokenType.RIGHT_BRACE) {
            body.add(parseStatement());
        }
        consume(TokenType.RIGHT_BRACE);
        return new WhileNode(condition, body);
    }

    private Statement parsePrint() {
        consume(TokenType.CAST);
        consume(TokenType.LEFT_PAREN);
        
        consume(TokenType.RIGHT_PAREN);
        throw new UnsupportedOperationException("Unimplemented method 'parsePrint'");
    }

    private Statement parseBooleanDeclaration() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'parseBooleanDeclaration'");
    }

    private Statement parseStringDeclaration() {
        consume(TokenType.SPELL);
        Token identifier = consume(TokenType.IDENTIFIER);
        consume(TokenType.EQUALS);
        Token string = consume(TokenType.STRING);
        consume(TokenType.SEMICOLON);
        return new StringCreateNode(
            identifier.getValue(),
            new StringLiteralNode(string.getValue())
        );
    }

    private Statement parseDoubleDeclaration() {
        consume(TokenType.GLYPH);
        Token identifier = consume(TokenType.IDENTIFIER);
        consume(TokenType.EQUALS);
        Token number = consume(TokenType.DOUBLE);
        consume(TokenType.SEMICOLON);

        return new DoubleCreateNode(
            identifier.getValue(),
            new DoubleLiteralNode(
                Double.parseDouble(number.getValue())
            )
        );
    }

    private Statement parseIntDeclaration() {
        consume(TokenType.RUNE);
        Token identifier = consume(TokenType.IDENTIFIER);

        consume(TokenType.EQUALS);

        Token number =
        consume(TokenType.INTEGER);

        consume(TokenType.SEMICOLON);

        return new IntCreateNode(
            identifier.getValue(),
            new IntLiteralNode(
                Integer.parseInt(number.getValue())
            )
        );
    }
}