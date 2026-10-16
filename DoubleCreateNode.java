public class DoubleCreateNode implements Statement,Declaration {
    private final String name;
    private final Expression value;

    public DoubleCreateNode(String name, Expression value) {
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
        return TokenType.GLYPH;
    }
    public StatementType getStatementType(){
        return StatementType.DoubleCreateNode;
    }
}
