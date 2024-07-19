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
public class MetalQualityReportDto {

    private MetalMinerOrganisationDto organisation;
    private String verdict;

    public MetalMinerOrganisationDto getOrganisation() {
        return organisation;
    }

    public void setOrganisation(MetalMinerOrganisationDto organisation) {
        this.organisation = organisation;
    }

    public String getVerdict() {
        return verdict;
    }

    public void setVerdict(String verdict) {
        this.verdict = verdict;
    }

}
