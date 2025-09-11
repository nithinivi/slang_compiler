use std::ptr::null;
use crate::context::RuntimeContext;
use crate::rd_parser::RdParser;

mod expression;
mod context;
mod operator;
mod lexer;
mod rd_parser;

fn main() {

    let mut parser = RdParser::new("-2 + 2 * 2" );
    let x = parser.callExpr(
    );
    println!("{:?}", x.unwrap().evaluate(&RuntimeContext::new()))

}
