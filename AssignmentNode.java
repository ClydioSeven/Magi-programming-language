public class AssignmentNode implements Statement {
    private final String name;
    private final Expression value;

    public AssignmentNode(String name, Expression value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Expression getValue() {
        return value;
    }
}