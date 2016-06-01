package facade;

import control.DbConnecter;
import entity.Missing;
import entity.Photo;
import entity.SearchNews;
import entity.User;
import exception.UnknownServerException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

public class MissingPeopleFacade {
    
    DbConnecter dbConn = new DbConnecter();
    EntityManagerFactory emf = dbConn.getEntityManager();
    EntityManager em;
    
    UserFacade userFacade = new UserFacade();
    
    public List<Missing> getAll() throws UnknownServerException {
        em = emf.createEntityManager();
        try {
            Query query = em.createQuery("SELECT m FROM Missing m");
            return query.getResultList();
        } catch (Exception e) {
            throw new UnknownServerException(e.getMessage());
        } finally {
            em.close();
        }
    }
    
    public Missing getMissing(long id) throws UnknownServerException {
        em = emf.createEntityManager();
        
        try {
            return em.find(Missing.class, id);
        } catch (Exception e) {
            throw new UnknownServerException(e.getMessage());
        }
    }
    
    public Missing createSearch(Missing missingToCreate) {
        em = emf.createEntityManager();
        
        try {
            em.getTransaction().begin();
            em.persist(missingToCreate);
            em.flush();
            em.getTransaction().commit();
                    
            return missingToCreate;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        
        return null;
    }
    
    // Set user to follow a specific missing.
    public Missing followMissing(String missingToFollow, User userToFollow) throws UnknownServerException {
        em = emf.createEntityManager();
        
        Missing missing = getMissing(Long.parseLong(missingToFollow));
        System.out.println("Missing 1: " + missing.getFollowers());
        
        try {
            missing.addFollower(userToFollow);
            System.out.println("Missing 2: " + missing.getFollowers());
            
            em.getTransaction().begin();
            em.merge(missing);
            em.getTransaction().commit();
            
            System.out.println("Missing 3: " + getMissing(missing.getId()).getFollowers());
            System.out.println("Missing 4: " + missing.getFollowers());
            return getMissing(missing.getId());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        
        return null;
    }
    
    public Missing unfollowMissing(String missingToFollow, User userToFollow) throws UnknownServerException {
        em = emf.createEntityManager();
        
        Missing missing = getMissing(Long.parseLong(missingToFollow));
        List<User> missingFollowers = missing.getFollowers();
        try {
            for(int i = 0; i < missingFollowers.size(); i++) {
                if(missingFollowers.get(i).getId() == userToFollow.getId()) {
                    missingFollowers.remove(i);
                }
            }
            
            missing.setFollowers(missingFollowers);
            
            em.getTransaction().begin();
            em.merge(missing);
            em.getTransaction().commit();
            
            return missing;
        } catch (Exception e) {
            throw new UnknownServerException(e.getMessage());
        } finally {
            em.close();
        }
    }
    
    // Set user to volunteer finding a missing.
    public Missing volunteerMissing(String missingToVolunteer, User userToFollow) throws UnknownServerException {
        em = emf.createEntityManager();
        
        Missing missing = getMissing(Long.parseLong(missingToVolunteer));
        
        try {
            missing.addVolunteer(userToFollow);
            
            em.getTransaction().begin();
            em.merge(missing);
            em.getTransaction().commit();
            
            return missing;
        } catch (Exception e) {
            throw new UnknownServerException(e.getMessage());
        } finally {
            em.close();
        }
    }
 
    
    public Missing unVolunteerMissing(String missingToFollow, User userToFollow) throws UnknownServerException {
        em = emf.createEntityManager();
        
        Missing missing = getMissing(Long.parseLong(missingToFollow));
        List<User> missingVolunteers = missing.getVolunteers();
        try {
            for(int i = 0; i < missingVolunteers.size(); i++) {
                if(missingVolunteers.get(i).getId() == userToFollow.getId()) {
                    missingVolunteers.remove(i);
                }
            }
            
            missing.setVolunteers(missingVolunteers);
            
            em.getTransaction().begin();
            em.merge(missing);
            em.getTransaction().commit();
            
            return missing;
        } catch (Exception e) {
            throw new UnknownServerException(e.getMessage());
        } finally {
            em.close();
        }
    }
    
    public boolean checkIfFollowing(Long missingId, Long userId) throws UnknownServerException {
        em = emf.createEntityManager();
        
        Missing missing = getMissing(missingId);
        List<User> missingFollowers;
        
        try {
            missingFollowers = missing.getFollowers();
        } catch (Exception e) {
            return false;
        }
        
        try {
            for (int i = 0; i < missingFollowers.size(); i++) {
                if(missingFollowers.get(i).getId().equals(userId)) {
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
        
        Missing missing = getMissing(missingId);
        List<User> missingFollowers;
        
        try {
            missingFollowers = missing.getVolunteers();
        } catch (Exception e) {
            return false;
        }
        
        try {
            for (int i = 0; i < missingFollowers.size(); i++) {
                if(missingFollowers.get(i).getId().equals(userId)) {
                    return true;
                }
            }
            
        } catch (Exception e) {
            throw new UnknownServerException(e.getMessage());
        }
        
        return false;
    }
    
    public boolean postMissingNews(User user, String missing, String message, byte[] imgArr) throws UnknownServerException {
        em = emf.createEntityManager();
        
        // Get missing
        Missing missingToPost = getMissing(Long.parseLong(missing));
        
        SearchNews newPost = new SearchNews();
        Photo photo = new Photo();
        photo.setImg(imgArr);
        newPost.setUserPosted(user);
        newPost.setSearch(missingToPost);
        newPost.setDescription(message);
        newPost.setPhotos(photo);
        
        try {
            em.getTransaction().begin();
            em.persist(photo);
            em.persist(newPost);
            em.getTransaction().commit();
            
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        
        return false;
    }
    
    public List<SearchNews> getAllNewsForSpecificMissing(String missingId) throws UnknownServerException {
        em = emf.createEntityManager();
        
        Missing missing = getMissing(Long.parseLong(missingId));
        List<SearchNews> newsList = new ArrayList();
        
        try {
            Query query = em.createQuery("SELECT n FROM SearchNews n WHERE n.search = :searchId");
            query.setParameter("searchId", missing);
            newsList = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return newsList;
    }
}
