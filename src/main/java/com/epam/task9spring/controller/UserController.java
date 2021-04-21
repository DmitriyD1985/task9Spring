package com.epam.task9spring.controller;


import com.epam.task9spring.service.MyUserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/regorauth")
@RestController
public class UserController {

    private MyUserDetailsService myUserDetailsService;

    public UserController(MyUserDetailsService myUserDetailsService) {
        this.myUserDetailsService = myUserDetailsService;
    }

    @PostMapping("/registration}")
    public String getOneRealEstate(@RequestBody String login, String name, String password, String role) {
        myUserDetailsService.saveUser(login, name, password, role);
        return "{\"msg\":\"Пользователь сохранен\"}";
    }

    @PostMapping("/authorization")
    public String updateRealEstate(@RequestBody String request) {
        myUserDetailsService.loadUserByUsername(request);
        return "RealEstate updated";
    }
}
