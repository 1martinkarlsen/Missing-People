package dk.vixo.missing_people;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import dk.vixo.missing_people.control.CameraPreview;
import dk.vixo.missing_people.control.ImageScaler;

public class CameraActivity extends AppCompatActivity {

    public static final int MEDIA_TYPE_IMAGE = 1;

    private Camera camera;
    private CameraPreview cameraPreview;

    private Button captureBtn;

    private Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);

            Bitmap scaledBm = ImageScaler.getResizedBitmap(bm, 300, 300);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            scaledBm.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();


            if (data == null) {
                return;
            }

            releaseCamera();
            //Intent pictureAcceptence = new Intent(CameraActivity.this, PictureAcceptActivity.class);
            //pictureAcceptence.putExtra("Picture", data);
            //startActivity(pictureAcceptence);
            //finish();

            Intent intentData = new Intent();
            intentData.putExtra("ImageByteArr", byteArray);
            setResult(RESULT_OK, intentData);
            finish();

//            try {
//                FileOutputStream fos = new FileOutputStream(file);
//                fos.write(data);
//                fos.close();
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_layout);

        camera = GetCameraInstance();
        cameraPreview = new CameraPreview(CameraActivity.this, camera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(cameraPreview);

        captureBtn = (Button) findViewById(R.id.captureBtn);
        captureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //camera.takePicture(null, null, pictureCallback);
                camera.takePicture(null, null, pictureCallback);
                Toast.makeText(CameraActivity.this, "Picture taken!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        releaseCamera();
    }

    public static Camera GetCameraInstance() {
        Camera c = null;

        try {
            c = Camera.open();
        } catch (Exception e) {

        }

        return c;
    }

    public void releaseCamera() {
        if (camera != null) {
            camera.setPreviewCallback(null);
            camera.stopPreview();
            cameraPreview.getHolder().removeCallback(cameraPreview);
            camera.release();
            camera = null;
        }
    }

//    private static Uri getOutputMediaFileUri(int type) {
//        return Uri.fromFile(getOutputMediaFile(type));
//    }

    private static File getOutputMediaFile(int type) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MissingPeople");

        Log.v("SD CARD", Environment.getExternalStorageState());
        Log.v("SD CARD", mediaStorageDir.getPath());

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.v("MEDIASTORAGE", "Oops!");
                return null;
            }
        }

        // Create media file
        String timeStamp = new SimpleDateFormat("ddmmyyyy").format(new Date());
        File mediaFile;

        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + timeStamp + ".jpg");
            Log.d("here", "WORKS!");
        } else {
            Log.d("here", "else.. !media_type_image");
            return null;
        }

        return mediaFile;
    }
}
