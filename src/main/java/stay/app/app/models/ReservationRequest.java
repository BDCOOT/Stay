package stay.app.app.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservationRequest {
    private Reservation reservation;
    private Mileage mileage;
}
