package ru.sinitsynme.analyticspro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.sinitsynme.analyticspro.entity.ApplicationEntity;
import ru.sinitsynme.analyticspro.entity.UserEntity;
import ru.sinitsynme.analyticspro.service.ApplicationService;
import ru.sinitsynme.analyticspro.service.UserService;

@Controller
@RequestMapping("/applications")
public class ApplicationsController {

    private final UserService userService;
    private final ApplicationService applicationService;

    @Autowired
    public ApplicationsController(UserService userService, ApplicationService applicationService) {
        this.userService = userService;
        this.applicationService = applicationService;
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ModelAndView applicationsListPage() {
        ModelAndView modelAndView = new ModelAndView("application/applicationsList");
        UserEntity user = userService.getPrincipalEntity().orElseThrow(
                () -> new AccessDeniedException("No access!")
        );

        modelAndView.addObject("appsList", user.getApplicationList());
        return modelAndView;
    }

    @GetMapping("/new")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView applicationFormPage() {
        ModelAndView modelAndView = new ModelAndView("application/applicationForm");
        ApplicationEntity app = new ApplicationEntity();
        return modelAndView.addObject(app);
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView applicationDiagramPage(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("application/applicationStats");

        UserEntity user = userService.getPrincipalEntity().orElseThrow(
                () -> new AccessDeniedException("No access!"));

        ApplicationEntity app = applicationService.getApplication(id, user);

        modelAndView.addObject(app);

        return modelAndView;
    }


    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public String addNewApplication(String appName, RedirectAttributes redirectAttributes) {
        ApplicationEntity app = applicationService.addApplication(appName,
                userService.getPrincipalEntity().orElseThrow(() -> new AccessDeniedException("Access denied")));
        redirectAttributes.addAttribute("message", String.format("Application %s has been successfully registered", app.getName()));
        return "redirect:/applications";
    }

}
