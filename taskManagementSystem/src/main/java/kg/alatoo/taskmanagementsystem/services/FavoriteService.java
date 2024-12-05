package kg.alatoo.taskmanagementsystem.services;

import kg.alatoo.taskmanagementsystem.Dto.FavoriteDto;
import kg.alatoo.taskmanagementsystem.entities.FavoriteEntity;
import kg.alatoo.taskmanagementsystem.repositories.FavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;

    public List<FavoriteDto> getAllFavorites(){
        List<FavoriteEntity> favoriteEntities = favoriteRepository.findAll();  // Получаем все записи из базы данных

        // Преобразуем каждую сущность FavoriteEntity в DTO
        return favoriteEntities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }


    // Метод для преобразования сущности FavoriteEntity в DTO
    public FavoriteDto toDto(FavoriteEntity favoriteEntity){
        return FavoriteDto.builder()
                .id(favoriteEntity.getId())
                .userId(favoriteEntity.getUser().getId())
                .username(favoriteEntity.getUser().getUsername())
                .entryId(favoriteEntity.getEntries().getId())
                .entryTitle(favoriteEntity.getEntries().getTitle())
                .build();
    }
}
