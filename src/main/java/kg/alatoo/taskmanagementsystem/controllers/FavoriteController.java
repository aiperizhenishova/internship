package kg.alatoo.taskmanagementsystem.controllers;

import kg.alatoo.taskmanagementsystem.Dto.FavoriteDto;
import kg.alatoo.taskmanagementsystem.Dto.SuccessDto;
import kg.alatoo.taskmanagementsystem.Dto.UserFavoritesDto;
import kg.alatoo.taskmanagementsystem.entities.EntriesEntity;
import kg.alatoo.taskmanagementsystem.entities.FavoriteEntity;
import kg.alatoo.taskmanagementsystem.entities.UserEntity;
import kg.alatoo.taskmanagementsystem.exceptions.ApiException;
import kg.alatoo.taskmanagementsystem.repositories.FavoriteRepository;
import kg.alatoo.taskmanagementsystem.repositories.EntriesRepository;
import kg.alatoo.taskmanagementsystem.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favorite")
public class FavoriteController {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntriesRepository entryRepository;

    // Запрос избранных записей по имени пользователя (username)
    @GetMapping("/username/{username}")
    public UserFavoritesDto getUserFavorites(@PathVariable("username") String username) {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException("User not found", HttpStatusCode.valueOf(404)));

        List<FavoriteEntity> favorites = favoriteRepository.findByUser(userEntity);

        return new UserFavoritesDto(userEntity, favorites);
    }

    @GetMapping("/get-all")
    public List<FavoriteEntity> getAll() {
        return favoriteRepository.findAll();
    }

    @GetMapping("get/{id}")
    public FavoriteEntity getById(@PathVariable("id") Long id) {
        return favoriteRepository.findById(id)
                .orElseThrow(() -> new ApiException("Favorite entry with ID " + id + " not found", HttpStatusCode.valueOf(404)));
    }

    @PostMapping("/create")
    public FavoriteEntity create(@RequestBody FavoriteDto favoriteDto) {
        FavoriteEntity favoriteEntity = new FavoriteEntity();

        // Убираем работу с userId
        if (favoriteDto.getEntryId() != null) {
            EntriesEntity entry = entryRepository.findById(favoriteDto.getEntryId())
                    .orElseThrow(() -> new ApiException("Entry not found", HttpStatusCode.valueOf(404)));
            favoriteEntity.setEntries(entry);
        }

        return favoriteRepository.save(favoriteEntity);
    }

    @PutMapping("/update/{id}")
    public FavoriteEntity update(@RequestBody FavoriteDto favoriteDto, @PathVariable Long id) {
        FavoriteEntity toUpdate = favoriteRepository.findById(id)
                .orElseThrow(() -> new ApiException("Favorite entry not found", HttpStatusCode.valueOf(404)));

        // Убираем работу с userId
        if (favoriteDto.getEntryId() != null) {
            EntriesEntity entry = entryRepository.findById(favoriteDto.getEntryId())
                    .orElseThrow(() -> new ApiException("Entry not found", HttpStatusCode.valueOf(404)));
            toUpdate.setEntries(entry);
        }

        return favoriteRepository.save(toUpdate);
    }

    @DeleteMapping("/delete/{id}")
    public SuccessDto delete(@PathVariable Long id) {
        favoriteRepository.deleteById(id);
        return new SuccessDto(true);
    }
}
