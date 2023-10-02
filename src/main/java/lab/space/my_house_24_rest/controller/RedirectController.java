package lab.space.my_house_24_rest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RedirectController {
    @GetMapping({"/", ""})
    public String redirectAdmin() {
        return "redirect:/swagger-ui/index.html#/";
    }
}