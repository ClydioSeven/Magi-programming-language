import java.util.List;

public class ProgramNode implements Node {
    private final List<Statement> statements;

    public ProgramNode(List<Statement> statements) {
        this.statements = statements;
    }

    public List<Statement> getStatements() {
        return statements;
    }
}
