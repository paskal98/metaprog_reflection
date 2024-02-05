package sk.tuke.meta.persistence.utils.sqlgenerator;

public enum TokenType {

    KEY,
    MANY_TO_ONE,
    ONE_TO_MANY,
    MANY_TO_MANY,
    ONE_TO_ONE,
    VARCHAR,
    INT,
    BIGINT,
    DATE,
    ANNOTATION,
    TYPE,
    NAME,
    DATA_VALUE,
    LEFT_BRACE,
    RIGHT_BRACE,
    OTHER, EQUAL, EOF
}
