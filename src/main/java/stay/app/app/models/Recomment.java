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
@Table(name="yb_recomment")
public class Recomment extends BaseEntity{

    @Id
    private String id;
    private String commentId;
    private String userId;
    private String description;
}
