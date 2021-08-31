import abc
import math


class OPERATOR:
    PLUS = '+'
    MINUS = '-'
    DIV = '/'
    MUL = '*'


class Exp(metaclass=abc.ABCMeta):
    """
    Abstract Expression evaluatation
    """
    @property
    def class_name(self):
        return self.__class__.__name__

    @staticmethod
    @abc.abstractmethod
    def evaluate(expression):
        "evalutaion method"


class NumericConstant(Exp):
    def __init__(self, value):
        self._value = value

    def evaluate(self):
        return self._value

    def __repr__(self):
        return f'{self.class_name}({self._value})'

    def __str__(self):
        return f'{self._value}'


class BinaryExp(Exp):
    def __init__(self, a: Exp, b: Exp, op: OPERATOR):
        self._ex1 = a
        self._ex2 = b
        self._op = op

    def evaluate(self):
        if self._op == OPERATOR.PLUS:
            return self._ex1.evaluate() + self._ex2.evaluate()

        elif self._op == OPERATOR.MINUS:
            return self._ex1.evaluate() - self._ex2.evaluate()

        elif self._op == OPERATOR.MUL:
            return self._ex1.evaluate() * self._ex2.evaluate()

        elif self._op == OPERATOR.PLUS:
            return self._ex1.evaluate() / self._ex2.evaluate()

        else:
            return math.nan

    def __repr__(self):
        return f"{self.class_name}({self._ex1}, {self._ex2}, '{self._op}')"

    def __str__(self):
        return f'( {self._ex1} {self._op} {self._ex2})'


class UnaryExp(Exp):
    def __init__(self, a: Exp, op: OPERATOR):
        self._ex1 = a
        self._op = op

    def evaluate(self):
        if self._op == OPERATOR.PLUS:
            return self._ex1.evaluate()

        elif self._op == OPERATOR.MINUS:
            return -self._ex1.evaluate()

        else:
            math.nan

    def __repr__(self):
        return f"{self.class_name}({self._ex1}, '{self._op}')"

    def __str__(self):
        return f'({self._op} {self._ex1})'


if __name__ == '__main__':
    exp1 = BinaryExp(NumericConstant(8) ,
                     NumericConstant(18) ,
                     OPERATOR.PLUS
    )

    print(exp1.evaluate())
    print(f'{exp1} \n')


    exp2 = UnaryExp(
          BinaryExp(NumericConstant(10),
                    BinaryExp(NumericConstant(30),
                              NumericConstant(50),
                              OPERATOR.PLUS
                              ),
                    OPERATOR.PLUS
                    ),
          OPERATOR.PLUS
          )
    print (exp2.evaluate())
    print(f'{exp2} \n')

    exp3  = BinaryExp(NumericConstant(400), exp2, OPERATOR.PLUS)
    print (exp3.evaluate())
    print(f'{exp3} \n')
