package dk.vixo.missing_people.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import dk.vixo.missing_people.MainActivity;
import dk.vixo.missing_people.R;
import dk.vixo.missing_people.control.ImageScaler;
import dk.vixo.missing_people.model.Missing;
import dk.vixo.missing_people.model.User;

public class SpecificMissingFragment extends Fragment {

    private OnPostClicked mCallback;
    Missing missingDetail;

    TextView txtName;
    TextView txtDescription;
    TextView txtDate;
    ImageView imgView;

    Button followBtn;
    Button volunteerBtn;
    Button postBtn;
    Button loadNewsBtn;

    public interface OnPostClicked {
        public void OnPostClicked(Missing itemDetail);
        public void NewsFragment(Missing itemDetail);
    }

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
        missingDetail.setId((int) bundle.getLong("Id"));
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

        missingDetail.setFollowing(bundle.getBoolean("IsFollowing"));
        missingDetail.setVolunteering(bundle.getBoolean("IsVolunteering"));
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

        followBtn = (Button) view.findViewById(R.id.btnFollow);
        volunteerBtn = (Button) view.findViewById(R.id.btnVolunteer);
        postBtn = (Button) view.findViewById(R.id.btnPost);
        loadNewsBtn = (Button) view.findViewById(R.id.loadNewsBtn);

        // Set color of Follow button
        if(missingDetail.isFollowing()) {
            followBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            followBtn.setText(R.string.Unfollow);
        } else {
            followBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            followBtn.setText(R.string.Follow);
        }

        // Set color of Volunteer button
        if(missingDetail.isVolunteering()) {
            volunteerBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            volunteerBtn.setText(R.string.UnVolunteer);
        } else {
            volunteerBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            volunteerBtn.setText(R.string.Volunteer);
        }


        // Set onclickListeners
        followBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(missingDetail.isFollowing()) {
                    new EngageMissingAsync("UnFollow").execute();
                } else {
                    new EngageMissingAsync("Follow").execute();
                }
            }
        });
        volunteerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(missingDetail.isVolunteering()) {
                    new EngageMissingAsync("UnVolunteer").execute();
                } else {
                    new EngageMissingAsync("Volunteer").execute();
                }
            }
        });

        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.OnPostClicked(missingDetail);
            }
        });

        loadNewsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.NewsFragment(missingDetail);
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof OnPostClicked) {
            mCallback = (OnPostClicked) context;
        } else {
            throw new ClassCastException(context.toString() +  " must be implementet OnMissingItemClickedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public class EngageMissingAsync extends AsyncTask<String, String, Missing> {

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").setPrettyPrinting().create();
        String data = "";

        public EngageMissingAsync(String engage) {
            data = engage;
        }

        @Override
        protected Missing doInBackground(String... params) {

            URL url = null;

            switch (data) {
                case "Follow":
                    try {
                        url = new URL("http://missing-1martinkarlsen.rhcloud.com/Missing_People/api/missing/follow");
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    break;

                case "UnFollow":
                    try {
                        url = new URL("http://missing-1martinkarlsen.rhcloud.com/Missing_People/api/missing/unfollow");
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    break;

                case "Volunteer":
                    try {
                        url = new URL("http://missing-1martinkarlsen.rhcloud.com/Missing_People/api/missing/volunteer");
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    break;

                case "UnVolunteer":
                    try {
                        url = new URL("http://missing-1martinkarlsen.rhcloud.com/Missing_People/api/missing/unvolunteer");
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    break;
            }

            try {
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                SharedPreferences userPref = ((MainActivity) getActivity()).getSharedPreferences("userPref", Context.MODE_PRIVATE);
                User myUser = gson.fromJson(userPref.getString("User", null), User.class);

                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("uid", myUser.getId());
                jsonObject.accumulate("sid", missingDetail.getId());

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
                    JSONObject missingObj = new JSONObject(response.toString());
                    Missing newMissing = new Missing(missingObj);

                    // Fixing image
                    String imgStr = missingObj.getString("Photo");
                    byte[] imgArr = Base64.decode(imgStr, Base64.DEFAULT);
                    //Bitmap bitmap = BitmapFactory.decodeByteArray(imgArr, 0, imgArr.length);

                    newMissing.setPhotoOfMissingPerson(ImageScaler.decodeSampleBitmapFromByteArray(imgArr, 100, 100));

                    newMissing.setFollowing(missingObj.getBoolean("IsFollowing"));
                    newMissing.setVolunteering(missingObj.getBoolean("IsVolunteering"));

                    missingDetail = newMissing;

                    return newMissing;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Missing missing) {
            super.onPostExecute(missing);

            if(missing != null) {

                if(data.equals("Follow") || data.equals("UnFollow")) {
                    // Set followBtn color
                    if (missing.isFollowing()) {
                        followBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                        followBtn.setText(R.string.Unfollow);
                        Toast.makeText(getActivity(), R.string.ToastFollow, Toast.LENGTH_SHORT).show();
                    } else {
                        followBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        followBtn.setText(R.string.Follow);
                        Toast.makeText(getActivity(), R.string.ToastUnFollow, Toast.LENGTH_SHORT).show();
                    }
                }

                if(data.equals("Volunteer") || data.equals("UnVolunteer")) {
                    // Set volunteerBtn color
                    if (missing.isVolunteering()) {
                        volunteerBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                        volunteerBtn.setText(R.string.Inactive);
                        Toast.makeText(getActivity(), R.string.ToastVolun, Toast.LENGTH_SHORT).show();
                    } else {
                        volunteerBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        volunteerBtn.setText(R.string.Active);
                        Toast.makeText(getActivity(), R.string.ToastUnVolun, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }
}
