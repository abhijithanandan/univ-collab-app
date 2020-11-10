package in.co.freshstart.android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.transition.Explode;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class FreshWelcomeActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "FreshStart";
    private GestureDetector gestureDetector;
    View.OnTouchListener gestureListener;
    Button swipeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fresh_welcome);

//        To show in full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        swipeButton = findViewById(R.id.welcome_3_button);
        gestureDetector = new GestureDetector(new SwipeGestureDectector());
        gestureListener = new View.OnTouchListener(){
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return gestureDetector.onTouchEvent(motionEvent);
            }
        };

        swipeButton.setOnTouchListener(gestureListener);


//        getWindow().setEnterTransition(new Explode());
//        getWindow().setExitTransition(new Explode());

        Log.i(TAG, "Executing onCreate form FreshWelcome Activity");
    }

    @Override
    public void onClick(View view) {

    }

    private class SwipeGestureDectector extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_MIN_DISTANCE = 50;
        private static final int SWIPE_MAX_OFF_PATH = 200;
        private static final int SWIPE_THRESHOLD_VELOCITY = 200;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            try {
                float diffAbs = Math.abs(e1.getY() - e2.getY());
                float diff = e1.getX() - e2.getX();

                if (diffAbs > SWIPE_MAX_OFF_PATH)
                    return false;

                // Left swipe
                if (diff > SWIPE_MIN_DISTANCE
                        && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    FreshWelcomeActivity.this.onLeftSwipe();
                }
                // Right swipe
                else if (-diff > SWIPE_MIN_DISTANCE
                        && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    FreshWelcomeActivity.this.onRightSwipe();
                }
            } catch (Exception e) {
                Log.e("Home", "Error on gestures");
            }
            return false;
        }
    }

    private void onLeftSwipe() {
        Intent intent = new Intent(FreshWelcomeActivity.this, FreshLoginActivity.class);
        startActivity(intent);
    }

    private void onRightSwipe() {
        Toast t = Toast.makeText(FreshWelcomeActivity.this, "Please swipe in opposite direction!!!", Toast.LENGTH_LONG);
        t.show();
    }
}


