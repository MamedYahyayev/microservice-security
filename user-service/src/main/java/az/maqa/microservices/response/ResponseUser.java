package az.maqa.microservices.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseUser {

    private String userId;

    private String name;

    private String surname;

    private String email;
}
