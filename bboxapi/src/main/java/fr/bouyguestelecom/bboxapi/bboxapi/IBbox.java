package fr.bouyguestelecom.bboxapi.bboxapi;

import fr.bouyguestelecom.bboxapi.bboxapi.callback.IBboxApplication;
import fr.bouyguestelecom.bboxapi.bboxapi.callback.IBboxDisplayToast;
import fr.bouyguestelecom.bboxapi.bboxapi.callback.IBboxGetApplicationIcon;
import fr.bouyguestelecom.bboxapi.bboxapi.callback.IBboxGetApplications;
import fr.bouyguestelecom.bboxapi.bboxapi.callback.IBboxGetChannel;
import fr.bouyguestelecom.bboxapi.bboxapi.callback.IBboxGetChannelListOnBox;
import fr.bouyguestelecom.bboxapi.bboxapi.callback.IBboxGetChannels;
import fr.bouyguestelecom.bboxapi.bboxapi.callback.IBboxGetCurrentChannel;
import fr.bouyguestelecom.bboxapi.bboxapi.callback.IBboxGetEpg;
import fr.bouyguestelecom.bboxapi.bboxapi.callback.IBboxGetEpgSimple;
import fr.bouyguestelecom.bboxapi.bboxapi.callback.IBboxGetListEpg;
import fr.bouyguestelecom.bboxapi.bboxapi.callback.IBboxGetOpenedChannels;
import fr.bouyguestelecom.bboxapi.bboxapi.callback.IBboxGetSessionId;
import fr.bouyguestelecom.bboxapi.bboxapi.callback.IBboxGetSpideoTv;
import fr.bouyguestelecom.bboxapi.bboxapi.callback.IBboxGetVolume;
import fr.bouyguestelecom.bboxapi.bboxapi.callback.IBboxMedia;
import fr.bouyguestelecom.bboxapi.bboxapi.callback.IBboxMessage;
import fr.bouyguestelecom.bboxapi.bboxapi.callback.IBboxRegisterApp;
import fr.bouyguestelecom.bboxapi.bboxapi.callback.IBboxSendMessage;
import fr.bouyguestelecom.bboxapi.bboxapi.callback.IBboxSetVolume;
import fr.bouyguestelecom.bboxapi.bboxapi.callback.IBboxStartApplication;
import fr.bouyguestelecom.bboxapi.bboxapi.callback.IBboxStopApplication;
import fr.bouyguestelecom.bboxapi.bboxapi.callback.IBboxSubscribe;
import fr.bouyguestelecom.bboxapi.bboxapi.callback.IBboxUnsubscribe;
import fr.bouyguestelecom.bboxapi.bboxapi.model.ChannelProfil;
import fr.bouyguestelecom.bboxapi.bboxapi.model.EpgMode;
import fr.bouyguestelecom.bboxapi.bboxapi.model.Moment;

public interface IBbox {

    /*

    void getToken(IBboxGetToken iBboxGetToken);

    */

    void getSessionId(String ip, String appId, String appSecret, IBboxGetSessionId iBboxGetSessionId);

    void getChannel(String appId, String appSecret, int epgChannelNumber, IBboxGetChannel iBboxGetChannel);

    void getChannels(String appId, String appSecret, ChannelProfil profil, IBboxGetChannels iBboxGetChannels);

    void getEpg(String appId, String appSecret, int epgChannelNumber, int period, String externalId, int limit, int page, EpgMode mode, IBboxGetListEpg iBboxGetListEpg);

    void getEpgByEventId(String appId, String appSecret, String eventId, EpgMode mode, IBboxGetEpg iBboxGetEpg);

    void getEpgSimple(String appId, String appSecret, String startTime, String endTime, EpgMode mode, IBboxGetEpgSimple iBboxGetEpgSimple);

    void getRecoTv(String appId, String appSecret, String user, Moment moment, IBboxGetSpideoTv iBboxGetSpideoTv);

    void getApps(String ip, String appId, String appSecret, IBboxGetApplications iBboxGetApplications);

    void getCurrentChannel(String ip, String appId, String appSecret, IBboxGetCurrentChannel iBboxGetCurrentChannel);

    void registerApp(String ip, String appId, String appSecret, String appName, IBboxRegisterApp iBboxRegisterApp);

    void subscribeNotification(String ip, String appId, String appSecret, String appRegisterId, String ressourceId, IBboxSubscribe iBboxSubscribe);

    String addListener(String ip, String appId, IBboxMedia iBboxMedia);

    void removeMediaListener(String ip, String appId, String channelListenerId);

    void removeAppListener(String ip, String appId, String channelListenerId);

    void removeMsgListener(String ip, String appId, String channelListenerId);

    String addListener(String ip, String appId, IBboxApplication iBboxApplication);

    String addListener(String ip, String appId, IBboxMessage iBboxMessage);

    void startApp(String ip, String appId, String appSecret, String packageName, IBboxStartApplication iBboxStartApplication);

    void startApp(String ip, String appId, String appSecret, String packageName, String deeplink, IBboxStartApplication iBboxStartApplication);

    void stopApp(String ip, String appId, String appSecret, String packageName, IBboxStopApplication iBboxStopApplication);

    void displayToast(final String ip, String appId, String appSecret,
                      final String msg, final String color, final String duration,
                      final String pos_x, final String pos_y,
                      final IBboxDisplayToast iBboxDisplayToast);

    void setVolume(final String ip, String appId, String appSecret,
                   final String volume,
                   final IBboxSetVolume iBboxSetVolume);

    void getVolume(final String ip, String appId, String appSecret,
                   final IBboxGetVolume iBboxGetVolume);

    void getChannelListOnBox(final String ip, String appId, String appSecret,
                             final IBboxGetChannelListOnBox iBboxGetChannelListOnBox);

    void getAppInfo(String ip, String appId, String appSecret, String packageName, IBboxGetApplications iBboxGetApplications);

    void getAppIcon(String ip, String appId, String appSecret, String packageName, IBboxGetApplicationIcon iBboxGetApplicationIcon);

    void getOpenedChannels(String ip, String appId, String appSecret, IBboxGetOpenedChannels iBboxGetOpenedChannels);

    void unsubscribeNotification(String ip, String appId, String appSecret, String channelId, IBboxUnsubscribe iBboxUnsubscribe);

    void sendMessage(final String ip, final String appId, String appSecret,
                     final String channelIdOrRoomName, final String appIdFromRegister, final String msgToSend,
                     final IBboxSendMessage iBboxSendMessage);


    /*

    void getApps(String ip, String appId, String appSecret, IBboxGetApplications iBboxGetApplications);

    void getUrlLogoChannel(String ip, String appId, String appSecret, int channel, IBboxGetLogoChannel IBboxGetLogoChannel);

    void registerApp( String ip, String appId, String appSecret,  String appName,  IBboxRegisterApp iBboxRegisterApp);

    void subscribeNotifChannel( String ip,  String appId, String appSecret,  String registerApp,  IBboxSubscribe iBboxSubscribe);

    String addNotifChannelListener(IBboxMedia iChannelListener);

    void removeNotifChannelListener(String channelListenerId);

    void cancelRequest();

    */
}
