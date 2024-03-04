package stay.app.app.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="yb_stay")
public class Stay extends BaseEntity{

    @Id
    private String id;
    private String userId;
    private String stayType;
    private String stayName;
    private int rating;
    private boolean brunch;
    private boolean dinner;
    private String img;
    private boolean allowed;

//    @ManyToOne
//    @JoinColumn(name = "userId", referencedColumnName = "userId", insertable = false, updatable = false)
//    private User user;
//
//    @OneToMany(mappedBy = "stay", cascade = CascadeType.REMOVE)
//    private List<Room> rooms;
}
