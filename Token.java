public class Token {

    private final TokenType type;
    private final String value;

    // Main constructor
    public Token(TokenType type, String value) {
        this.type = type;
        this.value = value;
    }

    
    public Token(TokenType type, int value) {
        this(type, String.valueOf(value));
    }

    
    public Token(TokenType type, double value) {
        this(type, String.valueOf(value));
    }

    public TokenType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {

        return switch(type) {

            case IDENTIFIER ->
                "[IDENTIFIER: " + value + "]";

            case INTEGER ->
                "[INTEGER: " + value + "]";

            case DOUBLE ->
                "[DOUBLE: " + value + "]";

            case STRING ->
                "[STRING: " + value + "]";

            default ->
                "[" + type + "]";
        };
    }
}