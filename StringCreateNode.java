public class StringCreateNode implements Statement {
    private final String identifier;
    private final StringLiteralNode value;

    public StringCreateNode(String identifier, StringLiteralNode value) {
        this.identifier = identifier;
        this.value = value;
    }

    public String getIdentifier() {
        return identifier;
    }

    public StringLiteralNode getValue() {
        return value;
    }
    
}
