package dk.vixo.missing_people.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dk.vixo.missing_people.MainActivity;
import dk.vixo.missing_people.R;
import dk.vixo.missing_people.model.User;

public class ProfileFragment extends Fragment {

    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private MainActivity mainRef;

    // TextViews
    TextView txtFullName;
    TextView txtEmail;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences userPref = getActivity().getSharedPreferences("userPref", Context.MODE_PRIVATE);

        String userStr = userPref.getString("User", null);
        User myUser;
        String title;

        myUser = gson.fromJson(userStr, User.class);
        title = myUser.getFirstname();

        ((MainActivity) getActivity()).setActionBarTitle(title);

        // Setting user information in TextViews.
        txtFullName.setText(myUser.getFirstname());
        //txtEmail.setText(myUser.getEmail());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Finding TextViews
        txtFullName = (TextView) view.findViewById(R.id.profileShowName);
        txtEmail = (TextView) view.findViewById(R.id.profileShowEmail);

        return inflater.inflate(R.layout.fragment_profile, container, false);
    }
}
