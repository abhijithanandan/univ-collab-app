package in.co.freshstart.android;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class PostActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private ImageButton SelectPostImage;
    private EditText PostDescription;
    private Button UpdatePostButton;
    private static final int Gallery_Pick = 1;
    private Uri ImageUri;
    private String Description;
    private StorageReference PostImageReference;
    private DatabaseReference UserRef, PostsRef;
    private FirebaseAuth mAuth;
    private String saveCurrentDate, saveCurrentTime, postRandomName, downloadUrl, current_user_id;
    private ProgressDialog LoadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mAuth = FirebaseAuth.getInstance();
        current_user_id = mAuth.getCurrentUser().getUid();
        PostImageReference = FirebaseStorage.getInstance().getReference();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        PostsRef = FirebaseDatabase.getInstance().getReference().child("Posts");

        mToolbar = (Toolbar) findViewById(R.id.update_post_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Update Post");

        SelectPostImage = (ImageButton) findViewById(R.id.select_post_image);
        PostDescription = (EditText) findViewById(R.id.post_description);
        UpdatePostButton = (Button) findViewById(R.id.update_post_button);
        LoadingBar = (ProgressDialog)  new ProgressDialog(this);

        SelectPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                OpenGallery();
            }
        });

        UpdatePostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ValidatePostInfo();
            }
        });

    }

    private void ValidatePostInfo() {

        Description = PostDescription.getText().toString();
        if(ImageUri == null){

            Toast.makeText(this, "Please select a post image", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Description)){

            Toast.makeText(this, "Please say something about your post", Toast.LENGTH_SHORT).show();
        }
        else{
            LoadingBar.setTitle("Add new Post");
            LoadingBar.setMessage("Please wait while we are updating your new post.....");
            LoadingBar.show();
            LoadingBar.setCanceledOnTouchOutside(true);
           StoringImageToStorage();
        }
    }

    private void StoringImageToStorage() {

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMMM-yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        saveCurrentTime = currentTime.format(calForTime.getTime());

        postRandomName = saveCurrentDate+saveCurrentTime;

        StorageReference filePath = PostImageReference.child("Post Images").child(ImageUri.getLastPathSegment() + postRandomName + ".jpg");
        filePath.putFile(ImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){

                    downloadUrl = task.getResult().getMetadata().getReference().getDownloadUrl().toString();
                    Toast.makeText(PostActivity.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                    SavingPostInformationToDatabase();
                }
                else {
                    String message = task.getException().getMessage();
                    Toast.makeText(PostActivity.this, "Error occured" + message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void SavingPostInformationToDatabase() {

        UserRef.child(current_user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

               if(snapshot.exists()) {
                   String userFullName = snapshot.child("fullname").getValue().toString();
                   String userProfileImage = snapshot.child("profileimage").getValue().toString();

                   HashMap postsMap = new HashMap();
                       postsMap.put("uid", current_user_id);
                       postsMap.put("date", saveCurrentDate);
                       postsMap.put("time", saveCurrentTime);
                       postsMap.put("description", Description);
                       postsMap.put("postimage", downloadUrl);
                       postsMap.put("profileimage", userProfileImage);
                       postsMap.put("username", userFullName);

                   PostsRef.child(current_user_id + postRandomName).updateChildren(postsMap)
                           .addOnCompleteListener(new OnCompleteListener() {
                               @Override
                               public void onComplete(@NonNull Task task) {

                                   if(task.isSuccessful()){
                                       Toast.makeText(PostActivity.this, "Post is updated successfully", Toast.LENGTH_SHORT).show();
                                       LoadingBar.dismiss();
                                   } else {

                                       String message = task.getException().getMessage();
                                       Toast.makeText(PostActivity.this, "Error occured" + message, Toast.LENGTH_SHORT).show();
                                       LoadingBar.dismiss();
                                   }

                               }
                           });


               }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void OpenGallery() {

        Intent GalleryIntent = new Intent();
        GalleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        GalleryIntent.setType("image/*");
        startActivityForResult(GalleryIntent, Gallery_Pick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Gallery_Pick && resultCode == RESULT_OK && data != null) {

           ImageUri =   data.getData();
           SelectPostImage.setImageURI(ImageUri);

        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home){
            
            SendUserToMainActivity();
        }

        return super.onOptionsItemSelected(item);
    }

    private void SendUserToMainActivity() {

        Intent MainIntent = new Intent(PostActivity.this, MainActivity.class);
        startActivity(MainIntent);
    }
}