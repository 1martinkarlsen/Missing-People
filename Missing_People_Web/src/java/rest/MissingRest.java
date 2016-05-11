package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import entity.Missing;
import entity.Photo;
import facade.MissingPeopleFacade;
import java.io.InputStream;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("missing")
public class MissingRest {

    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").setPrettyPrinting().create();
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
        
        for(Missing missingPerson : missingList) {
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
    
    @POST
    @Produces("application/json")
    @Consumes("application/json")
    @Path("/create")
    public Response createNewSearch(String content) {
        System.out.println(content);
        JsonObject json = new JsonParser().parse(content).getAsJsonObject();
        
        System.out.println(json);
        
        Photo photoOfMissingPerson = gson.fromJson(json.get("file"), Photo.class);
        Missing newSearch = gson.fromJson(content, Missing.class);
        newSearch.setImage(photoOfMissingPerson);
        
        // return null;
        return Response.ok(gson.toJson(mpFacade.createSearch(newSearch)), MediaType.APPLICATION_JSON).build();
    }

}
