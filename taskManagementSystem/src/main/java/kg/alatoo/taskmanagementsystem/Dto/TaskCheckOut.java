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
public class TaskCheckOut {

    @NotNull(message = "workerId is required")
    Long workerId;

    @NotNull(message = "taskId is required")
    Long taskId;

}
