package dk.vixo.missing_people.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import dk.vixo.missing_people.MainActivity;
import dk.vixo.missing_people.R;
import dk.vixo.missing_people.control.ImageScaler;
import dk.vixo.missing_people.control.MissingNewsAdapter;
import dk.vixo.missing_people.model.Missing;
import dk.vixo.missing_people.model.SearchNews;
import dk.vixo.missing_people.model.User;

public class MissingNewsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    MissingNewsAdapter newsAdapter;
    Missing missingDetail;

    ListView listView;

    ArrayList<SearchNews> newsList = new ArrayList();

    public MissingNewsFragment() {
        // Required empty public constructor
    }

    public static MissingNewsFragment newInstance(String param1, String param2) {
        MissingNewsFragment fragment = new MissingNewsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        missingDetail = new Missing();
        missingDetail.setId((int) bundle.getLong("MissingId"));

        newsAdapter = new MissingNewsAdapter(getContext(), newsList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_missing_news, container, false);

        listView = (ListView) v.findViewById(R.id.missingNewsList);
        listView.setAdapter(newsAdapter);

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        new FetchNews().execute();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public class FetchNews extends AsyncTask<String, String, String> {

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").setPrettyPrinting().create();

        @Override
        protected String doInBackground(String... params) {

            newsList.clear();

            try {
                URL url = new URL("http://missing-1martinkarlsen.rhcloud.com/Missing_People/api/missing/allMissingNews");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                SharedPreferences userPref = ((MainActivity) getActivity()).getSharedPreferences("userPref", Context.MODE_PRIVATE);
                User myUser = gson.fromJson(userPref.getString("User", null), User.class);

                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("MissingId", missingDetail.getId());

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
                    JSONObject json = new JSONObject(response.toString());
                    JSONArray jsonObj = json.getJSONArray("News");

                    for(int i = 0; i < jsonObj.length(); i++) {
                        JSONObject singleObj = jsonObj.getJSONObject(i);
                        JSONObject userObj = singleObj.getJSONObject("User");
                        SearchNews singleNews = new SearchNews();
                        singleNews.setId(singleObj.getInt("Id"));
                        User user = new User();
                        user.setId(userObj.getLong("Id"));
                        user.setFirstname(userObj.getString("Firstname"));
                        user.setLastname(userObj.getString("Lastname"));
                        singleNews.setPostUser(user);
                        singleNews.setDescription(singleObj.getString("Description"));
                        singleNews.setGeoPosition(singleObj.getString("GeoPosition"));

                        // Fixing image
                        String imgStr = singleObj.getString("Photo");
                        Bitmap bm = null;
                        if(singleObj.getString("Photo").equals("")) {
                            bm = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                        } else {
                            byte[] imgArr = Base64.decode(imgStr, Base64.DEFAULT);
                            bm = BitmapFactory.decodeByteArray(imgArr, 0, imgArr.length);
                        }

                        singleNews.setPhoto(bm);
                        newsList.add(singleNews);
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            newsAdapter.SetNewsList(newsList);
            newsAdapter.notifyDataSetChanged();
        }
    }
}
