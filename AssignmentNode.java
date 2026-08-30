public record AssignmentNode(
    String name,
    Expression value
) implements Statement {
}