/*
 *
 *  ↄ⃝ COPYLEFT 2019 ALL BUGS RESERVED by ZaJo.
 *
 */
package sample.mapstruct.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import sample.mapstruct.dto.ArticleTrackWithDocumentDto;
import sample.mapstruct.model.ArctileTrack;

/**
 *
 * @author vaclav
 */
@Mapper
public interface ArticleTrackMapperWithDocument {

    ArticleTrackMapperWithDocument INSTANCE = Mappers.getMapper(ArticleTrackMapperWithDocument.class );

    @Mapping(target = "article.kind", source = "article.type")
    @Mapping(target = "article.name", expression = "java(\"Jaws\")")
    @Mapping(target = "metal", ignore = true )
    @Mapping(target = "ornament", ignore = true )
    @Mapping(target = "material", ignore = true)
    @Mapping(target = "quality.document", source = "quality.report")
    @Mapping(target = "quality.document.organisation.name", constant = "NoIdeaInc" )
    ArticleTrackWithDocumentDto map(ArctileTrack source);

}
