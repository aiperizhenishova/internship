package kg.alatoo.taskmanagementsystem.Dto;

import kg.alatoo.taskmanagementsystem.entities.EntriesStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EntryDto {
    private Long id;
    private String title;
    private String description;
    private EntriesStatus status;
    private Long entryId;
    private String image;


    // Публичный конструктор с аргументами для инициализации всех полей
    public EntryDto(Long id, String title, String description, EntriesStatus status, Long entryId, String image) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.entryId = entryId;
        this.image = image;
    }
}
