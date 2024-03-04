package stay.app.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import stay.app.app.models.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, String> {

    Review findOneById(String id);

    boolean existsByReservationId(String reservationId);
}