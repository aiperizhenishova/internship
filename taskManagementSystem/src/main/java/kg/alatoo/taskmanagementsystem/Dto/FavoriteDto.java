package kg.alatoo.taskmanagementsystem.Dto;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class FavoriteDto {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime updatedAt;
    private String status;
    private Long entryId;
}
