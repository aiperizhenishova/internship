package kg.alatoo.taskmanagementsystem.Dto;

import kg.alatoo.taskmanagementsystem.entities.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public record CustomUserDetails(UserEntity user) implements UserDetails {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptySet(); // Роли не используются
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail(); // Используется email вместо имени пользователя
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Учетная запись не истекает
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Учетная запись не заблокирована
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Учетные данные не истекли
    }

    @Override
    public boolean isEnabled() {
        return true; // Учетная запись активна
    }
}
