package pasu.nakshatraDesigners.utils;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;
import org.jetbrains.annotations.Nullable;

/**
 * This BroadcastReceiver to update network connections is Available/Not.
 */
public class NetworkStatus extends BroadcastReceiver {
    public Context mContext;
    public static Context appContext;
    private String message;
    private Dialog sDialog;
    public static Dialog errorDialog;
    private final int REQUEST_READ_PHONE_STATE = 292;
    public static final String LOADING="loading";
    public static final String LOADED="loading";

    public enum Status {
        RUNNING,
        SUCCESS,
        FAILED,
        MAX
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        try {
            if (!isOnline(mContext)) {
                Toast.makeText(mContext, "No Internet", Toast.LENGTH_LONG).show();
            }
//			else if (CommonData.sContext != null )
//				new URLReachable(mContext).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static boolean isOnline(Context mContext2) {
        ConnectivityManager connectivity = (ConnectivityManager) mContext2.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }
        return false;
    }

    public boolean getConnectivityStatus(Context context) {
        boolean conn = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                if (activeNetwork.isConnected()) {
//					if(context!=null)
//						new URLReachable(context).execute();

                    conn = true;
                } else {
                    conn = false;
                }
            }
        } else {
            conn = false;
        }
        return conn;
    }
}