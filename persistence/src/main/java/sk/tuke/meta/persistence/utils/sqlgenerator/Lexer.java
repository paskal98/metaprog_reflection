package sk.tuke.meta.persistence.utils.sqlgenerator;

import java.util.Map;

public class Lexer {

    private static final Map<String, TokenType> keywords = Map.of(
            "annotationsOfField", TokenType.ANNOTATION,
            "typeOfField", TokenType.TYPE,
            "nameOfField", TokenType.NAME
//            "Id", TokenType.ID,
//            "ManyToOne", TokenType.MANY_TO_ONE,
//            "OneToMany", TokenType.ONE_TO_MANY,
//            "ManyToMany", TokenType.MANY_TO_MANY,
//            "OneToOne", TokenType.ONE_TO_ONE
    );


    private final String input;
    private int currentChar;


    public Lexer(String input) {
        this.input = input;
        this.currentChar =-1;
    }

    public Token nextToken() {
        try{

            while (true){
                consume();

                if(currentChar>=(input.length()))
                    return new Token(TokenType.EOF);

                if(Character.isWhitespace(input.charAt(currentChar))) continue;

                if (input.charAt(currentChar) == '{') {
                    return new Token(TokenType.LEFT_BRACE);
                }

                if (input.charAt(currentChar) == '}') {
                    return new Token(TokenType.RIGHT_BRACE);
                }

                if (input.charAt(currentChar) == '=') {
                    return new Token(TokenType.EQUAL);
                }

                if (Character.isLetter(input.charAt(currentChar))) {
                    return readName();
                }


            }
        }catch (Exception e){
            throw  new RuntimeException(e);
        }
    }

    private Token readName() {
        StringBuilder sb = new StringBuilder();
        do {
            sb.append(input.charAt(currentChar));
            consume();
        } while (Character.isLetterOrDigit(input.charAt(currentChar)));

        String name = sb.toString();

        TokenType tokenType = keywords.get(name);

        if(tokenType==null) return new Token(TokenType.DATA_VALUE,name);

        return new Token(tokenType,name);
    }

    private void consume(){
        currentChar++;
    }
}
