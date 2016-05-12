package dk.vixo.missing_people.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class User {

    private Long id;
    private String email;
    private String firstname;
    private String lastname;
    private Date created;
    private Date lastLogin;
    private boolean isBanned;
    private List<Missing> following = new ArrayList<Missing>();
    private List<Missing> volunteering = new ArrayList<Missing>();

    public User() {
    }

    public User(JSONObject jsonObject) {
        try {
            this.id = jsonObject.getLong("id");
            this.email = jsonObject.getString("email");
            this.firstname = jsonObject.getString("firstname");
            this.lastname = jsonObject.getString("lastname");
            this.isBanned = jsonObject.getBoolean("isBanned");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public boolean isBanned() {
        return isBanned;
    }

    public void setBanned(boolean banned) {
        isBanned = banned;
    }

    public List<Missing> getFollowing() {
        return following;
    }

    public void setFollowing(List<Missing> following) {
        this.following = following;
    }

    public List<Missing> getVolunteering() {
        return volunteering;
    }

    public void setVolunteering(List<Missing> volunteering) {
        this.volunteering = volunteering;
    }
}
