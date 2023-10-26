package spofo.stock.service;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.util.StringUtils.hasText;
import static spofo.stock.exception.ErrorCode.INTERNAL_SERVER_ERROR;
import static spofo.stock.exception.ErrorCode.INVALID_STOCK_CODE;

import java.net.URI;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.UnknownContentTypeException;
import org.springframework.web.util.UriComponentsBuilder;
import spofo.stock.config.KisAccessTokenDto;
import spofo.stock.data.StockCurrentPriceResponseDto;
import spofo.stock.data.StockSearchResponseDto;
import spofo.stock.data.request.kis.KisAccessTokenResponseDto;
import spofo.stock.data.request.kis.KisRequestDto;
import spofo.stock.data.request.kis.Output;
import spofo.stock.exception.StockException;
import spofo.stock.repository.StockRedisRepository;
import spofo.stock.repository.StockSearchRepository;
import spofo.stock.schedule.entity.Stock;
import spofo.stock.schedule.repository.StockScheduleRedisRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockService {

    private static final String KIS_URL = "https://openapi.koreainvestment.com:9443";
    private static final String CURRENT_PRICE_PATH = "/uapi/domestic-stock/v1/quotations/inquire-price";
    private static final String ACCESS_TOKEN_PATH = "/oauth2/tokenP";
    private static final String TRADE_ID = "FHKST01010100";
    private static final Duration TOKEN_TTL = Duration.ofHours(12);
    private static final String ACCESS_TOKEN_KEY = "access_token";

    @Value("${kis.appKey}")
    private String appKey;

    @Value("${kis.appSecret}")
    private String appSecret;

    private final RestClient restClient;
    private final KisAccessTokenDto kisAccessTokenDto;
    private final RedisTemplate<String, String> redisTemplate;
    private final StockRedisRepository stockRedisRepository;
    private final StockSearchRepository stockSearchRepository;
    private final StockScheduleRedisRepository stockScheduleRedisRepository;

    public StockCurrentPriceResponseDto findCurrentPriceByStockCode(String stockCode) {
        return stockRedisRepository.findById(stockCode)
                .orElseGet(() -> getCurrentPriceByKisRedis(stockCode));
    }

    @Transactional(readOnly = true)
    public List<StockSearchResponseDto> findStockByKeyword(String keyword) {
        return stockSearchRepository.findStocksByKeyword(keyword);
    }

    public List<StockCurrentPriceResponseDto> findCurrentPriceByStockList(
            List<String> stockCodeList) {

        return stockCodeList.stream()
                .map(this::findCurrentPriceByStockCode)
                .collect(Collectors.toList());
    }

    private StockCurrentPriceResponseDto getCurrentPriceByKisRedis(String stockCode) {

        URI uri = generateCurrentPriceApiUri(stockCode);
        String accessToken = getAccessToken();
        Output output = getCurrentPriceOutput(uri, accessToken);

        Stock stock = stockScheduleRedisRepository.findById(stockCode)
                .orElseThrow(() -> new StockException(INVALID_STOCK_CODE));

        StockCurrentPriceResponseDto stockCurrentPriceResponseDto = StockCurrentPriceResponseDto.of(output, stock);

        return stockRedisRepository.save(stockCurrentPriceResponseDto);
    }

    private StockCurrentPriceResponseDto getCurrentPriceByKis(String stockCode) {

        URI uri = generateCurrentPriceApiUri(stockCode);
        String accessToken = getAccessToken();
        Output output = getCurrentPriceOutput(uri, accessToken);

        String stockName = Optional.ofNullable(getValueFromRedis(stockCode)).orElseGet(
                () -> getStockFromRedis(stockCode));

        StockCurrentPriceResponseDto stockCurrentPriceResponseDto = StockCurrentPriceResponseDto.of(
                output, stockName);

        return stockRedisRepository.save(stockCurrentPriceResponseDto);
    }

    private String getStockFromRedis(String stockCode) {
        String stockName = stockSearchRepository.findStockNameByStockCode(stockCode)
                .orElseThrow(() -> new StockException(INVALID_STOCK_CODE));
        setValueToRedis(stockCode, stockName, Duration.ofDays(30));
        return stockName;
    }

    private URI generateCurrentPriceApiUri(String stockCode) {

        return UriComponentsBuilder
                .fromUriString(KIS_URL)
                .path(CURRENT_PRICE_PATH)
                .queryParam("FID_COND_MRKT_DIV_CODE", "{market_type}")
                .queryParam("FID_INPUT_ISCD", "{num}")
                .encode()
                .buildAndExpand("J", stockCode)
                .toUri();
    }

    private String getAccessToken() {
        String accessToken = getValueFromRedis(ACCESS_TOKEN_KEY);

        return hasText(accessToken) ? accessToken : getAccessTokenFromKis();
    }

    private Output getCurrentPriceOutput(URI uri, String accessToken) {
        try {
            KisRequestDto kisRequestDto = restClient.get()
                    .uri(uri)
                    .header("authorization", "Bearer " + accessToken)
                    .header("appkey", appKey)
                    .header("appsecret", appSecret)
                    .header("tr_id", TRADE_ID)
                    .accept(APPLICATION_JSON)
                    .retrieve()
                    .body(KisRequestDto.class);

            return kisRequestDto.getOutput();
        } catch (ResourceAccessException e){
            throw new StockException(INTERNAL_SERVER_ERROR);
        } catch (UnknownContentTypeException e) {
            throw new StockException(INTERNAL_SERVER_ERROR);
        }


    }

    private String getAccessTokenFromKis() {
        URI uri = UriComponentsBuilder
                .fromUriString(KIS_URL)
                .path(ACCESS_TOKEN_PATH)
                .encode()
                .buildAndExpand()
                .toUri();

        try {
            KisAccessTokenResponseDto responseBody = restClient.post()
                    .uri(uri)
                    .contentType(APPLICATION_JSON)
                    .body(kisAccessTokenDto)
                    .retrieve()
                    .toEntity(KisAccessTokenResponseDto.class)
                    .getBody();

            return setValueToRedis(ACCESS_TOKEN_KEY,
                    responseBody.getAccess_token(), TOKEN_TTL);
        } catch (ResourceAccessException e){
            throw new StockException(INTERNAL_SERVER_ERROR);
        } catch (UnknownContentTypeException e) {
            throw new StockException(INTERNAL_SERVER_ERROR);
        }
    }

    private String getValueFromRedis(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    private String setValueToRedis(String key, String value, Duration duration) {
        redisTemplate.opsForValue().set(key, value, duration);
        return value;
    }
}
