package dk.vixo.missing_people.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import dk.vixo.missing_people.MainActivity;
import dk.vixo.missing_people.R;
import dk.vixo.missing_people.model.Missing;

public class PostMissingFragment extends Fragment {

    private OnCameraActivityStartListener mCallback;
    private Missing missing;
    private OnFragmentInteractionListener mListener;

    private Button postBtn;
    private EditText editText;

    // Request code
    private int MY_CAMERA = 2;

    public ImageButton cameraBtn;

    public PostMissingFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static PostMissingFragment newInstance(String param1, String param2) {
        PostMissingFragment fragment = new PostMissingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        missing = new Missing();
        missing.setId((int) bundle.getLong("MissingId"));
        missing.setName(bundle.getString("NameOfMissingPerson"));
    }

    @Override
    public void onResume() {
        super.onResume();

        ((MainActivity) getActivity()).setActionBarTitle(missing.getName());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_post_missing, container, false);

        postBtn = (Button) v.findViewById(R.id.postNewsBtn);
        editText = (EditText) v.findViewById(R.id.editTextMessage);
        cameraBtn = (ImageButton) v.findViewById(R.id.imageBtnCamera);
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, MY_CAMERA);

                        return;
                    }
                } else {
                    Toast.makeText(getActivity(), "Can't detect any camera on this device.", Toast.LENGTH_SHORT).show();
                }

                mCallback.StartCameraActivity();
            }
        });

        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().equals("")) {
                    return;
                } else {
                    mCallback.PostMissingNews(editText.getText().toString(), "" + missing.getId());
                }
            }
        });

        return v;
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

        if (context instanceof OnCameraActivityStartListener) {
            mCallback = (OnCameraActivityStartListener) context;
        } else {
            throw new ClassCastException(context.toString() + " must be implementet OnMissingItemClickedListener");
        }
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

    public interface OnCameraActivityStartListener {
        void StartCameraActivity();

        void PostMissingNews(String text, String missingId);
    }
}
