package fr.lab.bbox.bboxapirunner.Demo;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import fr.lab.bbox.bboxapirunner.*;

/**
 * Created by AlexandreBigot on 19/01/2017.
 */

public class DemoDetection extends Fragment {

    private final static String TAG = MainActivity.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.home_bt_detect, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView foyer = (TextView) getView().findViewById(R.id.foyerID);
        foyer.setText("Foyer Martin");

        TextView loc = (TextView) getView().findViewById(R.id.locBbox);
        loc.setText("Paris");

        int alpha1 = 255;
        int alpha2 = 180;
        int alpha3 = 100;
        int alpha4 = 40;

        // Draw Bbox Miami
        ImageView bbox = (ImageView) getView().findViewById(R.id.bbox_miami);
        bbox.setImageResource(R.drawable.bbox_miami);

        // Draw Waves Circles
        // Circle 1
        final ImageView circle1 = (ImageView) getView().findViewById(R.id.circle1);

        Paint paint1 = new Paint();
        paint1.setColor(Color.argb(alpha1, 135, 206, 250));
        paint1.setStyle(Paint.Style.STROKE);
        paint1.setStrokeWidth(4.5f);

        Bitmap bmp1 = Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888);

        Canvas canvas1 = new Canvas(bmp1);
        canvas1.drawCircle(bmp1.getWidth() / 2, bmp1.getHeight() / 2, 230, paint1);

        circle1.setImageBitmap(bmp1);

        // Circle 2
        ImageView circle2 = (ImageView) getView().findViewById(R.id.circle2);

        Paint paint2 = new Paint();
        paint2.setColor(Color.argb(alpha2, 135, 206, 250));
        paint2.setStyle(Paint.Style.STROKE);
        paint2.setStrokeWidth(4.5f);

        Bitmap bmp2 = Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888);

        Canvas canvas2 = new Canvas(bmp2);
        canvas2.drawCircle(bmp2.getWidth() / 2, bmp2.getHeight() / 2, 230, paint2);

        circle2.setImageBitmap(bmp2);

        // Circle 3
        ImageView circle3 = (ImageView) getView().findViewById(R.id.circle3);

        Paint paint3 = new Paint();
        paint3.setColor(Color.argb(alpha3, 135, 206, 250));
        paint3.setStyle(Paint.Style.STROKE);
        paint3.setStrokeWidth(4.5f);

        Bitmap bmp3 = Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888);

        Canvas canvas3 = new Canvas(bmp3);
        canvas3.drawCircle(bmp3.getWidth() / 2, bmp3.getHeight() / 2, 230, paint3);

        circle3.setImageBitmap(bmp3);

        // Circle 4
        ImageView circle4 = (ImageView) getView().findViewById(R.id.circle4);

        Paint paint4 = new Paint();
        paint4.setColor(Color.argb(alpha4, 135, 206, 250));
        paint4.setStyle(Paint.Style.STROKE);
        paint4.setStrokeWidth(4.5f);

        Bitmap bmp4 = Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888);

        Canvas canvas4 = new Canvas(bmp4);
        canvas4.drawCircle(bmp4.getWidth() / 2, bmp4.getHeight() / 2, 240, paint4);

        circle4.setImageBitmap(bmp4);

        // Smartphone 1
        ImageView phone1 = (ImageView) getView().findViewById(R.id.smartphone1);
        phone1.setImageResource(R.drawable.galaxys7);
        TextView name1 = (TextView) getView().findViewById(R.id.namePhone1);
        name1.setText(DemoConstants.nameDevice1);



        /*
        while (btnPressed) {
            alphaTemp = alpha3;
            alpha3 = alpha2;
            alpha2 = alpha1;
            alpha1 = alphaTemp;

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        */

    }


    public void displayPhone(){

    }

}
