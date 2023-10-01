package spofo.stock.data;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StockSearchResponseDto {

    private String name;
    private String stockCode;
    private String imageUrl;
    private String market;
}
