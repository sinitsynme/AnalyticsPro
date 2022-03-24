package ru.sinitsynme.analyticspro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import ru.sinitsynme.analyticspro.dto.UserDto;
import ru.sinitsynme.analyticspro.entity.UserEntity;
import ru.sinitsynme.analyticspro.service.UserService;


@Controller
public class MainController {

    private final UserService userService;

    @Autowired
    public MainController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public ModelAndView getRegistrationPage(){
        ModelAndView modelAndView = new ModelAndView("user/userRegistration");
        UserDto user = new UserDto();
        modelAndView.addObject("user", user);
        return modelAndView;
    }



}
