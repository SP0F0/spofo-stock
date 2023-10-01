package spofo.stock.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public class ErrorResponse {

    private HttpStatus httpStatus;
    private String message;
}
