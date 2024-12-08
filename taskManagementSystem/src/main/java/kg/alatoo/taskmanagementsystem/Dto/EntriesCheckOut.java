package kg.alatoo.taskmanagementsystem.Dto;


import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EntriesCheckOut {

    @NotNull(message = "userId is required")
    Long userId;

    @NotNull(message = "entryId is required")
    Long entryId;

}
