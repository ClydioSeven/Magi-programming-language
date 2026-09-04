public class BooleanCreateNode implements Statement {
    private final String identifier;
    private final BooleanLiteralNode value;

    public BooleanCreateNode(String identifier, BooleanLiteralNode value) {
        this.identifier = identifier;
        this.value = value;
    }

    public String getIdentifier() {
        return identifier;
    }

    public BooleanLiteralNode getValue() {
        return value;
    }

}
