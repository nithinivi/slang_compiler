class TOKEN:
    ILLEGAL_TOKEN = -1
    TOK_PLUS = 0
    TOK_MUL = 1
    TOK_DIV = 2
    TOK_SUB = 3
    TOK_OPAREN = 4
    TOK_CPAREN = 5
    TOK_DOUBLE = 6
    TOK_NULL = 7




class Lexer:
    '''
    The Lexical Analysis Algorithm scans through the input
    and returns the token associated with the operator.
    '''
    def __init__(self, expr):
        self.iexpr = expr
        self.length = len(self.iexpr)
        self.index = 0
