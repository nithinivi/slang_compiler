package slang4java.support;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
public class CParserException extends Exception{
    private int  ErrorCode;
    @Setter
    private String ErrorString;
    @Setter
    private int LexicalOffset;

    public CParserException( int errorCode, String errorString, int lexicalOffset) {
        ErrorCode = errorCode;
        ErrorString = errorString;
        LexicalOffset = lexicalOffset;
    }
}


