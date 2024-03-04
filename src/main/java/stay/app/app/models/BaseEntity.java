package stay.app.app.models;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public class BaseEntity {

    @CreatedDate
    @Column(name = "createdat", updatable = false)
    private LocalDateTime createdAt;
    @LastModifiedDate
    @Column(name = "updatedat")
    private LocalDateTime updatedAt;
    //alter table yb_user modify createdAt datetime not null default CURRENT_TIMESTAMP
    //#{project_name}Application 에 @EnableJpaAuditing 어노테이션 세팅 까먹지마!!!!!!!!!!!!!!!
}
