package in.co.freshstart.android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.circularreveal.cardview.CircularRevealCardView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private RecyclerView PostList;
    private Toolbar mToolbar;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private FirebaseAuth mAuth;
    private DatabaseReference UserRef, PostsRef;
    private CircleImageView navProfileImage;
    private TextView navProfileName;
    String CurrentUserID;
    private FirebaseUser user;
    private ImageButton addNewPostButton;
    private static final String TAG = "FreshStart";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i(TAG, "Executing onCreate");

        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Home");

        drawerLayout = (DrawerLayout) findViewById(R.id.drawyer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        actionBarDrawerToggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        View navView = navigationView.inflateHeaderView(R.layout.navigation_header);
        navProfileImage = (CircleImageView) navView.findViewById(R.id.nav_profile_image);
        navProfileName = navView.findViewById(R.id.nav_user_full_name);
        addNewPostButton = (ImageButton) findViewById(R.id.add_new_post_button);

        PostList = (RecyclerView) findViewById(R.id.all_users_post_list);
        PostList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        PostList.setLayoutManager(linearLayoutManager);

        if (user != null){

            CurrentUserID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
            UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
            PostsRef = FirebaseDatabase.getInstance().getReference().child("Posts");

            UserRef.child(CurrentUserID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if(snapshot.hasChild("fullname")){

                        String fullName = snapshot.child("fullname").getValue().toString();
                        navProfileName.setText(fullName);
                    }
                    if(snapshot.hasChild("profileimage")) {

                        String image = snapshot.child("profileimage").getValue().toString();
                        Picasso.with(MainActivity.this).load(image).placeholder(R.drawable.profile).into(navProfileImage);
                    }
                    else {
                        Toast.makeText(MainActivity.this, "Profile Image does not exist", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else {

            sendUserToLoginActivity();
        }

        addNewPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendUserToPostActivity();
            }
        });

    }

    private void showAllUsersPosts() {

        FirebaseRecyclerOptions<Posts> options =
                new FirebaseRecyclerOptions.Builder<Posts>()
                .setQuery(PostsRef, Posts.class)
                .build();

        Log.i(TAG, "showAllUserPosts called");
        FirebaseRecyclerAdapter<Posts, PostsViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Posts, PostsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull PostsViewHolder holder, int position, @NonNull Posts model) {

                Log.i(TAG, "Executing onBindViewHolder");

                holder.setUsername(model.getUsername());
                holder.setDate(model.getDate());
                holder.setTime(model.getTime());
                holder.setDescription(model.getDescription());
                holder.setPostimage(getApplicationContext(), model.getPostimage());
                holder.setProfileimage(getApplicationContext(),  model.getProfileimage());

            }

            @NonNull
            @Override
            public PostsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                Log.i(TAG, "Executing onBindViewHolder");
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_posts_layout, parent, false);
                PostsViewHolder viewHolder = new PostsViewHolder(view);
                return viewHolder;

            }
        };
        PostList.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }

    public static class PostsViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public PostsViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            Log.i(TAG, "PostViewHolder constructor called");
        }

        public void setUsername(String username) {

            TextView postUsername = (TextView) mView.findViewById(R.id.timeline_post_profile_name);
            postUsername.setText(username);
        }

        public void setProfileimage(Context ctx, String profileimage) {

            CircleImageView image = (CircleImageView) mView.findViewById(R.id.timeline_post_profile_image);
            Picasso.with(ctx).load(profileimage).into(image);
        }

        public void setTime(String time) {

            TextView post_time = (TextView) mView.findViewById(R.id.timeline_post_time);
            post_time.setText(time);
        }

        public void setDate(String date) {

            TextView post_date = (TextView) mView.findViewById(R.id.timeline_post_date);
            post_date.setText(date);
        }

        public void setDescription(String description) {

            TextView Description = (TextView) mView.findViewById(R.id.timeline_post_description);
            Description.setText(description);
        }

        public void setPostimage(Context ctx, String postimage) {

            ImageView image = (ImageView) mView.findViewById(R.id.timeline_post_image);
            Picasso.with(ctx).load(postimage).into(image);

        }


    }

    private void SendUserToPostActivity() {

        Intent addNewPostIntent = new Intent(MainActivity.this, PostActivity.class);
        startActivity(addNewPostIntent);
    }


    @Override
    protected void onStart() {
        super.onStart();

        Log.i(TAG, "Executing onStart");
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser == null){
            sendUserToLoginActivity();
        }
        else{

            CheckUserExistence();
        }
        showAllUsersPosts();
    }

    private void CheckUserExistence() {

        final String current_user_id = mAuth.getCurrentUser().getUid();
        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

               if(!dataSnapshot.hasChild(current_user_id)){

                   SentUserToSetupActivity();
               }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void SentUserToSetupActivity() {


        Intent setupIntent = new Intent(MainActivity.this, SetupActivity.class);
        setupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(setupIntent);
        finish();
    }

    private void sendUserToLoginActivity() {

        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

       if(actionBarDrawerToggle.onOptionsItemSelected(item)){
           return true;
       }
        return super.onOptionsItemSelected(item);
    }

    private void UserMenuSelector(MenuItem item) {

        switch (item.getItemId()){

            case R.id.nav_post:
                SendUserToPostActivity();
                break;

            case R.id.nav_profile:
                Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_home:
                Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_friends:
                Toast.makeText(this, "Friends", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_find_friends:
                Toast.makeText(this, "Find friends", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_messages:
                Toast.makeText(this, "Messages", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_settings:
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_Logout:
                mAuth.signOut();
                sendUserToLoginActivity();
                break;

        }
    }
}