public class IntCreateNode implements Statement,Declaration {
    private final String name;
    private final Expression value;

    public IntCreateNode(String name, Expression value) {
        this.name = name;
        this.value = value;
    }

    public String getIdentifier() {
        return name;
    }

    public Expression getValue() {
        return value;
    }

    public TokenType getType() {
        return TokenType.RUNE;
    }
    public StatementType getStatementType(){
        return StatementType.IntCreateNode;
    }
}