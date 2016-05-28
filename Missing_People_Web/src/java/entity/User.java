package entity;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import security.PasswordHash;

@Entity
@Table(name="mp_users")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String email;
    private String password;
    private String firstname;
    private String lastname;
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLogin;
    private boolean isBanned;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "mp_userrole", joinColumns = {
    @JoinColumn(name = "id", referencedColumnName = "id")}, inverseJoinColumns = {
    @JoinColumn(name = "roleName")})
    private List<Role> roles = new ArrayList();
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "mp_followers", joinColumns = {
    @JoinColumn(name = "user_id", referencedColumnName = "id")}, inverseJoinColumns = {
    @JoinColumn(name = "follower_id", referencedColumnName = "id")})
    private List<Missing> following = new ArrayList();
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "mp_volunteers", joinColumns = {
    @JoinColumn(name = "id", referencedColumnName = "id")})
    private List<Missing> volunteering = new ArrayList();
    
    public User() {
        
    }
    
    public User(String email, String password, String firstname, String lastname) throws NoSuchAlgorithmException, InvalidKeySpecException {
        this.email = email;
        this.password = PasswordHash.createHash(password);
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public List<Missing> getFollowing() {
        return following;
    }

    public void setFollowing(List<Missing> following) {
        this.following = following;
    }
    
    public void addFollowing(Missing search) {
        following.add(search);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        this.password = PasswordHash.createHash(password);
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

    public boolean isIsBanned() {
        return isBanned;
    }

    public void setIsBanned(boolean isBanned) {
        this.isBanned = isBanned;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public void addRole(Role role) {
        roles.add(role);
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public List<Missing> getVolunteering() {
        return volunteering;
    }

    public void setVolunteering(List<Missing> volunteering) {
        this.volunteering = volunteering;
    }
    
    public void addVolunteering(Missing missing) {
        this.volunteering.add(missing);
    }
}
