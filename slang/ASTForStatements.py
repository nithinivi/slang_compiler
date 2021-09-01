"""Statement is what you excute for its effect"""
from abc import ABCMeta, abstractmethod
from AST import Exp


class Statement:
    @abstractmethod
    def excute(con):
        pass 


class PrintStatment(Statement):
    def __init__(self, ex:Exp) -> None:
        self._ex = ex

    def excute(self,con):
        a = self._ex.evaluate()
        print(a, end="")
        return True


class PrintLnStatment(PrintStatment):
    def excute(self, con):
        a = self._ex.evaluate()
        print(a)
        return True
 


