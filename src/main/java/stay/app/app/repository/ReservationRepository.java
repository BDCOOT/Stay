package stay.app.app.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import stay.app.app.models.Reservation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, String> {

    Reservation findOneById(String id);

    List<Reservation> findAllByUserIdOrderByCreatedAtDesc(String userId, Pageable page);

    List<Reservation> findAllByStayIdOrderByCreatedAtDesc(String stayId, Pageable page);

    int countByUserId(String userId);

    //기본쿼리 참고용
//    @Query("SELECT SUM(m.price) FROM Reservation m WHERE m.stayId = :stayId")
//    Integer revenueForStay(@Param("stayId") String stayId);

    @Query("SELECT SUM(m.price) FROM Reservation m WHERE m.stayId = :stayId AND (m.createdAt >= :startDate AND m.createdAt <= :endDate)")
    Integer revenueForStayByDateRange(@Param("stayId") String stayId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT SUM(price) FROM Reservation  WHERE stayId = :stayId")
    Integer revenueForStayById(@Param("stayId") String stayId);
}
