import java.util.HashMap;
import java.util.List;

public class SemanticAnalyzer {
    ProgramNode program;
    HashMap<String, TokenType> symbolTable = new HashMap<>();

    public SemanticAnalyzer(ProgramNode program) {
        System.out.println("Semantic Analyzer");
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
        if(statement instanceof Declaration){
            Declaration declaration = (Declaration) statement;
            if(symbolTable.containsKey(declaration.getIdentifier())){
                System.out.println("Error: Variable " + declaration.getIdentifier() + " is already declared.");
            }else{
                analyze(declaration.getValue(), declaration.getType());
                symbolTable.put(declaration.getIdentifier(), declaration.getType());
            }
        }
        if(statement instanceof AssignmentNode && !symbolTable.containsKey(((AssignmentNode) statement).getIdentifier())) {
            AssignmentNode assignment = (AssignmentNode)statement;
            System.out.println("Error: Variable " + assignment.getIdentifier() + " is not declared.");
        }
        if(statement instanceof ForNode){
            ForNode forNode = (ForNode) statement;
            analyze(forNode.getInitialization());
            analyze(forNode.getCondition(),TokenType.SIGIL);
            analyze(forNode.getIncrement());
            analyzeList(forNode.getBody());
        }
        if(statement instanceof WhileNode){
            WhileNode whileNode = (WhileNode) statement;
            analyze(whileNode.getCondition(),TokenType.SIGIL);
            analyzeList(whileNode.getBody());
        }
        if(statement instanceof IfNode){
            IfNode ifNode = (IfNode) statement;
            analyze(ifNode.getCondition(),TokenType.SIGIL);
            analyzeList(ifNode.getThenBranch());
            analyzeList(ifNode.getElseBranch());
        }
        if(statement instanceof PrintNode){
            PrintNode printNode = (PrintNode) statement;
            analyze(printNode.getExpression(),TokenType.CAST);
        }
        
    }
    private void analyze(Expression expression, TokenType Token){
        switch(Token){
            case RUNE:
            traverse(expression);
                break;
            case GLYPH:
                traverse(expression);
                break;
            case SIGIL:
                traverse(expression);
                break;
            case SPELL:
                traverse(expression);
                break;
            case CAST:
                traverse(expression);
                break;
            
        }

    }
    private void traverse(Expression expression){
        switch(expression.getClass().getSimpleName()){
            case "IntLiteralNode":
                IntLiteralNode intLiteralNode = (IntLiteralNode) expression;
                System.out.println("IntLiteralNode: " + intLiteralNode.getValue());
                break;
            case "StringLiteralNode":
                StringLiteralNode stringLiteralNode = (StringLiteralNode) expression;
                System.out.println("StringLiteralNode: " + stringLiteralNode.getValue());
                break;
            case "BooleanLiteralNode":
                BooleanLiteralNode booleanLiteralNode = (BooleanLiteralNode) expression;
                System.out.println("BooleanLiteralNode: " + booleanLiteralNode.getValue());
                break;
            case "DoubleLiteralNode":
                DoubleLiteralNode doubleLiteralNode = (DoubleLiteralNode) expression;
                System.out.println("DoubleLiteralNode: " + doubleLiteralNode.getValue());
                break;
            case "IdentifierNode":
                IdentifierNode identifierNode = (IdentifierNode) expression;
                System.out.println("IdentifierNode: " + identifierNode.getIdentifier());
                break;
            case "BinaryOperationNode":
                BinaryOperationNode binaryExpressionNode = (BinaryOperationNode) expression;
                System.out.println("BinaryExpressionNode: " + binaryExpressionNode.getOperator());
                traverse(binaryExpressionNode.getRight());
                traverse(binaryExpressionNode.getLeft());
                
                break;
            case "UnaryExpressionNode":
                UnaryOperationNode unaryExpressionNode = (UnaryOperationNode) expression;
                System.out.println("UnaryExpressionNode: " + unaryExpressionNode.getOperator());
                traverse(unaryExpressionNode.getOperand());
                break;
            default:
                System.out.println("Unknown expression type: " + expression.getClass().getSimpleName());
                break;
        }
    }

}
