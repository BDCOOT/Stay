package stay.app.app.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import stay.app.app.models.Stay;

import java.util.List;

@Repository
public interface StayRepository extends JpaRepository<Stay, String> {

    Stay findOneById(String id);

    Stay findOneByUserId(String userid);

    List<Stay> findAllByAllowedOrderByCreatedAtDesc(boolean allowed, Pageable page);

    List<Stay> findAllByAllowedOrderByRatingDesc(boolean allowed, Pageable pageable);

    @Query(value = "select * from yb_stay order by createdAt desc limit 2 offset :offset", nativeQuery = true)
    List<Stay> findAllByQuery(Integer offset);

    List<Stay> findAllByUserId(String userId);

    List<Stay> findAllByStayTypeAndAllowedOrderByCreatedAtDesc(String stayType, boolean allowed, Pageable page);

    @Modifying
    @Query("UPDATE Stay s SET s.allowed = :allowed WHERE s.id IN :idList")
    void updateAllowedByStayIdList(@Param("allowed") boolean allowed, @Param("idList") List<String> idList);

    //String으로 Id만 받아오는게 안되는데
//    List<String> findAllIdsByUserId(String userId);

//    @Query("SELECT s.id FROM Stay s WHERE s.userId = :userId")
//    List<String> findAllStayIdsByUserId(@Param("userId") String userId);

    void deleteAllByUserId(String userId);

    //    void deleteByIdIn(List<String> idList);

}
