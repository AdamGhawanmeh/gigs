package com.schoolproject.gigs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Random;

public class StudyActivity extends AppCompatActivity {
  private ArrayList<flashcardWid> flashcardArrayList= new ArrayList<>();
  private FirebaseFirestore db = FirebaseFirestore.getInstance();
  private FirebaseAuth mAuth = FirebaseAuth.getInstance();
  private FirebaseUser user = mAuth.getCurrentUser();

  private String Title;

  private selectFour SelectFour = new selectFour();
  private Fragment FragmentWriteAns = new fragmentWriteAns();
  private Fragment studiedAllFragment = new StudiedAllFragment();

  private int numZero = 0;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_study);



    Intent intent = getIntent();
    Bundle extras = intent.getExtras();
    if (extras != null) {
      Title = extras.getString("title");
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
                  Map<String, Object> data = document.getData();
                  for (Map.Entry<String, Object> entry : data.entrySet()) {
                    Map<String, Object> cardData = (Map<String, Object>) entry.getValue();
                    Long study = (Long) cardData.get("study");
                    if (study <= 4) {
                      String frontText = (String) cardData.get("frontText");
                      String backText = (String) cardData.get("backText");
                      flashcardWid card = new flashcardWid(frontText, backText);
                      card.setID(entry.getKey());
                      card.setStudy(study);
                      flashcardArrayList.add(card);
                      if (study==0){
                      numZero++;}
                    }
                  }
                  Collections.shuffle(flashcardArrayList);
                  if (flashcardArrayList.isEmpty()){
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, studiedAllFragment).commit();
                  }
                  else if (flashcardArrayList.size() >3 && flashcardArrayList.get(0).getStudy() == 0) {
                    Random random = new Random();
                    ArrayList<String> randomAnswers = new ArrayList<>();
                    while (randomAnswers.size() < 3) {
                      String randomAnswer = flashcardArrayList.get(random.nextInt(flashcardArrayList.size() - 1) + 1).getBackText().toString();
                      if (!randomAnswers.contains(randomAnswer)) {
                        randomAnswers.add(randomAnswer);
                      }
                    }
                    randomAnswers.add(flashcardArrayList.get(0).getBackText());
                    Collections.shuffle(randomAnswers);
                    SharedPreferences sharedPreferences = getSharedPreferences("selectFour", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("term", flashcardArrayList.get(0).getFrontText());
                    editor.putString("right answer", flashcardArrayList.get(0).getBackText());
                    editor.putString("cardID", flashcardArrayList.get(0).getID());
                    for (int i = 0; i <4; i++) {
                      editor.putString("answer" + (i+1), randomAnswers.get(i));
                    }
                    editor.apply();
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, SelectFour).commit();
                  }
                  else if (flashcardArrayList.get(0).getStudy()>0||flashcardArrayList.size()<4){
                    SharedPreferences sharedPreferences = getSharedPreferences("Write Answer", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("right answer",flashcardArrayList.get(0).getBackText());
                    editor.putString("card ID",flashcardArrayList.get(0).getID());
                    editor.putString("term",flashcardArrayList.get(0).getFrontText());
                    editor.apply();
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, FragmentWriteAns).commit();
                  }
                } else {
                  Log.d("Firestore", "Error getting document: ", task.getException());
                }
              }
            });
  }
  }