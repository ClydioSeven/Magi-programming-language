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
                    break;
                }
                throw new ArchMagi("Variable: "+assignment.getIdentifier()+" not declared");
            case BooleanCreateNode:
            case DoubleCreateNode:
            case IntCreateNode:
            case StringCreateNode:
                Declaration declaration = (Declaration)statement;
                if(declared(declaration.getIdentifier()))
                    throw new ArchMagi("Variable: "+declaration.getIdentifier()+" already declared.");
                analyze(declaration.getValue(),declaration.getType());
                symbolTable.put(declaration.getIdentifier(),declaration.getType());
                break;
            case ForNode:
                ForNode forNode = (ForNode) statement;
                analyze(forNode.getInitialization());
                analyze(forNode.getCondition(),TokenType.SIGIL) ;
                analyze(forNode.getIncrement()) ;
                analyzeList(forNode.getBody());
                symbolTable.remove(analyzeFor(forNode.getInitialization()));
                break;
            case IfNode:
                IfNode ifNode = (IfNode) statement;
                analyze(ifNode.getCondition(),TokenType.SIGIL);
                analyzeList(ifNode.getThenBranch()) ;
                analyzeList(ifNode.getElseBranch());
                break;
            case PrintNode:
                PrintNode printNode = (PrintNode) statement;
                traverse(printNode.getExpression());
                break;
            case WhileNode:
                WhileNode whileNode = (WhileNode) statement;
                analyze(whileNode.getCondition(),TokenType.SIGIL);
                analyzeList(whileNode.getBody());
                break;
            default:
                throw new ArchMagi("Unknown error");
        }
    }
    private String analyzeFor(Statement statement){
        Declaration declaration = (Declaration)statement;
        return declaration.getIdentifier();
    }
    private boolean declared(String string){
        return symbolTable.containsKey(string);
    }

    private void analyze(Expression expression, TokenType expected){
        TokenType real = traverse(expression);
        if(real !=expected){
            if(!bothNumeric(real,expected))
                throw new ArchMagi("Expected: "+expected+" received: "+real);
        }
    }
    private TokenType traverse(Expression expression){
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
                IdentifierNode identifierNode = (IdentifierNode) expression;
                if(!declared(identifierNode.getIdentifier())){
                    throw new ArchMagi("Identifier: "+identifierNode.getIdentifier()+ " not declared.");
                }
                return symbolTable.get(identifierNode.getIdentifier());
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
    private boolean eitherisString(TokenType left, TokenType right){
        return left == TokenType.SPELL || right==TokenType.SPELL;     
    }
    private boolean bothBoolean(TokenType left, TokenType right){
        return left == TokenType.SIGIL && right == TokenType.SIGIL;
    }
    private boolean bothNumeric(TokenType left, TokenType right){
        return isNumeric(left)&&isNumeric(right);
    }
    private boolean isNumeric(TokenType token){
        return token == TokenType.RUNE || token == TokenType.GLYPH;
    }
    private TokenType NumTable(TokenType left, TokenType right){
        if(left == TokenType.RUNE && right==TokenType.RUNE){
            return TokenType.RUNE;
        }
        return TokenType.GLYPH;
    }
    
}

