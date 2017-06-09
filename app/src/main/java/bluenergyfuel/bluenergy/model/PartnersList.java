package bluenergyfuel.bluenergy.model;

/**
 * Created by jockinjc0 on 5/3/17.
 */

public class PartnersList {

    private String partnerStore;
    private String description;
    private String image;
    private String subData;

    public String getSubData() {
        return subData;
    }

    public void setSubData(String subData) {
        this.subData = subData;
    }

    public PartnersList(){
        // Empty Constructor ...
    }

    public PartnersList(String partnerStore, String description, String image, String subData) {
        this.partnerStore = partnerStore;
        this.description = description;
        this.image = image;
        this.subData = subData;
    }

    public String getPartnerStore() {
        return partnerStore;
    }

    public void setPartnerStore(String partnerStore) {
        this.partnerStore = partnerStore;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
