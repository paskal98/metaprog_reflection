package sk.tuke.meta.persistence.utils.sqlgenerator;

import java.util.*;

public class Parser {

    private Map<String, TokenType> dataTypes = Map.of(
            "String",TokenType.VARCHAR,
            "int",TokenType.INT,
            "long",TokenType.BIGINT
    );
    private Map<String, TokenType> annotationTypes= Map.of(
            "Id",TokenType.KEY,
            "ManyToOne",TokenType.MANY_TO_ONE
    );
    private Map<String, Map<String, String>> classesToDSL;
    private final Lexer lexer;
    private Token symbol;
    private SQLQueryDefinition definition;

    public Parser(Lexer lexer, Map<String, Map<String, String>> classesToDSL) {
        this.lexer = lexer;
        this.classesToDSL = classesToDSL;
    }
    public SQLQueryDefinition  sqlQueryDefinition(){
        definition = new SQLQueryDefinition();
        Set<TokenType> tokens = Set.of(
                TokenType.ANNOTATION,
                TokenType.TYPE,
                TokenType.NAME
        );

        consume();

        while (tokens.contains(symbol.tokenType())) {
            switch (symbol.tokenType()) {
                case ANNOTATION -> annotations();
                case TYPE -> type();
                case NAME -> name();
            }
        }

        match(TokenType.EOF);
        return definition;
    }
    private void name() {
        match(TokenType.NAME);
        match(TokenType.LEFT_BRACE);
        while (symbol.getType() == TokenType.DATA_VALUE) {
            String name = (String) symbol.getValue();
            match(TokenType.DATA_VALUE);
            definition.addName(name);
        }
        match(TokenType.RIGHT_BRACE);
    }
    private void type() {
        match(TokenType.TYPE);
        match(TokenType.LEFT_BRACE);
        while (symbol.getType() == TokenType.DATA_VALUE) {
            String name = (String) symbol.getValue();
            match(TokenType.DATA_VALUE);
            definition.addType(fitDataType(name));
        }
        match(TokenType.RIGHT_BRACE);
    }
    private void annotations() {
        match(TokenType.ANNOTATION);
        match(TokenType.LEFT_BRACE);
        while (symbol.getType() == TokenType.DATA_VALUE) {
            String name = (String) symbol.getValue();
            match(TokenType.DATA_VALUE);
            definition.addAnnotation(fitAnnotation(name));
        }
        match(TokenType.RIGHT_BRACE);

    }
    private void match(TokenType expectedSymbol) {
        if (symbol.getType() == expectedSymbol) {
            consume();
        } else {
            throw new RuntimeException("Unexpected token: "+symbol.getType()+", expected: "+expectedSymbol);
        }
    }
    private void consume() {
        symbol = lexer.nextToken();
    }
    private String fitDataType(String name){
        TokenType tokenType = dataTypes.get(name);

        if (tokenType==null){

            String classFieldsUnparsed = classesToDSL.get(name).toString();
            classFieldsUnparsed = classFieldsUnparsed.substring(1, classFieldsUnparsed.length()-1);
            String[] classFields = classFieldsUnparsed.split(",");

            String typeField="undefined";
            for(String classField: classFields){
                Lexer lexerFiled = new Lexer(classField);

                // go to nameOfField
                while (lexerFiled.nextToken().getType()!=TokenType.TYPE);

                // get name of field
                while (true){
                    Token token = lexerFiled.nextToken();
                    if(token.getType()==TokenType.RIGHT_BRACE) break;
                    else if (token.getType()==TokenType.DATA_VALUE){
                        typeField=token.getValue().toString();
                    }
                }

                //is filed has Id annotation
                boolean isFieldId = false;
                while (true) {
                    Token token = lexerFiled.nextToken();
                    if(token.getType()==TokenType.RIGHT_BRACE) break;
                    else if (token.getType()==TokenType.DATA_VALUE && annotationTypes.get(token.getValue().toString())==TokenType.KEY)
                        isFieldId = true;
                }

                if(isFieldId) {
                    definition.addOldType(name);
                    return dataTypes.get(typeField).toString();
                }
            }

            return name;
        }


        return tokenType.name();
    }
    private String fitAnnotation(String annotation){
        TokenType tokenType = annotationTypes.get(annotation);

        if (tokenType==null)
            return annotation;
        else if(tokenType==TokenType.KEY)
            return "PRIMARY "+tokenType.name()+" ";
        else if(tokenType==TokenType.MANY_TO_ONE){

            String classFieldsUnparsed = classesToDSL.get(definition.getOldType()).toString();
            classFieldsUnparsed = classFieldsUnparsed.substring(1, classFieldsUnparsed.length()-1);
            String[] classFields = classFieldsUnparsed.split(",");

            String nameField="undefined";
            for(String classField: classFields){
                Lexer lexerFiled = new Lexer(classField);

                // go to nameOfField
                while (lexerFiled.nextToken().getType()!=TokenType.NAME);

                // get name of field
                while (true){
                    Token token = lexerFiled.nextToken();
                    if(token.getType()==TokenType.RIGHT_BRACE) break;
                    else if (token.getType()==TokenType.DATA_VALUE){
                        nameField=token.getValue().toString();
                    }
                }

                // go to annotationOfField
                while (lexerFiled.nextToken().getType()!=TokenType.ANNOTATION);

                //return if reference is foreign key and properly set query
                while (true) {
                    Token token = lexerFiled.nextToken();
                    if(token.getType()==TokenType.RIGHT_BRACE) break;
                    else if (token.getType()==TokenType.DATA_VALUE && annotationTypes.get(token.getValue().toString())==TokenType.KEY) {
                        definition.addName(definition.getName()+"_id");
                        return ",\nFOREIGN KEY (" + definition.getName().toLowerCase() + ") " + "REFERENCES " + definition.getOldType().toLowerCase() + "(" + nameField + ") ";
                    }
                }

            }
        }

        return tokenType.name();
    }

    private boolean matchInternal(TokenType token,TokenType expected){
        return token == expected;
    }


}
