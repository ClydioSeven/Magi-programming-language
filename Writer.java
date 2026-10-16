import java.util.List;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
public class Writer {
    private final List<Statement> program;
    private final ByteArrayOutputStream code = new ByteArrayOutputStream();
    private final Map<String, Integer> variableOffsets = new HashMap<>();
    private int stackOffset = 0;
    public Writer(List<Statement> program) {
        this.program = program;
        generateProgram();
    }

    public byte[] generateProgram() {
        for (Statement statement : program) {
            generateCode(statement);
        }
        generateExit();
        return code.toByteArray();
    }

    private void generateCode(Statement statement) {
        switch (statement.getStatementType()) {
            case PrintNode:
                generatePrint((PrintNode) statement);
                break;

            case IntCreateNode:
                evaluateLong(((IntCreateNode) statement).getValue());
                break;

            case DoubleCreateNode:
                evaluateDouble(((DoubleCreateNode) statement).getValue());
                break;

            case BooleanCreateNode:
                evaluateBoolean(((BooleanCreateNode) statement).getValue());
                break;

            default:
                throw new ArchMagi("Unsupported statement: "
                        + statement.getStatementType());
        }
    }

    private void generatePrint(PrintNode node) {
        Expression expression = node.getExpression();
        switch (expression.getExpressionType()) {
            case IntLiteralNode:
                IntLiteralNode literal = (IntLiteralNode) expression;

                emit(0xBF);
                emitInt64(literal.getValue());
                break;

            default:
                throw new ArchMagi("Expression not supported yet: "
                        + expression.getExpressionType());
        }
    }
    private long evaluateLong(Expression expression) {
        switch (expression.getExpressionType()) {

            case IntLiteralNode:
                return ((IntLiteralNode) expression).getValue();

            case BinaryOperationNode:
                BinaryOperationNode node = (BinaryOperationNode) expression;

                long left = evaluateLong(node.getLeft());
                long right = evaluateLong(node.getRight());

                switch (node.getOperator()) {
                    case PLUS:
                        return left + right;
                    case MINUS:
                        return left - right;
                    case MULTIPLY:
                        return left * right;
                    case DIVIDE:
                        return left / right;
                    default:
                        throw new RuntimeException("Unknown operator");
                }

            default:
                throw new ArchMagi("Unsupported expression");
        }
    }
    private boolean evaluateBoolean(Expression expression) {
        switch (expression.getExpressionType()) {

            case BooleanLiteralNode:
                return ((BooleanLiteralNode) expression).getValue();

            case BinaryOperationNode:
                BinaryOperationNode node = (BinaryOperationNode) expression;

                boolean left = evaluateBoolean(node.getLeft());
                boolean right = evaluateBoolean(node.getRight());

                switch (node.getOperator()) {
                    case EQUALS_EQUALS:
                        return left == right;
                    case NOT_EQUALS:
                        return left != right;
                    case LESS_THAN:
                        return left < right;
                    case GREATER_THAN:
                        return left > right;
                    case LESS_THAN_EQUALS:
                        return left <= right;
                    case GREATER_THAN_EQUALS:
                        return left >= right;
                    default:
                        throw new RuntimeException("Unknown operator");
                }

            default:
                throw new ArchMagi("Unsupported expression");
        }
    }
    private double evaluateDouble(Expression expression) {
        switch (expression.getExpressionType()) {

            case DoubleLiteralNode:
                return ((DoubleLiteralNode) expression).getValue();

            case BinaryOperationNode:
                BinaryOperationNode node = (BinaryOperationNode) expression;

                double left = evaluateDouble(node.getLeft());
                double right = evaluateDouble(node.getRight());

                switch (node.getOperator()) {
                    case PLUS:
                        return left + right;
                    case MINUS:
                        return left - right;
                    case MULTIPLY:
                        return left * right;
                    case DIVIDE:
                        return left / right;
                    default:
                        throw new RuntimeException("Unknown operator");
                }

            default:
                throw new ArchMagi("Unsupported expression");
        }
    }

    private void generateExit() {
        emit(0xB8);
        emitInt32(60);

        emit(0x31, 0xFF);
        emit(0x0F, 0x05);
    }

    private void emit(int... bytes) {
        for (int b : bytes) {
            code.write(b);
        }
    }

    private void emitInt32(int value) {
        emit(
            value,
            value >>> 8,
            value >>> 16,
            value >>> 24
        );
    }
    private void emitInt64(long value) {
    emit(
        (int) value,
        (int) (value >>> 8),
        (int) (value >>> 16),
        (int) (value >>> 24),
        (int) (value >>> 32),
        (int) (value >>> 40),
        (int) (value >>> 48),
        (int) (value >>> 56)
    );
}
}