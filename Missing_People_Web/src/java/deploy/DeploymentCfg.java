package deploy;

import entity.Role;
import entity.User;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class DeploymentCfg implements ServletContextListener {
    
    public static String PU_NAME = "Missing_PeoplePU";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        Map<String, String> env = System.getenv();
        //If we are running in the OPENSHIFT environment change the pu-name 
        if (env.keySet().contains("OPENSHIFT_MYSQL_DB_HOST")) {
          PU_NAME = "PU_OPENSHIFT";
        }
        try {
            ServletContext context = sce.getServletContext();
            EntityManagerFactory emf = Persistence.createEntityManagerFactory(DeploymentCfg.PU_NAME);
            EntityManager em = emf.createEntityManager();
            
            if(em.find(Role.class, "User") != null && em.find(Role.class, "Admin") != null) {
                return;
            }
            
            Role user = new Role("User");
            Role admin = new Role("Admin");
            User myUser = new User("martin@vixo.dk", "1234", "StartUser", "AdminUser");
            myUser.addRole(user);
            myUser.addRole(admin);
            
            try {
                System.out.println("TEST DEPLOY");
                em.getTransaction().begin();
                em.persist(user);
                em.persist(admin);
                em.persist(myUser);
                em.getTransaction().commit();
            } finally {
                em.close();
            }
        } catch (Exception e) {
            System.out.println("ERROR -> " + e.getMessage());
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}
