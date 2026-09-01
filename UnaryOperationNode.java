public class UnaryOperationNode implements Expression {
    private final Token operator;
    private final Expression operand;

    public UnaryOperationNode(Token operator, Expression operand) {
        this.operator = operator;
        this.operand = operand;
    }

    public Token getOperator() {
        return operator;
    }

    public Expression getOperand() {
        return operand;
    }
    
}
