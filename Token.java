public class Token {
    private TokenType type;
    private String value;

    public Token(TokenType type, String value) {
        this.type = type;
        this.value = value;
    }
    public Token(TokenType type,int value) {
        this.type = type;
        this.value = String.valueOf(value);
    }
    public Token(TokenType type,double value) {
        this.type = type;
        this.value = String.valueOf(value);
    }


    public TokenType getType() {
        return type;
    }
    public String getValue() {
        return value;
    }
    @Override
    public String toString() {
        return "Token{" +
                "type=" + type +
                ", value='" + value + '\'' +
                '}';
    }

}
