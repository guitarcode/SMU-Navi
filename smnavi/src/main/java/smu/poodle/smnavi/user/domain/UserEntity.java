package smu.poodle.smnavi.user.domain;

import jakarta.persistence.*;
import lombok.Getter;
import smu.poodle.smnavi.user.auth.Authority;

@Entity
@Table(name = "users")
@Getter
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String email; //이메일 = 닉네임
    @Column
    private String password; //비밀번호

    public UserEntity(){}

    @Enumerated(EnumType.STRING)
    private Authority authority;

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}