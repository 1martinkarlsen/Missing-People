package dk.vixo.missing_people.control;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import dk.vixo.missing_people.MainActivity;
import dk.vixo.missing_people.model.User;

public class PostMissingNewsThread extends AsyncTask<String, String, Boolean> {

    Context context;
    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").setPrettyPrinting().create();

    public String missingId;
    public byte[] imgArr;
    public String message;

    public PostMissingNewsThread(Context context, String missingId, byte[] imgArr, String message) {
        this.context = context;
        this.missingId = missingId;
        this.imgArr = imgArr;
        this.message = message;
    }

    @Override
    protected Boolean doInBackground(String... params) {

        try {
            URL url = new URL("http://missing-1martinkarlsen.rhcloud.com/Missing_People/api/missing/postNews");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            SharedPreferences userPref = (context.getApplicationContext().getSharedPreferences("userPref", Context.MODE_PRIVATE));
            User myUser = gson.fromJson(userPref.getString("User", null), User.class);


            String encodedImg = Base64.encodeToString(imgArr, Base64.DEFAULT);

            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("Id", myUser.getId());
            jsonObject.accumulate("MissingId", missingId);
            jsonObject.accumulate("Message", message);
            jsonObject.accumulate("ImageArr", encodedImg);

            Log.v("## ID", "" + myUser.getId());
            Log.v("## MID", "" + missingId);
            Log.v("## MES", message);
            Log.v("## IMG", "" + encodedImg);

            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setChunkedStreamingMode(0);
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.addRequestProperty("Accept", "application/json");
            urlConnection.addRequestProperty("Content-Type", "application/json");

            DataOutputStream out = new DataOutputStream(urlConnection.getOutputStream());
            out.writeBytes(jsonObject.toString());
            out.flush();
            out.close();

            int responseCode = urlConnection.getResponseCode();

            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }

            br.close();

            if (responseCode == 200) {
                JSONObject missingObj = new JSONObject(response.toString());
                boolean isPosted = missingObj.getBoolean("IsPosted");

                Log.v("IS POSTED?", "" + isPosted);

                return isPosted;
            }
            return false;
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    protected void onPostExecute(Boolean b) {
        super.onPostExecute(b);

        if(b) {
            Toast.makeText(context.getApplicationContext(), "News uploaded!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context.getApplicationContext(), "News uploaded!", Toast.LENGTH_SHORT).show();
        }
    }
}


