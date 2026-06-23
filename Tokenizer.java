import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Tokenizer {

     public static final Set<String> KEYWORDS =
        new HashSet<>(Arrays.asList(
            "Rune", //int
            "Chant", //while
            "Glyph", //double
            "Cast", // print
            "Ward", //if
            "else",
            "Spell", //String
            "Channel", //for
            "Sigil", //boolean
            "True",
            "False"
        ));

    List<Token> tokens = new ArrayList<>();

    int state = 0;
    int delta = 0;

    String source;

    // l = lexeme start
    int l = 0;

    // current = current character
    int current = 0;

    AsTable asTable = new AsTable();

    int[][] DFA = {

        {1, 2, 0, 4, 5, 6, 7, 8, -12, -13, -14, -15, 9, 10, -24, -26, -25, 11, 12, 13, -5, 14, -30, -99, -99, 0},

        {1, 1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -99, -1},

        {-2, 2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, 3, -2, -2, -2, -2, -2, -2, -99, -2},

        {-3, 3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -99, -3},

        {-4, -4, -4, -4, -4, -6, -4, -4, -4, -4, -4, -4, -4, -4, -4, -4, -4, -4, -4, -4, -4, -4, -4, -4, -99, -4},

        {-7, -7, -7, -7, -7, -8, -7, -7, -7, -7, -7, -7, -7, -7, -7, -7, -7, -7, -7, -7, -7, -7, -7, -7, -99, -7},

        {-9, -9, -9, -9, -9, -10, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -99, -9},

        {7, 7, 7, 7, 7, 7, -11, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 15, 7, -99},

        {8, 8, 8, 8, 8, 8, 8, 0, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, -99, 0},

        {-16, -16, -16, -16, -16, -16, -16, -16, -16, -16, -16, -16, -29, -16, -16, -16, -16, -16, -16, -16, -16, -16, -16, -16, -99, -16},

        {-17, -17, -17, -17, -17, -17, -17, -17, -17, -17, -17, -17, -17, -28, -17, -17, -17, -17, -17, -17, -17, -17, -17, -17, -99, -17},

        {-18, 3, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -99, -18},

        {-19, -19, -19, -19, -19, -19, -19, -19, -19, -19, -19, -19, -19, -19, -19, -19, -19, -19, -20, -19, -19, -19, -19, -19, -99, -19},

        {-21, -21, -21, -21, -21, -21, -21, -21, -21, -21, -21, -21, -21, -21, -21, -21, -21, -21, -21, -22, -21, -21, -21, -21, -99, -21},

        {-23, -23, -23, -23, -23, -27, -23, -23, -23, -23, -23, -23, -23, -23, -23, -23, -23, -23, -23, -23, -23, -23, -23, -23, -99, -23},

        {7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7}
    };

    public Tokenizer(String source) throws IOException {
            this.source = source;
            int[] Ascii = asTable.getAsTable();
            while (current < source.length()) {
                char ch = source.charAt(current);
                delta = DFA[state][Ascii[ch]];

        if(state == 0 && delta == 0) {

            current++;
            l = current;
            continue;
        }

        if(delta >= 0) {
            state = delta;
            current++;
        }

        else {
            String lexeme;
            if(needsRetract(delta)) {
                lexeme = source.substring(l, current);
            }
            else {
                lexeme = source.substring(l, current + 1);
                current++;
            }

            Token token = TokenAdder(delta, lexeme);

            if(token != null) {
                tokens.add(token);
            }

            state = 0;
            l = current;
        }
    }       
            if (state == 1) {
                tokens.add(TokenAdder(-1, source.substring(l, current)));
            }
            else if (state == 2) {
                tokens.add(TokenAdder(-2, source.substring(l, current)));
            }
            else if (state == 3) {
                tokens.add(TokenAdder(-3, source.substring(l, current)));
            }

            System.out.println(tokens);
    }

    private boolean needsRetract(int delta) {

        switch(delta) {

            case -1:
            case -2:
            case -3:
            case -4:
            case -7:
            case -9:
            case -16:
            case -17:
            case -18:
            case -19:
            case -21:
            case -23:
                return true;
        }

        return false;
    }

    private Token TokenAdder(int delta, String lexeme) {

        switch(delta) {

    case -1:
        if(KEYWORDS.contains(lexeme)) {
            return new Token(TokenType.KEYWORD, lexeme);
        }
        return new Token(TokenType.IDENTIFIER, lexeme);

    case -2:
        return new Token(TokenType.INTEGER, lexeme);

    case -3:
        return new Token(TokenType.DOUBLE, lexeme);

    case -4:
        return new Token(TokenType.LESS_THAN, lexeme);

    case -5:
        return new Token(TokenType.SEMICOLON, lexeme);

    case -6:
        return new Token(TokenType.LESS_EQUAL, lexeme);

    case -7:
        return new Token(TokenType.GREATER_THAN, lexeme);

    case -8:
        return new Token(TokenType.GREATER_EQUAL, lexeme);

    case -9:
        return new Token(TokenType.EQUALS, lexeme);

    case -10:
        return new Token(TokenType.EQUALS_EQUALS, lexeme);

    case -11:
        return new Token(TokenType.STRING, lexeme);

    case -12:
        return new Token(TokenType.LEFT_PAREN, lexeme);

    case -13:
        return new Token(TokenType.RIGHT_PAREN, lexeme);

    case -14:
        return new Token(TokenType.LEFT_BRACE, lexeme);

    case -15:
        return new Token(TokenType.RIGHT_BRACE, lexeme);

    case -16:
        return new Token(TokenType.PLUS, lexeme);

    case -17:
        return new Token(TokenType.MINUS, lexeme);

    case -18:
        return new Token(TokenType.DOT, lexeme);

    case -19:
        return new Token(TokenType.AMPERSAND, lexeme);

    case -20:
        return new Token(TokenType.AMPAMP, lexeme);

    case -21:
        return new Token(TokenType.VERTBAR, lexeme);

    case -22:
        return new Token(TokenType.VERTBARBAR, lexeme);

    case -23:
        return new Token(TokenType.NOT, lexeme);

    case -24:
        return new Token(TokenType.MULTIPLY, lexeme);

    case -25:
        return new Token(TokenType.COMMA, lexeme);

    case -26:
        return new Token(TokenType.DIVIDE, lexeme);

    case -27:
        return new Token(TokenType.NOT_EQUAL, lexeme);

    case -28:
        return new Token(TokenType.MINUS_MINUS, lexeme);

    case -29:
        return new Token(TokenType.PLUS_PLUS, lexeme);

    case -30:
        return new Token(TokenType.MODULO, lexeme);

    case -99:
        throw new RuntimeException("Invalid character: " + lexeme);
}

        return null;
    }
}