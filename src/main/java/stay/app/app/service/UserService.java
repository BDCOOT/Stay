package stay.app.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import stay.app.app.models.Cart;
import stay.app.app.models.Mileage;
import stay.app.app.models.User;
import stay.app.app.repository.CartRepository;
import stay.app.app.repository.MileageRepository;
import stay.app.app.repository.StayRepository;
import stay.app.app.repository.UserRepository;
import stay.app.app.utils.Bcrypt;
import stay.app.app.utils.GeneratedId;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final StayRepository stayRepository;
    private final MileageRepository mileageRepository;

    private final Bcrypt bcrypt;
    private final GeneratedId generatedId;

    @Transactional
    public void saveUser(User user) throws Exception{
        try{
            userRepository.save(user);
        }catch(Exception e){
            throw new Exception(e);
        }
    }

    @Transactional
    public void modifyAppKey(User user) throws Exception{
        try{
            userRepository.save(user);
        }catch(Exception e){
            throw new Exception(e);
        }
    }


    public boolean singIn(String userId, String appKey) throws Exception{
        try{
            User findUser = userRepository.findOneByUserId(userId);
            Boolean checking = bcrypt.CompareHash(appKey, findUser.getAppKey());
            if(!checking) return false;
            return true;
        }catch(Exception e){
            throw new Exception(e);
        }
    }

    @Transactional
    public void createMileage(String userId) throws Exception{
        try{
            Mileage mileage = new Mileage();
            String shortUUID2 = generatedId.shortUUID();
            mileage.setId(shortUUID2);
            mileage.setUserId(userId);
            mileage.setPoint(0);
            mileageRepository.save(mileage);
        }catch(Exception e){
            throw new Exception(e);
        }
    }

    @Transactional
    public User findOneByUserId(String user_id) throws Exception{
        try{
            return userRepository.findOneByUserId(user_id);
        }catch(Exception e){
            throw new Exception(e);
        }
    }

    @Transactional
    public void unRegister(String id) throws Exception{
        try{
            userRepository.deleteById(id);
        }catch(Exception e){
            throw new Exception(e);
        }
    }

    public Cart findOneCartById(String id) throws Exception{
        try{
            return cartRepository.findOneById(id);
        }catch(Exception e){
            throw new Exception(e);
        }
    }

    @Transactional
    public void addCart(Cart cart) throws Exception{
        try{
            cartRepository.save(cart);
        }catch(Exception e){
            throw new Exception(e);
        }
    }

    @Transactional
    public void deleteCart(String cartId) throws Exception{
        try{
            cartRepository.deleteById(cartId);
        }catch(Exception e){
            throw new Exception(e);
        }
    }

    @Transactional
    public void deleteStayByAdmin(String userId) throws Exception{
        try{
            stayRepository.deleteAllByUserId(userId);
        }catch(Exception e){
            throw new Exception(e);
        }
    }



}//class 끝
