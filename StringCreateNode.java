public class StringCreateNode implements Statement,Declaration {
    private final String identifier;
    private final Expression value;

    public StringCreateNode(String identifier, Expression value) {
        this.identifier = identifier;
        this.value = value;
    }

    public String getIdentifier() {
        return identifier;
    }

    public Expression getValue() {
        return value;
    }

    public TokenType getType() {
        return TokenType.SPELL;
    }
    public StatementType getStatementType(){
        return StatementType.StringCreateNode;
    }
}