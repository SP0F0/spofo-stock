package spofo.stock.data.request.publicdata;

import lombok.Data;

@Data
public class PublicDataResponseDto {

    private Header header;
    private Body body;
}
