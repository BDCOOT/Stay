package stay.app.app.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import stay.app.app.models.*;
import stay.app.app.service.StayService;
import stay.app.app.service.UserService;
import stay.app.app.utils.GeneratedId;
import stay.app.app.utils.ImageRegister;
import stay.app.app.utils.Jwt;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
@RequestMapping("/api/v1/stay")
public class StayController {

    private final StayService stayService;

    private final GeneratedId generatedId;
    private final Jwt jwt;
    private final ImageRegister imageRegister;

    @PostMapping("/add/room")
    public ResponseEntity<Object> addRoom(@RequestHeader String authorization,
                                          @ModelAttribute Room req,
                                          @RequestPart(required = false) MultipartFile[] image)throws Exception{
        Map<String, String> map = new HashMap<>();
        try{
            String shortUUID = generatedId.shortUUID();
            String decodedToeken = jwt.VerifyToken(authorization);

            req.setId(shortUUID);

            Stay stay = stayService.findOneById(req.getStayId());

            if(!(stay.getUserId().equals(decodedToeken))){
                map.put("result", "숙소 주인만 등록할 수 있습니다.");
                return new ResponseEntity<>(map,HttpStatus.OK);
            }

            if(image != null){
                List<String> images = imageRegister.CreateImages(image);
                String multiImages = String.join(",", images);

                req.setImg(multiImages);
            }else{
                req.setImg(null);
            }

            stayService.addRoom(req);
            map.put("result", "객실 등록 성공");
        }catch(Exception e){
            map.put("error", e.toString());
        }
        return new ResponseEntity<>(map,HttpStatus.OK);
    }

    @PostMapping("/modify/room")
    public ResponseEntity<Object> modifyRoom(@RequestHeader String authorization,
                                             @ModelAttribute Room req,
                                             @RequestPart(required = false) MultipartFile[] image,
                                             @RequestPart(required = false) String deleteImage)throws Exception{
        Map<String, String> map = new HashMap<>();
        try{
            String decodedToeken = jwt.VerifyToken(authorization);

            Stay stay = stayService.findOneById(req.getStayId());

            Room room = stayService.findOneByRoomId(req.getId());



            if(!(stay.getUserId().equals(decodedToeken))){
                map.put("result", "숙소 주인만 수정할 수 있습니다.");
                return new ResponseEntity<>(map,HttpStatus.OK);
            }

            List<String> previousImages = null;
            if(room.getImg() != null){
                previousImages = List.of(room.getImg().split(","));
            }

            List<String> modifyImages = new ArrayList<>(previousImages);

            if(deleteImage != null){
                List<String> deleteImages = List.of(deleteImage.split(","));
                for(String img : deleteImages){
                    modifyImages.remove(img);
                    imageRegister.DeleteFile(img);
                }
            }



            if(image != null){
                List<String> images = imageRegister.CreateImages(image);
                modifyImages.addAll(images);
            }
            String finalImage = String.join(",", modifyImages);
            room.setImg(finalImage);

            room.setRoomName(req.getRoomName());
            room.setBed(req.getBed());
            room.setPrice(req.getPrice());
            room.setLimited(req.getLimited());
            room.setDescription(req.getDescription());

            stayService.addRoom(room);
            map.put("result", "객실 수정 성공");
        }catch(Exception e){
            map.put("error", e.toString());
        }
        return new ResponseEntity<>(map,HttpStatus.OK);
    }


    @PostMapping("/delete/room")
    public ResponseEntity<Object> deleteRoom(@RequestHeader String authorization,
                                             @RequestBody Room req) throws Exception{
        Map<String, String> map = new HashMap<>();
        try{
            String decodedToeken = jwt.VerifyToken(authorization);

            Stay stay = stayService.findOneById(req.getStayId());

            Room room = stayService.findOneByRoomId(req.getId());


            if(!(stay.getUserId().equals(decodedToeken))){
                map.put("result", "숙소 주인만 삭제할 수 있습니다.");
                return new ResponseEntity<>(map,HttpStatus.OK);
            }

            if(room.getImg() != null){
                List<String> deleteImages = List.of(room.getImg().split(","));
                for(String img : deleteImages){
                    imageRegister.DeleteFile(img);
                }
            }
            stayService.deleteRoom(req.getId());
            map.put("result", "객실 삭제 성공");
        }catch(Exception e){
            map.put("error", e.toString());
        }
        return new ResponseEntity<>(map,HttpStatus.OK);
    }

    @PostMapping("/permit/reservation")
    public ResponseEntity<Object> permitReservation(@RequestHeader String authorization, @RequestBody Reservation req)throws Exception{
        Map<String, String> map = new HashMap<>();
        try{
            Reservation reservation = stayService.findOneReservationById(req.getId());

            String decodedToeken = jwt.VerifyToken(authorization);

            Stay stay = stayService.findOneById(req.getStayId());

            if(!(stay.getUserId().equals(decodedToeken))){
                map.put("result", "숙소 주인만 승인할 수 있습니다.");
                return new ResponseEntity<>(map,HttpStatus.OK);
            }

            reservation.setStatus(1);

            stayService.reservationRoom(reservation);
            map.put("result", "예약 승인 성공");
        }catch(Exception e){
            map.put("error", e.toString());
        }
        return new ResponseEntity<>(map,HttpStatus.OK);
    }


    @PostMapping("/cancel/reservation")
    public ResponseEntity<Object> cancelReservation(@RequestHeader String authorization, @RequestBody Reservation req)throws Exception{
        Map<String, String> map = new HashMap<>();
        try{
            Reservation reservation = stayService.findOneReservationById(req.getId());

            String decodedToeken = jwt.VerifyToken(authorization);

            Stay stay = stayService.findOneById(req.getStayId());

            if(!(stay.getUserId().equals(decodedToeken))){
                map.put("result", "숙소 주인만 예약을 취소할 수 있습니다.");
                return new ResponseEntity<>(map,HttpStatus.OK);
            }

            reservation.setStatus(2);

            stayService.reservationRoom(reservation);
            map.put("result", "예약 취소 성공");
        }catch(Exception e){
            map.put("error", e.toString());
        }
        return new ResponseEntity<>(map,HttpStatus.OK);
    }


    @GetMapping("/list")
    public ResponseEntity<Object> stayList(@RequestParam (value="stayType") String stayType,
                                        @RequestParam Integer page)throws Exception{
        Map<String, Object> map = new HashMap<>();

        try{
            //Pageable을 사용할 때 Page 수 표기 로직
            Integer offset = 0;
            if(page > 1){
                offset = page-1;
            }

            if(stayType == null) {
                map.put("result", "stayType을 입력해주세요.");
                return new ResponseEntity<>(map, HttpStatus.OK);
            }

            if(stayType.equals("all")){
                List<Stay> stayList = stayService.getAllStayListAllowedforUser(offset);
                map.put("result", stayList);
                return new ResponseEntity<>(map, HttpStatus.OK);
            }

            List<Stay> stayList = stayService.getStayTypeList(stayType, page);

            map.put("result", stayList);
        }catch(Exception e){
            map.put("error", e.toString());
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @GetMapping("/room/list")
    public ResponseEntity<Object> getRoomList(@RequestParam(required = false) String stayId,
                                              @RequestParam Integer page,
                                              @RequestParam String guestNum,
                                                @RequestParam(required = false) String minPrice,
                                                @RequestParam(required = false) String maxPrice,
                                              @RequestParam(value="checkIn") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime checkIn,
                                              @RequestParam(value="checkOut") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime checkOut
                                                )throws Exception{
        Map<String, Object> map = new HashMap<>();

        try{
            Integer offset = 0;
            if(page > 1 ){
                offset = page-1;
            }


            if(stayId == null){
            if(minPrice == null && maxPrice == null){
                //둘다 가격설정 안했을 경우
                List<Room> checkInAvailableroomList = stayService.getCheckInAvailableRooms(checkIn, checkOut, guestNum, offset);
                map.put("result", checkInAvailableroomList);
                return new ResponseEntity<>(map, HttpStatus.OK);
            }else if(minPrice != null && maxPrice != null){
                //둘다 가격설정 했을 경우
                List<Room> checkInAvailableroomListAndPrice = stayService.getCheckInAvailableRoomsAndPrice(checkIn, checkOut,minPrice,maxPrice, guestNum, offset);
                map.put("result", checkInAvailableroomListAndPrice);
                return new ResponseEntity<>(map, HttpStatus.OK);
            }else if(minPrice != null && maxPrice == null){
                //최소금액만 설정한 경우
                List<Room> minPriceSortList = stayService.getMinPriceSortList(checkIn, checkOut, minPrice, guestNum, offset);
                map.put("result", minPriceSortList);
                return new ResponseEntity<>(map, HttpStatus.OK);
            }else{
                map.put("result", "정의되지 않은 오류입니다.");
                }
            }

            if(stayId != null){
                if(minPrice == null && maxPrice == null){
                    //둘다 가격설정 안했을 경우
                    List<Room> checkInAvailableroomList = stayService.getCheckInAvailableRoomsByStayId(stayId, checkIn, checkOut, guestNum, offset);
                    map.put("result", checkInAvailableroomList);
                    return new ResponseEntity<>(map, HttpStatus.OK);
                }else if(minPrice != null && maxPrice != null){
                    //둘다 가격설정 했을 경우
                    List<Room> checkInAvailableroomListAndPrice = stayService.getCheckInAvailableRoomsAndPriceByStayId(stayId, checkIn, checkOut,minPrice,maxPrice, guestNum, offset);
                    map.put("result", checkInAvailableroomListAndPrice);
                    return new ResponseEntity<>(map, HttpStatus.OK);
                }else if(minPrice != null && maxPrice == null){
                    //최소금액만 설정한 경우
                    List<Room> minPriceSortList = stayService.getMinPriceSortListByStayId(stayId, checkIn, checkOut, minPrice, guestNum, offset);
                    map.put("result", minPriceSortList);
                    return new ResponseEntity<>(map, HttpStatus.OK);
                }else{
                    map.put("result", "정의되지 않은 오류입니다.");
                }
            }



        }catch(Exception e){
            map.put("error", e.toString());
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }



    @PostMapping("/closed/room")
    public ResponseEntity<Object> closedRoom(@RequestHeader String authorization,
                                                  @RequestBody Room req)throws Exception{
        Map<String, String> map = new HashMap<>();
        try{
            String decodedToken = jwt.VerifyToken(authorization);

            Room room = stayService.findOneByRoomId(req.getId());
            Stay stay = stayService.findOneByUserId(decodedToken);


            if(!(room.getStayId().equals(stay.getId()))){
                map.put("result", "숙소 주인만 접근할 수 있습니다.");
                return new ResponseEntity<>(map, HttpStatus.OK);
            }

            stayService.closedRoom(room);
            map.put("result", "객실 잠금이 성공했습니다.");
        }catch(Exception e){
            map.put("error", e.toString());
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PostMapping("/open/room")
    public ResponseEntity<Object> openRoom(@RequestHeader String authorization,
                                             @RequestBody Room req)throws Exception{
        Map<String, String> map = new HashMap<>();
        try{
            String decodedToken = jwt.VerifyToken(authorization);

            Room room = stayService.findOneByRoomId(req.getId());
            Stay stay = stayService.findOneByUserId(decodedToken);
            
            if(!(room.getStayId().equals(stay.getId()))){
                map.put("result", "숙소 주인만 접근할 수 있습니다.");
                return new ResponseEntity<>(map, HttpStatus.OK);
            }

            stayService.openRoom(room);
            map.put("result", "객실 오픈이 성공했습니다.");
        }catch(Exception e){
            map.put("error", e.toString());
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @GetMapping("/reservation/list")
    public ResponseEntity<Object> reservationList(@RequestHeader String authorization,
                                                  @RequestParam String stayId,
                                                  @RequestParam Integer page)throws Exception{
        Map<String, Object> map = new HashMap<>();
        try{
            Integer offset = 0;
            if(page > 1 ){
                offset = page-1;
            }
            System.out.println(offset);
            System.out.println(page);

            String decodedToken = jwt.VerifyToken(authorization);

            Stay stay = stayService.findOneById(stayId);

            if(!(stay.getUserId().equals(decodedToken))){
                map.put("error", "접근 권한이 없습니다.");
                return new ResponseEntity<>(map, HttpStatus.OK);
            }
            List<Reservation> reservationList = stayService.getReservationListByStayId(stayId, offset);
            map.put("result", reservationList);
        }catch(Exception e){
            map.put("error", e.toString());
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @GetMapping("/get/revenue")
    public ResponseEntity<Object> getRevenue(@RequestHeader String authorization,
                                             @RequestParam("stayId") String stayId,
                                             @RequestParam(value="startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
                                             @RequestParam(value="endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate)throws Exception{
        Map<String, String> map = new HashMap<>();
        try{
            String decodedToken = jwt.VerifyToken(authorization);

            Stay stay = stayService.findOneById(stayId);

            if(!(stay.getUserId().equals(decodedToken))){
                map.put("result", "접근 권한이 없습니다.");
                return new ResponseEntity<>(map, HttpStatus.OK);
            }

            if(startDate == null || endDate == null){
                int revenue = stayService.revenueByStay(stayId);
                map.put("result", String.valueOf(revenue));
                return new ResponseEntity<>(map, HttpStatus.OK);
            }

            int revenue = stayService.revenueSortDate(stayId, startDate, endDate);
            map.put("result", String.valueOf(revenue));
        }catch(Exception e){
            map.put("error", e.toString());
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }


}//class
