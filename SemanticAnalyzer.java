import java.util.HashMap;

public class SemanticAnalyzer {
    ProgramNode program;
    HashMap<String, TokenType> symbolTable;

    public SemanticAnalyzer(ProgramNode program) {
        this.program = program;
        for(Statement statement : program.getStatements()) {
                if(statement instanceof Helper) {
                    Helper helper = (Helper) statement;
                    System.out.println(helper.getIdentifier());
                }
        }
    }
}
