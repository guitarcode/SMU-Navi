package smu.poodle.smnavi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import smu.poodle.smnavi.domain.User;
import smu.poodle.smnavi.service.UserService;

@RestController
//Rest API ??
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/api/user")
    public User join(User user){
        userService.join(user);
        return user;
    }
}
