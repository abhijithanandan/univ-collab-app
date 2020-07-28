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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private EditText UserEmail, UserPassword, UserComfirmPassword;
    private Button CreateAccountButton;
    private FirebaseAuth mAuth;
    private ProgressDialog LoadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = (FirebaseAuth) FirebaseAuth.getInstance();

        UserEmail = (EditText) findViewById(R.id.register_email);
        UserPassword = (EditText) findViewById(R.id.register_password);
        UserComfirmPassword = (EditText) findViewById(R.id.register_confirm_password);
        CreateAccountButton = (Button) findViewById(R.id.register_create_account);
        LoadingBar = new ProgressDialog(this);

        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CreateNewAccount();
            }
        });
    }

    private void CreateNewAccount() {

        String email = UserEmail.getText().toString();
        String password = UserPassword.getText().toString();
        String confirm_password = UserComfirmPassword.getText().toString();

        if(TextUtils.isEmpty(email)){

            Toast.makeText(this, "Please provide your email....", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password)){

            Toast.makeText(this, "Please write your password....", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(confirm_password)){
            Toast.makeText(this, "Plase provide the conform password....", Toast.LENGTH_SHORT).show();
        }

        else if(!password.equals(confirm_password)){
            Toast.makeText(this, "Password mismatch!!!", Toast.LENGTH_SHORT).show();
        }
        else{

            LoadingBar.setTitle("Creating new Account");
            LoadingBar.setMessage("Please wait while we are creating your account.....");
            LoadingBar.show();
            LoadingBar.setCanceledOnTouchOutside(true);

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){

                                sendUserToSetupActivity();
                                Toast.makeText(RegisterActivity.this, "You are authenticated successfully....", Toast.LENGTH_SHORT).show();
                                LoadingBar.dismiss();
                            }
                            else {

                                String message = task.getException().getMessage();
                                Toast.makeText(RegisterActivity.this, "Error Occured:" + message, Toast.LENGTH_SHORT).show();
                                LoadingBar.dismiss();
                            }
                        }
                    });
        }
    }

    private void sendUserToSetupActivity() {

        Intent setupIntent = new Intent( this, SetupActivity.class);
        setupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(setupIntent);
        finish();
    }
}