package stay.app.app.utils;

import lombok.Getter;
import org.springframework.stereotype.Service;

@Service
@Getter
public class MileagePer {

    private final float bronze = 0.01F;
    private final float silver =  0.02F;
    private final float gold =  0.03F;
    private final float platinum =  0.04F;
    private final float diamond =  0.05F;


}
