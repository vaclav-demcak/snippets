/*
 *
 *  ↄ⃝ COPYLEFT 2019 ALL BUGS RESERVED by ZaJo.
 *
 */
package sample.mapstruct.mapper;

import org.mapstruct.InheritInverseConfiguration;
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
public interface ArticleTrackMapper {

    ArticleTrackMapper INSTANCE = Mappers.getMapper(ArticleTrackMapper.class );

     @Mapping(target = "article.kind", source = "article.type")
     @Mapping(target = "article.name", ignore = true)
     @Mapping(target = "ornament", source = "interior.ornament")
     @Mapping(target = "material.materialType", source = "material")
     @Mapping(target = "quality.report.organisation.name", source = "quality.report.organisationName")
    ArticleTrackDto map(ArctileTrack source);

    @Mapping(target = "article.kind", source = "source.article.type")
    @Mapping(target = "article.name", ignore = true)
    @Mapping(target = "ornament", source = "source.interior.ornament")
    @Mapping(target = "material.materialType", source = "source.material")
    @Mapping(target = "quality.report.organisation.name", source = "source.quality.report.organisationName")
    ArticleTrackDto mapAsWell(ArctileTrack source);

    @InheritInverseConfiguration( name = "map" )
    ArctileTrack map(ArticleTrackDto source);

}
