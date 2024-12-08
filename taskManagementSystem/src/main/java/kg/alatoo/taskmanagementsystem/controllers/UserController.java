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
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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


    private final String UPLOAD_DIR = "uploaded_images/";

    @PostMapping("/{id}/uploadImage")
    public ResponseEntity<String> uploadImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();
            Path path = Paths.get(UPLOAD_DIR + fileName);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            String imageUrl = "http://localhost:8080/uploaded_files/" + fileName;
            return ResponseEntity.ok("Image uploaded successfully: " + imageUrl);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Image upload failed");
        }
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

        if (userDto.getPassword() != null){
            toUpdate.setPassword(userDto.getPassword());
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