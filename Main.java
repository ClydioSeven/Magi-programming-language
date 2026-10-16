import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage:java MyProgram <filename>");
            return;
        }
        try {
            String source = Files.readString(Path.of(args[0]), StandardCharsets.UTF_8)
            .replace("\r\n", "\n")
            .replace("\r", "\n");
        try {
            Tokenizer tokenizer = new Tokenizer(source);
            Parser parser = new Parser(tokenizer.getTokens());
            ProgramNode program = parser.parseProgram();
            SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(program);
            Writer writer = new Writer(program.getStatements());
        }catch(ArchMagi e){
            System.out.println(e.getMessage());            
        }
        
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
}
