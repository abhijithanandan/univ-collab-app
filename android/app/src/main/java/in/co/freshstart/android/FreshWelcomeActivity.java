package in.co.freshstart.android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.transition.Explode;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class FreshWelcomeActivity extends AppCompatActivity {

    private static final String TAG = "FreshStart";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fresh_welcome);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


//        getWindow().setEnterTransition(new Explode());
//        getWindow().setExitTransition(new Explode());

        Log.i(TAG, "Executing onCreate form FreshWelcome Activity");
    }

}


