public record BinaryOperationNode(
    Expression left,
    TokenType operator,
    Expression right
) implements Expression {
}
