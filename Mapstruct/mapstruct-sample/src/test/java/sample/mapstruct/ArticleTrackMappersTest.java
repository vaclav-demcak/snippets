/*
 *
 *  ↄ⃝ COPYLEFT 2019 ALL BUGS RESERVED by ZaJo.
 *
 */
package sample.mapstruct;

import org.junit.Test;
import sample.mapstruct.dto.ArticleTrackDto;
import sample.mapstruct.dto.ArticleTrackWithDocumentDto;
import sample.mapstruct.mapper.ArticleTrackMapper;
import sample.mapstruct.mapper.ArticleTrackMapperConstant;
import sample.mapstruct.mapper.ArticleTrackMapperExpression;
import sample.mapstruct.mapper.ArticleTrackMapperWithDocument;
import sample.mapstruct.model.ArctileTrack;
import sample.mapstruct.model.Article;
import sample.mapstruct.model.Interior;
import sample.mapstruct.model.MaterialType;
import sample.mapstruct.model.MetalMaterial;
import sample.mapstruct.model.MetalQuality;
import sample.mapstruct.model.MetalQualityReport;
import sample.mapstruct.model.Ornament;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 * @author vaclav
 */
public class ArticleTrackMappersTest {

    @Test
    public void shouldAutomapAndHandleSourceAndTargetPropertyNesting() {

        // -- prepare
        ArctileTrack source = createArticleTrack();

        // -- action
        ArticleTrackDto target = ArticleTrackMapper.INSTANCE.map( source );

        // -- result
        assertThat( target.getName() ).isEqualTo( source.getName() );

        // article and articleDto can be automapped
        assertThat( target.getArticle() ).isNotNull();
        assertThat( target.getArticle().getKind() ).isEqualTo( source.getArticle().getType() );
        assertThat( target.getArticle().getName() ).isNull();

        // automapping takes care of mapping property "metal".
        assertThat( target.getMetal() ).isNotNull();
        assertThat( target.getMetal().getKind() ).isEqualTo( source.getMetal().getKind() );

        // ornament (nested asymetric source)
        assertThat( target.getOrnament() ).isNotNull();
        assertThat( target.getOrnament().getType() ).isEqualTo( source.getInterior().getOrnament().getType() );

        // material (nested asymetric target)
        assertThat( target.getMaterial() ).isNotNull();
        assertThat( target.getMaterial().getManufacturer() ).isNull();
        assertThat( target.getMaterial().getMaterialType() ).isNotNull();
        assertThat( target.getMaterial().getMaterialType().getType() ).isEqualTo( source.getMaterial().getType() );

        //  first symetric then asymetric
        assertThat( target.getQuality() ).isNotNull();
        assertThat( target.getQuality().getReport() ).isNotNull();
        assertThat( target.getQuality().getReport().getVerdict() )
            .isEqualTo( source.getQuality().getReport().getVerdict() );
        assertThat( target.getQuality().getReport().getOrganisation().getApproval() ).isNull();
        assertThat( target.getQuality().getReport().getOrganisation() ).isNotNull();
        assertThat( target.getQuality().getReport().getOrganisation().getName() )
            .isEqualTo( source.getQuality().getReport().getOrganisationName() );
    }

    @Test
    public void shouldAutomapAndHandleSourceAndTargetPropertyNestingReverse() {

        // -- prepare
        ArctileTrack source = createArticleTrack();

        // -- action
        ArticleTrackDto target = ArticleTrackMapper.INSTANCE.map( source );
        ArctileTrack source2 = ArticleTrackMapper.INSTANCE.map( target );

        // -- result
        assertThat( source2.getName() ).isEqualTo( source.getName() );

        // article
        assertThat( source2.getArticle() ).isNotNull();
        assertThat( source2.getArticle().getType() ).isEqualTo( source.getArticle().getType() );

        // interior, designer will not be mapped (asymetric) to target. Here it shows.
        assertThat( source2.getInterior() ).isNotNull();
        assertThat( source2.getInterior().getDesigner() ).isNull();
        assertThat( source2.getInterior().getOrnament() ).isNotNull();
        assertThat( source2.getInterior().getOrnament().getType() )
            .isEqualTo( source.getInterior().getOrnament().getType() );

        // material
        assertThat( source2.getMaterial() ).isNotNull();
        assertThat( source2.getMaterial().getType() ).isEqualTo( source.getMaterial().getType() );

        // metal
        assertThat( source2.getMetal().getKind() ).isEqualTo( source.getMetal().getKind() );

        // quality
        assertThat( source2.getQuality().getReport() ).isNotNull();
        assertThat( source2.getQuality().getReport().getOrganisationName() )
            .isEqualTo( source.getQuality().getReport().getOrganisationName() );
        assertThat( source2.getQuality().getReport().getVerdict() )
            .isEqualTo( source.getQuality().getReport().getVerdict() );
    }

    @Test
    public void shouldAutomapAndHandleSourceAndTargetPropertyNestingAndConstant() {

        // -- prepare
        ArctileTrack source = createArticleTrack();

        // -- action
        ArticleTrackDto target = ArticleTrackMapperConstant.INSTANCE.map( source );

        // -- result

        // fixed value
        assertThat( target.getArticle().getName() ).isEqualTo( "Necklace" );

        // automapping takes care of mapping property "metal".
        assertThat( target.getMetal() ).isNotNull();
        assertThat( target.getMetal().getKind() ).isEqualTo( source.getMetal().getKind() );

        // non-nested and constant
        assertThat( target.getMaterial() ).isNotNull();
        assertThat( target.getMaterial().getManufacturer() ).isEqualTo( "MMM" );
        assertThat( target.getMaterial().getMaterialType() ).isNotNull();
        assertThat( target.getMaterial().getMaterialType().getType() ).isEqualTo( source.getMaterial().getType() );

        assertThat( target.getOrnament() ).isNull();
        assertThat( target.getQuality() ).isNull();

    }

    @Test
    public void shouldAutomapAndHandleSourceAndTargetPropertyNestingAndExpresion() {

        // -- prepare
        ArctileTrack source = createArticleTrack();

        // -- action
        ArticleTrackDto target = ArticleTrackMapperExpression.INSTANCE.map( source );

        // -- result
        assertThat( target.getArticle().getName() ).isEqualTo( "Jaws" );

        assertThat( target.getMaterial() ).isNull();
        assertThat( target.getOrnament() ).isNull();
        assertThat( target.getMetal() ).isNull();

        assertThat( target.getQuality() ).isNotNull();
        assertThat( target.getQuality().getReport() ).isNotNull();
        assertThat( target.getQuality().getReport().getVerdict() )
            .isEqualTo( source.getQuality().getReport().getVerdict() );
        assertThat( target.getQuality().getReport().getOrganisation() ).isNotNull();
        assertThat( target.getQuality().getReport().getOrganisation().getApproval() ).isNull();
        assertThat( target.getQuality().getReport().getOrganisation().getName() ).isEqualTo( "Dunno" );
    }

   @Test
    public void shouldAutomapIntermediateLevelAndMapConstant() {

        // -- prepare
        ArctileTrack source = createArticleTrack();

        // -- action
        ArticleTrackWithDocumentDto target = ArticleTrackMapperWithDocument.INSTANCE.map( source );

        // -- result
        assertThat( target.getArticle().getName() ).isEqualTo( "Jaws" );

        assertThat( target.getMaterial() ).isNull();
        assertThat( target.getOrnament() ).isNull();
        assertThat( target.getMetal() ).isNull();

        assertThat( target.getQuality() ).isNotNull();
        assertThat( target.getQuality().getDocument() ).isNotNull();
        assertThat( target.getQuality().getDocument().getVerdict() )
            .isEqualTo( source.getQuality().getReport().getVerdict() );
        assertThat( target.getQuality().getDocument().getOrganisation() ).isNotNull();
        assertThat( target.getQuality().getDocument().getOrganisation().getApproval() ).isNull();
        assertThat( target.getQuality().getDocument().getOrganisation().getName() ).isEqualTo( "NoIdeaInc" );
    }

    private ArctileTrack createArticleTrack() {
        ArctileTrack articleTrack = new ArctileTrack();

        Article article = new Article();
        article.setType( "Carp" );

        MetalMaterial metalMaterial = new MetalMaterial();
        metalMaterial.setKind( "Silver" );

        Interior interior = new Interior();
        interior.setDesigner( "MrVeryFamous" );
        Ornament ornament = new Ornament();
        ornament.setType( "castle" );
        interior.setOrnament( ornament );

        MetalQuality quality = new MetalQuality();
        MetalQualityReport report = new MetalQualityReport();
        report.setVerdict( "PASSED" );
        report.setOrganisationName( "ACME" );
        quality.setReport( report );

        MaterialType materialType = new MaterialType();
        materialType.setType( "myMaterialType" );

        articleTrack.setName( "MyLittleArticleTrack" );
        articleTrack.setArticle( article );
        articleTrack.setMetal( metalMaterial );
        articleTrack.setInterior( interior );
        articleTrack.setMaterial( materialType );
        articleTrack.setQuality( quality );

        return articleTrack;
    }
}
