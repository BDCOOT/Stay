package stay.app.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import stay.app.app.models.Comment;
import stay.app.app.models.Recomment;
import stay.app.app.models.Review;
import stay.app.app.repository.CommentRepository;
import stay.app.app.repository.RecommentRepository;
import stay.app.app.repository.ReviewRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final RecommentRepository recommentRepository;
    private final ReviewRepository reviewRepository;
    private final StayService stayService;

    //Transactional : JPA는 기본적으로 Transactional 이 걸려있다. 그러므로 readonly=true로 초기화를 시키고 필요한 작업에만 Transactional 을 달아준다.

    @Transactional
    public void writeComment(Comment comment) throws Exception{
        try{
            commentRepository.save(comment);
        }catch(Exception e){
            throw new Exception(e);
        }
    }


    public Comment findOneById(String id) throws Exception{
        try{
            return commentRepository.findOneById(id);
        }catch(Exception e){
            throw new Exception(e);
        }
    }



    @Transactional
    public void deleteByCommentId(String id) throws Exception{
        try{
            commentRepository.deleteById(id);
        }catch(Exception e){
            throw new Exception(e);
        }
    }


    public Recomment findOneByRecoomentId(String recommentId) throws Exception{
        try{
            return recommentRepository.findOneById(recommentId);
        }catch(Exception e){
            throw new Exception(e);
        }
    }

    @Transactional
    public void writeRecomment(Recomment recomment) throws Exception{
        try{
            recommentRepository.save(recomment);
        }catch(Exception e){
            throw new Exception(e);
        }
    }

    @Transactional
    public void deleteRecomment(String recommentId) throws Exception{
        try{
            recommentRepository.deleteById(recommentId);
        }catch(Exception e){
            throw new Exception(e);
        }
    }

    @Transactional
    public void writeReview(Review review) throws Exception{
        try{
            reviewRepository.save(review);
        }catch(Exception e){
            throw new Exception(e);
        }
    }


    public void checkReviewExist(String reservationId) throws Exception{
        try{
            boolean reviewExists = reviewRepository.existsByReservationId(reservationId);
            if(reviewExists)  throw new IllegalStateException("이미 리뷰가 작성되었습니다.");
        }catch(Exception e){
            throw new Exception(e);
        }
    }


    public Review findOneByReviewId(String id) throws Exception{
        try{
            return reviewRepository.findOneById(id);
        }catch(Exception e){
            throw new Exception(e);
        }
    }

    @Transactional
    public void deleteByReviewId(String id) throws Exception{
        try{
            reviewRepository.deleteById(id);
        }catch(Exception e){
            throw new Exception(e);
        }
    }

}//class
