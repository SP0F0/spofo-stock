package spofo.stock.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KisAccessTokenDto {

    private final String grant_type = "client_credentials";
    private String appkey;
    private String appsecret;

    public KisAccessTokenDto(String appkey, String appsecret) {
        this.appkey = appkey;
        this.appsecret = appsecret;
    }
}
