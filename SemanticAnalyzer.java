import java.util.HashMap;
import java.util.List;

public class SemanticAnalyzer {
    ProgramNode program;
    HashMap<String, TokenType> symbolTable = new HashMap<>();

    public SemanticAnalyzer(ProgramNode program) {
        this.program = program;
        for(Statement statement : program.getStatements()) {
            analyze(statement);
        }
    }

    private void analyzeList(List<Statement> statements) {
        for(Statement statement : statements) {
            analyze(statement);
        }
    }
    private void analyze(Statement statement) {
        switch(statement.getStatementType()){
            case AssignmentNode:
                AssignmentNode assignment = (AssignmentNode)statement;
                if(declared(assignment.getIdentifier())){
                    analyze(assignment.getValue(),symbolTable.get(assignment.getIdentifier()));
                }
                else{
                    throw new ArchMagi("Variable: "+assignment.getIdentifier()+" not declared");
                }
            case BooleanCreateNode:
            case DoubleCreateNode:
            case IntCreateNode:
            case StringCreateNode:
                Declaration declaration = (Declaration)statement;
                analyze(declaration.getValue(),declaration.getType());
            case ForNode:
                ForNode forNode = (ForNode) statement;
                analyze(forNode.getInitialization()) ;
                analyze(forNode.getCondition(),TokenType.SIGIL) ;
                analyze(forNode.getIncrement()) ;
                analyzeList(forNode.getBody());
            case IfNode:
                IfNode ifNode = (IfNode) statement;
                analyze(ifNode.getCondition(),TokenType.SIGIL);
                analyzeList(ifNode.getThenBranch()) ;
                analyzeList(ifNode.getElseBranch());
            case PrintNode:
                PrintNode printNode = (PrintNode) statement;
                analyze(printNode.getExpression(),TokenType.CAST);
            case WhileNode:
                WhileNode whileNode = (WhileNode) statement;
                analyze(whileNode.getCondition(),TokenType.SIGIL);
                analyzeList(whileNode.getBody());
            default:
                System.out.println("Unknown error");
        }
    }
    private boolean declared(String string){
        if(symbolTable.containsKey(string))
            return true;
        return false;
    }

    private boolean analyze(Expression expression, TokenType Token){
        switch(Token){
            case RUNE:
            case GLYPH:
                return traverse(expression) == TokenType.RUNE || traverse(expression) == TokenType.GLYPH;
            default:
                return traverse(expression) == Token ;
        }

    }
    private TokenType traverse(Expression expression){
        switch(expression.getExpressionType()){
            case IntLiteralNode:
                IntLiteralNode intLiteralNode = (IntLiteralNode) expression;
                System.out.println("IntLiteralNode: " + intLiteralNode.getValue());
                return TokenType.RUNE;
            case StringLiteralNode:
                StringLiteralNode stringLiteralNode = (StringLiteralNode) expression;
                System.out.println("StringLiteralNode: " + stringLiteralNode.getValue());
                return TokenType.RUNE;
            case BooleanLiteralNode:
                BooleanLiteralNode booleanLiteralNode = (BooleanLiteralNode) expression;
                System.out.println("BooleanLiteralNode: " + booleanLiteralNode.getValue());
                return TokenType.RUNE;
            case DoubleLiteralNode:
                DoubleLiteralNode doubleLiteralNode = (DoubleLiteralNode) expression;
                System.out.println("DoubleLiteralNode: " + doubleLiteralNode.getValue());
                return TokenType.RUNE;
            case IdentifierNode:
                IdentifierNode identifierNode = (IdentifierNode) expression;
                if(symbolTable.containsKey(identifierNode.getIdentifier())){
                    System.out.println("Valid IdentifierNode: " + identifierNode.getIdentifier());
                }else{
                    System.out.println("Invalid IdentifierNode: " + identifierNode.getIdentifier());
                }
                
                return TokenType.RUNE;
            case BinaryOperationNode:
                BinaryOperationNode binaryOperationNode = (BinaryOperationNode) expression;
                System.out.println("BinaryExpressionNode: " + binaryOperationNode.getOperator());
                traverse(binaryOperationNode.getRight());
                traverse(binaryOperationNode.getLeft());
                
                return TokenType.RUNE;
            case UnaryOperationNode:
                UnaryOperationNode unaryOperationNode = (UnaryOperationNode) expression;
                System.out.println("UnaryExpressionNode: " + unaryOperationNode.getOperator());
                traverse(unaryOperationNode.getOperand());
                return TokenType.RUNE;
            default:
                System.out.println("Unknown expression type: " + expression.getClass().getSimpleName());
                return TokenType.RUNE;
        }
    }
    


}
