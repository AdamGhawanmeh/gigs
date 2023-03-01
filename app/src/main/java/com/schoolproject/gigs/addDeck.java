package com.schoolproject.gigs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class addDeck extends AppCompatActivity implements View.OnClickListener{

    private ImageView left,right,top,bottom;
    private TextView Create,Cancel;
    private EditText Title;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String userID = mAuth.getCurrentUser().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_deck);

        left=findViewById(R.id.left);
        right=findViewById(R.id.right);
        top=findViewById(R.id.top);
        bottom=findViewById(R.id.bottom);
        left.setOnClickListener(this);
        right.setOnClickListener(this);
        top.setOnClickListener(this);
        bottom.setOnClickListener(this);

        Create = findViewById(R.id.Create);
        Create.setOnClickListener(this);
        Cancel = findViewById(R.id.Cancel);
        Cancel.setOnClickListener(this);

        Title = findViewById(R.id.Title);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.Create:


                SharedPreferences sharedPreferences = getSharedPreferences("Title", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Title", Title.getText().toString());
                editor.apply();
                Intent intent = new Intent(this,newFlashCard.class);
                finish();
                startActivity(intent);
                break;
            default:
                finish();
                break;
        }
    }
}