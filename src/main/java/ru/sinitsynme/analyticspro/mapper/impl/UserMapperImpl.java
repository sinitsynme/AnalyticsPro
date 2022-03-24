package ru.sinitsynme.analyticspro.mapper.impl;

import org.springframework.stereotype.Component;
import ru.sinitsynme.analyticspro.dto.UserDto;
import ru.sinitsynme.analyticspro.entity.UserEntity;
import ru.sinitsynme.analyticspro.mapper.UserMapper;

@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserEntity toEntity(UserDto dto) {
        return new UserEntity(dto.getEmail(), dto.getPassword());
    }
}
