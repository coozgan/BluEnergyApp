package bluenergyfuel.bluenergy.model;

/**
 * Created by jockinjc0 on 4/23/17.
 */

public class ItemsList {

    private String title, points, image;

    public ItemsList(String title, String points, String image) {
        this.title = title;
        this.points = points;
        this.image = image;
    }

    public ItemsList(){
        //empty contructor
    }

    public String getTitle() {
        return title;
    }

    public String getPoints() {
        return points;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

}
