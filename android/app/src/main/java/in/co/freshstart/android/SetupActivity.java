package in.co.freshstart.android;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetupActivity extends AppCompatActivity {

    private EditText UserName, FullName, Country;
    private Button SaveInformationButton;
    private CircleImageView ProfileImage;
    private ProgressDialog LoadingBar;

    private FirebaseAuth mAuth;
    private DatabaseReference UserRef;
    private StorageReference UserProfileImageRef;

    String currentUserID;
    final static int Gallery_Pick = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);

        UserName = (EditText) findViewById(R.id.setup_username);
        FullName = (EditText) findViewById(R.id.setup_fullname);
        Country = (EditText) findViewById(R.id.setup_country);
        SaveInformationButton = (Button) findViewById(R.id.setup_save_button);
        ProfileImage = (CircleImageView) findViewById(R.id.setup_profile_image);
        LoadingBar = new ProgressDialog(this);
        UserProfileImageRef = FirebaseStorage.getInstance().getReference().child("profile Images");

        SaveInformationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveAccountSetupInformation();
            }
        });

        ProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, Gallery_Pick);
            }
        });

        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){

                    String image = snapshot.child("profileimage").getValue().toString();

                    Picasso.with(SetupActivity.this).load(image).placeholder(R.drawable.profile).into(ProfileImage);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Gallery_Pick && resultCode ==RESULT_OK && data != null) {
            Uri ImageUri = data.getData();

            CropImage.activity(ImageUri)
                    .setAspectRatio(1, 1)
                    .start(this);
        }

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if(resultCode == RESULT_OK){

                    LoadingBar.setTitle("Profile Image");
                    LoadingBar.setMessage("Please wait while we are updating your profile image.....");
                    LoadingBar.show();
                    LoadingBar.setCanceledOnTouchOutside(true);

                    assert result != null;
                    final Uri resultUri = result.getUri();

                    final StorageReference filePath = UserProfileImageRef.child(currentUserID + ".jpg");
//                    filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//                            if(task.isSuccessful()){
//                                Toast.makeText(SetupActivity.this, "Profile image stored successfully", Toast.LENGTH_SHORT).show();
//                                LoadingBar.dismiss();
//                                final String downloadUrl = task.getResult().getStorage().getDownloadUrl().toString();
//                                UserRef.child("profileimage").setValue(downloadUrl)
//                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                            @Override
//                                            public void onComplete(@NonNull Task<Void> task) {
//                                                if(task.isSuccessful()){
//
//                                                    Intent setupIntent = new Intent(SetupActivity.this, SetupActivity.class);
//
//                                                    Toast.makeText(SetupActivity.this, "Profile image download url set in database", Toast.LENGTH_SHORT).show();
//                                                    LoadingBar.dismiss();
//                                                }
//                                                else{
//                                                    String message = task.getException().toString();
//                                                    Toast.makeText(SetupActivity.this, "Error occured" + message, Toast.LENGTH_SHORT).show();
//                                                    LoadingBar.dismiss();
//                                                }
//
//                                            }
//                                        });
//
//                            }
//                        }
//                    });
                    filePath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(SetupActivity.this, "Profile image stored successfully", Toast.LENGTH_SHORT).show();
                            LoadingBar.dismiss();
                            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String downloadUrl = uri.toString();

                                    UserRef.child("profileimage").setValue(downloadUrl)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){

                                                        Intent setupIntent = new Intent(SetupActivity.this, SetupActivity.class);

                                                        Toast.makeText(SetupActivity.this, "Profile image download url set in database", Toast.LENGTH_SHORT).show();
                                                        LoadingBar.dismiss();
                                                    }
                                                    else{
                                                        String message = task.getException().toString();
                                                        Toast.makeText(SetupActivity.this, "Error occured" + message, Toast.LENGTH_SHORT).show();
                                                        LoadingBar.dismiss();
                                                    }

                                             }
                                    });
                                }
                            });

                        }
                    });
                }
                else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                    Toast.makeText(this, "Error occured:" + error, Toast.LENGTH_SHORT).show();
                    LoadingBar.dismiss();
                }
        }
    }

    private void SaveAccountSetupInformation() {

        String username =UserName.getText().toString();
        String fullname = FullName.getText().toString();
        String country =Country.getText().toString();

        if(TextUtils.isEmpty(username)){
            Toast.makeText(this, "Please enter your User Name", Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(fullname)){
            Toast.makeText(this, "Please enter your Full Name", Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(country)){
            Toast.makeText(this, "Please enter your country", Toast.LENGTH_SHORT).show();
        }
        else{

            LoadingBar.setTitle("Saving Information");
            LoadingBar.setMessage("Please wait while we are creating your new account.....");
            LoadingBar.show();
            LoadingBar.setCanceledOnTouchOutside(true);

            HashMap UserMap = new HashMap();
            UserMap.put("username", username);
            UserMap.put("fullname", fullname);
            UserMap.put("country", country);
            UserMap.put("status", "Hey there I'm using Fresh Start");
            UserMap.put("gender", "none");
            UserMap.put("dob", "none");
            UserMap.put("relationshipstatus", "none");
            UserRef.updateChildren(UserMap)
                    .addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                           if(task.isSuccessful()){
                               
                               SendUserToMainActivity();
                               Toast.makeText(SetupActivity.this, "Your account is created successfully", Toast.LENGTH_LONG).show();
                               LoadingBar.dismiss();
                           }
                           else{
                               String message = task.getException().getMessage();
                               Toast.makeText(SetupActivity.this, "Error Occured" + message, Toast.LENGTH_SHORT).show();
                               LoadingBar.dismiss();
                           }
                        }
                    });

        }
    }

    private void SendUserToMainActivity() {

        Intent mainIntent = new Intent(this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }
}