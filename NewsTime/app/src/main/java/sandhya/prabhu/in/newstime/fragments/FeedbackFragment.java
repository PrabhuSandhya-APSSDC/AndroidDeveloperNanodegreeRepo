package sandhya.prabhu.in.newstime.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import sandhya.prabhu.in.newstime.R;
import sandhya.prabhu.in.newstime.activities.MainActivity;
import sandhya.prabhu.in.newstime.model.Feedback;


public class FeedbackFragment extends Fragment {

    @BindView(R.id.userFeedback)
    EditText feedback;

    @BindView(R.id.save)
    Button button;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private String username;

    public FeedbackFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_feedback, container, false);
        ButterKnife.bind(this, v);
        getActivity().setTitle(getString(R.string.feedback_frag));
        if (getArguments().containsKey(MainActivity.USER_NAME)) {
            username = getArguments().getString(MainActivity.USER_NAME);
        }
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference(getString(R.string.user_feed));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userFeed = feedback.getText().toString();
                if (userFeed.equalsIgnoreCase("")) {
                    feedback.setError(getString(R.string.please_enter_feed));
                }
                if (username.equals("")) {
                    username = getString(R.string.anonymous_user);
                }
                Feedback feedback = new Feedback(userFeed);
                myRef.child(username).setValue(feedback);
                Toast.makeText(getActivity(), R.string.feed_saved, Toast.LENGTH_SHORT).show();
            }
        });
        return v;
    }

}
