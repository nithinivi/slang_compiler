use crate::expression::{BinaryExpression, Expression, NumericContext, UnaryExpression};
use crate::lexer::TOKEN::{ADD, DIV, MUL, SUB};
use crate::lexer::{Lexer, TOKEN};
use crate::operator::Operator;
use std::fmt;

trait ErrorForToken {
    fn for_token(token: TOKEN) -> ParseError;
}

#[derive(Debug)]
struct ParseError{
    token: TOKEN,
    line_no: usize,
}
impl ErrorForToken for ParseError {
    fn for_token(token: TOKEN, line_no : usize) -> ParseError {
        Self { token,  line_no }
    }
}

impl fmt::Display for ParseError {
    fn fmt(&self, f: &mut fmt::Formatter) -> fmt::Result {
         write!(f, "Parsing at {} for {:?}", self.line_no, self.token)
    }
}

pub struct RdParser<'a> {
    lexer: Lexer<'a>,
    current_token: TOKEN,
}

impl<'a> RdParser<'a> {
    pub fn new(source: &'a str) -> RdParser<'a> {
        Self {
            lexer: Lexer::new(source),
            current_token: TOKEN::ILLEGAL_TOKEN,
        }
    }

    fn next_token(&mut self) -> TOKEN{
        self.current_token = self.lexer.next_token();
        self.current_token
    }

    /* BNF
    <expr>   ::= <term> { ("+" | "-") <term> }
    <term>   ::= <factor> { ("*" | "/") <factor> }
    <factor> ::= <number>
               | "(" <expr> ")"

     */
    pub fn callExpr(&mut self) -> Result<Box<dyn Expression>, ParseError> {
        self.next_token();
        self.parse_expr()
    }

    /// <expr>   ::= <term> { ("+" | "-") <term> }
    fn parse_expr(&mut self) -> Result<Box<dyn Expression>, ParseError> {
        let mut left = self.parse_term()?;
        let mut operation_token = self.current_token;
        while  ADD == operation_token|| SUB == operation_token {
            let right = self.parse_term()?;
            left = Box::new(BinaryExpression::new(
                left,
                right,
                if operation_token == ADD {
                    Operator::PLUS
                } else {
                    Operator::MINUS
                },
            ));
            operation_token = self.next_token();
        }
        Ok(left)
    }

    /// <term>   ::= <factor> { ("*" | "/") <factor> }
    fn parse_term(&mut self) -> Result<Box<dyn Expression>, ParseError> {
        let mut left = self.parse_factor()?;
        let mut operation_token = self.current_token;
        while  MUL == operation_token || DIV == operation_token {
            let right: Box<dyn Expression> = self.parse_term()?;
            left = Box::new(BinaryExpression::new(
                left,
                right,
                if operation_token == MUL {
                    Operator::MUL
                } else {
                    Operator::DIV
                },
            ));
            operation_token = self.next_token();
        }
        Ok(left)
    }

    ///  <factor> ::= <number> | "(" <expr> ")"
    fn parse_factor(&mut self) -> Result<Box<dyn Expression>, ParseError> {
        match &self.current_token {
            TOKEN::TOK_DOUBLE => {
                let numeric_const_node = Box::new(NumericContext::new(self.lexer.number));
                self.next_token();
                Ok(numeric_const_node)
            }
            TOKEN::OPREN => {
                self.next_token();
                let node = self.parse_expr()?;
                if self.current_token != TOKEN::CPREN {
                    return Err(ParseError::for_token(self.current_token,self.lexer.number));
                }
                self.next_token();
                Ok(node)
            }
            ADD | SUB => {
                self.next_token();
                let operation_token = self.current_token;
                let factor = self.parse_factor()?;
                let unary_expression_node = UnaryExpression::new(
                    factor,
                    if operation_token == SUB {
                        Operator::MINUS
                    } else {
                        Operator::PLUS
                    },
                );
                Ok(Box::new(unary_expression_node))
            }
            _ => Err(ParseError::for_token(self.current_token)),
        }
    }
}
