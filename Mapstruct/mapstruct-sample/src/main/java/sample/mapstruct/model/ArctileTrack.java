/*
 *
 *  ↄ⃝ COPYLEFT 2019 ALL BUGS RESERVED by ZaJo.
 *
 */
package sample.mapstruct.model;

/**
 *
 * @author vaclav
 */
public class ArctileTrack {

    private Article article;
    private MetalMaterial metal;
    private String name;
    private MaterialType material;
    private Interior interior;
    private MetalQuality quality;

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article fish) {
        this.article = fish;
    }

    public MetalMaterial getMetal() {
        return metal;
    }

    public void setMetal(MetalMaterial plant) {
        this.metal = plant;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MaterialType getMaterial() {
        return material;
    }

    public void setMaterial(MaterialType material) {
        this.material = material;
    }

    public Interior getInterior() {
        return interior;
    }

    public void setInterior(Interior interior) {
        this.interior = interior;
    }

    public MetalQuality getQuality() {
        return quality;
    }

    public void setQuality(MetalQuality quality) {
        this.quality = quality;
    }

}
