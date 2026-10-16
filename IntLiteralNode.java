public class IntLiteralNode implements Expression {
    private final long value;

    public IntLiteralNode(long value) {
        this.value = value;
    }

    public Long getValue() {
        return value;
    }
    public ExpressionType getExpressionType(){
        return ExpressionType.IntLiteralNode;
    }
}