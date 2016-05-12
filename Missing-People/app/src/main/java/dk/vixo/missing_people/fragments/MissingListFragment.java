package dk.vixo.missing_people.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import dk.vixo.missing_people.MainActivity;
import dk.vixo.missing_people.R;
import dk.vixo.missing_people.control.MissingListAdapter;
import dk.vixo.missing_people.model.Missing;

public class MissingListFragment extends Fragment {

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

        missingListAdapter = new MissingListAdapter(getContext(), missingArr);
        //listViewMissingPersons = (ListView) getView().findViewById(R.id.listViewMissingPersons);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_missing_list, container, false);
        listViewMissingPersons = (ListView) view.findViewById(R.id.listViewMissingPersons);

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
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                URL url = new URL("http://projects-1martinkarlsen.rhcloud.com/Missing_People/api/missing/all");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setRequestMethod("GET");
                urlConnection.addRequestProperty("Accept", "application/json");
                urlConnection.addRequestProperty("Content-Type", "application/json");

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
                listViewMissingPersons.setAdapter(missingListAdapter);
            }
        }
    }
}