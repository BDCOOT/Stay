package stay.app.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import stay.app.app.models.User;


@Repository
public interface UserRepository extends JpaRepository<User, String> {

      User findOneByUserId(String userId);

}
