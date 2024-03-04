package stay.app.app.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="yb_cart")
public class Cart extends BaseEntity{

    @Id
    private String id;
    private String userId;
    private String roomId;
    private int price;
    private LocalDate checkinDate;
    private LocalDate checkoutDate;

}
