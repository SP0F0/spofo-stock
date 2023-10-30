package spofo.stock.schedule.task;

import static spofo.stock.exception.ErrorCode.INTERNAL_SERVER_ERROR;

import jakarta.annotation.PostConstruct;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.UnknownContentTypeException;
import org.springframework.web.util.UriComponentsBuilder;
import spofo.stock.data.request.publicdata.Item;
import spofo.stock.data.request.publicdata.PublicDataRequestDto;
import spofo.stock.exception.StockException;
import spofo.stock.schedule.entity.Stock;
import spofo.stock.schedule.repository.StockScheduleRedisRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockScheduleTasks {

    @Value("${public-data.url}")
    private String url;

    @Value("${public-data.path}")
    private String path;

    @Value("${public-data.decoding-key}")
    private String decodingKey;

    private final RestClient restClient;
    private final AmazonS3Service amazonS3Service;
    private final StockScheduleRedisRepository stockScheduleRedisRepository;


    @PostConstruct
    @Scheduled(cron = "0 0 8 * * ?")
    public List<Stock> saveStocks() {
        String recentTradingDate = getRecentTradeDate();
        URI uri = getUriComponents(recentTradingDate);
        PublicDataRequestDto response = getPublicDataDto(uri);

        List<Item> itemList = response.getResponse().getBody().getItems().getItem();
        List<Item> newStockList = itemList.stream()
                .filter(item -> stockScheduleRedisRepository.findById(item.getSrtnCd()).isEmpty())
                .toList();

        Map<String, String> imageUrlMap = amazonS3Service.uploadLogos(newStockList);
        List<Stock> stockList = getStockList(imageUrlMap, newStockList);

        stockScheduleRedisRepository.saveAll(stockList);
        return stockList;
    }

    private String getRecentTradeDate() {

        URI uri = UriComponentsBuilder
                .fromUriString(url)
                .path(path)
                .queryParam("numOfRows", "{num}")
                .queryParam("serviceKey", "{key}")
                .queryParam("resultType", "{type}")
                .encode()
                .buildAndExpand(1, decodingKey, "json")
                .toUri();

        try {

            PublicDataRequestDto response = restClient.get()
                    .uri(uri)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .body(PublicDataRequestDto.class);

            String recentTradingDate = response.getResponse().getBody().getItems()
                    .getItem()
                    .get(0)
                    .getBasDt();
            log.info("recentTradingDate = {}", recentTradingDate);
            return recentTradingDate;
        } catch (ResourceAccessException e){
            throw new StockException(INTERNAL_SERVER_ERROR);
        } catch (UnknownContentTypeException e) {
            throw new StockException(INTERNAL_SERVER_ERROR);
        }


    }

    private URI getUriComponents(String recentTradingDate) {

        URI uri = UriComponentsBuilder
                .fromUriString(url)
                .path(path)
                .queryParam("serviceKey", "{key}")
                .queryParam("numOfRows", "{num}")
                .queryParam("resultType", "{type}")
                .queryParam("basDt", "{date}")
                .encode()
                .buildAndExpand(decodingKey, 4000, "json", recentTradingDate)
                .toUri();

        return uri;
    }

    private PublicDataRequestDto getPublicDataDto(URI uri) {

        try {
            PublicDataRequestDto response = restClient.get()
                    .uri(uri)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .body(PublicDataRequestDto.class);

            return response;
        } catch (ResourceAccessException e){
            throw new StockException(INTERNAL_SERVER_ERROR);
        } catch (UnknownContentTypeException e) {
            throw new StockException(INTERNAL_SERVER_ERROR);
        }
    }

    private List<Stock> getStockList(Map<String, String> imageUrlMap,
            List<Item> itemList) {

        List<Stock> stockList = new ArrayList<>();

        for (Item item : itemList) {
            Stock stock = Stock.builder()
                    .name(item.getItmsNm())
                    .stockCode(item.getSrtnCd())
                    .imageUrl(imageUrlMap.get(item.getSrtnCd()))
                    .market(item.getMrktCtg())
                    .createdAt(item.getBasDt())
                    .build();

            stockList.add(stock);
        }

        return stockList;
    }
}
