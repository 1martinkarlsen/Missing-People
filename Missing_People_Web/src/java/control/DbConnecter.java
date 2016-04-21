package control;

import deploy.DeploymentCfg;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class DbConnecter {
    
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(DeploymentCfg.PU_NAME);
    
    public EntityManagerFactory getEntityManager() {
        return emf;
    }
}
