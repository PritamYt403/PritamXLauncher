package git.artdeell.mojo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.media.MediaPlayer;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;

public class IntroActivity extends Activity {

    private MediaPlayer mediaPlayer;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private boolean opened = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setStatusBarColor(Color.rgb(35, 8, 8));
        getWindow().setNavigationBarColor(Color.rgb(35, 8, 8));

        FrameLayout root = new FrameLayout(this);
        root.setBackgroundColor(Color.rgb(35, 8, 8));

        ImageView logo = new ImageView(this);
        logo.setImageResource(R.drawable.ic_mojo_full);
        logo.setScaleType(ImageView.ScaleType.FIT_CENTER);
        logo.setAlpha(0f);
        logo.setScaleX(0.65f);
        logo.setScaleY(0.65f);

        FrameLayout.LayoutParams logoParams =
                new FrameLayout.LayoutParams(
                        (int) (240 * getResources().getDisplayMetrics().density),
                        (int) (240 * getResources().getDisplayMetrics().density));
        logoParams.gravity = Gravity.CENTER;
        logoParams.bottomMargin =
                (int) (25 * getResources().getDisplayMetrics().density);
        root.addView(logo, logoParams);

        TextView title = new TextView(this);
        title.setText("PRITAMXLAUNCHER");
        title.setTextColor(Color.WHITE);
        title.setTextSize(18);
        title.setGravity(Gravity.CENTER);
        title.setLetterSpacing(0.18f);
        title.setAlpha(0f);

        FrameLayout.LayoutParams titleParams =
                new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT);
        titleParams.gravity = Gravity.CENTER;
        titleParams.topMargin =
                (int) (190 * getResources().getDisplayMetrics().density);
        root.addView(title, titleParams);

        setContentView(root);

        AnimatorSet animation = new AnimatorSet();
        animation.playTogether(
                ObjectAnimator.ofFloat(logo, View.ALPHA, 0f, 1f),
                ObjectAnimator.ofFloat(logo, View.SCALE_X, 0.65f, 1f),
                ObjectAnimator.ofFloat(logo, View.SCALE_Y, 0.65f, 1f),
                ObjectAnimator.ofFloat(title, View.ALPHA, 0f, 1f)
        );
        animation.setDuration(1100);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.start();

        try {
            mediaPlayer = MediaPlayer.create(this, R.raw.intro_sound);
            if (mediaPlayer != null) {
                mediaPlayer.setOnCompletionListener(mp -> {
                    mp.release();
                    if (mediaPlayer == mp) mediaPlayer = null;
                });
                mediaPlayer.start();
            }
        } catch (Exception ignored) {
            // Continue even if the sound cannot play.
        }

        handler.postDelayed(() -> {
            if (isFinishing() || opened) return;
            opened = true;
            startActivity(new Intent(
                    IntroActivity.this, TestStorageActivity.class));
            finish();
        }, 3200);
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);

        if (mediaPlayer != null) {
            try {
                mediaPlayer.release();
            } catch (Exception ignored) {
            }
            mediaPlayer = null;
        }

        super.onDestroy();
    }
}
