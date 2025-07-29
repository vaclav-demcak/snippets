package vd.sample.spring.mapstruct.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import vd.sample.spring.mapstruct.model.UserIdentDto;
import vd.sample.spring.mapstruct.repository.entity.User;
import vd.sample.spring.mapstruct.model.UserDto;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "userId.id" , source = "id")
    @Mapping(target = "userName", source = "name")
    @Mapping(target = "userPwd", source = "password")
    UserDto mapDtoFromEntity(User entity);

    @Mapping(target = "id", source = "userId.id")
    @Mapping(target = "name", source = "userName")
    @Mapping(target = "password", source = "userPwd")
    User mapEntityFromDto(UserDto dto);

    @Mapping(target = "id", source = "id")
    UserIdentDto mapDtoIdentFromEntity(User user);
}
