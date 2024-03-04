package stay.app.app.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="yb_review")
public class Review extends BaseEntity{

    @Id
    private String id;
    private String reservationId;
    private String userId;
    private int rating;
    private String description;
    private String img;
}
