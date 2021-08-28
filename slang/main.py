from ExpressionBuilder import ExpressionBuilder

def main():
    expr_str = "-2*(3+(4/2))"
    b  = ExpressionBuilder(expr_str)
    e = b.get_expression()
    print (f"Expression in String: {expr_str} ")
    print (f"AST: {e}")
    print (f"Result: { e.evaluate()}")
    


if __name__ == "__main__":
    main()