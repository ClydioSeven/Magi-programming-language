public class DoubleLiteralNode implements Expression {
    private final Double value;

    public DoubleLiteralNode(Double value) {
        this.value = value;
    }

    public Double getValue() {
        return value;
    }
    public ExpressionType getExpressionType(){
        return ExpressionType.DoubleLiteralNode;
    }
}
