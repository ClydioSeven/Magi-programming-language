public class VariableInfo {
    private int offset;
    private TokenType type;
    private Object knownValue;

    public VariableInfo(int offset, TokenType type, Object knownValue) {
        this.offset = offset;
        this.type = type;
        this.knownValue = knownValue;
    }

    public int getOffset() {
        return offset;
    }

    public TokenType getType() {
        return type;
    }

    public Object getKnownValue() {
        return knownValue;
    }

    public void setKnownValue(Object knownValue) {
        this.knownValue = knownValue;
    }
}