import java.util.HashMap;

public class SemanticAnalyzer {
    ProgramNode program;
    HashMap<String, TokenType> symbolTable;

    public SemanticAnalyzer(ProgramNode program) {
        System.out.println("Semantic Analyzer");
        this.program = program;
        for(Statement statement : program.getStatements()) {
                if(statement instanceof Declaration){
                    Declaration declaration = (Declaration) statement;
                    System.out.println(declaration.getIdentifier());
                    System.out.println(declaration.getType());
                }
        }
    }
}
