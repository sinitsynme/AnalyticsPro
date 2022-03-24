package ru.sinitsynme.analyticspro.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.sinitsynme.analyticspro.dto.UserDto;
import ru.sinitsynme.analyticspro.entity.UserEntity;

import java.util.Optional;

public interface UserService {

    UserEntity getUserByEmail(String email);

    UserEntity addUser(UserDto userEntity);

    boolean checkPresenceByEmail(String email);

    Optional<UserEntity> getPrincipalEntity();

    boolean isPrincipalAvailable();

}
