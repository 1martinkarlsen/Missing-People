package entity;

import java.io.File;
import java.io.Serializable;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="mp_search")
public class Missing implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String nameOfMissingPerson;
    @OneToOne(cascade = CascadeType.ALL)
    private Photo image;
    private String description;
    private String geoPosition;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateOfMissing;
    @OneToMany
    @JoinTable (name = "mp_searchAdmins") 
    private List<User> admins = new ArrayList();
    @ManyToMany(mappedBy = "volunteering")
    private List<User> volenteers = new ArrayList();
    @ManyToMany(mappedBy = "following")
    private List<User> followers = new ArrayList();

    public Missing() {
    }
    
    public Missing(String nameOfMissingPerson, String description, Date dateOfMissing) {
        this.nameOfMissingPerson = nameOfMissingPerson;
        this.image = image;
        this.description = description;
        this.dateOfMissing = dateOfMissing;
    }

    public String getGeoPosition() {
        return geoPosition;
    }

    public void setGeoPosition(String geoPosition) {
        this.geoPosition = geoPosition;
    }

    public List<User> getVolenteers() {
        return volenteers;
    }

    public void setVolenteers(List<User> volenteers) {
        this.volenteers = volenteers;
    }

    public Photo getImage() {
        return image;
    }

    public void setImage(Photo image) {
        this.image = image;
    }
    
    public String getNameOfMissingPerson() {
        return nameOfMissingPerson;
    }

    public void setNameOfMissingPerson(String nameOfMissingPerson) {
        this.nameOfMissingPerson = nameOfMissingPerson;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateOfMissing() {
        return dateOfMissing;
    }

    public void setDateOfMissing(Date dateOfMissing) {
        this.dateOfMissing = dateOfMissing;
    }

    public List<User> getAdmins() {
        return admins;
    }

    public void setAdmins(List<User> admins) {
        this.admins = admins;
    }

    public List<User> getFollowers() {
        return followers;
    }

    public void setFollowers(List<User> followers) {
        this.followers = followers;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public void addAdmin(User admin) {
        admins.add(admin);
    }
    
    public void addFollower(User follower) {
        followers.add(follower);
    }
    
    public void addVolunteer(User volunteer) {
        volenteers.add(volunteer);
    }
    
    
    
}
