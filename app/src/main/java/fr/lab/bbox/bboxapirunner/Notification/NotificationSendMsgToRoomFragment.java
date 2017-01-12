package fr.lab.bbox.bboxapirunner.Notification;

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
import fr.bouyguestelecom.bboxapi.bboxapi.callback.IBboxSendMessage;
import fr.lab.bbox.bboxapirunner.R;
import okhttp3.Request;

/**
 * Created by dinh on 01/07/16.
 */
public class NotificationSendMsgToRoomFragment extends Fragment implements View.OnClickListener {

    private Button mButton;
    private Context ctxt;
    private Handler handler;

    private EditText roomEdit;
    private String room = " ";

    private EditText appIdEdit;
    private String appId = " ";

    private EditText msgEdit;
    private String msg = " ";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_notification_sendmsgtoroom, container, false);

        mButton = (Button) view.findViewById(R.id.try_sendmsgtoroom);
        mButton.setOnClickListener(this);

        ctxt = getActivity().getApplicationContext();
        handler = new Handler();

        roomEdit = (EditText) view.findViewById(R.id.notification_roomname);
        appIdEdit = (EditText) view.findViewById(R.id.notification_appId);
        msgEdit = (EditText) view.findViewById(R.id.notification_msg);

        return view;
    }


    @Override
    public void onClick(final View v) {
        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext().getApplicationContext());
        final String ip = sharedPref.getString("bboxip", "");
        room = "Message/" + roomEdit.getText().toString();
        appId = appIdEdit.getText().toString();
        msg = msgEdit.getText().toString();

        Bbox.getInstance().sendMessage(ip,
                getResources().getString(fr.bouyguestelecom.bboxapi.R.string.APP_ID),
                getResources().getString(fr.bouyguestelecom.bboxapi.R.string.APP_SECRET),
                room, appId, msg,
                new IBboxSendMessage() {

                    @Override
                    public void onResponse() {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ctxt, "Send message success", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onFailure(Request request, int errorCode) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ctxt, "Send message failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

    }

}