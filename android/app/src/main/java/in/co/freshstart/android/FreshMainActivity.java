package in.co.freshstart.android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FreshMainActivity extends AppCompatActivity implements IFirebaseLoadDone {

    ViewPager viewPager;

    DatabaseReference posts;
    IFirebaseLoadDone iFirebaseLoadDone;
    private FreshPostAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fresh_main);

        posts = FirebaseDatabase.getInstance().getReference("Posts");
        iFirebaseLoadDone = this;

        loadPost();
    }

    private void loadPost() {
        posts.addListenerForSingleValueEvent(new ValueEventListener() {
            List<Posts> postsList = new ArrayList<>();
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot postSnapShot:snapshot.getChildren())
                    postsList.add(postSnapShot.getValue(Posts.class));
                iFirebaseLoadDone.onFirebaseLoadSuccess(postsList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                iFirebaseLoadDone.onFirebaseLoadFailed(error.getMessage());

            }
        });
    }


    @Override
    public void onFirebaseLoadSuccess(List<Posts> postsList) {

        adapter = new FreshPostAdapter(this, postsList);

        viewPager.setAdapter(adapter);
    }

    @Override
    public void onFirebaseLoadFailed(String message) {

    }
}