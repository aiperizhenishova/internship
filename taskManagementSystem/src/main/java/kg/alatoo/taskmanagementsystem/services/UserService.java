package kg.alatoo.taskmanagementsystem.services;

import kg.alatoo.taskmanagementsystem.Dto.SignUpDto;
import kg.alatoo.taskmanagementsystem.entities.FavoriteEntity;
import kg.alatoo.taskmanagementsystem.entities.UserEntity;
import kg.alatoo.taskmanagementsystem.exceptions.ApiException;
import kg.alatoo.taskmanagementsystem.model.UserModel;
import kg.alatoo.taskmanagementsystem.repositories.FavoriteRepository;
import kg.alatoo.taskmanagementsystem.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;



@Service
@Slf4j
public class UserService implements UserDetailsService {


    @Autowired
    private UserRepository userRepository;


    @Autowired
    private PasswordEncoder bCryptPassword;



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username + " not found"));

        return new UserModel(
                userEntity.getId(),
                userEntity.getUsername(),
                userEntity.getPassword(),
                userEntity.getModifiedAt()
        );
    }

    public void saveUser(SignUpDto signUpDto) {
        log.info("Sign up user: {}", signUpDto.getUsername());

        // Создаем новый объект пользователя
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(signUpDto.getUsername());
        userEntity.setPassword(bCryptPassword.encode(signUpDto.getPassword()));
        userEntity.setEmail(signUpDto.getEmail());
        userEntity.setModifiedAt(signUpDto.getModifiedAt());

        try {
            userRepository.save(userEntity);
        } catch (DataIntegrityViolationException e) {
            throw new ApiException("User " + signUpDto.getUsername() + " is already exists", HttpStatusCode.valueOf(409));
        } catch (Exception e) {
            log.error("Error occurred while saving user", e);
            throw new ApiException("Error while user creating", HttpStatusCode.valueOf(400));
        }
    }

}
