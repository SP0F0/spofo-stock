package spofo.stock.schedule.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@RedisHash(value = "stock", timeToLive = 6000)
public class Stock {

    private String name;
    private String stockCode;
    private String imageUrl;
    private String market;
    private String closingPrice;
    private String createdAt;
}
