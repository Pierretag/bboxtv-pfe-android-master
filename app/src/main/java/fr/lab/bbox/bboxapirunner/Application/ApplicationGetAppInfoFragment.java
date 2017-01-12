package fr.lab.bbox.bboxapirunner.Application;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import fr.bouyguestelecom.bboxapi.bboxapi.Bbox;
import fr.bouyguestelecom.bboxapi.bboxapi.callback.IBboxGetApplications;
import fr.bouyguestelecom.bboxapi.bboxapi.model.Application;
import fr.lab.bbox.bboxapirunner.R;
import okhttp3.Request;

/**
 * Created by dinh on 01/07/16.
 */
public class ApplicationGetAppInfoFragment extends Fragment implements View.OnClickListener {

    private Button mButton;
    private EditText packageNameEdit;
    private String packageName = " ";
    private Context ctxt;
    private Handler handler;

    ArrayList<String> listItems = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    ListView list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_app_getappinfo, container, false);

        mButton = (Button) view.findViewById(R.id.try_getappinfo);
        mButton.setOnClickListener(this);
        packageNameEdit = (EditText) view.findViewById(R.id.app_info_packagename);

        ctxt = getActivity().getApplicationContext();
        handler = new Handler();

        list = (ListView) view.findViewById(R.id.listViewAppInfo);
        adapter = new ArrayAdapter<String>(ctxt, android.R.layout.simple_list_item_1, listItems);
        list.setAdapter(adapter);

        return view;
    }


    @Override
    public void onClick(final View v) {

        listItems.clear();

        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext().getApplicationContext());
        String ip = sharedPref.getString("bboxip", "");
        packageName = packageNameEdit.getText().toString();
        Bbox.getInstance().getAppInfo(ip,
                getResources().getString(fr.bouyguestelecom.bboxapi.R.string.APP_ID),
                getResources().getString(fr.bouyguestelecom.bboxapi.R.string.APP_SECRET),
                packageName,
                new IBboxGetApplications() {
                    @Override
                    public void onResponse(final List<Application> applications) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ctxt, "Get app info success", Toast.LENGTH_SHORT).show();

                                for(int i = 0 ; i < applications.size() ; i++)
                                {
                                    listItems.add("appId : " + applications.get(i).getAppId() + "\n" +
                                            "appName : " + applications.get(i).getAppName() + "\n" +
                                            "appState : " + applications.get(i).getAppState() + "\n" +
                                            "component : " + applications.get(i).getComponent() + "\n" +
                                            "data : " + applications.get(i).getData() + "\n" +
                                            "leanback : " + applications.get(i).isLeanback() + "\n" +
                                            "logoUrl : " + applications.get(i).getUrlLogo() + "\n" +
                                            "packageName : " + applications.get(i).getPackageName()
                                    );
                                }

                                adapter.notifyDataSetChanged();
                            }
                        });
                    }

                    @Override
                    public void onFailure(Request request, int errorCode) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ctxt, "Get app info failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                ClipData myClip = ClipData.newPlainText("copy", listItems.get(pos));
                ClipboardManager myClipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                myClipboard.setPrimaryClip(myClip);
                Toast.makeText(ctxt, "Copied to Clipboard", Toast.LENGTH_SHORT).show();

                return true;
            }
        });
    }

}