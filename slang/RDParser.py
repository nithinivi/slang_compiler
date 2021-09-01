from Lexer import TOKEN, Lexer
from AST import OPERATOR, BinaryExp, NumericConstant, UnaryExp


class RDParser(Lexer):
    """."""

    def __init__(self, string: str):
        """."""
        self.string = string
        super().__init__(string)

    def call_expr(self):
        """."""
        self.current_token = self.get_token()
        return self.expr()

    def expr(self):
        """<Expr> ::= <Term> { + | - } <Expr> ."""
        return_value = self.term()
        while (self.current_token == TOKEN.TOK_PLUS
               or self.current_token == TOKEN.TOK_SUB):
            l_token = self.current_token
            self.current_token = self.get_token()
            e1 = self.expr()
            return_value = BinaryExp(
                return_value, e1,
                OPERATOR.PLUS if l_token == TOKEN.TOK_PLUS else OPERATOR.MINUS)
        return return_value

    def term(self):
        """<Term> ::= <Factor> { * | / } <Term> ."""
        return_value = self.factor()
        while (self.current_token == TOKEN.TOK_MUL
               or self.current_token == TOKEN.TOK_DIV):
            l_token = self.current_token
            self.current_token = self.get_token()
            e1 = self.term()
            return_value = BinaryExp(
                return_value, e1,
                OPERATOR.MUL if l_token == TOKEN.TOK_MUL else OPERATOR.DIV)
        return return_value

    def factor(self):
        """<Factor> ::= <TOK_DOUBLE> | ( <expr> ) | { + |- } <Factor>."""
        return_value = None
        if self.current_token == TOKEN.TOK_DOUBLE:
            return_value = NumericConstant(self.get_number())
            self.current_token = self.get_token()

        elif self.current_token == TOKEN.TOK_OPAREN:
            self.current_token = self.get_token()
            return_value = self.expr()

            if self.current_token != TOKEN.TOK_CPAREN:
                raise Exception("Missing ')' Parens")
            self.current_token = self.get_token()
            # the value is NULL TOKEN THER NOTHING MUCH TO DO.
        elif self.current_token == TOKEN.TOK_PLUS or self.current_token == TOKEN.TOK_SUB:
            l_token = self.current_token
            self.current_token = self.get_token()
            return_value = self.factor()
            return_value = UnaryExp(
                return_value,
                TOKEN.TOK_PLUS if l_token == TOKEN.TOK_PLUS else TOKEN.TOK_SUB)
        else:
            raise Exception("Illegal Token")
        return return_value
