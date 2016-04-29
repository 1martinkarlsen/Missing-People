package deploy;

import entity.Missing;
import entity.Role;
import entity.SearchNews;
import entity.User;
import java.util.Date;
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
            User martin = new User("martin@vixo.dk", "1234", "Martin", "Karlsen");
            User rasmus = new User("info@terranaut.dk", "1234", "Rasmus", "Hansen");
            User kasper = new User("kasper@sylvestworm.dk", "1234", "Kasper", "Worm");
            martin.addRole(user);
            martin.addRole(admin);
            rasmus.addRole(user);
            rasmus.addRole(admin);
            kasper.addRole(user);
            kasper.addRole(admin);
            
            Missing missingPerson1 = new Missing("Kasper Worm", "Forsvandt efter tur på Postpubben. Han plejer at dukke op efter skole, men mødte aldrig op, fredag.", new Date());
            missingPerson1.addAdmin(rasmus);
            SearchNews news1 = new SearchNews(missingPerson1, martin, "Stadig ikke ankommet kl. 20", "GeoPosition");
            SearchNews news2 = new SearchNews(missingPerson1, martin, "Stadig ikke ankommet kl. 24", "GeoPosition");
            Missing missingPerson2 = new Missing("Kasper Worm", "Forsvandt efter tur på Postpubben. Han plejer at dukke op efter skole, men mødte aldrig op, fredag.", new Date());
            missingPerson2.addAdmin(rasmus);
            SearchNews news21 = new SearchNews(missingPerson2, martin, "Stadig ikke ankommet kl. 20", "GeoPosition");
            SearchNews news22 = new SearchNews(missingPerson2, martin, "Stadig ikke ankommet kl. 24", "GeoPosition");
            Missing missingPerson3 = new Missing("Kasper Worm", "Forsvandt efter tur på Postpubben. Han plejer at dukke op efter skole, men mødte aldrig op, fredag.", new Date());
            missingPerson3.addAdmin(rasmus);
            SearchNews news31 = new SearchNews(missingPerson3, martin, "Stadig ikke ankommet kl. 20", "GeoPosition");
            SearchNews news32 = new SearchNews(missingPerson3, martin, "Stadig ikke ankommet kl. 24", "GeoPosition");
            Missing missingPerson4 = new Missing("Kasper Worm", "Forsvandt efter tur på Postpubben. Han plejer at dukke op efter skole, men mødte aldrig op, fredag.", new Date());
            missingPerson4.addAdmin(rasmus);
            SearchNews news41 = new SearchNews(missingPerson4, martin, "Stadig ikke ankommet kl. 20", "GeoPosition");
            SearchNews news42 = new SearchNews(missingPerson4, martin, "Stadig ikke ankommet kl. 24", "GeoPosition");
            Missing missingPerson5 = new Missing("Kasper Worm", "Forsvandt efter tur på Postpubben. Han plejer at dukke op efter skole, men mødte aldrig op, fredag.", new Date());
            missingPerson5.addAdmin(rasmus);
            SearchNews news51 = new SearchNews(missingPerson5, martin, "Stadig ikke ankommet kl. 20", "GeoPosition");
            SearchNews news52 = new SearchNews(missingPerson5, martin, "Stadig ikke ankommet kl. 24", "GeoPosition");
            Missing missingPerson6 = new Missing("Kasper Worm", "Forsvandt efter tur på Postpubben. Han plejer at dukke op efter skole, men mødte aldrig op, fredag.", new Date());
            missingPerson6.addAdmin(rasmus);
            SearchNews news61 = new SearchNews(missingPerson6, martin, "Stadig ikke ankommet kl. 20", "GeoPosition");
            SearchNews news62 = new SearchNews(missingPerson6, martin, "Stadig ikke ankommet kl. 24", "GeoPosition");
            Missing missingPerson7 = new Missing("Kasper Worm", "Forsvandt efter tur på Postpubben. Han plejer at dukke op efter skole, men mødte aldrig op, fredag.", new Date());
            missingPerson7.addAdmin(rasmus);
            SearchNews news71 = new SearchNews(missingPerson7, martin, "Stadig ikke ankommet kl. 20", "GeoPosition");
            SearchNews news72 = new SearchNews(missingPerson7, martin, "Stadig ikke ankommet kl. 24", "GeoPosition");
            Missing missingPerson8 = new Missing("Kasper Worm", "Forsvandt efter tur på Postpubben. Han plejer at dukke op efter skole, men mødte aldrig op, fredag.", new Date());
            missingPerson8.addAdmin(rasmus);
            SearchNews news81 = new SearchNews(missingPerson8, martin, "Stadig ikke ankommet kl. 20", "GeoPosition");
            SearchNews news82 = new SearchNews(missingPerson8, martin, "Stadig ikke ankommet kl. 24", "GeoPosition");
            Missing missingPerson9 = new Missing("Kasper Worm", "Forsvandt efter tur på Postpubben. Han plejer at dukke op efter skole, men mødte aldrig op, fredag.", new Date());
            missingPerson9.addAdmin(rasmus);
            SearchNews news91 = new SearchNews(missingPerson9, martin, "Stadig ikke ankommet kl. 20", "GeoPosition");
            SearchNews news92 = new SearchNews(missingPerson9, martin, "Stadig ikke ankommet kl. 24", "GeoPosition");
            
            try {
                System.out.println("TEST DEPLOY");
                em.getTransaction().begin();
                
                em.persist(user);
                em.persist(admin);
                em.persist(martin);
                em.persist(rasmus);
                em.persist(kasper);
                em.persist(missingPerson1);
                em.persist(news1);
                em.persist(news2);
                em.persist(missingPerson2);
                em.persist(news21);
                em.persist(news22);
                em.persist(missingPerson3);
                em.persist(news31);
                em.persist(news32);
                em.persist(missingPerson4);
                em.persist(news41);
                em.persist(news42);
                em.persist(missingPerson5);
                em.persist(news51);
                em.persist(news52);
                em.persist(missingPerson6);
                em.persist(news61);
                em.persist(news62);
                em.persist(missingPerson7);
                em.persist(news71);
                em.persist(news72);
                em.persist(missingPerson8);
                em.persist(news81);
                em.persist(news82);
                em.persist(missingPerson9);
                em.persist(news91);
                em.persist(news92);
                
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
