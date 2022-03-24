package ru.sinitsynme.analyticspro.exception.handler;

import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.sinitsynme.analyticspro.exception.UserRegistrationException;

@ControllerAdvice
public class AppExceptionHandlerController {

    @ExceptionHandler(UserRegistrationException.class)
    public String registrationException(RedirectAttributes redirectAttributes, Exception ex){
        redirectAttributes.addAttribute("error", ex.getMessage());
        return "redirect:/register";
    }

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public String authenticationException(RedirectAttributes redirectAttributes){
        redirectAttributes.addAttribute("error", "Wrong email or password");
        return "redirect:/login";
    }

}
