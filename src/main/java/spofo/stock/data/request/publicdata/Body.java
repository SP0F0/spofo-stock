package spofo.stock.data.request.publicdata;

import lombok.Data;

@Data
public class Body {

    private int numOfRows;
    private int pageNo;
    private int totalCount;
    private Items items;
}
