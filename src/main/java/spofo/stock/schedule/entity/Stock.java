package spofo.stock.schedule.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@RedisHash(value = "stock")
public class Stock {

    @Id
    private String stockCode;
    private String name;
    private String imageUrl;
    private String market;
    private String createdAt;
}
