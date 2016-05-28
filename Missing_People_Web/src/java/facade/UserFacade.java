package facade;

import control.DbConnecter;
import entity.Missing;
import entity.User;
import exception.UnknownServerException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import org.eclipse.jdt.core.compiler.InvalidInputException;
import security.PasswordHash;

public class UserFacade {
    
    DbConnecter dbConn = new DbConnecter();
    EntityManagerFactory emf = dbConn.getEntityManager();
    EntityManager em;
    
    public User login(String email, String password) throws InvalidInputException, NoSuchAlgorithmException, InvalidKeySpecException {
        return authenticate(email, password);
    }
    
    public User authenticate(String email, String password) throws InvalidInputException, NoSuchAlgorithmException, InvalidKeySpecException {
        EntityManager em = emf.createEntityManager();
        try {
            Query query = em.createQuery("SELECT u FROM User u WHERE u.email = :email");
            query.setParameter("email", email);
            User user = (User) query.getSingleResult();
            if (user == null) {
                return null;
            }
                
            if(!PasswordHash.validatePassword(password, user.getPassword()))
                throw new InvalidInputException("Invalid email or password.");
                
            return user;
            

        } finally {
            em.close();
        }
    }
    
    public User getUser(long id) {
        EntityManager em = emf.createEntityManager();
        
        try {
            return em.find(User.class, id);
        } catch (Exception e) {
            System.out.println("Could not find user -> " + e.getMessage());
        } finally {
            em.close();
        }
        
        return null;
    }
    
    // Set user to follow a specific missing.
    public User followMissing(Missing missingToFollow, String userToFollow) throws UnknownServerException {
        em = emf.createEntityManager();
        
        User user = getUser(Long.parseLong(userToFollow));
        
        try {
            user.addFollowing(missingToFollow);
            
            em.getTransaction().begin();
            em.merge(user);
            em.getTransaction().commit();
            
            return getUser(user.getId());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        
        return null;
    }
    
    public User unfollowMissing(String missingToFollow, String userToFollow) throws UnknownServerException {
        em = emf.createEntityManager();
        
        User user = getUser(Long.parseLong(userToFollow));
        List<Missing> missingFollowers = user.getFollowing();
        try {
            for(int i = 0; i < missingFollowers.size(); i++) {
                if(missingFollowers.get(i).getId() == Long.parseLong(missingToFollow)) {
                    missingFollowers.remove(i);
                }
            }
            
            user.setFollowing(missingFollowers);
            
            em.getTransaction().begin();
            em.merge(user);
            em.getTransaction().commit();
            
            return user;
        } catch (Exception e) {
            throw new UnknownServerException(e.getMessage());
        } finally {
            em.close();
        }
    }

    public User volunteerMissing(Missing missingToVolunteer, String userToVolunteer) {
        em = emf.createEntityManager();
        
        User user = getUser(Long.parseLong(userToVolunteer));
        
        try {
            user.addVolunteering(missingToVolunteer);
            
            em.getTransaction().begin();
            em.merge(user);
            em.getTransaction().commit();
            
            return getUser(user.getId());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        
        return null;
    }
    
    public User unvolunteerMissing(String missingToUnVolunteer, String userToVolunteer) throws UnknownServerException {
        em = emf.createEntityManager();
        
        User user = getUser(Long.parseLong(userToVolunteer));
        List<Missing> missingVolunteers = user.getVolunteering();
        try {
            for(int i = 0; i < missingVolunteers.size(); i++) {
                if(missingVolunteers.get(i).getId() == Long.parseLong(missingToUnVolunteer)) {
                    missingVolunteers.remove(i);
                }
            }
            
            user.setVolunteering(missingVolunteers);
            
            em.getTransaction().begin();
            em.merge(user);
            em.getTransaction().commit();
            
            return getUser(user.getId());
        } catch (Exception e) {
            throw new UnknownServerException(e.getMessage());
        } finally {
            em.close();
        }
    }
    
    public boolean checkIfFollowing(Long missingId, Long userId) throws UnknownServerException {
        em = emf.createEntityManager();
        
        User user = getUser(userId);
        List<Missing> missingFollowers;
        try {
            missingFollowers = user.getFollowing();
        } catch (Exception e) {
            return false;
        }
        
        try {
            for (int i = 0; i < missingFollowers.size(); i++) {
                if(missingFollowers.get(i).getId().equals(missingId)) {
                    return true;
                }
            }
        } catch (Exception e) {
            throw new UnknownServerException(e.getMessage());
        }
        
        return false;
    }
    
    public boolean checkIfVolunteering(Long missingId, Long userId) throws UnknownServerException {
        em = emf.createEntityManager();
        
        User user = getUser(userId);
        List<Missing> missingFollowers;
        
        try {
            missingFollowers = user.getVolunteering();
        } catch (Exception e) {
            return false;
        }
        
        try {
            for (int i = 0; i < missingFollowers.size(); i++) {
                if(missingFollowers.get(i).getId().equals(missingId)) {
                    return true;
                }
            }
            
        } catch (Exception e) {
            throw new UnknownServerException(e.getMessage());
        }
        
        return false;
    }
}
