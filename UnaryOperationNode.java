public class UnaryOperationNode implements Expression {
    private final TokenType operator;
    private final Expression operand;

    public UnaryOperationNode(TokenType operator, Expression operand) {
        this.operator = operator;
        this.operand = operand;
    }

    public TokenType getOperator() {
        return operator;
    }

    public Expression getOperand() {
        return operand;
    }
    
    public ExpressionType getExpressionType(){
        return ExpressionType.UnaryOperationNode;
    }
}
