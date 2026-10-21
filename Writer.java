import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class Writer {
    private final List<Statement> program;
    private final ByteArrayOutputStream code = new ByteArrayOutputStream();
    private final Map<String, Integer> variableOffsets = new HashMap<>();
    private HashMap<String,VariableInfo> variables = new HashMap<>();
    private int stackOffset = 0;

    public Writer(List<Statement> program) {
        this.program = program;
        generateProgram();
    }

    public byte[] generateProgram() {
        for (Statement statement : program) {
            //System.out.println("Generated code for statement: " + statement.getStatementType());
            generateCode(statement);
        }
        generateExit();
        return code.toByteArray();
    }

    private void generateCode(Statement statement) {
        switch (statement.getStatementType()) {
            case AssignmentNode:{
               AssignmentNode node = (AssignmentNode)statement;
               VariableInfo info = variables.get(node.getIdentifier());
               switch(info.getType()){
                    case RUNE:{
                        Number value = getValue(node.getValue());
                        variables.put(node.getIdentifier(), new VariableInfo(info.getOffset(),TokenType.RUNE,value.longValue()));
                        break;
                    }
                    case GLYPH:{
                        Number value = getValue(node.getValue());
                        variables.put(node.getIdentifier(), new VariableInfo(info.getOffset(),TokenType.GLYPH,value.doubleValue()));
                        break;
                    }
                    case SIGIL:{
                        Boolean value = evaluateBoolean(node.getValue());
                        variables.put(node.getIdentifier(), new VariableInfo(info.getOffset(),TokenType.SIGIL,value));
                        break;
                    }
                    case SPELL:{
                        String value = getString(node.getValue());
                        variables.put(node.getIdentifier(), new VariableInfo(info.getOffset(),TokenType.SPELL,value));
                        break;
                    }
                    
               } 
                break;
            }
            case StringCreateNode:{
                StringCreateNode node = (StringCreateNode)statement;
                String value = getString(node.getValue());
                variables.put(node.getIdentifier(),new VariableInfo(stackOffset+8,TokenType.SPELL,value));
                System.out.println(value);
                break;
            }
                
            case PrintNode:{
                PrintNode node = (PrintNode)statement;
                String value = getString(node.getExpression());
                System.out.println(value);
                break;
            }

            case IntCreateNode:{
                IntCreateNode node = (IntCreateNode)statement;
                Number value = getValue(node.getValue());
                variables.put(node.getIdentifier(), new VariableInfo(stackOffset+8,TokenType.RUNE,value.longValue()));
                //System.out.println(value);
                break;
            }
            case DoubleCreateNode:{
                DoubleCreateNode node = (DoubleCreateNode)statement;
                Number value = getValue(node.getValue());
                variables.put(node.getIdentifier(), new VariableInfo(stackOffset+8,TokenType.GLYPH,value.doubleValue()));
                break;
            }

            case BooleanCreateNode:{
                //System.out.println(evaluateBoolean(((BooleanCreateNode) statement).getValue()));
                BooleanCreateNode node  = (BooleanCreateNode)statement;
                Boolean value = evaluateBoolean(node.getValue());
                variables.put(node.getIdentifier(), new VariableInfo(stackOffset+8,TokenType.SIGIL,value.booleanValue()));

                break;
            }
            case IfNode:{
                IfNode ifNode = (IfNode)statement;
                Expression ifCondition = ifNode.getCondition();
                List<Statement> thenBranch = ifNode.getThenBranch();
                List<Statement> elseBranch = ifNode.getElseBranch();
                Boolean cond = evaluateBoolean(ifCondition);
            }
                break;
            case ForNode:{
                ForNode node = (ForNode)statement;
                Statement init = node.getInitialization();
                Expression cond = node.getCondition();
                List<Statement> body = node.getBody();
                Boolean active = evaluateBoolean(cond);
            }
                break;
            case WhileNode:{
                WhileNode node = (WhileNode)statement;
                Boolean cond = evaluateBoolean(node.getCondition());
                List<Statement> body = node.getBody();
            }
            default:
                throw new ArchMagi("Unsupported statement: "
                        + statement.getStatementType());
        }
    }
    private boolean evaluateBoolean(Expression expression) {
        //System.out.println(expression.getExpressionType());
        switch (expression.getExpressionType()) {
            case BooleanLiteralNode:{
                return ((BooleanLiteralNode) expression).getValue();
            }
            case BinaryOperationNode:{
                BinaryOperationNode node = (BinaryOperationNode) expression;
                switch (node.getOperator()) {
                    case EQUALS_EQUALS:{
                        return getEqualEqual(node);
                    }
                    case NOT_EQUAL:{
                        return !getEqualEqual(node);
                    }
                    case LESS_THAN:{
                            Number left = getValue(node.getLeft());
                            Number right = getValue(node.getRight());
                            return left.doubleValue() < right.doubleValue();
                        }
                    case GREATER_THAN:{
                            Number left = getValue(node.getLeft());
                            Number right = getValue(node.getRight());
                            return left.doubleValue() > right.doubleValue();
                        }
                    case LESS_EQUAL:{
                            Number left = getValue(node.getLeft());
                            Number right = getValue(node.getRight());
                            return left.doubleValue() <= right.doubleValue();
                        }
                    case GREATER_EQUAL:{
                            Number left = getValue(node.getLeft());
                            Number right = getValue(node.getRight());
                            return left.doubleValue() >= right.doubleValue();
                        }
                    case NOT:{
                        boolean left = evaluateBoolean(node.getLeft());
                        return !left;
                    }
                    default:
                        throw new RuntimeException("Unknown operator");
                }
            }
            case UnaryOperationNode:{
                boolean node = evaluateBoolean(((UnaryOperationNode) expression).getOperand());
                return !node;
            }

            default:
                throw new ArchMagi("Unsupported expression 2");
        }
    }
    private boolean getEqualEqual(BinaryOperationNode node){
        TokenType type = traverse(node.getLeft());
        switch(type){
            case SIGIL:{
                boolean left = evaluateBoolean(node.getLeft());
                boolean right = evaluateBoolean(node.getRight());
                return left == right;
            }
            case RUNE:
            case GLYPH:{
                Number left = getValue(node.getLeft());
                Number right = getValue(node.getRight());
                if(left instanceof Long && right instanceof Long){
                    return left.longValue() == right.longValue();
                }
                return left.doubleValue() == right.doubleValue();
            }
            case SPELL:{
                String left = getString(node.getLeft());
                String right = getString(node.getRight());
                //System.out.println("left: "+left +" Right: "+right);
                return left.equals(right);
            }
            default:
                throw new ArchMagi("Unsupported equality type: " + type);
        }
    }
    private TokenType traverse(Expression expression){
        //System.out.println(expression.getExpressionType());
        switch(expression.getExpressionType()){
            case IntLiteralNode:
                return TokenType.RUNE;
            case StringLiteralNode:
                return TokenType.SPELL;
            case BooleanLiteralNode:
                return TokenType.SIGIL;
            case DoubleLiteralNode:
                return TokenType.GLYPH;
            case IdentifierNode:
                IdentifierNode identifierNode = (IdentifierNode)expression;
                VariableInfo info = variables.get(identifierNode.getIdentifier());
                return info.getType();
            case BinaryOperationNode:
                BinaryOperationNode binaryOperationNode = (BinaryOperationNode) expression;
                TokenType left = traverse(binaryOperationNode.getLeft());
                TokenType right = traverse(binaryOperationNode.getRight());
                TokenType Operator = binaryOperationNode.getOperator();
                return checkTokenType(Operator, left, right);
            case UnaryOperationNode:
                UnaryOperationNode unaryOperationNode = (UnaryOperationNode) expression;
                TokenType token = traverse(unaryOperationNode.getOperand());
                return checkUnary(unaryOperationNode.getOperator(),token);
            default:
                throw new ArchMagi("Invalid expression");
        }
    }
    private String getString(Expression expression){
        switch(expression.getExpressionType()){
            case IntLiteralNode:
                return String.valueOf(((IntLiteralNode) expression).getValue());
            case DoubleLiteralNode:
                return String.valueOf(((DoubleLiteralNode) expression).getValue());
            case StringLiteralNode:
                return ((StringLiteralNode) expression).getValue();
            case BinaryOperationNode:
                BinaryOperationNode binaryOperationNode = (BinaryOperationNode)expression;
                String left =getString(binaryOperationNode.getLeft());
                String right=getString(binaryOperationNode.getRight());
                return left+right;
            case IdentifierNode:{
                IdentifierNode node = (IdentifierNode)expression;
                VariableInfo info = variables.get(node.getIdentifier());
                return info.getKnownValue().toString();
            }
            default:
                throw new ArchMagi("getString error");
                

        }
    }
    private TokenType checkUnary(TokenType Op, TokenType token){
        switch(Op){
            case MINUS:
                if(isNumeric(token))
                     return token;
                throw new ArchMagi("Illegal expression: "+Op+" "+token);
            case NOT:
                if(token==TokenType.SIGIL)
                    return token;
            default:
                throw new ArchMagi("Illegal expression: "+Op+" "+token);
        }
    }
    private TokenType checkTokenType(TokenType Op, TokenType left, TokenType right){
        switch(Op){
            case PLUS:
                if(eitherisString(left,right))
                    return TokenType.SPELL;
            case MINUS:
            case MULTIPLY:
            case DIVIDE:
            case MODULO:
                if(bothNumeric(left,right)) 
                    return NumTable(left, right);
                throw new ArchMagi("Illegal expression: "+left+" "+Op+" "+right);
            case LESS_THAN:
            case LESS_EQUAL:
            case GREATER_THAN:
            case GREATER_EQUAL:
                if(bothNumeric(left,right))
                    return TokenType.SIGIL;
                throw new ArchMagi("Illegal expression: "+left+" "+Op+" "+right);
            case NOT_EQUAL:
            case EQUALS_EQUALS:
                if(left == right||bothNumeric(left, right))
                    return TokenType.SIGIL;    
            case AMPAMP:
            case VERTBARBAR:
                if(bothBoolean(left,right))
                    return TokenType.SIGIL;

            default:
                throw new ArchMagi("Illegal expression: "+left+" "+Op+" "+right);    
        }
    }
    private boolean isNumeric(TokenType token){
        return token == TokenType.RUNE || token == TokenType.GLYPH;
    }
    private boolean eitherisString(TokenType left, TokenType right){
        return left == TokenType.SPELL || right==TokenType.SPELL;     
    }
    private boolean bothNumeric(TokenType left, TokenType right){
        return isNumeric(left)&&isNumeric(right);
    }
    private TokenType NumTable(TokenType left, TokenType right){
        if(left == TokenType.RUNE && right==TokenType.RUNE){
            return TokenType.RUNE;
        }
        return TokenType.GLYPH;
    }
    private boolean bothBoolean(TokenType left, TokenType right){
        return left == TokenType.SIGIL && right == TokenType.SIGIL;
    }
    private Number getValue(Expression expression) {
        switch (expression.getExpressionType()) {
            case IntLiteralNode:
                return ((IntLiteralNode) expression).getValue();

            case DoubleLiteralNode:
                return ((DoubleLiteralNode) expression).getValue();

            case BinaryOperationNode:
                BinaryOperationNode binOp = (BinaryOperationNode) expression;

                Number left = getValue(binOp.getLeft());
                Number right = getValue(binOp.getRight());

                if (left instanceof Long && right instanceof Long) {
                    switch (binOp.getOperator()) {
                        case PLUS:
                            return left.longValue() + right.longValue();
                        case MINUS:
                            return left.longValue() - right.longValue();
                        case MULTIPLY:
                            return left.longValue() * right.longValue();
                        case DIVIDE:
                            return left.longValue() / right.longValue();
                        case MODULO:
                        return left.longValue() % right.longValue();
                        default:
                            throw new ArchMagi("Unknown numeric operator");
                    }
                }

                switch (binOp.getOperator()) {
                    case PLUS:
                        return left.doubleValue() + right.doubleValue();
                    case MINUS:
                        return left.doubleValue() - right.doubleValue();
                    case MULTIPLY:
                        return left.doubleValue() * right.doubleValue();
                    case DIVIDE:
                        return left.doubleValue() / right.doubleValue();
                    case MODULO:
                        return left.doubleValue() % right.doubleValue();
                    default:
                        throw new ArchMagi("Unknown numeric operator");
                }
            case IdentifierNode:{
                IdentifierNode node = (IdentifierNode)expression;
                VariableInfo info = variables.get(node.getIdentifier());
                return (Number)info.getKnownValue();
            }
            default:
                throw new ArchMagi("Unsupported numeric expression: "+ expression.getExpressionType()
            );
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
}