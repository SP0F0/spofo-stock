package spofo.stock.repository;

import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import spofo.stock.data.StockSearchResponseDto;

@Repository
@RequiredArgsConstructor
public class StockSearchRepositoryImpl implements StockSearchRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<StockSearchResponseDto> findStocksByKeyword(String keyword) {
        String sql = "select stock_name, stock_code, stock_market, image_url "
                + "from Stock "
                + "where stock_name like ? "
                + "or stock_code like ?";
        String likeWord = "%" + keyword + "%";
        String[] param = List.of(likeWord, likeWord).toArray(new String[0]);

        return jdbcTemplate.query(sql, param, customMapper);
    }

    @Override
    public Optional<String> findStockNameByStockCode(String stockCode) {
        String sql = "select stock_name "
                + "from Stock "
                + "where stock_code = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, String.class, stockCode));
    }

    private final RowMapper<StockSearchResponseDto> customMapper = (ResultSet rs, int rowNum) -> {
        StockSearchResponseDto stockSearchResponseDto = StockSearchResponseDto.builder()
                .name(rs.getString("stock_name"))
                .stockCode(rs.getString("stock_code"))
                .market(rs.getString("stock_market"))
                .imageUrl(rs.getString("image_url"))
                .build();
        return stockSearchResponseDto;
    };
}
