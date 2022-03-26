package ru.sinitsynme.analyticspro.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.sinitsynme.analyticspro.exception.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;


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

    @ExceptionHandler(AccessDeniedException.class)
    public String accessDeniedException(RedirectAttributes redirectAttributes){
        redirectAttributes.addAttribute("error", "Oops... You don't have access to this page!");
        return "redirect:/";
    }

    @ExceptionHandler(ApplicationRegistrationException.class)
    public String applicationRegistrationException(RedirectAttributes redirectAttributes, Exception ex){
        redirectAttributes.addAttribute("error", ex.getMessage());
        return "redirect:/applications/new";
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ExceptionResponse> resourceNotFoundException(Exception ex){
        ExceptionResponse response = new ExceptionResponse(new Date(), ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoEventException.class)
    public String noEventException(RedirectAttributes redirectAttributes, Exception ex){
        redirectAttributes.addAttribute("error", ex.getMessage());
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String url = request.getRequestURI();
        return "redirect:" + url;
    }
}
