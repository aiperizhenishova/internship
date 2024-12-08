package kg.alatoo.taskmanagementsystem.entities;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "favorite")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FavoriteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;



    @ManyToOne
    @JoinColumn(name = "entry_id")
    private EntriesEntity entries;

    private String title;
    private String description;
    private LocalDateTime updatedAt;
    private String status;
    private String imageUrl;



    public String getImage() {
        if (imageUrl != null) {
            return "http://localhost:8080/uploaded_files/" + imageUrl;
        }
        return "no image";
    }

}
