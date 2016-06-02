package dk.vixo.missing_people.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import dk.vixo.missing_people.R;
import dk.vixo.missing_people.model.Missing;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private OnFragmentInteractionListener mListener;
    private GoogleMap gMap;

    ArrayList<Missing> missingArr = new ArrayList<Missing>();

    public MapsFragment() {
        // Required empty public constructor
    }

    public static MapsFragment newInstance() {
        MapsFragment fragment = new MapsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_maps, container, false);



        return v;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;

        gMap.getUiSettings().setZoomControlsEnabled(true);
        gMap.getUiSettings().setCompassEnabled(true);
    }

    public void updateMap(ArrayList<Missing> newList) {
        missingArr.clear();
        for(Missing single : newList) {
            missingArr.add(single);
        }


    }

    public void addMarkers() {
        gMap.clear();

        for(Missing single : missingArr) {
            if(!single.getGeoPosition().equals("")) {
                String[] latlng = single.getGeoPosition().split(",");
                int lat = Integer.parseInt(latlng[0]);
                int lng = Integer.parseInt(latlng[1]);
                gMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)));
            } else {
                gMap.addMarker(new MarkerOptions().position(new LatLng(55.66980, 12.57003)).title("Der er ikke oplyst position"));
            }
        }
    }

    public interface OnMapsInterationListener {
        public void onMissingMapsUpdate();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
