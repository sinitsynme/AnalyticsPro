package ru.sinitsynme.analyticspro.service;

import ru.sinitsynme.analyticspro.entity.ApplicationEntity;
import ru.sinitsynme.analyticspro.entity.UserEntity;

public interface ApplicationService {

    ApplicationEntity addApplication(String name, UserEntity user);

    ApplicationEntity getApplication(Long id, UserEntity user);

    boolean existsById(Long id);

}
