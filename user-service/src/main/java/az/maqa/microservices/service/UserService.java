package az.maqa.microservices.service;

import az.maqa.microservices.dto.UserDTO;
import az.maqa.microservices.request.RequestUser;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    UserDTO getUserDetailsByEmail(String email);

    UserDTO createUser(RequestUser requestUser);
}
