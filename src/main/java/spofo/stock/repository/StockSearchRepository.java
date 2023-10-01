package spofo.stock.repository;

import java.util.List;
import java.util.Optional;
import spofo.stock.data.StockSearchResponseDto;

public interface StockSearchRepository {

    List<StockSearchResponseDto> findStocksByKeyword(String keyword);

    Optional<String> findStockNameByStockCode(String stockCode);
}
