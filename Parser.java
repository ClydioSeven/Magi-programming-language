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
    private Statement parseStatementFor() {

        Token token = peek();

        return switch (token.getType()) {

            case RUNE ->
                parseIntDeclaration();

            case GLYPH ->
                parseDoubleDeclaration();

            case SIGIL ->
                parseBooleanDeclaration();

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
    private Statement parseIdentifierStatementFor() {
    Token next = peek(1);
    return switch (next.getType()) {
        case EQUALS ->
            parseAssignmentFor();

        case PLUS_PLUS ->
            parseIncrementFor();

        case MINUS_MINUS ->
            parseDecrementFor();

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
    private Statement parseDecrementFor() {
        Token identifier = consume(TokenType.IDENTIFIER);
        consume(TokenType.MINUS_MINUS);

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
    private Statement parseIncrementFor() {
        Token identifier = consume(TokenType.IDENTIFIER);
        consume(TokenType.PLUS_PLUS);

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
    private Statement parseAssignmentFor() {
        Token identifier = consume(TokenType.IDENTIFIER);
        consume(TokenType.EQUALS);
        Expression value = parseExpression();
        return new AssignmentNode(identifier.getValue(), value);
    }

    private Expression parseExpression() {
        return parseOrExpression();
    }

    private Expression parseOrExpression() {
        Expression left = parseAndExpression();
        return parseOrTail(left);
        
    }

    private Expression parseAndExpression() {
        Expression left = parseEqualityExpression();
        return parseAndTail(left);
        
    }
    private Expression parseEqualityExpression() {
        Expression left = parseComparisonExpression();
        return parseEqualityTail(left);
        
    }
    private Expression parseComparisonExpression() {
        Expression left = parseAdditiveExpression();
        return parseComparisonTail(left);
    }

    private Expression parseAdditiveExpression() {
        Expression left = parseTerm();
        return parseAdditiveTail(left);
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

    private Expression parseAdditiveTail(Expression leftExpression) {
        Token next = peek();
        switch (next.getType()) {
            case PLUS -> {
                consume(TokenType.PLUS);
                    Expression right = parseTerm();
                    Expression combined = new BinaryOperationNode(leftExpression, TokenType.PLUS, right);
                    return parseAdditiveTail(combined);
            }
            case MINUS -> {
                consume(TokenType.MINUS);
                    Expression right = parseTerm();
                    Expression combined = new BinaryOperationNode(leftExpression, TokenType.MINUS, right);
                    return parseAdditiveTail(combined);
            }
        }
        return leftExpression;
    }

    private Expression parseComparisonTail(Expression leftExpression) {
        Token next = peek();
        switch (next.getType()) {
            case LESS_THAN -> {
                consume(TokenType.LESS_THAN);
                Expression right = parseAdditiveExpression();
                Expression combined = new BinaryOperationNode(leftExpression, TokenType.LESS_THAN, right);
                return parseComparisonTail(combined);
            }
            case LESS_EQUAL -> {
                consume(TokenType.LESS_EQUAL);
                Expression right = parseAdditiveExpression();
                Expression combined = new BinaryOperationNode(leftExpression, TokenType.LESS_EQUAL, right);
                return parseComparisonTail(combined);
            }
            case GREATER_THAN -> {
                consume(TokenType.GREATER_THAN);
                Expression right = parseAdditiveExpression();
                Expression combined = new BinaryOperationNode(leftExpression, TokenType.GREATER_THAN, right);
                return parseComparisonTail(combined);
            }
            case GREATER_EQUAL -> {
                consume(TokenType.GREATER_EQUAL);
                Expression right = parseAdditiveExpression();
                Expression combined = new BinaryOperationNode(leftExpression, TokenType.GREATER_EQUAL, right);
                return parseComparisonTail(combined);
            }
        }
        return leftExpression;
    }

    private Expression parseEqualityTail(Expression leftExpression) {
        Token next = peek();
        switch (next.getType()) {
            case EQUALS_EQUALS -> {
                consume(TokenType.EQUALS_EQUALS);
                Expression right = parseComparisonExpression();
                Expression combined = new BinaryOperationNode(leftExpression, TokenType.EQUALS_EQUALS, right);
                return parseEqualityTail(combined);
            }
            case NOT_EQUAL ->{
                consume(TokenType.NOT_EQUAL);
                Expression right = parseComparisonExpression();
                Expression combined = new BinaryOperationNode(leftExpression, TokenType.NOT_EQUAL, right);
                return parseEqualityTail(combined);
            }
            
        };
        return leftExpression;
        
    }

    private Expression parseAndTail(Expression leftExpression) {
        Token next = peek();
        if(next.getType() == TokenType.AMPAMP){
            consume(TokenType.AMPAMP);
            Expression right = parseEqualityExpression();
            Expression combined = new BinaryOperationNode(leftExpression, TokenType.AMPAMP, right);
            return parseAndTail(combined);
        }
        return leftExpression;
    }

    private Expression parseOrTail(Expression leftExpression) {
        Token next = peek();
        if(next.getType() == TokenType.VERTBARBAR){
            consume(TokenType.VERTBARBAR);
            Expression right = parseAndExpression();
            Expression combined = new BinaryOperationNode(leftExpression, TokenType.VERTBARBAR, right);
            return parseOrTail(combined);
        }
        return leftExpression;
    }

    private Statement parseFor() {
        consume(TokenType.BARRAGE);
        consume(TokenType.LEFT_PAREN);
        Statement initialization = parseStatementFor();
        Expression condition = parseExpression();
        consume(TokenType.SEMICOLON);
        Statement increment = parseIdentifierStatementFor();
        consume(TokenType.RIGHT_PAREN);
        consume(TokenType.LEFT_BRACE);
        List<Statement> body = new ArrayList<>();
        while (peek().getType() != TokenType.RIGHT_BRACE) {
            body.add(parseStatement());
        }
        consume(TokenType.RIGHT_BRACE);
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
        Expression expression = parseExpression();
        consume(TokenType.RIGHT_PAREN);
        consume(TokenType.SEMICOLON);
        return new PrintNode(expression);
    }

    private Statement parseBooleanDeclaration() {
        consume(TokenType.SIGIL);
        Token identifier = consume(TokenType.IDENTIFIER);
        consume(TokenType.EQUALS);
        Expression expression = parseExpression();
        consume(TokenType.SEMICOLON);
        return new BooleanCreateNode(
            identifier.getValue(),
            expression
        );
    }
    
    private Statement parseStringDeclaration() {
        consume(TokenType.SPELL);
        Token identifier = consume(TokenType.IDENTIFIER);
        consume(TokenType.EQUALS);
        Expression string = parseExpression();
        consume(TokenType.SEMICOLON);
        return new StringCreateNode(
            identifier.getValue(),
            string
        );
    }

    private Statement parseDoubleDeclaration() {
        consume(TokenType.GLYPH);
        Token identifier = consume(TokenType.IDENTIFIER);
        consume(TokenType.EQUALS);
        Expression number = parseExpression();
        consume(TokenType.SEMICOLON);
        return new DoubleCreateNode(
            identifier.getValue(),
            number
            );
    }

    private Statement parseIntDeclaration() {
        consume(TokenType.RUNE);
        Token identifier = consume(TokenType.IDENTIFIER);

        consume(TokenType.EQUALS);

        Expression number = parseExpression();

        consume(TokenType.SEMICOLON);

        return new IntCreateNode(
            identifier.getValue(),
            number
        );
    }
}