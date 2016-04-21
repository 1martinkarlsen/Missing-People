package facade;

import control.DbConnecter;
import entity.User;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import org.eclipse.jdt.core.compiler.InvalidInputException;
import security.PasswordHash;

public class UserFacade {
    
    DbConnecter dbConn = new DbConnecter();
    EntityManagerFactory emf = dbConn.getEntityManager();
    
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
}
