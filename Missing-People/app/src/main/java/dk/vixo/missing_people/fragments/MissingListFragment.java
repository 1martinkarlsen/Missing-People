package dk.vixo.missing_people.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dk.vixo.missing_people.MainActivity;
import dk.vixo.missing_people.R;
import dk.vixo.missing_people.control.ImageScaler;
import dk.vixo.missing_people.control.MissingListAdapter;
import dk.vixo.missing_people.model.Missing;
import dk.vixo.missing_people.model.User;

public class MissingListFragment extends ListFragment {

    private OnMissingItemClickedListener mCallback;
    private OnFragmentInteractionListener mListener;

    ArrayList<Missing> missingArr = new ArrayList<Missing>();
    MissingListAdapter missingListAdapter;
    ListView listViewMissingPersons;

    public MissingListFragment() {
        // Required empty public constructor
    }

    public static MissingListFragment newInstance(String param1, String param2) {
        MissingListFragment fragment = new MissingListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new LoadAllMissingPeopleTask().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_missing_list, container, false);
        listViewMissingPersons = (ListView) view.findViewById(R.id.listViewMissingPersons);
        missingListAdapter = new MissingListAdapter(getContext(), missingArr);
        listViewMissingPersons.setAdapter(missingListAdapter);
        listViewMissingPersons.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Missing item = (Missing) listViewMissingPersons.getItemAtPosition(position);

                mCallback.onMissingSelected(position, item);
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof OnMissingItemClickedListener) {
            mCallback = (OnMissingItemClickedListener) context;
        } else {
            throw new ClassCastException(context.toString() +  " must be implementet OnMissingItemClickedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnMissingItemClickedListener {
        public void onMissingSelected(int position, Missing itemDetail);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public class LoadAllMissingPeopleTask extends AsyncTask<String, String, Boolean> {

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").setPrettyPrinting().create();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                URL url = new URL("http://projects-1martinkarlsen.rhcloud.com/Missing_People/api/missing/all");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                SharedPreferences userPref = ((MainActivity) getActivity()).getSharedPreferences("userPref", Context.MODE_PRIVATE);
                User myUser = gson.fromJson(userPref.getString("User", null), User.class);
                Log.v("USER ID ### ", myUser.getId().toString());
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
                        missingArr.add(singlePerson);
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
                missingListAdapter.notifyDataSetChanged();
//                //listViewMissingPersons.setAdapter(missingListAdapter);
            }
        }
    }
}