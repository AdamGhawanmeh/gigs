package com.schoolproject.gigs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class TestOptions extends AppCompatActivity implements View.OnClickListener {
  private final FirebaseFirestore db = FirebaseFirestore.getInstance();
  private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
  private final FirebaseUser user = mAuth.getCurrentUser();
  private EditText numQues;
  private Button submit;
  private String Title;
  private int maxQues;
  private int numQuesInt;
  private final List<flashcard> flashcardArrayList = new ArrayList<>();
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_test_options);

    numQues=findViewById(R.id.NumQuestions);
    submit=findViewById(R.id.submit_btn);

    Intent intent = getIntent();
    Bundle extras = intent.getExtras();
    if (extras != null) {
      Title = extras.getString("title");
      maxQues = extras.getInt("max question count");
    }
    submit.setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()){
      case R.id.submit_btn:
        String numQuesStr = numQues.getText().toString();
        if (numQuesStr.isEmpty() || Integer.parseInt(numQuesStr) <= 4) {
          Toast.makeText(TestOptions.this, "A test must contain 5 questions or more.", Toast.LENGTH_SHORT).show();
        }
        else {
          numQuesInt = Integer.parseInt(numQuesStr);
          if (numQuesInt > maxQues) {
            numQuesInt = maxQues;
            Toast.makeText(TestOptions.this, "The number of questions has been set to the maximum allowed.", Toast.LENGTH_SHORT).show();
          }
          db.collection("users")
                  .document(user.getUid())
                  .collection("Decks")
                  .document(Title)
                  .get()
                  .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                      if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                          Map<String, Object> data = document.getData();

                          List<String> keys = new ArrayList<>(data.keySet());
                          Collections.shuffle(keys);
                          int i = 0;
                          for (String key : keys) {
                            if (i < numQuesInt) {
                              Map<String, Object> value = (Map<String, Object>) data.get(key);
                              String frontText = (String) value.get("frontText");
                              String backText = (String) value.get("backText");

                              flashcard card = new flashcard(frontText, backText);

                              // Add the card to the adapter
                              flashcardArrayList.add(card);
                              i++;
                            }
                            else break;
                          }
                          // Create a new intent here
                          Intent intentTest = new Intent(getApplicationContext(), Test.class);
                          flashcard[] flashcardArray = new flashcard[flashcardArrayList.size()];
                          flashcardArray = flashcardArrayList.toArray(flashcardArray);
                          //turned to array because you can send an array through an intent but you can't send an arraylist
                          intentTest.putExtra("flashcards",flashcardArray);
                          intentTest.putExtra("score",0);
                          startActivity(intentTest);
                        }
                      }
                    }
                  }
                  );

        }
    }
  }
}
