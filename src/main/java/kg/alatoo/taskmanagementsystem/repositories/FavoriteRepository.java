package kg.alatoo.taskmanagementsystem.repositories;

import kg.alatoo.taskmanagementsystem.entities.FavoriteEntity;
import kg.alatoo.taskmanagementsystem.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<FavoriteEntity, Long> {

    Optional<FavoriteEntity> findByUser_Username(String username);

    List<FavoriteEntity> findAllByUser_UsernameContainingIgnoreCase(String username);
    List<FavoriteEntity> findByUser(UserEntity user);

    List<FavoriteEntity> findAllByUserId(Long userId);

    // Обновление картинки для пользователя
    Optional<FavoriteEntity> findByIdAndUser(Long id, UserEntity user);
}
