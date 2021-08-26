from .RDParser import RDParser

class ExpressionBuilder:
    def __init__(self, expr):
        self._expr_string = expr

    def get_expression(self):
        p = RDParser(self._expr_string)
        return p.call_expr()