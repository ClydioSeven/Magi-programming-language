public class BooleanCreateNode implements Statement {
    private final String identifier;
    private final Expression value;

    public BooleanCreateNode(String identifier, Expression value) {
        this.identifier = identifier;
        this.value = value;
    }

    public String getIdentifier() {
        return identifier;
    }

    public Expression getValue() {
        return value;
    }

}
