import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Tokenizer {

    List<Token> tokens = new ArrayList<>();

    int state = 0;
    int delta = 0;
    int l = 0;
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

        int ch;
        int[] Ascii = asTable.getAsTable();

        while (current < source.length()) {

            ch = source.charAt(current);

            delta = DFA[state][Ascii[ch]];

            if (delta > 0) {
                state = delta;
            }
            else if (delta < 0) {

                tokens.add(new Token(
                        this.getTokenType(delta),
                        source.substring(l, current)
                ));

                state = 0;
                l = current;
            }

            current++;
        }

        System.out.println(tokens);
    }

    public TokenType getTokenType(int delta) {

        switch (delta) {

            case -1: return TokenType.IDENTIFIER;
            case -2: return TokenType.INTEGER;
            case -3: return TokenType.DOUBLE;
            case -4: return TokenType.LESS_THAN;
            case -5: return TokenType.SEMICOLON;
            case -6: return TokenType.LESS_EQUAL;
            case -7: return TokenType.GREATER_THAN;
            case -8: return TokenType.GREATER_EQUAL;
            case -9: return TokenType.EQUALS;
            case -10: return TokenType.EQUALS_EQUALS;
            case -11: return TokenType.STRING;
            case -12: return TokenType.LEFT_PAREN;
            case -13: return TokenType.RIGHT_PAREN;
            case -14: return TokenType.LEFT_BRACE;
            case -15: return TokenType.RIGHT_BRACE;
            case -16: return TokenType.PLUS;
            case -17: return TokenType.MINUS;
            case -18: return TokenType.DOT;
            case -19: return TokenType.AMPERSAND;
            case -20: return TokenType.AMPAMP;
            case -21: return TokenType.VERTBAR;
            case -22: return TokenType.VERTBARBAR;
            case -23: return TokenType.NOT;
            case -24: return TokenType.MULTIPLY;
            case -25: return TokenType.COMMA;
            case -26: return TokenType.DIVIDE;
            case -27: return TokenType.NOT_EQUAL;
            case -28: return TokenType.MINUS_MINUS;
            case -29: return TokenType.PLUS_PLUS;
            case -30: return TokenType.MODULO;

            case -99:
                throw new RuntimeException("Invalid character");

            default:
                throw new RuntimeException("Unknown delta: " + delta);
        }
    }
}