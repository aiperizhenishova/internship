package kg.alatoo.taskmanagementsystem.Dto;

import lombok.Data;

@Data
public class LoginResponse { //класс, который возвращает ответ после успешного входа, например, токен:
    private String message;
    private String accessToken;
    private String refreshToken;
    private int status;

    

    // Конструктор
    public LoginResponse(String message, String accessToken, String refreshToken, int status) {
        this.message = message;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.status = status;
    }


}
