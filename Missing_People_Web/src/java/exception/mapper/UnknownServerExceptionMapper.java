package exception.mapper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import exception.UnknownServerException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class UnknownServerExceptionMapper implements ExceptionMapper<UnknownServerException> {

    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    
    @Override
    public Response toResponse(UnknownServerException e) {
        JsonObject json = new JsonObject();
        json.addProperty("httpError", "500");
        json.addProperty("errorCode", "1");
        json.addProperty("message", e.getMessage());
        
        return Response
                .status(500)
                .entity(gson.toJson(json))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
    
}
