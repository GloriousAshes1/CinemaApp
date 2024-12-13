package com.example.finalprojectmobileapplication.activity;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finalprojectmobileapplication.MyApplication;
import com.example.finalprojectmobileapplication.R;
import com.example.finalprojectmobileapplication.constant.ConstantKey;
import com.example.finalprojectmobileapplication.constant.GlobalFunction;
import com.example.finalprojectmobileapplication.databinding.ActivityMovieDetailBinding;
import com.example.finalprojectmobileapplication.model.Movie;
import com.example.finalprojectmobileapplication.util.DateTimeUtils;
import com.example.finalprojectmobileapplication.util.GlideUtils;
import com.example.finalprojectmobileapplication.util.StringUtil;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class MovieDetailActivity extends AppCompatActivity {

    private ActivityMovieDetailBinding mActivityMovieDetailBinding;
    private Movie mMovie;

    private SimpleExoPlayer mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityMovieDetailBinding = ActivityMovieDetailBinding.inflate(getLayoutInflater());
        setContentView(mActivityMovieDetailBinding.getRoot());

        getDataIntent();
    }

    private void getDataIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }
        Movie movie = (Movie) bundle.get(ConstantKey.KEY_INTENT_MOVIE_OBJECT);
        getMovieInformation(movie.getId());
    }

    private void getMovieInformation(long movieId) {
        MyApplication.get(this).getMovieDatabaseReference().child(String.valueOf(movieId))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        mMovie = snapshot.getValue(Movie.class);

                        displayDataMovie();
                        initListener();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void displayDataMovie() {
        if (mMovie == null) {
            return;
        }
        GlideUtils.loadUrl(mMovie.getImage(), mActivityMovieDetailBinding.imgMovie);
        mActivityMovieDetailBinding.tvTitleMovie.setText(mMovie.getName());
        mActivityMovieDetailBinding.tvCategoryName.setText(mMovie.getCategoryName());
        mActivityMovieDetailBinding.tvDateMovie.setText(mMovie.getDate());
        String strPrice = mMovie.getPrice() + ConstantKey.UNIT_CURRENCY_MOVIE;
        mActivityMovieDetailBinding.tvPriceMovie.setText(strPrice);
        mActivityMovieDetailBinding.tvDescriptionMovie.setText(mMovie.getDescription());

        if (!StringUtil.isEmpty(mMovie.getUrl())) {
            Log.e("Movie Url", mMovie.getUrl());
            initExoPlayer();
        }
    }

    private void initListener() {
        mActivityMovieDetailBinding.imgBack.setOnClickListener(view -> onBackPressed());
        mActivityMovieDetailBinding.btnWatchTrailer.setOnClickListener(view -> scrollToLayoutTrailer());
        mActivityMovieDetailBinding.imgPlayMovie.setOnClickListener(view -> startVideo());
        mActivityMovieDetailBinding.btnBooking.setOnClickListener(view -> onClickGoToConfirmBooking());
    }

    private void onClickGoToConfirmBooking() {
        if (mMovie == null) {
            return;
        }
        if (DateTimeUtils.convertDateToTimeStamp(mMovie.getDate()) < DateTimeUtils.getLongCurrentTimeStamp()) {
            Toast.makeText(this, getString(R.string.msg_movie_date_invalid), Toast.LENGTH_SHORT).show();
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putSerializable(ConstantKey.KEY_INTENT_MOVIE_OBJECT, mMovie);
//        GlobalFunction.startActivity(this, ConfirmBookingActivity.class, bundle);
    }

    private void scrollToLayoutTrailer() {
        long duration = 500;
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            float y = mActivityMovieDetailBinding.labelMovieTrailer.getY();
            ScrollView sv = mActivityMovieDetailBinding.scrollView;
            ObjectAnimator objectAnimator = ObjectAnimator.ofInt(sv, "scrollY", 0, (int) y);
            objectAnimator.start();

            startVideo();
        }, duration);
    }

    private void initExoPlayer() {
        PlayerView mExoPlayerView = mActivityMovieDetailBinding.exoplayer;

        if (mPlayer != null) {
            return;
        }
        mPlayer = new SimpleExoPlayer.Builder(this).build();
        // Set player
        mExoPlayerView.setPlayer(mPlayer);
        mExoPlayerView.hideController();
    }

    private void startVideo() {
        mActivityMovieDetailBinding.imgPlayMovie.setVisibility(View.GONE);
        if (mPlayer != null) {
            MediaItem mediaItem = MediaItem.fromUri(mMovie.getUrl());
            mPlayer.addMediaItem(mediaItem);
            // Prepare video source
            mPlayer.prepare();
            // Set play video
            mPlayer.setPlayWhenReady(true);
        }
    }
}