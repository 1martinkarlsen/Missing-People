package dk.vixo.missing_people.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dk.vixo.missing_people.MainActivity;
import dk.vixo.missing_people.R;
import dk.vixo.missing_people.model.Missing;

public class MapsFragment extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnInfoWindowClickListener {

    private OnMapsFragmentListener mCallback;
    private OnFragmentInteractionListener mListener;
    private GoogleMap gMap;

    HashMap<String, Missing> missingArr = new HashMap<String, Missing>();

    public MapsFragment() {
        // Required empty public constructor
    }

    public static MapsFragment newInstance(String param1, String param2) {
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

        SupportMapFragment mapFragment = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.maps_container));
        mapFragment.getMapAsync(this);

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();

        mCallback.onMissingMapsUpdate();
    }


    public void updateMap(ArrayList<Missing> newsList) {
        missingArr.clear();

        for(Missing single : newsList) {

            if(!single.getGeoPosition().equals("")) {
                String[] latlng = single.getGeoPosition().split(",");
                int lat = Integer.parseInt(latlng[0]);
                int lng = Integer.parseInt(latlng[1]);
                Log.d("TEST", "" + lat + "-" + lng);

                Marker marker = gMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(single.getName()));
                missingArr.put(marker.getId(), single);
            } else {
                Log.d("NAME", single.getName());
                Marker marker = gMap.addMarker(new MarkerOptions().position(new LatLng(20.67019, 12.57022)).title(single.getName()));
                missingArr.put(marker.getId(), single);
            }
        }
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

        if(context instanceof OnMapsFragmentListener) {
            mCallback = (OnMapsFragmentListener) context;
        } else {
            throw new ClassCastException(context.toString() +  " must be implementet OnMissingItemClickedListener");
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
        gMap.setOnInfoWindowClickListener(this);

    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Log.d("TEST", "TEST");

        for(Map.Entry<String, Missing> entry : missingArr.entrySet()) {
            if (entry.getKey().equals(marker.getId())) {
                mCallback.onMarkerClicked(entry.getValue());
            }
        }
    }

    public interface OnMapsFragmentListener {
        public void onMissingMapsUpdate();
        public void onMarkerClicked(Missing missing);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
