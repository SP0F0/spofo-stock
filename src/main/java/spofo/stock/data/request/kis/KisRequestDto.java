package spofo.stock.data.request.kis;

import java.util.Optional;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class KisRequestDto {

    private Output output;
    private String rt_cd;
    private String msg_cd;
    private String msg1;
}
