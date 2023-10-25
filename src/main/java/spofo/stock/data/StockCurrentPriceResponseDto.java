package spofo.stock.data;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import spofo.stock.data.request.kis.Output;
import spofo.stock.schedule.entity.Stock;

@Getter
@NoArgsConstructor
@RedisHash(value = "stockCurrentPrice", timeToLive = 60)
public class StockCurrentPriceResponseDto {

    @Id
    private String code;

    private String name;
    private String price;
    private String imageUrl;
    private String market;
    private String sector;

    @Builder
    private StockCurrentPriceResponseDto(String code, String name, String price, String market, String imageUrl,
            String sector) {
        this.code = code;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.market = market;
        this.sector = sector;
    }

    public static StockCurrentPriceResponseDto of(Output output, String stockName) {
        return StockCurrentPriceResponseDto.builder()
                .code(output.getStck_shrn_iscd())
                .name(stockName)
                .price(output.getStck_prpr())
                .market(output.getRprs_mrkt_kor_name())
                .sector(output.getBstp_kor_isnm())
                .build();
    }

    public static StockCurrentPriceResponseDto of(Output output, Stock stock) {
        return StockCurrentPriceResponseDto.builder()
                .code(stock.getStockCode())
                .name(stock.getName())
                .price(output.getStck_prpr())
                .imageUrl(stock.getImageUrl())
                .market(stock.getMarket())
                .sector(output.getBstp_kor_isnm())
                .build();
    }
}
