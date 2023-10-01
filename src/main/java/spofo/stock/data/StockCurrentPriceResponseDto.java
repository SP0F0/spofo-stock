package spofo.stock.data;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import spofo.stock.data.request.kis.Output;

@Getter
@NoArgsConstructor
@Builder
@RedisHash(value = "stock", timeToLive = 60)
public class StockCurrentPriceResponseDto {

    @Id
    private String code;

    private String name;
    private String price;
    private String market;
    private String sector;

    @Builder
    private StockCurrentPriceResponseDto(String code, String name, String price, String market,
            String sector) {
        this.code = code;
        this.name = name;
        this.price = price;
        this.market = market;
        this.sector = sector;
    }

    public static StockCurrentPriceResponseDto of(Output output, String stockName) {
        return StockCurrentPriceResponseDto.builder()
                .price(output.getStck_prpr())
                .name(stockName)
                .market(output.getRprs_mrkt_kor_name())
                .sector(output.getBstp_kor_isnm())
                .code(output.getStck_shrn_iscd())
                .build();
    }
}
