package dk.vixo.missing_people;

import android.Manifest;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.app.LoaderManager;
import android.content.Loader;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import dk.vixo.missing_people.control.ImageScaler;
import dk.vixo.missing_people.control.MissingListAdapter;
import dk.vixo.missing_people.control.PostMissingNewsThread;
import dk.vixo.missing_people.fragments.MapsFragment;
import dk.vixo.missing_people.fragments.MissingListFragment;
import dk.vixo.missing_people.fragments.MissingNewsFragment;
import dk.vixo.missing_people.fragments.PostMissingFragment;
import dk.vixo.missing_people.fragments.ProfileFragment;
import dk.vixo.missing_people.fragments.SpecificMissingFragment;
import dk.vixo.missing_people.model.Missing;
import dk.vixo.missing_people.model.User;

public class MainActivity extends AppCompatActivity
        implements MissingListFragment.OnMissingItemClickedListener,
        MapsFragment.OnMapsInterationListener,
        SpecificMissingFragment.OnPostClicked,
        PostMissingFragment.OnCameraActivityStartListener {

    // Fragments
    public MissingListFragment missingListFragment;
    public MapsFragment mapsFragment;
    public ProfileFragment profileFragment;
    public SpecificMissingFragment specificMissingFragment;
    public PostMissingFragment postMissingFragment;
    public MissingNewsFragment newsFragment;

    //BottomBar buttons
    private ImageButton home;
    private ImageButton maps;
    private ImageButton phone;

    //Permission int
    public static final int MY_CALL_PHONE = 1;
    public static final int CAMERA_TAKE_PIC = 3;
    public static final int CAMERA_ACCEPT_INT = 4;

    ArrayList<Missing> missingArr = new ArrayList<Missing>();

    public byte[] newsImageToUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Topbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //BottomBar
        home = (ImageButton) findViewById(R.id.homeBtn);
        maps = (ImageButton) findViewById(R.id.mapsBtn);
        phone = (ImageButton) findViewById(R.id.phoneBtn);

        //Home
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutFragHolder, missingListFragment).commit();
                Toast.makeText(MainActivity.this, "Home", Toast.LENGTH_SHORT).show();
            }
        });
        //Maps
        maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutFragHolder, mapsFragment).commit();
                Toast.makeText(MainActivity.this, "Maps", Toast.LENGTH_SHORT).show();
            }
        });

        //Phone
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:70101155"));
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, MY_CALL_PHONE);
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(callIntent);
            }
        });

        if (findViewById(R.id.frameLayoutFragHolder) != null) {
            if (savedInstanceState != null) {
                return;
            }

            missingListFragment = new MissingListFragment();

            getSupportFragmentManager().beginTransaction().add(R.id.frameLayoutFragHolder, missingListFragment).commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_profile) {
            if (findViewById(R.id.frameLayoutFragHolder) != null) {

                profileFragment = new ProfileFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutFragHolder, profileFragment).commit();
            }
        }
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_logout) {
            SharedPreferences userPref = getSharedPreferences("userPref", Context.MODE_PRIVATE);
            SharedPreferences.Editor prefEditor = userPref.edit();
            prefEditor.clear();
            prefEditor.commit();

            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void onMissingListUpdate() {
        new LoadAllMissingPeopleTask("List").execute();
    }

    @Override
    public void onMissingSelected(int position, Missing itemDetail) {
        if (findViewById(R.id.frameLayoutFragHolder) != null) {
            specificMissingFragment = new SpecificMissingFragment();
            Bundle details = new Bundle();

            // Compressing Bitmap
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            itemDetail.getPhotoOfMissingPerson().compress(Bitmap.CompressFormat.PNG, 100, stream);

//            // Date coverting to string
//            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy:hh:mm:ss");
//            String test = dateFormat.format(itemDetail.getDateOfMissing());

            // Setting missing person details
            details.putLong("Id", itemDetail.getId());
            details.putString("NameOfMissingPerson", itemDetail.getName());
            details.putString("Description", itemDetail.getDescription());
            //details.putString("DateOfMissing", test);
            details.putByteArray("ImageOfMissingPerson", stream.toByteArray());
            details.putBoolean("IsFollowing", itemDetail.isFollowing());
            details.putBoolean("IsVolunteering", itemDetail.isVolunteering());

            specificMissingFragment.setArguments(details);
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutFragHolder, specificMissingFragment).commit();
        }
    }

    @Override
    public void OnPostClicked(Missing itemDetail) {
        if (findViewById(R.id.frameLayoutFragHolder) != null) {
            postMissingFragment = new PostMissingFragment();
            Bundle postInfo = new Bundle();

            postInfo.putLong("MissingId", itemDetail.getId());
            postInfo.putString("NameOfMissingPerson", itemDetail.getName());
            postMissingFragment.setArguments(postInfo);

            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutFragHolder, postMissingFragment).commit();
        }
    }

    @Override
    public void NewsFragment(Missing itemDetail) {
        if(findViewById(R.id.frameLayoutFragHolder) != null) {
            newsFragment = new MissingNewsFragment();
            Bundle b = new Bundle();

            b.putLong("MissingId", itemDetail.getId());
            newsFragment.setArguments(b);

            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutFragHolder, newsFragment).commit();
        }
    }


    /*
    *
    *   START OF POST MISSING FRAGMENT INTERFACE METHODS.
    *
     */

    @Override
    public void StartCameraActivity() {
        Intent cameraIntent = new Intent(MainActivity.this, CameraActivity.class);
        startActivityForResult(cameraIntent, CAMERA_TAKE_PIC);
    }

    @Override
    public void PostMissingNews(String message, String missingId) {
        new PostMissingNewsThread(this, missingId, newsImageToUpload, message).execute();
    }
    /* END OF POST MISSING FRAGMENT INTERFACE METHODS */

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CAMERA_TAKE_PIC) {
            if(resultCode == RESULT_OK) {
                Bundle b = data.getExtras();
                byte[] imgArr = b.getByteArray("ImageByteArr");

                Intent pictureAcceptence = new Intent(MainActivity.this, PictureAcceptActivity.class);
                pictureAcceptence.putExtra("Picture", imgArr);
                Log.v("MAIN", "Starting PictureAcceptActivity");
                startActivityForResult(pictureAcceptence, CAMERA_ACCEPT_INT);
            }
        }
        if(requestCode == CAMERA_ACCEPT_INT) {
            if(resultCode == RESULT_OK) {
                Log.v("RESULT", "" + resultCode);
                Bundle b = data.getExtras();
                Log.v("DATA:", "" + b.getByteArray("ImageToUpload"));
                newsImageToUpload = b.getByteArray("ImageToUpload");
            }
            if(resultCode == RESULT_CANCELED) {
                StartCameraActivity();
            }
        }
    }

    @Override
    public void onMissingMapsUpdate() {
        new LoadAllMissingPeopleTask("Maps").execute();
    }

    public class LoadAllMissingPeopleTask extends AsyncTask<String, String, Boolean> {

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").setPrettyPrinting().create();
        String fragmentToUpdate = "";

        public LoadAllMissingPeopleTask(String frag) {
            this.fragmentToUpdate = frag;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            missingArr.clear();

            try {
                URL url = new URL("http://missing-1martinkarlsen.rhcloud.com/Missing_People/api/missing/all");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                SharedPreferences userPref = MainActivity.this.getSharedPreferences("userPref", Context.MODE_PRIVATE);
                User myUser = gson.fromJson(userPref.getString("User", null), User.class);

                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("id", myUser.getId());

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

                while((inputLine = br.readLine()) != null) {
                    response.append(inputLine);
                }

                br.close();

                if(responseCode == 200) {
                    JSONArray missingList = new JSONArray(response.toString());

                    for(int i = 0; i < missingList.length(); i++) {
                        Missing singlePerson = new Missing(missingList.getJSONObject(i));

//                        // Fixing date
//                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy");
//                        Date newDate = dateFormat.parse(missingList.getJSONObject(i).getString("DateOfMissing"));
//                        singlePerson.setDateOfMissing(newDate);

                        // Fixing image
                        String imgStr  = missingList.getJSONObject(i).getString("Photo");
                        byte[] imgArr = Base64.decode(imgStr, Base64.DEFAULT);
                        //Bitmap bitmap = BitmapFactory.decodeByteArray(imgArr, 0, imgArr.length);

                        singlePerson.setPhotoOfMissingPerson(ImageScaler.decodeSampleBitmapFromByteArray(imgArr, 100, 100));
                        singlePerson.setFollowing(missingList.getJSONObject(i).getBoolean("IsFollowing"));
                        singlePerson.setVolunteering(missingList.getJSONObject(i).getBoolean("IsVolunteering"));

                        missingArr.add(singlePerson);
                    }

                    Log.v("### START MISSING LIST", "###");
                    for(int y = 0; y < missingArr.size(); y++) {
                        Log.v("### MISSING -> ", missingArr.get(y).getName());
                    }

                    return true;
                }
                //Thread.sleep(2000);
            } catch (java.io.IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean s) {
            super.onPostExecute(s);

            if(s) {
                //Log.v("### NOTIFYING ###", "###");
                //missingListAdapter.SetMissingList(missingArr);
                //missingListAdapter.notifyDataSetChanged();

                if(fragmentToUpdate.equals("List")) {
                    missingListFragment.updateAdapter(missingArr);
                }
                if (fragmentToUpdate.equals("Maps")) {
                    mapsFragment.updateMap(missingArr);
                }
            }
        }
    }
}
