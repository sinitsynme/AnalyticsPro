package ru.sinitsynme.analyticspro.controller;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.sinitsynme.analyticspro.dto.filter.EventFilterDto;
import ru.sinitsynme.analyticspro.entity.ApplicationEntity;
import ru.sinitsynme.analyticspro.entity.UserEntity;
import ru.sinitsynme.analyticspro.entity.event.EventDateFilterType;
import ru.sinitsynme.analyticspro.service.ApplicationService;
import ru.sinitsynme.analyticspro.service.EventService;
import ru.sinitsynme.analyticspro.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/applications")
public class ApplicationsController {

    private final UserService userService;
    private final ApplicationService applicationService;
    private final EventService eventService;

    public ApplicationsController(UserService userService, ApplicationService applicationService, EventService eventService) {
        this.userService = userService;
        this.applicationService = applicationService;
        this.eventService = eventService;
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
    public ModelAndView applicationDiagramPage(@PathVariable Long id, @ModelAttribute EventFilterDto eventFilterDto, RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView("application/applicationStats");

        UserEntity user = userService.getPrincipalEntity().orElseThrow(
                () -> new AccessDeniedException("No access!"));

        ApplicationEntity app = applicationService.getApplication(id, user);

        if (app.getEventTypeList().size() == 0) {
            redirectAttributes.addAttribute("error",
                    String.format("Application \"%s\" has no registered events. Can't load dashboard", app.getName()));
            return new ModelAndView("redirect:/applications");
        }

        if (eventFilterDto.getTypeFilter() == null || eventFilterDto.getTypeFilter().size() == 0)
            eventFilterDto.setTypeFilter(List.copyOf(app.getEventTypeList()));
        if (eventFilterDto.getDateFilter() == null || eventFilterDto.getTypeFilter().size() == 0)
            eventFilterDto.setDateFilter(EventDateFilterType.ALL_TIME);


        List<List<Object>> pieChartData = eventService.formEventPieDiagramData(eventFilterDto);
        List<List<Object>> lineChartData = eventService.formEventLineDiagramData(eventFilterDto);

        modelAndView.addObject("app", app);
        modelAndView.addObject("eventTypes", app.getEventTypeList());
        modelAndView.addObject("dateFilters", EventDateFilterType.values());
        modelAndView.addObject("chartData", pieChartData);
        modelAndView.addObject("lineChartData", lineChartData);

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
