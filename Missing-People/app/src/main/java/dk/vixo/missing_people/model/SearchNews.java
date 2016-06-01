package dk.vixo.missing_people.model;

import android.graphics.Bitmap;

public class SearchNews {

    private long id;
    private String description;
    private String geoPosition;
    private Bitmap photo;
    private User postUser;

    public SearchNews() {
    }

    public SearchNews(long id, String description, String geoPosition, Bitmap photo, User postUser) {
        this.id = id;
        this.description = description;
        this.geoPosition = geoPosition;
        this.photo = photo;
        this.postUser = postUser;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGeoPosition() {
        return geoPosition;
    }

    public void setGeoPosition(String geoPosition) {
        this.geoPosition = geoPosition;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    public User getPostUser() {
        return postUser;
    }

    public void setPostUser(User postUser) {
        this.postUser = postUser;
    }
}
