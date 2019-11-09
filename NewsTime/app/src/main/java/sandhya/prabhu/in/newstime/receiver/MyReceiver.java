package sandhya.prabhu.in.newstime.receiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import sandhya.prabhu.in.newstime.R;
import sandhya.prabhu.in.newstime.activities.CountrySelectionActivity;
import sandhya.prabhu.in.newstime.activities.MainActivity;

public class MyReceiver extends BroadcastReceiver {

    private NotificationManager nm;
    private NotificationCompat.Builder builder;
    private int NOTIFICATION_ID = 12;
    private PendingIntent pi;
    public static final String ACTION_NOTIFY = "sandhya.prabhu.in.newstime.ACTION_NOTIFY";

    @Override
    public void onReceive(Context context, Intent intent) {
        String code = intent.getExtras().getString(CountrySelectionActivity.COUNTRY_CODE, "");
        nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle(context.getString(R.string.reminder_title));
        builder.setContentText(context.getString(R.string.time_to_read));
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        Intent intent1 = new Intent(context, MainActivity.class);
        pi = PendingIntent.getActivity(context,
                12,
                intent1,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        builder.setContentIntent(pi);
        nm.notify(NOTIFICATION_ID, builder.build());
    }
}
