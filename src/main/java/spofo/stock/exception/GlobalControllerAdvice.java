package spofo.stock.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> runtimeExceptionHandler(RuntimeException e) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }

    @ExceptionHandler(StockException.class)
    public ResponseEntity<?> stockExceptionHandler(StockException e) {
        ErrorResponse exceptionResponse = new ErrorResponse(
                e.getHttpStatus(),
                e.getMessage());

        return ResponseEntity
                .status(exceptionResponse.getHttpStatus())
                .body(exceptionResponse);
    }
}
