public class BooleanLiteralNode implements Expression {
    private final boolean value;

    public BooleanLiteralNode(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }
    
}
