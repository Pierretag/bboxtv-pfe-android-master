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
import fr.bouyguestelecom.bboxapi.bboxapi.callback.IBboxSetVolume;
import fr.lab.bbox.bboxapirunner.R;
import okhttp3.Request;

/**
 * Created by dinh on 01/07/16.
 */
public class UserInterfaceSetVolumeFragment extends Fragment implements View.OnClickListener {

    Button mButton;
    EditText volumedit;
    String volume = "0";
    private Context ctxt;
    private Handler handler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_userinterface_setvolume, container, false);

        mButton = (Button) view.findViewById(R.id.try_setvolume);
        mButton.setOnClickListener(this);
        volumedit = (EditText) view.findViewById(R.id.volume_setvolume);

        ctxt = getActivity().getApplicationContext();
        handler = new Handler();

        return view;
    }


    @Override
    public void onClick(View v) {
        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext().getApplicationContext());
        String ip = sharedPref.getString("bboxip", "");
        volume = volumedit.getText().toString();
        Bbox.getInstance().setVolume(ip,
                getResources().getString(fr.bouyguestelecom.bboxapi.R.string.APP_ID),
                getResources().getString(fr.bouyguestelecom.bboxapi.R.string.APP_SECRET),
                volume,
                new IBboxSetVolume() {
                    @Override
                    public void onResponse() {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ctxt, "Set volume success", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onFailure(Request request, int errorCode) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ctxt, "Set volume failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
    }
}