package fr.lab.bbox.bboxapirunner.Notification;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import fr.bouyguestelecom.bboxapi.bboxapi.Bbox;
import fr.bouyguestelecom.bboxapi.bboxapi.callback.IBboxApplication;
import fr.bouyguestelecom.bboxapi.bboxapi.callback.IBboxMedia;
import fr.bouyguestelecom.bboxapi.bboxapi.callback.IBboxMessage;
import fr.bouyguestelecom.bboxapi.bboxapi.callback.IBboxRegisterApp;
import fr.bouyguestelecom.bboxapi.bboxapi.callback.IBboxSubscribe;
import fr.bouyguestelecom.bboxapi.bboxapi.model.ApplicationResource;
import fr.bouyguestelecom.bboxapi.bboxapi.model.MediaResource;
import fr.bouyguestelecom.bboxapi.bboxapi.model.MessageResource;
import fr.lab.bbox.bboxapirunner.R;
import okhttp3.Request;

/**
 * Created by dinh on 01/07/16.
 */
public class NotificationSubscribeFragment extends Fragment implements View.OnClickListener {

    private Button mButton;
    private Context ctxt;
    private Handler handler;

    private EditText appNameEdit;
    private String appName = " ";

    private EditText ressourceEdit;
    private String ressource_id = " ";

    private TextView channelid_subscribe;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_notification_subscribe, container, false);

        mButton = (Button) view.findViewById(R.id.try_subscribe);
        mButton.setOnClickListener(this);

        ctxt = getActivity().getApplicationContext();
        handler = new Handler();

        appNameEdit = (EditText) view.findViewById(R.id.notification_appname);
        ressourceEdit = (EditText) view.findViewById(R.id.notification_ressource);

        channelid_subscribe = (TextView) view.findViewById(R.id.channelid_subscribe);

        return view;
    }


    @Override
    public void onClick(final View v) {

        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext().getApplicationContext());
        final String ip = sharedPref.getString("bboxip", "");
        appName = appNameEdit.getText().toString();
        ressource_id = ressourceEdit.getText().toString();

        Bbox.getInstance().registerApp(ip,
                getResources().getString(fr.bouyguestelecom.bboxapi.R.string.APP_ID),
                getResources().getString(fr.bouyguestelecom.bboxapi.R.string.APP_SECRET),
                appName,
                new IBboxRegisterApp() {

                    @Override
                    public void onResponse(final String registerApp) {
                        if (registerApp != null && !registerApp.isEmpty()) {

                            Log.i("notif", "ressource_id : " + ressource_id);

                            // TODO : switch/case is better

                            if(ressource_id.equalsIgnoreCase("media")){
                                // Subscribe ressource media
                                Bbox.getInstance().subscribeNotification(ip,
                                        getResources().getString(fr.bouyguestelecom.bboxapi.R.string.APP_ID),
                                        getResources().getString(fr.bouyguestelecom.bboxapi.R.string.APP_SECRET),
                                        registerApp,
                                        "Media",
                                        new IBboxSubscribe() {
                                            @Override
                                            public void onSubscribe() {

                                                handler.post(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        channelid_subscribe.setText("websocket opened");
                                                    }
                                                });

                                                Bbox.getInstance().addListener(ip,
                                                        registerApp,
                                                        new IBboxMedia() {
                                                            @Override
                                                            public void onNewMedia(final MediaResource mediaResource) {

                                                                Log.i("notif", "channel changed");

                                                                handler.post(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        Toast.makeText(ctxt, mediaResource.toString(), Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });

                                                            }
                                                        });
                                            }
                                            @Override
                                            public void onFailure(Request request, int errorCode) {
                                                handler.post(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(ctxt, "Notification failed", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        });
                                // end media
                            }

                            else if(ressource_id.equalsIgnoreCase("application")){
                                // Subscribe ressource application
                                Bbox.getInstance().subscribeNotification(ip,
                                        getResources().getString(fr.bouyguestelecom.bboxapi.R.string.APP_ID),
                                        getResources().getString(fr.bouyguestelecom.bboxapi.R.string.APP_SECRET),
                                        registerApp,
                                        "Application",
                                        new IBboxSubscribe() {
                                            @Override
                                            public void onSubscribe() {

                                                handler.post(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        channelid_subscribe.setText("websocket opened");
                                                    }
                                                });

                                                Bbox.getInstance().addListener(ip,
                                                        registerApp,
                                                        new IBboxApplication() {
                                                            @Override
                                                            public void onNewApplication(final ApplicationResource application) {

                                                                Log.i("notif", "app changed");

                                                                handler.post(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        Toast.makeText(ctxt, application.toString(), Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });

                                                            }
                                                        });
                                            }
                                            @Override
                                            public void onFailure(Request request, int errorCode) {
                                                handler.post(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(ctxt, "Notification failed", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        });
                                // end application
                            }

                            else if(ressource_id.contains("Message") || ressource_id.contains("message")){

                                if(ressource_id.contains("message")) {
                                    ressource_id = ressource_id.replace("message", "Message");
                                }

                                // Subscribe ressource msg
                                Bbox.getInstance().subscribeNotification(ip,
                                        getResources().getString(fr.bouyguestelecom.bboxapi.R.string.APP_ID),
                                        getResources().getString(fr.bouyguestelecom.bboxapi.R.string.APP_SECRET),
                                        registerApp,
                                        ressource_id,
                                        new IBboxSubscribe() {
                                            @Override
                                            public void onSubscribe() {

                                                handler.post(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        channelid_subscribe.setText("websocket opened");
                                                    }
                                                });

                                                Bbox.getInstance().addListener(ip,
                                                        registerApp,
                                                        new IBboxMessage() {
                                                            @Override
                                                            public void onNewMessage(final MessageResource message) {

                                                                Log.i("notif", "new msg received");

                                                                handler.post(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        Toast.makeText(ctxt, message.toString(), Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });

                                                            }
                                                        });
                                            }
                                            @Override
                                            public void onFailure(Request request, int errorCode) {
                                                handler.post(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(ctxt, "Notification failed", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        });
                                // end msg
                            }
                        }
                    }

                    @Override
                    public void onFailure(Request request, int errorCode) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ctxt, "Notification failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
    }

}