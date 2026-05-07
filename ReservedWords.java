import java.util.HashMap;

public class ReservedWords {
    HashMap<String, Integer> reservedWords;
    HashMap<Character, String> separatorWords;
    public ReservedWords() {
        reservedWords = new HashMap<>();
        reservedWords.put("Rune", 0); //int
        reservedWords.put("Glyph", 1); //double
        reservedWords.put("Chant", 2); // while
        reservedWords.put("Cast", 3); //print
        reservedWords.put("else", 4); 
        reservedWords.put("if", 5);
        reservedWords.put("Spell", 6); //string

        separatorWords = new HashMap<>();
        separatorWords.put(';', "SEMICOLON");
        separatorWords.put(',', "COMMA");
        separatorWords.put('(', "LPAREN");
        separatorWords.put(')', "RPAREN");
        separatorWords.put('{', "LBRACE");
        separatorWords.put('}', "RBRACE");
    }
    public boolean isReservedWord(String word) {
        return reservedWords.containsKey(word);
    }
    public int get(String word) {
        return reservedWords.get(word);
    }
    public String getSeparatorWord(char ch) {
        return separatorWords.get(ch);
    }
}
