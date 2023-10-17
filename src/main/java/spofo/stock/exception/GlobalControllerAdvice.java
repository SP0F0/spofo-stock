package spofo.stock.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<?> runtimeExceptionHandler(RuntimeException e) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<?> stockExceptionHandler(StockException e) {

        ErrorResponse errorResponse = new ErrorResponse(e.getStatus(),
                e.getStatus().value(), e.getMessage());

        return ResponseEntity
                .status(e.getStatus())
                .body(errorResponse);
    }
}
