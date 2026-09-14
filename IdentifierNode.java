public class IdentifierNode implements Expression {
    private final String name;

    public IdentifierNode(String name) {
        this.name = name;
    }

    public String getIdentifier() {
        return name;
    }
    public ExpressionType getExpressionType(){
        return ExpressionType.IdentifierNode;
    }
}
