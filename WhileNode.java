import java.util.List;

public class WhileNode implements Statement {
    private Expression condition;
    private List<Statement> body;

    public WhileNode(Expression condition, List<Statement> body) {
        this.condition = condition;
        this.body = body;
    }

    public Expression getCondition() {
        return condition;
    }

    public List<Statement> getBody() {
        return body;
    }
}
