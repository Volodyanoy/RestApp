package org.example.volodyanoy.RestApp.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // @Controller + @ResponseBody над каждым методом
@RequestMapping("/api")
public class HelloRESTController {

    @GetMapping("/sayHello")
    public String sayHello(){
        return "Hello world";
    }


}
