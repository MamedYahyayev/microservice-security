package az.maqa.microservices.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestUser {
    private String name;

    private String surname;

    private String email;

    private String password;
}
