package spofo.stock.schedule.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import spofo.stock.schedule.entity.Stock;
import spofo.stock.schedule.task.StockScheduleTasks;

@RestController
@RequiredArgsConstructor
public class ScheduleTestController {

    private final StockScheduleTasks stockScheduleTasks;

    @GetMapping("/save/stocks")
    public ResponseEntity<?> saveStocks() {

        List<Stock> stocks = stockScheduleTasks.saveStocks();
        return ResponseEntity.ok().body(stocks);
    }
}
