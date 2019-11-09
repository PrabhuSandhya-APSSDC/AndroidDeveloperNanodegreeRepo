package sandhya.prabhu.in.ndbakingapp.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import sandhya.prabhu.in.ndbakingapp.R;
import sandhya.prabhu.in.ndbakingapp.activities.RecipeListActivity;
import sandhya.prabhu.in.ndbakingapp.model.Steps;

public class RecipeDetailFragment extends Fragment {

    private List<Steps> stepsList;
    private int step_position;

    @BindView(R.id.desc_text)
    TextView stepDesc;

    @BindView(R.id.simpleExoPlayerView)
    SimpleExoPlayerView simpleExoPlayerView;

    @BindView(R.id.prev)
    ImageView prev_view;

    @BindView(R.id.next)
    ImageView next_view;

    @BindView(R.id.thumbnailView)
    ImageView imageView;


    private String videoUrl;
    private String thumbnailUrl;
    private int stepListLength;
    private boolean isPlayerStopped;
    private long playerStopPosition;

    @BindView(R.id.frameLayout)
    FrameLayout frameLayout;
    private View rootView;
    private SimpleExoPlayer exoPlayer;
    private boolean playWhenReady;
    private long current_video_position;
    private static final String TAG = "recipe_steps";
    private static final String CURRENT_VIDEO_POSITION = "currentVideoPosition";
    private static final String PLAY_BACK = "playBack";
    private static final String CURRENT_STEP_POS = "current_step_pos";

    public RecipeDetailFragment() {
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(RecipeListActivity.RECIPE_STEPS)) {
            stepsList = (List<Steps>) getArguments().getSerializable(RecipeListActivity.RECIPE_STEPS);
            step_position = getArguments().getInt(RecipeListActivity.STEP_POSITION, 0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.recipe_step_detail, container, false);
        ButterKnife.bind(this, rootView);
        if (savedInstanceState != null) {
            step_position = savedInstanceState.getInt(RecipeDetailFragment.CURRENT_STEP_POS, 0);
            current_video_position = savedInstanceState.getLong(RecipeDetailFragment.CURRENT_VIDEO_POSITION, 1);
            playWhenReady = savedInstanceState.getBoolean(RecipeDetailFragment.PLAY_BACK, false);
        }
        stepListLength = stepsList.size();
        videoUrl = stepsList.get(step_position).getVideoURL();
        thumbnailUrl = stepsList.get(step_position).getThumbnailURL();
        stepDesc.setText(stepsList.get(step_position).getDescription());
        showExoplayer();
        setStepNumber(step_position);
        prev_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                releasePlayer();
                step_position--;
                videoUrl = stepsList.get(step_position).getVideoURL();
                stepDesc.setText(stepsList.get(step_position).getDescription());
                setStepNumber(step_position);
                showExoplayer();
                initializePlayer();
                exoPlayer.seekTo(0);
            }
        });
        next_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                releasePlayer();
                step_position++;
                videoUrl = stepsList.get(step_position).getVideoURL();
                stepDesc.setText(stepsList.get(step_position).getDescription());
                setStepNumber(step_position);
                showExoplayer();
                initializePlayer();
                exoPlayer.seekTo(0);
            }
        });
        return rootView;
    }

    private void setStepNumber(int step_position) {
        if (step_position == 0) {
            prev_view.setVisibility(View.INVISIBLE);
        } else if (step_position == stepListLength - 1) {
            next_view.setVisibility(View.INVISIBLE);
        } else {
            prev_view.setVisibility(View.VISIBLE);
            next_view.setVisibility(View.VISIBLE);
        }
    }


    private void showExoplayer() {
        if (!videoUrl.equals("")) {
            frameLayout.setVisibility(View.VISIBLE);
            simpleExoPlayerView.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.GONE);
        } else {
            simpleExoPlayerView.setVisibility(View.GONE);
            if (!thumbnailUrl.equals("")) {
                imageView.setVisibility(View.VISIBLE);
                Picasso.with(getActivity()).load(thumbnailUrl).into(imageView);
            } else {
                frameLayout.setVisibility(View.GONE);
            }
        }
    }

    private void initializePlayer() {
        if (exoPlayer == null) {
            TrackSelector selector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            //noinspection deprecation
            exoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), selector, loadControl);
            simpleExoPlayerView.setPlayer(exoPlayer);
            MediaSource mediaSource = buildMediaSource(Uri.parse(videoUrl));
            exoPlayer.prepare(mediaSource);
            playWhenReady = true;
            exoPlayer.setPlayWhenReady(playWhenReady);
            if (current_video_position != 0 && !isPlayerStopped) {
                exoPlayer.seekTo(current_video_position);
            } else {
                exoPlayer.seekTo(playerStopPosition);
            }
        }
    }

    private MediaSource buildMediaSource(Uri parse) {
        return new ExtractorMediaSource.Factory(new DefaultHttpDataSourceFactory(TAG)).createMediaSource(parse);
    }

    private void releasePlayer() {
        if (exoPlayer != null) {
            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer = null;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (exoPlayer != null) {
            playerStopPosition = exoPlayer.getCurrentPosition();
            isPlayerStopped = true;
            releasePlayer();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (exoPlayer != null) {
            outState.putLong(RecipeDetailFragment.CURRENT_VIDEO_POSITION, exoPlayer.getCurrentPosition());
            outState.putBoolean(RecipeDetailFragment.PLAY_BACK, exoPlayer.getPlayWhenReady());
            outState.putInt(RecipeDetailFragment.CURRENT_STEP_POS, step_position);
        }
    }
}
