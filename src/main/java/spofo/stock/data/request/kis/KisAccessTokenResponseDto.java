package spofo.stock.data.request.kis;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class KisAccessTokenResponseDto {

    private String access_token;
    private String token_type;
    private Long expires_in;
}
