/*
 *
 *  ↄ⃝ COPYLEFT 2019 ALL BUGS RESERVED by ZaJo.
 *
 */
package sample.mapstruct.dto;

/**
 *
 * @author vaclav
 */
public class ArticleTrackWithDocumentDto {

    private ArticleDto article;
    private MetalMaterialDto metal;
    private String name;
    private MaterialDto material;
    private OrnamentDto ornament;
    private MetalQualityDocumentDto quality;

    public ArticleDto getArticle() {
        return article;
    }

    public void setArticle(ArticleDto fish) {
        this.article = fish;
    }

    public MetalMaterialDto getMetal() {
        return metal;
    }

    public void setMetal(MetalMaterialDto plant) {
        this.metal = plant;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MaterialDto getMaterial() {
        return material;
    }

    public void setMaterial(MaterialDto material) {
        this.material = material;
    }

    public OrnamentDto getOrnament() {
        return ornament;
    }

    public void setOrnament(OrnamentDto ornament) {
        this.ornament = ornament;
    }

    public MetalQualityDocumentDto getQuality() {
        return quality;
    }

    public void setQuality(MetalQualityDocumentDto quality) {
        this.quality = quality;
    }

}
