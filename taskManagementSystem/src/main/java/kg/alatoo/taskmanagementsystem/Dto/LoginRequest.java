package kg.alatoo.taskmanagementsystem.Dto;

import lombok.Data;

@Data
public class LoginRequest {        //класс, который принимает данные для входа (email и password):
    private String email;
    private String password;
}
