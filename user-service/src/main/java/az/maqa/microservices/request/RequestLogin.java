package az.maqa.microservices.request;

import lombok.Data;

@Data
public class RequestLogin {
    private String email;
    private String password;
}
