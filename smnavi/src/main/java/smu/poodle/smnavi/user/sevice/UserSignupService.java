package smu.poodle.smnavi.user.sevice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import smu.poodle.smnavi.user.domain.UserEntity;
import smu.poodle.smnavi.user.dto.LoginRequestDto;
import smu.poodle.smnavi.user.repository.UserRepository;

@Service
public class UserSignupService {
    @Autowired
    private final UserRepository userRepository;
    public UserSignupService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserEntity signup(LoginRequestDto loginRequestDto) {
        if (userRepository.findByEmail(loginRequestDto.getEmail()).isPresent()) {
            throw new RuntimeException("가입된 이메일이 이미 존재합니다.");
        }
        UserEntity user = new UserEntity();
        user.setEmail(loginRequestDto.getEmail());
        user.setPassword(passwordEncoder.encode(loginRequestDto.getPassword()));
        return userRepository.save(user);
    }

}
