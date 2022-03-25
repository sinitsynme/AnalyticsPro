package ru.sinitsynme.analyticspro.controller;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import ru.sinitsynme.analyticspro.dto.EventDto;
import ru.sinitsynme.analyticspro.dto.filter.EventFilterDto;
import ru.sinitsynme.analyticspro.entity.ApplicationEntity;
import ru.sinitsynme.analyticspro.entity.event.EventType;
import ru.sinitsynme.analyticspro.entity.UserEntity;
import ru.sinitsynme.analyticspro.service.ApplicationService;
import ru.sinitsynme.analyticspro.service.EventService;
import ru.sinitsynme.analyticspro.service.UserService;
import ru.sinitsynme.analyticspro.utils.ListUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

        if(app.getEventTypeList().size() == 0){
            redirectAttributes.addAttribute("error",
                    String.format("Application \"%s\" has no registered events. Can't load dashboard", app.getName()));
            return new ModelAndView("redirect:/applications");
        }


        List<EventType> eventTypeList;

        if(eventFilterDto.getFilter() == null || eventFilterDto.getFilter().size() == 0)
            eventFilterDto.setFilter(List.copyOf(app.getEventTypeList()));

        eventTypeList = eventFilterDto.getFilter();

        List<EventDto> eventDtos = eventService.listApplicationEventsByFilter(eventTypeList);
        Map<String, Long> chartData = eventDtos.stream().collect(Collectors.groupingBy(EventDto::getName, Collectors.counting()));

        modelAndView.addObject("app", app);
        modelAndView.addObject("eventTypes", app.getEventTypeList());
        modelAndView.addObject("eventDtos", eventDtos);
        modelAndView.addObject("chartData", ListUtils.mapToListOfPairs(chartData));

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
