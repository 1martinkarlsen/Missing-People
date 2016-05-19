package dk.vixo.missing_people;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.LoaderManager;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridLayout;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import dk.vixo.missing_people.control.MissingListAdapter;
import dk.vixo.missing_people.fragments.MissingListFragment;
import dk.vixo.missing_people.fragments.ProfileFragment;
import dk.vixo.missing_people.fragments.SpecificMissingFragment;
import dk.vixo.missing_people.model.Missing;

public class MainActivity extends AppCompatActivity
        implements MissingListFragment.OnMissingItemClickedListener {

    // Fragments
    public MissingListFragment missingListFragment;
    public ProfileFragment profileFragment;
    public SpecificMissingFragment specificMissingFragment;

    ArrayList<Missing> missingArr = new ArrayList<Missing>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(findViewById(R.id.frameLayoutFragHolder) != null) {
            if(savedInstanceState != null) {
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
            if(findViewById(R.id.frameLayoutFragHolder) != null) {

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
    public void onMissingSelected(int position) {
        if(findViewById(R.id.frameLayoutFragHolder) != null) {
            specificMissingFragment = new SpecificMissingFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutFragHolder, specificMissingFragment).commit();
        }
    }
}
