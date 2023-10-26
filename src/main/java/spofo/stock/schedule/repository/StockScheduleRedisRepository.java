package spofo.stock.schedule.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import spofo.stock.schedule.entity.Stock;

@Repository
public interface StockScheduleRedisRepository extends CrudRepository<Stock, String> {

}
