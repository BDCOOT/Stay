package stay.app.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import stay.app.app.models.User;


@Repository
public interface UserRepository extends JpaRepository<User, String> {

//    @Query("SELECT u FROM User u WHERE u.user_id = :user_id")
//    User findByUserId(String user_id);

      User findOneByUserId(String userId);




}
