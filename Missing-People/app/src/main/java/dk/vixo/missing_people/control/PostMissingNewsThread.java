package dk.vixo.missing_people.control;

import android.os.AsyncTask;

public class PostMissingNewsThread extends AsyncTask<String, String, String> {

    public byte[] imgArr;
    public String message;

    public PostMissingNewsThread(byte[] imgArr, String message) {
        this.imgArr = imgArr;
        this.message = message;
    }

    @Override
    protected String doInBackground(String... params) {
        return null;
    }
}
