public class AsTable {
	public static int LETTER = 0;
	public static int DIGIT = 1;
    public static int SPACE = 2;
	public static int TK_LESSER = 3;
	public static int TK_GREATER = 4;
	public static int EQUALS = 5;
	public static int QUOTES = 6;
	public static int TK_COMMENT = 7;
	public static int TK_LPARENT = 8;
    public static int TK_RPARENT = 9;
	public static int TK_LBRACKET = 10;
    public static int TK_RBRACKET = 11;
	public static int PLUS = 12;
	public static int MINUS = 13;
	public static int MULTIPLY = 14;
	public static int DIVIDE = 15;
	public static int TK_COMMA = 16;
	public static int TK_DOT=17;
	public static int TK_AMPERSAND=18;
	public static int TK_VERTBAR=19;
	public static int TK_SEMICOLON = 20;
	public static int TK_EXCLAMATION = 21;
	public static int TK_MODULO = 22;
	public static int TK_BACKSLASH = 23;
	public static int TK_SYMBOL = 24;
	public static int TK_ENTER = 25;
    public static int[] types = {
	-1,
	-1,
	-1,
	-1,
	-1,
	-1,
	-1,
	-1,
	-1,
	SPACE,
	TK_ENTER,
	-1,
	-1,
	TK_ENTER,
	-1,
	-1,
	-1,
	-1,
	-1,
	-1,
	-1,
	-1,
	-1,
	-1,
	-1,
	-1,
	-1,
	-1,
	-1,
	-1,
	-1,
	-1,
	SPACE,
	TK_EXCLAMATION,
	QUOTES,
	TK_COMMENT,
	-1,
	TK_MODULO,
	TK_AMPERSAND,
	TK_SYMBOL,
	TK_LPARENT,
	TK_RPARENT,
	MULTIPLY,
	PLUS,
	TK_COMMA,
	MINUS,
	TK_DOT,
	DIVIDE,
	DIGIT,
	DIGIT,
	DIGIT,
	DIGIT,
	DIGIT,
	DIGIT,
	DIGIT,
	DIGIT,
	DIGIT,
	DIGIT,
	TK_SYMBOL,
	TK_SEMICOLON,
	TK_LESSER,
	EQUALS,
	TK_GREATER,
	TK_SYMBOL,
	TK_SYMBOL,
	LETTER,
	LETTER,
	LETTER,
	LETTER,
	LETTER,
	LETTER,
	LETTER,
	LETTER,
	LETTER,
	LETTER,
	LETTER,
	LETTER,
	LETTER,
	LETTER,
	LETTER,
	LETTER,
	LETTER,
	LETTER,
	LETTER,
	LETTER,
	LETTER,
	LETTER,
	LETTER,
	LETTER,
	LETTER,
	LETTER,
	TK_SYMBOL,
	TK_BACKSLASH,
	TK_SYMBOL,
	TK_SYMBOL,
	LETTER,
	TK_SYMBOL,
	LETTER,
	LETTER,
	LETTER,
	LETTER,
	LETTER,
	LETTER,
	LETTER,
	LETTER,
	LETTER,
	LETTER,
	LETTER,
	LETTER,
	LETTER,
	LETTER,
	LETTER,
	LETTER,
	LETTER,
	LETTER,
	LETTER,
	LETTER,
	LETTER,
	LETTER,
	LETTER,
	LETTER,
	LETTER,
	LETTER,
	TK_LBRACKET,
	TK_VERTBAR,
	TK_RBRACKET,
	TK_SYMBOL,
	-1
};
    public int[] getAsTable() {
        return types;
    }
}