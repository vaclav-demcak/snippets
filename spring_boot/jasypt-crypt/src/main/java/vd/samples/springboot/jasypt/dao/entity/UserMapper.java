package vd.samples.springboot.jasypt.dao.entity;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import vd.samples.springboot.jasypt.dao.SqlDateMapperUtil;
import vd.samples.springboot.jasypt.model.UserDto;
import vd.samples.springboot.jasypt.model.UserResponseDto;

@Mapper(uses = SqlDateMapperUtil.class)
public interface UserMapper {

  UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

  User mapToEntity(UserDto dto);

  UserDto mapToDto(User entity);

  UserResponseDto mapToResponseDto(User entity);
}
