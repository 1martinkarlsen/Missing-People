package dk.vixo.missing_people;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

public class PictureAcceptActivity extends AppCompatActivity {

    FrameLayout pic;
    ImageView img;
    private Button cancelBtn;
    private Button acceptBtn;

    private Bitmap imageAsBitmap;
    private byte[] imageArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_accept);

        pic = (FrameLayout) findViewById(R.id.imageFrameLayout);
        img = (ImageView) findViewById(R.id.pictureToAccept);
        acceptBtn = (Button) findViewById(R.id.acceptBtn);
        cancelBtn = (Button) findViewById(R.id.cancelBtn);
        imageArr = getIntent().getExtras().getByteArray("Picture");
        imageAsBitmap = scaleBitmap(imageArr);

        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //imageAsBitmap = BitmapFactory.decodeByteArray(imageArr, 0, imageArr.length);
                //Bitmap newBitmap = ImageScaler.getResizedBitmap(imageAsBitmap, 100, 100);
                Bitmap newBitmap = BitmapFactory.decodeByteArray(imageArr, 0, imageArr.length);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                newBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                try {
                    Intent dataIntent = new Intent();
                    dataIntent.putExtra("ImageToUpload", byteArray);
                    setResult(RESULT_OK, dataIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                finish();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        img.setImageBitmap(imageAsBitmap);
    }

    public Bitmap scaleBitmap(byte[] data) {
        Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);

        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int screenHeight = getResources().getDisplayMetrics().heightPixels;

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            // Notice that width and height are reversed
            Bitmap scaled = Bitmap.createScaledBitmap(bm, screenHeight, screenWidth, true);
            int w = scaled.getWidth();
            int h = scaled.getHeight();
            // Setting post rotate to 90
            Matrix mtx = new Matrix();
            mtx.postRotate(90);
            // Rotating Bitmap
            bm = Bitmap.createBitmap(scaled, 0, 0, w, h, mtx, true);
        } else {
            //No need to reverse width and height
            Bitmap scaled = Bitmap.createScaledBitmap(bm, screenWidth, screenHeight, true);
            bm = scaled;
        }
        return bm;
    }
}
