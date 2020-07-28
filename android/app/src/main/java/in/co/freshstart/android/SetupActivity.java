package in.co.freshstart.android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetupActivity extends AppCompatActivity {

    private EditText UserName, FullName, Country;
    private Button SaveInformationButton;
    private CircleImageView ProfileImage;
    private ProgressDialog LoadingBar;

    private FirebaseAuth mAuth;
    private DatabaseReference UserRef;

    String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);

        UserName = (EditText) findViewById(R.id.setup_username);
        FullName = (EditText) findViewById(R.id.setup_fullname);
        Country = (EditText) findViewById(R.id.setup_country);
        SaveInformationButton = (Button) findViewById(R.id.setup_save_button);
        ProfileImage = (CircleImageView) findViewById(R.id.setup_profile_image);
        LoadingBar = new ProgressDialog(this);

        SaveInformationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveAccountSetupInformation();
            }
        });
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