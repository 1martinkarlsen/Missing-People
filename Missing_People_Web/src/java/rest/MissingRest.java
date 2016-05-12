package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import entity.Missing;
import entity.Photo;
import facade.MissingPeopleFacade;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.imageio.ImageIO;
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
import org.jose4j.base64url.internal.apache.commons.codec.binary.Base64;

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
    public Response getAllMissingPeople() throws SQLException {
        JsonObject response = new JsonObject();
        JsonArray jsonArr = new JsonArray();

        List<Missing> missingList = mpFacade.getAll();

        System.out.println("### MISSINGLIST --> " + missingList);
        
        for (Missing missingPerson : missingList) {
            JsonObject singlePerson = new JsonObject();
            
            String jsonImg = "";
            byte[] imgByteArr;
            
            if(missingPerson.getImage() != null) {
                System.out.println(missingPerson.getImage().getImg());
                jsonImg = Base64.encodeBase64String(missingPerson.getImage().getImg());
            }
            
            singlePerson.addProperty("id", missingPerson.getId());
            singlePerson.addProperty("Name", missingPerson.getNameOfMissingPerson());
            singlePerson.addProperty("Description", missingPerson.getDescription());
            singlePerson.addProperty("DateOfMissing", missingPerson.getDateOfMissing().toString());
            singlePerson.addProperty("GeoPosition", missingPerson.getGeoPosition());
            singlePerson.addProperty("Photo", jsonImg);

            jsonArr.add(singlePerson);
        }
        return Response.ok(gson.toJson(jsonArr), MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Produces("application/json")
    @Consumes("application/json")
    @Path("/create")
    public Response createNewSearch(String content) throws SQLException, IOException {
        System.out.println(content);
        JsonObject json = new JsonParser().parse(content).getAsJsonObject();

        System.out.println(json);
        String[] imgArr = json.get("file").toString().split(",");
        byte[] imageBytes = Base64.decodeBase64(imgArr[1]);
        
        //Blob img = new javax.sql.rowset.serial.SerialBlob(imageBytes);
        Photo photoOfMissingPerson = new Photo();
        photoOfMissingPerson.setImg(imageBytes);
        photoOfMissingPerson.setImgType(imgArr[0]);
        Missing newSearch = gson.fromJson(content, Missing.class);

        newSearch.setImage(photoOfMissingPerson);
        
//        DateFormat dateForUpload = new SimpleDateFormat("dd-MM-yyyy'T'HH:mmZ");
//        String dateAsStr = dateForUpload.format(new Date());
//        BufferedImage img = ImageIO.read(new ByteArrayInputStream(imageBytes));
//        System.out.println("#### " + img);
//        File f = new File("/images/missingpeople/"+dateAsStr+"-"+newSearch.getId());
//        ImageIO.write(img, "png", f);

        return Response.ok(gson.toJson(mpFacade.createSearch(newSearch)), MediaType.APPLICATION_JSON).build();
    }

}
