public class UnaryOperationNode implements Expression {
    private final Token operator;
    private final Expression operand;

    public UnaryOperationNode(TokenType not, Expression operand) {
        this.operator = not;
        this.operand = operand;
    }

    public Token getOperator() {
        return operator;
    }

    public Expression getOperand() {
        return operand;
    }
    
}
