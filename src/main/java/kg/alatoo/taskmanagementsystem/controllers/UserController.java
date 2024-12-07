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



    @PutMapping("/update-image/{userId}")
    public ResponseEntity<Void> updateUserImage(@PathVariable Long userId, @RequestParam String imageUrl) {
        userService.updateUserImage(userId, imageUrl); // Вызываем метод из UserService
        return ResponseEntity.ok().build(); // Возвращаем HTTP 200 OK
    }



    @GetMapping("/get/{id}")
    public UserFavEntDto getUserFavorites(@PathVariable Long id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userService.convertToUserFavEntDto(userEntity);
    }


    @PostMapping("/create")
    public UserEntity create(@RequestBody UserDto userDto) {
        // Создаем нового пользователя на основе данных из DTO
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(userDto.getUsername());
        userEntity.setPassword(userDto.getPassword());
        userEntity.setEmail(userDto.getEmail());

        // Сохраняем пользователя в базе данных
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