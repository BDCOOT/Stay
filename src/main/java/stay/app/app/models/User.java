package stay.app.app.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name="yb_user")
public class User extends BaseEntity{

    @Id
    private String id;
    private String userId;
    private String userName;
    private String appKey;
    private String email;
    private String phone;
    private String gender;
    private LocalDate birth;
    private String grade;
    private String userType;
    private boolean banned;


//    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
//    private List<Stay> stays;
}
