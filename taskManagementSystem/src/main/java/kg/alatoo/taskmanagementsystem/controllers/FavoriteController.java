package kg.alatoo.taskmanagementsystem.controllers;

import kg.alatoo.taskmanagementsystem.Dto.FavoriteDto;
import kg.alatoo.taskmanagementsystem.Dto.SuccessDto;
import kg.alatoo.taskmanagementsystem.entities.EntriesEntity;
import kg.alatoo.taskmanagementsystem.entities.FavoriteEntity;
import kg.alatoo.taskmanagementsystem.entities.UserEntity;
import kg.alatoo.taskmanagementsystem.exceptions.ApiException;
import kg.alatoo.taskmanagementsystem.repositories.FavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favorite")
public class FavoriteController {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @GetMapping("/get-all")
    public List<FavoriteEntity> getAll(){
        return favoriteRepository.findAll();

    }

    @GetMapping("get/{id}")
    public FavoriteEntity getById (@PathVariable("id") Long id){
        return favoriteRepository.findById(id).orElseThrow(() -> new ApiException("Favorite entry is" + id + "not found", HttpStatusCode.valueOf(404)));
    }


    @PostMapping("/create")
    public FavoriteEntity create(@RequestBody FavoriteDto favoriteDto) {
        FavoriteEntity favoriteEntity = new FavoriteEntity();

        if (favoriteDto.getUserId() != null) {
            UserEntity user = userRepository.findById(favoriteDto.getUserId())
                    .orElseThrow(() -> new ApiException("User not found", HttpStatusCode.valueOf(404)));
            favoriteEntity.setUser(user);
        }

        if (favoriteDto.getEntryId() != null) {
            EntriesEntity entry = entryRepository.findById(favoriteDto.getEntryId())
                    .orElseThrow(() -> new ApiException("Entry not found", HttpStatusCode.valueOf(404)));
            favoriteEntity.setEntries(entry);
        }

        return favoriteRepository.save(favoriteEntity);
    }

    @PutMapping("/update/{id}")
    public FavoriteEntity update(@RequestBody FavoriteDto favoriteDto, @PathVariable Long id){
        FavoriteEntity toUpdate = favoriteRepository.findById(id).orElseThrow(() -> new ApiException("Favorite entry not found", HttpStatusCode.valueOf(404)));

        if (favoriteDto.getUserId() != null){
            UserEntity user = userRepository.findById(favoriteDto.getUserId())
                    .orElseThrow(() -> new ApiException("User not found", HttpStatusCode.valueOf(404)));
            toUpdate.setUser(user);
        }

        if (favoriteDto.getEntryId() != null){
            EntriesEntity entry = entryRepository.findById(favoriteDto.getEntryId())
                    .orElseThrow(() -> new ApiException("Entry not found", HttpStatusCode.valueOf(404)));
            toUpdate.setEntries(entry);
        }



        return favoriteRepository.save(toUpdate);
    }

    @DeleteMapping("delete/{id}")
    public SuccessDto delete(@PathVariable Long id) {
        favoriteRepository.deleteById(id);
        return new SuccessDto(true);
    }

}

