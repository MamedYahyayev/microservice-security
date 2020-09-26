package az.maqa.microservices.controller;

import az.maqa.microservices.dto.UserDTO;
import az.maqa.microservices.request.RequestUser;
import az.maqa.microservices.response.ResponseUser;
import az.maqa.microservices.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final Environment environment;
    private final ModelMapper modelMapper;

    @PostMapping("/create")
    public ResponseUser createUser(@RequestBody RequestUser requestUser) {
        UserDTO user = userService.createUser(requestUser);
        return modelMapper.map(user, ResponseUser.class);
    }


    @GetMapping("/dead")
    public String hello() {
        return "Hello Oldum mennenenene";
    }

}
