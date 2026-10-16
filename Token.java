public class Token {

    private final TokenType type;
    private final String value;

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
         switch(type) {
            case IDENTIFIER:
                return "[IDENTIFIER: " + value + "]";

            case INTEGER:
                return "[INTEGER: " + value + "]";

            case DOUBLE:
                return "[DOUBLE: " + value + "]";

            case STRING:
                return "[STRING: " + value + "]";

            default:
                return "[" + type + "]";
        }
    }
}