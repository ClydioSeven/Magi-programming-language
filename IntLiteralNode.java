public class IntLiteralNode implements Expression {
    private final Integer value;

    public IntLiteralNode(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}