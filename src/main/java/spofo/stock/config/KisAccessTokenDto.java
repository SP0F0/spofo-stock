package spofo.stock.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KisAccessTokenDto {

    private final String grant_type = "client_credentials";
    private String appkey;
    private String appSecret;

    public KisAccessTokenDto(String appkey, String appSecret) {
        this.appkey = appkey;
        this.appSecret = appSecret;
    }
}
