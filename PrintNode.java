public class PrintNode implements Statement {
    private final Expression expression;

    public PrintNode(Expression expression) {
        this.expression = expression;
    }

    public Expression getExpression() {
        return expression;
    }
    public StatementType getStatementType(){
        return StatementType.PrintNode;
    }

}
