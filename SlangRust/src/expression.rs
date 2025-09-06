use crate::context::RuntimeContext;
use crate::operator::Operator;

pub trait Expression {
    fn evaluate(&self, context: &RuntimeContext) -> f32;
}

#[derive(Debug)]
struct NumericContext {
    value: f32,
}

impl NumericContext {
    pub fn new(value: f32) -> Self {
        Self { value }
    }
}

impl Expression for NumericContext {
    fn evaluate(&self, context: &RuntimeContext) -> f32 {
        self.value
    }
}

struct UnaryExpression {
    exp1: Box<dyn Expression>,
    operator: Operator,
}

impl UnaryExpression {
    pub fn new(exp1: Box<dyn Expression>, operator: Operator) -> Self {
        Self { exp1, operator }
    }
}

impl Expression for UnaryExpression {
    fn evaluate(&self, context: &RuntimeContext) -> f32 {
        match self.operator {
            Operator::PLUS => self.exp1.evaluate(context),
            Operator::MINUS => -self.exp1.evaluate(context),
            _ => f32::NAN,
        }
    }
}

struct BinaryExpression {
    l_exp: Box<dyn Expression>,
    r_expr: Box<dyn Expression>,
    operator: Operator,
}

impl BinaryExpression {
    pub fn new(
        l_exp: Box<dyn Expression>,
        r_expr: Box<dyn Expression>,
        operator: Operator,
    ) -> Self {
        Self {
            l_exp,
            r_expr,
            operator,
        }
    }
}

impl Expression for BinaryExpression {
    fn evaluate(&self, context: &RuntimeContext) -> f32 {
        match self.operator {
            Operator::PLUS => self.l_exp.evaluate(context) + self.r_expr.evaluate(context),
            Operator::MINUS => self.l_exp.evaluate(context) - self.r_expr.evaluate(context),
            Operator::DIV => self.l_exp.evaluate(context) / self.r_expr.evaluate(context),
            Operator::MUL => self.l_exp.evaluate(context) * self.r_expr.evaluate(context),
            _ => f32::NAN,
        }
    }
}

#[cfg(test)]
mod tests {
    use super::*;
    #[test]
    fn test_numeric_expression() {
        let numeric_context = NumericContext::new(3.14);
        assert_eq!(numeric_context.evaluate(&RuntimeContext::new()), 3.14);
    }

    #[test]
    fn test_binary_expression() {
        let binary_context_plus = BinaryExpression::new(
            Box::new(NumericContext::new(6.0)),
            Box::new(NumericContext::new(6.0)),
            Operator::PLUS,
        );
        let binary_context_minus = BinaryExpression::new(
            Box::new(NumericContext::new(6.0)),
            Box::new(NumericContext::new(6.0)),
            Operator::MINUS,
        );
        let binary_context_mul = BinaryExpression::new(
            Box::new(NumericContext::new(6.0)),
            Box::new(NumericContext::new(2.0)),
            Operator::MUL,
        );
        let binary_context_div = BinaryExpression::new(
            Box::new(NumericContext::new(6.0)),
            Box::new(NumericContext::new(2.0)),
            Operator::DIV,
        );
        assert_eq!(binary_context_plus.evaluate(&RuntimeContext::new()), 12.0);
        assert_eq!(binary_context_minus.evaluate(&RuntimeContext::new()), 0.0);
        assert_eq!(binary_context_mul.evaluate(&RuntimeContext::new()), 12.0);
        assert_eq!(binary_context_div.evaluate(&RuntimeContext::new()), 3.0);
    }

    #[test]
    fn test_unary_expression() {
        let unary_context_plus =
            UnaryExpression::new(Box::new(NumericContext::new(6.0)), Operator::PLUS);
        let unary_context_minus =
            UnaryExpression::new(Box::new(NumericContext::new(6.0)), Operator::MINUS);

        let unary_context_mul =
            UnaryExpression::new(Box::new(NumericContext::new(6.0)), Operator::MUL);

        let unary_context_illegal =
            UnaryExpression::new(Box::new(NumericContext::new(6.0)), Operator::ILLEGAL);


        assert_eq!(unary_context_plus.evaluate(&RuntimeContext::new()), 6.0);
        assert_eq!(unary_context_minus.evaluate(&RuntimeContext::new()), -6.0);
        assert!(unary_context_mul.evaluate(&RuntimeContext::new()).is_nan());
        assert!(unary_context_illegal.evaluate(&RuntimeContext::new()).is_nan())
    }
}
