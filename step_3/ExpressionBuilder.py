from RDParser import RDParser

class ExpressionBuilder:
    def __init__(self, expr):
        self._expr_string = expr

    def get_expression(self):
        p = RDParser(self._expr_string)
        return p.call_expr()

def main():
    expr_str = "((12 * 12 * 12) + 1) - ((10 * 10 * 10 ) + (9 *9 *9 )))"
    b  = ExpressionBuilder(expr_str)
    e = b.get_expression()
    print (f"Expression in String: {expr_str} ")
    print (f"AST: {e}")
    print (f"Result: { e.evaluate()}")

if __name__ == "__main__":
    main()
