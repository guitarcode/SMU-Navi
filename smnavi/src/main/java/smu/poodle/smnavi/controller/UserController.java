package smu.poodle.smnavi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import smu.poodle.smnavi.domain.User;
import smu.poodle.smnavi.service.UserService;

import java.util.List;

@RestController
//Rest API ??
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/api/user")
    public User join(@RequestBody User user){
        userService.join(user);
        return user;
    }

    @GetMapping("/api/user")
    public List<User> findAll(){
        return userService.all();
    }


    @PutMapping("/api/user/{userId}")
    public long updateUser(@PathVariable int userId, @RequestBody User user){
        userService.update(userId, user.getUsername());
        return userId;
    }

    @DeleteMapping("/api/user/{userId}")
    public long deleteUser(@PathVariable int userId){
        userService.delete(userId);
        return userId;
    }


}
