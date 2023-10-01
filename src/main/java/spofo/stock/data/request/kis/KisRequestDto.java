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

    public KisRequestDto(Output output, String rt_cd, String msg_cd, String msg1) {
        this.output = Optional.of(output).orElseGet(() -> new Output());
        this.rt_cd = rt_cd;
        this.msg_cd = msg_cd;
        this.msg1 = msg1;
    }
}
