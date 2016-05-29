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

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import dk.vixo.missing_people.control.MissingListAdapter;
import dk.vixo.missing_people.fragments.MissingListFragment;
import dk.vixo.missing_people.fragments.PostMissingFragment;
import dk.vixo.missing_people.fragments.ProfileFragment;
import dk.vixo.missing_people.fragments.SpecificMissingFragment;
import dk.vixo.missing_people.model.Missing;

public class MainActivity extends AppCompatActivity
        implements MissingListFragment.OnMissingItemClickedListener, SpecificMissingFragment.OnPostClicked {

    // Fragments
    public MissingListFragment missingListFragment;
    public ProfileFragment profileFragment;
    public SpecificMissingFragment specificMissingFragment;
    public PostMissingFragment postMissingFragment;

    //BottomBar buttons
    private ImageButton home;
    private ImageButton maps;
    private ImageButton phone;

    //Permission int
    private int MY_CALL_PHONE = 1;

    ArrayList<Missing> missingArr = new ArrayList<Missing>();

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
}
