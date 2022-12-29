package smu.poodle.smnavi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smu.poodle.smnavi.domain.User;
import smu.poodle.smnavi.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) //작업 하나?
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public void join(User user){
        userRepository.save(user);
    }


    public List<User> all(){
        return userRepository.findAll();
    }

    @Transactional
    public void update(int id, String username){
        User user = userRepository.findOne(id);
        user.setUsername(username);
    }

    public void delete(int id){
        User user = userRepository.findOne(id);
        userRepository.delete(user);
    }




    //결제 배송정보 DB , 사용자 구매목록 DB ....
}
