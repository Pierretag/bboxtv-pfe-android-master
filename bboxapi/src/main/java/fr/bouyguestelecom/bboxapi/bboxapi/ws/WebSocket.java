package fr.bouyguestelecom.bboxapi.bboxapi.ws;

import android.util.JsonReader;
import android.util.Log;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.Map;

import fr.bouyguestelecom.bboxapi.bboxapi.callback.IBboxApplication;
import fr.bouyguestelecom.bboxapi.bboxapi.callback.IBboxMedia;
import fr.bouyguestelecom.bboxapi.bboxapi.callback.IBboxMessage;
import fr.bouyguestelecom.bboxapi.bboxapi.model.ApplicationResource;
import fr.bouyguestelecom.bboxapi.bboxapi.model.MediaResource;
import fr.bouyguestelecom.bboxapi.bboxapi.model.MessageResource;
import fr.bouyguestelecom.bboxapi.bboxapi.model.Resource;
import fr.bouyguestelecom.bboxapi.bboxapi.util.ListenerList;

public class WebSocket {
    private static final String TAG = WebSocket.class.getSimpleName();
    private static final String WEBSOCKET_PREFIX = "ws://";
    private static final String WEBSOCKET_PORT = "9090";

    private WebSocketClient mWebSocketClient;

    private ListenerList<IBboxMedia> notifMedia = new ListenerList<>();
    private ListenerList<IBboxApplication> notifApps = new ListenerList<>();
    private ListenerList<IBboxMessage> notifMsg = new ListenerList<>();

    private String mAppId;
    private String mWebsocketAddress;

    public WebSocket(String ip, String appId) {
        mAppId = appId;
        mWebsocketAddress = WEBSOCKET_PREFIX + ip + ":" + WEBSOCKET_PORT;
        init();
    }

    private void init() {
        mWebSocketClient = new WebSocketClient(URI.create(mWebsocketAddress)) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                mWebSocketClient.send(mAppId);
            }

            @Override
            public void onMessage(String message) {
                try {
                    InputStream stream = new ByteArrayInputStream(message.getBytes());
                    JsonReader reader = new JsonReader(new InputStreamReader(stream, "UTF-8"));
                    Resource resource = new Resource(reader);

                    JSONObject obj = new JSONObject(message);

                    if (resource.getResourceId().equals("Media")) {
                        MediaResource mediaResource = new MediaResource(new JSONObject(resource.getBody()));

                        for (Map.Entry<String, IBboxMedia> iChannelListenerEntry : notifMedia.getMap().entrySet()) {
                            iChannelListenerEntry.getValue().onNewMedia(mediaResource);
                        }
                    }

                    if (obj.get("resourceId").toString().equals("Application")) {

                        ApplicationResource appResource = new ApplicationResource(obj.getJSONObject("body").get("packageName").toString(),
                                obj.getJSONObject("body").get("state").toString());

                        for (Map.Entry<String, IBboxApplication> iAppListenerEntry : notifApps.getMap().entrySet()) {
                            iAppListenerEntry.getValue().onNewApplication(appResource);
                        }
                    }

                    if (obj.get("resourceId").toString().contains("Message") || obj.get("resourceId").toString().contains(".")) {

                        MessageResource messageResource = new MessageResource(obj.getJSONObject("body").get("source").toString(),
                                obj.getJSONObject("body").get("message").toString());

                        for (Map.Entry<String, IBboxMessage> iMsgListenerEntry : notifMsg.getMap().entrySet()) {
                            iMsgListenerEntry.getValue().onNewMessage(messageResource);
                        }
                    }


                } catch (UnsupportedEncodingException | JSONException e) {
                    Log.e(TAG, "Error occurred", e);
                }
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                Log.i(TAG, "onClose");
            }

            @Override
            public void onError(Exception ex) {
                Log.e(TAG, "onError");
            }
        };

        mWebSocketClient.connect();
    }

    public void close() {
        if (mWebSocketClient != null) {
            mWebSocketClient.close();
        }
    }

    public String addNotifChannelListener(IBboxMedia iChannelListener) {
        return notifMedia.add(iChannelListener);
    }

    public void removeNotifChannelListener(String iMediaListenerId) {
        notifMedia.remove(iMediaListenerId);
    }

    public String addNotifApplication(IBboxApplication iAppListenerId) {
        return notifApps.add(iAppListenerId);
    }

    public void removeNotifApps(String iAppListenerId) {
        notifApps.remove(iAppListenerId);
    }

    public String addNotifMessage(IBboxMessage iMsgListenerId) {
        return notifMsg.add(iMsgListenerId);
    }

    public void removeNotifMsg(String iMsgListenerId) {
        notifMsg.remove(iMsgListenerId);
    }

    public ListenerList<IBboxMedia> getNotifMedia() {
        return notifMedia;
    }

    public ListenerList<IBboxApplication> getNotifApps() {
        return notifApps;
    }

    public ListenerList<IBboxMessage> getNotifMsg() {
        return notifMsg;
    }

}