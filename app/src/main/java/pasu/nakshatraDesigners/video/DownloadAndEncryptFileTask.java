package pasu.nakshatraDesigners.video;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;

import pasu.nakshatraDesigners.R;
import pasu.nakshatraDesigners.utils.DownloadListener;

/**
 * Created by michaeldunn on 3/13/17.
 */

public class DownloadAndEncryptFileTask extends AsyncTask<Void, Void, Void> {

    private String mUrl;
    private File mFile;
    private Cipher mCipher;
    private Context context;
    NotificationManager notificationManager;
    int Notification_ID = 121;
    DownloadListener downloadListener;
    public DownloadAndEncryptFileTask(String url, File file, Cipher cipher, Context context, DownloadListener listener) {
        if (url == null || url.isEmpty()) {
            throw new IllegalArgumentException("You need to supply a url to a clear MP4 file to download and encrypt, or modify the code to use a local encrypted mp4");
        }
        mUrl = url;
        mFile = file;
        mCipher = cipher;
        this.context = context;
        downloadListener = listener;
    }

    private void downloadAndEncrypt() throws Exception {

        URL url = new URL(mUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.connect();

        if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new IOException("server error: " + connection.getResponseCode() + ", " + connection.getResponseMessage());
        }

        InputStream inputStream = connection.getInputStream();
        FileOutputStream fileOutputStream = new FileOutputStream(mFile);
        CipherOutputStream cipherOutputStream = new CipherOutputStream(fileOutputStream, mCipher);

        byte buffer[] = new byte[1024 * 1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            Log.d(getClass().getCanonicalName(), "reading from http...");
            cipherOutputStream.write(buffer, 0, bytesRead);
        }
        System.out.println("downloadAndEncrypt");
        inputStream.close();
        cipherOutputStream.close();
        connection.disconnect();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        generateNotifications(context,"Download in progress",true);
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            downloadAndEncrypt();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Log.d(getClass().getCanonicalName(), "done");
        downloadListener.downloadCompleted();
        clearNotification(context);
    }



    public void generateNotifications(Context context, String message,
                                             boolean cancelable) {
         notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String title = context.getResources().getString(R.string.app_name);
//        Intent notificationIntent = new Intent(context, class1);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
        String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";
        Notification myNotication;
        Notification.Builder builder = null;

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_HIGH);
            // Configure the notification channel.
            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationManager.createNotificationChannel(notificationChannel);

            builder = new Notification.Builder(context, NOTIFICATION_CHANNEL_ID)
                    .setContentText(message)
                    .setContentTitle(context.getResources().getString(R.string.app_name))
                    .setOngoing(true)
                    .setSmallIcon(getNotificationIcon())
                    .setAutoCancel(cancelable)
                    .setLargeIcon(((BitmapDrawable) ContextCompat.getDrawable(context, R.drawable.launcher_icon)).getBitmap())
                    .setStyle(new Notification.BigTextStyle()
                            .bigText(message))
                    .setWhen(System.currentTimeMillis());
        } else {
            builder = new Notification.Builder(context)
                    .setAutoCancel(true)
                    .setTicker(context.getResources().getString(R.string.app_name))
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(cancelable)
                    .setOngoing(true)
                    .setSmallIcon(getNotificationIcon())
                    .setStyle(new Notification.BigTextStyle()
                            .bigText(message))
                    .setLargeIcon(((BitmapDrawable) context.getResources().getDrawable(R.drawable.launcher_icon)).getBitmap());

        }

        myNotication = builder.build();

        myNotication.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(Notification_ID, myNotication);
        Uri notification1 = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        try {
            Ringtone r = RingtoneManager.getRingtone(context, notification1);
            r.play();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    private int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.drawable.launcher_icon : R.drawable.launcher_icon;
    }

    private void clearNotification(Context context){
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(Notification_ID);
    }


}
