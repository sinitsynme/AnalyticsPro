package ru.sinitsynme.analyticspro.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import ru.sinitsynme.analyticspro.entity.ApplicationEntity;
import ru.sinitsynme.analyticspro.entity.UserEntity;
import ru.sinitsynme.analyticspro.exception.ApplicationRegistrationException;
import ru.sinitsynme.analyticspro.repository.ApplicationRepository;
import ru.sinitsynme.analyticspro.repository.UserRepository;
import ru.sinitsynme.analyticspro.service.ApplicationService;
import ru.sinitsynme.analyticspro.service.UserService;

import java.sql.Date;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Objects;

@Service
public class ApplicationServiceImpl implements ApplicationService {

    private final UserService userService;
    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;

    public ApplicationServiceImpl(UserService userService, UserRepository userRepository, ApplicationRepository applicationRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.applicationRepository = applicationRepository;
    }

    @Override
    public ApplicationEntity addApplication(String name, UserEntity user) {
        String trimmedName = name.trim();
        if(trimmedName.isEmpty()){
            throw new ApplicationRegistrationException("Application name cannot be empty");
        }
        if(user.getApplicationList().stream().anyMatch(it -> it.getName().equals(trimmedName))){
            throw new ApplicationRegistrationException(
                    String.format("Name \"%s\" matches with one of your registered applications", trimmedName));
        }

        ApplicationEntity app = new ApplicationEntity();
        app.setName(trimmedName);
        app.setUser(user);
        app.setRegistrationDate(Date.from(Instant.now()));

        applicationRepository.save(app);
        return app;
    }

    @Override
    public ApplicationEntity getApplication(Long id) {
        UserEntity principal = userService.getPrincipalEntity().orElseThrow(() -> new AccessDeniedException("No access!"));

        if(principal.getApplicationList().stream().noneMatch(it -> Objects.equals(it.getId(), id))){
            throw new AccessDeniedException("No access!");
        }

        return applicationRepository.getById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return applicationRepository.existsById(id);
    }

    @Override
    public void deleteById(Long id) {
        UserEntity principal = userService.getPrincipalEntity().orElseThrow(() -> new AccessDeniedException("No access!"));

        if(principal.getApplicationList().stream().noneMatch(it -> Objects.equals(it.getId(), id))){
            throw new AccessDeniedException("No access!");
        }

        applicationRepository.deleteById(id);
    }
}
