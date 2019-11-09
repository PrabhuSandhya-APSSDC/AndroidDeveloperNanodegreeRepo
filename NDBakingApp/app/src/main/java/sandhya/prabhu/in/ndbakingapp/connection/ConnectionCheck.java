package sandhya.prabhu.in.ndbakingapp.connection;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import sandhya.prabhu.in.ndbakingapp.R;


public class ConnectionCheck {

    private static Context c;

    public static boolean checkConnection(Context context) {
        c = context;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            Log.d("CONNECTION INFO", "Successful");
            return true;
        } else {
            showConnectionDisabledAlert();
            return false;
        }
    }

    private static void showConnectionDisabledAlert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(c);
        alertDialogBuilder.setMessage(R.string.internet_enable)
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }
}
