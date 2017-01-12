package fr.lab.bbox.bboxapirunner.UserInterface;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import fr.bouyguestelecom.bboxapi.bboxapi.Bbox;
import fr.bouyguestelecom.bboxapi.bboxapi.callback.IBboxDisplayToast;
import fr.bouyguestelecom.bboxapi.bboxapi.callback.IBboxSetVolume;
import fr.lab.bbox.bboxapirunner.R;
import okhttp3.Request;

/**
 * Created by dinh on 01/07/16.
 */
public class UserInterfaceDisplayToast extends Fragment implements View.OnClickListener {

    Button mButton;
    EditText msgedit;
    String msg = "";
    EditText coloredit;
    String color = "";
    EditText durationedit;
    String duration = "";
    EditText posxedit;
    String posx = "";
    EditText posyedit;
    String posy = "";
    private Context ctxt;
    private Handler handler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_userinterface_toast, container, false);

        mButton = (Button) view.findViewById(R.id.try_toast);
        mButton.setOnClickListener(this);
        msgedit = (EditText) view.findViewById(R.id.toast_message);
        coloredit = (EditText) view.findViewById(R.id.toast_color);
        durationedit = (EditText) view.findViewById(R.id.toast_duration);
        posxedit = (EditText) view.findViewById(R.id.toast_x);
        posyedit = (EditText) view.findViewById(R.id.toast_y);

        ctxt = getActivity().getApplicationContext();
        handler = new Handler();

        return view;
    }


    @Override
    public void onClick(View v) {
        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext().getApplicationContext());
        String ip = sharedPref.getString("bboxip", "");
        msg = msgedit.getText().toString();
        color = coloredit.getText().toString();
        duration = durationedit.getText().toString();
        posx = posxedit.getText().toString();
        posy = posyedit.getText().toString();
        Bbox.getInstance().displayToast(ip,
                getResources().getString(fr.bouyguestelecom.bboxapi.R.string.APP_ID),
                getResources().getString(fr.bouyguestelecom.bboxapi.R.string.APP_SECRET),
                msg, color, duration, posx, posy,
                new IBboxDisplayToast() {
                    @Override
                    public void onResponse() {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ctxt, "Display toast success", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onFailure(Request request, int errorCode) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ctxt, "Display toast failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
    }
}