package kg.alatoo.taskmanagementsystem.services;

import kg.alatoo.taskmanagementsystem.Dto.*;
import kg.alatoo.taskmanagementsystem.entities.FavoriteEntity;
import kg.alatoo.taskmanagementsystem.entities.UserEntity;
import kg.alatoo.taskmanagementsystem.exceptions.ApiException;
import kg.alatoo.taskmanagementsystem.model.UserModel;
import kg.alatoo.taskmanagementsystem.repositories.FavoriteRepository;
import kg.alatoo.taskmanagementsystem.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import kg.alatoo.taskmanagementsystem.entities.EntriesStatus;


import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder bCryptPassword;

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private JwtTokenService jwtTokenProvider;





/*
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserEntity getUserWithEntries(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
*/




    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }







    // Метод для получения пользователя с избранными записями
    public UserFavEntDto getUserWithFavorites(Long userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException("User not found", HttpStatusCode.valueOf(404)));

        // Получаем избранные записи
        List<FavoriteEntity> favoriteEntities = favoriteRepository.findAllByUserId(userId);

        // Преобразуем избранные записи в DTO
        List<FavoriteEntryDto> favoriteEntries = favoriteEntities.stream()
                .map(fav -> new FavoriteEntryDto(fav.getEntries().getId(), fav.getEntries().getTitle(), fav.getEntries().getImage(), fav.getEntries().getDescription()))
                .collect(Collectors.toList());

        // Возвращаем DTO с нужной информацией
        return UserFavEntDto.builder()
                .id(userEntity.getId())
                .username(userEntity.getUsername())
                .email(userEntity.getEmail())
                .modifiedAt(userEntity.getModifiedAt())
                .favorites(favoriteEntries)  // Список избранных записей
                .build();
    }


    // Этот метод нужен Spring Security, чтобы знать, кто залогинился.
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Поиск пользователя по email
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email" + email + " not found"));

        return new UserModel(
                userEntity.getId(),
                userEntity.getEmail(),
                userEntity.getPassword(),
                userEntity.getModifiedAt()
        );
    }




    // login Ищет юзера по email, проверяет пароль, если ок — всё норм и генерит jwt token, иначе кидает исключение
    public String loginUser(String email, String password) {
        // Поиск пользователя по email
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        // Проверка пароля
        if (!bCryptPassword.matches(password, userEntity.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        // Генерация токена
        return jwtTokenProvider.createAccessToken(userEntity.getEmail());
    }


    // Получить всех пользователей с их записями и избранными записями
    public List<UserFavEntDto> getAllUsersWithFavorites() {
        List<UserEntity> users = userRepository.findAll();  // Получаем всех пользователей

        return users.stream()
                .map(user -> {
                    // Получаем избранные записи для пользователя
                    List<FavoriteEntity> favoriteEntities = favoriteRepository.findAllByUserId(user.getId());
                    List<FavoriteEntryDto> favoriteEntries = favoriteEntities.stream()
                            .map(fav -> new FavoriteEntryDto(fav.getEntries().getId(), fav.getEntries().getTitle(), fav.getEntries().getImage(), fav.getEntries().getDescription()))
                            .collect(Collectors.toList());

                    // Получаем записи пользователя
                    List<EntryDto> entries = user.getEntries().stream()
                            .map(entry -> EntryDto.builder()
                                    .id(entry.getId())
                                    .title(entry.getTitle())
                                    .description(entry.getDescription())
                                    .status(entry.getStatus())
                                    .entryId(entry.getId())  // Или другой идентификатор записи
                                    .build())
                            .collect(Collectors.toList());

                    return UserFavEntDto.builder()
                            .id(user.getId())
                            .username(user.getUsername())
                            .email(user.getEmail())
                            .modifiedAt(user.getModifiedAt())
                            .build();
                })
                .collect(Collectors.toList());
    }


    public EntryDto convertToEntryDto(FavoriteEntity fav) {
        return EntryDto.builder()
                .id(fav.getEntries().getId())
                .title(fav.getEntries().getTitle())
                .description(fav.getEntries().getDescription())
                .status(EntriesStatus.PENDING)
                .entryId(fav.getEntries().getId())
                .build();
    }



    public UserDto convertToUserDto(UserEntity userEntity) {
        List<EntryDto> entries = userEntity.getEntries().stream()
                .map(entry -> new EntryDto(entry.getId(), entry.getTitle(), entry.getDescription(),
                        entry.getStatus(), entry.getId(), entry.getImage())) // передаем status как EntriesStatus, а не String
                .collect(Collectors.toList());

        List<FavoriteEntryDto> favoriteEntries = userEntity.getFavorites().stream()
                .map(fav -> new FavoriteEntryDto(fav.getEntries().getId(), fav.getEntries().getTitle(), fav.getEntries().getImage(), fav.getEntries().getDescription()))
                .collect(Collectors.toList());

        return UserDto.builder()
                .id(userEntity.getId())
                .username(userEntity.getUsername())
                .email(userEntity.getEmail())
                .entries(entries)
                .favorites(favoriteEntries)
                .build();
    }


    // Метод для регистрации пользователя (Проверяет email, шифрует пароль, сохраняет юзера в базе)
    public void saveUser(SignUpDto signUpDto) {
        log.info("Sign up user: {}", signUpDto.getUsername());

        // Создаем новый объект пользователя
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(signUpDto.getUsername());
        userEntity.setPassword(bCryptPassword.encode(signUpDto.getPassword()));
        userEntity.setEmail(signUpDto.getEmail());
        userEntity.setModifiedAt(signUpDto.getModifiedAt());

        try {
            userRepository.save(userEntity);     // сохраняем пользователя в базе
        } catch (DataIntegrityViolationException e) {
            throw new ApiException("User with email " + signUpDto.getEmail() + " already exists", HttpStatusCode.valueOf(409));
        } catch (Exception e) {
            log.error("Error occurred while saving user", e);
            throw new ApiException("Error while user creating", HttpStatusCode.valueOf(400));
        }
    }


    //этот метод отвечает за изб записи (какие данные будут выходить в постман)
    public UserFavEntDto convertToUserFavEntDto(UserEntity userEntity) {
        // Пример конвертации сущности пользователя в DTO
        List<FavoriteEntryDto> favoriteEntries = userEntity.getFavorites().stream()
                .map(fav -> FavoriteEntryDto.builder()
                        .entryId(fav.getId())
                        .title(fav.getTitle())
                        .description(fav.getDescription())
                        .image(fav.getImage())
                        .build())
                .collect(Collectors.toList());

        List<EntryDto> entries = userEntity.getEntries().stream()    //это для выдачи entries в постман
                .map(entry -> EntryDto.builder()
                        .id(entry.getId())
                        .title(entry.getTitle())
                        .description(entry.getDescription())
                        .status(entry.getStatus())
                        .entryId(entry.getId())
                        .image(entry.getImage())
                        .build())
                .collect(Collectors.toList());


        return UserFavEntDto.builder()                 //это полностью возвр все данные в постман(все что мы видем в постман)
                .id(userEntity.getId())
                .username(userEntity.getUsername())
                .email(userEntity.getEmail())
                .modifiedAt(userEntity.getModifiedAt())
                .password(userEntity.getPassword())
                .imageUrl(userEntity.getImageUrl())
                .favorites(favoriteEntries)
                .entries(entries)
                .build();
    }


    public void updateUserImage(Long userId, String imageUrl) {
        // Найти пользователя по ID
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Обновить URL изображения
        userEntity.setImageUrl(imageUrl);

        // Сохранить изменения
        userRepository.save(userEntity);
    }



}
