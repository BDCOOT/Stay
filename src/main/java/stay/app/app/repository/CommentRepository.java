package stay.app.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import stay.app.app.models.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, String> {

    Comment findOneById(String id);
}
