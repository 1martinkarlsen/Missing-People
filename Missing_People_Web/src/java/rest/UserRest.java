package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nimbusds.jose.JOSEException;
import control.TokenController;
import entity.User;
import exception.UnknownServerException;
import facade.UserFacade;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.eclipse.jdt.core.compiler.InvalidInputException;

@Path("user")
public class UserRest {

    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    UserFacade userFacade = new UserFacade();
    TokenController tokenCtrl = new TokenController();
    
    @Context
    private UriInfo context;

    public UserRest() {
    }

    @POST
    @Path("/login")
    @Produces("application/json")
    @Consumes("application/json")
    public Response login(String content) throws InvalidInputException, JOSEException, UnknownServerException, NoSuchAlgorithmException, InvalidKeySpecException {
        JsonObject parser = new JsonParser().parse(content).getAsJsonObject();
        String username = parser.get("email").getAsString();
        String password = parser.get("password").getAsString();
        
        if((parser.get("email").getAsString() == null || parser.get("email").getAsString().equals(""))
                && (parser.get("password").getAsString() == null || parser.get("password").getAsString().equals(""))) {
            throw new InvalidInputException("You have to enter both email address and password to login.");
        }
        
        JsonObject response = new JsonObject();
        User user = userFacade.login(username, password);
        
        if(user != null) {
            String token = tokenCtrl.createToken(username, user.getRoles());
            response.addProperty("email", user.getEmail());
            response.addProperty("firstname", user.getFirstname());
            response.addProperty("lastname", user.getLastname());
            response.addProperty("isBanned", user.isIsBanned());
            response.addProperty("token", token);
        } 
        
        return Response.ok(gson.toJson(response), MediaType.APPLICATION_JSON).build();
    }
}
