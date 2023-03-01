package com.schoolproject.gigs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class newFlashCard extends AppCompatActivity implements View.OnClickListener {
    private Switch switchPublic;
    private ImageView decks;
    private Button xButton;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private Button addButton;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String userID = mAuth.getCurrentUser().getUid();

    private EditText term;
    private EditText definition;
    private TextView title;

    private String Title;

    private String cardID = UUID.randomUUID().toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_flashcard);
        xButton = findViewById(R.id.xButton);
        xButton.setOnClickListener(this);
        decks=findViewById(R.id.deck_arrow);
        decks.setOnClickListener(this);
        term = findViewById(R.id.Term);
        definition=findViewById(R.id.definition);

        addButton = findViewById(R.id.add_button);
        addButton.setOnClickListener(this);

        title =findViewById(R.id.deck_title);

        SharedPreferences sharedPreferences = getSharedPreferences("Title", MODE_PRIVATE);
        Title = sharedPreferences.getString("Title", "Default Deck");

        title.setText(Title);
        switchPublic = findViewById(R.id.switchPublic);
        switchPublic.setChecked(sharedPreferences.getBoolean("switch",false));

        SharedPreferences.Editor editor = sharedPreferences.edit();
        switchPublic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editor.putBoolean("switch", true);
                    editor.apply();
                }
                else {
                    editor.putBoolean("switch", false);
                    editor.apply();

                }
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.xButton:
                Intent intent = new Intent(this,homePage.class);
                startActivity(intent);
                finish();
                break;
            case R.id.add_button:
                flashcard card = new flashcard(term.getText().toString(),definition.getText().toString());

                Map<String, Object> flashcardData = new HashMap<>();
                flashcardData.put(cardID, card);

                if (term.getText().toString().trim().isEmpty() || definition.getText().toString().trim().isEmpty()) {
                    Toast.makeText(this, "Please enter a term and definition", Toast.LENGTH_SHORT).show();
                    return;
                }

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("users")
                        .document(userID)
                        .collection("Decks")
                        .document(Title)
                        .set(flashcardData, SetOptions.merge())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("Firestore", "DocumentSnapshot successfully written!");
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("Firestore", "Error writing document", e);
                            }
                        });
                if (switchPublic.isChecked()){
                    db.collection("public decks")
                            .document(Title+"@"+userID)
                            .set(flashcardData, SetOptions.merge())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("Firestore", "DocumentSnapshot successfully written!");
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("Firestore", "Error writing document", e);
                                }
                            });

                }
                startActivity(getIntent());
                break;
            case R.id.deck_arrow:
                Intent decksIntent = new Intent(newFlashCard.this, decks.class);
                startActivity(decksIntent);

                break;
            default:
                throw new IllegalStateException("Unexpected value: " + view.getId());
        }
    }
    @Override
    public void onBackPressed() {
        // do nothing
    }
}