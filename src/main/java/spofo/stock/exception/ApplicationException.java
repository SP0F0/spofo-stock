package spofo.stock.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class ApplicationException extends RuntimeException {

    private ErrorCode errorCode;
    private HttpStatus httpStatus;
    private String message;

    public ApplicationException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.httpStatus = errorCode.getHttpStatus();
        this.message = null;
    }

    @Override
    public String getMessage() {
        if (message == null) {
            return errorCode.getMessage();
        } else {
            return String.format("%s : %s", errorCode.getMessage(), message);
        }
    }
}
