package kg.alatoo.taskmanagementsystem.Dto;

import kg.alatoo.taskmanagementsystem.entities.FavoriteEntity;
import kg.alatoo.taskmanagementsystem.entities.UserEntity;
import lombok.Data;

import java.util.List;

@Data
public class  UserFavoritesDto {

    private UserEntity user;
    private List<FavoriteEntity> favorites;


    public UserFavoritesDto(UserEntity user, List<FavoriteEntity> favorites) {
        this.user = user;
        this.favorites = favorites;
    }
}
