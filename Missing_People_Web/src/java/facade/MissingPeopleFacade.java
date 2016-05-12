package facade;

import control.DbConnecter;
import entity.Missing;
import entity.Photo;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

public class MissingPeopleFacade {
    
    DbConnecter dbConn = new DbConnecter();
    EntityManagerFactory emf = dbConn.getEntityManager();
    EntityManager em;
    
    public List<Missing> getAll() {
        em = emf.createEntityManager();
        try {
            Query query = em.createQuery("SELECT m FROM Missing m");
            return query.getResultList();
        } catch (Exception e) {
            System.out.println("You done fucked up! -> " + e.getMessage());
        } finally {
            em.close();
        }
        return null;
    }
    
    public Missing createSearch(Missing missingToCreate) {
        em = emf.createEntityManager();
        System.out.println("missingToCreate.getNameOfMissingPerson() " + missingToCreate.getNameOfMissingPerson());
        System.out.println("missingToCreate.getImage() " + missingToCreate.getImage());
        System.out.println("missingToCreate.getImage().getImgType() " + missingToCreate.getImage().getImgType());
        System.out.println("missingToCreate.getImage().getId() " + missingToCreate.getImage().getId());
        System.out.println("missingToCreate.getImage().toString() " + missingToCreate.getImage().toString());
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
}
