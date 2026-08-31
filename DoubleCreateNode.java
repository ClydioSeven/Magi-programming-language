public class DoubleCreateNode implements Statement {
    private final String name;
    private final Expression value;

    public DoubleCreateNode(String name, Expression value) {
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
