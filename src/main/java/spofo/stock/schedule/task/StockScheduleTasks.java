package spofo.stock.schedule.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import spofo.stock.data.request.publicdata.PublicDataRequestDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockScheduleTasks {

    private final RestClient restClient;
    private static final String publicDataUrl = "https://apis.data.go.kr/1160100/service/GetStockSecuritiesInfoService";
    private static final String publicDataPath = "/getStockPriceInfo";

    @Value("${public.data.decodingKey}")
    private String decodingKey;

    public void saveStocks() {

        String recentTradingDate = getRecentTradeDate();

    }

    private String getRecentTradeDate() {

        UriComponents uri = UriComponentsBuilder
                .fromUriString(publicDataUrl)
                .path(publicDataPath)
                .queryParam("numOfRows", "{num}")
                .queryParam("serviceKey", "{key}")
                .queryParam("resultType", "{type}")
                .encode()
                .buildAndExpand(1, decodingKey, "json");

        PublicDataRequestDto response = restClient.get()
                .uri(uri.toUri())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(PublicDataRequestDto.class);

        String recentTradingDate = response.getResponse().getBody().getItems()
                .getItem()
                .get(0)
                .getBasDt();

        log.info("recentTradingDate = {}", recentTradingDate);
        return recentTradingDate;
    }
}
