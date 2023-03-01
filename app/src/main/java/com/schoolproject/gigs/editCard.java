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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class editCard extends AppCompatActivity {
  private final FirebaseFirestore db = FirebaseFirestore.getInstance();

  private Button edit;
  private FirebaseAuth mAuth = FirebaseAuth.getInstance();
  private String userID = mAuth.getCurrentUser().getUid();

  private EditText term;
  private EditText definition;

  private String cardID,Title;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_edit_card);

    term = findViewById(R.id.Term);
    definition=findViewById(R.id.definition);
    Intent intent = getIntent();
    Bundle extras = intent.getExtras();
    if (extras != null) {
      cardID = extras.getString("cardID");
      term.setText(extras.getString("term"));
      definition.setText(extras.getString("definition"));
      SharedPreferences sharedPreferences = getSharedPreferences("edit",MODE_PRIVATE);
      sharedPreferences.getString("title",Title);
      Title = sharedPreferences.getString("title",Title);
    }

    edit = findViewById(R.id.edit);

    edit.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        flashcard card = new flashcard(term.getText().toString(),definition.getText().toString());

        Map<String, Object> flashcardData = new HashMap<>();
        flashcardData.put(cardID, card);

        if (term.getText().toString().trim().isEmpty() || definition.getText().toString().trim().isEmpty()) {
//          Toast.makeText(editCard.class, "Please enter a term and definition", Toast.LENGTH_SHORT).show();
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

      }
    });

  }
}