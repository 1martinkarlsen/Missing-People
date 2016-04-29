package entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="mp_searchNews")
public class SearchNews implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Missing search;
    @ManyToOne
    private User userPosted;
    private String description;
    private List<Photo> photos;
    private String geoPosition;

    public SearchNews() {
        
    }
    
    public SearchNews(Missing search, User userPosted, String description, String geoPosition) {
        this.search = search;
        this.userPosted = userPosted;
        this.description = description;
        this.geoPosition = geoPosition;
    }
    
    public Missing getSearch() {
        return search;
    }

    public void setSearch(Missing search) {
        this.search = search;
    }

    public User getUserPosted() {
        return userPosted;
    }

    public void setUserPosted(User userPosted) {
        this.userPosted = userPosted;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public String getGeoPosition() {
        return geoPosition;
    }

    public void setGeoPosition(String geoPosition) {
        this.geoPosition = geoPosition;
    }
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
}
