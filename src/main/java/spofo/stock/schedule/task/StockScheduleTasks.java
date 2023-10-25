package spofo.stock.schedule.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import spofo.stock.schedule.repository.StockScheduleRedisRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockScheduleTasks {

    private final RestClient restClient;
    private final StockScheduleRedisRepository stockScheduleRedisRepository;
    private static final String publicDataUrl = "https://apis.data.go.kr/1160100/service/GetStockSecuritiesInfoService";
    private static final String publicDataPath = "/getStockPriceInfo";

    @Value("${public.data.decodingKey}")
    private String decodingKey;


}
