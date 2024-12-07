package kg.alatoo.taskmanagementsystem.Dto;

import kg.alatoo.taskmanagementsystem.entities.EntriesStatus;
import kg.alatoo.taskmanagementsystem.entities.UserEntity;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class EntriesUpdateDto {

    String title;

    String description;

    LocalDate createdAt;

    LocalDate updatedAt;

    EntriesStatus status;

    //images

    UserEntity user;


}
