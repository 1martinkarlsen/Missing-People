package entity.deploy;

import entity.Role;
import entity.User;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-06-02T21:05:38")
@StaticMetamodel(Role.class)
public class Role_ { 

    public static volatile ListAttribute<Role, User> users;
    public static volatile SingularAttribute<Role, String> roleName;

}