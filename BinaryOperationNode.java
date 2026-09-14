public class BinaryOperationNode implements Expression {
    private final Expression left;
    private final TokenType operator;
    private final Expression right;

    public BinaryOperationNode(Expression left, TokenType operator, Expression right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    public Expression getLeft() {
        return left;
    }

    public TokenType getOperator() {
        return operator;
    }

    public Expression getRight() {
        return right;
    }
    public ExpressionType getExpressionType(){
        return ExpressionType.BinaryOperationNode;
    }
}
