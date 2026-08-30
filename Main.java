import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

//javac Main.java AsTable.java Tokenizer.java Token.java TokenType.java 
public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage:java MyProgram <filename>");
            return;
        }
        
        try (InputStream in = new FileInputStream(args[0])) {
                String source = Files.readString(Path.of(args[0]), StandardCharsets.UTF_8)
        .replace("\r\n", "\n")
        .replace("\r", "\n");

        Tokenizer tokenizer = new Tokenizer(source);
        Parser parser = new Parser(tokenizer.getTokens());
} catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
}
