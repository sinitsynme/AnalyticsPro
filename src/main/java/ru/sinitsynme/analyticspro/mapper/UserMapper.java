package ru.sinitsynme.analyticspro.mapper;

import ru.sinitsynme.analyticspro.dto.UserDto;
import ru.sinitsynme.analyticspro.entity.UserEntity;

public interface UserMapper {

    UserEntity toEntity(UserDto dto);

}
