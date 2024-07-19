/*
 *
 *  ↄ⃝ COPYLEFT 2019 ALL BUGS RESERVED by ZaJo.
 *
 */
package sample.mapstruct.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import sample.mapstruct.dto.ArticleTrackDto;
import sample.mapstruct.model.ArctileTrack;

/**
 *
 * @author vaclav
 */
@Mapper
public interface ArticleTrackMapperConstant {

    ArticleTrackMapperConstant INSTANCE = Mappers.getMapper(ArticleTrackMapperConstant.class );

    @Mapping(target = "article.kind", source = "article.type")
    @Mapping(target = "article.name", constant = "Necklace")
    @Mapping(target = "ornament", ignore = true )
    @Mapping(target = "material.materialType", source = "material")
    @Mapping(target = "material.manufacturer",  constant = "MMM" )
    @Mapping(target = "quality", ignore = true)
    ArticleTrackDto map(ArctileTrack source);

}
