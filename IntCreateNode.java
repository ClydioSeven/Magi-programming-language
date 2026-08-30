public record IntCreateNode(
    String name,
    Expression value
) implements Statement {
}