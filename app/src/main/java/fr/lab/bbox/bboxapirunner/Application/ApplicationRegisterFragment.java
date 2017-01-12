package fr.lab.bbox.bboxapirunner.Application;

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
import android.widget.TextView;
import android.widget.Toast;

import fr.bouyguestelecom.bboxapi.bboxapi.Bbox;
import fr.bouyguestelecom.bboxapi.bboxapi.callback.IBboxRegisterApp;
import fr.lab.bbox.bboxapirunner.R;
import okhttp3.Request;

/**
 * Created by dinh on 01/07/16.
 */
public class ApplicationRegisterFragment extends Fragment implements View.OnClickListener {

    private Button mButton;
    private Context ctxt;
    private Handler handler;
    private EditText appNameEdit;
    private String appName = " ";
    private TextView appid_reg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_app_register, container, false);

        mButton = (Button) view.findViewById(R.id.try_regapp);
        mButton.setOnClickListener(this);

        ctxt = getActivity().getApplicationContext();
        handler = new Handler();

        appNameEdit = (EditText) view.findViewById(R.id.app_reg_appname);

        appid_reg = (TextView) view.findViewById(R.id.appid_reg);

        return view;
    }


    @Override
    public void onClick(final View v) {

        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext().getApplicationContext());
        String ip = sharedPref.getString("bboxip", "");
        appName = appNameEdit.getText().toString();
        Bbox.getInstance().registerApp(ip,
                getResources().getString(fr.bouyguestelecom.bboxapi.R.string.APP_ID),
                getResources().getString(fr.bouyguestelecom.bboxapi.R.string.APP_SECRET),
                appName,
                new IBboxRegisterApp() {

                    @Override
                    public void onResponse(final String appId) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ctxt, "Register app success", Toast.LENGTH_SHORT).show();
                                appid_reg.setText("appId : " + appId);
                            }
                        });
                    }

                    @Override
                    public void onFailure(Request request, int errorCode) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ctxt, "Register app failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
    }

}