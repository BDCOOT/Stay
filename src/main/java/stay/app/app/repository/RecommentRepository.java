package stay.app.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import stay.app.app.models.Recomment;

@Repository
public interface RecommentRepository extends JpaRepository<Recomment, String> {

    Recomment findOneById(String id);
}
