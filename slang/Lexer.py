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
    def __init__(self, expr: str):
        self.iexpr = expr
        self.length = len(self.iexpr)
        self.index = 0

    def get_token(self):
        tok = TOKEN.TOK_NULL

        # skip white spaces
        while (self.index < self.length
               and (self.index == ' ' or self.iexpr[self.index] == '\t')):
            self.index += 1

        # end of string -> null
        if self.index == self.length:
            return TOKEN.TOK_NULL

        index_token = self.iexpr[self.index]

        if index_token == "+":
            tok = TOKEN.TOK_PLUS
            self.index += 1

        elif index_token == "-":
            tok = TOKEN.TOK_SUB
            self.index += 1

        elif index_token == "*":
            tok = TOKEN.TOK_MUL
            self.index += 1

        elif index_token == "/":
            tok = TOKEN.TOK_DIV
            self.index += 1

        elif index_token == "(":
            tok = TOKEN.TOK_OPAREN
            self.index += 1

        elif index_token == ")":
            tok = TOKEN.TOK_CPAREN
            self.index += 1

        # parsing string
        elif index_token.isdigit():
            num_string = ""

            while (self.index < self.length
                   and self.iexpr[self.index].isdigit()):

                num_string += self.iexpr[self.index]
                self.index += 1

            self.number = float(num_string)
            tok = TOKEN.TOK_DOUBLE

        else:
            raise ValueError("Error while Analysing the Token")

        return tok

    def get_number(self):
        return self.number
