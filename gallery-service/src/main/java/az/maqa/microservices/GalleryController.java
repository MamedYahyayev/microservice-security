package az.maqa.microservices;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GalleryController {

    @GetMapping
    public String gallery(){
        return "Gallery";
    }
}
