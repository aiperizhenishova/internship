package kg.alatoo.taskmanagementsystem.Dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FavoriteEntryDto {

    private Long entryId;
    private String title;
    private String description;
    private String image;


    public FavoriteEntryDto() {}

    public FavoriteEntryDto(Long entryId, String title, String description, String image) {
        this.entryId = entryId;
        this.title = title;
        this.description = description;
        this.image = image;
    }
}
