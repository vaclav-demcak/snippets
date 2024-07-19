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
public interface ArticleTrackMapperExpression {

    ArticleTrackMapperExpression INSTANCE = Mappers.getMapper(ArticleTrackMapperExpression.class );

    @Mapping(target = "article.kind", source = "article.type")
    @Mapping(target = "article.name", expression = "java(\"Jaws\")")
    @Mapping(target = "metal", ignore = true )
    @Mapping(target = "ornament", ignore = true )
    @Mapping(target = "material", ignore = true)
    @Mapping(target = "quality.report.organisation.name", expression = "java(\"Dunno\")" )
    ArticleTrackDto map(ArctileTrack source);

}
