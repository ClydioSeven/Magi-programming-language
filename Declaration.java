public interface Declaration {
    String getIdentifier();
    TokenType getType();
    Expression getValue();
}
