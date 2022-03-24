package ru.sinitsynme.analyticspro.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.sinitsynme.analyticspro.entity.UserEntity;
import ru.sinitsynme.analyticspro.exception.ResourceNotFoundException;
import ru.sinitsynme.analyticspro.repository.UserRepository;

import java.util.HashSet;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userFromDb = userRepository.findUserEntityByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("This email is invalid"));

        return new User(userFromDb.getEmail(), userFromDb.getPassword(), new HashSet<>());
    }
}
