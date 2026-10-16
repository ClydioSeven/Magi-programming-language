import java.util.List;

public class ForNode implements Statement {
    private final Statement initialization;
    private final Expression condition;
    private final Statement increment;
    private final List<Statement> body;

    public ForNode(Statement initialization, Expression condition, Statement increment, List<Statement> body) {
        this.initialization = initialization;
        this.condition = condition;
        this.increment = increment;
        this.body = body;
    }

    public Statement getInitialization() {
        return initialization;
    }

    public Expression getCondition() {
        return condition;
    }

    public Statement getIncrement() {
        return increment;
    }

    public List<Statement> getBody() {
        return body;
    }
    public StatementType getStatementType(){
        return StatementType.ForNode;
    }

}
