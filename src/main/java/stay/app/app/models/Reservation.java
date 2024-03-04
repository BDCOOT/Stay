package stay.app.app.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="yb_reservation")
public class Reservation extends BaseEntity{

    @Id
    public String id;
    public String userId;
    public String stayId;
    public String roomId;
    public LocalDateTime checkinDate;
    public LocalDateTime checkoutDate;
    public int price;
    public int usePoint;
    public int status;

}
