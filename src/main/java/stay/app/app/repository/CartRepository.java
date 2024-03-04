package stay.app.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import stay.app.app.models.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, String> {

    Cart findOneById(String id);
}
