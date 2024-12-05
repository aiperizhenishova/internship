package kg.alatoo.taskmanagementsystem.Dto;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class FavoriteDto {

    private Long id;

    private Long userId;
    private String username;

    private Long entryId;
    private String entryTitle;


}
