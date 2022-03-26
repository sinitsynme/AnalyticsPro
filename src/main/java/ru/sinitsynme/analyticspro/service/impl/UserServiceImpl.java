package ru.sinitsynme.analyticspro.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.sinitsynme.analyticspro.dto.UserDto;
import ru.sinitsynme.analyticspro.entity.UserEntity;
import ru.sinitsynme.analyticspro.exception.ResourceNotFoundException;
import ru.sinitsynme.analyticspro.exception.UserRegistrationException;
import ru.sinitsynme.analyticspro.mapper.UserMapper;
import ru.sinitsynme.analyticspro.repository.UserRepository;
import ru.sinitsynme.analyticspro.service.UserService;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserEntity getUserByEmail(String email) {
        return userRepository.findUserEntityByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("This email is invalid"));
    }

    @Override
    public UserEntity addUser(UserDto userDto) {
        if (userDto.getEmail().trim().isEmpty()) {
            throw new UserRegistrationException("Email cannot be empty");
        }

        if (!userDto.getConfirmPassword().equals(userDto.getPassword())) {
            throw new UserRegistrationException("Passwords are not equal");
        }
        if (userDto.getPassword().trim().isEmpty()) {
            throw new UserRegistrationException("Password cannot be empty");
        }

        if (checkPresenceByEmail(userDto.getEmail()))
            throw new UserRegistrationException("This email has been already taken by another user");

        UserEntity userEntity = userMapper.toEntity(userDto);
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        return userRepository.save(userEntity);
    }

    @Override
    public boolean checkPresenceByEmail(String email) {
        return userRepository.findUserEntityByEmail(email).isPresent();
    }

    @Override
    public Optional<UserEntity> getPrincipalEntity() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String email = ((UserDetails) authentication.getPrincipal()).getUsername();
            return Optional.of(getUserByEmail(email));
        }

        return Optional.empty();
    }

    @Override
    public boolean isPrincipalAvailable() {
        return getPrincipalEntity().isPresent();
    }


}
