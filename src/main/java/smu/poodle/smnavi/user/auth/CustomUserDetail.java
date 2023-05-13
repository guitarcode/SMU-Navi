package smu.poodle.smnavi.user.auth;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import smu.poodle.smnavi.user.domain.UserEntity;

import java.util.ArrayList;
import java.util.Collection;

@Getter
public class CustomUserDetail implements UserDetails {

    private UserEntity user;

    public CustomUserDetail(UserEntity user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
                           @Override
                           public String getAuthority() {
                               return user.getAuthority().name();
                           }
                       }
        );
        return collection;
    }

    public Long getUserid() { //id를 갖고옴
        return user.getId();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }


    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }



}
