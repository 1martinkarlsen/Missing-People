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

        missingListAdapter = new MissingListAdapter(getContext(), missingArr);
    }

    @Override
    public void onResume() {
        super.onResume();

        mCallback.onMissingListUpdate();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_missing_list, container, false);
        listViewMissingPersons = (ListView) view.findViewById(R.id.listViewMissingPersons);

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

    public void updateAdapter(ArrayList<Missing> newList) {
        missingArr.clear();
        for(Missing sing : newList) {
            missingArr.add(sing);
        }
        missingListAdapter.SetMissingList(missingArr);
        missingListAdapter.notifyDataSetChanged();
    }

    public interface OnMissingItemClickedListener {
        public void onMissingListUpdate();
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
}