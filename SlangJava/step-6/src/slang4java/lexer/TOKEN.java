package slang4java.lexer;


public enum TOKEN {
    ILLEGAL_TOKEN,
    TOK_PLUS,
    TOK_MUL,
    TOK_DIV,
    TOK_SUB,
    TOK_OPREN,
    TOK_CPREN,
    TOK_NULL,

    TOK_PRINT,
    TOK_PRINTLN,
    TOK_UNQUOTED_STRING,
    TOK_SEMI,

    TOK_VAR_NUMBER,
    TOK_VAR_STRING,
    TOK_VAR_BOOL,
    TOK_NUMERIC,
    TOK_COMMENT,
    TOK_BOOL_TRUE,
    TOK_BOOL_FALSE,
    TOK_STRING,
    TOK_ASSIGN,

    TOK_EQ, // '=='
    TOK_NEQ, // <>
    TOK_GT,  // >
    TOK_GTE, // >=
    TOK_LT,  // <
    TOK_LTE, // <=
    TOK_AND, // &&
    TOK_OR,  // ||
    TOK_NOT, // !

    TOK_IF,    // If
    TOK_THEN,  // Then
    TOK_ELSE,  // Else
    TOK_ENDIF, // Endif
    TOK_WHILE, // While
    TOK_WEND   // Wend


}


