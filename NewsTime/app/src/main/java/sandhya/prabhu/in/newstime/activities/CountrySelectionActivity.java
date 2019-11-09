package sandhya.prabhu.in.newstime.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import sandhya.prabhu.in.newstime.R;

public class CountrySelectionActivity extends AppCompatActivity {

    private static final String TAG = CountrySelectionActivity.class.getSimpleName();
    public static final String COUNTRY_CODE = "country_code";

    @BindView(R.id.profileImage)
    ImageView profilePic;

    @BindView(R.id.usernameDisplay)
    TextView usernameDisplay;

    @BindView(R.id.countrySpinner)
    Spinner country_spinner;

    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;

    @BindView(R.id.toggle)
    Switch sw;

    private String username;
    private String photoUri;
    private String country_code = "";
    private AlarmManager alarmManager;
    public static final String ACTION_NOTIFY = "sandhya.prabhu.in.newstime.ACTION_NOTIFY";
    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_selection);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        if (intent.hasExtra(MainActivity.USER_NAME)) {
            username = intent.getExtras().getString(MainActivity.USER_NAME, null);
            photoUri = intent.getExtras().getString(MainActivity.PHOTO_URL, null);
            setData();
        }
        country_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected_item = country_spinner.getSelectedItem().toString();
                switch (selected_item) {
                    case "China":
                        country_code = "ch";
                        break;
                    case "Japan":
                        country_code = "jp";
                        break;
                    case "India":
                        country_code = "in";
                        break;
                    case "United States":
                        country_code = "us";
                        break;
                    default:
                        Log.i(TAG, "Problem with Spinner selection");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        setReminder();
    }

    private void setData() {
        if (!username.equals(null)) {
            usernameDisplay.setText(getString(R.string.hello) + " " + username);
        }
        if (!photoUri.equals(null)) {
            Glide.with(this).load(Uri.parse(photoUri)).placeholder(R.drawable.no_profile_image).into(profilePic);
        }
    }

    private void setReminder() {
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent i = new Intent(ACTION_NOTIFY);
        pendingIntent = PendingIntent.getBroadcast(this, 12, i, PendingIntent.FLAG_UPDATE_CURRENT);
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR, 6);
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.SECOND, 0);
                    Toast.makeText(CountrySelectionActivity.this, R.string.reminder_on, Toast.LENGTH_SHORT).show();
                    long triggertime = calendar.getTimeInMillis();
                    long intervaltime = AlarmManager.INTERVAL_DAY;
                    alarmManager.setRepeating(AlarmManager.RTC, triggertime, intervaltime, pendingIntent);
                } else {
                    alarmManager.cancel(pendingIntent);
                    Toast.makeText(CountrySelectionActivity.this, R.string.reminder_off, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void gotoCategories(View view) {
        if (country_spinner.getSelectedItem().toString().equals(getString(R.string.choose_country))) {
            Snackbar.make(linearLayout, R.string.choose_a_country, Snackbar.LENGTH_SHORT).show();
        } else {
            if (!country_code.equals("")) {
                Toast.makeText(this, country_code, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, NewsCategoriesActivity.class);
                intent.putExtra(COUNTRY_CODE, country_code);
                intent.putExtra(MainActivity.USER_NAME, username);
                startActivity(intent);
            }
        }
    }
}
