package stay.app.app.controller;

import lombok.Generated;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import stay.app.app.models.*;
import stay.app.app.repository.UserRepository;
import stay.app.app.service.CommentService;
import stay.app.app.service.StayService;
import stay.app.app.service.UserService;
import stay.app.app.utils.Bcrypt;
import stay.app.app.utils.GeneratedId;
import stay.app.app.utils.ImageRegister;
import stay.app.app.utils.Jwt;

import java.util.*;

@RestController
@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;
    private final StayService stayService;
    private final CommentService commentService;

    private final ImageRegister imageRegister;
    private final GeneratedId generatedId;
    private final Jwt jwt;
    private final Bcrypt bcrypt;

    //회원가입
    @PostMapping("/signup")
    public ResponseEntity<Object> signUp(@RequestBody User req)throws Exception  {
        Map<String, String> map = new HashMap<>();
        try {
            String shortUUID = generatedId.shortUUID();
            String hashPassword = bcrypt.HashPassword(req.getAppKey());
            req.setId(shortUUID);
            req.setAppKey(hashPassword);
            req.setGrade("bronze");
            req.setUserType("일반회원");
            req.setBanned(false);

            userService.createMileage(req.getUserId());
            userService.saveUser(req);
            map.put("result", "회원 가입 성공");
        } catch (Exception e) {
            map.put("error", e.toString());
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    //로그인
    @PostMapping("/signin")
    public ResponseEntity<Object> signIn(@RequestBody User req)throws Exception {
        Map<String, String> map = new HashMap<>();
        try{
            boolean login = userService.singIn(req.getUserId(), req.getAppKey());

            if(!login){
                map.put("result", "로그인 실패");
                return new ResponseEntity<>(map,HttpStatus.OK);
            }

            String authorization = jwt.CreateToken(req.getUserId());

            map.put("authorization", authorization);
        }catch(Exception e){
            map.put("error", e.toString());
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    //비밀번호 수정
    @PostMapping("/modify/appkey")
    public ResponseEntity<Object> modifyAppkey(@RequestHeader String authorization,
                                               @RequestBody Map<String, String> req) throws Exception {
        Map<String, String> map = new HashMap<>();
        try {
            String decodedToken = jwt.VerifyToken(authorization);

            User user = userService.findOneByUserId(decodedToken);

            Boolean checking = bcrypt.CompareHash(req.get("appKey"), user.getAppKey());

            if (!checking) {
                map.put("result", "비밀번호가 일치하지 않습니다. 변경 실패");
                return new ResponseEntity<>(map, HttpStatus.OK);
            }

            String modifyApp_key = bcrypt.HashPassword(req.get("newAppKey"));
            user.setAppKey(modifyApp_key);
            userService.modifyAppKey(user);

            map.put("result", "비밀번호 변경 성공");
        } catch (Exception e) {
            map.put("error", e.toString());
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

     @GetMapping("/get/userinfo")
     public ResponseEntity<Object> getUserInfo(@RequestHeader String authorization) throws Exception {
         Map<String, Object> map = new HashMap<>();
        try{

            String decodedToken = jwt.VerifyToken(authorization);

            User user = userService.findOneByUserId(decodedToken);

            if(!(user.getUserId().equals(decodedToken))){
                map.put("result", "잘못된 접근입니다.");
                return new ResponseEntity<>(map, HttpStatus.OK);
            }


            Mileage mileage = stayService.findOneMiliageByUserId(decodedToken);

            int offset = 1;
            List<Reservation> reservationList = stayService.findAllByUserId(decodedToken, offset);
            int total_amount = 0;
            for(Reservation reservation : reservationList){
                total_amount += reservation.getPrice();
            }

            int count = stayService.countReservation(decodedToken);

            map.put("userInfo", user);
            map.put("myPoint", mileage.getPoint());
            map.put("totalAmount", total_amount);
            map.put("totalFrequency", count);
        }catch(Exception e){
            map.put("error", e.toString());
        }
         return new ResponseEntity<>(map, HttpStatus.OK);
     }


     //회원탈퇴
     @PostMapping("/unregister")
     public ResponseEntity<Object> unRegister(@RequestHeader String authorization,
                                                @RequestBody User req)throws Exception {
         Map<String, String> map = new HashMap<>();
        try{
            String decodedToken = jwt.VerifyToken(authorization);

            User user = userService.findOneByUserId(decodedToken);

            System.out.println(user.getUserId());

            if(!(user.getUserId().equals(decodedToken))){
                map.put("result", "아이디가 일치하지 않습니다.");
                return new ResponseEntity<>(map, HttpStatus.OK);
            }

            Boolean checking = bcrypt.CompareHash(req.getAppKey(), user.getAppKey());

            if(!checking){
                map.put("result", "비밀번호가 일치하지 않습니다.");
                return new ResponseEntity<>(map, HttpStatus.OK);
            }
            userService.unRegister(user.getId());
            map.put("result", "회원 탈퇴 성공");
        }catch(Exception e){
            map.put("error", e.toString());
        }
         return new ResponseEntity<>(map, HttpStatus.OK);
     }


     @PostMapping("/apply/stay")
     public ResponseEntity<Object> applyStay(@RequestHeader String authorization,
                                            @RequestBody  Stay req)throws Exception {
         Map<String, String> map = new HashMap<>();
        try{
            String shortUUID = generatedId.shortUUID();
            String decodedToken = jwt.VerifyToken(authorization);

            User user = userService.findOneByUserId(decodedToken);

            if(user.getUserType().equals("일반회원")){
                user.setUserType("판매회원");
                userService.saveUser(user);
            }

            req.setId(shortUUID);
            req.setUserId(decodedToken);

            stayService.applyStay(req);
            map.put("result", "숙소 신청 성공");
        }catch(Exception e){
            map.put("error", e.toString());

        }
         return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PostMapping("/reservation/room")
    public ResponseEntity<Object> reservationRoom(@RequestHeader String authorization,
                                                  @RequestBody Reservation req){
        Map<String, String> map = new HashMap<>();
        try{
            String decodedToken = jwt.VerifyToken(authorization);

            String shortUUID = generatedId.shortUUID();

            req.setId(shortUUID);
            req.setUserId(decodedToken);

            int totalPrice = req.getPrice();

            if(req.getUsePoint() != 0){
              Mileage mileage = stayService.findOneMiliageByUserId(decodedToken);

              if(mileage.getPoint() < req.getUsePoint()){
                  map.put("result", "보유한 마일리지보다 적은 수를 입력해주세요.");
                  return new ResponseEntity<>(map, HttpStatus.OK);
              }

              totalPrice = totalPrice - req.getUsePoint();
              mileage.setPoint(mileage.getPoint() - req.getUsePoint());
            }

            req.setPrice(totalPrice);

            stayService.reservationRoom(req);

            stayService.saveByGradeMiliage(decodedToken, totalPrice);
            map.put("result", "예약 완료");
        }catch(Exception e){
            map.put("error", e.toString());
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PostMapping("/reservation/cancel")
    public ResponseEntity<Object> reservationCancel(@RequestHeader String authorization,
                                                  @RequestBody Reservation req){
        Map<String, String> map = new HashMap<>();
        try{
            String decodedToken = jwt.VerifyToken(authorization);

            Reservation reservation = stayService.findOneReservationById(req.getId());

            System.out.println(decodedToken);
            if(!(reservation.getUserId().equals(decodedToken))){
                map.put("result", "예약한 본인만 취소할 수 있습니다.");
                return new ResponseEntity<>(map, HttpStatus.OK);
            }

            stayService.reservationCancel(req.getId());
            map.put("reulst", "예약이 취소에 성공하였습니다.");
        }catch(Exception e){
            map.put("error", e.toString());
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @GetMapping("/my/reservation")
    public ResponseEntity<Object> myReservation(@RequestHeader String authorization,
                                                @RequestParam Integer page
                                            ){
        Map<String, Object> map = new HashMap<>();
        try{
            Integer offset = 0;
            if(page > 1 ){
                offset = page-1;
            }

            String decodedToken = jwt.VerifyToken(authorization);

            List<Reservation> reservationList = stayService.findAllByUserId(decodedToken, offset);

            map.put("result", reservationList);
        }catch(Exception e){
            map.put("error", e.toString());
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PostMapping("/add/cart")
    public ResponseEntity<Object> addCart(@RequestHeader String authorization,
                                                  @RequestBody Cart req){
        Map<String, String> map = new HashMap<>();
        try{
            String decodedToken = jwt.VerifyToken(authorization);
            String shortUUID = generatedId.shortUUID();

            req.setId(shortUUID);
            req.setUserId(decodedToken);

            userService.addCart(req);
            map.put("result", "장바구니 담기 성공");
        }catch(Exception e){
            map.put("error", e.toString());
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PostMapping("/delete/cart")
    public ResponseEntity<Object> deleteCart(@RequestHeader String authorization,
                                                  @RequestBody Cart req){
        Map<String, String> map = new HashMap<>();
        try{
            String decodedToken = jwt.VerifyToken(authorization);

            Cart cart = userService.findOneCartById(req.getId());

            if(!(decodedToken.equals(cart.getUserId()))){
                map.put("result", "본인만 장바구니 삭제를 시도할 수 있습니다.");
                return new ResponseEntity<>(map, HttpStatus.OK);
            }

            userService.deleteCart(req.getId());
            map.put("result", "장바구니 삭제 성공");
        }catch(Exception e){
            map.put("error", e.toString());
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    //comment, recomment, review
    @PostMapping("/write/comment")
    public ResponseEntity<Object> writeComment (@RequestHeader String authorization,
                                                @RequestBody Comment req) throws Exception{
        Map<String, String> map = new HashMap<>();
        try{
            String shrotUUID = generatedId.shortUUID();
            String decodedToken = jwt.VerifyToken(authorization);

            req.setId(shrotUUID);
            req.setUserId(decodedToken);

            commentService.writeComment(req);

            map.put("result", "문의 작성 성공");
        }catch(Exception e){
            map.put("error", e.toString());
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PostMapping("/modify/comment")
    public ResponseEntity<Object> modifyComment (@RequestHeader String authorization,
                                                 @RequestBody Comment req) throws Exception {
        Map<String, String> map = new HashMap<>();
        try{
            String decodedToken = jwt.VerifyToken(authorization);

            Comment comment = commentService.findOneById(req.getId());

            if(!(comment.getUserId().equals(decodedToken))){
                map.put("result", "작성자만 수정할 수 있습니다.");
                return new ResponseEntity<>(map, HttpStatus.OK);
            }
            comment.setDescription(req.getDescription());

            commentService.writeComment(comment);
            map.put("result", "문의 수정 완료.");
        }catch(Exception e){
            map.put("error", e.toString());
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PostMapping("/delete/comment")
    public ResponseEntity<Object> deleteComment(@RequestHeader String authorization,
                                                @RequestBody Comment req)throws Exception{
        Map<String, String> map = new HashMap<>();
        try{
            String decodedToken = jwt.VerifyToken(authorization);

            Comment comment = commentService.findOneById(req.getId());

            if(!(comment.getUserId().equals(decodedToken))){
                map.put("result", "작성자만 삭제할 수 있습니다.");
                return new ResponseEntity<>(map, HttpStatus.OK);
            }

            commentService.deleteByCommentId(req.getId());
            map.put("result", "문의 삭제 완료.");
        }catch(Exception e){
            map.put("error", e.toString());
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PostMapping("/write/recomment")
    public ResponseEntity<Object> writeRecomment(@RequestHeader String authorization,
                                                  @RequestBody Recomment req){
        Map<String, String> map = new HashMap<>();
        try{
            String decodedToken = jwt.VerifyToken(authorization);

            Stay stay = stayService.findOneByUserId(decodedToken);
            Comment comment = commentService.findOneById(req.getCommentId());

            if(!(comment.getStayId().equals(stay.getId()))){
                map.put("result", "숙소 주인만 답글을 남길 수 있습니다.");
                return new ResponseEntity<>(map, HttpStatus.OK);
            }

            String shrotUUID = generatedId.shortUUID();
            req.setId(shrotUUID);
            req.setUserId(decodedToken);

            commentService.writeRecomment(req);
            map.put("result", "답글 작성 완료");
        }catch(Exception e){
            map.put("error", e.toString());
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PostMapping("/delete/recomment")
    public ResponseEntity<Object> deleteRecomment(@RequestHeader String authorization,
                                                 @RequestBody Recomment req){
        Map<String, String> map = new HashMap<>();
        try{
            String decodedToken = jwt.VerifyToken(authorization);

            Recomment recomment = commentService.findOneByRecoomentId(req.getId());

            if(!(recomment.getUserId().equals(decodedToken))){
                map.put("result", "숙소 주인만 답글을 삭제할 수 있습니다.");
                return new ResponseEntity<>(map, HttpStatus.OK);
            }

            commentService.deleteRecomment(req.getId());
            map.put("result", "답글 삭제 완료");
        }catch(Exception e){
            map.put("error", e.toString());
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }


    @PostMapping("/write/review")
    public ResponseEntity<Object> writeReview(@RequestHeader String authorization,
                                              @ModelAttribute Review req,
                                              @RequestPart(required = false) MultipartFile[] image){
        Map<String, String> map = new HashMap<>();
        try{
            String decodedToken = jwt.VerifyToken(authorization);

            Reservation reservation = stayService.findOneReservationById(req.getReservationId());
            
            if(!(reservation.getUserId().equals(decodedToken))){
                map.put("result", "구매자만 리뷰를 작성할 수 있습니다.");
                return new ResponseEntity<>(map, HttpStatus.OK);
            }

            if(req.getRating() < 0 || req.getRating() > 5){
                map.put("result", "점수는 1부터 5까지 입력가능합니다.");
                return new ResponseEntity<>(map, HttpStatus.OK);
            }

            String shrotUUID = generatedId.shortUUID();

            req.setId(shrotUUID);
            req.setUserId(decodedToken);

            if(image != null){
                List<String> images = imageRegister.CreateImages(image);
                String multiImages = String.join(",", images);

                req.setImg(multiImages);
            }else{
                req.setImg(null);
            }


            commentService.checkReviewExist(req.getReservationId());
            commentService.writeReview(req);

            map.put("result", "리뷰 작성 완료");
        }catch(Exception e){
            map.put("error", e.toString());
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }


    @PostMapping("/modify/review")
    public ResponseEntity<Object> modifyReview(@RequestHeader String authorization,
                                               @ModelAttribute Review req,
                                               @RequestPart(required = false) MultipartFile[] image,
                                               @RequestPart(required = false) String deleteImage){
        Map<String, String> map = new HashMap<>();
        try{
            String decodedToken = jwt.VerifyToken(authorization);

            Review previousReview = commentService.findOneByReviewId(req.getId());

            System.out.println(deleteImage);

            if(!(previousReview.getUserId().equals(decodedToken))){
                map.put("result", "작성자만 리뷰를 수정할 수 있습니다.");
                return new ResponseEntity<>(map, HttpStatus.OK);
            }

            if(req.getRating() < 0 || req.getRating() > 5){
                map.put("result", "점수는 1부터 5까지 입력가능합니다.");
                return new ResponseEntity<>(map, HttpStatus.OK);
            }


            //기존이미지 담음
            List<String> previousImages = null;
            if(previousReview.getImg() != null){
                previousImages = List.of(previousReview.getImg().split(","));
            }


            List<String> modifyImages = new ArrayList<>(previousImages);

            //삭제할 이미지가 있으면
//            if(req.getDeleteImage() != null){
//                List<String> deleteImages = List.of(req.getDeleteImage().split(","));
//                for(String img : deleteImages){
//                    //기존이미지에서 삭제
//                    modifyImages.remove(img);
//                    //경로 찾아 파일 삭제
//                    imageRegister.DeleteFile(img);
//                }
//            }

            //추가로 이미지 삽입
            if(image != null){
                List<String> images = imageRegister.CreateImages(image);
                modifyImages.addAll(images);
            }
            String finalImage = String.join(",", modifyImages);
            req.setImg(finalImage);

            previousReview.setRating(req.getRating());
            previousReview.setDescription(req.getDescription());

            commentService.writeReview(previousReview);
            map.put("result", "리뷰 수정 완료");
        }catch(Exception e){
            map.put("error", e.toString());
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PostMapping("/delete/review")
    public ResponseEntity<Object> deleteReview(@RequestHeader String authorization,
                                               @RequestBody Review req){
        Map<String, String> map = new HashMap<>();
        try{
            String decodedToken = jwt.VerifyToken(authorization);

            Review review = commentService.findOneByReviewId(req.getId());

            if(!(review.getUserId().equals(decodedToken))){
                map.put("result", "작성자만 리뷰를 삭제할 수 있습니다.");
                return new ResponseEntity<>(map, HttpStatus.OK);
            }

            commentService.deleteByReviewId(req.getId());
            map.put("result", "리뷰 삭제 완료");
        }catch(Exception e){
            map.put("error", e.toString());
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }




    //관리자 API 리스트
    @GetMapping("/admin/stay/list")
    public ResponseEntity<Object> stayListByAdmin (@RequestHeader  String authorization,
                                              @RequestParam Integer page,
                                              @RequestParam(value="allowed", required=false) boolean allwoed)throws Exception {
        Map<String, Object> map = new HashMap<>();
        try{
            Integer offset = 0;
            if(page > 1 ){
                offset = page-1;
            }

            String decodedToken = jwt.VerifyToken(authorization);

            User user = userService.findOneByUserId(decodedToken);

            if(!user.getUserType().equals("관리자")){
                map.put("result", "관리자만 접근할 수 있습니다.");
                return new ResponseEntity<>(map, HttpStatus.OK);
            }



            if(allwoed){
                List<Stay> allowedList = stayService.getAllowedSortStayList(allwoed, offset);
                System.out.println("allowed");
                map.put("result", allowedList);
                return new ResponseEntity<>(map, HttpStatus.OK);
            } else if(!allwoed){
                List<Stay> allowedList = stayService.getAllowedSortStayList(allwoed, offset);
                System.out.println("!allowed");
                map.put("result", allowedList);
                return new ResponseEntity<>(map, HttpStatus.OK);
            }

        }catch(Exception e){
            map.put("error", e.toString());
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PostMapping("/admin/permit/stay")
    public ResponseEntity<Object> permitSellerByAdmin(@RequestHeader String authorization, @RequestBody Stay req)throws Exception {
        Map<String, String> map = new HashMap<>();
        try{
            String decodedToken = jwt.VerifyToken(authorization);

            User user = userService.findOneByUserId(decodedToken);

            if(!user.getUserType().equals("관리자")){
                map.put("result", "관리자만 접근할 수 있습니다.");
                return new ResponseEntity<>(map, HttpStatus.OK);
            }

            Stay stay = stayService.findOneById(req.getId());
            stay.setAllowed(true);
            stayService.permitStay(stay);

            map.put("result", "관리자 권한으로 숙소 등록 성공");
        }catch(Exception e){
            map.put("error", e.toString());
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PostMapping("/admin/ban")
    public ResponseEntity<Object> userBanByAdmin(@RequestHeader String authorization,
                                                  @RequestBody User req){
        Map<String, String> map = new HashMap<>();
        try{
            String decodedToken = jwt.VerifyToken(authorization);

            User user = userService.findOneByUserId(decodedToken);
            if(!(user.getUserType().equals("관리자"))){
                map.put("result", "관리자만 접근할 수 있습니다.");
                return new ResponseEntity<>(map, HttpStatus.OK);
            }

            User banUser = userService.findOneByUserId(req.getUserId());
            System.out.println(banUser);

            banUser.setBanned(true);
            userService.saveUser(banUser);

            //각각 stayid 로 쿼리문을 사용해서 Allowed를 한번에 false로 업데이트 해주면됨
            List<Stay> stayList = stayService.getStayListByUserId(banUser.getUserId());
            List<String> stayIds = new ArrayList<>();
            for(Stay stay : stayList){
                stayIds.add(stay.getId());
            }
            stayService.updateStaysFalse(stayIds);
            stayService.updateRoomsFalse(stayIds);

            map.put("result", "reqUser가 밴 되었으며, reqUser의 숙소와 객실이 잠겼습니다.");
        }catch(Exception e){
            map.put("error", e.toString());
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }


    @PostMapping("/admin/delete/stay")
    public ResponseEntity<Object> deleteStayByAdmin(@RequestHeader String authorization,
                                                  @RequestBody User req){
        Map<String, String> map = new HashMap<>();
        try{
            String decodedToken = jwt.VerifyToken(authorization);

            User user = userService.findOneByUserId(decodedToken);
            if(!(user.getUserType().equals("관리자"))){
                map.put("result", "관리자만 접근할 수 있습니다.");
                return new ResponseEntity<>(map, HttpStatus.OK);
           }


            List<Stay> stayList = stayService.getStayListByUserId(req.getUserId());
            List<String> stayIdsList = new ArrayList<>();
            for(Stay stay : stayList){
                stayIdsList.add(stay.getId());
            }

            stayService.deleteRoomList(stayIdsList);
            userService.deleteStayByAdmin(req.getUserId());

            map.put("result", "관리자 권한으로 숙소 및 객실 삭제 성공");
        }catch(Exception e){
            map.put("error", e.toString());
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PostMapping("/admin/delete/review")
    public ResponseEntity<Object> deleteReviewByAdmin(@RequestHeader String authorization,
                                                 @RequestBody Review req){
        Map<String, String> map = new HashMap<>();
        try{
            String decodedToken = jwt.VerifyToken(authorization);

            User user = userService.findOneByUserId(decodedToken);
            if(!(user.getUserType().equals("관리자"))){
                map.put("result", "관리자만 접근할 수 있습니다.");
                return new ResponseEntity<>(map, HttpStatus.OK);
            }
            
            commentService.deleteByReviewId(req.getId());
            map.put("result", "관리자만 권한으로 리뷰 삭제 성공");
        }catch(Exception e){
            map.put("error", e.toString());
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PostMapping("/admin/cancel/reservation")
    public ResponseEntity<Object> cancelReservationByAdmin(@RequestHeader String authorization,
                                               @RequestBody Reservation req){
        Map<String, String> map = new HashMap<>();
        try{
            String decodedToken = jwt.VerifyToken(authorization);

            User user = userService.findOneByUserId(decodedToken);
            if(!(user.getUserType().equals("관리자"))){
                map.put("result", "관리자만 접근할 수 있습니다.");
                return new ResponseEntity<>(map, HttpStatus.OK);
            }

            Reservation reservation = stayService.findOneReservationById(req.getId());
            stayService.cancelReservationByAdmin(reservation);
            map.put("result", "관리자 권한으로 예약을 취소합니다.");
        }catch(Exception e){
            map.put("error", e.toString());
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }


}//class끝



//    @PostMapping("/")
//    public ResponseEntity<Object> method(@RequestHeader String Authorization,
//                                                  @RequestBody Reservation req){
//        Map<String, String> map = new HashMap<>();
//        try{
//
//        }catch(Exception e){
//            map.put("error", e.toString());
//        }
//        return new ResponseEntity<>(map, HttpStatus.OK);
//    }