package kg.alatoo.taskmanagementsystem.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class UserFavEntDto {

    private Long id;
    private String username;
    private String email;
    private LocalDateTime modifiedAt; //
    private String password;       //
    private List<FavoriteEntryDto> favorites;
    private List<EntryDto> entries;
    private String imageUrl;

}
