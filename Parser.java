import java.util.List;

public class Parser {
    private final List<Token> tokens;
    private int current = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    private Token peek() {
        return tokens.get(current);
    }

    private Token peek(int distance) {
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

    public ProgramNode parseProgram() {
        List<Statement> statements = new java.util.ArrayList<>();
        while (current < tokens.size()) {
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'parseExpression'");
    }

    private Statement parseFor() {
        consume(TokenType.BARRAGE);
        consume(TokenType.LEFT_PAREN);
        Token next = peek();
        Statement initialization;
        switch(next.getType()){
            case RUNE:
                consume(TokenType.RUNE);
                Token identifier = consume(TokenType.IDENTIFIER);
                consume(TokenType.EQUALS);
                Token number = consume(TokenType.INTEGER);
                initialization = new IntCreateNode(
                    identifier.getValue(),
                    new IntLiteralNode(
                        Integer.parseInt(number.getValue())
                    )
                );
                break;
            case GLYPH:
                consume(TokenType.GLYPH);
                Token identifier2 = consume(TokenType.IDENTIFIER);
                consume(TokenType.EQUALS);
                Token number2 = consume(TokenType.DOUBLE);
                initialization = new DoubleCreateNode(
                    identifier2.getValue(),
                    new DoubleLiteralNode(
                        Double.parseDouble(number2.getValue())
                    )
                );
                break;
            default:
                throw new IllegalArgumentException("Unexpected value: " + next.getType());
        }
        consume(TokenType.SEMICOLON);
        

    }

    private Statement parseIf() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'parseIf'");
    }

    private Statement parseWhile() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'parseWhile'");
    }

    private Statement parsePrint() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'parsePrint'");
    }

    private Statement parseBooleanDeclaration() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'parseBooleanDeclaration'");
    }

    private Statement parseStringDeclaration() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'parseStringDeclaration'");
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