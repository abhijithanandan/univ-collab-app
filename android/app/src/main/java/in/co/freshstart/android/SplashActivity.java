package in.co.freshstart.android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.transition.Scene;
import androidx.transition.Transition;
import androidx.transition.TransitionInflater;
import androidx.transition.TransitionManager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import static java.lang.Thread.sleep;

public class SplashActivity extends AppCompatActivity {

    private ViewGroup sceneRoot;
    private static final String TAG = "FreshStart";
    private static int SPLASH_SCREEN_TIMEOUT=2000;
    private static final int SECOND_SPLASH_SCREEN_TIMEOUT = 4000;
    private ProgressBar bar;
    private ImageButton imageButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startAnimation();
            }
        }, SPLASH_SCREEN_TIMEOUT);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                sendToWelcomeActivity();
            }
        }, SECOND_SPLASH_SCREEN_TIMEOUT);


        Log.i(TAG, "Executing onCreate form Splash Activity");

        }

    private void sendToWelcomeActivity() {

//        Fade fade = new Fade();
//        View decor = getWindow().getDecorView();
//        fade.excludeTarget(findViewById(R.id.welcome_progess_bar), true);

//        getWindow().setEnterTransition(new Explode());
//        getWindow().setExitTransition(new Explode());

        Intent i = new Intent(SplashActivity.this,
                FreshWelcomeActivity.class);
        imageButton = (ImageButton) findViewById(R.id.welcome_2_imageButton);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(SplashActivity.this, imageButton, ViewCompat.getTransitionName(imageButton));
        startActivity(i, options.toBundle());
    }

    private void startAnimation() {
        Log.i(TAG, "Starting animation in Splash Activity");
        Scene scene;
        sceneRoot = (ViewGroup) findViewById(R.id.splash_root_layout);
        scene = Scene.getSceneForLayout(sceneRoot, R.layout.splash_page_2, this);

        Transition transition = TransitionInflater.from(this)
                .inflateTransition(R.transition.changebound_transition);

        TransitionManager.go(scene, transition);
    }
}