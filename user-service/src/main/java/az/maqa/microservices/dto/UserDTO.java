package az.maqa.microservices.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private String userId;
    private String name;
    private String surname;
    private String email;
    private String password;
    private String encryptedPassword;

}
