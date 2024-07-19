package vd.sandbox.spring.jpa.api;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import vd.sandbox.spring.jpa.db.model.User;
import vd.sandbox.spring.jpa.model.UserDto;
import vd.sandbox.spring.jpa.model.UserResponseDto;

@Mapper(uses = SqlDateMapperUtil.class)
public interface UserMapper {

  UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

  User mapToEntity(UserDto dto);

  UserDto mapToDto(User entity);

  UserResponseDto mapToResponseDto(User entity);
}
