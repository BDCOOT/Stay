package stay.app.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import stay.app.app.models.*;
import stay.app.app.repository.*;
import stay.app.app.utils.MileagePer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StayService {

    private final UserRepository userRepository;
    private final StayRepository stayRepository;
    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;
    private final MileageRepository mileageRepository;
    private final MileagePer mileagePer;

    @Transactional
    public void applyStay(Stay stay) throws Exception {
        try{
            stayRepository.save(stay);
        }catch(Exception e){
            throw new Exception(e);
        }
    }

    public List<Stay> getAllowedSortStayList(boolean allowed, Integer offset) throws Exception{
        try{
            Pageable page = PageRequest.of(offset, 2);
            return stayRepository.findAllByAllowedOrderByCreatedAtDesc(allowed, page);
        }catch(Exception e){
            throw new Exception(e);
        }
    }

    public List<Stay> getStayListByUserId(String userId) throws Exception{
        try{
            return stayRepository.findAllByUserId(userId);
        }catch(Exception e){
            throw new Exception(e);
        }
    }


    public List<Stay> getAllStayListAllowedforUser(Integer offset) throws Exception{
        try{
            Pageable pageable = PageRequest.of(offset, 2);
            return stayRepository.findAllByAllowedOrderByRatingDesc(true, pageable);
        }catch(Exception e){
            throw new Exception(e);
        }
    }


    public List<Stay> getStayTypeList(String stayType, Integer offset) throws Exception{
        try{
            Pageable page = PageRequest.of(offset, 2);
            return stayRepository.findAllByStayTypeAndAllowedOrderByCreatedAtDesc(stayType, true, page);
        }catch(Exception e){
            throw new Exception(e);
        }

    }

    @Transactional
    public void updateStaysFalse(List<String> idList) throws Exception{
        try{
            stayRepository.updateAllowedByStayIdList(false, idList);
        }catch(Exception e){
            throw new Exception(e);
        }
    }

    @Transactional
    public void updateRoomsFalse(List<String> idList) throws Exception{
        try{
           roomRepository.updateAllowedByStayIdList(false, idList);
        }catch(Exception e){
            throw new Exception(e);
        }
    }

    public Stay findOneByUserId(String userid) throws Exception{
        try{
            return stayRepository.findOneByUserId(userid);
        }catch(Exception e){
            throw new Exception(e);
        }
    }

    public Stay findOneById(String id) throws Exception{
        try{
            return stayRepository.findOneById(id);
        }catch(Exception e){
            throw new Exception(e);
        }
    }

    @Transactional
    public void permitStay(Stay stay) throws Exception{
        try{
            stayRepository.save(stay);
        }catch(Exception e){
            throw new Exception(e);
        }
    }

    @Transactional
    public void addRoom(Room room) throws Exception{
        try{
            room.setReservationStatus(true);
            room.setAllowed(true);
            roomRepository.save(room);
        }catch(Exception e){
            throw new Exception(e);
        }
    }

    @Transactional
    public void deleteRoom(String id) throws Exception{
        try{
            roomRepository.deleteRoomById(id);
        }catch(Exception e){
            throw new Exception(e);
        }

    }
    //스테이아이디 포함안했을때
    public List<Room> getCheckInAvailableRooms(LocalDateTime checkIn, LocalDateTime checkOut,String limited, Integer offset) throws Exception{
        try{
            Pageable page = PageRequest.of(offset, 2);
            return roomRepository.checkAvailableRoom(checkIn, checkOut,limited, page);
        }catch(Exception e){
            throw new Exception(e);
        }
    }

    public List<Room> getCheckInAvailableRoomsAndPrice(LocalDateTime checkIn, LocalDateTime checkOut, String minPrice, String maxPrice, String limited, Integer offset) throws Exception{
        try{
            Pageable page = PageRequest.of(offset, 2);
            return roomRepository.checkAvailableRoomAndPrice(checkIn, checkOut, minPrice, maxPrice, limited, page);
        }catch(Exception e){
            throw new Exception(e);
        }
    }

    public List<Room> getMinPriceSortList(LocalDateTime checkIn, LocalDateTime checkOut, String minPrice, String limited, Integer offset) throws Exception{
        try{
            Pageable page = PageRequest.of(offset, 2);
            return roomRepository.minPriceSortList(checkIn, checkOut, minPrice, limited, page);
        }catch(Exception e){
            throw new Exception(e);
        }
    }


    // 스테이아이디 포함했을때
    public List<Room> getCheckInAvailableRoomsByStayId(String stayId, LocalDateTime checkIn, LocalDateTime checkOut,String limited, Integer offset) throws Exception{
        try{
            Pageable page = PageRequest.of(offset, 2);
            return roomRepository.checkAvailableRoomByStayId(stayId, checkIn, checkOut, limited, page);
        }catch(Exception e){
            throw new Exception(e);
        }
    }

    public List<Room> getCheckInAvailableRoomsAndPriceByStayId(String stayId, LocalDateTime checkIn, LocalDateTime checkOut, String minPrice, String maxPrice, String limited, Integer offset) throws Exception{
        try{
            Pageable page = PageRequest.of(offset, 2);
            return roomRepository.checkAvailableRoomAndPriceByStayId(stayId, checkIn, checkOut, minPrice, maxPrice, limited, page);
        }catch(Exception e){
            throw new Exception(e);
        }
    }

    public List<Room> getMinPriceSortListByStayId(String stayId, LocalDateTime checkIn, LocalDateTime checkOut, String minPrice, String limited, Integer offset) throws Exception{
        try{
            Pageable page = PageRequest.of(offset, 2);
            return roomRepository.minPriceSortListByStayId(stayId, checkIn, checkOut, minPrice, limited, page);
        }catch(Exception e){
            throw new Exception(e);
        }
    }

    public Mileage findOneMiliageByUserId(String userId) throws Exception{
        try{
            return mileageRepository.findOneByUserId(userId);
        }catch(Exception e){
            throw new Exception(e);
        }
    }


    @Transactional
    public void saveByGradeMiliage(String userId, int price) throws Exception {
        try{
            Mileage mileage = mileageRepository.findOneByUserId(userId);

            User user = userRepository.findOneByUserId(userId);
            if(user.getGrade().equals("bronze")){
                price = (int) (price * mileagePer.getBronze());
            }
            if(user.getGrade().equals("silver")){
                price = (int) (price * mileagePer.getSilver());
            }
            if(user.getGrade().equals("gold")){
                price = (int) (price * mileagePer.getGold());
            }
            if(user.getGrade().equals("platinum")){
                price = (int) (price * mileagePer.getPlatinum());
            }
            if(user.getGrade().equals("diamond")){
                price = (int) (price * mileagePer.getDiamond());
            }
            mileage.setPoint(mileage.getPoint() + price);
            mileageRepository.save(mileage);
        }catch(Exception e){
            throw new Exception(e);
        }
    }

    //숙소 예약
    @Transactional
    public void reservationRoom(Reservation reservation) throws Exception{
        try{
            reservationRepository.save(reservation);
        }catch(Exception e){
            throw new Exception(e);
        }
    }

    @Transactional
    public void cancelReservationByAdmin(Reservation reservation) throws Exception{
        try{
            reservation.setStatus(2);
            reservationRepository.save(reservation);
        }catch(Exception e){
            throw new Exception(e);
        }
    }

    @Transactional
    public void reservationCancel(String reservationId) throws Exception{
        try{
            reservationRepository.deleteById(reservationId);
        }catch(Exception e){
            throw new Exception(e);
        }
    }

    @Transactional
    public Reservation findOneReservationById(String id) throws Exception{
        try{
            return reservationRepository.findOneById(id);
        }catch(Exception e){
            throw new Exception(e);
        }
    }



    public List<Reservation> findAllByUserId(String userId, Integer offset) throws Exception{
        try{
            Pageable page = PageRequest.of(offset, 2);
            return reservationRepository.findAllByUserIdOrderByCreatedAtDesc(userId, page);
        }catch(Exception e){
            throw new Exception(e);
        }
    }


    public Room findOneByRoomId(String id) throws Exception{
        try{
            return roomRepository.findOneById(id);
        }catch(Exception e){
            throw new Exception(e);
        }
    }



    @Transactional
    public void closedRoom(Room room) throws Exception{
        try{
            room.setReservationStatus(false);
            roomRepository.save(room);
        }catch(Exception e){
            throw new Exception(e);
        }
    }

    @Transactional
    public void openRoom(Room room) throws Exception{
        try{
            room.setReservationStatus(true);
            roomRepository.save(room);
        }catch(Exception e){
            throw new Exception(e);
        }
    }


    public List<Reservation> getReservationListByStayId(String stayId, Integer offset) throws Exception{
        try{
            Pageable page = PageRequest.of(offset, 2);
            System.out.println(page);
            return reservationRepository.findAllByStayIdOrderByCreatedAtDesc(stayId, page);
        }catch(Exception e){
            throw new Exception(e);
        }
    }


    public int revenueByStay(String stayId) throws Exception{
        try{
            return reservationRepository.revenueForStayById(stayId);
        }catch(Exception e){
            throw new Exception(e);
        }
    }

    public int revenueSortDate(String stayId, LocalDateTime startDate, LocalDateTime endDate) throws Exception{
        try{
            return reservationRepository.revenueForStayByDateRange(stayId, startDate, endDate);
        }catch(Exception e){
            throw new Exception(e);
        }
    }



    @Transactional
    public void deleteRoomList(List<String> stayIdsList) throws Exception{
        try{
            roomRepository.deleteByStayIdIn(stayIdsList);
        }catch(Exception e){
            throw new Exception(e);
        }
    }


    public int countReservation(String userId) throws Exception {
        try{
            return reservationRepository.countByUserId(userId);
        }catch(Exception e){
            throw new Exception(e);
        }
    }
}//class

