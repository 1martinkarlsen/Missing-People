package dk.vixo.missing_people.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import dk.vixo.missing_people.R;
import dk.vixo.missing_people.model.Missing;

public class SpecificMissingFragment extends Fragment {

    Missing missingDetail;

    TextView txtName;
    TextView txtDescription;
    TextView txtDate;
    ImageView imgView;

    public SpecificMissingFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SpecificMissingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SpecificMissingFragment newInstance(String param1, String param2) {
        SpecificMissingFragment fragment = new SpecificMissingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        missingDetail = new Missing();
        missingDetail.setName(bundle.getString("NameOfMissingPerson"));
        missingDetail.setDescription(bundle.getString("Description"));

//        // Converting date
//        DateFormat format = new SimpleDateFormat("dd-mm-yyyy");
//        Date compDate = null;
//        try {
//            compDate = format.parse(bundle.getString("DateOfMissing"));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        missingDetail.setDateOfMissing(compDate);

        // Converting image
        Bitmap b = BitmapFactory.decodeByteArray(bundle.getByteArray("ImageOfMissingPerson"), 0, bundle.getByteArray("ImageOfMissingPerson").length);
        missingDetail.setPhotoOfMissingPerson(b);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_specific_missing, container, false);

        // Testing name
        txtName = (TextView) view.findViewById(R.id.detailName);
        txtName.setText(missingDetail.getName());

        // Testing description
        txtDescription = (TextView) view.findViewById(R.id.detailDescription);
        txtDescription.setText(missingDetail.getDescription());

//        // Testing date
//        txtDate = (TextView) view.findViewById(R.id.testDate);
//        txtDate.setText(missingDetail.getDescription());

        // Testing image
        imgView = (ImageView) view.findViewById(R.id.detailImage);
        imgView.setImageBitmap(missingDetail.getPhotoOfMissingPerson());

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
