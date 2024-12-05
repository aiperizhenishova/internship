package kg.alatoo.taskmanagementsystem.repositories;

import kg.alatoo.taskmanagementsystem.entities.EntriesEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EntriesRepository extends JpaRepository<EntriesEntity, Long> {
}
