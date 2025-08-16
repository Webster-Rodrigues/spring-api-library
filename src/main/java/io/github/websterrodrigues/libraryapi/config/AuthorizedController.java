package io.github.websterrodrigues.libraryapi.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AuthorizedController {

    @GetMapping("/authorized")
    @ResponseBody
    public String getAuthorizationCode(@RequestParam("code") String code){
        return "Seu authorization code: " + code;
    }
}
