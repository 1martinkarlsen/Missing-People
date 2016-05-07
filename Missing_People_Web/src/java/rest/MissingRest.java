package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import entity.Missing;
import facade.MissingPeopleFacade;
import java.util.List;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("missing")
public class MissingRest {

    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    MissingPeopleFacade mpFacade = new MissingPeopleFacade();
    
    @Context
    private UriInfo context;

    public MissingRest() {
    }

    @GET
    @Produces("application/json")
    @Path("/all")
    public Response getAllMissingPeople() {
        JsonObject response = new JsonObject();
        JsonArray jsonArr = new JsonArray();
        
        List<Missing> missingList = mpFacade.getAll();
        
        for(Missing missingPerson : mpFacade.getAll()) {
            JsonObject singlePerson = new JsonObject();
            
            singlePerson.addProperty("id", missingPerson.getId());
            singlePerson.addProperty("Name", missingPerson.getNameOfMissingPerson());
            singlePerson.addProperty("Description", missingPerson.getDescription());
            singlePerson.addProperty("DateOfMissing", missingPerson.getDateOfMissing().toString());
            singlePerson.addProperty("GeoPosition", missingPerson.getGeoPosition());
            singlePerson.addProperty("Photo", "");
            
            jsonArr.add(singlePerson);
        }
        return Response.ok(gson.toJson(jsonArr), MediaType.APPLICATION_JSON).build();
    }

}
