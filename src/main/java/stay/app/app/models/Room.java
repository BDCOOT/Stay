package stay.app.app.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="yb_room")
public class Room extends BaseEntity{

    @Id
    private String id;
//    @Column(name = "stay_id")
    private String stayId;
    private String roomName;
    private int bed;
    private int price;
    private int discount;
    private int limited;
    private String description;
    private boolean reservationStatus;
    private boolean allowed;
    private String img;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "stay_id", referencedColumnName = "id", insertable = false, updatable = false)
//    private Stay stay;

}
