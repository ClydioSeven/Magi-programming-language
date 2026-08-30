import java.util.List;

public class Parser {
    private final List<Token> tokens;
    private int current=0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }
    private Token peek(){
        return tokens.get(current);
    }
    private Token peek(int distance){
        return tokens.get(current + distance);
    }
}
