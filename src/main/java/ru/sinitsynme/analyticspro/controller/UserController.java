package ru.sinitsynme.analyticspro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.sinitsynme.analyticspro.dto.UserDto;
import ru.sinitsynme.analyticspro.service.UserService;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public String registerUser(UserDto userDto, RedirectAttributes redirectAttributes) {
        userService.addUser(userDto);
        redirectAttributes.addAttribute("message", "Successful registration!");
        return "redirect:/login";
    }

}
