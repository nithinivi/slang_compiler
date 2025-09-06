use std::ops::Index;

#[derive(Debug)]
enum TOKEN {
    ILLEGAL_TOKEN = -1,
    TOK_PLUS = 1,
    TOK_MUL,
    TOK_DIV,
    TOK_SUB,
    TOK_OPREN,
    TOK_CPREN,
    TOK_DOUBLE,
    TOK_NULL,
}

struct Lexer<'a> {
    source: &'a str,
    index: usize,
    length: usize,
    number: f32,
    char: Vec<char>,
}

impl<'a> Lexer<'a> {
    fn new(source: &'a str) -> Lexer<'a> {
        let len = source.len();
        let char = source.chars().collect();
        Self {
            source,
            index: 0,
            length: len,
            number: f32::NAN,
            char,
        }
    }

    fn c(&self) -> char {
        self.char[self.index]
    }

    pub fn next_token(&mut self) -> TOKEN {
        let mut tok = TOKEN::ILLEGAL_TOKEN;
        while self.index < self.length && self.c().is_whitespace() {
            self.index += 1
        }

        if self.index == self.length {
            tok = TOKEN::TOK_NULL;
        }

        match self.c() {
            '+' => {
                self.index += 1;
                tok = TOKEN::TOK_PLUS
            }
            '*' => {
                self.index += 1;
                tok = TOKEN::TOK_MUL
            }
            '/' => {
                self.index += 1;
                tok = TOKEN::TOK_DIV
            }
            '-' => {
                self.index += 1;
                tok = TOKEN::TOK_SUB
            }
            '(' => {
                self.index += 1;
                tok = TOKEN::TOK_OPREN
            }
            ')' => {
                self.index += 1;
                tok = TOKEN::TOK_CPREN
            }
            x if x.is_ascii_digit() => {
                let mut seen_dot = false;
                let start = self.index;

                while self.index < self.length {
                    if self.c().is_ascii_digit() {
                        self.index += 1;
                    } else if self.c() == '.' && !seen_dot {
                        seen_dot = true;
                        self.index += 1;
                    } else {
                        break;
                    }
                }
                let s: String = self.char[start..self.index].iter().collect();
                self.number = s.parse::<f32>().unwrap_or(f32::NAN);
            }

            _ => {panic!("Error While Analyzing Tokens")}
        }

        tok
    }
}


#[cfg(test)]
mod tests {
    use super::*;
    #[test]
    fn test_next_token() {
        let mut lexer = Lexer::new("12.0");
        lexer.next_token();
        assert_eq!(lexer.number, 12.0)
    }
}
