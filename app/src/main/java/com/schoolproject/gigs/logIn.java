package com.schoolproject.gigs;

import static com.schoolproject.gigs.R.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class logIn extends AppCompatActivity implements View.OnClickListener{


    private static final int RC_SIGN_IN = 123;
    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;

    private GoogleSignInAccount account;
    private TextView SignUpRedirect;
    private EditText email,password;
    private Button login;
    private SignInClient oneTapClient;
    private BeginSignInRequest signInRequest;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private Task<Void> mDatabase;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user!=null){
            startActivity(new Intent(this,homePage.class));

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_log_in);
        createRequest();

        mAuth= FirebaseAuth.getInstance();
        email = findViewById(id.email);
        password = findViewById(id.password);
        login = findViewById(R.id.login);

        login.setOnClickListener(this);

    }

    private void createRequest() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case id.signUpRedirect:
                Intent SignUpIntent = new Intent(logIn.this, SignUpPage.class);
                startActivity(SignUpIntent);
                finish();
                break;
            case id.login:
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();

                if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
                    Toast.makeText(logIn.this, "Empty Credentials!", Toast.LENGTH_SHORT).show();
                } else {
                    loginUser(txt_email , txt_password);
                }
        }
    }

    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(logIn.this, "Logged in", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(logIn.this,homePage.class));
            finish();}
                else {
                    Toast.makeText(logIn.this, "Incorrect email or password",Toast.LENGTH_SHORT).show();
                }


            }
        });
    }
}