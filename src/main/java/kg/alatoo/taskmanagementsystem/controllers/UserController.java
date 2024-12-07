package kg.alatoo.taskmanagementsystem.controllers;


import kg.alatoo.taskmanagementsystem.Dto.SuccessDto;
import kg.alatoo.taskmanagementsystem.Dto.UserDto;
import kg.alatoo.taskmanagementsystem.Dto.UserFavEntDto;
import kg.alatoo.taskmanagementsystem.entities.FavoriteEntity;
import kg.alatoo.taskmanagementsystem.entities.UserEntity;
import kg.alatoo.taskmanagementsystem.exceptions.ApiException;
import kg.alatoo.taskmanagementsystem.repositories.FavoriteRepository;
import kg.alatoo.taskmanagementsystem.repositories.UserRepository;
import kg.alatoo.taskmanagementsystem.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private FavoriteRepository favoriteRepository;



/*
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserEntity> getUser(@PathVariable Long userId) {
        UserEntity user = userService.getUserWithEntries(userId);
        return ResponseEntity.ok(user);
    }
 */











    // Метод для получения всех пользователей с их избранными записями
    @GetMapping("/get-all")
    public List<UserFavEntDto> getAllUsersWithFavorites() {
        List<UserEntity> userEntities = userService.getAllUsers(); // Получаем всех пользователей

        // Конвертируем каждого пользователя в DTO
        return userEntities.stream()
                .map(userService::convertToUserFavEntDto)
                .collect(Collectors.toList());
    }




/*
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<UserEntity>> getAllUsers() {
        List<UserEntity> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

*/














    @PutMapping("/update-image/{userId}")
    public ResponseEntity<Void> updateUserImage(@PathVariable Long userId, @RequestParam String imageUrl) {
        userService.updateUserImage(userId, imageUrl); // Вызываем метод из UserService
        return ResponseEntity.ok().build(); // Возвращаем HTTP 200 OK
    }






    @GetMapping("/{id}")
    public UserFavEntDto getUserFavorites(Long userId) {
        // Получение пользователя из базы данных по ID (например, через репозиторий)
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        // Конвертация в DTO
        return userService.convertToUserFavEntDto(userEntity);
    }

    @PostMapping("/create")
    public UserEntity create (@RequestBody UserEntity userEntity){
        return userRepository.save(userEntity);
    }



    @PutMapping("/update/{id}")
    public UserEntity update(@RequestBody UserDto userDto, @PathVariable Long id){
        UserEntity toUpdate = userRepository.findById(id).get();

        if (userDto.getUsername() != null){
            toUpdate.setUsername(userDto.getUsername());
        }

        if (userDto.getEmail() != null){
            toUpdate.setEmail(userDto.getEmail());
        }

        if (userDto.getImageUrl() != null) { // Если есть ссылка на картинку, обновить
            toUpdate.setImageUrl(userDto.getImageUrl());
        }

        toUpdate.setModifiedAt(LocalDateTime.now());
        return userRepository.save(toUpdate);
    }


    @DeleteMapping("delete/{id}")
    public SuccessDto delete(@PathVariable Long id) {
        userRepository.deleteById(id);
        return new SuccessDto(true);
    }


}