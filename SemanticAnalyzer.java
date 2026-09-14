import java.util.HashMap;
import java.util.List;

public class SemanticAnalyzer {
    ProgramNode program;
    HashMap<String, TokenType> symbolTable = new HashMap<>();

    public SemanticAnalyzer(ProgramNode program) {
        boolean flag=true;
        System.out.println("Semantic Analyzer");
        this.program = program;
        for(Statement statement : program.getStatements()) {
            flag = analyze(statement);
            if(!flag){
                break;
            }

        }
    }

    private boolean analyzeList(List<Statement> statements) {
        boolean flag=true;
        for(Statement statement : statements) {
            analyze(statement);
            if(!flag) return false;
        }
        return true;
    }
    private boolean analyze(Statement statement) {
        switch(statement.getStatementType()){
            case AssignmentNode:
                AssignmentNode assignment = (AssignmentNode)statement;
                return symbolTable.containsKey(assignment.getIdentifier()) && analyze(assignment.getValue(),symbolTable.get(assignment.getIdentifier()));
            case BooleanCreateNode:
            case DoubleCreateNode:
            case IntCreateNode:
            case StringCreateNode:
                 Declaration declaration = (Declaration)statement;
                return analyze(declaration.getValue(),declaration.getType());
            case ForNode:
                ForNode forNode = (ForNode) statement;
                return analyze(forNode.getInitialization())  && 
                analyze(forNode.getCondition(),TokenType.SIGIL) &&
                analyze(forNode.getIncrement()) &&
                analyzeList(forNode.getBody());
            case IfNode:
                IfNode ifNode = (IfNode) statement;
                return analyze(ifNode.getCondition(),TokenType.SIGIL) &&
                analyzeList(ifNode.getThenBranch()) &&
                analyzeList(ifNode.getElseBranch());
            case PrintNode:
                PrintNode printNode = (PrintNode) statement;
                return analyze(printNode.getExpression(),TokenType.CAST);
            case WhileNode:
                WhileNode whileNode = (WhileNode) statement;
                return analyze(whileNode.getCondition(),TokenType.SIGIL) &&
                analyzeList(whileNode.getBody());
            default:
                return false;
            
        }
        
    }

    private boolean analyze(Expression expression, TokenType Token){
        switch(Token){
            case RUNE:
            case GLYPH:
                return traverseNum(expression);
            case SIGIL:
                return traverseSigil(expression);
            case SPELL:
                return traverseSpell(expression);
            case CAST:
                return traverseCast(expression);
            default:
                return false;
        }

    }
    private void traverse(Expression expression){
        switch(expression.getExpressionType()){
            case IntLiteralNode:
                IntLiteralNode intLiteralNode = (IntLiteralNode) expression;
                System.out.println("IntLiteralNode: " + intLiteralNode.getValue());
                break;
            case StringLiteralNode:
                StringLiteralNode stringLiteralNode = (StringLiteralNode) expression;
                System.out.println("StringLiteralNode: " + stringLiteralNode.getValue());
                break;
            case BooleanLiteralNode:
                BooleanLiteralNode booleanLiteralNode = (BooleanLiteralNode) expression;
                System.out.println("BooleanLiteralNode: " + booleanLiteralNode.getValue());
                break;
            case DoubleLiteralNode:
                DoubleLiteralNode doubleLiteralNode = (DoubleLiteralNode) expression;
                System.out.println("DoubleLiteralNode: " + doubleLiteralNode.getValue());
                break;
            case IdentifierNode:
                IdentifierNode identifierNode = (IdentifierNode) expression;
                if(symbolTable.containsKey(identifierNode.getIdentifier())){
                    System.out.println("Valid IdentifierNode: " + identifierNode.getIdentifier());
                }else{
                    System.out.println("Invalid IdentifierNode: " + identifierNode.getIdentifier());
                }
                
                break;
            case BinaryOperationNode:
                BinaryOperationNode binaryOperationNode = (BinaryOperationNode) expression;
                System.out.println("BinaryExpressionNode: " + binaryOperationNode.getOperator());
                traverse(binaryOperationNode.getRight());
                traverse(binaryOperationNode.getLeft());
                
                break;
            case UnaryOperationNode:
                UnaryOperationNode unaryOperationNode = (UnaryOperationNode) expression;
                System.out.println("UnaryExpressionNode: " + unaryOperationNode.getOperator());
                traverse(unaryOperationNode.getOperand());
                break;
            default:
                System.out.println("Unknown expression type: " + expression.getClass().getSimpleName());
                break;
        }
    }
    private boolean traverseNum(Expression expression){
        switch(expression.getExpressionType()){
            case StringLiteralNode:
            case BooleanLiteralNode:
            case UnaryOperationNode:
                return false;
            case IdentifierNode:
                IdentifierNode identifierNode = (IdentifierNode) expression;
                if(symbolTable.containsKey(identifierNode.getIdentifier())){
                    TokenType type = symbolTable.get(identifierNode.getIdentifier());
                    if(type == TokenType.RUNE || type ==TokenType.GLYPH)return true;
                }
                return false;
            case BinaryOperationNode:
                BinaryOperationNode binaryExpressionNode = (BinaryOperationNode) expression;
                System.out.println("BinaryExpressionNode: " + binaryExpressionNode.getOperator());
                return traverseNum(binaryExpressionNode.getRight()) && traverseNum(binaryExpressionNode.getLeft());
            default:
                return true;
        }
    }
    private boolean traverseSigil(Expression expression){
        switch(expression.getExpressionType()){
            case BinaryOperationNode:
                BinaryOperationNode binaryExpressionNode = (BinaryOperationNode) expression;
                System.out.println("BinaryExpressionNode: " + binaryExpressionNode.getOperator());
                return traverseSigil(binaryExpressionNode.getRight()) && traverseNum(binaryExpressionNode.getLeft());
            default:
                return true;
        }
    }
    private boolean traverseSpell(Expression expression){
        switch(expression.getExpressionType()){
            default:
            return true;
        }
    }
    private boolean traverseCast(Expression expression){
        switch(expression.getExpressionType()){
            default:
            return true;
        }
    }


}
