package spofo.stock.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 에러 발생"),
    INVALID_KIS_KEY(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰"),
    INVALID_STOCK_CODE(HttpStatus.BAD_REQUEST, "유효하지 않은 종목 코드")
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
