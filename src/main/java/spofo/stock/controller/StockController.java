package spofo.stock.controller;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import spofo.stock.data.StockCurrentPriceResponseDto;
import spofo.stock.data.StockSearchResponseDto;
import spofo.stock.service.StockService;

@Slf4j
@RestController
@RequestMapping("/stocks")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @GetMapping("/{stockCode}")
    public ResponseEntity<StockCurrentPriceResponseDto> getStockCurrentPrice(
            @PathVariable("stockCode") String stockCode) {
        log.info("stockCode : {}", stockCode);
        StockCurrentPriceResponseDto stockCurrentPriceResponseDto = stockService.findCurrentPriceByStockCode(
                stockCode);
        return ResponseEntity.ok(stockCurrentPriceResponseDto);
    }

    @GetMapping("")
    public ResponseEntity<List<StockCurrentPriceResponseDto>> getStocksCurrentPrice(
            @RequestBody Map<String, List<String>> stockCodeList
    ) {
        for (String stockCode : stockCodeList.get("stockCodeList")) {
            log.info("stockCode : {}", stockCode);
        }
        List<StockCurrentPriceResponseDto> currentPriceByStockList = stockService.findCurrentPriceByStockList(
                stockCodeList.get("stockCodeList"));
        return ResponseEntity.ok(currentPriceByStockList);
    }

    @GetMapping("/search")
    public ResponseEntity<List<StockSearchResponseDto>> getStocksByKeyword(
            @RequestParam("keyword") String keyword
    ) {
        List<StockSearchResponseDto> stockList = stockService.findStockByKeyword(
                keyword);
        return ResponseEntity.ok(stockList);
    }
}
