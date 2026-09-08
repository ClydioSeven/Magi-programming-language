import java.util.HashMap;

public class SemanticAnalyzer {
    ProgramNode program;
    HashMap<String, TokenType> symbolTable = new HashMap<>();

    public SemanticAnalyzer(ProgramNode program) {
        System.out.println("Semantic Analyzer");
        this.program = program;
        for(Statement statement : program.getStatements()) {
                if(statement instanceof Declaration){
                    Declaration declaration = (Declaration) statement;
                    if(symbolTable.containsKey(declaration.getIdentifier())){
                        System.out.println("Error: Variable " + declaration.getIdentifier() + " is already declared.");
                    }else{
                        symbolTable.put(declaration.getIdentifier(), declaration.getType());
                    }
                }
                if(statement instanceof AssignmentNode && !symbolTable.containsKey(((AssignmentNode) statement).getIdentifier())) {
                    AssignmentNode assignment = (AssignmentNode)statement;
                    System.out.println("Error: Variable " + assignment.getIdentifier() + " is not declared.");
                }
                
        }
    }
}
