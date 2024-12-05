package kg.alatoo.taskmanagementsystem.controllers;


import kg.alatoo.taskmanagementsystem.Dto.SuccessDto;
import kg.alatoo.taskmanagementsystem.Dto.EntriesUpdateDto;
import kg.alatoo.taskmanagementsystem.entities.EntriesEntity;
import kg.alatoo.taskmanagementsystem.exceptions.ApiException;
import kg.alatoo.taskmanagementsystem.repositories.EntriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/entries")
public class EntriesController {

    @Autowired
    private EntriesRepository entriesRepository;

    @GetMapping("get/{id}")
    public EntriesEntity getById (@PathVariable("id") Long id){
        return entriesRepository.findById(id).orElseThrow(() -> new ApiException("Entry" + id + "is not found", HttpStatusCode.valueOf(404)));
    }

    @GetMapping("/get-all")
    public List<EntriesEntity> getAll(){
        return entriesRepository.findAll();
    }

    @PostMapping("/create")
    public EntriesEntity create(@RequestBody EntriesEntity newTask){
        return entriesRepository.save(newTask);
    }


    @PutMapping("update/{id}")
    public EntriesEntity update(@RequestBody EntriesUpdateDto task, @PathVariable("id") Long id){
        EntriesEntity toUpdate = entriesRepository.findById(id).get();


        if (task.getTitle() != null){
            toUpdate.setTitle(task.getTitle());
        }

        if (task.getDescription() != null){
            toUpdate.setDescription(task.getDescription());
        }


        if (task.getCreatedAt() != null){
            toUpdate.setCreatedAt(task.getCreatedAt());
        }

        if (task.getUpdatedAt() != null){
            toUpdate.setUpdatedAt(task.getUpdatedAt().atStartOfDay());
        }

        return entriesRepository.save(toUpdate);

    }

    @DeleteMapping("delete/{id}")
    public SuccessDto delete(@PathVariable Long id) {
        entriesRepository.deleteById(id);
        return new SuccessDto(true);
    }

}
