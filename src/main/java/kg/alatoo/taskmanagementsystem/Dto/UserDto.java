package kg.alatoo.taskmanagementsystem.Dto;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import kg.alatoo.taskmanagementsystem.entities.FavoriteEntity;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class UserDto {

     private Long id;

     private String username;

     private String email;

     private LocalDateTime modifiedAt; //

     private String password;    //

     private List<EntryDto> entries;

     //@JsonManagedReference

     private List<FavoriteEntryDto> favorites;   //favoritedto  было

     private String imageUrl;


}
