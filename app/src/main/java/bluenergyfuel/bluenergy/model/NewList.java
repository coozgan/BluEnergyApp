package bluenergyfuel.bluenergy.model;

/**
 * Created by jockinjc0 on 5/15/17.
 */

public class NewList {
    private String image;
    private String title;
    private String contents;
    public NewList(){
        //empty constructor
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public NewList(String image, String title, String contents) {
        this.image = image;
        this.title = title;
        this.contents = contents;
    }

}
