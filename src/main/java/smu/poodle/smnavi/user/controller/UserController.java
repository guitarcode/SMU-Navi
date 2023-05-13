package smu.poodle.smnavi.user.controller;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import smu.poodle.smnavi.user.domain.UserEntity;
import smu.poodle.smnavi.user.dto.LoginRequestDto;
import smu.poodle.smnavi.user.sevice.UserService;
import smu.poodle.smnavi.user.sevice.UserSignupService;

import java.util.ArrayList;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final UserSignupService userSignupService;


    @PostMapping("/api/user/signup")
    public UserEntity signup(@RequestBody LoginRequestDto loginRequestDto){
        return userSignupService.signup(loginRequestDto);
    }

    @PostMapping("/api/user/login") //로그인
    public ResponseEntity<?> login(HttpServletRequest request, HttpServletResponse response,
                                   @RequestBody LoginRequestDto loginRequestDto){
        UserDetails userDetails = userService.loadUserByUsername(loginRequestDto.getEmail());
        Authentication authentication
                = new UsernamePasswordAuthenticationToken(userDetails, loginRequestDto.getPassword(), new ArrayList<>()); //인증 토큰 생성
        try {
            authenticationManager.authenticate(authentication); //인증 수행
        } catch (AuthenticationException e){ //인증 실패시
            throw new BadCredentialsException("Invalid email or password");
        }
        SecurityContextHolder.getContext().setAuthentication(authentication); //로그인한 사용자의 객체를 현재 스레드의 객체에 저장
        HttpSession session = request.getSession();
        session.setAttribute
                (HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                        SecurityContextHolder.getContext()); //인증 성공 시 인증 객체 저장(현재 실행중인 스레드의 인증 정보를 저장하고 있는 객체이며 로그인된 사용자의 보안 정보를 포함함)

        Cookie cookie = new Cookie("JSESSIONID", session.getId()); //JSESSIONID 이름의 쿠키 생성. 쿠키의 값은 서버에서 생성한 세선 ID
        //쿠키를 사용하여 클라이언트와 서버간의 세션을 유지하면서 클라이언트가 요청을 보낼 때마다 세선 ID를 서버에 전송하여 인증
        cookie.setPath("/"); //쿠키 경로 지정
//        cookie.setHttpOnly(true); //setHttpOnly 메소드를 호출하면 쿠키가 http전용 쿠키로 설정 -> 자바 스크립트 등으로 접근하는 것이 불가능해짐
        cookie.setMaxAge(30000 * 60); //쿠키의 유효기간[s]. 30분
//        cookie.setSecure(true); //쿠키가 https 프로토콜을 통해서만 전송되도록 설정
        response.addCookie(cookie); //클라이언트에게 응답으로 보낼 쿠키를 추가
        return new ResponseEntity(HttpStatus.OK); //인증 성공 시 반환
    }




}