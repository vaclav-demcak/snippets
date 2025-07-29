package vd.sample.spring.mapstruct.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import vd.sample.spring.mapstruct.model.TrainDto;
import vd.sample.spring.mapstruct.model.TrainIdentDto;
import vd.sample.spring.mapstruct.repository.entity.Train;

@Mapper
public interface TrainMapper {

    TrainMapper INSTANCE = Mappers.getMapper(TrainMapper.class);

    @Mapping(target = "trainId.id" , source = "id")
    @Mapping(target = "trainName", source = "name")
    @Mapping(target = "linePrice", source = "linePrice")
    TrainDto mapDtoFromEntity(Train entity);

    @Mapping(target = "id", source = "trainId.id")
    @Mapping(target = "name", source = "trainName")
    @Mapping(target = "linePrice", source = "linePrice")
    Train mapEntityFromDto(TrainDto dto);

    @Mapping(target = "id", source = "id")
    TrainIdentDto mapDtoIdentFromEntity(Train entity);
}
