package com.schoolproject.gigs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Random;

public class DeckDetailsPublic extends AppCompatActivity {
  private String Title;
  private RecyclerView recyclerView;
  ArrayList<flashcardWid> flashcardArrayList;
  flashcardAdapter flashcardAdapter;
  private FirebaseFirestore db = FirebaseFirestore.getInstance();
  private FirebaseAuth mAuth = FirebaseAuth.getInstance();
  private FirebaseUser user = mAuth.getCurrentUser();
  private LinearLayout add;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_deck_details_public);
    Intent intent = getIntent();
    Bundle extras = intent.getExtras();
    if (extras != null) {
      Title = extras.getString("Title");
      SharedPreferences sharedPreferences = getSharedPreferences("edit",MODE_PRIVATE);
      SharedPreferences.Editor editor = sharedPreferences.edit();
      editor.putString("title",Title);
      editor.apply();
    }

    recyclerView= findViewById(R.id.flashcards);
    recyclerView.setHasFixedSize(true);
    recyclerView.setHasFixedSize(true);
    LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
//        layoutManager.setOrientation();
    recyclerView.setLayoutManager(layoutManager);

    flashcardArrayList = new ArrayList<flashcardWid>();
    flashcardAdapter = new flashcardAdapter(flashcardArrayList);
    recyclerView.setAdapter(flashcardAdapter);
    EventChangeListener();
    add = findViewById(R.id.Add_Deck);
    add.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        db.collection("public decks")
                .document(Title)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                  @Override
                  public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                      DocumentSnapshot document = task.getResult();
                      Map<String, Object> data = document.getData();
                      db.collection("users")
                              .document(user.getUid())
                              .collection("Decks").document(removeAfterAtSymbol(Title)).set(data).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                  if (task.isSuccessful()) {
                                    Log.d("Firestore", "Successfully updated document");
                                  } else {
                                    Log.d("Firestore", "Error getting document: ", task.getException());
                                  }
                                }
                              });
                    } else {
                      Log.d("Firestore", "Error getting document: ", task.getException());
                    }
                  }
                });
      }
    });

  }

  private void EventChangeListener() {
    db.collection("public decks")
            .document(Title)
            .get()
            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
              @Override
              public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                  DocumentSnapshot document = task.getResult();
                  if (document.exists()) {
                    Map<String, Object> data = document.getData();
                    for (Map.Entry<String, Object> entry : data.entrySet()) {
                      String key = entry.getKey();
                      Map<String, Object> value = (Map<String, Object>) entry.getValue();
                      String frontText = (String) value.get("frontText");
                      String backText = (String) value.get("backText");

                      flashcardWid card = new flashcardWid(frontText, backText);
                      card.setID(key);
                      // Add the card to the adapter
                      flashcardArrayList.add(card);

                    }
                    add.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                        db.collection("public decks")
                                .document(Title)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                  @Override
                                  public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                      DocumentSnapshot document = task.getResult();
                                      Map<String, Object> data = document.getData();
                                      db.collection("users")
                                              .document(user.getUid())
                                              .collection("Decks").document(removeAfterAtSymbol(Title)).set(data,SetOptions.merge()).addOnCompleteListener(new OnCompleteListener() {
                                                @Override
                                                public void onComplete(@NonNull Task task) {
                                                  if (task.isSuccessful()) {
                                                    Log.d("Firestore", "Successfully updated document");
                                                    Toast.makeText(DeckDetailsPublic.this, "deck added successfully", Toast.LENGTH_SHORT).show();
                                                  } else {
                                                    Log.d("Firestore", "Error getting document: ", task.getException());
                                                  }
                                                }
                                              });
                                    } else {
                                      Log.d("Firestore", "Error getting document: ", task.getException());
                                    }
                                  }
                                });
                      }
                    });

                  }
                    flashcardAdapter.notifyDataSetChanged();
                  }
                }
            });
  }

  public static String removeAfterAtSymbol(String inputString) {
    int atIndex = inputString.indexOf('@');
    if (atIndex != -1) {
      return inputString.substring(0, atIndex);
    } else {
      return inputString;
    }
  }
}