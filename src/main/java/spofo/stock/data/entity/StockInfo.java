package spofo.stock.data.entity;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class StockInfo {

    private String name;
    private String stockCode;
    private String imageUrl; // https://images.therich.io/images/logo/kr/086520.png
    private String market;
    private String createdAt;
}
