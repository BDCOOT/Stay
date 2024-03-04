package stay.app.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import stay.app.app.models.Mileage;

@Repository
public interface MileageRepository extends JpaRepository<Mileage, String> {

    Mileage findOneByUserId(String userId);
}
