package stay.app.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import stay.app.app.models.Stay;

import java.util.List;

@Repository
public interface StayRepository extends JpaRepository<Stay, String> {

    Stay findOneById(String id);

    Stay findOneByUserId(String userid);

    List<Stay> findAllByAllowed(boolean allowed);

    List<Stay> findAllByUserId(String userId);

    List<Stay> findAllByStayTypeAndAllowedOrderByStayTypeDesc(String stayType, boolean allowed);

    //String으로 Id만 받아오는게 안되는데
//    List<String> findAllIdsByUserId(String userId);

//    @Query("SELECT s.id FROM Stay s WHERE s.userId = :userId")
//    List<String> findAllStayIdsByUserId(@Param("userId") String userId);

    void deleteAllByUserId(String userId);

    //    void deleteByIdIn(List<String> idList);

}
