package com.schoolproject.gigs;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class SignUpPage extends AppCompatActivity implements View.OnClickListener {
    private Button SignUpButton;
    private EditText email,password,confirmPassword,username;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);
        SignUpButton =findViewById(R.id.SignUp);
        username= findViewById(R.id.username);
        email= findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirm_password);
        SignUpButton.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                auth = FirebaseAuth.getInstance();
                String usernameTxt = username.getText().toString();
                String emailTxt=email.getText().toString();
                String passwordTxt=password.getText().toString();
                String confPassTxt=confirmPassword.getText().toString();
                    if(TextUtils.isEmpty(emailTxt)||TextUtils.isEmpty(passwordTxt)||TextUtils.isEmpty(confPassTxt)){
                        Toast.makeText(SignUpPage.this, "Please fill in all credentials", Toast.LENGTH_SHORT).show();
                    }
                    else if (passwordTxt.length()<8){
                        Toast.makeText(SignUpPage.this, "Password must be at least 8 characters long", Toast.LENGTH_SHORT).show();
                    }
                    
                    else if (!(passwordTxt.equals(confPassTxt))){
                        Toast.makeText(SignUpPage.this, "Password and password confirmation do not match", Toast.LENGTH_SHORT).show();
                    }

                    else{
                        registerUser(emailTxt,passwordTxt, usernameTxt);
                    }
            }
        });
    }

    private void registerUser(String email, String password, String DisplayName) {

        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(SignUpPage.this , new OnCompleteListener<AuthResult>() {



            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
if (task.isSuccessful()){

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Map<String, Object> userHM = new HashMap<>();
    userHM.put("name", "@"+DisplayName);
    userHM.put("email", email);

    db.collection("users").document(auth.getCurrentUser().getUid().toString()).set(userHM, SetOptions.merge())
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d("worked", "DocumentSnapshot successfully written!");
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w("didn't work", "Error writing document", e);
                }
            });
    Toast.makeText(SignUpPage.this, "Signed up successfully", Toast.LENGTH_SHORT).show();
    Intent intent = new Intent(SignUpPage.this,signup2.class);
    intent.putExtra("username","@"+DisplayName);
    startActivity(intent);
    finish();
}else{
    try {
        throw task.getException();
    }
    catch(FirebaseAuthUserCollisionException e) {
        // email already taken
        Toast.makeText(getApplicationContext(), "Email already taken!", Toast.LENGTH_SHORT).show();
    } catch (Exception e) {
        Toast.makeText(SignUpPage.this, "Sign Up Failed", Toast.LENGTH_SHORT).show();
    }
}
            }
        });



}

    @Override
    public void onClick(View view) {

    }
}