package stay.app.app.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import stay.app.app.models.Room;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, String> {

    Room findOneById(String id);

    @Modifying
    @Query("UPDATE Room r SET r.allowed = :allowed WHERE r.stayId IN :stayIdList")
    void updateAllowedByStayIdList(@Param("allowed") boolean allowed, @Param("stayIdList") List<String> stayIdList);

    void deleteByStayIdIn(List<String> idList);

    //queryDSL 을 설치하면 쿼리문을 동적으로 할당할 수 있으니 추 후 도입해보세요
    @Query(value = "SELECT * FROM yb_room a WHERE not exists ( SELECT * FROM yb_reservation b WHERE a.id = b.room_id and (b.checkin_date BETWEEN :checkIn AND :checkOut OR b.checkout_date BETWEEN :checkIn AND :checkOut))  AND limited >= :limited", nativeQuery = true)
    List<Room> checkAvailableRoom(@Param("checkIn") LocalDateTime checkIn, @Param("checkOut") LocalDateTime checkOut,@Param("limited") String limited, Pageable pageable);

    @Query(value = "SELECT * FROM yb_room a WHERE not exists ( SELECT * FROM yb_reservation b WHERE a.id = b.room_id and (b.checkin_date BETWEEN :checkIn AND :checkOut OR b.checkout_date BETWEEN :checkIn AND :checkOut ))AND price between :minPrice AND :maxPrice AND limited >= :limited", nativeQuery = true)
    List<Room> checkAvailableRoomAndPrice(@Param("checkIn") LocalDateTime checkIn, @Param("checkOut") LocalDateTime checkOut,@Param("minPrice") String minPrice,@Param("maxPrice") String maxPrice,@Param("limited") String limited, Pageable pageable);

    @Query(value = "SELECT * FROM yb_room a WHERE not exists ( SELECT * FROM yb_reservation b WHERE a.id = b.room_id and (b.checkin_date BETWEEN :checkIn AND :checkOut OR b.checkout_date BETWEEN :checkIn AND :checkOut ))AND price >= :minPrice AND limited >= :limited", nativeQuery = true)
    List<Room> minPriceSortList(@Param("checkIn") LocalDateTime checkIn, @Param("checkOut") LocalDateTime checkOut,@Param("minPrice") String minPrice,@Param("limited") String limited, Pageable pageable);
    //stayId 설정했을 때
    @Query(value = "SELECT * FROM yb_room a WHERE a.stay_id = :stayId AND not exists ( SELECT * FROM yb_reservation b WHERE a.id = b.room_id and (b.checkin_date BETWEEN :checkIn AND :checkOut OR b.checkout_date BETWEEN :checkIn AND :checkOut))  AND limited >= :limited", nativeQuery = true)
    List<Room> checkAvailableRoomByStayId(@Param("stayId") String stayId, @Param("checkIn") LocalDateTime checkIn, @Param("checkOut") LocalDateTime checkOut,@Param("limited") String limited, Pageable pageable);

    @Query(value = "SELECT * FROM yb_room a WHERE a.stay_id = :stayId AND not exists ( SELECT * FROM yb_reservation b WHERE a.id = b.room_id and (b.checkin_date BETWEEN :checkIn AND :checkOut OR b.checkout_date BETWEEN :checkIn AND :checkOut ))AND price between :minPrice AND :maxPrice AND limited >= :limited", nativeQuery = true)
    List<Room> checkAvailableRoomAndPriceByStayId(@Param("stayId") String stayId, @Param("checkIn") LocalDateTime checkIn, @Param("checkOut") LocalDateTime checkOut,@Param("minPrice") String minPrice,@Param("maxPrice") String maxPrice,@Param("limited") String limited, Pageable pageable);

    @Query(value = "SELECT * FROM yb_room a WHERE a.stay_id = :stayId AND not exists ( SELECT * FROM yb_reservation b WHERE a.id = b.room_id and (b.checkin_date BETWEEN :checkIn AND :checkOut OR b.checkout_date BETWEEN :checkIn AND :checkOut ))AND price >= :minPrice AND limited >= :limited", nativeQuery = true)
    List<Room> minPriceSortListByStayId(@Param("stayId") String stayId, @Param("checkIn") LocalDateTime checkIn, @Param("checkOut") LocalDateTime checkOut,@Param("minPrice") String minPrice,@Param("limited") String limited, Pageable pageable);
}//interface
