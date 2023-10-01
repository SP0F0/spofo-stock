package spofo.stock.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import spofo.stock.data.StockCurrentPriceResponseDto;

@Repository
public interface StockRedisRepository extends CrudRepository<StockCurrentPriceResponseDto, String> {

}
