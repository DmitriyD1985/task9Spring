package com.epam.task9spring.controller;


import com.epam.task9spring.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/registration")
    public String getOneRealEstate(@RequestBody String request) {
        userService.saveUser(request);
        return "{\"msg\":\"Пользователь зарегистрирован\"}";
    }

    @PostMapping("/authorization")
    public String updateRealEstate(@RequestBody String request) {
        System.out.println("request");
        System.out.println(request);
        userService.loadUserByUsername(request);
        return "{\"msg\":\"Пользователь авторизован\"}";
    }
}